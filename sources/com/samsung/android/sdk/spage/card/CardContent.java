package com.samsung.android.sdk.spage.card;

import android.content.ContentValues;
import android.text.TextUtils;
import com.samsung.android.sdk.spage.card.base.FieldData;
import com.samsung.android.sdk.spage.card.base.JsonFieldData;
import com.samsung.android.sdk.spage.card.base.Manipulator;

public final class CardContent {
    private final ContentValues mCardData = new ContentValues();
    private final int mCardId;
    private String mExtraState = "NORMAL";
    private String mTemplateId;

    private static class FieldPropertyPutter extends Manipulator<FieldPropertyPutter> {
        public FieldPropertyPutter(JsonFieldData jsonFieldData) {
            super(jsonFieldData);
        }

        public void setFieldOption(int fieldOption) {
            if (fieldOption > 0) {
                put("FIELD_OPTION", fieldOption);
            }
        }

        public void clearFieldOption() {
            remove("FIELD_OPTION");
        }
    }

    public CardContent(int cardId) {
        this.mCardId = cardId;
        this.mCardData.put("idNo", Integer.toString(cardId));
    }

    public ContentValues getCardData() {
        return this.mCardData;
    }

    public void put(String key, FieldData fieldData) {
        if (fieldData == null) {
            throw new IllegalArgumentException("FieldData is null");
        } else if (key == null) {
            throw new IllegalArgumentException("Key is null");
        } else {
            this.mCardData.put(key, fieldData.getData());
        }
    }

    public void put(String key, int fieldOption, JsonFieldData jsonFieldData) {
        if (jsonFieldData == null) {
            throw new IllegalArgumentException("FieldData is null");
        } else if (key == null) {
            throw new IllegalArgumentException("Key is null");
        } else {
            FieldPropertyPutter fieldPropertyPutter = new FieldPropertyPutter(jsonFieldData);
            fieldPropertyPutter.setFieldOption(fieldOption);
            this.mCardData.put(key, fieldPropertyPutter.getData());
            fieldPropertyPutter.clearFieldOption();
        }
    }

    public void setExtraState(String extraState) {
        if (TextUtils.isEmpty(this.mTemplateId) || "NORMAL".equals(extraState)) {
            this.mExtraState = extraState;
            this.mCardData.put("extraState", extraState);
            return;
        }
        throw new IllegalStateException("if template id already set, extra state should be NORMAL or not set.");
    }
}
