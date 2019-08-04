package com.samsung.android.sdk.spage.card;

import com.samsung.android.sdk.spage.card.base.ActionFieldData;

public class TextData extends ActionFieldData<TextData> {
    public TextData setText(String text) {
        remove("resName");
        return (TextData) put("rawString", text);
    }
}
