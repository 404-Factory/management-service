package com.factory.management.validation.constraintvalidators;

import com.factory.management.validation.constraints.ValidStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {

    private static final Set<String> VALUES = Set.of("NORMAL", "WARNING", "CRITICAL");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return VALUES.contains(value.toUpperCase());
    }
}
