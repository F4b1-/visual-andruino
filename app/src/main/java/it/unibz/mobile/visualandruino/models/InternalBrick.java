package it.unibz.mobile.visualandruino.models;

import java.util.ArrayList;

public class InternalBrick {
    private ArrayList<Brick> subBricks;
    private InternalSubTypes subType;

    public InternalBrick(ArrayList<Brick> subBricks) {
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
