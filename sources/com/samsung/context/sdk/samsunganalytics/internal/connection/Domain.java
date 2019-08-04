package com.samsung.context.sdk.samsunganalytics.internal.connection;

import com.samsung.context.sdk.samsunganalytics.internal.util.Utils;

public enum Domain {
    ;
    
    String domain;

    static {
        REGISTRATION = new Domain("REGISTRATION", 0, Utils.isEngBin() ? "https://stg-api.di.atlas.samsung.com" : "https://regi.di.atlas.samsung.com");
        POLICY = new Domain("POLICY", 1, Utils.isEngBin() ? "https://stg-api.di.atlas.samsung.com" : "https://dc.di.atlas.samsung.com");
        DLS = new Domain("DLS", 2, "");
        $VALUES = new Domain[]{REGISTRATION, POLICY, DLS};
    }

    private Domain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
