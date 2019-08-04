package com.samsung.android.sdk.spage.card.base;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonFieldData<T extends JsonFieldData> implements FieldData {
    private final JSONObject mData = new JSONObject();

    public String getData() {
        return this.mData.toString();
    }

    protected T put(String key, String value) {
        if (value == null) {
            throw new IllegalArgumentException(key + " value is null");
        }
        try {
            this.mData.put(key, value);
        } catch (JSONException e) {
            Log.d("JsonFieldData", e.getMessage());
        }
        return this;
    }

    protected T put(String key, int value) {
        try {
            this.mData.put(key, value);
        } catch (JSONException e) {
            Log.d("JsonFieldData", e.getMessage());
        }
        return this;
    }

    protected void remove(String key) {
        if (this.mData.has(key)) {
            this.mData.remove(key);
        }
    }
}
