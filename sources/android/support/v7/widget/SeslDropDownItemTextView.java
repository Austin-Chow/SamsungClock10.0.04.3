package android.support.v7.widget;

import android.content.Context;
import android.util.AttributeSet;

public class SeslDropDownItemTextView extends SeslCheckedTextView {
    public SeslDropDownItemTextView(Context context) {
        this(context, null);
    }

    public SeslDropDownItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public SeslDropDownItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        setTypeface(null, checked ? 1 : 0);
    }
}
