package com.sec.android.app.clockpackage.ringtonepicker.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;
import com.sec.android.app.clockpackage.common.util.ClockUtils;

class RingtoneListAdapter extends SimpleCursorAdapter {
    private boolean mIsEnable = true;
    private boolean mIsTalkBackEnabled = false;

    RingtoneListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    void setTalkBackEnable(boolean isEnable) {
        this.mIsTalkBackEnabled = isEnable;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        view.setEnabled(this.mIsEnable);
        ClockUtils.setAccessibilityFontSize(context, (CheckedTextView) view);
        if (this.mIsTalkBackEnabled) {
            view.setClickable(!this.mIsEnable);
        } else {
            view.setClickable(false);
        }
    }
}
