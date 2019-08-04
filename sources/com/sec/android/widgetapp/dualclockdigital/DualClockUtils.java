package com.sec.android.widgetapp.dualclockdigital;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteFullException;
import android.widget.Toast;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.SharedManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import java.util.Locale;

public class DualClockUtils {
    static void changeCity(Context context, int id, int widgetId, int uniqueId) {
        Cursor c = context.getContentResolver().query(DualClockDigital.DATA_URI, null, "homezone > 0 and wid = " + widgetId, null, "homezone asc");
        if (c != null) {
            try {
                if (c.getCount() == 1) {
                    c.moveToPosition(0);
                    if (id == 1) {
                        uniqueId = c.getInt(5);
                    } else {
                        uniqueId = -1;
                    }
                } else {
                    c.moveToPosition(id - 1);
                    uniqueId = c.getInt(5);
                }
                c.close();
            } catch (CursorIndexOutOfBoundsException ce) {
                Log.secE("DualClockUtils", "CursorIndexOutOfBoundsException" + ce);
                if (c.getCount() > 0) {
                    c.moveToPosition(0);
                    uniqueId = c.getInt(5);
                }
                c.close();
            } catch (Exception e) {
                Log.secE("DualClockUtils", "Exception" + e);
                c.close();
            } catch (Throwable th) {
                c.close();
            }
            String cityName = CityManager.findCityCountryNameByUniqueId(Integer.valueOf(uniqueId));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setComponent(new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain"));
            intent.setFlags(270532608);
            intent.putExtra("where", "menu_dualclock_widget");
            intent.putExtra("homezone", id);
            intent.putExtra("cityname", cityName);
            intent.putExtra("uniqueid", uniqueId);
            intent.putExtra("wid", widgetId);
            Log.secD("DualClockUtils", "changeCity() ->  cityName : " + cityName + " uniqueId : " + uniqueId);
            intent.addFlags(32768);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                try {
                    ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCityListActivity");
                    try {
                        intent.setComponent(cn);
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e3) {
                        ComponentName componentName = cn;
                        Toast.makeText(context, String.format(context.getResources().getString(R.string.need_samsung_apps), new Object[]{context.getResources().getString(R.string.app_name)}), 0).show();
                    }
                } catch (ActivityNotFoundException e4) {
                    Toast.makeText(context, String.format(context.getResources().getString(R.string.need_samsung_apps), new Object[]{context.getResources().getString(R.string.app_name)}), 0).show();
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void saveDBCityCountry(android.content.Context r12, int r13, int r14, int r15) {
        /*
        r2 = 0;
        if (r13 != 0) goto L_0x0008;
    L_0x0003:
        if (r14 != 0) goto L_0x0008;
    L_0x0005:
        if (r15 != 0) goto L_0x0008;
    L_0x0007:
        return;
    L_0x0008:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r4 = "homezone = ";
        r1 = r1.append(r4);
        r4 = java.lang.String.valueOf(r13);
        r1 = r1.append(r4);
        r4 = " and wid = ";
        r1 = r1.append(r4);
        r1 = r1.append(r14);
        r3 = r1.toString();
        r1 = java.lang.Integer.valueOf(r15);
        r7 = com.sec.android.app.clockpackage.worldclock.model.CityManager.findCityByUniqueId(r1);
        r0 = r12.getContentResolver();
        if (r7 == 0) goto L_0x0007;
    L_0x0037:
        if (r0 == 0) goto L_0x0007;
    L_0x0039:
        r1 = com.sec.android.widgetapp.dualclockdigital.DualClockDigital.DATA_URI;
        r5 = "homezone asc";
        r4 = r2;
        r6 = r0.query(r1, r2, r3, r4, r5);
        if (r6 == 0) goto L_0x0007;
    L_0x0044:
        r8 = getContentValues(r7, r13, r14);	 Catch:{ SQLiteFullException -> 0x0060, Exception -> 0x0074 }
        if (r8 == 0) goto L_0x0055;
    L_0x004a:
        r9 = r6.getCount();	 Catch:{ SQLiteFullException -> 0x0060, Exception -> 0x0074 }
        if (r9 != 0) goto L_0x0059;
    L_0x0050:
        r1 = com.sec.android.widgetapp.dualclockdigital.DualClockDigital.DATA_URI;	 Catch:{ SQLiteFullException -> 0x0060, Exception -> 0x0074 }
        r0.insert(r1, r8);	 Catch:{ SQLiteFullException -> 0x0060, Exception -> 0x0074 }
    L_0x0055:
        r6.close();
        goto L_0x0007;
    L_0x0059:
        r1 = com.sec.android.widgetapp.dualclockdigital.DualClockDigital.DATA_URI;	 Catch:{ SQLiteFullException -> 0x0060, Exception -> 0x0074 }
        r2 = 0;
        r0.update(r1, r8, r3, r2);	 Catch:{ SQLiteFullException -> 0x0060, Exception -> 0x0074 }
        goto L_0x0055;
    L_0x0060:
        r11 = move-exception;
        r1 = 2131361993; // 0x7f0a00c9 float:1.8343754E38 double:1.0530327396E-314;
        r1 = r12.getString(r1);	 Catch:{ all -> 0x0092 }
        r2 = 0;
        r1 = android.widget.Toast.makeText(r12, r1, r2);	 Catch:{ all -> 0x0092 }
        r1.show();	 Catch:{ all -> 0x0092 }
        r6.close();
        goto L_0x0007;
    L_0x0074:
        r10 = move-exception;
        r1 = "DualClockUtils";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0092 }
        r2.<init>();	 Catch:{ all -> 0x0092 }
        r4 = "Exception";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0092 }
        r2 = r2.append(r10);	 Catch:{ all -> 0x0092 }
        r2 = r2.toString();	 Catch:{ all -> 0x0092 }
        com.sec.android.app.clockpackage.common.util.Log.secE(r1, r2);	 Catch:{ all -> 0x0092 }
        r6.close();
        goto L_0x0007;
    L_0x0092:
        r1 = move-exception;
        r6.close();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.widgetapp.dualclockdigital.DualClockUtils.saveDBCityCountry(android.content.Context, int, int, int):void");
    }

    static void restoreWidget(Context context, int oldWidgetId, int newWidgetId) {
        Log.secD("DualClockUtils", "restoreWidget oldWidgetId : " + oldWidgetId + " newWidgetId : " + newWidgetId);
        if (oldWidgetId == -1 || newWidgetId == -1) {
            Log.secE("DualClockUtils", "Widget id is wrong");
            return;
        }
        int widgetId = newWidgetId;
        for (int i = 0; i < 2; i++) {
            int homeZone = i + 1;
            String key = Integer.toString(oldWidgetId) + "_" + Integer.toString(homeZone);
            Log.secD("DualClockUtils", "key : " + key);
            int cityUnqId = SharedManager.getRestoredData(context, key);
            if (cityUnqId != -1) {
                City city = CityManager.findCityByUniqueId(Integer.valueOf(cityUnqId));
                String whereClause = "homezone = " + String.valueOf(homeZone) + " and wid = " + widgetId;
                Cursor cursor = context.getContentResolver().query(DualClockDigital.DATA_URI, null, whereClause, null, "homezone asc");
                if (cursor != null) {
                    if (city != null) {
                        try {
                            int cursorCount = cursor.getCount();
                            Log.secD("DualClockUtils", "whereClause : " + whereClause + " cursorCount : " + cursorCount);
                            ContentValues contentValues = getContentValues(city, homeZone, widgetId);
                            if (cursorCount == 0) {
                                if (context.getContentResolver().insert(DualClockDigital.DATA_URI, contentValues) != null) {
                                    Log.secD("DualClockUtils", "insert successful");
                                }
                            } else if (context.getContentResolver().update(DualClockDigital.DATA_URI, contentValues, whereClause, null) >= 0) {
                                Log.secD("DualClockUtils", "update successful");
                            }
                        } catch (SQLiteFullException e) {
                            Toast.makeText(context, context.getResources().getString(R.string.memory_full), 0).show();
                        } catch (Exception e2) {
                            Log.secE("DualClockUtils", "Exception" + e2);
                        } finally {
                            cursor.close();
                        }
                    }
                    cursor.close();
                } else {
                    return;
                }
            }
            Log.secE("DualClockUtils", key + " preference does not exist");
        }
    }

    static void updateDBCityCountry(Context context) {
        String systemLocale = Locale.getDefault().getDisplayName();
        String dbLocale = new SharedManager(context).getPrefDBLocale();
        Log.secD("DualClockUtils", "updateDBCityCountry dbLocale : " + dbLocale + " systemLocale : " + systemLocale);
        if (dbLocale == null || !systemLocale.equals(dbLocale)) {
            CityManager.initCity(context);
            WorldclockDBManager.updateDBLocale(context);
        }
        Cursor c = getDBAll(context);
        if (c == null) {
            Log.secE("DualClockUtils", "updateDBCityCountry cursor is null after getDBAll");
        } else if (c.getCount() == 0) {
            Log.secE("DualClockUtils", "updateDBCityCountry cursor count is 0");
            c.close();
        } else {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                try {
                    String whereClause = DualClockDigital.COLUMNS[0] + " = " + c.getString(0);
                    City city = CityManager.findCityByUniqueId(Integer.valueOf(c.getInt(5)));
                    if (city != null) {
                        context.getContentResolver().update(DualClockDigital.DATA_URI, getContentValues(city), whereClause, null);
                    }
                    c.moveToNext();
                } catch (Exception e) {
                    Log.secE("DualClockUtils", "Exception : " + e.toString());
                }
            }
            c.close();
        }
    }

    private static ContentValues getContentValues(City city, int whichCity, int widgetIds) {
        ContentValues c = new ContentValues();
        c.put(DualClockDigital.COLUMNS[1], city.getName());
        c.put(DualClockDigital.COLUMNS[2], Integer.valueOf(city.getOffsetMillis()));
        c.put(DualClockDigital.COLUMNS[3], Integer.valueOf(-1));
        c.put(DualClockDigital.COLUMNS[4], Integer.valueOf(whichCity));
        c.put(DualClockDigital.COLUMNS[5], Integer.valueOf(city.getUniqueId()));
        c.put(DualClockDigital.COLUMNS[6], Integer.valueOf(widgetIds));
        return c;
    }

    private static ContentValues getContentValues(City city) {
        ContentValues c = new ContentValues();
        c.put(DualClockDigital.COLUMNS[1], "" + city.getName());
        c.put(DualClockDigital.COLUMNS[5], "" + city.getUniqueId());
        return c;
    }

    private static Cursor getDBAll(Context context) {
        return context.getContentResolver().query(DualClockDigital.DATA_URI, null, null, null, null);
    }
}
