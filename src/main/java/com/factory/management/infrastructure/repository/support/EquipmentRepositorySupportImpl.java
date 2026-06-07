package com.factory.management.infrastructure.repository.support;

import com.factory.management.dto.response.EquipmentResponse;
import com.factory.management.infrastructure.enums.EquipmentStatus;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.factory.management.infrastructure.entity.QEquipment.equipment;

@Repository
@RequiredArgsConstructor
public class EquipmentRepositorySupportImpl implements EquipmentRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EquipmentResponse> fetchEquipmentsWithCondition(Long processId, String status) {
        return queryFactory
            .select(getEquipmentResponseProjection())
            .from(equipment)
            .where(
                eqStatus(status),
                eqProcessId(processId)
            )
            .fetch();
    }

    public EquipmentResponse fetchEquipment(Long id) {
        return queryFactory
            .select(getEquipmentResponseProjection())
            .from(equipment)
            .where(
                equipment.id.eq(id)
            )
            .fetchOne();
    }

    private BooleanExpression eqStatus(String status) {
        return status != null ? equipment.status.eq(EquipmentStatus.fromCode(status)) : null;
    }

    private BooleanExpression eqProcessId(Long processId) {
        return processId != null ? equipment.process.id.eq(processId) : null;
    }

    private ConstructorExpression<EquipmentResponse> getEquipmentResponseProjection() {
        return Projections.constructor(
            EquipmentResponse.class,
            equipment.id,
            equipment.name,
            equipment.process.id,
            equipment.process.name,
            equipment.status.stringValue()
        );
    }
}
