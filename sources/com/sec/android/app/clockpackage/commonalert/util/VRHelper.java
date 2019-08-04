package com.sec.android.app.clockpackage.commonalert.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.sec.android.app.clockpackage.common.util.Log;

public class VRHelper {

    public static final class Global implements BaseColumns {
        private static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://com.samsung.android.hmt.vrsvc.vr/global/");

        public static boolean getBoolean(ContentResolver contentResolver, String key, boolean defaultValue) {
            String result = getString(contentResolver, key, Boolean.toString(defaultValue));
            if (TextUtils.isEmpty(result)) {
                return defaultValue;
            }
            if (result.equals("true") || result.equals("false")) {
                return Boolean.parseBoolean(result);
            }
            Log.secE("VRHelper/Global", "IllegalState!! result=" + result);
            return defaultValue;
        }

        public static String getString(ContentResolver contentResolver, String key, String defaultValue) {
            Cursor c = null;
            try {
                ContentResolver contentResolver2 = contentResolver;
                c = contentResolver2.query(getUriFor(key), new String[]{"key", "value"}, null, null, "last_updated DESC LIMIT 1");
                if (c == null || !c.moveToFirst()) {
                    if (c != null) {
                        c.close();
                    }
                    return defaultValue;
                }
                defaultValue = c.getString(c.getColumnIndex("value"));
                if (c != null) {
                    c.close();
                }
                return defaultValue;
            } catch (Exception e) {
                Log.secE("VRHelper/Global", "key=" + key + " " + e.getMessage());
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }

        private static Uri getUriFor(String name) {
            return getUriFor(CONTENT_ID_URI_BASE, name);
        }

        private static Uri getUriFor(Uri uri, String name) {
            return Uri.withAppendedPath(uri, name);
        }
    }
}
