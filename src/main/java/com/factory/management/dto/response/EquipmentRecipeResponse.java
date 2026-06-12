package com.factory.management.dto.response;

import com.factory.management.infrastructure.entity.Equipment;
import com.factory.management.infrastructure.entity.EquipmentRecipeDetail;
import com.factory.management.infrastructure.entity.MasterRecipe;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EquipmentRecipeResponse {

    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private Long masterRecipeId;
    private Double version;
    private List<EquipmentRecipeDetailResponse> details;

    @Builder
    public EquipmentRecipeResponse(Long id, Long equipmentId, String equipmentName,
        Long masterRecipeId, Double version, List<EquipmentRecipeDetailResponse> details) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.masterRecipeId = masterRecipeId;
        this.version = version;
        this.details = details;
    }
}