package com.sec.android.app.clockpackage.common.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.android.emergencymode.SemEmergencyManager;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.EventBuilder;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.ScreenViewBuilder;
import com.samsung.context.sdk.samsunganalytics.SamsungAnalytics;
import com.sec.android.app.clockpackage.common.C0645R;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClockUtils {
    public static long alarmAlertTimeInCall = 0;
    private static final StringBuilder mBuilder = new StringBuilder();
    public static boolean sIsTimerAlertStarted = false;
    public static long timerAlertTimeInCall = 0;

    public static String toTwoDigitString(int number) {
        Object[] mArgs = new Object[]{Integer.valueOf(number)};
        mBuilder.delete(0, mBuilder.length());
        if (number < 10) {
            mBuilder.append(NumberFormat.getInstance().format(0)).append(NumberFormat.getInstance().format(Integer.valueOf(String.format("%d", mArgs))));
        } else {
            mBuilder.append(NumberFormat.getInstance().format(Integer.valueOf(String.format("%2d", mArgs))));
        }
        return mBuilder.toString();
    }

    public static String toTwoDigitStringUSLocale(int number) {
        Object[] mArgs = new Object[]{Integer.valueOf(number)};
        mBuilder.delete(0, mBuilder.length());
        if (number < 10) {
            mBuilder.append(NumberFormat.getInstance(Locale.US).format(0)).append(NumberFormat.getInstance(Locale.US).format(Integer.valueOf(String.format("%d", mArgs))));
        } else {
            mBuilder.append(NumberFormat.getInstance(Locale.US).format(Integer.valueOf(String.format("%2d", mArgs))));
        }
        return mBuilder.toString();
    }

    public static String toDigitString(int number) {
        Object[] mArgs = new Object[]{Integer.valueOf(number)};
        mBuilder.delete(0, mBuilder.length());
        mBuilder.append(String.format("%d", mArgs));
        return mBuilder.toString();
    }

    public static int getAmPmHour(int h) {
        int mul = 1;
        if (h < 12) {
            mul = -1;
        }
        h %= 12;
        if (h == 0) {
            h = 12;
        }
        return mul * h;
    }

    public static boolean isValidNumberString(String targetString) {
        if (!isEnableString(targetString)) {
            return false;
        }
        int size = targetString.length();
        for (int a = 0; a < size; a++) {
            char c = targetString.charAt(a);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEnableString(String targetString) {
        return (targetString == null || targetString.isEmpty() || "".equals(targetString)) ? false : true;
    }

    public static Context getBootAwareContext(Context context) {
        if (!StateUtils.isDirectBootMode(context) || StateUtils.isUserUnlockedDevice(context)) {
            return context;
        }
        return context.createDeviceProtectedStorageContext();
    }

    public static boolean isStringContainAndStartMatchWithoutSpaceForBixby(String expectedString, String actualString) {
        expectedString = expectedString.replaceAll("\\p{Z}", "").toLowerCase();
        actualString = actualString.replaceAll("\\p{Z}", "").toLowerCase();
        if (actualString.length() <= 0) {
            return false;
        }
        if (expectedString.length() > actualString.length()) {
            return expectedString.startsWith(actualString);
        }
        return actualString.contains(expectedString);
    }

    public static String getVersionInfo(Context context) {
        String version = "Unknown";
        if (context == null) {
            return version;
        }
        try {
            version = context.getApplicationContext().getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.secE("ClockUtils", "getVersionInfo :" + e.getMessage());
        }
        String str = version;
        return version;
    }

    public static int getVersionCode(Context context) {
        int version = 0;
        if (context == null) {
            return version;
        }
        try {
            version = context.getApplicationContext().getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.secE("ClockUtils", "getVersionCode :" + e.getMessage());
        }
        int i = version;
        return version;
    }

    public static String getDayOfWeekString(Context context, int day, int length) {
        if (length == 0) {
            switch (day) {
                case 1:
                    return context.getResources().getString(C0645R.string.sun1);
                case 2:
                    return context.getResources().getString(C0645R.string.mon1);
                case 3:
                    return context.getResources().getString(C0645R.string.tue1);
                case 4:
                    return context.getResources().getString(C0645R.string.wed1);
                case 5:
                    return context.getResources().getString(C0645R.string.thu1);
                case 6:
                    return context.getResources().getString(C0645R.string.fri1);
                case 7:
                    return context.getResources().getString(C0645R.string.sat1);
                default:
                    Log.secE("ClockUtils", "1. day value is invalid : " + day);
                    return null;
            }
        } else if (length == 3) {
            switch (day) {
                case 1:
                    return context.getResources().getString(C0645R.string.alarmrepeatday0);
                case 2:
                    return context.getResources().getString(C0645R.string.alarmrepeatday1);
                case 3:
                    return context.getResources().getString(C0645R.string.alarmrepeatday2);
                case 4:
                    return context.getResources().getString(C0645R.string.alarmrepeatday3);
                case 5:
                    return context.getResources().getString(C0645R.string.alarmrepeatday4);
                case 6:
                    return context.getResources().getString(C0645R.string.alarmrepeatday5);
                case 7:
                    return context.getResources().getString(C0645R.string.alarmrepeatday6);
                default:
                    Log.secE("ClockUtils", "2. day value is invalid : " + day);
                    return null;
            }
        } else if (length == 2) {
            switch (day) {
                case 1:
                    return context.getResources().getString(C0645R.string.sun);
                case 2:
                    return context.getResources().getString(C0645R.string.mon);
                case 3:
                    return context.getResources().getString(C0645R.string.tue);
                case 4:
                    return context.getResources().getString(C0645R.string.wed);
                case 5:
                    return context.getResources().getString(C0645R.string.thu);
                case 6:
                    return context.getResources().getString(C0645R.string.fri);
                case 7:
                    return context.getResources().getString(C0645R.string.sat);
                default:
                    Log.secE("ClockUtils", "3. day value is invalid : " + day);
                    return null;
            }
        } else if (length != 4) {
            return null;
        } else {
            switch (day) {
                case 1:
                    return context.getResources().getString(C0645R.string.sunday);
                case 2:
                    return context.getResources().getString(C0645R.string.monday);
                case 3:
                    return context.getResources().getString(C0645R.string.tuesday);
                case 4:
                    return context.getResources().getString(C0645R.string.wednesday);
                case 5:
                    return context.getResources().getString(C0645R.string.thursday);
                case 6:
                    return context.getResources().getString(C0645R.string.friday);
                case 7:
                    return context.getResources().getString(C0645R.string.saturday);
                default:
                    Log.secE("ClockUtils", "4. day value is invalid : " + day);
                    return null;
            }
        }
    }

    public static int getStartDayOfWeek() {
        return Calendar.getInstance().getFirstDayOfWeek();
    }

    public static String getTopActivity(Context context) {
        List<RunningTaskInfo> info = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        return (info == null || info.size() <= 0 || info.get(0) == null) ? "" : ((RunningTaskInfo) info.get(0)).topActivity.getClassName();
    }

    public static int getTopActivityProcessId(Context context) {
        List<RunningAppProcessInfo> processInfos = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (((RunningAppProcessInfo) processInfos.get(i)).processName.equals("com.sec.android.app.clockpackage")) {
                Log.m41d("ClockUtils", "getTopActivityProcessId pid = " + ((RunningAppProcessInfo) processInfos.get(i)).pid + " Task.size() = " + processInfos.size());
                return ((RunningAppProcessInfo) processInfos.get(i)).pid;
            }
        }
        Log.m41d("ClockUtils", "getTopActivityProcessId return -1");
        return -1;
    }

    public static boolean isRunningClass(Context ctx, String className) {
        for (RunningTaskInfo task : ((ActivityManager) ctx.getSystemService("activity")).getRunningTasks(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED)) {
            if (className.equalsIgnoreCase(task.baseActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getMyProcessId() {
        Log.m41d("ClockUtils", "getMyProcessId myPid = " + Process.myPid());
        return Process.myPid();
    }

    public static void sendBCSResponseIntent(Context context, String Information) {
        Intent at_intent = new Intent("com.samsung.intent.action.BCS_RESPONSE");
        at_intent.putExtra("response", Information);
        context.sendBroadcast(at_intent);
    }

    public static Typeface getFontFromOpenTheme(Context context) {
        Typeface font = null;
        String fontPath = System.getString(context.getContentResolver(), "theme_font_clock");
        if (fontPath != null) {
            try {
                if (!TextUtils.isEmpty(fontPath)) {
                    font = Typeface.createFromFile(fontPath);
                }
            } catch (Exception e) {
                Log.secE("ClockUtils", "Exception : " + e);
            }
        }
        return font;
    }

    public static String getFormatDateTime(Context mContext, long settingTime, boolean isTTS) {
        String currentLanguage = Locale.getDefault().getLanguage();
        if (currentLanguage.equals(Locale.KOREA.getLanguage()) || currentLanguage.equals(Locale.JAPAN.getLanguage()) || currentLanguage.equalsIgnoreCase("nb") || currentLanguage.equalsIgnoreCase("da") || currentLanguage.equalsIgnoreCase("fr") || currentLanguage.equalsIgnoreCase("lt") || currentLanguage.equalsIgnoreCase("hu") || currentLanguage.equalsIgnoreCase("pa") || currentLanguage.equalsIgnoreCase("vi") || currentLanguage.equalsIgnoreCase("iw") || Locale.getDefault().getDisplayLanguage().equals("polski")) {
            int flags = 0;
            if (!isTTS) {
                flags = 0 | 524288;
            }
            return DateUtils.formatDateTime(mContext, settingTime, (flags | 2) | 16);
        } else if (currentLanguage.equalsIgnoreCase("ar")) {
            weekFlags = 0;
            dateFlags = 0;
            if (!isTTS) {
                weekFlags = 0 | 524288;
                dateFlags = 0 | 524288;
            }
            selectDayOfWeek = DateUtils.formatDateTime(mContext, settingTime, weekFlags | 2);
            return selectDayOfWeek + String.format("، ", new Object[]{","}) + DateUtils.formatDateTime(mContext, settingTime, dateFlags | 16);
        } else if (currentLanguage.equalsIgnoreCase("my")) {
            weekFlags = 0;
            if (!isTTS) {
                weekFlags = 0 | 524288;
            }
            selectDayOfWeek = DateUtils.formatDateTime(mContext, settingTime, weekFlags | 2);
            return selectDayOfWeek + ", " + DateUtils.formatDateTime(mContext, settingTime, 0);
        } else if (currentLanguage.equalsIgnoreCase("de")) {
            weekFlags = 0;
            if (!isTTS) {
                weekFlags = 0 | 524288;
            }
            selectDayOfWeek = DateUtils.formatDateTime(mContext, settingTime, weekFlags | 2);
            dateFlags = 0;
            if (StateUtils.getScreenDensityIndex(mContext) >= 2) {
                dateFlags = 0 | 524288;
            }
            return selectDayOfWeek + ", " + DateUtils.formatDateTime(mContext, settingTime, dateFlags);
        } else if (currentLanguage.equals(Locale.CHINA.getLanguage())) {
            weekFlags = 0;
            dateFlags = 0;
            if (!isTTS) {
                weekFlags = 0 | 524288;
                dateFlags = 0 | 524288;
            }
            return DateUtils.formatDateTime(mContext, settingTime, dateFlags | 16) + ", " + DateUtils.formatDateTime(mContext, settingTime, weekFlags | 2);
        } else {
            weekFlags = 0;
            dateFlags = 0;
            if (!isTTS) {
                weekFlags = 0 | 524288;
                dateFlags = 0 | 524288;
            }
            selectDayOfWeek = DateUtils.formatDateTime(mContext, settingTime, weekFlags | 2);
            return selectDayOfWeek + ", " + DateUtils.formatDateTime(mContext, settingTime, dateFlags | 16);
        }
    }

    public static String numberToArabic(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        int i = 0;
        while (i < sb.length()) {
            if (sb.charAt(i) >= '0' && sb.charAt(i) <= '9') {
                sb.setCharAt(i, (char) ((sb.charAt(i) - 48) + 1632));
            }
            i++;
        }
        return sb.toString();
    }

    public static String numberToPersian(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        int i = 0;
        while (i < sb.length()) {
            if (sb.charAt(i) >= '0' && sb.charAt(i) <= '9') {
                sb.setCharAt(i, (char) ((sb.charAt(i) - 48) + 1776));
            }
            i++;
        }
        return sb.toString();
    }

    public static String arabicToNumber(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        int i = 0;
        while (i < sb.length()) {
            if (sb.charAt(i) >= '٠' && sb.charAt(i) <= '٩') {
                sb.setCharAt(i, (char) ((sb.charAt(i) - 1632) + 48));
            }
            i++;
        }
        return sb.toString();
    }

    public static boolean isValidPackageInEmergency(Context context) {
        return SemEmergencyManager.getInstance(context).checkValidPackage("com.sec.android.app.clockpackage", null, 1);
    }

    public static int getGlobalSettingFontSize(Context context) {
        return Global.getInt(context.getContentResolver(), "font_size", 2);
    }

    public static void setAccessibilityFontSize(Context context, TextView view) {
        int fontLevel = Global.getInt(context.getContentResolver(), "font_size", 2);
        if (fontLevel >= 7 && fontLevel <= 10) {
            float textSize = (float) context.getResources().getDimensionPixelSize(C0645R.dimen.default_list_item_size);
            switch (fontLevel) {
                case 7:
                    textSize = (float) context.getResources().getDimensionPixelSize(C0645R.dimen.accessibility_huge_font_size8);
                    break;
                case 8:
                    textSize = (float) context.getResources().getDimensionPixelSize(C0645R.dimen.accessibility_huge_font_size9);
                    break;
                case 9:
                    textSize = (float) context.getResources().getDimensionPixelSize(C0645R.dimen.accessibility_huge_font_size10);
                    break;
                case 10:
                    textSize = (float) context.getResources().getDimensionPixelSize(C0645R.dimen.accessibility_huge_font_size11);
                    break;
            }
            if (view != null) {
                try {
                    view.setTextSize(0, textSize);
                } catch (Exception e) {
                    Log.secE("ClockUtils", "Exception : " + e);
                }
            }
        }
    }

    public static void setTextSize(TextView view, float textSize) {
        if (view != null) {
            try {
                view.setTextSize(0, (float) Math.ceil((double) textSize));
            } catch (Exception e) {
                Log.secE("ClockUtils", "Exception");
            }
        }
    }

    public static void setLargeTextSize(Context context, TextView[] textViews, float maxFontScale) {
        if (context.getResources().getConfiguration().fontScale > maxFontScale) {
            for (TextView textView : textViews) {
                if (textView != null) {
                    textView.setTextSize(1, (textView.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity) * maxFontScale);
                }
            }
        }
    }

    private static void setTextSize(Context context, TextView view, float textSize, float maxSizeScale) {
        if (view != null) {
            float fontScale = context.getResources().getConfiguration().fontScale;
            float defaultTextSize = textSize / fontScale;
            Log.secD("ClockUtils", "setLargeTextSize fontScale : " + fontScale + ", " + textSize + ", " + defaultTextSize);
            if (fontScale > maxSizeScale) {
                fontScale = maxSizeScale;
            }
            setTextSize(view, defaultTextSize * fontScale);
        }
    }

    public static void setLargeTextSize(Context context, TextView view, float textSize) {
        setTextSize(context, view, textSize, 1.3f);
    }

    public static void setLargeTextSizeForAppBar(Context context, TextView view, float textSize) {
        setTextSize(context, view, textSize, 1.1f);
    }

    public static int getNavigationBarHeight(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ? res.getDimensionPixelSize(resourceId) : 0;
    }

    public static int getStatusBarHeight(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void nullViewDrawablesRecursive(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            try {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    nullViewDrawablesRecursive(viewGroup.getChildAt(index));
                }
                return;
            } catch (Exception e) {
                Log.secE("ClockUtils", "nullViewDrawablesRecursive InflateException");
                return;
            }
        }
        nullViewDrawable(view);
    }

    private static void nullViewDrawable(View view) {
        try {
            view.setOnClickListener(null);
        } catch (Exception e) {
            Log.secE("ClockUtils", "nullViewDrawable setOnClickListener Exception");
        }
        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Exception e2) {
            Log.secE("ClockUtils", "nullViewDrawable setOnCreateContextMenuListener Exception");
        }
        try {
            view.setOnFocusChangeListener(null);
        } catch (Exception e3) {
            Log.secE("ClockUtils", "nullViewDrawable setOnFocusChangeListener Exception");
        }
        try {
            view.setOnKeyListener(null);
        } catch (Exception e4) {
            Log.secE("ClockUtils", "nullViewDrawable setOnKeyListener Exception");
        }
        try {
            view.setOnLongClickListener(null);
        } catch (Exception e5) {
            Log.secE("ClockUtils", "nullViewDrawable setOnLongClickListener Exception");
        }
        try {
            view.setTouchDelegate(null);
        } catch (Exception e6) {
            Log.secE("ClockUtils", "nullViewDrawable setTouchDelegate Exception");
        }
        try {
            view.setBackground(null);
        } catch (Exception e7) {
            Log.secE("ClockUtils", "nullViewDrawable setBackground Exception");
        }
        try {
            view.setAnimation(null);
        } catch (Exception e8) {
            Log.secE("ClockUtils", "nullViewDrawable setAnimation Exception");
        }
        try {
            view.setContentDescription(null);
        } catch (Exception e9) {
            Log.secE("ClockUtils", "nullViewDrawable setContentDescription Exception");
        }
        try {
            view.setTag(null);
        } catch (Exception e10) {
            Log.secE("ClockUtils", "nullViewDrawable setTag Exception");
        }
        Drawable d = view.getBackground();
        if (d != null) {
            try {
                d.setCallback(null);
            } catch (Exception e11) {
                Log.secE("ClockUtils", "nullViewDrawable setCallback Exception");
            }
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            d = imageView.getDrawable();
            if (d != null) {
                d.setCallback(null);
            }
            imageView.setImageDrawable(null);
            imageView.setBackground(null);
        }
    }

    public static int convertDpToPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
    }

    public static void setNumber(TextView tv, int time) {
        try {
            tv.setText(toDigitString(time));
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.secE("ClockUtils", "Exception : " + e.toString());
        }
    }

    public static void insertSaLog(String screenID) {
        Log.secD("Clock_SA", "screenID = " + screenID);
        if (screenID != null) {
            SamsungAnalytics.getInstance().sendLog(((ScreenViewBuilder) new ScreenViewBuilder().setScreenView(screenID)).build());
        }
    }

    public static void insertSaLog(String screenID, String event) {
        Log.secD("Clock_SA", "screenID = " + screenID + " / event = " + event);
        if (screenID != null) {
            SamsungAnalytics.getInstance().sendLog(((EventBuilder) new EventBuilder().setScreenView(screenID)).setEventName(event).build());
        }
    }

    public static void insertSaLog(String screenID, String event, String detail, long value) {
        Log.secD("Clock_SA", "screenID = " + screenID + " / event = " + event + "/ detail = " + detail + " / value = " + value);
        if (screenID != null) {
            Map<String, String> customDimen = new HashMap();
            customDimen.put("det", detail);
            SamsungAnalytics.getInstance().sendLog(((EventBuilder) ((EventBuilder) new EventBuilder().setScreenView(screenID)).setEventName(event).setDimension(customDimen)).setEventValue(value).build());
        }
    }

    public static void insertSaLog(String screenID, String event, long value) {
        Log.secD("Clock_SA", "screenID = " + screenID + " / event = " + event + " / value = " + value);
        if (screenID != null) {
            SamsungAnalytics.getInstance().sendLog(((EventBuilder) new EventBuilder().setScreenView(screenID)).setEventName(event).setEventValue(value).build());
        }
    }

    public static void insertSaLog(String screenID, String event, String detail) {
        Log.secD("Clock_SA", "screenID = " + screenID + " / event = " + event + " / detail = " + detail);
        if (screenID != null) {
            Map<String, String> customDimen = new HashMap();
            customDimen.put("det", detail);
            SamsungAnalytics.getInstance().sendLog(((EventBuilder) ((EventBuilder) new EventBuilder().setScreenView(screenID)).setEventName(event).setDimension(customDimen)).build());
        }
    }

    public static void insertSaStatusLog(Context context, String status, long value) {
        Log.secD("Clock_SA", "status = " + status + " / value = " + value);
        Editor editor = context.getSharedPreferences("SASettingPref", 0).edit();
        editor.putLong(status, value);
        editor.apply();
    }

    public static void insertSaStatusLog(Context context, String status, String detail) {
        Log.secD("Clock_SA", "status= " + status + "/ detail = " + detail);
        Editor editor = context.getSharedPreferences("SASettingPref", 0).edit();
        editor.putString(status, detail);
        editor.apply();
    }

    @SuppressLint({"PrivateResource"})
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(C0645R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
    }

    public static String getWhereKey(String[] keys) {
        String stringKey = "";
        if (keys == null) {
            return stringKey;
        }
        for (String key : keys) {
            if (!(key == null || key.isEmpty())) {
                stringKey = stringKey.concat(",").concat(key);
            }
        }
        stringKey = '(' + (stringKey + ')').substring(1);
        String str = stringKey;
        return stringKey;
    }

    public static String[] toStringArray(ArrayList<String> al) {
        int count = al.size();
        String[] result = new String[count];
        for (int pos = 0; pos < count; pos++) {
            result[pos] = (String) al.get(pos);
        }
        return result;
    }

    public static String getTimeSeparatorText(Context context) {
        String separatorText;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(context) ? "Hm" : "hm");
        int hourIndex = bestDateTimePattern.lastIndexOf(72);
        if (hourIndex == -1) {
            hourIndex = bestDateTimePattern.lastIndexOf(104);
        }
        if (hourIndex == -1) {
            separatorText = ":";
        } else {
            int minuteIndex = bestDateTimePattern.indexOf(109, hourIndex + 1);
            if (minuteIndex == -1) {
                separatorText = Character.toString(bestDateTimePattern.charAt(hourIndex + 1));
            } else {
                separatorText = bestDateTimePattern.substring(hourIndex + 1, minuteIndex);
            }
        }
        return separatorText.replace("'", "");
    }

    public static Uri setMusicLibraryRingtone(Uri pickedUri, Context context, int streamType) {
        if (pickedUri != null && (pickedUri.isRelative() || pickedUri.getScheme().equals("file") || !pickedUri.getHost().equals("media"))) {
            pickedUri = updateMediaDB(pickedUri, context);
            Log.secI("ClockUtils", "setMusicLibraryRingtone() - pickedUri : " + pickedUri);
        }
        if (pickedUri == null) {
            return null;
        }
        ContentValues cv = new ContentValues();
        if (streamType == 4) {
            cv.put("is_alarm", "1");
        } else {
            cv.put("is_ringtone", "1");
        }
        try {
            Log.secI("ClockUtils", "pickedUri update!!" + pickedUri);
            context.getContentResolver().update(pickedUri, cv, "_id=?", new String[]{String.valueOf(ContentUris.parseId(pickedUri))});
            return pickedUri;
        } catch (NumberFormatException e) {
            Log.secE("ClockUtils", "Exception : " + e.toString());
            return pickedUri;
        }
    }

    private static Uri updateMediaDB(Uri originalUri, Context context) {
        Log.secD("ClockUtils", "UpdateMediaDB");
        String filePath = originalUri.getPath();
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if ("com.metago.astro.filecontent".equals(originalUri.getHost())) {
            int index = filePath.indexOf("mnt");
            if (index > 0) {
                filePath = filePath.substring(index - 1).replace("/mnt/sdcard", Environment.getExternalStorageDirectory().toString());
            }
        }
        if (TextUtils.isEmpty(extension)) {
            int dotPos = filePath.lastIndexOf(46);
            if (dotPos >= 0) {
                extension = filePath.substring(dotPos + 1);
            }
        }
        Uri uri;
        try {
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
            if (mimeType == null) {
                uri = null;
                return null;
            }
            Log.secD("ClockUtils", "updateMediaDB - extension(" + extension + "), mimeType(" + mimeType + ')');
            if (mimeType.startsWith("audio")) {
                if (extension.equals("3ga")) {
                    Log.secD("ClockUtils", "updateMediaDB - no mimeType, but it's audio file extension - " + extension);
                }
                Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, "_data=\"" + filePath + '\"', null, null);
                Uri uri2;
                if (cursor == null || cursor.getCount() <= 0) {
                    File file = new File(filePath);
                    ContentValues values = new ContentValues();
                    String absolutePath = file.getAbsolutePath();
                    values.put("_data", absolutePath);
                    values.put("title", file.getName().substring(0, file.getName().lastIndexOf(".")));
                    values.put("mime_type", "audio/*");
                    values.put("_size", Long.valueOf(file.length()));
                    Uri tempUri = Media.getContentUriForPath(file.getAbsolutePath());
                    context.getContentResolver().delete(tempUri, "_data=\"" + file.getAbsolutePath() + '\"', null);
                    uri2 = context.getContentResolver().insert(tempUri, values);
                    Log.secD("ClockUtils", "updateMediaDB - tempUri(" + tempUri + "), newUri(" + uri2 + ')');
                    uri = uri2;
                    return uri2;
                }
                cursor.moveToFirst();
                Log.secD("ClockUtils", "this file is already has uri value.");
                uri2 = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, (long) cursor.getInt(cursor.getColumnIndex("_id")));
                cursor.close();
                uri = uri2;
                return uri2;
            }
            Log.secD("ClockUtils", "updateMediaDB - mimeType is not audio - return null");
            uri = null;
            return null;
        } catch (Exception e) {
            Log.secE("ClockUtils", "updateMediaDB - exception is Occurred - return null" + e);
            uri = null;
            return null;
        }
    }

    public static boolean getPrefDefault(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences("isSetDefault", 0);
        return pref.getBoolean(name, pref.contains("isSetDefault"));
    }

    public static void setPrefDefault(Context context, String name) {
        if (!getPrefDefault(context, name)) {
            Editor ed = context.getSharedPreferences("isSetDefault", 0).edit();
            ed.putBoolean(name, true);
            ed.apply();
        }
    }

    public static void updateListBothSideMargin(Context context, ViewGroup layout) {
        if (layout != null) {
            if (StateUtils.isMultiWindowMinSize(context, 801, true)) {
                layout.getLayoutParams().width = -1;
                return;
            }
            layout.getLayoutParams().width = (int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.75d);
        }
    }

    public static void startAlertSettingActivity(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.commonalert.activity.AlertSettingActivity");
        intent.setFlags(393216);
        context.startActivity(intent);
    }

    public static void startCustomizationServiceSettingActivity(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.common.activity.CustomizationServiceOnActivity");
        intent.setFlags(393216);
        context.startActivity(intent);
    }
}
