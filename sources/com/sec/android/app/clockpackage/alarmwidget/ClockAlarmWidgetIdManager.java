package com.sec.android.app.clockpackage.alarmwidget;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.sec.android.app.clockpackage.common.util.Log;

public class ClockAlarmWidgetIdManager {
    public static void setWidgetId(Context context, int widgetId, int alarmId) {
        context.getSharedPreferences("AlarmWidgetIDs", 0).edit().putInt("AlarmWidget_Indices" + alarmId, widgetId).apply();
    }

    private static int getListItemCount(Context context, int widgetId) {
        return context.getSharedPreferences("AlarmWidgetIDs", 0).getInt("AlarmWidgetIDs" + widgetId + "_ListItem_Count", 0);
    }

    private static int increaseListItemCount(Context context, int widgetId) {
        Editor edit = context.getSharedPreferences("AlarmWidgetIDs", 0).edit();
        int listCount = getListItemCount(context, widgetId);
        if (listCount >= 1) {
            Log.secE("AlarmWidget_IDMGR", "setListItem() - listCount exceed MAX_LIST_ITEM, listCount:" + listCount);
            listCount = 1;
        } else {
            listCount++;
        }
        Log.secE("AlarmWidget_IDMGR", "increaseListItemCount() - widgetId:" + widgetId + ", listCount:" + listCount);
        edit.putInt("AlarmWidgetIDs" + widgetId + "_ListItem_Count", listCount).apply();
        return listCount;
    }

    public static int getListItem(Context context, int widgetId, int itemIndex) {
        if (itemIndex > getListItemCount(context, widgetId)) {
            Log.secE("AlarmWidget_IDMGR", "getListItem() : ItemIndex exceed ListItemCount. itemIndex=" + itemIndex);
        }
        Log.secE("AlarmWidget_IDMGR", "getListItem() : itemIndex=" + itemIndex + ", widgetId:" + widgetId);
        return context.getSharedPreferences("AlarmWidgetIDs", 0).getInt("AlarmWidgetIDs" + widgetId + "_ListItemID" + itemIndex, 0);
    }

    public static void setListItem(Context context, int widgetId, int listItemID) {
        Editor edit = context.getSharedPreferences("AlarmWidgetIDs", 0).edit();
        int currentListCount = increaseListItemCount(context, widgetId);
        Log.secE("AlarmWidget_IDMGR", "currentListCount = " + currentListCount);
        edit.putInt("AlarmWidgetIDs" + widgetId + "_ListItemID" + currentListCount, listItemID).apply();
    }

    public static int getDuplicateWidgetId(Context context, int widgetId, int duplicateID) {
        return context.getSharedPreferences("AlarmWidgetIDs", 0).getInt(new StringBuilder().append("AlarmWidgetIDs").append(widgetId).append("_ListItemID").append(increaseListItemCount(context, widgetId)).toString(), -1) == duplicateID ? widgetId : -1;
    }

    public static void removeItem(Context ctx, int widgetId) {
        Editor edit = ctx.getSharedPreferences("AlarmWidgetIDs", 0).edit();
        int alarmId = getAlarmId(ctx, widgetId);
        int currentListCount = increaseListItemCount(ctx, widgetId);
        Log.secD("AlarmWidget_IDMGR", "JavaE : removeItem() \nkey1= AlarmWidget_Indices" + alarmId + "key2= " + "AlarmWidgetIDs" + widgetId + "_ListItemID" + currentListCount);
        edit.remove("AlarmWidget_Indices" + alarmId).apply();
        edit.remove("AlarmWidgetIDs" + widgetId + "_ListItemID" + currentListCount).apply();
        edit.remove("AlarmWidgetIDs" + widgetId + "_ListItem_Count").apply();
    }

    private static int getAlarmId(Context context, int widgetId) {
        return getListItem(context, widgetId, getListItem(context, widgetId, 0) + 1);
    }

    public static int getBnRAlarmWidgetData(Context ctx, int widgetId) {
        int alarmId = ctx.getSharedPreferences("BNR_ALARM_WIDGET", 0).getInt(Integer.toString(widgetId), -1);
        Log.secD("BNR_CLOCK_ALARM", "bnr_alarmwidget (**) : getBnRAlarmWidget() / widgetId=" + widgetId + "/alarmId=" + alarmId);
        return alarmId;
    }

    public static void removeBnRAlarmWidgetData(Context ctx, int widgetId) {
        Log.secD("BNR_CLOCK_ALARM", "bnr_alarmwidget : removeBnRAlarmWidget() / widgetId=" + widgetId);
        ctx.getSharedPreferences("BNR_ALARM_WIDGET", 0).edit().remove(Integer.toString(widgetId)).apply();
    }
}
