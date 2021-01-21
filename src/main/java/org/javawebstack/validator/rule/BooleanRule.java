package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

public class BooleanRule implements ValidationRule {
    public String validate(Validator validator, AbstractElement value) {
        if(value == null)
            return null;
        if(value.isBoolean())
            return null;
        if(value.isString() && (value.string().equals("true") || value.string().equals("false")))
            return null;
        return "Not a boolean value";
    }
}
