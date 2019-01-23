package it.unibz.mobile.visualandruino.models;

import java.io.Serializable;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.enums.BrickStatus;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

public class Brick implements Serializable {
    private String name;
    private BrickTypes brickType;
    private BrickStatus brickStatus;
    private long brickUiId;


    private ArrayList<Parameter> parameters;



    public Brick(String name, BrickTypes brickType, ArrayList<Parameter> parameters)
    {
        this.name = name;
        this.brickType = brickType;
        this.parameters = parameters;
        this.brickStatus= BrickStatus.Waiting;
    }


    public String getParametersText() {
        String params="";
        for ( int i=0; i< parameters.size(); i++ )
        {
            params=params+  parameters.get(i).getParameterName()+"="+parameters.get(i).getValue()+";";
        }

        return params;
    }





    public String getName() {
        return name;
    }

    public BrickTypes getBrickType() {
        return brickType;
    }

    public BrickStatus getBrickStatus() {
        return brickStatus;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrickType(BrickTypes type) {
        this.brickType = type;
    }

    public void setBrickStatus(BrickStatus status) {
        this.brickStatus = status;
    }

    public long getBrickUiId() {
        return brickUiId;
    }

    public void setBrickUiId(long brickUiId) {
        this.brickUiId = brickUiId;
    }
}

