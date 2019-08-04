package com.sec.android.app.clockpackage.alarm.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;

public final class BixbyBriefingAlarmItem implements Cloneable {
    public long mAlarmAlertTime = -1;
    public int mAlarmTime = -1;
    public int mDaytime = 0;
    public int mId = -1;
    public boolean mIsSuccess = false;
    public String mPath = "";
    public long mPlayTimeOfBixbyBriefing = 0;
    public Uri mUri;
    public int mWeatherConditionCode;
    public String mWeatherCpLink = "";

    public void writeToParcel(Parcel dest) {
        dest.writeInt(this.mIsSuccess ? 1 : 0);
        dest.writeInt(this.mId);
        dest.writeInt(this.mAlarmTime);
        dest.writeLong(this.mAlarmAlertTime);
        dest.writeString(this.mUri != null ? this.mUri.toString() : "");
        dest.writeString(this.mPath);
        dest.writeLong(this.mPlayTimeOfBixbyBriefing);
        dest.writeInt(this.mWeatherConditionCode);
        dest.writeString(this.mWeatherCpLink);
        dest.writeInt(this.mDaytime);
    }

    public void readFromIntent(Intent intent) {
        boolean z = true;
        if (intent != null) {
            byte[] alarmData = intent.getByteArrayExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_BIXBY_BRIEFING_DATA");
            if (alarmData != null) {
                Parcel in = Parcel.obtain();
                in.unmarshall(alarmData, 0, alarmData.length);
                in.setDataPosition(0);
                if (in.readInt() != 1) {
                    z = false;
                }
                this.mIsSuccess = z;
                this.mId = in.readInt();
                this.mAlarmTime = in.readInt();
                this.mAlarmAlertTime = in.readLong();
                String uriString = in.readString();
                this.mUri = uriString != null ? Uri.parse(uriString) : null;
                this.mPath = in.readString();
                this.mPlayTimeOfBixbyBriefing = in.readLong();
                this.mWeatherConditionCode = in.readInt();
                this.mWeatherCpLink = in.readString();
                this.mDaytime = in.readInt();
                in.recycle();
            }
        }
    }

    public final String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("mIsSuccess : ").append(this.mIsSuccess).append('\n');
        ret.append("mId : ").append(this.mId).append('\n');
        ret.append("alarmT_ : ").append(AlarmItem.digitToAlphabetStr(Integer.toString(this.mAlarmTime))).append('\n');
        ret.append("AlertT_ : ").append(AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(this.mAlarmAlertTime))).append('\n');
        if (Feature.DEBUG_ENG) {
            ret.append("mUri : ").append(this.mUri).append('\n');
            ret.append("mPath : ").append(this.mPath).append('\n');
        }
        ret.append("mPlayTimeOfBixbyBriefing : ").append(this.mPlayTimeOfBixbyBriefing).append('\n');
        ret.append("mWeatherConditionCode : ").append(this.mWeatherConditionCode).append('\n');
        ret.append("mWeatherCpLink : ").append(this.mWeatherCpLink).append('\n');
        ret.append("mDaytime : ").append(this.mDaytime).append('\n');
        return ret.toString();
    }

    public void init() {
        this.mIsSuccess = false;
        this.mId = -1;
        this.mAlarmTime = -1;
        this.mAlarmAlertTime = -1;
        this.mUri = null;
        this.mPath = "";
        this.mPlayTimeOfBixbyBriefing = 0;
        this.mWeatherConditionCode = 999;
        this.mWeatherCpLink = "";
        this.mDaytime = 0;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            Log.secE("BixbyBriefingAlarmItem", "clone CloneNotSupportedException");
            return null;
        }
    }
}
