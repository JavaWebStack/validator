package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

/**
 * Rule: alpha_num
 */
public class AlphaNumRule implements ValidationRule {
    @Override
    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if (value == null || value.isNull())
            return null;

        String pattern = "[A-Za-z0-9]*";
        if (!value.string().matches(pattern))
            return "Value must only contain alpha-numeric characters.";

        return null;
    }
}
