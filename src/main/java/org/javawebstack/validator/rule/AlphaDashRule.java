package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: alpha_dash
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphaDashRule {
    class Validator implements ValidationRule {
        public Validator(AlphaDashRule rule) {}
        public Validator() {}

        @Override
        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;

            String pattern = "[A-Za-z0-9-_]*";
            if (!value.string().matches(pattern))
                return "Value must only contain alpha-numeric characters as well as dashes and underscores.";

            return null;
        }
    }
}
