package com.sec.android.app.clockpackage.timer.callback;

public interface TimerBtnViewModelListener {
    boolean isEditMode();

    void onCancelBtn();

    void onCheckInputData();

    void onDisablePickerEditMode();

    void onPauseBtn();

    void onResumeBtn();

    void onStartBtn();
}
