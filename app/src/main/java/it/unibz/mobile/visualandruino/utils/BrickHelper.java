package it.unibz.mobile.visualandruino.utils;


import android.support.v4.util.Pair;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;

public class BrickHelper {

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

              if(currentBrick.getBrickType() == BrickTypes.INTERNAL) {

                if(((InternalBrick) currentBrick).getSubType().toString().startsWith("END")) {
                    /**
                     * Internal Command End
                     */
                    brickList.add(currentInternalSuperBrick);
                    insideCommandStructure = false;
                    currentInternalSuperBrick = null;

                } else {
                    /**
                     * Internal Command Start
                     */
                    insideCommandStructure = true;
                    currentInternalSuperBrick = (InternalBrick) currentBrick;
                }
            }

            /**
             * Add as SubBrick
             */
            else if(insideCommandStructure && currentInternalSuperBrick != null) {

                ArrayList<Brick> subBricks = currentInternalSuperBrick.getSubBricks();
                if(subBricks != null) {
                    subBricks.add(currentBrick);
                    currentInternalSuperBrick.setSubBricks(subBricks, currentInternalSuperBrick.getSubType());
                }

            }
           else {
                /**
                 * Regular Bricks
                 */
                brickList.add(pair.second);
            }

        }

        return brickList;

    }
}
