package com.factory.management.controller;

import com.factory.management.service.DefectService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DefectController.class)
class DefectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefectService defectService;

    @Test
    @DisplayName("불량 개수 조회 API가 올바른 파라미터와 함께 결과를 반환하는지 테스트한다")
    void getCountApiTest() throws Exception {
        // given
        String equipmentName = "EQ-01";
        LocalDate startDate = LocalDate.of(2026, 6, 8);
        LocalDate endDate = LocalDate.of(2026, 6, 8);
        when(defectService.getCount(eq(equipmentName), eq(startDate), eq(endDate))).thenReturn(42L);

        // when & then
        mockMvc.perform(get("/api/managemnt/defects/count")
                .param("equipmentName", equipmentName)
                .param("startDate", "2026-06-08")
                .param("endDate", "2026-06-08")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("42"));
    }
}
