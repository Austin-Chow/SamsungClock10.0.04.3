package com.sec.android.app.clockpackage.common.util;

import android.net.Uri;
import android.provider.BaseColumns;

public final class SleepPatternContract {
    private static final Uri AUTHORITY_URI = Uri.parse("content://com.samsung.android.rubin.persona.sleeppattern");

    public static final class SleepPatternInfo implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(SleepPatternContract.AUTHORITY_URI, "sleep_pattern_info");
    }
}
