package android.support.design.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.design.C0011R;
import android.support.design.widget.TabLayout;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class SeslTabRoundRectIndicator extends SeslAbsIndicatorView {
    private Drawable mBackground;
    private AnimationSet mPressAnimationSet;

    /* renamed from: android.support.design.internal.SeslTabRoundRectIndicator$1 */
    class C00161 implements AnimationListener {
        C00161() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            SeslTabRoundRectIndicator.this.mPressAnimationSet = null;
        }
    }

    public SeslTabRoundRectIndicator(Context context) {
        this(context, null);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Drawable drawable;
        super(context, attrs, defStyleAttr, defStyleRes);
        if (isLightTheme()) {
            drawable = getContext().getDrawable(C0011R.drawable.sesl_tablayout_subtab_indicator_background);
        } else {
            drawable = getContext().getDrawable(C0011R.drawable.sesl_tablayout_subtab_indicator_background_dark);
        }
        this.mBackground = drawable;
        setBackground(this.mBackground);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != 0 && !isSelected()) {
            setHideImmediatly();
        }
    }

    public void setHideImmediatly() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        startAnimation(alphaAnimation);
        setAlpha(0.0f);
    }

    void onHide() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        startAnimation(alphaAnimation);
        setAlpha(0.0f);
    }

    void onShow() {
        if (this.mPressAnimationSet == null) {
            onReleased();
        } else if (this.mPressAnimationSet != null && this.mPressAnimationSet.hasEnded()) {
            onReleased();
        }
    }

    void onPressed() {
        setAlpha(1.0f);
        this.mPressAnimationSet = new AnimationSet(false);
        this.mPressAnimationSet.setStartOffset(50);
        this.mPressAnimationSet.setFillAfter(true);
        this.mPressAnimationSet.setAnimationListener(new C00161());
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(50);
        scaleAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        this.mPressAnimationSet.addAnimation(scaleAnimation);
        if (!isSelected()) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(50);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
            this.mPressAnimationSet.addAnimation(alphaAnimation);
        }
        startAnimation(this.mPressAnimationSet);
    }

    void onReleased() {
        setAlpha(1.0f);
        this.mPressAnimationSet = null;
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(350);
        scaleAnimation.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        startAnimation(animationSet);
    }

    private boolean isLightTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            return true;
        }
        return false;
    }

    void onSetSelectedIndicatorColor(int color) {
        if (!(getBackground() instanceof NinePatchDrawable)) {
            getBackground().setTint(color);
            if (!isSelected()) {
                setHideImmediatly();
            }
        }
    }
}
