package it.unibz.mobile.visualandruino.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Parameter implements Serializable {
    private String parameterName;
    private String value;
    ArrayList<String> allowedValues;

    public Parameter() {

    }


    public Parameter(String name, String val)
    {
        parameterName=name;
        value=val;
        allowedValues= new  ArrayList<String>();
    }
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

    public void addToAllowedValues(ArrayList<String> valuesToBeAdded) {
        valuesToBeAdded.addAll((List<String>) this.allowedValues.clone());
        this.allowedValues = valuesToBeAdded;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
