package com.sec.android.app.clockpackage.timer.viewmodel;

import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerFragment$$Lambda$1 implements OnApplyWindowInsetsListener {
    private final TimerFragment arg$1;

    private TimerFragment$$Lambda$1(TimerFragment timerFragment) {
        this.arg$1 = timerFragment;
    }

    public static OnApplyWindowInsetsListener lambdaFactory$(TimerFragment timerFragment) {
        return new TimerFragment$$Lambda$1(timerFragment);
    }

    @Hidden
    public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        return this.arg$1.lambda$onCreate$0(view, windowInsetsCompat);
    }
}
