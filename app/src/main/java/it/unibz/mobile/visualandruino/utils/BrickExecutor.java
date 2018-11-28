package it.unibz.mobile.visualandruino.utils;

import android.widget.Toast;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.ComparatorTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;


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
            if(((InternalBrick) currentBrick).getSubType() == InternalSubTypes.FOR) {
                int forLoopLimiter = Integer.parseInt(currentBrick.getParameters().get(0).getValue());
                for(int i=0; i<=forLoopLimiter; i++) {
                    executeBlocks(((InternalBrick) currentBrick).getSubBricks() , pinNumber);
                }
            }


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

    public void executeInternal(InternalBrick currentBrick, String pinNumber) {
        executeInternal(currentBrick, pinNumber, this);
    }

    public void executeInternal(InternalBrick currentBrick, String pinNumber, BrickExecutor brickExecutor) {
        /**
         * *** FOR ***
         */
        if(currentBrick.getSubType() == InternalSubTypes.FOR) {
            int referenceValue = Integer.parseInt(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            int forLoopLimiter = Integer.parseInt(currentBrick.getParameters().get(2).getValue());


            if(comparator == ComparatorTypes.GREATER && referenceValue < forLoopLimiter) {
                for(int i = referenceValue; i<forLoopLimiter; i++) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                }
            }

            if(comparator == ComparatorTypes.LESS && referenceValue > forLoopLimiter) {
                for(int i= referenceValue; i>forLoopLimiter; i--) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                }
            }
        }

        /**
         * *** WHILE ***
         */
        if(currentBrick.getSubType() == InternalSubTypes.FOR) {
            int referenceValue = Integer.parseInt(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            int loopLimiter = Integer.parseInt(currentBrick.getParameters().get(2).getValue());

            if(comparator == ComparatorTypes.GREATER) {
                while (referenceValue > loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                    referenceValue--;
                }
            }

            if(comparator == ComparatorTypes.LESS) {
                while (referenceValue < loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                    referenceValue++;
                }
            }

            if(comparator == ComparatorTypes.EQUALS) {
                while (referenceValue == loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                    referenceValue++;
                }
            }

            if(comparator == ComparatorTypes.NOTEQUALS) {
                while (referenceValue != loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                    referenceValue++;
                }
            }
        }


        /**
         * *** IF ***
         */
        if(currentBrick.getSubType() == InternalSubTypes.IF) {
            int referenceValue = Integer.parseInt(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            int secondValue = Integer.parseInt(currentBrick.getParameters().get(2).getValue());

            if(comparator == ComparatorTypes.GREATER) {
                if(referenceValue > secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                }
            }

            else if(comparator == ComparatorTypes.LESS) {
                if(referenceValue < secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                }
            }

            else if(comparator == ComparatorTypes.EQUALS) {
                if(referenceValue == secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                }
            }

            else if(comparator == ComparatorTypes.NOTEQUALS) {
                if(referenceValue != secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() , pinNumber);
                }
            }


            //TODO more of them
        }


    }
}
