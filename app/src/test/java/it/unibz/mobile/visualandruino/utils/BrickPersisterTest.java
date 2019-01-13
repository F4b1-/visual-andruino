package it.unibz.mobile.visualandruino.utils;


import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickStatus;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;

import static org.junit.Assert.assertEquals;

public class BrickPersisterTest {

    @Test
    public void persistSketchTest() throws Exception {
        ArrayList<Brick> bricks = new ArrayList<Brick>();

        /**
         * DigitalWrite
         */
        Parameter val1=new Parameter();
        val1.setParameterName("PinNumber");
        val1.setValue(String.valueOf(("5")));
        Parameter val2=new Parameter();
        ArrayList<String> allowedValues = new ArrayList<String>();
        allowedValues.add("HIGH");
        allowedValues.add("LOW");
        val2.setAllowedValues(allowedValues);
        val2.setParameterName("WriteValue");
        val2.setValue(String.valueOf(("HIGH")));

        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        arr.add(val1);
        arr.add(val2);
        BrickBuilder bb = new BrickBuilder("DigitalWrite", BrickTypes.ARDUINO_COMMAND , arr);
        bb.setCommandId(3);
        Brick item= bb.buildBrick();

        /**
         * AnalogWrite
         */
        Parameter analogVal2=new Parameter();
        analogVal2.setParameterName("analogWrite");
        analogVal2.setValue(String.valueOf((0)));
        ArrayList<Parameter> arrAnalog=new ArrayList<Parameter>();
        arrAnalog.add(val1);
        arrAnalog.add(analogVal2);
        bb = new BrickBuilder("AnalogWrite", BrickTypes.ARDUINO_COMMAND , arrAnalog);
        bb.setCommandId(2);
        Brick item2= bb.buildBrick();

        bricks.add(item);
        bricks.add(item2);


        assertEquals("[{\"commandId\":3,\"name\":\"DigitalWrite\",\"brickType\":\"ARDUINO_COMMAND\",\"brickStatus\":\"Waiting\",\"brickUiId\":0,\"parameters\":[{\"parameterName\":\"PinNumber\",\"value\":\"5\"},{\"parameterName\":\"WriteValue\",\"value\":\"HIGH\",\"allowedValues\":[\"HIGH\",\"LOW\"]}]},{\"commandId\":2,\"name\":\"AnalogWrite\",\"brickType\":\"ARDUINO_COMMAND\",\"brickStatus\":\"Waiting\",\"brickUiId\":0,\"parameters\":[{\"parameterName\":\"PinNumber\",\"value\":\"5\"},{\"parameterName\":\"analogWrite\",\"value\":\"0\"}]}]", BrickPersister.translateSketchToJson(bricks));
    }


    @Test
    public void loadSketchTest() throws Exception {
        ArrayList<Brick> bricks = new ArrayList<>();
        Parameter val=new Parameter();
        val.setValue(String.valueOf((2)));
        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        arr.add(val );
        BrickBuilder bb = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);

        bb.setCommandId(3);


        Brick item= bb.buildBrick();


        Parameter val2=new Parameter();
        val2.setValue(String.valueOf((3)));
        ArrayList<Parameter> arr2=new ArrayList<Parameter>();
        arr2.add(val2);

        bb = new BrickBuilder("test2", BrickTypes.ARDUINO_COMMAND, arr2);
        bb.setCommandId(4);
        Brick item2= bb.buildBrick();


        bricks.add(item);
        bricks.add(item2);

        String jsonArr = "[{'commandId':3,'name':'test', 'brickStatus':'Waiting', 'brickType':'ARDUINO_COMMAND','type':2,'parameters':[{'value':'2'}]},{'commandId':4,'name':'test2','brickType':'ARDUINO_COMMAND',  'brickStatus':'Waiting','type':3,'parameters':[{'value':'3'}]}]";

        ArrayList<Brick> loadedArr = BrickPersister.loadSketchFromJson(jsonArr);

        assertThat(((ArduinoCommandBrick)bricks.get(0))).isEqualToComparingFieldByFieldRecursively(loadedArr.get(0));
        assertThat(((ArduinoCommandBrick)bricks.get(1))).isEqualToComparingFieldByFieldRecursively(loadedArr.get(1));


    }



}
