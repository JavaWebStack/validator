package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

public class ArrayRule implements ValidationRule {

    private final int min;
    private final int max;

    public ArrayRule(int min, int max){
        this.min = min;
        this.max = max;
    }

    public ArrayRule(String[] params){
        int min = 0;
        int max = Integer.MAX_VALUE;
        if(params.length > 0)
            min = Integer.parseInt(params[0]);
        if(params.length > 1)
            max = Integer.parseInt(params[1]);
        this.min = min;
        this.max = max;
    }

    public String validate(Validator validator, AbstractElement value) {
        if(value == null)
            return null;
        if(!value.isArray())
            return "Not an array";
        if(value.array().size() < min)
            return String.format("Shorter than minimum array length (%d < %d)", value.array().size(), min);
        if(value.array().size() > max)
            return String.format("Longer than maximum array length (%d > %d)", value.array().size(), max);
        return null;
    }

}
