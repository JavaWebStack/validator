package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: ipv4
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IPv4AddressRule {
    class Validator extends RegexRule.Validator {
        public Validator(IPv4AddressRule rule) {
            this();
        }

        public Validator() {
            super("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            return super.validate(context, field, value) == null ? null : "Not a valid IPv4 Address";
        }
    }
}
