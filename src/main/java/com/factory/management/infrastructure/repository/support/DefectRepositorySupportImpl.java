package com.factory.management.infrastructure.repository.support;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.factory.management.infrastructure.entity.QDefect.defect;

@Repository
@RequiredArgsConstructor
public class DefectRepositorySupportImpl implements DefectRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public long getDefectCount(String equipmentName, LocalDateTime since) {

        Long result = queryFactory
            .select(defect.count())
            .from(defect)
            .where(
                equipmentNameEq(equipmentName),
                occurredTimeGoe(since)
            )
            .fetchOne();

        return result != null ? result: 0L;
    }

    private BooleanExpression equipmentNameEq(String equipmentName) {
        return equipmentName != null ? defect.causeEquipmentName.eq(equipmentName) : null;
    }

    private BooleanExpression occurredTimeGoe(LocalDateTime since) {
        if (since == null) {
            return null;
        }

        Instant start = since.toInstant(ZoneOffset.UTC);

        return defect.occurredTime.goe(start);
    }
}
