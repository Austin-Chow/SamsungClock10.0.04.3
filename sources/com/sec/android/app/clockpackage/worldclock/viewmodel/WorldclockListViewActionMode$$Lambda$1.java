package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockListViewActionMode$$Lambda$1 implements OnClickListener {
    private final WorldclockListViewActionMode arg$1;

    private WorldclockListViewActionMode$$Lambda$1(WorldclockListViewActionMode worldclockListViewActionMode) {
        this.arg$1 = worldclockListViewActionMode;
    }

    public static OnClickListener lambdaFactory$(WorldclockListViewActionMode worldclockListViewActionMode) {
        return new WorldclockListViewActionMode$$Lambda$1(worldclockListViewActionMode);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$initMultiSelectActionBar$0(view);
    }
}
