package com.sec.android.app.clockpackage.commonalert.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.android.view.animation.SineInOut33;
import com.samsung.android.view.animation.SineInOut70;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.C0661R;

public class AlertSlidingTabHandle extends FrameLayout {
    private AlertClearCircleImageView mAnimationTargetCircle = null;
    private AlertClearCircleImageView mClearTargetCircle = null;
    private Context mContext = null;
    private int mCoverType = 0;
    private int mCurrentState;
    private int mDiameter;
    private TextView mDismissHandleView = null;
    private int mHandleType = 0;
    private AnimationSet mRepeatHandleAnimation;
    private ImageView mTabBg = null;
    private ImageView mTargetCircle = null;
    private int mTopOffset;

    public AlertSlidingTabHandle(Context context, int handleType) {
        super(context);
        this.mContext = context;
        this.mHandleType = handleType;
        init();
    }

    public AlertSlidingTabHandle(Context context, int handleType, int coverType) {
        super(context);
        this.mContext = context;
        this.mHandleType = handleType;
        this.mCoverType = coverType;
        init();
        setEnable();
    }

    @SuppressLint({"SwitchIntDef"})
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        switch (visibility) {
            case 0:
                Log.secD("AlertSlidingTab:Handle", "mContainer View.VISIBLE");
                return;
            case 4:
                Log.secD("AlertSlidingTab:Handle", "mContainer View.INVISIBLE");
                return;
            default:
                Log.secD("AlertSlidingTab:Handle", "mContainer default");
                return;
        }
    }

    private void init() {
        Log.secD("AlertSlidingTab:Handle", "Handle - init()");
        LayoutInflater.from(this.mContext).inflate(C0661R.layout.alert_sliding_tab, this, true);
        this.mCurrentState = 0;
        LayoutParams containerParams = new LayoutParams(-2, -2);
        this.mDiameter = (int) getResources().getDimension(C0661R.dimen.alarm_tap_circle_size);
        if (this.mCoverType == 3) {
            this.mDiameter = ClockUtils.convertDpToPixel(this.mContext, 202);
            DisplayMetrics metrics = this.mContext.getResources().getDisplayMetrics();
            containerParams = new LayoutParams(metrics.widthPixels, metrics.heightPixels / 2);
        }
        containerParams.gravity = 17;
        this.mTabBg = (ImageView) findViewById(C0661R.id.tabCircle);
        this.mClearTargetCircle = (AlertClearCircleImageView) findViewById(C0661R.id.clearTargetCircle);
        this.mAnimationTargetCircle = (AlertClearCircleImageView) findViewById(C0661R.id.animTargetCircle);
        this.mTargetCircle = (ImageView) findViewById(C0661R.id.targetCircle);
        this.mDismissHandleView = (TextView) findViewById(C0661R.id.handleImageView);
        ClockUtils.setLargeTextSize(this.mContext, this.mDismissHandleView, (float) getResources().getDimensionPixelSize(C0661R.dimen.alarm_snooze_text_size));
        setDismissHandleBg();
        if (this.mCoverType == 2) {
            this.mClearTargetCircle.setImageResource(C0661R.drawable.alert_dismiss_handle_press_clear_cover);
            this.mAnimationTargetCircle.setImageResource(C0661R.drawable.alert_sliding_tab_mask_press_clear_cover_anim);
            this.mTargetCircle.setImageResource(C0661R.drawable.alert_sliding_tab_bg_press_clear_cover);
        } else {
            this.mClearTargetCircle.setImageResource(C0661R.drawable.alert_dismiss_handle_press);
            this.mAnimationTargetCircle.setImageResource(C0661R.drawable.alert_sliding_tab_mask_press_anim);
            this.mTargetCircle.setImageResource(this.mCoverType == 3 ? C0661R.drawable.alert_sliding_tab_bg_press_sview_cover : C0661R.drawable.alert_sliding_tab_bg_press);
        }
        this.mTargetCircle.setVisibility(4);
        this.mClearTargetCircle.setVisibility(4);
        setLayoutParams(containerParams);
        makeHandleAnimation();
        this.mAnimationTargetCircle.startAnimation(this.mRepeatHandleAnimation);
    }

    public void setState(int newState) {
        int preState = this.mCurrentState;
        this.mCurrentState = newState;
        Log.m41d("AlertSlidingTab:Handle", "setState mCurrentState = " + this.mCurrentState);
        switch (this.mCurrentState) {
            case 0:
                if (preState == 1) {
                    reset();
                    return;
                } else if (preState == 2) {
                    setEnable();
                    return;
                } else {
                    return;
                }
            case 1:
                showTarget();
                return;
            case 2:
                setDisable();
                return;
            case 3:
                if (this.mTabBg != null) {
                    this.mTabBg.setOnFocusChangeListener(null);
                    this.mTabBg.setOnClickListener(null);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setDismissHandleBg() {
        this.mTabBg.setImageResource(this.mCoverType == 2 ? C0661R.drawable.alert_dismiss_handle_clear_cover_bg : C0661R.drawable.alert_dismiss_handle_bg);
    }

    private void makeHandleAnimation() {
        Log.secD("AlertSlidingTab:Handle", "makeHandleAnimation");
        this.mRepeatHandleAnimation = new AnimationSet(false);
        ScaleAnimation scaleAni = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAni.setDuration(2000);
        scaleAni.setInterpolator(new SineInOut70());
        scaleAni.setRepeatCount(-1);
        scaleAni.setRepeatMode(1);
        AlphaAnimation alphaAni = new AlphaAnimation(1.0f, 0.0f);
        alphaAni.setStartOffset(1000);
        alphaAni.setDuration(1000);
        alphaAni.setInterpolator(new SineInOut33());
        alphaAni.setRepeatCount(-1);
        alphaAni.setRepeatMode(1);
        this.mRepeatHandleAnimation.addAnimation(scaleAni);
        this.mRepeatHandleAnimation.addAnimation(alphaAni);
    }

    public int getState() {
        return this.mCurrentState;
    }

    private void showTarget() {
        Log.m41d("AlertSlidingTab:Handle", "showTarget");
        this.mAnimationTargetCircle.clearAnimation();
        AnimationSet aniSet = new AnimationSet(true);
        aniSet.addAnimation(new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, 1, 0.5f, 1, 0.5f));
        aniSet.addAnimation(new AlphaAnimation(0.0f, 1.0f));
        aniSet.setDuration(300);
        aniSet.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432582));
        aniSet.setFillAfter(true);
        this.mClearTargetCircle.clearAnimation();
        this.mClearTargetCircle.setVisibility(0);
        this.mClearTargetCircle.startAnimation(aniSet);
        if (this.mTabBg != null) {
            Drawable d = this.mTabBg.getBackground();
            if (d != null) {
                try {
                    d.setCallback(null);
                } catch (Exception e) {
                    Log.secE("AlertSlidingTab:Handle", "Exception : " + e.toString());
                }
            }
            this.mTabBg.setImageDrawable(null);
        }
        if (this.mHandleType == 1 && this.mTabBg != null) {
            setDismissHandleBg();
        }
    }

    private void reset() {
        Log.secD("AlertSlidingTab:Handle", "reset()");
        AnimationSet aniSet = new AnimationSet(true);
        aniSet.addAnimation(new ScaleAnimation(1.0f, 0.3f, 1.0f, 0.3f, 1, 0.5f, 1, 0.5f));
        aniSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
        aniSet.setDuration(300);
        aniSet.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432581));
        if (this.mTabBg != null) {
            Drawable d = this.mTabBg.getBackground();
            if (d != null) {
                try {
                    d.setCallback(null);
                } catch (Exception e) {
                    Log.secE("AlertSlidingTab:Handle", "Exception : " + e.toString());
                }
            }
            this.mTabBg.setImageDrawable(null);
        }
        this.mClearTargetCircle.clearAnimation();
        this.mClearTargetCircle.startAnimation(aniSet);
        this.mClearTargetCircle.setVisibility(4);
        if (this.mHandleType == 1 && this.mTabBg != null) {
            setDismissHandleBg();
        }
        if (this.mRepeatHandleAnimation == null) {
            makeHandleAnimation();
        }
        this.mAnimationTargetCircle.startAnimation(this.mRepeatHandleAnimation);
    }

    public void invalidateCircle() {
        this.mClearTargetCircle.invalidate();
    }

    private void setDisable() {
        Log.secD("AlertSlidingTab:Handle", "setDisable()");
        AlphaAnimation alphaAni = new AlphaAnimation(1.0f, 0.35f);
        alphaAni.setDuration(200);
        alphaAni.setFillAfter(true);
        this.mTabBg.startAnimation(alphaAni);
        this.mClearTargetCircle.setVisibility(4);
        this.mClearTargetCircle.clearInnerCircle(0.0f);
    }

    private void setEnable() {
        Log.secD("AlertSlidingTab:Handle", "setEnable()");
        AlphaAnimation tabAni = new AlphaAnimation(0.35f, 1.0f);
        tabAni.setDuration(200);
        tabAni.setFillAfter(true);
        this.mTabBg.startAnimation(tabAni);
        this.mClearTargetCircle.setVisibility(4);
    }

    public void updateMovingCircle(float x, float y) {
        Log.secD("AlertSlidingTab:Handle", "updateMovingCircle : " + this.mCurrentState);
        if (this.mCurrentState == 1) {
            double ratio = getTargetProximityRatio(x, y);
            Log.secD("AlertSlidingTab:Handle", "updateMovingCircle Ratio: " + ratio);
            if (this.mClearTargetCircle.getVisibility() != 0) {
                this.mClearTargetCircle.setVisibility(0);
            }
            float diameter = ((float) this.mClearTargetCircle.getMeasuredWidth()) * ((float) ratio);
            if (diameter > ((float) this.mTabBg.getWidth())) {
                this.mClearTargetCircle.clearInnerCircle(diameter / 2.0f);
            } else {
                this.mClearTargetCircle.clearInnerCircle(((float) this.mTabBg.getWidth()) / 2.0f);
            }
        }
    }

    public boolean isHandleSelected(float x, float y) {
        this.mTopOffset = ((View) getParent()).getTop();
        Log.secD("AlertSlidingTab:Handle", "mTopOffset : " + this.mTopOffset);
        if (this.mTopOffset < 0) {
            this.mTopOffset = 0;
        }
        return isInCircle(x, y);
    }

    public boolean isThresholdReached(float x, float y) {
        return getTargetProximityRatio(x, y) >= 0.95d;
    }

    private double getTargetProximityRatio(float x, float y) {
        int[] tmpPos = new int[2];
        if (StateUtils.isContextInDexMode(this.mContext)) {
            getLocationInWindow(tmpPos);
        } else {
            getLocationOnScreen(tmpPos);
        }
        float pivotX = (float) (tmpPos[0] + (getWidth() / 2));
        float pivotY = (float) ((tmpPos[1] + (getHeight() / 2)) - this.mTopOffset);
        if (this.mCoverType == 3) {
            pivotX -= (float) ClockUtils.convertDpToPixel(this.mContext, 34);
        }
        double dx = Math.abs((double) (x - pivotX));
        double dy = Math.abs((double) (y - pivotY));
        double posLength = Math.sqrt((dx * dx) + (dy * dy));
        Log.secD("AlertSlidingTab:Handle", "posLength: " + posLength + " mDiameter: " + this.mDiameter);
        return posLength / ((double) this.mDiameter);
    }

    private boolean isInCircle(float x, float y) {
        int[] tmpPos = new int[2];
        if (StateUtils.isContextInDexMode(this.mContext)) {
            getLocationInWindow(tmpPos);
        } else {
            getLocationOnScreen(tmpPos);
        }
        float pivotX = (float) (tmpPos[0] + (getWidth() / 2));
        if (this.mCoverType == 3) {
            pivotX -= (float) ClockUtils.convertDpToPixel(this.mContext, 34);
        }
        float pivotY = (float) ((tmpPos[1] + (getHeight() / 2)) - this.mTopOffset);
        double dx = Math.abs((double) (x - pivotX));
        double dy = Math.abs((double) (y - pivotY));
        double ratio = Math.sqrt((dx * dx) + (dy * dy)) / ((double) this.mDiameter);
        Log.secD("AlertSlidingTab:Handle", "isInCircle - x : " + x + " y : " + y + " ratio : " + ratio);
        if (ratio < 1.0d) {
            return true;
        }
        return false;
    }

    public ImageView getTabBg() {
        return this.mTabBg;
    }

    public View getDismissHandle() {
        return this.mDismissHandleView;
    }

    public int getHandleType() {
        return this.mHandleType;
    }

    public void clearCircleAnimation() {
        this.mTargetCircle.clearAnimation();
        this.mClearTargetCircle.clearInnerCircle(((float) this.mClearTargetCircle.getMeasuredWidth()) / 2.0f);
        this.mRepeatHandleAnimation = null;
    }
}
