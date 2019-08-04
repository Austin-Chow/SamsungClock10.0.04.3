package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.content.Context;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchBixbyActionListener;
import com.sec.android.app.clockpackage.stopwatch.model.StopwatchData;

public class StopwatchBixbyActionListenerImpl implements StopwatchBixbyActionListener {
    private StopwatchManager mStopwatchManager = StopwatchManager.getInstance();

    public StopwatchBixbyActionListenerImpl(Context context) {
        this.mStopwatchManager.setContext(context);
    }

    public void onStart() {
        if (StopwatchData.getStopwatchState() == 2) {
            this.mStopwatchManager.resume();
        } else {
            this.mStopwatchManager.start();
        }
    }

    public void onStop() {
        if (StopwatchData.getStopwatchState() == 1) {
            this.mStopwatchManager.stop();
            this.mStopwatchManager.saveSharedPreference(null);
        }
    }

    public void onReset() {
        this.mStopwatchManager.reset();
    }

    public void onLap() {
        switch (StopwatchData.getStopwatchState()) {
            case 1:
                this.mStopwatchManager.addLap();
                return;
            case 2:
                this.mStopwatchManager.resume();
                this.mStopwatchManager.addLap();
                return;
            default:
                this.mStopwatchManager.start();
                this.mStopwatchManager.addLap();
                return;
        }
    }
}
