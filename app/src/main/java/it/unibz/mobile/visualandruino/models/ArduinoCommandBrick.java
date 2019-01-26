package it.unibz.mobile.visualandruino.models;


import java.io.Serializable;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

public class ArduinoCommandBrick extends Brick implements Serializable {
    private int commandId;


    public ArduinoCommandBrick(String name, ArrayList<Parameter> parameters, int commandId)
    {
        super(name, BrickTypes.ARDUINO_COMMAND, parameters);
        this.commandId = commandId;

    }


    public int getCommandId() {
        return commandId;
    }

}
