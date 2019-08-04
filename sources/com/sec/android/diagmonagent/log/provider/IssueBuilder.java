package com.sec.android.diagmonagent.log.provider;

public class IssueBuilder {
    private String mDescription = "";
    private String mEventId = "";
    private String mRelayClient = "";
    private String mRelayVer = "";
    private String mResultCode = "";
    private boolean mUiMode = true;
    private boolean mWifiOnly = true;

    public IssueBuilder setResultCode(String resultCode) {
        this.mResultCode = resultCode;
        return this;
    }

    public String getResultCode() {
        return this.mResultCode;
    }

    public IssueBuilder setUiMode(boolean mode) {
        this.mUiMode = mode;
        return this;
    }

    public boolean getUiMode() {
        return this.mUiMode;
    }

    public IssueBuilder setWifiOnly(boolean wifiOnly) {
        this.mWifiOnly = wifiOnly;
        return this;
    }

    public boolean getWifiOnly() {
        return this.mWifiOnly;
    }

    public String getEventId() {
        return this.mEventId;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getRelayClient() {
        return this.mRelayClient;
    }

    public String getRelayClientVer() {
        return this.mRelayVer;
    }
}
