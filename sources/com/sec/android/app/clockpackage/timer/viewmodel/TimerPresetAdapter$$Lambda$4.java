package com.sec.android.app.clockpackage.timer.viewmodel;

import android.view.View;
import android.view.View.OnLongClickListener;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetAdapter.PresetViewHolder;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerPresetAdapter$$Lambda$4 implements OnLongClickListener {
    private final TimerPresetAdapter arg$1;
    private final PresetViewHolder arg$2;

    private TimerPresetAdapter$$Lambda$4(TimerPresetAdapter timerPresetAdapter, PresetViewHolder presetViewHolder) {
        this.arg$1 = timerPresetAdapter;
        this.arg$2 = presetViewHolder;
    }

    public static OnLongClickListener lambdaFactory$(TimerPresetAdapter timerPresetAdapter, PresetViewHolder presetViewHolder) {
        return new TimerPresetAdapter$$Lambda$4(timerPresetAdapter, presetViewHolder);
    }

    @Hidden
    public boolean onLongClick(View view) {
        return this.arg$1.lambda$onCreateViewHolder$1(this.arg$2, view);
    }
}
