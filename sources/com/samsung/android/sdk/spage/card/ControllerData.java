package com.samsung.android.sdk.spage.card;

import android.text.TextUtils;
import com.samsung.android.sdk.spage.card.base.JsonFieldData;

public class ControllerData extends JsonFieldData<ControllerData> {
    private int mFlags = 0;

    public ControllerData(String controllerType) {
        if (TextUtils.isEmpty(controllerType)) {
            throw new IllegalArgumentException("controller type not valid");
        }
        put("controllerType", controllerType);
    }

    public ControllerData setState(int state) {
        return (ControllerData) put("state", Integer.toString(state));
    }
}
