package com.sec.android.diagmonagent.log.provider;

import android.os.Bundle;
import java.util.List;

public abstract class newAbstractMasterLogProvider extends newAbstractLogProvider {
    protected abstract List<String> setAuthorityList();

    protected abstract String setServiceName();

    public boolean onCreate() {
        if (!super.onCreate()) {
            return false;
        }
        data.putBundle("registered", makeBundle("registered", false));
        data.putBundle("pushRegistered", makeBundle("pushRegistered", false));
        data.putBundle("tryRegistering", makeBundle("tryRegistering", true));
        data.putBundle("nonce", makeBundle("nonce", ""));
        data.putBundle("authorityList", makeAuthorityListBundle(setAuthorityList()));
        data.putBundle("serviceName", makeBundle("serviceName", setServiceName()));
        data.putBundle("deviceId", makeBundle("deviceId", setDeviceId()));
        data.putBundle("deviceInfo", setDeviceInfo());
        data.putBundle("uploadWifionly", makeBundle("uploadWifionly", setUploadWiFiOnly()));
        data.putBundle("supportPush", makeBundle("supportPush", setSupportPush()));
        data.putBundle("logList", makeLogListBundle(setLogList()));
        data.putBundle("plainLogList", makeLogListBundle(setPlainLogList()));
        return true;
    }

    public void setConfiguration(DiagMonConfig config) {
        data.putBundle("authorityList", makeAuthorityListBundle(config.getAuthorityList()));
        data.putBundle("serviceName", makeBundle("serviceName", config.getServiceName()));
        data.putBundle("deviceId", makeBundle("deviceId", config.getDeviceId()));
        data.putBundle("agreed", makeBundle("agreed", config.getAgree()));
        data.putBundle("logList", makeLogListBundle(config.getLogList()));
        data.putBundle("plainLogList", makeLogListBundle(setPlainLogList()));
    }

    private Bundle makeBundle(String key, boolean value) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(key, value);
        return bundle;
    }

    private Bundle makeBundle(String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    private Bundle makeAuthorityListBundle(List<String> authorityList) {
        Bundle authorityListBundle = new Bundle();
        for (String authority : authorityList) {
            authorityListBundle.putString(authority, authority);
        }
        return authorityListBundle;
    }

    protected String setDeviceId() {
        newPackageInformation newpackageinformation = newPackageInformation.instance;
        return newPackageInformation.getTWID();
    }

    protected Bundle setDeviceInfo() {
        return newPackageInformation.instance.getDeviceInfoBundle(getContext());
    }

    protected boolean setUploadWiFiOnly() {
        return true;
    }

    protected boolean setSupportPush() {
        return true;
    }

    private void enforceAgreement() {
    }

    public Bundle call(String method, String arg, Bundle extras) {
        enforceSelfOrSystem();
        if ("get".equals(method) && "registered".equals(arg)) {
            enforceAgreement();
        }
        return super.call(method, arg, extras);
    }
}
