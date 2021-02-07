package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

import java.lang.reflect.Field;

public class IPv6AddressRule extends RegexRule {
    public IPv6AddressRule() {
        super("([0-9a-fA-F]{1,4})(:(?1)){7}");
    }

    public String validate(Validator validator, Field field, AbstractElement value) {
        return super.validate(validator, field, value) == null ? null : "Not a valid IPv6 Address";
    }
}
