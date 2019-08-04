package com.sec.android.app.clockpackage.timer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import com.sec.android.app.clockpackage.timer.C0728R;

public class TimerGridItem extends RelativeLayout implements Checkable {
    private CheckBox mCheckBox;

    public TimerGridItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public boolean isChecked() {
        if (this.mCheckBox == null) {
            if (findViewById(C0728R.id.checkbox) == null) {
                return false;
            }
            this.mCheckBox = (CheckBox) findViewById(C0728R.id.checkbox);
        }
        return this.mCheckBox.isChecked();
    }

    public void setChecked(boolean checked) {
        if (this.mCheckBox == null) {
            if (findViewById(C0728R.id.checkbox) != null) {
                this.mCheckBox = (CheckBox) findViewById(C0728R.id.checkbox);
            } else {
                return;
            }
        }
        this.mCheckBox.setChecked(checked);
    }

    public void toggle() {
        if (this.mCheckBox == null) {
            if (findViewById(C0728R.id.checkbox) != null) {
                this.mCheckBox = (CheckBox) findViewById(C0728R.id.checkbox);
            } else {
                return;
            }
        }
        this.mCheckBox.toggle();
    }
}
