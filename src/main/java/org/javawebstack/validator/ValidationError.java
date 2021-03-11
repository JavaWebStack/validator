package org.javawebstack.validator;

import org.javawebstack.validator.rule.ValidationRule;

public class ValidationError {

    private final ValidationRule rule;
    private final String message;

    public ValidationError(ValidationRule rule, String message) {
        this.rule = rule;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ValidationRule getRule() {
        return rule;
    }

    public Class<? extends ValidationRule> getRuleType() {
        return rule.getClass();
    }

    public String toString() {
        return message;
    }

}
