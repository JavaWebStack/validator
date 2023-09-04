package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Locale;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CharsetRule {

    String value() default "";
    boolean upper() default false;
    boolean lower() default false;
    boolean numeric() default false;
    boolean accents() default false;

    class Validator implements ValidationRule {

        private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
        private static final String ACCENTS = "äëïöüéàèùâêîôûç";

        private final String charset;

        public Validator(CharsetRule rule) {
            this(makeCharset(rule));
        }

        public Validator(String charset) {
            this.charset = charset;
        }

        public Validator(String[] params) {
            this(params.length > 0 ? params[0] : "");
        }

        public String validate(ValidationContext context, Field field, AbstractElement value) {
            if (value == null || value.isNull())
                return null;
            if (!value.isPrimitive())
                return "Not a string value";
            char[] chars = value.string().toCharArray();
            for(int i=0; i<chars.length; i++) {
                char c = chars[i];
                if(!charset.contains(String.valueOf(c))) {
                    return String.format("Forbidden character '%c' at position %d", c, i);
                }
            }
            return null;
        }

        private static String makeCharset(CharsetRule rule) {
            StringBuilder sb = new StringBuilder(rule.value());
            if(rule.lower()) {
                sb.append(ALPHA);
                if(rule.accents())
                    sb.append(ACCENTS);
            }
            if(rule.upper()) {
                sb.append(ALPHA.toUpperCase(Locale.ROOT));
                if(rule.accents())
                    sb.append(ACCENTS.toUpperCase(Locale.ROOT));
            }
            if(rule.numeric()) {
                sb.append("1234567890");
            }
            return sb.toString();
        }

    }

}
