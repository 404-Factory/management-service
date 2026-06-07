package com.factory.management.infrastructure.repository.support;

import com.factory.management.dto.response.DefectCountResponse;
import com.querydsl.core.types.Projections;
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
    public DefectCountResponse getDefectCount(String equipmentName, LocalDate startDate, LocalDate endDate) {

        return queryFactory
            .select(Projections.constructor(
                DefectCountResponse.class,
                defect.count()
            ))
            .from(defect)
            .where(
                equipmentNameEq(equipmentName),
                occurredTimeGoe(startDate),
                occurredTimeLt(endDate)
            )
            .fetchOne();
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
