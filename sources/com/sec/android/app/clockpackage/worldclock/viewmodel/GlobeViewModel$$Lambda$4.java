package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class GlobeViewModel$$Lambda$4 implements OnClickListener {
    private final GlobeViewModel arg$1;

    private GlobeViewModel$$Lambda$4(GlobeViewModel globeViewModel) {
        this.arg$1 = globeViewModel;
    }

    public static OnClickListener lambdaFactory$(GlobeViewModel globeViewModel) {
        return new GlobeViewModel$$Lambda$4(globeViewModel);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$updateZoomInOut$1(view);
    }
}
