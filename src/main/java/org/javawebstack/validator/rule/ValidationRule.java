package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

public interface ValidationRule {

    String validate(Validator validator, GraphElement value);

}
