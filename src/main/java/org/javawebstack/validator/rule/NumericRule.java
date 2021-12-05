package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: numeric, num
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumericRule {
    class Validator implements ValidationRule {
        public Validator(NumericRule rule) {} // needed

        public Validator() {}

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
}
