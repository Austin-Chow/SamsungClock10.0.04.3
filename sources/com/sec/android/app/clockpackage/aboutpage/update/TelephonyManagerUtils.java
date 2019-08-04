package com.sec.android.app.clockpackage.aboutpage.update;

import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephonyManagerUtils {
    public static String getMcc(Context context) {
        String mcc = "";
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService("phone");
        if (telMgr == null) {
            return mcc;
        }
        String networkOperator = telMgr.getSimOperator();
        if (networkOperator == null || networkOperator.length() < 3) {
            return mcc;
        }
        return networkOperator.substring(0, 3);
    }

    public static String getMnc(Context context) {
        String mnc = "";
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService("phone");
        if (telMgr == null) {
            return mnc;
        }
        String networkOperator = telMgr.getSimOperator();
        if (networkOperator == null || networkOperator.length() <= 3) {
            return mnc;
        }
        return networkOperator.substring(3);
    }
}
