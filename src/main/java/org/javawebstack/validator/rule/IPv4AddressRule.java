package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

import java.lang.reflect.Field;

public class IPv4AddressRule extends RegexRule {

    public IPv4AddressRule() {
        super("((([01][0-9]{0,2})|(2[0-4][0-9])|(25[0-5])))(\\.(?1)){3}");
    }

    public String validate(Validator validator, Field field, AbstractElement value) {
        return super.validate(validator, field, value) == null ? null : "Not a valid IPv4 Address";
    }
}
