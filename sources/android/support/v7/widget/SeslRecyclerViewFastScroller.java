package android.support.v7.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.math.MathUtils;
import android.support.v4.view.SeslViewGroupReflector;
import android.support.v4.view.SeslViewReflector.SeslMeasureSpecReflector;
import android.support.v7.recyclerview.C0270R;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroupOverlay;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SectionIndexer;
import android.widget.TextView;

class SeslRecyclerViewFastScroller {
    private static Property<View, Integer> BOTTOM = new IntProperty<View>("bottom") {
        public void setValue(View object, int value) {
            object.setBottom(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getBottom());
        }
    };
    private static final int DURATION_CROSS_FADE = 0;
    private static final int DURATION_FADE_IN = 167;
    private static final int DURATION_FADE_OUT = 167;
    private static final int DURATION_RESIZE = 100;
    public static final int EFFECT_STATE_CLOSE = 0;
    public static final int EFFECT_STATE_OPEN = 1;
    private static final long FADE_TIMEOUT = 2500;
    private static Property<View, Integer> LEFT = new IntProperty<View>("left") {
        public void setValue(View object, int value) {
            object.setLeft(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getLeft());
        }
    };
    private static final int MIN_PAGES = 1;
    private static final int OVERLAY_ABOVE_THUMB = 2;
    private static final int OVERLAY_AT_THUMB = 1;
    private static final int OVERLAY_FLOATING = 0;
    private static final int PREVIEW_LEFT = 0;
    private static final int PREVIEW_RIGHT = 1;
    private static Property<View, Integer> RIGHT = new IntProperty<View>("right") {
        public void setValue(View object, int value) {
            object.setRight(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getRight());
        }
    };
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_VISIBLE = 1;
    private static final String TAG = "SeslFastScroller";
    private static final long TAP_TIMEOUT = ((long) ViewConfiguration.getTapTimeout());
    private static final int THUMB_POSITION_INSIDE = 1;
    private static final int THUMB_POSITION_MIDPOINT = 0;
    private static Property<View, Integer> TOP = new IntProperty<View>("top") {
        public void setValue(View object, int value) {
            object.setTop(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getTop());
        }
    };
    private int mAdditionalBottomPadding;
    private float mAdditionalTouchArea = 0.0f;
    private boolean mAlwaysShow;
    private int mColorPrimary = -1;
    private final Rect mContainerRect = new Rect();
    private Context mContext;
    private int mCurrentSection = -1;
    private AnimatorSet mDecorAnimation;
    private final Runnable mDeferHide = new C03821();
    private int mEffectState = 0;
    private boolean mEnabled;
    private int mFirstVisibleItem;
    private int mHeaderCount;
    private float mInitialTouchY;
    private boolean mLayoutFromRight;
    private Adapter mListAdapter;
    private boolean mLongList;
    private boolean mMatchDragPosition;
    private int mOldChildCount;
    private int mOldItemCount;
    private float mOldThumbPosition = -1.0f;
    private int mOrientation;
    private final ViewGroupOverlay mOverlay;
    private int mOverlayPosition;
    private long mPendingDrag = -1;
    private AnimatorSet mPreviewAnimation;
    private final View mPreviewImage;
    private int mPreviewMarginEnd;
    private int mPreviewMinHeight;
    private int mPreviewMinWidth;
    private int mPreviewPadding;
    private final int[] mPreviewResId = new int[2];
    private final TextView mPrimaryText;
    private final RecyclerView mRecyclerView;
    private int mScaledTouchSlop;
    private int mScrollBarStyle;
    private boolean mScrollCompleted;
    private float mScrollY = 0.0f;
    private int mScrollbarPosition = -1;
    private final TextView mSecondaryText;
    private SectionIndexer mSectionIndexer;
    private Object[] mSections;
    private boolean mShowingPreview;
    private boolean mShowingPrimary;
    private int mState;
    private final AnimatorListener mSwitchPrimaryListener = new C03832();
    private final Rect mTempBounds = new Rect();
    private final Rect mTempMargins = new Rect();
    private int mTextAppearance;
    private ColorStateList mTextColor;
    private float mTextSize;
    private Drawable mThumbDrawable;
    private final ImageView mThumbImage;
    private int mThumbMarginEnd;
    private int mThumbMinHeight;
    private int mThumbMinWidth;
    private float mThumbOffset;
    private int mThumbPosition;
    private float mThumbRange;
    private Drawable mTrackDrawable;
    private final ImageView mTrackImage;
    private int mTrackPadding;
    private boolean mUpdatingLayout;
    private int mWidth;

    /* renamed from: android.support.v7.widget.SeslRecyclerViewFastScroller$1 */
    class C03821 implements Runnable {
        C03821() {
        }

        public void run() {
            SeslRecyclerViewFastScroller.this.setState(0);
        }
    }

    /* renamed from: android.support.v7.widget.SeslRecyclerViewFastScroller$2 */
    class C03832 extends AnimatorListenerAdapter {
        C03832() {
        }

        public void onAnimationEnd(Animator animation) {
            SeslRecyclerViewFastScroller.this.mShowingPrimary = !SeslRecyclerViewFastScroller.this.mShowingPrimary;
        }
    }

    public SeslRecyclerViewFastScroller(RecyclerView recyclerView, int styleResId) {
        boolean z = true;
        this.mRecyclerView = recyclerView;
        this.mOldItemCount = recyclerView.getAdapter().getItemCount();
        this.mOldChildCount = recyclerView.getChildCount();
        this.mContext = recyclerView.getContext();
        this.mScaledTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
        this.mScrollBarStyle = recyclerView.getScrollBarStyle();
        this.mScrollCompleted = true;
        this.mState = 1;
        if (this.mContext.getApplicationInfo().targetSdkVersion < 11) {
            z = false;
        }
        this.mMatchDragPosition = z;
        this.mTrackImage = new ImageView(this.mContext);
        this.mTrackImage.setScaleType(ScaleType.FIT_XY);
        this.mThumbImage = new ImageView(this.mContext);
        this.mThumbImage.setScaleType(ScaleType.FIT_XY);
        this.mPreviewImage = new View(this.mContext);
        this.mPreviewImage.setAlpha(0.0f);
        this.mPrimaryText = createPreviewTextView(this.mContext);
        this.mSecondaryText = createPreviewTextView(this.mContext);
        setStyle(styleResId);
        ViewGroupOverlay overlay = recyclerView.getOverlay();
        this.mOverlay = overlay;
        overlay.add(this.mTrackImage);
        overlay.add(this.mThumbImage);
        overlay.add(this.mPreviewImage);
        overlay.add(this.mPrimaryText);
        overlay.add(this.mSecondaryText);
        this.mPreviewMarginEnd = this.mContext.getResources().getDimensionPixelOffset(C0270R.dimen.sesl_fast_scroll_preview_margin_end);
        this.mThumbMarginEnd = this.mContext.getResources().getDimensionPixelOffset(C0270R.dimen.sesl_fast_scroll_thumb_margin_end);
        this.mAdditionalTouchArea = this.mContext.getResources().getDimension(C0270R.dimen.sesl_fast_scroll_additional_touch_area);
        this.mTrackPadding = this.mContext.getResources().getDimensionPixelOffset(C0270R.dimen.sesl_fast_scroller_track_padding);
        this.mAdditionalBottomPadding = this.mContext.getResources().getDimensionPixelOffset(C0270R.dimen.sesl_fast_scroller_additional_bottom_padding);
        this.mPrimaryText.setPadding(this.mPreviewPadding, 0, this.mPreviewPadding, 0);
        this.mSecondaryText.setPadding(this.mPreviewPadding, 0, this.mPreviewPadding, 0);
        getSectionsFromIndexer();
        updateLongList(this.mOldChildCount, this.mOldItemCount);
        setScrollbarPosition(recyclerView.getVerticalScrollbarPosition());
        postAutoHide();
    }

    private void updateAppearance() {
        int width = 0;
        TypedValue outValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(C0270R.attr.colorPrimary, outValue, true);
        this.mColorPrimary = this.mContext.getResources().getColor(outValue.resourceId);
        this.mTrackImage.setImageDrawable(this.mTrackDrawable);
        if (this.mTrackDrawable != null) {
            width = Math.max(0, this.mTrackDrawable.getIntrinsicWidth());
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setTint(this.mColorPrimary);
        }
        this.mThumbImage.setImageDrawable(this.mThumbDrawable);
        this.mThumbImage.setMinimumWidth(this.mThumbMinWidth);
        this.mThumbImage.setMinimumHeight(this.mThumbMinHeight);
        if (this.mThumbDrawable != null) {
            width = Math.max(width, this.mThumbDrawable.getIntrinsicWidth());
        }
        this.mWidth = Math.max(width, this.mThumbMinWidth);
        this.mPreviewImage.setMinimumWidth(this.mPreviewMinWidth);
        this.mPreviewImage.setMinimumHeight(this.mPreviewMinHeight);
        if (this.mTextAppearance != 0) {
            this.mPrimaryText.setTextAppearance(this.mContext, this.mTextAppearance);
            this.mSecondaryText.setTextAppearance(this.mContext, this.mTextAppearance);
        }
        if (this.mTextColor != null) {
            this.mPrimaryText.setTextColor(this.mTextColor);
            this.mSecondaryText.setTextColor(this.mTextColor);
        }
        if (this.mTextSize > 0.0f) {
            this.mPrimaryText.setTextSize(0, this.mTextSize);
            this.mSecondaryText.setTextSize(0, this.mTextSize);
        }
        int textMinSize = Math.max(0, this.mPreviewMinHeight);
        this.mPrimaryText.setMinimumWidth(this.mPreviewMinWidth);
        this.mPrimaryText.setMinimumHeight(textMinSize);
        this.mPrimaryText.setIncludeFontPadding(false);
        this.mSecondaryText.setMinimumWidth(this.mPreviewMinWidth);
        this.mSecondaryText.setMinimumHeight(textMinSize);
        this.mSecondaryText.setIncludeFontPadding(false);
        this.mOrientation = this.mContext.getResources().getConfiguration().orientation;
        refreshDrawablePressedState();
    }

    public void setStyle(int resId) {
        TypedArray ta = this.mContext.obtainStyledAttributes(null, C0270R.styleable.FastScroll, 16843767, resId);
        int N = ta.getIndexCount();
        for (int i = 0; i < N; i++) {
            int index = ta.getIndex(i);
            if (index == C0270R.styleable.FastScroll_position) {
                this.mOverlayPosition = ta.getInt(index, 0);
            } else if (index == C0270R.styleable.FastScroll_backgroundLeft) {
                this.mPreviewResId[0] = ta.getResourceId(index, 0);
            } else if (index == C0270R.styleable.FastScroll_backgroundRight) {
                this.mPreviewResId[1] = ta.getResourceId(index, 0);
            } else if (index == C0270R.styleable.FastScroll_thumbDrawable) {
                this.mThumbDrawable = ta.getDrawable(index);
            } else if (index == C0270R.styleable.FastScroll_trackDrawable) {
                this.mTrackDrawable = ta.getDrawable(index);
            } else if (index == C0270R.styleable.FastScroll_android_textAppearance) {
                this.mTextAppearance = ta.getResourceId(index, 0);
            } else if (index == C0270R.styleable.FastScroll_android_textColor) {
                this.mTextColor = ta.getColorStateList(index);
            } else if (index == C0270R.styleable.FastScroll_android_textSize) {
                this.mTextSize = (float) ta.getDimensionPixelSize(index, 0);
            } else if (index == C0270R.styleable.FastScroll_android_minWidth) {
                this.mPreviewMinWidth = ta.getDimensionPixelSize(index, 0);
            } else if (index == C0270R.styleable.FastScroll_android_minHeight) {
                this.mPreviewMinHeight = ta.getDimensionPixelSize(index, 0);
            } else if (index == C0270R.styleable.FastScroll_thumbMinWidth) {
                this.mThumbMinWidth = ta.getDimensionPixelSize(index, 0);
            } else if (index == C0270R.styleable.FastScroll_thumbMinHeight) {
                this.mThumbMinHeight = ta.getDimensionPixelSize(index, 0);
            } else if (index == C0270R.styleable.FastScroll_android_padding) {
                this.mPreviewPadding = ta.getDimensionPixelSize(index, 0);
            } else if (index == C0270R.styleable.FastScroll_thumbPosition) {
                this.mThumbPosition = ta.getInt(index, 0);
            }
        }
        ta.recycle();
        updateAppearance();
    }

    public void remove() {
        this.mOverlay.remove(this.mTrackImage);
        this.mOverlay.remove(this.mThumbImage);
        this.mOverlay.remove(this.mPreviewImage);
        this.mOverlay.remove(this.mPrimaryText);
        this.mOverlay.remove(this.mSecondaryText);
    }

    public void setEnabled(boolean enabled) {
        Log.d(TAG, "setEnabled() enabled = " + enabled);
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            onStateDependencyChanged(true);
        }
    }

    public boolean isEnabled() {
        return this.mEnabled && (this.mLongList || this.mAlwaysShow);
    }

    public void setAlwaysShow(boolean alwaysShow) {
        if (this.mAlwaysShow != alwaysShow) {
            this.mAlwaysShow = alwaysShow;
            onStateDependencyChanged(false);
        }
    }

    public boolean isAlwaysShowEnabled() {
        return this.mAlwaysShow;
    }

    private void onStateDependencyChanged(boolean peekIfEnabled) {
        if (!isEnabled()) {
            stop();
        } else if (isAlwaysShowEnabled()) {
            setState(1);
        } else if (this.mState == 1) {
            postAutoHide();
        } else if (peekIfEnabled) {
            setState(1);
            postAutoHide();
        }
        SeslViewGroupReflector.resolvePadding(this.mRecyclerView);
    }

    public void setScrollBarStyle(int style) {
        if (this.mScrollBarStyle != style) {
            this.mScrollBarStyle = style;
            updateLayout();
        }
    }

    public void stop() {
        setState(0);
    }

    public void setScrollbarPosition(int position) {
        int i = 1;
        if (position == 0) {
            position = this.mRecyclerView.mLayout.getLayoutDirection() == 1 ? 1 : 2;
        }
        if (this.mScrollbarPosition != position) {
            boolean z;
            this.mScrollbarPosition = position;
            if (position != 1) {
                z = true;
            } else {
                z = false;
            }
            this.mLayoutFromRight = z;
            int[] iArr = this.mPreviewResId;
            if (!this.mLayoutFromRight) {
                i = 0;
            }
            this.mPreviewImage.setBackgroundResource(iArr[i]);
            this.mPreviewImage.getBackground().setTintMode(Mode.MULTIPLY);
            this.mPreviewImage.getBackground().setTint(this.mColorPrimary);
            updateLayout();
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    int getEffectState() {
        return this.mEffectState;
    }

    float getScrollY() {
        return this.mScrollY;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateLayout();
    }

    public void onItemCountChanged(int childCount, int itemCount) {
        if (this.mOldItemCount != itemCount || this.mOldChildCount != childCount) {
            this.mOldItemCount = itemCount;
            this.mOldChildCount = childCount;
            if ((itemCount - childCount > 0) && this.mState != 2) {
                setThumbPos(getPosFromItemCount(this.mRecyclerView.findFirstVisibleItemPosition(), childCount, itemCount));
            }
            updateLongList(childCount, itemCount);
        }
    }

    private void updateLongList(int childCount, int itemCount) {
        boolean longList = childCount > 0 && (canScrollList(1) || canScrollList(-1));
        if (this.mLongList != longList) {
            this.mLongList = longList;
            onStateDependencyChanged(true);
        }
    }

    private TextView createPreviewTextView(Context context) {
        LayoutParams params = new LayoutParams(-2, -2);
        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.MIDDLE);
        textView.setGravity(17);
        textView.setAlpha(0.0f);
        textView.setLayoutDirection(this.mRecyclerView.getLayoutDirection());
        return textView;
    }

    public void updateLayout() {
        if (!this.mUpdatingLayout) {
            this.mUpdatingLayout = true;
            updateContainerRect();
            layoutThumb();
            layoutTrack();
            updateOffsetAndRange();
            this.mUpdatingLayout = false;
            Rect bounds = this.mTempBounds;
            measurePreview(this.mPrimaryText, bounds);
            applyLayout(this.mPrimaryText, bounds);
            measurePreview(this.mSecondaryText, bounds);
            applyLayout(this.mSecondaryText, bounds);
            bounds.left -= this.mPreviewImage.getPaddingLeft();
            bounds.top -= this.mPreviewImage.getPaddingTop();
            bounds.right += this.mPreviewImage.getPaddingRight();
            bounds.bottom += this.mPreviewImage.getPaddingBottom();
            applyLayout(this.mPreviewImage, bounds);
        }
    }

    private void applyLayout(View view, Rect bounds) {
        view.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
        view.setPivotX(this.mLayoutFromRight ? (float) (bounds.right - bounds.left) : 0.0f);
    }

    private void measurePreview(View v, Rect out) {
        Rect margins = this.mTempMargins;
        margins.left = this.mPreviewImage.getPaddingLeft();
        margins.top = this.mPreviewImage.getPaddingTop();
        margins.right = this.mPreviewImage.getPaddingRight();
        margins.bottom = this.mPreviewImage.getPaddingBottom();
        if (this.mOverlayPosition == 0) {
            measureFloating(v, margins, out);
        } else {
            measureViewToSide(v, this.mThumbImage, margins, out);
        }
    }

    private void measureViewToSide(View view, View adjacent, Rect margins, Rect out) {
        int marginRight;
        int marginLeft;
        int maxWidth;
        int right;
        int left;
        if (this.mLayoutFromRight) {
            if (adjacent == null) {
                marginRight = this.mThumbMarginEnd;
                marginLeft = 0;
            } else {
                marginRight = this.mPreviewMarginEnd;
                marginLeft = 0;
            }
        } else if (adjacent == null) {
            marginLeft = this.mThumbMarginEnd;
            marginRight = 0;
        } else {
            marginLeft = this.mPreviewMarginEnd;
            marginRight = 0;
        }
        Rect container = this.mContainerRect;
        int containerWidth = container.width();
        if (adjacent == null) {
            maxWidth = containerWidth;
        } else if (this.mLayoutFromRight) {
            maxWidth = adjacent.getLeft();
        } else {
            maxWidth = containerWidth - adjacent.getRight();
        }
        int adjMaxHeight = Math.max(0, container.height());
        int adjMaxWidth = Math.max(0, (maxWidth - marginLeft) - marginRight);
        view.measure(MeasureSpec.makeMeasureSpec(adjMaxWidth, Integer.MIN_VALUE), SeslMeasureSpecReflector.makeSafeMeasureSpec(MeasureSpec.getSize(adjMaxHeight), 0));
        int width = Math.min(adjMaxWidth, view.getMeasuredWidth());
        if (this.mLayoutFromRight) {
            right = (adjacent == null ? container.right : adjacent.getLeft()) - marginRight;
            left = right - width;
        } else {
            left = (adjacent == null ? container.left : adjacent.getRight()) + marginLeft;
            right = left + width;
        }
        out.set(left, 0, right, view.getMeasuredHeight() + 0);
    }

    private void measureFloating(View preview, Rect margins, Rect out) {
        int marginLeft;
        int marginTop;
        int marginRight;
        if (margins == null) {
            marginLeft = 0;
            marginTop = 0;
            marginRight = 0;
        } else {
            marginLeft = margins.left;
            marginTop = margins.top;
            marginRight = margins.right;
        }
        Rect container = this.mContainerRect;
        int containerWidth = container.width();
        View view = preview;
        view.measure(MeasureSpec.makeMeasureSpec(Math.max(0, (containerWidth - marginLeft) - marginRight), Integer.MIN_VALUE), SeslMeasureSpecReflector.makeSafeMeasureSpec(MeasureSpec.getSize(Math.max(0, container.height())), 0));
        int containerHeight = container.height();
        int width = preview.getMeasuredWidth();
        int top = ((containerHeight / 10) + marginTop) + container.top;
        int left = ((containerWidth - width) / 2) + container.left;
        Rect rect = out;
        rect.set(left, top, left + width, top + preview.getMeasuredHeight());
    }

    private void updateContainerRect() {
        RecyclerView list = this.mRecyclerView;
        SeslViewGroupReflector.resolvePadding(this.mRecyclerView);
        Rect container = this.mContainerRect;
        container.left = 0;
        container.top = 0;
        container.right = list.getWidth();
        container.bottom = list.getHeight();
        int scrollbarStyle = this.mScrollBarStyle;
        if (scrollbarStyle == 16777216 || scrollbarStyle == 0) {
            container.left += list.getPaddingLeft();
            container.top += list.getPaddingTop();
            container.right -= list.getPaddingRight();
            container.bottom -= list.getPaddingBottom();
            if (scrollbarStyle == 16777216) {
                int width = getWidth();
                if (this.mScrollbarPosition == 2) {
                    container.right += width;
                } else {
                    container.left -= width;
                }
            }
        }
    }

    private void layoutThumb() {
        Rect bounds = this.mTempBounds;
        measureViewToSide(this.mThumbImage, null, null, bounds);
        applyLayout(this.mThumbImage, bounds);
    }

    private void layoutTrack() {
        int top;
        int bottom;
        View track = this.mTrackImage;
        View thumb = this.mThumbImage;
        Rect container = this.mContainerRect;
        track.measure(MeasureSpec.makeMeasureSpec(Math.max(0, container.width()), Integer.MIN_VALUE), SeslMeasureSpecReflector.makeSafeMeasureSpec(MeasureSpec.getSize(Math.max(0, container.height())), 0));
        if (this.mThumbPosition == 1) {
            top = container.top + this.mTrackPadding;
            bottom = (container.bottom - this.mTrackPadding) - this.mAdditionalBottomPadding;
        } else {
            int thumbHalfHeight = thumb.getHeight() / 2;
            top = (container.top + thumbHalfHeight) + this.mTrackPadding;
            bottom = ((container.bottom - thumbHalfHeight) - this.mTrackPadding) - this.mAdditionalBottomPadding;
        }
        int trackWidth = track.getMeasuredWidth();
        int left = thumb.getLeft() + ((thumb.getWidth() - trackWidth) / 2);
        track.layout(left, top, left + trackWidth, bottom);
    }

    private void updateOffsetAndRange() {
        float min;
        float max;
        View trackImage = this.mTrackImage;
        View thumbImage = this.mThumbImage;
        if (this.mThumbPosition == 1) {
            float halfThumbHeight = ((float) thumbImage.getHeight()) / 2.0f;
            min = ((float) trackImage.getTop()) + halfThumbHeight;
            max = ((float) trackImage.getBottom()) - halfThumbHeight;
        } else {
            min = (float) trackImage.getTop();
            max = (float) trackImage.getBottom();
        }
        this.mThumbOffset = min;
        this.mThumbRange = max - min;
    }

    private void setState(int state) {
        this.mRecyclerView.removeCallbacks(this.mDeferHide);
        if (this.mAlwaysShow && state == 0) {
            state = 1;
        }
        if (state != this.mState) {
            switch (state) {
                case 0:
                    transitionToHidden();
                    break;
                case 1:
                    transitionToVisible();
                    break;
                case 2:
                    transitionPreviewLayout(this.mCurrentSection);
                    break;
            }
            this.mState = state;
            refreshDrawablePressedState();
        }
    }

    private void refreshDrawablePressedState() {
        boolean isPressed = this.mState == 2;
        this.mThumbImage.setPressed(isPressed);
        this.mTrackImage.setPressed(isPressed);
    }

    private void transitionToHidden() {
        Log.d(TAG, "transitionToHidden() mState = " + this.mState);
        int duration = 0;
        this.mShowingPreview = false;
        this.mCurrentSection = -1;
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
            duration = 167;
        }
        Animator fadeOut = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration((long) duration);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{fadeOut});
        this.mDecorAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mDecorAnimation.start();
    }

    private void transitionToVisible() {
        Log.d(TAG, "transitionToVisible()");
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator fadeIn = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage).setDuration(167);
        Animator fadeOut = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(167);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{fadeIn, fadeOut});
        this.mDecorAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mShowingPreview = false;
        this.mDecorAnimation.start();
    }

    private void transitionToDragging() {
        Log.d(TAG, "transitionToDragging()");
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator fadeIn = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage).setDuration(167);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{fadeIn});
        this.mDecorAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mDecorAnimation.start();
        this.mShowingPreview = true;
    }

    private void postAutoHide() {
        this.mRecyclerView.removeCallbacks(this.mDeferHide);
        this.mRecyclerView.postDelayed(this.mDeferHide, FADE_TIMEOUT);
    }

    public boolean canScrollList(int direction) {
        int childCount = this.mRecyclerView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        int firstPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        Rect listPadding = this.mRecyclerView.mListPadding;
        if (direction > 0) {
            int lastBottom = this.mRecyclerView.getChildAt(childCount - 1).getBottom();
            if (firstPosition + childCount < this.mRecyclerView.getAdapter().getItemCount() || lastBottom > this.mRecyclerView.getHeight() - listPadding.bottom) {
                return true;
            }
            return false;
        }
        int firstTop = this.mRecyclerView.getChildAt(0).getTop();
        if (firstPosition > 0 || firstTop < listPadding.top) {
            return true;
        }
        return false;
    }

    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isEnabled()) {
            if ((canScrollList(1) || canScrollList(-1)) && this.mState != 2) {
                if (this.mOldThumbPosition != -1.0f) {
                    setThumbPos(this.mOldThumbPosition);
                    this.mOldThumbPosition = -1.0f;
                } else {
                    setThumbPos(getPosFromItemCount(firstVisibleItem, visibleItemCount, totalItemCount));
                }
            }
            this.mScrollCompleted = true;
            if (this.mFirstVisibleItem != firstVisibleItem) {
                this.mFirstVisibleItem = firstVisibleItem;
                if (this.mState != 2) {
                    setState(1);
                    postAutoHide();
                    return;
                }
                return;
            }
            return;
        }
        setState(0);
    }

    private void getSectionsFromIndexer() {
        this.mSectionIndexer = null;
        Adapter adapter = this.mRecyclerView.getAdapter();
        if (adapter instanceof SectionIndexer) {
            this.mListAdapter = adapter;
            this.mSectionIndexer = (SectionIndexer) adapter;
            this.mSections = this.mSectionIndexer.getSections();
            return;
        }
        this.mListAdapter = adapter;
        this.mSections = null;
    }

    public void onSectionsChanged() {
        this.mListAdapter = null;
    }

    private void scrollTo(float position) {
        int sectionCount;
        int targetIndex;
        int sectionIndex;
        this.mScrollCompleted = false;
        int count = this.mRecyclerView.getAdapter().getItemCount();
        Object[] sections = this.mSections;
        if (sections == null) {
            sectionCount = 0;
        } else {
            sectionCount = sections.length;
        }
        if (sections == null || sectionCount <= 0) {
            targetIndex = MathUtils.constrain((int) (((float) count) * position), 0, count - 1);
            sectionIndex = -1;
        } else {
            int exactSection = MathUtils.constrain((int) (((float) sectionCount) * position), 0, sectionCount - 1);
            int targetSection = exactSection;
            targetIndex = this.mSectionIndexer.getPositionForSection(targetSection);
            sectionIndex = targetSection;
            int nextIndex = count;
            int prevIndex = targetIndex;
            int prevSection = targetSection;
            int nextSection = targetSection + 1;
            if (targetSection < sectionCount - 1) {
                nextIndex = this.mSectionIndexer.getPositionForSection(targetSection + 1);
            }
            if (nextIndex == targetIndex) {
                while (targetSection > 0) {
                    targetSection--;
                    prevIndex = this.mSectionIndexer.getPositionForSection(targetSection);
                    if (prevIndex == targetIndex) {
                        if (targetSection == 0) {
                            sectionIndex = 0;
                            break;
                        }
                    }
                    prevSection = targetSection;
                    sectionIndex = targetSection;
                    break;
                }
            }
            int nextNextSection = nextSection + 1;
            while (nextNextSection < sectionCount && this.mSectionIndexer.getPositionForSection(nextNextSection) == nextIndex) {
                nextNextSection++;
                nextSection++;
            }
            float prevPosition = ((float) prevSection) / ((float) sectionCount);
            float nextPosition = ((float) nextSection) / ((float) sectionCount);
            float snapThreshold = count == 0 ? Float.MAX_VALUE : 0.125f / ((float) count);
            if (prevSection != exactSection || position - prevPosition >= snapThreshold) {
                targetIndex = prevIndex + ((int) ((((float) (nextIndex - prevIndex)) * (position - prevPosition)) / (nextPosition - prevPosition)));
            } else {
                targetIndex = prevIndex;
            }
            targetIndex = MathUtils.constrain(targetIndex, 0, count - 1);
        }
        if (this.mRecyclerView.mLayout instanceof LinearLayoutManager) {
            ((LinearLayoutManager) this.mRecyclerView.mLayout).scrollToPositionWithOffset(this.mHeaderCount + targetIndex, 0);
        } else {
            ((StaggeredGridLayoutManager) this.mRecyclerView.mLayout).scrollToPositionWithOffset(this.mHeaderCount + targetIndex, 0);
        }
        onScroll(this.mRecyclerView.findFirstVisibleItemPosition(), this.mRecyclerView.getChildCount(), this.mRecyclerView.getAdapter().getItemCount());
        this.mCurrentSection = sectionIndex;
        boolean hasPreview = transitionPreviewLayout(sectionIndex);
        Log.d(TAG, "scrollTo() called transitionPreviewLayout() sectionIndex =" + sectionIndex + ", position = " + position);
        if (!this.mShowingPreview && hasPreview) {
            transitionToDragging();
        } else if (this.mShowingPreview && !hasPreview) {
            transitionToVisible();
        }
    }

    private boolean transitionPreviewLayout(int sectionIndex) {
        TextView showing;
        View target;
        Object[] sections = this.mSections;
        String text = null;
        if (sections != null && sectionIndex >= 0 && sectionIndex < sections.length) {
            Object section = sections[sectionIndex];
            if (section != null) {
                text = section.toString();
            }
        }
        Rect bounds = this.mTempBounds;
        View preview = this.mPreviewImage;
        if (this.mShowingPrimary) {
            showing = this.mPrimaryText;
            target = this.mSecondaryText;
        } else {
            showing = this.mSecondaryText;
            target = this.mPrimaryText;
        }
        target.setText(text);
        measurePreview(target, bounds);
        applyLayout(target, bounds);
        if (this.mState == 1) {
            showing.setText("");
        } else if (this.mState == 2 && target.getText() == showing.getText()) {
            return !TextUtils.isEmpty(text);
        }
        if (this.mPreviewAnimation != null) {
            this.mPreviewAnimation.cancel();
        }
        Animator showTarget = animateAlpha(target, 1.0f).setDuration(TAP_TIMEOUT);
        Animator hideShowing = animateAlpha(showing, 0.0f).setDuration(TAP_TIMEOUT);
        hideShowing.addListener(this.mSwitchPrimaryListener);
        bounds.left -= preview.getPaddingLeft();
        bounds.top -= preview.getPaddingTop();
        bounds.right += preview.getPaddingRight();
        bounds.bottom += preview.getPaddingBottom();
        Animator resizePreview = animateBounds(preview, bounds);
        resizePreview.setDuration(100);
        this.mPreviewAnimation = new AnimatorSet();
        Builder builder = this.mPreviewAnimation.play(hideShowing).with(showTarget);
        builder.with(resizePreview);
        int previewWidth = (preview.getWidth() - preview.getPaddingLeft()) - preview.getPaddingRight();
        int targetWidth = target.getWidth();
        if (targetWidth > previewWidth) {
            target.setScaleX(((float) previewWidth) / ((float) targetWidth));
            builder.with(animateScaleX(target, 1.0f).setDuration(100));
        } else {
            target.setScaleX(1.0f);
        }
        int showingWidth = showing.getWidth();
        if (showingWidth > targetWidth) {
            builder.with(animateScaleX(showing, ((float) targetWidth) / ((float) showingWidth)).setDuration(100));
        }
        this.mPreviewAnimation.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
        this.mPreviewAnimation.start();
        return !TextUtils.isEmpty(text);
    }

    private void setThumbPos(float position) {
        Rect container = this.mContainerRect;
        int top = container.top;
        int bottom = container.bottom;
        if (position > 1.0f) {
            position = 1.0f;
        } else if (position < 0.0f) {
            position = 0.0f;
        }
        float thumbMiddle = (this.mThumbRange * position) + this.mThumbOffset;
        this.mThumbImage.setTranslationY(thumbMiddle - (((float) this.mThumbImage.getHeight()) / 2.0f));
        View previewImage = this.mPreviewImage;
        float previewHalfHeight = ((float) previewImage.getHeight()) / 2.0f;
        float previewTop = MathUtils.constrain(thumbMiddle, ((float) top) + previewHalfHeight, ((float) bottom) - previewHalfHeight) - previewHalfHeight;
        previewImage.setTranslationY(previewTop);
        this.mPrimaryText.setTranslationY(previewTop);
        this.mSecondaryText.setTranslationY(previewTop);
    }

    private float getPosFromMotionEvent(float y) {
        if (this.mThumbRange <= 0.0f) {
            return 0.0f;
        }
        return MathUtils.constrain((y - this.mThumbOffset) / this.mThumbRange, 0.0f, 1.0f);
    }

    private float getPosFromItemCount(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        SectionIndexer sectionIndexer = this.mSectionIndexer;
        if (sectionIndexer == null || this.mListAdapter == null) {
            getSectionsFromIndexer();
        }
        if (visibleItemCount == 0 || totalItemCount == 0) {
            return 0.0f;
        }
        boolean hasSections = (sectionIndexer == null || this.mSections == null || this.mSections.length <= 0) ? false : true;
        if (hasSections && this.mMatchDragPosition) {
            firstVisibleItem -= this.mHeaderCount;
            if (firstVisibleItem < 0) {
                return 0.0f;
            }
            float incrementalPos;
            int positionsInSection;
            float posWithinSection;
            totalItemCount -= this.mHeaderCount;
            View child = this.mRecyclerView.getChildAt(0);
            if (child == null || child.getHeight() == 0) {
                incrementalPos = 0.0f;
            } else {
                incrementalPos = ((float) (this.mRecyclerView.getPaddingTop() - child.getTop())) / ((float) child.getHeight());
            }
            int section = sectionIndexer.getSectionForPosition(firstVisibleItem);
            int sectionPos = sectionIndexer.getPositionForSection(section);
            int sectionCount = this.mSections.length;
            if (section < sectionCount - 1) {
                int nextSectionPos;
                if (section + 1 < sectionCount) {
                    nextSectionPos = sectionIndexer.getPositionForSection(section + 1);
                } else {
                    nextSectionPos = totalItemCount - 1;
                }
                positionsInSection = nextSectionPos - sectionPos;
            } else {
                positionsInSection = totalItemCount - sectionPos;
            }
            if (positionsInSection == 0) {
                posWithinSection = 0.0f;
            } else {
                posWithinSection = ((((float) firstVisibleItem) + incrementalPos) - ((float) sectionPos)) / ((float) positionsInSection);
            }
            float result = (((float) section) + posWithinSection) / ((float) sectionCount);
            if (firstVisibleItem <= 0 || firstVisibleItem + visibleItemCount != totalItemCount) {
                return result;
            }
            int maxSize;
            int currentVisibleSize;
            View lastChild = this.mRecyclerView.getChildAt(visibleItemCount - 1);
            int bottomPadding = this.mRecyclerView.getPaddingBottom();
            if (this.mRecyclerView.getClipToPadding()) {
                maxSize = lastChild.getHeight();
                currentVisibleSize = (this.mRecyclerView.getHeight() - bottomPadding) - lastChild.getTop();
            } else {
                maxSize = lastChild.getHeight() + bottomPadding;
                currentVisibleSize = this.mRecyclerView.getHeight() - lastChild.getTop();
            }
            if (currentVisibleSize <= 0 || maxSize <= 0) {
                return result;
            }
            return result + ((1.0f - result) * (((float) currentVisibleSize) / ((float) maxSize)));
        } else if (visibleItemCount != totalItemCount) {
            return ((float) firstVisibleItem) / ((float) (totalItemCount - visibleItemCount));
        } else {
            if ((this.mRecyclerView.mLayout instanceof StaggeredGridLayoutManager) && firstVisibleItem != 0) {
                View view = this.mRecyclerView.getChildAt(0);
                if (view != null && ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).isFullSpan()) {
                    return 1.0f;
                }
            }
            return 0.0f;
        }
    }

    private void cancelFling() {
        MotionEvent cancelFling = MotionEvent.obtain(TAP_TIMEOUT, TAP_TIMEOUT, 3, 0.0f, 0.0f, 0);
        this.mRecyclerView.onTouchEvent(cancelFling);
        cancelFling.recycle();
    }

    private void cancelPendingDrag() {
        this.mPendingDrag = -1;
    }

    private void startPendingDrag() {
        this.mPendingDrag = SystemClock.uptimeMillis() + TAP_TIMEOUT;
    }

    private void beginDrag() {
        Log.d(TAG, "beginDrag() !!!");
        this.mPendingDrag = -1;
        if (this.mListAdapter == null) {
            getSectionsFromIndexer();
        }
        this.mRecyclerView.requestDisallowInterceptTouchEvent(true);
        cancelFling();
        setState(2);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case 0:
                Log.d(TAG, "onInterceptTouchEvent() ACTION_DOWN ev.getY() = " + ev.getY());
                if (!isPointInside(ev.getX(), ev.getY())) {
                    return false;
                }
                if (!this.mRecyclerView.isInScrollingContainer()) {
                    return true;
                }
                this.mInitialTouchY = ev.getY();
                startPendingDrag();
                return false;
            case 1:
            case 3:
                cancelPendingDrag();
                return false;
            case 2:
                if (!isPointInside(ev.getX(), ev.getY())) {
                    cancelPendingDrag();
                    return false;
                } else if (this.mPendingDrag < TAP_TIMEOUT || this.mPendingDrag > SystemClock.uptimeMillis()) {
                    return false;
                } else {
                    beginDrag();
                    float pos = getPosFromMotionEvent(this.mInitialTouchY);
                    this.mOldThumbPosition = pos;
                    scrollTo(pos);
                    Log.d(TAG, "onInterceptTouchEvent() ACTION_MOVE pendingdrag open()");
                    return onTouchEvent(ev);
                }
            default:
                return false;
        }
    }

    public boolean onInterceptHoverEvent(MotionEvent ev) {
        if (isEnabled()) {
            int actionMasked = ev.getActionMasked();
            if ((actionMasked == 9 || actionMasked == 7) && this.mState == 0 && isPointInside(ev.getX(), ev.getY())) {
                setState(1);
                postAutoHide();
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent me) {
        Rect container = this.mContainerRect;
        int top = container.top;
        int bottom = container.bottom;
        View trackImage = this.mTrackImage;
        float min = (float) trackImage.getTop();
        float max = (float) trackImage.getBottom();
        this.mScrollY = me.getY();
        if (!isEnabled()) {
            return false;
        }
        float pos;
        switch (me.getActionMasked()) {
            case 0:
                if (isPointInside(me.getX(), me.getY()) && !this.mRecyclerView.isInScrollingContainer()) {
                    beginDrag();
                    this.mEffectState = 1;
                    Log.d(TAG, "onTouchEvent() ACTION_DOWN.. open() called with posY " + me.getY());
                    return true;
                }
            case 1:
                if (this.mPendingDrag >= TAP_TIMEOUT) {
                    beginDrag();
                    pos = getPosFromMotionEvent(me.getY());
                    this.mOldThumbPosition = pos;
                    setThumbPos(pos);
                    scrollTo(pos);
                    this.mEffectState = 1;
                    Log.d(TAG, "onTouchEvent() ACTION_UP.. open() called with posY " + me.getY());
                }
                if (this.mState == 2) {
                    this.mRecyclerView.requestDisallowInterceptTouchEvent(false);
                    setState(1);
                    postAutoHide();
                    this.mEffectState = 0;
                    this.mScrollY = 0.0f;
                    return true;
                }
                break;
            case 2:
                Log.d(TAG, "onTouchEvent() ACTION_MOVE.. mState= " + this.mState + ", mInitialTouchY=" + this.mInitialTouchY);
                if (this.mPendingDrag >= TAP_TIMEOUT && Math.abs(me.getY() - this.mInitialTouchY) > ((float) this.mScaledTouchSlop)) {
                    beginDrag();
                    if (this.mScrollY > ((float) top) && this.mScrollY < ((float) bottom)) {
                        Log.d(TAG, "onTouchEvent() ACTION_MOVE 1 mScrollY=" + this.mScrollY + ", min=" + min + ", max=" + max);
                        if (this.mScrollY < ((float) top) + min) {
                            this.mScrollY = ((float) top) + min;
                        } else if (this.mScrollY > max) {
                            this.mScrollY = max;
                        }
                        this.mEffectState = 1;
                    }
                }
                if (this.mState == 2) {
                    pos = getPosFromMotionEvent(me.getY());
                    this.mOldThumbPosition = pos;
                    setThumbPos(pos);
                    if (this.mScrollCompleted) {
                        scrollTo(pos);
                    }
                    if (this.mScrollY > ((float) top) && this.mScrollY < ((float) bottom)) {
                        Log.d(TAG, "onTouchEvent() ACTION_MOVE 2 mScrollY=" + this.mScrollY + ", min=" + min + ", max=" + max);
                        if (this.mScrollY < ((float) top) + min) {
                            this.mScrollY = ((float) top) + min;
                        } else if (this.mScrollY > max) {
                            this.mScrollY = max;
                        }
                        this.mEffectState = 1;
                    }
                    return true;
                }
                break;
            case 3:
                cancelPendingDrag();
                if (this.mState == 2) {
                    setState(0);
                }
                this.mEffectState = 0;
                this.mScrollY = 0.0f;
                break;
        }
        return false;
    }

    private boolean isPointInside(float x, float y) {
        return isPointInsideX(x) && isPointInsideY(y) && this.mState != 0;
    }

    private boolean isPointInsideX(float x) {
        if (this.mLayoutFromRight) {
            if (x >= ((float) this.mThumbImage.getLeft()) - this.mAdditionalTouchArea) {
                return true;
            }
            return false;
        } else if (x > ((float) this.mThumbImage.getRight()) + this.mAdditionalTouchArea) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isPointInsideY(float y) {
        float offset = this.mThumbImage.getTranslationY();
        return y >= ((float) this.mThumbImage.getTop()) + offset && y <= ((float) this.mThumbImage.getBottom()) + offset;
    }

    private static Animator groupAnimatorOfFloat(Property<View, Float> property, float value, View... views) {
        AnimatorSet animSet = new AnimatorSet();
        Builder builder = null;
        for (int i = views.length - 1; i >= 0; i--) {
            Animator anim = ObjectAnimator.ofFloat(views[i], property, new float[]{value});
            if (builder == null) {
                builder = animSet.play(anim);
            } else {
                builder.with(anim);
            }
        }
        return animSet;
    }

    private static Animator animateScaleX(View v, float target) {
        return ObjectAnimator.ofFloat(v, View.SCALE_X, new float[]{target});
    }

    private static Animator animateAlpha(View v, float alpha) {
        return ObjectAnimator.ofFloat(v, View.ALPHA, new float[]{alpha});
    }

    private static Animator animateBounds(View v, Rect bounds) {
        r0 = new PropertyValuesHolder[4];
        r0[0] = PropertyValuesHolder.ofInt(LEFT, new int[]{bounds.left});
        r0[1] = PropertyValuesHolder.ofInt(TOP, new int[]{bounds.top});
        r0[2] = PropertyValuesHolder.ofInt(RIGHT, new int[]{bounds.right});
        r0[3] = PropertyValuesHolder.ofInt(BOTTOM, new int[]{bounds.bottom});
        return ObjectAnimator.ofPropertyValuesHolder(v, r0);
    }
}
