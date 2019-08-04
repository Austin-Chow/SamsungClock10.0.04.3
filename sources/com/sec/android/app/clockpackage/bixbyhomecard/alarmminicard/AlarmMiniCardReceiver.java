package com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sec.android.app.clockpackage.common.util.Log;

public class AlarmMiniCardReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            Log.secD("AlarmMiniCardReceiver", "action = " + action);
            Object obj = -1;
            switch (action.hashCode()) {
                case -1662713978:
                    if (action.equals("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    AlarmMiniCardProvider.forceUpdate(context);
                    return;
                default:
                    return;
            }
        }
    }
}
