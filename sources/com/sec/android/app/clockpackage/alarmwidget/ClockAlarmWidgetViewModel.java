package com.sec.android.app.clockpackage.alarmwidget;

import android.content.Context;
import android.text.Spannable;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetContract.View;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetContract.ViewModel;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.Locale;

public class ClockAlarmWidgetViewModel implements ViewModel {
    private ClockAlarmWidgetModel mModel;
    private View mView;

    public ClockAlarmWidgetViewModel(ClockAlarmWidgetModel model) {
        this.mModel = model;
        attachView(new ClockAlarmWidgetView());
    }

    public void refresh(Context context, int appWidgetId, int listItemId, boolean isSetting) {
        refresh(context, appWidgetId, listItemId, this.mModel, isSetting);
    }

    public void refresh(Context context, int appWidgetId, int listItemId, ClockAlarmWidgetModel model, boolean isSetting) {
        this.mView.inflate(context, context.getPackageName(), model.getWidgetSize(), isSetting);
        Log.secD("ClockAlarmWidgetViewModel", "refresh : appWidgetId = " + appWidgetId);
        updateClockAlarmWidgetView(context, appWidgetId, listItemId);
        this.mView.setBackgroundColorFilter(model.getBackgroundColor());
        this.mView.setBackgroundImageAlpha(model.getTransparency());
        this.mView.setImageColorFilter(model.getOnOffImageColor(), model.getIsActivate());
        this.mView.setTextColor(model.getNameAndDateTextColor(), model.getTimeAndAmPmTextColor(), model.getNoItemTextColor());
        if (!isSetting) {
            if (model.getIsEmpty()) {
                this.mView.setWidgetClickPendingIntent(ClockAlarmWidgetUtils.getPendingIntentWidgetId(context, "com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW", appWidgetId, listItemId), null, model.getIsEmpty());
            } else {
                this.mView.setWidgetClickPendingIntent(ClockAlarmWidgetUtils.getPendingIntentWidgetId(context, "com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_EDIT", appWidgetId, listItemId), ClockAlarmWidgetUtils.getPendingIntentWidgetId(context, "com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ONOFF", appWidgetId, listItemId), model.getIsEmpty());
            }
        }
    }

    public void setTransparency(Context context, int theme, int transparency) {
        ClockAlarmWidgetDataManager.setTransparency(context, this.mModel, theme, transparency);
    }

    public RemoteViews getRemoteViews() {
        return this.mView.getRemoteViews();
    }

    public void attachView(View viewCallback) {
        this.mView = viewCallback;
    }

    public void updateClockAlarmWidgetView(Context context, int widgetId, int listItemId) {
        AlarmItem alarmItem = AlarmProvider.getAlarm(context, listItemId);
        if (Feature.DEBUG_ENG) {
            Log.secD("ClockAlarmWidgetViewModel", "setAlarmData() / alarmItem = " + alarmItem);
        }
        if (alarmItem == null) {
            getRemoteViews().setViewVisibility(R.id.alarm_noitem, 0);
            getRemoteViews().setViewVisibility(R.id.alarm_item, 8);
            getRemoteViews().setContentDescription(R.id.alarm_noitem, context.getResources().getString(R.string.tap_to_create_alarm) + context.getResources().getString(R.string.button));
            return;
        }
        getRemoteViews().setViewVisibility(R.id.alarm_noitem, 8);
        getRemoteViews().setViewVisibility(R.id.alarm_item, 0);
        getRemoteViews().setViewVisibility(R.id.alarm_name, 0);
        getRemoteViews().setViewVisibility(R.id.alarm_time, 0);
        getRemoteViews().setViewVisibility(R.id.alarm_ampm, 8);
        getRemoteViews().setViewVisibility(R.id.alarm_ampm_kor, 8);
        if (Feature.DEBUG_ENG) {
            Log.secD("ClockAlarmWidgetViewModel", "AlarmItem mAlarmName: " + alarmItem.mAlarmName);
            Log.secD("ClockAlarmWidgetViewModel", "AlarmItem mAlarmTime: " + alarmItem.mAlarmTime);
            Log.secD("ClockAlarmWidgetViewModel", "AlarmItem mRepeatType: " + alarmItem.mRepeatType);
            Log.secD("ClockAlarmWidgetViewModel", "AlarmItem mActivate:" + alarmItem.mActivate);
        }
        String alarmName = ClockAlarmWidgetUtils.getAlarmName(context, alarmItem);
        String alarmTimeString = ClockAlarmWidgetUtils.getAlarmTime(context, alarmItem);
        String amPmString = ClockAlarmWidgetUtils.getAmPmString(alarmItem);
        boolean isWorkingDay = Feature.isSupportAlarmOptionMenuForWorkingDay() && alarmItem.isWorkingDay();
        updateClockAlarmWidgetData(context, getRemoteViews(), alarmName, alarmTimeString, amPmString, alarmItem.mAlarmTime, isWorkingDay, alarmItem.isWeeklyAlarm(), alarmItem.mActivate, ClockAlarmWidgetUtils.calculateNotidaysAndSetText(context, alarmItem, false), ClockAlarmWidgetUtils.calculateNotidaysAndSetText(context, alarmItem, true), alarmItem.getAlarmRepeat());
    }

    public void updateClockAlarmWidgetData(Context context, RemoteViews views, String alarmName, String alarmTimeString, String amPmString, int mAlarmTime, boolean isWorkingDay, boolean isWeeklyAlarm, int mActivate, String alarmNotiDays, String alarmNotiDaysTTS, int alarmRepeat) {
        views.setTextViewText(R.id.alarm_name, alarmName);
        views.setTextViewText(R.id.alarm_time, alarmTimeString);
        StringBuilder readDescription = new StringBuilder().append(alarmName);
        if (!DateFormat.is24HourFormat(context)) {
            int ampmTextViewId = R.id.alarm_ampm;
            if (StateUtils.isLeftAmPm()) {
                ampmTextViewId = R.id.alarm_ampm_kor;
            }
            if (Locale.getDefault().getLanguage().equals(Locale.CHINA.getLanguage())) {
                int smallestScreenWidthDp = context.getResources().getConfiguration().smallestScreenWidthDp;
                int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
                if (smallestScreenWidthDp == 360 && densityDpi == 240) {
                    views.setFloat(ampmTextViewId, "setTextSize", 16.0f);
                }
            }
            if (amPmString.length() > 5) {
                int screenZoomIndex = StateUtils.getScreenDensityIndex(context);
                Log.secD("ClockAlarmWidgetViewModel", "screenZoomIndex = " + screenZoomIndex + "amPm = " + amPmString.length());
                if (screenZoomIndex == 2) {
                    views.setTextViewTextSize(ampmTextViewId, 1, 10.0f);
                }
            }
            Locale locale = Locale.getDefault();
            if ("GB".equals(locale.getCountry())) {
                views.setTextViewText(ampmTextViewId, amPmString.toLowerCase(locale));
            }
            views.setTextViewText(ampmTextViewId, amPmString);
            views.setViewVisibility(ampmTextViewId, 0);
        }
        readDescription = readDescription.append(", ").append(ClockAlarmWidgetUtils.getTimeText(context, mAlarmTime / 100, mAlarmTime % 100));
        if (!isWeeklyAlarm || isWorkingDay) {
            views.setViewVisibility(R.id.alarm_repeat_days, 8);
            views.setViewVisibility(R.id.alarm_date, 0);
            if (isWorkingDay) {
                String workdays = context.getResources().getString(R.string.alarm_workdays);
                views.setTextViewText(R.id.alarm_date, workdays);
                readDescription = readDescription.append(", ").append(workdays);
            } else if (alarmNotiDays != null) {
                views.setTextViewText(R.id.alarm_date, alarmNotiDays);
                readDescription = readDescription.append(", ").append(alarmNotiDaysTTS);
            }
        } else {
            views.setViewVisibility(R.id.alarm_date, 8);
            views.setViewVisibility(R.id.alarm_repeat_days, 0);
            Spannable span = ClockAlarmWidgetUtils.getRepeatDaysString(context, alarmRepeat, mActivate, false, this.mModel.getIsDartFont());
            views.setCharSequence(R.id.alarm_repeat_days, "setText", span);
            readDescription = readDescription.append(", ").append(context.getResources().getString(R.string.alarmrepeat)).append(ClockAlarmWidgetUtils.getRepeatDaysString(context, alarmRepeat, mActivate, true, this.mModel.getIsDartFont()).toString());
        }
        if (mActivate == 1 || mActivate == 2) {
            views.setContentDescription(R.id.alarm_onoff_btn, context.getResources().getString(R.string.alarm_turn_off) + ", " + readDescription);
            readDescription = readDescription.append(", ").append(context.getResources().getString(R.string.alarm_on));
        } else {
            views.setContentDescription(R.id.alarm_onoff_btn, context.getResources().getString(R.string.alarm_turn_on) + ", " + readDescription);
            readDescription = readDescription.append(", ").append(context.getResources().getString(R.string.alarm_off));
        }
        views.setContentDescription(R.id.alarm_item, readDescription);
    }
}
