package com.sec.android.app.clockpackage.alarm.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.format.Time;
import com.samsung.android.calendar.secfeature.SECCalendarFeatures;
import com.samsung.android.calendar.secfeature.holidays.checker.JapanHolidayChecker;
import com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarConverter;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Calendar;
import java.util.TimeZone;

public class HolidayUtil {
    public static final String[] CHINA_HOLIDAY_PROJECTION = new String[]{"_id", "title", "startDay", "endDay"};
    public static final Uri CHINA_HOLIDAY_URI = Uri.parse("content://com.samsung.android.chinaholiday/holidays_period_by_title");
    private static final String[] CHINA_HOLIDAY_WITH_WORKING_DAY_PROJECTION = new String[]{"_id", "title", "begin", "startDay"};
    private static final Uri CHINA_HOLIDAY_WITH_WORKING_DAY_URI = Uri.parse("content://com.samsung.android.chinaholiday/holidays_with_workingday");

    public static long getNextAlertTimeForHolidayOrWorkingDayAlarm(Context context, long alarmAlertTime, int repeatType, boolean bSubstituteHoliday) {
        if (Feature.isSupportAlarmOptionMenuForWorkingDay()) {
            return getNextAlertTimeForWorkingDayAlarm(context, alarmAlertTime, repeatType);
        }
        return getNextAlertTimeForNationHoliday(alarmAlertTime, repeatType, bSubstituteHoliday);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long getNextAlertTimeForWorkingDayAlarm(android.content.Context r15, long r16, int r18) {
        /*
        r9 = getCalendar();
        r0 = r16;
        r9.setTimeInMillis(r0);
        r2 = "HolidayUtil";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "1 getNextAlertTimeForWorkingDayAlarm = ";
        r3 = r3.append(r4);
        r4 = r9.getTimeInMillis();
        r3 = r3.append(r4);
        r4 = 32;
        r3 = r3.append(r4);
        r4 = r9.getTime();
        r4 = r4.toString();
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r2, r3);
        r2 = CHINA_HOLIDAY_URI;
        r8 = r2.buildUpon();
        r2 = r15.getContentResolver();
        r3 = r8.build();
        r4 = CHINA_HOLIDAY_PROJECTION;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r11 = r2.query(r3, r4, r5, r6, r7);
        if (r11 == 0) goto L_0x0091;
    L_0x0050:
        r10 = r11.getCount();
        r2 = "HolidayUtil";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "count = ";
        r3 = r3.append(r4);
        r3 = r3.append(r10);
        r3 = r3.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r2, r3);
        if (r10 <= 0) goto L_0x008e;
    L_0x006e:
        r11.moveToFirst();
    L_0x0071:
        r2 = r9.getTimeInMillis();
        r13 = getFestivalEffectDay(r15, r2);
        r2 = 7;
        r12 = r9.get(r2);
        r2 = 1;
        if (r2 >= r12) goto L_0x00b6;
    L_0x0081:
        r2 = 7;
        if (r12 >= r2) goto L_0x00b6;
    L_0x0084:
        r2 = -1;
        if (r13 != r2) goto L_0x00c1;
    L_0x0087:
        r2 = "HolidayUtil";
        r3 = "festival == Alarm.TYPE_INVALID) {";
        com.sec.android.app.clockpackage.common.util.Log.secD(r2, r3);
    L_0x008e:
        r11.close();
    L_0x0091:
        r2 = "HolidayUtil";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "3 getNextAlertTimeForWorkingDayAlarm = ";
        r3 = r3.append(r4);
        r4 = r9.getTime();
        r4 = r4.toString();
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r2, r3);
        r2 = r9.getTimeInMillis();
        return r2;
    L_0x00b6:
        r2 = 2;
        if (r13 != r2) goto L_0x00c1;
    L_0x00b9:
        r2 = "HolidayUtil";
        r3 = "if (festival == TYPE_WORKING_DAY) {";
        com.sec.android.app.clockpackage.common.util.Log.secD(r2, r3);
        goto L_0x008e;
    L_0x00c1:
        r2 = 6;
        r0 = r18;
        r3 = com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil.getNextAlertDayOffset(r9, r0);
        r9.add(r2, r3);
        r2 = "HolidayUtil";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "2 getNextAlertTimeForWorkingDayAlarm = ";
        r3 = r3.append(r4);
        r4 = r9.getTime();
        r4 = r4.toString();
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r2, r3);
        goto L_0x0071;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.model.HolidayUtil.getNextAlertTimeForWorkingDayAlarm(android.content.Context, long, int):long");
    }

    private static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    private static long getNextAlertTimeForNationHoliday(long alarmAlertTime, int repeatType, boolean bSubstituteHoliday) {
        Calendar cal = getCalendar();
        cal.setTimeInMillis(alarmAlertTime);
        Log.secD("HolidayUtil", "1 getNextAlertTimeForNationHoliday = " + cal.getTime().toString() + ' ' + cal.getTimeInMillis());
        while (isHolidayInCalendar(cal.getTimeInMillis(), bSubstituteHoliday)) {
            cal.add(6, AlarmItemUtil.getNextAlertDayOffset(cal, repeatType));
            Log.secD("HolidayUtil", "2 getNextAlertTimeForNationHoliday = " + cal.getTime().toString());
        }
        Log.secD("HolidayUtil", "3 getNextAlertTimeForNationHoliday = " + cal.getTime().toString());
        return cal.getTimeInMillis();
    }

    public static int getFestivalEffectDay(Context context, long alertTime) {
        int festival = -1;
        Cursor cursor = context.getContentResolver().query(CHINA_HOLIDAY_WITH_WORKING_DAY_URI.buildUpon().build(), CHINA_HOLIDAY_WITH_WORKING_DAY_PROJECTION, null, null, null);
        if (cursor != null) {
            int count = cursor.getCount();
            Log.secD("HolidayUtil", "count = " + count);
            if (count > 0) {
                Calendar cal = getCalendar();
                cal.setTimeInMillis(alertTime);
                Log.secD("HolidayUtil", "alertTime = " + cal.getTime().toString());
                int julianDay = Time.getJulianDay(cal.getTimeInMillis(), (long) (TimeZone.getDefault().getRawOffset() / 1000));
                Log.secD("HolidayUtil", "julianDay = " + julianDay);
                cursor.moveToFirst();
                do {
                    String title = cursor.getString(1);
                    int startDay = cursor.getInt(3);
                    if (startDay == julianDay) {
                        Log.secD("HolidayUtil", "title = " + title);
                        Log.secD("HolidayUtil", "startDay = " + startDay);
                        festival = "WorkingDay".equals(title) ? 2 : 1;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.secD("HolidayUtil", "isFestivalEffectDay festival = " + festival);
        return festival;
    }

    public static boolean isHolidayInCalendar(long alertTime, boolean bSubstituteHoliday) {
        boolean bHoliday;
        if (Feature.isSupportSubstituteHolidayMenu()) {
            bHoliday = isHolidayInCalendarForKorea(alertTime, bSubstituteHoliday);
        } else {
            bHoliday = isHolidayInCalendarForJapan(alertTime);
        }
        Log.secD("HolidayUtil", "isHolidayInCalendar bHoliday = " + bHoliday);
        return bHoliday;
    }

    public static boolean isHolidayInCalendarForKorea(long alertTime, boolean bSubstituteHoliday) {
        boolean bHolidayInCalendarForKorea = false;
        SolarLunarConverter solarLunarConverter = SECCalendarFeatures.getInstance().getSolarLunarConverter();
        if (solarLunarConverter != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alertTime);
            Time tempTime = new Time();
            tempTime.set(cal.get(5), cal.get(2), cal.get(1));
            if (bSubstituteHoliday) {
                if (solarLunarConverter.isHoliday(tempTime) || solarLunarConverter.isSubstHoliday(tempTime)) {
                    bHolidayInCalendarForKorea = true;
                    Log.secD("HolidayUtil", "isHolidayInCalendarForKorea bHolidayInCalendarForKorea true");
                }
            } else if (solarLunarConverter.isHoliday(tempTime)) {
                bHolidayInCalendarForKorea = true;
            }
        } else {
            Log.m42e("HolidayUtil", "isHolidayInCalendarForKorea solarLunarConverter is null");
        }
        Log.secD("HolidayUtil", "isHolidayInCalendarForKorea bHolidayInCalendarForKorea = " + bHolidayInCalendarForKorea);
        return bHolidayInCalendarForKorea;
    }

    public static boolean isHolidayInCalendarForJapan(long alertTime) {
        boolean bHoliday = false;
        JapanHolidayChecker mJpnHolidayChecker = JapanHolidayChecker.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alertTime);
        Log.secD("HolidayUtil", cal.getTime().toString());
        Time tempTime = new Time();
        tempTime.set(cal.get(5), cal.get(2), cal.get(1));
        if (mJpnHolidayChecker.isHoliday(tempTime)) {
            bHoliday = true;
        } else {
            tempTime.weekDay = cal.get(7) - 1;
            Log.secD("HolidayUtil", "isHolidayInCalendarForJapan tempTime.weekDay = " + tempTime.weekDay);
            if (mJpnHolidayChecker.isSubstHoliday(tempTime)) {
                bHoliday = true;
            }
        }
        Log.secD("HolidayUtil", "isHolidayInCalendarForJapan bHoliday = " + bHoliday);
        return bHoliday;
    }

    public static boolean isHolidayInCalendarForChina(Context context, long alarmAlertTime) {
        boolean isChinaHoliday = false;
        Builder builder = CHINA_HOLIDAY_URI.buildUpon();
        Cursor chainaHolidayCursor = null;
        if (context != null) {
            chainaHolidayCursor = context.getContentResolver().query(builder.build(), CHINA_HOLIDAY_PROJECTION, null, null, null);
        }
        if (chainaHolidayCursor != null) {
            if (chainaHolidayCursor.moveToFirst()) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(alarmAlertTime);
                int festival = getFestivalEffectDay(context, alarmAlertTime);
                int dayOfWeek = cal.get(7);
                if (1 >= dayOfWeek || dayOfWeek >= 7) {
                    if (festival != 2) {
                        isChinaHoliday = true;
                    }
                } else if (festival == 1) {
                    isChinaHoliday = true;
                }
            }
            chainaHolidayCursor.close();
        }
        return isChinaHoliday;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getHolidayName(android.content.Context r25, long r26) {
        /*
        r20 = 0;
        r3 = 2;
        r4 = new java.lang.String[r3];
        r3 = 0;
        r5 = "_id";
        r4[r3] = r5;
        r3 = 1;
        r5 = "name";
        r4[r3] = r5;
        r3 = 2;
        r12 = new java.lang.String[r3];
        r3 = 0;
        r5 = "title";
        r12[r3] = r5;
        r3 = 1;
        r5 = "begin";
        r12[r3] = r5;
        r2 = r25.getContentResolver();
        r23 = "account_name='local.samsungholiday' AND name = 'legalHoliday'";
        r3 = android.provider.CalendarContract.Calendars.CONTENT_URI;	 Catch:{ Exception -> 0x0050 }
        r5 = "account_name='local.samsungholiday' AND name = 'legalHoliday'";
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r14 = r2.query(r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0050 }
        r24 = 0;
        if (r14 == 0) goto L_0x0037;
    L_0x0031:
        r3 = r14.getCount();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        if (r3 != 0) goto L_0x008c;
    L_0x0037:
        r3 = "HolidayUtil";
        r5 = "There's no holiday calendar";
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r5);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = 0;
        if (r14 == 0) goto L_0x0046;
    L_0x0041:
        if (r24 == 0) goto L_0x0077;
    L_0x0043:
        r14.close();	 Catch:{ Throwable -> 0x0049 }
    L_0x0046:
        r21 = r20;
    L_0x0048:
        return r3;
    L_0x0049:
        r5 = move-exception;
        r0 = r24;
        r0.addSuppressed(r5);	 Catch:{ Exception -> 0x0050 }
        goto L_0x0046;
    L_0x0050:
        r18 = move-exception;
        r3 = "HolidayUtil";
        r5 = "getHolidayName Exception2";
        com.sec.android.app.clockpackage.common.util.Log.secE(r3, r5);
    L_0x0058:
        r3 = "HolidayUtil";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "getHolidayName holidayName : ";
        r5 = r5.append(r7);
        r0 = r20;
        r5 = r5.append(r0);
        r5 = r5.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r5);
        r21 = r20;
        r3 = r20;
        goto L_0x0048;
    L_0x0077:
        r14.close();	 Catch:{ Exception -> 0x0050 }
        goto L_0x0046;
    L_0x007b:
        r19.moveToNext();	 Catch:{ Throwable -> 0x0159, all -> 0x018d }
        r3 = 0;
        r0 = r19;
        r20 = r0.getString(r3);	 Catch:{ Throwable -> 0x0159, all -> 0x018d }
        if (r19 == 0) goto L_0x008c;
    L_0x0087:
        if (r7 == 0) goto L_0x0154;
    L_0x0089:
        r19.close();	 Catch:{ Throwable -> 0x014e, Exception -> 0x0124, all -> 0x013e }
    L_0x008c:
        r3 = r14.moveToNext();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        if (r3 == 0) goto L_0x016e;
    L_0x0092:
        r3 = 0;
        r16 = r14.getLong(r3);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = java.util.TimeZone.getDefault();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = r3.getRawOffset();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = r3 / 1000;
        r10 = (long) r3;	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r0 = r26;
        r22 = android.text.format.Time.getJulianDay(r0, r10);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = "HolidayUtil";
        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r5.<init>();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r7 = "getHolidayName julianDay = ";
        r5 = r5.append(r7);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r0 = r22;
        r5 = r5.append(r0);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r5 = r5.toString();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r5);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3.<init>();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r5 = "calendar_id=";
        r3 = r3.append(r5);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r0 = r16;
        r3 = r3.append(r0);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r8 = r3.toString();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r3 = android.provider.CalendarContract.Instances.CONTENT_BY_DAY_URI;	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r13 = r3.buildUpon();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r0 = r22;
        r10 = (long) r0;	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        android.content.ContentUris.appendId(r13, r10);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r0 = r22;
        r10 = (long) r0;	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        android.content.ContentUris.appendId(r13, r10);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r6 = r13.build();	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r5 = r2;
        r7 = r12;
        r19 = r5.query(r6, r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x0124 }
        r7 = 0;
        if (r19 == 0) goto L_0x00ff;
    L_0x00f9:
        r3 = r19.getCount();	 Catch:{ Throwable -> 0x0159, all -> 0x018d }
        if (r3 != 0) goto L_0x007b;
    L_0x00ff:
        r3 = "HolidayUtil";
        r5 = "There's no holiday events in this date range";
        com.sec.android.app.clockpackage.common.util.Log.secD(r3, r5);	 Catch:{ Throwable -> 0x0159, all -> 0x018d }
        if (r19 == 0) goto L_0x010b;
    L_0x0108:
        r19.close();	 Catch:{ Throwable -> 0x0159, all -> 0x018d }
    L_0x010b:
        if (r19 == 0) goto L_0x0112;
    L_0x010d:
        if (r7 == 0) goto L_0x013a;
    L_0x010f:
        r19.close();	 Catch:{ Throwable -> 0x011f, Exception -> 0x0124, all -> 0x013e }
    L_0x0112:
        if (r14 == 0) goto L_0x0119;
    L_0x0114:
        if (r24 == 0) goto L_0x014a;
    L_0x0116:
        r14.close();	 Catch:{ Throwable -> 0x0143 }
    L_0x0119:
        r21 = r20;
        r3 = r20;
        goto L_0x0048;
    L_0x011f:
        r3 = move-exception;
        r7.addSuppressed(r3);	 Catch:{ Exception -> 0x0124 }
        goto L_0x0112;
    L_0x0124:
        r15 = move-exception;
        r3 = "HolidayUtil";
        r5 = "getHolidayName Exception1";
        com.sec.android.app.clockpackage.common.util.Log.secE(r3, r5);	 Catch:{ Throwable -> 0x012e, all -> 0x013e }
        goto L_0x008c;
    L_0x012e:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0130 }
    L_0x0130:
        r5 = move-exception;
        r7 = r3;
    L_0x0132:
        if (r14 == 0) goto L_0x0139;
    L_0x0134:
        if (r7 == 0) goto L_0x0189;
    L_0x0136:
        r14.close();	 Catch:{ Throwable -> 0x0184 }
    L_0x0139:
        throw r5;	 Catch:{ Exception -> 0x0050 }
    L_0x013a:
        r19.close();	 Catch:{ Exception -> 0x0124 }
        goto L_0x0112;
    L_0x013e:
        r3 = move-exception;
        r5 = r3;
        r7 = r24;
        goto L_0x0132;
    L_0x0143:
        r3 = move-exception;
        r0 = r24;
        r0.addSuppressed(r3);	 Catch:{ Exception -> 0x0050 }
        goto L_0x0119;
    L_0x014a:
        r14.close();	 Catch:{ Exception -> 0x0050 }
        goto L_0x0119;
    L_0x014e:
        r3 = move-exception;
        r7.addSuppressed(r3);	 Catch:{ Exception -> 0x0124 }
        goto L_0x008c;
    L_0x0154:
        r19.close();	 Catch:{ Exception -> 0x0124 }
        goto L_0x008c;
    L_0x0159:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x015b }
    L_0x015b:
        r5 = move-exception;
        r7 = r3;
    L_0x015d:
        if (r19 == 0) goto L_0x0164;
    L_0x015f:
        if (r7 == 0) goto L_0x016a;
    L_0x0161:
        r19.close();	 Catch:{ Throwable -> 0x0165, Exception -> 0x0124, all -> 0x013e }
    L_0x0164:
        throw r5;	 Catch:{ Exception -> 0x0124 }
    L_0x0165:
        r3 = move-exception;
        r7.addSuppressed(r3);	 Catch:{ Exception -> 0x0124 }
        goto L_0x0164;
    L_0x016a:
        r19.close();	 Catch:{ Exception -> 0x0124 }
        goto L_0x0164;
    L_0x016e:
        if (r14 == 0) goto L_0x0058;
    L_0x0170:
        if (r24 == 0) goto L_0x017f;
    L_0x0172:
        r14.close();	 Catch:{ Throwable -> 0x0177 }
        goto L_0x0058;
    L_0x0177:
        r3 = move-exception;
        r0 = r24;
        r0.addSuppressed(r3);	 Catch:{ Exception -> 0x0050 }
        goto L_0x0058;
    L_0x017f:
        r14.close();	 Catch:{ Exception -> 0x0050 }
        goto L_0x0058;
    L_0x0184:
        r3 = move-exception;
        r7.addSuppressed(r3);	 Catch:{ Exception -> 0x0050 }
        goto L_0x0139;
    L_0x0189:
        r14.close();	 Catch:{ Exception -> 0x0050 }
        goto L_0x0139;
    L_0x018d:
        r3 = move-exception;
        r5 = r3;
        goto L_0x015d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.model.HolidayUtil.getHolidayName(android.content.Context, long):java.lang.String");
    }
}
