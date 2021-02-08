package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDRule implements ValidationRule {

    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if(!value.isString())
            return "Not a valid uuid value";
        try {
            UUID.fromString(value.string());
            return null;
        }catch (Exception ignored){
            return "Not a valid uuid value";
        }
    }

}
