package com.sec.android.app.clockpackage.alarmwidget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;

public class ClockAlarmWidgetDataManager {
    public static ClockAlarmWidgetModel loadModel(Context context, int appWidgetId, int listItemId) {
        boolean isEmpty;
        boolean isActive = true;
        ViewModelHelper viewModelHelper = new ViewModelHelper(context, appWidgetId);
        AlarmItem alarmItem = AlarmProvider.getAlarm(context, listItemId);
        if (alarmItem == null && listItemId != 0) {
            ClockAlarmWidgetIdManager.setListItem(context, appWidgetId, 0);
            ClockAlarmWidgetIdManager.setWidgetId(context, appWidgetId, 0);
        }
        if (alarmItem == null) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        if (alarmItem == null || alarmItem.mActivate == 0) {
            isActive = false;
        }
        ClockAlarmWidgetModel model = new ClockAlarmWidgetModel();
        model.setTransparency(255 - viewModelHelper.getTransparency(2));
        model.setBackgroundColor(getBackgroundColor(context, viewModelHelper.getTheme(2)));
        boolean isDarkFont = WidgetSettingUtils.isDarkFont(context, viewModelHelper.getTheme(2), viewModelHelper.getTransparency(2));
        model.setIsEmpty(isEmpty);
        model.setIsActivate(isActive);
        model.setIsDartFont(isDarkFont);
        if (alarmItem != null) {
            model.setAlarmRepeatIndex(alarmItem.getAlarmRepeat());
        }
        model.setNameAndDateTextColor(getAlarmNameAndDateTextFontColor(context, isDarkFont, isActive));
        model.setTimeAndAmPmTextColor(getAlarmTimeAndAmPmTextFontColor(context, isDarkFont, isActive));
        model.setNoItemTextColor(getNoItemTextFontColor(context, isDarkFont));
        model.setmOnOffImageColor(getOnOffImageColor(isDarkFont, isActive));
        setWidgetSize(model, getWidgetType(context, appWidgetId));
        setTransparency(context, model, viewModelHelper.getTheme(2), viewModelHelper.getTransparency(2));
        return model;
    }

    private static int getBackgroundColor(Context context, int theme) {
        return ContextCompat.getColor(context, theme == 1 ? R.color.widget_dark_bg_color : R.color.widget_light_bg_color);
    }

    private static int getAlarmNameAndDateTextFontColor(Context context, boolean isDarkFont, boolean isActive) {
        if (isActive) {
            return ContextCompat.getColor(context, isDarkFont ? R.color.alarm_widget_on_name_date_wbg : R.color.alarm_widget_on_name_date);
        }
        return ContextCompat.getColor(context, isDarkFont ? R.color.alarm_widget_off_name_date_wbg : R.color.alarm_widget_off_name_date);
    }

    private static int getAlarmTimeAndAmPmTextFontColor(Context context, boolean isDarkFont, boolean isActive) {
        if (isActive) {
            return ContextCompat.getColor(context, isDarkFont ? R.color.alarm_widget_on_time_ampm_wbg : R.color.alarm_widget_on_time_ampm);
        }
        return ContextCompat.getColor(context, isDarkFont ? R.color.alarm_widget_off_time_ampm_wbg : R.color.alarm_widget_off_time_ampm);
    }

    private static int getNoItemTextFontColor(Context context, boolean isDarkFont) {
        return ContextCompat.getColor(context, isDarkFont ? R.color.widget_no_item_wbg : R.color.widget_no_item);
    }

    private static int getOnOffImageColor(boolean isDarkFont, boolean isActive) {
        return isActive ? isDarkFont ? R.drawable.alarm_widget_icon_on_wbg : R.drawable.alarm_widget_icon_on : isDarkFont ? R.drawable.alarm_widget_icon_off_wbg : R.drawable.alarm_widget_icon_off;
    }

    private static int getWidgetType(Context context, int appWidgetId) {
        return AppWidgetUtils.getWidgetSize(context, appWidgetId);
    }

    public static void setTransparency(Context context, ClockAlarmWidgetModel model, int theme, int transparency) {
        boolean isDarkFont = WidgetSettingUtils.isDarkFont(context, theme, transparency);
        model.setNameAndDateTextColor(getAlarmNameAndDateTextFontColor(context, isDarkFont, model.getIsActivate()));
        model.setTimeAndAmPmTextColor(getAlarmTimeAndAmPmTextFontColor(context, isDarkFont, model.getIsActivate()));
        model.setNoItemTextColor(getNoItemTextFontColor(context, isDarkFont));
        model.setBackgroundColor(getBackgroundColor(context, theme));
        model.setIsDartFont(isDarkFont);
        model.setTransparency(255 - transparency);
        model.setmOnOffImageColor(getOnOffImageColor(isDarkFont, model.getIsActivate()));
    }

    private static void setWidgetSize(ClockAlarmWidgetModel model, int widgetSize) {
        model.setWidgetSize(widgetSize);
    }
}
