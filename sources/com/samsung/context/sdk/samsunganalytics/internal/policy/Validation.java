package com.samsung.context.sdk.samsunganalytics.internal.policy;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.UserManager;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.SamsungAnalytics;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class Validation {
    public static String SALT = "RSSAV1wsc2s314SAamk";

    private Validation() {
    }

    public static Map<String, String> checkSizeLimit(Map<String, String> map) {
        Map<String, String> returnMap = new HashMap();
        for (Entry<String, String> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.length() > 40) {
                Debug.LogENG("cd key length over:" + key);
                key = key.substring(0, 40);
            }
            if (value.length() > 1024) {
                Debug.LogENG("cd value length over:" + value);
                value = value.substring(0, 1024);
            }
            returnMap.put(key, value);
        }
        return returnMap;
    }

    public static boolean isValidConfig(final Application context, final Configuration configuration) {
        if (context == null) {
            Utils.throwException("context cannot be null");
            return false;
        } else if (configuration == null) {
            Utils.throwException("Configuration cannot be null");
            return false;
        } else if (!TextUtils.isEmpty(configuration.getDeviceId()) || configuration.isEnableAutoDeviceId()) {
            if (configuration.isEnableUseInAppLogging()) {
                if (configuration.getUserAgreement() == null) {
                    Utils.throwException("If you want to use In App Logging, you should implement UserAgreement interface");
                    return false;
                } else if (PolicyUtils.hasDMA && !hasPermission(context, "com.sec.spp.permission.TOKEN", false)) {
                    Utils.throwException("SamsungAnalytics2 need to define 'com.sec.spp.permission.TOKEN_XXXX' permission in AndroidManifest");
                    return false;
                }
            } else if (!hasPermission(context, "com.sec.spp.permission.TOKEN", false)) {
                Utils.throwException("If you want to use DLC Logger, define 'com.sec.spp.permission.TOKEN_XXXX' permission in AndroidManifest");
                return false;
            } else if (!TextUtils.isEmpty(configuration.getDeviceId())) {
                Utils.throwException("This mode is not allowed to set device Id");
                return false;
            } else if (!TextUtils.isEmpty(configuration.getUserId())) {
                Utils.throwException("This mode is not allowed to set user Id");
                return false;
            }
            if (configuration.getVersion() == null) {
                Utils.throwException("you should set the UI version");
                return false;
            }
            if (VERSION.SDK_INT >= 24) {
                UserManager um = (UserManager) context.getSystemService("user");
                if (!(um == null || um.isUserUnlocked())) {
                    Debug.LogE("The user has not unlocked the device.");
                    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                        public void onReceive(Context innerContext, Intent intent) {
                            Debug.LogD("receive " + intent.getAction());
                            SamsungAnalytics.setConfiguration(context, configuration);
                        }
                    };
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
                    intentFilter.addAction("android.intent.action.USER_UNLOCKED");
                    context.registerReceiver(broadcastReceiver, intentFilter);
                    return false;
                }
            }
            return true;
        } else {
            Utils.throwException("Device Id is empty, set Device Id or enable auto device id");
            return false;
        }
    }

    public static boolean isLoggingEnableDevice() {
        String className;
        String methodName;
        if (VERSION.SDK_INT > 23) {
            className = "com.samsung.android.feature.SemFloatingFeature";
            methodName = "getBoolean";
        } else {
            className = "com.samsung.android.feature.FloatingFeature";
            methodName = "getEnableStatus";
        }
        boolean ret = false;
        try {
            Class cls = Class.forName(className);
            Object obj = cls.getMethod("getInstance", null).invoke(null, new Object[0]);
            ret = ((Boolean) cls.getMethod(methodName, new Class[]{String.class}).invoke(obj, new Object[]{"SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE"})).booleanValue();
            if (ret) {
                Debug.LogD("cf feature is supported");
            } else {
                Debug.LogD("feature is not supported");
            }
            return ret;
        } catch (Exception e) {
            Debug.LogD("Floating feature is not supported (non-samsung device)");
            Debug.LogException(Validation.class, e);
            boolean z = ret;
            return false;
        }
    }

    public static boolean hasPermission(Context context, String permission, boolean isMatchDefinitely) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096);
            if (info.requestedPermissions != null) {
                for (String perm : info.requestedPermissions) {
                    if (isMatchDefinitely) {
                        if (perm.equalsIgnoreCase(permission)) {
                            return true;
                        }
                    } else if (perm.startsWith(permission)) {
                        return true;
                    }
                }
            }
        } catch (NameNotFoundException e) {
            Debug.LogException(Validation.class, e);
        }
        return false;
    }

    public static String sha256(String input) {
        Exception e;
        if (input == null) {
            return null;
        }
        try {
            MessageDigest.getInstance("SHA-256").update(input.getBytes("UTF-8"));
            return String.format(Locale.US, "%064x", new Object[]{new BigInteger(1, digest.digest())});
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            Debug.LogException(Validation.class, e);
            return null;
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            Debug.LogException(Validation.class, e);
            return null;
        }
    }
}
