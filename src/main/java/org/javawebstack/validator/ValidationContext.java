package org.javawebstack.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationContext {

    private Validator validator;
    private final Map<String, Object> attributes = new HashMap<>();

    void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidator() {
        return validator;
    }

    public <T> T attrib(String key) {
        return (T) attributes.get(key);
    }

    public ValidationContext attrib(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

}
