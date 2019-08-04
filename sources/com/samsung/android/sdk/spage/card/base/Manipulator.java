package com.samsung.android.sdk.spage.card.base;

public abstract class Manipulator<T extends Manipulator> extends JsonFieldData<T> {
    private final JsonFieldData mFieldData;

    public Manipulator(JsonFieldData fieldData) {
        this.mFieldData = fieldData;
    }

    public String getData() {
        return this.mFieldData.getData();
    }

    protected T put(String key, String value) {
        this.mFieldData.put(key, value);
        return this;
    }

    protected T put(String key, int value) {
        this.mFieldData.put(key, value);
        return this;
    }

    protected void remove(String key) {
        this.mFieldData.remove(key);
    }
}
