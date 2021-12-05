package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: ipv6
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IPv6AddressRule {
    class Validator extends RegexRule.Validator {
        public Validator(IPv6AddressRule rule) {
            this();
        }

        public Validator() {
            super("([0-9a-fA-F]{1,4})(:(?1)){7}");
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            return super.validate(context, field, value) == null ? null : "Not a valid IPv6 Address";
        }
    }
}
