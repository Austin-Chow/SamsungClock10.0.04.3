package com.samsung.android.sdk.spage.card;

import com.samsung.android.sdk.spage.card.base.ActionFieldData;

public class ImageData extends ActionFieldData<ImageData> {
    public ImageData setImageUri(String uriString) {
        remove("resName");
        return (ImageData) put("uriString", uriString);
    }
}
