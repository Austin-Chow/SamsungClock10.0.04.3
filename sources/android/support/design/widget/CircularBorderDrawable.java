package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;

class CircularBorderDrawable extends Drawable {
    private ColorStateList mBorderTint;
    float mBorderWidth;
    private int mBottomInnerStrokeColor;
    private int mBottomOuterStrokeColor;
    private int mCurrentBorderTintColor;
    private boolean mInvalidateShader;
    final Paint mPaint;
    final Rect mRect;
    final RectF mRectF;
    private float mRotation;
    private int mTopInnerStrokeColor;
    private int mTopOuterStrokeColor;

    public void draw(Canvas canvas) {
        if (this.mInvalidateShader) {
            this.mPaint.setShader(createGradientShader());
            this.mInvalidateShader = false;
        }
        float halfBorderWidth = this.mPaint.getStrokeWidth() / 2.0f;
        RectF rectF = this.mRectF;
        copyBounds(this.mRect);
        rectF.set(this.mRect);
        rectF.left += halfBorderWidth;
        rectF.top += halfBorderWidth;
        rectF.right -= halfBorderWidth;
        rectF.bottom -= halfBorderWidth;
        canvas.save();
        canvas.rotate(this.mRotation, rectF.centerX(), rectF.centerY());
        canvas.drawOval(rectF, this.mPaint);
        canvas.restore();
    }

    public boolean getPadding(Rect padding) {
        int borderWidth = Math.round(this.mBorderWidth);
        padding.set(borderWidth, borderWidth, borderWidth, borderWidth);
        return true;
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    void setBorderTint(ColorStateList tint) {
        if (tint != null) {
            this.mCurrentBorderTintColor = tint.getColorForState(getState(), this.mCurrentBorderTintColor);
        }
        this.mBorderTint = tint;
        this.mInvalidateShader = true;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public int getOpacity() {
        return this.mBorderWidth > 0.0f ? -3 : -2;
    }

    final void setRotation(float rotation) {
        if (rotation != this.mRotation) {
            this.mRotation = rotation;
            invalidateSelf();
        }
    }

    protected void onBoundsChange(Rect bounds) {
        this.mInvalidateShader = true;
    }

    public boolean isStateful() {
        return (this.mBorderTint != null && this.mBorderTint.isStateful()) || super.isStateful();
    }

    protected boolean onStateChange(int[] state) {
        if (this.mBorderTint != null) {
            int newColor = this.mBorderTint.getColorForState(state, this.mCurrentBorderTintColor);
            if (newColor != this.mCurrentBorderTintColor) {
                this.mInvalidateShader = true;
                this.mCurrentBorderTintColor = newColor;
            }
        }
        if (this.mInvalidateShader) {
            invalidateSelf();
        }
        return this.mInvalidateShader;
    }

    private Shader createGradientShader() {
        Rect rect = this.mRect;
        copyBounds(rect);
        float borderRatio = this.mBorderWidth / ((float) rect.height());
        return new LinearGradient(0.0f, (float) rect.top, 0.0f, (float) rect.bottom, new int[]{ColorUtils.compositeColors(this.mTopOuterStrokeColor, this.mCurrentBorderTintColor), ColorUtils.compositeColors(this.mTopInnerStrokeColor, this.mCurrentBorderTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.mTopInnerStrokeColor, 0), this.mCurrentBorderTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.mBottomInnerStrokeColor, 0), this.mCurrentBorderTintColor), ColorUtils.compositeColors(this.mBottomInnerStrokeColor, this.mCurrentBorderTintColor), ColorUtils.compositeColors(this.mBottomOuterStrokeColor, this.mCurrentBorderTintColor)}, new float[]{0.0f, borderRatio, 0.5f, 0.5f, 1.0f - borderRatio, 1.0f}, TileMode.CLAMP);
    }
}
