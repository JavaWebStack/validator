package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

/**
 * Rule: req, required
 */
public class RequiredRule implements ValidationRule {
    public String validate(ValidationContext context, Field field, AbstractElement value) {
        return value != null ? null : "Missing required field";
    }
}
