package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EQUIPMENT_RECIPE_DETAIL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EquipmentRecipeDetail {

    @EmbeddedId
    private EquipmentRecipeDetailId id;

    @MapsId("equipmentRecId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_rec_id")
    private EquipmentRecipeEntity equipmentRecipe;

    @Column(name = "min")
    private Double min;

    @Column(name = "max")
    private Double max;
}
