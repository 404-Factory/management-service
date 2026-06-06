package com.factory.management_service.domain.rule;

import com.factory.management_service.domain.type.RuleName;

public record RuleKey(
                String parameter,
                RuleName causeRule) {
}
