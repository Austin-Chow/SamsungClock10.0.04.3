package com.samsung.android.sdk.sgi.base;

import android.util.Log;

final class SGBaseException {
    SGBaseException() {
    }

    public static void handle(Exception e, String str) {
        if (str == null || str.isEmpty()) {
            str = "Unhandled exception in module Base";
        }
        e.printStackTrace();
        Log.e("SGI", str + " :" + e.toString());
        System.exit(0);
    }
}
