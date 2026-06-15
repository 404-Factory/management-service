package com.factory.management.controller;

import com.factory.common.contract.autoconfigure.ContractAutoConfiguration;
import com.factory.management.dto.response.EquipmentRecipeDetailResponse;
import com.factory.management.dto.response.EquipmentRecipeResponse;
import com.factory.management.dto.response.EquipmentResponse;
import com.factory.management.exception.ManagementErrorCode;
import com.factory.management.exception.ManagementException;
import com.factory.management.service.EquipmentRecipeService;
import com.factory.management.service.EquipmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = EquipmentController.class)
@ImportAutoConfiguration(ContractAutoConfiguration.class)
class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EquipmentService equipmentService;

    @MockitoBean
    private EquipmentRecipeService equipmentRecipeService;

    @Test
    @DisplayName("설비 목록 조회 API가 결과를 반환한다")
    void getEquipments_returnsOk() throws Exception {
        EquipmentResponse response = EquipmentResponse.builder()
            .id(1L)
            .name("EQ-01")
            .processId(1L)
            .processName("DEPOSITION")
            .status("RUNNING")
            .build();
        when(equipmentService.getAllEquipments(isNull(), isNull()))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/management/equipments")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(1L))
            .andExpect(jsonPath("$.data[0].name").value("EQ-01"))
            .andExpect(jsonPath("$.data[0].status").value("RUNNING"));
    }

    @Test
    @DisplayName("설비 단건 조회 API가 결과를 반환한다")
    void getEquipment_returnsOk() throws Exception {
        EquipmentResponse response = EquipmentResponse.builder()
            .id(1L)
            .name("EQ-01")
            .processId(1L)
            .processName("DEPOSITION")
            .status("RUNNING")
            .build();
        when(equipmentService.getEquipment(1L)).thenReturn(response);

        mockMvc.perform(get("/api/management/equipments/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.name").value("EQ-01"));
    }

    @Test
    @DisplayName("존재하지 않는 설비 조회 시 404를 반환한다")
    void getEquipment_notFound_returns404() throws Exception {
        when(equipmentService.getEquipment(999L))
            .thenThrow(new ManagementException(ManagementErrorCode.EQUIPMENT_NOT_FOUND));

        mockMvc.perform(get("/api/management/equipments/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("설비 레시피 조회 API가 결과를 반환한다")
    void getEquipmentRecipe_returnsOk() throws Exception {
        EquipmentRecipeResponse response = EquipmentRecipeResponse.builder()
            .id(1L)
            .equipmentId(1L)
            .equipmentName("EQ-01")
            .masterRecipeId(1L)
            .version(1.0)
            .details(List.of())
            .build();
        when(equipmentRecipeService.getEquipmentRecipeByEquipmentId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/management/equipments/1/recipe")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.equipmentId").value(1L))
            .andExpect(jsonPath("$.data.equipmentName").value("EQ-01"))
            .andExpect(jsonPath("$.data.version").value(1.0));
    }

    @Test
    @DisplayName("존재하지 않는 설비 레시피 조회 시 404를 반환한다")
    void getEquipmentRecipe_notFound_returns404() throws Exception {
        when(equipmentRecipeService.getEquipmentRecipeByEquipmentId(999L))
            .thenThrow(new ManagementException(ManagementErrorCode.EQUIPMENT_RECIPE_NOT_FOUND));

        mockMvc.perform(get("/api/management/equipments/999/recipe")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("유효하지 않은 status 값으로 조회 시 400을 반환한다")
    void getEquipments_invalidStatus_returns400() throws Exception {
        mockMvc.perform(get("/api/management/equipments")
                .param("status", "INVALID_STATUS")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
