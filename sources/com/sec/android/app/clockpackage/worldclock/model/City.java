package com.sec.android.app.clockpackage.worldclock.model;

import java.util.TimeZone;

public class City {
    public int mArrowDirection;
    private int mArrowTextOffsetX;
    private int mArrowTextOffsetY;
    private String mCityPlaceIds;
    private int mCityUnqId;
    private String mCountry;
    private String mCountryPinyin;
    private boolean mIsDBCurrentLocation;
    private boolean mIsDBSelected;
    private float mLatitude;
    public float mLatitudeBillboard;
    private float mLongitude;
    public float mLongitudeBillboard;
    private String mName;
    private String mNamePinyin;
    private TimeZone mTimeZone = null;
    private String mTimeZoneId;
    public int mZoomLevel;

    public City(String name, String country, String timeZoneId, int zoomLevel, int arrowDirection, int cityUnqId, float latitude, float longitude, float latitudeBillboard, float longitudeBillboard, int textOffsetX, int textOffsetY, String cityPlaceIds) {
        initCityInfo(name, country, timeZoneId, cityUnqId, cityPlaceIds, zoomLevel, arrowDirection, latitude, longitude, latitudeBillboard, longitudeBillboard, textOffsetX, textOffsetY);
    }

    public City(String name, String country, String timeZoneId, int cityUnqId, String cityPlaceIds) {
        initCityInfo(name, country, timeZoneId, cityUnqId, cityPlaceIds, -1, -1, -1.0f, -1.0f, -1.0f, -1.0f, -1, -1);
    }

    public String getNamePinyin() {
        return this.mNamePinyin;
    }

    public String getCountryPinyin() {
        return this.mCountryPinyin;
    }

    public void setPinYin(String namePinyin, String countryPinyin) {
        this.mNamePinyin = namePinyin;
        this.mCountryPinyin = countryPinyin;
    }

    private void initCityInfo(String name, String country, String timeZoneId, int cityUnqID, String cityPlaceIds, int zoomLevel, int arrowDirection, float latitude, float longitude, float latitudeBillboard, float longitudeBillboard, int textOffsetX, int textOffsetY) {
        this.mName = name;
        this.mCountry = country;
        this.mTimeZoneId = timeZoneId;
        this.mCityUnqId = cityUnqID;
        this.mCityPlaceIds = cityPlaceIds;
        this.mZoomLevel = zoomLevel;
        this.mArrowDirection = arrowDirection;
        this.mLatitudeBillboard = latitudeBillboard;
        this.mLongitudeBillboard = longitudeBillboard;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mArrowTextOffsetX = textOffsetX;
        this.mArrowTextOffsetY = textOffsetY;
    }

    public String getCurrentTimeGMT() {
        int rawOffset = getLocalOffset();
        StringBuilder GMT = new StringBuilder("GMT");
        if (rawOffset / 3600000 >= 0) {
            GMT.append('+');
        }
        GMT.append(Integer.toString(rawOffset / 3600000));
        if (rawOffset % 3600000 > 0 || rawOffset % 3600000 < 0) {
            GMT.append(':');
            GMT.append(Integer.toString(Math.abs((rawOffset % 3600000) / 60000)));
        }
        return GMT.toString();
    }

    public boolean getDBSelected() {
        return this.mIsDBSelected;
    }

    public void setDBSelected(boolean isDBSelected) {
        this.mIsDBSelected = isDBSelected;
    }

    public boolean getDBCurrentLocation() {
        return this.mIsDBCurrentLocation;
    }

    public void setDBCurrentLocation(boolean isDBCurrentLocation) {
        this.mIsDBCurrentLocation = isDBCurrentLocation;
    }

    public float getLatitude() {
        return this.mLatitude;
    }

    public float getLongitude() {
        return this.mLongitude;
    }

    public String getName() {
        return this.mName;
    }

    public String getCountry() {
        return this.mCountry;
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

    public int getZoomLevel() {
        return this.mZoomLevel;
    }

    public int getUniqueId() {
        return this.mCityUnqId;
    }

    public String getCityPlaceId() {
        return this.mCityPlaceIds;
    }

    public int getOffsetMillis() {
        return getLocalOffset();
    }

    public int getLocalOffset() {
        long now = System.currentTimeMillis();
        if (this.mTimeZone != null) {
            return this.mTimeZone.getOffset(now);
        }
        return TimeZone.getTimeZone(this.mTimeZoneId).getOffset(now);
    }

    public int getXTextOffset() {
        return this.mArrowTextOffsetX;
    }

    public int getYTextOffset() {
        return this.mArrowTextOffsetY;
    }

    public int getArrowDirection() {
        return this.mArrowDirection;
    }
}
