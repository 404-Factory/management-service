package com.factory.management_service.service;

import com.factory.management_service.common.config.AwsS3Properties;
import com.factory.management_service.domain.dto.MonthlySummaryResponseDTO;
import com.factory.management_service.domain.dto.SensorSummaryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.LocalInputFile;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3SummaryService {

    static {
        System.setProperty("hadoop.home.dir", "/");
    }

    private final S3Client s3Client;
    private final AwsS3Properties awsS3Properties;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MonthlySummaryResponseDTO getMonthlySummary(String equipmentName) {
        Map<String, List<Double>> avgByType = new LinkedHashMap<>();
        Map<String, String> unitByType = new LinkedHashMap<>();
        long totalOutOfRecipe = 0;

        LocalDate today = LocalDate.now();
        for (int i = 1; i <= 30; i++) {
            String dateStr = today.minusDays(i).format(DATE_FMT);
            String prefix = String.format("summary-data/date=%s/equipmentId=%s/", dateStr, equipmentName);

            for (ParquetRow row : fetchRowsByPrefix(prefix)) {
                avgByType.computeIfAbsent(row.sensorType(), k -> new ArrayList<>()).add(row.avgValue());
                unitByType.put(row.sensorType(), row.unit());
                totalOutOfRecipe += row.outOfRecipeCount();
            }
        }

        List<SensorSummaryDTO> sensors = avgByType.entrySet().stream()
                .map(e -> SensorSummaryDTO.builder()
                        .sensorType(e.getKey())
                        .unit(unitByType.get(e.getKey()))
                        .avgValue(roundTwo(e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0)))
                        .build())
                .collect(Collectors.toList());

        return MonthlySummaryResponseDTO.builder()
                .totalOutOfRecipe(totalOutOfRecipe)
                .sensors(sensors)
                .build();
    }

    public Map<String, String> debugS3(String equipmentName) {
        Map<String, String> result = new LinkedHashMap<>();
        String bucket = awsS3Properties.getS3().getBucket();
        String prefix = String.format("summary-data/date=2026-05-28/equipmentId=%s/", equipmentName);

        result.put("bucket", bucket);
        result.put("region", awsS3Properties.getRegion());
        result.put("prefix", prefix);

        try {
            List<String> keys = listParquetKeys(prefix);
            if (keys.isEmpty()) {
                result.put("foundFiles", "없음");
                return result;
            }
            result.put("foundFiles", keys.get(0));

            // 파일 직접 읽어서 스키마와 첫 번째 행 확인
            byte[] bytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder().bucket(bucket).key(keys.get(0)).build()
            ).asByteArray();
            result.put("fileSize", bytes.length + " bytes");

            Path tempFile = Files.createTempFile("parquet-debug-", ".parquet");
            try {
                Files.write(tempFile, bytes);
                try (ParquetFileReader fileReader = ParquetFileReader.open(new LocalInputFile(tempFile))) {
                    MessageType schema = fileReader.getFooter().getFileMetaData().getSchema();
                    result.put("schema", schema.toString());

                    PageReadStore pages = fileReader.readNextRowGroup();
                    if (pages != null) {
                        result.put("rowCount", String.valueOf(pages.getRowCount()));
                        MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
                        RecordReader<Group> rr = columnIO.getRecordReader(pages, new GroupRecordConverter(schema));
                        Group g = rr.read();
                        if (g != null) result.put("firstRow", g.toString());
                    }
                }
            } finally {
                Files.deleteIfExists(tempFile);
            }
        } catch (Throwable e) {
            result.put("error", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return result;
    }

    private List<ParquetRow> fetchRowsByPrefix(String prefix) {
        try {
            List<String> keys = listParquetKeys(prefix);
            if (keys.isEmpty()) return Collections.emptyList();

            return fetchRows(keys.get(0));
        } catch (Throwable e) {
            log.error("### prefix 조회 실패: {}", prefix, e);
            return Collections.emptyList();
        }
    }

    private List<String> listParquetKeys(String prefix) {
        return s3Client.listObjectsV2(ListObjectsV2Request.builder()
                        .bucket(awsS3Properties.getS3().getBucket())
                        .prefix(prefix)
                        .build())
                .contents().stream()
                .map(S3Object::key)
                .filter(k -> k.endsWith(".parquet"))
                .collect(Collectors.toList());
    }

    private List<ParquetRow> fetchRows(String s3Key) {
        Path tempFile = null;
        try {
            byte[] bytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(awsS3Properties.getS3().getBucket())
                            .key(s3Key)
                            .build()
            ).asByteArray();

            tempFile = Files.createTempFile("parquet-", ".parquet");
            Files.write(tempFile, bytes);
            return readParquet(tempFile);

        } catch (Throwable e) {
            log.error("### S3 Parquet 읽기 실패: {}", s3Key, e);
            return Collections.emptyList();
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
            }
        }
    }

    private List<ParquetRow> readParquet(Path path) throws IOException {
        List<ParquetRow> rows = new ArrayList<>();
        try (ParquetFileReader fileReader = ParquetFileReader.open(new LocalInputFile(path))) {
            MessageType schema = fileReader.getFooter().getFileMetaData().getSchema();
            MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
            PageReadStore pages;
            while ((pages = fileReader.readNextRowGroup()) != null) {
                RecordReader<Group> recordReader = columnIO.getRecordReader(pages, new GroupRecordConverter(schema));
                long count = pages.getRowCount();
                for (long i = 0; i < count; i++) {
                    rows.add(toRow(recordReader.read()));
                }
            }
        }
        return rows;
    }

    private ParquetRow toRow(Group g) {
        return new ParquetRow(
                g.getString("sensorType", 0),
                g.getString("unit", 0),
                g.getDouble("avg_value", 0),
                g.getLong("out_of_recipe_count", 0)
        );
    }

    private double roundTwo(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private record ParquetRow(String sensorType, String unit, double avgValue, long outOfRecipeCount) {}
}
