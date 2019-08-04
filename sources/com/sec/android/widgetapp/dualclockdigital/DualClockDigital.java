package com.sec.android.widgetapp.dualclockdigital;

import android.net.Uri;

public class DualClockDigital {
    public static final String[] COLUMNS = new String[]{"_id", "city", "gmt", "dst", "homezone", "uniqueid", "wid"};
    public static final Uri DATA_URI = Uri.parse("content://com.sec.android.provider.clockpackage.dualclockdigital/HOMEZONE/");
}
