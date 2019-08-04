package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.CtsVoiceController;
import com.sec.android.app.clockpackage.common.util.Log;

public class TimerCTSHandleActivity extends ClockActivity {
    protected void onCreate(Bundle icicle) {
        try {
            super.onCreate(icicle);
            Log.secD("TimerCTSHandleActivity", "onCreate()");
            Intent intent = getIntent();
            CtsVoiceController.getController().setContext(getApplicationContext());
            if (intent != null) {
                if ("android.intent.action.SET_TIMER".equals(intent.getAction())) {
                    handleSetTimer(intent);
                } else if ("android.intent.action.SHOW_TIMERS".equals(intent.getAction())) {
                    handleShowTimer();
                } else if ("android.intent.action.DISMISS_TIMER".equals(intent.getAction())) {
                    handleDismissTimer();
                }
            }
            finish();
        } catch (Throwable th) {
            finish();
        }
    }

    private void handleSetTimer(Intent intent) {
        Log.secD("TimerCTSHandleActivity", "handleSetTimer");
        boolean isSkipUi = intent.getBooleanExtra("android.intent.extra.alarm.SKIP_UI", false);
        Log.secD("TimerCTSHandleActivity", "isSkipUi = " + isSkipUi);
        int inputSecs = intent.getIntExtra("android.intent.extra.alarm.LENGTH", 60);
        Log.secD("TimerCTSHandleActivity", "inputSecs = " + inputSecs);
        long inputMillis = ((long) inputSecs) * 1000;
        String message = intent.getStringExtra("android.intent.extra.alarm.MESSAGE");
        Log.secD("TimerCTSHandleActivity", "message = " + message);
        TimerManager timerManager = TimerManager.getInstance();
        timerManager.setContext(getApplicationContext());
        if (intent.hasExtra("android.intent.extra.alarm.LENGTH")) {
            String voiceMessage;
            if (inputMillis < 1000) {
                voiceMessage = "Invalid timer length";
                CtsVoiceController.getController().notifyVoiceFailure(this, "Invalid timer length");
            }
            timerManager.cancelTimer();
            timerManager.setInputTime(0, 0, inputSecs);
            timerManager.startTimer(inputMillis, inputMillis, message);
            timerManager.setTimerState(1);
            if (!isSkipUi) {
                ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
                Intent newIntent = new Intent();
                newIntent.putExtra("com.sec.android.clockpackage.timer.TIMER_NAME", message);
                newIntent.putExtra("clockpackage.select.tab", 3);
                newIntent.setComponent(cn);
                startActivity(newIntent);
            }
            voiceMessage = "Timer is created";
            CtsVoiceController.getController().notifyVoiceSuccess(this, "Timer is created");
            return;
        }
        cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
        newIntent = new Intent();
        newIntent.putExtra("clockpackage.select.tab", 3);
        newIntent.setComponent(cn);
        startActivity(newIntent);
    }

    private void handleShowTimer() {
        Log.secD("TimerCTSHandleActivity", "handleShowTimer");
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
        Intent newIntent = new Intent();
        newIntent.putExtra("clockpackage.select.tab", 3);
        newIntent.setComponent(cn);
        startActivity(newIntent);
    }

    private void handleDismissTimer() {
        Log.secD("TimerCTSHandleActivity", "handleDismissTimer");
        if (ClockUtils.sIsTimerAlertStarted) {
            sendBroadcast(new Intent("com.sec.android.clockpackage.timer.FINISH_ALERT"));
            String voiceMessage = "Timer is dismissed";
            CtsVoiceController.getController().notifyVoiceSuccess(this, "Timer is dismissed");
            return;
        }
        voiceMessage = "No expired timer";
        CtsVoiceController.getController().notifyVoiceFailure(this, "No expired timer");
    }
}
