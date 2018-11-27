package it.unibz.mobile.visualandruino.models;


import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private String parameterName;
    private String value;
    ArrayList<String> allowedValues;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(ArrayList<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
