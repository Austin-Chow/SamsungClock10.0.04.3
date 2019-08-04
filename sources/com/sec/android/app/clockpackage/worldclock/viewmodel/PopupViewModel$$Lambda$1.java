package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class PopupViewModel$$Lambda$1 implements Runnable {
    private final PopupViewModel arg$1;

    private PopupViewModel$$Lambda$1(PopupViewModel popupViewModel) {
        this.arg$1 = popupViewModel;
    }

    public static Runnable lambdaFactory$(PopupViewModel popupViewModel) {
        return new PopupViewModel$$Lambda$1(popupViewModel);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$updateWeather$0();
    }
}
