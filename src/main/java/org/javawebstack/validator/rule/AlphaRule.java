package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

/**
 * Rule: alpha
 */
public class AlphaRule implements ValidationRule {
    @Override
    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if (value == null)
            return null;

        String pattern = "[A-Za-z]*";
        if (!value.string().matches(pattern))
            return "Value must only contain alphabetic characters.";

        return null;
    }
}
