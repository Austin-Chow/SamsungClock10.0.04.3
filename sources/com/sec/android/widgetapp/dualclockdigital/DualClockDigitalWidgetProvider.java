package com.sec.android.widgetapp.dualclockdigital;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Iterator;

public class DualClockDigitalWidgetProvider extends AppWidgetProvider implements UncaughtExceptionHandler {
    public void onEnabled(Context context) {
        Log.m41d("DualClockDigitalWidgetProvider", "onEnabled()");
        super.onEnabled(context);
        if (!CityManager.sIsCityManagerLoad) {
            CityManager.initCity(context);
        }
        AppWidgetUtils.setIsSamsungHome(context);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            try {
                int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, DualClockDigitalWidgetProvider.class));
                String action = intent.getAction();
                if (action != null) {
                    if (!CityManager.sIsCityManagerLoad) {
                        CityManager.initCity(context);
                        updateWidgets(context);
                    }
                    Log.secD("DualClockDigitalWidgetProvider", "onReceive() / action = " + action);
                    Object obj = -1;
                    switch (action.hashCode()) {
                        case -1894358607:
                            if (action.equals("com.sec.android.app.clockpackage.dualclockdigital.ADD_CITY")) {
                                obj = null;
                                break;
                            }
                            break;
                        case -351890175:
                            if (action.equals("com.sec.android.app.clockpackage.dualclockdigital.CHANGE_CITY_SECOND")) {
                                obj = 5;
                                break;
                            }
                            break;
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
                        case 1150598536:
                            if (action.equals("com.sec.android.intent.action.WALLPAPER_CHANGED")) {
                                obj = 3;
                                break;
                            }
                            break;
                        case 1223702627:
                            if (action.equals("com.sec.android.app.clockpackage.dualclockdigital.CHANGE_CITY_FIRST")) {
                                obj = 4;
                                break;
                            }
                            break;
                        case 1419545477:
                            if (action.equals("com.sec.android.widgetapp.dualclockdigital.ACTION_DUAL_CLOCK_DIGITAL_SETTING_CHANGED")) {
                                obj = 6;
                                break;
                            }
                            break;
                    }
                    int id;
                    int widId;
                    int uniqueId;
                    switch (obj) {
                        case null:
                            if (!(widgetIds == null || widgetIds.length == 0)) {
                                int homeZone = intent.getIntExtra("homezone", 0);
                                int widgetId = intent.getIntExtra("wid", 0);
                                DualClockUtils.saveDBCityCountry(context, homeZone, widgetId, intent.getIntExtra("uniqueid", 0));
                                DualClockDigitalViewModel.updateDualClock(context, AppWidgetManager.getInstance(context), widgetId);
                                break;
                            }
                        case 1:
                            if (!(widgetIds == null || widgetIds.length == 0)) {
                                DualClockUtils.updateDBCityCountry(context);
                                DualClockDigitalViewModel.updateDualClock(context, AppWidgetManager.getInstance(context), -1);
                                break;
                            }
                        case 2:
                        case 3:
                            DualClockDigitalViewModel.updateDualClock(context, AppWidgetManager.getInstance(context), -1);
                            break;
                        case 4:
                            if (!(widgetIds == null || widgetIds.length == 0)) {
                                id = intent.getIntExtra("id", 1);
                                widId = intent.getIntExtra("widId", 1);
                                uniqueId = intent.getIntExtra("uniqueId", 1);
                                Log.secD("DualClockDigitalWidgetProvider", "id: " + id + " widId : " + widId + " uniqueId : " + uniqueId);
                                DualClockUtils.changeCity(context, id, widId, uniqueId);
                                if (id == 1) {
                                    ClockUtils.insertSaLog("135", "1373");
                                    break;
                                }
                            }
                            break;
                        case 5:
                            if (!(widgetIds == null || widgetIds.length == 0)) {
                                id = intent.getIntExtra("id", 1);
                                widId = intent.getIntExtra("widId", 1);
                                uniqueId = intent.getIntExtra("uniqueId", 1);
                                Log.secD("DualClockDigitalWidgetProvider", "id: " + id + " widId : " + widId + " uniqueId : " + uniqueId);
                                DualClockUtils.changeCity(context, id, widId, uniqueId);
                                if (id == 2) {
                                    ClockUtils.insertSaLog("135", "1374");
                                    break;
                                }
                            }
                            break;
                        case 6:
                            DualClockDigitalViewModel.updateDualClock(context, AppWidgetManager.getInstance(context), WidgetSettingUtils.getAppWidgetIdFromIntent(intent));
                            break;
                    }
                    super.onReceive(context, intent);
                }
            } catch (IllegalStateException e) {
                Log.secE("DualClockDigitalWidgetProvider", "onReceive getAppWidgetIds IllegalStateException e = " + e);
            }
        }
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.secD("DualClockDigitalWidgetProvider", "onAppWidgetOptionsChanged ::: appWidgetId :: " + appWidgetId);
        DualClockDigitalViewModel.updateDualClock(context, appWidgetManager, appWidgetId);
    }

    private void updateWidgets(Context context) {
        try {
            int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, DualClockDigitalWidgetProvider.class));
            Log.secD("DualClockDigitalWidgetProvider", "updateWidgets widgetIds length : " + String.valueOf(widgetIds.length));
            onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);
        } catch (IllegalStateException e) {
            Log.secE("DualClockDigitalWidgetProvider", "updateWidgets getAppWidgetIds IllegalStateException e = " + e);
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.m41d("DualClockDigitalWidgetProvider", "onUpdate()");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        ClockUtils.insertSaLog("135");
        AppWidgetUtils.setIsSamsungHome(context);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            Log.secD("DualClockDigitalWidgetProvider", "onUpdate appWidgetIds.length : " + appWidgetIds.length);
            if (CityManager.sCities == null || CityManager.sCitiesId == null) {
                CityManager.initCity(context);
                updateWidgets(context);
            }
            if (SharedManager.hasInvalidRestoredData(context)) {
                Log.secD("DualClockDigitalWidgetProvider", "onUpdate removeRestoredData");
                removeRestoredData(context);
            }
            if (appWidgetIds.length == 1 && SharedManager.hasValidRestoredData(context)) {
                Log.secD("DualClockDigitalWidgetProvider", "onUpdate hasValidRestoredData");
                Bundle option = appWidgetManager.getAppWidgetOptions(appWidgetIds[0]);
                int oldWidgetId = option.getInt("Old_WidgetId");
                int newWidgetID = option.getInt("New_WidgetId");
                Log.secD("DualClockDigitalWidgetProvider", "oldWidgetId : " + oldWidgetId + " newWidgetID : " + newWidgetID);
                if (!(oldWidgetId == -1 || newWidgetID == -1 || newWidgetID != appWidgetIds[0])) {
                    Log.secD("DualClockDigitalWidgetProvider", "onUpdate restoreWidget");
                    DualClockUtils.restoreWidget(context, oldWidgetId, newWidgetID);
                }
                SharedManager.addWidgetIds(context, appWidgetIds[0]);
            }
            if (SharedManager.getPrefIDs(context).length == 0) {
                for (int addWidgetIds : appWidgetIds) {
                    SharedManager.addWidgetIds(context, addWidgetIds);
                }
            }
            Log.secD("DualClockDigitalWidgetProvider", "updateViews count appWidgetIds.length : " + String.valueOf(appWidgetIds.length));
            for (int addWidgetIds2 : appWidgetIds) {
                DualClockDigitalViewModel.updateDualClock(context, appWidgetManager, addWidgetIds2);
            }
        }
    }

    public void onDisabled(Context context) {
        Log.m41d("DualClockDigitalWidgetProvider", "onDisabled()");
        super.onDisabled(context);
        SharedManager.removeAllWidgetIds(context);
        deleteAllWidget(context);
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.m41d("DualClockDigitalWidgetProvider", "onDeleted()");
        super.onDeleted(context, appWidgetIds);
        SharedManager.removeWidgetIds(context, appWidgetIds[0]);
        deleteWidget(context, appWidgetIds[0]);
        new ViewModelHelper(context, appWidgetIds[0]).removePreference(1);
    }

    private void deleteWidget(Context context, int widgetId) {
        Log.secD("DualClockDigitalWidgetProvider", "deleteWidget()");
        if (widgetId >= 0) {
            context.getContentResolver().delete(DualClockDigital.DATA_URI, "wid = " + widgetId, null);
        }
    }

    private void deleteAllWidget(Context context) {
        Log.secD("DualClockDigitalWidgetProvider", "deleteAllWidget()");
        context.getContentResolver().delete(DualClockDigital.DATA_URI, null, null);
    }

    private void removeRestoredData(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        try {
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, DualClockDigitalWidgetProvider.class));
            ArrayList<String> restoredList = new ArrayList();
            for (int appWidgetOptions : appWidgetIds) {
                Bundle option = appWidgetManager.getAppWidgetOptions(appWidgetOptions);
                if (option != null) {
                    int oldWidgetId = option.getInt("Old_WidgetId");
                    Log.secD("DualClockDigitalWidgetProvider", "Old_WidgetId : " + oldWidgetId);
                    if (oldWidgetId != -1) {
                        restoredList.add(Integer.toString(oldWidgetId) + "_" + Integer.toString(1));
                        restoredList.add(Integer.toString(oldWidgetId) + "_" + Integer.toString(2));
                    }
                }
            }
            Iterator it = restoredList.iterator();
            while (it.hasNext()) {
                SharedManager.removeRestoredData(context, (String) it.next());
            }
            SharedManager.removeRestoredTime(context);
        } catch (IllegalStateException e) {
            Log.secE("DualClockDigitalWidgetProvider", "removeRestoredData getAppWidgetIds IllegalStateException e = " + e);
        }
    }

    public void uncaughtException(Thread arg0, Throwable arg1) {
        Log.secE("DualClockDigitalWidgetProvider", "uncaughtException");
    }
}
