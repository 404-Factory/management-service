package com.factory.management_service.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MasterRecipeDetailId implements Serializable {

    private String masterRecipeId;

    private String param;
}