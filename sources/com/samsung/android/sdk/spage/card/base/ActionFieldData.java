package com.samsung.android.sdk.spage.card.base;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public abstract class ActionFieldData<T extends ActionFieldData> extends JsonFieldData<T> {
    public T setIntent(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Intent is null");
        }
        remove(NotificationCompat.CATEGORY_EVENT);
        return (ActionFieldData) put("intent", intent.toUri(1));
    }
}
