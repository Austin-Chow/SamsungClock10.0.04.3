package com.sec.android.app.clockpackage.worldclock.model;

import android.support.v7.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class WorldclockMainListViewModelData {
    private ActionMode mActionMode;
    private boolean mCheckedActionMode = false;
    private boolean mIsActionMode;
    private View mMultiSelectModeTitle;
    private CheckBox mSelectAllCheckBox;
    private ViewGroup mSelectAllLayout;
    private TextView mSelectItemText;

    public ActionMode getActionMode() {
        return this.mActionMode;
    }

    public void setActionMode(ActionMode actionMode) {
        this.mActionMode = actionMode;
    }

    public boolean isActionMode() {
        return this.mIsActionMode;
    }

    public void setIsActionMode(boolean isActionMode) {
        this.mIsActionMode = isActionMode;
    }

    public ViewGroup getSelectAllLayout() {
        return this.mSelectAllLayout;
    }

    public void setSelectAllLayout(ViewGroup selectAllLayout) {
        this.mSelectAllLayout = selectAllLayout;
    }

    public View getMultiSelectModeTitle() {
        return this.mMultiSelectModeTitle;
    }

    public void setMultiSelectModeTitle(View multiSelectModeTitle) {
        this.mMultiSelectModeTitle = multiSelectModeTitle;
    }

    public boolean isCheckedActionMode() {
        return this.mCheckedActionMode;
    }

    public void setCheckedActionMode(boolean isCheckedActionMode) {
        this.mCheckedActionMode = isCheckedActionMode;
    }

    public TextView getSelectItemText() {
        return this.mSelectItemText;
    }

    public void setSelectItemText(TextView selectItemText) {
        this.mSelectItemText = selectItemText;
    }

    public CheckBox getSelectAllCheckBox() {
        return this.mSelectAllCheckBox;
    }

    public void setSelectAllCheckBox(CheckBox selectAllCheckBox) {
        this.mSelectAllCheckBox = selectAllCheckBox;
    }
}
