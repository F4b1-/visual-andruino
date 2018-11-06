package it.unibz.mobile.visualandruino.models;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

public class Brick {
    private String name;
    private BrickTypes brickType;
    private int type;

    private ArrayList<Value> parameters;

    public Brick(String name, BrickTypes brickType, int type, ArrayList<Value> parameters)
    {
        this.name = name;
        this.brickType = brickType;
        this.type = type;
        this.parameters = parameters;
    }



    public String getName() {
        return name;
    }

    public BrickTypes getBrickType() {
        return brickType;
    }

    public ArrayList<Value> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrickType(BrickTypes type) {
        this.brickType = type;
    }

    public void setParameters(ArrayList<Value> parameters) {
        this.parameters = parameters;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
