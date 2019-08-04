package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.widget.SeslTextViewReflector;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View.BaseSavedState;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class SeslCheckedTextView extends TextView implements Checkable {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private int mBasePadding;
    private Drawable mCheckMarkDrawable;
    private int mCheckMarkGravity;
    private int mCheckMarkResource;
    private ColorStateList mCheckMarkTintList;
    private Mode mCheckMarkTintMode;
    private int mCheckMarkWidth;
    private boolean mChecked;
    private int mDrawablePadding;
    private boolean mHasCheckMarkTint;
    private boolean mHasCheckMarkTintMode;
    private boolean mNeedRequestlayout;

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C03491();
        boolean checked;

        /* renamed from: android.support.v7.widget.SeslCheckedTextView$SavedState$1 */
        static class C03491 implements Creator<SavedState> {
            C03491() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.checked = ((Boolean) in.readValue(null)).booleanValue();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(Boolean.valueOf(this.checked));
        }

        public String toString() {
            return "SeslCheckedTextView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + this.checked + "}";
        }
    }

    public SeslCheckedTextView(Context context) {
        this(context, null);
    }

    public SeslCheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843720);
    }

    public SeslCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCheckMarkTintList = null;
        this.mCheckMarkTintMode = null;
        this.mHasCheckMarkTint = false;
        this.mHasCheckMarkTintMode = false;
        this.mCheckMarkGravity = GravityCompat.START;
        TypedArray a = context.obtainStyledAttributes(attrs, C0247R.styleable.CheckedTextView, defStyleAttr, defStyleRes);
        Drawable d = a.getDrawable(C0247R.styleable.CheckedTextView_android_checkMark);
        if (d != null) {
            setCheckMarkDrawable(d);
        }
        if (a.hasValue(C0247R.styleable.CheckedTextView_android_checkMarkTintMode)) {
            this.mCheckMarkTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.CheckedTextView_android_checkMarkTintMode, -1), this.mCheckMarkTintMode);
            this.mHasCheckMarkTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.CheckedTextView_android_checkMarkTint)) {
            this.mCheckMarkTintList = a.getColorStateList(C0247R.styleable.CheckedTextView_android_checkMarkTint);
            this.mHasCheckMarkTint = true;
        }
        this.mCheckMarkGravity = a.getInt(C0247R.styleable.CheckedTextView_checkMarkGravity, GravityCompat.START);
        setChecked(a.getBoolean(C0247R.styleable.CheckedTextView_android_checked, false));
        this.mDrawablePadding = context.getResources().getDimensionPixelSize(C0247R.dimen.sesl_checked_text_padding);
        a.recycle();
        applyCheckMarkTint();
    }

    public void toggle() {
        setChecked(!this.mChecked);
    }

    @ExportedProperty
    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            refreshDrawableState();
            SeslViewReflector.notifyViewAccessibilityStateChangedIfNeeded(this, 0);
        }
    }

    public void setCheckMarkDrawable(int resId) {
        if (resId == 0 || resId != this.mCheckMarkResource) {
            setCheckMarkDrawableInternal(resId != 0 ? getContext().getDrawable(resId) : null, resId);
        }
    }

    public void setCheckMarkDrawable(Drawable d) {
        setCheckMarkDrawableInternal(d, 0);
    }

    private void setCheckMarkDrawableInternal(Drawable d, int resId) {
        boolean z = true;
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setCallback(null);
            unscheduleDrawable(this.mCheckMarkDrawable);
        }
        this.mNeedRequestlayout = d != this.mCheckMarkDrawable;
        if (d != null) {
            d.setCallback(this);
            if (getVisibility() != 0) {
                z = false;
            }
            d.setVisible(z, false);
            d.setState(CHECKED_STATE_SET);
            setMinHeight(d.getIntrinsicHeight());
            this.mCheckMarkWidth = d.getIntrinsicWidth();
            d.setState(getDrawableState());
        } else {
            this.mCheckMarkWidth = 0;
        }
        this.mCheckMarkDrawable = d;
        this.mCheckMarkResource = resId;
        applyCheckMarkTint();
        SeslViewReflector.resolvePadding(this);
        setBasePadding(isCheckMarkAtStart());
    }

    public void setCheckMarkTintList(ColorStateList tint) {
        this.mCheckMarkTintList = tint;
        this.mHasCheckMarkTint = true;
        applyCheckMarkTint();
    }

    public ColorStateList getCheckMarkTintList() {
        return this.mCheckMarkTintList;
    }

    public void setCheckMarkTintMode(Mode tintMode) {
        this.mCheckMarkTintMode = tintMode;
        this.mHasCheckMarkTintMode = true;
        applyCheckMarkTint();
    }

    public Mode getCheckMarkTintMode() {
        return this.mCheckMarkTintMode;
    }

    private void applyCheckMarkTint() {
        if (this.mCheckMarkDrawable == null) {
            return;
        }
        if (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode) {
            this.mCheckMarkDrawable = this.mCheckMarkDrawable.mutate();
            if (this.mHasCheckMarkTint) {
                this.mCheckMarkDrawable.setTintList(this.mCheckMarkTintList);
            }
            if (this.mHasCheckMarkTintMode) {
                this.mCheckMarkDrawable.setTintMode(this.mCheckMarkTintMode);
            }
            if (this.mCheckMarkDrawable.isStateful()) {
                this.mCheckMarkDrawable.setState(getDrawableState());
            }
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.mCheckMarkDrawable != null) {
            boolean z;
            Drawable drawable = this.mCheckMarkDrawable;
            if (visibility == 0) {
                z = true;
            } else {
                z = false;
            }
            drawable.setVisible(z, false);
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.jumpToCurrentState();
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mCheckMarkDrawable || super.verifyDrawable(who);
    }

    public Drawable getCheckMarkDrawable() {
        return this.mCheckMarkDrawable;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        updatePadding();
    }

    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getBounds();
            if (ViewUtils.isLayoutRtl(this) && SeslTextViewReflector.getField_mSingleLine(this)) {
                invalidate(dirty.left, dirty.top, dirty.right, dirty.bottom);
            }
        }
    }

    private void updatePadding() {
        int i = 1;
        SeslViewReflector.resetPaddingToInitialValues(this);
        int newPadding = this.mCheckMarkDrawable != null ? (this.mCheckMarkWidth + this.mBasePadding) + this.mDrawablePadding : this.mBasePadding;
        boolean z;
        if (isCheckMarkAtStart()) {
            z = this.mNeedRequestlayout;
            if (SeslViewReflector.getField_mPaddingLeft(this) == newPadding) {
                i = 0;
            }
            this.mNeedRequestlayout = i | z;
            SeslViewReflector.setField_mPaddingLeft(this, newPadding);
        } else {
            z = this.mNeedRequestlayout;
            if (SeslViewReflector.getField_mPaddingRight(this) == newPadding) {
                i = 0;
            }
            this.mNeedRequestlayout = i | z;
            SeslViewReflector.setField_mPaddingRight(this, newPadding);
        }
        if (this.mNeedRequestlayout) {
            requestLayout();
            this.mNeedRequestlayout = false;
        }
    }

    private void setBasePadding(boolean checkmarkAtStart) {
        if (checkmarkAtStart) {
            this.mBasePadding = getPaddingLeft();
        } else {
            this.mBasePadding = getPaddingRight();
        }
    }

    private boolean isCheckMarkAtStart() {
        return (Gravity.getAbsoluteGravity(this.mCheckMarkGravity, getLayoutDirection()) & 7) == 3;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable checkMarkDrawable = this.mCheckMarkDrawable;
        if (checkMarkDrawable != null) {
            int left;
            int right;
            int verticalGravity = getGravity() & 112;
            int height = checkMarkDrawable.getIntrinsicHeight();
            int y = 0;
            switch (verticalGravity) {
                case 16:
                    y = (getHeight() - height) / 2;
                    break;
                case 80:
                    y = getHeight() - height;
                    break;
            }
            boolean checkMarkAtStart = isCheckMarkAtStart();
            int width = getWidth();
            int top = y;
            int bottom = top + height;
            if (checkMarkAtStart) {
                left = this.mBasePadding;
                right = left + this.mCheckMarkWidth;
            } else {
                right = width - this.mBasePadding;
                left = right - this.mCheckMarkWidth;
            }
            if (ViewUtils.isLayoutRtl(this)) {
                checkMarkDrawable.setBounds(getScrollX() + left, top, getScrollX() + right, bottom);
            } else {
                checkMarkDrawable.setBounds(left, top, right, bottom);
            }
            checkMarkDrawable.draw(canvas);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(getScrollX() + left, top, getScrollX() + right, bottom);
            }
        }
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable checkMarkDrawable = this.mCheckMarkDrawable;
        if (checkMarkDrawable != null && checkMarkDrawable.isStateful() && checkMarkDrawable.setState(getDrawableState())) {
            invalidateDrawable(checkMarkDrawable);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setHotspot(x, y);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return CheckedTextView.class.getName();
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.checked = isChecked();
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setChecked(this.mChecked);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setCheckable(true);
        info.setChecked(this.mChecked);
    }
}
