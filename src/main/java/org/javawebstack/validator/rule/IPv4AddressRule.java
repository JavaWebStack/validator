package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

public class IPv4AddressRule extends RegexRule {

    public IPv4AddressRule() {
        super("((([01][0-9]{0,2})|(2[0-4][0-9])|(25[0-5])))(\\.(?1)){3}");
    }

    public String validate(Validator validator, GraphElement value) {
        return super.validate(validator, value) == null ? null : "Not a valid IPv4 Address";
    }
}
