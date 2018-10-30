package it.unibz.mobile.visualandruino.utils;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.Value;


//Builder Class
public class BrickBuilder{

    // required parameters
    private String name;
    private BrickTypes brickType;
    private int type;
    private ArrayList<Value> parameters;

    // optional parameters
    private int commandId;

    public BrickBuilder(String name, BrickTypes brickType, int type, ArrayList<Value> parameters){
        this.name = name;
        this.brickType = brickType;
        this.type = type;
        this.parameters = parameters;
    }

    public BrickBuilder setCommandId(int commandId) {
        this.commandId =commandId;
        return this;
    }


    public Brick buildBrick(){

        //TODO not null check for parameters

        if(this.brickType== null){
            return null;
        }
        if(this.brickType == BrickTypes.ARDUINO_COMMAND){
            return new ArduinoCommandBrick(this.name, this.type,parameters, this.commandId);

        } else if(this.brickType == BrickTypes.INTERNAL){
            //TODO

        } else if(this.brickType == BrickTypes.ANDROID){
            //TODO
        }

        return null;

    }

}
