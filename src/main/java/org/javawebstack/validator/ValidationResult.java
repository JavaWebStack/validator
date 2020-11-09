package org.javawebstack.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {

    private final Map<String[], List<String>> errors;

    ValidationResult(Map<String[], List<String>> errors){
        this.errors = errors;
    }

    public Map<String[], List<String>> getErrors(){
        return errors;
    }

    public Map<String, List<String>> getErrorMap(){
        Map<String, List<String>> errorMap = new HashMap<>();
        getErrors().forEach((k,v) -> errorMap.put(String.join(".", k), v));
        return errorMap;
    }

    public boolean isValid(){
        return errors.size() == 0;
    }

}
