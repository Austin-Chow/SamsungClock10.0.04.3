package com.sec.android.app.clockpackage.timer.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.samsung.android.view.animation.SineInOut33;
import com.samsung.android.view.animation.SineInOut60;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerTimeViewListener;

public class TimerTimeView extends LinearLayout {
    private TextView mColonHMS;
    private TextView mColonMS;
    private Context mContext;
    private int mHour;
    private TextView mHourPostfix;
    private TextView mHourPrefix;
    private int mMinute;
    private TextView mMinutePostfix;
    private TextView mMinutePrefix;
    private int mSecond;
    private TextView mSecondPostfix;
    private TextView mSecondPrefix;
    private TimerTimeViewListener mTimerTimeViewListener;

    public TimerTimeView(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimerTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public TimerTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void setTimeTextView(long time) {
        Log.secD("TimerTimeView", "setTimeTextView()");
        LayoutInflater picker_inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        removeAllViews();
        picker_inflater.inflate(C0728R.layout.timer_timeview_hms, this);
        this.mHourPrefix = (TextView) findViewById(C0728R.id.timer_hour_prefix);
        this.mHourPostfix = (TextView) findViewById(C0728R.id.timer_hour_postfix);
        this.mMinutePrefix = (TextView) findViewById(C0728R.id.timer_minute_prefix);
        this.mMinutePostfix = (TextView) findViewById(C0728R.id.timer_minute_postfix);
        this.mSecondPrefix = (TextView) findViewById(C0728R.id.timer_second_prefix);
        this.mSecondPostfix = (TextView) findViewById(C0728R.id.timer_second_postfix);
        this.mColonHMS = (TextView) findViewById(C0728R.id.timer_hms_colon);
        this.mColonMS = (TextView) findViewById(C0728R.id.timer_ms_colon);
        this.mHourPrefix.semSetHoverPopupType(0);
        this.mHourPostfix.semSetHoverPopupType(0);
        this.mMinutePrefix.semSetHoverPopupType(0);
        this.mMinutePostfix.semSetHoverPopupType(0);
        this.mSecondPrefix.semSetHoverPopupType(0);
        this.mSecondPostfix.semSetHoverPopupType(0);
        this.mColonHMS.semSetHoverPopupType(0);
        this.mColonMS.semSetHoverPopupType(0);
        String timeSeparator = ClockUtils.getTimeSeparatorText(this.mContext);
        this.mColonHMS.setText(timeSeparator);
        this.mColonMS.setText(timeSeparator);
        if (Feature.isTablet(this.mContext)) {
            enableMultiSelection(false);
        }
        initTimeTextView(time);
        setFontFromOpenTheme();
        updateLayout();
    }

    public void initTimeTextView(long time) {
        Log.secD("TimerTimeView", "initTimeView() / remainMillis = " + time);
        long remainMillis = time;
        int milliSecond = (int) (remainMillis % 1000);
        if (milliSecond > Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            remainMillis += (long) (1000 - milliSecond);
        }
        this.mHour = (int) (remainMillis / 3600000);
        this.mMinute = (int) ((remainMillis % 3600000) / 60000);
        this.mSecond = (int) ((remainMillis % 60000) / 1000);
        setTimeToView(this.mHourPrefix, this.mHourPostfix, this.mHour);
        setTimeToView(this.mMinutePrefix, this.mMinutePostfix, this.mMinute);
        setTimeToView(this.mSecondPrefix, this.mSecondPostfix, this.mSecond);
        setTimeViewContentDescription();
    }

    public void setTimerTimeViewListener(TimerTimeViewListener listener) {
        this.mTimerTimeViewListener = listener;
    }

    public void destroyView() {
        this.mHourPrefix = null;
        this.mHourPostfix = null;
        this.mMinutePrefix = null;
        this.mMinutePostfix = null;
        this.mSecondPrefix = null;
        this.mSecondPostfix = null;
        this.mColonHMS = null;
        this.mColonMS = null;
        removeAllViews();
    }

    public void changeTime(int hour, int minute, int second) {
        if (!(this.mSecondPostfix == null || second % 10 == this.mSecond % 10)) {
            if (second % 10 == 9) {
                setNumber(this.mSecondPostfix, 9);
            } else {
                setNumber(this.mSecondPostfix, second % 10);
            }
        }
        if (second / 10 == this.mSecond / 10) {
            setNumber(this.mSecondPrefix, second / 10);
        } else if (second / 10 == 5) {
            setNumber(this.mSecondPrefix, 5);
        } else {
            setNumber(this.mSecondPrefix, second / 10);
        }
        if (minute % 10 == this.mMinute % 10) {
            setNumber(this.mMinutePostfix, minute % 10);
        } else if (minute % 10 == 9) {
            setNumber(this.mMinutePostfix, 9);
        } else {
            setNumber(this.mMinutePostfix, minute % 10);
        }
        if (minute / 10 == this.mMinute / 10) {
            setNumber(this.mMinutePrefix, minute / 10);
        } else if (minute / 10 == 5) {
            setNumber(this.mMinutePrefix, 5);
        } else {
            setNumber(this.mMinutePrefix, minute / 10);
        }
        if (hour % 10 == this.mHour % 10) {
            setNumber(this.mHourPostfix, hour % 10);
        } else if (hour % 10 == 9) {
            setNumber(this.mHourPostfix, 9);
        } else {
            setNumber(this.mHourPostfix, hour % 10);
        }
        if (hour / 10 == this.mHour / 10) {
            setNumber(this.mHourPrefix, hour / 10);
        } else if (hour / 10 == 5) {
            setNumber(this.mHourPrefix, 5);
        } else {
            setNumber(this.mHourPrefix, hour / 10);
        }
        this.mSecond = second;
        this.mMinute = minute;
        this.mHour = hour;
        setTimeViewContentDescription();
    }

    private void setTimeToView(TextView prefix, TextView postfix, int time) {
        if (prefix != null) {
            setNumber(prefix, time / 10);
        }
        if (postfix != null) {
            setNumber(postfix, time % 10);
        }
    }

    private void setNumber(TextView tv, int time) {
        if (tv != null) {
            try {
                tv.setText(ClockUtils.toDigitString(time));
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.secE("TimerTimeView", "Exception : " + e.toString());
            }
        }
    }

    private void setFontFromOpenTheme() {
        Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (font == null) {
            font = Typeface.create("sans-serif-light", 0);
        }
        if (this.mHourPrefix != null) {
            this.mHourPrefix.setTypeface(font);
        }
        if (this.mHourPostfix != null) {
            this.mHourPostfix.setTypeface(font);
        }
        if (this.mMinutePrefix != null) {
            this.mMinutePrefix.setTypeface(font);
        }
        if (this.mMinutePostfix != null) {
            this.mMinutePostfix.setTypeface(font);
        }
        if (this.mSecondPrefix != null) {
            this.mSecondPrefix.setTypeface(font);
        }
        if (this.mSecondPostfix != null) {
            this.mSecondPostfix.setTypeface(font);
        }
        if (this.mColonHMS != null) {
            this.mColonHMS.setTypeface(font);
        }
        if (this.mColonMS != null) {
            this.mColonMS.setTypeface(font);
        }
    }

    private void setTimeViewContentDescription() {
        Resources res = this.mContext.getResources();
        setContentDescription((this.mHour != 0 ? Integer.toString(this.mHour) + ' ' + res.getString(C0728R.string.timer_hour) + ' ' : "") + Integer.toString(this.mMinute) + ' ' + res.getString(C0728R.string.timer_minute) + ' ' + Integer.toString(this.mSecond) + ' ' + res.getString(C0728R.string.timer_second));
    }

    private void enableMultiSelection(boolean enable) {
        if (this.mHourPrefix != null) {
            this.mHourPrefix.semSetMultiSelectionEnabled(enable);
        }
        if (this.mHourPostfix != null) {
            this.mHourPostfix.semSetMultiSelectionEnabled(enable);
        }
        if (this.mMinutePrefix != null) {
            this.mMinutePrefix.semSetMultiSelectionEnabled(enable);
        }
        if (this.mMinutePostfix != null) {
            this.mMinutePostfix.semSetMultiSelectionEnabled(enable);
        }
        if (this.mSecondPrefix != null) {
            this.mSecondPrefix.semSetMultiSelectionEnabled(enable);
        }
        if (this.mSecondPostfix != null) {
            this.mSecondPostfix.semSetMultiSelectionEnabled(enable);
        }
        if (this.mColonHMS != null) {
            this.mColonHMS.semSetMultiSelectionEnabled(enable);
        }
        if (this.mColonMS != null) {
            this.mColonMS.semSetMultiSelectionEnabled(enable);
        }
    }

    public void updateLayout() {
        Log.secD("TimerTimeView", "updateLayout()");
        setTimeLayout(this.mHourPrefix);
        setTimeLayout(this.mHourPostfix);
        setTimeLayout(this.mMinutePrefix);
        setTimeLayout(this.mMinutePostfix);
        setTimeLayout(this.mSecondPrefix);
        setTimeLayout(this.mSecondPostfix);
        setBackgroundLayout((RelativeLayout) findViewById(C0728R.id.timer_hour_bg));
        setBackgroundLayout((RelativeLayout) findViewById(C0728R.id.timer_minute_bg));
        setBackgroundLayout((RelativeLayout) findViewById(C0728R.id.timer_second_bg));
        setColonLayout(this.mColonHMS);
        setColonLayout(this.mColonMS);
    }

    private void setTimeLayout(TextView view) {
        if (view != null) {
            LayoutParams timeParam = (LayoutParams) view.getLayoutParams();
            if (timeParam != null) {
                timeParam.width = getWidth(false);
                timeParam.height = -2;
            }
            view.setTextSize(0, (float) getResources().getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_time_textsize));
        }
    }

    private void setBackgroundLayout(RelativeLayout layout) {
        if (layout != null) {
            LayoutParams backgroundParam = (LayoutParams) layout.getLayoutParams();
            if (backgroundParam != null) {
                backgroundParam.width = -2;
                backgroundParam.height = -2;
            }
        }
    }

    private void setColonLayout(TextView view) {
        if (view != null) {
            LayoutParams colonParam = (LayoutParams) view.getLayoutParams();
            if (colonParam != null) {
                colonParam.width = getWidth(true);
                colonParam.height = -2;
            }
            view.setTextSize(0, (float) getResources().getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_colon_textsize));
        }
    }

    public void startAnimation(boolean isStart) {
        setVisibility(0);
        timePositionAnimatorForStart(findViewById(C0728R.id.timer_hour_bg), isStart).start();
        timePositionAnimatorForStart(findViewById(C0728R.id.timer_minute_bg), isStart).start();
        timePositionAnimatorForStart(findViewById(C0728R.id.timer_second_bg), isStart).start();
        timePositionAnimatorForStart(this.mColonHMS, isStart).start();
        timePositionAnimatorForStart(this.mColonMS, isStart).start();
        timeScaleAnimatorForStart(this.mHourPrefix, isStart).start();
        timeScaleAnimatorForStart(this.mHourPostfix, isStart).start();
        timeScaleAnimatorForStart(this.mMinutePrefix, isStart).start();
        timeScaleAnimatorForStart(this.mMinutePostfix, isStart).start();
        timeScaleAnimatorForStart(this.mSecondPrefix, isStart).start();
        timeScaleAnimatorForStart(this.mSecondPostfix, isStart).start();
        timeScaleAnimatorForStart(this.mColonHMS, isStart).start();
        timeScaleAnimatorForStart(this.mColonMS, isStart).start();
        timeOpacityAnimatorForStart(isStart).start();
    }

    private ValueAnimator timePositionAnimatorForStart(final View view, boolean isStart) {
        int startWidth;
        int endWidth;
        Resources res = getResources();
        if (view instanceof RelativeLayout) {
            if (isStart) {
                startWidth = this.mTimerTimeViewListener.onGetPickerWidth(false);
                endWidth = getWidth(false) * 2;
            } else {
                startWidth = getWidth(false) * 2;
                endWidth = this.mTimerTimeViewListener.onGetPickerWidth(false);
            }
        } else if (isStart) {
            startWidth = this.mTimerTimeViewListener.onGetPickerWidth(true);
            endWidth = getWidth(true);
        } else {
            startWidth = getWidth(true);
            endWidth = this.mTimerTimeViewListener.onGetPickerWidth(true);
        }
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{startWidth, endWidth});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                layoutParams.width = ((Integer) animation.getAnimatedValue()).intValue();
                view.setLayoutParams(layoutParams);
            }
        });
        animator.setInterpolator(new SineInOut60());
        animator.setDuration(300);
        animator.setTarget(view);
        return animator;
    }

    private ValueAnimator timeScaleAnimatorForStart(final TextView view, boolean isStart) {
        int startTextSize;
        int endTextSize;
        Resources res = getResources();
        if (isStart) {
            startTextSize = res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_colon_textsize);
            endTextSize = res.getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_colon_textsize);
        } else {
            startTextSize = res.getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_colon_textsize);
            endTextSize = res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_colon_textsize);
        }
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{startTextSize, endTextSize});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTextSize(0, (float) ((Integer) animation.getAnimatedValue()).intValue());
            }
        });
        animator.setInterpolator(new SineInOut33());
        animator.setDuration(300);
        animator.setTarget(view);
        return animator;
    }

    private ObjectAnimator timeOpacityAnimatorForStart(final boolean isStart) {
        ObjectAnimator animator;
        final RelativeLayout timeViewLayout = (RelativeLayout) findViewById(C0728R.id.timer_timeview_layout);
        if (isStart) {
            animator = ObjectAnimator.ofFloat(timeViewLayout, "alpha", new float[]{0.0f, 1.0f}).setDuration(300);
        } else {
            animator = ObjectAnimator.ofFloat(timeViewLayout, "alpha", new float[]{1.0f, 0.0f}).setDuration(200);
        }
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (timeViewLayout != null) {
                    timeViewLayout.setVisibility(0);
                    timeViewLayout.setAlpha(isStart ? 0.0f : 1.0f);
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (timeViewLayout != null) {
                    if (!isStart) {
                        timeViewLayout.setVisibility(8);
                    }
                    timeViewLayout.setAlpha(1.0f);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        return animator;
    }

    private int getWidth(boolean isColon) {
        int width;
        Resources res = getResources();
        int circleWidth = this.mTimerTimeViewListener.onGetCircleWidth();
        if (circleWidth >= res.getDimensionPixelSize(C0728R.dimen.timer_circle_bg_min_width)) {
            width = (circleWidth * 3) / 26;
        } else if (res.getConfiguration().orientation == 1) {
            width = (((int) (((double) res.getDisplayMetrics().widthPixels) * 0.87d)) * 3) / 26;
        } else {
            width = ((int) (((double) res.getDisplayMetrics().widthPixels) * 0.507d)) / 10;
        }
        width = Math.min(Math.max(width, res.getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_time_width)), res.getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_time_max_width));
        return isColon ? (width * 2) / 3 : width;
    }
}
