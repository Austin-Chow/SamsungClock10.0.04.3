package com.sec.android.widgetapp.dualclockdigital;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DualClockDigitalDataContentProvider extends ContentProvider {
    private static final UriMatcher MATCHER = new UriMatcher(-1);
    private SQLiteAdapter mDbAdapter;

    static {
        MATCHER.addURI("com.sec.android.provider.clockpackage.dualclockdigital", "HOMEZONE", 2);
    }

    public boolean onCreate() {
        this.mDbAdapter = new SQLiteAdapter(getContext(), "dualclock", 4);
        this.mDbAdapter.open();
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (MATCHER.match(uri)) {
            case 2:
                Cursor cursor = this.mDbAdapter.selectAll("dualclock", DualClockDigital.COLUMNS, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues values) {
        int wid = 0;
        Integer widInteger = Integer.valueOf(0);
        if (values != null) {
            widInteger = values.getAsInteger("wid");
        }
        if (widInteger != null) {
            wid = widInteger.intValue();
        }
        String orderBy = "homezone asc";
        String whereClause = "homezone > 0 and wid = " + wid;
        switch (MATCHER.match(uri)) {
            case 2:
                Cursor c = getContext().getContentResolver().query(DualClockDigital.DATA_URI, null, whereClause, null, orderBy);
                this.mDbAdapter.insert("dualclock", values);
                if (c != null) {
                    if (c.getCount() == 0) {
                        SharedManager.addWidgetIds(getContext(), wid);
                    }
                    c.close();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return uri;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case 2:
                int count = this.mDbAdapter.updateByWhere("dualclock", values, selection);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public int delete(Uri uri, String where, String[] whereArgs) {
        switch (MATCHER.match(uri)) {
            case 2:
                int count = this.mDbAdapter.delete("dualclock", where, whereArgs);
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
