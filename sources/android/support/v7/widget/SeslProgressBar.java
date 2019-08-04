package android.support.v7.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.SeslStateListDrawableReflector;
import android.support.v4.math.MathUtils;
import android.support.v4.util.Pools.SynchronizedPool;
import android.support.v4.view.SeslViewReflector;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.RemoteViews.RemoteView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

@RemoteView
public class SeslProgressBar extends View {
    private static final boolean IS_BASE_SDK_VERSION = (VERSION.SDK_INT <= 23);
    private static final int MAX_LEVEL = 10000;
    public static final int MODE_DUAL_COLOR = 2;
    public static final int MODE_SPLIT = 4;
    public static final int MODE_STANDARD = 0;
    public static final int MODE_VERTICAL = 3;
    public static final int MODE_WARNING = 1;
    private static final int PROGRESS_ANIM_DURATION = 80;
    private static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR = new DecelerateInterpolator();
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 200;
    private final FloatProperty<SeslProgressBar> VISUAL_PROGRESS;
    private AccessibilityEventSender mAccessibilityEventSender;
    private boolean mAggregatedIsVisible;
    private AlphaAnimation mAnimation;
    private boolean mAttached;
    private int mBehavior;
    private Drawable mCurrentDrawable;
    protected int mCurrentMode;
    protected float mDensity;
    private int mDuration;
    private boolean mHasAnimation;
    private boolean mInDrawing;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private Interpolator mInterpolator;
    private int mMax;
    int mMaxHeight;
    private boolean mMaxInitialized;
    int mMaxWidth;
    private int mMin;
    int mMinHeight;
    private boolean mMinInitialized;
    int mMinWidth;
    boolean mMirrorForRtl;
    private boolean mNoInvalidate;
    private boolean mOnlyIndeterminate;
    private int mProgress;
    private Drawable mProgressDrawable;
    private ProgressTintInfo mProgressTintInfo;
    private final ArrayList<RefreshData> mRefreshData;
    private boolean mRefreshIsPosted;
    private RefreshProgressRunnable mRefreshProgressRunnable;
    int mSampleWidth;
    private int mSecondaryProgress;
    private boolean mShouldStartAnimationDrawable;
    private Transformation mTransformation;
    private long mUiThreadId;
    private float mVisualProgress;

    private class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        public void run() {
            SeslProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    private static class ProgressTintInfo {
        boolean mHasIndeterminateTint;
        boolean mHasIndeterminateTintMode;
        boolean mHasProgressBackgroundTint;
        boolean mHasProgressBackgroundTintMode;
        boolean mHasProgressTint;
        boolean mHasProgressTintMode;
        boolean mHasSecondaryProgressTint;
        boolean mHasSecondaryProgressTintMode;
        ColorStateList mIndeterminateTintList;
        Mode mIndeterminateTintMode;
        ColorStateList mProgressBackgroundTintList;
        Mode mProgressBackgroundTintMode;
        ColorStateList mProgressTintList;
        Mode mProgressTintMode;
        ColorStateList mSecondaryProgressTintList;
        Mode mSecondaryProgressTintMode;

        private ProgressTintInfo() {
        }
    }

    private static class RefreshData {
        private static final int POOL_MAX = 24;
        private static final SynchronizedPool<RefreshData> sPool = new SynchronizedPool(24);
        public boolean animate;
        public boolean fromUser;
        public int id;
        public int progress;

        private RefreshData() {
        }

        public static RefreshData obtain(int id, int progress, boolean fromUser, boolean animate) {
            RefreshData rd = (RefreshData) sPool.acquire();
            if (rd == null) {
                rd = new RefreshData();
            }
            rd.id = id;
            rd.progress = progress;
            rd.fromUser = fromUser;
            rd.animate = animate;
            return rd;
        }

        public void recycle() {
            sPool.release(this);
        }
    }

    private class RefreshProgressRunnable implements Runnable {
        private RefreshProgressRunnable() {
        }

        public void run() {
            synchronized (SeslProgressBar.this) {
                int count = SeslProgressBar.this.mRefreshData.size();
                for (int i = 0; i < count; i++) {
                    RefreshData rd = (RefreshData) SeslProgressBar.this.mRefreshData.get(i);
                    SeslProgressBar.this.doRefreshProgress(rd.id, rd.progress, rd.fromUser, true, rd.animate);
                    rd.recycle();
                }
                SeslProgressBar.this.mRefreshData.clear();
                SeslProgressBar.this.mRefreshIsPosted = false;
            }
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C03811();
        int progress;
        int secondaryProgress;

        /* renamed from: android.support.v7.widget.SeslProgressBar$SavedState$1 */
        static class C03811 implements Creator<SavedState> {
            C03811() {
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
            this.progress = in.readInt();
            this.secondaryProgress = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.progress);
            out.writeInt(this.secondaryProgress);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekBarMode {
    }

    public SeslProgressBar(Context context) {
        this(context, null);
    }

    public SeslProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 16842871);
    }

    public SeslProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        boolean z = false;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSampleWidth = 0;
        this.mMirrorForRtl = false;
        this.mRefreshData = new ArrayList();
        this.mCurrentMode = 0;
        this.VISUAL_PROGRESS = new FloatProperty<SeslProgressBar>("visual_progress") {
            public void setValue(SeslProgressBar object, float value) {
                object.setVisualProgress(C0247R.id.progress, value);
                object.mVisualProgress = value;
            }

            public Float get(SeslProgressBar object) {
                return Float.valueOf(object.mVisualProgress);
            }
        };
        this.mUiThreadId = Thread.currentThread().getId();
        initProgressBar();
        TypedArray a = context.obtainStyledAttributes(attrs, C0247R.styleable.ProgressBar, defStyleAttr, defStyleRes);
        this.mNoInvalidate = true;
        Drawable progressDrawable = a.getDrawable(C0247R.styleable.ProgressBar_android_progressDrawable);
        if (progressDrawable != null) {
            if (needsTileify(progressDrawable)) {
                setProgressDrawableTiled(progressDrawable);
            } else {
                setProgressDrawable(progressDrawable);
            }
        }
        this.mDuration = a.getInt(C0247R.styleable.ProgressBar_android_indeterminateDuration, this.mDuration);
        this.mMinWidth = a.getDimensionPixelSize(C0247R.styleable.ProgressBar_android_minWidth, this.mMinWidth);
        this.mMaxWidth = a.getDimensionPixelSize(C0247R.styleable.ProgressBar_android_maxWidth, this.mMaxWidth);
        this.mMinHeight = a.getDimensionPixelSize(C0247R.styleable.ProgressBar_android_minHeight, this.mMinHeight);
        this.mMaxHeight = a.getDimensionPixelSize(C0247R.styleable.ProgressBar_android_maxHeight, this.mMaxHeight);
        this.mBehavior = a.getInt(C0247R.styleable.ProgressBar_android_indeterminateBehavior, this.mBehavior);
        int resID = a.getResourceId(C0247R.styleable.ProgressBar_android_interpolator, 17432587);
        if (resID > 0) {
            setInterpolator(context, resID);
        }
        setMin(a.getInt(C0247R.styleable.ProgressBar_min, this.mMin));
        setMax(a.getInt(C0247R.styleable.ProgressBar_android_max, this.mMax));
        setProgress(a.getInt(C0247R.styleable.ProgressBar_android_progress, this.mProgress));
        setSecondaryProgress(a.getInt(C0247R.styleable.ProgressBar_android_secondaryProgress, this.mSecondaryProgress));
        Drawable indeterminateDrawable = a.getDrawable(C0247R.styleable.ProgressBar_android_indeterminateDrawable);
        if (indeterminateDrawable != null) {
            if (needsTileify(indeterminateDrawable)) {
                setIndeterminateDrawableTiled(indeterminateDrawable);
            } else {
                setIndeterminateDrawable(indeterminateDrawable);
            }
        }
        this.mOnlyIndeterminate = a.getBoolean(C0247R.styleable.ProgressBar_android_indeterminateOnly, this.mOnlyIndeterminate);
        this.mNoInvalidate = false;
        if (this.mOnlyIndeterminate || a.getBoolean(C0247R.styleable.ProgressBar_android_indeterminate, this.mIndeterminate)) {
            z = true;
        }
        setIndeterminate(z);
        this.mMirrorForRtl = a.getBoolean(C0247R.styleable.ProgressBar_android_mirrorForRtl, this.mMirrorForRtl);
        if (a.hasValue(C0247R.styleable.ProgressBar_android_progressTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.ProgressBar_android_progressTintMode, -1), null);
            this.mProgressTintInfo.mHasProgressTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_progressTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressTintList = a.getColorStateList(C0247R.styleable.ProgressBar_android_progressTint);
            this.mProgressTintInfo.mHasProgressTint = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_progressBackgroundTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressBackgroundTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.ProgressBar_android_progressBackgroundTintMode, -1), null);
            this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_progressBackgroundTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mProgressBackgroundTintList = a.getColorStateList(C0247R.styleable.ProgressBar_android_progressBackgroundTint);
            this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_secondaryProgressTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mSecondaryProgressTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.ProgressBar_android_secondaryProgressTintMode, -1), null);
            this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_secondaryProgressTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mSecondaryProgressTintList = a.getColorStateList(C0247R.styleable.ProgressBar_android_secondaryProgressTint);
            this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_indeterminateTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mIndeterminateTintMode = DrawableUtils.parseTintMode(a.getInt(C0247R.styleable.ProgressBar_android_indeterminateTintMode, -1), null);
            this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        }
        if (a.hasValue(C0247R.styleable.ProgressBar_android_indeterminateTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new ProgressTintInfo();
            }
            this.mProgressTintInfo.mIndeterminateTintList = a.getColorStateList(C0247R.styleable.ProgressBar_android_indeterminateTint);
            this.mProgressTintInfo.mHasIndeterminateTint = true;
        }
        a.recycle();
        applyProgressTints();
        applyIndeterminateTint();
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
        this.mDensity = context.getResources().getDisplayMetrics().density;
    }

    private static boolean needsTileify(Drawable dr) {
        int N;
        int i;
        if (dr instanceof LayerDrawable) {
            LayerDrawable orig = (LayerDrawable) dr;
            N = orig.getNumberOfLayers();
            for (i = 0; i < N; i++) {
                if (needsTileify(orig.getDrawable(i))) {
                    return true;
                }
            }
            return false;
        } else if (dr instanceof StateListDrawable) {
            StateListDrawable in = (StateListDrawable) dr;
            N = StateListDrawable_getStateCount(in);
            for (i = 0; i < N; i++) {
                Drawable d = StateListDrawable_getStateDrawable(in, i);
                if (d != null && needsTileify(d)) {
                    return true;
                }
            }
            return false;
        } else if (dr instanceof BitmapDrawable) {
            return true;
        } else {
            return false;
        }
    }

    private Drawable tileify(Drawable drawable, boolean clip) {
        int N;
        int i;
        if (drawable instanceof LayerDrawable) {
            LayerDrawable orig = (LayerDrawable) drawable;
            N = orig.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];
            for (i = 0; i < N; i++) {
                int id = orig.getId(i);
                Drawable drawable2 = orig.getDrawable(i);
                boolean z = id == C0247R.id.progress || id == C0247R.id.secondaryProgress;
                outDrawables[i] = tileify(drawable2, z);
            }
            Drawable layerDrawable = new LayerDrawable(outDrawables);
            for (i = 0; i < N; i++) {
                layerDrawable.setId(i, orig.getId(i));
                if (VERSION.SDK_INT >= 23) {
                    layerDrawable.setLayerGravity(i, orig.getLayerGravity(i));
                    layerDrawable.setLayerWidth(i, orig.getLayerWidth(i));
                    layerDrawable.setLayerHeight(i, orig.getLayerHeight(i));
                    layerDrawable.setLayerInsetLeft(i, orig.getLayerInsetLeft(i));
                    layerDrawable.setLayerInsetRight(i, orig.getLayerInsetRight(i));
                    layerDrawable.setLayerInsetTop(i, orig.getLayerInsetTop(i));
                    layerDrawable.setLayerInsetBottom(i, orig.getLayerInsetBottom(i));
                    layerDrawable.setLayerInsetStart(i, orig.getLayerInsetStart(i));
                    layerDrawable.setLayerInsetEnd(i, orig.getLayerInsetEnd(i));
                }
            }
            return layerDrawable;
        } else if (drawable instanceof StateListDrawable) {
            StateListDrawable in = (StateListDrawable) drawable;
            Drawable out = new StateListDrawable();
            N = StateListDrawable_getStateCount(in);
            for (i = 0; i < N; i++) {
                int[] stateset = StateListDrawable_getStateSet(in, i);
                Drawable d = StateListDrawable_getStateDrawable(in, i);
                if (d != null) {
                    out.addState(stateset, tileify(d, clip));
                }
            }
            return out;
        } else if (!(drawable instanceof BitmapDrawable)) {
            return drawable;
        } else {
            BitmapDrawable clone = (BitmapDrawable) drawable.getConstantState().newDrawable(getResources());
            clone.setTileModeXY(TileMode.REPEAT, TileMode.CLAMP);
            if (this.mSampleWidth <= 0) {
                this.mSampleWidth = clone.getIntrinsicWidth();
            }
            if (clip) {
                return new ClipDrawable(clone, 3, 1);
            }
            return clone;
        }
    }

    private Drawable tileifyIndeterminate(Drawable drawable) {
        if (!(drawable instanceof AnimationDrawable)) {
            return drawable;
        }
        AnimationDrawable background = (AnimationDrawable) drawable;
        int N = background.getNumberOfFrames();
        Drawable newBg = new AnimationDrawable();
        newBg.setOneShot(background.isOneShot());
        for (int i = 0; i < N; i++) {
            Drawable frame = tileify(background.getFrame(i), true);
            frame.setLevel(MAX_LEVEL);
            newBg.addFrame(frame, background.getDuration(i));
        }
        newBg.setLevel(MAX_LEVEL);
        return newBg;
    }

    private void initProgressBar() {
        this.mMin = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mSecondaryProgress = 0;
        this.mIndeterminate = false;
        this.mOnlyIndeterminate = false;
        this.mDuration = 4000;
        this.mBehavior = 1;
        this.mMinWidth = 24;
        this.mMaxWidth = 48;
        this.mMinHeight = 24;
        this.mMaxHeight = 48;
    }

    @ExportedProperty(category = "progress")
    public synchronized boolean isIndeterminate() {
        return this.mIndeterminate;
    }

    public synchronized void setIndeterminate(boolean indeterminate) {
        if (!((this.mOnlyIndeterminate && this.mIndeterminate) || indeterminate == this.mIndeterminate)) {
            this.mIndeterminate = indeterminate;
            if (indeterminate) {
                swapCurrentDrawable(this.mIndeterminateDrawable);
                startAnimation();
            } else {
                swapCurrentDrawable(this.mProgressDrawable);
                stopAnimation();
            }
        }
    }

    private void swapCurrentDrawable(Drawable newDrawable) {
        Drawable oldDrawable = this.mCurrentDrawable;
        this.mCurrentDrawable = newDrawable;
        if (oldDrawable != this.mCurrentDrawable) {
            if (oldDrawable != null) {
                oldDrawable.setVisible(false, false);
            }
            if (this.mCurrentDrawable != null) {
                boolean z;
                Drawable drawable = this.mCurrentDrawable;
                if (getWindowVisibility() == 0 && isShown()) {
                    z = true;
                } else {
                    z = false;
                }
                drawable.setVisible(z, false);
            }
        }
    }

    public Drawable getIndeterminateDrawable() {
        return this.mIndeterminateDrawable;
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (this.mIndeterminateDrawable != d) {
            if (this.mIndeterminateDrawable != null) {
                this.mIndeterminateDrawable.setCallback(null);
                unscheduleDrawable(this.mIndeterminateDrawable);
            }
            this.mIndeterminateDrawable = d;
            if (d != null) {
                d.setCallback(this);
                DrawableCompat.setLayoutDirection(d, getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                applyIndeterminateTint();
            }
            if (this.mIndeterminate) {
                swapCurrentDrawable(d);
                postInvalidate();
            }
        }
    }

    public void setIndeterminateTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mIndeterminateTintList = tint;
        this.mProgressTintInfo.mHasIndeterminateTint = true;
        applyIndeterminateTint();
    }

    public ColorStateList getIndeterminateTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mIndeterminateTintList : null;
    }

    public void setIndeterminateTintMode(Mode tintMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mIndeterminateTintMode = tintMode;
        this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        applyIndeterminateTint();
    }

    public Mode getIndeterminateTintMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mIndeterminateTintMode : null;
    }

    private void applyIndeterminateTint() {
        if (this.mIndeterminateDrawable != null && this.mProgressTintInfo != null) {
            ProgressTintInfo tintInfo = this.mProgressTintInfo;
            if (tintInfo.mHasIndeterminateTint || tintInfo.mHasIndeterminateTintMode) {
                this.mIndeterminateDrawable = this.mIndeterminateDrawable.mutate();
                if (tintInfo.mHasIndeterminateTint) {
                    this.mIndeterminateDrawable.setTintList(tintInfo.mIndeterminateTintList);
                }
                if (tintInfo.mHasIndeterminateTintMode) {
                    this.mIndeterminateDrawable.setTintMode(tintInfo.mIndeterminateTintMode);
                }
                if (this.mIndeterminateDrawable.isStateful()) {
                    this.mIndeterminateDrawable.setState(getDrawableState());
                }
            }
        }
    }

    public void setIndeterminateDrawableTiled(Drawable d) {
        if (d != null) {
            d = tileifyIndeterminate(d);
        }
        setIndeterminateDrawable(d);
    }

    public Drawable getProgressDrawable() {
        return this.mProgressDrawable;
    }

    public void setProgressDrawable(Drawable d) {
        if (this.mProgressDrawable != d) {
            if (this.mProgressDrawable != null) {
                this.mProgressDrawable.setCallback(null);
                unscheduleDrawable(this.mProgressDrawable);
            }
            this.mProgressDrawable = d;
            if (d != null) {
                d.setCallback(this);
                DrawableCompat.setLayoutDirection(d, getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                if (this.mCurrentMode == 3) {
                    int drawableWidth = d.getMinimumWidth();
                    if (this.mMaxWidth < drawableWidth) {
                        this.mMaxWidth = drawableWidth;
                        requestLayout();
                    }
                } else {
                    int drawableHeight = d.getMinimumHeight();
                    if (this.mMaxHeight < drawableHeight) {
                        this.mMaxHeight = drawableHeight;
                        requestLayout();
                    }
                }
                applyProgressTints();
            }
            if (!this.mIndeterminate) {
                swapCurrentDrawable(d);
                postInvalidate();
            }
            updateDrawableBounds(getWidth(), getHeight());
            updateDrawableState();
            doRefreshProgress(C0247R.id.progress, this.mProgress, false, false, false);
            doRefreshProgress(C0247R.id.secondaryProgress, this.mSecondaryProgress, false, false, false);
        }
    }

    public boolean getMirrorForRtl() {
        return this.mMirrorForRtl;
    }

    private void applyProgressTints() {
        if (this.mProgressDrawable != null && this.mProgressTintInfo != null) {
            applyPrimaryProgressTint();
            applyProgressBackgroundTint();
            applySecondaryProgressTint();
        }
    }

    private void applyPrimaryProgressTint() {
        if (this.mProgressTintInfo.mHasProgressTint || this.mProgressTintInfo.mHasProgressTintMode) {
            Drawable target = getTintTarget(C0247R.id.progress, true);
            if (target != null) {
                if (this.mProgressTintInfo.mHasProgressTint) {
                    target.setTintList(this.mProgressTintInfo.mProgressTintList);
                }
                if (this.mProgressTintInfo.mHasProgressTintMode) {
                    target.setTintMode(this.mProgressTintInfo.mProgressTintMode);
                }
                if (target.isStateful()) {
                    target.setState(getDrawableState());
                }
            }
        }
    }

    private void applyProgressBackgroundTint() {
        if (this.mProgressTintInfo.mHasProgressBackgroundTint || this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
            Drawable target = getTintTarget(C0247R.id.background, false);
            if (target != null) {
                if (this.mProgressTintInfo.mHasProgressBackgroundTint) {
                    target.setTintList(this.mProgressTintInfo.mProgressBackgroundTintList);
                }
                if (this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
                    target.setTintMode(this.mProgressTintInfo.mProgressBackgroundTintMode);
                }
                if (target.isStateful()) {
                    target.setState(getDrawableState());
                }
            }
        }
    }

    private void applySecondaryProgressTint() {
        if (this.mProgressTintInfo.mHasSecondaryProgressTint || this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
            Drawable target = getTintTarget(C0247R.id.secondaryProgress, false);
            if (target != null) {
                if (this.mProgressTintInfo.mHasSecondaryProgressTint) {
                    target.setTintList(this.mProgressTintInfo.mSecondaryProgressTintList);
                }
                if (this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
                    target.setTintMode(this.mProgressTintInfo.mSecondaryProgressTintMode);
                }
                if (target.isStateful()) {
                    target.setState(getDrawableState());
                }
            }
        }
    }

    public void setProgressTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressTintList = tint;
        this.mProgressTintInfo.mHasProgressTint = true;
        if (this.mProgressDrawable != null) {
            applyPrimaryProgressTint();
        }
    }

    public ColorStateList getProgressTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressTintList : null;
    }

    public void setProgressTintMode(Mode tintMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressTintMode = tintMode;
        this.mProgressTintInfo.mHasProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            applyPrimaryProgressTint();
        }
    }

    public Mode getProgressTintMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressTintMode : null;
    }

    public void setProgressBackgroundTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBackgroundTintList = tint;
        this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        if (this.mProgressDrawable != null) {
            applyProgressBackgroundTint();
        }
    }

    public ColorStateList getProgressBackgroundTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressBackgroundTintList : null;
    }

    public void setProgressBackgroundTintMode(Mode tintMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBackgroundTintMode = tintMode;
        this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        if (this.mProgressDrawable != null) {
            applyProgressBackgroundTint();
        }
    }

    public Mode getProgressBackgroundTintMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressBackgroundTintMode : null;
    }

    public void setSecondaryProgressTintList(ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mSecondaryProgressTintList = tint;
        this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        if (this.mProgressDrawable != null) {
            applySecondaryProgressTint();
        }
    }

    public ColorStateList getSecondaryProgressTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mSecondaryProgressTintList : null;
    }

    public void setSecondaryProgressTintMode(Mode tintMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressTintInfo();
        }
        this.mProgressTintInfo.mSecondaryProgressTintMode = tintMode;
        this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            applySecondaryProgressTint();
        }
    }

    public Mode getSecondaryProgressTintMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mSecondaryProgressTintMode : null;
    }

    private Drawable getTintTarget(int layerId, boolean shouldFallback) {
        Drawable layer = null;
        Drawable d = this.mProgressDrawable;
        if (d == null) {
            return null;
        }
        this.mProgressDrawable = d.mutate();
        if (d instanceof LayerDrawable) {
            layer = ((LayerDrawable) d).findDrawableByLayerId(layerId);
        }
        if (shouldFallback && layer == null) {
            return d;
        }
        return layer;
    }

    public void setProgressDrawableTiled(Drawable d) {
        if (d != null) {
            d = tileify(d, false);
        }
        setProgressDrawable(d);
    }

    Drawable getCurrentDrawable() {
        return this.mCurrentDrawable;
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mProgressDrawable || who == this.mIndeterminateDrawable || super.verifyDrawable(who);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.jumpToCurrentState();
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.jumpToCurrentState();
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            DrawableCompat.setLayoutDirection(d, getLayoutDirection());
        }
        if (this.mIndeterminateDrawable != null) {
            DrawableCompat.setLayoutDirection(this.mIndeterminateDrawable, getLayoutDirection());
        }
        if (this.mProgressDrawable != null) {
            DrawableCompat.setLayoutDirection(this.mProgressDrawable, getLayoutDirection());
        }
    }

    public void postInvalidate() {
        if (!this.mNoInvalidate) {
            super.postInvalidate();
        }
    }

    private synchronized void doRefreshProgress(int id, int progress, boolean fromUser, boolean callBackToApp, boolean animate) {
        int range = this.mMax - this.mMin;
        float scale = range > 0 ? ((float) (progress - this.mMin)) / ((float) range) : 0.0f;
        boolean isPrimary = id == C0247R.id.progress;
        int level = (int) (10000.0f * scale);
        Drawable d = this.mCurrentDrawable;
        if (d == null) {
            invalidate();
        } else if (d instanceof LayerDrawable) {
            progressDrawable = ((LayerDrawable) d).findDrawableByLayerId(id);
            if (progressDrawable != null && canResolveLayoutDirection()) {
                DrawableCompat.setLayoutDirection(progressDrawable, getLayoutDirection());
            }
            if (progressDrawable != null) {
                d = progressDrawable;
            }
            d.setLevel(level);
        } else if (d instanceof StateListDrawable) {
            int numStates = StateListDrawable_getStateCount((StateListDrawable) d);
            for (int i = 0; i < numStates; i++) {
                Drawable mStateDrawable = StateListDrawable_getStateDrawable((StateListDrawable) d, i);
                progressDrawable = null;
                if (mStateDrawable == null) {
                    break;
                }
                Drawable drawable;
                if (mStateDrawable instanceof LayerDrawable) {
                    progressDrawable = ((LayerDrawable) mStateDrawable).findDrawableByLayerId(id);
                    if (progressDrawable != null && canResolveLayoutDirection()) {
                        DrawableCompat.setLayoutDirection(progressDrawable, getLayoutDirection());
                    }
                }
                if (progressDrawable != null) {
                    drawable = progressDrawable;
                } else {
                    drawable = d;
                }
                drawable.setLevel(level);
            }
        } else {
            if (null != null) {
                d = null;
            }
            d.setLevel(level);
        }
        if (isPrimary && animate) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, this.VISUAL_PROGRESS, new float[]{scale});
            animator.setAutoCancel(true);
            animator.setDuration(80);
            animator.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
            animator.start();
        } else {
            setVisualProgress(id, scale);
        }
        if (isPrimary && callBackToApp) {
            onProgressRefresh(scale, fromUser, progress);
        }
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        if (((AccessibilityManager) getContext().getSystemService("accessibility")).isEnabled()) {
            scheduleAccessibilityEventSender();
        }
        if (this.mSecondaryProgress > this.mProgress && !fromUser) {
            refreshProgress(C0247R.id.secondaryProgress, this.mSecondaryProgress, false, false);
        }
    }

    private void setVisualProgress(int id, float progress) {
        this.mVisualProgress = progress;
        Drawable d = this.mCurrentDrawable;
        if (d instanceof LayerDrawable) {
            d = ((LayerDrawable) d).findDrawableByLayerId(id);
            if (d == null) {
                d = this.mCurrentDrawable;
            }
        }
        if (d != null) {
            d.setLevel((int) (10000.0f * progress));
        } else {
            invalidate();
        }
        onVisualProgressChanged(id, progress);
    }

    void onVisualProgressChanged(int id, float progress) {
    }

    private synchronized void refreshProgress(int id, int progress, boolean fromUser, boolean animate) {
        if (this.mUiThreadId == Thread.currentThread().getId()) {
            doRefreshProgress(id, progress, fromUser, true, animate);
        } else {
            if (this.mRefreshProgressRunnable == null) {
                this.mRefreshProgressRunnable = new RefreshProgressRunnable();
            }
            this.mRefreshData.add(RefreshData.obtain(id, progress, fromUser, animate));
            if (this.mAttached && !this.mRefreshIsPosted) {
                post(this.mRefreshProgressRunnable);
                this.mRefreshIsPosted = true;
            }
        }
    }

    public synchronized void setProgress(int progress) {
        setProgressInternal(progress, false, false);
    }

    public void setProgress(int progress, boolean animate) {
        setProgressInternal(progress, false, animate);
    }

    synchronized boolean setProgressInternal(int progress, boolean fromUser, boolean animate) {
        boolean z = false;
        synchronized (this) {
            if (!this.mIndeterminate) {
                progress = MathUtils.constrain(progress, this.mMin, this.mMax);
                if (progress != this.mProgress) {
                    this.mProgress = progress;
                    refreshProgress(C0247R.id.progress, this.mProgress, fromUser, animate);
                    z = true;
                }
            }
        }
        return z;
    }

    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (!this.mIndeterminate) {
            if (secondaryProgress < this.mMin) {
                secondaryProgress = this.mMin;
            }
            if (secondaryProgress > this.mMax) {
                secondaryProgress = this.mMax;
            }
            if (secondaryProgress != this.mSecondaryProgress) {
                this.mSecondaryProgress = secondaryProgress;
                refreshProgress(C0247R.id.secondaryProgress, this.mSecondaryProgress, false, false);
            }
        }
    }

    @ExportedProperty(category = "progress")
    public synchronized int getProgress() {
        return this.mIndeterminate ? 0 : this.mProgress;
    }

    @ExportedProperty(category = "progress")
    public synchronized int getSecondaryProgress() {
        return this.mIndeterminate ? 0 : this.mSecondaryProgress;
    }

    @ExportedProperty(category = "progress")
    public synchronized int getMin() {
        return this.mMin;
    }

    @ExportedProperty(category = "progress")
    public synchronized int getMax() {
        return this.mMax;
    }

    public synchronized void setMin(int min) {
        if (this.mMaxInitialized && min > this.mMax) {
            min = this.mMax;
        }
        this.mMinInitialized = true;
        if (!this.mMaxInitialized || min == this.mMin) {
            this.mMin = min;
        } else {
            this.mMin = min;
            postInvalidate();
            if (this.mProgress < min) {
                this.mProgress = min;
            }
            refreshProgress(C0247R.id.progress, this.mProgress, false, false);
        }
    }

    public synchronized void setMax(int max) {
        if (this.mMinInitialized && max < this.mMin) {
            max = this.mMin;
        }
        this.mMaxInitialized = true;
        if (!this.mMinInitialized || max == this.mMax) {
            this.mMax = max;
        } else {
            this.mMax = max;
            postInvalidate();
            if (this.mProgress > max) {
                this.mProgress = max;
            }
            refreshProgress(C0247R.id.progress, this.mProgress, false, false);
        }
    }

    public final synchronized void incrementProgressBy(int diff) {
        setProgress(this.mProgress + diff);
    }

    public final synchronized void incrementSecondaryProgressBy(int diff) {
        setSecondaryProgress(this.mSecondaryProgress + diff);
    }

    void startAnimation() {
        if (getVisibility() != 0) {
            return;
        }
        if (!IS_BASE_SDK_VERSION || getWindowVisibility() == 0) {
            if (this.mIndeterminateDrawable instanceof Animatable) {
                this.mShouldStartAnimationDrawable = true;
                this.mHasAnimation = false;
            } else {
                this.mHasAnimation = true;
                if (this.mInterpolator == null) {
                    this.mInterpolator = new LinearInterpolator();
                }
                if (this.mTransformation == null) {
                    this.mTransformation = new Transformation();
                } else {
                    this.mTransformation.clear();
                }
                if (this.mAnimation == null) {
                    this.mAnimation = new AlphaAnimation(0.0f, 1.0f);
                } else {
                    this.mAnimation.reset();
                }
                this.mAnimation.setRepeatMode(this.mBehavior);
                this.mAnimation.setRepeatCount(-1);
                this.mAnimation.setDuration((long) this.mDuration);
                this.mAnimation.setInterpolator(this.mInterpolator);
                this.mAnimation.setStartTime(-1);
            }
            postInvalidate();
        }
    }

    void stopAnimation() {
        this.mHasAnimation = false;
        if (this.mIndeterminateDrawable instanceof Animatable) {
            ((Animatable) this.mIndeterminateDrawable).stop();
            this.mShouldStartAnimationDrawable = false;
        }
        postInvalidate();
    }

    public void setInterpolator(Context context, int resID) {
        setInterpolator(AnimationUtils.loadInterpolator(context, resID));
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
            if (!this.mIndeterminate) {
                return;
            }
            if (visibility == 8 || visibility == 4) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (!this.mIndeterminate) {
            return;
        }
        if (visibility == 8 || visibility == 4) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (isVisible != this.mAggregatedIsVisible) {
            this.mAggregatedIsVisible = isVisible;
            if (this.mIndeterminate) {
                if (isVisible) {
                    startAnimation();
                } else {
                    stopAnimation();
                }
            }
            if (this.mCurrentDrawable != null) {
                this.mCurrentDrawable.setVisible(isVisible, false);
            }
        }
    }

    public void invalidateDrawable(Drawable dr) {
        if (!this.mInDrawing) {
            if (verifyDrawable(dr)) {
                Rect dirty = dr.getBounds();
                int scrollX = getScrollX() + getPaddingLeft();
                int scrollY = getScrollY() + getPaddingTop();
                invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
                return;
            }
            super.invalidateDrawable(dr);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
    }

    protected void updateDrawableBounds(int w, int h) {
        w -= getPaddingRight() + getPaddingLeft();
        h -= getPaddingTop() + getPaddingBottom();
        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;
        if (this.mIndeterminateDrawable != null) {
            if (this.mOnlyIndeterminate && !(this.mIndeterminateDrawable instanceof AnimationDrawable)) {
                float intrinsicAspect = ((float) this.mIndeterminateDrawable.getIntrinsicWidth()) / ((float) this.mIndeterminateDrawable.getIntrinsicHeight());
                float boundAspect = ((float) w) / ((float) h);
                if (((double) Math.abs(intrinsicAspect - boundAspect)) < 1.0E-7d) {
                    if (boundAspect > intrinsicAspect) {
                        int width = (int) (((float) h) * intrinsicAspect);
                        left = (w - width) / 2;
                        right = left + width;
                    } else {
                        int height = (int) (((float) w) * (1.0f / intrinsicAspect));
                        top = (h - height) / 2;
                        bottom = top + height;
                    }
                }
            }
            if (this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                int tempLeft = left;
                left = w - right;
                right = w - tempLeft;
            }
            this.mIndeterminateDrawable.setBounds(left, top, right, bottom);
        }
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setBounds(0, 0, right, bottom);
        }
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    void drawTrack(Canvas canvas) {
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            int saveCount = canvas.save();
            if (this.mCurrentMode != 3 && this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                canvas.translate((float) (getWidth() - getPaddingRight()), (float) getPaddingTop());
                canvas.scale(-1.0f, 1.0f);
            } else {
                canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
            }
            long time = getDrawingTime();
            if (this.mHasAnimation) {
                this.mAnimation.getTransformation(time, this.mTransformation);
                float scale = this.mTransformation.getAlpha();
                try {
                    this.mInDrawing = true;
                    d.setLevel((int) (10000.0f * scale));
                    postInvalidateOnAnimation();
                } finally {
                    this.mInDrawing = false;
                }
            }
            d.draw(canvas);
            canvas.restoreToCount(saveCount);
            if (this.mShouldStartAnimationDrawable && (d instanceof Animatable)) {
                ((Animatable) d).start();
                this.mShouldStartAnimationDrawable = false;
            }
        }
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicHeight()));
        }
        updateDrawableState();
        setMeasuredDimension(resolveSizeAndState(dw + (getPaddingLeft() + getPaddingRight()), widthMeasureSpec, 0), resolveSizeAndState(dh + (getPaddingTop() + getPaddingBottom()), heightMeasureSpec, 0));
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    private void updateDrawableState() {
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable progressDrawable = this.mProgressDrawable;
        if (progressDrawable != null && progressDrawable.isStateful()) {
            changed = false | progressDrawable.setState(state);
        }
        Drawable indeterminateDrawable = this.mIndeterminateDrawable;
        if (indeterminateDrawable != null && indeterminateDrawable.isStateful()) {
            changed |= indeterminateDrawable.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setHotspot(x, y);
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.setHotspot(x, y);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.progress = this.mProgress;
        ss.secondaryProgress = this.mSecondaryProgress;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
        setSecondaryProgress(ss.secondaryProgress);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIndeterminate) {
            startAnimation();
        }
        if (this.mRefreshData != null) {
            synchronized (this) {
                int count = this.mRefreshData.size();
                for (int i = 0; i < count; i++) {
                    RefreshData rd = (RefreshData) this.mRefreshData.get(i);
                    doRefreshProgress(rd.id, rd.progress, rd.fromUser, true, rd.animate);
                    rd.recycle();
                }
                this.mRefreshData.clear();
            }
        }
        this.mAttached = true;
    }

    protected void onDetachedFromWindow() {
        if (this.mIndeterminate) {
            stopAnimation();
        }
        if (this.mRefreshProgressRunnable != null) {
            removeCallbacks(this.mRefreshProgressRunnable);
            this.mRefreshIsPosted = false;
        }
        if (this.mAccessibilityEventSender != null) {
            removeCallbacks(this.mAccessibilityEventSender);
        }
        super.onDetachedFromWindow();
        this.mAttached = false;
    }

    public CharSequence getAccessibilityClassName() {
        return ProgressBar.class.getName();
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setItemCount(this.mMax - this.mMin);
        event.setCurrentItemIndex(this.mProgress);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (!isIndeterminate()) {
            info.setRangeInfo(RangeInfo.obtain(0, 0.0f, (float) getMax(), (float) getProgress()));
        }
    }

    private void scheduleAccessibilityEventSender() {
        if (this.mAccessibilityEventSender == null) {
            this.mAccessibilityEventSender = new AccessibilityEventSender();
        } else {
            removeCallbacks(this.mAccessibilityEventSender);
        }
        postDelayed(this.mAccessibilityEventSender, 200);
    }

    public boolean isAnimating() {
        return isIndeterminate() && getWindowVisibility() == 0 && isShown();
    }

    public void setMode(int mode) {
        this.mCurrentMode = mode;
        Drawable progressDrawable;
        switch (mode) {
            case 3:
                progressDrawable = getContext().getDrawable(C0247R.drawable.sesl_scrubber_progress_vertical);
                if (progressDrawable != null) {
                    setProgressDrawableTiled(progressDrawable);
                    return;
                }
                return;
            case 4:
                progressDrawable = getContext().getDrawable(C0247R.drawable.sesl_split_seekbar_background_progress);
                if (progressDrawable != null) {
                    setProgressDrawableTiled(progressDrawable);
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onSlidingRefresh(int level) {
        Drawable d = this.mCurrentDrawable;
        if (this.mCurrentDrawable != null) {
            Drawable progressDrawable = null;
            if (d instanceof LayerDrawable) {
                progressDrawable = ((LayerDrawable) d).findDrawableByLayerId(C0247R.id.progress);
            }
            if (progressDrawable != null) {
                progressDrawable.setLevel(level);
            }
        }
    }

    public int getPaddingLeft() {
        return SeslViewReflector.getField_mPaddingLeft(this);
    }

    public int getPaddingRight() {
        return SeslViewReflector.getField_mPaddingRight(this);
    }

    private static int StateListDrawable_getStateCount(StateListDrawable sd) {
        if (IS_BASE_SDK_VERSION) {
            SeslStateListDrawableReflector.getStateCount(sd);
        }
        return 0;
    }

    private static Drawable StateListDrawable_getStateDrawable(StateListDrawable sd, int count) {
        if (IS_BASE_SDK_VERSION) {
            return SeslStateListDrawableReflector.getStateDrawable(sd, count);
        }
        return null;
    }

    private int[] StateListDrawable_getStateSet(StateListDrawable sd, int count) {
        if (IS_BASE_SDK_VERSION) {
            return SeslStateListDrawableReflector.getStateSet(sd, count);
        }
        return null;
    }
}
