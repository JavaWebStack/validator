package org.javawebstack.validator.rule;

import org.javawebstack.graph.GraphElement;
import org.javawebstack.graph.GraphMapper;
import org.javawebstack.validator.Validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateRule implements ValidationRule {
    private DateFormat dateFormat;

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

    public String validate(Validator validator, GraphElement value) {

        return null;
    }
}
