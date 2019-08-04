package com.samsung.android.sdk.sgi.render;

import android.util.Log;

final class SGRenderException {
    SGRenderException() {
    }

    public static void handle(Exception e, String str) {
        if (str == null || !str.isEmpty()) {
            str = "Unhandled exception in module Render";
        }
        e.printStackTrace();
        Log.e("SGI", str + " :" + e.toString());
        System.exit(0);
    }
}
