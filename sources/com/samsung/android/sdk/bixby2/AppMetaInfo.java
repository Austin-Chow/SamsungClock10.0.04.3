package com.samsung.android.sdk.bixby2;

public class AppMetaInfo {
    int appVersionCode;
    String capsuleId;

    public AppMetaInfo(String capsuleId, int appVersionCode) {
        this.capsuleId = capsuleId;
        this.appVersionCode = appVersionCode;
    }

    public String getCapsuleId() {
        return this.capsuleId;
    }

    public void setCapsuleId(String capsuleId) {
        this.capsuleId = capsuleId;
    }

    public int getAppVersionCode() {
        return this.appVersionCode;
    }
}
