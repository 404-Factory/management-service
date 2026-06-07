package com.factory.management.infrastructure.repository.support;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.factory.management.infrastructure.entity.QDefect.defect;

@Repository
@RequiredArgsConstructor
public class DefectRepositorySupportImpl implements DefectRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public long getDefectCount(String equipmentName, LocalDate startDate, LocalDate endDate) {

        Long result = queryFactory
            .select(defect.count())
            .from(defect)
            .where(
                equipmentNameEq(equipmentName),
                occurredTimeGoe(startDate),
                occurredTimeLt(endDate)
            )
            .fetchOne();

        return result != null ? result: 0L;
    }

    private BooleanExpression equipmentNameEq(String equipmentName) {
        return equipmentName != null ? defect.causeEquipmentName.eq(equipmentName) : null;
    }

    private BooleanExpression occurredTimeGoe(LocalDate startDate) {
        if (startDate == null) {
            return null;
        }

        Instant start = startDate
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant();

        return defect.occurredTime.goe(start);
    }

    private BooleanExpression occurredTimeLt(LocalDate endDate) {
        if (endDate == null) {
            return null;
        }

        Instant end = endDate
            .plusDays(1)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant();

        return defect.occurredTime.lt(end);
    }
}
