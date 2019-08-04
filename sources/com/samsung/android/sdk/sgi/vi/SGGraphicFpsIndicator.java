package com.samsung.android.sdk.sgi.vi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGVersionInformation;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;

public class SGGraphicFpsIndicator extends SGFpsIndicator {
    protected int mAvrFps;
    protected int mColor;
    protected int mHeight;
    protected int mHorizontalLine;
    protected int mLast;
    protected long mLastDrawTime;
    protected int mMaxFps;
    protected int mMinFps;
    protected Paint mPaint;
    protected int[] mPoints;
    protected int mSize;
    protected int mStep;
    protected long mUpdateInterval;
    protected int mVerStrWidth;
    protected String mVerString;
    protected int mWidth;

    public SGGraphicFpsIndicator() {
        this(0, 0, Resources.getSystem().getDisplayMetrics().widthPixels / 2, ((double) Resources.getSystem().getDisplayMetrics().density) < 1.5d ? 100 : SGKeyCode.CODE_NUMPAD_6, 60, -1, 0.8f);
    }

    public SGGraphicFpsIndicator(int x, int y, int width, int height, int horizontalLine, int color, float opacity) {
        super(new RectF((float) x, (float) y, (float) (x + width), (float) (y + height)), opacity);
        this.mVerString = "";
        this.mLastDrawTime = 0;
        this.mUpdateInterval = 100;
        this.mStep = 5;
        this.mWidth = width;
        this.mHeight = height;
        this.mHorizontalLine = horizontalLine;
        this.mSize = this.mWidth / this.mStep;
        this.mPoints = new int[this.mSize];
        this.mPaint = new Paint();
        this.mColor = color;
        this.mPaint.setColor(color);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setTextSize(((double) Resources.getSystem().getDisplayMetrics().density) < 1.5d ? 16.0f : 28.0f);
        this.mPaint.setTypeface(Typeface.defaultFromStyle(1));
    }

    private final int getPoint(int num) {
        return ((this.mHeight - 20) - (((this.mHeight - 20) * num) / 60)) + 20;
    }

    protected void onDraw(Bitmap bitmap) {
        int temp = (int) getFps();
        this.mPoints[this.mLast] = temp;
        this.mLast++;
        if (this.mLast >= this.mSize) {
            this.mLast = 0;
        }
        long curTime = System.currentTimeMillis();
        if (curTime - this.mLastDrawTime >= this.mUpdateInterval) {
            this.mLastDrawTime = curTime;
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawLine(0.0f, (float) getPoint(this.mHorizontalLine), (float) this.mWidth, (float) getPoint(this.mHorizontalLine), this.mPaint);
            this.mPaint.setColor(this.mColor);
            int x = 0;
            int curIndex = this.mLast;
            this.mMaxFps = temp;
            this.mAvrFps = temp;
            this.mMinFps = temp;
            int count = 0;
            for (int i = 0; i < this.mSize - 1; i++) {
                int point = this.mPoints[curIndex];
                curIndex++;
                if (curIndex >= this.mSize) {
                    curIndex = 0;
                }
                if (point != 0) {
                    this.mAvrFps += point;
                    count++;
                    if (this.mMaxFps < point) {
                        this.mMaxFps = point;
                    }
                }
                if (this.mMinFps > point) {
                    this.mMinFps = point;
                }
                x += this.mStep;
                canvas.drawLine((float) (x - this.mStep), (float) getPoint(point), (float) x, (float) getPoint(this.mPoints[curIndex]), this.mPaint);
            }
            if (count > 0) {
                this.mAvrFps /= count;
            }
            this.mPaint.setColor(SupportMenu.CATEGORY_MASK);
            canvas.drawText("min = " + this.mMinFps + " avr = " + this.mAvrFps + " max = " + this.mMaxFps, 10.0f, (float) (this.mHeight - 5), this.mPaint);
            canvas.drawText(this.mVerString, (float) ((this.mWidth - this.mVerStrWidth) - 10), (float) (this.mHeight - 5), this.mPaint);
        }
    }

    public final void setShowVersion(boolean show) {
        if (show) {
            SGVersionInformation info = SGConfiguration.getVersionInformation();
            this.mVerString = "" + info.mMajor + "." + info.mMinor + "." + info.mPatch + "." + info.mBuild + " - " + info.mDate + " Release";
            this.mVerStrWidth = (int) this.mPaint.measureText(this.mVerString, 0, this.mVerString.length());
            return;
        }
        this.mVerString = "";
        this.mVerStrWidth = 0;
    }

    public final void setUpdateInterval(int interval) {
        this.mUpdateInterval = (long) interval;
    }
}
