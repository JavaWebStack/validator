package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

public class IPv6AddressRule extends RegexRule {
    public IPv6AddressRule() {
        super("([0-9a-fA-F]{1,4})(:(?1)){7}");
    }

    public String validate(Validator validator, GraphElement value) {
        return super.validate(validator, value) == null ? null : "Not a valid IPv6 Address";
    }
}
