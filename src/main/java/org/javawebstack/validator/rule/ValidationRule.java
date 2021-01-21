package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

public interface ValidationRule {

    String validate(Validator validator, AbstractElement value);

}
