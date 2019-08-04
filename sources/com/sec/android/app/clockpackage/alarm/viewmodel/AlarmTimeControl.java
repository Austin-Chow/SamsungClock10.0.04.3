package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.widget.TextView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Calendar;

public class AlarmTimeControl {
    public final Calendar mAlertDateCalendar = Calendar.getInstance();
    private final Context mContext;
    public int mHourValue = 6;
    public int mMinValue = 0;
    public long mNextAlertTimeMillis;
    public int mRepeatDateWorkingState;

    public AlarmTimeControl(Context context) {
        this.mContext = context;
    }

    public boolean setAlertDateToDefault() {
        boolean isTodayAlert = true;
        Calendar alertDateCalendar = Calendar.getInstance();
        alertDateCalendar.set(11, this.mHourValue);
        alertDateCalendar.set(12, this.mMinValue);
        alertDateCalendar.set(13, 0);
        alertDateCalendar.set(14, 0);
        if (alertDateCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            alertDateCalendar.add(5, 1);
            isTodayAlert = false;
        }
        this.mNextAlertTimeMillis = alertDateCalendar.getTimeInMillis();
        this.mAlertDateCalendar.setTimeInMillis(this.mNextAlertTimeMillis);
        return isTodayAlert;
    }

    public int getCheckDayForDateAlarm() {
        if (this.mRepeatDateWorkingState != 2) {
            return 0;
        }
        int tempRepeatType = 0 & 15;
        int alarmRepeatData = (0 | ((((((1 << (((7 - this.mAlertDateCalendar.get(7)) + 1) * 4)) & -16) | 0) >> 4) << 4) & -16)) | 1;
        Log.secD("AlarmTimeControl", "getCheckDayForDateAlarm() - result = 0x" + Integer.toHexString(alarmRepeatData));
        return alarmRepeatData;
    }

    public boolean setAlertDateByCalendar(int year, int monthOfYear, int dayOfMonth) {
        boolean isDateNotinThePast = true;
        this.mAlertDateCalendar.set(year, monthOfYear, dayOfMonth, this.mHourValue, this.mMinValue, 0);
        this.mAlertDateCalendar.set(14, 0);
        this.mRepeatDateWorkingState = 2;
        if (this.mAlertDateCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            this.mRepeatDateWorkingState = 0;
            Log.secI("AlarmTimeControl", "setAlertDateByCalendar() selected the past time!!");
            this.mAlertDateCalendar.setTimeInMillis(System.currentTimeMillis());
            this.mAlertDateCalendar.add(5, 1);
            isDateNotinThePast = false;
        }
        this.mNextAlertTimeMillis = this.mAlertDateCalendar.getTimeInMillis();
        return isDateNotinThePast;
    }

    public void getAlarmAlertDateText(TextView textView) {
        StringBuilder nextAlarmNotidays = new StringBuilder();
        StringBuilder alarmNotidaysContentDescription = new StringBuilder();
        this.mAlertDateCalendar.setTimeInMillis(this.mNextAlertTimeMillis);
        this.mAlertDateCalendar.set(11, this.mHourValue);
        this.mAlertDateCalendar.set(12, this.mMinValue);
        this.mNextAlertTimeMillis = this.mAlertDateCalendar.getTimeInMillis();
        if (this.mRepeatDateWorkingState != 2 || this.mNextAlertTimeMillis <= System.currentTimeMillis()) {
            if (this.mRepeatDateWorkingState == 2) {
                this.mRepeatDateWorkingState = 0;
            }
            if (setAlertDateToDefault()) {
                nextAlarmNotidays.append(this.mContext.getString(C0490R.string.today));
            } else {
                nextAlarmNotidays.append(this.mContext.getString(C0490R.string.tomorrow));
            }
            nextAlarmNotidays.append("-");
            alarmNotidaysContentDescription.append(nextAlarmNotidays.toString());
        } else {
            Calendar currentCalendar = Calendar.getInstance();
            if (currentCalendar.get(1) >= this.mAlertDateCalendar.get(1)) {
                int currentDayOfYear = currentCalendar.get(6);
                int currentTime = (currentCalendar.get(11) * 60) + currentCalendar.get(12);
                int alertDayOfYear = this.mAlertDateCalendar.get(6);
                int alertTime = (this.mHourValue * 60) + this.mMinValue;
                if (currentDayOfYear == alertDayOfYear && currentTime <= alertTime) {
                    nextAlarmNotidays.append(this.mContext.getString(C0490R.string.today));
                    nextAlarmNotidays.append("-");
                    alarmNotidaysContentDescription.append(nextAlarmNotidays.toString());
                } else if (currentDayOfYear + 1 == alertDayOfYear) {
                    nextAlarmNotidays.append(this.mContext.getString(C0490R.string.tomorrow));
                    nextAlarmNotidays.append("-");
                    alarmNotidaysContentDescription.append(nextAlarmNotidays.toString());
                }
            }
        }
        nextAlarmNotidays.append(notiDate(this.mNextAlertTimeMillis, false));
        textView.setText(nextAlarmNotidays.toString());
        alarmNotidaysContentDescription.append(notiDate(this.mNextAlertTimeMillis, true));
        textView.setContentDescription(alarmNotidaysContentDescription.toString());
    }

    private String notiDate(long settingTime, boolean isTTS) {
        return ClockUtils.getFormatDateTime(this.mContext, settingTime, isTTS);
    }
}
