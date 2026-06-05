package com.factory.management_service.registry;

import com.factory.management_service.domain.rule.DefectCandidate;
import com.factory.management_service.domain.rule.RuleKey;
import com.factory.management_service.domain.type.RuleName;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RuleRegistry {

        /**
         * key:
         * recipe_parameter + rule_name
         *
         * value:
         * defect distribution
         */
        private final Map<RuleKey, List<DefectCandidate>> rules = new HashMap<>();

        @PostConstruct
        public void init() {

                // =================================================
                // 도포 - Spin Speed
                // =================================================

                rules.put(
                                new RuleKey("Spin Speed", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "PR",
                                                                "PR_THICKNESS",
                                                                80),
                                                new DefectCandidate(
                                                                "EDGE_BEAD",
                                                                "EDGE_BEAD_FAIL",
                                                                20)));

                rules.put(
                                new RuleKey("Spin Speed", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "PR",
                                                                "PR_THICKNESS",
                                                                70),
                                                new DefectCandidate(
                                                                "THICKNESS",
                                                                "THICKNESS_UNEVEN",
                                                                30)));

                rules.put(
                                new RuleKey("Spin Speed", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "THICKNESS",
                                                                "THICKNESS_UNEVEN",
                                                                60),
                                                new DefectCandidate(
                                                                "PR",
                                                                "PR_THICKNESS",
                                                                40)));

                // =================================================
                // 도포 - Soft Bake Temp
                // =================================================

                rules.put(
                                new RuleKey("Soft Bake Temperature", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "PINHOLE",
                                                                "PINHOLE_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("Soft Bake Temperature", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("Soft Bake Temperature", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE_FAIL",
                                                                70),
                                                new DefectCandidate(
                                                                "PINHOLE",
                                                                "PINHOLE_FAIL",
                                                                30)));

                // =================================================
                // 포토 - PEB
                // =================================================

                rules.put(
                                new RuleKey("PEB", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "CD",
                                                                "CD_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("PEB", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "CD",
                                                                "CD_FAIL",
                                                                70),
                                                new DefectCandidate(
                                                                "PATTERN",
                                                                "PATTERN_OPEN",
                                                                30)));

                rules.put(
                                new RuleKey("PEB", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "CD",
                                                                "CD_FAIL",
                                                                100)));

                // =================================================
                // 포토 - Exposure Dose
                // =================================================

                rules.put(
                                new RuleKey("Exposure Dose", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "CD",
                                                                "CD_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("Exposure Dose", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "PATTERN",
                                                                "PATTERN_OPEN",
                                                                50),
                                                new DefectCandidate(
                                                                "PATTERN",
                                                                "BRIDGE",
                                                                50)));

                rules.put(
                                new RuleKey("Exposure Dose", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "CD",
                                                                "CD_FAIL",
                                                                60),
                                                new DefectCandidate(
                                                                "PATTERN",
                                                                "PATTERN_OPEN",
                                                                20),
                                                new DefectCandidate(
                                                                "PATTERN",
                                                                "BRIDGE",
                                                                20)));
                // =================================================
                // 식각 - Chamber Pressure
                // =================================================

                rules.put(
                                new RuleKey("Chamber Pressure", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "ETCH_PROFILE",
                                                                "ETCH_PROFILE_DEFECT",
                                                                100)));

                rules.put(
                                new RuleKey("Chamber Pressure", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "ETCH_PROFILE",
                                                                "ETCH_PROFILE_DEFECT",
                                                                100)));

                rules.put(
                                new RuleKey("Chamber Pressure", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "ETCH_PROFILE",
                                                                "ETCH_PROFILE_DEFECT",
                                                                100)));
                // =================================================
                // 식각 - Chuck Temperature
                // =================================================

                rules.put(
                                new RuleKey("Chuck Temperature", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "THICKNESS",
                                                                "THICKNESS_NON_UNIFORM",
                                                                100)));

                rules.put(
                                new RuleKey("Chuck Temperature", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "THICKNESS",
                                                                "THICKNESS_NON_UNIFORM",
                                                                100)));

                rules.put(
                                new RuleKey("Chuck Temperature", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "THICKNESS",
                                                                "THICKNESS_NON_UNIFORM",
                                                                100)));

                // =================================================
                // 클리어 - Chemical Temperature
                // =================================================

                rules.put(
                                new RuleKey("Chemical Temperature", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE",
                                                                100)));

                rules.put(
                                new RuleKey("Chemical Temperature", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE",
                                                                60),
                                                new DefectCandidate(
                                                                "FILM_DAMAGE",
                                                                "BOTTOM_LAYER_DAMAGE",
                                                                40)));

                rules.put(
                                new RuleKey("Chemical Temperature", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE",
                                                                100)));

                // =================================================
                // 클리어 - Chemical Concentration
                // =================================================

                rules.put(
                                new RuleKey("Chemical Concentration", RuleName.NELSON_RULE_1),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE",
                                                                50),
                                                new DefectCandidate(
                                                                "FILM_DAMAGE",
                                                                "BOTTOM_LAYER_DAMAGE",
                                                                50)));

                rules.put(
                                new RuleKey("Chemical Concentration", RuleName.BIAS_RATIO_RULE),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE",
                                                                40),
                                                new DefectCandidate(
                                                                "FILM_DAMAGE",
                                                                "BOTTOM_LAYER_DAMAGE",
                                                                60)));

                rules.put(
                                new RuleKey("Chemical Concentration", RuleName.NELSON_RULE_3),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE",
                                                                20),
                                                new DefectCandidate(
                                                                "FILM_DAMAGE",
                                                                "BOTTOM_LAYER_DAMAGE",
                                                                80)));

        }

        public List<DefectCandidate> getCandidates(
                        String recipeParameter,
                        RuleName ruleName) {

                return rules.getOrDefault(
                                new RuleKey(recipeParameter, ruleName),
                                List.of());
        }
}