package com.samsung.context.sdk.samsunganalytics.internal.policy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.BuildConfig;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.internal.Callback;
import com.samsung.context.sdk.samsunganalytics.internal.connection.API;
import com.samsung.context.sdk.samsunganalytics.internal.device.DeviceInfo;
import com.samsung.context.sdk.samsunganalytics.internal.executor.Executor;
import com.samsung.context.sdk.samsunganalytics.internal.util.Debug;
import com.samsung.context.sdk.samsunganalytics.internal.util.Preferences;
import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;
import java.util.HashMap;
import java.util.Map;

public class PolicyUtils {
    static boolean hasDMA = false;

    public static int getRemainingQuota(Context context, int networkType) {
        int quota = 0;
        int uploaded = 0;
        SharedPreferences pref = Preferences.getPreferences(context);
        if (networkType == 1) {
            quota = pref.getInt("dq-w", 0);
            uploaded = pref.getInt("wifi_used", 0);
        } else if (networkType == 0) {
            quota = pref.getInt("dq-3g", 0);
            uploaded = pref.getInt("data_used", 0);
        }
        return quota - uploaded;
    }

    public static int hasQuota(Context context, int networkType, int size) {
        int quota = 0;
        int uploaded = 0;
        int limit = 0;
        SharedPreferences pref = Preferences.getPreferences(context);
        if (networkType == 1) {
            quota = pref.getInt("dq-w", 0);
            uploaded = pref.getInt("wifi_used", 0);
            limit = pref.getInt("oq-w", 0);
        } else if (networkType == 0) {
            quota = pref.getInt("dq-3g", 0);
            uploaded = pref.getInt("data_used", 0);
            limit = pref.getInt("oq-3g", 0);
        }
        Debug.LogENG("Quota : " + quota + "/ Uploaded : " + uploaded + "/ limit : " + limit + "/ size : " + size);
        if (quota < uploaded + size) {
            Debug.LogD("DLS Sender", "send result fail : Over daily quota");
            return -1;
        } else if (limit >= size) {
            return 0;
        } else {
            Debug.LogD("DLS Sender", "send result fail : Over once quota");
            return -11;
        }
    }

    public static boolean isPolicyExpired(Context context) {
        SharedPreferences pref = Preferences.getPreferences(context);
        if (Utils.compareDays(1, Long.valueOf(pref.getLong("quota_reset_date", 0)))) {
            resetQuota(pref);
        }
        return Utils.compareDays(pref.getInt("rint", 1), Long.valueOf(pref.getLong("policy_received_date", 0)));
    }

    public static void resetQuota(SharedPreferences pref) {
        pref.edit().putLong("quota_reset_date", System.currentTimeMillis()).putInt("data_used", 0).putInt("wifi_used", 0).apply();
    }

    public static Map<String, String> makePolicyParam(Context context, DeviceInfo deviceInfo, Configuration configuration) {
        Map<String, String> params = new HashMap();
        params.put("pkn", context.getPackageName());
        params.put("dm", deviceInfo.getDeviceModel());
        if (!TextUtils.isEmpty(deviceInfo.getMcc())) {
            params.put("mcc", deviceInfo.getMcc());
        }
        if (!TextUtils.isEmpty(deviceInfo.getMnc())) {
            params.put("mnc", deviceInfo.getMnc());
        }
        params.put("uv", configuration.getVersion());
        params.put("sv", BuildConfig.VERSION_NAME);
        return params;
    }

    public static void getPolicy(Context context, Configuration configuration, Executor executor, DeviceInfo deviceInfo, Callback callback) {
        executor.execute(makeGetPolicyClient(context, configuration, deviceInfo, callback));
    }

    public static void getPolicy(Context context, Configuration configuration, Executor executor, DeviceInfo deviceInfo) {
        executor.execute(makeGetPolicyClient(context, configuration, deviceInfo, null));
    }

    public static GetPolicyClient makeGetPolicyClient(Context context, Configuration configuration, DeviceInfo deviceInfo, Callback callback) {
        GetPolicyClient clientAPI = new GetPolicyClient(API.GET_POLICY, configuration.getTrackingId(), makePolicyParam(context, deviceInfo, configuration), Preferences.getPreferences(context), callback);
        Debug.LogENG("trid: " + configuration.getTrackingId().substring(0, 7) + ", uv: " + configuration.getVersion());
        return clientAPI;
    }

    public static void useQuota(Context context, int networkType, int size) {
        SharedPreferences pref = Preferences.getPreferences(context);
        if (networkType == 1) {
            pref.edit().putInt("wifi_used", pref.getInt("wifi_used", 0) + size).apply();
        } else if (networkType == 0) {
            pref.edit().putInt("data_used", Preferences.getPreferences(context).getInt("data_used", 0) + size).apply();
        }
    }

    public static boolean checkDMA(Context context) {
        try {
            boolean z;
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.sec.android.diagmonagent", 0);
            Debug.LogD("Validation", "dma pkg:" + packageInfo.versionCode);
            if (packageInfo.versionCode >= 540000000) {
                z = true;
            } else {
                z = false;
            }
            hasDMA = z;
        } catch (Exception e) {
            hasDMA = false;
            Debug.LogD("DMA not found" + e.getMessage());
        }
        return hasDMA;
    }

    public static String getCSC() {
        String className = "android.os.SystemProperties";
        String methodName = "get";
        String ret = null;
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke(null, new Object[]{"ro.csc.sales_code"});
        } catch (Exception e) {
            return ret;
        }
    }
}
