package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DiagMonConfig {
    private boolean mAgreement = false;
    private String mAppVer = "";
    private List<String> mAuthorityList;
    private Context mContext;
    private String mDeviceId = "";
    private List<String> mLogList;
    private String mServiceId = "";
    private String mServiceName = "Samsung Software";
    private String mTrackingId = "";

    public DiagMonConfig(Context cxt) {
        this.mContext = cxt;
        this.mAuthorityList = new ArrayList();
        this.mLogList = new ArrayList();
        setAppVersion();
    }

    public DiagMonConfig setAgree(String agree) {
        if (agree.isEmpty()) {
            Log.w("DIAGMON_AGENT", "Empty agreement");
            this.mAgreement = false;
        } else if (agree.equals("Y") || agree.equals("D")) {
            this.mAgreement = true;
        } else {
            Log.w("DIAGMON_AGENT", "Wrong agreement : " + agree);
            this.mAgreement = false;
        }
        return this;
    }

    public boolean getAgree() {
        return this.mAgreement;
    }

    public DiagMonConfig setServiceId(String serviceId) {
        this.mServiceId = serviceId;
        setAuthorityList(serviceId);
        return this;
    }

    public String getServiceId() {
        return this.mServiceId;
    }

    public DiagMonConfig setLogList(List<String> logList) {
        this.mLogList = logList;
        return this;
    }

    public List<String> getLogList() {
        return this.mLogList;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public String getServiceName() {
        return this.mServiceName;
    }

    public DiagMonConfig setAuthorityList(String authority) {
        this.mAuthorityList.add("com.sec.android.log." + authority);
        return this;
    }

    protected List<String> getAuthorityList() {
        return this.mAuthorityList;
    }

    public DiagMonConfig setTrackingId(String id) {
        this.mTrackingId = id;
        return this;
    }

    public String getTrackingId() {
        return this.mTrackingId;
    }

    protected void setAppVersion() {
        this.mAppVer = DiagMonUtil.getPackageVersion(this.mContext);
    }
}
