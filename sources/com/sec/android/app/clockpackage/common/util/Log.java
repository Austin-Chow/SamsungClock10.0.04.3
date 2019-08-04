package com.sec.android.app.clockpackage.common.util;

import com.samsung.android.util.SemLog;

public class Log {
    public static void secI(String tag, String message) {
        SemLog.secI(tag, message);
    }

    public static void secE(String tag, String message) {
        SemLog.secE(tag, message);
    }

    public static void secV(String tag, String message) {
        SemLog.secV(tag, message);
    }

    public static void secW(String tag, String message) {
        SemLog.secW(tag, message);
    }

    public static void secD(String tag, String message) {
        SemLog.secD(tag, message);
    }

    /* renamed from: i */
    public static void m43i(String tag, String message) {
        android.util.Log.i(tag, message);
    }

    /* renamed from: e */
    public static void m42e(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    /* renamed from: w */
    public static void m44w(String tag, String message) {
        android.util.Log.w(tag, message);
    }

    /* renamed from: d */
    public static void m41d(String tag, String message) {
        android.util.Log.d(tag, message);
    }
}
