package com.samsung.android.sdk.spage.card.event;

import android.os.Bundle;

public final class ItemSelectionEvent extends Event {
    private String mSelectedItem;
    private int mSelectedItemIndex;

    ItemSelectionEvent(String type, Bundle extras) {
        super(type, extras);
    }

    protected void initialize(Bundle extras) {
        this.mSelectedItemIndex = extras.getInt("selectedItemIndex", -1);
        this.mSelectedItem = extras.getString("selectedItem", "");
    }
}
