package com.sec.android.app.clockpackage.timer.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerPresetViewActionMode$$Lambda$4 implements Runnable {
    private final TimerPresetViewActionMode arg$1;
    private final boolean arg$2;

    private TimerPresetViewActionMode$$Lambda$4(TimerPresetViewActionMode timerPresetViewActionMode, boolean z) {
        this.arg$1 = timerPresetViewActionMode;
        this.arg$2 = z;
    }

    public static Runnable lambdaFactory$(TimerPresetViewActionMode timerPresetViewActionMode, boolean z) {
        return new TimerPresetViewActionMode$$Lambda$4(timerPresetViewActionMode, z);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$startSelectModeActionBarAnimation$3(this.arg$2);
    }
}
