package com.samsung.context.sdk.samsunganalytics.internal.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.System;
import com.samsung.context.sdk.samsunganalytics.AnalyticsException;
import com.samsung.context.sdk.samsunganalytics.internal.sender.LogType;

public class Utils {
    public static boolean isEngBin() {
        return Build.TYPE.equals("eng");
    }

    public static void throwException(String message) {
        if (isEngBin()) {
            throw new AnalyticsException(message);
        }
        Debug.LogE(message);
    }

    public static long getDaysAgo(int days) {
        return Long.valueOf(System.currentTimeMillis()).longValue() - (((long) days) * 86400000);
    }

    public static boolean compareDays(int days, Long befMillis) {
        return Long.valueOf(System.currentTimeMillis()).longValue() > befMillis.longValue() + (((long) days) * 86400000);
    }

    public static LogType getTypeForServer(String rawType) {
        return "dl".equals(rawType) ? LogType.DEVICE : LogType.UIX;
    }

    public static boolean isDiagnosticAgree(Context context) {
        return System.getInt(context.getContentResolver(), "samsung_errorlog_agree", 0) == 1;
    }
}
