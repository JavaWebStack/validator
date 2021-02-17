package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

/**
 * Rule: ipv4
 */
public class IPv4AddressRule extends RegexRule {

    public IPv4AddressRule() {
        super("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    }

    public String validate(ValidationContext context, Field field, AbstractElement value) {
        return super.validate(context, field, value) == null ? null : "Not a valid IPv4 Address";
    }
}
