package com.factory.management_service.domain.rule;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RuleKey {

    private String parameter;
    private String causeRule;
}
