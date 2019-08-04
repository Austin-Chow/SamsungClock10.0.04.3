package com.sec.android.app.clockpackage.stopwatch.callback;

public interface StopwatchBtnViewModelListener {
    boolean isFragmentAdded();

    void onDismissCopyPopupWindow();

    void onResetBtn();

    void onResumeBtn();

    void onSetViewState();

    void onStartBtn();

    void onStopBtn();
}
