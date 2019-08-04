package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.os.SemSystemProperties;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmCvConstants;
import com.sec.android.app.clockpackage.alarm.model.CelebVoiceProvider;
import com.sec.android.app.clockpackage.alarm.model.HolidayUtil;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.UserProfileDataBroker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class CelebUtils {
    private static final ArrayList<String> DAY_VARIATION = new ArrayList(Arrays.asList(new String[]{"", "_sun", "_mon", "_tue", "_wed", "_thu", "_fri", "_sat"}));
    private static final ArrayList<String> TIME_VARIATION = new ArrayList(Arrays.asList(new String[]{"", "_mo", "_no", "_af", "_ni"}));

    private static int getTimeType(int hourMinute) {
        if (hourMinute < 0 || hourMinute >= 2400) {
            return -1;
        }
        if (400 <= hourMinute && hourMinute < 1130) {
            return 1;
        }
        if (1130 <= hourMinute && hourMinute < 1400) {
            return 2;
        }
        if (1400 > hourMinute || hourMinute >= 1900) {
            return 4;
        }
        return 3;
    }

    private static String getTimeString(int hourMinute) {
        String timeVariation = (String) TIME_VARIATION.get(getTimeType(hourMinute));
        Log.secD("CelebUtils", "getTimeString timeVariation = " + timeVariation + " hourMinute = " + hourMinute);
        return timeVariation;
    }

    private static String getDayString() {
        String dayVariation = "";
        int dayOfWeek = Calendar.getInstance().get(7);
        if (dayOfWeek >= 1 && dayOfWeek <= 7) {
            dayVariation = (String) DAY_VARIATION.get(dayOfWeek);
        }
        Log.secD("CelebUtils", "getDayString dayVariation = " + dayVariation);
        return dayVariation;
    }

    private static int getUserGender(Context context) {
        return UserProfileDataBroker.getGenderTypeValue(context);
    }

    private static String getGenderString(Context context, int celebGender) {
        String genderVariation = "";
        int userGender = getUserGender(context);
        Log.secD("CelebUtils", "celebGender: " + celebGender + " userGender: " + userGender);
        if (celebGender == 1) {
            switch (userGender) {
                case 1:
                case 3:
                    return "_n";
                case 2:
                    return "_f";
                default:
                    return genderVariation;
            }
        } else if (celebGender != 2) {
            return genderVariation;
        } else {
            switch (userGender) {
                case 1:
                    return "_m";
                case 2:
                case 3:
                    return "_n";
                default:
                    return genderVariation;
            }
        }
    }

    private static String getWeatherTypeString(int weatherCodeNum) {
        int weatherTypeNo;
        switch (weatherCodeNum) {
            case 1:
            case 3:
            case 17:
            case 27:
            case 34:
            case 36:
            case 39:
            case 41:
            case 42:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                weatherTypeNo = 1;
                break;
            case 2:
            case 4:
            case 29:
            case 35:
            case 37:
            case 50:
            case 51:
            case 82:
                weatherTypeNo = 2;
                break;
            case 5:
                weatherTypeNo = 4;
                break;
            case 6:
            case 9:
            case 10:
                weatherTypeNo = 5;
                break;
            case 7:
            case 8:
                weatherTypeNo = 6;
                break;
            case 11:
            case 52:
                weatherTypeNo = 14;
                break;
            case 12:
            case 43:
            case 44:
            case 46:
                weatherTypeNo = 7;
                break;
            case 13:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            case 30:
            case 31:
            case 59:
            case 60:
            case 61:
            case 78:
                weatherTypeNo = 8;
                break;
            case 14:
            case 38:
            case 40:
            case 48:
            case 49:
            case 53:
            case 58:
                weatherTypeNo = 9;
                break;
            case 15:
            case 18:
            case 19:
            case 26:
            case 45:
            case 47:
            case 57:
            case 62:
            case 81:
                weatherTypeNo = 10;
                break;
            case 16:
            case 69:
            case 83:
                weatherTypeNo = 11;
                break;
            case 23:
            case 32:
            case 33:
            case 55:
            case 56:
            case 68:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                weatherTypeNo = 3;
                break;
            case 28:
            case 79:
            case 80:
                weatherTypeNo = 13;
                break;
            case 54:
                weatherTypeNo = 12;
                break;
            default:
                weatherTypeNo = 999;
                break;
        }
        String weatherTypeString = String.format("_w%03d", new Object[]{Integer.valueOf(weatherTypeNo)});
        Log.secD("CelebUtils", "getWeatherTypeString weatherTypeString = " + weatherTypeString);
        return weatherTypeString;
    }

    private static String getHolidayFileName(Context context, String celebVoicePath, long alertTime) {
        String fileName = null;
        String preFixFileName = celebVoicePath.substring(celebVoicePath.lastIndexOf(46) + 1);
        String holidayString = getHolidayString(context, alertTime);
        if (holidayString != null) {
            String selection = "FILE_NAME LIKE '%" + preFixFileName + holidayString + "_hv%'";
            int count = CelebVoiceProvider.getCountOfSelection(context, celebVoicePath, selection);
            Log.secD("CelebUtils", "getHolidayFileName selection = " + selection + " count = " + count);
            if (count > 0) {
                fileName = preFixFileName + holidayString + "_hv" + (((int) (Math.random() * ((double) count))) + 1) + ".ogg";
            }
        }
        Log.secD("CelebUtils", "getHolidayFileName fileName = " + fileName);
        return fileName;
    }

    public static String getHolidayFullPath(Context context, String celebVoicePath, String type, long alertTime) {
        String fullFilePath = null;
        String fileName = getHolidayFileName(context, celebVoicePath, alertTime);
        if (CelebVoiceProvider.getCountOfSelection(context, celebVoicePath, "FILE_NAME LIKE '%" + fileName + "'") == 1) {
            fullFilePath = AlarmCvConstants.getUIDRootPath() + type + "/" + celebVoicePath + "/assets" + "/" + fileName;
        }
        Log.secD("CelebUtils", "getHolidayFullPath fullFilePath = " + fullFilePath);
        return fullFilePath;
    }

    public static String getWeatherOrTimeDayGenderFullPath(Context context, String celebVoicePath, String type, int alarmTime, int weatherConditionCode) {
        String fileName;
        String fullFilePath = null;
        if (getWeatherTypeString(weatherConditionCode).equals(getWeatherTypeString(999)) || !isWeatherChoice()) {
            fileName = getTimeDayGenderFileName(context, celebVoicePath, alarmTime);
        } else {
            fileName = getWeatherFileName(context, celebVoicePath, weatherConditionCode);
        }
        String selection = "FILE_NAME LIKE '%" + fileName + "'";
        Log.m41d("CelebUtils", "getWeatherOrTimeDayGenderFullPath selection = " + selection);
        if (CelebVoiceProvider.getCountOfSelection(context, celebVoicePath, selection) == 1) {
            fullFilePath = AlarmCvConstants.getUIDRootPath() + type + "/" + celebVoicePath + "/assets" + "/" + fileName;
        }
        Log.secD("CelebUtils", "getWeatherOrTimeDayGenderFullPath fullFilePath = " + fullFilePath);
        return fullFilePath;
    }

    private static String[] getHolidayNames(Context context) {
        String countryName = SemSystemProperties.getCountryIso();
        String[] holidayNames = new String[5];
        Object obj = -1;
        switch (countryName.hashCode()) {
            case 2155:
                if (countryName.equals("CN")) {
                    obj = 2;
                    break;
                }
                break;
            case 2374:
                if (countryName.equals("JP")) {
                    obj = 1;
                    break;
                }
                break;
            case 2407:
                if (countryName.equals("KR")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return context.getResources().getStringArray(C0490R.array.ko_holiday_name);
            case 1:
                return context.getResources().getStringArray(C0490R.array.jp_holiday_name);
            case 2:
                return context.getResources().getStringArray(C0490R.array.cn_holiday_name);
            default:
                return holidayNames;
        }
    }

    private static String getHolidayString(Context context, long alertTime) {
        String holidayString = null;
        String todayHolidayName = HolidayUtil.getHolidayName(context, alertTime);
        if (todayHolidayName != null) {
            String[] holidayNames = getHolidayNames(context);
            int count = holidayNames.length;
            for (int i = 0; i < count; i++) {
                Log.secD("CelebUtils", "getHolidayString holidayNames[" + i + "] = " + holidayNames[i]);
                if (holidayNames[i].equals(todayHolidayName)) {
                    holidayString = String.format("_h%02d", new Object[]{Integer.valueOf(i + 1)});
                    Log.secD("CelebUtils", "getHolidayString todayHolidayName = " + todayHolidayName + " holidayString = " + holidayString);
                    break;
                }
            }
        }
        Log.secD("CelebUtils", "getHolidayString holidayString = " + holidayString);
        return holidayString;
    }

    public static String getGreetingString() {
        String greetingVariation = "";
        String locale = Locale.getDefault().toString();
        Object obj = -1;
        switch (locale.hashCode()) {
            case -1825825033:
                if (locale.equals("zh_HK_#Hant")) {
                    obj = 6;
                    break;
                }
                break;
            case -1274512914:
                if (locale.equals("fil_PH")) {
                    obj = 13;
                    break;
                }
                break;
            case -326827913:
                if (locale.equals("zh_TW_#Hant")) {
                    obj = 3;
                    break;
                }
                break;
            case 96646026:
                if (locale.equals("en_AU")) {
                    obj = 15;
                    break;
                }
                break;
            case 96646068:
                if (locale.equals("en_CA")) {
                    obj = 16;
                    break;
                }
                break;
            case 96646193:
                if (locale.equals("en_GB")) {
                    obj = 17;
                    break;
                }
                break;
            case 96646258:
                if (locale.equals("en_IE")) {
                    obj = 18;
                    break;
                }
                break;
            case 96646434:
                if (locale.equals("en_NZ")) {
                    obj = 19;
                    break;
                }
                break;
            case 96646478:
                if (locale.equals("en_PH")) {
                    obj = 20;
                    break;
                }
                break;
            case 96646644:
                if (locale.equals("en_US")) {
                    obj = 21;
                    break;
                }
                break;
            case 96646781:
                if (locale.equals("en_ZA")) {
                    obj = 22;
                    break;
                }
                break;
            case 100340341:
                if (locale.equals("in_ID")) {
                    obj = 8;
                    break;
                }
                break;
            case 100876622:
                if (locale.equals("ja_JP")) {
                    obj = 14;
                    break;
                }
                break;
            case 102217250:
                if (locale.equals("ko_KR")) {
                    obj = 23;
                    break;
                }
                break;
            case 104183525:
                if (locale.equals("ms_MY")) {
                    obj = 9;
                    break;
                }
                break;
            case 110320671:
                if (locale.equals("th_TH")) {
                    obj = 10;
                    break;
                }
                break;
            case 110439711:
                if (locale.equals("tl_PH")) {
                    obj = 12;
                    break;
                }
                break;
            case 112197572:
                if (locale.equals("vi_VN")) {
                    obj = 11;
                    break;
                }
                break;
            case 115861276:
                if (locale.equals("zh_CN")) {
                    obj = null;
                    break;
                }
                break;
            case 115861428:
                if (locale.equals("zh_HK")) {
                    obj = 5;
                    break;
                }
                break;
            case 115861812:
                if (locale.equals("zh_TW")) {
                    obj = 2;
                    break;
                }
                break;
            case 712568926:
                if (locale.equals("zh_CN_#Hans")) {
                    obj = 1;
                    break;
                }
                break;
            case 1848306773:
                if (locale.equals("zh_MO_#Hans")) {
                    obj = 4;
                    break;
                }
                break;
            case 1848306774:
                if (locale.equals("zh_MO_#Hant")) {
                    obj = 7;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
            case 1:
            case 2:
            case 3:
            case 4:
                greetingVariation = "01";
                break;
            case 5:
            case 6:
            case 7:
                greetingVariation = "02";
                break;
            case 8:
                greetingVariation = "03";
                break;
            case 9:
                greetingVariation = "04";
                break;
            case 10:
                greetingVariation = "05";
                break;
            case 11:
                greetingVariation = "06";
                break;
            case 12:
            case 13:
                greetingVariation = "09";
                break;
            case 14:
                greetingVariation = "08";
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                greetingVariation = "09";
                break;
            case 23:
                greetingVariation = "10";
                break;
        }
        greetingVariation = "_l" + greetingVariation;
        Log.secD("CelebUtils", "getGreetingString greetingVariation = " + greetingVariation + ", locale : " + locale);
        return greetingVariation;
    }

    public static String getOldCelebFullPath(String mCelebVoicePath, String type, int mWeatherConditionCode) {
        return AlarmCvConstants.getUIDRootPath() + type + "/" + mCelebVoicePath + "/assets" + "/" + (mCelebVoicePath.substring(mCelebVoicePath.lastIndexOf(46) + 1) + "_" + "w" + String.format("%03d", new Object[]{Integer.valueOf(mWeatherConditionCode)}) + ".ogg");
    }

    public static String getGreetingOnlyFullPath(Context context) {
        String fileName = "sca_default_v01" + getGreetingString() + "_lv1";
        Log.secD("CelebUtils", "setDelayTimeForTtsAlarm fullPath = " + fileName);
        if (context.getResources().getIdentifier("raw/" + fileName, null, "com.sec.android.app.clockpackage") == 0) {
            return "";
        }
        return "android.resource://com.sec.android.app.clockpackage/raw/" + fileName;
    }

    private static String getWeatherFileName(Context context, String celebVoicePath, int weatherConditionCode) {
        String fileName = null;
        String preFixFileName = celebVoicePath.substring(celebVoicePath.lastIndexOf(46) + 1);
        String weatherTypeString = getWeatherTypeString(weatherConditionCode);
        String selection = "FILE_NAME LIKE '%" + preFixFileName + weatherTypeString + "_wv%'";
        int count = CelebVoiceProvider.getCountOfSelection(context, celebVoicePath, selection);
        Log.secD("CelebUtils", "getWeatherFileName  selection = " + selection + " count = " + count);
        if (count > 0) {
            fileName = preFixFileName + weatherTypeString + "_wv" + (((int) (Math.random() * ((double) count))) + 1) + ".ogg";
        }
        Log.secD("CelebUtils", "getWeatherFileName fileName = " + fileName);
        return fileName;
    }

    private static String getTimeDayGenderString(Context context, String celebVoicePath, int hourMinute) {
        String timeVariation = getTimeString(hourMinute);
        String dayVariation = getDayString();
        String fullTimeDayGender = timeVariation + dayVariation + getGenderString(context, CelebVoiceProvider.getCelebGender(context, celebVoicePath));
        Log.secD("CelebUtils", "getTimeDayGenderString fullTimeDayGender = " + fullTimeDayGender);
        return fullTimeDayGender;
    }

    private static String getTimeDayGenderFileName(Context context, String celebVoicePath, int alarmTime) {
        String fileName = celebVoicePath.substring(celebVoicePath.lastIndexOf(46) + 1) + getTimeDayGenderString(context, celebVoicePath, alarmTime) + ".ogg";
        Log.secD("CelebUtils", "getTimeDayGenderFileName fileName = " + fileName);
        return fileName;
    }

    private static boolean isWeatherChoice() {
        return ((int) (Math.random() * 10.0d)) % 4 == 0;
    }
}
