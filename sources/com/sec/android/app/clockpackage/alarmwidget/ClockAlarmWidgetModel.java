package com.sec.android.app.clockpackage.alarmwidget;

public class ClockAlarmWidgetModel {
    private int mAlarmRepeatIndex;
    private int mBackgroundColor;
    private boolean mIsActive;
    private boolean mIsDartFont;
    private boolean mIsEmpty;
    private int mNameAndDateTextColor;
    private int mNoItemTextColor;
    private int mOnOffImageColor;
    private int mTimeAndAmPmTextColor;
    private int mTransparency;
    private int mWidgetSize;

    public void setNameAndDateTextColor(int textColor) {
        this.mNameAndDateTextColor = textColor;
    }

    public void setTimeAndAmPmTextColor(int textColor) {
        this.mTimeAndAmPmTextColor = textColor;
    }

    public void setNoItemTextColor(int textColor) {
        this.mNoItemTextColor = textColor;
    }

    public void setTransparency(int transparency) {
        this.mTransparency = transparency;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public void setmOnOffImageColor(int imageColor) {
        this.mOnOffImageColor = imageColor;
    }

    public void setWidgetSize(int widgetSize) {
        this.mWidgetSize = widgetSize;
    }

    public void setIsDartFont(boolean isDartFont) {
        this.mIsDartFont = isDartFont;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.mIsEmpty = isEmpty;
    }

    public void setAlarmRepeatIndex(int alarmRepeatIndex) {
        this.mAlarmRepeatIndex = alarmRepeatIndex;
    }

    public void setIsActivate(boolean isActive) {
        this.mIsActive = isActive;
    }

    public int getNameAndDateTextColor() {
        return this.mNameAndDateTextColor;
    }

    public int getTimeAndAmPmTextColor() {
        return this.mTimeAndAmPmTextColor;
    }

    public int getNoItemTextColor() {
        return this.mNoItemTextColor;
    }

    public int getTransparency() {
        return this.mTransparency;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public int getOnOffImageColor() {
        return this.mOnOffImageColor;
    }

    public int getWidgetSize() {
        return this.mWidgetSize;
    }

    public boolean getIsEmpty() {
        return this.mIsEmpty;
    }

    public boolean getIsDartFont() {
        return this.mIsDartFont;
    }

    public int getAlarmRepeatIndex() {
        return this.mAlarmRepeatIndex;
    }

    public boolean getIsActivate() {
        return this.mIsActive;
    }
}
