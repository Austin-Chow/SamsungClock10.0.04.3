package com.sec.android.app.clockpackage.worldclock.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteFullException;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class WorldclockDBManager {
    public static Cursor getDBAll(Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Worldclock.DATA_URI, null, null, null, null);
        } catch (SQLiteFullException e) {
            Log.secE("WorldclockDBManager", "Exception : " + e.toString());
        }
        return cursor;
    }

    private static Cursor getDBSelect(Context context, int index) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Worldclock.DATA_URI, new String[]{Worldclock.WC_COLUMNS[index]}, null, null, null);
        } catch (SQLiteFullException e) {
            Log.secE("WorldclockDBManager", "Exception : " + e.toString());
        }
        return cursor;
    }

    public static void updateCityChoice(Context context) {
        CityManager.cleanDBSelected();
        Cursor cur = getDBAll(context);
        if (cur != null) {
            if (cur.getCount() == 0) {
                cur.close();
                return;
            }
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                City city = CityManager.findCityByUniqueId(Integer.valueOf(cur.getInt(4)));
                if (city != null) {
                    city.setDBSelected(true);
                }
                cur.moveToNext();
            }
            cur.close();
        }
    }

    public static void updateDBLocale(Context context) {
        String systemLocale = Locale.getDefault().getDisplayName();
        SharedManager sm = new SharedManager(context);
        String dbLocale = sm.getPrefDBLocale();
        if (!systemLocale.equals(dbLocale)) {
            sm.setPrefDBLocale(systemLocale);
            if (dbLocale != null) {
                updateDBCityCountry(context);
            }
        }
    }

    public static void updateDBCityCountry(Context context) {
        Cursor cur = getDBAll(context);
        if (cur != null) {
            if (cur.getCount() == 0) {
                cur.close();
                return;
            }
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                String whereClause = Worldclock.WC_COLUMNS[4] + " = " + cur.getInt(4);
                City city = CityManager.findCityByUniqueId(Integer.valueOf(cur.getInt(4)));
                if (city != null) {
                    context.getContentResolver().update(Worldclock.DATA_URI, getContentValues(city.getName(), city.getCountry()), whereClause, null);
                }
                cur.moveToNext();
            }
            cur.close();
        }
    }

    public static int getDBCursorCnt(Context context) {
        Cursor dbCursor = getDBSelect(context, 0);
        if (dbCursor == null) {
            return 0;
        }
        int dbCnt = dbCursor.getCount();
        dbCursor.close();
        return dbCnt;
    }

    public static boolean saveDB(Context context, City city) {
        if (city == null) {
            return false;
        }
        try {
            if (context.getContentResolver().insert(Worldclock.DATA_URI, getContentValues(city)) != null) {
                return true;
            }
            return false;
        } catch (SQLiteFullException e) {
            Log.secE("WorldclockDBManager", "Exception : " + e.toString());
            return false;
        }
    }

    public static boolean saveDB(Context context, ArrayList<ListItem> mItems) {
        try {
            Iterator it = mItems.iterator();
            while (it.hasNext()) {
                if (context.getContentResolver().insert(Worldclock.DATA_URI, getContentValues((ListItem) it.next())) == null) {
                    return false;
                }
            }
            return true;
        } catch (SQLiteFullException e) {
            return false;
        }
    }

    public static boolean updateDB(Context context, City city, String where) {
        try {
            if (context.getContentResolver().update(Worldclock.DATA_URI, getContentValues(city), where, null) < 0) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            Log.secE("WorldclockDBManager", "Exception : " + e.toString());
            return false;
        }
    }

    public static boolean deleteDB(Context context) {
        try {
            context.getContentResolver().delete(Worldclock.DATA_URI, null, null);
            return true;
        } catch (SQLiteFullException e) {
            return false;
        }
    }

    public static boolean deleteDB(Context context, String where, String[] selectionArgs) {
        try {
            context.getContentResolver().delete(Worldclock.DATA_URI, where, selectionArgs);
            return true;
        } catch (SQLiteFullException e) {
            return false;
        }
    }

    public static boolean isDuplication(Context context, int uniqueID) {
        boolean b = false;
        Cursor cursor = getDBSelect(context, 4);
        if (cursor == null) {
            boolean b2 = false;
            return false;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (uniqueID == cursor.getInt(4)) {
                b = true;
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        b2 = b;
        return b;
    }

    private static ContentValues getContentValues(City city) {
        if (city == null) {
            return null;
        }
        ContentValues c = new ContentValues();
        c.put(Worldclock.WC_COLUMNS[1], city.getName() + (city.getCountry().length() > 0 ? " / " + city.getCountry() : ""));
        c.put(Worldclock.WC_COLUMNS[2], city.getTimeZoneId());
        c.put(Worldclock.WC_COLUMNS[3], Integer.valueOf(-1));
        c.put(Worldclock.WC_COLUMNS[4], Integer.valueOf(city.getUniqueId()));
        c.put(Worldclock.WC_COLUMNS[5], Float.valueOf(city.getLatitude()));
        c.put(Worldclock.WC_COLUMNS[6], Float.valueOf(city.getLongitude()));
        return c;
    }

    private static ContentValues getContentValues(String cityName, String cityCountry) {
        ContentValues c = new ContentValues();
        c.put(Worldclock.WC_COLUMNS[1], cityName + (cityCountry.length() > 0 ? " / " + cityCountry : ""));
        return c;
    }

    private static ContentValues getContentValues(ListItem item) {
        ContentValues c = new ContentValues();
        c.put(Worldclock.WC_COLUMNS[1], item.getTopLabel());
        c.put(Worldclock.WC_COLUMNS[2], item.getTimeZoneId());
        c.put(Worldclock.WC_COLUMNS[3], Integer.valueOf(-1));
        c.put(Worldclock.WC_COLUMNS[4], Integer.valueOf(item.getUniqueId()));
        c.put(Worldclock.WC_COLUMNS[5], Float.valueOf(item.getLatitude()));
        c.put(Worldclock.WC_COLUMNS[6], Float.valueOf(item.getLongitude()));
        return c;
    }

    public static ContentValues getContentValues(int uniqueId) {
        City city = CityManager.findCityByUniqueId(Integer.valueOf(uniqueId));
        Log.secD("WorldclockDBManager", "getContentValues() uniqueId : " + uniqueId);
        return getContentValues(city);
    }
}
