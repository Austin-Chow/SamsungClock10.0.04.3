package com.sec.android.widgetapp.digitalclock;

public class DigitalClockWidgetModel {
    private int mBackgroundColor;
    private int mTextColor;
    private int mTransparency;
    private int mWidgetSize;

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public void setTransparency(int transparency) {
        this.mTransparency = transparency;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public void setWidgetSize(int widgetSize) {
        this.mWidgetSize = widgetSize;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public int getTransparency() {
        return this.mTransparency;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public int getWidgetSize() {
        return this.mWidgetSize;
    }
}
