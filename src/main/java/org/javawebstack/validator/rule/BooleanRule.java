package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

public class BooleanRule implements ValidationRule {
    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if(value == null)
            return null;
        if(value.isBoolean())
            return null;
        if(value.isString() && (value.string().equals("true") || value.string().equals("false")))
            return null;
        return "Not a boolean value";
    }
}
