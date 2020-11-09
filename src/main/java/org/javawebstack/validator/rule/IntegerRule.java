package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

public class IntegerRule implements ValidationRule {

    private final int min;
    private final int max;

    public IntegerRule(int min, int max){
        this.min = min;
        this.max = max;
    }

    public IntegerRule(String[] params){
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        if(params.length > 0)
            min = Integer.parseInt(params[0]);
        if(params.length > 1)
            max = Integer.parseInt(params[1]);
        this.min = min;
        this.max = max;
    }

    public IntegerRule(){
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public String validate(Validator validator, GraphElement value) {
        if(value == null)
            return null;
        int v;
        if(value.isNumber()) {
            v = value.number().intValue();
        }else if(value.isString()){
            try {
                v = Integer.parseInt(value.string());
            }catch (NumberFormatException ex){
                return "Not an integer value";
            }
        }else{
            return "Not an integer value";
        }
        if(v < min)
            return String.format("Smaller than the minimum value (%d < %d)", v, min);
        if(v > max)
            return String.format("Greater than the maximum value (%d > %d)", v, max);
        return null;
    }
}
