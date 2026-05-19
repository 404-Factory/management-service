package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MASTER_RECIPE_DETAIL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MasterRecipeDetailEntity {

    @EmbeddedId
    private MasterRecipeDetailId id;

    @MapsId("masterRecipeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_recipe_id")
    private MasterRecipeEntity masterRecipe;

    @Column(name = "min")
    private Double min;

    @Column(name = "max")
    private Double max;
}
