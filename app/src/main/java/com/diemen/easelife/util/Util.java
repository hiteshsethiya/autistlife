package com.diemen.easelife.util;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class Util {

    public static boolean isInteger(String value)
    {
        boolean status = false;

        try
        {
            Integer.parseInt(value);
            status = true;
        }
        catch(NumberFormatException e)
        {
            status = false;
        }
        return status;
    }
}