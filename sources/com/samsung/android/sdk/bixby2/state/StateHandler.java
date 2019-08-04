package com.samsung.android.sdk.bixby2.state;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import com.samsung.android.sdk.bixby2.AppMetaInfo;
import com.samsung.android.sdk.bixby2.LogUtil;
import com.samsung.android.sdk.bixby2.Sbixby;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

public class StateHandler {
    private static final String TAG = StateHandler.class.getSimpleName();
    private static StateHandler mInstance;
    private Callback mCallback = null;

    public static abstract class Callback {
        public abstract String onAppStateRequested();

        public String onCapsuleIdRequested() {
            return null;
        }
    }

    private StateHandler() {
    }

    public static synchronized StateHandler getInstance() {
        StateHandler stateHandler;
        synchronized (StateHandler.class) {
            if (mInstance == null) {
                mInstance = new StateHandler();
            }
            stateHandler = mInstance;
        }
        return stateHandler;
    }

    private AppMetaInfo getDefaultAppMetaInfo(Context context) {
        Exception ee;
        PackageManager pkgManager = context.getPackageManager();
        try {
            String pkgName = context.getPackageName();
            Bundle appData = pkgManager.getApplicationInfo(pkgName, 128).metaData;
            if (appData != null && appData.containsKey("com.samsung.android.bixby.capsuleId")) {
                return new AppMetaInfo(appData.getString("com.samsung.android.bixby.capsuleId"), pkgManager.getPackageInfo(pkgName, 0).versionCode);
            }
            LogUtil.m8e(TAG, "Can't get Capsule ID from Meta data:" + pkgName);
            return null;
        } catch (NameNotFoundException e) {
            ee = e;
            LogUtil.m8e(TAG, "Failed to get Meta data info: " + ee.getMessage());
            return null;
        } catch (NullPointerException e2) {
            ee = e2;
            LogUtil.m8e(TAG, "Failed to get Meta data info: " + ee.getMessage());
            return null;
        }
    }

    public String getAppState(Context context) {
        if (this.mCallback == null) {
            LogUtil.m8e(TAG, "StateHandler.Callback instance is null");
            return null;
        }
        String stateInfo = this.mCallback.onAppStateRequested();
        if (TextUtils.isEmpty(stateInfo)) {
            LogUtil.m7d(TAG, "state info is empty.");
            return null;
        }
        AppMetaInfo appMetaInfo;
        String capsuleId = this.mCallback.onCapsuleIdRequested();
        Map<String, AppMetaInfo> metaInfoMap = Sbixby.getInstance().getAppMetaInfoMap();
        if (TextUtils.isEmpty(capsuleId)) {
            LogUtil.m7d(TAG, "capsuleId is empty");
            if (metaInfoMap == null || (metaInfoMap != null && metaInfoMap.size() == 0)) {
                appMetaInfo = getDefaultAppMetaInfo(context);
            } else if (metaInfoMap.size() == 1) {
                LogUtil.m7d(TAG, "Map for App Meta Info. has only one");
                appMetaInfo = (AppMetaInfo) ((Entry) metaInfoMap.entrySet().iterator().next()).getValue();
            } else {
                LogUtil.m8e(TAG, "No Capsule Id and multiple App Meta Info. Can't pick one");
                return null;
            }
        } else if (metaInfoMap == null || !metaInfoMap.containsKey(capsuleId)) {
            LogUtil.m7d(TAG, "Map for App Meta Info. is empty");
            appMetaInfo = getDefaultAppMetaInfo(context);
            if (appMetaInfo != null) {
                appMetaInfo.setCapsuleId(capsuleId);
            }
        } else {
            appMetaInfo = (AppMetaInfo) metaInfoMap.get(capsuleId);
        }
        if (appMetaInfo == null) {
            LogUtil.m8e(TAG, "App Meta Info. is null");
            return null;
        }
        try {
            JSONObject stateJson = new JSONObject(stateInfo);
            stateJson.put("capsuleId", appMetaInfo.getCapsuleId());
            stateJson.put("appId", context.getPackageName());
            stateJson.put("appVersionCode", appMetaInfo.getAppVersionCode());
            LogUtil.m7d(TAG, "state info: " + stateJson.toString());
            return stateJson.toString();
        } catch (JSONException ee) {
            LogUtil.m8e(TAG, ee.getMessage());
            return null;
        }
    }
}
