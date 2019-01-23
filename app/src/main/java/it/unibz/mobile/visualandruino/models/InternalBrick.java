package it.unibz.mobile.visualandruino.models;

import java.io.Serializable;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;

public class InternalBrick extends Brick implements Serializable {
    private ArrayList<Brick> subBricks;
    private InternalSubTypes subType;


    public InternalBrick(String name, ArrayList<Parameter> parameters, ArrayList<Brick> subBricks, InternalSubTypes subType) {
        super(name, BrickTypes.INTERNAL, parameters);
        this.subBricks = subBricks;
        this.subType = subType;
    }

    public ArrayList<Brick> getSubBricks() {
        return subBricks;
    }

    public InternalSubTypes getSubType() {
        return subType;
    }

    public void setSubBricks(ArrayList<Brick> subBricks, InternalSubTypes subType) {
        this.subBricks = subBricks;
        this.subType = subType;
    }
}
