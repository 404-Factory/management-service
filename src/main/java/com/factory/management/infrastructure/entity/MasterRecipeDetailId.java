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
public class MasterRecipeDetailId implements Serializable {

    @Column(name = "master_recipe_id")
    private Long masterRecipeId;

    @Column(name = "param")
    private String param;
}