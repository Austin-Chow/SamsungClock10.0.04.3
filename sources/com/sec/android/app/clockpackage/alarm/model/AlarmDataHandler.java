package com.sec.android.app.clockpackage.alarm.model;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class AlarmDataHandler {

    private static class SetAlarmActiveTask extends AsyncTask<Object, Long, Boolean> {
        Context mContext;
        AlarmItem mItem;

        private SetAlarmActiveTask() {
        }

        protected Boolean doInBackground(Object[] objects) {
            this.mContext = (Context) objects[0];
            int id = ((Integer) objects[1]).intValue();
            boolean willChangeButtonActive = ((Boolean) objects[2]).booleanValue();
            int curActive = ((Integer) objects[3]).intValue();
            this.mItem = AlarmProvider.getAlarm(this.mContext, id);
            if (this.mItem == null) {
                return Boolean.valueOf(false);
            }
            if (willChangeButtonActive) {
                this.mItem.mActivate = 1;
                if (this.mItem.isSpecificDate()) {
                    this.mItem.mSnoozeDoneCount = 0;
                    if (System.currentTimeMillis() > this.mItem.mAlarmAlertTime) {
                        this.mItem.setSpecificDate(false);
                    }
                }
                if (!this.mItem.isSpecificDate()) {
                    if (this.mItem.isOneTimeAlarm()) {
                        this.mItem.mSnoozeDoneCount = 0;
                        Calendar c = Calendar.getInstance();
                        int curHour = c.get(11);
                        int curMinute = c.get(12);
                        int curDay = c.get(7);
                        if ((curHour * 100) + curMinute >= this.mItem.mAlarmTime) {
                            Log.secD("AlarmDataHandler", "saveAlarmRepeat() - (curHour * 100 + (mHour * 100 + mMin) )");
                            c.add(6, 1);
                            curDay = c.get(7);
                            Log.secD("AlarmDataHandler", "curDay = " + curDay);
                        }
                        AlarmItem alarmItem = this.mItem;
                        alarmItem.mRepeatType &= 15;
                        alarmItem = this.mItem;
                        alarmItem.mRepeatType |= (1 << (((7 - curDay) + 1) * 4)) & -16;
                        int checkDay = this.mItem.getAlarmRepeat();
                        Log.secD("AlarmDataHandler", "saveAlarmRepeat() : checkDay = 0x" + Integer.toHexString(checkDay));
                        int result = 0 | ((checkDay << 4) & -16);
                        Log.secD("AlarmDataHandler", "result = 0x" + Integer.toHexString(result));
                        this.mItem.mRepeatType = result | 1;
                        Log.secD("AlarmDataHandler", "mItem.mRepeatType = 0x" + Integer.toHexString(this.mItem.mRepeatType));
                    } else {
                        this.mItem.mSnoozeDoneCount = 0;
                    }
                    long oldCreationTime = this.mItem.getCreateTime();
                    this.mItem.setCreateTime();
                    this.mItem.calculateOriginalAlertTime();
                    this.mItem.calculateFirstAlertTime(this.mContext);
                    this.mItem.setCreateTime(oldCreationTime);
                }
            } else if (curActive != 2) {
                this.mItem.mActivate = 0;
            } else if (!this.mItem.isOneTimeAlarm()) {
                this.mItem.mActivate = 0;
                this.mItem.mSnoozeDoneCount = 0;
                this.mItem.calculateOriginalAlertTime();
                this.mItem.calculateFirstAlertTime(this.mContext);
            } else if (this.mItem.getAlertDayCount() == 1) {
                this.mItem.mActivate = 0;
                this.mItem.mSnoozeDoneCount = 0;
                this.mItem.mAlarmAlertTime = -1;
                if (this.mItem.isSpecificDate()) {
                    this.mItem.setSpecificDate(false);
                }
            } else {
                this.mItem.clearRepeatDay(Calendar.getInstance());
                this.mItem.calculateOriginalAlertTime();
                this.mItem.mActivate = 0;
                this.mItem.calculateFirstAlertTime(this.mContext);
                this.mItem.mSnoozeDoneCount = 0;
            }
            Log.secD("AlarmDataHandler", "setAlarmActive to " + willChangeButtonActive);
            this.mContext.getContentResolver().update(AlarmProvider.CONTENT_URI, this.mItem.getContentValues(), "_id = " + this.mItem.mId, null);
            AlarmProvider.enableNextAlert(this.mContext);
            AlarmUtil.sendStopAlertByChangeIntent(this.mContext, this.mItem.mId);
            return Boolean.valueOf(willChangeButtonActive);
        }

        protected void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                AlarmUtil.saveMsg(this.mContext, this.mItem, null, Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            }
        }
    }

    public static Cursor getAlarmData(Context context) {
        return context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, null, null, "alarmtime ASC , alerttime ASC");
    }

    public static boolean deleteAlarm(Context context, int id) {
        Log.secD("AlarmDataHandler", "deleteAlarm id = " + id);
        int deleteCount = context.getContentResolver().delete(AlarmProvider.CONTENT_URI, "_id = " + id, null);
        if (deleteCount > 0) {
            AlarmProvider.enableNextAlert(context);
            AlarmUtil.sendStopAlertByChangeIntent(context, id);
        }
        return deleteCount > 0;
    }

    public static synchronized boolean deleteAlarms(Context context, ArrayList<Integer> alarmIds) {
        boolean z;
        synchronized (AlarmDataHandler.class) {
            int ret = -1;
            if (alarmIds != null) {
                if (alarmIds.size() != 0) {
                    StringBuffer sb = new StringBuffer("_id");
                    sb.append(" IN (");
                    Iterator it = alarmIds.iterator();
                    while (it.hasNext()) {
                        int alarmId = ((Integer) it.next()).intValue();
                        sb.append('\'').append(alarmId).append('\'').append(',');
                        AlarmUtil.sendStopAlertByChangeIntent(context, alarmId);
                    }
                    sb.setCharAt(sb.length() - 1, ')');
                    try {
                        ret = context.getContentResolver().delete(AlarmProvider.CONTENT_URI, sb.toString(), null);
                    } catch (Exception e) {
                        Log.secE("AlarmDataHandler", "Exception : " + e.toString());
                    }
                    if (ret != alarmIds.size()) {
                        Log.m41d("AlarmDataHandler", "deleteAlarms : Not matching delete count : " + ret + " != " + alarmIds.size());
                    }
                    if (ret >= 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                }
            }
            z = false;
        }
        return z;
    }

    public static void deleteAllAlarms(Context context, ArrayList<Integer> alarmIds) {
        Iterator it = alarmIds.iterator();
        while (it.hasNext()) {
            int alarmId = ((Integer) it.next()).intValue();
            Log.secD("AlarmDataHandler", "deleteAllAlarms alarmId = " + alarmId);
            AlarmUtil.sendStopAlertByChangeIntent(context, alarmId);
        }
        int deleteCount = context.getContentResolver().delete(AlarmProvider.CONTENT_URI, null, null);
        if (deleteCount > 0) {
            Log.secD("AlarmDataHandler", "deleteAllAlarms deleteCount = " + deleteCount);
            AlarmProvider.enableNextAlert(context);
        }
    }

    public static ArrayList<Integer> updateDismissedAlarm(Context context, AlarmItem item) {
        ArrayList<Integer> removedAlarmId = new ArrayList();
        if (item.isOneTimeAlarm() && !item.isSpecificDate()) {
            long backupAlarmAlertTime = item.mAlarmAlertTime;
            removedAlarmId = removeSpecificDateAlarm(context, item);
            item.mAlarmAlertTime = backupAlarmAlertTime;
        }
        if (item.mSnoozeActivate) {
            item.updateDismissedState(context);
            context.getContentResolver().update(AlarmProvider.CONTENT_URI, item.getContentValues(), "_id = " + item.mId, null);
            AlarmProvider.enableNextAlert(context);
        }
        return removedAlarmId;
    }

    private static ArrayList<Integer> removeSpecificDateAlarm(Context context, AlarmItem item) {
        ArrayList<Integer> changedAlarmIds = new ArrayList();
        Calendar c = Calendar.getInstance();
        if ((c.get(11) * 100) + c.get(12) >= item.mAlarmTime) {
            Log.secD("AlarmDataHandler", "(curHour * 100 + curMinute >= item.mAlarmTime)");
            c.add(6, 1);
        }
        c.set(11, item.mAlarmTime / 100);
        c.set(12, item.mAlarmTime % 100);
        c.set(13, 0);
        c.set(14, 0);
        item.mAlarmAlertTime = c.getTimeInMillis();
        Log.secD("AlarmDataHandler", "removeSpecificDateAlarm item.mAlarmAlertTime = " + c.getTime().toString());
        Log.secD("AlarmDataHandler", "mId = " + item.mId + ", mAlarmTime = " + item.mAlarmTime + ", mAlarmAlertTime = " + item.mAlarmAlertTime + ", mRepeatType = 0x" + Integer.toHexString(item.mRepeatType) + ", mDailyBriefing = 0b" + Integer.toBinaryString(item.mDailyBriefing) + ", mAlarmName = " + item.mAlarmName);
        Cursor cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, "active = 0 AND alerttime = " + item.mAlarmAlertTime + " AND " + "alarmtime" + " = " + item.mAlarmTime + " AND " + "name" + "= '" + item.mAlarmName.replace("'", "''") + '\'', null, "createtime DESC");
        if (cursor != null) {
            int cursorItemCnt = cursor.getCount();
            Log.secD("AlarmDataHandler", "removeSpecificDateAlarm cursorItemCnt = " + cursorItemCnt);
            if (cursorItemCnt <= 0) {
                cursor.close();
            }
            cursor.moveToFirst();
            for (int i = 0; i < cursorItemCnt; i++) {
                int cursorID = cursor.getInt(0);
                int cursorRepeatType = cursor.getInt(5);
                if (item.mId == cursorID || AlarmItem.isWeeklyAlarm(cursorRepeatType)) {
                    cursor.moveToNext();
                } else {
                    if (AlarmItem.isSpecificDate(cursor.getInt(11))) {
                        Log.secD("AlarmDataHandler", "checkDuplicationAlarmToRemoveSpecificDateAlarm cursorID = " + cursorID);
                        deleteAlarm(context, cursorID);
                        changedAlarmIds.add(Integer.valueOf(cursorID));
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return changedAlarmIds;
    }

    public static ArrayList<Integer> getAllAlarmIds(Context context) {
        ArrayList<Integer> alarmIds = new ArrayList();
        Cursor cursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, new String[]{"_id"}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count <= 0) {
                cursor.close();
            } else {
                for (int i = 0; i < count; i++) {
                    alarmIds.add(Integer.valueOf(cursor.getInt(0)));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return alarmIds;
    }

    public static void setAlarmActive(Context context, int id, boolean willChangeButtonActive, int activeNow) {
        Log.secD("AlarmDataHandler", "setAlarmActive() - id: " + id + ",willChangeButtonActive: " + willChangeButtonActive + ",activeNow: " + activeNow);
        new SetAlarmActiveTask().execute(new Object[]{context, Integer.valueOf(id), Boolean.valueOf(willChangeButtonActive), Integer.valueOf(activeNow)});
    }
}
