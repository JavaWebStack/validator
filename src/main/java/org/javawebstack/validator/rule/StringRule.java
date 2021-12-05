package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: string
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringRule {
    int min();
    int max();

    class Validator implements ValidationRule {
        private final int min;
        private final int max;

        public Validator(StringRule rule) {
            this.min = rule.min();
            this.max = rule.max();
        }

        public Validator(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public Validator(String[] params) {
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
}
