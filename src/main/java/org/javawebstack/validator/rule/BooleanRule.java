package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: boolean, bool
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanRule {
    class Validator implements ValidationRule {
        public Validator(BooleanRule rule) {}

        public Validator() {}

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            if (value.isBoolean())
                return null;
            if (value.isString() && (value.string().equals("true") || value.string().equals("false")))
                return null;
            return "Not a boolean value";
        }
    }
}
