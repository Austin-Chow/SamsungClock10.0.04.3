package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.samsung.context.sdk.samsunganalytics.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagMonUtil {
    public static String getPackageVersion(Context context) {
        try {
            String versionName = "";
            PackageManager packMgr = context.getPackageManager();
            if (packMgr != null) {
                return packMgr.getPackageInfo(context.getPackageName(), 0).versionName;
            }
        } catch (NameNotFoundException e) {
            Log.i("DIAGMON_AGENT", context.getPackageName() + " is not found");
        }
        return "";
    }

    public static int getUid(Context cxt) {
        return cxt.getApplicationInfo().uid;
    }

    public static Intent makeBundle(Context cxt, DiagMonConfig config, IssueBuilder isb) {
        Intent i;
        JSONObject obj = new JSONObject();
        if (getUid(cxt) == 1000) {
            i = new Intent("com.sec.android.diagmonagent.intent.REPORT_ERROR_V2");
        } else {
            i = new Intent("com.sec.android.diagmonagent.intent.REPORT_ERROR_APP");
        }
        Bundle uploadMO = new Bundle();
        i.addFlags(32);
        uploadMO.putBundle("DiagMon", new Bundle());
        uploadMO.getBundle("DiagMon").putBundle("CFailLogUpload", new Bundle());
        uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").putString("ServiceID", config.getServiceId());
        uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").putBundle("Ext", new Bundle());
        uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("ClientV", getPackageVersion(cxt));
        if (!TextUtils.isEmpty(isb.getRelayClient())) {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("RelayClient", isb.getRelayClient());
        }
        if (!TextUtils.isEmpty(isb.getRelayClientVer())) {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("RelayClientV", isb.getRelayClientVer());
        }
        if (isb.getUiMode()) {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("UiMode", "1");
        } else {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("UiMode", "0");
        }
        uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("ResultCode", isb.getResultCode());
        if (!TextUtils.isEmpty(isb.getEventId())) {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("EventID", isb.getEventId());
        }
        try {
            obj.put("SasdkV", BuildConfig.VERSION_NAME);
            obj.put("SdkV", DiagMonSDK.getSDKVer());
            obj.put("TrackingID", config.getTrackingId());
            obj.put("Description", isb.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("Description", obj.toString());
        if (isb.getWifiOnly()) {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("WifiOnlyFeature", "1");
        } else {
            uploadMO.getBundle("DiagMon").getBundle("CFailLogUpload").getBundle("Ext").putString("WifiOnlyFeature", "0");
        }
        i.putExtra("uploadMO", uploadMO);
        i.setFlags(32);
        return i;
    }
}
