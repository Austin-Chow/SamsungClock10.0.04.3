package com.sec.android.app.clockpackage.timer.viewmodel;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerPresetAdapter$$Lambda$6 implements OnMenuItemClickListener {
    private final TimerPresetAdapter arg$1;
    private final int arg$2;

    private TimerPresetAdapter$$Lambda$6(TimerPresetAdapter timerPresetAdapter, int i) {
        this.arg$1 = timerPresetAdapter;
        this.arg$2 = i;
    }

    public static OnMenuItemClickListener lambdaFactory$(TimerPresetAdapter timerPresetAdapter, int i) {
        return new TimerPresetAdapter$$Lambda$6(timerPresetAdapter, i);
    }

    @Hidden
    public boolean onMenuItemClick(MenuItem menuItem) {
        return this.arg$1.lambda$null$2(this.arg$2, menuItem);
    }
}
