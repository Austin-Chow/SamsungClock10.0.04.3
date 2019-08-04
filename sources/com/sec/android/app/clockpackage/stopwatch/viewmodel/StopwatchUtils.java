package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.stopwatch.C0706R;

public class StopwatchUtils {
    public static String getDescriptionString(Context context, int hour, int minute, int second, int millisecond) {
        String secondNum = second + "." + ClockUtils.toTwoDigitString(millisecond);
        Resources r = context.getResources();
        switch (hour) {
            case 0:
                if (minute == 0) {
                    return secondNum + r.getString(C0706R.string.timer_sec);
                }
                if (minute == 1) {
                    return minute + r.getString(C0706R.string.timer_minute) + secondNum + r.getString(C0706R.string.timer_sec);
                }
                return minute + r.getString(C0706R.string.timer_min) + secondNum + r.getString(C0706R.string.timer_sec);
            case 1:
                if (minute == 0) {
                    return hour + r.getString(C0706R.string.timer_hour) + secondNum + r.getString(C0706R.string.timer_sec);
                }
                if (minute == 1) {
                    return hour + r.getString(C0706R.string.timer_hour) + minute + r.getString(C0706R.string.timer_minute) + secondNum + r.getString(C0706R.string.timer_sec);
                }
                return hour + r.getString(C0706R.string.timer_hour) + minute + r.getString(C0706R.string.timer_min) + secondNum + r.getString(C0706R.string.timer_sec);
            default:
                if (minute == 0) {
                    return hour + r.getString(C0706R.string.timer_hr) + secondNum + r.getString(C0706R.string.timer_sec);
                }
                if (minute == 1) {
                    return hour + r.getString(C0706R.string.timer_hr) + minute + r.getString(C0706R.string.timer_minute) + secondNum + r.getString(C0706R.string.timer_sec);
                }
                return hour + r.getString(C0706R.string.timer_hr) + minute + r.getString(C0706R.string.timer_min) + secondNum + r.getString(C0706R.string.timer_sec);
        }
    }

    public static boolean checkForLandscape(Context context) {
        return (context.getResources().getConfiguration().orientation != 2 || StateUtils.isInMultiwindow() || StateUtils.isScreenDp(context.getApplicationContext(), 512)) ? false : true;
    }
}
