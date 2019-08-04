package com.sec.android.app.clockpackage.common.viewmodel;

import android.content.Context;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.common.util.Log;

public class CoverManager {
    private CoverStateListener mCoverStateListener;
    private boolean mIsRegister = false;
    private StateListener mListener = new C06581();
    private ScoverManager mScoverManager;

    /* renamed from: com.sec.android.app.clockpackage.common.viewmodel.CoverManager$1 */
    class C06581 extends StateListener {
        C06581() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if (state.getSwitchState()) {
                if (CoverManager.this.mCoverStateListener != null) {
                    CoverManager.this.mCoverStateListener.onStateChanged(true);
                }
            } else if (CoverManager.this.mCoverStateListener != null) {
                CoverManager.this.mCoverStateListener.onStateChanged(false);
            }
        }
    }

    public interface CoverStateListener {
        void onStateChanged(boolean z);
    }

    public CoverManager(Context context) {
        this.mScoverManager = new ScoverManager(context);
    }

    public void registerListener() {
        if (this.mScoverManager != null) {
            this.mIsRegister = true;
            this.mScoverManager.registerListener(this.mListener);
        }
        Log.secD("CoverManager", "registerListener");
    }

    public void unregisterListener() {
        if (!(!this.mIsRegister || this.mScoverManager == null || this.mListener == null)) {
            this.mScoverManager.unregisterListener(this.mListener);
            this.mListener = null;
        }
        Log.secD("CoverManager", "unregisterListener");
    }

    public CoverManager setCoverStateListener(CoverStateListener listener) {
        Log.secD("CoverManager", "setCoverStateListener");
        this.mCoverStateListener = listener;
        return this;
    }

    public boolean isCoverClosed() {
        if (this.mScoverManager == null) {
            return false;
        }
        ScoverState coverState = this.mScoverManager.getCoverState();
        if (coverState != null) {
            return !coverState.getSwitchState();
        } else {
            return false;
        }
    }
}
