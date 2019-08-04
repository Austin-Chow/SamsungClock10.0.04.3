package com.sec.android.app.clockpackage.commonalert.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.sec.android.app.clockpackage.common.util.Log;

public class AlertClearCircleImageView extends ImageView {
    private Paint mInnerCircle = null;
    private float mRadius = 0.0f;

    public AlertClearCircleImageView(Context context) {
        super(context);
        init();
    }

    public AlertClearCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlertClearCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(1, null);
        this.mInnerCircle = new Paint();
        this.mInnerCircle.setColor(0);
        this.mInnerCircle.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.mInnerCircle.setAntiAlias(true);
        this.mRadius = 0.0f;
    }

    public void clearInnerCircle(float radius) {
        if (radius < 0.0f || radius > ((float) getMeasuredWidth()) / 2.0f) {
            Log.secD("AlertClearCircleImageView", "clearInnerCircle: radius value is wrong");
            return;
        }
        Log.secD("AlertClearCircleImageView", "clearInnerCircle: radius - " + radius);
        this.mRadius = radius;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mInnerCircle != null && this.mRadius > 0.0f) {
            float cx = ((float) getMeasuredWidth()) / 2.0f;
            float cy = ((float) getMeasuredHeight()) / 2.0f;
            Log.secD("AlertClearCircleImageView", "onDraw: cx(" + cx + ") cy(" + cy + ") radius(" + this.mRadius + ")");
            canvas.drawCircle(cx, cy, this.mRadius, this.mInnerCircle);
        }
    }
}
