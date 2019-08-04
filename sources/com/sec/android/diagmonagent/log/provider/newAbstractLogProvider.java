package com.sec.android.diagmonagent.log.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import com.samsung.context.sdk.samsunganalytics.BuildConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class newAbstractLogProvider extends ContentProvider {
    public static Bundle data;

    protected abstract String getAuthority();

    protected abstract List<String> setLogList();

    protected List<String> setPlainLogList() {
        return Arrays.asList(new String[0]);
    }

    public boolean onCreate() {
        data = new Bundle();
        data.putBundle("diagmonSupportV1VersionName", getDiagmonSupportV1VersionNameBundle());
        data.putBundle("diagmonSupportV1VersionCode", getDiagmonSupportV1VersionCodeBundle());
        return true;
    }

    private Bundle getDiagmonSupportV1VersionNameBundle() {
        Bundle bundle = new Bundle();
        try {
            Object object = BuildConfig.class.getDeclaredField("VERSION_NAME").get(null);
            if (object instanceof String) {
                bundle.putString("diagmonSupportV1VersionName", (String) String.class.cast(object));
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e2) {
        } catch (IllegalArgumentException e3) {
        }
        return bundle;
    }

    private Bundle getDiagmonSupportV1VersionCodeBundle() {
        Bundle bundle = new Bundle();
        try {
            bundle.putInt("diagmonSupportV1VersionCode", BuildConfig.class.getDeclaredField("VERSION_CODE").getInt(null));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e2) {
        } catch (IllegalArgumentException e3) {
        }
        return bundle;
    }

    protected Bundle makeLogListBundle(List<String> logList) {
        Bundle logListBundle = new Bundle();
        for (String log : logList) {
            String log2;
            try {
                log2 = new File(log2).getCanonicalPath();
            } catch (IOException e) {
            }
            logListBundle.putParcelable(log2, new Builder().scheme("content").authority(getAuthority()).path(log2).build());
        }
        return logListBundle;
    }

    protected void enforceSelfOrSystem() {
    }

    public Bundle call(String method, String arg, Bundle extras) {
        enforceSelfOrSystem();
        if ("clear".equals(method)) {
            return clear();
        }
        if ("set".equals(method)) {
            return set(arg, extras);
        }
        if ("get".equals(method) && !contains(arg) && data.getBundle(arg) != null) {
            return data.getBundle(arg);
        }
        if ("get".equals(method)) {
            return get(arg);
        }
        return super.call(method, arg, extras);
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        enforceSelfOrSystem();
        String path = uri.getPath();
        if (data.getBundle("logList") == null || data.getBundle("plainLogList") == null) {
            throw new RuntimeException("Data is corrupted");
        } else if (data.getBundle("logList").containsKey(path) || data.getBundle("plainLogList").containsKey(path)) {
            return openParcelFileDescriptor(path);
        } else {
            throw new FileNotFoundException();
        }
    }

    protected ParcelFileDescriptor openParcelFileDescriptor(String path) throws FileNotFoundException {
        return ParcelFileDescriptor.open(new File(path), 268435456);
    }

    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new RuntimeException("Operation not supported");
    }

    public String getType(Uri arg0) {
        enforceSelfOrSystem();
        return "text/plain";
    }

    public Uri insert(Uri arg0, ContentValues arg1) {
        throw new RuntimeException("Operation not supported");
    }

    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        throw new RuntimeException("Operation not supported");
    }

    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        throw new RuntimeException("Operation not supported");
    }

    protected SharedPreferences getDiagMonSharedPreferences() {
        return getContext().getSharedPreferences("diagmon_preferences", 0);
    }

    protected Bundle clear() {
        Editor edit = getDiagMonSharedPreferences().edit();
        edit.clear();
        edit.apply();
        return Bundle.EMPTY;
    }

    protected Bundle set(String key, Bundle extras) {
        Editor edit = getDiagMonSharedPreferences().edit();
        Object value = extras.get(key);
        if (value instanceof Boolean) {
            edit.putBoolean(key, ((Boolean) value).booleanValue());
        }
        if (value instanceof Float) {
            edit.putFloat(key, ((Float) value).floatValue());
        }
        if (value instanceof Integer) {
            edit.putInt(key, ((Integer) value).intValue());
        }
        if (value instanceof Long) {
            edit.putLong(key, ((Long) value).longValue());
        }
        if (value instanceof String) {
            edit.putString(key, (String) value);
        }
        edit.apply();
        return Bundle.EMPTY;
    }

    protected boolean contains(String key) {
        return getDiagMonSharedPreferences().contains(key);
    }

    protected Bundle get(String key) {
        SharedPreferences sharedPreferences = getDiagMonSharedPreferences();
        Bundle retval = new Bundle();
        try {
            retval.putBoolean(key, sharedPreferences.getBoolean(key, false));
        } catch (ClassCastException e) {
        }
        try {
            retval.putFloat(key, sharedPreferences.getFloat(key, 0.0f));
        } catch (ClassCastException e2) {
        }
        try {
            retval.putInt(key, sharedPreferences.getInt(key, 0));
        } catch (ClassCastException e3) {
        }
        try {
            retval.putLong(key, sharedPreferences.getLong(key, 0));
        } catch (ClassCastException e4) {
        }
        try {
            retval.putString(key, sharedPreferences.getString(key, null));
        } catch (ClassCastException e5) {
        }
        return retval;
    }
}
