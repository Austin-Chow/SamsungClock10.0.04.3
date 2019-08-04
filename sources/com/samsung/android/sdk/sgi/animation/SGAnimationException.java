package com.samsung.android.sdk.sgi.animation;

import android.util.Log;

final class SGAnimationException {
    SGAnimationException() {
    }

    public static void handle(Exception e, String str) {
        if (str == null || str.isEmpty()) {
            str = "Unhandled exception in module Animation";
        }
        e.printStackTrace();
        Log.e("SGI", str + " :" + e.toString());
        System.exit(0);
    }
}
