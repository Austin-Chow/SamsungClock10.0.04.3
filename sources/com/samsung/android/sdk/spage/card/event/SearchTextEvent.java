package com.samsung.android.sdk.spage.card.event;

import android.os.Bundle;

public final class SearchTextEvent extends Event {
    private String mSearchText;

    SearchTextEvent(String type, Bundle extras) {
        super(type, extras);
    }

    protected void initialize(Bundle extras) {
        this.mSearchText = extras.getString("searchText", "");
    }
}
