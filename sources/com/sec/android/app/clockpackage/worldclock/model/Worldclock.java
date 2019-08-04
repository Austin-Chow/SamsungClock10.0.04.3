package com.sec.android.app.clockpackage.worldclock.model;

import android.net.Uri;

public class Worldclock {
    public static final Uri DATA_URI = Uri.parse("content://com.sec.android.provider.stri_s1_worldclock/HOMEZONE/");
    public static final Long LIST_ANIMATION_DURATION = Long.valueOf(400);
    public static final String[] WC_COLUMNS = new String[]{"_id", "city", "gmt", "dst", "homezone", "pointX", "pointY"};
    public static final Long WEATHER_ANIMATION_DURATION = Long.valueOf(100);
}
