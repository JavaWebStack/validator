package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleRule {

    double min() default Double.MIN_VALUE;
    double max() default Double.MAX_VALUE;

    class Validator implements ValidationRule {

        private final double min;
        private final double max;

        public Validator(DoubleRule rule) {
            this(rule.min(), rule.max());
        }

        public Validator(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public Validator(String[] params) {
            double min = Double.MIN_VALUE;
            double max = Double.MAX_VALUE;
            if (params.length > 0)
                min = Double.parseDouble(params[0]);
            if (params.length > 1)
                max = Double.parseDouble(params[1]);
            this.min = min;
            this.max = max;
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            double v;
            if (value.isNumber()) {
                v = value.number().doubleValue();
            } else if (value.isString()) {
                try {
                    v = Double.parseDouble(value.string());
                } catch (NumberFormatException ex) {
                    return "Not a double value";
                }
            } else {
                return "Not a double value";
            }
            if (v < min)
                return String.format("Smaller than the minimum value (%f < %f)", v, min);
            if (v > max)
                return String.format("Greater than the maximum value (%f > %f)", v, max);
            return null;
        }

        public String toString() {
            return "Validator{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }

    }
}
