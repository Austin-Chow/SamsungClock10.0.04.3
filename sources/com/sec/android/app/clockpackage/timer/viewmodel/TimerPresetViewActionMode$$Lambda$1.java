package com.sec.android.app.clockpackage.timer.viewmodel;

import android.view.View;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerPresetViewActionMode$$Lambda$1 implements Runnable {
    private final boolean arg$1;
    private final View arg$2;

    private TimerPresetViewActionMode$$Lambda$1(boolean z, View view) {
        this.arg$1 = z;
        this.arg$2 = view;
    }

    public static Runnable lambdaFactory$(boolean z, View view) {
        return new TimerPresetViewActionMode$$Lambda$1(z, view);
    }

    @Hidden
    public void run() {
        TimerPresetViewActionMode.lambda$setActionMenuVisibility$0(this.arg$1, this.arg$2);
    }
}
