package it.unibz.mobile.visualandruino.utils;


import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

public class BrickHelper {

    private Map<String, Integer> setVariables = new HashMap();

    // static variable single_instance of type Singleton
    private static BrickHelper single_instance = null;

    // static method to create instance of Singleton class
    public static BrickHelper getInstance()
    {
        if (single_instance == null) {
            single_instance = new BrickHelper();

        }

        return single_instance;
    }

    /**
     * Translates the ui Bricks that know only one layer to the backend Bricks that know sub bricks.
     * In practice this means that all of the bricks within the start and end brick of an internal structure
     * (if/while/for) are stored as subBricks of the internal Brick.
     * Example:
     * UI: IF START - Brick 1 - Brick 2 - IF END
     * Backend: IF Brick {SubBricks: Brick 1, Brick 2}
     * @param uiBricks
     * @return the the Backend ArrayList
     */
    public static ArrayList<Brick> translateUiBricksToBackendBricks(ArrayList<Pair<Long, Brick>> uiBricks) {
        ArrayList<Brick> brickList = new ArrayList<>();
        boolean insideCommandStructure = false;
        InternalBrick currentInternalSuperBrick = null;

        for(Pair<Long, Brick> pair : uiBricks) {
            Brick currentBrick = pair.second;
            currentBrick.setBrickUiId(pair.first);

              if(currentBrick.getBrickType() == BrickTypes.INTERNAL) {

                if(((InternalBrick) currentBrick).getSubType().toString().startsWith("END")) {
                    /**
                     * Internal Command End
                     */
                    if(currentInternalSuperBrick==null)
                    {
                        throw new BrickIncorrectOrderException(((InternalBrick) currentBrick).getSubType().toString()+" without Start founded" );
                    }

                    if(!("END"+currentInternalSuperBrick.getSubType().toString())
                            .equals(((InternalBrick) currentBrick).getSubType().toString()))
                    {
                        throw new BrickIncorrectOrderException(((InternalBrick) currentBrick).getSubType().toString()+" without Start founded" );
                    }

                    if(insideCommandStructure)
                    {
                        brickList.add(currentInternalSuperBrick);
                        insideCommandStructure = false;
                        currentInternalSuperBrick = null;
                    }else
                    {
                        throw new BrickIncorrectOrderException(
                                "Brick "+ ((InternalBrick) currentBrick).getSubType().toString()+ " founded before start internal brick" );
                    }

                } else {
                    /**
                     * Internal Command Start
                     */
                    if(currentInternalSuperBrick!=null)
                    {
                        throw new BrickIncorrectOrderException("Internal brick under Internal brick is not supported" );
                    }
                    insideCommandStructure = true;
                    ((InternalBrick) currentBrick).setSubBricks(new ArrayList<Brick>(), ((InternalBrick) currentBrick).getSubType());
                    currentInternalSuperBrick = (InternalBrick) currentBrick;
                }
            }

            /**
             * Add as SubBrick
             */
            else if(insideCommandStructure && currentInternalSuperBrick != null) {

                ArrayList<Brick> subBricks = currentInternalSuperBrick.getSubBricks();
                if(subBricks == null)
                {
                    subBricks= new ArrayList<Brick>();
                }
                subBricks.add(currentBrick);
                currentInternalSuperBrick.setSubBricks(subBricks, currentInternalSuperBrick.getSubType());


            }
           else {
                /**
                 * Regular Bricks
                 */
                brickList.add(currentBrick);
            }

        }
        if(insideCommandStructure)
        {
            throw new BrickIncorrectOrderException("Started Internal Brick with out end" );
        }

        return brickList;

    }

    public String getCurrentVariablesFormatted() {
        StringBuilder sb = new StringBuilder();
        String header = "<br>Current Variables<br>";

        sb.append(header);
        Iterator it = setVariables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            sb.append("&#8226;" +  pair.getKey() + "=" + pair.getValue() + "<br/>");
            it.remove();
        }

        return sb.toString();
    }

    public void setSetVariable(String variableName, Integer variableValue) {
        setVariables.put(variableName, variableValue);
    }

    public Integer getSetVariable(String variableName) {
        Integer variableValue = null;
        if(setVariables.containsKey(variableName)) {
            variableValue = setVariables.get(variableName);
        }

        return variableValue;
    }


    public static ArrayList<Brick> copyBricks(ArrayList<Brick> list) {
        ArrayList<Brick> copy = new ArrayList<Brick>(list.size());
        for (Brick item : list) copy.add(item);
        return copy;
    }


}
