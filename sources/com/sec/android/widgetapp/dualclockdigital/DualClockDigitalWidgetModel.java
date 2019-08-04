package com.sec.android.widgetapp.dualclockdigital;

public class DualClockDigitalWidgetModel {
    private int mBackgroundColor;
    private int mImageColor;
    private boolean mSupported;
    private int mTextColor;
    private int mTransparency;

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public void setTransparency(int transparency) {
        this.mTransparency = transparency;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public void setImageColor(int imageColor) {
        this.mImageColor = imageColor;
    }

    public void setSupportedWidget(boolean supported) {
        this.mSupported = supported;
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

    public int getImageColor() {
        return this.mImageColor;
    }

    public boolean getSupportedWidget() {
        return this.mSupported;
    }
}
