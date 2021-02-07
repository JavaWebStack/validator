package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

import java.lang.reflect.Field;

public class RequiredRule implements ValidationRule {
    public String validate(Validator validator, Field field, AbstractElement value) {
        return value != null ? null : "Missing required field";
    }
}
