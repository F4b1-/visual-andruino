package it.unibz.mobile.visualandruino.utils;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import it.unibz.mobile.visualandruino.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BrickPersisterInstrumentedTest {

    private Context instrumentationCtx;

    @Before
    public void setup() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();

    }



    @Test
    public void writeReadJSONFileTest() throws Exception {
        String testContent = new String("{\"value\": \"test\"}");
        BrickPersister.writeJsonToFile(instrumentationCtx, "testFolder","test", testContent);

        assertEquals(testContent, BrickPersister.readJsonFile(instrumentationCtx, "testFolder", "test"));

    }

    private static void removeFile(Context context, String folderName, String fname){

        File file = new File(context.getFilesDir() + File.separator + folderName + File.separator +  fname);
        if(file.exists()) {
            file.delete();
        }

    }

    @Test
    public void saveStandardSketchCreateTest() throws Exception {
        removeFile(instrumentationCtx, Constants.SKETCHES_FOLDER, Constants.STANDARD_SKETCH);
        BrickPersister.saveStandardSketch(instrumentationCtx);
        //String standardContent = "[{\"commandId\":3,\"brickType\":\"ARDUINO_COMMAND\",\"name\":\"DigitalWrite\",\"parameters\":[{\"parameterName\":\"PinNumber\",\"value\":\"5\"},{\"allowedValues\":[\"HIGH\",\"LOW\"],\"parameterName\":\"WriteValue\",\"value\":\"HIGH\"}]},{\"commandId\":2,\"brickType\":\"ARDUINO_COMMAND\",\"name\":\"AnalogWrite\",\"parameters\":[{\"parameterName\":\"PinNumber\",\"value\":\"5\"},{\"parameterName\":\"analogWrite\",\"value\":\"0\"}]}]";
        assertNotNull(BrickPersister.readJsonFile(instrumentationCtx, Constants.SKETCHES_FOLDER,
                Constants.STANDARD_SKETCH));
        //assertEquals(standardContent, BrickPersister.readJsonFile(instrumentationCtx, Constants.SKETCHES_FOLDER,
        //        Constants.STANDARD_SKETCH));

    }

    /*
    @Test
    public void saveStandardSketchExistsTest() throws Exception {
        removeFile(instrumentationCtx, Constants.SKETCHES_FOLDER, Constants.STANDARD_SKETCH);
        saveStandardSketchCreateTest();
        boolean createdNewStandardFile = BrickPersister.saveStandardSketch(instrumentationCtx);
        //String standardContent = "[{\"commandId\":3,\"brickStatus\":\"Waiting\",\"brickType\":\"ARDUINO_COMMAND\",\"brickUiId\":0,\"name\":\"DigitalWrite\",\"parameters\":[{\"parameterName\":\"PinNumber\",\"value\":\"5\"},{\"allowedValues\":[\"HIGH\",\"LOW\"],\"parameterName\":\"WriteValue\",\"value\":\"HIGH\"}]},{\"commandId\":2,\"brickType\":\"ARDUINO_COMMAND\",\"name\":\"AnalogWrite\",\"parameters\":[{\"parameterName\":\"PinNumber\",\"value\":\"5\"},{\"parameterName\":\"analogWrite\",\"value\":\"0\"}]}]";
        assertFalse(createdNewStandardFile);
        assertNotNull(BrickPersister.readJsonFile(instrumentationCtx, Constants.SKETCHES_FOLDER,
                Constants.STANDARD_SKETCH));
        //assertEquals(standardContent, BrickPersister.readJsonFile(instrumentationCtx, Constants.SKETCHES_FOLDER,
        //        Constants.STANDARD_SKETCH));

    }*/



}
