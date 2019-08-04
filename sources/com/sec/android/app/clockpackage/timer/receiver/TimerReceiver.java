package com.sec.android.app.clockpackage.timer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerManager;

public class TimerReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (Feature.DEBUG_ENG) {
                Log.secD("TimerReceiver", "onReceive() / action = " + action);
            }
            TimerManager timerManager = TimerManager.getInstance();
            timerManager.setContext(context);
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
                case 1275858459:
                    if (action.equals("com.samsung.intent.action.BCS_REQUEST")) {
                        i = 3;
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
                    if (!TimerManager.sStartedByUser) {
                        timerManager.resetSharedPreference();
                        if (!TimerData.isTimerStateResetedOrNone()) {
                            timerManager.cancelTimer();
                            timerManager.setTimerState(3);
                            timerManager.setInputTime(TimerData.getInputMillis());
                            timerManager.updateScreenReset();
                            return;
                        }
                        return;
                    }
                    return;
                case 1:
                    TimerManager.sIsRebootSequence = true;
                    timerManager.resetSharedPreference();
                    return;
                case 2:
                    int reason = intent.getIntExtra("reason", 0);
                    Log.secD("TimerReceiver", "action : ACTION_EMERGENCY, reason : " + reason);
                    if (reason == 2 || reason == 4) {
                        if (ClockUtils.sIsTimerAlertStarted) {
                            Log.secD("TimerReceiver", "TimerAlertUtils.ACTION_FINISH_ALERT");
                            context.sendBroadcast(new Intent("com.sec.android.clockpackage.timer.FINISH_ALERT"));
                        }
                        timerManager.stopNotification();
                    }
                    if (!ClockUtils.isValidPackageInEmergency(context) && reason == 2) {
                        timerManager.cancelTimer();
                        timerManager.setTimerState(3);
                        timerManager.updateScreenReset();
                    }
                    if (!ClockUtils.isValidPackageInEmergency(context)) {
                        return;
                    }
                    if (reason == 5 || reason == 3) {
                        timerManager.restoreSharedPreference();
                        timerManager.updateNotification();
                        return;
                    }
                    return;
                case 3:
                    atCommand(context, intent);
                    return;
                default:
                    return;
            }
        }
    }

    private void atCommand(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        if (data == null) {
            Log.secD("TimerReceiver", "there is no extras");
            return;
        }
        String command = data.getString("command");
        Log.secD("TimerReceiver", "Timer BCS_REQUEST : " + command + ' ' + data.getString("param"));
        if (command != null) {
            String REQUEST_AT_CMD_TIME_OF_TIMER = "AT+CTMRV=";
            String REQUEST_AT_CMD_NUMBER_OF_TIMER = "AT+CTMRV=NR";
            if ("AT+CTMRV=NR".equals(command)) {
                Log.secD("TimerReceiver", "Number of Timer  1");
                ClockUtils.sendBCSResponseIntent(context, Integer.toString(1));
            } else if (!command.contains("AT+CTMRV=")) {
            } else {
                if (Integer.parseInt(command.replace("AT+CTMRV=", "")) != 0) {
                    Log.secD("TimerReceiver", "Timer Wrong number. only support 0");
                    ClockUtils.sendBCSResponseIntent(context, "00:00:00");
                    return;
                }
                ClockUtils.sendBCSResponseIntent(context, TimerData.getInputTimeStr());
                Log.secD("TimerReceiver", "Timer get InputTimerStr :  " + TimerData.getInputTimeStr());
            }
        }
    }
}
