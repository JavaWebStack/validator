package org.javawebstack.validator.rule;

import org.javawebstack.abstractdata.AbstractElement;
import org.javawebstack.validator.ValidationContext;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateRule implements ValidationRule {

    private final DateFormat dateFormat;

    public DateRule(DateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    public DateRule(String[] params){
        switch (params.length > 0 ? params[0] : "datetime"){
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
        if(value == null || value.isNull())
            return null;
        if(!value.isString())
            return "Not a valid date";
        try {
            dateFormat.parse(value.string());
        } catch (Exception ex) {
            return "Not a valid date";
        }
        return null;
    }
}
