package com.sec.android.app.clockpackage.common.viewmodel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import com.sec.android.app.clockpackage.common.activity.ClockFragment;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PowerController;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.CoverManager.CoverStateListener;

public abstract class StopwatchTimerCommonFragment extends ClockFragment {
    protected static String TAG_SUB = "StopwatchTimerCommonFragment";
    protected FragmentActivity mActivity;
    protected BroadcastReceiver mBroadcastReceiver = null;
    private CoverManager mCoverManager = null;
    private CoverStateListener mCoverStateListener = new C06601();
    private boolean mIsFirstLaunch = true;
    protected BroadcastReceiver mLocalIntentReceiver = null;
    private PowerController mPowerController = new PowerController();

    /* renamed from: com.sec.android.app.clockpackage.common.viewmodel.StopwatchTimerCommonFragment$1 */
    class C06601 implements CoverStateListener {
        C06601() {
        }

        public void onStateChanged(boolean isOpen) {
            Log.secD(StopwatchTimerCommonFragment.TAG_SUB, "CoverStateListener onStateChanged() / isOpen = " + isOpen + ", isResumed() = " + StopwatchTimerCommonFragment.this.isResumed());
            if (isOpen && (ClockTab.isStopWatchTab() || ClockTab.isTimerTab())) {
                StopwatchTimerCommonFragment.this.acquireDim();
            } else {
                StopwatchTimerCommonFragment.this.mPowerController.releaseDim();
            }
        }
    }

    protected abstract void insertSaLogCurrentTab();

    protected abstract boolean isStartedState();

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (FragmentActivity) context;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCoverManager();
    }

    public void onResume() {
        super.onResume();
        if (StateUtils.isScreenOn(this.mActivity.getApplicationContext())) {
            acquireDim();
        }
        insertSaLogCurrentTab();
    }

    public void onPause() {
        super.onPause();
        releaseDim();
    }

    public void onTabSelected() {
        acquireDim();
        insertSaLogCurrentTab();
    }

    public void onTabUnselected() {
        releaseDim();
    }

    protected void acquireDim() {
        if (isResumed() && this.mActivity != null && isStartedState()) {
            this.mPowerController.acquireDim(this.mActivity.getApplicationContext(), TAG_SUB);
        }
    }

    protected void releaseDim() {
        this.mPowerController.releaseDim();
    }

    protected void registerReceiver(IntentFilter filter, IntentFilter localFilter) {
        this.mActivity.registerReceiver(this.mBroadcastReceiver, filter);
        LocalBroadcastManager.getInstance(this.mActivity.getApplicationContext()).registerReceiver(this.mLocalIntentReceiver, localFilter);
    }

    public void onDestroy() {
        Log.secD("StopwatchTimerCommonFragment", "onDestroy()");
        if (this.mCoverManager != null) {
            this.mCoverManager.setCoverStateListener(null);
            this.mCoverManager.unregisterListener();
            this.mCoverManager = null;
        }
        if (this.mBroadcastReceiver != null) {
            this.mActivity.unregisterReceiver(this.mBroadcastReceiver);
            this.mBroadcastReceiver = null;
        }
        if (this.mLocalIntentReceiver != null) {
            LocalBroadcastManager.getInstance(this.mActivity.getApplicationContext()).unregisterReceiver(this.mLocalIntentReceiver);
        }
        super.onDestroy();
    }

    private void initCoverManager() {
        this.mCoverManager = new CoverManager(this.mActivity.getApplicationContext()).setCoverStateListener(this.mCoverStateListener);
        if (this.mCoverManager != null) {
            this.mCoverManager.registerListener();
        }
    }

    protected boolean isFirstLaunch() {
        return this.mIsFirstLaunch;
    }

    protected void setIsFirstLaunch(boolean mIsFirstLaunch) {
        this.mIsFirstLaunch = mIsFirstLaunch;
    }
}
