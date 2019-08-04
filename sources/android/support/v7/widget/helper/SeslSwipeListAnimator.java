package android.support.v7.widget.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.SeslViewReflector;
import android.support.v7.util.SeslRoundedCorner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class SeslSwipeListAnimator {
    private static final String TAG = "SeslSwipeListAnimator";
    private final int DEFAULT_DRAWABLE_PADDING = 10;
    private final int DEFAULT_LEFT_COLOR = Color.parseColor("#6ebd52");
    private final int DEFAULT_RIGHT_COLOR = Color.parseColor("#56c0e5");
    private final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");
    private final int DEFAULT_TEXT_SIZE = 15;
    private final int DIRECTION_LTR = 0;
    private final int DIRECTION_RTL = 1;
    private boolean mAnimationCleared = true;
    private Paint mBgLeftToRight = null;
    private Paint mBgRightToLeft = null;
    private Context mContext;
    private BitmapDrawable mDrawSwipeBitmapDrawable = null;
    private RecyclerView mRecyclerView;
    private SeslRoundedCorner mSeslRoundedCorner;
    private Bitmap mSwipeBitmap = null;
    private SwipeConfiguration mSwipeConfiguration;
    private Rect mSwipeRect = null;
    private int mTempRoundedCorner = 0;
    private Paint mTextPaint = null;

    public static class SwipeConfiguration {
        public int UNSET_VALUE = -1;
        public int colorLeftToRight = this.UNSET_VALUE;
        public int colorRightToLeft = this.UNSET_VALUE;
        public Drawable drawableLeftToRight;
        public int drawablePadding = this.UNSET_VALUE;
        public Drawable drawableRightToLeft;
        public int textColor = this.UNSET_VALUE;
        public String textLeftToRight;
        public String textRightToLeft;
        public int textSize = this.UNSET_VALUE;
    }

    public SeslSwipeListAnimator(RecyclerView recyclerView, Context context) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mSeslRoundedCorner = new SeslRoundedCorner(context);
    }

    public void setSwipeConfiguration(SwipeConfiguration swipeConfiguration) {
        this.mSwipeConfiguration = swipeConfiguration;
        if (this.mSwipeConfiguration.textLeftToRight == null) {
            this.mSwipeConfiguration.textLeftToRight = " ";
        }
        if (this.mSwipeConfiguration.textRightToLeft == null) {
            this.mSwipeConfiguration.textRightToLeft = " ";
        }
        if (this.mSwipeConfiguration.colorLeftToRight == this.mSwipeConfiguration.UNSET_VALUE) {
            this.mSwipeConfiguration.colorLeftToRight = this.DEFAULT_LEFT_COLOR;
        }
        if (this.mSwipeConfiguration.colorRightToLeft == this.mSwipeConfiguration.UNSET_VALUE) {
            this.mSwipeConfiguration.colorRightToLeft = this.DEFAULT_RIGHT_COLOR;
        }
        if (this.mSwipeConfiguration.textColor == this.mSwipeConfiguration.UNSET_VALUE) {
            this.mSwipeConfiguration.textColor = this.DEFAULT_TEXT_COLOR;
        }
        if (this.mSwipeConfiguration.textSize == this.mSwipeConfiguration.UNSET_VALUE) {
            this.mSwipeConfiguration.textSize = 15;
        }
        if (this.mSwipeConfiguration.drawablePadding == this.mSwipeConfiguration.UNSET_VALUE) {
            this.mSwipeConfiguration.drawablePadding = 10;
        }
        this.mBgLeftToRight = initPaintWithAlphaAntiAliasing(this.mSwipeConfiguration.colorLeftToRight);
        this.mBgRightToLeft = initPaintWithAlphaAntiAliasing(this.mSwipeConfiguration.colorRightToLeft);
        this.mTextPaint = initPaintWithAlphaAntiAliasing(this.mSwipeConfiguration.textColor);
        this.mTextPaint.setTextSize((float) convertDipToPixels(this.mContext, this.mSwipeConfiguration.textSize));
    }

    private Paint initPaintWithAlphaAntiAliasing(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    private int convertDipToPixels(Context context, int dip) {
        return Math.round(((float) dip) * context.getResources().getDisplayMetrics().density);
    }

    public void doMoveAction(Canvas c, View viewForeground, float deltaX, boolean isCurrentlyActive) {
        float swipeProgress = deltaX / ((float) viewForeground.getWidth());
        float deltaXAbs = Math.abs(deltaX);
        if (!(SeslViewReflector.semGetRoundedCorners(viewForeground) == 0 || deltaX == 0.0f)) {
            this.mTempRoundedCorner = SeslViewReflector.semGetRoundedCorners(viewForeground);
            SeslViewReflector.semSetRoundedCorners(viewForeground, 0);
            viewForeground.invalidate();
        }
        if (deltaX != 0.0f || isCurrentlyActive) {
            drawRectToBitmapCanvas(viewForeground, deltaX, swipeProgress);
            if (this.mAnimationCleared) {
                this.mAnimationCleared = false;
                Log.d(TAG, "SwipeAnimation is drawing for View = " + viewForeground);
            }
        }
        viewForeground.setTranslationX(deltaX);
        viewForeground.setAlpha(1.0f - (deltaXAbs / ((float) viewForeground.getWidth())));
        this.mDrawSwipeBitmapDrawable = getBitmapDrawableToSwipeBitmap();
        if (this.mDrawSwipeBitmapDrawable != null) {
            this.mRecyclerView.invalidate(this.mDrawSwipeBitmapDrawable.getBounds());
            this.mDrawSwipeBitmapDrawable.draw(c);
        }
    }

    private int calculateTopOfList(View view) {
        int viewTop = view.getTop();
        View viewParent = (View) view.getParent();
        if (viewParent == null || (viewParent instanceof RecyclerView)) {
            return viewTop;
        }
        return viewTop + calculateTopOfList(viewParent);
    }

    private Canvas drawRectToBitmapCanvas(View view, float deltaX, float swipeProgress) {
        int viewTop = calculateTopOfList(view);
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        this.mSwipeRect = new Rect(view.getLeft(), viewTop, viewWidth, viewTop + viewHeight);
        if (this.mSwipeBitmap == null) {
            this.mSwipeBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(this.mSwipeBitmap);
        canvas.drawColor(0, Mode.CLEAR);
        float deltaXAbs = Math.abs(deltaX);
        float rectAlpha = (deltaXAbs / ((float) view.getWidth())) * 255.0f;
        Drawable d;
        Rect drawableBound;
        int width;
        int height;
        Rect iconDrawableRect;
        if (swipeProgress > 0.0f) {
            d = this.mSwipeConfiguration.drawableLeftToRight;
            if (d != null) {
                drawableBound = d.getBounds();
                width = drawableBound.width();
                height = drawableBound.height();
                iconDrawableRect = new Rect(this.mSwipeConfiguration.drawablePadding, 0, this.mSwipeConfiguration.drawablePadding + width, height);
                iconDrawableRect.offset(0, (viewHeight - height) / 2);
            } else {
                iconDrawableRect = new Rect(0, 0, 0, 0);
            }
            drawRectInto(canvas, new Rect(0, 0, (int) deltaX, viewHeight), iconDrawableRect, d, this.mBgLeftToRight, 255, this.mSwipeConfiguration.textLeftToRight, (float) this.mSwipeConfiguration.textSize, 0);
            drawRectInto(canvas, new Rect((int) deltaX, 0, viewWidth, viewHeight), iconDrawableRect, d, this.mBgLeftToRight, (int) rectAlpha, this.mSwipeConfiguration.textLeftToRight, (float) this.mSwipeConfiguration.textSize, 0);
        } else if (swipeProgress < 0.0f) {
            d = this.mSwipeConfiguration.drawableRightToLeft;
            if (d != null) {
                drawableBound = d.getBounds();
                width = drawableBound.width();
                height = drawableBound.height();
                int paddingFromRight = viewWidth - this.mSwipeConfiguration.drawablePadding;
                iconDrawableRect = new Rect(paddingFromRight - width, 0, paddingFromRight, height);
                iconDrawableRect.offset(0, (viewHeight - height) / 2);
            } else {
                iconDrawableRect = new Rect(viewWidth, 0, viewWidth, 0);
            }
            drawRectInto(canvas, new Rect(viewWidth - ((int) deltaXAbs), 0, viewWidth, viewHeight), iconDrawableRect, d, this.mBgRightToLeft, 255, this.mSwipeConfiguration.textRightToLeft, (float) this.mSwipeConfiguration.textSize, 1);
            drawRectInto(canvas, new Rect(0, 0, viewWidth - ((int) deltaXAbs), viewHeight), iconDrawableRect, d, this.mBgRightToLeft, (int) rectAlpha, this.mSwipeConfiguration.textRightToLeft, (float) this.mSwipeConfiguration.textSize, 1);
        }
        if (this.mTempRoundedCorner != 0) {
            this.mSeslRoundedCorner.setRoundedCorners(this.mTempRoundedCorner);
            this.mSeslRoundedCorner.drawRoundedCorner(canvas);
        }
        return canvas;
    }

    private void drawRectInto(Canvas canvas, Rect clipRect, Rect iconClipRect, Drawable d, Paint rectPaint, int rectAlpha, String text, float textSize, int direction) {
        canvas.save();
        rectPaint.setAlpha(rectAlpha);
        this.mTextPaint.setAlpha(rectAlpha);
        canvas.clipRect(clipRect);
        canvas.drawRect(clipRect, rectPaint);
        if (d != null) {
            d.setBounds(iconClipRect);
            d.draw(canvas);
        }
        drawSwipeText(canvas, this.mTextPaint, text, direction, iconClipRect);
        canvas.restore();
    }

    private void drawSwipeText(Canvas canvas, Paint paint, String text, int direction, Rect iconClipRect) {
        float x;
        int cHeight = canvas.getHeight();
        int cWidth = canvas.getWidth();
        Rect r = new Rect();
        paint.setTextAlign(Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        FontMetrics fm = paint.getFontMetrics();
        float y = ((((float) cHeight) / 2.0f) + (Math.abs(fm.top - fm.bottom) / 2.0f)) - fm.bottom;
        if (direction == 0) {
            x = (float) (iconClipRect.right + this.mSwipeConfiguration.drawablePadding);
        } else {
            x = (float) ((iconClipRect.left - this.mSwipeConfiguration.drawablePadding) - r.right);
        }
        canvas.drawText(text, x, y, paint);
    }

    private BitmapDrawable getBitmapDrawableToSwipeBitmap() {
        if (this.mSwipeBitmap == null) {
            return null;
        }
        BitmapDrawable bd = new BitmapDrawable(this.mRecyclerView.getResources(), this.mSwipeBitmap);
        bd.setBounds(this.mSwipeRect);
        return bd;
    }

    private void drawTextToCenter(Canvas canvas, Paint paint, String text) {
        int cHeight = canvas.getHeight();
        int cWidth = canvas.getWidth();
        Rect r = new Rect();
        paint.setTextAlign(Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        canvas.drawText(text, ((((float) cWidth) / 2.0f) - (((float) r.width()) / 2.0f)) - ((float) r.left), ((((float) cHeight) / 2.0f) + (((float) r.height()) / 2.0f)) - ((float) r.bottom), paint);
    }

    public void clearSwipeAnimation(View view) {
        Log.d(TAG, "clearSwipeAnimation, view = " + view);
        this.mAnimationCleared = true;
        if (this.mTempRoundedCorner != 0) {
            SeslViewReflector.semSetRoundedCorners(view, this.mTempRoundedCorner);
            view.invalidate();
            this.mTempRoundedCorner = 0;
        }
        if (this.mDrawSwipeBitmapDrawable != null) {
            this.mDrawSwipeBitmapDrawable.getBitmap().recycle();
            this.mDrawSwipeBitmapDrawable = null;
            this.mSwipeBitmap = null;
        }
        view.setTranslationX(0.0f);
        view.setAlpha(1.0f);
    }

    public void onSwiped(View view) {
        clearSwipeAnimation(view);
    }
}
