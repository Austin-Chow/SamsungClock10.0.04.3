package com.sec.android.diagmonagent.log.provider;

import java.util.ArrayList;
import java.util.List;

public class DiagMonProvider extends newAbstractMasterLogProvider {
    public static String AUTHORITY = "";

    public boolean onCreate() {
        return super.onCreate();
    }

    public void setConfiguration(DiagMonConfig config) {
        AUTHORITY = "com.sec.android.log." + config.getServiceId();
        super.setConfiguration(config);
    }

    protected List<String> setAuthorityList() {
        return new ArrayList();
    }

    protected List<String> setLogList() {
        return new ArrayList();
    }

    protected List<String> setPlainLogList() {
        return new ArrayList();
    }

    protected String setServiceName() {
        return "";
    }

    protected String setDeviceId() {
        return "";
    }

    protected String getAuthority() {
        return AUTHORITY;
    }
}
