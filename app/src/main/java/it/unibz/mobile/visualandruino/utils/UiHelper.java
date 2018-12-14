package it.unibz.mobile.visualandruino.utils;

import com.jraska.console.Console;

import java.text.SimpleDateFormat;
import java.util.Date;



public class UiHelper {

    private static String getTime() {

        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
        String format = s.format(new Date());

        return format;

    }

    public static void writeCommand(String command) {
        Console.writeLine(getTime() + " " +  command);
    }
}
