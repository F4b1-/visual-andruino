package it.unibz.mobile.visualandruino.utils;


import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

import static org.junit.Assert.assertEquals;

public class BrickPersisterTest {

    @Test
    public void persistSketchTest() throws Exception {
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        Parameter val=new Parameter();
        val.setValue(String.valueOf((2)));
        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        arr.add(val );
        BrickBuilder bb = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, 2 , arr);
        bb.setCommandId(3);
        Brick item= bb.buildBrick();

        Parameter val2=new Parameter();
        val2.setValue(String.valueOf((3)));
        ArrayList<Parameter> arr2=new ArrayList<Parameter>();
        arr2.add(val2);

        bb = new BrickBuilder("test2", BrickTypes.ARDUINO_COMMAND, 3 , arr2);
        bb.setCommandId(4);
        Brick item2= bb.buildBrick();

        bricks.add(item);
        bricks.add(item2);


        assertEquals(BrickPersister.translateSketchToJson(bricks), "[{\"commandId\":3,\"name\":\"test\",\"brickType\":\"ARDUINO_COMMAND\",\"type\":2,\"parameters\":[{\"value\":\"2\"}]},{\"commandId\":4,\"name\":\"test2\",\"brickType\":\"ARDUINO_COMMAND\",\"type\":3,\"parameters\":[{\"value\":\"3\"}]}]");
    }


    @Test
    public void loadSketchTest() throws Exception {
        ArrayList<Brick> bricks = new ArrayList<>();
        Parameter val=new Parameter();
        val.setValue(String.valueOf((2)));
        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        arr.add(val );
        BrickBuilder bb = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, 2 , arr);
        bb.setCommandId(3);
        Brick item= bb.buildBrick();

        Parameter val2=new Parameter();
        val2.setValue(String.valueOf((3)));
        ArrayList<Parameter> arr2=new ArrayList<Parameter>();
        arr2.add(val2);

        bb = new BrickBuilder("test2", BrickTypes.ARDUINO_COMMAND, 3 , arr2);
        bb.setCommandId(4);
        Brick item2= bb.buildBrick();

        bricks.add(item);
        bricks.add(item2);

        String jsonArr = "[{'commandId':3,'name':'test','brickType':'ARDUINO_COMMAND','type':2,'parameters':[{'value':'2'}]},{'commandId':4,'name':'test2','brickType':'ARDUINO_COMMAND','type':3,'parameters':[{'value':'3'}]}]";

        ArrayList<Brick> loadedArr = BrickPersister.loadSketchFromJson(jsonArr);
        assertThat(((ArduinoCommandBrick)bricks.get(0))).isEqualToComparingFieldByFieldRecursively(loadedArr.get(0));
        assertThat(((ArduinoCommandBrick)bricks.get(1))).isEqualToComparingFieldByFieldRecursively(loadedArr.get(1));


    }


}
