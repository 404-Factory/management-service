package com.factory.management.controller;

import com.factory.common.contract.autoconfigure.ContractAutoConfiguration;
import com.factory.management.service.DefectService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = DefectController.class)
@ImportAutoConfiguration({
    ContractAutoConfiguration.class
})
class DefectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefectService defectService;

    @Test
    @DisplayName("불량 개수 조회 API가 올바른 파라미터와 함께 결과를 반환하는지 테스트한다")
    void getCountApiTest() throws Exception {
        // given
        String equipmentName = "EQ-01";
        LocalDateTime since = LocalDateTime.of(2026, 6, 8, 0, 0);
        when(defectService.getCount(eq(equipmentName), eq(since))).thenReturn(42L);

        // when & then
        mockMvc.perform(get("/api/management/defects/count")
                .param("equipmentName", equipmentName)
                .param("since", "2026-06-08T00:00")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("success"))
            .andExpect(jsonPath("$.data").value(42L));;
    }
}
