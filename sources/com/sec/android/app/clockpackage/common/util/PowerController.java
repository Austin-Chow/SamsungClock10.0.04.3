package com.sec.android.app.clockpackage.common.util;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import java.util.concurrent.locks.ReentrantLock;

public class PowerController {
    private ReentrantLock lock = new ReentrantLock(true);
    private WakeLock wakeLockForDim;
    private WakeLock wakeLockForPartialWakeLock;

    public void acquireDim(Context context, String request) {
        Log.secD("HELL-PowerController", "acquireDim : " + request);
        try {
            this.lock.lock();
            if (this.wakeLockForDim == null || !this.wakeLockForDim.isHeld()) {
                if (context != null) {
                    this.wakeLockForDim = ((PowerManager) context.getSystemService("power")).newWakeLock(805306374, request);
                    this.wakeLockForDim.acquire();
                }
                this.lock.unlock();
                return;
            }
            Log.secD("HELL-PowerController", "acquireDim isHeld");
        } finally {
            this.lock.unlock();
        }
    }

    public void releaseDim() {
        Log.secD("HELL-PowerController", "releaseDim");
        try {
            this.lock.lock();
            if (this.wakeLockForDim != null) {
                if (this.wakeLockForDim.isHeld()) {
                    Log.secD("HELL-PowerController", "release / isHeld()");
                    this.wakeLockForDim.release();
                }
                this.wakeLockForDim = null;
            }
            this.lock.unlock();
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public void acquirePartial(Context context, String request) {
        Log.secD("HELL-PowerController", "acquirePartial");
        try {
            this.lock.lock();
            if (context != null && (this.wakeLockForPartialWakeLock == null || !this.wakeLockForPartialWakeLock.isHeld())) {
                this.wakeLockForPartialWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, request);
                this.wakeLockForPartialWakeLock.acquire();
            }
            this.lock.unlock();
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public void releasePartial() {
        Log.secD("HELL-PowerController", "releasePartial");
        try {
            this.lock.lock();
            if (this.wakeLockForPartialWakeLock != null) {
                this.wakeLockForPartialWakeLock.release();
                this.wakeLockForPartialWakeLock = null;
            }
            this.lock.unlock();
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }
}
