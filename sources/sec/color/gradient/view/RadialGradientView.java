package sec.color.gradient.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import java.io.IOException;
import java.io.InputStream;
import sec.color.gradient.interpolater.EasingSineFunc;

public class RadialGradientView extends View {
    float baseScaleHeight = 0.0f;
    float baseScaleWidth = 0.0f;
    float gradient1_AlphaValue = 0.0f;
    float gradient1_ScaleValue = 0.0f;
    float gradient1_alpha = 0.8f;
    float gradient1_x = 0.25f;
    float gradient1_y = 0.43f;
    float gradient2_alpha = 1.0f;
    float gradient2_x = 1.2f;
    float gradient2_y = -0.09f;
    float gradient3_AlphaValue = 0.0f;
    float gradient3_ScaleValue = 0.0f;
    float gradient3_alpha = 0.9f;
    float gradient3_x = 0.45f;
    float gradient3_y = 0.43f;
    AnimationState mAnimationState;
    Bitmap mBitmapGradientPatternWhite = null;
    int mColor1 = SupportMenu.CATEGORY_MASK;
    int mColor2 = -16711936;
    int mColor3 = -16776961;
    ColorFilter mColorFilter1 = null;
    ColorFilter mColorFilter2 = null;
    ColorFilter mColorFilter3 = null;
    boolean mFlagInit = false;
    boolean mFlagShowArc = false;
    int mGradientRadial;
    float mGradientScale;
    Paint mPaint;
    Paint mPaintArc;
    ValueAnimator mRepeatAnimator;
    int mRepeatDuration = 4000;
    int mStartDuration = 2000;
    ValueAnimator mStartGradient1_Animator;
    int mStartGradient1_Delay = 400;
    ValueAnimator mStartGradient3_Animator;
    int mViewHeight;
    int mViewWidth;
    float scale1 = 1.3f;
    float scale2 = 1.8f;
    float scale3 = 2.0f;

    /* renamed from: sec.color.gradient.view.RadialGradientView$1 */
    class C09201 implements AnimatorListener {
        C09201() {
        }

        public void onAnimationStart(Animator animator) {
        }

        public void onAnimationEnd(Animator animator) {
            RadialGradientView.this.mAnimationState = AnimationState.REPEAT;
            RadialGradientView.this.mRepeatAnimator.start();
        }

        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationRepeat(Animator animator) {
        }
    }

    /* renamed from: sec.color.gradient.view.RadialGradientView$2 */
    class C09212 implements AnimatorUpdateListener {
        C09212() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            RadialGradientView.this.invalidate();
        }
    }

    /* renamed from: sec.color.gradient.view.RadialGradientView$3 */
    class C09223 implements AnimatorUpdateListener {
        C09223() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            RadialGradientView.this.invalidate();
        }
    }

    public enum AnimationState {
        NONE,
        START,
        REPEAT,
        END
    }

    protected void onDraw(Canvas canvas) {
        if (this.mFlagInit) {
            int i;
            this.baseScaleWidth = (((float) this.mViewWidth) / ((float) (this.mGradientRadial + this.mGradientRadial))) * this.mGradientScale;
            this.baseScaleHeight = this.baseScaleWidth;
            if (this.mAnimationState == AnimationState.START) {
                this.gradient1_AlphaValue = EasingSineFunc.getInstance().easeInOut(((Float) this.mStartGradient1_Animator.getAnimatedValue()).floatValue(), 0.0f, 1.0f, 1.0f);
                this.gradient3_AlphaValue = EasingSineFunc.getInstance().easeInOut(((Float) this.mStartGradient3_Animator.getAnimatedValue()).floatValue(), 0.0f, 1.0f, 1.0f);
                this.gradient1_ScaleValue = (float) (((0.5d * ((double) this.scale1)) * ((double) this.baseScaleWidth)) + (((double) ((this.scale1 * this.baseScaleWidth) * this.gradient1_AlphaValue)) * 0.7d));
                this.gradient3_ScaleValue = (float) (((0.5d * ((double) this.scale3)) * ((double) this.baseScaleWidth)) + (((double) ((this.scale3 * this.baseScaleWidth) * this.gradient3_AlphaValue)) * 0.7d));
            } else if (this.mAnimationState == AnimationState.REPEAT) {
                this.gradient1_AlphaValue = EasingSineFunc.getInstance().easeInOut(((Float) this.mRepeatAnimator.getAnimatedValue()).floatValue(), 0.0f, 1.0f, 1.0f);
                this.gradient3_AlphaValue = EasingSineFunc.getInstance().easeInOut(((Float) this.mRepeatAnimator.getAnimatedValue()).floatValue(), 0.0f, 1.0f, 1.0f);
                this.gradient1_ScaleValue = (float) (((0.85d * ((double) this.scale1)) * ((double) this.baseScaleWidth)) + (((double) ((this.scale1 * this.baseScaleWidth) * this.gradient1_AlphaValue)) * 0.35d));
                this.gradient3_ScaleValue = (float) (((0.85d * ((double) this.scale3)) * ((double) this.baseScaleWidth)) + (((double) ((this.scale3 * this.baseScaleWidth) * this.gradient3_AlphaValue)) * 0.35d));
            }
            this.mPaint.setColorFilter(this.mColorFilter3);
            if (this.mAnimationState == AnimationState.START) {
                this.mPaint.setAlpha((int) ((this.gradient3_alpha * this.gradient3_AlphaValue) * 255.0f));
            } else if (this.mAnimationState == AnimationState.REPEAT) {
                this.mPaint.setAlpha((int) (this.gradient3_alpha * 255.0f));
            }
            for (i = 0; i < 4; i++) {
                canvas.save();
                canvas.translate(((float) this.mViewWidth) * this.gradient3_x, ((float) this.mViewWidth) * this.gradient3_y);
                canvas.scale(this.gradient3_ScaleValue, this.gradient3_ScaleValue, this.scale3 / 0.5f, this.scale3 / 0.5f);
                canvas.rotate((float) (i * 90), 0.0f, 0.0f);
                canvas.drawBitmap(this.mBitmapGradientPatternWhite, 0.0f, 0.0f, this.mPaint);
                if (this.mFlagShowArc) {
                    this.mPaintArc.setColor(-16776961);
                    canvas.drawArc((float) (-this.mGradientRadial), (float) (-this.mGradientRadial), (float) this.mGradientRadial, (float) this.mGradientRadial, 270.0f, 90.0f, true, this.mPaintArc);
                }
                canvas.restore();
            }
            this.mPaint.setColorFilter(this.mColorFilter2);
            if (this.mAnimationState == AnimationState.START || this.mAnimationState == AnimationState.REPEAT) {
                this.mPaint.setAlpha((int) (this.gradient2_alpha * 255.0f));
            }
            for (i = 0; i < 4; i++) {
                canvas.save();
                canvas.translate(((float) this.mViewWidth) * this.gradient2_x, ((float) this.mViewWidth) * this.gradient2_y);
                canvas.scale(this.scale2 * this.baseScaleWidth, this.scale2 * this.baseScaleWidth, 0.0f, 0.0f);
                canvas.rotate((float) (i * 90), 0.0f, 0.0f);
                canvas.drawBitmap(this.mBitmapGradientPatternWhite, 0.0f, 0.0f, this.mPaint);
                if (this.mFlagShowArc) {
                    this.mPaintArc.setColor(-7829368);
                    canvas.drawArc((float) (-this.mGradientRadial), (float) (-this.mGradientRadial), (float) this.mGradientRadial, (float) this.mGradientRadial, 270.0f, 90.0f, true, this.mPaintArc);
                }
                canvas.restore();
            }
            this.mPaint.setColorFilter(this.mColorFilter1);
            if (this.mAnimationState == AnimationState.START) {
                this.mPaint.setAlpha((int) ((this.gradient1_alpha * this.gradient1_AlphaValue) * 255.0f));
            } else if (this.mAnimationState == AnimationState.REPEAT) {
                this.mPaint.setAlpha((int) (this.gradient1_alpha * 255.0f));
            }
            for (i = 0; i < 4; i++) {
                canvas.save();
                canvas.translate(((float) this.mViewWidth) * this.gradient1_x, ((float) this.mViewWidth) * this.gradient1_y);
                canvas.scale(this.gradient1_ScaleValue, this.gradient1_ScaleValue, this.scale1 / 0.5f, this.scale1 / 0.5f);
                canvas.rotate((float) (i * 90), 0.0f, 0.0f);
                canvas.drawBitmap(this.mBitmapGradientPatternWhite, 0.0f, 0.0f, this.mPaint);
                if (this.mFlagShowArc) {
                    this.mPaintArc.setColor(-65281);
                    canvas.drawArc((float) (-this.mGradientRadial), (float) (-this.mGradientRadial), (float) this.mGradientRadial, (float) this.mGradientRadial, 270.0f, 90.0f, true, this.mPaintArc);
                }
                canvas.restore();
            }
        } else {
            Log.d("[RadialGradientView]", "This is not initialized. RadialGradientView must be initialize by init() method.");
            Log.d("[RadialGradientView]", "And RadialGradientView must be set color in each gradient color by setColors() method.");
            Log.d("[RadialGradientView]", "And RadialGradientView must be set alpha in each gradient color by setAlphas() method.");
            if (this.mColorFilter1 == null || this.mColorFilter2 == null || this.mColorFilter3 == null) {
                Log.d("[RadialGradientView]", "Android ColorFilter is null.");
            }
        }
        super.onDraw(canvas);
    }

    public void init() {
        String mapFile = "radial_gradient.gm";
        this.mGradientRadial = 2048;
        this.mGradientScale = 1.0f;
        AssetManager assetManager = getContext().getAssets();
        Options o = new Options();
        o.inScaled = false;
        InputStream isBitmapDithered = null;
        try {
            isBitmapDithered = assetManager.open(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap gradientBitmap = BitmapFactory.decodeStream(isBitmapDithered, null, o);
        int width = gradientBitmap.getWidth();
        int height = gradientBitmap.getHeight();
        this.mBitmapGradientPatternWhite = Bitmap.createBitmap(width, height, Config.ALPHA_8);
        int[] pixels = new int[width];
        for (int y = 0; y < height; y++) {
            gradientBitmap.getPixels(pixels, 0, width, 0, y, width, 1);
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = ViewCompat.MEASURED_SIZE_MASK | ((pixels[i] << 8) & ViewCompat.MEASURED_STATE_MASK);
            }
            this.mBitmapGradientPatternWhite.setPixels(pixels, 0, width, 0, y, width, 1);
        }
        gradientBitmap.recycle();
        this.mPaint = new Paint();
        this.mPaintArc = new Paint();
        this.mPaintArc.setStrokeWidth(8.0f);
        this.mPaintArc.setStyle(Style.STROKE);
        this.mAnimationState = AnimationState.NONE;
        this.mStartGradient1_Animator = new ValueAnimator();
        this.mStartGradient1_Animator.setInterpolator(new LinearInterpolator());
        this.mStartGradient1_Animator.setRepeatCount(0);
        this.mStartGradient1_Animator.setFloatValues(new float[]{0.0f, 1.0f});
        this.mStartGradient1_Animator.setDuration((long) this.mStartDuration);
        this.mStartGradient1_Animator.setStartDelay((long) this.mStartGradient1_Delay);
        this.mStartGradient1_Animator.addListener(new C09201());
        this.mStartGradient1_Animator.addUpdateListener(new C09212());
        this.mStartGradient3_Animator = new ValueAnimator();
        this.mStartGradient3_Animator.setInterpolator(new LinearInterpolator());
        this.mStartGradient3_Animator.setRepeatCount(0);
        this.mStartGradient3_Animator.setFloatValues(new float[]{0.0f, 1.0f});
        this.mStartGradient3_Animator.setDuration((long) this.mStartDuration);
        this.mRepeatAnimator = new ValueAnimator();
        this.mRepeatAnimator.setInterpolator(new LinearInterpolator());
        this.mRepeatAnimator.setRepeatCount(-1);
        this.mRepeatAnimator.setRepeatMode(1);
        this.mRepeatAnimator.setFloatValues(new float[]{1.0f, 0.0f, 1.0f});
        this.mRepeatAnimator.setDuration((long) this.mRepeatDuration);
        this.mRepeatAnimator.addUpdateListener(new C09223());
        setColors(this.mColor1, this.mColor2, this.mColor3);
        this.mFlagInit = true;
    }

    public void setArcShow(boolean flag) {
        this.mFlagShowArc = flag;
    }

    public void setColors(int iColor1, int iColor2, int iColor3) {
        this.mStartGradient3_Animator.setCurrentPlayTime(0);
        this.mColor1 = iColor1 | ViewCompat.MEASURED_STATE_MASK;
        this.mColor2 = iColor2 | ViewCompat.MEASURED_STATE_MASK;
        this.mColor3 = iColor3 | ViewCompat.MEASURED_STATE_MASK;
        this.mColorFilter1 = new PorterDuffColorFilter(this.mColor1, Mode.SRC_IN);
        this.mColorFilter2 = new PorterDuffColorFilter(this.mColor2, Mode.SRC_IN);
        this.mColorFilter3 = new PorterDuffColorFilter(this.mColor3, Mode.SRC_IN);
        invalidate();
    }

    public void setAlphas(float iAlpha1, float iAlpha2, float iAlpha3) {
        this.gradient1_alpha = iAlpha1;
        this.gradient2_alpha = iAlpha2;
        this.gradient3_alpha = iAlpha3;
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mViewWidth = w;
        this.mViewHeight = h;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        invalidate();
    }

    public void startAnimation() {
        if (this.mAnimationState != AnimationState.START) {
            this.mAnimationState = AnimationState.START;
            this.mStartGradient1_Animator.start();
            this.mStartGradient3_Animator.start();
            return;
        }
        stopAnimation();
    }

    public void stopAnimation() {
        if (this.mAnimationState == AnimationState.START || this.mAnimationState == AnimationState.REPEAT) {
            this.mAnimationState = AnimationState.END;
            this.mStartGradient1_Animator.cancel();
            this.mStartGradient3_Animator.cancel();
            this.mRepeatAnimator.cancel();
        }
        invalidate();
    }

    public RadialGradientView(Context context) {
        super(context);
    }

    public RadialGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadialGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
