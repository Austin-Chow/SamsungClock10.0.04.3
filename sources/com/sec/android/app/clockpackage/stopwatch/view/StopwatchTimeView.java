package com.sec.android.app.clockpackage.stopwatch.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.stopwatch.C0706R;

public class StopwatchTimeView extends ConstraintLayout {
    protected TextView mColonHMS;
    protected TextView mColonMS;
    private Context mContext;
    protected TextView mHourPostfix;
    protected TextView mHourPrefix;
    protected TextView mMillisPostfix;
    protected TextView mMillisPrefix;
    protected TextView mMinutePostfix;
    protected TextView mMinutePrefix;
    protected TextView mPeriod;
    protected TextView mSecondPostfix;
    protected TextView mSecondPrefix;
    private ConstraintLayout mTimeView;

    public StopwatchTimeView(Context context) {
        super(context);
        init(context);
    }

    public StopwatchTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StopwatchTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        init();
    }

    public void init() {
        Log.secD("StopwatchTimeView", "init");
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        removeAllViews();
        View view = inflater.inflate(C0706R.layout.stopwatch_timeview, this, true);
        this.mTimeView = (ConstraintLayout) view.findViewById(C0706R.id.stopwatch_time_text_layout);
        this.mHourPrefix = (TextView) view.findViewById(C0706R.id.stopwatch_hour_prefix);
        this.mHourPostfix = (TextView) view.findViewById(C0706R.id.stopwatch_hour_postfix);
        this.mMinutePostfix = (TextView) view.findViewById(C0706R.id.stopwatch_minute_postfix);
        this.mMinutePrefix = (TextView) view.findViewById(C0706R.id.stopwatch_minute_prefix);
        this.mSecondPostfix = (TextView) view.findViewById(C0706R.id.stopwatch_second_postfix);
        this.mSecondPrefix = (TextView) view.findViewById(C0706R.id.stopwatch_second_prefix);
        this.mMillisPostfix = (TextView) view.findViewById(C0706R.id.stopwatch_milli_postfix);
        this.mMillisPrefix = (TextView) view.findViewById(C0706R.id.stopwatch_milli_prefix);
        this.mColonHMS = (TextView) view.findViewById(C0706R.id.stopwatch_hms_colon);
        this.mColonMS = (TextView) view.findViewById(C0706R.id.stopwatch_ms_colon);
        this.mPeriod = (TextView) view.findViewById(C0706R.id.stopwatch_ms_period);
        String timeSeparatorText = ClockUtils.getTimeSeparatorText(this.mContext);
        this.mColonHMS.setText(timeSeparatorText);
        this.mColonMS.setText(timeSeparatorText);
        setFontFromOpenTheme();
        this.mPeriod.semSetHoverPopupType(0);
        this.mHourPrefix.semSetHoverPopupType(0);
        this.mHourPostfix.semSetHoverPopupType(0);
        this.mMinutePostfix.semSetHoverPopupType(0);
        this.mMinutePrefix.semSetHoverPopupType(0);
        this.mSecondPostfix.semSetHoverPopupType(0);
        this.mSecondPrefix.semSetHoverPopupType(0);
        this.mMillisPostfix.semSetHoverPopupType(0);
        this.mMillisPrefix.semSetHoverPopupType(0);
        this.mColonHMS.semSetHoverPopupType(0);
        this.mColonMS.semSetHoverPopupType(0);
        this.mPeriod.semSetHoverPopupType(0);
        if (Feature.isTablet(this.mContext)) {
            enableMultiSelection(false);
        }
    }

    public void updateTextSize() {
        Log.secD("StopwatchTimeView", "updateTextSize()");
        setTimeLayout(this.mHourPrefix);
        setTimeLayout(this.mHourPostfix);
        setTimeLayout(this.mMinutePrefix);
        setTimeLayout(this.mMinutePostfix);
        setTimeLayout(this.mSecondPrefix);
        setTimeLayout(this.mSecondPostfix);
        setTimeLayout(this.mMillisPrefix);
        setTimeLayout(this.mMillisPostfix);
        setTimeLayout(this.mColonHMS);
        setTimeLayout(this.mColonMS);
        setTimeLayout(this.mPeriod);
    }

    private void setTimeLayout(TextView view) {
        Resources res = getResources();
        LayoutParams timeViewParam = (LayoutParams) view.getLayoutParams();
        if (timeViewParam != null) {
            timeViewParam.width = (res.getDimensionPixelSize(C0706R.dimen.stopwatch_time_textview_width) * 65) / 100;
            timeViewParam.height = -2;
        }
        view.setTextSize(0, ((float) (getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_time_textview_textsize) * 6)) / 10.0f);
    }

    private void setFontFromOpenTheme() {
        Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (font == null) {
            font = Typeface.create("sans-serif-light", 0);
        }
        this.mHourPrefix.setTypeface(font);
        this.mHourPostfix.setTypeface(font);
        this.mMinutePrefix.setTypeface(font);
        this.mMinutePostfix.setTypeface(font);
        this.mSecondPrefix.setTypeface(font);
        this.mSecondPostfix.setTypeface(font);
        this.mMillisPostfix.setTypeface(font);
        this.mMillisPrefix.setTypeface(font);
        this.mColonHMS.setTypeface(font);
        this.mColonMS.setTypeface(font);
        this.mPeriod.setTypeface(font);
    }

    public void updateTimeView(int hour, int minute, int second, int millis) {
        try {
            setTimeView(hour, minute, second, millis);
        } catch (Exception e) {
            Log.secE("StopwatchTimeView", "Exception : " + e.toString());
            Log.secD("StopwatchTimeView", "updateTimeView Exception");
            init();
        }
    }

    public int getTimeViewHeight() {
        this.mTimeView.measure(0, 0);
        return this.mTimeView.getMeasuredHeight();
    }

    protected void setTimeView(int hour, int minute, int second, int millis) {
        if (this.mHourPrefix != null && this.mHourPostfix != null && this.mMinutePrefix != null && this.mMinutePostfix != null && this.mSecondPrefix != null && this.mSecondPostfix != null && this.mColonHMS != null && this.mColonMS != null && this.mPeriod != null && this.mMillisPrefix != null && this.mMillisPostfix != null) {
            if (hour > 0) {
                this.mHourPrefix.setVisibility(0);
                this.mHourPostfix.setVisibility(0);
                this.mColonHMS.setVisibility(0);
                ClockUtils.setNumber(this.mHourPrefix, hour / 10);
                ClockUtils.setNumber(this.mHourPostfix, hour % 10);
            } else {
                this.mHourPrefix.setVisibility(8);
                this.mHourPostfix.setVisibility(8);
                this.mColonHMS.setVisibility(8);
            }
            ClockUtils.setNumber(this.mMinutePrefix, minute / 10);
            ClockUtils.setNumber(this.mMinutePostfix, minute % 10);
            ClockUtils.setNumber(this.mSecondPrefix, second / 10);
            ClockUtils.setNumber(this.mSecondPostfix, second % 10);
            ClockUtils.setNumber(this.mMillisPrefix, millis / 10);
            ClockUtils.setNumber(this.mMillisPostfix, millis % 10);
        }
    }

    public void destroy() {
        this.mHourPrefix = null;
        this.mHourPostfix = null;
        this.mMinutePrefix = null;
        this.mMinutePostfix = null;
        this.mSecondPrefix = null;
        this.mSecondPostfix = null;
        this.mMillisPostfix = null;
        this.mMillisPrefix = null;
        this.mColonHMS = null;
        this.mColonMS = null;
        this.mPeriod = null;
    }

    public void enableMultiSelection(boolean enable) {
        if (this.mHourPrefix != null && this.mHourPostfix != null && this.mMinutePrefix != null && this.mMinutePostfix != null && this.mSecondPrefix != null && this.mSecondPostfix != null && this.mColonHMS != null && this.mColonMS != null && this.mPeriod != null && this.mMillisPrefix != null && this.mMillisPostfix != null) {
            this.mHourPrefix.semSetMultiSelectionEnabled(enable);
            this.mHourPostfix.semSetMultiSelectionEnabled(enable);
            this.mMinutePrefix.semSetMultiSelectionEnabled(enable);
            this.mMinutePostfix.semSetMultiSelectionEnabled(enable);
            this.mSecondPrefix.semSetMultiSelectionEnabled(enable);
            this.mSecondPostfix.semSetMultiSelectionEnabled(enable);
            this.mMillisPostfix.semSetMultiSelectionEnabled(enable);
            this.mMillisPrefix.semSetMultiSelectionEnabled(enable);
            this.mColonHMS.semSetMultiSelectionEnabled(enable);
            this.mColonMS.semSetMultiSelectionEnabled(enable);
            this.mPeriod.semSetMultiSelectionEnabled(enable);
        }
    }
}
