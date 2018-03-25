package com.cyb.wework.utils;

import android.util.Log;

/**
 * Created by cyb on 2018/1/29.
 */

public class LogUtil {

    public static boolean showLog = true;
    public static String defaultTAG = "CYB";

    public static void d(String message){
        d(defaultTAG, message);
    }

    public static void d(String tag, String message){
        if(showLog){
            Log.d(tag, message);
        }
    }
}
