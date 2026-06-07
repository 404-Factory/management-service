package com.factory.management.mapper;

import com.factory.management.dto.response.EquipmentRecipeDetailResponse;
import com.factory.management.infrastructure.entity.EquipmentRecipeDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface EquipmentRecipeDetailMapper {

    @Mapping(source = "id.param", target = "param")
    EquipmentRecipeDetailResponse toDto(EquipmentRecipeDetail equipmentRecipeDetail);
}
