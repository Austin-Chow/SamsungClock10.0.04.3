package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.SeslHoverPopupWindowReflector;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import android.widget.AbsSeekBar;
import android.widget.SeekBar;
import java.util.ArrayList;
import java.util.List;

public abstract class SeslAbsSeekBar extends SeslProgressBar {
    private static final String CURRENT_SEC_ACTIVE_THEMEPACKAGE = "current_sec_active_themepackage";
    private static final int HOVER_DETECT_TIME = 200;
    private static final int HOVER_POPUP_WINDOW_GRAVITY_CENTER_HORIZONTAL_ON_POINT = 513;
    private static final int HOVER_POPUP_WINDOW_GRAVITY_TOP_ABOVE = 12336;
    private static final boolean IS_BASE_SDK_VERSION = (VERSION.SDK_INT <= 23);
    private static final int MUTE_VIB_DISTANCE_LVL = 400;
    private static final int MUTE_VIB_DURATION = 500;
    private static final int MUTE_VIB_TOTAL = 4;
    private static final int NO_ALPHA = 255;
    private boolean mAllowedSeekBarAnimation;
    private int mCurrentProgressLevel;
    private ColorStateList mDefaultActivatedProgressColor;
    private ColorStateList mDefaultActivatedThumbColor;
    private ColorStateList mDefaultNormalProgressColor;
    private float mDisabledAlpha;
    private Drawable mDivider;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTickMarkTint;
    private boolean mHasTickMarkTintMode;
    private int mHoveringLevel;
    private boolean mIsDragging;
    private boolean mIsDraggingForSliding;
    private boolean mIsFirstSetProgress;
    private boolean mIsTouchDisabled;
    boolean mIsUserSeekable;
    private int mKeyProgressIncrement;
    private boolean mLargeFont;
    private AnimatorSet mMuteAnimationSet;
    private ColorStateList mOverlapActivatedProgressColor;
    private ColorStateList mOverlapActivatedThumbColor;
    private Drawable mOverlapBackground;
    private ColorStateList mOverlapNormalProgressColor;
    private int mOverlapPoint;
    private Drawable mOverlapPrimary;
    private Paint mPaint;
    private int mPreviousHoverPopupType;
    private int mScaledTouchSlop;
    private Drawable mSplitProgress;
    private boolean mSplitTrack;
    private final Rect mTempRect;
    private Drawable mThumb;
    private int mThumbOffset;
    private int mThumbPosX;
    private float mThumbPosXFloat;
    private int mThumbPosY;
    private float mThumbPosYFloat;
    private ColorStateList mThumbTintList;
    private Mode mThumbTintMode;
    private Drawable mTickMark;
    private ColorStateList mTickMarkTintList;
    private Mode mTickMarkTintMode;
    private float mTouchDownX;
    private float mTouchDownY;
    float mTouchProgressOffset;
    private boolean mUseMuteAnimation;

    /* renamed from: android.support.v7.widget.SeslAbsSeekBar$1 */
    class C03471 implements AnimatorUpdateListener {
        C03471() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            SeslAbsSeekBar.this.mCurrentProgressLevel = ((Integer) animation.getAnimatedValue()).intValue();
            SeslAbsSeekBar.this.onSlidingRefresh(SeslAbsSeekBar.this.mCurrentProgressLevel);
        }
    }

    public SeslAbsSeekBar(Context context) {
        super(context);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkTintMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeekBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mLargeFont = false;
        this.mIsTouchDisabled = false;
        this.mPreviousHoverPopupType = 0;
    }

    public SeslAbsSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkTintMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeekBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mLargeFont = false;
        this.mIsTouchDisabled = false;
        this.mPreviousHoverPopupType = 0;
    }

    public SeslAbsSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslAbsSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkTintMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeekBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mLargeFont = false;
        this.mIsTouchDisabled = false;
        this.mPreviousHoverPopupType = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, C0247R.styleable.AppCompatSeekBar, defStyleAttr, defStyleRes);
        setThumb(a.getDrawable(C0247R.styleable.AppCompatSeekBar_android_thumb));
        if (a.hasValue(C0247R.styleable.AppCompatSeekBar_android_thumbTintMode)) {
            this.mThumbTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.AppCompatSeekBar_android_thumbTintMode, -1), this.mThumbTintMode);
            this.mHasThumbTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.AppCompatSeekBar_android_thumbTint)) {
            this.mThumbTintList = a.getColorStateList(C0247R.styleable.AppCompatSeekBar_android_thumbTint);
            this.mHasThumbTint = true;
        }
        setTickMark(a.getDrawable(C0247R.styleable.AppCompatSeekBar_tickMark));
        if (a.hasValue(C0247R.styleable.AppCompatSeekBar_tickMarkTintMode)) {
            this.mTickMarkTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.AppCompatSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
            this.mHasTickMarkTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.AppCompatSeekBar_tickMarkTint)) {
            this.mTickMarkTintList = a.getColorStateList(C0247R.styleable.AppCompatSeekBar_tickMarkTint);
            this.mHasTickMarkTint = true;
        }
        this.mSplitTrack = a.getBoolean(C0247R.styleable.AppCompatSeekBar_android_splitTrack, false);
        setThumbOffset(a.getDimensionPixelOffset(C0247R.styleable.AppCompatSeekBar_android_thumbOffset, getThumbOffset()));
        boolean useDisabledAlpha = a.getBoolean(C0247R.styleable.AppCompatSeekBar_useDisabledAlpha, true);
        a.recycle();
        if (useDisabledAlpha) {
            TypedArray ta = context.obtainStyledAttributes(attrs, C0247R.styleable.AppCompatTheme, 0, 0);
            this.mDisabledAlpha = ta.getFloat(C0247R.styleable.AppCompatTheme_android_disabledAlpha, 0.5f);
            ta.recycle();
        } else {
            this.mDisabledAlpha = 1.0f;
        }
        applyThumbTint();
        applyTickMarkTint();
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Resources res = context.getResources();
        this.mDefaultNormalProgressColor = colorToColorStateList(res.getColor(C0247R.color.sesl_seekbar_control_color_normal));
        this.mDefaultActivatedProgressColor = colorToColorStateList(res.getColor(C0247R.color.sesl_seekbar_control_color_activated));
        this.mOverlapNormalProgressColor = colorToColorStateList(res.getColor(C0247R.color.sesl_seekbar_overlap_color_normal));
        this.mOverlapActivatedProgressColor = colorToColorStateList(res.getColor(C0247R.color.sesl_seekbar_overlap_color_activated));
        this.mOverlapActivatedThumbColor = this.mOverlapActivatedProgressColor;
        this.mDefaultActivatedThumbColor = getThumbTintList();
        if (this.mDefaultActivatedThumbColor == null) {
            this.mDefaultActivatedThumbColor = colorToColorStateList(res.getColor(C0247R.color.sesl_thumb_control_color_activated));
        }
        this.mAllowedSeekBarAnimation = res.getBoolean(C0247R.bool.sesl_seekbar_sliding_animation);
        if (this.mAllowedSeekBarAnimation) {
            initMuteAnimation();
        }
    }

    public void setThumb(Drawable thumb) {
        boolean needUpdate;
        if (this.mThumb == null || thumb == this.mThumb) {
            needUpdate = false;
        } else {
            this.mThumb.setCallback(null);
            needUpdate = true;
        }
        if (thumb != null) {
            thumb.setCallback(this);
            if (canResolveLayoutDirection()) {
                DrawableCompat.setLayoutDirection(thumb, ViewCompat.getLayoutDirection(this));
            }
            if (this.mCurrentMode == 3) {
                this.mThumbOffset = thumb.getIntrinsicHeight() / 2;
            } else {
                this.mThumbOffset = thumb.getIntrinsicWidth() / 2;
            }
            if (needUpdate && !(thumb.getIntrinsicWidth() == this.mThumb.getIntrinsicWidth() && thumb.getIntrinsicHeight() == this.mThumb.getIntrinsicHeight())) {
                requestLayout();
            }
        }
        this.mThumb = thumb;
        applyThumbTint();
        invalidate();
        if (needUpdate) {
            updateThumbAndTrackPos(getWidth(), getHeight());
            if (thumb != null && thumb.isStateful()) {
                thumb.setState(getDrawableState());
            }
        }
    }

    public Drawable getThumb() {
        return this.mThumb;
    }

    public void setThumbTintColor(int color) {
        ColorStateList mOverlapColor = colorToColorStateList(color);
        if (!mOverlapColor.equals(this.mDefaultActivatedThumbColor)) {
            this.mDefaultActivatedThumbColor = mOverlapColor;
        }
    }

    public void setThumbTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
        this.mDefaultActivatedThumbColor = tint;
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintMode(Mode tintMode) {
        this.mThumbTintMode = tintMode;
        this.mHasThumbTintMode = true;
        applyThumbTint();
    }

    public Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }

    private void applyThumbTint() {
        if (this.mThumb == null) {
            return;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.mThumb = this.mThumb.mutate();
            if (this.mHasThumbTint) {
                DrawableCompat.setTintList(this.mThumb, this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                DrawableCompat.setTintMode(this.mThumb, this.mThumbTintMode);
            }
            if (this.mThumb.isStateful()) {
                this.mThumb.setState(getDrawableState());
            }
        }
    }

    public int getThumbOffset() {
        return this.mThumbOffset;
    }

    public void setThumbOffset(int thumbOffset) {
        this.mThumbOffset = thumbOffset;
        invalidate();
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public void setTickMark(Drawable tickMark) {
        if (this.mTickMark != null) {
            this.mTickMark.setCallback(null);
        }
        this.mTickMark = tickMark;
        if (tickMark != null) {
            tickMark.setCallback(this);
            DrawableCompat.setLayoutDirection(tickMark, ViewCompat.getLayoutDirection(this));
            if (tickMark.isStateful()) {
                tickMark.setState(getDrawableState());
            }
            applyTickMarkTint();
        }
        invalidate();
    }

    public Drawable getTickMark() {
        return this.mTickMark;
    }

    public void setTickMarkTintList(ColorStateList tint) {
        this.mTickMarkTintList = tint;
        this.mHasTickMarkTint = true;
        applyTickMarkTint();
    }

    public ColorStateList getTickMarkTintList() {
        return this.mTickMarkTintList;
    }

    public void setTickMarkTintMode(Mode tintMode) {
        this.mTickMarkTintMode = tintMode;
        this.mHasTickMarkTintMode = true;
        applyTickMarkTint();
    }

    public Mode getTickMarkTintMode() {
        return this.mTickMarkTintMode;
    }

    private void applyTickMarkTint() {
        if (this.mTickMark == null) {
            return;
        }
        if (this.mHasTickMarkTint || this.mHasTickMarkTintMode) {
            this.mTickMark = this.mTickMark.mutate();
            if (this.mHasTickMarkTint) {
                DrawableCompat.setTintList(this.mTickMark, this.mTickMarkTintList);
            }
            if (this.mHasTickMarkTintMode) {
                DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
            }
            if (this.mTickMark.isStateful()) {
                this.mTickMark.setState(getDrawableState());
            }
        }
    }

    public void setKeyProgressIncrement(int increment) {
        if (increment < 0) {
            increment = -increment;
        }
        this.mKeyProgressIncrement = increment;
    }

    public int getKeyProgressIncrement() {
        return this.mKeyProgressIncrement;
    }

    public synchronized void setMin(int min) {
        super.setMin(min);
        int range = getMax() - getMin();
        if (this.mKeyProgressIncrement == 0 || range / this.mKeyProgressIncrement > 20) {
            setKeyProgressIncrement(Math.max(1, Math.round(((float) range) / 20.0f)));
        }
    }

    public synchronized void setMax(int max) {
        super.setMax(max);
        this.mIsFirstSetProgress = true;
        int range = getMax() - getMin();
        if (this.mKeyProgressIncrement == 0 || range / this.mKeyProgressIncrement > 20) {
            setKeyProgressIncrement(Math.max(1, Math.round(((float) range) / 20.0f)));
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mThumb || who == this.mTickMark || super.verifyDrawable(who);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mThumb != null) {
            this.mThumb.jumpToCurrentState();
        }
        if (this.mTickMark != null) {
            this.mTickMark.jumpToCurrentState();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null && this.mDisabledAlpha < 1.0f) {
            int alpha;
            if (isEnabled()) {
                alpha = 255;
            } else {
                alpha = (int) (255.0f * this.mDisabledAlpha);
            }
            progressDrawable.setAlpha(alpha);
            if (!(this.mOverlapPrimary == null || this.mOverlapBackground == null)) {
                this.mOverlapPrimary.setAlpha(alpha);
                this.mOverlapBackground.setAlpha(alpha);
            }
        }
        if (this.mThumb != null && this.mHasThumbTint) {
            if (isEnabled()) {
                DrawableCompat.setTintList(this.mThumb, this.mDefaultActivatedThumbColor);
                updateDualColorMode();
            } else {
                DrawableCompat.setTintList(this.mThumb, null);
            }
        }
        Drawable thumb = this.mThumb;
        if (thumb != null && thumb.isStateful() && thumb.setState(getDrawableState())) {
            invalidateDrawable(thumb);
        }
        Drawable tickMark = this.mTickMark;
        if (tickMark != null && tickMark.isStateful() && tickMark.setState(getDrawableState())) {
            invalidateDrawable(tickMark);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mThumb != null) {
            DrawableCompat.setHotspot(this.mThumb, x, y);
        }
    }

    void onVisualProgressChanged(int id, float scale) {
        super.onVisualProgressChanged(id, scale);
        if (id == C0247R.id.progress) {
            Drawable thumb = this.mThumb;
            if (thumb != null) {
                setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
                invalidate();
            }
        }
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        boolean isMuteAnimationNeeded;
        int targetLevel = (int) (10000.0f * scale);
        if (!this.mUseMuteAnimation || this.mIsFirstSetProgress || this.mIsDraggingForSliding) {
            isMuteAnimationNeeded = false;
        } else {
            isMuteAnimationNeeded = true;
        }
        if (isMuteAnimationNeeded && this.mCurrentProgressLevel != 0 && targetLevel == 0) {
            startMuteAnimation();
            return;
        }
        cancelMuteAnimation();
        this.mIsFirstSetProgress = false;
        this.mCurrentProgressLevel = targetLevel;
        super.onProgressRefresh(scale, fromUser, progress);
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateThumbAndTrackPos(w, h);
    }

    private void updateThumbAndTrackPos(int w, int h) {
        if (this.mCurrentMode == 3) {
            updateThumbAndTrackPosInVertical(w, h);
            return;
        }
        int trackOffset;
        int thumbOffset;
        int paddedHeight = (h - getPaddingTop()) - getPaddingBottom();
        Drawable track = getCurrentDrawable();
        Drawable thumb = this.mThumb;
        int trackHeight = Math.min(this.mMaxHeight, paddedHeight);
        int thumbHeight = thumb == null ? 0 : thumb.getIntrinsicHeight();
        int offsetHeight;
        if (thumbHeight > trackHeight) {
            offsetHeight = (paddedHeight - thumbHeight) / 2;
            trackOffset = offsetHeight + ((thumbHeight - trackHeight) / 2);
            thumbOffset = offsetHeight;
        } else {
            offsetHeight = (paddedHeight - trackHeight) / 2;
            trackOffset = offsetHeight;
            thumbOffset = offsetHeight + ((trackHeight - thumbHeight) / 2);
        }
        if (track != null) {
            track.setBounds(0, trackOffset, (w - getPaddingRight()) - getPaddingLeft(), trackOffset + trackHeight);
        }
        if (thumb != null) {
            setThumbPos(w, thumb, getScale(), thumbOffset);
        }
        updateSplitProgress();
    }

    private void updateThumbAndTrackPosInVertical(int w, int h) {
        int trackOffset;
        int thumbOffset;
        int paddedWidth = (w - getPaddingLeft()) - getPaddingRight();
        Drawable track = getCurrentDrawable();
        Drawable thumb = this.mThumb;
        int trackWidth = Math.min(this.mMaxWidth, paddedWidth);
        int thumbWidth = thumb == null ? 0 : thumb.getIntrinsicWidth();
        int offsetWidth;
        if (thumbWidth > trackWidth) {
            offsetWidth = (paddedWidth - thumbWidth) / 2;
            trackOffset = offsetWidth + ((thumbWidth - trackWidth) / 2);
            thumbOffset = offsetWidth;
        } else {
            offsetWidth = (paddedWidth - trackWidth) / 2;
            trackOffset = offsetWidth;
            thumbOffset = offsetWidth + ((trackWidth - thumbWidth) / 2);
        }
        if (track != null) {
            track.setBounds(trackOffset, 0, paddedWidth - trackOffset, (h - getPaddingBottom()) - getPaddingTop());
        }
        if (thumb != null) {
            setThumbPosInVertical(h, thumb, getScale(), thumbOffset);
        }
    }

    private float getScale() {
        int min = getMin();
        int range = getMax() - min;
        return range > 0 ? ((float) (getProgress() - min)) / ((float) range) : 0.0f;
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int offset) {
        if (this.mCurrentMode == 3) {
            setThumbPosInVertical(getHeight(), thumb, scale, offset);
            return;
        }
        int top;
        int bottom;
        int left;
        int available = (w - getPaddingLeft()) - getPaddingRight();
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available = (available - thumbWidth) + (this.mThumbOffset * 2);
        int thumbPos = (int) ((((float) available) * scale) + 0.5f);
        if (offset == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            top = oldBounds.top;
            bottom = oldBounds.bottom;
        } else {
            top = offset;
            bottom = offset + thumbHeight;
        }
        if (this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
            left = available - thumbPos;
        } else {
            left = thumbPos;
        }
        int right = left + thumbWidth;
        Drawable background = getBackground();
        if (background != null) {
            int offsetX = getPaddingLeft() - this.mThumbOffset;
            int offsetY = getPaddingTop();
            DrawableCompat.setHotspotBounds(background, left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
        }
        thumb.setBounds(left, top, right, bottom);
        this.mThumbPosX = getPaddingLeft() + left;
        this.mThumbPosY = ((thumbHeight / 2) + top) + getPaddingTop();
        this.mThumbPosXFloat = (((float) this.mThumbPosX) + (((float) thumbWidth) / 2.0f)) - ((float) this.mThumbOffset);
        this.mThumbPosYFloat = (((float) top) + (((float) thumbHeight) / 2.0f)) + ((float) getPaddingTop());
        updateSplitProgress();
    }

    private void setThumbPosInVertical(int h, Drawable thumb, float scale, int offset) {
        int left;
        int right;
        int available = (h - getPaddingTop()) - getPaddingBottom();
        int thumbWidth = thumb.getIntrinsicHeight();
        int thumbHeight = thumb.getIntrinsicHeight();
        available = (available - thumbHeight) + (this.mThumbOffset * 2);
        int thumbPos = (int) ((((float) available) * scale) + 0.5f);
        if (offset == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            left = oldBounds.left;
            right = oldBounds.right;
        } else {
            left = offset;
            right = offset + thumbWidth;
        }
        int top = available - thumbPos;
        int bottom = top + thumbHeight;
        Drawable background = getBackground();
        if (background != null) {
            int offsetX = getPaddingLeft();
            int offsetY = getPaddingTop() - this.mThumbOffset;
            DrawableCompat.setHotspotBounds(background, left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
        }
        thumb.setBounds(left, top, right, bottom);
        this.mThumbPosX = ((thumbWidth / 2) + left) + getPaddingLeft();
        this.mThumbPosY = getPaddingTop() + top;
        this.mThumbPosXFloat = (((float) left) + (((float) thumbWidth) / 2.0f)) + ((float) getPaddingLeft());
        this.mThumbPosYFloat = (float) this.mThumbPosY;
    }

    private void updateSplitProgress() {
        if (this.mCurrentMode == 4) {
            Drawable d = this.mSplitProgress;
            Rect base = getCurrentDrawable().getBounds();
            if (d != null) {
                if (this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                    d.setBounds(this.mThumbPosX, base.top, getWidth() - getPaddingRight(), base.bottom);
                } else {
                    d.setBounds(getPaddingLeft(), base.top, this.mThumbPosX, base.bottom);
                }
            }
            int w = getWidth();
            int h = getHeight();
            if (this.mDivider != null) {
                this.mDivider.setBounds((int) ((((float) w) / 2.0f) - ((this.mDensity * 4.0f) / 2.0f)), (int) ((((float) h) / 2.0f) - ((this.mDensity * 22.0f) / 2.0f)), (int) ((((float) w) / 2.0f) + ((this.mDensity * 4.0f) / 2.0f)), (int) ((((float) h) / 2.0f) + ((this.mDensity * 22.0f) / 2.0f)));
            }
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        if (this.mThumb != null) {
            DrawableCompat.setLayoutDirection(this.mThumb, layoutDirection);
        }
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (supportIsHoveringUIEnabled()) {
            int hoverPopupType = getHoverPopupType();
            if (isHoverPopupTypeUserCustom(hoverPopupType) && this.mPreviousHoverPopupType != hoverPopupType) {
                this.mPreviousHoverPopupType = hoverPopupType;
                setHoverPopupGravity(12849);
                setHoverPopupOffset(getMeasuredHeight() / 2);
                setHoverPopupDetectTime();
            }
        }
        if (this.mCurrentMode == 4) {
            this.mSplitProgress.draw(canvas);
            this.mDivider.draw(canvas);
        }
        if (!this.mIsTouchDisabled) {
            drawThumb(canvas);
        }
    }

    void drawTrack(Canvas canvas) {
        Drawable thumbDrawable = this.mThumb;
        if (thumbDrawable == null || !this.mSplitTrack) {
            super.drawTrack(canvas);
            drawTickMarks(canvas);
        } else {
            Rect insets = DrawableUtils.getOpticalBounds(thumbDrawable);
            Rect tempRect = this.mTempRect;
            thumbDrawable.copyBounds(tempRect);
            tempRect.offset(getPaddingLeft() - this.mThumbOffset, getPaddingTop());
            tempRect.left += insets.left;
            tempRect.right -= insets.right;
            int saveCount = canvas.save();
            canvas.clipRect(tempRect, Op.DIFFERENCE);
            super.drawTrack(canvas);
            drawTickMarks(canvas);
            canvas.restoreToCount(saveCount);
        }
        if (!checkInvalidatedDualColorMode()) {
            canvas.save();
            if (this.mCurrentMode == 3) {
                canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
            } else if (this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                canvas.translate((float) (getWidth() - getPaddingRight()), (float) getPaddingTop());
                canvas.scale(-1.0f, 1.0f);
            } else {
                canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
            }
            this.mOverlapBackground.draw(canvas);
            if (getProgress() > this.mOverlapPoint) {
                this.mOverlapPrimary.draw(canvas);
            }
            canvas.restore();
        }
    }

    protected void drawTickMarks(Canvas canvas) {
        int halfH = 1;
        if (this.mTickMark != null) {
            int count = getMax() - getMin();
            if (count > 1) {
                int halfW;
                int w = this.mTickMark.getIntrinsicWidth();
                int h = this.mTickMark.getIntrinsicHeight();
                if (w >= 0) {
                    halfW = w / 2;
                } else {
                    halfW = 1;
                }
                if (h >= 0) {
                    halfH = h / 2;
                }
                this.mTickMark.setBounds(-halfW, -halfH, halfW, halfH);
                float spacing = ((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / ((float) count);
                int saveCount = canvas.save();
                canvas.translate((float) getPaddingLeft(), ((float) getHeight()) / 2.0f);
                for (int i = 0; i <= count; i++) {
                    this.mTickMark.draw(canvas);
                    canvas.translate(spacing, 0.0f);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }

    void drawThumb(Canvas canvas) {
        if (this.mThumb != null) {
            int saveCount = canvas.save();
            if (this.mCurrentMode == 3) {
                canvas.translate((float) getPaddingLeft(), (float) (getPaddingTop() - this.mThumbOffset));
            } else {
                canvas.translate((float) (getPaddingLeft() - this.mThumbOffset), (float) getPaddingTop());
            }
            this.mThumb.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int thumbHeight = 0;
        synchronized (this) {
            Drawable d = getCurrentDrawable();
            int dw = 0;
            int dh = 0;
            if (d != null) {
                if (this.mCurrentMode == 3) {
                    int thumbWidth = this.mThumb == null ? 0 : this.mThumb.getIntrinsicHeight();
                    dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicHeight()));
                    dh = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicWidth()));
                    dw = Math.max(thumbWidth, dw);
                } else {
                    if (this.mThumb != null) {
                        thumbHeight = this.mThumb.getIntrinsicHeight();
                    }
                    dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicWidth()));
                    dh = Math.max(thumbHeight, Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicHeight())));
                }
            }
            setMeasuredDimension(resolveSizeAndState(dw + (getPaddingLeft() + getPaddingRight()), widthMeasureSpec, 0), resolveSizeAndState(dh + (getPaddingTop() + getPaddingBottom()), heightMeasureSpec, 0));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mIsUserSeekable || this.mIsTouchDisabled || !isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                this.mIsDraggingForSliding = false;
                if (supportIsInScrollingContainer()) {
                    this.mTouchDownX = event.getX();
                    this.mTouchDownY = event.getY();
                    return true;
                }
                startDrag(event);
                return true;
            case 1:
                if (this.mIsDraggingForSliding) {
                    this.mIsDraggingForSliding = false;
                }
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                invalidate();
                return true;
            case 2:
                this.mIsDraggingForSliding = true;
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    return true;
                }
                float x = event.getX();
                float y = event.getY();
                if ((this.mCurrentMode == 3 || Math.abs(x - this.mTouchDownX) <= ((float) this.mScaledTouchSlop)) && (this.mCurrentMode != 3 || Math.abs(y - this.mTouchDownY) <= ((float) this.mScaledTouchSlop))) {
                    return true;
                }
                startDrag(event);
                return true;
            case 3:
                this.mIsDraggingForSliding = false;
                if (this.mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
                return true;
            default:
                return true;
        }
    }

    private void startDrag(MotionEvent event) {
        setPressed(true);
        if (this.mThumb != null) {
            invalidate(this.mThumb.getBounds());
        }
        onStartTrackingTouch();
        trackTouchEvent(event);
        attemptClaimDrag();
    }

    private void setHotspot(float x, float y) {
        Drawable bg = getBackground();
        if (bg != null) {
            DrawableCompat.setHotspot(bg, x, y);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        if (this.mCurrentMode == 3) {
            trackTouchEventInVertical(event);
            return;
        }
        float scale;
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());
        int width = getWidth();
        int availableWidth = (width - getPaddingLeft()) - getPaddingRight();
        float progress = 0.0f;
        if (ViewUtils.isLayoutRtl(this) && this.mMirrorForRtl) {
            if (x > width - getPaddingRight()) {
                scale = 0.0f;
            } else if (x < getPaddingLeft()) {
                scale = 1.0f;
            } else {
                scale = ((float) ((availableWidth - x) + getPaddingLeft())) / ((float) availableWidth);
                progress = this.mTouchProgressOffset;
            }
        } else if (x < getPaddingLeft()) {
            scale = 0.0f;
        } else if (x > width - getPaddingRight()) {
            scale = 1.0f;
        } else {
            scale = ((float) (x - getPaddingLeft())) / ((float) availableWidth);
            progress = this.mTouchProgressOffset;
        }
        int range = getMax() - getMin();
        float basicWidth = 1.0f / ((float) getMax());
        if (scale > 0.0f && scale < 1.0f) {
            float remainder = scale % basicWidth;
            if (remainder > basicWidth / 2.0f) {
                scale += basicWidth - remainder;
            }
        }
        progress += ((float) range) * scale;
        setHotspot((float) x, (float) y);
        setProgressInternal(Math.round(progress), true, false);
    }

    private void trackTouchEventInVertical(MotionEvent event) {
        float scale;
        int height = getHeight();
        int availableHeight = (height - getPaddingTop()) - getPaddingBottom();
        int x = Math.round(event.getX());
        int y = height - Math.round(event.getY());
        float progress = 0.0f;
        if (y < getPaddingBottom()) {
            scale = 0.0f;
        } else if (y > height - getPaddingTop()) {
            scale = 1.0f;
        } else {
            scale = ((float) (y - getPaddingBottom())) / ((float) availableHeight);
            progress = this.mTouchProgressOffset;
        }
        progress += ((float) getMax()) * scale;
        setHotspot((float) x, (float) y);
        setProgressInternal((int) progress, true, false);
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    void onKeyChange() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isEnabled()) {
            int increment = this.mKeyProgressIncrement;
            if (this.mCurrentMode == 3) {
                switch (keyCode) {
                    case 19:
                    case 70:
                    case 81:
                        break;
                    case 20:
                    case 69:
                        increment = -increment;
                        break;
                }
                if (ViewUtils.isLayoutRtl(this)) {
                    increment = -increment;
                }
                if (setProgressInternal(getProgress() + increment, true, true)) {
                    onKeyChange();
                    return true;
                }
            }
            switch (keyCode) {
                case 21:
                case 69:
                    increment = -increment;
                    break;
                case 22:
                case 70:
                case 81:
                    break;
                default:
                    break;
            }
            if (ViewUtils.isLayoutRtl(this)) {
                increment = -increment;
            }
            if (setProgressInternal(getProgress() + increment, true, true)) {
                onKeyChange();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean setProgressInternal(int progress, boolean fromUser, boolean animate) {
        boolean result = super.setProgressInternal(progress, fromUser, animate);
        updateWarningMode(progress);
        updateDualColorMode();
        return result;
    }

    public CharSequence getAccessibilityClassName() {
        Log.d("qweqweqwe", "Stack:", new Throwable("stack dump"));
        return AbsSeekBar.class.getName();
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(SeekBar.class.getName());
        if (isEnabled()) {
            int progress = getProgress();
            if (progress > getMin()) {
                AccessibilityNodeInfoCompat.wrap(info).addAction(8192);
            }
            if (progress < getMax()) {
                AccessibilityNodeInfoCompat.wrap(info).addAction(4096);
            }
        }
    }

    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (super.performAccessibilityAction(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        switch (action) {
            case 4096:
            case 8192:
                if (!canUserSetProgress()) {
                    return false;
                }
                int increment = Math.max(1, Math.round(((float) (getMax() - getMin())) / 20.0f));
                if (action == 8192) {
                    increment = -increment;
                }
                if (!setProgressInternal(getProgress() + increment, true, true)) {
                    return false;
                }
                onKeyChange();
                return true;
            case 16908349:
                if (!canUserSetProgress()) {
                    return false;
                }
                if (arguments == null || !arguments.containsKey(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_PROGRESS_VALUE)) {
                    return false;
                }
                return setProgressInternal((int) arguments.getFloat(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_PROGRESS_VALUE), true, true);
            default:
                return false;
        }
    }

    boolean canUserSetProgress() {
        return !isIndeterminate() && isEnabled();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, getScale(), Integer.MIN_VALUE);
            invalidate();
        }
    }

    void onStartTrackingHover(int hoverLevel, int posX, int posY) {
    }

    void onStopTrackingHover() {
    }

    void onHoverChanged(int hoverLevel, int posX, int posY) {
    }

    private void trackHoverEvent(int posX) {
        float scale;
        int width = getWidth();
        int available = (width - getPaddingLeft()) - getPaddingRight();
        float hoverLevel = 0.0f;
        if (posX < getPaddingLeft()) {
            scale = 0.0f;
        } else if (posX > width - getPaddingRight()) {
            scale = 1.0f;
        } else {
            scale = ((float) (posX - getPaddingLeft())) / ((float) available);
            hoverLevel = this.mTouchProgressOffset;
        }
        this.mHoveringLevel = (int) (hoverLevel + (((float) getMax()) * scale));
    }

    public boolean onHoverEvent(MotionEvent event) {
        if (supportIsHoveringUIEnabled()) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case 7:
                    trackHoverEvent(x);
                    onHoverChanged(this.mHoveringLevel, x, y);
                    if (isHoverPopupTypeUserCustom(getHoverPopupType())) {
                        setHoveringPoint((int) event.getRawX(), (int) event.getRawY());
                        updateHoverPopup();
                        break;
                    }
                    break;
                case 9:
                    trackHoverEvent(x);
                    onStartTrackingHover(this.mHoveringLevel, x, y);
                    break;
                case 10:
                    onStopTrackingHover();
                    break;
            }
        }
        return super.onHoverEvent(event);
    }

    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);
    }

    public Rect getThumbBounds() {
        return this.mThumb != null ? this.mThumb.getBounds() : null;
    }

    public int getThumbHeight() {
        return this.mThumb.getIntrinsicHeight();
    }

    public void setMode(int mode) {
        super.setMode(mode);
        switch (mode) {
            case 0:
                setProgressTintList(this.mDefaultActivatedProgressColor);
                setThumbTintList(this.mDefaultActivatedThumbColor);
                break;
            case 1:
                updateWarningMode(getProgress());
                break;
            case 3:
                setThumb(getContext().getResources().getDrawable(C0247R.drawable.sesl_scrubber_control_anim));
                break;
            case 4:
                this.mSplitProgress = getContext().getResources().getDrawable(C0247R.drawable.sesl_split_seekbar_primary_progress);
                this.mDivider = getContext().getResources().getDrawable(C0247R.drawable.sesl_split_seekbar_vertical_bar);
                updateSplitProgress();
                break;
        }
        invalidate();
    }

    public void setOverlapPointForDualColor(int value) {
        if (value < getMax()) {
            if (value == -1) {
                this.mOverlapPoint = value;
                setProgressTintList(this.mDefaultActivatedProgressColor);
                setThumbTintList(this.mDefaultActivatedThumbColor);
            } else {
                this.mOverlapPoint = value;
                getDualOverlapDrawable();
                updateDualColorMode();
            }
            invalidate();
        }
    }

    private void updateDualColorMode() {
        if (!checkInvalidatedDualColorMode()) {
            DrawableCompat.setTintList(this.mOverlapPrimary, this.mOverlapActivatedProgressColor);
            DrawableCompat.setTintList(this.mOverlapBackground, this.mOverlapNormalProgressColor);
            if (!this.mLargeFont) {
                if (getProgress() > this.mOverlapPoint) {
                    setProgressOverlapTintList(this.mOverlapActivatedProgressColor);
                    setThumbOverlapTintList(this.mOverlapActivatedThumbColor);
                } else {
                    setProgressTintList(this.mDefaultActivatedProgressColor);
                    setThumbTintList(this.mDefaultActivatedThumbColor);
                }
            }
            updateBoundsForDualColor();
        }
    }

    private void updateBoundsForDualColor() {
        if (getCurrentDrawable() != null && !checkInvalidatedDualColorMode()) {
            Rect base = getCurrentDrawable().getBounds();
            int maxProgress = getMax();
            int curProgress = getProgress();
            if (this.mCurrentMode == 0) {
                int left;
                int width = base.right - base.left;
                if (this.mOverlapPoint == 0 || this.mOverlapPoint >= getProgress() || this.mLargeFont) {
                    left = (int) (((float) base.left) + (((float) width) * (((float) this.mOverlapPoint) / ((float) maxProgress))));
                } else {
                    left = base.left;
                }
                int right = Math.min((int) (((float) base.left) + (((float) width) * (((float) curProgress) / ((float) maxProgress)))), base.right);
                this.mOverlapBackground.setBounds(left, base.top, base.right, base.bottom);
                this.mOverlapPrimary.setBounds(left, base.top, right, base.bottom);
            } else if (this.mCurrentMode == 3) {
                int height = base.bottom - base.top;
                int bottom = (int) (((float) base.top) + (((float) height) * (((float) (maxProgress - this.mOverlapPoint)) / ((float) maxProgress))));
                int top = (int) (((float) base.top) + (((float) height) * (((float) (maxProgress - curProgress)) / ((float) maxProgress))));
                this.mOverlapBackground.setBounds(base.left, base.top, base.right, bottom);
                this.mOverlapPrimary.setBounds(base.left, top, base.right, bottom);
            }
        }
    }

    private boolean checkInvalidatedDualColorMode() {
        return this.mOverlapPoint == -1 || this.mOverlapBackground == null;
    }

    private void getDualOverlapDrawable() {
        if (this.mCurrentMode == 0) {
            this.mOverlapPrimary = getContext().getResources().getDrawable(C0247R.drawable.sesl_scrubber_progress_horizontal_extra);
            this.mOverlapBackground = getContext().getResources().getDrawable(C0247R.drawable.sesl_scrubber_progress_horizontal_extra);
        } else if (this.mCurrentMode == 3) {
            this.mOverlapPrimary = getContext().getResources().getDrawable(C0247R.drawable.sesl_scrubber_progress_vertical_extra);
            this.mOverlapBackground = getContext().getResources().getDrawable(C0247R.drawable.sesl_scrubber_progress_vertical_extra);
        }
    }

    protected void updateDrawableBounds(int w, int h) {
        super.updateDrawableBounds(w, h);
        updateThumbAndTrackPos(w, h);
        updateBoundsForDualColor();
    }

    private ColorStateList colorToColorStateList(int color) {
        return new ColorStateList(new int[][]{new int[0]}, new int[]{color});
    }

    private void updateWarningMode(int progress) {
        boolean isMax = true;
        if (this.mCurrentMode == 1) {
            if (progress != getMax()) {
                isMax = false;
            }
            if (isMax) {
                setProgressOverlapTintList(this.mOverlapActivatedProgressColor);
                setThumbOverlapTintList(this.mOverlapActivatedThumbColor);
                return;
            }
            setProgressTintList(this.mDefaultActivatedProgressColor);
            setThumbTintList(this.mDefaultActivatedThumbColor);
        }
    }

    private void initMuteAnimation() {
        this.mMuteAnimationSet = new AnimatorSet();
        List<Animator> list = new ArrayList();
        int duration = MUTE_VIB_DURATION / 8;
        int distance = MUTE_VIB_DISTANCE_LVL;
        for (int i = 0; i < 8; i++) {
            boolean isGoingDirection;
            ValueAnimator progressZeroAnimation;
            if (i % 2 == 0) {
                isGoingDirection = true;
            } else {
                isGoingDirection = false;
            }
            if (isGoingDirection) {
                progressZeroAnimation = ValueAnimator.ofInt(new int[]{0, distance});
            } else {
                progressZeroAnimation = ValueAnimator.ofInt(new int[]{distance, 0});
            }
            progressZeroAnimation.setDuration((long) duration);
            progressZeroAnimation.setInterpolator(new LinearInterpolator());
            progressZeroAnimation.addUpdateListener(new C03471());
            list.add(progressZeroAnimation);
            if (isGoingDirection) {
                distance = (int) (((double) distance) * 0.6d);
            }
        }
        this.mMuteAnimationSet.playSequentially(list);
    }

    private void cancelMuteAnimation() {
        if (this.mMuteAnimationSet != null && this.mMuteAnimationSet.isRunning()) {
            this.mMuteAnimationSet.cancel();
        }
    }

    private void startMuteAnimation() {
        cancelMuteAnimation();
        if (this.mMuteAnimationSet != null) {
            this.mMuteAnimationSet.start();
        }
    }

    protected void onSlidingRefresh(int level) {
        super.onSlidingRefresh(level);
        float scale = ((float) level) / 10000.0f;
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
    }

    private void setThumbOverlapTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
    }

    public void setProgressTintList(ColorStateList tint) {
        super.setProgressTintList(tint);
        this.mDefaultActivatedProgressColor = tint;
    }

    private void setProgressOverlapTintList(ColorStateList tint) {
        super.setProgressTintList(tint);
    }

    public boolean supportIsHoveringUIEnabled() {
        return IS_BASE_SDK_VERSION && SeslViewReflector.isHoveringUIEnabled(this);
    }

    public void setHoverPopupGravity(int type) {
        if (IS_BASE_SDK_VERSION) {
            SeslHoverPopupWindowReflector.setGravity(SeslViewReflector.semGetHoverPopup(this, true), type);
        }
    }

    public void setHoverPopupOffset(int y) {
        if (IS_BASE_SDK_VERSION) {
            SeslHoverPopupWindowReflector.setOffset(SeslViewReflector.semGetHoverPopup(this, true), 0, y);
        }
    }

    public void setHoverPopupDetectTime() {
        if (IS_BASE_SDK_VERSION) {
            SeslHoverPopupWindowReflector.setHoverDetectTime(SeslViewReflector.semGetHoverPopup(this, true), 200);
        }
    }

    public void setHoveringPoint(int x, int y) {
        if (IS_BASE_SDK_VERSION) {
            SeslHoverPopupWindowReflector.setHoveringPoint(this, x, y);
        }
    }

    public void updateHoverPopup() {
        if (IS_BASE_SDK_VERSION) {
            SeslHoverPopupWindowReflector.update(SeslViewReflector.semGetHoverPopup(this, true));
        }
    }

    public boolean isHoverPopupTypeUserCustom(int type) {
        return IS_BASE_SDK_VERSION && type == SeslHoverPopupWindowReflector.getField_TYPE_USER_CUSTOM();
    }

    public boolean isHoverPopupTypeNone(int type) {
        return IS_BASE_SDK_VERSION && type == SeslHoverPopupWindowReflector.getField_TYPE_NONE();
    }

    public int getHoverPopupType() {
        if (IS_BASE_SDK_VERSION) {
            return SeslViewReflector.semGetHoverPopupType(this);
        }
        return 0;
    }

    public boolean supportIsInScrollingContainer() {
        return IS_BASE_SDK_VERSION && SeslViewReflector.isInScrollingContainer(this);
    }
}
