package android.support.design.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

public class SeslTabDotLineIndicator extends SeslAbsIndicatorView {
    private int mDiameter;
    private int mInterval;
    private Paint mPaint;
    private float mScaleFrom;
    private final float mScaleFromDiff;
    private int mWidth;

    public SeslTabDotLineIndicator(Context context) {
        this(context, null);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mInterval = 2;
        this.mDiameter = 1;
        this.mDiameter = (int) TypedValue.applyDimension(1, 2.5f, context.getResources().getDisplayMetrics());
        this.mInterval = (int) TypedValue.applyDimension(1, 2.5f, context.getResources().getDisplayMetrics());
        this.mPaint = new Paint();
        this.mPaint.setFlags(1);
        this.mScaleFromDiff = TypedValue.applyDimension(1, 5.0f, context.getResources().getDisplayMetrics());
    }

    void onHide() {
        setAlpha(0.0f);
    }

    public void setHideImmediatly() {
        setAlpha(0.0f);
    }

    void onShow() {
        onReleased();
    }

    void onPressed() {
        setAlpha(1.0f);
        invalidate();
    }

    void onReleased() {
        setAlpha(1.0f);
    }

    private void updateDotLineScaleFrom() {
        if (this.mWidth != getWidth() || this.mWidth == 0) {
            this.mWidth = getWidth();
            if (this.mWidth <= 0) {
                this.mScaleFrom = 0.9f;
            } else {
                this.mScaleFrom = (((float) this.mWidth) - this.mScaleFromDiff) / ((float) this.mWidth);
            }
        }
    }

    void onSetSelectedIndicatorColor(int color) {
        this.mPaint.setColor(color);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateDotLineScaleFrom();
        if ((isPressed() || isSelected()) && (getBackground() instanceof ColorDrawable)) {
            int width = getWidth();
            int height = getHeight();
            int circleCount = ((width - this.mDiameter) / (this.mInterval + this.mDiameter)) + 1;
            int spaceCount = circleCount - 1;
            int startInterval = (int) ((((float) this.mDiameter) / 2.0f) + 0.5f);
            int leftSpace = (width - this.mDiameter) - ((this.mInterval + this.mDiameter) * (circleCount - 1));
            if (this.mDiameter % 2 != 0) {
                leftSpace--;
            }
            int offSet = 0;
            int offSetOnePixel = 0;
            if (spaceCount > 0) {
                offSet = leftSpace / spaceCount;
                offSetOnePixel = leftSpace % spaceCount;
            }
            int dis = 0;
            for (int i = 0; i < circleCount; i++) {
                canvas.drawCircle((float) (startInterval + dis), (float) (height / 2), ((float) this.mDiameter) / 2.0f, this.mPaint);
                dis += (this.mDiameter + this.mInterval) + offSet;
                if (i < offSetOnePixel) {
                    dis++;
                }
            }
        }
    }
}
