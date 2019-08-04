package com.sec.android.app.clockpackage.timer.viewmodel;

import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.callback.TimerBixbyActionListener;

public class TimerBixbyActionHandler {
    private TimerBixbyActionListener mTimerListener;

    public TimerBixbyActionHandler(TimerBixbyActionListener listener) {
        this.mTimerListener = listener;
    }

    public void onAction(String action) {
        Log.m41d("TimerBixbyActionHandler", "onAction action : " + action);
        Object obj = -1;
        switch (action.hashCode()) {
            case -692240746:
                if (action.equals("RestartTimer")) {
                    obj = 1;
                    break;
                }
                break;
            case -671358909:
                if (action.equals("StopTimer")) {
                    obj = 2;
                    break;
                }
                break;
            case 85757131:
                if (action.equals("CancelTimer")) {
                    obj = 3;
                    break;
                }
                break;
            case 409836579:
                if (action.equals("StartTimer")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                this.mTimerListener.onStart();
                return;
            case 1:
                if (!this.mTimerListener.onRestartAlert()) {
                    this.mTimerListener.onStart();
                    return;
                }
                return;
            case 2:
                this.mTimerListener.onStop();
                return;
            case 3:
                this.mTimerListener.onCancel();
                return;
            default:
                return;
        }
    }

    public void setTimerTime(long millis) {
        this.mTimerListener.onSetInputTime(millis);
    }

    public String setSelectedPreset(String presetName, long inputMillis) {
        return this.mTimerListener.onSetSelectedPreset(presetName, inputMillis);
    }
}
