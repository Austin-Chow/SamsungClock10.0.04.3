package com.samsung.android.sdk.sgi.base;

public final class SGAngleConverter {
    public static double deg2Rad(double value) {
        return SGJNI.SGAngleConverter_deg2Rad(value);
    }

    public static double rad2Deg(double value) {
        return SGJNI.SGAngleConverter_rad2Deg(value);
    }
}
