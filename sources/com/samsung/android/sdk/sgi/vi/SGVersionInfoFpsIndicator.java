package com.samsung.android.sdk.sgi.vi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import com.samsung.android.sdk.sgi.base.SGConfiguration;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVersionInformation;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;

public final class SGVersionInfoFpsIndicator extends SGFpsIndicator {
    private boolean mDrawn;
    private Paint mPaint;
    private String mString;

    public SGVersionInfoFpsIndicator() {
        this(0, 0, SupportMenu.CATEGORY_MASK);
    }

    public SGVersionInfoFpsIndicator(int x, int y, int textColor) {
        super(new RectF(), 1.0f);
        this.mDrawn = false;
        this.mPaint = new Paint();
        this.mPaint.setColor(textColor);
        this.mPaint.setTextSize(Resources.getSystem().getDisplayMetrics().density < 1.5f ? 16.0f : 28.0f);
        SGVersionInformation info = SGConfiguration.getVersionInformation();
        this.mString = "" + info.mMajor + "." + info.mMinor + "." + info.mPatch + "." + info.mBuild + " - " + info.mDate + " Release";
        setSize(new SGVector2f(this.mPaint.measureText(this.mString, 0, this.mString.length()) + 20.0f, 30.0f));
        setPosition(new SGVector2f((float) x, (float) y));
    }

    protected void onDraw(Bitmap bitmap) {
        if (!this.mDrawn) {
            this.mDrawn = true;
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.argb(SGKeyCode.CODE_NUMPAD_6, 0, 0, 0));
            canvas.drawText(this.mString, (float) 10, (float) (15 - (((int) (this.mPaint.ascent() + this.mPaint.descent())) / 2)), this.mPaint);
        }
    }
}
