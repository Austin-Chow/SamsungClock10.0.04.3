package com.samsung.android.scloud.oem.lib;

import android.os.Build;
import android.util.Log;
import java.util.Locale;

public class LOG {
    private static boolean bLogEnabled = "eng".equals(Build.TYPE);

    /* renamed from: i */
    public static void m6i(String tag, String msg) {
        if (bLogEnabled && msg != null) {
            Log.i("[PDLIB]" + tag, msg);
        }
    }

    /* renamed from: d */
    public static void m3d(String tag, String msg) {
        if (bLogEnabled && msg != null) {
            Log.d("[PDLIB]" + tag, msg);
        }
    }

    /* renamed from: e */
    public static void m4e(String tag, String msg, Throwable e) {
        Locale locale = Locale.US;
        String str = "%s %s";
        Object[] objArr = new Object[2];
        objArr[0] = msg;
        objArr[1] = e == null ? "" : Log.getStackTraceString(e);
        Log.e("[PDLIB]SCLOUD_ERR-" + tag, String.format(locale, str, objArr));
    }

    /* renamed from: f */
    public static void m5f(String tag, String msg) {
        if (msg != null) {
            Log.i("[PDLIB]" + tag, msg);
        }
    }
}
