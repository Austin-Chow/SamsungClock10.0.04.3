package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchBixbyActionListener;

public class StopwatchBixbyActionHandler {
    private StopwatchBixbyActionListener mStopwatchListener;

    public StopwatchBixbyActionHandler(StopwatchBixbyActionListener listener) {
        this.mStopwatchListener = listener;
    }

    public void onAction(String action) {
        Log.secD("StopwatchBixbyActionHandler", "onAction action : " + action);
        Object obj = -1;
        switch (action.hashCode()) {
            case 295396331:
                if (action.equals("StartStopwatch")) {
                    obj = null;
                    break;
                }
                break;
            case 438999051:
                if (action.equals("StopStopwatch")) {
                    obj = 1;
                    break;
                }
                break;
            case 821085598:
                if (action.equals("ResetStopwatch")) {
                    obj = 2;
                    break;
                }
                break;
            case 1835785938:
                if (action.equals("LapStopwatch")) {
                    obj = 3;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                this.mStopwatchListener.onStart();
                return;
            case 1:
                this.mStopwatchListener.onStop();
                return;
            case 2:
                this.mStopwatchListener.onReset();
                return;
            case 3:
                this.mStopwatchListener.onLap();
                return;
            default:
                return;
        }
    }
}
