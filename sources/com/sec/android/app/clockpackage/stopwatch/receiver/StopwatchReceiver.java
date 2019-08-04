package com.sec.android.app.clockpackage.stopwatch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchManager;

public class StopwatchReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            StopwatchManager stopwatchManager = StopwatchManager.getInstance();
            stopwatchManager.setContext(context);
            String action = intent.getAction();
            if (Feature.DEBUG_ENG) {
                Log.secD("StopwatchReceiver", "onReceive() / action = " + action);
            }
            int i = -1;
            switch (action.hashCode()) {
                case -147579983:
                    if (action.equals("com.samsung.intent.action.EMERGENCY_STATE_CHANGED")) {
                        i = 2;
                        break;
                    }
                    break;
                case 798292259:
                    if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                        i = 0;
                        break;
                    }
                    break;
                case 1947666138:
                    if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                        boolean z = true;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    if (!StopwatchManager.sStartedByUser) {
                        stopwatchManager.resetSharedPreference();
                        stopwatchManager.reset();
                        return;
                    }
                    return;
                case 1:
                    StopwatchManager.sIsRebootSequence = true;
                    stopwatchManager.resetSharedPreference();
                    return;
                case 2:
                    int reason = intent.getIntExtra("reason", 0);
                    Log.secD("StopwatchReceiver", "action : ACTION_EMERGENCY, reason : " + reason);
                    if (reason == 2 || reason == 4) {
                        stopwatchManager.stopNotification();
                    }
                    if (!ClockUtils.isValidPackageInEmergency(context)) {
                        return;
                    }
                    if (reason == 5 || reason == 3) {
                        stopwatchManager.restoreSharedPreference();
                        stopwatchManager.updateNotification();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
