package com.factory.management_service.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EquipmentRecipeDetailId implements Serializable {

    private String equipmentRecId;

    private String param;
}
