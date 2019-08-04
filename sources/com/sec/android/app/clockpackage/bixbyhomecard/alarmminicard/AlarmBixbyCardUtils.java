package com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard;

import android.content.Context;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.RubinStateContract;
import com.sec.android.app.clockpackage.common.util.SleepPatternData.Record;
import com.sec.android.app.clockpackage.common.util.SleepPatternDataBroker;
import java.util.Calendar;

public class AlarmBixbyCardUtils {
    public static int mBedTimeEndHour = 6;
    public static long mBedTimeEndMillis = 0;
    public static int mBedTimeStartHour = 22;
    public static long mBedTimeStartMillis = 0;
    private static Context mContext;
    public static int mStartYourDayEndHour = 10;
    public static long mStartYourDayEndMillis = 0;
    public static int mStartYourDayStartHour = 6;
    public static long mStartYourDayStartMillis = 0;

    public static void setContext(Context context) {
        mContext = context;
        int hour = Calendar.getInstance().get(11);
    }

    public static void setStartYourDayHour() {
        if (!(RubinStateContract.isRubinActivated(mContext) && setStartYourDay())) {
            setStartYourDayWithoutRubin();
        }
        Log.secD("AlarmBixbyCardUtils", "mStartYourDayStartMillis= " + mStartYourDayStartMillis + " mStartYourDayEndMillis= " + mStartYourDayEndMillis + " mStartYourDayStartHour= " + mStartYourDayStartHour + " mStartYourDayEndHour= " + mStartYourDayEndHour);
    }

    private static boolean setStartYourDay() {
        long currTime = System.currentTimeMillis();
        long todayWakeupTime = getWakeupTime(0);
        if (!isValid(todayWakeupTime) || currTime > todayWakeupTime) {
            long tomorrowWakeupTime = getWakeupTime(1);
            if (!isValid(tomorrowWakeupTime) || currTime >= tomorrowWakeupTime - 3600000) {
                Log.secD("AlarmBixbyCardUtils", "start your Day is false");
                return false;
            }
            mStartYourDayStartMillis = tomorrowWakeupTime - 3600000;
            mStartYourDayEndMillis = tomorrowWakeupTime + 7200000;
            mStartYourDayStartHour = getHourFromMillis(mStartYourDayStartMillis);
            mStartYourDayEndHour = getHourFromMillis(mStartYourDayEndMillis);
            Log.secD("AlarmBixbyCardUtils", "Start your day: current Time in between tomorrow wake up period");
            return true;
        }
        mStartYourDayStartMillis = todayWakeupTime - 3600000;
        mStartYourDayEndMillis = todayWakeupTime + 7200000;
        mStartYourDayStartHour = getHourFromMillis(mStartYourDayStartMillis);
        mStartYourDayEndHour = getHourFromMillis(mStartYourDayEndMillis);
        Log.secD("AlarmBixbyCardUtils", "Start your day(TODAY): current time is before end of start your day of Today");
        return true;
    }

    public static void setStartYourDayWithoutRubin() {
        mStartYourDayStartHour = 6;
        mStartYourDayEndHour = 10;
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, mStartYourDayStartHour);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Log.secD("AlarmBixbyCardUtils", "now : " + now.getTimeInMillis() + " calendar :" + calendar.getTimeInMillis());
        if (calendar.before(now)) {
            calendar.add(6, 1);
        }
        mStartYourDayStartMillis = calendar.getTimeInMillis();
        calendar.set(11, mStartYourDayEndHour);
        mStartYourDayEndMillis = calendar.getTimeInMillis();
    }

    private static long getWakeupTime(int day) {
        long midNightMilliSec;
        Calendar calendar = Calendar.getInstance();
        if (day == 1) {
            calendar.add(5, 1);
            midNightMilliSec = getDayMidNightMillis(1);
        } else {
            calendar.add(5, 0);
            midNightMilliSec = getDayMidNightMillis(0);
        }
        SleepPatternDataBroker.setContext(mContext);
        Record todaySleepData = SleepPatternDataBroker.getBestSleepPatternByDayOfWeek(SleepPatternDataBroker.getDayOfWeek(calendar.get(7)));
        if (todaySleepData == null || !todaySleepData.isConfident) {
            return 0;
        }
        return todaySleepData.wakeupTime + midNightMilliSec;
    }

    private static int getHourFromMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(11);
    }

    private static long getDayMidNightMillis(int day) {
        Calendar midnight = Calendar.getInstance();
        if (day == 1) {
            midnight.add(5, 1);
        }
        midnight.set(11, 0);
        midnight.set(12, 0);
        midnight.set(13, 0);
        midnight.set(14, 0);
        return midnight.getTimeInMillis();
    }

    private static boolean isValid(long value) {
        if (value > 0) {
            return true;
        }
        return false;
    }
}
