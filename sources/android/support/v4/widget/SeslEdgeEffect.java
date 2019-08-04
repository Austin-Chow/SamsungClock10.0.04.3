package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.SeslHapticFeedbackConstantsReflector;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;

public class SeslEdgeEffect extends EdgeEffect {
    private static final double ANGLE = 0.5235987755982988d;
    private static final int[] ATTRS = new int[]{16843982};
    private static final float COS = ((float) Math.cos(ANGLE));
    private static final float EDGE_CONTROL_POINT_HEIGHT_NON_TAB_IN_DIP = 29.0f;
    private static final float EDGE_CONTROL_POINT_HEIGHT_TAB_IN_DIP = 19.0f;
    private static final float EDGE_PADDING_NON_TAB_IN_DIP = 5.0f;
    private static final float EDGE_PADDING_TAB_IN_DIP = 3.0f;
    private static final float EPSILON = 0.001f;
    private static final float MAX_GLOW_SCALE = 2.0f;
    private static final int MAX_VELOCITY = 10000;
    private static final int MIN_VELOCITY = 100;
    private static final int MSG_CALL_ONRELEASE = 1;
    private static final float PULL_GLOW_BEGIN = 0.0f;
    private static final int PULL_TIME = 167;
    private static final int RECEDE_TIME = 450;
    private static final int SESL_APPEAR_TIME = 250;
    private static final int SESL_KEEP_TIME = 0;
    private static final int SESL_STATE_APPEAR = 5;
    private static final int SESL_STATE_KEEP = 6;
    private static final float SIN = ((float) Math.sin(ANGLE));
    private static final int STATE_ABSORB = 2;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PULL = 1;
    private static final int STATE_PULL_DECAY = 4;
    private static final int STATE_RECEDE = 3;
    private static final float TAB_HEIGHT_BUFFER_IN_DIP = 5.0f;
    private static final String TAG = "SeslEdgeEffect";
    private float SESL_MAX_ALPHA = 0.15f;
    private float SESL_MAX_SCALE = 1.0f;
    private final Rect mBounds = new Rect();
    private float mDisplacement = 0.5f;
    private final DisplayMetrics mDisplayMetrics;
    private float mDuration;
    private float mEdgeControlPointHeight;
    private float mEdgePadding;
    private Runnable mForceCallOnRelease = new C02022();
    private float mGlowAlpha;
    private float mGlowAlphaFinish;
    private float mGlowAlphaStart;
    private float mGlowScaleY;
    private float mGlowScaleYFinish;
    private float mGlowScaleYStart;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SeslEdgeEffect.this.onRelease();
                    return;
                default:
                    return;
            }
        }
    };
    private final Interpolator mInterpolator;
    private boolean mOnReleaseCalled = false;
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private float mPullDistance;
    private View mSeslHostView;
    private long mStartTime;
    private int mState = 0;
    private final float mTabHeight;
    private final float mTabHeightBuffer;
    private float mTargetDisplacement = 0.5f;
    private float mTempDeltaDistance;
    private float mTempDisplacement;

    /* renamed from: android.support.v4.widget.SeslEdgeEffect$2 */
    class C02022 implements Runnable {
        C02022() {
        }

        public void run() {
            SeslEdgeEffect.this.mOnReleaseCalled = true;
            SeslEdgeEffect.this.onPull(SeslEdgeEffect.this.mTempDeltaDistance, SeslEdgeEffect.this.mTempDisplacement);
            SeslEdgeEffect.this.mHandler.sendEmptyMessageDelayed(1, 700);
        }
    }

    public SeslEdgeEffect(Context context) {
        super(context);
        this.mPaint.setAntiAlias(true);
        TypedArray a = context.getTheme().obtainStyledAttributes(ATTRS);
        int themeColor = a.getColor(0, -10066330);
        a.recycle();
        this.mPaint.setColor((ViewCompat.MEASURED_SIZE_MASK & themeColor) | 855638016);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        this.mInterpolator = new DecelerateInterpolator();
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mTabHeight = dipToPixels(85.0f);
        this.mTabHeightBuffer = dipToPixels(5.0f);
    }

    public void setSeslHostView(View hostView) {
        this.mSeslHostView = hostView;
    }

    private float dipToPixels(float dipValue) {
        return TypedValue.applyDimension(1, dipValue, this.mDisplayMetrics);
    }

    public void setSize(int width, int height) {
        float r = (((float) width) * 0.75f) / SIN;
        float h = r - (COS * r);
        float or = (((float) height) * 0.75f) / SIN;
        float oh = or - (COS * or);
        if (((float) width) <= this.mTabHeight + this.mTabHeightBuffer) {
            this.mEdgePadding = dipToPixels(EDGE_PADDING_TAB_IN_DIP);
            this.mEdgeControlPointHeight = dipToPixels(EDGE_CONTROL_POINT_HEIGHT_TAB_IN_DIP);
        } else {
            this.mEdgePadding = dipToPixels(5.0f);
            this.mEdgeControlPointHeight = dipToPixels(EDGE_CONTROL_POINT_HEIGHT_NON_TAB_IN_DIP);
        }
        this.mBounds.set(this.mBounds.left, this.mBounds.top, width, (int) Math.min((float) height, h));
    }

    public boolean isFinished() {
        return this.mState == 0;
    }

    public void finish() {
        this.mState = 0;
    }

    public void onPull(float deltaDistance) {
        onPull(deltaDistance, 0.5f);
    }

    private boolean isEdgeEffectRunning() {
        return this.mState == 5 || this.mState == 6 || this.mState == 3 || this.mState == 2;
    }

    public void onPullCallOnRelease(float deltaDistance, float displacement, int delayTime) {
        this.mTempDeltaDistance = deltaDistance;
        this.mTempDisplacement = displacement;
        this.mHandler.postDelayed(this.mForceCallOnRelease, (long) delayTime);
    }

    public void onPull(float deltaDistance, float displacement) {
        if (this.mPullDistance == 0.0f) {
            this.mOnReleaseCalled = false;
            if (isEdgeEffectRunning()) {
                this.mPullDistance += deltaDistance;
            }
        }
        long now = AnimationUtils.currentAnimationTimeMillis();
        this.mTargetDisplacement = displacement;
        if (this.mState != 4 || ((float) (now - this.mStartTime)) >= this.mDuration) {
            if (this.mState != 1) {
                this.mGlowScaleY = Math.max(0.0f, this.mGlowScaleY);
            }
            if (!isEdgeEffectRunning()) {
                if (this.mPullDistance == 0.0f || this.mOnReleaseCalled) {
                    if (this.mSeslHostView != null) {
                        int indexOfHaptic = SeslHapticFeedbackConstantsReflector.semGetVibrationIndex(28);
                        if (indexOfHaptic != -1) {
                            this.mSeslHostView.performHapticFeedback(indexOfHaptic);
                        }
                    }
                    this.mState = 1;
                    this.mStartTime = now;
                    this.mDuration = 167.0f;
                    this.mPullDistance += deltaDistance;
                }
            }
        }
    }

    public void onRelease() {
        this.mPullDistance = 0.0f;
        this.mOnReleaseCalled = true;
        if (this.mState == 1 || this.mState == 4) {
            this.mState = 3;
            this.mGlowAlphaStart = this.mGlowAlpha;
            this.mGlowScaleYStart = this.mGlowScaleY;
            this.mGlowAlphaFinish = 0.0f;
            this.mGlowScaleYFinish = 0.0f;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 450.0f;
        }
    }

    public void onAbsorb(int velocity) {
        if (!isEdgeEffectRunning()) {
            if (this.mSeslHostView != null) {
                this.mSeslHostView.performHapticFeedback(SeslHapticFeedbackConstantsReflector.semGetVibrationIndex(28));
            }
            this.mOnReleaseCalled = true;
            this.mState = 2;
            velocity = Math.min(Math.max(100, Math.abs(velocity)), MAX_VELOCITY);
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 250.0f;
            this.mGlowAlphaStart = 0.0f;
            this.mGlowScaleYStart = 0.0f;
            this.mGlowScaleYFinish = this.SESL_MAX_SCALE;
            this.mGlowAlphaFinish = this.SESL_MAX_ALPHA;
            this.mTargetDisplacement = 0.5f;
            this.mHandler.sendEmptyMessageDelayed(1, 700);
        }
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    public int getColor() {
        return this.mPaint.getColor();
    }

    public boolean draw(Canvas canvas) {
        update();
        int count = canvas.save();
        float centerX = (float) this.mBounds.centerX();
        canvas.scale(1.0f, Math.min(this.mGlowScaleY, 1.0f), centerX, 0.0f);
        float displacement = Math.max(0.0f, Math.min(this.mDisplacement, 1.0f)) - 0.5f;
        float controlX = centerX;
        float controlY = this.mEdgeControlPointHeight + this.mEdgePadding;
        float topDistance = ((float) this.mBounds.width()) * 0.2f;
        this.mPath.reset();
        this.mPath.moveTo(0.0f, 0.0f);
        this.mPath.lineTo(0.0f, this.mEdgePadding);
        this.mPath.cubicTo(controlX - topDistance, controlY, controlX + topDistance, controlY, (float) this.mBounds.width(), this.mEdgePadding);
        this.mPath.lineTo((float) this.mBounds.width(), 0.0f);
        this.mPath.close();
        this.mPaint.setAlpha((int) (255.0f * this.mGlowAlpha));
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restoreToCount(count);
        boolean oneLastFrame = false;
        if (this.mState == 3 && this.mGlowScaleY == 0.0f) {
            this.mState = 0;
            oneLastFrame = true;
        }
        return this.mState != 0 || oneLastFrame;
    }

    public int getMaxHeight() {
        return (int) ((((float) this.mBounds.height()) * MAX_GLOW_SCALE) + 0.5f);
    }

    private void update() {
        float t = Math.min(((float) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) / this.mDuration, 1.0f);
        float interp = this.mInterpolator.getInterpolation(t);
        this.mGlowAlpha = this.mGlowAlphaStart + ((this.mGlowAlphaFinish - this.mGlowAlphaStart) * interp);
        this.mGlowScaleY = this.mGlowScaleYStart + ((this.mGlowScaleYFinish - this.mGlowScaleYStart) * interp);
        this.mDisplacement = (this.mDisplacement + this.mTargetDisplacement) / MAX_GLOW_SCALE;
        if (t >= 0.999f || this.mState == 1) {
            float f;
            switch (this.mState) {
                case 1:
                    this.mState = 5;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 250.0f;
                    this.mGlowAlphaStart = 0.0f;
                    this.mGlowScaleYStart = 0.0f;
                    this.mGlowAlphaFinish = this.SESL_MAX_ALPHA;
                    this.mGlowScaleYFinish = this.SESL_MAX_SCALE;
                    this.mGlowScaleY = 0.0f;
                    this.mOnReleaseCalled = false;
                    return;
                case 2:
                    this.mState = 6;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 0.0f;
                    f = this.SESL_MAX_ALPHA;
                    this.mGlowAlphaStart = f;
                    this.mGlowAlphaFinish = f;
                    f = this.SESL_MAX_SCALE;
                    this.mGlowScaleYStart = f;
                    this.mGlowScaleYFinish = f;
                    return;
                case 3:
                    this.mState = 0;
                    return;
                case 4:
                    this.mState = 3;
                    return;
                case 5:
                    this.mState = 6;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 0.0f;
                    f = this.SESL_MAX_ALPHA;
                    this.mGlowAlphaStart = f;
                    this.mGlowAlphaFinish = f;
                    f = this.SESL_MAX_SCALE;
                    this.mGlowScaleYStart = f;
                    this.mGlowScaleYFinish = f;
                    return;
                case 6:
                    this.mState = 3;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 450.0f;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0f;
                    this.mGlowScaleYFinish = 0.0f;
                    return;
                default:
                    return;
            }
        }
    }
}
