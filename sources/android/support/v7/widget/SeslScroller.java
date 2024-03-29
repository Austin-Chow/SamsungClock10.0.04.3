package android.support.v7.widget;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

class SeslScroller {
    private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
    private static final int DEFAULT_DURATION = 250;
    private static final float END_TENSION = 1.0f;
    private static final int FLING_MODE = 1;
    private static final float INFLEXION = 0.35f;
    private static final int NB_SAMPLES = 100;
    private static final float P1 = 0.175f;
    private static final float P2 = 0.35000002f;
    private static final int SCROLL_MODE = 0;
    private static final float[] SPLINE_POSITION = new float[101];
    private static final float[] SPLINE_TIME = new float[101];
    private static final float START_TENSION = 0.5f;
    private float mCurrVelocity;
    private int mCurrX;
    private int mCurrY;
    private float mDeceleration;
    private float mDeltaX;
    private float mDeltaY;
    private int mDistance;
    private int mDuration;
    private float mDurationReciprocal;
    private int mFinalX;
    private int mFinalY;
    private boolean mFinished;
    private float mFlingFriction;
    private boolean mFlywheel;
    private final Interpolator mInterpolator;
    private int mMaxX;
    private int mMaxY;
    private int mMinX;
    private int mMinY;
    private int mMode;
    private float mPhysicalCoeff;
    private final float mPpi;
    private long mStartTime;
    private int mStartX;
    private int mStartY;
    private float mVelocity;

    static class ViscousFluidInterpolator implements Interpolator {
        private static final float VISCOUS_FLUID_NORMALIZE = (SeslScroller.END_TENSION / viscousFluid(SeslScroller.END_TENSION));
        private static final float VISCOUS_FLUID_OFFSET = (SeslScroller.END_TENSION - (VISCOUS_FLUID_NORMALIZE * viscousFluid(SeslScroller.END_TENSION)));
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        ViscousFluidInterpolator() {
        }

        private static float viscousFluid(float x) {
            x *= VISCOUS_FLUID_SCALE;
            if (x < SeslScroller.END_TENSION) {
                return x - (SeslScroller.END_TENSION - ((float) Math.exp((double) (-x))));
            }
            return 0.36787945f + ((SeslScroller.END_TENSION - 0.36787945f) * (SeslScroller.END_TENSION - ((float) Math.exp((double) (SeslScroller.END_TENSION - x)))));
        }

        public float getInterpolation(float input) {
            float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
            if (interpolated > 0.0f) {
                return interpolated + VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }

    static {
        float x_min = 0.0f;
        float y_min = 0.0f;
        for (int i = 0; i < 100; i++) {
            float x;
            float coef;
            float y;
            float alpha = ((float) i) / 100.0f;
            float x_max = END_TENSION;
            while (true) {
                x = x_min + ((x_max - x_min) / 2.0f);
                coef = (3.0f * x) * (END_TENSION - x);
                float tx = ((((END_TENSION - x) * P1) + (P2 * x)) * coef) + ((x * x) * x);
                if (((double) Math.abs(tx - alpha)) < 1.0E-5d) {
                    break;
                } else if (tx > alpha) {
                    x_max = x;
                } else {
                    x_min = x;
                }
            }
            SPLINE_POSITION[i] = ((((END_TENSION - x) * START_TENSION) + x) * coef) + ((x * x) * x);
            float y_max = END_TENSION;
            while (true) {
                y = y_min + ((y_max - y_min) / 2.0f);
                coef = (3.0f * y) * (END_TENSION - y);
                float dy = ((((END_TENSION - y) * START_TENSION) + y) * coef) + ((y * y) * y);
                if (((double) Math.abs(dy - alpha)) < 1.0E-5d) {
                    break;
                } else if (dy > alpha) {
                    y_max = y;
                } else {
                    y_min = y;
                }
            }
            SPLINE_TIME[i] = ((((END_TENSION - y) * P1) + (P2 * y)) * coef) + ((y * y) * y);
        }
        float[] fArr = SPLINE_POSITION;
        SPLINE_TIME[100] = END_TENSION;
        fArr[100] = END_TENSION;
    }

    public SeslScroller(Context context) {
        this(context, null);
    }

    public SeslScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public SeslScroller(Context context, Interpolator interpolator, boolean flywheel) {
        this.mFlingFriction = ViewConfiguration.getScrollFriction();
        this.mFinished = true;
        if (interpolator == null) {
            this.mInterpolator = new ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mPpi = context.getResources().getDisplayMetrics().density * 160.0f;
        this.mDeceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
        this.mFlywheel = flywheel;
        this.mPhysicalCoeff = computeDeceleration(0.84f);
    }

    public final void setFriction(float friction) {
        this.mDeceleration = computeDeceleration(friction);
        this.mFlingFriction = friction;
    }

    private float computeDeceleration(float friction) {
        return (386.0878f * this.mPpi) * friction;
    }

    public final boolean isFinished() {
        return this.mFinished;
    }

    public final void forceFinished(boolean finished) {
        this.mFinished = finished;
    }

    public final int getDuration() {
        return this.mDuration;
    }

    public final int getCurrX() {
        return this.mCurrX;
    }

    public final int getCurrY() {
        return this.mCurrY;
    }

    public float getCurrVelocity() {
        if (this.mMode == 1) {
            return this.mCurrVelocity;
        }
        return this.mVelocity - ((this.mDeceleration * ((float) timePassed())) / 2000.0f);
    }

    public final int getStartX() {
        return this.mStartX;
    }

    public final int getStartY() {
        return this.mStartY;
    }

    public final int getFinalX() {
        return this.mFinalX;
    }

    public final int getFinalY() {
        return this.mFinalY;
    }

    public boolean computeScrollOffset() {
        if (this.mFinished) {
            return false;
        }
        int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
        if (timePassed < this.mDuration) {
            switch (this.mMode) {
                case 0:
                    float x = this.mInterpolator.getInterpolation(((float) timePassed) * this.mDurationReciprocal);
                    this.mCurrX = this.mStartX + Math.round(this.mDeltaX * x);
                    this.mCurrY = this.mStartY + Math.round(this.mDeltaY * x);
                    break;
                case 1:
                    float t = ((float) timePassed) / ((float) this.mDuration);
                    int index = (int) (100.0f * t);
                    float distanceCoef = END_TENSION;
                    float velocityCoef = 0.0f;
                    if (index < 100) {
                        float t_inf = ((float) index) / 100.0f;
                        float t_sup = ((float) (index + 1)) / 100.0f;
                        float d_inf = SPLINE_POSITION[index];
                        velocityCoef = (SPLINE_POSITION[index + 1] - d_inf) / (t_sup - t_inf);
                        distanceCoef = d_inf + ((t - t_inf) * velocityCoef);
                    }
                    this.mCurrVelocity = ((((float) this.mDistance) * velocityCoef) / ((float) this.mDuration)) * 1000.0f;
                    this.mCurrX = this.mStartX + Math.round(((float) (this.mFinalX - this.mStartX)) * distanceCoef);
                    this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
                    this.mCurrX = Math.max(this.mCurrX, this.mMinX);
                    this.mCurrY = this.mStartY + Math.round(((float) (this.mFinalY - this.mStartY)) * distanceCoef);
                    this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
                    this.mCurrY = Math.max(this.mCurrY, this.mMinY);
                    if (this.mCurrX == this.mFinalX && this.mCurrY == this.mFinalY) {
                        this.mFinished = true;
                        break;
                    }
            }
        }
        this.mCurrX = this.mFinalX;
        this.mCurrY = this.mFinalY;
        this.mFinished = true;
        return true;
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mFinished = false;
        this.mDuration = duration;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        this.mFinalX = startX + dx;
        this.mFinalY = startY + dy;
        this.mDeltaX = (float) dx;
        this.mDeltaY = (float) dy;
        this.mDurationReciprocal = END_TENSION / ((float) this.mDuration);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        if (this.mFlywheel && !this.mFinished) {
            float oldVel = getCurrVelocity();
            float dx = (float) (this.mFinalX - this.mStartX);
            float dy = (float) (this.mFinalY - this.mStartY);
            float hyp = (float) Math.hypot((double) dx, (double) dy);
            float oldVelocityX = (dx / hyp) * oldVel;
            float oldVelocityY = (dy / hyp) * oldVel;
            if (Math.signum((float) velocityX) == Math.signum(oldVelocityX) && Math.signum((float) velocityY) == Math.signum(oldVelocityY)) {
                velocityX = (int) (((float) velocityX) + oldVelocityX);
                velocityY = (int) (((float) velocityY) + oldVelocityY);
            }
        }
        this.mMode = 1;
        this.mFinished = false;
        float velocity = (float) Math.hypot((double) velocityX, (double) velocityY);
        this.mVelocity = velocity;
        this.mDuration = getSplineFlingDuration(velocity);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = startX;
        this.mStartY = startY;
        float coeffX = velocity == 0.0f ? END_TENSION : ((float) velocityX) / velocity;
        float coeffY = velocity == 0.0f ? END_TENSION : ((float) velocityY) / velocity;
        double totalDistance = getSplineFlingDistance(velocity);
        this.mDistance = (int) (((double) Math.signum(velocity)) * totalDistance);
        this.mMinX = minX;
        this.mMaxX = maxX;
        this.mMinY = minY;
        this.mMaxY = maxY;
        this.mFinalX = ((int) Math.round(((double) coeffX) * totalDistance)) + startX;
        this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
        this.mFinalX = Math.max(this.mFinalX, this.mMinX);
        this.mFinalY = ((int) Math.round(((double) coeffY) * totalDistance)) + startY;
        this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
        this.mFinalY = Math.max(this.mFinalY, this.mMinY);
    }

    private double getSplineDeceleration(float velocity) {
        return Math.log((double) ((INFLEXION * Math.abs(velocity)) / (this.mFlingFriction * this.mPhysicalCoeff)));
    }

    private int getSplineFlingDuration(float velocity) {
        return (int) (1000.0d * Math.exp(getSplineDeceleration(velocity) / (((double) DECELERATION_RATE) - 1.0d)));
    }

    private double getSplineFlingDistance(float velocity) {
        return ((double) (this.mFlingFriction * this.mPhysicalCoeff)) * Math.exp((((double) DECELERATION_RATE) / (((double) DECELERATION_RATE) - 1.0d)) * getSplineDeceleration(velocity));
    }

    public void abortAnimation() {
        this.mCurrX = this.mFinalX;
        this.mCurrY = this.mFinalY;
        this.mFinished = true;
    }

    public void extendDuration(int extend) {
        this.mDuration = timePassed() + extend;
        this.mDurationReciprocal = END_TENSION / ((float) this.mDuration);
        this.mFinished = false;
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
    }

    public void setFinalX(int newX) {
        this.mFinalX = newX;
        this.mDeltaX = (float) (this.mFinalX - this.mStartX);
        this.mFinished = false;
    }

    public void setFinalY(int newY) {
        this.mFinalY = newY;
        this.mDeltaY = (float) (this.mFinalY - this.mStartY);
        this.mFinished = false;
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        return !this.mFinished && Math.signum(xvel) == Math.signum((float) (this.mFinalX - this.mStartX)) && Math.signum(yvel) == Math.signum((float) (this.mFinalY - this.mStartY));
    }
}
