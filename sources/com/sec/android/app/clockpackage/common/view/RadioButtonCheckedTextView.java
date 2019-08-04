package com.sec.android.app.clockpackage.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.RadioButton;

public class RadioButtonCheckedTextView extends CheckedTextView {
    public RadioButtonCheckedTextView(Context context) {
        super(context);
    }

    public RadioButtonCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioButtonCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CharSequence getAccessibilityClassName() {
        return RadioButton.class.getName();
    }
}
