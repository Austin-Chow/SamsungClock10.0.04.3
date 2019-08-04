package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.os.EnvironmentCompat;

public class newPackageInformation {
    public static newPackageInformation instance = new newPackageInformation();

    public String getPackageName(Context context) {
        return context.getPackageName();
    }

    public String getPackageVersion(Context context) {
        return getPackageVersion(context, getPackageName(context));
    }

    public String getPackageVersion(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            try {
                return packageManager.getPackageInfo(packageName, 0).versionName;
            } catch (NameNotFoundException e) {
            }
        }
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public static String getTWID() {
        String serialNo = getSerialNo();
        if ("".equals(serialNo)) {
            return "";
        }
        return "TWID:" + serialNo;
    }

    public static String getSerialNo() {
        if (VERSION.SDK_INT >= 26) {
            return "";
        }
        return Build.SERIAL;
    }

    public Bundle getDeviceInfoBundle(Context context) {
        Bundle deviceInfo = new Bundle();
        try {
            deviceInfo.putBundle("deviceInfo", new Bundle());
        } catch (Exception e) {
        }
        try {
            deviceInfo.getBundle("deviceInfo").putString("serviceClientVer", getPackageVersion(context));
        } catch (Exception e2) {
        }
        return deviceInfo;
    }
}
