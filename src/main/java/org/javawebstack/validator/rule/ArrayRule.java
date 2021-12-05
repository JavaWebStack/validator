package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: array, list
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayRule {
    int min();
    int max();

    class Validator implements ValidationRule {
        private final int min;
        private final int max;

        public Validator(ArrayRule rule) {
            this(rule.min(), rule.max());
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
            if (!value.isArray())
                return "Not an array";
            if (value.array().size() < min)
                return String.format("Shorter than minimum array length (%d < %d)", value.array().size(), min);
            if (value.array().size() > max)
                return String.format("Longer than maximum array length (%d > %d)", value.array().size(), max);
            return null;
        }
    }
}
