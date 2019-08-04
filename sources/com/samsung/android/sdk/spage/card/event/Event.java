package com.samsung.android.sdk.spage.card.event;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class Event {
    private String mEventName;
    private String mEventType;

    Event(String type, Bundle extras) {
        initialize0(type, extras);
    }

    public static Event newEvent(Bundle extras) {
        String type = extras.getString("eventType");
        if (TextUtils.isEmpty(type)) {
            type = "default";
        }
        Object obj = -1;
        switch (type.hashCode()) {
            case 366526597:
                if (type.equals("SearchTextEvent")) {
                    obj = 2;
                    break;
                }
                break;
            case 530697857:
                if (type.equals("ItemSelectionEvent")) {
                    obj = 1;
                    break;
                }
                break;
            case 1544803905:
                if (type.equals("default")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return new Event(type, extras);
            case 1:
                return new ItemSelectionEvent(type, extras);
            case 2:
                return new SearchTextEvent(type, extras);
            default:
                return null;
        }
    }

    public String getEventName() {
        return this.mEventName;
    }

    private void initialize0(String type, Bundle extras) {
        this.mEventType = type;
        this.mEventName = extras.getString(NotificationCompat.CATEGORY_EVENT);
        initialize(extras);
    }

    protected void initialize(Bundle extras) {
    }
}
