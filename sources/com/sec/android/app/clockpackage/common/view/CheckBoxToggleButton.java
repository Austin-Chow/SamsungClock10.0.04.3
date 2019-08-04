package com.sec.android.app.clockpackage.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ToggleButton;

public class CheckBoxToggleButton extends ToggleButton {
    public CheckBoxToggleButton(Context context) {
        super(context);
    }

    public CheckBoxToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBoxToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CharSequence getAccessibilityClassName() {
        return CheckBox.class.getName();
    }
}
