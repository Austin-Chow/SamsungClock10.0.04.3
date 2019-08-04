package com.sec.android.app.clockpackage.timer.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.samsung.android.view.animation.SineInOut60;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.C0728R;
import java.lang.ref.WeakReference;

public class TimerCircleView extends ImageView {
    private static long sDrawInterval;
    private static int sHour;
    private static long sInputMillis = 0;
    private static boolean sIsStartState = false;
    private static int sMinute;
    private static long sRemainMillis = 0;
    private static int sSecond;
    private int mAlpha = 255;
    private float mCenterX;
    private float mCenterY;
    private int mCircleAlertColor;
    private ValueAnimator mCircleAlertColorAnimator;
    private Paint mCircleBGPaint = null;
    private int mCircleBackgroundColor;
    private int mCircleDrawColor;
    private int mCircleOnGoingColor;
    private Paint mCirclePaint = null;
    private float mCircleRadius;
    private Context mContext;
    private Paint mEndEffectPaint = null;
    private Handler mHandler = new NoLeakHandler();
    private float mLayoutWidth;
    private Paint mStartEffectPaint = null;

    /* renamed from: com.sec.android.app.clockpackage.timer.view.TimerCircleView$1 */
    class C07341 implements AnimatorUpdateListener {
        C07341() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            TimerCircleView.this.mAlpha = (int) (255.0f * ((Float) animation.getAnimatedValue()).floatValue());
            TimerCircleView.this.invalidate();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.view.TimerCircleView$5 */
    class C07385 implements AnimatorUpdateListener {
        C07385() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            TimerCircleView.this.mCircleAlertColor = ((Integer) animation.getAnimatedValue()).intValue();
            TimerCircleView.this.invalidate();
        }
    }

    private static final class NoLeakHandler extends Handler {
        private final WeakReference<TimerCircleView> mView;

        private NoLeakHandler(TimerCircleView view) {
            this.mView = new WeakReference(view);
        }

        public void handleMessage(Message msg) {
            TimerCircleView view = (TimerCircleView) this.mView.get();
            if (view != null) {
                view.invalidate();
                if (TimerCircleView.sIsStartState) {
                    sendEmptyMessageDelayed(0, TimerCircleView.sDrawInterval);
                }
            }
        }
    }

    public TimerCircleView(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimerCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public TimerCircleView(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);
        this.mContext = context;
    }

    private static void setDrawInterval() {
        sDrawInterval = (sInputMillis + 180) / 1000;
        if (sDrawInterval > 50) {
            sDrawInterval = 50;
        } else if (sDrawInterval < 10) {
            sDrawInterval = 10;
        }
    }

    public void init(long inputMillis, long remainMillis) {
        Log.secD("TimerCircleView", "init()");
        if (this.mCirclePaint == null) {
            this.mCirclePaint = new Paint();
        }
        this.mCirclePaint.setStyle(Style.STROKE);
        this.mCirclePaint.setAntiAlias(true);
        if (this.mStartEffectPaint == null) {
            this.mStartEffectPaint = new Paint();
        }
        this.mStartEffectPaint.setStyle(Style.STROKE);
        this.mStartEffectPaint.setAntiAlias(true);
        if (this.mEndEffectPaint == null) {
            this.mEndEffectPaint = new Paint();
        }
        this.mEndEffectPaint.setStyle(Style.STROKE);
        this.mEndEffectPaint.setAntiAlias(true);
        if (this.mCircleBGPaint == null) {
            this.mCircleBGPaint = new Paint();
        }
        this.mCircleBGPaint.setAntiAlias(true);
        this.mCircleBGPaint.setStyle(Style.STROKE);
        this.mCircleBackgroundColor = getResources().getColor(C0728R.color.timer_circle_bg_color, null);
        this.mCircleAlertColor = getResources().getColor(C0728R.color.timer_circle_alert_start_color, null);
        this.mCircleOnGoingColor = getResources().getColor(C0728R.color.timer_circle_ongoing_start_color, null);
        this.mCircleBGPaint.setColor(this.mCircleBackgroundColor);
        updateTime(inputMillis, remainMillis);
        setCircleSize();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.secD("TimerCircleView", "onConfigurationChanged() / newConfig = " + newConfig);
        setCircleSize();
        invalidate();
    }

    public void resumeView(boolean isStartState) {
        setCircleSize();
        if (isStartState) {
            start();
        }
    }

    public void updateLayout() {
        Log.secD("TimerCircleView", "updateLayout()");
        LayoutParams circleLayoutParam = (LayoutParams) getLayoutParams();
        if (getVisibility() == 4) {
            circleLayoutParam.width = -1;
            circleLayoutParam.height = -1;
        } else {
            circleLayoutParam.width = (int) this.mLayoutWidth;
            circleLayoutParam.height = (int) this.mLayoutWidth;
        }
        setLayoutParams(circleLayoutParam);
    }

    private void setCircleSize() {
        int strokeWidth = getResources().getDimensionPixelSize(C0728R.dimen.timer_circle_stroke_width);
        this.mLayoutWidth = (float) getCircleWidth();
        this.mCenterX = this.mLayoutWidth / 2.0f;
        this.mCenterY = this.mLayoutWidth / 2.0f;
        this.mCircleRadius = (this.mLayoutWidth / 2.0f) - ((float) strokeWidth);
        if (this.mCirclePaint != null) {
            this.mCirclePaint.setStrokeWidth((float) strokeWidth);
        }
        if (this.mStartEffectPaint != null) {
            this.mStartEffectPaint.setStrokeWidth((float) strokeWidth);
        }
        if (this.mEndEffectPaint != null) {
            this.mEndEffectPaint.setStrokeWidth((float) strokeWidth);
        }
        if (this.mCircleBGPaint != null) {
            this.mCircleBGPaint.setStrokeWidth((float) strokeWidth);
        }
        updateLayout();
    }

    public void updateTime(long inputMillis, long remainMillis) {
        long tempRemainMillis = remainMillis + 800;
        sHour = (int) (tempRemainMillis / 3600000);
        sMinute = (int) ((tempRemainMillis % 3600000) / 60000);
        sSecond = (int) ((tempRemainMillis % 60000) / 1000);
        sInputMillis = inputMillis - 180;
        sRemainMillis = remainMillis < 180 ? 0 : remainMillis - 180;
    }

    public void start() {
        Log.secD("TimerCircleView", "start()");
        sIsStartState = true;
        setDrawInterval();
        invalidate();
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(0, sDrawInterval);
        }
    }

    public void stop() {
        Log.secD("TimerCircleView", "stop()");
        sIsStartState = false;
        invalidate();
    }

    public void reset() {
        Log.secD("TimerCircleView", "reset()");
        sIsStartState = false;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        int sc = canvas.saveLayer(0.0f, 0.0f, this.mCenterX * 2.0f, this.mCenterY * 2.0f, null);
        if (sHour == 0 && sMinute == 0 && sSecond <= 5) {
            this.mCircleDrawColor = sSecond == 0 ? this.mCircleBackgroundColor : this.mCircleAlertColor;
        } else {
            this.mCircleDrawColor = this.mCircleOnGoingColor;
        }
        drawCircle(canvas);
        canvas.restoreToCount(sc);
    }

    private void drawCircle(Canvas canvas) {
        this.mCircleBGPaint.setAlpha(this.mAlpha);
        canvas.drawCircle(this.mCenterX, this.mCenterY, this.mCircleRadius, this.mCircleBGPaint);
        float currentAngle = (((float) sRemainMillis) / ((float) sInputMillis)) * 360.0f;
        this.mCirclePaint.setColor(this.mCircleDrawColor);
        this.mCirclePaint.setAlpha(this.mAlpha);
        Canvas canvas2 = canvas;
        canvas2.drawArc(this.mCenterX - this.mCircleRadius, this.mCenterY - this.mCircleRadius, this.mCircleRadius + this.mCenterX, this.mCircleRadius + this.mCenterY, -90.0f, currentAngle, false, this.mCirclePaint);
    }

    public void startAnimation(boolean isStart) {
        circleOpacityAnimatorForStart(isStart).start();
        circleScaleAnimatorForStart(isStart).start();
    }

    private ValueAnimator circleOpacityAnimatorForStart(final boolean isStart) {
        ValueAnimator animator;
        if (isStart) {
            animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        } else {
            animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        }
        animator.setDuration(100);
        animator.addUpdateListener(new C07341());
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                if (!isStart) {
                    TimerCircleView.this.setVisibility(4);
                    TimerCircleView.this.mAlpha = 255;
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        return animator;
    }

    private ValueAnimator circleScaleAnimatorForStart(boolean isStart) {
        ValueAnimator animator;
        if (isStart) {
            animator = ValueAnimator.ofFloat(new float[]{0.95f, 1.0f});
        } else {
            animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.95f});
        }
        final int strokeWidth = getResources().getDimensionPixelSize(C0728R.dimen.timer_circle_stroke_width);
        animator.setDuration(300);
        animator.setInterpolator(new SineInOut60());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                TimerCircleView.this.mCircleRadius = ((TimerCircleView.this.mLayoutWidth / 2.0f) - ((float) strokeWidth)) * ((Float) animation.getAnimatedValue()).floatValue();
                TimerCircleView.this.invalidate();
            }
        });
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                TimerCircleView.this.mCircleRadius = (TimerCircleView.this.mLayoutWidth / 2.0f) - ((float) strokeWidth);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        return animator;
    }

    public void startColorChangedAnimator() {
        if (this.mCircleAlertColorAnimator == null) {
            this.mCircleAlertColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mCircleOnGoingColor), Integer.valueOf(this.mCircleAlertColor)});
            this.mCircleAlertColorAnimator.setDuration(333);
            this.mCircleAlertColorAnimator.addUpdateListener(new C07385());
            this.mCircleAlertColorAnimator.start();
        }
    }

    public void resetColorChangedAnimator() {
        if (this.mCircleAlertColorAnimator != null) {
            this.mCircleAlertColorAnimator = null;
        }
    }

    public int getCircleWidth() {
        int statusBarHeight;
        Resources res = getResources();
        boolean isMultiWindowMode = ((Activity) this.mContext).isInMultiWindowMode();
        int buttonLayoutHeight = res.getDimensionPixelSize(isMultiWindowMode ? C0728R.dimen.stopwatch_button_height : C0728R.dimen.stopwatch_button_layout_height);
        if (isMultiWindowMode || Feature.isTablet(this.mContext) || StateUtils.isContextInDexMode(this.mContext) || res.getConfiguration().orientation != 2) {
            statusBarHeight = ClockUtils.getStatusBarHeight(this.mContext);
        } else {
            statusBarHeight = 0;
        }
        return Math.min(Math.min((int) (((double) res.getDisplayMetrics().widthPixels) * 0.87d), ((((res.getDisplayMetrics().heightPixels - buttonLayoutHeight) - ClockUtils.getActionBarHeight(this.mContext)) - statusBarHeight) - res.getDimensionPixelSize(C0728R.dimen.clock_tab_height)) - res.getDimensionPixelSize(C0728R.dimen.timer_circle_vertical_padding)), res.getDimensionPixelSize(C0728R.dimen.timer_circle_bg_max_width));
    }
}
