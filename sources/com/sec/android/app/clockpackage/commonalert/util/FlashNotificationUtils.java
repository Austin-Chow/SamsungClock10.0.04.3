package com.sec.android.app.clockpackage.commonalert.util;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;

public class FlashNotificationUtils {
    public static void startFlashNotification(Context context) {
        Log.secD("FlashNotificationUtils", "startFlashNotification");
        if (!StateUtils.isDndModeAlarmMuted(context)) {
            try {
                ((AccessibilityManager) context.getSystemService("accessibility")).semStartFlashNotification(context);
            } catch (NoSuchMethodError e) {
                Log.secE("FlashNotificationUtils", "NoSuchMethodError");
            }
        }
    }

    public static void stopFlashNotification(Context context) {
        Log.secD("FlashNotificationUtils", "stopFlashNotification");
        try {
            ((AccessibilityManager) context.getSystemService("accessibility")).semStopFlashNotification(context);
        } catch (NoSuchMethodError e) {
            Log.secE("FlashNotificationUtils", "NoSuchMethodError");
        }
    }
}
