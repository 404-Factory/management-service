package com.factory.management_service.domain.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EQUIPMENT_RECIPE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EquipmentRecipeEntity {

    @Id
    @Column(name = "equipment_rec_id", length = 50)
    private String equipmentRecId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private EquipmentEntity equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_recipe_id")
    private MasterRecipeEntity masterRecipe;

    @Column(name = "version")
    private Double version;

    @OneToMany(mappedBy = "equipmentRecipe", fetch = FetchType.LAZY)
    private List<EquipmentRecipeDetail> details;
}