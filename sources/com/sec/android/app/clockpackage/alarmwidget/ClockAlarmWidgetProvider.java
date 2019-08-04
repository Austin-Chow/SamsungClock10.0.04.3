package com.sec.android.app.clockpackage.alarmwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetContract.ViewModel;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;

public class ClockAlarmWidgetProvider extends AppWidgetProvider {
    private static int sAlarmActionPrevious = 0;
    private static int sAlarmIndex = 0;
    private static int sTotalWidgetCnt = 0;

    public void onEnabled(Context context) {
        super.onEnabled(context);
        AppWidgetUtils.setIsSamsungHome(context);
        Log.m41d("AlarmWidget_Provider", "onEnabled()");
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        AppWidgetUtils.setIsSamsungHome(context);
        Log.m41d("AlarmWidget_Provider", "onUpdate() /appWidgetIds.length= " + appWidgetIds.length);
        if (appWidgetIds.length == 1) {
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetIds[0]);
            if (options != null) {
                int oldWidgetId = options.getInt("Old_WidgetId");
                int newWidgetId = options.getInt("New_WidgetId");
                Log.secD("BNR_CLOCK_ALARMWIDGET", "alarmwidget_bnr (**) : onUpdate  / widget (" + oldWidgetId + " -> " + newWidgetId + ")");
                int alarmId = ClockAlarmWidgetIdManager.getBnRAlarmWidgetData(context, oldWidgetId);
                Log.m41d("BNR_CLOCK_ALARMWIDGET", "alarmwidget_bnr (**) : onUpdate /oldWidgetId=" + oldWidgetId + "/newWidgetId=" + newWidgetId + "/alarmId=" + alarmId);
                if (!(oldWidgetId == -1 || newWidgetId == -1)) {
                    ClockAlarmWidgetIdManager.setListItem(context, newWidgetId, alarmId);
                    ClockAlarmWidgetIdManager.setWidgetId(context, newWidgetId, alarmId);
                }
                ClockAlarmWidgetIdManager.removeBnRAlarmWidgetData(context, oldWidgetId);
                options.putInt("Old_WidgetId", -1);
                options.putInt("New_WidgetId", -1);
                appWidgetManager.updateAppWidgetOptions(appWidgetIds[0], options);
            }
        }
        updateClock(context, AppWidgetManager.getInstance(context));
        ClockUtils.insertSaStatusLog(context, "5113", (long) sTotalWidgetCnt);
    }

    public void onDeleted(Context ctx, int[] appWidgetIds) {
        Log.m41d("AlarmWidget_Provider", "onDeleted()");
        super.onDeleted(ctx, appWidgetIds);
        ClockAlarmWidgetIdManager.removeItem(ctx, appWidgetIds[0]);
        new ViewModelHelper(ctx, appWidgetIds[0]).removePreference(2);
        ClockUtils.insertSaStatusLog(ctx, "5113", (long) sTotalWidgetCnt);
    }

    public void onDisabled(Context context) {
        Log.m41d("AlarmWidget_Provider", "onDisabled()");
        super.onDisabled(context);
    }

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager awm, int appWidgetId, Bundle newOptions) {
        int minWidth = newOptions.getInt("appWidgetMinWidth");
        int maxWidth = newOptions.getInt("appWidgetMaxWidth");
        int minHeight = newOptions.getInt("appWidgetMinHeight");
        Log.secD("AlarmWidget_Provider", "alarmwidget_d_resize : onAppWidgetOptionsChanged() / minWidth" + minWidth + "/maxWidth=" + maxWidth + "/minHeight=" + minHeight + "/maxHeight=" + newOptions.getInt("appWidgetMaxHeight"));
        updateViews(context, awm, appWidgetId);
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            boolean bEnterEasyMode = intent.getBooleanExtra("easymode", false);
            int selectedIndex = intent.getIntExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", 0);
            int selectedWidgetId = intent.getIntExtra("widgetId", -1);
            int duplicatedAlarmId = intent.getIntExtra("duplicateId", -1);
            int listItemId = ClockAlarmWidgetIdManager.getListItem(context, selectedWidgetId, selectedIndex + 1);
            try {
                int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ClockAlarmWidgetProvider.class));
                if (sAlarmActionPrevious == 1) {
                    selectedIndex = sAlarmIndex;
                }
                Log.secD("AlarmWidget_Provider", "onReceive(): action = " + action.substring(action.lastIndexOf(46)));
                Log.secD("AlarmWidget_Provider", "onReceive(): selectedIndex = " + selectedIndex + ", selectedWidgetId = " + selectedWidgetId + ", listItemId = " + listItemId + ", duplicatedAlarmId = " + duplicatedAlarmId + ", bEnterEasyMode = " + bEnterEasyMode);
                Object obj = -1;
                switch (action.hashCode()) {
                    case -1662713978:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET")) {
                            obj = null;
                            break;
                        }
                        break;
                    case -1267814167:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_EDIT")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case -637994991:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ONOFF")) {
                            obj = 5;
                            break;
                        }
                        break;
                    case -290815162:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SETTING_CHANGED")) {
                            obj = 13;
                            break;
                        }
                        break;
                    case -19011148:
                        if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                            obj = 9;
                            break;
                        }
                        break;
                    case 69562097:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SETTING")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 502473491:
                        if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                            obj = 11;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            obj = 10;
                            break;
                        }
                        break;
                    case 1133924026:
                        if (action.equals("com.samsung.launcher.action.EASY_MODE_CHANGE")) {
                            obj = 6;
                            break;
                        }
                        break;
                    case 1150598536:
                        if (action.equals("com.sec.android.intent.action.WALLPAPER_CHANGED")) {
                            obj = 12;
                            break;
                        }
                        break;
                    case 1286628478:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW")) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 1294398883:
                        if (action.equals("com.samsung.android.theme.themecenter.THEME_APPLY")) {
                            obj = 8;
                            break;
                        }
                        break;
                    case 1803106331:
                        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SELECT")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 1938004612:
                        if (action.equals("com.sec.android.intent.action.LAUNCHER_CHANGED")) {
                            obj = 7;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        sAlarmActionPrevious = 0;
                        updateClock(context, AppWidgetManager.getInstance(context));
                        break;
                    case 1:
                        sAlarmActionPrevious = 0;
                        if (duplicatedAlarmId != -1) {
                            int count;
                            if (widgetIds != null) {
                                count = widgetIds.length;
                            } else {
                                count = 0;
                            }
                            for (int i = 0; i < count; i++) {
                                int duplicateWidgetId = ClockAlarmWidgetIdManager.getDuplicateWidgetId(context, widgetIds[i], duplicatedAlarmId);
                                Log.secE("AlarmWidget_Provider", "duplicateWidgetId :" + duplicateWidgetId);
                                if (duplicateWidgetId != -1) {
                                    ClockAlarmWidgetIdManager.setListItem(context, duplicateWidgetId, selectedIndex);
                                    ClockAlarmWidgetIdManager.setWidgetId(context, duplicateWidgetId, selectedIndex);
                                    updateViews(context, AppWidgetManager.getInstance(context), duplicateWidgetId);
                                }
                            }
                        }
                        ClockAlarmWidgetIdManager.setListItem(context, selectedWidgetId, selectedIndex);
                        ClockAlarmWidgetIdManager.setWidgetId(context, selectedWidgetId, selectedIndex);
                        updateViews(context, AppWidgetManager.getInstance(context), selectedWidgetId);
                        break;
                    case 2:
                        Intent intent2 = new Intent("android.intent.action.VIEW");
                        if (context.getSharedPreferences("isSetDefault", 0).getInt("alarmBootState", 0) == 0 && ClockAlarmWidgetUtils.getAlarmItemCount(context) == 0) {
                            AlarmUtil.setPresetAlarm(context);
                            context.getSharedPreferences("isSetDefault", 0).edit().putInt("alarmBootState", 1).apply();
                        }
                        if (ClockAlarmWidgetUtils.getAlarmItemCount(context) == 0) {
                            intent2.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity");
                            intent2.setType("alarm_create_direct");
                            intent2.putExtra("AlarmLaunchMode", 2);
                            intent2.putExtra("widgetId", selectedWidgetId);
                            intent2.putExtra("ListItemPosition", listItemId);
                            intent2.putExtra("from", "SimpleClockAlarmWidget");
                            sAlarmActionPrevious = 0;
                            intent2.setFlags(335806464);
                        } else {
                            sAlarmActionPrevious = 0;
                            intent2.setAction("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW");
                            intent2.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmWidgetListActivity");
                            intent2.setType("alarm_edit_direct");
                            intent2.putExtra("AlarmLaunchMode", 2);
                            intent2.putExtra("widgetId", selectedWidgetId);
                            intent2.putExtra("from", "SimpleClockAlarmWidget");
                            intent2.setFlags(268468224);
                        }
                        if (context.getApplicationContext().getPackageManager() == null || context.getApplicationContext().getPackageManager().queryIntentActivities(intent2, 0).size() <= 0) {
                            Log.secD("AlarmWidget_Provider", "Activity Not Found !");
                        } else {
                            context.startActivity(intent2);
                        }
                        ClockUtils.insertSaLog("134", "1372");
                        break;
                    case 3:
                        Intent editIntent = new Intent();
                        editIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity");
                        editIntent.putExtra("AlarmLaunchMode", 2);
                        editIntent.putExtra("widgetId", selectedWidgetId);
                        editIntent.putExtra("ListItemPosition", listItemId);
                        editIntent.putExtra("from", "SimpleClockAlarmWidget");
                        if (ClockAlarmWidgetUtils.getAlarmItemCount(context) == 0) {
                            editIntent.setType("alarm_create_direct");
                            sAlarmActionPrevious = 0;
                            editIntent.setFlags(335806464);
                        } else {
                            editIntent.setType("alarm_edit_direct");
                            editIntent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", listItemId);
                            sAlarmIndex = listItemId;
                            sAlarmActionPrevious = 1;
                            editIntent.setFlags(335806464);
                        }
                        if (context.getApplicationContext().getPackageManager() == null || context.getApplicationContext().getPackageManager().queryIntentActivities(editIntent, 0).size() <= 0) {
                            Log.secD("AlarmWidget_Provider", "Activity Not Found !");
                        } else {
                            context.startActivity(editIntent);
                        }
                        ClockUtils.insertSaLog("134", "1371");
                        break;
                    case 4:
                        Intent launchIntent = new Intent();
                        launchIntent.setClassName(context.getPackageName(), "com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetSettingActivity");
                        launchIntent.setFlags(343965696);
                        launchIntent.putExtra("WidgetType", 2);
                        launchIntent.putExtra("appWidgetId", selectedWidgetId);
                        try {
                            context.startActivity(launchIntent);
                            break;
                        } catch (ActivityNotFoundException e) {
                            break;
                        }
                    case 5:
                        sAlarmActionPrevious = 0;
                        AlarmItem alarmItem = AlarmProvider.getAlarm(context, listItemId);
                        if (alarmItem != null) {
                            Boolean active;
                            if (alarmItem.mActivate == 0) {
                                active = Boolean.valueOf(true);
                                ClockUtils.insertSaLog("134", "1151", "1");
                            } else {
                                active = Boolean.valueOf(false);
                                ClockUtils.insertSaLog("134", "1151", "0");
                            }
                            AlarmDataHandler.setAlarmActive(context, listItemId, active.booleanValue(), alarmItem.mActivate);
                            if ((active.booleanValue() ? 1 : null) == null) {
                                AlarmNotificationHelper.clearNotification(context, listItemId);
                            }
                        }
                        updateClock(context, AppWidgetManager.getInstance(context));
                        break;
                    case 6:
                        if (!bEnterEasyMode) {
                            updateClock(context, AppWidgetManager.getInstance(context));
                            break;
                        }
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        updateClock(context, AppWidgetManager.getInstance(context));
                        break;
                    case 13:
                        int updateWidgetId = intent.getIntExtra("appWidgetId", -1);
                        if (updateWidgetId == -1) {
                            updateClock(context, AppWidgetManager.getInstance(context));
                            break;
                        } else {
                            updateViews(context, AppWidgetManager.getInstance(context), updateWidgetId);
                            break;
                        }
                    default:
                        Log.secE("TAG", "onReceive() -  meaningless intent");
                        break;
                }
                super.onReceive(context, intent);
            } catch (IllegalStateException e2) {
                Log.secE("AlarmWidget_Provider", "getAppWidgetIds IllegalStateException e = " + e2);
            }
        }
    }

    private static void updateClock(Context context, AppWidgetManager aw) {
        Log.secD("AlarmWidget_Provider", "alarmWidget_d :: updateClock()");
        ComponentName provider = new ComponentName(context.getPackageName(), ClockAlarmWidgetProvider.class.getName());
        Log.secD("AlarmWidget_Provider", "isDCM()=" + Feature.isDCM(context) + "/isSamsungHome()=" + AppWidgetUtils.isSamsungHome());
        int[] widgetIds = aw.getAppWidgetIds(provider);
        sTotalWidgetCnt = widgetIds.length;
        for (int i = 0; i < sTotalWidgetCnt; i++) {
            updateViews(context, aw, widgetIds[i]);
        }
    }

    private static void updateViews(Context context, AppWidgetManager awm, int widgetId) {
        int itemIndex = ClockAlarmWidgetIdManager.getListItem(context, widgetId, 0);
        int listItemId = ClockAlarmWidgetIdManager.getListItem(context, widgetId, itemIndex + 1);
        Log.secD("AlarmWidget_Provider", "setAlarmData() / widgetId=" + widgetId + "/itemIndex=" + itemIndex + "/listItemId=" + listItemId);
        RemoteViews view = updateAppWidgetView(context, widgetId, listItemId);
        if (view != null) {
            awm.updateAppWidget(widgetId, view);
        }
    }

    private static RemoteViews updateAppWidgetView(Context context, int appWidgetId, int listItemID) {
        ViewModel viewModel = new ClockAlarmWidgetViewModel(ClockAlarmWidgetDataManager.loadModel(context, appWidgetId, listItemID));
        viewModel.refresh(context, appWidgetId, listItemID, false);
        if (viewModel.getRemoteViews() != null) {
            return viewModel.getRemoteViews();
        }
        return null;
    }
}
