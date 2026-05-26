package com.factory.management_service.registry;

import com.factory.management_service.domain.rule.DefectCandidate;
import com.factory.management_service.domain.rule.RuleKey;
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
                                new RuleKey("Spin Speed", "Nelson Rule 1"),

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
                                new RuleKey("Spin Speed", "Nelson Rule 2"),

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
                                new RuleKey("Spin Speed", "Nelson Rule 3"),

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
                                new RuleKey("Soft Bake Temp", "Nelson Rule 1"),

                                List.of(
                                                new DefectCandidate(
                                                                "PINHOLE",
                                                                "PINHOLE_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("Soft Bake Temp", "Nelson Rule 2"),

                                List.of(
                                                new DefectCandidate(
                                                                "RESIDUE",
                                                                "RESIDUE_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("Soft Bake Temp", "Nelson Rule 3"),

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
                // 포토 - Exposure Dose
                // =================================================

                rules.put(
                                new RuleKey("Exposure Dose", "Nelson Rule 1"),

                                List.of(
                                                new DefectCandidate(
                                                                "CD",
                                                                "CD_FAIL",
                                                                100)));

                rules.put(
                                new RuleKey("Exposure Dose", "Nelson Rule 2"),

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
                                new RuleKey("Exposure Dose", "Nelson Rule 3"),

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
        }

        public List<DefectCandidate> getCandidates(
                        String recipeParameter,
                        String ruleName) {

                return rules.getOrDefault(
                                new RuleKey(recipeParameter, ruleName),
                                List.of());
        }
}