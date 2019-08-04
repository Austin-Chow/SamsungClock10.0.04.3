package com.sec.android.app.clockpackage.common.feature;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.SemSystemProperties;
import android.os.Vibrator;
import android.util.Log;
import com.samsung.android.feature.SemCscFeature;
import com.samsung.android.feature.SemFloatingFeature;
import java.util.List;

public class Feature {
    public static final boolean DEBUG_ENG;
    private static final Uri VOICE_ENABLE_URI = Uri.parse("content://com.samsung.android.bixby.agent.settings/bixby_voice_isenable");

    static {
        boolean z = true;
        if (SemSystemProperties.getInt("ro.debuggable", 0) != 1) {
            z = false;
        }
        DEBUG_ENG = z;
    }

    private static boolean getCscEnableStatus(String featureName) {
        try {
            return SemCscFeature.getInstance().getBoolean(featureName);
        } catch (NullPointerException e) {
            Log.e("Feature", "NullPointerException occurs");
            return false;
        }
    }

    private static String getCscString(String featureName) {
        return SemCscFeature.getInstance().getString(featureName);
    }

    private static boolean getFloatingEnableStatus(String featureName) {
        return SemFloatingFeature.getInstance().getBoolean(featureName);
    }

    private static String getFloatingString(String featureName) {
        return SemFloatingFeature.getInstance().getString(featureName);
    }

    public static boolean hasActivity(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        if (list == null) {
            return false;
        }
        Log.d("Feature", "hasActivity : " + list.size());
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean hasPackage(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        try {
            packageManager.getApplicationInfo(packageName, 128);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isPackageSupportedVersion(Context context, String packageName, int minVersionCode) {
        boolean bSupportedVersion = false;
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                    if (packageInfo.versionCode >= minVersionCode) {
                        bSupportedVersion = true;
                    } else {
                        Log.d("Feature", "isPackageSupportedVersion packageInfo.versionCode = " + packageInfo.versionCode + " minVersionCode = " + minVersionCode);
                    }
                } catch (NameNotFoundException e) {
                    boolean z = false;
                    return 0;
                }
            }
        }
        Log.d("Feature", "isPackageSupportedVersion bSupportedVersion = " + bSupportedVersion);
        return bSupportedVersion;
    }

    public static boolean isSupportVibration(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        return vibrator != null && vibrator.hasVibrator();
    }

    public static boolean isSupportHolidayAlarm() {
        return getCscEnableStatus("CscFeature_Clock_SupportAlarmOptionMenuForHoliday");
    }

    public static boolean isSupportHolidayForCeleb() {
        return false;
    }

    public static boolean isSupportSubstituteHolidayMenu() {
        return getCscEnableStatus("CscFeature_Clock_SupportAlarmSubstituteHolidayMenu");
    }

    public static boolean isSupportAlarmOptionMenuForWorkingDay() {
        return getCscEnableStatus("CscFeature_Clock_SupportAlarmOptionMenuForWorkingDay");
    }

    public static boolean isSupportNewsCpForBixbyBriefing() {
        boolean bSupportNewsCpForBixbyBriefing = false;
        String countryCode = SemSystemProperties.getCountryIso();
        if ("US".equalsIgnoreCase(countryCode) || "KR".equalsIgnoreCase(countryCode) || "CN".equalsIgnoreCase(countryCode) || "GB".equalsIgnoreCase(countryCode) || "FR".equalsIgnoreCase(countryCode) || "DE".equalsIgnoreCase(countryCode) || "IT".equalsIgnoreCase(countryCode) || "ES".equalsIgnoreCase(countryCode)) {
            bSupportNewsCpForBixbyBriefing = true;
        }
        Log.d("Feature", "bSupportNewsCpForBixbyBriefing = " + bSupportNewsCpForBixbyBriefing);
        return bSupportNewsCpForBixbyBriefing;
    }

    public static boolean isSupportCelebrityAlarm() {
        String countryCode = SemSystemProperties.getCountryIso();
        if ("KR".equalsIgnoreCase(countryCode) || "CN".equalsIgnoreCase(countryCode) || "JP".equalsIgnoreCase(countryCode) || "TW".equalsIgnoreCase(countryCode) || "HK".equalsIgnoreCase(countryCode) || "SG".equalsIgnoreCase(countryCode) || "MY".equalsIgnoreCase(countryCode) || "TH".equalsIgnoreCase(countryCode) || "VN".equalsIgnoreCase(countryCode) || "PH".equalsIgnoreCase(countryCode) || "ID".equalsIgnoreCase(countryCode)) {
            return true;
        }
        return false;
    }

    public static boolean isCelebrityAlarmAsDefaultSoundMode() {
        if ("KR".equalsIgnoreCase(SemSystemProperties.getCountryIso())) {
            return true;
        }
        return false;
    }

    public static boolean isSupportBixbyBriefingMenu(Context context) {
        return isBixbySupported() && isPackageSupportedVersion(context, "com.samsung.android.bixby.agent", 100405000) && isEnableBixbyVoice(context);
    }

    private static boolean isEnableBixbyVoice(Context context) {
        boolean bEnableBixbyVoice = false;
        try {
            Cursor c = context.getContentResolver().query(VOICE_ENABLE_URI, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                bEnableBixbyVoice = c.getInt(c.getColumnIndex("bixby_voice_isenable")) == 1;
                c.close();
            }
        } catch (SecurityException e) {
            Log.e("Feature", "SecurityException while accessing bixby voice setting");
        } catch (Exception e2) {
            Log.e("Feature", "Exception : " + e2.toString());
        }
        if (!bEnableBixbyVoice) {
            Log.d("Feature", "isEnableBixbyVoice bEnableBixbyVoice = false");
        }
        return bEnableBixbyVoice;
    }

    public static boolean isSupportAlarmSoundMenu() {
        return getCscEnableStatus("CscFeature_Clock_SupportAlarmSoundMenu");
    }

    public static boolean isMobileKeyboardSupported() {
        return getFloatingEnableStatus("SEC_FLOATING_FEATURE_COMMON_SUPPORT_NFC_HW_KEYBOARD");
    }

    public static boolean isSupportForceImmersive() {
        return getFloatingString("SEC_FLOATING_FEATURE_FRAMEWORK_CONFIG_NAVIGATION_BAR_THEME").contains("SupportForceImmersive");
    }

    public static boolean isDCM(Context context) {
        return hasPackage(context, "com.nttdocomo.android.dhome");
    }

    public static boolean isSupportTimerResetButton() {
        return getCscEnableStatus("CscFeature_Clock_SupportTimerResetButton");
    }

    public static boolean isSupportChinaPresetTimer() {
        return "China".equalsIgnoreCase(getCscString("CscFeature_Clock_ConfigLocalTimerPreset")) || getCscEnableStatus("CscFeature_Clock_SupportAlarmOptionMenuForWorkingDay");
    }

    public static boolean isLiveDemoBinary() {
        return getCscEnableStatus("CscFeature_Common_EnableLiveDemo") || getFloatingEnableStatus("SEC_FLOATING_FEATURE_COMMON_SUPPORT_UNPACK");
    }

    public static boolean isMirroringSupported() {
        return getCscEnableStatus("CscFeature_Common_EnableUiDisplayMirroring");
    }

    public static boolean isMotionSupported() {
        return getFloatingEnableStatus("SEC_FLOATING_FEATURE_SETTINGS_SUPPORT_MOTION");
    }

    public static boolean isAutoPowerOnOffMenuSupported() {
        return getCscEnableStatus("CscFeature_Clock_EnableAutoPowerOnOffMenu");
    }

    public static boolean isDisableIsraelCountry() {
        return getCscEnableStatus("CscFeature_Clock_DisableIsraelCountry");
    }

    public static boolean isReplaceNameTaiwanWithTaipei() {
        return getCscEnableStatus("CscFeature_Clock_ReplaceNameTaiwanWithTaipei");
    }

    public static boolean isSupportMinimizedSip() {
        return getCscEnableStatus("CscFeature_Common_SupportMinimizedSip");
    }

    public static boolean isSupportDualSpeaker() {
        return getFloatingEnableStatus("SEC_FLOATING_FEATURE_AUDIO_SUPPORT_PSEUDO_DUAL_SPEAKER");
    }

    public static String getWeekDayColor() {
        return getCscString("CscFeature_Calendar_SetColorOfDays");
    }

    public static boolean isMusicAutoRecommendationSupported() {
        return getFloatingEnableStatus("SEC_FLOATING_FEATURE_MMFW_SUPPORT_MUSIC_AUTO_RECOMMENDATION");
    }

    public static boolean isBixbySupported() {
        return getFloatingEnableStatus("SEC_FLOATING_FEATURE_COMMON_SUPPORT_BIXBY");
    }

    public static boolean isVibetonzSupported(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (VERSION.SEM_INT >= 2801) {
            if (vibrator == null || vibrator.semGetSupportedVibrationType() < 2) {
                return false;
            }
            return true;
        } else if (vibrator == null || !vibrator.semIsHapticSupported()) {
            return false;
        } else {
            return true;
        }
    }

    public static int getWeatherDefaultUnit() {
        return SemCscFeature.getInstance().getInt("CscFeature_Weather_ConfigDefTempUnit", 1);
    }

    public static boolean isWeatherDefaultOff() {
        return "OFF".equalsIgnoreCase(getCscString("CscFeature_Clock_ConfigDefStatusWorldclockWeather"));
    }

    public static boolean isPersianCalendar() {
        return getCscEnableStatus("CscFeature_Common_SupportPersianCalendar");
    }

    public static boolean isTablet(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm != null && pm.hasSystemFeature("com.samsung.feature.device_category_tablet");
    }

    public static boolean isSupportBikeMode() {
        return getCscString("CscFeature_Common_ConfigBikeMode").contains("bikemode");
    }

    public static boolean isFolder(Context context) {
        return context.getPackageManager().hasSystemFeature("com.sec.feature.folder_type");
    }
}
