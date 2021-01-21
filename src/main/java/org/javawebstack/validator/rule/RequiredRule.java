package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

public class RequiredRule implements ValidationRule {
    public String validate(Validator validator, AbstractElement value) {
        return value != null ? null : "Missing required field";
    }
}
