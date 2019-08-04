package com.sec.android.app.clockpackage.worldclock.viewmodel;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class SearchBarViewModel$$Lambda$1 implements Runnable {
    private final SearchBarViewModel arg$1;

    private SearchBarViewModel$$Lambda$1(SearchBarViewModel searchBarViewModel) {
        this.arg$1 = searchBarViewModel;
    }

    public static Runnable lambdaFactory$(SearchBarViewModel searchBarViewModel) {
        return new SearchBarViewModel$$Lambda$1(searchBarViewModel);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$performTouchSearchBar$0();
    }
}
