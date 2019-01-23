package it.unibz.mobile.visualandruino.utils;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.Constants;
import it.unibz.mobile.visualandruino.ListFragment;
import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickStatus;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.ComparatorTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;


//Builder Class
public class BrickExecutor {


    public void executeBrick(Brick currentBrick, ListFragment fragment, boolean debug) {



        if(debug)
        {
            fragment.setBrickStatus(currentBrick.getBrickUiId(), BrickStatus.Started);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        if (currentBrick.getBrickType() == BrickTypes.ARDUINO_COMMAND) {
            String command = "";
            command += ((ArduinoCommandBrick) currentBrick).getCommandId();

            for (Parameter value : currentBrick.getParameters()) {
                command += " " + value.getValue();
            }
            command += ";";
            command = command.replace("HIGH", "1");
            command = command.replace("LOW", "0");

            if (BrickCommunicator.getInstance().getmSmoothBluetooth() != null) {
                BrickCommunicator.getInstance().sendCommand(command);
            }
            if(debug)
                fragment.printDebbug("send command "+ currentBrick.getName());

        } else if (currentBrick.getBrickType() == BrickTypes.INTERNAL) {

            executeInternal((InternalBrick) currentBrick, fragment, debug);


        } else if (currentBrick.getBrickType() == BrickTypes.ANDROID) {
            //TODO Execute Android commands
        }


        if(debug) {
            //reset final brick
            fragment.setBrickStatus(currentBrick.getBrickUiId(), BrickStatus.Waiting);
        }



    }


    public void executeBlocks(ArrayList<Brick> bricks, ListFragment fragment, boolean debug) {

        Brick currentBrick = bricks.get(0);


        executeBrick(currentBrick, fragment, debug);


        bricks.remove(0);
        if (bricks.size() > 0) {
            executeBlocks(bricks, fragment, debug);
        }


    }



    public Integer lookUpVariableValue(String parameter) {
        Integer variableValue = null;
        variableValue = BrickHelper.getInstance().getSetVariable(parameter);
        try {
            if(variableValue == null) {
                variableValue = Integer.valueOf(parameter);
            }
        } catch(Exception e) {
        }

        return variableValue;
    }

    public boolean allParametersSet(Integer referenceValue, ComparatorTypes comparator, Integer firstValue) {
        return (referenceValue != null && comparator != null && firstValue != null);
    }

    public void executeInternal(InternalBrick currentBrick,  ListFragment fragment, boolean debug) {
         executeInternal(currentBrick,fragment, debug, this);
    }

    public void executeInternal(InternalBrick currentBrick,  ListFragment fragment, final BrickExecutor brickExecutor, boolean debug) {
        executeInternal(currentBrick,fragment, debug, brickExecutor);
    }

    public void executeInternal(final InternalBrick currentBrick, ListFragment fragment, boolean debug, final BrickExecutor brickExecutor) {

        /**
         * *** FOR ***
         */
        if (currentBrick.getSubType() == InternalSubTypes.FOR) {
            Integer referenceValue = lookUpVariableValue(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            Integer forLoopLimiter = Integer.parseInt(currentBrick.getParameters().get(2).getValue());
            if (allParametersSet(referenceValue, comparator, forLoopLimiter)) {

                if (comparator == ComparatorTypes.GREATER && referenceValue < forLoopLimiter) {
                    printDebbug(fragment,debug,"For Start");
                    for (int i = referenceValue; i < forLoopLimiter; i++) {
                        printDebbug(fragment,debug,"For "+i+"<"+forLoopLimiter );

                        ArrayList<Brick> subBricks=BrickHelper.copyBricks(currentBrick.getSubBricks());
                        brickExecutor.executeBlocks(subBricks,fragment,debug);
                    }
                    printDebbug(fragment,debug,"For End");
                }

                if (comparator == ComparatorTypes.LESS && referenceValue > forLoopLimiter) {
                    for (int i = referenceValue; i > forLoopLimiter; i--) {
                        printDebbug(fragment,debug,"For "+i+">"+forLoopLimiter );
                        ArrayList<Brick> subBricks=BrickHelper.copyBricks(currentBrick.getSubBricks());
                        brickExecutor.executeBlocks(subBricks,fragment,debug);
                    }
                }
            }
        }

        /**
         * *** WHILE ***
         */
        if (currentBrick.getSubType() == InternalSubTypes.WHILE) {
            Integer referenceValue = lookUpVariableValue(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            int loopLimiter = Integer.parseInt(currentBrick.getParameters().get(2).getValue());

            if (allParametersSet(referenceValue, comparator, loopLimiter)) {
                if (comparator == ComparatorTypes.GREATER) {
                    while (referenceValue > loopLimiter) {
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                        referenceValue--;
                    }
                }

                if (comparator == ComparatorTypes.LESS) {
                    while (referenceValue < loopLimiter) {
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                        referenceValue++;
                    }
                }

                if (comparator == ComparatorTypes.EQUALS) {
                    while (referenceValue == loopLimiter) {
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                        referenceValue++;
                    }
                }

                if (comparator == ComparatorTypes.NOTEQUALS) {
                    while (referenceValue != loopLimiter) {
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                        referenceValue++;
                    }
                }
            }
        }


        /**
         * *** IF ***
         */
        if (currentBrick.getSubType() == InternalSubTypes.IF) {

            Integer referenceValue = lookUpVariableValue(currentBrick.getParameters().get(0).getValue());
            ComparatorTypes comparator = ComparatorTypes.valueOf(currentBrick.getParameters().get(1).getValue());
            int secondValue = Integer.parseInt(currentBrick.getParameters().get(2).getValue());
            if (allParametersSet(referenceValue, comparator, secondValue)) {
                if (comparator == ComparatorTypes.GREATER) {
                    if (referenceValue > secondValue) {
                        printDebbug(fragment,debug,"If: true");
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                    }else {
                        printDebbug(fragment,debug,"If: false");
                    }
                } else if (comparator == ComparatorTypes.LESS) {
                    if (referenceValue < secondValue) {
                        printDebbug(fragment,debug,"If: true");
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                    }else {
                        printDebbug(fragment,debug,"If: false");
                    }
                } else if (comparator == ComparatorTypes.EQUALS) {
                    if (referenceValue == secondValue) {
                        printDebbug(fragment,debug,"If: true");
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                    }else {
                        printDebbug(fragment,debug,"If: false");
                    }
                } else if (comparator == ComparatorTypes.NOTEQUALS) {
                    if (referenceValue != secondValue) {
                        printDebbug(fragment,debug,"If: true");
                        brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
                    }else {
                        printDebbug(fragment,debug,"If: false");
                    }
                }
            }



        }

        /**
         * *** ASSIGN VARIABLE  ***
         */
        if (currentBrick.getSubType() == InternalSubTypes.VARIABLE && currentBrick.getSubBricks() != null && !currentBrick.getSubBricks().isEmpty()) {
            BrickCommunicator.getInstance().setAwaitingReturn(true);


            brickExecutor.executeBlocks(currentBrick.getSubBricks(),fragment,debug);
            awaitReturn(0);

            Parameter variableNameParam = currentBrick.getParameters().get(0);
            if (variableNameParam != null) {
                String variableName = variableNameParam.getValue();
                int returnedValue = BrickCommunicator.getInstance().getCurrentReturnValue();

                BrickHelper.getInstance().setSetVariable(variableName, returnedValue);

            }


        }
        //TODO more of them



    }
    public void printDebbug(ListFragment fragment, boolean debug, String message)
    {
        if(debug)
            fragment.printDebbug(message);
    }

    public void awaitReturn(int retries) {

        if (BrickCommunicator.getInstance().isAwaitingReturn() && retries < Constants.RETURN_VALUE_RETRIES) {

            try {
                Thread.sleep(200);
                awaitReturn(retries + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}
