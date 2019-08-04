package com.samsung.context.sdk.samsunganalytics;

public class Configuration {
    private int auidType = -1;
    private DBOpenHelper dbOpenHelper;
    private String deviceId;
    private boolean enableAutoDeviceId = false;
    private boolean enableFastReady = false;
    private boolean enableUseInAppLogging = false;
    private boolean isAlwaysRunningApp = false;
    private int networkTimeoutInMilliSeconds = 0;
    private String overrideIp;
    private int queueSize = 0;
    private int restrictedNetworkType = -1;
    private String trackingId;
    private boolean useAnonymizeIp = false;
    private UserAgreement userAgreement;
    private String userId;
    private String version;

    public String getTrackingId() {
        return this.trackingId;
    }

    public Configuration setTrackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Configuration setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Configuration enableAutoDeviceId() {
        this.enableAutoDeviceId = true;
        return this;
    }

    public boolean isEnableUseInAppLogging() {
        return this.enableUseInAppLogging;
    }

    public boolean isEnableAutoDeviceId() {
        return this.enableAutoDeviceId;
    }

    @Deprecated
    public String getUserId() {
        return this.userId;
    }

    public boolean isUseAnonymizeIp() {
        return this.useAnonymizeIp;
    }

    public UserAgreement getUserAgreement() {
        return this.userAgreement;
    }

    public Configuration setUserAgreement(UserAgreement userAgreement) {
        this.userAgreement = userAgreement;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public Configuration setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getOverrideIp() {
        return this.overrideIp;
    }

    public boolean isAlwaysRunningApp() {
        return this.isAlwaysRunningApp;
    }

    public boolean isEnableFastReady() {
        return this.enableFastReady;
    }

    public int getNetworkTimeoutInMilliSeconds() {
        return this.networkTimeoutInMilliSeconds;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public DBOpenHelper getDbOpenHelper() {
        return this.dbOpenHelper;
    }

    public int getAuidType() {
        return this.auidType;
    }

    public void setAuidType(int auidType) {
        this.auidType = auidType;
    }

    public int getRestrictedNetworkType() {
        return this.restrictedNetworkType;
    }
}
