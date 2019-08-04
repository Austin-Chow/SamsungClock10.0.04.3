package com.sec.android.app.clockpackage.common.util;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.sec.android.app.clockpackage.common.util.SleepPatternContract.SleepPatternInfo;
import com.sec.android.app.clockpackage.common.util.SleepPatternData.Record;
import java.util.ArrayList;
import java.util.List;

public final class SleepPatternDataBroker {
    private static final Record INVALID_DATA = null;
    private static final List<String> LOOKUP = new ArrayList(10);
    private static Context mContext;
    private static SleepPatternData mSleepPatternData;

    static {
        LOOKUP.add("ALL");
        LOOKUP.add("WEEKDAY");
        LOOKUP.add("WEEKEND");
        LOOKUP.add("MONDAY");
        LOOKUP.add("TUESDAY");
        LOOKUP.add("WEDNESDAY");
        LOOKUP.add("THURSDAY");
        LOOKUP.add("FRIDAY");
        LOOKUP.add("SATURDAY");
        LOOKUP.add("SUNDAY");
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static int getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return 9;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            default:
                return 0;
        }
    }

    private static boolean isDayOfWeek(int weekType) {
        switch (weekType) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return true;
            default:
                return false;
        }
    }

    public static Record getBestSleepPatternByDayOfWeek(int dayOfWeek) {
        if (!isDayOfWeek(dayOfWeek)) {
            return null;
        }
        Record sleepPatternInfo = getSleepPatternData(dayOfWeek);
        if (sleepPatternInfo != null && sleepPatternInfo.isConfident) {
            return sleepPatternInfo;
        }
        boolean isWeekend = false;
        if (dayOfWeek == 8 || dayOfWeek == 9) {
            isWeekend = true;
        }
        sleepPatternInfo = isWeekend ? getSleepPatternData(2) : getSleepPatternData(1);
        if (sleepPatternInfo != null && sleepPatternInfo.isConfident) {
            return sleepPatternInfo;
        }
        sleepPatternInfo = getSleepPatternData(0);
        return (sleepPatternInfo == null || !sleepPatternInfo.isConfident) ? INVALID_DATA : sleepPatternInfo;
    }

    public static Record getSleepPatternData(int type) {
        return buildSleepPatternData().getData(type);
    }

    private static SleepPatternData buildSleepPatternData() {
        buildData();
        return mSleepPatternData;
    }

    private static void buildData() {
        if (mSleepPatternData == null) {
            mSleepPatternData = new SleepPatternData();
        } else {
            mSleepPatternData.clearData();
        }
        querySleepPatternData(mSleepPatternData);
    }

    private static void querySleepPatternData(SleepPatternData outData) {
        try {
            Throwable th;
            Cursor c = mContext.getContentResolver().query(SleepPatternInfo.CONTENT_URI, new String[]{"week_type", "wakeup_time", "bedtime", "is_confident", "confidence", "analyzed_time"}, null, null, null);
            Throwable th2 = null;
            if (c != null) {
                while (c.moveToNext()) {
                    try {
                        String weekTypeStr = c.getString(0);
                        int type = LOOKUP.indexOf(weekTypeStr);
                        boolean isConfident = !"0".equals(c.getString(3));
                        Record data = new Record(type, isConfident ? c.getLong(1) : 21600000, isConfident ? c.getLong(2) : 82800000, isConfident, isConfident ? c.getFloat(4) : 0.5f, c.getLong(5));
                        outData.putData(type, data);
                        Log.secD("SleepPatternDataBroker", "SleepPattern Record put=" + weekTypeStr + ": " + data.type + "/" + data.wakeupTime + "/" + data.bedTime + "/" + data.isConfident + "/" + data.confidence + "/" + data.analyzedAt);
                    } catch (Throwable th3) {
                        th = th3;
                        th2 = th;
                    }
                }
            }
            c.close();
            if (c == null) {
                return;
            }
            if (th2 != null) {
                try {
                    c.close();
                    return;
                } catch (Throwable th4) {
                    th2.addSuppressed(th4);
                    return;
                }
            }
            c.close();
            return;
            if (c != null) {
                if (th2 != null) {
                    try {
                        c.close();
                    } catch (Throwable th42) {
                        th2.addSuppressed(th42);
                    }
                } else {
                    c.close();
                }
            }
            throw th;
            throw th;
            Log.secD("SleepPatternDataBroker", "query error");
        } catch (NullPointerException e) {
        } catch (SQLException e2) {
        }
    }
}
