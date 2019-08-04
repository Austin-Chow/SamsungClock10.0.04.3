package com.samsung.android.sdk.sgi.vi;

import android.util.Log;

final class SGVIException {
    SGVIException() {
    }

    public static void handle(Exception e, String str) {
        if (str == null || str.isEmpty()) {
            str = "Unhandled exception in module VI";
        }
        e.printStackTrace();
        Log.e("SGI", str + " :" + e.toString());
        System.exit(0);
    }
}
