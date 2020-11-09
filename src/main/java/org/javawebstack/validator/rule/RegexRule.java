package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.validator.Validator;

import java.util.regex.Pattern;

public class RegexRule implements ValidationRule {
    private final String regex;
    private final Pattern pattern;
    public RegexRule(String regex){
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }
    public String validate(Validator validator, GraphElement value) {
        if(value == null)
            return null;
        return value.isString() && pattern.matcher(value.string()).matches() ? null : "Doesn't match the expected pattern";
    }

    public String getRegex() {
        return regex;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
