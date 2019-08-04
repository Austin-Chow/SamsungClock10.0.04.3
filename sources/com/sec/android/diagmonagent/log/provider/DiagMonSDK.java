package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class DiagMonSDK {
    public static String TAG = "DIAGMON_SDK";
    private static DiagMonConfig config = null;
    private static DiagMonProvider elp;
    private static DiagMonSDK instance;

    public static class DiagMonHelper {
        public static void issueReport(Context cxt, IssueBuilder isb) {
            DiagMonSDK.issueReport(cxt, isb);
        }
    }

    public static DiagMonSDK setConfiguration(DiagMonConfig configuration) {
        synchronized (DiagMonSDK.class) {
            if (configChecker(configuration)) {
                instance = new DiagMonSDK();
                config = configuration;
                elp = new DiagMonProvider();
                elp.setConfiguration(configuration);
            } else {
                Log.w(TAG, "DiagMonConfig can't be set");
            }
        }
        return instance;
    }

    protected static boolean configChecker(DiagMonConfig configuration) {
        if (configuration == null) {
            Log.w(TAG, "Configuration is null");
            return false;
        } else if (configuration.getServiceId().isEmpty()) {
            Log.w(TAG, "ServiceId is empty");
            return false;
        } else if (configuration.getAgree()) {
            return true;
        } else {
            Log.w(TAG, "Not Agreed");
            return false;
        }
    }

    private static boolean issueReport(Context cxt, IssueBuilder isb) {
        if (instance == null) {
            Log.w(TAG, "DiagMonSDK is null");
            return false;
        }
        Log.i(TAG, "DiagMonSDK is ok");
        if (config.getAgree()) {
            Log.i(TAG, "Agreement is ok - " + config.getAgree());
            if (TextUtils.isEmpty(config.getServiceId())) {
                Log.w(TAG, "serviceId is empty");
                return false;
            }
            Log.i(TAG, "serviceId is ok - " + config.getServiceId());
            if (config.getLogList().size() < 1) {
                Log.w(TAG, "No log List");
                return false;
            }
            Log.i(TAG, "logList size is ok - " + config.getLogList().size());
            if (TextUtils.isEmpty(isb.getResultCode())) {
                Log.w(TAG, "ResultCode is empty");
                return false;
            }
            Log.i(TAG, "ResultCode is ok - " + isb.getResultCode());
            cxt.sendBroadcast(DiagMonUtil.makeBundle(cxt, config, isb));
            Log.i(TAG, "SendBroadcast");
            return true;
        }
        Log.w(TAG, "not agreed");
        return false;
    }

    public static String getSDKVer() {
        return "1.1";
    }
}
