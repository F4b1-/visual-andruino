package it.unibz.mobile.visualandruino.models;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;

public class InternalBrick extends Brick {
    private ArrayList<Brick> subBricks;
    private InternalSubTypes subType;

    public InternalBrick(String name, int type, ArrayList<Parameter> parameters, ArrayList<Brick> subBricks) {
        super(name, BrickTypes.ARDUINO_COMMAND, type, parameters);
        this.subBricks = subBricks;
    }

    public ArrayList<Brick> getSubBricks() {
        return subBricks;
    }

    public InternalSubTypes getSubType() {
        return subType;
    }

    public void setSubType(InternalSubTypes subType) {
        this.subType = subType;
    }

    public void setSubBricks(ArrayList<Brick> subBricks, InternalSubTypes subType) {
        this.subBricks = subBricks;
        this.subType = subType;
    }
}
