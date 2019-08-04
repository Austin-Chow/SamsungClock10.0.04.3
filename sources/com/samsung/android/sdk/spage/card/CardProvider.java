package com.samsung.android.sdk.spage.card;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.samsung.android.sdk.spage.card.event.Event;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class CardProvider extends ContentProvider {
    protected abstract void onUpdate(Context context, CardContentManager cardContentManager, int[] iArr);

    private static String getCallingPackageName(Context context) {
        int uid = Binder.getCallingUid();
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return null;
        }
        String[] packageName = pm.getPackagesForUid(uid);
        if (packageName == null || packageName.length <= 0) {
            return null;
        }
        return packageName[0];
    }

    public final int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public final String getType(Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public final Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public final boolean onCreate() {
        return true;
    }

    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public final int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public final Bundle call(String method, String arg, Bundle extras) {
        if (!"callRequest".equals(method) || extras == null) {
            return extras;
        }
        Log.d("CardProvider", "onReceive executing call method");
        Context context = getContext();
        if (!isAllowed(context, getCallingPackageName(context))) {
            return extras;
        }
        Intent intent = (Intent) extras.getParcelable("callIntent");
        if (intent == null) {
            return null;
        }
        String action = intent.getAction();
        long token = Binder.clearCallingIdentity();
        try {
            if ("com.samsung.android.app.spage.action.CARD_UPDATE".equals(action)) {
                Log.d("CardProvider", "onReceive onUpdate");
                onUpdate(context, CardContentManager.getInstance(), intent.getIntArrayExtra("IdNo"));
            } else if ("com.samsung.android.app.spage.action.CARD_ENABLED".equals(action)) {
                Log.d("CardProvider", "onReceive onEnabled");
                onEnabled(context, intent.getIntArrayExtra("IdNo"));
            } else if ("com.samsung.android.app.spage.action.CARD_DISABLED".equals(action)) {
                Log.d("CardProvider", "onReceive onDisabled");
                onDisabled(context, intent.getIntArrayExtra("IdNo"));
            } else if ("com.samsung.android.app.spage.action.CARD_EVENT".equals(action)) {
                Log.d("CardProvider", "onReceive newEvent");
                Bundle eventExtras = intent.getExtras();
                if (eventExtras == null) {
                    Binder.restoreCallingIdentity(token);
                    return null;
                }
                Event event = Event.newEvent(eventExtras);
                if (event != null) {
                    onReceiveEvent(context, CardContentManager.getInstance(), intent.getIntExtra("IdNo", -1), event);
                }
            } else if ("com.samsung.android.app.spage.action.CARD_INSTANT_UPDATE".equals(action)) {
                Log.d("CardProvider", "onReceive Instant update");
                int updateCode = intent.getIntExtra("updateCode", 0);
                if (updateCode != 0) {
                    onInstantUpdate(context, CardContentManager.getInstance(), intent.getIntExtra("IdNo", -1), updateCode);
                } else {
                    Log.e("CardProvider", "wrong update code - zero");
                }
            } else if ("com.samsung.android.app.spage.action.MULTI_INSTANCE_PREFERENCE_UPDATE".equals(action)) {
                Log.d("CardProvider", "onReceive onPreferenceRequested");
                onPreferenceRequested(context, CardContentManager.getInstance(), intent.getIntExtra("IdNo", -1));
            }
            Binder.restoreCallingIdentity(token);
            return extras;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(token);
        }
    }

    protected void onEnabled(Context context, int[] cardIds) {
    }

    protected void onDisabled(Context context, int[] cardIds) {
    }

    protected void onReceiveEvent(Context context, CardContentManager cardContentManager, int cardId, Event event) {
    }

    protected void onInstantUpdate(Context context, CardContentManager cardContentManager, int cardId, int updateCode) {
    }

    protected void onPreferenceRequested(Context context, CardContentManager cardContentManager, int parentInstanceCardId) {
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    private boolean isAllowed(Context context, String callingPackageName) {
        boolean z = true;
        if ("com.samsung.android.app.spage".equals(callingPackageName)) {
            if ("eng".equals(Build.TYPE)) {
                try {
                    ApplicationInfo ai = context.getPackageManager().getApplicationInfo(callingPackageName, 128);
                    if (ai == null || (ai.flags & 129) == 0) {
                        z = false;
                    }
                    return z;
                } catch (NameNotFoundException e) {
                    Log.d("CardProvider", "NameNotFoundException " + callingPackageName);
                    return false;
                }
            }
            try {
                for (Signature signature : context.getPackageManager().getPackageInfo(callingPackageName, 64).signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    if ("nKUXDzgZGd/gRG/NqxixmhQ7MWM=".equals(Base64.encodeToString(md.digest(), 2))) {
                        return true;
                    }
                }
            } catch (NameNotFoundException e2) {
                Log.d("CardProvider", "NameNotFoundException/NoSuchAlgorithmException" + callingPackageName);
                return false;
            } catch (NoSuchAlgorithmException e3) {
                Log.d("CardProvider", "NameNotFoundException/NoSuchAlgorithmException" + callingPackageName);
                return false;
            }
        }
        Log.d("CardProvider", "Not allowed package " + callingPackageName);
        return false;
    }
}
