package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.ContextThemeWrapper;
import android.widget.Toast;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.AlarmCvConstants;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.model.CelebVoiceProvider;
import com.sec.android.app.clockpackage.alarm.model.HolidayUtil;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmUtil {
    public static int checkSameAlarm(Context context, AlarmItem item) {
        int i;
        Cursor cursor;
        int sameAlarmItemId = -1;
        String tempAlarmName = item.mAlarmName.replace("'", "''");
        StringBuilder append = new StringBuilder().append("active = 1 AND alarmtime = ").append(item.mAlarmTime).append(" AND ").append("alerttime").append(" = ").append(item.mAlarmAlertTime).append(" AND ").append("repeattype").append(" = ").append(item.mRepeatType).append(" AND ").append("snzactive").append(" = ");
        if (item.mSnoozeActivate) {
            i = 1;
        } else {
            i = 0;
        }
        String selectionClause = append.append(i).append(" AND ").append("snzduration").append(" = ").append(item.mSnoozeDuration).append(" AND ").append("snzrepeat").append(" = ").append(item.mSnoozeRepeat).append(" AND ").append("dailybrief").append(" = ").append(item.mDailyBriefing).append(" AND ").append("alarmsound").append(" = ").append(item.mAlarmSoundType).append(" AND ").append("alarmtone").append(" = ").append(item.mAlarmSoundTone).append(" AND ").append("volume").append(" = ").append(item.mAlarmVolume).append(" AND ").append("vibrationpattern").append(" = ").append(item.mVibrationPattern).toString();
        try {
            cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, selectionClause + " AND " + "name" + "= '" + tempAlarmName + '\'', null, "createtime DESC");
        } catch (SQLiteException e) {
            Log.secE("AlarmUtil", "checkSameAlarm () / SQLiteException : " + e.toString());
            item.mAlarmName = item.mAlarmName.substring(0, 19);
            cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, selectionClause + " AND " + "name" + "= '" + item.mAlarmName.replace("'", "''") + '\'', null, "createtime DESC");
        }
        if (cursor != null) {
            int sameAlarmItemCnt = cursor.getCount();
            if (sameAlarmItemCnt > 0) {
                cursor.moveToFirst();
                sameAlarmItemId = cursor.getInt(0);
                String strAlarmTime = getAlarmTimeString(context, item.mAlarmTime);
                String strSameAlarm = null;
                try {
                    strSameAlarm = context.getResources().getString(C0490R.string.alarm_exist_same);
                } catch (NotFoundException e2) {
                    Log.secE("AlarmUtil", "Exception : " + e2.toString());
                }
                if (strSameAlarm != null) {
                    Context context2 = context;
                    AlarmItem alarmItem = item;
                    saveMsg(context2, alarmItem, String.format(strSameAlarm, new Object[]{strAlarmTime}), 600);
                }
            }
            Log.secD("AlarmUtil", "checkSameAlarm() / sameAlarmItemCnt = " + sameAlarmItemCnt + "/ sameAlarmItemID = " + sameAlarmItemId);
            cursor.close();
        }
        return sameAlarmItemId;
    }

    private static String getOldCelebPreviewUri(Context context, String path) {
        if (path == null) {
            return null;
        }
        String previewUri = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), null, "PKG_NAME='" + path + '\'', null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String type = cursor.getString(cursor.getColumnIndex("TYPE"));
                previewUri = AlarmCvConstants.getUIDRootPath() + type + "/" + path + "/" + "assets/" + cursor.getString(cursor.getColumnIndex("PREVIEW_FILE"));
                Log.secD("AlarmUtil", "getOldCelebPreviewUri " + previewUri);
            }
            cursor.close();
        }
        return previewUri;
    }

    public static String getCelebVoiceSubTextValue(Context context, String path, boolean showExpiredInfo, boolean isExpired) {
        if (path == null) {
            return null;
        }
        if (isNewCelebDefault(path)) {
            return context.getResources().getString(C0490R.string.default_celeb_title);
        }
        Log.secD("AlarmUtil", "getCelebVoiceSubTextValue()  / showExpiredInfo = " + showExpiredInfo + "/ isExpired = " + isExpired + "/ path =" + path);
        String retStr = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), null, "PKG_NAME='" + path + '\'', null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String contentName = cursor.getString(cursor.getColumnIndex("CONTENT_NAME"));
                Long expiredDate = Long.valueOf(cursor.getLong(cursor.getColumnIndex("VALID_DATE_LONG")));
                if (!showExpiredInfo) {
                    retStr = contentName;
                } else if (isExpired) {
                    retStr = context.getString(C0490R.string.alarm_option_expired, new Object[]{contentName});
                } else {
                    retStr = context.getString(C0490R.string.alarm_option_expires, new Object[]{contentName, new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(context) ? "MMM d, yyyy HH:mm" : "MMM d, yyyy hh:mm aaa"), Locale.getDefault()).format(expiredDate)});
                    Log.secD("AlarmUtil", "date : " + displayDate);
                }
            }
            cursor.close();
        }
        Log.secD("AlarmUtil", "getCelebVoiceSubTextValue() /  expiredString =" + retStr);
        return retStr;
    }

    public static boolean isExpiredCelebVoice(Context context, String path) {
        boolean isExpired = true;
        if (isNewCelebDefault(path)) {
            int isExpired2 = 1;
            return false;
        }
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), null, "PKG_NAME='" + path + '\'', null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                if (Long.valueOf(cursor.getLong(cursor.getColumnIndex("VALID_DATE_LONG"))).longValue() < System.currentTimeMillis()) {
                    isExpired = true;
                } else {
                    isExpired = false;
                }
            }
            cursor.close();
        }
        Log.secD("AlarmUtil", "isExpiredCelebVoice : " + isExpired + ", " + path);
        boolean z = isExpired;
        return isExpired;
    }

    public static String getAlarmTimeString(Context context, int alarmTime) {
        Calendar c = Calendar.getInstance();
        int min = alarmTime % 100;
        c.set(11, alarmTime / 100);
        c.set(12, min);
        return formatTime(context, c);
    }

    public static String formatTime(Context context, Calendar c) {
        if (c == null) {
            return "";
        }
        String format = DateFormat.is24HourFormat(context) ? "kk:mm" : "h:mm aa";
        if (!"h:mm aa".equals(format)) {
            return (String) DateFormat.format(format, c);
        }
        String AmPm;
        int currentHour = c.get(11);
        int currentMinute = c.get(12);
        String[] AmPmText = new DateFormatSymbols().getAmPmStrings();
        if (currentHour < 12 || currentHour == 24) {
            AmPm = AmPmText[0];
        } else {
            AmPm = AmPmText[1];
        }
        if (Locale.getDefault().getLanguage().equals(Locale.KOREA.getLanguage()) || Locale.getDefault().getLanguage().equals(Locale.CHINA.getLanguage())) {
            if (currentHour % 12 == 0) {
                currentHour = 12;
            } else {
                currentHour %= 12;
            }
            return String.format("%s %01d:%02d", new Object[]{AmPm, Integer.valueOf(currentHour), Integer.valueOf(currentMinute)});
        } else if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
            if (currentHour % 12 == 0) {
                currentHour = 0;
            } else {
                currentHour %= 12;
            }
            return String.format("%s%01d:%02d", new Object[]{AmPm, Integer.valueOf(currentHour), Integer.valueOf(currentMinute)});
        } else if (!Locale.getDefault().getLanguage().equals("ur")) {
            return DateFormat.format("h:mm ", c) + AmPm;
        } else {
            if (currentHour % 12 == 0) {
                currentHour = 0;
            } else {
                currentHour %= 12;
            }
            return String.format("%01d:%02d%s", new Object[]{Integer.valueOf(currentHour), Integer.valueOf(currentMinute), "‎ " + AmPm});
        }
    }

    public static long[] getAlertDiffFromCurrent(long alertMillis) {
        long[] dateData = new long[4];
        Calendar c = Calendar.getInstance();
        long delta = alertMillis - ((c.getTimeInMillis() / 60000) * 60000);
        long minutes = (delta / 60000) % 60;
        long hours = delta / 3600000;
        long days = hours / 24;
        long years = 0;
        Calendar nextCal = Calendar.getInstance();
        nextCal.set(1, c.get(1) + 2);
        nextCal.set(13, 0);
        long nextCalMillis = nextCal.getTimeInMillis() - 1000;
        long tempDay;
        if (nextCalMillis <= alertMillis) {
            tempDay = (nextCalMillis - ((c.getTimeInMillis() / 60000) * 60000)) / 86400000;
            years = 2;
            days = (days - tempDay) - 1;
            Log.secD("AlarmUtil", "getAlertDiffFromCurrent() 2 YEAR, tempDay = " + tempDay + " , days = " + days);
        } else {
            nextCal.set(1, c.get(1) + 1);
            nextCal.set(13, 0);
            nextCalMillis = nextCal.getTimeInMillis() - 1000;
            if (nextCalMillis <= alertMillis) {
                tempDay = (nextCalMillis - ((c.getTimeInMillis() / 60000) * 60000)) / 86400000;
                years = 1;
                Log.secD("AlarmUtil", "getAlertDiffFromCurrent() 1 YEAR tempDay = " + tempDay + " , days = " + days);
                days = (days - tempDay) - 1;
            } else {
                Log.secD("AlarmUtil", "getAlertDiffFromCurrent() less than 1YEAR");
            }
        }
        hours %= 24;
        dateData[0] = minutes;
        dateData[1] = hours;
        dateData[2] = days;
        dateData[3] = years;
        return dateData;
    }

    public static void saveMsg(Context context, AlarmItem item, String str, int delay) {
        if (item.mActivate != 0) {
            Log.secD("AlarmUtil", "saveMsg() / str = " + str);
            Context contextThemeWrapper = new ContextThemeWrapper(context, 16974123);
            Resources r = context.getResources();
            long[] dateData = getAlertDiffFromCurrent(item.mAlarmAlertTime);
            String alertTimeInfoStr = "";
            if (dateData[2] > 0 || dateData[3] > 0) {
                long alertTime = item.mAlarmAlertTime;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alertTime);
                String formattedTime = formatTime(context, calendar);
                String formattedDate = ClockUtils.getFormatDateTime(context, alertTime, true);
                if (str == null) {
                    alertTimeInfoStr = String.format(r.getString(C0490R.string.alarm_set_over_24h), new Object[]{formattedTime, formattedDate});
                } else {
                    alertTimeInfoStr = String.format(r.getString(C0490R.string.alarm_set_over_24h_exist), new Object[]{formattedTime, formattedDate});
                }
            } else {
                String str2;
                long minutes = dateData[0];
                long hours = dateData[1];
                String[] formats = r.getStringArray(C0490R.array.alarm_set);
                String minSeq = minutes == 0 ? "" : minutes == 1 ? r.getString(C0490R.string.minute) : r.getString(C0490R.string.minutes, new Object[]{ClockUtils.toDigitString((int) minutes)});
                String hourSeq = hours == 0 ? "" : hours == 1 ? r.getString(C0490R.string.hour) : r.getString(C0490R.string.hours, new Object[]{ClockUtils.toDigitString((int) hours)});
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    if (hours == 2) {
                        hourSeq = r.getString(C0490R.string.arabic_hours_2);
                    } else if (hours >= 11) {
                        hourSeq = r.getString(C0490R.string.hours, new Object[]{ClockUtils.toDigitString((int) hours)}).split(" ")[0] + ' ' + r.getString(C0490R.string.hour).split(" ")[0];
                    }
                    if (minutes == 2) {
                        minSeq = r.getString(C0490R.string.arabic_minutes_2);
                    } else if (minutes >= 11) {
                        minSeq = r.getString(C0490R.string.minutes, new Object[]{ClockUtils.toDigitString((int) minutes)}).split(" ")[0] + ' ' + r.getString(C0490R.string.minute).split(" ")[0];
                    }
                }
                int index = (hours > 0 ? 2 : 0) | (minutes > 0 ? 4 : 0);
                if (index == 4 && minutes == 1) {
                    index = 0;
                }
                StringBuilder append = new StringBuilder().append(String.format(formats[index], new Object[]{"", hourSeq, minSeq}));
                if (str == null) {
                    str2 = "";
                } else {
                    str2 = '\n' + str;
                }
                alertTimeInfoStr = append.append(str2).toString();
            }
            Log.secD("AlarmUtil", "saveMsg() / alertTimeInfoStr = " + alertTimeInfoStr);
            Toast toast = Toast.makeText(contextThemeWrapper, null, 1);
            toast.setText(alertTimeInfoStr);
            final Toast toast2 = toast;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    toast2.show();
                }
            }, (long) delay);
        }
    }

    public static void showMaxCountToast(Context context) {
        Toast toast = Toast.makeText(context, null, 1);
        toast.setText(context.getResources().getString(C0490R.string.alarm_max_item, new Object[]{Integer.valueOf(50)}));
        toast.show();
    }

    public static void sendAlarmDeleteModeUpdate(Context context) {
        Intent alarmDeleteMode = new Intent();
        alarmDeleteMode.setAction("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_DELETE_MODE_CHANGE");
        LocalBroadcastManager.getInstance(context).sendBroadcast(alarmDeleteMode);
    }

    public static void sendStopAlertByChangeIntent(Context context, int id) {
        Intent stopAlert = new Intent("com.samsung.sec.android.clockpacakge.alarm.ALARM_EDIT_MESSAGE");
        stopAlert.putExtra("AlarmID", id);
        context.sendBroadcast(stopAlert);
    }

    public static void sendSelctionToAlarmWidget(Context context, int widgetId, int alarmId, int duplicateId, int position) {
        Log.secD("AlarmUtil", "sendSelctionToAlarmWidget() / widgetID=" + widgetId + "/alarmId=" + alarmId + "/duplicateId=" + duplicateId + "/position=" + position);
        Intent intent = new Intent("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_SELECT");
        intent.setPackage("com.sec.android.app.clockpackage");
        intent.addFlags(402653184);
        intent.putExtra("widgetId", widgetId);
        intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", alarmId);
        if (duplicateId != -1) {
            intent.putExtra("duplicateId", duplicateId);
        }
        if (position != -1) {
            intent.putExtra("ListItemPosition", position);
        }
        context.sendBroadcast(intent);
    }

    public static void sendFinishLaunchActivityToAlarmWidget(Context context, int widgetId) {
        Intent intent = new Intent("com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_LAUNCH_ACTIVITY_FINISH");
        intent.putExtra("widgetId", widgetId);
        context.sendBroadcast(intent);
    }

    public static void sendShowWeatherIconIntent(Context context) {
        Intent weatherIconIntent = new Intent();
        weatherIconIntent.setAction("com.samsung.android.bixby.intent.action.REQUEST_SHOW_WEATHER_ICON");
        LocalBroadcastManager.getInstance(context).sendBroadcast(weatherIconIntent);
    }

    public static void sendAlarmAlertIntent(Context context, Intent intent) {
        Log.secV("AlarmUtil", "sendAlarmAlertIntent");
        Intent alarmAlertIntent = new Intent();
        alarmAlertIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT");
        alarmAlertIntent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA"));
        context.sendBroadcast(alarmAlertIntent);
    }

    public static void sendOpenCalendarSettingIntent(Context context) {
        if (Feature.DEBUG_ENG) {
            Log.secV("AlarmUtil", "sendOpenCalendarSettingIntent");
        }
        try {
            context.startActivity(new Intent("com.sec.android.intent.calendar.setting"));
        } catch (ActivityNotFoundException e) {
            Log.m42e("AlarmUtil", "calendar setting activity not Found");
        }
    }

    public static boolean isValidChinaDB(Context context) {
        Time time = new Time();
        time.setToNow();
        if (time.month != 11 || time.monthDay <= 23) {
            time.month = 0;
            time.monthDay = 1;
        } else {
            time.year++;
            time.month = 0;
            time.monthDay = 1;
        }
        int needToUpdateDb = Time.getJulianDay(time.normalize(true), time.gmtoff);
        Log.secD("AlarmUtil", "need to be update '" + time.year + ", " + time.month + ", " + time.monthDay + "' DB. / needToUpdateDB = " + needToUpdateDb);
        Cursor cursor = context.getContentResolver().query(HolidayUtil.CHINA_HOLIDAY_URI.buildUpon().build(), HolidayUtil.CHINA_HOLIDAY_PROJECTION, null, null, null);
        if (cursor != null) {
            int nCount = cursor.getCount();
            Log.secD("AlarmUtil", "nCount = " + nCount);
            if (nCount > 0) {
                cursor.moveToFirst();
                do {
                    int latestDb = cursor.getInt(2);
                    if (needToUpdateDb <= latestDb) {
                        Time latestDbTime = new Time();
                        latestDbTime.setJulianDay(latestDb);
                        Log.secD("AlarmUtil", "already Updated '" + latestDbTime.year + ", " + latestDbTime.month + ", " + latestDbTime.monthDay + "' DB.  / latestDb = " + latestDb);
                        Log.secD("AlarmUtil", "find next year china holiday");
                        cursor.close();
                        return true;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Log.secE("AlarmUtil", "can not get holiday data");
        }
        return false;
    }

    public static void setStopAlarmAlertValue(boolean bValue) {
        Alarm.isStopAlarmAlert = bValue;
    }

    public static boolean needToShowAlarmAlert(Context context, AlarmItem item) {
        if (item.isHoliday() && Feature.isSupportHolidayAlarm() && item.isFirstAlarm() && item.isWeeklyAlarm() && HolidayUtil.isHolidayInCalendar(item.mAlarmAlertTime, item.isSubstituteHoliday())) {
            Log.m41d("AlarmUtil", "needToShowAlarmAlert bNeedToShowAlarmAlert = false(Japan)");
            return false;
        } else if (!item.isWorkingDay() || !Feature.isSupportAlarmOptionMenuForWorkingDay() || !item.isWeeklyAlarm() || !item.isFirstAlarm()) {
            return true;
        } else {
            int festival = HolidayUtil.getFestivalEffectDay(context, item.mAlarmAlertTime);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(item.mAlarmAlertTime);
            int dayOfWeek = cal.get(7);
            if (1 >= dayOfWeek || dayOfWeek >= 7) {
                if (festival == 2) {
                    return true;
                }
                Log.m41d("AlarmUtil", "needToShowAlarmAlert false festival != Alarm.TYPE_WORKING_DAY");
                return false;
            } else if (festival != 1) {
                return true;
            } else {
                Log.m41d("AlarmUtil", "needToShowAlarmAlert false festival == Alarm.TYPE_HOLIDAY");
                return false;
            }
        }
    }

    public static void setPresetAlarm(Context context) {
        AlarmItem item = AlarmItem.createItem(context, 6, 0);
        item.mSoundUri = "alarm_preset_default_uri";
        item.setWeekDayAlarm();
        if (Feature.isSupportCelebrityAlarm()) {
            item.setSoundModeNewCeleb();
            item.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        } else if (Feature.isSupportBixbyBriefingMenu(context)) {
            item.setSoundModeNewBixby();
        }
        context.getContentResolver().insert(AlarmProvider.CONTENT_URI, item.getContentValues());
    }

    public static void deletePresetAlarm(Context context) {
        SharedPreferences pref = context.getSharedPreferences("isSetDefault", 0);
        int prefState = pref.getInt("alarmBootState", 0);
        Log.m41d("AlarmUtil", "deletePresetAlarm() prefState = " + prefState);
        if (prefState == 1) {
            try {
                if (AlarmProvider.getAlarmCount(context) == 1) {
                    Cursor alarmCursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, "active = 0 AND alarmtime = 600", null, null);
                    if (alarmCursor != null) {
                        if (alarmCursor.moveToFirst() && AlarmItem.isWeeklyAlarm(alarmCursor.getInt(5))) {
                            String alarmName = alarmCursor.getString(20);
                            if (alarmName != null && alarmName.isEmpty()) {
                                Log.secD("AlarmUtil", "deletePresetAlarm() delete preset alarm");
                                AlarmDataHandler.deleteAlarm(context, alarmCursor.getInt(0));
                            }
                        }
                        alarmCursor.close();
                    }
                }
            } catch (SecurityException e) {
                Log.secE("AlarmUtil", "deletePresetAlarm() SecurityException");
                return;
            } finally {
                pref.edit().putInt("alarmBootState", 2).apply();
            }
        }
        pref.edit().putInt("alarmBootState", 2).apply();
    }

    public static String getAlarmDefaultSoundUriString(Context context, AlarmRingtoneManager alarmRingtoneManager) {
        if (alarmRingtoneManager.hasCustomThemeRingtone()) {
            return Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(context).toString());
        }
        return alarmRingtoneManager.getAlarmTonePreference(context);
    }

    public static boolean isValidVibPattern(Context context, int vibPattern) {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.android.settings.personalvibration.PersonalVibrationProvider"), new String[]{"_id", "vibration_name", "vibration_pattern"}, "vibration_pattern=" + vibPattern, null, null, null);
        boolean isValidPattern = false;
        if (cursor != null) {
            Log.secD("AlarmUtil", "mCursor : " + cursor.getCount());
            if (cursor.getCount() != 0) {
                isValidPattern = true;
            }
            cursor.close();
        }
        Log.secD("AlarmUtil", "isValidVibPattern() / checkedVibPattern = " + vibPattern + ", isValidPattern : " + isValidPattern);
        return isValidPattern;
    }

    public static boolean isNewVibrationList(int vibrationPattern) {
        return vibrationPattern < 50033 || vibrationPattern > 50037;
    }

    public static boolean isNewCelebDefault(String path) {
        if (ClockUtils.isEnableString(path)) {
            return path.contains("android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01");
        }
        return false;
    }

    public static boolean isValidCelebrityVoicePath(Context context, String path) {
        boolean z = true;
        if (!isNewCelebDefault(path)) {
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), new String[]{"_id", "PKG_NAME"}, "PKG_NAME='" + path + '\'', null, null);
            z = false;
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    z = true;
                }
                cursor.close();
            }
            Log.secD("AlarmUtil", "isValidCelebrityVoicePath() / isValidCelebrityVoicePath : " + path + ", " + z);
        }
        return z;
    }

    public static void startGalaxyAppsForCelebrityVoice(Context context) {
        Log.secD("AlarmUtil", "startGalaxyAppsForCelebrityVoice");
        Intent intent = new Intent();
        intent.setData(Uri.parse("samsungapps://CategoryList/0000005450"));
        intent.putExtra("type", NotificationCompat.CATEGORY_ALARM);
        intent.putExtra("hideUpBtn", true);
        intent.putExtra("hideSearchBtn", true);
        intent.putExtra("source", "Alarm");
        intent.addFlags(335544352);
        context.startActivity(intent);
    }

    public static void playRingtonePreview(Context context, Uri RintoneUri, int alarmVolume, boolean isVisibleVolumePanel) {
        RingtonePlayer.setStreamVolume(context, alarmVolume, isVisibleVolumePanel);
        RingtonePlayer.playMediaPlayer(context, RintoneUri);
    }

    public static void playCelebPreview(Context context, String celebPath, int alarmVolume, boolean isVisibleVolumePanel) {
        String backgroundUri = "android.resource://com.sec.android.app.clockpackage/raw/default_sound";
        String newCelebUri = "";
        if (ClockUtils.isEnableString(celebPath) && isValidCelebrityVoicePath(context, celebPath) && !isExpiredCelebVoice(context, celebPath)) {
            if (celebPath.contains("sca")) {
                newCelebUri = getNewCelebPreviewUri(context, celebPath);
            } else if (celebPath.contains("bca")) {
                backgroundUri = getOldCelebPreviewUri(context, celebPath);
            }
            RingtonePlayer.setStreamVolume(context, alarmVolume, isVisibleVolumePanel);
            if (ClockUtils.isEnableString(backgroundUri)) {
                RingtonePlayer.playMediaPlayer(context, Uri.parse(backgroundUri));
            }
            if (ClockUtils.isEnableString(newCelebUri) && !(CelebUtils.getGreetingOnlyFullPath(context).isEmpty() && getPreviewFileName(context, celebPath).isEmpty())) {
                RingtonePlayer.playSecondMedioPlayer(context, Uri.parse(newCelebUri), 5000);
            }
            Log.secD("AlarmUtil", "playCelebPreview, backgroundUri : " + backgroundUri + ", newCelebUri : " + newCelebUri);
            return;
        }
        Log.secD("AlarmUtil", "playCelebPreview, backgroundUri : " + backgroundUri);
        playRingtonePreview(context, Uri.parse(backgroundUri), alarmVolume, isVisibleVolumePanel);
    }

    private static String getNewCelebPreviewUri(Context context, String celebPath) {
        if (isNewCelebDefault(celebPath)) {
            return CelebUtils.getGreetingOnlyFullPath(context);
        }
        String previewUri = "";
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), null, "PKG_NAME='" + celebPath + '\'', null, null);
        if (cursor == null) {
            return previewUri;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            previewUri = getNewCelebPreviewUri(context, celebPath, cursor.getString(cursor.getColumnIndex("TYPE")));
        }
        cursor.close();
        return previewUri;
    }

    private static String getNewCelebPreviewUri(Context context, String celebPath, String type) {
        return AlarmCvConstants.getUIDRootPath() + type + "/" + celebPath + "/" + "assets/" + getPreviewFileName(context, celebPath);
    }

    private static String getPreviewFileName(Context context, String celebVoicePath) {
        String greetingVariation = CelebUtils.getGreetingString();
        String preFixFileName = celebVoicePath.substring(celebVoicePath.lastIndexOf(46) + 1);
        if (CelebVoiceProvider.getCountOfSelection(context, celebVoicePath, "FILE_NAME LIKE '%" + preFixFileName + greetingVariation + "_lv%'") == 0) {
            return "";
        }
        return preFixFileName + greetingVariation + "_lv1" + ".ogg";
    }

    public static String getCelebVoiceName(Context context, String path) {
        if (isNewCelebDefault(path)) {
            return "celeb default";
        }
        String contentName = "";
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), null, "PKG_NAME='" + path + '\'', null, null);
        if (cursor == null) {
            return contentName;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            contentName = cursor.getString(cursor.getColumnIndex("CONTENT_NAME"));
        }
        cursor.close();
        return contentName;
    }
}
