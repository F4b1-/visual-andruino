package it.unibz.mobile.visualandruino.utils;

import android.widget.Toast;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickStatus;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.ComparatorTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;


//Builder Class
public class BrickExecutor {



    public void executeBrick(Brick currentBrick)
    {

        currentBrick.setBrickStatus(BrickStatus.Started);
        if(currentBrick.getBrickType() == BrickTypes.ARDUINO_COMMAND) {
            String command = "";
            command += ((ArduinoCommandBrick) currentBrick).getCommandId();

            for(Parameter value : currentBrick.getParameters()) {
                command +=" "+ value.getValue();
            }
            command += ";";
            command=command.replace("HIGH","1");
            command=command.replace("LOW","0");
            BrickCommunicator.getInstance().sendCommand(command);



        } else if(currentBrick.getBrickType() == BrickTypes.INTERNAL) {

            executeInternal((InternalBrick) currentBrick);/*
            // Execute subBricks recursively
            if(((InternalBrick) currentBrick).getSubType() == InternalSubTypes.FOR) {
                int forLoopLimiter = Integer.parseInt(currentBrick.getParameters().get(0).getValue());
                for(int i=0; i<=forLoopLimiter; i++) {
                    executeBlocks(((InternalBrick) currentBrick).getSubBricks() );
                }
            }*/


            // TODO Execute
        } else if(currentBrick.getBrickType() == BrickTypes.ANDROID) {
            //TODO Execute Android commands
        }

        currentBrick.setBrickStatus(BrickStatus.Finished);
    }

    public void executeBlocks(ArrayList<Brick> bricks ) {
        Brick currentBrick = bricks.get(0);

        executeBrick(currentBrick);

        //remove at the end and move on

        bricks.remove(0);
        if(bricks.size()>0)
        {
            executeBlocks(bricks);
        }
        currentBrick.setBrickStatus(BrickStatus.Waiting);


    }

    public void executeInternal(InternalBrick currentBrick) {
        executeInternal(currentBrick, this);
    }

    public void executeInternal(InternalBrick currentBrick,  BrickExecutor brickExecutor) {
        /**
         * *** FOR ***
         */
        if(currentBrick.getSubType() == InternalSubTypes.FOR) {
            int referenceValue = Integer.parseInt(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            int forLoopLimiter = Integer.parseInt(currentBrick.getParameters().get(2).getValue());


            if(comparator == ComparatorTypes.GREATER && referenceValue < forLoopLimiter) {
                for(int i = referenceValue; i<forLoopLimiter; i++) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                }
            }

            if(comparator == ComparatorTypes.LESS && referenceValue > forLoopLimiter) {
                for(int i= referenceValue; i>forLoopLimiter; i--) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
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
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                    referenceValue--;
                }
            }

            if(comparator == ComparatorTypes.LESS) {
                while (referenceValue < loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                    referenceValue++;
                }
            }

            if(comparator == ComparatorTypes.EQUALS) {
                while (referenceValue == loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                    referenceValue++;
                }
            }

            if(comparator == ComparatorTypes.NOTEQUALS) {
                while (referenceValue != loopLimiter) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
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
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                }
            }

            else if(comparator == ComparatorTypes.LESS) {
                if(referenceValue < secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                }
            }

            else if(comparator == ComparatorTypes.EQUALS) {
                if(referenceValue == secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                }
            }

            else if(comparator == ComparatorTypes.NOTEQUALS) {
                if(referenceValue != secondValue) {
                    brickExecutor.executeBlocks(currentBrick.getSubBricks() );
                }
            }


            //TODO more of them
        }


    }
}
