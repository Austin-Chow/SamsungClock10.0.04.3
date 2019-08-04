package com.sec.android.app.clockpackage.timer.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteFullException;
import android.net.Uri;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.ArrayList;

public class TimerPresetItem {
    private int mHour;
    private long mId;
    private String mImage;
    private int mMinute;
    private String mName;
    private int mOrder;
    private int mSecond;
    private String mTime;

    public TimerPresetItem() {
        this.mId = -1;
    }

    public TimerPresetItem(Cursor c) {
        this.mId = c.getLong(0);
        this.mName = c.getString(1);
        this.mImage = c.getString(2);
        this.mOrder = c.getInt(4);
        setTime(c.getString(3));
    }

    public static ContentValues getContentValues(TimerPresetItem item) {
        ContentValues values = new ContentValues(5);
        if (item.mId != -1) {
            values.put("id", Long.valueOf(item.mId));
        }
        values.put("timername", item.getName());
        values.put("timerimagename", item.getImage());
        values.put("time", item.getTime());
        values.put("timerorder", Integer.valueOf(item.getOrder()));
        return values;
    }

    private static Uri getUri(long id) {
        return ContentUris.withAppendedId(TimerProvider.CONTENT_URI, id);
    }

    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }

    public static ArrayList<TimerPresetItem> getAllPreset(ContentResolver contentResolver, String selection, String... selectionArgs) {
        Cursor cursor = contentResolver.query(TimerProvider.CONTENT_URI, TimerProvider.QUERY_COLUMNS, selection, selectionArgs, null);
        ArrayList<TimerPresetItem> list = new ArrayList();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    list.add(new TimerPresetItem(cursor));
                } finally {
                    cursor.close();
                }
            }
        }
        return list;
    }

    public static TimerPresetItem addPreset(ContentResolver contentResolver, TimerPresetItem item) throws SQLiteFullException {
        Log.secD("TimerPresetItem", "addPreset() / item = " + item.toString());
        item.mImage = "default";
        item.mId = getId(contentResolver.insert(TimerProvider.CONTENT_URI, getContentValues(item)));
        return item;
    }

    public static boolean updatePreset(ContentResolver contentResolver, TimerPresetItem item) throws SQLiteFullException {
        Log.secD("TimerPresetItem", "updatePreset() / item = " + item.toString());
        if (item.mId == -1) {
            return false;
        }
        if (((long) contentResolver.update(getUri(item.mId), getContentValues(item), null, null)) == 1) {
            return true;
        }
        return false;
    }

    public static boolean deletePreset(ContentResolver contentResolver, ArrayList<Long> ids) throws SQLiteFullException {
        Log.secD("TimerPresetItem", "deletePreset() / ids = " + ids);
        int count = ids.size();
        if (count <= 0) {
            return false;
        }
        String[] selectionArgs = new String[count];
        StringBuilder where = new StringBuilder("id IN (");
        int i = 0;
        while (i < count) {
            if (((Long) ids.get(i)).longValue() != -1) {
                where.append(i < count + -1 ? "?," : "?)");
                selectionArgs[i] = ids.get(i) + "";
            }
            i++;
        }
        if (contentResolver.delete(TimerProvider.CONTENT_URI, where.toString(), selectionArgs) == count) {
            return true;
        }
        return false;
    }

    public static int getPresetCount(Context context) {
        Cursor cursor = context.getContentResolver().query(TimerProvider.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return 0;
        }
        int itemCount = cursor.getCount();
        cursor.close();
        return itemCount;
    }

    public String toString() {
        return "TimerPresetItem { mId = " + this.mId + ", mName = '" + this.mName + ", mImage = '" + this.mImage + ", mTime = '" + this.mTime + ", mOrder = " + this.mOrder + " }";
    }

    public long getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImage() {
        return this.mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getTime() {
        return this.mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
        if (this.mTime != null) {
            String[] sourceStrArray = this.mTime.split(":");
            try {
                this.mHour = Integer.parseInt(sourceStrArray[0]);
            } catch (NumberFormatException e) {
                this.mHour = 0;
            }
            try {
                this.mMinute = Integer.parseInt(sourceStrArray[1]);
            } catch (NumberFormatException e2) {
                this.mMinute = 0;
            }
            try {
                this.mSecond = Integer.parseInt(sourceStrArray[2]);
            } catch (NumberFormatException e3) {
                this.mSecond = 0;
            }
        }
    }

    public int getHour() {
        return this.mHour;
    }

    public int getMinute() {
        return this.mMinute;
    }

    public int getSecond() {
        return this.mSecond;
    }

    public String getDisplayTime(Context context) {
        String timeSeparator = ClockUtils.getTimeSeparatorText(context);
        if (this.mHour == 0) {
            return ClockUtils.toTwoDigitString(this.mMinute) + timeSeparator + ClockUtils.toTwoDigitString(this.mSecond);
        }
        return ClockUtils.toTwoDigitString(this.mHour) + timeSeparator + ClockUtils.toTwoDigitString(this.mMinute) + timeSeparator + ClockUtils.toTwoDigitString(this.mSecond);
    }

    public void setTime(int hour, int minute, int second) {
        this.mHour = hour;
        this.mMinute = minute;
        this.mSecond = second;
        this.mTime = ClockUtils.toTwoDigitString(this.mHour) + ":" + ClockUtils.toTwoDigitString(this.mMinute) + ":" + ClockUtils.toTwoDigitString(this.mSecond);
    }

    public int getOrder() {
        return this.mOrder;
    }

    public void setOrder(int order) {
        this.mOrder = order;
    }
}
