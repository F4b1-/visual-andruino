package it.unibz.mobile.visualandruino.utils;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import it.unibz.mobile.visualandruino.Constants;
import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.ComparatorTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;


public class BrickPersister {

    public static String translateSketchToJson(ArrayList<Brick> bricks) {
        Gson gson = new Gson();
        String json = gson.toJson(bricks);
        return json;
    }

    public static ArrayList<Brick> loadSketchFromJson(String sketch) {
        Gson gson = new Gson();
        final JsonArray data = new JsonParser().parse(sketch).getAsJsonArray();

        ArrayList<Brick> list = new ArrayList();
        for (JsonElement element : data) {
            String currentBrickType = ((JsonObject) element).get("brickType").getAsString();
            if(currentBrickType.equals(BrickTypes.ARDUINO_COMMAND.toString())) {
                list.add(gson.fromJson(element, ArduinoCommandBrick.class));
            } else if(currentBrickType.equals(BrickTypes.INTERNAL.toString())) {
                //TODO
            } else if(currentBrickType.equals(BrickTypes.ANDROID.toString())) {
                //TODO
            }
        }

        return list;
    }


    public static void writeJsonToFile(Context context, String folderName, String fileName, String fileContents) {
        try {

            String folder = context.getFilesDir().getAbsolutePath() + File.separator + folderName;

            File subFolder = new File(folder);

            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }

            FileOutputStream outputStream = new FileOutputStream(new File(subFolder, fileName));

            outputStream.write(fileContents.getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            Log.e("ERROR", e.toString());
        } catch (IOException e) {
            Log.e("ERROR", e.toString());
        }

    }

    public static String readJsonFile(Context context, String folderName, String fileName) {

        StringBuilder sb = new StringBuilder();

        try {
            String folder = context.getFilesDir().getAbsolutePath() + File.separator + folderName;

            File subFolder = new File(folder);

            FileInputStream outputStream = new FileInputStream(new File(subFolder, fileName));
            InputStreamReader isr = new InputStreamReader(outputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return sb.toString();

    }

    private static boolean fileExist(Context context, String folderName, String fname){
        File file = new File(context.getFilesDir() + File.separator + folderName + File.separator +  fname);
        boolean fileExists = file.exists();
        return fileExists;

    }

    public static boolean saveStandardSketch(Context context) {
        /*
        if(fileExist(context, Constants.SKETCHES_FOLDER, Constants.STANDARD_SKETCH)) {
            return false;
        }*/

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


        /**
         * adding bricks
         */
        bricks.add(item);
        bricks.add(item2);
        InternalBrick ifBrick = createIfBrick();
        if(ifBrick != null) {
            bricks.add(ifBrick);
        }


        writeJsonToFile(context, Constants.SKETCHES_FOLDER, Constants.STANDARD_SKETCH, translateSketchToJson(bricks));

        return true;
    }

private static InternalBrick createIfBrick() {
    /**
     * If
     */
    /*ArrayList<Parameter> arr=new ArrayList<Parameter>();
    BrickBuilder bbSub = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);
    Brick itemSub= bbSub.buildBrick();

    ArrayList<Brick> subList=new ArrayList<Brick>();
    subList.add(itemSub);*/


    ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
    Parameter valInternal=new Parameter();
    valInternal.setValue(String.valueOf((2)));
    arrInternal.add(valInternal);

    Parameter valInternalComp=new Parameter();
    valInternalComp.setValue(ComparatorTypes.GREATER.toString());
    arrInternal.add(valInternalComp);

    Parameter valInternalRef=new Parameter();
    valInternalRef.setValue(String.valueOf((3)));
    arrInternal.add(valInternalRef);

    BrickBuilder bb = new BrickBuilder("If", BrickTypes.INTERNAL, arrInternal);
    bb.setSubType(InternalSubTypes.IF);
    //bb.setSubBricks(subList);

    InternalBrick item= (InternalBrick) bb.buildBrick();
    return item;
}

}
