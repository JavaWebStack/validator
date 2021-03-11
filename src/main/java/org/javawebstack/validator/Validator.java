package org.javawebstack.validator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import org.javawebstack.abstractdata.AbstractArray;
import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.abstractdata.AbstractMapper;
import org.javawebstack.abstractdata.AbstractNull;
import org.javawebstack.validator.rule.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public class Validator {

    private static final Map<String, Constructor<? extends ValidationRule>> validationRules = new HashMap<>();
    private static final Map<Class<?>, Validator> validators = new HashMap<>();

    static {
        registerRuleType("string", StringRule.class);
        registerRuleType("boolean", BooleanRule.class);
        registerRuleType("bool", BooleanRule.class);
        registerRuleType("enum", EnumRule.class);
        registerRuleType("required", RequiredRule.class);
        registerRuleType("req", RequiredRule.class);
        registerRuleType("ipv4", IPv4AddressRule.class);
        registerRuleType("ipv6", IPv6AddressRule.class);
        registerRuleType("int", IntegerRule.class);
        registerRuleType("integer", IntegerRule.class);
        registerRuleType("numeric", NumericRule.class);
        registerRuleType("num", NumericRule.class);
        registerRuleType("date", DateRule.class);
        registerRuleType("array", ArrayRule.class);
        registerRuleType("list", ArrayRule.class);
        registerRuleType("alpha", AlphaRule.class);
        registerRuleType("alpha_num", AlphaNumRule.class);
        registerRuleType("alpha_dash", AlphaDashRule.class);
        registerRuleType("email", EmailRule.class);
        registerRuleType("regex", RegexRule.class);
    }

    public static void registerRuleType(String name, Class<? extends ValidationRule> type) {
        try {
            Constructor<? extends ValidationRule> constructor = type.getDeclaredConstructor(String[].class);
            constructor.setAccessible(true);
            validationRules.put(name, constructor);
            return;
        } catch (NoSuchMethodException ignored) {
        }
        try {
            Constructor<? extends ValidationRule> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            validationRules.put(name, constructor);
        } catch (NoSuchMethodException ignored) {
        }
    }

    public static ValidationRule makeRule(String name, String[] params) {
        Constructor<? extends ValidationRule> constructor = validationRules.get(name);
        if (constructor == null)
            return null;
        if (constructor.getParameterCount() == 0) {
            try {
                return constructor.newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {
            }
        } else {
            try {
                return constructor.newInstance((Object) params);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }
        return null;
    }

    public static ValidationRule makeRule(String source) {
        String[] params = null;
        if (source.contains("(")) {
            String[] spl = source.split("\\(", 2);
            source = spl[0];
            String s = spl[1];
            s = s.substring(0, s.length() - 1);
            AbstractArray array = AbstractArray.fromJson(new Gson().fromJson("[" + s + "]", JsonArray.class));
            if (array.stream().filter(e -> e.isPrimitive()).count() == array.size()) {
                params = new String[array.size()];
                for (int i = 0; i < params.length; i++)
                    params[i] = array.get(i).toString();
            }
        }
        if (params == null)
            params = new String[0];
        return makeRule(source, params);
    }

    public static ValidationRule[] makeRules(String... sources) {
        ValidationRule[] rules = new ValidationRule[sources.length];
        for (int i = 0; i < rules.length; i++)
            rules[i] = makeRule(sources[i]);
        return rules;
    }

    public static Validator getValidator(Class<?> type) {
        Validator validator = validators.get(type);
        if (validator == null) {
            validator = new Validator();
            getClassRules(null, type).forEach(validator::rule);
            validators.put(type, validator);
        }
        return validator;
    }

    public static <T> T map(ValidationContext context, Class<T> type, AbstractElement element, AbstractMapper mapper) {
        Validator validator = getValidator(type);
        ValidationResult result = validator.validate(context, element);
        if (!result.isValid())
            throw new ValidationException(result);
        return mapper.fromAbstract(element, type);
    }

    public static <T> T map(ValidationContext context, Class<T> type, AbstractElement element) {
        return map(context, type, element, new AbstractMapper());
    }

    private final Map<String[], ValidationConfig> rules = new HashMap<>();

    public Validator rule(String[] key, ValidationRule... rules) {
        return rule(key, Arrays.asList(rules));
    }

    public Validator rule(String[] key, List<ValidationRule> rules) {
        this.rules.put(key, new ValidationConfig(null, rules));
        return this;
    }

    private Validator rule(String[] key, ValidationConfig config) {
        this.rules.put(key, config);
        return this;
    }

    public Validator rule(String key, List<ValidationRule> rules) {
        return rule(Arrays.stream(key.split("\\.")).map(k -> k.equals("*") ? null : k).toArray(String[]::new), rules);
    }

    public Validator rule(String key, ValidationRule... rules) {
        return rule(key, Arrays.asList(rules));
    }

    public ValidationResult validate(ValidationContext context, AbstractElement rootElement) {
        context.setValidator(this);
        Map<String[], List<ValidationError>> errors = new HashMap<>();
        for (String[] key : rules.keySet()) {
            errors.putAll(check(context, rules, new String[0], new String[0], key, rootElement));
        }
        return new ValidationResult(context, errors);
    }

    private Map<String[], List<ValidationError>> check(ValidationContext context, Map<String[], ValidationConfig> rules, String[] keyPrefix, String[] resolvedKeyPrefix, String[] key, AbstractElement element) {
        if (key.length == 0) {
            Map<String[], List<ValidationError>> errors = new HashMap<>();
            ValidationConfig config = getMapValue(rules, keyPrefix);
            for (ValidationRule rule : config.rules) {
                String error = rule.validate(context, config.field, element);
                if (error != null) {
                    if (!errors.containsKey(resolvedKeyPrefix))
                        errors.put(resolvedKeyPrefix, new ArrayList<>());
                    errors.get(resolvedKeyPrefix).add(new ValidationError(rule, error));
                }
            }
            return errors;
        }
        if (element == null)
            element = AbstractNull.INSTANCE;
        String[] innerKey = new String[key.length - 1];
        System.arraycopy(key, 1, innerKey, 0, innerKey.length);
        String[] innerKeyPrefix = new String[keyPrefix.length + 1];
        System.arraycopy(keyPrefix, 0, innerKeyPrefix, 0, keyPrefix.length);
        innerKeyPrefix[innerKeyPrefix.length - 1] = key[0];
        if (key[0].equals("*")) {
            Map<String[], List<ValidationError>> errors = new HashMap<>();
            if (element.isArray()) {
                for (int i = 0; i < element.array().size(); i++) {
                    String[] innerResolvedKeyPrefix = new String[keyPrefix.length + 1];
                    System.arraycopy(resolvedKeyPrefix, 0, innerResolvedKeyPrefix, 0, resolvedKeyPrefix.length);
                    innerResolvedKeyPrefix[innerResolvedKeyPrefix.length - 1] = String.valueOf(i);
                    errors.putAll(check(context, rules, innerKeyPrefix, innerResolvedKeyPrefix, innerKey, element.array().get(i)));
                }
            }
            if (element.isObject()) {
                for (String k : element.object().keys()) {
                    String[] innerResolvedKeyPrefix = new String[keyPrefix.length + 1];
                    System.arraycopy(resolvedKeyPrefix, 0, innerResolvedKeyPrefix, 0, resolvedKeyPrefix.length);
                    innerResolvedKeyPrefix[innerResolvedKeyPrefix.length - 1] = k;
                    errors.putAll(check(context, rules, innerKeyPrefix, innerResolvedKeyPrefix, innerKey, element.object().get(k)));
                }
            }
            return errors;
        }
        AbstractElement value = AbstractNull.INSTANCE;
        if (element.isArray()) {
            try {
                value = element.array().get(Integer.parseInt(key[0]));
            } catch (Exception ignored) {
            }
        }
        if (element.isObject()) {
            value = element.object().get(key[0]);
        }
        String[] innerResolvedKeyPrefix = new String[keyPrefix.length + 1];
        System.arraycopy(resolvedKeyPrefix, 0, innerResolvedKeyPrefix, 0, resolvedKeyPrefix.length);
        innerResolvedKeyPrefix[innerResolvedKeyPrefix.length - 1] = key[0];
        return check(context, rules, innerKeyPrefix, innerResolvedKeyPrefix, innerKey, value);
    }

    private static boolean stringArrayEqual(String[] a, String[] b) {
        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null && b[i] == null)
                continue;
            if (a[i] == null || b[i] == null)
                return false;
            if (!a[i].equals(b[i]))
                return false;
        }
        return true;
    }

    private static <V> V getMapValue(Map<String[], V> map, String[] key) {
        for (String[] k : map.keySet()) {
            if (stringArrayEqual(k, key)) {
                return map.get(k);
            }
        }
        return null;
    }

    private static <V> void putValidationConfigMapValue(Map<String[], ValidationConfig> map, String[] key, ValidationConfig value) {
        for (String[] k : map.keySet()) {
            if (stringArrayEqual(k, key)) {
                map.put(k, value);
                return;
            }
        }
        map.put(key, value);
    }

    private static void addMapRules(Field field, Map<String[], ValidationConfig> map, String[] key, List<ValidationRule> rules) {
        ValidationConfig config = getMapValue(map, key);
        if (config == null)
            config = new ValidationConfig(field, new ArrayList<>());
        config.rules.addAll(rules);
        putValidationConfigMapValue(map, key, config);
    }

    private static String toSnakeCase(String source) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(source.charAt(0)));
        for (int i = 1; i < source.length(); i++) {
            if (Character.isUpperCase(source.charAt(i))) {
                if (!Character.isUpperCase(source.charAt(i - 1)))
                    sb.append("_");
                sb.append(Character.toLowerCase(source.charAt(i)));
            } else {
                sb.append(source.charAt(i));
            }
        }
        return sb.toString();
    }

    private static String getFieldName(Field field) {
        SerializedName[] serializedNames = field.getAnnotationsByType(SerializedName.class);
        if (serializedNames.length > 0)
            return serializedNames[0].value();
        return toSnakeCase(field.getName());
    }

    private static class ValidationConfig {
        private final Field field;
        private final List<ValidationRule> rules;

        public ValidationConfig(Field field, List<ValidationRule> rules) {
            this.field = field;
            this.rules = rules;
        }
    }

    private static Map<String[], ValidationConfig> getClassRules(Field field, Class<?> type) {
        Map<String[], ValidationConfig> rules = new HashMap<>();
        if (type.isAnnotation())
            return rules;
        if (type.equals(String.class))
            return rules;
        if (type.equals(Timestamp.class) || type.equals(java.util.Date.class)) {
            rules.put(new String[0], new ValidationConfig(field, Collections.singletonList(new DateRule(new String[]{}))));
            return rules;
        }
        if (type.equals(Date.class)) {
            rules.put(new String[0], new ValidationConfig(field, Collections.singletonList(new DateRule(new String[]{"date"}))));
            return rules;
        }
        if (type.equals(Boolean.class)) {
            rules.put(new String[0], new ValidationConfig(field, Collections.singletonList(new BooleanRule())));
            return rules;
        }
        if (type.equals(Integer.class)) {
            rules.put(new String[0], new ValidationConfig(field, Collections.singletonList(new IntegerRule(Integer.MIN_VALUE, Integer.MAX_VALUE))));
            return rules;
        }
        if (type.equals(UUID.class)) {
            rules.put(new String[0], new ValidationConfig(field, Collections.singletonList(new UUIDRule())));
            return rules;
        }
        if (type.isEnum()) {
            rules.put(new String[0], new ValidationConfig(field, Collections.singletonList(new EnumRule((Class<? extends Enum<?>>) type))));
            return rules;
        }
        if (type.isArray()) {
            getClassRules(null, type.getComponentType()).forEach((key, validators) -> {
                String[] actualKey = new String[key.length + 1];
                actualKey[0] = "*";
                System.arraycopy(key, 0, actualKey, 1, key.length);
                addMapRules(null, rules, actualKey, validators.rules);
            });
            return rules;
        }
        for (Field f : getFieldsRecursive(type)) {
            String name = getFieldName(f);
            getClassRules(f, f.getType()).forEach((key, validators) -> {
                String[] actualKey = new String[key.length + 1];
                actualKey[0] = name;
                System.arraycopy(key, 0, actualKey, 1, key.length);
                addMapRules(f, rules, actualKey, validators.rules);
            });
            f.setAccessible(true);
            Rule[] ruleAnnotations = f.getDeclaredAnnotationsByType(Rule.class);
            if (ruleAnnotations.length > 0) {
                List<ValidationRule> r = new ArrayList<>();
                for (String source : ruleAnnotations[0].value()) {
                    ValidationRule rule = Validator.makeRule(source);
                    if (rule != null)
                        r.add(rule);
                }
                if (r.size() > 0)
                    addMapRules(f, rules, new String[]{name}, r);
            }
        }
        return rules;
    }

    private static List<Field> getFieldsRecursive(Class<?> type) {
        List<Field> fields;
        if (type.getSuperclass() != null && !type.getSuperclass().equals(Object.class))
            fields = getFieldsRecursive(type.getSuperclass());
        else
            fields = new ArrayList<>();
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        return fields;
    }

}
