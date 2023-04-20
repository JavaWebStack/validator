package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: int, integer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerRule {
    int min();
    int max() default Integer.MAX_VALUE;
    int step() default 1;

    class Validator implements ValidationRule {
        private final int min;
        private final int max;
        private final int step;

        public Validator(IntegerRule rule) {
            this(rule.min(), rule.max(), rule.step());
        }

        public Validator(int min, int max, int step) {
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public Validator(int min, int max) {
            this(min, max, 1);
        }

        public Validator(String[] params) {
            int min = Integer.MIN_VALUE;
            int max = Integer.MAX_VALUE;
            int step = 1;
            if (params.length > 0)
                min = Integer.parseInt(params[0]);
            if (params.length > 1)
                max = Integer.parseInt(params[1]);
            if (params.length > 2)
                step = Integer.parseInt(params[2]);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            int v;
            if (value.isNumber()) {
                v = value.number().intValue();
            } else if (value.isString()) {
                try {
                    v = Integer.parseInt(value.string());
                } catch (NumberFormatException ex) {
                    return "Not an integer value";
                }
            } else {
                return "Not an integer value";
            }
            if (v < min)
                return String.format("Smaller than the minimum value (%d < %d)", v, min);
            if (v > max)
                return String.format("Greater than the maximum value (%d > %d)", v, max);
            if (step > 1 && (v - min) % step != 0)
                return String.format("Not in steps of %d", step);
            return null;
        }

        public String toString() {
            return "IntegerRule{" +
                    "min=" + min +
                    ", max=" + max +
                    ", step=" + step +
                    '}';
        }
    }
}
