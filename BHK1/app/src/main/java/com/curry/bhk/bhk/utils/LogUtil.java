package com.curry.bhk.bhk.utils;

import android.util.Log;

/**
 * Created by Curry on 2016/10/19.
 *
 * when LEVEL equal to VERBOSE ,can print all log .
 * when LEVEL equal to NOTHING , can shield all log.
 */
public class LogUtil {
    public static  final int VERBOSE = 1;
    public static  final int DEBUG = 2;
    public static  final int INFO = 3;
    public static  final int WARN = 4;
    public static  final int ERROR = 5;
    public static  final int NOTHING = 6;
    public static  final int LEVEL = VERBOSE;

    public static void v(String tag,String msg){
        if(LEVEL<=VERBOSE){
            Log.e(tag,msg);
        }
    }public static void d(String tag,String msg){
        if(LEVEL<=DEBUG){
            Log.e(tag,msg);
        }
    }public static void i(String tag,String msg){
        if(LEVEL<=INFO){
            Log.e(tag,msg);
        }
    }public static void w(String tag,String msg){
        if(LEVEL<=WARN){
            Log.e(tag,msg);
        }
    }public static void e(String tag,String msg){
        if(LEVEL<=ERROR){
            Log.e(tag,msg);
        }
    }
}
