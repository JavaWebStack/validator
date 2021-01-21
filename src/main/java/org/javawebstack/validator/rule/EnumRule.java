package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumRule implements ValidationRule {
    private final List<String> values;
    public EnumRule(List<String> values){
        this.values = values;
    }
    public EnumRule(String... values){
        this(Arrays.asList(values));
    }
    public EnumRule(Class<? extends Enum<?>> enumType){
        this(Arrays.stream(enumType.getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
    }
    public String validate(Validator validator, AbstractElement value) {
        if(value == null)
            return null;
        return value.isString() && values.contains(value.string()) ? null : String.format("Not an element of [%s]", String.join(",", values));
    }
}
