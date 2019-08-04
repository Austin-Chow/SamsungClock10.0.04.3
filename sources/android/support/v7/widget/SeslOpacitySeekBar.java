package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.widget.SeekBar;

class SeslOpacitySeekBar extends SeekBar {
    private static final int SEEKBAR_MAX_VALUE = 255;
    private static final String TAG = "SeslOpacitySeekBar";
    private int[] mColors = new int[]{-1, ViewCompat.MEASURED_STATE_MASK};
    private final Context mContext;
    private GradientDrawable mProgressDrawable;
    private final Resources mResources;

    public SeslOpacitySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mResources = context.getResources();
    }

    void init(int color) {
        setMax(255);
        initColor(color);
        initProgressDrawable();
        initThumb();
    }

    private void initColor(int color) {
        hsv = new float[3];
        Color.colorToHSV(color, hsv);
        int alpha = Color.alpha(color);
        this.mColors[0] = Color.HSVToColor(0, hsv);
        this.mColors[1] = Color.HSVToColor(255, hsv);
        setProgress(alpha);
    }

    void restoreColor(int color) {
        initColor(color);
        this.mProgressDrawable.setColors(this.mColors);
        setProgressDrawable(this.mProgressDrawable);
    }

    void changeColorBase(int color) {
        if (this.mProgressDrawable != null) {
            this.mColors[1] = color;
            this.mProgressDrawable.setColors(this.mColors);
            setProgressDrawable(this.mProgressDrawable);
            setProgress(getMax());
        }
    }

    private void initProgressDrawable() {
        this.mProgressDrawable = (GradientDrawable) this.mContext.getDrawable(C0247R.drawable.sesl_color_picker_opacity_seekbar);
        setProgressDrawable(this.mProgressDrawable);
    }

    private void initThumb() {
        setThumb(this.mResources.getDrawable(C0247R.drawable.sesl_color_picker_seekbar_cursor));
        setThumbOffset(0);
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
}
