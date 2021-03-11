package org.javawebstack.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {

    private final ValidationContext context;
    private final Map<String[], List<ValidationError>> errors;

    ValidationResult(ValidationContext context, Map<String[], List<ValidationError>> errors) {
        this.context = context;
        this.errors = errors;
    }

    public ValidationContext getContext() {
        return context;
    }

    public Map<String[], List<ValidationError>> getErrors() {
        return errors;
    }

    public Map<String, List<ValidationError>> getErrorMap() {
        Map<String, List<ValidationError>> errorMap = new HashMap<>();
        getErrors().forEach((k, v) -> errorMap.put(String.join(".", k), v));
        return errorMap;
    }

    public boolean isValid() {
        return errors.size() == 0;
    }

}
