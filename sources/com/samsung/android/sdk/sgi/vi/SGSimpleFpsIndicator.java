package com.samsung.android.sdk.sgi.vi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;

public class SGSimpleFpsIndicator extends SGFpsIndicator {
    protected long mLastDrawTime;
    protected Paint mPaint;
    protected long mUpdateInterval;

    public SGSimpleFpsIndicator() {
        this(0, 0, SupportMenu.CATEGORY_MASK);
    }

    public SGSimpleFpsIndicator(int x, int y, int textColor) {
        super(new RectF((float) x, (float) y, (Resources.getSystem().getConfiguration().screenLayout & 15) == 2 ? 70.0f : (float) (x + 90), (Resources.getSystem().getConfiguration().screenLayout & 15) == 2 ? 30.0f : (float) (y + 40)), 1.0f);
        this.mLastDrawTime = 0;
        this.mUpdateInterval = 100;
        this.mPaint = new Paint();
        this.mPaint.setColor(textColor);
        this.mPaint.setTextSize(Resources.getSystem().getDisplayMetrics().density < 1.5f ? 16.0f : 28.0f);
    }

    protected void onDraw(Bitmap bitmap) {
        long curTime = System.currentTimeMillis();
        if (curTime - this.mLastDrawTime >= this.mUpdateInterval) {
            this.mLastDrawTime = curTime;
            String str = String.format("%.1f", new Object[]{Float.valueOf(getFps())});
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawText(str, 10.0f, (float) (15 - (((int) (this.mPaint.ascent() + this.mPaint.descent())) / 2)), this.mPaint);
        }
    }

    public final void setUpdateInterval(int interval) {
        this.mUpdateInterval = (long) interval;
    }
}
