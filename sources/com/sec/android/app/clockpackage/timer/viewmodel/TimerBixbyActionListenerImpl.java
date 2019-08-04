package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.callback.TimerBixbyActionListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import java.util.ArrayList;

public class TimerBixbyActionListenerImpl implements TimerBixbyActionListener {
    private Context mContext;
    private TimerManager mTimerManager = TimerManager.getInstance();

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerBixbyActionListenerImpl$1 */
    class C07841 implements Runnable {
        C07841() {
        }

        public void run() {
            TimerBixbyActionListenerImpl.this.mTimerManager.updateScreenStart(0, TimerData.getInputMillis());
        }
    }

    public TimerBixbyActionListenerImpl(Context context) {
        this.mTimerManager.setContext(context);
        this.mContext = context;
    }

    public boolean onRestartAlert() {
        if (!ClockUtils.sIsTimerAlertStarted) {
            return false;
        }
        Log.secD("TimerBixbyActionListenerImpl", "TimerAlertStarted onRestartAlert()");
        this.mContext.sendBroadcast(new Intent("com.sec.android.clockpackage.timer.finishAlertByRestart"));
        this.mTimerManager.callTimerBroadcast();
        this.mTimerManager.callRestartToastPopup(this.mContext, false);
        return true;
    }

    public void onStart() {
        Log.secD("TimerBixbyActionListenerImpl", "onStart");
        if (TimerData.isTimerStateResetedOrNone()) {
            if (TimerData.getInputMillis() == 0) {
                this.mTimerManager.setInputTime(60000);
            }
            new Handler().postDelayed(new C07841(), 50);
        } else if (TimerData.getTimerState() == 2) {
            this.mTimerManager.startTimer(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis());
            this.mTimerManager.setTimerState(1);
        }
    }

    public void onStop() {
        if (ClockUtils.sIsTimerAlertStarted) {
            this.mContext.sendBroadcast(new Intent("com.sec.android.clockpackage.timer.FINISH_ALERT"));
        } else if (TimerData.getTimerState() == 1) {
            this.mTimerManager.stopTimer();
            this.mTimerManager.setTimerState(2);
        }
    }

    public void onCancel() {
        if (TimerData.getTimerState() == 1 || TimerData.getTimerState() == 2) {
            this.mTimerManager.cancelTimer();
            this.mTimerManager.setTimerState(3);
        }
    }

    public void onSetInputTime(long millis) {
        TimerData.savePreviousInput(millis);
        this.mTimerManager.setInputTime(millis);
    }

    public String onSetSelectedPreset(String presetName, long inputMillis) {
        long presetId = -1;
        String selectedPresetName = null;
        Editor ed = this.mContext.getSharedPreferences("TIMER", 0).edit();
        if (ClockUtils.isEnableString(presetName)) {
            Log.secD("TimerBixbyActionListenerImpl", "onSetSelectedPreset() presetName : " + presetName + " inputMillis : " + inputMillis);
            ArrayList<TimerPresetItem> mPresetList = TimerPresetItem.getAllPreset(this.mContext.getContentResolver(), null, new String[0]);
            int count = 0;
            for (int i = 0; i < mPresetList.size(); i++) {
                TimerPresetItem presetItem = (TimerPresetItem) mPresetList.get(i);
                if (presetName.equalsIgnoreCase(presetItem.getName()) && (inputMillis <= 0 || inputMillis == TimerData.convertMillis(presetItem.getHour(), presetItem.getMinute(), presetItem.getSecond()))) {
                    count++;
                    if (count > 1) {
                        presetId = -1;
                        break;
                    }
                    presetId = presetItem.getId();
                    selectedPresetName = presetItem.getName();
                    this.mTimerManager.setInputTime(presetItem.getHour(), presetItem.getMinute(), presetItem.getSecond());
                    TimerData.savePreviousInput(presetItem.getHour(), presetItem.getMinute(), presetItem.getSecond());
                }
            }
        }
        ed.putLong("selectedPresetId", presetId);
        ed.apply();
        return selectedPresetName;
    }
}
