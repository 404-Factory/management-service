package com.factory.management.simulator.rule;

import com.factory.management.simulator.type.RuleName;

public record RuleKey(
                String parameter,
                RuleName causeRule) {
}
