package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Rule: uuid
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UUIDRule {
    class Validator implements ValidationRule {
        public Validator(UUIDRule rule) {} // needed

        public Validator() {}

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if(value == null || value.isNull())
                return null;
            if (!value.isString())
                return "Not a valid uuid value";
            try {
                UUID.fromString(value.string());
                return null;
            } catch (Exception ignored) {
                return "Not a valid uuid value";
            }
        }
    }
}
