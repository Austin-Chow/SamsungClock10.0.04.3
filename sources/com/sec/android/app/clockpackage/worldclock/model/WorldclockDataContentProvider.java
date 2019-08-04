package com.sec.android.app.clockpackage.worldclock.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteFullException;
import android.net.Uri;
import com.sec.android.app.clockpackage.common.util.Log;

public class WorldclockDataContentProvider extends ContentProvider {
    private static final UriMatcher MATCHER = new UriMatcher(-1);
    private SQLiteAdapter mDbAdapter;

    static {
        MATCHER.addURI("com.sec.android.provider.stri_s1_worldclock", "HOMEZONE", 2);
    }

    public boolean onCreate() {
        this.mDbAdapter = new SQLiteAdapter(getContext(), "worldclock.db");
        this.mDbAdapter.open();
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (MATCHER.match(uri)) {
            case 2:
                Cursor cursor = this.mDbAdapter.selectAll("worldclock", Worldclock.WC_COLUMNS, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues values) {
        switch (MATCHER.match(uri)) {
            case 2:
                try {
                    this.mDbAdapter.insert("worldclock", values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return uri;
                } catch (SQLiteFullException e) {
                    Log.secE("WorldclockDataContentProvider", "Exception : " + e.toString());
                    return null;
                }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case 2:
                try {
                    int count = this.mDbAdapter.updateByWhere("worldclock", values, selection);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return count;
                } catch (SQLiteFullException e) {
                    Log.secE("WorldclockDataContentProvider", "Exception : " + e.toString());
                    return -1;
                }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public int delete(Uri uri, String where, String[] whereArgs) {
        switch (MATCHER.match(uri)) {
            case 2:
                int count = this.mDbAdapter.delete("worldclock", where, whereArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public String getType(Uri uri) {
        return null;
    }
}
