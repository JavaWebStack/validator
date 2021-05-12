package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

/**
 * Rule: string
 */
public class StringRule implements ValidationRule {

    private final int min;
    private final int max;

    public StringRule(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public StringRule(String[] params) {
        int min = 0;
        int max = Integer.MAX_VALUE;
        if (params.length > 0)
            min = Integer.parseInt(params[0]);
        if (params.length > 1)
            max = Integer.parseInt(params[1]);
        this.min = min;
        this.max = max;
    }

    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if (value == null || value.isNull())
            return null;
        if (!value.isPrimitive())
            return "Not a string value";
        if (value.toString().length() < min)
            return String.format("Shorter than minimum string length (%d < %d)", value.toString().length(), min);
        if (value.toString().length() > max)
            return String.format("Longer than maximum string length (%d > %d)", value.toString().length(), max);
        return null;
    }
}
