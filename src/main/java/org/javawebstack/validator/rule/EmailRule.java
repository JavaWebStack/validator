package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.lang.reflect.Field;

/**
 * Rule: email
 */
public class EmailRule implements ValidationRule {
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
