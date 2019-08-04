package com.sec.android.app.clockpackage.bixby;

import android.content.Context;
import com.samsung.android.sdk.bixby2.Sbixby;
import com.sec.android.app.clockpackage.common.util.Log;

public class BixbyDeepLink {
    public void registerInstanceToCapsule(Context context) {
        Sbixby.initialize(context);
    }

    public void registerActionHandler() {
        try {
            Log.secD("BixbyDeepLink", "registerActionHandler()");
            Sbixby mInstance = Sbixby.getInstance();
            BixbyActionHandler mBixbyActionHandler = new BixbyActionHandler();
            mInstance.addActionHandler("OpenClock", mBixbyActionHandler);
            mInstance.addActionHandler("SetAlarm", mBixbyActionHandler);
            mInstance.addActionHandler("FindAlarms", mBixbyActionHandler);
            mInstance.addActionHandler("EnableAlarms", mBixbyActionHandler);
            mInstance.addActionHandler("DisableAlarms", mBixbyActionHandler);
            mInstance.addActionHandler("DeleteAlarms", mBixbyActionHandler);
            mInstance.addActionHandler("EditAlarm", mBixbyActionHandler);
            mInstance.addActionHandler("DismissAlarm", mBixbyActionHandler);
            mInstance.addActionHandler("SnoozeAlarm", mBixbyActionHandler);
            mInstance.addActionHandler("StartStopwatch", mBixbyActionHandler);
            mInstance.addActionHandler("StopStopwatch", mBixbyActionHandler);
            mInstance.addActionHandler("ResetStopwatch", mBixbyActionHandler);
            mInstance.addActionHandler("LapStopwatch", mBixbyActionHandler);
            mInstance.addActionHandler("GetTimerState", mBixbyActionHandler);
            mInstance.addActionHandler("StartTimer", mBixbyActionHandler);
            mInstance.addActionHandler("StopTimer", mBixbyActionHandler);
            mInstance.addActionHandler("CancelTimer", mBixbyActionHandler);
            mInstance.addActionHandler("DismissTimer", mBixbyActionHandler);
        } catch (IllegalStateException e) {
            Log.secE("BixbyDeepLink", "registerAlarmActionHandler() The Sbixby instance is NULL");
        }
    }
}
