package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Rule: req, required
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRule  {
    boolean allowEmptyStrings() default false;

    class Validator implements ValidationRule {
        private final boolean allowEmptyStrings;
        public Validator(RequiredRule rule) {
            this.allowEmptyStrings = rule.allowEmptyStrings();
        }

        public Validator(boolean allowEmptyStrings) {
            this.allowEmptyStrings = allowEmptyStrings;
        }

        public Validator() {
            this.allowEmptyStrings = false;
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value.isNull())
                return "Missing required field";
            if (value.getType() == AbstractElement.Type.STRING && !allowEmptyStrings && value.string().length() == 0)
                return "Missing required field";
            return null;
        }
    }
}
