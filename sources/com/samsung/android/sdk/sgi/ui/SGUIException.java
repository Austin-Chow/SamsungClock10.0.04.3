package com.samsung.android.sdk.sgi.ui;

import android.util.Log;

final class SGUIException {
    SGUIException() {
    }

    public static void handle(Exception e, String str) {
        if (str == null || str.isEmpty()) {
            str = "Unhandled exception in module UI";
        }
        e.printStackTrace();
        Log.e("SGI", str + " :" + e.toString());
        System.exit(0);
    }
}
