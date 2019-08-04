package com.samsung.android.sdk.spage.card;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public final class CardContentManager {
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.samsung.android.app.spage.provider");
    private static final Uri CARD_CONTENT_CHANGE_NOTIFICATION_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "change");
    private static final Uri CARD_INFO_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "info");
    private static final Uri CARD_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "card");
    private static final Uri CUSTOMIZE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "customize");
    private static final Uri INSTANT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "instant");
    private static volatile CardContentManager sInstance = null;

    private CardContentManager() {
    }

    public static CardContentManager getInstance() {
        if (sInstance == null) {
            synchronized (CardContentManager.class) {
                if (sInstance == null) {
                    sInstance = new CardContentManager();
                }
                CardContentManager.class.notifyAll();
            }
        }
        return sInstance;
    }

    private boolean isContentProviderEnabled(Context context) {
        ContentProviderClient client = context.getContentResolver().acquireContentProviderClient(BASE_CONTENT_URI);
        if (client == null) {
            Log.d("CardContentManager", "content provider is null");
            return false;
        }
        client.release();
        return true;
    }

    public void updateCardContent(Context context, CardContent cardContent) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        } else if (cardContent == null) {
            throw new IllegalArgumentException("card content is null");
        } else if (isContentProviderEnabled(context)) {
            beforeUpdateContent(cardContent);
            context.getContentResolver().update(CARD_URI, cardContent.getCardData(), null, null);
        }
    }

    public void notifyCardContentChange(Context context, int cardID) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        } else if (isContentProviderEnabled(context)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("idNo", Integer.toString(cardID));
            context.getContentResolver().update(CARD_CONTENT_CHANGE_NOTIFICATION_URI, contentValues, null, null);
        }
    }

    private void beforeUpdateContent(CardContent cardContent) {
        ContentValues cardData = cardContent.getCardData();
        if ("NO_CONTENTS".equals(cardData.get("extraState"))) {
            Iterator it = new ArrayList(cardData.keySet()).iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (key.startsWith("tag_data_")) {
                    String value = (String) cardData.get(key);
                    cardData.remove(key);
                    Object obj = -1;
                    switch (key.hashCode()) {
                        case -59763423:
                            if (key.equals("tag_data_1")) {
                                obj = null;
                                break;
                            }
                            break;
                        case -59763422:
                            if (key.equals("tag_data_2")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case -59763421:
                            if (key.equals("tag_data_3")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case -59763420:
                            if (key.equals("tag_data_4")) {
                                obj = 3;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            cardData.put("tag_data_no_content_3", value);
                            break;
                        case 1:
                            cardData.put("tag_data_no_content_4", value);
                            break;
                        case 2:
                            cardData.put("tag_data_no_content_5", value);
                            break;
                        case 3:
                            cardData.put("tag_data_no_content_6", value);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
