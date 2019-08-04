package com.sec.android.app.clockpackage.timer.callback;

public interface TimerAnimationViewModelListener {
    int onGetButtonLayoutBottomMargin();

    boolean onIsDisplayTitleView();

    void onSetPresetViewVisibility(int i);

    void onSetTimerNameVisibility();

    void onSetViewState(boolean z, boolean z2);
}
