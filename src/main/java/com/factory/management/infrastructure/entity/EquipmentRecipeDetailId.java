package com.factory.management.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class EquipmentRecipeDetailId implements Serializable {

    @Column(name = "equipment_recipe_id")
    private Long equipmentRecipeId;

    @Column(name = "param")
    private String param;
}
