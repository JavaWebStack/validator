package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

/**
 * Rule: numeric, num
 */
public class NumericRule implements ValidationRule {
    @Override
    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if (value == null || value.isNull())
            return null;

        float v;
        if (value.isNumber())
            v = value.number().floatValue();
        else if (value.isString()) {
            try {
                v = Float.parseFloat(value.string());
            } catch (NumberFormatException ex) {
                return "Not a numeric value";
            }
        } else {
            return "Not a numeric value";
        }

        return null;
    }
}
