package com.factory.management_service.common.config;

import com.factory.management_service.dao.*;
import com.factory.management_service.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final ProcessRepository processRepository;
        private final EquipmentRepository equipmentRepository;

        private final MasterRecipeRepository masterRecipeRepository;
        private final MasterRecipeDetailRepository masterRecipeDetailRepository;

        private final EquipmentRecipeRepository equipmentRecipeRepository;
        private final EquipmentRecipeDetailRepository equipmentRecipeDetailRepository;

        private final LotRepository lotRepository;
        private final AnomalyRepository anomalyRepository;
        private final DefectRepository defectRepository;

        @Override
        public void run(String... args) {

                if (productRepository.count() > 0) {
                        return;
                }

                // =====================================================
                // PRODUCT
                // =====================================================

                ProductEntity product = productRepository.save(
                                ProductEntity.builder()
                                                .productName("DRAM-16GB")
                                                .build());

                // =====================================================
                // PROCESS
                // =====================================================

                ProcessEntity coat = processRepository.save(
                                ProcessEntity.builder()
                                                .processName("DEPOSITION")
                                                .build());

                ProcessEntity photo = processRepository.save(
                                ProcessEntity.builder()
                                                .processName("PHOTO")
                                                .build());

                ProcessEntity etch = processRepository.save(
                                ProcessEntity.builder()
                                                .processName("ETCH")
                                                .build());

                ProcessEntity clear = processRepository.save(
                                ProcessEntity.builder()
                                                .processName("CLEANING")
                                                .build());

                // =====================================================
                // EQUIPMENT
                // =====================================================

                EquipmentEntity coater1 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-DEPOSITION-001")
                                                .process(coat)
                                                .build());

                EquipmentEntity coater2 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-DEPOSITION-002")
                                                .process(coat)
                                                .build());

                EquipmentEntity photo1 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-PHOTO-001")
                                                .process(photo)
                                                .build());

                EquipmentEntity photo2 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-PHOTO-002")
                                                .process(photo)
                                                .build());

                EquipmentEntity photo3 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-PHOTO-003")
                                                .process(photo)
                                                .build());

                EquipmentEntity photo4 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-PHOTO-004")
                                                .process(photo)
                                                .build());

                EquipmentEntity etch1 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-ETCH-001")
                                                .process(etch)
                                                .build());

                EquipmentEntity etch2 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-ETCH-002")
                                                .process(etch)
                                                .build());

                EquipmentEntity cleaner1 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-CLEANING-001")
                                                .process(clear)
                                                .build());

                EquipmentEntity cleaner2 = equipmentRepository.save(
                                EquipmentEntity.builder()
                                                .equipmentName("EQP-CLEANING-002")
                                                .process(clear)
                                                .build());

                // =====================================================
                // MASTER RECIPE
                // =====================================================

                MasterRecipeEntity coatRecipe = masterRecipeRepository.save(
                                MasterRecipeEntity.builder()
                                                .product(product)
                                                .process(coat)
                                                .build());

                MasterRecipeEntity photoRecipe = masterRecipeRepository.save(
                                MasterRecipeEntity.builder()
                                                .product(product)
                                                .process(photo)
                                                .build());

                MasterRecipeEntity etchRecipe = masterRecipeRepository.save(
                                MasterRecipeEntity.builder()
                                                .product(product)
                                                .process(etch)
                                                .build());

                MasterRecipeEntity clearRecipe = masterRecipeRepository.save(
                                MasterRecipeEntity.builder()
                                                .product(product)
                                                .process(clear)
                                                .build());

                // =====================================================
                // MASTER RECIPE DETAIL
                // =====================================================

                masterRecipeDetailRepository.saveAll(List.of(

                                // 도포
                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                coatRecipe.getMasterRecipeId(),
                                                                "Spin Speed (rpm)"))
                                                .masterRecipe(coatRecipe)
                                                .min(1500.0)
                                                .max(2500.0)
                                                .build(),

                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                coatRecipe.getMasterRecipeId(),
                                                                "Soft Bake Temperature (°C)"))
                                                .masterRecipe(coatRecipe)
                                                .min(85.0)
                                                .max(110.0)
                                                .build(),

                                // 포토
                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                photoRecipe.getMasterRecipeId(),
                                                                "Exposure Dose (mJ/cm²)"))
                                                .masterRecipe(photoRecipe)
                                                .min(120.0)
                                                .max(180.0)
                                                .build(),

                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                photoRecipe.getMasterRecipeId(),
                                                                "Post Exposure Bake (PEB) Temperature"))
                                                .masterRecipe(photoRecipe)
                                                .min(90.0)
                                                .max(120.0)
                                                .build(),

                                // 식각
                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                etchRecipe.getMasterRecipeId(),
                                                                "Chamber Pressure (mTorr)"))
                                                .masterRecipe(etchRecipe)
                                                .min(10.0)
                                                .max(50.0)
                                                .build(),

                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                etchRecipe.getMasterRecipeId(),
                                                                "Chuck Temperature (°C)"))
                                                .masterRecipe(etchRecipe)
                                                .min(40.0)
                                                .max(80.0)
                                                .build(),

                                // 클리어
                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                clearRecipe.getMasterRecipeId(),
                                                                "Chemical Temperature (°C)"))
                                                .masterRecipe(clearRecipe)
                                                .min(20.0)
                                                .max(40.0)
                                                .build(),

                                MasterRecipeDetailEntity.builder()
                                                .id(new MasterRecipeDetailId(
                                                                clearRecipe.getMasterRecipeId(),
                                                                "Chemical 농도 (%)"))
                                                .masterRecipe(clearRecipe)
                                                .min(5.0)
                                                .max(15.0)
                                                .build()));

                // =====================================================
                // EQUIPMENT RECIPE
                // =====================================================

                EquipmentRecipeEntity er1 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(coater1)
                                                .masterRecipe(coatRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er2 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(coater2)
                                                .masterRecipe(coatRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er3 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(photo1)
                                                .masterRecipe(photoRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er4 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(photo2)
                                                .masterRecipe(photoRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er5 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(photo3)
                                                .masterRecipe(photoRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er6 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(photo4)
                                                .masterRecipe(photoRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er7 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(etch1)
                                                .masterRecipe(etchRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er8 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(etch2)
                                                .masterRecipe(etchRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er9 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(cleaner1)
                                                .masterRecipe(clearRecipe)
                                                .version(1.0)
                                                .build());

                EquipmentRecipeEntity er10 = equipmentRecipeRepository.save(
                                EquipmentRecipeEntity.builder()
                                                .equipment(cleaner2)
                                                .masterRecipe(clearRecipe)
                                                .version(1.0)
                                                .build());

                // =====================================================
                // EQUIPMENT RECIPE DETAIL
                // =====================================================

                saveRecipeDetail(er1, "Spin Speed (rpm)", 1500.0, 2500.0);
                saveRecipeDetail(er1, "Soft Bake Temperature (°C)", 85.0, 110.0);

                saveRecipeDetail(er2, "Spin Speed (rpm)", 1500.0, 2500.0);
                saveRecipeDetail(er2, "Soft Bake Temperature (°C)", 85.0, 110.0);

                saveRecipeDetail(er3, "Exposure Dose (mJ/cm²)", 120.0, 180.0);
                saveRecipeDetail(er3, "Post Exposure Bake (PEB) Temperature", 90.0, 120.0);

                saveRecipeDetail(er4, "Exposure Dose (mJ/cm²)", 120.0, 180.0);
                saveRecipeDetail(er4, "Post Exposure Bake (PEB) Temperature", 90.0, 120.0);

                saveRecipeDetail(er5, "Exposure Dose (mJ/cm²)", 120.0, 180.0);
                saveRecipeDetail(er5, "Post Exposure Bake (PEB) Temperature", 90.0, 120.0);

                saveRecipeDetail(er6, "Exposure Dose (mJ/cm²)", 120.0, 180.0);
                saveRecipeDetail(er6, "Post Exposure Bake (PEB) Temperature", 90.0, 120.0);

                saveRecipeDetail(er7, "Chamber Pressure (mTorr)", 10.0, 50.0);
                saveRecipeDetail(er7, "Chuck Temperature (°C)", 40.0, 80.0);

                saveRecipeDetail(er8, "Chamber Pressure (mTorr)", 10.0, 50.0);
                saveRecipeDetail(er8, "Chuck Temperature (°C)", 40.0, 80.0);

                saveRecipeDetail(er9, "Chemical Temperature (°C)", 20.0, 40.0);
                saveRecipeDetail(er9, "Chemical 농도 (%)", 5.0, 15.0);

                saveRecipeDetail(er10, "Chemical Temperature (°C)", 20.0, 40.0);
                saveRecipeDetail(er10, "Chemical 농도 (%)", 5.0, 15.0);

                // =====================================================
                // LOT
                // =====================================================

                LotEntity lot1 = lotRepository.save(
                                LotEntity.builder()
                                                .product(product)
                                                .process(coat)
                                                .equipment(coater1)
                                                .masterRecipe(coatRecipe)
                                                .lotGrade("G")
                                                .productQty(15)
                                                .productionType("양산")
                                                .build());

                LotEntity lot2 = lotRepository.save(
                                LotEntity.builder()
                                                .product(product)
                                                .process(photo)
                                                .equipment(photo2)
                                                .masterRecipe(photoRecipe)
                                                .lotGrade("G")
                                                .productQty(15)
                                                .productionType("양산")
                                                .build());

                LotEntity lot3 = lotRepository.save(
                                LotEntity.builder()
                                                .product(product)
                                                .process(etch)
                                                .equipment(etch1)
                                                .masterRecipe(etchRecipe)
                                                .lotGrade("G")
                                                .productQty(20)
                                                .productionType("시제품")
                                                .build());

                LotEntity lot4 = lotRepository.save(
                                LotEntity.builder()
                                                .product(product)
                                                .process(clear)
                                                .equipment(cleaner1)
                                                .masterRecipe(clearRecipe)
                                                .lotGrade("G")
                                                .productQty(30)
                                                .productionType("양산")
                                                .build());

                // =====================================================
                // DEFECT
                // =====================================================

                defectRepository.saveAll(List.of(

                                createDefect(lot1, coat, coater1, "Scratch", "D001"),
                                createDefect(lot1, coat, coater1, "Bubble", "D002"),

                                createDefect(lot2, photo, photo2, "Overlay", "D003"),
                                createDefect(lot2, photo, photo2, "Overlay", "D003"),
                                createDefect(lot2, photo, photo2, "FocusFail", "D004"),

                                createDefect(lot3, etch, etch1, "EtchFail", "D005"),
                                createDefect(lot3, etch, etch1, "EtchFail", "D005"),
                                createDefect(lot3, etch, etch1, "Particle", "D006"),

                                createDefect(lot4, clear, cleaner1, "Residue", "D007")));

                System.out.println("===== LOCAL TEST DATA SEEDED =====");
        }

        private void saveRecipeDetail(
                        EquipmentRecipeEntity recipe,
                        String param,
                        Double min,
                        Double max) {

                equipmentRecipeDetailRepository.save(
                                EquipmentRecipeDetail.builder()
                                                .id(new EquipmentRecipeDetailId(
                                                                recipe.getEquipmentRecId(),
                                                                param))
                                                .equipmentRecipe(recipe)
                                                .min(min)
                                                .max(max)
                                                .build());
        }

        private DefectEntity createDefect(
                        LotEntity lot,
                        ProcessEntity process,
                        EquipmentEntity equipment,
                        String type,
                        String code) {

                return DefectEntity.builder()
                                .lot(lot)
                                .defectType(type)
                                .defectCode(code)
                                .detectedTime(LocalDateTime.now())
                                .occurredTime(LocalDateTime.now())
                                .causeProcessId(process.getProcessId())
                                .causeProcessName(process.getProcessName())
                                .causeEquipmentId(equipment.getEquipmentId())
                                .causeEquipmentName(equipment.getEquipmentName())
                                .build();
        }
}