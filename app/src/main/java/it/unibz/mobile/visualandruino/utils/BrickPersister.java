package it.unibz.mobile.visualandruino.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;


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


}
