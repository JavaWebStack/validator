package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Rule: date
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRule {
    String value();

    class Validator implements ValidationRule {
        private final DateFormat dateFormat;

        public Validator(DateRule rule) {
            this.dateFormat = new SimpleDateFormat(rule.value());
        }

        public Validator(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public Validator(String[] params) {
            switch (params.length > 0 ? params[0] : "datetime") {
                case "date":
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    break;
                case "time":
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    break;
                default:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
            }
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            if (!value.isString())
                return "Not a valid date";
            try {
                dateFormat.parse(value.string());
            } catch (Exception ex) {
                return "Not a valid date";
            }
            return null;
        }
    }
}
