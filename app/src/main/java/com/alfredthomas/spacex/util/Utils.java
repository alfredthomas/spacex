package com.alfredthomas.spacex.util;

/**
 * Created by AJ on 2/19/2018.
 */

public class Utils {
    //straight forward conversion to yes/no
    public static String convertToYesNo(boolean bool)
    {
        if(bool)
            return "yes";
        return "no";
    }
    //changes null or "null" into "" or "N/A"
    public static String removeNull(String startString)
    {
        //either return empty string or N/A. depends on preference
        if(startString == null || startString.equals("null"))
            return "N/A";
        return startString;
    }
}
