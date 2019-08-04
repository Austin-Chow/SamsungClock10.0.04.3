package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockCommonFragment$$Lambda$2 implements OnClickListener {
    private final WorldclockCommonFragment arg$1;

    private WorldclockCommonFragment$$Lambda$2(WorldclockCommonFragment worldclockCommonFragment) {
        this.arg$1 = worldclockCommonFragment;
    }

    public static OnClickListener lambdaFactory$(WorldclockCommonFragment worldclockCommonFragment) {
        return new WorldclockCommonFragment$$Lambda$2(worldclockCommonFragment);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCreateView$1(view);
    }
}
