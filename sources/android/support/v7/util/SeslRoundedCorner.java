package android.support.v7.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.C0247R;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class SeslRoundedCorner {
    Drawable mBottomLeftRound;
    int mBottomLeftRoundColor;
    Drawable mBottomRightRound;
    int mBottomRightRoundColor;
    Context mContext;
    boolean mIsMutate;
    boolean mIsStrokeRoundedCorner;
    Resources mRes;
    int mRoundRadius;
    Drawable mRoundStrokeBottom;
    int mRoundStrokeHeight;
    Drawable mRoundStrokeTop;
    Rect mRoundedCornerBounds;
    int mRoundedCornerMode;
    Drawable mTopLeftRound;
    int mTopLeftRoundColor;
    Drawable mTopRightRound;
    int mTopRightRoundColor;
    int mX;
    int mY;

    public SeslRoundedCorner(Context context) {
        this(context, true);
    }

    public SeslRoundedCorner(Context context, boolean isStrokeRoundedCorner) {
        this.mIsStrokeRoundedCorner = true;
        this.mIsMutate = false;
        this.mRoundRadius = -1;
        this.mRoundedCornerBounds = new Rect();
        this.mContext = context;
        this.mRes = context.getResources();
        this.mIsStrokeRoundedCorner = isStrokeRoundedCorner;
        initRoundedCorner();
    }

    public SeslRoundedCorner(Context context, boolean isStrokeRoundedCorner, boolean isMutate) {
        this.mIsStrokeRoundedCorner = true;
        this.mIsMutate = false;
        this.mRoundRadius = -1;
        this.mRoundedCornerBounds = new Rect();
        this.mContext = context;
        this.mRes = context.getResources();
        this.mIsStrokeRoundedCorner = isStrokeRoundedCorner;
        this.mIsMutate = isMutate;
        initRoundedCorner();
    }

    public void setRoundedCorners(int corners) {
        if ((corners & -16) != 0) {
            throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + corners);
        }
        this.mRoundedCornerMode = corners;
        if (this.mTopLeftRound == null || this.mTopRightRound == null || this.mBottomLeftRound == null || this.mBottomRightRound == null) {
            initRoundedCorner();
        }
    }

    public void setRoundedCornerColor(int corners, int color) {
        if (this.mIsStrokeRoundedCorner) {
            Log.d("SeslRoundedCorner", "can not change round color on stroke rounded corners");
        } else if (corners == 0) {
            throw new IllegalArgumentException("There is no rounded corner on = " + this);
        } else if ((corners & -16) != 0) {
            throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + corners);
        } else {
            if (!(color == this.mTopLeftRoundColor && color == this.mBottomLeftRoundColor)) {
                Log.d("SeslRoundedCorner", "change color = " + color + ", on =" + corners + ", top color = " + this.mTopLeftRoundColor + ", bottom color = " + this.mBottomLeftRoundColor);
            }
            if (this.mTopLeftRound == null || this.mTopRightRound == null || this.mBottomLeftRound == null || this.mBottomRightRound == null) {
                initRoundedCorner();
            }
            PorterDuffColorFilter pdcf = new PorterDuffColorFilter(color, Mode.SRC_IN);
            if ((corners & 1) != 0) {
                this.mTopLeftRoundColor = color;
                this.mTopLeftRound.setColorFilter(pdcf);
            }
            if ((corners & 2) != 0) {
                this.mTopRightRoundColor = color;
                this.mTopRightRound.setColorFilter(pdcf);
            }
            if ((corners & 4) != 0) {
                this.mBottomLeftRoundColor = color;
                this.mBottomLeftRound.setColorFilter(pdcf);
            }
            if ((corners & 8) != 0) {
                this.mBottomRightRoundColor = color;
                this.mBottomRightRound.setColorFilter(pdcf);
            }
        }
    }

    private void initRoundedCorner() {
        boolean darkTheme = true;
        this.mRoundRadius = (int) TypedValue.applyDimension(1, (float) 26, this.mRes.getDisplayMetrics());
        TypedValue outValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            darkTheme = false;
        }
        if (darkTheme) {
            this.mIsStrokeRoundedCorner = false;
        }
        Log.d("SeslRoundedCorner", "initRoundedCorner, rounded corner with stroke = " + this.mIsStrokeRoundedCorner + ", dark theme = " + darkTheme + ", mutate " + this.mIsMutate);
        int color;
        if (this.mIsStrokeRoundedCorner) {
            color = this.mRes.getColor(C0247R.color.sesl_round_and_bgcolor_light);
            this.mBottomRightRoundColor = color;
            this.mBottomLeftRoundColor = color;
            this.mTopRightRoundColor = color;
            this.mTopLeftRoundColor = color;
            this.mTopLeftRound = this.mRes.getDrawable(C0247R.drawable.sesl_top_left_round_stroke, this.mContext.getTheme());
            this.mTopRightRound = this.mRes.getDrawable(C0247R.drawable.sesl_top_right_round_stroke, this.mContext.getTheme());
            this.mBottomLeftRound = this.mRes.getDrawable(C0247R.drawable.sesl_bottom_left_round_stroke, this.mContext.getTheme());
            this.mBottomRightRound = this.mRes.getDrawable(C0247R.drawable.sesl_bottom_right_round_stroke, this.mContext.getTheme());
        } else if (this.mIsMutate) {
            color = this.mRes.getColor(C0247R.color.sesl_round_and_bgcolor_dark);
            this.mBottomRightRoundColor = color;
            this.mBottomLeftRoundColor = color;
            this.mTopRightRoundColor = color;
            this.mTopLeftRoundColor = color;
            this.mTopLeftRound = this.mRes.getDrawable(C0247R.drawable.sesl_top_left_round).mutate();
            this.mTopRightRound = this.mRes.getDrawable(C0247R.drawable.sesl_top_right_round).mutate();
            this.mBottomLeftRound = this.mRes.getDrawable(C0247R.drawable.sesl_bottom_left_round).mutate();
            this.mBottomRightRound = this.mRes.getDrawable(C0247R.drawable.sesl_bottom_right_round).mutate();
        } else {
            color = this.mRes.getColor(C0247R.color.sesl_round_and_bgcolor_dark);
            this.mBottomRightRoundColor = color;
            this.mBottomLeftRoundColor = color;
            this.mTopRightRoundColor = color;
            this.mTopLeftRoundColor = color;
            this.mTopLeftRound = this.mRes.getDrawable(C0247R.drawable.sesl_top_left_round);
            this.mTopRightRound = this.mRes.getDrawable(C0247R.drawable.sesl_top_right_round);
            this.mBottomLeftRound = this.mRes.getDrawable(C0247R.drawable.sesl_bottom_left_round);
            this.mBottomRightRound = this.mRes.getDrawable(C0247R.drawable.sesl_bottom_right_round);
        }
        Drawable drawable = this.mRes.getDrawable(C0247R.drawable.sesl_round_stroke, this.mContext.getTheme());
        this.mRoundStrokeBottom = drawable;
        this.mRoundStrokeTop = drawable;
        this.mRoundStrokeHeight = this.mRes.getDimensionPixelSize(C0247R.dimen.sesl_round_stroke_height);
    }

    public int getRoundedCornerRadius() {
        return this.mRoundRadius;
    }

    public void drawRoundedCorner(Canvas canvas) {
        canvas.getClipBounds(this.mRoundedCornerBounds);
        if ((this.mRoundedCornerMode & 1) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeTop.setBounds(0, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundStrokeHeight);
                this.mRoundStrokeTop.draw(canvas);
            }
            this.mTopLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            this.mTopRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeBottom.setBounds(0, this.mRoundedCornerBounds.bottom - this.mRoundStrokeHeight, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
                this.mRoundStrokeBottom.draw(canvas);
            }
            this.mBottomLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.bottom);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            this.mBottomRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
            this.mBottomRightRound.draw(canvas);
        }
    }

    public void drawRoundedCorner(View view, Canvas canvas) {
        if (view.getTranslationY() != 0.0f) {
            this.mX = Math.round(view.getX());
            this.mY = Math.round(view.getY());
        } else {
            this.mX = view.getLeft();
            this.mY = view.getTop();
        }
        this.mRoundedCornerBounds.set(this.mX, this.mY, this.mX + view.getWidth(), this.mY + view.getHeight());
        if ((this.mRoundedCornerMode & 1) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeTop.setBounds(0, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundStrokeHeight);
                this.mRoundStrokeTop.draw(canvas);
            }
            this.mTopLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            this.mTopRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.top, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top + this.mRoundRadius);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeBottom.setBounds(0, this.mRoundedCornerBounds.bottom - this.mRoundStrokeHeight, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
                this.mRoundStrokeBottom.draw(canvas);
            }
            this.mBottomLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.bottom);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            this.mBottomRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.bottom - this.mRoundRadius, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom);
            this.mBottomRightRound.draw(canvas);
        }
    }
}
