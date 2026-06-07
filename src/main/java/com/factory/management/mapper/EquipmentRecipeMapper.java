package com.factory.management.mapper;

import com.factory.management.dto.response.EquipmentRecipeResponse;
import com.factory.management.infrastructure.entity.EquipmentRecipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    uses = EquipmentRecipeDetailMapper.class
)
public interface EquipmentRecipeMapper {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "equipment.name", target = "equipmentName")
    @Mapping(source = "masterRecipe.id", target = "masterRecipeId")
    EquipmentRecipeResponse toDto(EquipmentRecipe equipmentRecipe);
}
