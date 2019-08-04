package com.sec.android.app.clockpackage.worldclock.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.TimeZone;

public class ListItem implements Parcelable {
    public static final Creator<ListItem> CREATOR = new C08371();
    private int mCurrentWeatherState;
    private String mDayOrNight;
    private int mHourDiff;
    private int mId;
    private boolean mIsChecked;
    private float mLatitude;
    private float mLongitude;
    private int mMinDiff;
    private String mMobileLink;
    private String mPlaceId;
    private int mStartHour;
    private int mStartMin;
    private TimeZone mTimeZone;
    private String mTimeZoneId;
    private String mTopLabel;
    private int mUniqueId;
    private String mWeatherDescription;
    private int mWeatherIconNum;
    private String mWeatherLayoutDescription;
    private int mWeatherTemperature;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.model.ListItem$1 */
    static class C08371 implements Creator<ListItem> {
        C08371() {
        }

        public ListItem createFromParcel(Parcel p) {
            return new ListItem(p);
        }

        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    }

    public ListItem(int _id, String topLabel, String timeZoneId, int uniqueId, float latitude, float longitude) {
        this.mMinDiff = 0;
        this.mHourDiff = 0;
        this.mStartHour = 0;
        this.mStartMin = 0;
        this.mCurrentWeatherState = -1;
        this.mId = _id;
        this.mTopLabel = topLabel;
        this.mTimeZoneId = timeZoneId;
        this.mTimeZone = TimeZone.getTimeZone(timeZoneId);
        this.mUniqueId = uniqueId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getTopLabel() {
        return this.mTopLabel;
    }

    public boolean getSelected() {
        return this.mIsChecked;
    }

    public int getId() {
        return this.mId;
    }

    public String getTimeZoneId() {
        return this.mTimeZoneId;
    }

    public TimeZone getTimeZone() {
        if (this.mTimeZone == null) {
            this.mTimeZone = TimeZone.getTimeZone(this.mTimeZoneId);
        }
        return this.mTimeZone;
    }

    public int getUniqueId() {
        return this.mUniqueId;
    }

    public float getLatitude() {
        return this.mLatitude;
    }

    public float getLongitude() {
        return this.mLongitude;
    }

    public String getPlaceId() {
        return this.mPlaceId;
    }

    public String getWeatherDescription() {
        return this.mWeatherDescription;
    }

    public String getWeatherLayoutDescription() {
        return this.mWeatherLayoutDescription;
    }

    public String getDayOrNight() {
        return this.mDayOrNight;
    }

    public int getWeatherIconNum() {
        return this.mWeatherIconNum;
    }

    public int getWeatherTemp() {
        return this.mWeatherTemperature;
    }

    public int getHourDiff() {
        return this.mHourDiff;
    }

    public int getMinDiff() {
        return this.mMinDiff;
    }

    public int getStartHour() {
        return this.mStartHour;
    }

    public int getStartMin() {
        return this.mStartMin;
    }

    public String getMobileLink() {
        return this.mMobileLink;
    }

    public void setPlaceId(String placeId) {
        this.mPlaceId = placeId;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.mWeatherDescription = weatherDescription;
    }

    public void setWeatherLayoutDescription(String weatherLayoutDescription) {
        this.mWeatherLayoutDescription = weatherLayoutDescription;
    }

    public void setDayOrNight(String dayOrNight) {
        this.mDayOrNight = dayOrNight;
    }

    public void setWeatherIconNum(int weatherIconNum) {
        this.mWeatherIconNum = weatherIconNum;
    }

    public void setWeatherTemp(int weatherTemperature) {
        this.mWeatherTemperature = weatherTemperature;
    }

    public void setSelected(boolean select) {
        this.mIsChecked = select;
    }

    public void setHourDiff(int millis) {
        this.mHourDiff = millis;
    }

    public void setMinDiff(int millis) {
        this.mMinDiff = millis;
    }

    public void setStartHour(int millis) {
        this.mStartHour = millis;
    }

    public void setStartMin(int millis) {
        this.mStartMin = millis;
    }

    public void setMobileLink(String mobileLink) {
        this.mMobileLink = mobileLink;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel _dest, int _flags) {
        _dest.writeInt(this.mId);
        _dest.writeString(this.mTopLabel);
        _dest.writeString(this.mTimeZoneId);
        _dest.writeInt(this.mUniqueId);
        _dest.writeFloat(this.mLatitude);
        _dest.writeFloat(this.mLongitude);
    }

    private ListItem(Parcel p) {
        this.mMinDiff = 0;
        this.mHourDiff = 0;
        this.mStartHour = 0;
        this.mStartMin = 0;
        this.mCurrentWeatherState = -1;
        this.mId = p.readInt();
        this.mTopLabel = p.readString();
        this.mTimeZoneId = p.readString();
        this.mUniqueId = p.readInt();
        this.mLatitude = p.readFloat();
        this.mLongitude = p.readFloat();
    }

    public int getCurrentWeatherState() {
        return this.mCurrentWeatherState;
    }

    public void setCurrentWeatherState(int currentWeatherState) {
        this.mCurrentWeatherState = currentWeatherState;
    }
}
