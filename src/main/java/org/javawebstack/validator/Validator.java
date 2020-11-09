package org.javawebstack.validator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.javawebstack.graph.GraphArray;
import org.javawebstack.graph.GraphElement;
import org.javawebstack.graph.GraphMapper;
import org.javawebstack.validator.rule.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;

public class Validator {

    private static final Map<String, Constructor<? extends ValidationRule>> validationRules = new HashMap<>();
    private static final Map<Class<?>, Validator> validators = new HashMap<>();

    static {
        registerRuleType("string", StringRule.class);
        registerRuleType("boolean", BooleanRule.class);
        registerRuleType("enum", EnumRule.class);
        registerRuleType("required", RequiredRule.class);
        registerRuleType("ipv4", IPv4AddressRule.class);
        registerRuleType("ipv6", IPv6AddressRule.class);
        registerRuleType("integer", IntegerRule.class);
        registerRuleType("timestamp", TimestampRule.class);
    }

    public static void registerRuleType(String name, Class<? extends ValidationRule> type){
        try {
            Constructor<? extends ValidationRule> constructor = type.getDeclaredConstructor(String[].class);
            constructor.setAccessible(true);
            validationRules.put(name, constructor);
            return;
        } catch (NoSuchMethodException ignored) {}
        try {
            Constructor<? extends ValidationRule> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            validationRules.put(name, constructor);
        } catch (NoSuchMethodException ignored) {}
    }

    public static ValidationRule makeRule(String name, String[] params){
        Constructor<? extends ValidationRule> constructor = validationRules.get(name);
        if(constructor == null)
            return null;
        if(constructor.getParameterCount() == 0){
            try {
                return constructor.newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {}
        }else{
            try {
                return constructor.newInstance((Object) params);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {}
        }
        return null;
    }

    public static ValidationRule makeRule(String source){
        String[] params = null;
        if(source.contains("(")){
            String[] spl = source.split("\\(", 2);
            source = spl[0];
            String s = spl[1];
            s = s.substring(0, s.length()-1);
            GraphArray array = GraphArray.fromJson(new Gson().fromJson("["+s+"]", JsonArray.class));
            if(array.stream().filter(e -> e.isPrimitive()).count() == array.size()){
                params = new String[array.size()];
                for(int i=0; i<params.length; i++)
                    params[i] = array.get(i).toString();
            }
        }
        if(params == null)
            params = new String[0];
        return makeRule(source, params);
    }

    public static ValidationRule[] makeRules(String... sources){
        ValidationRule[] rules = new ValidationRule[sources.length];
        for(int i=0; i<rules.length; i++)
            rules[i] = makeRule(sources[i]);
        return rules;
    }

    public static Validator getValidator(Class<?> type){
        Validator validator = validators.get(type);
        if(validator == null){
            validator = new Validator();
            getClassRules(type).forEach(validator::rule);
            validators.put(type, validator);
        }
        return validator;
    }

    public static <T> T map(Class<T> type, GraphElement element, GraphMapper mapper){
        Validator validator = getValidator(type);
        ValidationResult result = validator.validate(element);
        if(!result.isValid())
            throw new ValidationException(result);
        return mapper.fromGraph(element, type);
    }

    public static <T> T map(Class<T> type, GraphElement element){
        return map(type, element, new GraphMapper());
    }

    private final Map<String[], List<ValidationRule>> rules = new HashMap<>();

    public Validator rule(String[] key, ValidationRule... rules){
        return rule(key, Arrays.asList(rules));
    }

    public Validator rule(String[] key, List<ValidationRule> rules){
        this.rules.put(key, rules);
        return this;
    }

    public Validator rule(String key, List<ValidationRule> rules){
        return rule(Arrays.stream(key.split("\\.")).map(k -> k.equals("*") ? null : k).toArray(String[]::new), rules);
    }

    public Validator rule(String key, ValidationRule... rules){
        return rule(key, Arrays.asList(rules));
    }

    public ValidationResult validate(GraphElement rootElement){
        Map<String[], List<String>> errors = new HashMap<>();
        for(String[] sourceKey : rules.keySet()){
            Map<String[], GraphElement> map = matchKeys(sourceKey, rootElement);
            List<ValidationRule> validators = rules.get(sourceKey);
            map.forEach((key, value) -> {
                List<String> fieldErrors = new ArrayList<>();
                for(ValidationRule rule : validators){
                    String error = rule.validate(this, value);
                    if(error != null)
                        fieldErrors.add(error);
                }
                if(fieldErrors.size() > 0)
                    errors.put(key, fieldErrors);
            });
        }
        return new ValidationResult(errors);
    }

    private Map<String[], GraphElement> matchKeys(String[] keys, GraphElement element){
        Map<String[], GraphElement> result = new HashMap<>();
        if(keys.length == 0){
            result.put(keys, element);
            return result;
        }
        if(element.isArray())
            element = element.object();
        if(element.isObject()){
            String currentKey = keys[0];
            String[] newKeys = new String[keys.length-1];
            System.arraycopy(keys, 1, newKeys, 0, newKeys.length);
            if(currentKey != null){
                matchKeys(newKeys, element.object().get(currentKey)).forEach((nk, actualValue) -> {
                    String[] actualKey = new String[nk.length + 1];
                    actualKey[0] = currentKey;
                    System.arraycopy(nk, 0, actualKey, 1, nk.length);
                    result.put(actualKey, actualValue);
                });
            }else{
                element.object().forEach((k, value) -> {
                    matchKeys(newKeys, value).forEach((nk, actualValue) -> {
                        String[] actualKey = new String[nk.length + 1];
                        actualKey[0] = k;
                        System.arraycopy(nk, 0, actualKey, 1, nk.length);
                        result.put(actualKey, actualValue);
                    });
                });
            }
        }
        return result;
    }

    private static Map<String[], List<ValidationRule>> getClassRules(Class<?> type){
        Map<String[], List<ValidationRule>> rules = new HashMap<>();
        if(type.isAnnotation())
            return rules;
        if(type.equals(String.class))
            return rules;
        if(type.equals(Timestamp.class) || type.equals(Date.class)){
            rules.put(new String[0], Collections.singletonList(new TimestampRule()));
            return rules;
        }
        if(type.equals(Boolean.class)){
            rules.put(new String[0], Collections.singletonList(new BooleanRule()));
            return rules;
        }
        if(type.equals(Integer.class)){
            rules.put(new String[0], Collections.singletonList(new IntegerRule()));
            return rules;
        }
        if(type.isEnum()){
            rules.put(new String[0], Collections.singletonList(new EnumRule((Class<? extends Enum<?>>) type)));
            return rules;
        }
        if(type.isArray()){
            getClassRules(type.getComponentType()).forEach((key, validators) -> {
                String[] actualKey = new String[key.length+1];
                actualKey[0] = "*";
                System.arraycopy(key, 0, actualKey, 1, key.length);
                rules.put(actualKey, validators);
            });
            return rules;
        }
        for(Field field : getFieldsRecursive(type)){
            String name = field.getName();
            getClassRules(field.getType()).forEach((key, validators) -> {
                String[] actualKey = new String[key.length+1];
                actualKey[0] = name;
                System.arraycopy(key, 0, actualKey, 1, key.length);
                rules.put(actualKey, validators);
            });
            field.setAccessible(true);
            Rule[] ruleAnnotations = field.getDeclaredAnnotationsByType(Rule.class);
            if(ruleAnnotations.length > 0){
                List<ValidationRule> r = new ArrayList<>();
                for(String source : ruleAnnotations[0].value()){
                    ValidationRule rule = Validator.makeRule(source);
                    if(rule != null)
                        r.add(rule);
                }
                if(r.size() > 0)
                    rules.put(new String[]{name}, r);
            }
        }
        return rules;
    }

    private static List<Field> getFieldsRecursive(Class<?> type){
        List<Field> fields;
        if(type.getSuperclass() != null && !type.getSuperclass().equals(Object.class))
            fields = getFieldsRecursive(type.getSuperclass());
        else
            fields = new ArrayList<>();
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        return fields;
    }

}
