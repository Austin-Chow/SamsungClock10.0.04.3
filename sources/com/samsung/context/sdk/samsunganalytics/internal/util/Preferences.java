package com.samsung.context.sdk.samsunganalytics.internal.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("SamsungAnalyticsPrefs", 0);
    }
}
