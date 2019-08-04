package com.sec.android.app.clockpackage.commonwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.lang.reflect.Method;

public class AppWidgetUtils {
    private static boolean sIsSamsungHome = true;

    private static int getWidgetRowSpan(AppWidgetManager awm, int widgetId) {
        return awm.getAppWidgetOptions(widgetId).getInt("semAppWidgetRowSpan", -1);
    }

    private static int getWidgetColumnSpan(AppWidgetManager awm, int widgetId) {
        return awm.getAppWidgetOptions(widgetId).getInt("semAppWidgetColumnSpan", -1);
    }

    public static int getWidgetSize(Context context, int widgetId) {
        int columnSpan = getWidgetColumnSpan(AppWidgetManager.getInstance(context), widgetId);
        Log.m41d("AppWidgetUtils", "getWidgetSize widgetId : " + widgetId + " columnSpan : " + columnSpan);
        if (columnSpan > 3 || columnSpan == -1) {
            return 2;
        }
        return 1;
    }

    public static boolean isSupportedWidget(Context context, int widgetId) {
        int rowSpan = getWidgetRowSpan(AppWidgetManager.getInstance(context), widgetId);
        Log.m41d("AppWidgetUtils", "isSupportedWidget widgetId : " + widgetId + " rowSpan : " + rowSpan);
        return rowSpan < 2;
    }

    public static boolean isSamsungHome() {
        return sIsSamsungHome;
    }

    public static void setIsSamsungHome(Context context) {
        boolean z = false;
        String defaultHomePackage = null;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        PackageManager packageManager = context == null ? null : context.getPackageManager();
        ResolveInfo resolveInfo = packageManager == null ? null : packageManager.resolveActivity(intent, 0);
        if ((resolveInfo == null ? null : resolveInfo.activityInfo) != null) {
            defaultHomePackage = resolveInfo.activityInfo.packageName;
        }
        if (defaultHomePackage == null) {
            Log.secE("AppWidgetUtils", "setIsSamsungHome() cannot get package name");
            sIsSamsungHome = false;
        } else if (defaultHomePackage.equals("android")) {
            Log.m41d("AppWidgetUtils", "defaultHomePackage Name : System has no default launcher.");
            sIsSamsungHome = true;
        } else {
            Log.secD("AppWidgetUtils", "defaultHomePackage Name : " + defaultHomePackage);
            if (defaultHomePackage.equals("com.sec.android.app.launcher") || defaultHomePackage.equals("com.sec.android.preloadinstaller")) {
                z = true;
            }
            sIsSamsungHome = z;
        }
    }

    public static boolean needThirdPartyLayout(Context context) {
        return !isSamsungHome() || StateUtils.isMobileKeyboard(context) || Feature.isDCM(context);
    }

    public static boolean canSetFlagForClockWidgetTextViewWidth() {
        Exception e;
        Log.secD("AppWidgetUtils", "canSetFlagForClockWidgetTextViewWidth");
        try {
            Method setFlagForClockWidgetTextViewWidth = TextView.class.getMethod("setFlagForClockWidgetTextViewWidth", new Class[]{Boolean.TYPE});
            Class aClass = null;
            try {
                aClass = Class.forName("android.view.RemotableViewMethod");
            } catch (ClassNotFoundException e2) {
                Log.secD("AppWidgetUtils", "canSetFlagForClockWidgetTextViewWidth exception : " + e2.toString());
            }
            if (aClass != null) {
                if (setFlagForClockWidgetTextViewWidth.isAnnotationPresent(aClass)) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e3) {
            e = e3;
            Log.secD("AppWidgetUtils", "canSetFlagForClockWidgetTextViewWidth exception : " + e.toString());
            return false;
        } catch (NoSuchMethodException e4) {
            e = e4;
            Log.secD("AppWidgetUtils", "canSetFlagForClockWidgetTextViewWidth exception : " + e.toString());
            return false;
        }
    }
}
