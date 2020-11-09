package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

public class StringRule implements ValidationRule {

    private final int min;
    private final int max;

    public StringRule(int min, int max){
        this.min = min;
        this.max = max;
    }

    public StringRule(String[] params){
        int min = 0;
        int max = Integer.MAX_VALUE;
        if(params.length > 0)
            min = Integer.parseInt(params[0]);
        if(params.length > 1)
            max = Integer.parseInt(params[1]);
        this.min = min;
        this.max = max;
    }

    public String validate(Validator validator, GraphElement value) {
        if(value == null)
            return null;
        if(!value.isPrimitive())
            return "Not a string value";
        if(value.toString().length() < min)
            return String.format("Shorter than minimum length (%d < %d)", value.toString().length(), min);
        if(value.toString().length() > max)
            return String.format("Longer than maximum length (%d > %d)", value.toString().length(), max);
        return null;
    }
}
