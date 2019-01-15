package it.unibz.mobile.visualandruino.utils;

import android.content.Context;
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
        ArrayList<Brick> list = new ArrayList();

        if(sketch != null && !sketch.isEmpty()) {
            final JsonArray data = new JsonParser().parse(sketch).getAsJsonArray();

            for (JsonElement element : data) {
                String currentBrickType = ((JsonObject) element).get("brickType").getAsString();
                if(currentBrickType.equals(BrickTypes.ARDUINO_COMMAND.toString())) {
                    list.add(gson.fromJson(element, ArduinoCommandBrick.class));
                } else if(currentBrickType.equals(BrickTypes.INTERNAL.toString())) {
                    InternalBrick internal = gson.fromJson(element, InternalBrick.class);

                    if(((JsonObject) element).get("subBricks")!=null)
                    {
                        final JsonArray dataSubBricks  = ((JsonObject) element).get("subBricks").getAsJsonArray();

                        ArrayList<Brick> internalSubBricks = new ArrayList<>();

                        for (JsonElement elementSubBrick : dataSubBricks) {
                            internalSubBricks.add(gson.fromJson(elementSubBrick, ArduinoCommandBrick.class));
                        }
                        internal.setSubBricks(internalSubBricks, internal.getSubType());

                    }


                    list.add(internal);




                } else if(currentBrickType.equals(BrickTypes.ANDROID.toString())) {
                    //TODO
                }
            }
        }
/*
        ArrayList<Brick> listCasted = new ArrayList();
        for(Brick superBrick : list) {
            if(superBrick.getBrickType() == BrickTypes.INTERNAL) {
                if(!((InternalBrick)superBrick).getSubBricks().isEmpty()) {
                    for(Brick subBrick : ((InternalBrick)superBrick).getSubBricks()) {
                        subBrick
                    }
                }
            }
        }*/

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

    public static String[] getInternalSketches(Context context, String folderName) {
        ArrayList<String> internalSketches = new ArrayList<>();


            String folder = context.getFilesDir().getAbsolutePath() + File.separator + folderName;

            File subFolder = new File(folder);

            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }

            File[] files = subFolder.listFiles();

            for (int i = 0; i < files.length; i++)
            {
                internalSketches.add(files[i].getName());
                
            }

            return internalSketches.toArray(new String[0]);
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

    public static Brick createToneBrick()
    {

        Parameter val1=new Parameter("PinNumber",String.valueOf(("5")));
        ArrayList<String> allowedValuesV1 = BrickPersister.getArrValues(5,53,1);
        val1.setAllowedValues(allowedValuesV1);


        Parameter analogVal2=new Parameter("Tone",String.valueOf((0)));
        analogVal2.setAllowedValues(BrickPersister.getArrValues(0,1000,10));


        ArrayList<Parameter> arrTone=new ArrayList<Parameter>();
        arrTone.add(val1);
        arrTone.add(analogVal2);

        BrickBuilder bb = new BrickBuilder("Tone", BrickTypes.ARDUINO_COMMAND , arrTone);
        bb.setCommandId(4);
        return bb.buildBrick();
    }
    public static Brick createDigitalWriteBrick()
    {

        Parameter val1=new Parameter("PinNumber",String.valueOf(("5")));
        ArrayList<String> allowedValuesV1 = BrickPersister.getArrValues(5,53,1);
        val1.setAllowedValues(allowedValuesV1);


        Parameter val2=new Parameter("WriteValue",String.valueOf(("HIGH")));
        ArrayList<String> allowedValues = new ArrayList<String>();
        allowedValues.add("HIGH");
        allowedValues.add("LOW");
        val2.setAllowedValues(allowedValues);
        val2.setParameterName("WriteValue");


        ArrayList<Parameter> arrParameters=new ArrayList<Parameter>();
        arrParameters.add(val1);
        arrParameters.add(val2);
        BrickBuilder bb = new BrickBuilder("DigitalWrite", BrickTypes.ARDUINO_COMMAND , arrParameters);
        bb.setCommandId(3);
        return bb.buildBrick();
    }
    public static Brick createAnalogWriteBrick()
    {
        Parameter val1=new Parameter("PinNumber",String.valueOf(("5")));
        ArrayList<String> allowedValuesV1 = BrickPersister.getArrValues(5,53,1);
        val1.setAllowedValues(allowedValuesV1);

        Parameter analogVal2=new Parameter("analogWrite",String.valueOf((0)));
        analogVal2.setAllowedValues(BrickPersister.getArrValues(0,200,10));


        ArrayList<Parameter> arrAnalog=new ArrayList<Parameter>();
        arrAnalog.add(val1);
        arrAnalog.add(analogVal2);
        BrickBuilder bb = new BrickBuilder("AnalogWrite", BrickTypes.ARDUINO_COMMAND , arrAnalog);
        bb.setCommandId(2);
        return bb.buildBrick();
    }

    public static Brick createAnalogReadBrick()
    {
        Parameter val1=new Parameter("PinNumber",String.valueOf(("5")));
        ArrayList<String> allowedValuesV1 = BrickPersister.getArrValues(5,53,1);
        val1.setAllowedValues(allowedValuesV1);



        ArrayList<Parameter> arrAnalog=new ArrayList<Parameter>();
        arrAnalog.add(val1);
        BrickBuilder bb = new BrickBuilder("AnalogRead", BrickTypes.ARDUINO_COMMAND , arrAnalog);
        bb.setCommandId(-1);
        return bb.buildBrick();
    }

    public static Brick createDigitalReadBrick()
    {
        Parameter val1=new Parameter("PinNumber",String.valueOf(("5")));
        ArrayList<String> allowedValuesV1 = BrickPersister.getArrValues(5,53,1);
        val1.setAllowedValues(allowedValuesV1);



        ArrayList<Parameter> arrAnalog=new ArrayList<Parameter>();
        arrAnalog.add(val1);
        BrickBuilder bb = new BrickBuilder("DigitalRead", BrickTypes.ARDUINO_COMMAND , arrAnalog);
        bb.setCommandId(-2);
        return bb.buildBrick();
    }

    public static ArrayList<Brick> constructStandardSketch() {
        ArrayList<Brick> bricks = new ArrayList<Brick>();

        /**
         * DigitalWrite
         */
        Brick item= createDigitalWriteBrick();

        /**
         * AnalogWrite
         */
        Brick item2= createAnalogWriteBrick();


        Brick item3= createAnalogReadBrick();
        Brick itemDR= createDigitalReadBrick();



        Brick itemTone= createToneBrick();
        Brick item4= createVariableBrick();
        Brick item5= createEndVariableBrick();

        Brick item6= createIfBrick();
        Brick item7= createEndIfBrick();


        Brick item8= createFORBrick();
        Brick item9= createEndFORBrick();




        /**
         * adding bricks
         */
        bricks.add(item);
        bricks.add(item2);
        bricks.add(item3);

        bricks.add(itemDR);
        bricks.add(itemTone);
        bricks.add(item4);
        bricks.add(item5);
        bricks.add(item6);
        bricks.add(item7);
        bricks.add(item8);
        bricks.add(item9);

        return bricks;

    }


    public static boolean saveStandardSketch(Context context) {

        /*if(fileExist(context, Constants.SKETCHES_FOLDER, Constants.STANDARD_SKETCH)) {
            return false;
        }
*/

        /*
        Brick ifBrick = createIfBrick();


        /*
        if(ifBrick != null) {
            bricks.add(ifBrick);
        }
        Brick endBrick= createEndIfBrick();
        bricks.add(endBrick );


        //Variable
        bricks.add(createVariableBrick());
        bricks.add(createEndVariableBrick());
        */


        writeJsonToFile(context, Constants.SKETCHES_FOLDER, Constants.STANDARD_SKETCH, translateSketchToJson(constructStandardSketch()));

        return true;
    }
private  static ArrayList<String> getArrValues(int init, int max, int step){
    ArrayList<String> allowedValuesAW = new ArrayList<String>();
    for ( int i=init; i<=max;i+=step  )
    {
        allowedValuesAW.add( String.valueOf(i));
    }
    return allowedValuesAW;
}
public static Brick createIfBrick() {
    /**
     * If
     */
    /*ArrayList<Parameter> arr=new ArrayList<Parameter>();
    BrickBuilder bbSub = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);
    Brick itemSub= bbSub.buildBrick();

    ArrayList<Brick> subList=new ArrayList<Brick>();
    subList.add(itemSub);*/


    ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
    Parameter valInternal=new Parameter("Internal",String.valueOf((2)));
    valInternal.setAllowedValues(BrickPersister.getArrValues(0,50,1));
    arrInternal.add(valInternal);

    Parameter valInternalComp=new Parameter("InternalComp",ComparatorTypes.GREATER.toString());
    ArrayList<String> allowedValues = new ArrayList<String>();

    allowedValues.add(ComparatorTypes.EQUALS.toString());
    allowedValues.add(ComparatorTypes.NOTEQUALS.toString());
    allowedValues.add(ComparatorTypes.GREATER.toString());
    allowedValues.add(ComparatorTypes.LESS.toString());
    valInternalComp.setAllowedValues(allowedValues);

    arrInternal.add(valInternalComp);

    Parameter valInternalRef=new Parameter("Ref",String.valueOf((3)));
    valInternalRef.setAllowedValues(BrickPersister.getArrValues(0,50,1));
    arrInternal.add(valInternalRef);

    BrickBuilder bb = new BrickBuilder("If", BrickTypes.INTERNAL, arrInternal);
    bb.setSubType(InternalSubTypes.IF);
    //bb.setSubBricks(subList);

    InternalBrick item= (InternalBrick) bb.buildBrick();
    return item;
}


    public static Brick createEndIfBrick() {

        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        BrickBuilder bb = new BrickBuilder("EndIf", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.ENDIF);
        //bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();
        return item;
    }



    public static Brick createVariableBrick() {
        /**
         * If
         */
    /*ArrayList<Parameter> arr=new ArrayList<Parameter>();
    BrickBuilder bbSub = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);
    Brick itemSub= bbSub.buildBrick();

    ArrayList<Brick> subList=new ArrayList<Brick>();
    subList.add(itemSub);*/


        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        Parameter valInternal=new Parameter("variableName", "name");
        arrInternal.add(valInternal);


        BrickBuilder bb = new BrickBuilder("Variable", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.VARIABLE);
        //bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();
        return item;
    }


    public static Brick createEndVariableBrick() {

        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        BrickBuilder bb = new BrickBuilder("EndVariable", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.ENDVARIABLE);
        //bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();
        return item;
    }


    public static Brick createFORBrick() {
        /**
         * While
         */

        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        Parameter valInternal=new Parameter("Ref",String.valueOf((2)));
        valInternal.setAllowedValues(BrickPersister.getArrValues(0,50,1));
        arrInternal.add(valInternal);

        Parameter valInternalComp=new Parameter("Comparator",ComparatorTypes.GREATER.toString());
        ArrayList<String> allowedValues = new ArrayList<String>();

        allowedValues.add(ComparatorTypes.GREATER.toString());
        allowedValues.add(ComparatorTypes.LESS.toString());
        valInternalComp.setAllowedValues(allowedValues);

        arrInternal.add(valInternalComp);

        Parameter valInternalRef=new Parameter("Limiter",String.valueOf((3)));
        valInternalRef.setAllowedValues(BrickPersister.getArrValues(-50,50,1));
        arrInternal.add(valInternalRef);

        BrickBuilder bb = new BrickBuilder("FOR", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.FOR);
        //bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();
        return item;
    }


    public static Brick createEndFORBrick() {

        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        BrickBuilder bb = new BrickBuilder("EndFOR", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.ENDFOR);
        //bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();
        return item;
    }

}
