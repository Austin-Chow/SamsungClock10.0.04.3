package com.sec.android.app.clockpackage.worldclock.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WorldclockCityWeatherInfo implements Parcelable {
    public static final Creator<WorldclockCityWeatherInfo> CREATOR = new C08391();
    private String mCityName;
    private String mCityNameEng;
    private int mCurrentState;
    private int mCurrentTemperature;
    private String mDayOrNight;
    private String mMobileLink;
    private String mPlaceId;
    private String mWeatherDescription;
    private int mWeatherIconNum;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo$1 */
    static class C08391 implements Creator<WorldclockCityWeatherInfo> {
        C08391() {
        }

        public WorldclockCityWeatherInfo createFromParcel(Parcel in) {
            return new WorldclockCityWeatherInfo(in);
        }

        public WorldclockCityWeatherInfo[] newArray(int size) {
            return new WorldclockCityWeatherInfo[size];
        }
    }

    public WorldclockCityWeatherInfo() {
        this.mPlaceId = "";
        this.mCityName = "";
        this.mCityNameEng = "";
        this.mCurrentTemperature = 0;
        this.mWeatherIconNum = -1;
        this.mMobileLink = "";
        this.mWeatherDescription = "";
        this.mDayOrNight = "";
        this.mCurrentState = -1;
    }

    private WorldclockCityWeatherInfo(Parcel source) {
        this.mPlaceId = "";
        this.mCityName = "";
        this.mCityNameEng = "";
        this.mCurrentTemperature = 0;
        this.mWeatherIconNum = -1;
        this.mMobileLink = "";
        this.mWeatherDescription = "";
        this.mDayOrNight = "";
        this.mCurrentState = -1;
        this.mPlaceId = source.readString();
        this.mCityName = source.readString();
        this.mCityNameEng = source.readString();
        this.mCurrentTemperature = source.readInt();
        this.mWeatherIconNum = source.readInt();
        this.mMobileLink = source.readString();
        this.mWeatherDescription = source.readString();
        this.mDayOrNight = source.readString();
        this.mCurrentState = source.readInt();
    }

    public int getCurrentTemperature() {
        return this.mCurrentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.mCurrentTemperature = (int) currentTemperature;
    }

    public int getWeatherIconNum() {
        return this.mWeatherIconNum;
    }

    public void setWeatherIconNum(int weatherIconNum) {
        this.mWeatherIconNum = weatherIconNum;
    }

    public String getMobileLink() {
        return this.mMobileLink;
    }

    public void setMobileLink(String link) {
        this.mMobileLink = link;
    }

    public String getWeatherDescription() {
        return this.mWeatherDescription;
    }

    public void setWeatherDescription(String description) {
        this.mWeatherDescription = description;
    }

    public void setDayOrNight(String dayOrNight) {
        this.mDayOrNight = dayOrNight;
    }

    public String getDayOrNight() {
        return this.mDayOrNight;
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public void setCurrentState(int currentState) {
        this.mCurrentState = currentState;
    }

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPlaceId);
        dest.writeString(this.mCityName);
        dest.writeString(this.mCityNameEng);
        dest.writeInt(this.mCurrentTemperature);
        dest.writeInt(this.mWeatherIconNum);
        dest.writeString(this.mMobileLink);
        dest.writeString(this.mWeatherDescription);
        dest.writeString(this.mDayOrNight);
        dest.writeInt(this.mCurrentState);
    }
}
