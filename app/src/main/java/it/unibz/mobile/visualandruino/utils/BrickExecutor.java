package it.unibz.mobile.visualandruino.utils;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;


//Builder Class
public class BrickExecutor {



    public void executeBrick(Brick currentBrick, String pinNumber)
    {
        if(currentBrick.getBrickType() == BrickTypes.ARDUINO_COMMAND) {
            String command = "";
            command += ((ArduinoCommandBrick) currentBrick).getCommandId();
            command += " "+pinNumber+ " ";

            for(Parameter value : currentBrick.getParameters()) {
                command += value.getValue();
            }
            command += ";";
            BrickCommunicator.getInstance().sendCommand(command);

        } else if(currentBrick.getBrickType() == BrickTypes.INTERNAL) {
            // Execute subBricks recursively
            executeBlocks(((InternalBrick) currentBrick).getSubBricks() , pinNumber);

            // TODO Execute
        } else if(currentBrick.getBrickType() == BrickTypes.ANDROID) {
            //TODO Execute Android commands
        }
    }

    public void executeBlocks(ArrayList<Brick> bricks, String pinNumber ) {
        Brick currentBrick = bricks.get(0);

        executeBrick(currentBrick, pinNumber);

        //remove at the end and move on

        bricks.remove(0);
        if(bricks.size()>0)
        {
            executeBlocks(bricks, pinNumber);
        }


    }
}
