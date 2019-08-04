package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class SearchBarListViewModel$$Lambda$1 implements OnItemClickListener {
    private final SearchBarListViewModel arg$1;

    private SearchBarListViewModel$$Lambda$1(SearchBarListViewModel searchBarListViewModel) {
        this.arg$1 = searchBarListViewModel;
    }

    public static OnItemClickListener lambdaFactory$(SearchBarListViewModel searchBarListViewModel) {
        return new SearchBarListViewModel$$Lambda$1(searchBarListViewModel);
    }

    @Hidden
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.arg$1.lambda$setDropdownList$0(adapterView, view, i, j);
    }
}
