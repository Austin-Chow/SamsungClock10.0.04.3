package com.sec.android.app.clockpackage.common.util;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserProfileContract {
    private static final Uri AUTHORITY_URI = Uri.parse("content://com.samsung.android.rubin.userprofile");

    public static final class UserProfile implements BaseColumns {
        public static final Uri CATEGORY_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "category_glob");
        public static final Uri CONTENT_URI = Uri.withAppendedPath(UserProfileContract.AUTHORITY_URI, "user_profile");
        public static final Uri PERIOD_CATEGORY_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "period_category_glob");
        public static final Uri PERIOD_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "period");
    }
}
