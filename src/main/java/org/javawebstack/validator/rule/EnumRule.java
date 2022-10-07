package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rule: enum
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumRule {
    String[] value();

    class Validator implements ValidationRule {
        private final List<String> values;

        public Validator(EnumRule rule) {
            this(rule.value());
        }

        public Validator(List<String> values) {
            this.values = values;
        }

        public Validator(String... values) {
            this(Arrays.asList(values));
        }

        public Validator(Class<? extends Enum<?>> enumType) {
            this(Arrays.stream(enumType.getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            return value.isString() && values.contains(value.string()) ? null : String.format("Not an element of [%s]", String.join(",", values));
        }
    }
}
