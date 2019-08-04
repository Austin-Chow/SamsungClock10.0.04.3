package com.sec.android.widgetapp.analogclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;

public class AnalogClockWidgetProvider extends AppWidgetProvider {
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null && intent.getAction() != null) {
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            try {
                int[] widgetIds = awm.getAppWidgetIds(new ComponentName(context, AnalogClockWidgetProvider.class));
                String action = intent.getAction();
                Log.secD("AnalogClockWidgetProvider", "onReceive() / action = " + action);
                Object obj = -1;
                switch (action.hashCode()) {
                    case -19011148:
                        if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 510438608:
                        if (action.equals("com.sec.android.widgetapp.analogclock.SHOW_CLOCKPACKAGE")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 1150598536:
                        if (action.equals("com.sec.android.intent.action.WALLPAPER_CHANGED")) {
                            obj = null;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                    case 1:
                    case 2:
                        if (widgetIds != null && widgetIds.length != 0) {
                            AnalogClockViewModel.updateAnalogClock(context, awm, widgetIds);
                            return;
                        }
                        return;
                    case 3:
                        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
                        Intent startClockPackage = new Intent("android.intent.action.MAIN");
                        startClockPackage.addCategory("android.intent.category.LAUNCHER");
                        startClockPackage.setComponent(cn);
                        startClockPackage.setFlags(268468224);
                        context.startActivity(startClockPackage);
                        return;
                    default:
                        return;
                }
            } catch (IllegalStateException e) {
                Log.secE("AnalogClockWidgetProvider", "getAppWidgetIds IllegalStateException e = " + e);
            }
        }
    }

    public void onUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds) {
        super.onUpdate(context, awm, appWidgetIds);
        Log.m41d("AnalogClockWidgetProvider", "onUpdate");
        AnalogClockViewModel.updateAnalogClock(context, awm, appWidgetIds);
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager awm, int appWidgetId, Bundle newOptions) {
        int[] widgetIds;
        super.onAppWidgetOptionsChanged(context, awm, appWidgetId, newOptions);
        Log.m41d("AnalogClockWidgetProvider", "onAppWidgetOptionsChanged");
        if (WidgetSettingUtils.isValidWidgetId(appWidgetId)) {
            widgetIds = new int[]{appWidgetId};
        } else {
            try {
                widgetIds = awm.getAppWidgetIds(new ComponentName(context, AnalogClockWidgetProvider.class));
            } catch (IllegalStateException e) {
                Log.secE("AnalogClockWidgetProvider", "getAppWidgetIds IllegalStateException e = " + e);
                return;
            }
        }
        AnalogClockViewModel.updateAnalogClock(context, awm, widgetIds);
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.m41d("AnalogClockWidgetProvider", "onDeleted() : " + appWidgetIds[0]);
    }

    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.m41d("AnalogClockWidgetProvider", "onDisabled()");
    }
}
