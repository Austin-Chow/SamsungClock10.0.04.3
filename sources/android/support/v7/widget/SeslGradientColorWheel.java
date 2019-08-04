package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

class SeslGradientColorWheel extends View {
    private static final String TAG = "SeslGradientColorWheel";
    private final int[] HUE_COLORS = new int[]{SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
    private Drawable cursorDrawable;
    private final Context mContext;
    private Paint mCursorPaint;
    private final int mCursorPaintSize;
    private float mCursorPosX;
    private float mCursorPosY;
    private final int mCursorStrokeSize;
    private Paint mHuePaint;
    private OnWheelColorChangedListener mListener;
    private int mOrbitalRadius;
    private int mRadius;
    private final Resources mResources;
    private Paint mSaturationPaint;
    private Paint mStrokePaint;

    interface OnWheelColorChangedListener {
        void onWheelColorChanged(float f, float f2);
    }

    void setOnColorWheelInterface(OnWheelColorChangedListener listener) {
        this.mListener = listener;
    }

    public SeslGradientColorWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mResources = context.getResources();
        this.mCursorPaintSize = this.mResources.getDimensionPixelSize(C0247R.dimen.sesl_color_picker_gradient_wheel_cursor_paint_size);
        this.mCursorStrokeSize = this.mResources.getDimensionPixelSize(C0247R.dimen.sesl_color_picker_gradient_wheel_cursor_paint_size) + (this.mResources.getDimensionPixelSize(C0247R.dimen.sesl_color_picker_gradient_wheel_cursor_out_stroke_size) * 2);
        init();
    }

    private void init() {
        int wheelSize = this.mResources.getDimensionPixelSize(C0247R.dimen.sesl_color_picker_gradient_wheel_size) + this.mCursorStrokeSize;
        this.mRadius = wheelSize / 2;
        this.mOrbitalRadius = this.mRadius - (this.mCursorStrokeSize / 2);
        Shader hueShader = new SweepGradient((float) this.mRadius, (float) this.mRadius, this.HUE_COLORS, null);
        this.mHuePaint = new Paint(1);
        this.mHuePaint.setShader(hueShader);
        this.mHuePaint.setStyle(Style.FILL);
        Shader saturationShader = new RadialGradient((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, -1, 0, TileMode.CLAMP);
        this.mSaturationPaint = new Paint(1);
        this.mSaturationPaint.setShader(saturationShader);
        this.mCursorPaint = new Paint();
        this.mStrokePaint = new Paint(1);
        this.mStrokePaint.setColor(this.mContext.getColor(SeslColorPicker.sIsLightTheme ? C0247R.color.sesl_color_picker_stroke_color_light : C0247R.color.sesl_action_bar_background_color_dark));
        this.cursorDrawable = this.mResources.getDrawable(C0247R.drawable.sesl_color_picker_gradient_wheel_cursor);
        LayoutParams layoutParams = new LayoutParams(wheelSize, wheelSize);
        layoutParams.gravity = 1;
        setLayoutParams(layoutParams);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float distance = (float) Math.sqrt(Math.pow((double) (event.getX() - ((float) this.mRadius)), 2.0d) + Math.pow((double) (event.getY() - ((float) this.mRadius)), 2.0d));
        switch (event.getAction()) {
            case 0:
                if (distance <= ((float) this.mRadius)) {
                    playSoundEffect(0);
                    break;
                }
                return false;
            case 2:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
        }
        this.mCursorPosX = event.getX();
        this.mCursorPosY = event.getY();
        if (distance > ((float) this.mOrbitalRadius)) {
            this.mCursorPosX = ((float) this.mRadius) + ((((float) this.mOrbitalRadius) * (this.mCursorPosX - ((float) this.mRadius))) / distance);
            this.mCursorPosY = ((float) this.mRadius) + ((((float) this.mOrbitalRadius) * (this.mCursorPosY - ((float) this.mRadius))) / distance);
        }
        if (this.mListener != null) {
            this.mListener.onWheelColorChanged(((float) ((Math.atan2((double) (this.mCursorPosY - ((float) this.mRadius)), (double) (((float) this.mRadius) - this.mCursorPosX)) * 180.0d) / 3.141592653589793d)) + 180.0f, distance / ((float) this.mOrbitalRadius));
        } else {
            Log.d(TAG, "Listener is not set.");
        }
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, this.mStrokePaint);
        canvas.drawCircle((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, this.mHuePaint);
        canvas.drawCircle((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, this.mSaturationPaint);
        canvas.drawCircle(this.mCursorPosX, this.mCursorPosY, ((float) this.mCursorPaintSize) / 2.0f, this.mCursorPaint);
        this.cursorDrawable.setBounds(((int) this.mCursorPosX) - (this.mCursorPaintSize / 2), ((int) this.mCursorPosY) - (this.mCursorPaintSize / 2), ((int) this.mCursorPosX) + (this.mCursorPaintSize / 2), ((int) this.mCursorPosY) + (this.mCursorPaintSize / 2));
        this.cursorDrawable.draw(canvas);
    }

    void setColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        updateCursorPosition(hsv[0], hsv[1]);
    }

    private void updateCursorPosition(float hue, float saturation) {
        double angle = (((double) hue) * 3.141592653589793d) / 180.0d;
        float distance = ((float) this.mOrbitalRadius) * saturation;
        this.mCursorPosX = (float) ((int) (((double) this.mRadius) + (((double) distance) * Math.cos(angle))));
        this.mCursorPosY = (float) ((int) (((double) this.mRadius) - (((double) distance) * Math.sin(angle))));
        invalidate();
    }

    private static Drawable resizeDrawable(Context context, BitmapDrawable image, int width, int height) {
        if (image == null) {
            return null;
        }
        Bitmap bitmap = image.getBitmap();
        Matrix matrix = new Matrix();
        float scaleWidth = 0.0f;
        float scaleHeight = 0.0f;
        if (width > 0) {
            scaleWidth = ((float) width) / ((float) bitmap.getWidth());
        }
        if (height > 0) {
            scaleHeight = ((float) height) / ((float) bitmap.getHeight());
        }
        matrix.postScale(scaleWidth, scaleHeight);
        return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
    }

    void updateCursorColor(int color) {
        this.mCursorPaint.setColor(color);
    }
}
