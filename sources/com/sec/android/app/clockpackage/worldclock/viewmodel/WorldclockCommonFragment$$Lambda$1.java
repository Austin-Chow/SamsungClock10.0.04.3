package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class WorldclockCommonFragment$$Lambda$1 implements OnOffsetChangedListener {
    private final WorldclockCommonFragment arg$1;

    private WorldclockCommonFragment$$Lambda$1(WorldclockCommonFragment worldclockCommonFragment) {
        this.arg$1 = worldclockCommonFragment;
    }

    public static OnOffsetChangedListener lambdaFactory$(WorldclockCommonFragment worldclockCommonFragment) {
        return new WorldclockCommonFragment$$Lambda$1(worldclockCommonFragment);
    }

    @Hidden
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        this.arg$1.lambda$onCreateView$0(appBarLayout, i);
    }
}
