package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Rule: regex
 */
public class RegexRule implements ValidationRule {
    private final String regex;
    private final Pattern pattern;

    public RegexRule(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public String validate(ValidationContext context, Field field, AbstractElement value) {
        if (value == null)
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
