package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "LOT_INFO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private ProcessEntity process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private EquipmentEntity equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_recipe_id")
    private MasterRecipeEntity masterRecipe;

    @Column(length = 50)
    private String lotGrade;

    @Column(precision = 22, scale = 5)
    private BigDecimal productQty;

    @Column(length = 50)
    private String productionType;
}