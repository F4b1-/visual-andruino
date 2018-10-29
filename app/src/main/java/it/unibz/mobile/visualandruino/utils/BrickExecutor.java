package it.unibz.mobile.visualandruino.utils;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Value;


//Builder Class
public class BrickExecutor {


    void executeBlocks(ArrayList<Brick> bricks) {
        Brick currentBrick = bricks.get(0);


        if(currentBrick.getBrickType() == BrickTypes.ARDUINO_COMMAND) {
            String command = "";
            command += ((ArduinoCommandBrick) currentBrick).getCommandId();

            for(Value value : currentBrick.getParameters()) {
                command += value.getValue();
            }

            BrickCommunicator.getInstance().sendCommand(command);

        } else if(currentBrick.getBrickType() == BrickTypes.INTERNAL) {
            // Execute subBricks recursively
            executeBlocks(((InternalBrick) currentBrick).getSubBricks());

            // TODO Execute
        } else if(currentBrick.getBrickType() == BrickTypes.ANDROID) {
            //TODO Execute Android commands
        }


        //remove at the end and move on
        bricks.remove(0);
        executeBlocks(bricks);
    }
}
