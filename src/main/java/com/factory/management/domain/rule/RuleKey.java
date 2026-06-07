package com.factory.management.domain.rule;

import com.factory.management.domain.type.RuleName;

public record RuleKey(
                String parameter,
                RuleName causeRule) {
}
