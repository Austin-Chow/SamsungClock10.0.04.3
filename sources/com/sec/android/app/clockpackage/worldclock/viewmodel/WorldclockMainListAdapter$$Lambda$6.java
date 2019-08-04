package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockMainListAdapter$$Lambda$6 implements OnCheckedChangeListener {
    private final WorldclockViewHolder arg$1;

    private WorldclockMainListAdapter$$Lambda$6(WorldclockViewHolder worldclockViewHolder) {
        this.arg$1 = worldclockViewHolder;
    }

    public static OnCheckedChangeListener lambdaFactory$(WorldclockViewHolder worldclockViewHolder) {
        return new WorldclockMainListAdapter$$Lambda$6(worldclockViewHolder);
    }

    @Hidden
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.arg$1.mListItem.setChecked(z);
    }
}
