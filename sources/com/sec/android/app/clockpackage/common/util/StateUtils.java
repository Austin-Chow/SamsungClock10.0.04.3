package com.sec.android.app.clockpackage.common.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.os.SemSystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import com.samsung.android.app.SemMultiWindowManager;
import com.samsung.android.emergencymode.SemEmergencyManager;
import com.samsung.android.game.SemGameManager;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.Scover;
import com.samsung.android.sdk.cover.ScoverManager;
import com.sec.android.app.clockpackage.common.feature.Feature;
import java.lang.reflect.Field;
import java.util.Locale;

public class StateUtils {
    private static final float[] DENSITY_BASE_PIXEL = new float[]{3.5f, 4.0f, 4.5f};
    private static final float[] DENSITY_BASE_PIXEL_TABLET = new float[]{1.75f, 2.0f, 2.25f, 2.625f, 3.0f};
    private static boolean sIsClockPackageInDexMode = false;

    public static boolean isInVoipCall(Context context) {
        boolean isInVoIPCall;
        Exception e;
        boolean z;
        try {
            AudioManager mAudioManager = (AudioManager) context.getSystemService("audio");
            Log.secD("StateUtils", "mAudioManager.isVoiceCallActive = " + mAudioManager.semIsVoiceCallActive());
            Log.secD("StateUtils", "mAudioManager.isRecordActive = " + mAudioManager.semIsRecordActive(-1));
            if (!(mAudioManager.semIsVoiceCallActive() && mAudioManager.semIsRecordActive(-1)) && (mAudioManager.semIsVoiceCallActive() || isInCall(context) || !(mAudioManager.getMode() == 2 || mAudioManager.getMode() == 3))) {
                isInVoIPCall = false;
            } else {
                isInVoIPCall = true;
            }
            try {
            } catch (Exception e2) {
                e = e2;
                z = isInVoIPCall;
                Log.secE("StateUtils", "Exception Exception e = " + e.toString());
                return false;
            }
            try {
                Log.secD("StateUtils", "isInVoipCall = " + isInVoIPCall);
                int mode = mAudioManager.getMode();
                String log = "mAudioManager.getMode() = " + mode;
                switch (mode) {
                    case 0:
                        log = log + ":MODE_NORMAL";
                        break;
                    case 1:
                        log = log + ":MODE_RINGTONE";
                        break;
                    case 2:
                        log = log + ":MODE_IN_CALL";
                        break;
                    case 3:
                        log = log + ":MODE_IN_COMMUNICATION";
                        break;
                }
                Log.secD("StateUtils", log);
                z = isInVoIPCall;
                return isInVoIPCall;
            } catch (Exception e3) {
                e = e3;
                z = isInVoIPCall;
                Log.secE("StateUtils", "Exception Exception e = " + e.toString());
                return false;
            }
        } catch (Exception e4) {
            e = e4;
            Log.secE("StateUtils", "Exception Exception e = " + e.toString());
            return false;
        }
    }

    public static boolean isInCallState(Context context) {
        return isInVoipCall(context) || isInCall(context);
    }

    public static boolean isDualSlot(Context context) {
        boolean z = true;
        if (context == null) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null) {
            return false;
        }
        if (telephonyManager.semGetSimState(1) == 0) {
            z = false;
        }
        return z;
    }

    public static boolean isInCall(Context context) {
        try {
            if (!isDualSlot(context) || Feature.isLiveDemoBinary()) {
                int callState = ((TelephonyManager) context.getSystemService("phone")).getCallState();
                Log.secD("StateUtils", "isInCallState callState = " + callState);
                return callState != 0;
            }
            return (getMultiSimCallState(context, 0) == 0 && getMultiSimCallState(context, 1) == 0) ? false : true;
        } catch (Exception e) {
            Log.secE("StateUtils", "isInCall Exception");
            return false;
        }
    }

    public static int getMultiSimCallState(Context context, int slotIndex) {
        if (context == null) {
            return 0;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null) {
            return 0;
        }
        int callState = telephonyManager.createForSubscriptionId(slotIndex).semGetCallState(slotIndex);
        Log.secD("StateUtils", "semGetCallState : " + callState + " slotIndex : " + slotIndex);
        return callState;
    }

    public static boolean isUltraPowerSavingMode(Context context) {
        return SemEmergencyManager.isEmergencyMode(context) && SemEmergencyManager.getInstance(context).checkModeType(512);
    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService("power")).isInteractive();
    }

    public static boolean isRtl() {
        if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) != 1) {
            return false;
        }
        Log.secD("StateUtils", "isRtl is true!");
        return true;
    }

    public static boolean isFlashNotificationEnabled(Context context) {
        boolean isEnableNotiFlash = true;
        boolean isEnabled = false;
        if (System.semGetIntForUser(context.getContentResolver(), "flash_notification", 0, -2) != 1) {
            isEnableNotiFlash = false;
        }
        if (isEnableNotiFlash) {
            isEnabled = true;
        }
        if (Feature.DEBUG_ENG) {
            Log.secV("StateUtils", "isEnableNotiFlash : " + isEnableNotiFlash + " isEnabled : " + isEnabled);
        }
        return isEnabled;
    }

    public static boolean isScreenNotificationEnabled(Context context) {
        boolean isEnableNotiScreen = true;
        boolean isEnabled = false;
        if (System.semGetIntForUser(context.getContentResolver(), "screen_notification", 0, -2) != 1) {
            isEnableNotiScreen = false;
        }
        if (isEnableNotiScreen) {
            isEnabled = true;
        }
        if (Feature.DEBUG_ENG) {
            Log.secV("StateUtils", "isEnableNotiScreen : " + isEnableNotiScreen + " isEnabled : " + isEnabled);
        }
        return isEnabled;
    }

    public static boolean isLightNotificationEnabled(Context context) {
        boolean isEnabled = false;
        if (isScreenNotificationEnabled(context) || isFlashNotificationEnabled(context)) {
            isEnabled = true;
        }
        if (Feature.DEBUG_ENG) {
            Log.secV("StateUtils", "isLightNotification Enabled : " + isEnabled);
        }
        return isEnabled;
    }

    public static boolean isDndModeAlarmMuted(Context context) {
        boolean isAlarmMuted = true;
        String zenMode = "zen_mode";
        int flagZenMode = Global.getInt(context.getContentResolver(), "zen_mode", 0);
        if (VERSION.SDK_INT >= 28) {
            if (flagZenMode == 0 || !isDisallowAlarms(context)) {
                isAlarmMuted = false;
            }
        } else if (flagZenMode != 2) {
            isAlarmMuted = false;
        }
        Log.secD("StateUtils", "isDndModeAlarmMuted : isAlarmMuted = " + isAlarmMuted + " , flagZenMode = " + flagZenMode);
        return isAlarmMuted;
    }

    @SuppressLint({"InlinedApi"})
    private static boolean isDisallowAlarms(Context context) {
        boolean disallowAlarms = (((NotificationManager) context.getSystemService("notification")).getNotificationPolicy().priorityCategories & 32) == 0;
        Log.secD("StateUtils", "disallowAlarms = " + disallowAlarms);
        return disallowAlarms;
    }

    public static boolean isDriveLinkRunning(Context context) {
        try {
            if (Secure.getInt(context.getContentResolver(), "car_mode_on") == 0) {
                return false;
            }
            Log.m41d("StateUtils", "isDriveLinkRunning true");
            return true;
        } catch (SettingNotFoundException e) {
            Log.secE("StateUtils", "SettingNotFoundException e = " + e);
            return false;
        }
    }

    public static boolean isMirrorLinkRunning() {
        if (!SemSystemProperties.get("net.mirrorlink.on").equals("1")) {
            return false;
        }
        Log.m41d("StateUtils", "isMirrorLinkRunning true");
        return true;
    }

    private static boolean isKidsModeOn(Context context) {
        ComponentName compKidsHome = new ComponentName("com.sec.android.app.kidshome", "com.sec.android.app.kidshome.apps.ui.AppsActivity");
        PackageManager pm = context.getPackageManager();
        Intent homeIntent = new Intent("android.intent.action.MAIN");
        homeIntent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveInfo = pm.resolveActivity(homeIntent, 65536);
        boolean activated = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name).equals(compKidsHome);
        if (activated) {
            Log.m41d("StateUtils", "Kids mode is on");
        }
        return activated;
    }

    public static boolean isLeftAmPm() {
        return Locale.getDefault().getLanguage().equals("ko") || Locale.getDefault().getLanguage().equals("ja") || Locale.getDefault().getLanguage().equals("iw") || Locale.getDefault().getLanguage().equals("ur") || Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("my");
    }

    public static boolean isKeyguardLocked(Context context) {
        KeyguardManager localKeyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        if (localKeyguardManager == null || !localKeyguardManager.isKeyguardLocked()) {
            return false;
        }
        Log.secD("StateUtils", "isKeyguardLocked");
        return true;
    }

    public static boolean needToShowAsFullScreen(Context context) {
        if (!isScreenOn(context)) {
            Log.m41d("StateUtils", "needToShowAsFullScreen true !pm.isScreenOn");
            return true;
        } else if (isVideoCall(context)) {
            Log.m41d("StateUtils", "needToShowAsFullScreen false isVideoCall is true -> HUN");
            return false;
        } else if (isKeyguardLocked(context)) {
            Log.m41d("StateUtils", "needToShowAsFullScreen true isKeyguardLocked");
            return true;
        } else if (isMum(context) && !isAfwForByod(context)) {
            Log.m41d("StateUtils", "needToShowAsFullScreen true isMum");
            return true;
        } else if (Feature.isTablet(context) || !isCoverClosed(context)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMirroringEnabled() {
        if (!Feature.isMirroringSupported()) {
            return false;
        }
        String curLanguage = Locale.getDefault().getLanguage();
        if (curLanguage.equalsIgnoreCase("ar") || curLanguage.equalsIgnoreCase("iw") || curLanguage.equalsIgnoreCase("he")) {
            return true;
        }
        return false;
    }

    public static boolean isMobileKeyboard(Context context) {
        if (!Feature.isMobileKeyboardSupported() || context == null || context.getResources() == null) {
            return false;
        }
        Configuration conf = context.getResources().getConfiguration();
        try {
            Field getConfiguration = Configuration.class.getField("semMobileKeyboardCovered");
            if (getConfiguration == null || getConfiguration.getInt(conf) != 1) {
                return false;
            }
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        } catch (IllegalAccessException e2) {
            return false;
        }
    }

    public static boolean isInLockTaskMode(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        if (am == null || am.getLockTaskModeState() == 0) {
            return false;
        }
        Log.secD("StateUtils", "isInLockTaskMode(pin window) is true");
        return true;
    }

    public static boolean isInBikeMode(Context context) {
        return Secure.getInt(context.getContentResolver(), "isBikeMode", 0) != 0;
    }

    public static boolean isTalkBackEnabled(Context context) {
        if (context == null) {
            return false;
        }
        boolean talkBackEnabled = false;
        String accessibilityService = Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (accessibilityService != null) {
            if (accessibilityService.matches("(?i).*com.samsung.android.app.talkback.TalkBackService.*") || accessibilityService.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*")) {
                talkBackEnabled = true;
            } else {
                talkBackEnabled = false;
            }
        }
        Log.secD("StateUtils", "isTalkBackEnabled talkBackEnabled = " + talkBackEnabled);
        return talkBackEnabled;
    }

    public static boolean isTurnOffAllSoundMode(Context context) {
        return System.semGetIntForUser(context.getContentResolver(), "all_sound_off", 0, -2) == 1;
    }

    public static boolean isCoverClosed(Context context) {
        Boolean mDeviceSupportCoverSDK = Boolean.valueOf(true);
        try {
            new Scover().initialize(context.getApplicationContext());
            Log.secI("StateUtils", "isCoverClosed - initialize");
        } catch (IllegalArgumentException e) {
            mDeviceSupportCoverSDK = Boolean.valueOf(false);
            Log.secE("StateUtils", "isCoverClosed - IllegalArgumentException");
        } catch (SsdkUnsupportedException e2) {
            mDeviceSupportCoverSDK = Boolean.valueOf(false);
            Log.secE("StateUtils", "isCoverClosed - SsdkUnsupportedException : This device is not supported Scover!!!");
        }
        if (mDeviceSupportCoverSDK.booleanValue()) {
            ScoverManager mCoverManager = new ScoverManager(context);
            if (mCoverManager.getCoverState() == null) {
                Log.m41d("StateUtils", "isCoverClosed true - mCoverManager.getCoverState() == null");
                return true;
            } else if (!mCoverManager.getCoverState().getSwitchState()) {
                Log.m41d("StateUtils", "isCoverClosed true - !mCoverManager.getCoverState().getSwitchState()");
                return true;
            }
        }
        return false;
    }

    public static boolean isAfwForByod(Context context) {
        boolean bAfwForByod = ((UserManager) context.getSystemService("user")).semIsManagedProfile();
        Log.m41d("StateUtils", "isAfwForByod = " + bAfwForByod);
        return bAfwForByod;
    }

    private static boolean isMum(Context context) {
        if (context == null) {
            Log.m41d("StateUtils", "isMum false if (context == null)");
            return false;
        }
        int creatorUid = UserHandle.semGetCallingUserId();
        int currentUid = 0;
        try {
            currentUid = ActivityManager.semGetCurrentUser();
            if (creatorUid != currentUid) {
                Log.m41d("StateUtils", "isMum true creatorUid : " + creatorUid + " currentUid : " + currentUid);
            }
        } catch (Exception e) {
            Log.secE("StateUtils", "exception : " + e.toString());
        }
        if (creatorUid != currentUid) {
            return true;
        }
        return false;
    }

    public static boolean isCustomTheme(Context mContext) {
        if (isContextInDexMode(mContext)) {
            Log.secD("StateUtils", "knoxDesktopMode is not support Theme.");
            return false;
        }
        String packageName = null;
        try {
            packageName = System.getString(mContext.getContentResolver(), "current_sec_active_themepackage");
        } catch (Exception e) {
            Log.secE("StateUtils", "isCustomTheme EXCEPTION !!");
        }
        if (packageName == null || packageName.isEmpty() || packageName.equalsIgnoreCase("null") || packageName.equals("com.samsung.festival.chinadefault")) {
            return false;
        }
        Log.secD("StateUtils", "isCustomTheme is True. Theme has been installed");
        Log.secD("StateUtils", "isCustomTheme packageName = " + packageName);
        return true;
    }

    public static boolean checkHapticFeedbackEnabled(Context context) {
        Vibrator mVibrator = (Vibrator) context.getSystemService("vibrator");
        if (System.getInt(context.getContentResolver(), "haptic_feedback_enabled", 0) == 1 && isHapticSupported(mVibrator)) {
            return true;
        }
        return false;
    }

    private static boolean isHapticSupported(Vibrator vibrator) {
        if (VERSION.SEM_INT >= 2801) {
            return vibrator.semGetSupportedVibrationType() >= 2;
        } else {
            return vibrator.semIsHapticSupported();
        }
    }

    public static boolean isContextInDexMode(Context context) {
        if (context.getResources().getConfiguration().semDesktopModeEnabled == 1) {
            return true;
        }
        return false;
    }

    public static boolean isVoiceNoteRecording(Context context) {
        if (!"com.sec.android.app.voicenote".equals(getCurrentAudioFocusPackageName(context))) {
            return false;
        }
        Log.m41d("StateUtils", "isVoiceNoteRecording  bVoiceNoteRecording = true");
        return true;
    }

    public static boolean isRecordingState(Context context) {
        boolean bRecordingMode = false;
        if (isRecordActive(context)) {
            boolean bRecordActiveForCamcorder = isRecordingCamcorder(context);
            String audioFocusPackageName = "";
            if (bRecordActiveForCamcorder) {
                bRecordingMode = true;
            } else {
                audioFocusPackageName = getCurrentAudioFocusPackageName(context);
                if (audioFocusPackageName != null) {
                    String PKG_NAME_MEMO = "com.samsung.android.app.memo";
                    String PKG_NAME_SAMSUNG_NOTES = "com.samsung.android.app.notes";
                    String PKG_NAME_SNOTE = "com.samsung.android.snote";
                    String PKG_NAME_FMRADIO = "com.sec.android.app.fm";
                    String PKG_NAME_MMS = "com.samsung.android.messaging";
                    if ("com.sec.android.app.voicenote".equals(audioFocusPackageName) || "com.samsung.android.app.memo".equals(audioFocusPackageName) || "com.samsung.android.app.notes".equals(audioFocusPackageName) || "com.samsung.android.snote".equals(audioFocusPackageName) || "com.sec.android.app.fm".equals(audioFocusPackageName) || "com.samsung.android.messaging".equals(audioFocusPackageName)) {
                        bRecordingMode = true;
                    }
                }
            }
            if (bRecordingMode) {
                Log.secD("StateUtils", "isRecordingState bRecordActiveForCamcorder = " + bRecordActiveForCamcorder + " audioFocusPackageName = " + audioFocusPackageName);
            }
        }
        return bRecordingMode;
    }

    private static boolean isRecordActive(Context context) {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService("audio");
        if (audioManager != null) {
            return audioManager.semIsRecordActive(-1);
        }
        return false;
    }

    public static boolean isRecordingCamcorder(Context context) {
        boolean bRecordActiveForCamcorder = false;
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService("audio");
        if (audioManager != null) {
            bRecordActiveForCamcorder = audioManager.semIsRecordActive(5) || audioManager.semIsRecordActive(MediaRecorder.semGetInputSource(8)) || SemSystemProperties.get("service.camera.rec.running").equals("1");
        }
        Log.secD("StateUtils", "bRecordActiveForCamcorder = " + bRecordActiveForCamcorder);
        return bRecordActiveForCamcorder;
    }

    public static String getCurrentAudioFocusPackageName(Context context) {
        String packageName = ((AudioManager) context.getApplicationContext().getSystemService("audio")).semGetAudioFocusedPackageName();
        Log.m41d("StateUtils", "semGetAudioFocusedPackageName = " + packageName);
        return packageName;
    }

    public static boolean isUniversalSwitchEnabled(Context context) {
        boolean universalSwitchEnabled = false;
        String accesibilityService = Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (accesibilityService != null) {
            universalSwitchEnabled = accesibilityService.matches("(?i).*com.samsung.accessibility.universalswitch.UniversalSwitchService.*");
        }
        Log.secD("StateUtils", "isUniversalSwitch On : " + universalSwitchEnabled);
        return universalSwitchEnabled;
    }

    public static boolean isSwitchAccessEnabled(Context context) {
        boolean switchAccessEnabled = false;
        String accesibilityService = Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (accesibilityService != null) {
            switchAccessEnabled = accesibilityService.matches("(?i).*com.android.switchaccess.SwitchAccessService.*");
        }
        Log.secD("StateUtils", "isSwitchAccessService On : " + switchAccessEnabled);
        return switchAccessEnabled;
    }

    public static boolean isDirectBootMode(Context context) {
        boolean enabled = false;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (devicePolicyManager != null) {
            try {
                enabled = devicePolicyManager.getStorageEncryptionStatus() == 5;
                Log.secD("StateUtils", "isDirectBootMode On : " + enabled);
            } catch (IllegalStateException e) {
                Log.m42e("StateUtils", "IllegalStateException e = " + e);
            }
        }
        return enabled;
    }

    public static boolean isEasyMode(Context context) {
        boolean isEasyMode = false;
        try {
            isEasyMode = System.getInt(context.getContentResolver(), "easy_mode_switch", 1) == 0;
        } catch (Exception e) {
            Log.secE("StateUtils", "SettingNotFoundException e = " + e);
        }
        if (isEasyMode) {
            Log.m41d("StateUtils", "isAirPlaneMode bAirPlaneModeOn = true");
        }
        return isEasyMode;
    }

    public static boolean isUserUnlockedDevice(Context context) {
        boolean unlocked = true;
        UserManager um = (UserManager) context.getSystemService("user");
        if (um != null) {
            unlocked = um.isUserUnlocked();
        }
        Log.secD("StateUtils", "isUserUnlockedDevice : " + unlocked);
        return unlocked;
    }

    public static Context getAlarmDBContext(Context context) {
        return isDirectBootMode(context) ? context.createDeviceProtectedStorageContext() : context;
    }

    private static int getCurrentResolution(Context context) {
        int currentResolution;
        String sizeStr = Global.getString(context.getContentResolver(), "display_size_forced");
        int width = 0;
        if (!(sizeStr == null || "".equals(sizeStr))) {
            String[] sizeArray = sizeStr.split(",");
            width = (sizeArray == null || sizeArray.length <= 1) ? 1440 : Integer.parseInt(sizeArray[0]);
        }
        if (width >= 1440) {
            currentResolution = 2;
        } else if (width <= LiveIconLoader.DENSITY_720 || width > 1080) {
            currentResolution = 0;
        } else {
            currentResolution = 1;
        }
        Log.secD("StateUtils", "getCurrentResolution: width = " + width + "currentResolution = " + currentResolution);
        return currentResolution;
    }

    private static int[] getProperDensities(Context context, float[] densities) {
        int[] converted_density = new int[densities.length];
        int resolution = getCurrentResolution(context);
        float[] convert_ratio = new float[]{0.5f, 0.75f, 1.0f};
        for (int i = 0; i < densities.length; i++) {
            converted_density[i] = (int) ((densities[i] * 160.0f) * convert_ratio[resolution]);
        }
        return converted_density;
    }

    private static int[] getProperTabletDensities(float[] densities) {
        int[] converted_density = new int[densities.length];
        for (int i = 0; i < converted_density.length; i++) {
            converted_density[i] = (int) (160.0f * densities[i]);
        }
        return converted_density;
    }

    public static int getScreenDensityIndex(Context context) {
        if (context == null) {
            return -1;
        }
        int[] densities;
        if (Feature.isTablet(context)) {
            densities = getProperTabletDensities(DENSITY_BASE_PIXEL_TABLET);
        } else {
            densities = getProperDensities(context, DENSITY_BASE_PIXEL);
        }
        int currentDensity = Secure.getInt(context.getContentResolver(), "display_density_forced", -1);
        for (int i = 0; i < densities.length; i++) {
            if (currentDensity == densities[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isScreenDp(Context context, int screenDp) {
        return ((int) (((double) context.getResources().getConfiguration().smallestScreenWidthDp) * getDensity(context))) >= screenDp;
    }

    public static boolean isMultiWindowMinSize(Context context, int minSizeDp, boolean isWidth) {
        Configuration configuration = context.getResources().getConfiguration();
        return ((int) (((double) (isWidth ? configuration.screenWidthDp : configuration.screenHeightDp)) * getDensity(context))) <= minSizeDp;
    }

    private static double getDensity(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display display = windowManager == null ? null : windowManager.getDefaultDisplay();
        if (display != null) {
            display.getRealMetrics(metrics);
        }
        return display == null ? 1.0d : ((double) configuration.densityDpi) / ((double) metrics.densityDpi);
    }

    public static boolean isVideoCall(Context context) {
        if (context == null) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return telephonyManager != null && telephonyManager.semIsVideoCall();
    }

    private static boolean isAirPlaneMode(Context context) {
        boolean bAirPlaneModeOn = false;
        try {
            bAirPlaneModeOn = Global.getInt(context.getContentResolver(), "airplane_mode_on") == 1;
        } catch (SettingNotFoundException e) {
            Log.secE("StateUtils", "SettingNotFoundException e = " + e);
        }
        if (bAirPlaneModeOn) {
            Log.m41d("StateUtils", "isAirPlaneMode bAirPlaneModeOn = true");
        }
        return bAirPlaneModeOn;
    }

    public static boolean isPossibleStateForBixbyBriefing(Context context) {
        boolean bSupportBixbyBriefingMenu = Feature.isSupportBixbyBriefingMenu(context);
        boolean bUltraPowerSavingMode = isUltraPowerSavingMode(context);
        boolean bKnoxDesktopMode = isContextInDexMode(context);
        if (!bSupportBixbyBriefingMenu || isAirPlaneMode(context) || bUltraPowerSavingMode || bKnoxDesktopMode || isKidsModeOn(context)) {
            String logString = "";
            if (!bSupportBixbyBriefingMenu) {
                logString = logString + " bSupportBixbyBriefingMenu = false";
            }
            if (bUltraPowerSavingMode) {
                logString = logString + " bUltraPowerSavingMode = true";
            }
            if (bKnoxDesktopMode) {
                logString = logString + " bKnoxDesktopMode = true";
            }
            Log.m41d("StateUtils", "isPossibleStateForBixbyBriefing = false," + logString);
            return false;
        }
        Log.m41d("StateUtils", "isPossibleStateForBixbyBriefing bPossibleBixbyBriefing = true");
        return true;
    }

    public static boolean isSoundModeOnForJapan(Context context) {
        boolean bSoundModeOn = false;
        if (Feature.isSupportAlarmSoundMenu()) {
            if (getAlarmDBContext(context).getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getBoolean("sound_mode_jpn", true) && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sound_mode_jpn", true)) {
                bSoundModeOn = true;
            } else {
                bSoundModeOn = false;
            }
            Log.secD("StateUtils", "isSoundModeOnForJapan : " + bSoundModeOn);
        }
        return bSoundModeOn;
    }

    public static boolean isMannerModeState(Context context) {
        boolean bVibrateForAlarms = false;
        if (isSoundModeOnForJapan(context) && isMuteOrVibrateForSystemSoundMode(context)) {
            bVibrateForAlarms = true;
        }
        Log.secD("StateUtils", "isMannerModeState bVibrateForAlarms = " + bVibrateForAlarms);
        return bVibrateForAlarms;
    }

    public static boolean isMuteOrVibrateForSystemSoundMode(Context context) {
        boolean bMuteOrVibrate = false;
        AudioManager mAudioManager = (AudioManager) context.getSystemService("audio");
        if (mAudioManager != null && (mAudioManager.getRingerMode() == 0 || mAudioManager.getRingerMode() == 1)) {
            bMuteOrVibrate = true;
        }
        Log.secD("StateUtils", "isMuteOrVibrateForSystemSoundMode bMuteOrVibrate = " + bMuteOrVibrate);
        return bMuteOrVibrate;
    }

    public static boolean isUseArabianNumberInRtl() {
        return "iw".equals(Locale.getDefault().getLanguage()) || "ur".equals(Locale.getDefault().getLanguage()) || "tr".equals(Locale.getDefault().getLanguage());
    }

    public static boolean isNetWorkConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void setClockPackageInDexMode(boolean isClockPackageInDexMode) {
        sIsClockPackageInDexMode = isClockPackageInDexMode;
    }

    public static boolean isClockPackageInDexMode() {
        return sIsClockPackageInDexMode;
    }

    public static boolean isCanNotExpandAboutExtendedAppBar(Context context) {
        return context.getResources().getConfiguration().orientation == 2 || ((isInMultiwindow() && context.getResources().getConfiguration().smallestScreenWidthDp < 480) || isContextInDexMode(context) || isEasyMode(context));
    }

    public static boolean isInMultiwindow() {
        return new SemMultiWindowManager().getMode() != 0;
    }

    public static boolean isSplitMode() {
        return new SemMultiWindowManager().getMode() == 2;
    }

    public static boolean isVisibleNaviBar(Context context) {
        boolean isVisibleNavibar = true;
        if (Feature.isSupportForceImmersive()) {
            try {
                isVisibleNavibar = Global.getInt(context.getContentResolver(), "navigationbar_hide_bar_enabled") == 0;
                Log.secD("StateUtils", "isVisibleNaviBar : " + isVisibleNavibar);
            } catch (SettingNotFoundException e) {
                Log.secE("StateUtils", "SettingNotFoundException");
            }
        }
        return isVisibleNavibar;
    }

    public static boolean isRemoteAction(KeyEvent event) {
        return event.getKeyCode() == 42 && event.isCtrlPressed() && event.isShiftPressed();
    }

    public static boolean isShowCustomizeService(Context context) {
        boolean isRubinAvailable = false;
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.samsung.android.rubin.app", 0);
            int enabledState = pm.getApplicationEnabledSetting("com.samsung.android.rubin.app");
            if (enabledState == 1 || enabledState == 0) {
                isRubinAvailable = true;
            } else {
                isRubinAvailable = false;
            }
        } catch (NameNotFoundException e) {
            Log.m41d("StateUtils", "isAppEnabled NameNotFound : com.samsung.android.rubin.app");
        }
        Log.m41d("StateUtils", "isShowCustomizeService rubin is enable = " + isRubinAvailable);
        return isRubinAvailable;
    }

    public static boolean isGameModeOn() {
        boolean bIsGameMode = false;
        try {
            bIsGameMode = new SemGameManager().isForegroundGame();
        } catch (Exception e) {
            Log.m41d("StateUtils", "Exception occurs accessing SemGameManager");
        }
        Log.m41d("StateUtils", "isGameModeOn = " + bIsGameMode);
        return bIsGameMode;
    }

    public static boolean isHighTextContrastEnabled(Context context) {
        return Secure.getInt(context.getContentResolver(), "high_text_contrast_enabled", 0) == 1;
    }
}
