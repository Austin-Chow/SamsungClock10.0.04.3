package com.sec.android.app.clockpackage.timer.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.view.Cover;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerCoverListener;
import java.util.Timer;
import java.util.TimerTask;

public class TimerCover extends Cover {
    private boolean isHMS = false;
    private CountDownTimer mCountDownTimer;
    private TimerCoverDialog mDialog;
    private long mElapsedMillis = 0;
    private int mHour = 0;
    private TextView mHourPostfix;
    private TextView mHourPrefix;
    private long mHunElapsedMillis = 0;
    private int mMinute = 0;
    private TextView mMinutePostfix;
    private TextView mMinutePrefix;
    private View mRestartButton;
    private int mSecond = 0;
    private TextView mSecondPostfix;
    private TextView mSecondPrefix;
    private Timer mStopLedTimer = null;
    private LinearLayout mTimeLayout = null;
    private TimerCoverListener mTimerCoverListener;
    private String mTimerNameString = "";

    private class TimerCoverDialog extends CoverDialog {

        /* renamed from: com.sec.android.app.clockpackage.timer.view.TimerCover$TimerCoverDialog$1 */
        class C07401 implements OnClickListener {
            C07401() {
            }

            public void onClick(View v) {
                Log.secD("TimerCover", "mRestartButton onClick");
                TimerCover.this.mTimerCoverListener.onRestartAlert();
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.timer.view.TimerCover$TimerCoverDialog$2 */
        class C07412 extends TimerTask {
            C07412() {
            }

            public void run() {
                Log.secD("TimerCover", "send broadcast to LED side");
                if (TimerCover.this.mCoverType == 7) {
                    TimerCover.this.mTimerCoverListener.onSendActionTimerStoppedInAlert("com.sec.android.cover.ledcover");
                } else if (TimerCover.this.mCoverType == 11) {
                    TimerCover.this.mTimerCoverListener.onSendActionTimerStoppedInAlert("com.sec.android.cover.neoncover");
                }
            }
        }

        private TimerCoverDialog(Context context, int coverSize) {
            super(context, coverSize);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            if (coverSize == 0) {
                inflater.inflate(C0728R.layout.timer_cover_view, (ViewGroup) TimerCover.this.mCoverView);
            } else {
                inflater.inflate(C0728R.layout.timer_cover_clear_view, (ViewGroup) TimerCover.this.mCoverView);
            }
        }

        public void initViews() {
            initTimeViews();
            TimerCover.this.mRestartButton = findViewById(C0728R.id.timer_restart_btn);
            if (TimerCover.this.mRestartButton == null) {
                Log.secD("TimerCover", "mRestartButton null");
            } else {
                TimerCover.this.mRestartButton.setOnClickListener(new C07401());
            }
            TextView timerNameView = (TextView) findViewById(C0728R.id.timer_name);
            if (timerNameView != null) {
                Log.secD("TimerCover", "mTimerNameString : " + TimerCover.this.mTimerNameString);
                if (TimerCover.this.mTimerNameString == null || "".equals(TimerCover.this.mTimerNameString)) {
                    timerNameView.setText(C0728R.string.timer_times_out);
                } else {
                    timerNameView.setText(TimerCover.this.mTimerNameString);
                }
            }
            TextView restartView = (TextView) findViewById(C0728R.id.restartBtn_textview);
            if (restartView != null) {
                ClockUtils.setLargeTextSize(TimerCover.this.mContext, restartView, (float) TimerCover.this.mContext.getResources().getDimensionPixelSize(C0728R.dimen.alarm_snooze_text_size));
            }
            TimerCover.this.mCoverView.setVisibility(0);
            alertStart();
        }

        protected int ccTabSelectorId() {
            return C0728R.id.timer_cc_tab_selector;
        }

        private void initTimeViews() {
            Log.secD("TimerCover", "initTimeViews");
            if (TimerCover.this.mTimerCoverListener.onGetOffHookElapsedMillis() != 0) {
                TimerCover.this.mElapsedMillis = System.currentTimeMillis() - TimerCover.this.mTimerCoverListener.onGetOffHookElapsedMillis();
            }
            TimerCover.this.mHour = (int) (TimerCover.this.mElapsedMillis / 3600000);
            TimerCover.this.mMinute = (int) ((TimerCover.this.mElapsedMillis % 3600000) / 60000);
            TimerCover.this.mSecond = (int) ((TimerCover.this.mElapsedMillis % 60000) / 1000);
            LayoutInflater inflater = (LayoutInflater) TimerCover.this.mContext.getSystemService("layout_inflater");
            TimerCover.this.mTimeLayout = (LinearLayout) findViewById(C0728R.id.timer_cover_time_view);
            TimerCover.this.mTimeLayout.removeAllViewsInLayout();
            switch (TimerCover.this.mCoverViewSize) {
                case 0:
                    inflater.inflate(C0728R.layout.timer_cover_timeview_hms, TimerCover.this.mTimeLayout);
                    break;
                case 2:
                case 4:
                    inflater.inflate(C0728R.layout.timer_cover_clear_timeview_hms, TimerCover.this.mTimeLayout);
                    break;
            }
            TimerCover.this.mHourPrefix = (TextView) findViewById(C0728R.id.timer_hour_prefix);
            TimerCover.this.mHourPostfix = (TextView) findViewById(C0728R.id.timer_hour_postfix);
            TimerCover.this.mMinutePrefix = (TextView) findViewById(C0728R.id.timer_minute_prefix);
            TimerCover.this.mMinutePostfix = (TextView) findViewById(C0728R.id.timer_minute_postfix);
            TimerCover.this.mSecondPrefix = (TextView) findViewById(C0728R.id.timer_second_prefix);
            TimerCover.this.mSecondPostfix = (TextView) findViewById(C0728R.id.timer_second_postfix);
            String timeSeparatorText = ClockUtils.getTimeSeparatorText(TimerCover.this.mContext);
            TextView colonMS = (TextView) findViewById(C0728R.id.timer_ms_colon);
            ((TextView) findViewById(C0728R.id.timer_hms_colon)).setText(timeSeparatorText);
            colonMS.setText(timeSeparatorText);
            setTextSize(new TextView[]{TimerCover.this.mHourPrefix, TimerCover.this.mHourPostfix, TimerCover.this.mMinutePrefix, TimerCover.this.mMinutePostfix, TimerCover.this.mSecondPrefix, TimerCover.this.mSecondPostfix, colonHMS, colonMS});
            setTimeView(TimerCover.this.mHour, TimerCover.this.mMinute, TimerCover.this.mSecond);
        }

        private void setTextSize(TextView[] textViews) {
            float textSize = (float) TimerCover.this.mContext.getResources().getDimensionPixelSize(C0728R.dimen.timer_alert_hms_textview_number_textsize);
            if (StateUtils.isHighTextContrastEnabled(TimerCover.this.mContext)) {
                textSize = (float) TimerCover.this.mContext.getResources().getDimensionPixelSize(C0728R.dimen.timer_alert_hms_textview_number_textsize_high_contrast);
            }
            for (TextView textView : textViews) {
                if (textView != null) {
                    textView.setTextSize(0, textSize);
                }
            }
        }

        private void setTimeView(int hour, int minute, int second) {
            Log.secD("TimerCover", "SetTimeView");
            if (TimerCover.this.mHourPrefix != null) {
                setNumber(TimerCover.this.mHourPrefix, hour / 10);
            }
            if (TimerCover.this.mHourPostfix != null) {
                setNumber(TimerCover.this.mHourPostfix, hour % 10);
            }
            if (TimerCover.this.mMinutePrefix != null) {
                setNumber(TimerCover.this.mMinutePrefix, minute / 10);
            }
            if (TimerCover.this.mMinutePostfix != null) {
                setNumber(TimerCover.this.mMinutePostfix, minute % 10);
            }
            if (TimerCover.this.mSecondPrefix != null) {
                setNumber(TimerCover.this.mSecondPrefix, second / 10);
            }
            if (TimerCover.this.mSecondPostfix != null) {
                setNumber(TimerCover.this.mSecondPostfix, second % 10);
            }
            if (TimerCover.this.mTimeLayout != null) {
                TimerCover.this.mTimeLayout.setContentDescription(TimerCover.this.mTimerCoverListener.onGetContentDescription(hour, minute, second));
            }
        }

        private void setNumber(TextView tv, int time) {
            try {
                tv.setText(ClockUtils.toDigitString(time));
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.secE("TimerCover", "Exception : " + e.toString());
            }
        }

        private void alertStart() {
            Log.secD("TimerCover", "alertStart");
            if (TimerCover.this.mHunElapsedMillis != 0) {
                TimerCover.this.mElapsedMillis = TimerCover.this.mHunElapsedMillis;
                TimerCover.this.mHunElapsedMillis = 0;
            }
            setTimer(359999990 - TimerCover.this.mElapsedMillis);
            if (TimerCover.this.mCountDownTimer != null) {
                TimerCover.this.mCountDownTimer.start();
            }
            if (!TimerCover.this.mCoverState.getSwitchState()) {
                if (TimerCover.this.mCoverType == 7 || TimerCover.this.mCoverType == 11) {
                    Log.secD("TimerCover", "when TYPE_LED_COVER & TYPE_NEON_COVER cover close , after 1 min send broadcast to LED side");
                    if (TimerCover.this.mStopLedTimer != null) {
                        TimerCover.this.mStopLedTimer.cancel();
                    }
                    TimerCover.this.mStopLedTimer = new Timer();
                    TimerCover.this.mStopLedTimer.schedule(new C07412(), 60000);
                }
            }
        }

        private void setTimer(long time) {
            Log.secD("TimerCover", "setTimer");
            if (TimerCover.this.mCountDownTimer != null) {
                TimerCover.this.mCountDownTimer.cancel();
                TimerCover.this.mCountDownTimer = null;
            }
            TimerCover.this.mCountDownTimer = new CountDownTimer(time, 50) {
                public void onTick(long millisUntilFinished) {
                    TimerCover.this.mElapsedMillis = 359999990 - millisUntilFinished;
                    TimerCoverDialog.this.updateTimeView();
                }

                public void onFinish() {
                    Log.secD("TimerCover", "CountDownTimer onFinish");
                }
            };
        }

        private void updateTimeView() {
            int hour = (int) (TimerCover.this.mElapsedMillis / 3600000);
            int minute = (int) ((TimerCover.this.mElapsedMillis % 3600000) / 60000);
            int second = (int) ((TimerCover.this.mElapsedMillis % 60000) / 1000);
            int milli_second = (int) (TimerCover.this.mElapsedMillis % 1000);
            TextView minus = (TextView) findViewById(C0728R.id.timer_alert_minus);
            minus.setText("-");
            minus.setVisibility(second > 0 ? 0 : 4);
            if (!(hour == 0 || TimerCover.this.isHMS)) {
                initTimeViews();
                TimerCover.this.isHMS = true;
                Log.secD("TimerCover", "isHMS");
            }
            if (TimerCover.this.mSecond != second && milli_second < Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                if (TimerCover.this.mHourPrefix != null) {
                    setNumber(TimerCover.this.mHourPrefix, hour / 10);
                }
                if (TimerCover.this.mHourPostfix != null) {
                    setNumber(TimerCover.this.mHourPostfix, hour % 10);
                }
                if (TimerCover.this.mMinutePrefix != null) {
                    setNumber(TimerCover.this.mMinutePrefix, minute / 10);
                }
                if (TimerCover.this.mMinutePostfix != null) {
                    setNumber(TimerCover.this.mMinutePostfix, minute % 10);
                }
                if (TimerCover.this.mSecondPrefix != null) {
                    setNumber(TimerCover.this.mSecondPrefix, second / 10);
                }
                if (TimerCover.this.mSecondPostfix != null) {
                    setNumber(TimerCover.this.mSecondPostfix, second % 10);
                }
                if (TimerCover.this.mTimeLayout != null) {
                    TimerCover.this.mTimeLayout.setContentDescription(TimerCover.this.mTimerCoverListener.onGetContentDescription(hour, minute, second));
                }
                TimerCover.this.mHour = hour;
                TimerCover.this.mMinute = minute;
                TimerCover.this.mSecond = second;
            }
        }
    }

    public TimerCover(Context mContext, int coverSize, ScoverState state, int type) {
        super(mContext, coverSize, state, type);
        this.mDialog = new TimerCoverDialog(mContext, coverSize);
    }

    public TimerCoverDialog coverDialog() {
        return this.mDialog;
    }

    protected int coverViewId(int coverSize) {
        if (coverSize == 0) {
            return C0728R.id.timer_cover_view;
        }
        return C0728R.id.timer_cover_clear_view;
    }

    public View getButton() {
        return this.mRestartButton;
    }

    protected void finishAlert(boolean forceFinish) {
        this.mTimerCoverListener.onFinishAlert();
    }

    protected boolean isOptionalButtionVisible() {
        return true;
    }

    public void setTimerValues(String timerName, long hunElapsedMillis) {
        this.mTimerNameString = timerName;
        this.mHunElapsedMillis = hunElapsedMillis;
    }

    public void setListener(TimerCoverListener listener) {
        this.mTimerCoverListener = listener;
    }

    public void dismissDialog() {
        if (this.mStopLedTimer != null) {
            this.mStopLedTimer.cancel();
        }
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.cancel();
            this.mCountDownTimer = null;
        }
        super.dismissDialog();
    }
}
