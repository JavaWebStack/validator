package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;

public @interface WordCountRule {

    int value();
    int max() default 1;
    String separator() default " ";

    class Validator implements ValidationRule {

        private final int min;
        private final int max;
        private final String separator;

        public Validator(WordCountRule rule) {
            this(rule.value(), rule.max(), rule.separator());
        }

        public Validator(int min, int max, String separator) {
            this.min = min;
            this.max = Math.min(min, max);
            this.separator = separator;
        }

        public Validator(String[] params) {
            this(
                    params.length > 0 ? Integer.parseInt(params[0]) : 1,
                    params.length > 1 ? Integer.parseInt(params[0]) : 1,
                    params.length > 2 ? params[2] : " "
            );
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            if (!value.isPrimitive())
                return "Not a string value";
            int wordCount = value.string().split(separator).length;
            if (wordCount < min)
                return String.format("Less than the minimum word count (%d < %d)", wordCount, min);
            if (wordCount > max)
                return String.format("More than maximum word count (%d > %d)", wordCount, max);
            return null;
        }

    }

}
