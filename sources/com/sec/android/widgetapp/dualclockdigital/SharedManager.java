package com.sec.android.widgetapp.dualclockdigital;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class SharedManager {
    static int[] getPrefIDs(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        int length = pref.getInt("DCWidgetID_Length", 0);
        int[] WidgetId = new int[length];
        for (int i = 0; i < length; i++) {
            WidgetId[i] = pref.getInt("DCWidgetIDs" + (i + 1), 0);
        }
        return WidgetId;
    }

    static void addWidgetIds(Context context, int id) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        Editor ed = pref.edit();
        int length = pref.getInt("DCWidgetID_Length", 0) + 1;
        if (id != pref.getInt("DCWidgetIDs" + (length - 1), 0)) {
            ed.putInt("DCWidgetID_Length", length);
            ed.putInt("DCWidgetIDs" + length, id);
            ed.apply();
        }
    }

    static void removeWidgetIds(Context context, int id) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        Editor ed = pref.edit();
        if (pref.contains("DCWidgetID_Length")) {
            int length = pref.getInt("DCWidgetID_Length", 0);
            int[] WidgetId = new int[length];
            int[] tmpWidgetId = new int[length];
            int widgetPosition = -1;
            int i = 0;
            int tmp = 0;
            while (i < length) {
                int tmp2;
                WidgetId[i] = pref.getInt("DCWidgetIDs" + (i + 1), 0);
                if (WidgetId[i] == id) {
                    widgetPosition = i;
                    tmp2 = tmp;
                } else {
                    tmp2 = tmp + 1;
                    tmpWidgetId[tmp] = WidgetId[i];
                }
                i++;
                tmp = tmp2;
            }
            if (widgetPosition != -1) {
                ed.putInt("DCWidgetID_Length", length - 1);
                for (i = 0; i < length - 1; i++) {
                    ed.putInt("DCWidgetIDs" + (i + 1), tmpWidgetId[i]);
                }
                ed.remove("DCWidgetIDs" + length);
                ed.apply();
            }
        }
    }

    static void removeAllWidgetIds(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        Editor ed = pref.edit();
        int length = pref.getInt("DCWidgetID_Length", 0);
        for (int i = 0; i < length; i++) {
            ed.remove("DCWidgetIDs" + (i + 1));
        }
        ed.remove("DCWidgetID_Length");
        ed.apply();
    }

    static void addRestoredData(Context context, String key, int uniqueId) {
        Editor ed = context.getSharedPreferences("DCWidgetIDs", 0).edit();
        if (key != null && uniqueId != -1) {
            ed.putInt(key, uniqueId);
            ed.apply();
        }
    }

    static int getRestoredData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        if (pref == null || key == null) {
            return -1;
        }
        return pref.getInt(key, -1);
    }

    static void removeRestoredData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        if (pref != null && key != null) {
            Editor ed = pref.edit();
            ed.remove(key);
            ed.apply();
        }
    }

    static void addRestoredTime(Context context) {
        Editor ed = context.getSharedPreferences("DCWidgetIDs", 0).edit();
        ed.putLong("DCWidgetRestoredTime", System.currentTimeMillis());
        ed.apply();
    }

    static void removeRestoredTime(Context context) {
        Editor ed = context.getSharedPreferences("DCWidgetIDs", 0).edit();
        ed.remove("DCWidgetRestoredTime");
        ed.apply();
    }

    static boolean hasInvalidRestoredData(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        if (pref == null) {
            return false;
        }
        long restoredTime = pref.getLong("DCWidgetRestoredTime", 0);
        long min = (((System.currentTimeMillis() - restoredTime) / 1000) % 3600) / 60;
        if (restoredTime <= 0 || min < 10) {
            return false;
        }
        return true;
    }

    static boolean hasValidRestoredData(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DCWidgetIDs", 0);
        if (pref == null) {
            return false;
        }
        long restoredTime = pref.getLong("DCWidgetRestoredTime", 0);
        long min = (((System.currentTimeMillis() - restoredTime) / 1000) % 3600) / 60;
        if (restoredTime <= 0 || min >= 10) {
            return false;
        }
        return true;
    }
}
