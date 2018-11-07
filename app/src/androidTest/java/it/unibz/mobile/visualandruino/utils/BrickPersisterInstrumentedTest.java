package it.unibz.mobile.visualandruino.utils;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
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



}
