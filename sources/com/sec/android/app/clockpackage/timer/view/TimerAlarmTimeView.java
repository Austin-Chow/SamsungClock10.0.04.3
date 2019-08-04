package com.sec.android.app.clockpackage.timer.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.C0728R;

public class TimerAlarmTimeView extends LinearLayout {
    private TextView mColonHMS;
    private TextView mColonMS;
    private Context mContext = null;
    private TextView mHourPostfix;
    private TextView mHourPrefix;
    private TextView mMinusTextView;
    private TextView mMinutePostfix;
    private TextView mMinutePrefix;
    private TextView mSecondPostfix;
    private TextView mSecondPrefix;

    public TimerAlarmTimeView(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimerAlarmTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void init(int hour, int minute, int second) {
        Log.secD("TimerAlarmTimeView", "init");
        removeAllViewsInLayout();
        LayoutInflater.from(this.mContext).inflate(C0728R.layout.timer_timeview_alert, this, true);
        this.mHourPrefix = (TextView) findViewById(C0728R.id.timer_hour_prefix);
        this.mHourPostfix = (TextView) findViewById(C0728R.id.timer_hour_postfix);
        this.mMinutePrefix = (TextView) findViewById(C0728R.id.timer_minute_prefix);
        this.mMinutePostfix = (TextView) findViewById(C0728R.id.timer_minute_postfix);
        this.mSecondPrefix = (TextView) findViewById(C0728R.id.timer_second_prefix);
        this.mSecondPostfix = (TextView) findViewById(C0728R.id.timer_second_postfix);
        this.mColonHMS = (TextView) findViewById(C0728R.id.timer_hms_colon);
        this.mColonMS = (TextView) findViewById(C0728R.id.timer_ms_colon);
        this.mMinusTextView = (TextView) findViewById(C0728R.id.timer_alert_minus);
        String timeSeparatorText = ClockUtils.getTimeSeparatorText(this.mContext);
        this.mColonHMS.setText(timeSeparatorText);
        this.mColonMS.setText(timeSeparatorText);
        updateTimeView(hour, minute, second);
        if (this.mMinusTextView != null) {
            int i;
            this.mMinusTextView.setText("-");
            TextView textView = this.mMinusTextView;
            if (second > 0) {
                i = 0;
            } else {
                i = 4;
            }
            textView.setVisibility(i);
        }
        TextView[] textViews = new TextView[]{this.mHourPrefix, this.mHourPostfix, this.mMinutePrefix, this.mMinutePostfix, this.mSecondPrefix, this.mSecondPostfix, this.mColonHMS, this.mColonMS};
        setTextSize(textViews);
        setFontFromOpenTheme(textViews);
    }

    public void updateTimeView(int hour, int minute, int second) {
        if (second > 0 && this.mMinusTextView != null) {
            this.mMinusTextView.setVisibility(0);
        }
        if (this.mHourPrefix != null) {
            setNumber(this.mHourPrefix, hour / 10);
        }
        if (this.mHourPostfix != null) {
            setNumber(this.mHourPostfix, hour % 10);
        }
        if (this.mMinutePrefix != null) {
            setNumber(this.mMinutePrefix, minute / 10);
        }
        if (this.mMinutePostfix != null) {
            setNumber(this.mMinutePostfix, minute % 10);
        }
        if (this.mSecondPrefix != null) {
            setNumber(this.mSecondPrefix, second / 10);
        }
        if (this.mSecondPostfix != null) {
            setNumber(this.mSecondPostfix, second % 10);
        }
    }

    private void setNumber(TextView tv, int time) {
        try {
            tv.setText(ClockUtils.toDigitString(time));
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.secE("TimerAlarmTimeView", "Exception : " + e.toString());
        }
    }

    private void setTextSize(TextView[] textViews) {
        float textSize = (float) getResources().getDimensionPixelSize(C0728R.dimen.timer_alert_hms_textview_number_textsize);
        if (StateUtils.isHighTextContrastEnabled(this.mContext)) {
            textSize = (float) getResources().getDimensionPixelSize(C0728R.dimen.timer_alert_hms_textview_number_textsize_high_contrast);
        }
        for (TextView textView : textViews) {
            if (textView != null) {
                textView.setTextSize(0, textSize);
            }
        }
    }

    private void setFontFromOpenTheme(TextView[] textViews) {
        int i = 0;
        Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
        Typeface minusFont = font;
        if (font == null) {
            font = Typeface.create("sans-serif-light", 0);
            minusFont = Typeface.create("sec-roboto-light", 0);
        }
        int length = textViews.length;
        while (i < length) {
            TextView textView = textViews[i];
            if (textView != null) {
                textView.setTypeface(font);
            }
            i++;
        }
        if (this.mMinusTextView != null) {
            this.mMinusTextView.setTypeface(minusFont);
        }
    }

    public void onDestroy() {
        this.mContext = null;
        this.mHourPrefix = null;
        this.mHourPostfix = null;
        this.mMinutePrefix = null;
        this.mMinutePostfix = null;
        this.mSecondPrefix = null;
        this.mSecondPostfix = null;
        this.mColonHMS = null;
        this.mColonMS = null;
        this.mMinusTextView = null;
    }
}
