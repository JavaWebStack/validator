package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: email
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailRule {
    class Validator implements ValidationRule {
        public Validator(EmailRule rule) {}
        public Validator() {}

        @Override
        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;

            try {
                InternetAddress emailAddr = new InternetAddress(value.string());
                emailAddr.validate();
            } catch (AddressException ex) {
                return "Value is not a valid email address";
            }

            return null;
        }
    }
}
