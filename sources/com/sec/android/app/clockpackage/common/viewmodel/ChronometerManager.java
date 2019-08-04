package com.sec.android.app.clockpackage.common.viewmodel;

import android.content.Context;
import com.sec.android.app.clockpackage.common.util.Log;

public abstract class ChronometerManager {
    public static boolean sIsRebootSequence = false;
    protected static Object sLock = new Object();
    public static boolean sStartedByUser = false;
    protected Context mContext = null;
    protected boolean mIsClockPackageResumed = false;
    protected boolean mIsFragmentShowing;

    protected abstract void startNotification();

    protected abstract void stopNotification();

    public boolean isClockPackageResumed() {
        return this.mIsClockPackageResumed;
    }

    public void setIsClockPackageResumed(boolean isResumed) {
        if (this.mIsClockPackageResumed != isResumed) {
            this.mIsClockPackageResumed = isResumed;
            updateNotification();
        }
    }

    public void updateNotification() {
        Log.secD("ChronometerManager", "updateNotification()");
        if (this.mIsClockPackageResumed) {
            stopNotification();
        } else {
            startNotification();
        }
    }

    public void fragmentStarted() {
        this.mIsFragmentShowing = true;
    }
}
