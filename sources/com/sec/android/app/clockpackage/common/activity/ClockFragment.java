package com.sec.android.app.clockpackage.common.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;

public abstract class ClockFragment extends Fragment {
    protected View mFragmentView = null;

    public abstract boolean onClockDispatchKeyEvent(KeyEvent keyEvent, View view);

    public abstract void onTabSelected();

    public abstract void onTabUnselected();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cleanupResources();
    }

    public void onDestroy() {
        cleanupResources();
        super.onDestroy();
    }

    private void cleanupResources() {
        Log.secD("ClockFragment", "cleanupResources()");
        if (this.mFragmentView != null) {
            ClockUtils.nullViewDrawablesRecursive(this.mFragmentView);
        }
        this.mFragmentView = null;
    }

    public void onResume() {
        Log.secD("ClockFragment", "onResume : " + getTag());
        super.onResume();
    }

    public void onPause() {
        Log.secD("ClockFragment", "onPause : " + getTag());
        super.onPause();
    }

    public void onMultiWindowModeChanged(boolean isMultiWindowMode) {
    }

    public void onStartActionMode(BottomNavigationView bottomView) {
    }

    public void onFinishActionMode() {
    }
}
