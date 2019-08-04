package com.sec.android.app.clockpackage.alarm.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.samsung.android.widget.SemTimePicker;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;

public class AlarmTimePickerLayout extends LinearLayout {
    private LinearLayout mPickerLayout;
    private SemTimePicker mSemTimePicker;

    public static class AlarmTimePicker extends SemTimePicker {
        private int maxHeight;
        private int maxWidth;

        public AlarmTimePicker(Context context, AttributeSet attrs) {
            super(context, attrs);
            updatePickerTextSize();
            updateMaxPickerSize();
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            LayoutParams params = (LayoutParams) getLayoutParams();
            if (MeasureSpec.getSize(heightMeasureSpec) > this.maxHeight) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.maxHeight, Integer.MIN_VALUE);
                params.height = this.maxHeight;
                params.weight = 0.0f;
                setMinimumHeight(this.maxHeight);
            } else {
                setMinimumHeight(0);
            }
            if (MeasureSpec.getSize(widthMeasureSpec) <= this.maxWidth || this.maxWidth == -1) {
                params.width = -1;
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.maxWidth, Integer.MIN_VALUE);
                params.width = this.maxWidth;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            updatePickerTextSize();
            updateMaxPickerSize();
        }

        private void updateMaxPickerSize() {
            this.maxHeight = getResources().getDimensionPixelSize(C0490R.dimen.alarm_timepicker_max_height);
            this.maxWidth = getResources().getDimensionPixelSize(C0490R.dimen.alarm_timepicker_max_width);
        }

        private void updatePickerTextSize() {
            float textSize = (float) getResources().getDimensionPixelOffset(C0490R.dimen.alarm_timepicker_main_text_size);
            setTextSize(0, textSize);
            setTextSize(1, textSize);
            setTextSize(3, textSize);
            setTextSize(2, (float) getResources().getDimensionPixelOffset(C0490R.dimen.alarm_timepicker_ampm_text_size));
            setTextStyle(0);
            setTextStyle(1);
            setTextStyle(3);
            setTextStyle(2);
        }

        private void setTextSize(int picker, float textSize) {
            try {
                setNumberPickerTextSize(picker, textSize);
            } catch (NoSuchMethodError e) {
                Log.secE("AlarmTimePickerLayout", "NoSuchMethodError : " + e);
            }
        }

        private void setTextStyle(int picker) {
            try {
                Typeface tf = ClockUtils.getFontFromOpenTheme(getContext());
                if (tf == null || picker == 2) {
                    tf = Typeface.create("sans-serif-light", 0);
                }
                setNumberPickerTextTypeface(picker, tf);
            } catch (NoSuchMethodError e) {
                Log.secE("AlarmTimePickerLayout", "NoSuchMethodError : " + e);
            }
        }
    }

    public AlarmTimePickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void createView(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0490R.layout.alarm_timepicker_layout, this, true);
        this.mSemTimePicker = (SemTimePicker) findViewById(C0490R.id.timepicker);
        this.mPickerLayout = (LinearLayout) findViewById(C0490R.id.timepicker_layout);
        updateMargin();
    }

    public SemTimePicker getTimePicker() {
        return this.mSemTimePicker;
    }

    public void removeInstance() {
        removeAllViews();
        destroyDrawingCache();
    }

    public void updateHeight(int pickerHeight) {
        LayoutParams params = (LayoutParams) this.mPickerLayout.getLayoutParams();
        if (params.height != pickerHeight) {
            params.height = pickerHeight;
            getLayoutParams().height = pickerHeight;
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateMargin();
    }

    private void updateMargin() {
        ((LayoutParams) findViewById(C0490R.id.top_margin).getLayoutParams()).weight = getWeight(C0490R.dimen.alarm_timepicker_top_margin_weight);
        ((LayoutParams) this.mSemTimePicker.getLayoutParams()).weight = getWeight(C0490R.dimen.alarm_timepicker_weight);
        ((LayoutParams) findViewById(C0490R.id.bottom_margin).getLayoutParams()).weight = getWeight(C0490R.dimen.alarm_timepicker_bottom_margin_weight);
    }

    private float getWeight(int dimenId) {
        TypedValue value = new TypedValue();
        getResources().getValue(dimenId, value, true);
        return value.getFloat();
    }
}
