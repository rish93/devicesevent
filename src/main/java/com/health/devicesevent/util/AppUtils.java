package com.health.devicesevent.util;

public class AppUtils {
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


    public static String removeTrailingComma(String input){
      return   input.replaceAll(",$", "");
    }

}
