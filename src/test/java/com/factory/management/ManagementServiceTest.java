package com.factory.management;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.factory.management.dao.EquipmentRecipeRepository;
import com.factory.management.dao.EquipmentRepository;
import com.factory.management.domain.type.EquipmentStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ManagementServiceTest {

        @Mock
        private EquipmentRepository equipmentRepository;

        @Mock
        private AnomalyRepository anomalyRepository;

        @Mock
        private EquipmentRecipeRepository equipmentRecipeRepository;

        @InjectMocks
        private ManagementService managementService;

        private ProcessEntity processEntity;
        private EquipmentEntity equipmentEntity;
        private EquipmentRecipeEntity recipeEntity;
        private AnomalyEntity anomalyEntity;
        private EquipmentRecipeDetail recipeDetailEntity;
        private MasterRecipeEntity masterRecipeEntity;

        @BeforeEach
        void setUp() {

                processEntity = ProcessEntity.builder()
                                .processId(1L)
                                .build();

                equipmentEntity = EquipmentEntity.builder()
                                .equipmentId(1L)
                                .equipmentName("설비A")
                                .process(processEntity)
                                .status(EquipmentStatus.NORMAL)
                                .build();

                masterRecipeEntity = MasterRecipeEntity.builder()
                                .masterRecipeId(1L)
                                .build();

                recipeEntity = EquipmentRecipeEntity.builder()
                                .equipmentRecId(1L)
                                .equipment(equipmentEntity)
                                .masterRecipe(masterRecipeEntity)
                                .version(1.0)
                                .details(List.of())
                                .build();

                anomalyEntity = AnomalyEntity.builder()
                                .equipment(equipmentEntity)
                                .anomalyType(null)
                                .equipmentRecipe(recipeEntity)
                                .occurredTime(LocalDateTime.now())
                                .build();
        }

        @Test
        @DisplayName("대시보드 설비 목록 조회 성공")
        void getDashboardList_success() {

                // given
                when(equipmentRepository.findAll())
                                .thenReturn(List.of(equipmentEntity));

                // when
                List<EquipmentResponseDTO> result = managementService.getDashboardList();

                // then
                assertThat(result).hasSize(1);

                verify(equipmentRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("공정별 설비 목록 조회 성공")
        void getEquipmentList_success() {

                // given
                Long processId = 1L;

                when(equipmentRepository.findByProcess_ProcessId(processId))
                                .thenReturn(Optional.of(List.of(equipmentEntity)));

                // when
                List<EquipmentResponseDTO> result = managementService.getEquipmentList(processId);

                // then
                assertThat(result).hasSize(1);

                verify(equipmentRepository).findByProcess_ProcessId(processId);
        }

        @Test
        @DisplayName("공정별 설비 목록 조회 실패")
        void getEquipmentList_fail() {

                // given
                Long processId = 1L;

                when(equipmentRepository.findByProcess_ProcessId(processId))
                                .thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> managementService.getEquipmentList(processId))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("설비 목록을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("설비 상세 조회 성공")
        void getEquipmentDetail_success() {

                // given
                Long equipmentId = 1L;

                when(equipmentRepository.findById(equipmentId))
                                .thenReturn(Optional.of(equipmentEntity));

                // when
                EquipmentResponseDTO result = managementService.getEquipmentDetail(equipmentId);

                // then
                assertThat(result).isNotNull();

                verify(equipmentRepository).findById(equipmentId);
        }

        @Test
        @DisplayName("설비 상세 조회 실패")
        void getEquipmentDetail_fail() {

                // given
                Long equipmentId = 1L;

                when(equipmentRepository.findById(equipmentId))
                                .thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> managementService.getEquipmentDetail(equipmentId))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("설비를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("최근 이상 조회 성공")
        void getRecentAnomaly_success() {

                // given
                Long equipmentId = 1L;

                when(anomalyRepository.findRecentAnomaliesByEquipmentId(
                                any(LocalDateTime.class),
                                eq(equipmentId)))
                                .thenReturn(List.of(anomalyEntity));

                // when
                List<AnomalyResponseDTO> result = managementService.getRecentAnomaly(equipmentId);

                // then
                assertThat(result).hasSize(1);

                verify(anomalyRepository)
                                .findRecentAnomaliesByEquipmentId(
                                                any(LocalDateTime.class),
                                                eq(equipmentId));
        }

        @Test
        @DisplayName("현재 레시피 조회 성공")
        void getCurrentRecipe_success() {

                // given
                Long equipmentId = 1L;

                when(equipmentRecipeRepository
                                .findTopByEquipment_EquipmentIdOrderByVersionDesc(equipmentId))
                                .thenReturn(Optional.of(recipeEntity));

                // when
                EquipmentRecipeResponseDTO result = managementService.getCurrentRecipe(equipmentId);

                // then
                assertThat(result).isNotNull();

                verify(equipmentRecipeRepository)
                                .findTopByEquipment_EquipmentIdOrderByVersionDesc(equipmentId);
        }

        @Test
        @DisplayName("현재 레시피 조회 실패")
        void getCurrentRecipe_fail() {

                // given
                Long equipmentId = 1L;

                when(equipmentRecipeRepository
                                .findTopByEquipment_EquipmentIdOrderByVersionDesc(equipmentId))
                                .thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> managementService.getCurrentRecipe(equipmentId))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("레시피를 찾을 수 없습니다.");
        }
}