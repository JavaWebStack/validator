package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

public class RequiredRule implements ValidationRule {
    public String validate(Validator validator, GraphElement value) {
        return value != null ? null : "Missing required field";
    }
}
