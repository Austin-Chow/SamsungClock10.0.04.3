package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.view.inputmethod.InputMethodManager;
import com.sec.android.app.clockpackage.timer.C0728R;

public class TimerUtils {
    private static String getDescriptionStringMinuteSecond(Resources r, int minute, int second) {
        switch (minute) {
            case 0:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_second);
                }
                return r.getString(C0728R.string.timer_alert_passed_time_seconds, new Object[]{Integer.valueOf(second)});
            case 1:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_minute_second);
                }
                return r.getString(C0728R.string.timer_alert_passed_time_minute_seconds, new Object[]{Integer.valueOf(second)});
            default:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_minutes_second, new Object[]{Integer.valueOf(minute)});
                }
                return r.getString(C0728R.string.timer_alert_passed_time_minutes_seconds, new Object[]{Integer.valueOf(minute), Integer.valueOf(second)});
        }
    }

    private static String getDescriptionStringOneHourMinuteSecond(Resources r, int minute, int second) {
        switch (minute) {
            case 0:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_hour_minutes_second, new Object[]{Integer.valueOf(minute)});
                }
                return r.getString(C0728R.string.timer_alert_passed_time_hour_minutes_seconds, new Object[]{Integer.valueOf(minute), Integer.valueOf(second)});
            case 1:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_hour_minute_second);
                }
                return r.getString(C0728R.string.timer_alert_passed_time_hour_minute_seconds, new Object[]{Integer.valueOf(second)});
            default:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_hour_minutes_second, new Object[]{Integer.valueOf(minute)});
                }
                return r.getString(C0728R.string.timer_alert_passed_time_hour_minutes_seconds, new Object[]{Integer.valueOf(minute), Integer.valueOf(second)});
        }
    }

    private static String getDescriptionStringHoursMinuteSecond(Resources r, int hour, int minute, int second) {
        switch (minute) {
            case 1:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_hours_minute_second, new Object[]{Integer.valueOf(hour)});
                }
                return r.getString(C0728R.string.timer_alert_passed_time_hours_minute_seconds, new Object[]{Integer.valueOf(hour), Integer.valueOf(second)});
            default:
                if (second == 1) {
                    return r.getString(C0728R.string.timer_alert_passed_time_hours_minutes_second, new Object[]{Integer.valueOf(hour), Integer.valueOf(minute)});
                }
                return r.getString(C0728R.string.timer_alert_passed_time_hours_minutes_seconds, new Object[]{Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second)});
        }
    }

    public static String getDescriptionString(Context context, int hour, int minute, int second) {
        Resources r = context.getResources();
        if (hour == 0) {
            return getDescriptionStringMinuteSecond(r, minute, second);
        }
        if (hour == 1) {
            return getDescriptionStringOneHourMinuteSecond(r, minute, second);
        }
        return getDescriptionStringHoursMinuteSecond(r, hour, minute, second);
    }

    private static boolean isConnectBluetoothKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
        return imm != null && imm.semIsAccessoryKeyboard();
    }

    public static boolean isShowIme(Context context) {
        if (isConnectBluetoothKeyboard(context) && Secure.getInt(context.getContentResolver(), "show_ime_with_hard_keyboard", 1) != 1) {
            return false;
        }
        return true;
    }
}
