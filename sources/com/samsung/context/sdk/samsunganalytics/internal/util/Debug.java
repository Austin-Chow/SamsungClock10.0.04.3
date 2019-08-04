package com.samsung.context.sdk.samsunganalytics.internal.util;

import android.util.Log;

public class Debug {
    public static void LogENG(String log) {
        if (Utils.isEngBin()) {
            Log.d("SamsungAnalytics200003", "[ENG ONLY] " + log);
        }
    }

    public static void LogD(String log) {
        Log.d("SamsungAnalytics200003", log);
    }

    public static void LogD(String classname, String log) {
        LogD("[" + classname + "] " + log);
    }

    public static void LogE(String log) {
        Log.e("SamsungAnalytics200003", log);
    }

    public static void LogException(Class classname, Exception e) {
        if (e != null) {
            Log.w("SamsungAnalytics200003", "[" + classname.getSimpleName() + "] " + e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }
}
