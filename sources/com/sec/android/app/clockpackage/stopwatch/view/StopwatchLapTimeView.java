package com.sec.android.app.clockpackage.stopwatch.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.stopwatch.C0706R;

public class StopwatchLapTimeView extends ConstraintLayout {
    private Context mContext;
    protected TextView mLapColonHMS;
    protected TextView mLapColonMS;
    protected TextView mLapHourPostfix;
    protected TextView mLapHourPrefix;
    protected TextView mLapMillisPostfix;
    protected TextView mLapMillisPrefix;
    protected TextView mLapMinutePostfix;
    protected TextView mLapMinutePrefix;
    protected TextView mLapPeriod;
    protected TextView mLapSecondPostfix;
    protected TextView mLapSecondPrefix;
    private ConstraintLayout mLapTimeView;

    public StopwatchLapTimeView(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        init();
    }

    public StopwatchLapTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void init() {
        Log.secD("StopwatchLapTimeView", "init");
        removeAllViewsInLayout();
        LayoutInflater.from(this.mContext).inflate(C0706R.layout.stopwatch_laptimeview, this, true);
        this.mLapTimeView = (ConstraintLayout) findViewById(C0706R.id.stopwatch_laptime_text_layout);
        this.mLapHourPrefix = (TextView) findViewById(C0706R.id.stopwatch_lap_hour_prefix);
        this.mLapHourPostfix = (TextView) findViewById(C0706R.id.stopwatch_lap_hour_postfix);
        this.mLapMinutePostfix = (TextView) findViewById(C0706R.id.stopwatch_lap_minute_postfix);
        this.mLapMinutePrefix = (TextView) findViewById(C0706R.id.stopwatch_lap_minute_prefix);
        this.mLapSecondPostfix = (TextView) findViewById(C0706R.id.stopwatch_lap_second_postfix);
        this.mLapSecondPrefix = (TextView) findViewById(C0706R.id.stopwatch_lap_second_prefix);
        this.mLapMillisPostfix = (TextView) findViewById(C0706R.id.stopwatch_lap_milli_postfix);
        this.mLapMillisPrefix = (TextView) findViewById(C0706R.id.stopwatch_lap_milli_prefix);
        this.mLapColonHMS = (TextView) findViewById(C0706R.id.stopwatch_lap_hms_colon);
        this.mLapColonMS = (TextView) findViewById(C0706R.id.stopwatch_lap_ms_colon);
        this.mLapPeriod = (TextView) findViewById(C0706R.id.stopwatch_lap_ms_period);
        String timeSeparatorText = ClockUtils.getTimeSeparatorText(this.mContext);
        this.mLapColonHMS.setText(timeSeparatorText);
        this.mLapColonMS.setText(timeSeparatorText);
        setFontFromOpenTheme();
        this.mLapHourPrefix.semSetHoverPopupType(0);
        this.mLapHourPostfix.semSetHoverPopupType(0);
        this.mLapMinutePostfix.semSetHoverPopupType(0);
        this.mLapMinutePrefix.semSetHoverPopupType(0);
        this.mLapSecondPostfix.semSetHoverPopupType(0);
        this.mLapSecondPrefix.semSetHoverPopupType(0);
        this.mLapMillisPostfix.semSetHoverPopupType(0);
        this.mLapMillisPrefix.semSetHoverPopupType(0);
        this.mLapColonHMS.semSetHoverPopupType(0);
        this.mLapColonMS.semSetHoverPopupType(0);
        this.mLapPeriod.semSetHoverPopupType(0);
        if (Feature.isTablet(this.mContext)) {
            enableMultiSelection(false);
        }
        updateTextSize(false);
    }

    public void updateTextSize(boolean isMultiwindow) {
        Log.secD("StopwatchLapTimeView", "updateTextSize()");
        setLapTimeLayout(this.mLapHourPrefix, isMultiwindow);
        setLapTimeLayout(this.mLapHourPostfix, isMultiwindow);
        setLapTimeLayout(this.mLapMinutePostfix, isMultiwindow);
        setLapTimeLayout(this.mLapMinutePrefix, isMultiwindow);
        setLapTimeLayout(this.mLapSecondPostfix, isMultiwindow);
        setLapTimeLayout(this.mLapSecondPrefix, isMultiwindow);
        setLapTimeLayout(this.mLapMillisPostfix, isMultiwindow);
        setLapTimeLayout(this.mLapMillisPrefix, isMultiwindow);
        setLapTimeLayout(this.mLapColonHMS, isMultiwindow);
        setLapTimeLayout(this.mLapColonMS, isMultiwindow);
        setLapTimeLayout(this.mLapPeriod, isMultiwindow);
    }

    private void setLapTimeLayout(TextView view, boolean isMultiwindow) {
        if (isMultiwindow) {
            Resources res = getResources();
            LayoutParams timeViewParam = (LayoutParams) view.getLayoutParams();
            if (timeViewParam != null) {
                timeViewParam.width = (res.getDimensionPixelSize(C0706R.dimen.stopwatch_laptime_textview_width) * 65) / 100;
                timeViewParam.height = -2;
            }
            view.setTextSize(0, ((float) (((double) getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_laptime_textsize)) * 5.5d)) / 10.0f);
            return;
        }
        ClockUtils.setLargeTextSize(this.mContext.getApplicationContext(), view, (float) this.mContext.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_laptime_textsize));
    }

    public int getLapTimeViewHeight() {
        this.mLapTimeView.measure(0, 0);
        return this.mLapTimeView.getMeasuredHeight();
    }

    public void updateLapTime(int lapHour, int lapMinute, int lapSecond, int lapMillis) {
        try {
            getLapTimeText(lapHour, lapMinute, lapSecond, lapMillis);
        } catch (Exception e) {
            Log.secE("StopwatchLapTimeView", "Exception : " + e.toString());
            Log.secD("StopwatchLapTimeView", "updateTimeView Exception");
            init();
        }
    }

    protected void getLapTimeText(int lapHour, int lapMinute, int lapSecond, int lapMillis) {
        if (this.mLapHourPrefix != null && this.mLapHourPostfix != null && this.mLapMinutePrefix != null && this.mLapMinutePostfix != null && this.mLapSecondPrefix != null && this.mLapSecondPostfix != null && this.mLapColonHMS != null && this.mLapColonMS != null && this.mLapPeriod != null && this.mLapMillisPrefix != null && this.mLapMillisPostfix != null) {
            if (lapHour > 0) {
                this.mLapHourPrefix.setVisibility(0);
                this.mLapHourPostfix.setVisibility(0);
                this.mLapColonHMS.setVisibility(0);
                ClockUtils.setNumber(this.mLapHourPrefix, lapHour / 10);
                ClockUtils.setNumber(this.mLapHourPostfix, lapHour % 10);
            } else {
                this.mLapHourPrefix.setVisibility(8);
                this.mLapHourPostfix.setVisibility(8);
                this.mLapColonHMS.setVisibility(8);
            }
            ClockUtils.setNumber(this.mLapMinutePrefix, lapMinute / 10);
            ClockUtils.setNumber(this.mLapMinutePostfix, lapMinute % 10);
            ClockUtils.setNumber(this.mLapSecondPrefix, lapSecond / 10);
            ClockUtils.setNumber(this.mLapSecondPostfix, lapSecond % 10);
            ClockUtils.setNumber(this.mLapMillisPrefix, lapMillis / 10);
            ClockUtils.setNumber(this.mLapMillisPostfix, lapMillis % 10);
        }
    }

    private void setFontFromOpenTheme() {
        Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (font == null) {
            font = Typeface.create("sans-serif-light", 0);
        }
        this.mLapHourPrefix.setTypeface(font);
        this.mLapHourPostfix.setTypeface(font);
        this.mLapMinutePrefix.setTypeface(font);
        this.mLapMinutePostfix.setTypeface(font);
        this.mLapSecondPrefix.setTypeface(font);
        this.mLapSecondPostfix.setTypeface(font);
        this.mLapMillisPostfix.setTypeface(font);
        this.mLapMillisPrefix.setTypeface(font);
        this.mLapColonHMS.setTypeface(font);
        this.mLapColonMS.setTypeface(font);
        this.mLapPeriod.setTypeface(font);
    }

    public void destroy() {
        this.mContext = null;
        this.mLapHourPrefix = null;
        this.mLapHourPostfix = null;
        this.mLapMinutePrefix = null;
        this.mLapMinutePostfix = null;
        this.mLapSecondPrefix = null;
        this.mLapSecondPostfix = null;
        this.mLapMillisPostfix = null;
        this.mLapMillisPrefix = null;
        this.mLapColonHMS = null;
        this.mLapColonMS = null;
        this.mLapPeriod = null;
    }

    public void enableMultiSelection(boolean enable) {
        if (this.mLapHourPrefix != null && this.mLapHourPostfix != null && this.mLapMinutePrefix != null && this.mLapMinutePostfix != null && this.mLapSecondPrefix != null && this.mLapSecondPostfix != null && this.mLapColonHMS != null && this.mLapColonMS != null && this.mLapPeriod != null && this.mLapMillisPrefix != null && this.mLapMillisPostfix != null) {
            this.mLapHourPrefix.semSetMultiSelectionEnabled(enable);
            this.mLapHourPostfix.semSetMultiSelectionEnabled(enable);
            this.mLapMinutePrefix.semSetMultiSelectionEnabled(enable);
            this.mLapMinutePostfix.semSetMultiSelectionEnabled(enable);
            this.mLapSecondPrefix.semSetMultiSelectionEnabled(enable);
            this.mLapSecondPostfix.semSetMultiSelectionEnabled(enable);
            this.mLapMillisPostfix.semSetMultiSelectionEnabled(enable);
            this.mLapMillisPrefix.semSetMultiSelectionEnabled(enable);
            this.mLapColonHMS.semSetMultiSelectionEnabled(enable);
            this.mLapColonMS.semSetMultiSelectionEnabled(enable);
            this.mLapPeriod.semSetMultiSelectionEnabled(enable);
        }
    }
}
