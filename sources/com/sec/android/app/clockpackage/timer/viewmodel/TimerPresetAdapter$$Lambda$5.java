package com.sec.android.app.clockpackage.timer.viewmodel;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetAdapter.PresetViewHolder;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TimerPresetAdapter$$Lambda$5 implements OnCreateContextMenuListener {
    private final TimerPresetAdapter arg$1;
    private final PresetViewHolder arg$2;

    private TimerPresetAdapter$$Lambda$5(TimerPresetAdapter timerPresetAdapter, PresetViewHolder presetViewHolder) {
        this.arg$1 = timerPresetAdapter;
        this.arg$2 = presetViewHolder;
    }

    public static OnCreateContextMenuListener lambdaFactory$(TimerPresetAdapter timerPresetAdapter, PresetViewHolder presetViewHolder) {
        return new TimerPresetAdapter$$Lambda$5(timerPresetAdapter, presetViewHolder);
    }

    @Hidden
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        this.arg$1.lambda$onCreateViewHolder$3(this.arg$2, contextMenu, view, contextMenuInfo);
    }
}
