package it.unibz.mobile.visualandruino.utils;


import android.support.v4.util.Pair;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;
import static org.assertj.core.api.Assertions.assertThat;


public class BrickHelperTest {

    @Test
    public void persistSketchTest() throws Exception {
        ArrayList<Pair<Long, Brick>> uiBricks = new ArrayList<>();

        Brick startIfBrick = new InternalBrick("startIf", null, new ArrayList<Brick>(), InternalSubTypes.IF);
        startIfBrick.setBrickType(BrickTypes.INTERNAL);
        Pair<Long, Brick> startPair = new Pair<>(new Long(1), startIfBrick);
        uiBricks.add(startPair);

        Brick arduinoCommandBrick1 = new ArduinoCommandBrick("sub1", null, 1);
        arduinoCommandBrick1.setBrickType(BrickTypes.ARDUINO_COMMAND);
        Pair<Long, Brick> commandPair1 = new Pair<>(new Long(2), arduinoCommandBrick1);
        uiBricks.add(commandPair1);

        Brick arduinoCommandBrick2 = new ArduinoCommandBrick("sub2", null, 1);
        arduinoCommandBrick2.setBrickType(BrickTypes.ARDUINO_COMMAND);
        Pair<Long, Brick> commandPair2 = new Pair<>(new Long(3), arduinoCommandBrick2);
        uiBricks.add(commandPair2);

        Brick endIfBrick = new InternalBrick("endIf", null, null, InternalSubTypes.ENDIF);
        endIfBrick.setBrickType(BrickTypes.INTERNAL);
        Pair<Long, Brick> endPair = new Pair<>(new Long(4), endIfBrick);
        uiBricks.add(endPair);


        ArrayList<Brick> subBricks = new ArrayList<Brick>();
        subBricks.add(arduinoCommandBrick1);
        subBricks.add(arduinoCommandBrick2);
        Brick backendIfBrick = new InternalBrick("backendIf", null, new ArrayList<Brick>(), InternalSubTypes.IF);
        backendIfBrick.setBrickType(BrickTypes.INTERNAL);
        ((InternalBrick)backendIfBrick).setSubBricks(subBricks, InternalSubTypes.IF);


        Brick actualSubBrick1 = ((InternalBrick)BrickHelper.getInstance().translateUiBricksToBackendBricks(uiBricks).get(0)).getSubBricks().get(0);
        Brick actualSubBrick2 = ((InternalBrick)BrickHelper.getInstance().translateUiBricksToBackendBricks(uiBricks).get(0)).getSubBricks().get(1);

        assertThat((arduinoCommandBrick1)).isEqualToComparingFieldByFieldRecursively(actualSubBrick1);
        assertThat((arduinoCommandBrick2)).isEqualToComparingFieldByFieldRecursively(actualSubBrick2);

    }





}
