package com.sec.android.app.clockpackage.commonwidget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings.System;

public class WidgetSettingUtils {
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("com.sec.android.widgetapp.clockwidgets_preferences", 0);
    }

    public static int getSharedPreference(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static boolean isWhiteWallPaper(Context context) {
        return System.getInt(context.getContentResolver(), "need_dark_font", 0) > 0;
    }

    public static boolean isDarkFont(Context context, int theme, int transparency) {
        boolean isWhiteWallPaper = isWhiteWallPaper(context);
        if (theme == 0) {
            return isWhiteWallPaper || transparency < 127;
        } else {
            if (theme != 1) {
                return true;
            }
            boolean value = isWhiteWallPaper && transparency > 127;
            return value;
        }
    }

    public static boolean isLandscape(Context context) {
        return context != null && context.getResources().getConfiguration().orientation == 2;
    }

    public static boolean isValidWidgetId(int appWidgetId) {
        return appWidgetId != 0;
    }

    public static int getAppWidgetIdFromIntent(Intent intent) {
        int i = 0;
        Uri uri = intent.getData();
        if (uri == null) {
            return intent.getIntExtra("appWidgetId", i);
        }
        try {
            return Integer.parseInt(uri.getLastPathSegment());
        } catch (NumberFormatException e) {
            return i;
        }
    }

    public static int getDefaultTransparency(int widgetType) {
        if (widgetType == 0) {
            return 255;
        }
        return Math.round(216.75f);
    }
}
