package it.unibz.mobile.visualandruino.models;


import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

public class ArduinoCommandBrick extends Brick {
    private int commandId;


    public ArduinoCommandBrick(String name, int type, ArrayList<Value> parameters, int commandId)
    {
        super(name, BrickTypes.ARDUINO_COMMAND, type, parameters);
        this.commandId = commandId;
    }


    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }
}
