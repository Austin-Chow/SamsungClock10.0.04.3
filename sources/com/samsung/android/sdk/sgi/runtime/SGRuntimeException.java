package com.samsung.android.sdk.sgi.runtime;

import android.util.Log;

final class SGRuntimeException {
    SGRuntimeException() {
    }

    public static void handle(Exception e, String str) {
        if (str == null || str.isEmpty()) {
            str = "Unhandled exception in module Runtime";
        }
        e.printStackTrace();
        Log.e("SGI", str + " :" + e.toString());
        System.exit(0);
    }
}
