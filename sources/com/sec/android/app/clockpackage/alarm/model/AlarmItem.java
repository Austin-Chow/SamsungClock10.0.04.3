package com.sec.android.app.clockpackage.alarm.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Calendar;
import java.util.TimeZone;

public final class AlarmItem implements Cloneable {
    public int mActivate = 0;
    public long mAlarmAlertTime = -1;
    public String mAlarmName = "";
    public int mAlarmSoundTone = 0;
    public int mAlarmSoundType = 2;
    public int mAlarmTime = -1;
    public int mAlarmVolume = 11;
    public String mCelebVoicePath = "";
    public long mCreateTime = -1;
    public int mDailyBriefing = 0;
    public int mId = -1;
    public int mNotificationType = 0;
    public int mRepeatType = 0;
    public boolean mSnoozeActivate = true;
    public int mSnoozeDoneCount = 0;
    public int mSnoozeDuration = 1;
    public int mSnoozeRepeat = 2;
    public String mSoundUri = "";
    private boolean mSubdueActivate = false;
    private int mSubdueDuration = 1;
    private int mSubdueTone = 0;
    private int mSubdueUri = 0;
    public int mVibrationPattern = 50035;
    public String mWeatherMusicPath = "";

    public void writeToParcel(Parcel dest) {
        int i = 1;
        dest.writeInt(this.mId);
        dest.writeInt(this.mActivate);
        dest.writeLong(this.mCreateTime);
        dest.writeLong(this.mAlarmAlertTime);
        dest.writeInt(this.mAlarmTime);
        dest.writeInt(this.mRepeatType);
        dest.writeInt(this.mNotificationType);
        dest.writeInt(this.mSnoozeActivate ? 1 : 0);
        dest.writeInt(this.mSnoozeDuration);
        dest.writeInt(this.mSnoozeRepeat);
        dest.writeInt(this.mSnoozeDoneCount);
        dest.writeInt(this.mDailyBriefing);
        if (!this.mSubdueActivate) {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.mSubdueDuration);
        dest.writeInt(this.mSubdueTone);
        dest.writeInt(this.mAlarmSoundType);
        dest.writeInt(this.mAlarmSoundTone);
        dest.writeInt(this.mAlarmVolume);
        dest.writeInt(this.mSubdueUri);
        dest.writeString(this.mSoundUri);
        dest.writeString(this.mAlarmName);
        dest.writeInt(this.mVibrationPattern);
        dest.writeString("");
        dest.writeString(this.mCelebVoicePath);
    }

    public void readFromIntent(Intent intent) {
        boolean z = true;
        if (intent != null) {
            byte[] alarmData = intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA");
            if (alarmData != null) {
                Parcel in = Parcel.obtain();
                in.unmarshall(alarmData, 0, alarmData.length);
                in.setDataPosition(0);
                this.mId = in.readInt();
                this.mActivate = in.readInt();
                this.mCreateTime = in.readLong();
                this.mAlarmAlertTime = in.readLong();
                this.mAlarmTime = in.readInt();
                this.mRepeatType = in.readInt();
                this.mNotificationType = in.readInt();
                this.mSnoozeActivate = in.readInt() == 1;
                this.mSnoozeDuration = in.readInt();
                this.mSnoozeRepeat = in.readInt();
                this.mSnoozeDoneCount = in.readInt();
                this.mDailyBriefing = in.readInt();
                if (in.readInt() != 1) {
                    z = false;
                }
                this.mSubdueActivate = z;
                this.mSubdueDuration = in.readInt();
                this.mSubdueTone = in.readInt();
                this.mAlarmSoundType = in.readInt();
                this.mAlarmSoundTone = in.readInt();
                this.mAlarmVolume = in.readInt();
                this.mSubdueUri = in.readInt();
                this.mSoundUri = in.readString();
                this.mAlarmName = in.readString();
                this.mVibrationPattern = in.readInt();
                this.mWeatherMusicPath = in.readString();
                this.mCelebVoicePath = in.readString();
                this.mWeatherMusicPath = "";
                if (this.mSoundUri == null) {
                    this.mSoundUri = "";
                }
                if (this.mCelebVoicePath == null) {
                    this.mCelebVoicePath = "";
                }
                in.recycle();
            }
        }
    }

    public ContentValues getContentValues() {
        int i = 1;
        ContentValues value = new ContentValues();
        value.put("active", Integer.valueOf(this.mActivate));
        value.put("createtime", Long.valueOf(this.mCreateTime));
        value.put("alerttime", Long.valueOf(this.mAlarmAlertTime));
        value.put("alarmtime", Integer.valueOf(this.mAlarmTime));
        value.put("repeattype", Integer.valueOf(this.mRepeatType));
        value.put("notitype", Integer.valueOf(0));
        value.put("snzactive", Integer.valueOf(this.mSnoozeActivate ? 1 : 0));
        value.put("snzduration", Integer.valueOf(this.mSnoozeDuration));
        value.put("snzrepeat", Integer.valueOf(this.mSnoozeRepeat));
        value.put("snzcount", Integer.valueOf(this.mSnoozeDoneCount));
        value.put("dailybrief", Integer.valueOf(this.mDailyBriefing));
        String str = "sbdactive";
        if (!this.mSubdueActivate) {
            i = 0;
        }
        value.put(str, Integer.valueOf(i));
        value.put("sbdduration", Integer.valueOf(this.mSubdueDuration));
        value.put("sbdtone", Integer.valueOf(this.mSubdueTone));
        value.put("alarmsound", Integer.valueOf(this.mAlarmSoundType));
        value.put("alarmtone", Integer.valueOf(this.mAlarmSoundTone));
        value.put("volume", Integer.valueOf(this.mAlarmVolume));
        value.put("sbduri", Integer.valueOf(this.mSubdueUri));
        value.put("alarmuri", this.mSoundUri == null ? "" : this.mSoundUri);
        value.put("name", this.mAlarmName == null ? "" : this.mAlarmName);
        value.put("vibrationpattern", Integer.valueOf(this.mVibrationPattern));
        value.put("locationtext", "");
        value.put("map", this.mCelebVoicePath == null ? "" : this.mCelebVoicePath);
        return value;
    }

    private long getNextAlertTime(Calendar c) {
        Log.secD("AlarmItem", "1 getNextAlertTime : " + c.getTime().toString());
        if (isSpecificDate() || !isOneTimeAlarm()) {
            c.add(6, AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType));
        } else {
            c.add(6, 1);
        }
        Log.secD("AlarmItem", "2 getNextAlertTime : " + c.getTime().toString());
        c.set(11, this.mAlarmTime / 100);
        c.set(12, this.mAlarmTime % 100);
        c.set(13, 0);
        c.set(14, 0);
        Log.secD("AlarmItem", "3 getNextAlertTime : " + c.getTime().toString());
        return c.getTimeInMillis();
    }

    public void calculateFirstAlertTime(Context context) {
        calculateFirstAlertTime(context, System.currentTimeMillis());
    }

    public void calculateFirstAlertTime(Context context, long baseTimeMillis) {
        Log.secD("AlarmItem", "calculateFirstAlertTime");
        if (isSpecificDate() && this.mAlarmAlertTime < System.currentTimeMillis()) {
            setSpecificDate(false);
            this.mActivate = 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(baseTimeMillis);
        Log.secD("AlarmItem", "calendar(baseTimeMillis)=" + baseTimeMillis + "/ system=" + System.currentTimeMillis() + "/alarmAlertT___=" + this.mAlarmAlertTime);
        Calendar cr = Calendar.getInstance();
        cr.setTimeInMillis(this.mAlarmAlertTime);
        cr.add(6, -1);
        this.mAlarmAlertTime = getNextAlertTime(cr);
        Log.secD("AlarmItem", "mActivate=" + this.mActivate + "/ baseTimeMillis=" + baseTimeMillis + "/ alarmAlertT___=" + this.mAlarmAlertTime);
        if (this.mAlarmAlertTime < baseTimeMillis) {
            c.setTimeInMillis(this.mAlarmAlertTime);
            this.mAlarmAlertTime = getNextAlertTime(c);
            if (this.mAlarmAlertTime < baseTimeMillis) {
                c.setTimeInMillis(baseTimeMillis);
                this.mAlarmAlertTime = getNextAlertTime(c);
            }
        }
        if (!isSpecificDate() && isOneTimeAlarm()) {
            Log.secD("AlarmItem", "if (!isSpecificDate() && isOneTimeAlarm()) {");
            Calendar cc = Calendar.getInstance();
            cc.setTimeInMillis(this.mAlarmAlertTime);
            int curDay = cc.get(7);
            this.mRepeatType &= 15;
            this.mRepeatType |= (1 << (((7 - curDay) + 1) * 4)) & -16;
            Log.secD("AlarmItem", "mRepeatType : 0x" + Integer.toHexString(this.mRepeatType));
        }
        updateNextAlertTime(context);
    }

    public void calculateOriginalAlertTime() {
        Calendar c = Calendar.getInstance();
        c.set(11, this.mAlarmTime / 100);
        c.set(12, this.mAlarmTime % 100);
        c.set(13, 0);
        c.set(14, 0);
        this.mAlarmAlertTime = c.getTimeInMillis();
    }

    public void calculateNextAlertTime() {
        calculateNextAlertTime(System.currentTimeMillis());
    }

    public void calculateNextAlertTime(long baseTimeMillis) {
        Log.secD("AlarmItem", "calculateNextAlertTime");
        switch (this.mActivate) {
            case 1:
            case 2:
                if (this.mSnoozeActivate) {
                    this.mSnoozeDoneCount++;
                    this.mAlarmAlertTime += (long) getSnoozeDurationMinutes();
                    break;
                }
                break;
        }
        calculateAlertTime(baseTimeMillis);
    }

    private void calculateAlertTime(long baseTimeMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(baseTimeMillis);
        switch (this.mActivate) {
            case 0:
                this.mAlarmAlertTime = -1;
                this.mSnoozeDoneCount = 0;
                return;
            case 1:
            case 2:
                int dayOffset;
                if (this.mSnoozeActivate) {
                    if (this.mSnoozeDoneCount > getSnoozeRepeatTimes() && AlarmItemUtil.CONTINUOUSLY != getSnoozeRepeatTimes()) {
                        if (isOneTimeAlarm()) {
                            if (isSpecificDate()) {
                                setSpecificDate(false);
                            }
                            if (getAlertDayCount() == 1) {
                                this.mSnoozeDoneCount = 0;
                                this.mActivate = 0;
                                this.mAlarmAlertTime = -1;
                                Log.secD("AlarmItem", "----------------- active = " + this.mActivate + "\n next alert : snooze active" + "\n all snooze had finished. clear alarm.");
                                return;
                            }
                            clearRepeatDay(c);
                        }
                        dayOffset = AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType);
                        this.mActivate = 1;
                        this.mAlarmAlertTime = getNextAlertTime(c);
                        this.mSnoozeDoneCount = 0;
                        while (baseTimeMillis > this.mAlarmAlertTime) {
                            c.setTimeInMillis(this.mAlarmAlertTime);
                            this.mAlarmAlertTime = getNextAlertTime(c);
                        }
                        Log.secD("AlarmItem", "----------------- active = " + this.mActivate + "\n next alert : snooze active" + "\n new alarm set as normal alarm with snooze on next (" + dayOffset + ") day");
                        return;
                    } else if (this.mActivate != 1) {
                        if (this.mAlarmAlertTime < baseTimeMillis) {
                            i = this.mSnoozeDoneCount;
                            while (i < getSnoozeRepeatTimes()) {
                                this.mSnoozeDoneCount++;
                                this.mAlarmAlertTime += (long) getSnoozeDurationMinutes();
                                if (this.mAlarmAlertTime <= baseTimeMillis) {
                                    i++;
                                }
                            }
                        }
                        if (this.mSnoozeDoneCount <= getSnoozeRepeatTimes() || AlarmItemUtil.CONTINUOUSLY == getSnoozeRepeatTimes()) {
                            Log.secD("AlarmItem", "----------------- active = " + this.mActivate + "\n next alert : snooze active" + "\n set next snooze.");
                            return;
                        }
                        if (isOneTimeAlarm()) {
                            if (isSpecificDate()) {
                                setSpecificDate(false);
                            }
                            if (getAlertDayCount() == 1) {
                                this.mSnoozeDoneCount = 0;
                                this.mAlarmAlertTime = -1;
                                this.mActivate = 0;
                                Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n snooze end. change as inactive alarm.");
                                return;
                            }
                            clearRepeatDay(c);
                        }
                        this.mSnoozeDoneCount = 0;
                        dayOffset = AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType);
                        this.mActivate = 1;
                        this.mAlarmAlertTime = getNextAlertTime(c);
                        while (baseTimeMillis > this.mAlarmAlertTime) {
                            c.setTimeInMillis(this.mAlarmAlertTime);
                            this.mAlarmAlertTime = getNextAlertTime(c);
                        }
                        Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n change to active. new alarm set as normal alarm with snooze on next (" + dayOffset + ") day");
                        return;
                    } else if (this.mAlarmAlertTime <= baseTimeMillis) {
                        i = this.mSnoozeDoneCount;
                        while (i < getSnoozeRepeatTimes()) {
                            this.mSnoozeDoneCount++;
                            this.mAlarmAlertTime += (long) getSnoozeDurationMinutes();
                            if (this.mAlarmAlertTime <= baseTimeMillis) {
                                i++;
                            } else if (this.mAlarmAlertTime <= baseTimeMillis) {
                                this.mActivate = 2;
                                Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n found next snooze.");
                                return;
                            } else {
                                if (isOneTimeAlarm()) {
                                    if (isSpecificDate()) {
                                        setSpecificDate(false);
                                    }
                                    if (getAlertDayCount() != 1) {
                                        this.mSnoozeDoneCount = 0;
                                        this.mAlarmAlertTime = -1;
                                        this.mActivate = 0;
                                        Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n alarm fired but no snooze can be alert.");
                                        return;
                                    }
                                    clearRepeatDay(c);
                                }
                                this.mSnoozeDoneCount = 0;
                                this.mActivate = 1;
                                dayOffset = AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType);
                                this.mAlarmAlertTime = getNextAlertTime(c);
                                while (baseTimeMillis > this.mAlarmAlertTime) {
                                    c.setTimeInMillis(this.mAlarmAlertTime);
                                    this.mAlarmAlertTime = getNextAlertTime(c);
                                }
                                Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n change to active. new alarm set as normal alarm with snooze on next (" + dayOffset + ") day");
                                return;
                            }
                        }
                        if (this.mAlarmAlertTime <= baseTimeMillis) {
                            if (isOneTimeAlarm()) {
                                if (isSpecificDate()) {
                                    setSpecificDate(false);
                                }
                                if (getAlertDayCount() != 1) {
                                    clearRepeatDay(c);
                                } else {
                                    this.mSnoozeDoneCount = 0;
                                    this.mAlarmAlertTime = -1;
                                    this.mActivate = 0;
                                    Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n alarm fired but no snooze can be alert.");
                                    return;
                                }
                            }
                            this.mSnoozeDoneCount = 0;
                            this.mActivate = 1;
                            dayOffset = AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType);
                            this.mAlarmAlertTime = getNextAlertTime(c);
                            while (baseTimeMillis > this.mAlarmAlertTime) {
                                c.setTimeInMillis(this.mAlarmAlertTime);
                                this.mAlarmAlertTime = getNextAlertTime(c);
                            }
                            Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n change to active. new alarm set as normal alarm with snooze on next (" + dayOffset + ") day");
                            return;
                        }
                        this.mActivate = 2;
                        Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n found next snooze.");
                        return;
                    } else if (this.mSnoozeDoneCount > 0) {
                        this.mActivate = 2;
                        Log.secD("AlarmItem", "----------------- ALARM_SNOOZE\n next alert : snooze active\n active alarm changed as snooze");
                        return;
                    } else {
                        Log.secD("AlarmItem", "----------------- ALARM_ACTIVE\n next alert : snooze active\n active alarm set");
                        return;
                    }
                } else if (baseTimeMillis > this.mAlarmAlertTime) {
                    if (isOneTimeAlarm()) {
                        if (isSpecificDate()) {
                            setSpecificDate(false);
                        }
                        if (getAlertDayCount() == 1) {
                            this.mSnoozeDoneCount = 0;
                            if (isSpecificDate()) {
                                Log.secD("AlarmItem", "It is date alarm mAlarmAlertTime = " + this.mAlarmAlertTime);
                            } else {
                                this.mAlarmAlertTime = -1;
                            }
                            this.mActivate = 0;
                            Log.secD("AlarmItem", "-----------------6 ALARM_INACTIVE\n next alert : snooze inactive\n alarm set as tomorrow.");
                            return;
                        }
                        clearRepeatDay(c);
                    }
                    this.mSnoozeDoneCount = 0;
                    dayOffset = AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType);
                    this.mActivate = 1;
                    this.mAlarmAlertTime = getNextAlertTime(c);
                    while (baseTimeMillis > this.mAlarmAlertTime) {
                        c.setTimeInMillis(this.mAlarmAlertTime);
                        this.mAlarmAlertTime = getNextAlertTime(c);
                    }
                    Log.secD("AlarmItem", "----------------- active = " + this.mActivate + "\n next alert : snooze inactive" + "\n new alarm set as normal alarm on next (" + dayOffset + ") day");
                    return;
                } else {
                    Log.secD("AlarmItem", "----------------- active = " + this.mActivate + "\n next alert : snooze inactive" + "\n valid alarm as one shot alert.");
                    return;
                }
            default:
                return;
        }
    }

    public static AlarmItem createItemFromCursor(Cursor c) {
        boolean z = true;
        AlarmItem item = new AlarmItem();
        item.mId = c.getInt(0);
        item.mActivate = c.getInt(1);
        item.mCreateTime = c.getLong(2);
        item.mAlarmAlertTime = c.getLong(3);
        item.mAlarmTime = c.getInt(4);
        item.mRepeatType = c.getInt(5);
        item.mNotificationType = c.getInt(6);
        item.mSnoozeActivate = c.getInt(7) == 1;
        item.mSnoozeDuration = c.getInt(8);
        item.mSnoozeRepeat = c.getInt(9);
        item.mSnoozeDoneCount = c.getInt(10);
        item.mDailyBriefing = c.getInt(11);
        if (c.getInt(12) != 1) {
            z = false;
        }
        item.mSubdueActivate = z;
        item.mSubdueDuration = c.getInt(13);
        item.mSubdueTone = c.getInt(14);
        item.mAlarmSoundType = c.getInt(15);
        item.mAlarmSoundTone = c.getInt(16);
        item.mAlarmVolume = c.getInt(17);
        item.mSubdueUri = c.getInt(18);
        item.mSoundUri = c.getString(19);
        item.mAlarmName = c.getString(20);
        item.mVibrationPattern = c.getInt(26);
        item.mWeatherMusicPath = c.getString(25);
        item.mCelebVoicePath = c.getString(25);
        item.mWeatherMusicPath = "";
        if (item.mSoundUri == null) {
            item.mSoundUri = "";
        }
        if (item.mAlarmName == null) {
            item.mAlarmName = "";
        }
        if (item.mCelebVoicePath == null) {
            item.mCelebVoicePath = "";
        }
        return item;
    }

    public static AlarmItem createItem(Context context, int hour, int minutes) {
        Log.secD("AlarmItem", "createItem() : hour : " + hour + ", minutes : " + minutes);
        AlarmItem item = new AlarmItem();
        item.setCreateTime();
        item.mAlarmTime = (hour * 100) + minutes;
        item.calculateOriginalAlertTime();
        item.mSoundUri = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(context).toString());
        item.mAlarmSoundTone = new AlarmRingtoneManager(context).getRingtoneIndex(item.mSoundUri);
        item.setOneTimeAlarm();
        item.calculateFirstAlertTime(context, item.mAlarmAlertTime);
        return item;
    }

    public final String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("mId : ").append(this.mId).append('\n');
        ret.append("mActivate : ").append(this.mActivate).append('\n');
        ret.append("createT : ").append(digitToAlphabetStr(AlarmItemUtil.getTimeString(this.mCreateTime))).append('\n');
        ret.append("AlertT_ : ").append(digitToAlphabetStr(AlarmItemUtil.getTimeString(this.mAlarmAlertTime))).append('\n');
        ret.append("mAlarmTime : ").append(digitToAlphabetStr(Integer.toString(this.mAlarmTime))).append('\n');
        ret.append("mRepeatType : ").append(this.mRepeatType).append(", 0x").append(Integer.toHexString(this.mRepeatType)).append('\n');
        ret.append("mSnoozeActivate : ").append(this.mSnoozeActivate).append('\n');
        ret.append("mSnoozeDuration : ").append(this.mSnoozeDuration).append("->").append(getSnoozeDuration()).append('\n');
        ret.append("mSnoozeRepeat : ").append(this.mSnoozeRepeat).append("->").append(getSnoozeRepeatTimes()).append('\n');
        ret.append("mSnoozeDoneCount : ").append(this.mSnoozeDoneCount).append('\n');
        ret.append("mDailyBriefing : 0b").append(Integer.toBinaryString(this.mDailyBriefing)).append('\n');
        ret.append("mAlarmSoundType : ").append(this.mAlarmSoundType).append('\n');
        ret.append("mAlarmSoundTone : ").append(this.mAlarmSoundTone).append('\n');
        ret.append("mAlarmVolume : ").append(this.mAlarmVolume).append('\n');
        if (Feature.DEBUG_ENG) {
            ret.append("mSoundUri : ").append(this.mSoundUri).append('\n');
            ret.append("mAlarmName : ").append(this.mAlarmName).append('\n');
        } else if (!(this.mSoundUri == null || "".equals(this.mSoundUri))) {
            Object substring;
            String HIGHLIGHT_OFFSET = "highlight_offset";
            StringBuilder append = ret.append("mSoundUri : ");
            String str = this.mSoundUri.contains("%2Finternal%2F") ? "in " : this.mSoundUri.contains("%2Fexternal%2F") ? "ex " : "NOT_ex ";
            append = append.append(str);
            if (this.mSoundUri.contains("highlight_offset")) {
                substring = this.mSoundUri.substring(this.mSoundUri.lastIndexOf("highlight_offset"));
            } else {
                substring = Character.valueOf(' ');
            }
            append.append(substring).append('\n');
        }
        ret.append("mVibrationPattern : ").append(this.mVibrationPattern).append('\n');
        ret.append("mCelebVoicePath : ").append(this.mCelebVoicePath).append('\n');
        return ret.toString();
    }

    public final String toStringShort() {
        String separateChar = "|";
        StringBuilder b = new StringBuilder();
        b.append(this.mId).append(separateChar);
        b.append(this.mActivate).append(separateChar);
        b.append(digitToAlphabetStr(AlarmItemUtil.getTimeString(this.mCreateTime))).append(separateChar);
        b.append(this.mAlarmAlertTime <= 0 ? Long.valueOf(this.mAlarmAlertTime) : digitToAlphabetStr(AlarmItemUtil.getTimeString(this.mAlarmAlertTime))).append(separateChar);
        b.append(digitToAlphabetStr(Integer.toString(this.mAlarmTime))).append(separateChar);
        b.append(Integer.toHexString(this.mRepeatType)).append(separateChar);
        b.append(this.mSnoozeActivate ? '1' : '0').append(separateChar);
        b.append(getSnoozeDuration()).append(separateChar);
        b.append(getSnoozeRepeatTimes()).append(separateChar);
        b.append(this.mSnoozeDoneCount).append(separateChar);
        b.append(Integer.toBinaryString(this.mDailyBriefing)).append(separateChar);
        b.append(this.mAlarmSoundType).append(separateChar);
        b.append(this.mAlarmSoundTone).append(separateChar);
        b.append(this.mAlarmVolume).append(separateChar);
        if (!(this.mSoundUri == null || "".equals(this.mSoundUri))) {
            String HIGHLIGHT_OFFSET = "highlight_offset";
            String str = this.mSoundUri.contains("%2Finternal%2F") ? "in" : this.mSoundUri.contains("%2Fexternal%2F") ? "ex" : "NOT_ex";
            b.append(str).append(this.mSoundUri.contains("highlight_offset") ? this.mSoundUri.substring(this.mSoundUri.lastIndexOf("highlight_offset")).replace("highlight_offset", "") : "").append(separateChar);
        }
        if (Feature.DEBUG_ENG) {
            b.append(this.mAlarmName).append(separateChar);
        }
        b.append(this.mVibrationPattern).append(separateChar);
        if (Feature.DEBUG_ENG) {
            b.append(this.mCelebVoicePath);
        } else {
            try {
                if (!(this.mCelebVoicePath == null || this.mCelebVoicePath.isEmpty())) {
                    String newCelebeVoicePath;
                    if (this.mCelebVoicePath.contains("/")) {
                        newCelebeVoicePath = this.mCelebVoicePath.substring(this.mCelebVoicePath.lastIndexOf("/") + 1);
                        b.append(newCelebeVoicePath);
                        Log.secD("AlarmItem", "mCelebVoicePath : " + this.mCelebVoicePath);
                        Log.secD("AlarmItem", "newCelebeVoicePath : " + newCelebeVoicePath);
                    } else if (this.mCelebVoicePath.contains(".")) {
                        newCelebeVoicePath = this.mCelebVoicePath.substring(this.mCelebVoicePath.lastIndexOf(".") + 1);
                        b.append(newCelebeVoicePath);
                        Log.secD("AlarmItem", "else mCelebVoicePath : " + this.mCelebVoicePath);
                        Log.secD("AlarmItem", "else newCelebeVoicePath : " + newCelebeVoicePath);
                    }
                }
            } catch (Exception e) {
                Log.secE("AlarmItem", "Exception = " + e.toString());
            }
        }
        return b.toString();
    }

    public static String digitToAlphabetStr(String oldStr) {
        if (Feature.DEBUG_ENG) {
            return oldStr;
        }
        String[] alphabet = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String newStr = oldStr.replaceAll(":", "##");
        for (int i = 0; i <= 9; i++) {
            newStr = newStr.replace(Integer.toString(i), alphabet[i]);
        }
        return newStr;
    }

    public void updateDismissedState(Context context) {
        Calendar c = Calendar.getInstance();
        if (isOneTimeAlarm()) {
            if (isSpecificDate()) {
                setSpecificDate(false);
            }
            if (getAlertDayCount() == 1) {
                this.mActivate = 0;
                calculateNextAlertTime();
                return;
            }
            clearRepeatDay(c);
            calculateOriginalAlertTime();
            this.mActivate = 1;
            calculateFirstAlertTime(context);
            this.mSnoozeDoneCount = 0;
            return;
        }
        calculateOriginalAlertTime();
        this.mActivate = 1;
        calculateFirstAlertTime(context);
        this.mSnoozeDoneCount = 0;
    }

    public int getAlertDayCount() {
        int nCount = 0;
        int operator = 1;
        for (int i = 0; i < 7; i++) {
            operator <<= 4;
            if ((this.mRepeatType & operator) > 0) {
                nCount++;
            }
        }
        Log.secD("AlarmItem", "nCount : " + nCount);
        return nCount;
    }

    public void clearRepeatDay(Calendar c) {
        int offset = c.get(7);
        int operator = 1;
        for (int i = 0; i <= 7 - offset; i++) {
            operator <<= 4;
        }
        Log.secD("AlarmItem", "offset:" + offset);
        Log.secD("AlarmItem", "repeat type : " + Integer.toHexString(this.mRepeatType));
        Log.secD("AlarmItem", "operator : " + Integer.toHexString(operator) + "\n~operator : " + Integer.toHexString(operator ^ -1));
        this.mRepeatType &= operator ^ -1;
        Log.secD("AlarmItem", "repeat type : " + Integer.toHexString(this.mRepeatType));
    }

    public boolean isFirstAlarm() {
        return this.mActivate == 1;
    }

    public boolean isOneTimeAlarm() {
        return (this.mRepeatType & 15) == 1;
    }

    public boolean isWeeklyAlarm() {
        return (this.mRepeatType & 15) == 5;
    }

    public static boolean isWeeklyAlarm(int repeatType) {
        return (repeatType & 15) == 5;
    }

    public boolean isSpecificDate() {
        Log.secD("AlarmItem", "isSpecificDate mDailyBriefing = [1] " + Integer.toBinaryString(this.mDailyBriefing));
        if ((this.mDailyBriefing & 1) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isSpecificDate(int dailyBriefing) {
        Log.secD("AlarmItem", "isSpecificDate dailyBriefing = [1] " + Integer.toBinaryString(dailyBriefing));
        if ((dailyBriefing & 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean isIncreasingVolume() {
        Log.secD("AlarmItem", "isIncreasingVolume mDailyBriefing = [2] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 2) == 2;
    }

    public boolean isHoliday() {
        Log.secD("AlarmItem", "isHoliday mDailyBriefing = [3] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 4) == 4;
    }

    public boolean isWorkingDay() {
        Log.secD("AlarmItem", "isWorkingDay mDailyBriefing = [4] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 8) == 8;
    }

    public boolean isSubstituteHoliday() {
        Log.secD("AlarmItem", "isSubstituteHoliday mDailyBriefing = [5] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 16) == 16;
    }

    public static boolean isSubstituteHoliday(int dailyBriefing) {
        Log.secD("AlarmItem", "isSubstituteHoliday mDailyBriefing = [5] " + Integer.toBinaryString(dailyBriefing));
        return (dailyBriefing & 16) == 16;
    }

    public boolean isVibrationAlarm() {
        Log.secD("AlarmItem", "isVibrationAlarm " + this.mAlarmSoundType);
        if (this.mAlarmSoundType == 2 || this.mAlarmSoundType == 1) {
            return true;
        }
        return false;
    }

    public boolean isSoundOnState() {
        boolean bSoundOnState = isMasterSoundOn() && this.mAlarmSoundTone != -2 && ((isNewBixbyOn() || isNewCelebOn() || isAlarmToneOn()) && this.mAlarmVolume > 0);
        Log.secD("AlarmItem", "isSoundOnState bSoundOnState = " + bSoundOnState);
        return bSoundOnState;
    }

    public boolean isPossibleTtsAlarm() {
        return isTtsAlarm() && isSoundOnState();
    }

    public boolean isPossibleBixbyBriefingAlarm() {
        boolean bPossibleBixbyBriefingAlarm = false;
        if (isMasterSoundOn() && isFirstAlarm() && (isNewBixbyOn() || isNewCelebOn())) {
            bPossibleBixbyBriefingAlarm = true;
        }
        Log.secD("AlarmItem", "isPossibleBixbyBriefingAlarm bPossibleBixbyBriefingAlarm = " + bPossibleBixbyBriefingAlarm);
        return bPossibleBixbyBriefingAlarm;
    }

    public boolean isTtsAlarm() {
        Log.secD("AlarmItem", "isTtsAlarm mDailyBriefing = [6] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 32) == 32;
    }

    public boolean isAlarmToneOn() {
        Log.secD("AlarmItem", "isAlarmToneOn() / mDailyBriefing[7] (ON = bit 0) = " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 64) != 64;
    }

    public static boolean isAlarmToneOn(int dailyBriefing) {
        boolean isSoundOn = (dailyBriefing & 64) != 64;
        Log.secD("AlarmItem", "isAlarmToneOn() / dailyBriefing[7] (ON = bit 0) = " + Integer.toBinaryString(dailyBriefing) + "/ isSoundOn = " + isSoundOn);
        return isSoundOn;
    }

    public boolean isMasterSoundOn() {
        Log.secD("AlarmItem", "isMasterSoundOn() / mDailyBriefing[14] (ON = bit 0) = " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 8192) != 8192;
    }

    public static boolean isMasterSoundOn(int dailyBriefing) {
        boolean isMasterSoundOn = (dailyBriefing & 8192) != 8192;
        Log.secD("AlarmItem", "isMasterSoundOn() / dailyBriefing[14] (ON = bit 0) = " + Integer.toBinaryString(dailyBriefing) + "/ isMasterSoundOn = " + isMasterSoundOn);
        return isMasterSoundOn;
    }

    public boolean isBixbyBriefingOn() {
        Log.secD("AlarmItem", "isBixbyBriefingOn() / mDailyBriefing = [8] (ON = bit 1) = " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 128) == 128;
    }

    public static boolean isBixbyBriefingOn(int dailyBriefing) {
        boolean isBixbyBriefingOn = (dailyBriefing & 128) == 128;
        Log.secD("AlarmItem", "isBixbyBriefingOn() / dailyBriefing = [8] (ON = bit 1) = " + Integer.toBinaryString(dailyBriefing) + "/ isBixbyBriefingOn() isBixbyBriefingOn =" + isBixbyBriefingOn);
        return isBixbyBriefingOn;
    }

    public boolean isBixbyVoiceOn() {
        Log.secD("AlarmItem", "isBixbyVoiceOn() / mDailyBriefing[12] (ON = bit0) = " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 2048) != 2048;
    }

    public static boolean isBixbyVoiceOn(int dailyBriefing) {
        boolean isBixbyVoiceOn = (dailyBriefing & 2048) != 2048;
        Log.secD("AlarmItem", "isBixbyVoiceOn()/mDailyBriefing[12] (ON = bit0)" + Integer.toBinaryString(dailyBriefing) + "/isBixbyVoiceOn() isBixbyVoiceOn=" + isBixbyVoiceOn);
        return isBixbyVoiceOn;
    }

    public boolean isBixbyCelebVoice() {
        Log.secD("AlarmItem", "isBixbyCelebVoice() mDailyBriefing = [13] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 4096) == 4096;
    }

    public static boolean isBixbyCelebVoice(int dailyBriefing) {
        boolean isBixbyCelebVoice = (dailyBriefing & 4096) == 4096;
        Log.secD("AlarmItem", "isBixbyCelebVoice()/mDailyBriefing[13] (ON = bit 1)" + Integer.toBinaryString(dailyBriefing) + "/isBixbyCelebVoice() isBixbyCelebVoice=" + isBixbyCelebVoice);
        return isBixbyCelebVoice;
    }

    public boolean isNewBixbyOn() {
        Log.secD("AlarmItem", "isNewBixbyOn() / mDailyBriefing = [15] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 16384) == 16384;
    }

    public static boolean isNewBixbyOn(int dailyBriefing) {
        boolean isNewBixbyOn = (dailyBriefing & 16384) == 16384;
        Log.secD("AlarmItem", "isNewBixbyOn() / dailyBriefing[15](0 = OFF, 1 = ON) = " + Integer.toBinaryString(dailyBriefing) + "/ isNewBixbyOn = " + isNewBixbyOn);
        return isNewBixbyOn;
    }

    public void setNewBixbyOn(boolean bNewBixbyOn) {
        if (isNewBixbyOn() != bNewBixbyOn) {
            this.mDailyBriefing ^= 16384;
        }
        Log.secD("AlarmItem", "setNewBixby() / bNewBixbyOn = " + bNewBixbyOn + " mDailyBriefing[15] = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    private static int setNewBixbyOn(int dailyBriefing, boolean bNewBixbyOn) {
        if (isNewBixbyOn(dailyBriefing) != bNewBixbyOn) {
            dailyBriefing ^= 16384;
        }
        Log.secD("AlarmItem", "setNewBixbyOn() / bNewBixbyOn = " + bNewBixbyOn + "/dailyBriefing[15](0 = OFF, 1 = ON) = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    public boolean isNewCelebOn() {
        Log.secD("AlarmItem", "isNewCelebOn() / mDailyBriefing = [16] " + Integer.toBinaryString(this.mDailyBriefing));
        return (this.mDailyBriefing & 32768) == 32768;
    }

    public static boolean isNewCelebOn(int dailyBriefing) {
        boolean isNewCelebOn = (dailyBriefing & 32768) == 32768;
        Log.secD("AlarmItem", "isNewCelebOn() / mDailyBriefing[16](0 = OFF, 1 = ON) = " + Integer.toBinaryString(dailyBriefing) + "/ isNewCelebOn = " + isNewCelebOn);
        return isNewCelebOn;
    }

    public void setNewCelebOn(boolean bNewCelebOn) {
        if (isNewCelebOn() != bNewCelebOn) {
            this.mDailyBriefing ^= 32768;
        }
        Log.secD("AlarmItem", "setNewCelebOn() / bNewCelebOn = " + bNewCelebOn + " mDailyBriefing[16] = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    private static int setNewCelebOn(int dailyBriefing, boolean bNewCelebOn) {
        if (isNewCelebOn(dailyBriefing) != bNewCelebOn) {
            dailyBriefing ^= 32768;
        }
        Log.secD("AlarmItem", "setNewCelebOn() / bNewCelebOn = " + bNewCelebOn + "/dailyBriefing[16](0 = OFF, 1 = ON) = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    public boolean isDefaultStop() {
        int snoozeRepeatTimes = getSnoozeRepeatTimes();
        return !this.mSnoozeActivate || (this.mSnoozeDoneCount >= snoozeRepeatTimes && AlarmItemUtil.CONTINUOUSLY != snoozeRepeatTimes);
    }

    public int getAlarmRepeat() {
        return this.mRepeatType >> 4;
    }

    public int getSnoozeRepeatTimes() {
        return AlarmItemUtil.ALARM_SNOOZE_COUNT_TABLE[this.mSnoozeRepeat];
    }

    public int getSnoozeDurationMinutes() {
        return getSnoozeDuration() * 60000;
    }

    public int getSnoozeDuration() {
        int[] iArr = AlarmItemUtil.ALARM_SNOOZE_DURATION_TABLE;
        int i = (this.mSnoozeDuration <= 0 || this.mSnoozeDuration >= 5) ? 1 : this.mSnoozeDuration;
        return iArr[i];
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public void setSpecificDate(boolean bSpecificDate) {
        if (isSpecificDate() != bSpecificDate) {
            this.mDailyBriefing ^= 1;
            Log.secD("AlarmItem", "setSpecificDate() / bSpecificDate = " + bSpecificDate + " mDailyBriefing[1] = " + Integer.toBinaryString(this.mDailyBriefing));
        }
    }

    public void setHoliday(boolean bHoliday) {
        if (isHoliday() != bHoliday) {
            this.mDailyBriefing ^= 4;
            Log.secD("AlarmItem", "setHoliday() / bHoliday = " + bHoliday + " mDailyBriefing[3] = " + Integer.toBinaryString(this.mDailyBriefing));
        }
    }

    public void setWorkingDay(boolean bWorkingDay) {
        if (isWorkingDay() != bWorkingDay) {
            this.mDailyBriefing ^= 8;
            Log.secD("AlarmItem", "setWorkingDay() / bWorkingDay = " + bWorkingDay + " mDailyBriefing[4] = " + Integer.toBinaryString(this.mDailyBriefing));
        }
    }

    public void setSubstituteHoliday(boolean bSubstituteHoliday) {
        if (isSubstituteHoliday() != bSubstituteHoliday) {
            this.mDailyBriefing ^= 16;
        }
        Log.secD("AlarmItem", "setSubstituteHoliday() / bSubstituteHoliday = " + bSubstituteHoliday + " mDailyBriefing[5] = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public void setTtsAlarm(boolean bTtsOn) {
        if (isTtsAlarm() != bTtsOn) {
            this.mDailyBriefing ^= 32;
        }
        Log.secD("AlarmItem", "setTtsAlarm() / bTtsOn = " + bTtsOn + " mDailyBriefing[6] = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public void setAlarmToneOn(boolean bAlarmToneOn) {
        if (isAlarmToneOn() != bAlarmToneOn) {
            this.mDailyBriefing ^= 64;
        }
        Log.secD("AlarmItem", "setAlarmToneOn() / bAlarmToneOn = " + bAlarmToneOn + "/ mDailyBriefing[7] (ON = bit 0) = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int setAlarmToneOn(int dailyBriefing, boolean bAlarmToneOn) {
        if (isAlarmToneOn(dailyBriefing) != bAlarmToneOn) {
            dailyBriefing ^= 64;
        }
        Log.secD("AlarmItem", "setAlarmToneOn() / bAlarmToneOn = " + bAlarmToneOn + "/ mDailyBriefing[7] (ON = bit 0) = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    private void setBixbyBriefingOn(boolean bBixbyBriefingOn) {
        if (isBixbyBriefingOn() != bBixbyBriefingOn) {
            this.mDailyBriefing ^= 128;
        }
        Log.secD("AlarmItem", "setBixbyBriefingOn() / bBixbyBriefingOn = " + bBixbyBriefingOn + " mDailyBriefing[8] = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int setBixbyBriefingOn(int dailyBriefing, boolean bBixbyBriefingOn) {
        if (isBixbyBriefingOn(dailyBriefing) != bBixbyBriefingOn) {
            dailyBriefing ^= 128;
        }
        Log.secD("AlarmItem", "setBixbyBriefingOn() / bBixbyBriefingOn = " + bBixbyBriefingOn + " dailyBriefing[8] = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    private void setBixbyVoiceOn(boolean bBixbyVoiceOn) {
        if (isBixbyVoiceOn() != bBixbyVoiceOn) {
            this.mDailyBriefing ^= 2048;
        }
        Log.secD("AlarmItem", "setBixbyVoiceOn() / bBixbyVoiceOn = " + bBixbyVoiceOn + "/ mDailyBriefing[12] (ON = bit 0) =" + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int setBixbyVoiceOn(int dailyBriefing, boolean bBixbyVoiceOn) {
        if (isBixbyVoiceOn(dailyBriefing) != bBixbyVoiceOn) {
            dailyBriefing ^= 2048;
        }
        Log.secD("AlarmItem", "setBixbyVoiceOn() / bBixbyVoiceOn = " + bBixbyVoiceOn + " mDailyBriefing[12] (ON = bit 0) = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    private void setBixbyCelebVoice(boolean bBixbyCelebVoice) {
        if (isBixbyCelebVoice() != bBixbyCelebVoice) {
            this.mDailyBriefing ^= 4096;
        }
        Log.secD("AlarmItem", "setBixbyCelebVoice() / bBixbyCelebVoice = " + bBixbyCelebVoice + " mDailyBriefing[13] = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int setBixbyCelebVoice(int dailyBriefing, boolean bBixbyCelebVoice) {
        if (isBixbyCelebVoice(dailyBriefing) != bBixbyCelebVoice) {
            dailyBriefing ^= 4096;
        }
        Log.secD("AlarmItem", "setBixbyCelebVoice() / bBixbyCelebVoice = " + bBixbyCelebVoice + " dailyBriefing[13]( 0 = OFF, 1 = ON) = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    public void setMasterSoundOn(boolean bMasterSoundOn) {
        if (isMasterSoundOn() != bMasterSoundOn) {
            this.mDailyBriefing ^= 8192;
        }
        Log.secD("AlarmItem", "setMasterSoundOn() / bMasterSoundOn = " + bMasterSoundOn + "/ mDailyBriefing[14] (ON = bit 0) = " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int setMasterSoundOn(int dailyBriefing, boolean bMasterSoundOn) {
        if (isMasterSoundOn(dailyBriefing) != bMasterSoundOn) {
            dailyBriefing ^= 8192;
        }
        Log.secD("AlarmItem", "setMasterSoundOn() / bMasterSoundOn = " + bMasterSoundOn + "/ mDailyBriefing[14] (ON = bit 0) = " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    public void setCreateTime() {
        this.mCreateTime = Calendar.getInstance().getTimeInMillis();
    }

    public void setCreateTime(long time) {
        this.mCreateTime = time;
    }

    public void setWeekDayAlarm() {
        String defaultWeekday = "XXXXXBR";
        String weekdayString = Feature.getWeekDayColor();
        int repeat = 17895680;
        if (weekdayString.length() == 7 && !weekdayString.substring(0, 5).equals("XXXXXBR".substring(0, 5))) {
            repeat = 0;
            for (int i = 0; i < weekdayString.length(); i++) {
                if (weekdayString.charAt((12 - i) % 7) == 'X') {
                    repeat |= 1 << ((i + 1) * 4);
                }
            }
        }
        this.mRepeatType = repeat;
        setWeeklyAlarm();
    }

    public void setWeeklyAlarm() {
        this.mRepeatType &= -16;
        this.mRepeatType |= 5;
    }

    public void setOneTimeAlarm() {
        this.mRepeatType &= -16;
        this.mRepeatType |= 1;
    }

    public void initIncreasingVolume() {
        boolean bIncreasingVolumeOn = (this.mDailyBriefing & 2) == 2;
        Log.secD("AlarmItem", "1 initIncreasingVolume() + dailyBriefing = [2] / " + Integer.toBinaryString(this.mDailyBriefing) + "/ bIncreasingVolumeOn = " + bIncreasingVolumeOn);
        if (!bIncreasingVolumeOn) {
            this.mDailyBriefing ^= 2;
        }
        Log.secD("AlarmItem", "2 initIncreasingVolume() + mDailyBriefing = [2] / " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int initIncreasingVolume(int dailyBriefing) {
        boolean bIncreasingVolumeOn = (dailyBriefing & 2) == 2;
        Log.secD("AlarmItem", "1 initIncreasingVolume(dailyBriefing) + dailyBriefing = [2] / " + Integer.toBinaryString(dailyBriefing) + "/ bIncreasingVolumeOn = " + bIncreasingVolumeOn);
        if (!bIncreasingVolumeOn) {
            dailyBriefing ^= 2;
        }
        Log.secD("AlarmItem", "2 initIncreasingVolume(dailyBriefing) + dailyBriefing = [2] / " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    public void initWeatherBg() {
        initRecommendWeatherBg();
        initWeatherMusicPath();
    }

    private void initWeatherMusicPath() {
        Log.secD("AlarmItem", "initWeatherMusicPath()");
        this.mWeatherMusicPath = "";
    }

    private void initRecommendWeatherBg() {
        boolean bRecommendWeatherBgOff = (this.mDailyBriefing & 256) == 256;
        Log.secD("AlarmItem", "1 initRecommendWeatherBg() + dailyBriefing = [9] 1 / " + Integer.toBinaryString(this.mDailyBriefing) + "/ bRecommendWeatherBgOff = " + bRecommendWeatherBgOff);
        if (bRecommendWeatherBgOff) {
            this.mDailyBriefing ^= 256;
        }
        Log.secD("AlarmItem", "2 initRecommendWeatherBg() + dailyBriefing = [9] 2 / " + Integer.toBinaryString(this.mDailyBriefing));
    }

    public static int initRecommendWeatherBg(int dailyBriefing) {
        boolean bRecommendWeatherBgOff = (dailyBriefing & 256) == 256;
        Log.secD("AlarmItem", "1 initRecommendWeatherBg() + dailyBriefing = [9] 1 / " + Integer.toBinaryString(dailyBriefing) + "/ bRecommendWeatherBgOff = " + bRecommendWeatherBgOff);
        if (bRecommendWeatherBgOff) {
            dailyBriefing ^= 256;
        }
        Log.secD("AlarmItem", "2 initRecommendWeatherBg(dailyBriefing) + dailyBriefing = [9] 2 / " + Integer.toBinaryString(dailyBriefing));
        return dailyBriefing;
    }

    public void initNotUsedBixbyCeleb() {
        setBixbyBriefingOn(false);
        setBixbyVoiceOn(true);
        setBixbyCelebVoice(false);
    }

    public static int initNotUsedBixbyCeleb(int dailyBriefing) {
        return setBixbyCelebVoice(setBixbyVoiceOn(setBixbyBriefingOn(dailyBriefing, false), true), false);
    }

    public boolean updateSpecificDateAlertTime(Context context) {
        boolean bUpdated = false;
        int prevTimeZone = AlarmSharedManager.getTimeZone(context);
        if (999 != prevTimeZone) {
            int curTimeZone = TimeZone.getDefault().getOffset(System.currentTimeMillis());
            Log.secD("AlarmItem", "updateSpecificDateAlertTime curTimeZone = " + curTimeZone + " prevTimeZone = " + prevTimeZone);
            long tempAlertTime = this.mAlarmAlertTime + ((long) (prevTimeZone - curTimeZone));
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(tempAlertTime);
            Log.secD("AlarmItem", "updateSpecificDateAlertTime tempAlertTime = " + c.getTime().toString());
            if ((c.get(11) * 100) + c.get(12) == this.mAlarmTime) {
                bUpdated = true;
                this.mAlarmAlertTime = tempAlertTime;
            }
        }
        Log.secD("AlarmItem", "updateSpecificDateAlertTime bUpdated = " + bUpdated + " item.mAlarmAlertTime = " + digitToAlphabetStr(AlarmItemUtil.getTimeString(this.mAlarmAlertTime)));
        return bUpdated;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        AlarmItem item = (AlarmItem) o;
        if (this.mSoundUri == null) {
            this.mSoundUri = "";
        }
        if (this.mAlarmName == null) {
            this.mAlarmName = "";
        }
        if (this.mCelebVoicePath == null) {
            this.mCelebVoicePath = "";
        }
        if (this.mActivate == item.mActivate && this.mCreateTime == item.mCreateTime && this.mAlarmAlertTime == item.mAlarmAlertTime && this.mAlarmTime == item.mAlarmTime && this.mRepeatType == item.mRepeatType && this.mNotificationType == item.mNotificationType && this.mSnoozeActivate == item.mSnoozeActivate && this.mSnoozeDuration == item.mSnoozeDuration && this.mSnoozeRepeat == item.mSnoozeRepeat && this.mSnoozeDoneCount == item.mSnoozeDoneCount && this.mDailyBriefing == item.mDailyBriefing && this.mSubdueActivate == item.mSubdueActivate && this.mSubdueDuration == item.mSubdueDuration && this.mSubdueTone == item.mSubdueTone && this.mAlarmSoundType == item.mAlarmSoundType && this.mAlarmSoundTone == item.mAlarmSoundTone && this.mAlarmVolume == item.mAlarmVolume && this.mSubdueUri == item.mSubdueUri && this.mSoundUri.equals(item.mSoundUri) && this.mAlarmName.equals(item.mAlarmName) && this.mVibrationPattern == item.mVibrationPattern && this.mCelebVoicePath.equals(item.mCelebVoicePath)) {
            return true;
        }
        return false;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            Log.secE("AlarmItem", "clone CloneNotSupportedException");
            return null;
        }
    }

    private void updateNextAlertTime(Context context) {
        if (!isFirstAlarm() || !isWeeklyAlarm()) {
            return;
        }
        if ((isHoliday() && Feature.isSupportHolidayAlarm()) || (isWorkingDay() && Feature.isSupportAlarmOptionMenuForWorkingDay())) {
            this.mAlarmAlertTime = HolidayUtil.getNextAlertTimeForHolidayOrWorkingDayAlarm(context, this.mAlarmAlertTime, this.mRepeatType, isSubstituteHoliday(this.mDailyBriefing));
        }
    }

    public long getNextAlertTimeForPreDismissedAlarm(Context context, AlarmItem item, long alertTime) {
        long nextAlertTime = -1;
        if (item.isFirstAlarm() && item.isWeeklyAlarm()) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(alertTime);
            c.add(6, AlarmItemUtil.getNextAlertDayOffset(c, this.mRepeatType));
            nextAlertTime = c.getTimeInMillis();
            if ((item.isHoliday() && Feature.isSupportHolidayAlarm()) || (item.isWorkingDay() && Feature.isSupportAlarmOptionMenuForWorkingDay())) {
                nextAlertTime = HolidayUtil.getNextAlertTimeForHolidayOrWorkingDayAlarm(context, nextAlertTime, item.mRepeatType, isSubstituteHoliday(item.mDailyBriefing));
            }
        }
        Log.secD("AlarmItem", "getNextAlertTimeForPreDismissedAlarm alertTime = " + digitToAlphabetStr(AlarmItemUtil.getTimeString(alertTime)) + "nextAlertTime = " + digitToAlphabetStr(AlarmItemUtil.getTimeString(nextAlertTime)));
        return nextAlertTime;
    }

    public void setSoundModeNewCeleb() {
        setNewCelebOn(true);
        setNewBixbyOn(false);
        setAlarmToneOn(false);
    }

    public void setSoundModeNewBixby() {
        setNewCelebOn(false);
        setNewBixbyOn(true);
        setAlarmToneOn(false);
    }

    public void setSoundModeAlarmTone() {
        setNewCelebOn(false);
        setNewBixbyOn(false);
        setAlarmToneOn(true);
    }

    public static int setSoundModeNewCeleb(int dailyBriefing) {
        return setAlarmToneOn(setNewBixbyOn(setNewCelebOn(dailyBriefing, true), false), false);
    }

    public static int setSoundModeNewBixby(int dailyBriefing) {
        return setAlarmToneOn(setNewBixbyOn(setNewCelebOn(dailyBriefing, false), true), false);
    }

    public static int setSoundModeAlarmTone(int dailyBriefing) {
        return setAlarmToneOn(setNewBixbyOn(setNewCelebOn(dailyBriefing, false), false), true);
    }
}
