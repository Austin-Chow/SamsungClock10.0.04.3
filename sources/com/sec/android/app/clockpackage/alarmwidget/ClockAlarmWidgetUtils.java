package com.sec.android.app.clockpackage.alarmwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClockAlarmWidgetUtils {
    public static int getAlarmItemCount(Context context) {
        Cursor c = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, null, null, null);
        if (c == null) {
            return 0;
        }
        int count = c.getCount();
        c.close();
        return count;
    }

    public static String getAlarmName(Context context, AlarmItem alarmItem) {
        if (alarmItem.mAlarmName.length() == 0) {
            return context.getResources().getString(R.string.alarm);
        }
        return alarmItem.mAlarmName;
    }

    public static String getAmPmString(AlarmItem alarmItem) {
        String[] amPmTexts = new DateFormatSymbols().getAmPmStrings();
        return alarmItem.mAlarmTime / 100 >= 12 ? amPmTexts[1] : amPmTexts[0];
    }

    public static String getAlarmTime(Context context, AlarmItem alarmItem) {
        String hourStr;
        boolean b24HourFormat = DateFormat.is24HourFormat(context);
        String colon = ClockUtils.getTimeSeparatorText(context);
        int alarmTime = alarmItem.mAlarmTime;
        int hour = alarmTime / 100;
        int minute = alarmTime % 100;
        if (b24HourFormat) {
            hourStr = ClockUtils.toTwoDigitString(hour);
        } else if (hour % 12 != 0) {
            int nhour = hour;
            hourStr = ClockUtils.toTwoDigitString(hour % 12);
            if (hour > 12) {
                nhour -= 12;
            }
            if (nhour / 10 == 0) {
                hourStr = ClockUtils.toDigitString(nhour % 10);
            }
        } else if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
            hourStr = ClockUtils.toDigitString(0);
        } else {
            hourStr = ClockUtils.toTwoDigitString(12);
        }
        return hourStr + colon + ClockUtils.toTwoDigitString(minute);
    }

    public static String calculateNotidaysAndSetText(Context mContext, AlarmItem alarmItem, boolean isTTS) {
        int alarmTime = alarmItem.mAlarmTime;
        int hourValue = alarmTime / 100;
        int minValue = alarmTime % 100;
        Calendar curCalendar = Calendar.getInstance();
        long currentTime = curCalendar.getTimeInMillis();
        int curDay = curCalendar.get(7);
        Log.secD("ClockAlarmWidgetUtils", "calculateNotidaysAndSetText() checkDay = " + 0 + ", hourValue = " + hourValue);
        if (alarmItem.mAlarmAlertTime < currentTime) {
            alarmItem.calculateOriginalAlertTime();
        }
        Log.secD("ClockAlarmWidgetUtils", "alarmItem.mAlarmAlertTime < currentTime " + String.valueOf(alarmItem.mAlarmAlertTime) + " < " + String.valueOf(currentTime));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmItem.mAlarmAlertTime);
        int mAlarmYear = calendar.get(1);
        int mAlarmMonth = calendar.get(2);
        int mAlarmDay = calendar.get(5);
        long settingTime = calendar.getTimeInMillis();
        Log.secD("ClockAlarmWidgetUtils", "calculateNotidaysAndSetText() -mAlarmYear=" + mAlarmYear + ", mAlarmMonth=" + mAlarmMonth + ", mAlarmDay = " + mAlarmDay);
        boolean isSnoozeAlarm = false;
        if (alarmItem.mSnoozeActivate && alarmItem.mSnoozeDoneCount < alarmItem.getSnoozeRepeatTimes()) {
            isSnoozeAlarm = alarmItem.mActivate != 0;
        }
        if (settingTime < currentTime && !isSnoozeAlarm) {
            calendar.add(5, 1);
            settingTime = calendar.getTimeInMillis();
        }
        calendar.setTimeInMillis(settingTime);
        Log.secD("ClockAlarmWidgetUtils", "days = " + ((int) (((settingTime - ((currentTime / 60000) * 60000)) / 3600000) / 24)) + ", setAlarmDay = " + calendar.get(7) + ", curDay = " + curDay);
        Log.secD("ClockAlarmWidgetUtils", "isTTS=" + isTTS);
        String nextAlarmNotidays = ClockUtils.getFormatDateTime(mContext, settingTime, isTTS);
        Log.secD("ClockAlarmWidgetUtils", ", mHour = " + hourValue + ", mMin = " + minValue + "/ nextAlarmNotidays=" + nextAlarmNotidays);
        return nextAlarmNotidays;
    }

    public static String getTimeText(Context context, int hour, int minute) {
        String hourStr;
        StringBuilder alarmTimeText = new StringBuilder();
        String amPm = "";
        if (DateFormat.is24HourFormat(context)) {
            hourStr = ClockUtils.toTwoDigitString(hour);
        } else {
            if (hour % 12 != 0) {
                hourStr = ClockUtils.toDigitString(hour % 12);
            } else if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
                hourStr = ClockUtils.toDigitString(0);
            } else {
                hourStr = ClockUtils.toDigitString(12);
            }
            String[] amPmTexts = new DateFormatSymbols().getAmPmStrings();
            String amText = amPmTexts[0];
            String pmText = amPmTexts[1];
            if (hour >= 12) {
                amPm = pmText;
            } else {
                amPm = amText;
            }
        }
        String minuteStr = ClockUtils.toTwoDigitString(minute);
        Log.secD("ClockAlarmWidgetUtils", "widget_tts getTimeText / hour=" + hour + "/hourStr=" + hourStr + "/minute=" + minute + "/minuteStr=" + minuteStr + "/amPm" + amPm);
        if (Locale.getDefault().getLanguage().equals(Locale.KOREA.getLanguage())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
            Date date = null;
            try {
                date = simpleDateFormat.parse(hourStr + ':' + minuteStr);
            } catch (Exception e) {
                Log.secE("ClockAlarmWidgetUtils", "Exception : " + e.toString());
            }
            if (date != null) {
                alarmTimeText = alarmTimeText.append(amPm).append(simpleDateFormat.format(date));
            } else {
                alarmTimeText = alarmTimeText.append(amPm).append(' ').append(hourStr).append(' ').append(minuteStr);
            }
        } else if (hour <= 1 && minute == 0) {
            alarmTimeText = alarmTimeText.append(hourStr).append(' ').append(context.getResources().getString(R.string.timer_hour)).append(' ').append(amPm);
        } else if (hour <= 1 && minute == 1) {
            alarmTimeText = alarmTimeText.append(hourStr).append(' ').append(context.getResources().getString(R.string.timer_hour)).append(' ').append(minuteStr).append(' ').append(context.getResources().getString(R.string.timer_minute)).append(' ').append(amPm);
        } else if (hour <= 1) {
            alarmTimeText = alarmTimeText.append(hourStr).append(' ').append(context.getResources().getString(R.string.timer_hour)).append(' ').append(minuteStr).append(' ').append(context.getResources().getString(R.string.timer_min)).append(' ').append(amPm);
        } else if (minute == 1) {
            alarmTimeText = alarmTimeText.append(hourStr).append(' ').append(context.getResources().getString(R.string.timer_hr)).append(' ').append(minuteStr).append(' ').append(context.getResources().getString(R.string.timer_minute)).append(' ').append(amPm);
        } else if (minute == 0) {
            alarmTimeText = alarmTimeText.append(hourStr).append(' ').append(context.getResources().getString(R.string.timer_hr)).append(' ').append(amPm);
        } else {
            alarmTimeText = alarmTimeText.append(hourStr).append(' ').append(context.getResources().getString(R.string.timer_hr)).append(' ').append(minuteStr).append(' ').append(context.getResources().getString(R.string.timer_min)).append(' ').append(amPm);
        }
        Log.secD("ClockAlarmWidgetUtils", "widget_tts alarmTimeText=" + alarmTimeText);
        return alarmTimeText.toString();
    }

    public static Spannable getRepeatDaysString(Context context, int repeatDays, int activate, boolean isTTS, boolean isNeedDarkFont) {
        String letterSpace = "  ";
        if (StateUtils.getScreenDensityIndex(context) >= 2 || Feature.isTablet(context)) {
            letterSpace = " ";
        }
        int startDay = ClockUtils.getStartDayOfWeek();
        boolean isMirroringEnabled = StateUtils.isMirroringEnabled();
        SpannableStringBuilder str = new SpannableStringBuilder();
        for (int count = 0; count < 7; count++) {
            int dayOfWeek;
            if (isMirroringEnabled) {
                dayOfWeek = (((startDay - 1) + 6) - count) % 7;
            } else {
                dayOfWeek = ((startDay - 1) + count) % 7;
            }
            int isRepeatDay = (repeatDays >> ((6 - dayOfWeek) * 4)) & 15;
            if (!isTTS) {
                int repeatColor;
                String dayOfWeekString = ClockUtils.getDayOfWeekString(context, dayOfWeek + 1, 0);
                int startPos = str.length();
                str.append(dayOfWeekString).append(letterSpace);
                if (activate == 0) {
                    if (isRepeatDay <= 0) {
                        repeatColor = isNeedDarkFont ? R.color.alarm_widget_off_repeat_day_inactive_wbg : R.color.alarm_widget_off_repeat_day_inactive;
                    } else if (isNeedDarkFont) {
                        repeatColor = R.color.alarm_widget_off_repeat_day_active_wbg;
                    } else {
                        repeatColor = R.color.alarm_widget_off_repeat_day_active;
                    }
                } else if (isRepeatDay > 0) {
                    repeatColor = isNeedDarkFont ? R.color.alarm_widget_on_repeat_day_active_wbg : R.color.alarm_widget_on_repeat_day_active;
                } else {
                    repeatColor = isNeedDarkFont ? R.color.alarm_widget_on_repeat_day_inactive_wbg : R.color.alarm_widget_on_repeat_day_inactive;
                }
                str.setSpan(new StyleSpan(isRepeatDay > 0 ? 1 : 0), startPos, dayOfWeekString.length() + startPos, 33);
                str.setSpan(new ForegroundColorSpan(context.getColor(repeatColor)), startPos, dayOfWeekString.length() + startPos, 33);
            } else if (isRepeatDay > 0) {
                str.append(", ").append(ClockUtils.getDayOfWeekString(context, dayOfWeek + 1, 3));
            }
        }
        if (isTTS) {
            return str;
        }
        return (SpannableStringBuilder) str.subSequence(0, str.length() - letterSpace.length());
    }

    public static PendingIntent getPendingIntentWidgetId(Context context, String action, int appWidgetId, int listItemID) {
        return PendingIntent.getBroadcast(context, appWidgetId, getIntentWidgetId(action, appWidgetId, listItemID), 0);
    }

    public static Intent getIntentWidgetId(String action, int appWidgetId, int listItemID) {
        Intent intent = new Intent();
        intent.setPackage("com.sec.android.app.clockpackage");
        intent.setAction(action);
        if (action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_EDIT")) {
            intent.addFlags(32768);
        }
        intent.putExtra("widgetId", appWidgetId);
        if (!action.equals("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW")) {
            intent.putExtra("ListItemPosition", listItemID);
        }
        return intent;
    }
}
