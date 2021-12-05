package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: req, required
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRule  {
    class Validator implements ValidationRule {
        public Validator(RequiredRule rule) {} // needed

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            return !value.isNull() ? null : "Missing required field";
        }
    }
}
