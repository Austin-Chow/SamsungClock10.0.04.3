package com.sec.android.app.clockpackage.common.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

public final class RubinStateContract {
    public static final Uri RUBIN_STATE_URI = Uri.parse("content://com.samsung.android.rubin.state");
    private static String mCurrentRubinState;
    private static boolean mRubinSupportedAppEnabled;

    public static void checkRubinState(Context context) {
        Bundle bundle = null;
        try {
            bundle = context.getContentResolver().call(RUBIN_STATE_URI, "getRubinState", null, null);
        } catch (IllegalArgumentException e) {
            Log.secE("RubinStateContract", "Can't use " + RUBIN_STATE_URI);
        }
        if (bundle != null) {
            mCurrentRubinState = bundle.getString("currentRubinState");
            mRubinSupportedAppEnabled = bundle.getBoolean("isEnabledInSupportedApps");
        } else {
            mCurrentRubinState = "ACCOUNT_NOT_SIGNED_IN";
        }
        Log.secD("RubinStateContract", "Rubin State: " + mCurrentRubinState);
    }

    public static String getCurrentRubinState() {
        return mCurrentRubinState;
    }

    public static boolean getRubinSupportedAppEnabled() {
        return mRubinSupportedAppEnabled;
    }

    public static boolean isRubinActivated(Context context) {
        if (mCurrentRubinState == null) {
            checkRubinState(context);
        }
        return "OK".equals(mCurrentRubinState) && mRubinSupportedAppEnabled;
    }
}
