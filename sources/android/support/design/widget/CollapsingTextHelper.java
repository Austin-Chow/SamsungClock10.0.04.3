package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.v4.math.MathUtils;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.animation.Interpolator;

final class CollapsingTextHelper {
    private static final Paint DEBUG_DRAW_PAINT = null;
    private static final boolean USE_SCALING_TEXTURE = (VERSION.SDK_INT < 18);
    private boolean mBoundsChanged;
    private final Rect mCollapsedBounds;
    private float mCollapsedDrawX;
    private float mCollapsedDrawY;
    private int mCollapsedShadowColor;
    private float mCollapsedShadowDx;
    private float mCollapsedShadowDy;
    private float mCollapsedShadowRadius;
    private ColorStateList mCollapsedTextColor;
    private int mCollapsedTextGravity = 16;
    private float mCollapsedTextSize = 15.0f;
    private Typeface mCollapsedTypeface;
    private final RectF mCurrentBounds;
    private float mCurrentDrawX;
    private float mCurrentDrawY;
    private float mCurrentTextSize;
    private Typeface mCurrentTypeface;
    private boolean mDrawTitle;
    private final Rect mExpandedBounds;
    private float mExpandedDrawX;
    private float mExpandedDrawY;
    private float mExpandedFraction;
    private int mExpandedShadowColor;
    private float mExpandedShadowDx;
    private float mExpandedShadowDy;
    private float mExpandedShadowRadius;
    private ColorStateList mExpandedTextColor;
    private int mExpandedTextGravity = 16;
    private float mExpandedTextSize = 15.0f;
    private Bitmap mExpandedTitleTexture;
    private Typeface mExpandedTypeface;
    private boolean mIsRtl;
    private Interpolator mPositionInterpolator;
    private float mScale;
    private int[] mState;
    private CharSequence mText;
    private final TextPaint mTextPaint;
    private Interpolator mTextSizeInterpolator;
    private CharSequence mTextToDraw;
    private final View mView;

    static {
        if (DEBUG_DRAW_PAINT != null) {
            DEBUG_DRAW_PAINT.setAntiAlias(true);
            DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }

    public CollapsingTextHelper(View view) {
        this.mView = view;
        this.mTextPaint = new TextPaint(129);
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
    }

    void setTextSizeInterpolator(Interpolator interpolator) {
        this.mTextSizeInterpolator = interpolator;
        recalculate();
    }

    void setPositionInterpolator(Interpolator interpolator) {
        this.mPositionInterpolator = interpolator;
        recalculate();
    }

    void setExpandedTextSize(float textSize) {
        if (this.mExpandedTextSize != textSize) {
            this.mExpandedTextSize = textSize;
            recalculate();
        }
    }

    void setCollapsedTextColor(ColorStateList textColor) {
        if (this.mCollapsedTextColor != textColor) {
            this.mCollapsedTextColor = textColor;
            recalculate();
        }
    }

    void setExpandedTextColor(ColorStateList textColor) {
        if (this.mExpandedTextColor != textColor) {
            this.mExpandedTextColor = textColor;
            recalculate();
        }
    }

    void setExpandedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(this.mExpandedBounds, left, top, right, bottom)) {
            this.mExpandedBounds.set(left, top, right, bottom);
            this.mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    void setCollapsedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(this.mCollapsedBounds, left, top, right, bottom)) {
            this.mCollapsedBounds.set(left, top, right, bottom);
            this.mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    void onBoundsChanged() {
        boolean z = this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0;
        this.mDrawTitle = z;
    }

    void setExpandedTextGravity(int gravity) {
        if (this.mExpandedTextGravity != gravity) {
            this.mExpandedTextGravity = gravity;
            recalculate();
        }
    }

    int getExpandedTextGravity() {
        return this.mExpandedTextGravity;
    }

    void setCollapsedTextGravity(int gravity) {
        if (this.mCollapsedTextGravity != gravity) {
            this.mCollapsedTextGravity = gravity;
            recalculate();
        }
    }

    int getCollapsedTextGravity() {
        return this.mCollapsedTextGravity;
    }

    void setCollapsedTextAppearance(int resId) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), resId, C0247R.styleable.TextAppearance);
        if (a.hasValue(C0247R.styleable.TextAppearance_android_textColor)) {
            this.mCollapsedTextColor = a.getColorStateList(C0247R.styleable.TextAppearance_android_textColor);
        }
        if (a.hasValue(C0247R.styleable.TextAppearance_android_textSize)) {
            this.mCollapsedTextSize = (float) a.getDimensionPixelSize(C0247R.styleable.TextAppearance_android_textSize, (int) this.mCollapsedTextSize);
        }
        this.mCollapsedShadowColor = a.getInt(C0247R.styleable.TextAppearance_android_shadowColor, 0);
        this.mCollapsedShadowDx = a.getFloat(C0247R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mCollapsedShadowDy = a.getFloat(C0247R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mCollapsedShadowRadius = a.getFloat(C0247R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        a.recycle();
        if (VERSION.SDK_INT >= 16) {
            this.mCollapsedTypeface = readFontFamilyTypeface(resId);
        }
        recalculate();
    }

    void setExpandedTextAppearance(int resId) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), resId, C0247R.styleable.TextAppearance);
        if (a.hasValue(C0247R.styleable.TextAppearance_android_textColor)) {
            this.mExpandedTextColor = a.getColorStateList(C0247R.styleable.TextAppearance_android_textColor);
        }
        if (a.hasValue(C0247R.styleable.TextAppearance_android_textSize)) {
            this.mExpandedTextSize = (float) a.getDimensionPixelSize(C0247R.styleable.TextAppearance_android_textSize, (int) this.mExpandedTextSize);
        }
        this.mExpandedShadowColor = a.getInt(C0247R.styleable.TextAppearance_android_shadowColor, 0);
        this.mExpandedShadowDx = a.getFloat(C0247R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mExpandedShadowDy = a.getFloat(C0247R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mExpandedShadowRadius = a.getFloat(C0247R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        a.recycle();
        if (VERSION.SDK_INT >= 16) {
            this.mExpandedTypeface = readFontFamilyTypeface(resId);
        }
        recalculate();
    }

    private Typeface readFontFamilyTypeface(int resId) {
        TypedArray a = this.mView.getContext().obtainStyledAttributes(resId, new int[]{16843692});
        try {
            String family = a.getString(0);
            if (family != null) {
                Typeface create = Typeface.create(family, 0);
                return create;
            }
            a.recycle();
            return null;
        } finally {
            a.recycle();
        }
    }

    void setCollapsedTypeface(Typeface typeface) {
        if (areTypefacesDifferent(this.mCollapsedTypeface, typeface)) {
            this.mCollapsedTypeface = typeface;
            recalculate();
        }
    }

    void setExpandedTypeface(Typeface typeface) {
        if (areTypefacesDifferent(this.mExpandedTypeface, typeface)) {
            this.mExpandedTypeface = typeface;
            recalculate();
        }
    }

    void setTypefaces(Typeface typeface) {
        this.mExpandedTypeface = typeface;
        this.mCollapsedTypeface = typeface;
        recalculate();
    }

    Typeface getCollapsedTypeface() {
        return this.mCollapsedTypeface != null ? this.mCollapsedTypeface : Typeface.DEFAULT;
    }

    Typeface getExpandedTypeface() {
        return this.mExpandedTypeface != null ? this.mExpandedTypeface : Typeface.DEFAULT;
    }

    void setExpansionFraction(float fraction) {
        fraction = MathUtils.clamp(fraction, 0.0f, 1.0f);
        if (fraction != this.mExpandedFraction) {
            this.mExpandedFraction = fraction;
            calculateCurrentOffsets();
        }
    }

    final boolean setState(int[] state) {
        this.mState = state;
        if (!isStateful()) {
            return false;
        }
        recalculate();
        return true;
    }

    final boolean isStateful() {
        return (this.mCollapsedTextColor != null && this.mCollapsedTextColor.isStateful()) || (this.mExpandedTextColor != null && this.mExpandedTextColor.isStateful());
    }

    float getExpansionFraction() {
        return this.mExpandedFraction;
    }

    float getCollapsedTextSize() {
        return this.mCollapsedTextSize;
    }

    private void calculateCurrentOffsets() {
        calculateOffsets(this.mExpandedFraction);
    }

    private void calculateOffsets(float fraction) {
        interpolateBounds(fraction);
        this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, fraction, this.mPositionInterpolator);
        this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, fraction, this.mPositionInterpolator);
        setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, fraction, this.mTextSizeInterpolator));
        if (this.mCollapsedTextColor != this.mExpandedTextColor) {
            this.mTextPaint.setColor(blendColors(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), fraction));
        } else {
            this.mTextPaint.setColor(getCurrentCollapsedTextColor());
        }
        this.mTextPaint.setShadowLayer(lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, fraction, null), lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, fraction, null), lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, fraction, null), blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, fraction));
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }

    private int getCurrentExpandedTextColor() {
        if (this.mState != null) {
            return this.mExpandedTextColor.getColorForState(this.mState, 0);
        }
        return this.mExpandedTextColor.getDefaultColor();
    }

    private int getCurrentCollapsedTextColor() {
        if (this.mState != null) {
            return this.mCollapsedTextColor.getColorForState(this.mState, 0);
        }
        return this.mCollapsedTextColor.getDefaultColor();
    }

    private void calculateBaseOffsets() {
        float width;
        int i;
        int i2 = 1;
        float currentTextSize = this.mCurrentTextSize;
        calculateUsingTextSize(this.mCollapsedTextSize);
        if (this.mTextToDraw != null) {
            width = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
        } else {
            width = 0.0f;
        }
        int i3 = this.mCollapsedTextGravity;
        if (this.mIsRtl) {
            i = 1;
        } else {
            i = 0;
        }
        int collapsedAbsGravity = GravityCompat.getAbsoluteGravity(i3, i);
        switch (collapsedAbsGravity & 112) {
            case 48:
                this.mCollapsedDrawY = ((float) this.mCollapsedBounds.top) - this.mTextPaint.ascent();
                break;
            case 80:
                this.mCollapsedDrawY = (float) this.mCollapsedBounds.bottom;
                break;
            default:
                this.mCollapsedDrawY = ((float) this.mCollapsedBounds.centerY()) + (((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f) - this.mTextPaint.descent());
                break;
        }
        switch (collapsedAbsGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case 1:
                this.mCollapsedDrawX = ((float) this.mCollapsedBounds.centerX()) - (width / 2.0f);
                break;
            case 5:
                this.mCollapsedDrawX = ((float) this.mCollapsedBounds.right) - width;
                break;
            default:
                this.mCollapsedDrawX = (float) this.mCollapsedBounds.left;
                break;
        }
        calculateUsingTextSize(this.mExpandedTextSize);
        if (this.mTextToDraw != null) {
            width = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
        } else {
            width = 0.0f;
        }
        int i4 = this.mExpandedTextGravity;
        if (!this.mIsRtl) {
            i2 = 0;
        }
        int expandedAbsGravity = GravityCompat.getAbsoluteGravity(i4, i2);
        switch (expandedAbsGravity & 112) {
            case 48:
                this.mExpandedDrawY = ((float) this.mExpandedBounds.top) - this.mTextPaint.ascent();
                break;
            case 80:
                this.mExpandedDrawY = (float) this.mExpandedBounds.bottom;
                break;
            default:
                this.mExpandedDrawY = ((float) this.mExpandedBounds.centerY()) + (((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f) - this.mTextPaint.descent());
                break;
        }
        switch (expandedAbsGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case 1:
                this.mExpandedDrawX = ((float) this.mExpandedBounds.centerX()) - (width / 2.0f);
                break;
            case 5:
                this.mExpandedDrawX = ((float) this.mExpandedBounds.right) - width;
                break;
            default:
                this.mExpandedDrawX = (float) this.mExpandedBounds.left;
                break;
        }
        clearTexture();
        setInterpolatedTextSize(currentTextSize);
    }

    private void interpolateBounds(float fraction) {
        this.mCurrentBounds.left = lerp((float) this.mExpandedBounds.left, (float) this.mCollapsedBounds.left, fraction, this.mPositionInterpolator);
        this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, fraction, this.mPositionInterpolator);
        this.mCurrentBounds.right = lerp((float) this.mExpandedBounds.right, (float) this.mCollapsedBounds.right, fraction, this.mPositionInterpolator);
        this.mCurrentBounds.bottom = lerp((float) this.mExpandedBounds.bottom, (float) this.mCollapsedBounds.bottom, fraction, this.mPositionInterpolator);
    }

    public void draw(Canvas canvas) {
        int saveCount = canvas.save();
        if (this.mTextToDraw != null && this.mDrawTitle) {
            float x = this.mCurrentDrawX;
            float y = this.mCurrentDrawY;
            float ascent = this.mTextPaint.ascent() * this.mScale;
            float descent = this.mTextPaint.descent() * this.mScale;
            if (this.mScale != 1.0f) {
                canvas.scale(this.mScale, this.mScale, x, y);
            }
            canvas.drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), x, y, this.mTextPaint);
        }
        canvas.restoreToCount(saveCount);
    }

    private boolean calculateIsRtl(CharSequence text) {
        boolean defaultIsRtl = true;
        if (ViewCompat.getLayoutDirection(this.mView) != 1) {
            defaultIsRtl = false;
        }
        return (defaultIsRtl ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(text, 0, text.length());
    }

    private void setInterpolatedTextSize(float textSize) {
        calculateUsingTextSize(textSize);
        this.mView.postInvalidateOnAnimation();
    }

    private boolean areTypefacesDifferent(Typeface first, Typeface second) {
        return !(first == null || first.equals(second)) || (first == null && second != null);
    }

    private void calculateUsingTextSize(float textSize) {
        boolean z = true;
        if (this.mText != null) {
            float newTextSize;
            float availableWidth;
            float collapsedWidth = (float) this.mCollapsedBounds.width();
            float expandedWidth = (float) this.mExpandedBounds.width();
            boolean updateDrawText = false;
            if (isClose(textSize, this.mCollapsedTextSize)) {
                newTextSize = this.mCollapsedTextSize;
                this.mScale = 1.0f;
                if (areTypefacesDifferent(this.mCurrentTypeface, this.mCollapsedTypeface)) {
                    this.mCurrentTypeface = this.mCollapsedTypeface;
                    updateDrawText = true;
                }
                availableWidth = collapsedWidth;
            } else {
                newTextSize = this.mExpandedTextSize;
                if (areTypefacesDifferent(this.mCurrentTypeface, this.mExpandedTypeface)) {
                    this.mCurrentTypeface = this.mExpandedTypeface;
                    updateDrawText = true;
                }
                if (isClose(textSize, this.mExpandedTextSize)) {
                    this.mScale = 1.0f;
                } else {
                    this.mScale = textSize / this.mExpandedTextSize;
                }
                float textSizeRatio = this.mCollapsedTextSize / this.mExpandedTextSize;
                if (expandedWidth * textSizeRatio > collapsedWidth) {
                    availableWidth = Math.min(collapsedWidth / textSizeRatio, expandedWidth);
                } else {
                    availableWidth = expandedWidth;
                }
            }
            if (availableWidth > 0.0f) {
                if (this.mCurrentTextSize != newTextSize || this.mBoundsChanged || updateDrawText) {
                    updateDrawText = true;
                } else {
                    updateDrawText = false;
                }
                this.mCurrentTextSize = newTextSize;
                this.mBoundsChanged = false;
            }
            if (this.mTextToDraw == null || updateDrawText) {
                this.mTextPaint.setTextSize(this.mCurrentTextSize);
                this.mTextPaint.setTypeface(this.mCurrentTypeface);
                TextPaint textPaint = this.mTextPaint;
                if (this.mScale == 1.0f) {
                    z = false;
                }
                textPaint.setLinearText(z);
                CharSequence title = TextUtils.ellipsize(this.mText, this.mTextPaint, availableWidth, TruncateAt.END);
                if (!TextUtils.equals(title, this.mTextToDraw)) {
                    this.mTextToDraw = title;
                    this.mIsRtl = calculateIsRtl(this.mTextToDraw);
                }
            }
        }
    }

    public void recalculate() {
        if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
            calculateBaseOffsets();
            calculateCurrentOffsets();
        }
    }

    void setText(CharSequence text) {
        if (text == null || !text.equals(this.mText)) {
            this.mText = text;
            this.mTextToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    CharSequence getText() {
        return this.mText;
    }

    private void clearTexture() {
        if (this.mExpandedTitleTexture != null) {
            this.mExpandedTitleTexture.recycle();
            this.mExpandedTitleTexture = null;
        }
    }

    private static boolean isClose(float value, float targetValue) {
        return Math.abs(value - targetValue) < 0.001f;
    }

    ColorStateList getCollapsedTextColor() {
        return this.mCollapsedTextColor;
    }

    private static int blendColors(int color1, int color2, float ratio) {
        float inverseRatio = 1.0f - ratio;
        return Color.argb((int) ((((float) Color.alpha(color1)) * inverseRatio) + (((float) Color.alpha(color2)) * ratio)), (int) ((((float) Color.red(color1)) * inverseRatio) + (((float) Color.red(color2)) * ratio)), (int) ((((float) Color.green(color1)) * inverseRatio) + (((float) Color.green(color2)) * ratio)), (int) ((((float) Color.blue(color1)) * inverseRatio) + (((float) Color.blue(color2)) * ratio)));
    }

    private static float lerp(float startValue, float endValue, float fraction, Interpolator interpolator) {
        if (interpolator != null) {
            fraction = interpolator.getInterpolation(fraction);
        }
        return AnimationUtils.lerp(startValue, endValue, fraction);
    }

    private static boolean rectEquals(Rect r, int left, int top, int right, int bottom) {
        return r.left == left && r.top == top && r.right == right && r.bottom == bottom;
    }
}
