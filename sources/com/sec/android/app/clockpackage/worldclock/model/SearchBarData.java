package com.sec.android.app.clockpackage.worldclock.model;

import android.view.inputmethod.InputMethodManager;

public class SearchBarData {
    private boolean mDropdownListShown = false;
    private InputMethodManager mImm = null;
    private boolean mIsCurrentLocationListShowing = false;
    private String mLastString = "";
    private int mModelType = 1;

    public boolean isCurrentLocationListShowing() {
        return this.mIsCurrentLocationListShowing;
    }

    public void setCurrentLocationListShowing(boolean currentLocationListShowing) {
        this.mIsCurrentLocationListShowing = currentLocationListShowing;
    }

    public InputMethodManager getImm() {
        return this.mImm;
    }

    public void setImm(InputMethodManager imm) {
        this.mImm = imm;
    }

    public boolean isDropdownListShown() {
        return this.mDropdownListShown;
    }

    public void setDropdownListShown(boolean dropdownListShown) {
        this.mDropdownListShown = dropdownListShown;
    }

    public String getLastString() {
        return this.mLastString;
    }

    public void setLastString(String lastString) {
        this.mLastString = lastString;
    }

    public int getModelType() {
        return this.mModelType;
    }

    public void setModelType(int modelType) {
        this.mModelType = modelType;
    }
}
