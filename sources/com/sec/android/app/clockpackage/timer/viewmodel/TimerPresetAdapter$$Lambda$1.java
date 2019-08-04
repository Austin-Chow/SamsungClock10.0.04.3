package com.sec.android.app.clockpackage.timer.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetAdapter.PresetViewHolder;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerPresetAdapter$$Lambda$1 implements OnClickListener {
    private final TimerPresetAdapter arg$1;
    private final PresetViewHolder arg$2;

    private TimerPresetAdapter$$Lambda$1(TimerPresetAdapter timerPresetAdapter, PresetViewHolder presetViewHolder) {
        this.arg$1 = timerPresetAdapter;
        this.arg$2 = presetViewHolder;
    }

    public static OnClickListener lambdaFactory$(TimerPresetAdapter timerPresetAdapter, PresetViewHolder presetViewHolder) {
        return new TimerPresetAdapter$$Lambda$1(timerPresetAdapter, presetViewHolder);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCreateViewHolder$0(this.arg$2, view);
    }
}
