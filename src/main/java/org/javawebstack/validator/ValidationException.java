package org.javawebstack.validator;

public class ValidationException extends RuntimeException {

    private final ValidationResult result;

    public ValidationException(ValidationResult result){
        this.result = result;
    }

    public ValidationResult getResult() {
        return result;
    }
}
