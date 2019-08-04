package com.sec.android.widgetapp.digitalclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;

public class DigitalClockWidgetProvider extends AppWidgetProvider {
    public void onEnabled(Context context) {
        AppWidgetUtils.setIsSamsungHome(context);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        AppWidgetUtils.setIsSamsungHome(context);
        DigitalClockViewModel.updateDigitalClock(context, appWidgetManager, appWidgetIds);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            try {
                int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, DigitalClockWidgetProvider.class));
                String action = intent.getAction();
                Log.secD("DigitalClockAppWidget", "onReceive() action : " + action);
                int i = -1;
                switch (action.hashCode()) {
                    case -1606982654:
                        if (action.equals("com.sec.android.widgetapp.digitalclock.ACTION_DIGITAL_CLOCK_SETTING_CHANGED")) {
                            i = 3;
                            break;
                        }
                        break;
                    case -880066276:
                        if (action.equals("com.sec.android.widgetapp.digitalclock.SHOW_CLOCKPACKAGE")) {
                            i = 4;
                            break;
                        }
                        break;
                    case -19011148:
                        if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                            i = 0;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            i = 1;
                            break;
                        }
                        break;
                    case 1150598536:
                        if (action.equals("com.sec.android.intent.action.WALLPAPER_CHANGED")) {
                            i = 2;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                        if (!(widgetIds == null || widgetIds.length == 0)) {
                            DigitalClockViewModel.updateDigitalClock(context, appWidgetManager, widgetIds);
                            break;
                        }
                    case 3:
                        if (WidgetSettingUtils.isValidWidgetId(WidgetSettingUtils.getAppWidgetIdFromIntent(intent))) {
                            DigitalClockViewModel.updateDigitalClock(context, appWidgetManager, new int[]{WidgetSettingUtils.getAppWidgetIdFromIntent(intent)});
                            break;
                        }
                        break;
                    case 4:
                        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
                        Intent startClockPackage = new Intent("android.intent.action.MAIN");
                        startClockPackage.addCategory("android.intent.category.LAUNCHER");
                        startClockPackage.setComponent(cn);
                        startClockPackage.setFlags(268468224);
                        context.startActivity(startClockPackage);
                        break;
                }
                super.onReceive(context, intent);
            } catch (IllegalStateException e) {
                Log.secE("DigitalClockAppWidget", "getAppWidgetIds IllegalStateException e = " + e);
            }
        }
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        int[] widgetIds;
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        if (WidgetSettingUtils.isValidWidgetId(appWidgetId)) {
            widgetIds = new int[]{appWidgetId};
        } else {
            try {
                widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, DigitalClockWidgetProvider.class));
            } catch (IllegalStateException e) {
                Log.secE("DigitalClockAppWidget", "getAppWidgetIds IllegalStateException e = " + e);
                return;
            }
        }
        DigitalClockViewModel.updateDigitalClock(context, appWidgetManager, widgetIds);
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        new ViewModelHelper(context, appWidgetIds[0]).removePreference(0);
    }
}
