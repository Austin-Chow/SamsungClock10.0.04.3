package android.support.v7.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.v4.os.TraceCompat;
import android.support.v4.provider.SeslSettingsReflector.SeslSystemReflector;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.SeslInputDeviceReflector;
import android.support.v4.view.SeslPointerIconReflector;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import android.support.v4.widget.SeslEdgeEffect;
import android.support.v4.widget.SeslTextViewReflector;
import android.support.v7.recyclerview.C0270R;
import android.support.v7.util.SeslSubheaderRoundedCorner;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView extends ViewGroup implements NestedScrollingChild2, ScrollingView {
    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
    private static final boolean ALLOW_THREAD_GAP_WORK;
    private static final int[] CLIP_TO_PADDING_ATTR = new int[]{16842987};
    static final boolean DEBUG = false;
    static final int DEFAULT_ORIENTATION = 1;
    static final boolean DISPATCH_TEMP_DETACH = false;
    private static final int FOCUS_MOVE_DOWN = 1;
    private static final int FOCUS_MOVE_FULL_DOWN = 3;
    private static final int FOCUS_MOVE_FULL_UP = 2;
    private static final int FOCUS_MOVE_UP = 0;
    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
    static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    static final long FOREVER_NS = Long.MAX_VALUE;
    private static final int GTP_STATE_NONE = 0;
    private static final int GTP_STATE_PRESSED = 2;
    private static final int GTP_STATE_SHOWN = 1;
    public static final int HORIZONTAL = 0;
    private static final int HOVERSCROLL_DOWN = 2;
    private static final int HOVERSCROLL_HEIGHT_BOTTOM_DP = 25;
    private static final int HOVERSCROLL_HEIGHT_TOP_DP = 25;
    private static final int HOVERSCROLL_UP = 1;
    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE};
    static final int MAX_SCROLL_DURATION = 2000;
    private static final int MOTION_EVENT_ACTION_PEN_DOWN = 211;
    private static final int MOTION_EVENT_ACTION_PEN_MOVE = 213;
    private static final int MOTION_EVENT_ACTION_PEN_UP = 212;
    private static final int MSG_HOVERSCROLL_MOVE = 0;
    private static final int[] NESTED_SCROLLING_ATTRS = new int[]{16843830};
    public static final long NO_ID = -1;
    public static final int NO_POSITION = -1;
    static final boolean POST_UPDATES_ON_ANIMATION;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static final String TAG = "SeslRecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    static final String TRACE_PREFETCH_TAG = "RV Prefetch";
    static final String TRACE_SCROLL_TAG = "RV Scroll";
    static final boolean VERBOSE_TRACING = false;
    public static final int VERTICAL = 1;
    static final Interpolator sQuinticInterpolator = new C03264();
    private int GO_TO_TOP_HIDE;
    private int HOVERSCROLL_DELAY;
    private float HOVERSCROLL_SPEED;
    RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    boolean mAdapterUpdateDuringMeasure;
    private boolean mAlwaysDisableHoverHighlight;
    int mAnimatedBlackTop;
    private final Runnable mAutoHide;
    int mBlackTop;
    private SeslEdgeEffect mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    public ChildHelper mChildHelper;
    boolean mClipToPadding;
    private View mCloseChildByBottom;
    private View mCloseChildByTop;
    private int mCloseChildPositionByBottom;
    private int mCloseChildPositionByTop;
    private Context mContext;
    boolean mDataSetHasChangedAfterLayout;
    boolean mDispatchItemsChangedEvent;
    private int mDispatchScrollCounter;
    private int mDistanceFromCloseChildBottom;
    private int mDistanceFromCloseChildTop;
    boolean mDrawLastItemOutlineStoke;
    boolean mDrawLastOutLineStroke;
    boolean mDrawOutlineStroke;
    boolean mDrawRect;
    boolean mDrawReverse;
    boolean mDrawWhiteTheme;
    private int mEatenAccessibilityChangeFlags;
    boolean mEnableFastScroller;
    private boolean mEnableGoToTop;
    private int mExtraPaddingInBottomHoverArea;
    private int mExtraPaddingInTopHoverArea;
    private SeslRecyclerViewFastScroller mFastScroller;
    private boolean mFastScrollerEnabled;
    private SeslFastScrollerEventListener mFastScrollerEventListener;
    boolean mFirstLayoutComplete;
    GapWorker mGapWorker;
    private final Runnable mGoToToFadeInRunnable;
    private final Runnable mGoToToFadeOutRunnable;
    private int mGoToTopBottomPadding;
    private ValueAnimator mGoToTopFadeInAnimator;
    private ValueAnimator mGoToTopFadeOutAnimator;
    private boolean mGoToTopFadeOutStart;
    private Drawable mGoToTopImage;
    private Drawable mGoToTopImageLight;
    private int mGoToTopLastState;
    private boolean mGoToTopMoved;
    private Rect mGoToTopRect;
    private int mGoToTopSize;
    private int mGoToTopState;
    private boolean mGoToToping;
    boolean mHasFixedSize;
    private boolean mHasNestedScrollRange;
    public boolean mHoverAreaEnter;
    private int mHoverBottomAreaHeight;
    private Handler mHoverHandler;
    private long mHoverRecognitionCurrentTime;
    private long mHoverRecognitionDurationTime;
    private long mHoverRecognitionStartTime;
    private int mHoverScrollDirection;
    private boolean mHoverScrollEnable;
    private int mHoverScrollSpeed;
    private long mHoverScrollStartTime;
    private boolean mHoverScrollStateChanged;
    private int mHoverScrollStateForListener;
    private long mHoverScrollTimeInterval;
    private int mHoverTopAreaHeight;
    private boolean mHoveringEnabled;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTopOffsetOfScreen;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mInterceptRequestLayoutDepth;
    private boolean mIsArrowKeyPressed;
    boolean mIsAttached;
    private boolean mIsCloseChildSetted;
    private boolean mIsCtrlKeyPressed;
    private boolean mIsCtrlMultiSelection;
    private boolean mIsEnabledPaddingInHoverScroll;
    private boolean mIsFirstMultiSelectionMove;
    private boolean mIsFirstPenMoveEvent;
    private boolean mIsGoToTopShown;
    private boolean mIsHoverOverscrolled;
    private boolean mIsLongPressMultiSelection;
    private boolean mIsMouseWheel;
    private boolean mIsNeedPenSelectIconSet;
    private boolean mIsNeedPenSelection;
    private boolean mIsPenDragBlockEnabled;
    private boolean mIsPenHovered;
    private boolean mIsPenPressed;
    private boolean mIsPenSelectPointerSetted;
    private boolean mIsPenSelectionEnabled;
    private boolean mIsSendHoverScrollState;
    ItemAnimator mItemAnimator;
    private ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    int mLastBlackTop;
    private int mLastTouchX;
    private int mLastTouchY;
    LayoutManager mLayout;
    boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter;
    boolean mLayoutWasDefered;
    private SeslEdgeEffect mLeftGlow;
    Rect mListPadding;
    private SeslLongPressMultiSelectionListener mLongPressMultiSelectionListener;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private int mNavigationBarHeight;
    private boolean mNeedsHoverScroll;
    private final int[] mNestedOffsets;
    private boolean mNestedScroll;
    private int mNestedScrollRange;
    private boolean mNewTextViewHoverState;
    private final RecyclerViewDataObserver mObserver;
    private int mOldHoverScrollDirection;
    private boolean mOldTextViewHoverState;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private OnFlingListener mOnFlingListener;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    private SeslOnMultiSelectedListener mOnMultiSelectedListener;
    private int mPenDistanceFromTrackedChildTop;
    private int mPenDragBlockBottom;
    private Drawable mPenDragBlockImage;
    private int mPenDragBlockLeft;
    private Rect mPenDragBlockRect;
    private int mPenDragBlockRight;
    private int mPenDragBlockTop;
    private int mPenDragEndX;
    private int mPenDragEndY;
    private long mPenDragScrollTimeInterval;
    private ArrayList<Integer> mPenDragSelectedItemArray;
    private int mPenDragSelectedViewPosition;
    private int mPenDragStartX;
    private int mPenDragStartY;
    private View mPenTrackedChild;
    private int mPenTrackedChildPosition;
    final List<ViewHolder> mPendingAccessibilityImportanceChange;
    private SavedState mPendingSavedState;
    boolean mPostedAnimatorRunner;
    LayoutPrefetchRegistryImpl mPrefetchRegistry;
    private boolean mPreserveFocusAfterLayout;
    private int mRectColor;
    private Paint mRectPaint;
    final Recycler mRecycler;
    RecyclerListener mRecyclerListener;
    private int mRemainNestedScrollRange;
    private SeslEdgeEffect mRightGlow;
    private View mRootViewCheckForDialog;
    private float mScaledHorizontalScrollFactor;
    private float mScaledVerticalScrollFactor;
    private final int[] mScrollConsumed;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private NestedScrollingChildHelper mScrollingChildHelper;
    Drawable mSelector;
    Rect mSelectorRect;
    private SeslOnGoToTopClickListener mSeslOnGoToTopClickListener;
    private int mSeslPagingTouchSlop;
    private SeslSubheaderRoundedCorner mSeslRoundedCorner;
    private int mSeslTouchSlop;
    private int mShowFadeOutGTP;
    boolean mShowGTPAtFirstTime;
    private boolean mSizeChnage;
    final State mState;
    private int mStrokeColor;
    private int mStrokeHeight;
    private Paint mStrokePaint;
    final Rect mTempRect;
    private final Rect mTempRect2;
    final RectF mTempRectF;
    private SeslEdgeEffect mTopGlow;
    private int mTouchSlop;
    final Runnable mUpdateChildViewsRunnable;
    private boolean mUsePagingTouchSlopForStylus;
    private VelocityTracker mVelocityTracker;
    final ViewFlinger mViewFlinger;
    private final ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;
    private final int[] mWindowOffsets;

    public static abstract class ViewHolder {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_SET_A11Y_ITEM_DELEGATE = 16384;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        public final View itemView;
        private int mFlags;
        private boolean mInChangeScrap = false;
        private int mIsRecyclableCount = 0;
        long mItemId = -1;
        int mItemViewType = -1;
        WeakReference<RecyclerView> mNestedRecyclerView;
        int mOldPosition = -1;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads = null;
        int mPendingAccessibilityState = -1;
        int mPosition = -1;
        int mPreLayoutPosition = -1;
        private Recycler mScrapContainer = null;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        List<Object> mUnmodifiedPayloads = null;
        private int mWasImportantForAccessibilityBeforeHidden = 0;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
            addFlags(8);
            offsetPosition(offset, applyToPreLayout);
            this.mPosition = mNewPosition;
        }

        void offsetPosition(int offset, boolean applyToPreLayout) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (applyToPreLayout) {
                this.mPreLayoutPosition += offset;
            }
            this.mPosition += offset;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams) this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }

        boolean shouldIgnore() {
            return (this.mFlags & 128) != 0;
        }

        @Deprecated
        public final int getPosition() {
            return this.mPreLayoutPosition == -1 ? this.mPosition : this.mPreLayoutPosition;
        }

        public final int getLayoutPosition() {
            return this.mPreLayoutPosition == -1 ? this.mPosition : this.mPreLayoutPosition;
        }

        public final int getAdapterPosition() {
            if (this.mOwnerRecyclerView == null) {
                return -1;
            }
            return this.mOwnerRecyclerView.getAdapterPositionFor(this);
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        boolean isScrap() {
            return this.mScrapContainer != null;
        }

        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            return (this.mFlags & 32) != 0;
        }

        void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        void stopIgnoring() {
            this.mFlags &= -129;
        }

        void setScrapContainer(Recycler recycler, boolean isChangeScrap) {
            this.mScrapContainer = recycler;
            this.mInChangeScrap = isChangeScrap;
        }

        boolean isInvalid() {
            return (this.mFlags & 4) != 0;
        }

        boolean needsUpdate() {
            return (this.mFlags & 2) != 0;
        }

        boolean isBound() {
            return (this.mFlags & 1) != 0;
        }

        boolean isRemoved() {
            return (this.mFlags & 8) != 0;
        }

        boolean hasAnyOfTheFlags(int flags) {
            return (this.mFlags & flags) != 0;
        }

        boolean isTmpDetached() {
            return (this.mFlags & 256) != 0;
        }

        boolean isAdapterPositionUnknown() {
            return (this.mFlags & 512) != 0 || isInvalid();
        }

        void setFlags(int flags, int mask) {
            this.mFlags = (this.mFlags & (mask ^ -1)) | (flags & mask);
        }

        void addFlags(int flags) {
            this.mFlags |= flags;
        }

        void addChangePayload(Object payload) {
            if (payload == null) {
                addFlags(1024);
            } else if ((this.mFlags & 1024) == 0) {
                createPayloadsIfNeeded();
                this.mPayloads.add(payload);
            }
        }

        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList();
                this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
            }
        }

        void clearPayload() {
            if (this.mPayloads != null) {
                this.mPayloads.clear();
            }
            this.mFlags &= -1025;
        }

        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) != 0) {
                return FULLUPDATE_PAYLOADS;
            }
            if (this.mPayloads == null || this.mPayloads.size() == 0) {
                return FULLUPDATE_PAYLOADS;
            }
            return this.mUnmodifiedPayloads;
        }

        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            RecyclerView.clearNestedRecyclerViewIfNotNested(this);
        }

        private void onEnteredHiddenState(RecyclerView parent) {
            if (this.mPendingAccessibilityState != -1) {
                this.mWasImportantForAccessibilityBeforeHidden = this.mPendingAccessibilityState;
            } else {
                this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            }
            parent.setChildImportantForAccessibilityInternal(this, 4);
        }

        private void onLeftHiddenState(RecyclerView parent) {
            parent.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("ViewHolder{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
            if (isScrap()) {
                sb.append(" scrap ").append(this.mInChangeScrap ? "[changeScrap]" : "[attachedScrap]");
            }
            if (isInvalid()) {
                sb.append(" invalid");
            }
            if (!isBound()) {
                sb.append(" unbound");
            }
            if (needsUpdate()) {
                sb.append(" update");
            }
            if (isRemoved()) {
                sb.append(" removed");
            }
            if (shouldIgnore()) {
                sb.append(" ignored");
            }
            if (isTmpDetached()) {
                sb.append(" tmpDetached");
            }
            if (!isRecyclable()) {
                sb.append(" not recyclable(" + this.mIsRecyclableCount + ")");
            }
            if (isAdapterPositionUnknown()) {
                sb.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }

        public final void setIsRecyclable(boolean recyclable) {
            this.mIsRecyclableCount = recyclable ? this.mIsRecyclableCount - 1 : this.mIsRecyclableCount + 1;
            if (this.mIsRecyclableCount < 0) {
                this.mIsRecyclableCount = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            } else if (!recyclable && this.mIsRecyclableCount == 1) {
                this.mFlags |= 16;
            } else if (recyclable && this.mIsRecyclableCount == 0) {
                this.mFlags &= -17;
            }
        }

        public final boolean isRecyclable() {
            return (this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView);
        }

        private boolean shouldBeKeptAsChild() {
            return (this.mFlags & 16) != 0;
        }

        private boolean doesTransientStatePreventRecycling() {
            return (this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView);
        }

        boolean isUpdated() {
            return (this.mFlags & 2) != 0;
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private boolean mHasStableIds = false;
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public abstract int getItemCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public void onBindViewHolder(VH holder, int position, List<Object> list) {
            onBindViewHolder(holder, position);
        }

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            try {
                TraceCompat.beginSection(RecyclerView.TRACE_CREATE_VIEW_TAG);
                VH holder = onCreateViewHolder(parent, viewType);
                if (holder.itemView.getParent() != null) {
                    throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
                }
                holder.mItemViewType = viewType;
                return holder;
            } finally {
                TraceCompat.endSection();
            }
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.mPosition = position;
            if (hasStableIds()) {
                holder.mItemId = getItemId(position);
            }
            holder.setFlags(1, 519);
            TraceCompat.beginSection(RecyclerView.TRACE_BIND_VIEW_TAG);
            onBindViewHolder(holder, position, holder.getUnmodifiedPayloads());
            holder.clearPayload();
            android.view.ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).mInsetsDirty = true;
            }
            TraceCompat.endSection();
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public void setHasStableIds(boolean hasStableIds) {
            if (hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            this.mHasStableIds = hasStableIds;
        }

        public long getItemId(int position) {
            return -1;
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public void onViewRecycled(VH vh) {
        }

        public boolean onFailedToRecycleView(VH vh) {
            return false;
        }

        public void onViewAttachedToWindow(VH vh) {
        }

        public void onViewDetachedFromWindow(VH vh) {
        }

        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.unregisterObserver(observer);
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int position) {
            this.mObservable.notifyItemRangeChanged(position, 1);
        }

        public final void notifyItemChanged(int position, Object payload) {
            this.mObservable.notifyItemRangeChanged(position, 1, payload);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeChanged(positionStart, itemCount);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            this.mObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        public final void notifyItemInserted(int position) {
            this.mObservable.notifyItemRangeInserted(position, 1);
        }

        public final void notifyItemMoved(int fromPosition, int toPosition) {
            this.mObservable.notifyItemMoved(fromPosition, toPosition);
        }

        public final void notifyItemRangeInserted(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeInserted(positionStart, itemCount);
        }

        public final void notifyItemRemoved(int position) {
            this.mObservable.notifyItemRangeRemoved(position, 1);
        }

        public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public static abstract class ItemDecoration {
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            onDraw(c, parent);
        }

        @Deprecated
        public void onDraw(Canvas c, RecyclerView parent) {
        }

        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            onDrawOver(c, parent);
        }

        @Deprecated
        public void onDrawOver(Canvas c, RecyclerView parent) {
        }

        public void seslOnDispatchDraw(Canvas c, RecyclerView parent, State state) {
        }

        @Deprecated
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            getItemOffsets(outRect, ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition(), parent);
        }
    }

    public static abstract class ItemAnimator {
        static final int ANIMATION_TYPE_DEFAULT = 1;
        static final int ANIMATION_TYPE_EXPAND_COLLAPSE = 2;
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration = 120;
        private int mAnimationType = 1;
        private long mChangeDuration = 250;
        private long mExpandCollapseDuration = 700;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList();
        private ViewHolder mGroupViewHolder = null;
        private View mHostView = null;
        private ItemAnimatorListener mListener = null;
        private long mMoveDuration = 250;
        private long mRemoveDuration = 120;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges {
        }

        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        interface ItemAnimatorListener {
            void onAnimationFinished(ViewHolder viewHolder);
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo setFrom(ViewHolder holder) {
                return setFrom(holder, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder holder, int flags) {
                View view = holder.itemView;
                this.left = view.getLeft();
                this.top = view.getTop();
                this.right = view.getRight();
                this.bottom = view.getBottom();
                return this;
            }
        }

        public abstract boolean animateAppearance(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animateDisappearance(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animatePersistence(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract void endAnimation(ViewHolder viewHolder);

        public abstract void endAnimations();

        public abstract boolean isRunning();

        public abstract void runPendingAnimations();

        public long getExpandCollapseDuration() {
            return this.mExpandCollapseDuration;
        }

        public int getItemAnimationTypeInternal() {
            return this.mAnimationType;
        }

        public void setItemAnimationTypeInternal(int animationType) {
            this.mAnimationType = animationType;
        }

        public void setHostView(View hostView) {
            this.mHostView = hostView;
        }

        public View getHostView() {
            return this.mHostView;
        }

        public void setGroupViewHolderInternal(ViewHolder vH) {
            this.mGroupViewHolder = vH;
        }

        public void clearGroupViewHolderInternal() {
            this.mGroupViewHolder = null;
        }

        public ViewHolder getGroupViewHolderInternal() {
            return this.mGroupViewHolder;
        }

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public void setMoveDuration(long moveDuration) {
            this.mMoveDuration = moveDuration;
        }

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public void setAddDuration(long addDuration) {
            this.mAddDuration = addDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public void setRemoveDuration(long removeDuration) {
            this.mRemoveDuration = removeDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        public void setChangeDuration(long changeDuration) {
            this.mChangeDuration = changeDuration;
        }

        void setListener(ItemAnimatorListener listener) {
            this.mListener = listener;
        }

        public ItemHolderInfo recordPreLayoutInformation(State state, ViewHolder viewHolder, int changeFlags, List<Object> list) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        public ItemHolderInfo recordPostLayoutInformation(State state, ViewHolder viewHolder) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int flags = viewHolder.mFlags & 14;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            if ((flags & 4) == 0) {
                int oldPos = viewHolder.getOldPosition();
                int pos = viewHolder.getAdapterPosition();
                if (!(oldPos == -1 || pos == -1 || oldPos == pos)) {
                    flags |= 2048;
                }
            }
            int i = flags;
            return flags;
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            onAnimationFinished(viewHolder);
            if (this.mListener != null) {
                this.mListener.onAnimationFinished(viewHolder);
            }
        }

        public void onAnimationFinished(ViewHolder viewHolder) {
        }

        public final void dispatchAnimationStarted(ViewHolder viewHolder) {
            onAnimationStarted(viewHolder);
        }

        public void onAnimationStarted(ViewHolder viewHolder) {
        }

        public final boolean isRunning(ItemAnimatorFinishedListener listener) {
            boolean running = isRunning();
            if (listener != null) {
                if (running) {
                    this.mFinishedListeners.add(listener);
                } else {
                    listener.onAnimationsFinished();
                }
            }
            return running;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
            return true;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder, List<Object> list) {
            return canReuseUpdatedViewHolder(viewHolder);
        }

        public final void dispatchAnimationsFinished() {
            int count = this.mFinishedListeners.size();
            for (int i = 0; i < count; i++) {
                ((ItemAnimatorFinishedListener) this.mFinishedListeners.get(i)).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }
    }

    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }

    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);

        void onRequestDisallowInterceptTouchEvent(boolean z);

        void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);
    }

    public static class LayoutParams extends MarginLayoutParams {
        final Rect mDecorInsets = new Rect();
        boolean mInsetsDirty = true;
        boolean mPendingInvalidate = false;
        ViewHolder mViewHolder;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }

        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }

        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }

        @Deprecated
        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }

        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }

        public int getViewAdapterPosition() {
            return this.mViewHolder.getAdapterPosition();
        }
    }

    public static abstract class LayoutManager {
        boolean mAutoMeasure = false;
        ChildHelper mChildHelper;
        private int mHeight;
        private int mHeightMode;
        ViewBoundsCheck mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
        private final Callback mHorizontalBoundCheckCallback = new C03321();
        boolean mIsAttachedToWindow = false;
        private boolean mItemPrefetchEnabled = true;
        private boolean mMeasurementCacheEnabled = true;
        int mPrefetchMaxCountObserved;
        boolean mPrefetchMaxObservedInInitialPrefetch;
        RecyclerView mRecyclerView;
        boolean mRequestedSimpleAnimations = false;
        SmoothScroller mSmoothScroller;
        ViewBoundsCheck mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
        private final Callback mVerticalBoundCheckCallback = new C03332();
        private int mWidth;
        private int mWidthMode;

        public interface LayoutPrefetchRegistry {
            void addPosition(int i, int i2);
        }

        /* renamed from: android.support.v7.widget.RecyclerView$LayoutManager$1 */
        class C03321 implements Callback {
            C03321() {
            }

            public int getChildCount() {
                return LayoutManager.this.getChildCount();
            }

            public View getParent() {
                return LayoutManager.this.mRecyclerView;
            }

            public View getChildAt(int index) {
                return LayoutManager.this.getChildAt(index);
            }

            public int getParentStart() {
                return LayoutManager.this.getPaddingLeft();
            }

            public int getParentEnd() {
                return LayoutManager.this.getWidth() - LayoutManager.this.getPaddingRight();
            }

            public int getChildStart(View view) {
                return LayoutManager.this.getDecoratedLeft(view) - ((LayoutParams) view.getLayoutParams()).leftMargin;
            }

            public int getChildEnd(View view) {
                return LayoutManager.this.getDecoratedRight(view) + ((LayoutParams) view.getLayoutParams()).rightMargin;
            }
        }

        /* renamed from: android.support.v7.widget.RecyclerView$LayoutManager$2 */
        class C03332 implements Callback {
            C03332() {
            }

            public int getChildCount() {
                return LayoutManager.this.getChildCount();
            }

            public View getParent() {
                return LayoutManager.this.mRecyclerView;
            }

            public View getChildAt(int index) {
                return LayoutManager.this.getChildAt(index);
            }

            public int getParentStart() {
                return LayoutManager.this.getPaddingTop();
            }

            public int getParentEnd() {
                return LayoutManager.this.getHeight() - LayoutManager.this.getPaddingBottom();
            }

            public int getChildStart(View view) {
                return LayoutManager.this.getDecoratedTop(view) - ((LayoutParams) view.getLayoutParams()).topMargin;
            }

            public int getChildEnd(View view) {
                return LayoutManager.this.getDecoratedBottom(view) + ((LayoutParams) view.getLayoutParams()).bottomMargin;
            }
        }

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidth = 0;
                this.mHeight = 0;
            } else {
                this.mRecyclerView = recyclerView;
                this.mChildHelper = recyclerView.mChildHelper;
                this.mWidth = recyclerView.getWidth();
                this.mHeight = recyclerView.getHeight();
            }
            this.mWidthMode = 1073741824;
            this.mHeightMode = 1073741824;
        }

        void setMeasureSpecs(int wSpec, int hSpec) {
            this.mWidth = MeasureSpec.getSize(wSpec);
            this.mWidthMode = MeasureSpec.getMode(wSpec);
            if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mWidth = 0;
            }
            this.mHeight = MeasureSpec.getSize(hSpec);
            this.mHeightMode = MeasureSpec.getMode(hSpec);
            if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mHeight = 0;
            }
        }

        void setMeasuredDimensionFromChildren(int widthSpec, int heightSpec) {
            int count = getChildCount();
            if (count == 0) {
                this.mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
                return;
            }
            int minX = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            int minY = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                Rect bounds = this.mRecyclerView.mTempRect;
                getDecoratedBoundsWithMargins(child, bounds);
                if (bounds.left < minX) {
                    minX = bounds.left;
                }
                if (bounds.right > maxX) {
                    maxX = bounds.right;
                }
                if (bounds.top < minY) {
                    minY = bounds.top;
                }
                if (bounds.bottom > maxY) {
                    maxY = bounds.bottom;
                }
            }
            this.mRecyclerView.mTempRect.set(minX, minY, maxX, maxY);
            setMeasuredDimension(this.mRecyclerView.mTempRect, widthSpec, heightSpec);
        }

        public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
            setMeasuredDimension(chooseSize(wSpec, (childrenBounds.width() + getPaddingLeft()) + getPaddingRight(), getMinimumWidth()), chooseSize(hSpec, (childrenBounds.height() + getPaddingTop()) + getPaddingBottom(), getMinimumHeight()));
        }

        public void requestLayout() {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.requestLayout();
            }
        }

        public void assertInLayoutOrScroll(String message) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertInLayoutOrScroll(message);
            }
        }

        public static int chooseSize(int spec, int desired, int min) {
            int mode = MeasureSpec.getMode(spec);
            int size = MeasureSpec.getSize(spec);
            switch (mode) {
                case Integer.MIN_VALUE:
                    return Math.min(size, Math.max(desired, min));
                case 1073741824:
                    return size;
                default:
                    return Math.max(desired, min);
            }
        }

        public void assertNotInLayoutOrScroll(String message) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertNotInLayoutOrScroll(message);
            }
        }

        @Deprecated
        public void setAutoMeasureEnabled(boolean enabled) {
            this.mAutoMeasure = enabled;
        }

        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public final void setItemPrefetchEnabled(boolean enabled) {
            if (enabled != this.mItemPrefetchEnabled) {
                this.mItemPrefetchEnabled = enabled;
                this.mPrefetchMaxCountObserved = 0;
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }

        public final boolean isItemPrefetchEnabled() {
            return this.mItemPrefetchEnabled;
        }

        public void collectAdjacentPrefetchPositions(int dx, int dy, State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public void collectInitialPrefetchPositions(int adapterItemCount, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        void dispatchAttachedToWindow(RecyclerView view) {
            this.mIsAttachedToWindow = true;
            onAttachedToWindow(view);
        }

        void dispatchDetachedFromWindow(RecyclerView view, Recycler recycler) {
            this.mIsAttachedToWindow = false;
            onDetachedFromWindow(view, recycler);
        }

        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }

        public void postOnAnimation(Runnable action) {
            if (this.mRecyclerView != null) {
                ViewCompat.postOnAnimation(this.mRecyclerView, action);
            }
        }

        public boolean removeCallbacks(Runnable action) {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.removeCallbacks(action);
            }
            return false;
        }

        public void onAttachedToWindow(RecyclerView view) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView view) {
        }

        public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
            onDetachedFromWindow(view);
        }

        public boolean getClipToPadding() {
            return this.mRecyclerView != null && this.mRecyclerView.mClipToPadding;
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e(RecyclerView.TAG, "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onLayoutCompleted(State state) {
        }

        public boolean checkLayoutParams(LayoutParams lp) {
            return lp != null;
        }

        public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof MarginLayoutParams) {
                return new LayoutParams((MarginLayoutParams) lp);
            }
            return new LayoutParams(lp);
        }

        public LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
            return new LayoutParams(c, attrs);
        }

        public int scrollHorizontallyBy(int dx, Recycler recycler, State state) {
            return 0;
        }

        public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
            return 0;
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public void scrollToPosition(int position) {
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            Log.e(RecyclerView.TAG, "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            if (!(this.mSmoothScroller == null || smoothScroller == this.mSmoothScroller || !this.mSmoothScroller.isRunning())) {
                this.mSmoothScroller.stop();
            }
            this.mSmoothScroller = smoothScroller;
            this.mSmoothScroller.start(this.mRecyclerView, this);
        }

        public boolean isSmoothScrolling() {
            return this.mSmoothScroller != null && this.mSmoothScroller.isRunning();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection(this.mRecyclerView);
        }

        public void endAnimation(View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }

        public void addDisappearingView(View child) {
            addDisappearingView(child, -1);
        }

        public void addDisappearingView(View child, int index) {
            addViewInt(child, index, true);
        }

        public void addView(View child) {
            addView(child, -1);
        }

        public void addView(View child, int index) {
            addViewInt(child, index, false);
        }

        private void addViewInt(View child, int index, boolean disappearing) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(child);
            if (disappearing || holder.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(holder);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(holder);
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (holder.wasReturnedFromScrap() || holder.isScrap()) {
                if (holder.isScrap()) {
                    holder.unScrap();
                } else {
                    holder.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(child, index, child.getLayoutParams(), false);
            } else if (child.getParent() == this.mRecyclerView) {
                int currentIndex = this.mChildHelper.indexOfChild(child);
                if (index == -1) {
                    index = this.mChildHelper.getChildCount();
                }
                if (currentIndex == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(child) + this.mRecyclerView.exceptionLabel());
                } else if (currentIndex != index) {
                    this.mRecyclerView.mLayout.moveView(currentIndex, index);
                }
            } else {
                this.mChildHelper.addView(child, index, false);
                lp.mInsetsDirty = true;
                if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                    this.mSmoothScroller.onChildAttachedToWindow(child);
                }
            }
            if (lp.mPendingInvalidate) {
                holder.itemView.invalidate();
                lp.mPendingInvalidate = false;
            }
        }

        public void removeView(View child) {
            this.mChildHelper.removeView(child);
        }

        public void removeViewAt(int index) {
            if (getChildAt(index) != null) {
                this.mChildHelper.removeViewAt(index);
            }
        }

        public void removeAllViews() {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                this.mChildHelper.removeViewAt(i);
            }
        }

        public int getBaseline() {
            return -1;
        }

        public int getPosition(View view) {
            return ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        }

        public int getItemViewType(View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }

        public View findContainingItemView(View view) {
            if (this.mRecyclerView == null) {
                return null;
            }
            View found = this.mRecyclerView.findContainingItemView(view);
            if (found == null) {
                return null;
            }
            if (this.mChildHelper.isHidden(found)) {
                return null;
            }
            return found;
        }

        public View findViewByPosition(int position) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh != null && vh.getLayoutPosition() == position && !vh.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !vh.isRemoved())) {
                    return child;
                }
            }
            return null;
        }

        public void detachView(View child) {
            int ind = this.mChildHelper.indexOfChild(child);
            if (ind >= 0) {
                detachViewInternal(ind, child);
            }
        }

        public void detachViewAt(int index) {
            detachViewInternal(index, getChildAt(index));
        }

        private void detachViewInternal(int index, View view) {
            this.mChildHelper.detachViewFromParent(index);
        }

        public void attachView(View child, int index, LayoutParams lp) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
            if (vh.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(vh);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(vh);
            }
            this.mChildHelper.attachViewToParent(child, index, lp, vh.isRemoved());
        }

        public void attachView(View child, int index) {
            attachView(child, index, (LayoutParams) child.getLayoutParams());
        }

        public void attachView(View child) {
            attachView(child, -1);
        }

        public void removeDetachedView(View child) {
            this.mRecyclerView.removeDetachedView(child, false);
        }

        public void moveView(int fromIndex, int toIndex) {
            View view = getChildAt(fromIndex);
            if (view == null) {
                throw new IllegalArgumentException("Cannot move a child from non-existing index:" + fromIndex + this.mRecyclerView.toString());
            }
            detachViewAt(fromIndex);
            attachView(view, toIndex);
        }

        public void detachAndScrapView(View child, Recycler recycler) {
            scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(child), child);
        }

        public void detachAndScrapViewAt(int index, Recycler recycler) {
            scrapOrRecycleView(recycler, index, getChildAt(index));
        }

        public void removeAndRecycleView(View child, Recycler recycler) {
            removeView(child);
            recycler.recycleView(child);
        }

        public void removeAndRecycleViewAt(int index, Recycler recycler) {
            View view = getChildAt(index);
            removeViewAt(index);
            recycler.recycleView(view);
        }

        public int getChildCount() {
            return this.mChildHelper != null ? this.mChildHelper.getChildCount() : 0;
        }

        public View getChildAt(int index) {
            return this.mChildHelper != null ? this.mChildHelper.getChildAt(index) : null;
        }

        public int getWidthMode() {
            return this.mWidthMode;
        }

        public int getHeightMode() {
            return this.mHeightMode;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getPaddingLeft() {
            return this.mRecyclerView != null ? this.mRecyclerView.getPaddingLeft() : 0;
        }

        public int getPaddingTop() {
            return this.mRecyclerView != null ? this.mRecyclerView.getPaddingTop() : 0;
        }

        public int getPaddingRight() {
            return this.mRecyclerView != null ? this.mRecyclerView.getPaddingRight() : 0;
        }

        public int getPaddingBottom() {
            return this.mRecyclerView != null ? this.mRecyclerView.getPaddingBottom() : 0;
        }

        public int getPaddingStart() {
            return this.mRecyclerView != null ? ViewCompat.getPaddingStart(this.mRecyclerView) : 0;
        }

        public int getPaddingEnd() {
            return this.mRecyclerView != null ? ViewCompat.getPaddingEnd(this.mRecyclerView) : 0;
        }

        public boolean isFocused() {
            return this.mRecyclerView != null && this.mRecyclerView.isFocused();
        }

        public boolean hasFocus() {
            return this.mRecyclerView != null && this.mRecyclerView.hasFocus();
        }

        public View getFocusedChild() {
            if (this.mRecyclerView == null) {
                return null;
            }
            View focused = this.mRecyclerView.getFocusedChild();
            if (focused == null || this.mChildHelper.isHidden(focused)) {
                return null;
            }
            return focused;
        }

        public int getItemCount() {
            Adapter a = this.mRecyclerView != null ? this.mRecyclerView.getAdapter() : null;
            return a != null ? a.getItemCount() : 0;
        }

        public void offsetChildrenHorizontal(int dx) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenHorizontal(dx);
            }
        }

        public void offsetChildrenVertical(int dy) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenVertical(dy);
            }
        }

        public void ignoreView(View view) {
            if (view.getParent() != this.mRecyclerView || this.mRecyclerView.indexOfChild(view) == -1) {
                throw new IllegalArgumentException("View should be fully attached to be ignored" + this.mRecyclerView.exceptionLabel());
            }
            ViewHolder vh = RecyclerView.getChildViewHolderInt(view);
            vh.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder(vh);
        }

        public void stopIgnoringView(View view) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(view);
            vh.stopIgnoring();
            vh.resetInternal();
            vh.addFlags(4);
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                scrapOrRecycleView(recycler, i, getChildAt(i));
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int index, View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (!viewHolder.shouldIgnore()) {
                if (!viewHolder.isInvalid() || viewHolder.isRemoved() || this.mRecyclerView.mAdapter.hasStableIds()) {
                    detachViewAt(index);
                    recycler.scrapView(view);
                    this.mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
                    return;
                }
                removeViewAt(index);
                recycler.recycleViewHolderInternal(viewHolder);
            }
        }

        void removeAndRecycleScrapInt(Recycler recycler) {
            int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; i--) {
                View scrap = recycler.getScrapViewAt(i);
                ViewHolder vh = RecyclerView.getChildViewHolderInt(scrap);
                if (!vh.shouldIgnore()) {
                    vh.setIsRecyclable(false);
                    if (vh.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrap, false);
                    }
                    if (this.mRecyclerView.mItemAnimator != null) {
                        this.mRecyclerView.mItemAnimator.endAnimation(vh);
                    }
                    vh.setIsRecyclable(true);
                    recycler.quickRecycleScrapView(scrap);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }

        public void measureChild(View child, int widthUsed, int heightUsed) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
            heightUsed += insets.top + insets.bottom;
            int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(), (getPaddingLeft() + getPaddingRight()) + (widthUsed + (insets.left + insets.right)), lp.width, canScrollHorizontally());
            int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(), (getPaddingTop() + getPaddingBottom()) + heightUsed, lp.height, canScrollVertically());
            if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
                child.measure(widthSpec, heightSpec);
            }
        }

        boolean shouldReMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return (this.mMeasurementCacheEnabled && isMeasurementUpToDate(child.getMeasuredWidth(), widthSpec, lp.width) && isMeasurementUpToDate(child.getMeasuredHeight(), heightSpec, lp.height)) ? false : true;
        }

        boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return (!child.isLayoutRequested() && this.mMeasurementCacheEnabled && isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width) && isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height)) ? false : true;
        }

        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }

        public void setMeasurementCacheEnabled(boolean measurementCacheEnabled) {
            this.mMeasurementCacheEnabled = measurementCacheEnabled;
        }

        private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
            int specMode = MeasureSpec.getMode(spec);
            int specSize = MeasureSpec.getSize(spec);
            if (dimension > 0 && childSize != dimension) {
                return false;
            }
            switch (specMode) {
                case Integer.MIN_VALUE:
                    if (specSize < childSize) {
                        return false;
                    }
                    return true;
                case 0:
                    return true;
                case 1073741824:
                    if (specSize != childSize) {
                        return false;
                    }
                    return true;
                default:
                    return false;
            }
        }

        public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
            heightUsed += insets.top + insets.bottom;
            int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(), (((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin) + (widthUsed + (insets.left + insets.right)), lp.width, canScrollHorizontally());
            int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(), (((getPaddingTop() + getPaddingBottom()) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height, canScrollVertically());
            if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
                child.measure(widthSpec, heightSpec);
            }
        }

        @Deprecated
        public static int getChildMeasureSpec(int parentSize, int padding, int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else {
                    resultSize = 0;
                    resultMode = 0;
                }
            } else if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = 1073741824;
            } else if (childDimension == -1) {
                resultSize = size;
                resultMode = 1073741824;
            } else if (childDimension == -2) {
                resultSize = size;
                resultMode = Integer.MIN_VALUE;
            }
            return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        public static int getChildMeasureSpec(int parentSize, int parentMode, int padding, int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else if (childDimension == -1) {
                    switch (parentMode) {
                        case Integer.MIN_VALUE:
                        case 1073741824:
                            resultSize = size;
                            resultMode = parentMode;
                            break;
                        case 0:
                            resultSize = 0;
                            resultMode = 0;
                            break;
                        default:
                            break;
                    }
                } else if (childDimension == -2) {
                    resultSize = 0;
                    resultMode = 0;
                }
            } else if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = 1073741824;
            } else if (childDimension == -1) {
                resultSize = size;
                resultMode = parentMode;
            } else if (childDimension == -2) {
                resultSize = size;
                resultMode = (parentMode == Integer.MIN_VALUE || parentMode == 1073741824) ? Integer.MIN_VALUE : 0;
            }
            return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        public int getDecoratedMeasuredWidth(View child) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return (child.getMeasuredWidth() + insets.left) + insets.right;
        }

        public int getDecoratedMeasuredHeight(View child) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return (child.getMeasuredHeight() + insets.top) + insets.bottom;
        }

        public void layoutDecorated(View child, int left, int top, int right, int bottom) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            child.layout(insets.left + left, insets.top + top, right - insets.right, bottom - insets.bottom);
        }

        public void layoutDecoratedWithMargins(View child, int left, int top, int right, int bottom) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = lp.mDecorInsets;
            child.layout((insets.left + left) + lp.leftMargin, (insets.top + top) + lp.topMargin, (right - insets.right) - lp.rightMargin, (bottom - insets.bottom) - lp.bottomMargin);
        }

        public void getTransformedBoundingBox(View child, boolean includeDecorInsets, Rect out) {
            if (includeDecorInsets) {
                Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
                out.set(-insets.left, -insets.top, child.getWidth() + insets.right, child.getHeight() + insets.bottom);
            } else {
                out.set(0, 0, child.getWidth(), child.getHeight());
            }
            if (this.mRecyclerView != null) {
                Matrix childMatrix = child.getMatrix();
                if (!(childMatrix == null || childMatrix.isIdentity())) {
                    RectF tempRectF = this.mRecyclerView.mTempRectF;
                    tempRectF.set(out);
                    childMatrix.mapRect(tempRectF);
                    out.set((int) Math.floor((double) tempRectF.left), (int) Math.floor((double) tempRectF.top), (int) Math.ceil((double) tempRectF.right), (int) Math.ceil((double) tempRectF.bottom));
                }
            }
            out.offset(child.getLeft(), child.getTop());
        }

        public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
            RecyclerView.getDecoratedBoundsWithMarginsInt(view, outBounds);
        }

        public int getDecoratedLeft(View child) {
            return child.getLeft() - getLeftDecorationWidth(child);
        }

        public int getDecoratedTop(View child) {
            return child.getTop() - getTopDecorationHeight(child);
        }

        public int getDecoratedRight(View child) {
            return child.getRight() + getRightDecorationWidth(child);
        }

        public int getDecoratedBottom(View child) {
            return child.getBottom() + getBottomDecorationHeight(child);
        }

        public void calculateItemDecorationsForChild(View child, Rect outRect) {
            if (this.mRecyclerView == null) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(this.mRecyclerView.getItemDecorInsetsForChild(child));
            }
        }

        public int getTopDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.top;
        }

        public int getBottomDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.bottom;
        }

        public int getLeftDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.left;
        }

        public int getRightDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.right;
        }

        public View onFocusSearchFailed(View focused, int direction, Recycler recycler, State state) {
            return null;
        }

        public View onInterceptFocusSearch(View focused, int direction) {
            return null;
        }

        private int[] getChildRectangleOnScreenScrollAmount(RecyclerView parent, View child, Rect rect, boolean immediate) {
            int dx;
            int dy;
            int[] out = new int[2];
            int parentLeft = getPaddingLeft();
            int parentTop = getPaddingTop();
            int parentRight = getWidth() - getPaddingRight();
            int parentBottom = getHeight() - getPaddingBottom();
            int childLeft = (child.getLeft() + rect.left) - child.getScrollX();
            int childTop = (child.getTop() + rect.top) - child.getScrollY();
            int childRight = childLeft + rect.width();
            int childBottom = childTop + rect.height();
            int offScreenLeft = Math.min(0, childLeft - parentLeft);
            int offScreenTop = Math.min(0, childTop - parentTop);
            int offScreenRight = Math.max(0, childRight - parentRight);
            int offScreenBottom = Math.max(0, childBottom - parentBottom);
            if (getLayoutDirection() == 1) {
                if (offScreenRight != 0) {
                    dx = offScreenRight;
                } else {
                    dx = Math.max(offScreenLeft, childRight - parentRight);
                }
            } else if (offScreenLeft != 0) {
                dx = offScreenLeft;
            } else {
                dx = Math.min(childLeft - parentLeft, offScreenRight);
            }
            if (offScreenTop != 0) {
                dy = offScreenTop;
            } else {
                dy = Math.min(childTop - parentTop, offScreenBottom);
            }
            out[0] = dx;
            out[1] = dy;
            return out;
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
            return requestChildRectangleOnScreen(parent, child, rect, immediate, false);
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
            int[] scrollAmount = getChildRectangleOnScreenScrollAmount(parent, child, rect, immediate);
            int dx = scrollAmount[0];
            int dy = scrollAmount[1];
            if (focusedChildVisible && !isFocusedChildVisibleAfterScrolling(parent, dx, dy)) {
                return false;
            }
            if (dx == 0 && dy == 0) {
                return false;
            }
            if (immediate) {
                parent.scrollBy(dx, dy);
            } else {
                parent.smoothScrollBy(dx, dy);
            }
            return true;
        }

        public boolean isViewPartiallyVisible(View child, boolean completelyVisible, boolean acceptEndPointInclusion) {
            boolean isViewFullyVisible;
            boolean z = true;
            if (this.mHorizontalBoundCheck.isViewWithinBoundFlags(child, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(child, 24579)) {
                isViewFullyVisible = true;
            } else {
                isViewFullyVisible = false;
            }
            if (completelyVisible) {
                return isViewFullyVisible;
            }
            if (isViewFullyVisible) {
                z = false;
            }
            return z;
        }

        private boolean isFocusedChildVisibleAfterScrolling(RecyclerView parent, int dx, int dy) {
            View focusedChild = parent.getFocusedChild();
            if (focusedChild == null) {
                return false;
            }
            int parentLeft = getPaddingLeft();
            int parentTop = getPaddingTop();
            int parentRight = getWidth() - getPaddingRight();
            int parentBottom = getHeight() - getPaddingBottom();
            Rect bounds = this.mRecyclerView.mTempRect;
            getDecoratedBoundsWithMargins(focusedChild, bounds);
            if (bounds.left - dx >= parentRight || bounds.right - dx <= parentLeft || bounds.top - dy >= parentBottom || bounds.bottom - dy <= parentTop) {
                return false;
            }
            return true;
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView parent, View child, View focused) {
            return isSmoothScrolling() || parent.isComputingLayout();
        }

        public boolean onRequestChildFocus(RecyclerView parent, State state, View child, View focused) {
            return onRequestChildFocus(parent, child, focused);
        }

        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> arrayList, int direction, int focusableMode) {
            return false;
        }

        public void onItemsChanged(RecyclerView recyclerView) {
        }

        public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount, Object payload) {
            onItemsUpdated(recyclerView, positionStart, itemCount);
        }

        public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount) {
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
            this.mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
        }

        public void setMeasuredDimension(int widthSize, int heightSize) {
            this.mRecyclerView.setMeasuredDimension(widthSize, heightSize);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth(this.mRecyclerView);
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight(this.mRecyclerView);
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }

        void stopSmoothScroller() {
            if (this.mSmoothScroller != null) {
                this.mSmoothScroller.stop();
            }
        }

        private void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }

        public void onScrollStateChanged(int state) {
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                if (!RecyclerView.getChildViewHolderInt(getChildAt(i)).shouldIgnore()) {
                    removeAndRecycleViewAt(i, recycler);
                }
            }
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
            onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, info);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat info) {
            if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
                info.addAction(8192);
                info.setScrollable(true);
            }
            if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
                info.addAction(4096);
                info.setScrollable(true);
            }
            info.setCollectionInfo(CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state)));
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, event);
        }

        public void onInitializeAccessibilityEvent(Recycler recycler, State state, AccessibilityEvent event) {
            boolean z = true;
            if (this.mRecyclerView != null && event != null) {
                if (!(this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1) || this.mRecyclerView.canScrollHorizontally(1))) {
                    z = false;
                }
                event.setScrollable(z);
                if (this.mRecyclerView.mAdapter != null) {
                    event.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                }
            }
        }

        void onInitializeAccessibilityNodeInfoForItem(View host, AccessibilityNodeInfoCompat info) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(host);
            if (vh != null && !vh.isRemoved() && !this.mChildHelper.isHidden(vh.itemView)) {
                onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, host, info);
            }
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View host, AccessibilityNodeInfoCompat info) {
            int rowIndexGuess;
            int columnIndexGuess;
            if (canScrollVertically()) {
                rowIndexGuess = getPosition(host);
            } else {
                rowIndexGuess = 0;
            }
            if (canScrollHorizontally()) {
                columnIndexGuess = getPosition(host);
            } else {
                columnIndexGuess = 0;
            }
            info.setCollectionItemInfo(CollectionItemInfoCompat.obtain(rowIndexGuess, 1, columnIndexGuess, 1, false, false));
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return 0;
        }

        public int getRowCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !canScrollVertically()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !canScrollHorizontally()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        boolean performAccessibilityAction(int action, Bundle args) {
            return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, action, args);
        }

        public boolean performAccessibilityAction(Recycler recycler, State state, int action, Bundle args) {
            if (this.mRecyclerView == null) {
                return false;
            }
            int vScroll = 0;
            int hScroll = 0;
            switch (action) {
                case 4096:
                    if (this.mRecyclerView.canScrollVertically(1)) {
                        vScroll = (getHeight() - getPaddingTop()) - getPaddingBottom();
                    }
                    if (this.mRecyclerView.canScrollHorizontally(1)) {
                        hScroll = (getWidth() - getPaddingLeft()) - getPaddingRight();
                        break;
                    }
                    break;
                case 8192:
                    if (this.mRecyclerView.canScrollVertically(-1)) {
                        vScroll = -((getHeight() - getPaddingTop()) - getPaddingBottom());
                    }
                    if (this.mRecyclerView.canScrollHorizontally(-1)) {
                        hScroll = -((getWidth() - getPaddingLeft()) - getPaddingRight());
                        break;
                    }
                    break;
            }
            if (vScroll == 0 && hScroll == 0) {
                return false;
            }
            this.mRecyclerView.scrollBy(hScroll, vScroll);
            return true;
        }

        boolean performAccessibilityActionForItem(View view, int action, Bundle args) {
            return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, action, args);
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int action, Bundle args) {
            return false;
        }

        public static Properties getProperties(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            Properties properties = new Properties();
            TypedArray a = context.obtainStyledAttributes(attrs, C0270R.styleable.RecyclerView, defStyleAttr, defStyleRes);
            properties.orientation = a.getInt(C0270R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = a.getInt(C0270R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = a.getBoolean(C0270R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = a.getBoolean(C0270R.styleable.RecyclerView_stackFromEnd, false);
            a.recycle();
            return properties;
        }

        void setExactMeasureSpecsFrom(RecyclerView recyclerView) {
            setMeasureSpecs(MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }

        boolean shouldMeasureTwice() {
            return false;
        }

        boolean hasFlexibleChildInBothOrientations() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                android.view.ViewGroup.LayoutParams lp = getChildAt(i).getLayoutParams();
                if (lp.width < 0 && lp.height < 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction = new Action(0, 0);
        private boolean mRunning;
        private int mTargetPosition = -1;
        private View mTargetView;

        public interface ScrollVectorProvider {
            PointF computeScrollVectorForPosition(int i);
        }

        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean mChanged;
            private int mConsecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;

            public Action(int dx, int dy) {
                this(dx, dy, Integer.MIN_VALUE, null);
            }

            public Action(int dx, int dy, int duration) {
                this(dx, dy, duration, null);
            }

            public Action(int dx, int dy, int duration, Interpolator interpolator) {
                this.mJumpToPosition = -1;
                this.mChanged = false;
                this.mConsecutiveUpdates = 0;
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
            }

            public void jumpTo(int targetPosition) {
                this.mJumpToPosition = targetPosition;
            }

            boolean hasJumpTarget() {
                return this.mJumpToPosition >= 0;
            }

            void runIfNecessary(RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    int position = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(position);
                    this.mChanged = false;
                } else if (this.mChanged) {
                    validate();
                    if (this.mInterpolator != null) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    } else if (this.mDuration == Integer.MIN_VALUE) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                    } else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                    }
                    this.mConsecutiveUpdates++;
                    if (this.mConsecutiveUpdates > 10) {
                        Log.e(RecyclerView.TAG, "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.mChanged = false;
                } else {
                    this.mConsecutiveUpdates = 0;
                }
            }

            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                } else if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            public int getDx() {
                return this.mDx;
            }

            public void setDx(int dx) {
                this.mChanged = true;
                this.mDx = dx;
            }

            public int getDy() {
                return this.mDy;
            }

            public void setDy(int dy) {
                this.mChanged = true;
                this.mDy = dy;
            }

            public int getDuration() {
                return this.mDuration;
            }

            public void setDuration(int duration) {
                this.mChanged = true;
                this.mDuration = duration;
            }

            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }

            public void setInterpolator(Interpolator interpolator) {
                this.mChanged = true;
                this.mInterpolator = interpolator;
            }

            public void update(int dx, int dy, int duration, Interpolator interpolator) {
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
                this.mChanged = true;
            }
        }

        protected abstract void onSeekTargetStep(int i, int i2, State state, Action action);

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onTargetFound(View view, State state, Action action);

        void start(RecyclerView recyclerView, LayoutManager layoutManager) {
            this.mRecyclerView = recyclerView;
            this.mLayoutManager = layoutManager;
            if (this.mTargetPosition == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = findViewByPosition(getTargetPosition());
            onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
        }

        public void setTargetPosition(int targetPosition) {
            this.mTargetPosition = targetPosition;
        }

        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        protected final void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                onStop();
                this.mRecyclerView.mState.mTargetPosition = -1;
                this.mTargetView = null;
                this.mTargetPosition = -1;
                this.mPendingInitialRun = false;
                this.mLayoutManager.onSmoothScrollerStopped(this);
                this.mLayoutManager = null;
                this.mRecyclerView = null;
            }
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        private void onAnimation(int dx, int dy) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || recyclerView == null) {
                stop();
            }
            this.mPendingInitialRun = false;
            if (this.mTargetView != null) {
                if (getChildPosition(this.mTargetView) == this.mTargetPosition) {
                    onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(recyclerView);
                    stop();
                } else {
                    Log.e(RecyclerView.TAG, "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                onSeekTargetStep(dx, dy, recyclerView.mState, this.mRecyclingAction);
                boolean hadJumpTarget = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(recyclerView);
                if (!hadJumpTarget) {
                    return;
                }
                if (this.mRunning) {
                    this.mPendingInitialRun = true;
                    recyclerView.mViewFlinger.postOnAnimation();
                    return;
                }
                stop();
            }
        }

        public int getChildPosition(View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public View findViewByPosition(int position) {
            return this.mRecyclerView.mLayout.findViewByPosition(position);
        }

        @Deprecated
        public void instantScrollToPosition(int position) {
            this.mRecyclerView.scrollToPosition(position);
        }

        protected void onChildAttachedToWindow(View child) {
            if (getChildPosition(child) == getTargetPosition()) {
                this.mTargetView = child;
            }
        }

        protected void normalize(PointF scrollVector) {
            float magnitude = (float) Math.sqrt((double) ((scrollVector.x * scrollVector.x) + (scrollVector.y * scrollVector.y)));
            scrollVector.x /= magnitude;
            scrollVector.y /= magnitude;
        }
    }

    public static abstract class OnFlingListener {
        public abstract boolean onFling(int i, int i2);
    }

    /* renamed from: android.support.v7.widget.RecyclerView$1 */
    class C03231 implements Runnable {
        C03231() {
        }

        public void run() {
            if (RecyclerView.this.mFirstLayoutComplete && !RecyclerView.this.isLayoutRequested()) {
                if (!RecyclerView.this.mIsAttached) {
                    RecyclerView.this.requestLayout();
                } else if (RecyclerView.this.mLayoutFrozen) {
                    RecyclerView.this.mLayoutWasDefered = true;
                } else {
                    RecyclerView.this.consumePendingUpdateOperations();
                }
            }
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$3 */
    class C03253 implements Runnable {
        C03253() {
        }

        public void run() {
            if (RecyclerView.this.mItemAnimator != null) {
                RecyclerView.this.mItemAnimator.runPendingAnimations();
            }
            RecyclerView.this.mPostedAnimatorRunner = false;
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$4 */
    static class C03264 implements Interpolator {
        C03264() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$5 */
    class C03275 implements ProcessCallback {
        C03275() {
        }

        public void processDisappeared(ViewHolder viewHolder, ItemHolderInfo info, ItemHolderInfo postInfo) {
            RecyclerView.this.mRecycler.unscrapView(viewHolder);
            RecyclerView.this.animateDisappearance(viewHolder, info, postInfo);
        }

        public void processAppeared(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo info) {
            RecyclerView.this.animateAppearance(viewHolder, preInfo, info);
        }

        public void processPersistent(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo) {
            viewHolder.setIsRecyclable(false);
            if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                if (RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, preInfo, postInfo)) {
                    RecyclerView.this.postAnimationRunner();
                }
            } else if (RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, preInfo, postInfo)) {
                RecyclerView.this.postAnimationRunner();
            }
        }

        public void unused(ViewHolder viewHolder) {
            RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$6 */
    class C03286 implements Callback {
        C03286() {
        }

        public int getChildCount() {
            return RecyclerView.this.getChildCount();
        }

        public void addView(View child, int index) {
            RecyclerView.this.addView(child, index);
            RecyclerView.this.dispatchChildAttached(child);
        }

        public int indexOfChild(View view) {
            return RecyclerView.this.indexOfChild(view);
        }

        public void removeViewAt(int index) {
            View child = RecyclerView.this.getChildAt(index);
            if (child != null) {
                RecyclerView.this.dispatchChildDetached(child);
                child.clearAnimation();
            }
            RecyclerView.this.removeViewAt(index);
        }

        public View getChildAt(int offset) {
            return RecyclerView.this.getChildAt(offset);
        }

        public void removeAllViews() {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                RecyclerView.this.dispatchChildDetached(child);
                child.clearAnimation();
            }
            RecyclerView.this.removeAllViews();
        }

        public ViewHolder getChildViewHolder(View view) {
            return RecyclerView.getChildViewHolderInt(view);
        }

        public void attachViewToParent(View child, int index, android.view.ViewGroup.LayoutParams layoutParams) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
            if (vh != null) {
                if (vh.isTmpDetached() || vh.shouldIgnore()) {
                    vh.clearTmpDetachFlag();
                } else {
                    throw new IllegalArgumentException("Called attach on a child which is not detached: " + vh + RecyclerView.this.exceptionLabel());
                }
            }
            RecyclerView.this.attachViewToParent(child, index, layoutParams);
        }

        public void detachViewFromParent(int offset) {
            View view = getChildAt(offset);
            if (view != null) {
                ViewHolder vh = RecyclerView.getChildViewHolderInt(view);
                if (vh != null) {
                    if (!vh.isTmpDetached() || vh.shouldIgnore()) {
                        vh.addFlags(256);
                    } else {
                        throw new IllegalArgumentException("called detach on an already detached child " + vh + RecyclerView.this.exceptionLabel());
                    }
                }
            }
            RecyclerView.this.detachViewFromParent(offset);
        }

        public void onEnteredHiddenState(View child) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
            if (vh != null) {
                vh.onEnteredHiddenState(RecyclerView.this);
            }
        }

        public void onLeftHiddenState(View child) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
            if (vh != null) {
                vh.onLeftHiddenState(RecyclerView.this);
            }
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$7 */
    class C03297 implements Callback {
        C03297() {
        }

        public ViewHolder findViewHolder(int position) {
            ViewHolder vh = RecyclerView.this.findViewHolderForPosition(position, true);
            if (vh == null) {
                return null;
            }
            if (RecyclerView.this.mChildHelper.isHidden(vh.itemView)) {
                return null;
            }
            return vh;
        }

        public void offsetPositionsForRemovingInvisible(int start, int count) {
            RecyclerView.this.offsetPositionRecordsForRemove(start, count, true);
            RecyclerView.this.mItemsAddedOrRemoved = true;
            State state = RecyclerView.this.mState;
            state.mDeletedInvisibleItemCountSincePreviousLayout += count;
        }

        public void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount) {
            RecyclerView.this.offsetPositionRecordsForRemove(positionStart, itemCount, false);
            RecyclerView.this.mItemsAddedOrRemoved = true;
        }

        public void markViewHoldersUpdated(int positionStart, int itemCount, Object payload) {
            RecyclerView.this.viewRangeUpdate(positionStart, itemCount, payload);
            RecyclerView.this.mItemsChanged = true;
        }

        public void onDispatchFirstPass(UpdateOp op) {
            dispatchUpdate(op);
        }

        void dispatchUpdate(UpdateOp op) {
            switch (op.cmd) {
                case 1:
                    RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, op.positionStart, op.itemCount);
                    return;
                case 2:
                    RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, op.positionStart, op.itemCount);
                    return;
                case 4:
                    RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, op.positionStart, op.itemCount, op.payload);
                    return;
                case 8:
                    RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, op.positionStart, op.itemCount, 1);
                    return;
                default:
                    return;
            }
        }

        public void onDispatchSecondPass(UpdateOp op) {
            dispatchUpdate(op);
        }

        public void offsetPositionsForAdd(int positionStart, int itemCount) {
            RecyclerView.this.offsetPositionRecordsForInsert(positionStart, itemCount);
            RecyclerView.this.mItemsAddedOrRemoved = true;
        }

        public void offsetPositionsForMove(int from, int to) {
            RecyclerView.this.offsetPositionRecordsForMove(from, to);
            RecyclerView.this.mItemsAddedOrRemoved = true;
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$8 */
    class C03308 implements AnimatorUpdateListener {
        C03308() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            try {
                RecyclerView.this.mGoToTopImage.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: android.support.v7.widget.RecyclerView$9 */
    class C03319 implements AnimatorUpdateListener {
        C03319() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            try {
                RecyclerView.this.mGoToTopImage.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                RecyclerView.this.invalidate();
            } catch (Exception e) {
            }
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            return !this.mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount, null);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        }
    }

    public interface ChildDrawingOrderCallback {
        int onGetChildDrawingOrder(int i, int i2);
    }

    private class ItemAnimatorRestoreListener implements ItemAnimatorListener {
        ItemAnimatorRestoreListener() {
        }

        public void onAnimationFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.mShadowedHolder != null && item.mShadowingHolder == null) {
                item.mShadowedHolder = null;
            }
            item.mShadowingHolder = null;
            if (!item.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(item.itemView) && item.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(item.itemView, false);
            }
        }
    }

    public interface OnChildAttachStateChangeListener {
        void onChildViewAttachedToWindow(View view);

        void onChildViewDetachedFromWindow(View view);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount = 0;
        SparseArray<ScrapData> mScrap = new SparseArray();

        static class ScrapData {
            long mBindRunningAverageNs = 0;
            long mCreateRunningAverageNs = 0;
            int mMaxScrap = 5;
            final ArrayList<ViewHolder> mScrapHeap = new ArrayList();

            ScrapData() {
            }
        }

        public void clear() {
            for (int i = 0; i < this.mScrap.size(); i++) {
                ((ScrapData) this.mScrap.valueAt(i)).mScrapHeap.clear();
            }
        }

        public void setMaxRecycledViews(int viewType, int max) {
            ScrapData scrapData = getScrapDataForType(viewType);
            scrapData.mMaxScrap = max;
            ArrayList<ViewHolder> scrapHeap = scrapData.mScrapHeap;
            while (scrapHeap.size() > max) {
                scrapHeap.remove(scrapHeap.size() - 1);
            }
        }

        public int getRecycledViewCount(int viewType) {
            return getScrapDataForType(viewType).mScrapHeap.size();
        }

        public ViewHolder getRecycledView(int viewType) {
            ScrapData scrapData = (ScrapData) this.mScrap.get(viewType);
            if (scrapData == null || scrapData.mScrapHeap.isEmpty()) {
                return null;
            }
            ArrayList<ViewHolder> scrapHeap = scrapData.mScrapHeap;
            return (ViewHolder) scrapHeap.remove(scrapHeap.size() - 1);
        }

        int size() {
            int count = 0;
            for (int i = 0; i < this.mScrap.size(); i++) {
                ArrayList<ViewHolder> viewHolders = ((ScrapData) this.mScrap.valueAt(i)).mScrapHeap;
                if (viewHolders != null) {
                    count += viewHolders.size();
                }
            }
            return count;
        }

        public void putRecycledView(ViewHolder scrap) {
            int viewType = scrap.getItemViewType();
            ArrayList<ViewHolder> scrapHeap = getScrapDataForType(viewType).mScrapHeap;
            if (((ScrapData) this.mScrap.get(viewType)).mMaxScrap > scrapHeap.size()) {
                scrap.resetInternal();
                scrapHeap.add(scrap);
            }
        }

        long runningAverage(long oldAverage, long newValue) {
            return oldAverage == 0 ? newValue : ((oldAverage / 4) * 3) + (newValue / 4);
        }

        void factorInCreateTime(int viewType, long createTimeNs) {
            ScrapData scrapData = getScrapDataForType(viewType);
            scrapData.mCreateRunningAverageNs = runningAverage(scrapData.mCreateRunningAverageNs, createTimeNs);
        }

        void factorInBindTime(int viewType, long bindTimeNs) {
            ScrapData scrapData = getScrapDataForType(viewType);
            scrapData.mBindRunningAverageNs = runningAverage(scrapData.mBindRunningAverageNs, bindTimeNs);
        }

        boolean willCreateInTime(int viewType, long approxCurrentNs, long deadlineNs) {
            long expectedDurationNs = getScrapDataForType(viewType).mCreateRunningAverageNs;
            return expectedDurationNs == 0 || approxCurrentNs + expectedDurationNs < deadlineNs;
        }

        boolean willBindInTime(int viewType, long approxCurrentNs, long deadlineNs) {
            long expectedDurationNs = getScrapDataForType(viewType).mBindRunningAverageNs;
            return expectedDurationNs == 0 || approxCurrentNs + expectedDurationNs < deadlineNs;
        }

        void attach(Adapter adapter) {
            this.mAttachCount++;
        }

        void detach() {
            this.mAttachCount--;
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            if (oldAdapter != null) {
                detach();
            }
            if (!compatibleWithPrevious && this.mAttachCount == 0) {
                clear();
            }
            if (newAdapter != null) {
                attach(newAdapter);
            }
        }

        private ScrapData getScrapDataForType(int viewType) {
            ScrapData scrapData = (ScrapData) this.mScrap.get(viewType);
            if (scrapData != null) {
                return scrapData;
            }
            scrapData = new ScrapData();
            this.mScrap.put(viewType, scrapData);
            return scrapData;
        }
    }

    public final class Recycler {
        static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList();
        final ArrayList<ViewHolder> mCachedViews = new ArrayList();
        ArrayList<ViewHolder> mChangedScrap = null;
        RecycledViewPool mRecyclerPool;
        private int mRequestedCacheMax = 2;
        private final List<ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
        private ViewCacheExtension mViewCacheExtension;
        int mViewCacheMax = 2;

        public void clear() {
            this.mAttachedScrap.clear();
            recycleAndClearCachedViews();
        }

        public void setViewCacheSize(int viewCount) {
            this.mRequestedCacheMax = viewCount;
            updateViewCacheSize();
        }

        void updateViewCacheSize() {
            this.mViewCacheMax = this.mRequestedCacheMax + (RecyclerView.this.mLayout != null ? RecyclerView.this.mLayout.mPrefetchMaxCountObserved : 0);
            for (int i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > this.mViewCacheMax; i--) {
                recycleCachedViewAt(i);
            }
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        boolean validateViewHolderForOffsetPosition(ViewHolder holder) {
            if (holder.isRemoved()) {
                return RecyclerView.this.mState.isPreLayout();
            }
            if (holder.mPosition < 0 || holder.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + holder + RecyclerView.this.exceptionLabel());
            } else if (!RecyclerView.this.mState.isPreLayout() && RecyclerView.this.mAdapter.getItemViewType(holder.mPosition) != holder.getItemViewType()) {
                return false;
            } else {
                if (!RecyclerView.this.mAdapter.hasStableIds() || holder.getItemId() == RecyclerView.this.mAdapter.getItemId(holder.mPosition)) {
                    return true;
                }
                return false;
            }
        }

        private boolean tryBindViewHolderByDeadline(ViewHolder holder, int offsetPosition, int position, long deadlineNs) {
            holder.mOwnerRecyclerView = RecyclerView.this;
            int viewType = holder.getItemViewType();
            long startBindNs = RecyclerView.this.getNanoTime();
            if (deadlineNs != RecyclerView.FOREVER_NS && !this.mRecyclerPool.willBindInTime(viewType, startBindNs, deadlineNs)) {
                return false;
            }
            RecyclerView.this.mAdapter.bindViewHolder(holder, offsetPosition);
            this.mRecyclerPool.factorInBindTime(holder.getItemViewType(), RecyclerView.this.getNanoTime() - startBindNs);
            attachAccessibilityDelegateOnBind(holder);
            if (RecyclerView.this.mState.isPreLayout()) {
                holder.mPreLayoutPosition = position;
            }
            return true;
        }

        public void bindViewToPosition(View view, int position) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (holder == null) {
                throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter" + RecyclerView.this.exceptionLabel());
            }
            int offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
            if (offsetPosition < 0 || offsetPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + position + "(offset:" + offsetPosition + ").state:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
            }
            LayoutParams rvLayoutParams;
            tryBindViewHolderByDeadline(holder, offsetPosition, position, RecyclerView.FOREVER_NS);
            android.view.ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp == null) {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else if (RecyclerView.this.checkLayoutParams(lp)) {
                rvLayoutParams = (LayoutParams) lp;
            } else {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateLayoutParams(lp);
                holder.itemView.setLayoutParams(rvLayoutParams);
            }
            rvLayoutParams.mInsetsDirty = true;
            rvLayoutParams.mViewHolder = holder;
            rvLayoutParams.mPendingInvalidate = holder.itemView.getParent() == null;
        }

        public int convertPreLayoutPositionToPostLayout(int position) {
            if (position >= 0 && position < RecyclerView.this.mState.getItemCount()) {
                return !RecyclerView.this.mState.isPreLayout() ? position : RecyclerView.this.mAdapterHelper.findPositionOffset(position);
            } else {
                throw new IndexOutOfBoundsException("invalid position " + position + ". State item count is " + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
            }
        }

        public View getViewForPosition(int position) {
            return getViewForPosition(position, false);
        }

        View getViewForPosition(int position, boolean dryRun) {
            return tryGetViewHolderForPositionByDeadline(position, dryRun, RecyclerView.FOREVER_NS).itemView;
        }

        ViewHolder tryGetViewHolderForPositionByDeadline(int position, boolean dryRun, long deadlineNs) {
            if (position < 0 || position >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + position + "(" + position + "). Item count:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
            }
            ViewHolder holder;
            LayoutParams rvLayoutParams;
            boolean fromScrapOrHiddenOrCache = false;
            ViewHolder holder2 = null;
            if (RecyclerView.this.mState.isPreLayout()) {
                holder2 = getChangedScrapViewForPosition(position);
                fromScrapOrHiddenOrCache = holder2 != null;
            }
            if (holder2 == null) {
                holder2 = getScrapOrHiddenOrCachedHolderForPosition(position, dryRun);
                if (holder2 != null) {
                    if (validateViewHolderForOffsetPosition(holder2)) {
                        fromScrapOrHiddenOrCache = true;
                    } else {
                        if (!dryRun) {
                            holder2.addFlags(4);
                            if (holder2.isScrap()) {
                                RecyclerView.this.removeDetachedView(holder2.itemView, false);
                                holder2.unScrap();
                            } else if (holder2.wasReturnedFromScrap()) {
                                holder2.clearReturnedFromScrapFlag();
                            }
                            recycleViewHolderInternal(holder2);
                        }
                        holder2 = null;
                    }
                }
            }
            if (holder2 == null) {
                int offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
                if (offsetPosition < 0 || offsetPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + position + "(offset:" + offsetPosition + ").state:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
                }
                int type = RecyclerView.this.mAdapter.getItemViewType(offsetPosition);
                if (RecyclerView.this.mAdapter.hasStableIds()) {
                    holder2 = getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(offsetPosition), type, dryRun);
                    if (holder2 != null) {
                        holder2.mPosition = offsetPosition;
                        fromScrapOrHiddenOrCache = true;
                    }
                }
                if (holder2 == null && this.mViewCacheExtension != null) {
                    View view = this.mViewCacheExtension.getViewForPositionAndType(this, position, type);
                    if (view != null) {
                        holder2 = RecyclerView.this.getChildViewHolder(view);
                        if (holder2 == null) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder" + RecyclerView.this.exceptionLabel());
                        } else if (holder2.shouldIgnore()) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view." + RecyclerView.this.exceptionLabel());
                        }
                    }
                }
                if (holder2 == null) {
                    holder2 = getRecycledViewPool().getRecycledView(type);
                    if (holder2 != null) {
                        holder2.resetInternal();
                        if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                            invalidateDisplayListInt(holder2);
                        }
                    }
                }
                holder = holder2;
                if (holder == null) {
                    long start = RecyclerView.this.getNanoTime();
                    if (deadlineNs != RecyclerView.FOREVER_NS && !this.mRecyclerPool.willCreateInTime(type, start, deadlineNs)) {
                        return null;
                    }
                    holder2 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, type);
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        RecyclerView innerView = RecyclerView.findNestedRecyclerView(holder2.itemView);
                        if (innerView != null) {
                            holder2.mNestedRecyclerView = new WeakReference(innerView);
                        }
                    }
                    this.mRecyclerPool.factorInCreateTime(type, RecyclerView.this.getNanoTime() - start);
                } else {
                    holder2 = holder;
                }
            }
            if (fromScrapOrHiddenOrCache && !RecyclerView.this.mState.isPreLayout() && holder2.hasAnyOfTheFlags(8192)) {
                holder2.setFlags(0, 8192);
                if (RecyclerView.this.mState.mRunSimpleAnimations) {
                    RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(holder2, RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, holder2, ItemAnimator.buildAdapterChangeFlagsForAnimations(holder2) | 4096, holder2.getUnmodifiedPayloads()));
                }
            }
            boolean bound = false;
            if (RecyclerView.this.mState.isPreLayout() && holder2.isBound()) {
                holder2.mPreLayoutPosition = position;
            } else if (!holder2.isBound() || holder2.needsUpdate() || holder2.isInvalid()) {
                bound = tryBindViewHolderByDeadline(holder2, RecyclerView.this.mAdapterHelper.findPositionOffset(position), position, deadlineNs);
            }
            android.view.ViewGroup.LayoutParams lp = holder2.itemView.getLayoutParams();
            if (lp == null) {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                holder2.itemView.setLayoutParams(rvLayoutParams);
            } else if (RecyclerView.this.checkLayoutParams(lp)) {
                rvLayoutParams = (LayoutParams) lp;
            } else {
                android.view.ViewGroup.LayoutParams rvLayoutParams2 = (LayoutParams) RecyclerView.this.generateLayoutParams(lp);
                holder2.itemView.setLayoutParams(rvLayoutParams2);
            }
            rvLayoutParams.mViewHolder = holder2;
            boolean z = fromScrapOrHiddenOrCache && bound;
            rvLayoutParams.mPendingInvalidate = z;
            holder = holder2;
            return holder2;
        }

        private void attachAccessibilityDelegateOnBind(ViewHolder holder) {
            if (RecyclerView.this.isAccessibilityEnabled()) {
                View itemView = holder.itemView;
                if (ViewCompat.getImportantForAccessibility(itemView) == 0) {
                    ViewCompat.setImportantForAccessibility(itemView, 1);
                }
                if (RecyclerView.this.mAccessibilityDelegate == null) {
                    RecyclerView.this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(RecyclerView.this));
                    Log.d(RecyclerView.TAG, "attachAccessibilityDelegate: mAccessibilityDelegate is null, so re create");
                }
                if (RecyclerView.this.mAccessibilityDelegate != null && !ViewCompat.hasAccessibilityDelegate(itemView)) {
                    holder.addFlags(16384);
                    ViewCompat.setAccessibilityDelegate(itemView, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
                }
            }
        }

        private void invalidateDisplayListInt(ViewHolder holder) {
            if (holder.itemView instanceof ViewGroup) {
                invalidateDisplayListInt((ViewGroup) holder.itemView, false);
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean invalidateThis) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    invalidateDisplayListInt((ViewGroup) view, true);
                }
            }
            if (!invalidateThis) {
                return;
            }
            if (viewGroup.getVisibility() == 4) {
                viewGroup.setVisibility(0);
                viewGroup.setVisibility(4);
                return;
            }
            int visibility = viewGroup.getVisibility();
            viewGroup.setVisibility(4);
            viewGroup.setVisibility(visibility);
        }

        public void recycleView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (holder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (holder.isScrap()) {
                holder.unScrap();
            } else if (holder.wasReturnedFromScrap()) {
                holder.clearReturnedFromScrapFlag();
            }
            recycleViewHolderInternal(holder);
        }

        void recycleViewInternal(View view) {
            recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }

        void recycleAndClearCachedViews() {
            for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
                recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
            if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
            }
        }

        void recycleCachedViewAt(int cachedViewIndex) {
            addViewHolderToRecycledViewPool((ViewHolder) this.mCachedViews.get(cachedViewIndex), true);
            this.mCachedViews.remove(cachedViewIndex);
        }

        void recycleViewHolderInternal(ViewHolder holder) {
            boolean z = false;
            if (holder.isScrap() || holder.itemView.getParent() != null) {
                StringBuilder append = new StringBuilder().append("Scrapped or attached views may not be recycled. isScrap:").append(holder.isScrap()).append(" isAttached:");
                if (holder.itemView.getParent() != null) {
                    z = true;
                }
                throw new IllegalArgumentException(append.append(z).append(RecyclerView.this.exceptionLabel()).toString());
            } else if (holder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + holder + RecyclerView.this.exceptionLabel());
            } else if (holder.shouldIgnore()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle." + RecyclerView.this.exceptionLabel());
            } else {
                boolean forceRecycle;
                boolean transientStatePreventsRecycling = holder.doesTransientStatePreventRecycling();
                if (RecyclerView.this.mAdapter != null && transientStatePreventsRecycling && RecyclerView.this.mAdapter.onFailedToRecycleView(holder)) {
                    forceRecycle = true;
                } else {
                    forceRecycle = false;
                }
                boolean cached = false;
                boolean recycled = false;
                if (forceRecycle || holder.isRecyclable()) {
                    if (this.mViewCacheMax > 0 && !holder.hasAnyOfTheFlags(526)) {
                        int cachedViewSize = this.mCachedViews.size();
                        if (cachedViewSize >= this.mViewCacheMax && cachedViewSize > 0) {
                            recycleCachedViewAt(0);
                            cachedViewSize--;
                        }
                        int targetCacheIndex = cachedViewSize;
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK && cachedViewSize > 0 && !RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(holder.mPosition)) {
                            int cacheIndex = cachedViewSize - 1;
                            while (cacheIndex >= 0) {
                                if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(((ViewHolder) this.mCachedViews.get(cacheIndex)).mPosition)) {
                                    break;
                                }
                                cacheIndex--;
                            }
                            targetCacheIndex = cacheIndex + 1;
                        }
                        this.mCachedViews.add(targetCacheIndex, holder);
                        cached = true;
                    }
                    if (!cached) {
                        addViewHolderToRecycledViewPool(holder, true);
                        recycled = true;
                    }
                }
                RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
                if (!cached && !recycled && transientStatePreventsRecycling) {
                    holder.mOwnerRecyclerView = null;
                }
            }
        }

        void addViewHolderToRecycledViewPool(ViewHolder holder, boolean dispatchRecycled) {
            RecyclerView.clearNestedRecyclerViewIfNotNested(holder);
            if (holder.hasAnyOfTheFlags(16384)) {
                holder.setFlags(0, 16384);
                ViewCompat.setAccessibilityDelegate(holder.itemView, null);
            }
            if (dispatchRecycled) {
                dispatchViewRecycled(holder);
            }
            holder.mOwnerRecyclerView = null;
            getRecycledViewPool().putRecycledView(holder);
        }

        void quickRecycleScrapView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
            recycleViewHolderInternal(holder);
        }

        void scrapView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (!holder.hasAnyOfTheFlags(12) && holder.isUpdated() && !RecyclerView.this.canReuseUpdatedViewHolder(holder)) {
                if (this.mChangedScrap == null) {
                    this.mChangedScrap = new ArrayList();
                }
                holder.setScrapContainer(this, true);
                this.mChangedScrap.add(holder);
            } else if (!holder.isInvalid() || holder.isRemoved() || RecyclerView.this.mAdapter.hasStableIds()) {
                holder.setScrapContainer(this, false);
                this.mAttachedScrap.add(holder);
            } else {
                throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool." + RecyclerView.this.exceptionLabel());
            }
        }

        void unscrapView(ViewHolder holder) {
            if (holder.mInChangeScrap) {
                this.mChangedScrap.remove(holder);
            } else {
                this.mAttachedScrap.remove(holder);
            }
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
        }

        int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        View getScrapViewAt(int index) {
            return ((ViewHolder) this.mAttachedScrap.get(index)).itemView;
        }

        void clearScrap() {
            this.mAttachedScrap.clear();
            if (this.mChangedScrap != null) {
                this.mChangedScrap.clear();
            }
        }

        ViewHolder getChangedScrapViewForPosition(int position) {
            if (this.mChangedScrap != null) {
                int changedScrapSize = this.mChangedScrap.size();
                if (changedScrapSize != 0) {
                    ViewHolder holder;
                    int i = 0;
                    while (i < changedScrapSize) {
                        holder = (ViewHolder) this.mChangedScrap.get(i);
                        if (holder.wasReturnedFromScrap() || holder.getLayoutPosition() != position) {
                            i++;
                        } else {
                            holder.addFlags(32);
                            return holder;
                        }
                    }
                    if (RecyclerView.this.mAdapter.hasStableIds()) {
                        int offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
                        if (offsetPosition > 0 && offsetPosition < RecyclerView.this.mAdapter.getItemCount()) {
                            long id = RecyclerView.this.mAdapter.getItemId(offsetPosition);
                            i = 0;
                            while (i < changedScrapSize) {
                                holder = (ViewHolder) this.mChangedScrap.get(i);
                                if (holder.wasReturnedFromScrap() || holder.getItemId() != id) {
                                    i++;
                                } else {
                                    holder.addFlags(32);
                                    return holder;
                                }
                            }
                        }
                    }
                    return null;
                }
            }
            return null;
        }

        ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int position, boolean dryRun) {
            int scrapCount = this.mAttachedScrap.size();
            int i = 0;
            while (i < scrapCount) {
                ViewHolder holder = (ViewHolder) this.mAttachedScrap.get(i);
                if (holder.wasReturnedFromScrap() || holder.getLayoutPosition() != position || holder.isInvalid() || (!RecyclerView.this.mState.mInPreLayout && holder.isRemoved())) {
                    i++;
                } else {
                    holder.addFlags(32);
                    return holder;
                }
            }
            if (!dryRun) {
                View view = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(position);
                if (view != null) {
                    ViewHolder vh = RecyclerView.getChildViewHolderInt(view);
                    RecyclerView.this.mChildHelper.unhide(view);
                    int layoutIndex = RecyclerView.this.mChildHelper.indexOfChild(view);
                    if (layoutIndex == -1) {
                        throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + vh + RecyclerView.this.exceptionLabel());
                    }
                    RecyclerView.this.mChildHelper.detachViewFromParent(layoutIndex);
                    scrapView(view);
                    vh.addFlags(8224);
                    return vh;
                }
            }
            int cacheSize = this.mCachedViews.size();
            i = 0;
            while (i < cacheSize) {
                holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder.isInvalid() || holder.getLayoutPosition() != position) {
                    i++;
                } else if (dryRun) {
                    return holder;
                } else {
                    this.mCachedViews.remove(i);
                    return holder;
                }
            }
            return null;
        }

        ViewHolder getScrapOrCachedViewForId(long id, int type, boolean dryRun) {
            int i;
            for (i = this.mAttachedScrap.size() - 1; i >= 0; i--) {
                ViewHolder holder = (ViewHolder) this.mAttachedScrap.get(i);
                if (holder.getItemId() == id && !holder.wasReturnedFromScrap()) {
                    if (type == holder.getItemViewType()) {
                        holder.addFlags(32);
                        if (!holder.isRemoved() || RecyclerView.this.mState.isPreLayout()) {
                            return holder;
                        }
                        holder.setFlags(2, 14);
                        return holder;
                    } else if (!dryRun) {
                        this.mAttachedScrap.remove(i);
                        RecyclerView.this.removeDetachedView(holder.itemView, false);
                        quickRecycleScrapView(holder.itemView);
                    }
                }
            }
            for (i = this.mCachedViews.size() - 1; i >= 0; i--) {
                holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder.getItemId() == id) {
                    if (type == holder.getItemViewType()) {
                        if (dryRun) {
                            return holder;
                        }
                        this.mCachedViews.remove(i);
                        return holder;
                    } else if (!dryRun) {
                        recycleCachedViewAt(i);
                        return null;
                    }
                }
            }
            return null;
        }

        void dispatchViewRecycled(ViewHolder holder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(holder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(holder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
            }
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            clear();
            getRecycledViewPool().onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious);
        }

        void offsetPositionRecordsForMove(int from, int to) {
            int inBetweenOffset;
            int start;
            int end;
            if (from < to) {
                start = from;
                end = to;
                inBetweenOffset = -1;
            } else {
                start = to;
                end = from;
                inBetweenOffset = 1;
            }
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder != null && holder.mPosition >= start && holder.mPosition <= end) {
                    if (holder.mPosition == from) {
                        holder.offsetPosition(to - from, false);
                    } else {
                        holder.offsetPosition(inBetweenOffset, false);
                    }
                }
            }
        }

        void offsetPositionRecordsForInsert(int insertedAt, int count) {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder != null && holder.mPosition >= insertedAt) {
                    holder.offsetPosition(count, true);
                }
            }
        }

        void offsetPositionRecordsForRemove(int removedFrom, int count, boolean applyToPreLayout) {
            int removedEnd = removedFrom + count;
            for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
                ViewHolder holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder != null) {
                    if (holder.mPosition >= removedEnd) {
                        holder.offsetPosition(-count, applyToPreLayout);
                    } else if (holder.mPosition >= removedFrom) {
                        holder.addFlags(8);
                        recycleCachedViewAt(i);
                    }
                }
            }
        }

        void setViewCacheExtension(ViewCacheExtension extension) {
            this.mViewCacheExtension = extension;
        }

        void setRecycledViewPool(RecycledViewPool pool) {
            if (this.mRecyclerPool != null) {
                this.mRecyclerPool.detach();
            }
            this.mRecyclerPool = pool;
            if (pool != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
            }
        }

        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }

        void viewRangeUpdate(int positionStart, int itemCount) {
            int positionEnd = positionStart + itemCount;
            for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
                ViewHolder holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder != null) {
                    int pos = holder.mPosition;
                    if (pos >= positionStart && pos < positionEnd) {
                        holder.addFlags(2);
                        recycleCachedViewAt(i);
                    }
                }
            }
        }

        void markKnownViewsInvalid() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = (ViewHolder) this.mCachedViews.get(i);
                if (holder != null) {
                    holder.addFlags(6);
                    holder.addChangePayload(null);
                }
            }
            if (RecyclerView.this.mAdapter == null || !RecyclerView.this.mAdapter.hasStableIds()) {
                recycleAndClearCachedViews();
            }
        }

        void clearOldPositions() {
            int i;
            int cachedCount = this.mCachedViews.size();
            for (i = 0; i < cachedCount; i++) {
                ((ViewHolder) this.mCachedViews.get(i)).clearOldPosition();
            }
            int scrapCount = this.mAttachedScrap.size();
            for (i = 0; i < scrapCount; i++) {
                ((ViewHolder) this.mAttachedScrap.get(i)).clearOldPosition();
            }
            if (this.mChangedScrap != null) {
                int changedScrapCount = this.mChangedScrap.size();
                for (i = 0; i < changedScrapCount; i++) {
                    ((ViewHolder) this.mChangedScrap.get(i)).clearOldPosition();
                }
            }
        }

        void markItemDecorInsetsDirty() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                LayoutParams layoutParams = (LayoutParams) ((ViewHolder) this.mCachedViews.get(i)).itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    private class RecyclerViewDataObserver extends AdapterDataObserver {
        RecyclerViewDataObserver() {
        }

        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            RecyclerView.this.mState.mStructureChanged = true;
            RecyclerView.this.processDataSetCompletelyChanged(true);
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
            if (RecyclerView.this.mFastScroller != null) {
                RecyclerView.this.mFastScroller.onSectionsChanged();
            }
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(positionStart, itemCount, payload)) {
                triggerUpdateProcessor();
            }
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(fromPosition, toPosition, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        void triggerUpdateProcessor() {
            if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
                ViewCompat.postOnAnimation(RecyclerView.this, RecyclerView.this.mUpdateChildViewsRunnable);
                return;
            }
            RecyclerView.this.mAdapterUpdateDuringMeasure = true;
            RecyclerView.this.requestLayout();
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C03341();
        Parcelable mLayoutState;

        /* renamed from: android.support.v7.widget.RecyclerView$SavedState$1 */
        static class C03341 implements ClassLoaderCreator<SavedState> {
            C03341() {
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            if (loader == null) {
                loader = LayoutManager.class.getClassLoader();
            }
            this.mLayoutState = in.readParcelable(loader);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.mLayoutState, 0);
        }

        void copyFrom(SavedState other) {
            this.mLayoutState = other.mLayoutState;
        }
    }

    public interface SeslFastScrollerEventListener {
        void onPressed(float f);

        void onReleased(float f);
    }

    public interface SeslLongPressMultiSelectionListener {
        void onItemSelected(RecyclerView recyclerView, View view, int i, long j);

        void onLongPressMultiSelectionEnded(int i, int i2);

        void onLongPressMultiSelectionStarted(int i, int i2);
    }

    public interface SeslOnGoToTopClickListener {
        boolean onGoToTopClick(RecyclerView recyclerView);
    }

    public interface SeslOnMultiSelectedListener {
        void onMultiSelectStart(int i, int i2);

        void onMultiSelectStop(int i, int i2);

        void onMultiSelected(RecyclerView recyclerView, View view, int i, long j);
    }

    public static class SimpleOnItemTouchListener implements OnItemTouchListener {
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public static class State {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout = false;
        boolean mIsMeasuring = false;
        int mItemCount = 0;
        int mLayoutStep = 1;
        int mPreviousLayoutItemCount = 0;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations = false;
        boolean mRunSimpleAnimations = false;
        boolean mStructureChanged = false;
        private int mTargetPosition = -1;
        boolean mTrackOldChangeHolders = false;

        @Retention(RetentionPolicy.SOURCE)
        @interface LayoutState {
        }

        void assertLayoutStep(int accepted) {
            if ((this.mLayoutStep & accepted) == 0) {
                throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(accepted) + " but it is " + Integer.toBinaryString(this.mLayoutStep));
            }
        }

        State reset() {
            this.mTargetPosition = -1;
            if (this.mData != null) {
                this.mData.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mIsMeasuring = false;
            return this;
        }

        void prepareForNestedPrefetch(Adapter adapter) {
            this.mLayoutStep = 1;
            this.mItemCount = adapter.getItemCount();
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }

        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }

        public void remove(int resourceId) {
            if (this.mData != null) {
                this.mData.remove(resourceId);
            }
        }

        public <T> T get(int resourceId) {
            if (this.mData == null) {
                return null;
            }
            return this.mData.get(resourceId);
        }

        public void put(int resourceId, Object data) {
            if (this.mData == null) {
                this.mData = new SparseArray();
            }
            this.mData.put(resourceId, data);
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            return this.mTargetPosition != -1;
        }

        public boolean didStructureChange() {
            return this.mStructureChanged;
        }

        public int getItemCount() {
            return this.mInPreLayout ? this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout : this.mItemCount;
        }

        public int getRemainingScrollHorizontal() {
            return this.mRemainingScrollHorizontal;
        }

        public int getRemainingScrollVertical() {
            return this.mRemainingScrollVertical;
        }

        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mIsMeasuring=" + this.mIsMeasuring + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }
    }

    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler recycler, int i, int i2);
    }

    class ViewFlinger implements Runnable {
        private boolean mEatRunOnAnimationRequest = false;
        Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        private boolean mReSchedulePostAnimationCallback = false;
        private SeslOverScroller mScroller;

        ViewFlinger() {
            this.mScroller = new SeslOverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }

        public void run() {
            if (RecyclerView.this.mLayout == null) {
                stop();
                return;
            }
            disableRunOnAnimationRequests();
            RecyclerView.this.consumePendingUpdateOperations();
            SeslOverScroller scroller = this.mScroller;
            SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
            if (scroller.computeScrollOffset()) {
                int hresult;
                int[] scrollConsumed = RecyclerView.this.mScrollConsumed;
                int x = scroller.getCurrX();
                int y = scroller.getCurrY();
                int dx = x - this.mLastFlingX;
                int dy = y - this.mLastFlingY;
                int vresult = 0;
                this.mLastFlingX = x;
                this.mLastFlingY = y;
                int overscrollX = 0;
                int overscrollY = 0;
                if (RecyclerView.this.dispatchNestedPreScroll(dx, dy, scrollConsumed, null, 1)) {
                    dx -= scrollConsumed[0];
                    dy -= scrollConsumed[1];
                    RecyclerView.this.adjustNestedScrollRangeBy(scrollConsumed[1]);
                } else {
                    RecyclerView.this.adjustNestedScrollRangeBy(dy);
                }
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.startInterceptRequestLayout();
                    RecyclerView.this.onEnterLayoutOrScroll();
                    TraceCompat.beginSection(RecyclerView.TRACE_SCROLL_TAG);
                    RecyclerView.this.fillRemainingScrollValues(RecyclerView.this.mState);
                    if (dx != 0) {
                        hresult = RecyclerView.this.mLayout.scrollHorizontallyBy(dx, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        overscrollX = dx - hresult;
                    } else {
                        hresult = 0;
                    }
                    if (dy != 0) {
                        vresult = RecyclerView.this.mLayout.scrollVerticallyBy(dy, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        overscrollY = dy - vresult;
                        if (RecyclerView.this.mGoToTopState == 0) {
                            RecyclerView.this.setupGoToTop(1);
                            RecyclerView.this.autoHide(1);
                        }
                    }
                    TraceCompat.endSection();
                    RecyclerView.this.repositionShadowingViews();
                    RecyclerView.this.onExitLayoutOrScroll();
                    RecyclerView.this.stopInterceptRequestLayout(false);
                    if (!(smoothScroller == null || smoothScroller.isPendingInitialRun() || !smoothScroller.isRunning())) {
                        int adapterSize = RecyclerView.this.mState.getItemCount();
                        if (adapterSize == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.getTargetPosition() >= adapterSize) {
                            smoothScroller.setTargetPosition(adapterSize - 1);
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        } else {
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        }
                    }
                } else {
                    hresult = 0;
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                if (RecyclerView.this.getOverScrollMode() != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(dx, dy);
                }
                boolean nested = RecyclerView.this.dispatchNestedScroll(hresult, vresult, overscrollX, overscrollY, null, 1);
                if (nested) {
                    RecyclerView.this.mScrollOffset[0] = 0;
                    RecyclerView.this.mScrollOffset[1] = 0;
                }
                if (!(overscrollX == 0 && overscrollY == 0)) {
                    int vel = (int) scroller.getCurrVelocity();
                    int velX = 0;
                    if (overscrollX != x) {
                        velX = overscrollX < 0 ? -vel : overscrollX > 0 ? vel : 0;
                    }
                    int velY = 0;
                    if (overscrollY != y) {
                        velY = overscrollY < 0 ? -vel : overscrollY > 0 ? vel : 0;
                    }
                    if ((!nested || overscrollY >= 0) && RecyclerView.this.getOverScrollMode() != 2) {
                        RecyclerView.this.absorbGlows(velX, velY);
                    }
                    if ((!nested || overscrollY >= 0) && ((velX != 0 || overscrollX == x || scroller.getFinalX() == 0) && (velY != 0 || overscrollY == y || scroller.getFinalY() == 0))) {
                        scroller.abortAnimation();
                    }
                }
                if (!(hresult == 0 && vresult == 0)) {
                    RecyclerView.this.dispatchOnScrolled(hresult, vresult);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                boolean fullyConsumedVertical = dy != 0 && RecyclerView.this.mLayout.canScrollVertically() && vresult == dy;
                boolean fullyConsumedHorizontal = dx != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && hresult == dx;
                boolean fullyConsumedAny = (dx == 0 && dy == 0) || fullyConsumedHorizontal || fullyConsumedVertical;
                if (scroller.isFinished() || !(fullyConsumedAny || RecyclerView.this.hasNestedScrollingParent(1))) {
                    if (RecyclerView.this.getOverScrollMode() != 2 && RecyclerView.this.mNestedScroll) {
                        RecyclerView.this.pullGlows((float) dx, (float) overscrollX, (float) dy, (float) overscrollY);
                        RecyclerView.this.considerReleasingGlowsOnScroll(x, y);
                    }
                    RecyclerView.this.setScrollState(0);
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                    }
                    RecyclerView.this.stopNestedScroll(1);
                } else {
                    postOnAnimation();
                    if (RecyclerView.this.mGapWorker != null) {
                        RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, dx, dy);
                    }
                }
            }
            if (smoothScroller != null) {
                if (smoothScroller.isPendingInitialRun()) {
                    smoothScroller.onAnimation(0, 0);
                }
                if (!this.mReSchedulePostAnimationCallback) {
                    smoothScroller.stop();
                }
            }
            enableRunOnAnimationRequests();
        }

        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
                return;
            }
            RecyclerView.this.removeCallbacks(this);
            ViewCompat.postOnAnimation(RecyclerView.this, this);
        }

        public void fling(int velocityX, int velocityY) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, Integer.MIN_VALUE, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
            postOnAnimation();
        }

        public void smoothScrollBy(int dx, int dy) {
            smoothScrollBy(dx, dy, 0, 0);
        }

        public void smoothScrollBy(int dx, int dy, int vx, int vy) {
            smoothScrollBy(dx, dy, computeScrollDuration(dx, dy, vx, vy));
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
        }

        private int computeScrollDuration(int dx, int dy, int vx, int vy) {
            int duration;
            int absDx = Math.abs(dx);
            int absDy = Math.abs(dy);
            boolean horizontal = absDx > absDy;
            int velocity = (int) Math.sqrt((double) ((vx * vx) + (vy * vy)));
            int delta = (int) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            int containerSize = horizontal ? RecyclerView.this.getWidth() : RecyclerView.this.getHeight();
            int halfContainerSize = containerSize / 2;
            float distance = ((float) halfContainerSize) + (((float) halfContainerSize) * distanceInfluenceForSnapDuration(Math.min(1.0f, (1.0f * ((float) delta)) / ((float) containerSize))));
            if (velocity > 0) {
                duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
            } else {
                if (!horizontal) {
                    absDx = absDy;
                }
                duration = (int) (((((float) absDx) / ((float) containerSize)) + 1.0f) * 300.0f);
            }
            return Math.min(duration, RecyclerView.MAX_SCROLL_DURATION);
        }

        public void smoothScrollBy(int dx, int dy, int duration) {
            smoothScrollBy(dx, dy, duration, RecyclerView.sQuinticInterpolator);
        }

        public void smoothScrollBy(int dx, int dy, Interpolator interpolator) {
            int computeScrollDuration = computeScrollDuration(dx, dy, 0, 0);
            if (interpolator == null) {
                interpolator = RecyclerView.sQuinticInterpolator;
            }
            smoothScrollBy(dx, dy, computeScrollDuration, interpolator);
        }

        public void smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator) {
            int axis;
            if (dx != 0) {
                axis = 2;
            } else {
                axis = 1;
            }
            RecyclerView.this.startNestedScroll(axis, 1);
            if (!RecyclerView.this.dispatchNestedPreScroll(dx, dy, null, null, 1)) {
                if (this.mInterpolator != interpolator) {
                    this.mInterpolator = interpolator;
                    this.mScroller = new SeslOverScroller(RecyclerView.this.getContext(), interpolator);
                }
                RecyclerView.this.setScrollState(2);
                this.mLastFlingY = 0;
                this.mLastFlingX = 0;
                this.mScroller.startScroll(0, 0, dx, dy, duration);
                if (VERSION.SDK_INT < 23) {
                    this.mScroller.computeScrollOffset();
                }
                postOnAnimation();
            }
            RecyclerView.this.adjustNestedScrollRangeBy(dy);
        }

        public void stop() {
            RecyclerView.this.removeCallbacks(this);
            this.mScroller.abortAnimation();
        }
    }

    static {
        boolean z = VERSION.SDK_INT == 18 || VERSION.SDK_INT == 19 || VERSION.SDK_INT == 20;
        FORCE_INVALIDATE_DISPLAY_LIST = z;
        if (VERSION.SDK_INT >= 23) {
            z = true;
        } else {
            z = false;
        }
        ALLOW_SIZE_IN_UNSPECIFIED_SPEC = z;
        if (VERSION.SDK_INT >= 16) {
            z = true;
        } else {
            z = false;
        }
        POST_UPDATES_ON_ANIMATION = z;
        if (VERSION.SDK_INT >= 21) {
            z = true;
        } else {
            z = false;
        }
        ALLOW_THREAD_GAP_WORK = z;
        if (VERSION.SDK_INT <= 15) {
            z = true;
        } else {
            z = false;
        }
        FORCE_ABS_FOCUS_SEARCH_DIRECTION = z;
        if (VERSION.SDK_INT <= 15) {
            z = true;
        } else {
            z = false;
        }
        IGNORE_DETACHED_FOCUSED_CHILD = z;
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSeslTouchSlop = 0;
        this.mSeslPagingTouchSlop = 0;
        this.mUsePagingTouchSlopForStylus = false;
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new C03231();
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mItemDecorations = new ArrayList();
        this.mOnItemTouchListeners = new ArrayList();
        this.mSeslOnGoToTopClickListener = null;
        this.mInterceptRequestLayoutDepth = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger();
        this.mPrefetchRegistry = ALLOW_THREAD_GAP_WORK ? new LayoutPrefetchRegistryImpl() : null;
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mWindowOffsets = new int[2];
        this.mIsPenSelectionEnabled = true;
        this.mIsPenPressed = false;
        this.mIsFirstPenMoveEvent = true;
        this.mIsNeedPenSelection = false;
        this.mPenDragSelectedViewPosition = -1;
        this.mIsPenDragBlockEnabled = true;
        this.mPenDragStartX = 0;
        this.mPenDragStartY = 0;
        this.mPenDragEndX = 0;
        this.mPenDragEndY = 0;
        this.mPenDragBlockLeft = 0;
        this.mPenDragBlockTop = 0;
        this.mPenDragBlockRight = 0;
        this.mPenDragBlockBottom = 0;
        this.mPenTrackedChild = null;
        this.mPenTrackedChildPosition = -1;
        this.mPenDistanceFromTrackedChildTop = 0;
        this.mPenDragBlockRect = new Rect();
        this.mInitialTopOffsetOfScreen = 0;
        this.mRemainNestedScrollRange = 0;
        this.mNestedScrollRange = 0;
        this.mHasNestedScrollRange = false;
        this.mIsCtrlKeyPressed = false;
        this.mIsLongPressMultiSelection = false;
        this.mIsFirstMultiSelectionMove = true;
        this.mIsCtrlMultiSelection = false;
        this.mIsMouseWheel = false;
        this.mNestedScroll = false;
        this.mFastScrollerEnabled = false;
        this.mEnableGoToTop = false;
        this.mSizeChnage = false;
        this.mGoToToping = false;
        this.mGoToTopMoved = false;
        this.mGoToTopRect = new Rect();
        this.mGoToTopState = 0;
        this.mGoToTopLastState = 0;
        this.mShowGTPAtFirstTime = false;
        this.mShowFadeOutGTP = 0;
        this.GO_TO_TOP_HIDE = 2500;
        this.mGoToTopFadeOutStart = false;
        this.mIsGoToTopShown = false;
        this.mDrawRect = false;
        this.mDrawOutlineStroke = true;
        this.mDrawLastItemOutlineStoke = false;
        this.mDrawWhiteTheme = true;
        this.mDrawLastOutLineStroke = true;
        this.mDrawReverse = false;
        this.mBlackTop = -1;
        this.mLastBlackTop = -1;
        this.mAnimatedBlackTop = -1;
        this.mRectPaint = new Paint();
        this.mStrokePaint = new Paint();
        this.HOVERSCROLL_SPEED = 10.0f;
        this.mHoveringEnabled = true;
        this.mIsPenHovered = false;
        this.mAlwaysDisableHoverHighlight = false;
        this.mRootViewCheckForDialog = null;
        this.mIsPenSelectPointerSetted = false;
        this.mIsNeedPenSelectIconSet = false;
        this.mOldTextViewHoverState = false;
        this.mNewTextViewHoverState = false;
        this.mHoverScrollSpeed = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mPenDragScrollTimeInterval = 500;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mIsSendHoverScrollState = false;
        this.HOVERSCROLL_DELAY = 0;
        this.mHoverScrollStateForListener = 0;
        this.mIsEnabledPaddingInHoverScroll = false;
        this.mHoverAreaEnter = false;
        this.mSelectorRect = new Rect();
        this.mHoverScrollEnable = true;
        this.mHoverScrollStateChanged = false;
        this.mNeedsHoverScroll = false;
        this.mHoverTopAreaHeight = 0;
        this.mHoverBottomAreaHeight = 0;
        this.mListPadding = new Rect();
        this.mExtraPaddingInTopHoverArea = 0;
        this.mExtraPaddingInBottomHoverArea = 0;
        this.mIsCloseChildSetted = false;
        this.mOldHoverScrollDirection = -1;
        this.mCloseChildByTop = null;
        this.mCloseChildPositionByTop = -1;
        this.mDistanceFromCloseChildTop = 0;
        this.mCloseChildByBottom = null;
        this.mCloseChildPositionByBottom = -1;
        this.mDistanceFromCloseChildBottom = 0;
        this.mHoverHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (RecyclerView.this.mAdapter == null) {
                            Log.e(RecyclerView.TAG, "No adapter attached; skipping MSG_HOVERSCROLL_MOVE");
                            return;
                        }
                        RecyclerView.this.mHoverRecognitionCurrentTime = System.currentTimeMillis();
                        RecyclerView.this.mHoverRecognitionDurationTime = (RecyclerView.this.mHoverRecognitionCurrentTime - RecyclerView.this.mHoverRecognitionStartTime) / 1000;
                        if (RecyclerView.this.mIsPenHovered && RecyclerView.this.mHoverRecognitionCurrentTime - RecyclerView.this.mHoverScrollStartTime < RecyclerView.this.mHoverScrollTimeInterval) {
                            return;
                        }
                        if (!RecyclerView.this.mIsPenPressed || RecyclerView.this.mHoverRecognitionCurrentTime - RecyclerView.this.mHoverScrollStartTime >= RecyclerView.this.mPenDragScrollTimeInterval) {
                            int offset;
                            if (RecyclerView.this.mIsPenHovered && !RecyclerView.this.mIsSendHoverScrollState) {
                                if (RecyclerView.this.mScrollListener != null) {
                                    RecyclerView.this.mHoverScrollStateForListener = 1;
                                    RecyclerView.this.mScrollListener.onScrollStateChanged(RecyclerView.this, 1);
                                }
                                RecyclerView.this.mIsSendHoverScrollState = true;
                            }
                            int count = RecyclerView.this.getChildCount();
                            boolean canScrollDown = RecyclerView.this.findFirstChildPosition() + count < RecyclerView.this.mAdapter.getItemCount();
                            if (!canScrollDown && count > 0) {
                                View child = RecyclerView.this.getChildAt(count - 1);
                                canScrollDown = child.getBottom() > RecyclerView.this.getBottom() - RecyclerView.this.mListPadding.bottom || child.getBottom() > RecyclerView.this.getHeight() - RecyclerView.this.mListPadding.bottom;
                            }
                            boolean canScrollUp = RecyclerView.this.findFirstChildPosition() > 0;
                            if (!canScrollUp && RecyclerView.this.getChildCount() > 0) {
                                canScrollUp = RecyclerView.this.getChildAt(0).getTop() < RecyclerView.this.mListPadding.top;
                            }
                            RecyclerView.this.mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, RecyclerView.this.HOVERSCROLL_SPEED, RecyclerView.this.mContext.getResources().getDisplayMetrics()) + 0.5f);
                            if (RecyclerView.this.mHoverRecognitionDurationTime > 2 && RecyclerView.this.mHoverRecognitionDurationTime < 4) {
                                RecyclerView.this.mHoverScrollSpeed = RecyclerView.this.mHoverScrollSpeed + ((int) (((double) RecyclerView.this.mHoverScrollSpeed) * 0.1d));
                            } else if (RecyclerView.this.mHoverRecognitionDurationTime >= 4 && RecyclerView.this.mHoverRecognitionDurationTime < 5) {
                                RecyclerView.this.mHoverScrollSpeed = RecyclerView.this.mHoverScrollSpeed + ((int) (((double) RecyclerView.this.mHoverScrollSpeed) * 0.2d));
                            } else if (RecyclerView.this.mHoverRecognitionDurationTime >= 5) {
                                RecyclerView.this.mHoverScrollSpeed = RecyclerView.this.mHoverScrollSpeed + ((int) (((double) RecyclerView.this.mHoverScrollSpeed) * 0.3d));
                            }
                            if (RecyclerView.this.mHoverScrollDirection == 2) {
                                offset = RecyclerView.this.mHoverScrollSpeed * -1;
                                if ((RecyclerView.this.mPenTrackedChild == null && RecyclerView.this.mCloseChildByBottom != null) || (RecyclerView.this.mOldHoverScrollDirection != RecyclerView.this.mHoverScrollDirection && RecyclerView.this.mIsCloseChildSetted)) {
                                    RecyclerView.this.mPenTrackedChild = RecyclerView.this.mCloseChildByBottom;
                                    RecyclerView.this.mPenDistanceFromTrackedChildTop = RecyclerView.this.mDistanceFromCloseChildBottom;
                                    RecyclerView.this.mPenTrackedChildPosition = RecyclerView.this.mCloseChildPositionByBottom;
                                    RecyclerView.this.mOldHoverScrollDirection = RecyclerView.this.mHoverScrollDirection;
                                    RecyclerView.this.mIsCloseChildSetted = true;
                                }
                            } else {
                                offset = RecyclerView.this.mHoverScrollSpeed * 1;
                                if ((RecyclerView.this.mPenTrackedChild == null && RecyclerView.this.mCloseChildByTop != null) || (RecyclerView.this.mOldHoverScrollDirection != RecyclerView.this.mHoverScrollDirection && RecyclerView.this.mIsCloseChildSetted)) {
                                    RecyclerView.this.mPenTrackedChild = RecyclerView.this.mCloseChildByTop;
                                    RecyclerView.this.mPenDistanceFromTrackedChildTop = RecyclerView.this.mDistanceFromCloseChildTop;
                                    RecyclerView.this.mPenTrackedChildPosition = RecyclerView.this.mCloseChildPositionByTop;
                                    RecyclerView.this.mOldHoverScrollDirection = RecyclerView.this.mHoverScrollDirection;
                                    RecyclerView.this.mIsCloseChildSetted = true;
                                }
                            }
                            if (RecyclerView.this.getChildAt(RecyclerView.this.getChildCount() - 1) == null) {
                                return;
                            }
                            if ((offset >= 0 || !canScrollUp) && (offset <= 0 || !canScrollDown)) {
                                int overscrollMode = RecyclerView.this.getOverScrollMode();
                                boolean canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && !RecyclerView.this.contentFits());
                                if (canOverscroll && !RecyclerView.this.mIsHoverOverscrolled) {
                                    RecyclerView.this.ensureTopGlow();
                                    RecyclerView.this.ensureBottomGlow();
                                    if (RecyclerView.this.mHoverScrollDirection == 2) {
                                        RecyclerView.this.mTopGlow.setSize(RecyclerView.this.getWidth(), RecyclerView.this.getHeight());
                                        RecyclerView.this.mTopGlow.onPullCallOnRelease(0.4f, 0.5f, 0);
                                        if (!RecyclerView.this.mBottomGlow.isFinished()) {
                                            RecyclerView.this.mBottomGlow.onRelease();
                                        }
                                    } else if (RecyclerView.this.mHoverScrollDirection == 1) {
                                        RecyclerView.this.mBottomGlow.setSize(RecyclerView.this.getWidth(), RecyclerView.this.getHeight());
                                        RecyclerView.this.mBottomGlow.onPullCallOnRelease(0.4f, 0.5f, 0);
                                        RecyclerView.this.setupGoToTop(1);
                                        RecyclerView.this.autoHide(1);
                                        if (!RecyclerView.this.mTopGlow.isFinished()) {
                                            RecyclerView.this.mTopGlow.onRelease();
                                        }
                                    }
                                    RecyclerView.this.invalidate();
                                    RecyclerView.this.mIsHoverOverscrolled = true;
                                }
                                if (RecyclerView.this.mScrollState == 1) {
                                    RecyclerView.this.setScrollState(0);
                                }
                                if (!canOverscroll && !RecyclerView.this.mIsHoverOverscrolled) {
                                    RecyclerView.this.mIsHoverOverscrolled = true;
                                    return;
                                }
                                return;
                            }
                            RecyclerView.this.startNestedScroll(2, 1);
                            if (RecyclerView.this.dispatchNestedPreScroll(0, offset, null, null, 1)) {
                                RecyclerView.this.adjustNestedScrollRangeBy(offset);
                            } else {
                                RecyclerView.this.scrollByInternal(0, offset, null);
                                RecyclerView.this.setScrollState(1);
                                if (RecyclerView.this.mIsLongPressMultiSelection) {
                                    RecyclerView.this.updateLongPressMultiSelection(RecyclerView.this.mPenDragEndX, RecyclerView.this.mPenDragEndY, false);
                                }
                            }
                            RecyclerView.this.mHoverHandler.sendEmptyMessageDelayed(0, (long) RecyclerView.this.HOVERSCROLL_DELAY);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mPendingAccessibilityImportanceChange = new ArrayList();
        this.mItemAnimatorRunner = new C03253();
        this.mViewInfoProcessCallback = new C03275();
        this.mGoToToFadeOutRunnable = new Runnable() {
            public void run() {
                RecyclerView.this.playGotoToFadeOut();
            }
        };
        this.mGoToToFadeInRunnable = new Runnable() {
            public void run() {
                RecyclerView.this.playGotoToFadeIn();
            }
        };
        this.mAutoHide = new Runnable() {
            public void run() {
                RecyclerView.this.setupGoToTop(0);
            }
        };
        this.mContext = context;
        Log.d(TAG, context + "-sesl_recyclerview_version: " + "10.0.48");
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, CLIP_TO_PADDING_ATTR, defStyle, 0);
            this.mClipToPadding = a.getBoolean(0, true);
            a.recycle();
        } else {
            this.mClipToPadding = true;
        }
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        seslInitConfigurations(context);
        setWillNotDraw(getOverScrollMode() == 2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        initAdapterManager();
        initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        boolean nestedScrollingEnabled = true;
        if (attrs != null) {
            a = context.obtainStyledAttributes(attrs, C0270R.styleable.RecyclerView, defStyle, 0);
            String layoutManagerName = a.getString(C0270R.styleable.RecyclerView_layoutManager);
            if (a.getInt(C0270R.styleable.RecyclerView_android_descendantFocusability, -1) == -1) {
                setDescendantFocusability(262144);
            }
            a.recycle();
            createLayoutManager(context, layoutManagerName, attrs, defStyle, 0);
            if (VERSION.SDK_INT >= 21) {
                a = context.obtainStyledAttributes(attrs, NESTED_SCROLLING_ATTRS, defStyle, 0);
                nestedScrollingEnabled = a.getBoolean(0, true);
                a.recycle();
            }
        } else {
            setDescendantFocusability(262144);
        }
        Resources r = context.getResources();
        TypedValue value = new TypedValue();
        this.mPenDragBlockImage = r.getDrawable(C0270R.drawable.sesl_pen_block_selection);
        if (context.getTheme().resolveAttribute(C0270R.attr.goToTopStyle, value, true)) {
            this.mGoToTopImageLight = r.getDrawable(value.resourceId);
        }
        context.getTheme().resolveAttribute(C0270R.attr.roundedCornerColor, value, true);
        if (value.resourceId > 0) {
            this.mRectColor = r.getColor(value.resourceId);
        }
        this.mRectPaint.setColor(this.mRectColor);
        this.mRectPaint.setStyle(Style.FILL_AND_STROKE);
        context.getTheme().resolveAttribute(C0270R.attr.roundedCornerStrokeColor, value, true);
        if (value.resourceId > 0) {
            this.mStrokeColor = r.getColor(value.resourceId);
        }
        this.mStrokePaint.setColor(this.mStrokeColor);
        this.mStrokePaint.setStyle(Style.FILL_AND_STROKE);
        this.mItemAnimator.setHostView(this);
        this.mSeslRoundedCorner = new SeslSubheaderRoundedCorner(getContext());
        this.mSeslRoundedCorner.setRoundedCorners(12);
        setNestedScrollingEnabled(nestedScrollingEnabled);
    }

    public void seslInitConfigurations(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        Resources r = context.getResources();
        this.mTouchSlop = vc.getScaledTouchSlop();
        this.mSeslTouchSlop = vc.getScaledTouchSlop();
        this.mSeslPagingTouchSlop = vc.getScaledPagingTouchSlop();
        this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(vc, context);
        this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(vc, context);
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        this.mHoverTopAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, r.getDisplayMetrics()) + 0.5f);
        this.mHoverBottomAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, r.getDisplayMetrics()) + 0.5f);
        this.mGoToTopBottomPadding = r.getDimensionPixelSize(C0270R.dimen.sesl_go_to_top_scrollable_view_gap);
        this.mGoToTopSize = r.getDimensionPixelSize(C0270R.dimen.sesl_go_to_top_scrollable_view_size);
        this.mNavigationBarHeight = r.getDimensionPixelSize(C0270R.dimen.sesl_navigation_bar_height);
        this.mStrokeHeight = r.getDimensionPixelSize(C0270R.dimen.sesl_round_stroke_height);
    }

    String exceptionLabel() {
        return " " + super.toString() + ", adapter:" + this.mAdapter + ", layout:" + this.mLayout + ", context:" + getContext();
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate) {
        this.mAccessibilityDelegate = accessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this, this.mAccessibilityDelegate);
    }

    private void createLayoutManager(Context context, String className, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (className != null) {
            className = className.trim();
            if (!className.isEmpty()) {
                className = getFullClassName(context, className);
                try {
                    ClassLoader classLoader;
                    Constructor<? extends LayoutManager> constructor;
                    if (isInEditMode()) {
                        classLoader = getClass().getClassLoader();
                    } else {
                        classLoader = context.getClassLoader();
                    }
                    Class<? extends LayoutManager> layoutManagerClass = classLoader.loadClass(className).asSubclass(LayoutManager.class);
                    Object[] constructorArgs = null;
                    try {
                        constructor = layoutManagerClass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        constructorArgs = new Object[]{context, attrs, Integer.valueOf(defStyleAttr), Integer.valueOf(defStyleRes)};
                    } catch (NoSuchMethodException e) {
                        constructor = layoutManagerClass.getConstructor(new Class[0]);
                    }
                    constructor.setAccessible(true);
                    setLayoutManager((LayoutManager) constructor.newInstance(constructorArgs));
                } catch (NoSuchMethodException e1) {
                    e1.initCause(e);
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Error creating LayoutManager " + className, e1);
                } catch (ClassNotFoundException e2) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Unable to find LayoutManager " + className, e2);
                } catch (InvocationTargetException e3) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className, e3);
                } catch (InstantiationException e4) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className, e4);
                } catch (IllegalAccessException e5) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Cannot access non-public constructor " + className, e5);
                } catch (ClassCastException e6) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Class is not a LayoutManager " + className, e6);
                }
            }
        }
    }

    private String getFullClassName(Context context, String className) {
        if (className.charAt(0) == '.') {
            return context.getPackageName() + className;
        }
        return !className.contains(".") ? RecyclerView.class.getPackage().getName() + '.' + className : className;
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new C03286());
    }

    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new C03297());
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        this.mHasFixedSize = hasFixedSize;
    }

    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (clipToPadding != this.mClipToPadding) {
            invalidateGlows();
        }
        this.mClipToPadding = clipToPadding;
        super.setClipToPadding(clipToPadding);
        if (this.mFirstLayoutComplete) {
            requestLayout();
        }
    }

    public boolean getClipToPadding() {
        return this.mClipToPadding;
    }

    public void setScrollingTouchSlop(int slopConstant) {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        Log.d(TAG, "setScrollingTouchSlop(): slopConstant[" + slopConstant + "]");
        seslSetPagingTouchSlopForStylus(false);
        switch (slopConstant) {
            case 0:
                break;
            case 1:
                this.mTouchSlop = vc.getScaledPagingTouchSlop();
                return;
            default:
                Log.w(TAG, "setScrollingTouchSlop(): bad argument constant " + slopConstant + "; using default value");
                break;
        }
        this.mTouchSlop = vc.getScaledTouchSlop();
    }

    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
        processDataSetCompletelyChanged(true);
        requestLayout();
    }

    public void setAdapter(Adapter adapter) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, false, true);
        processDataSetCompletelyChanged(false);
        requestLayout();
    }

    void removeAndRecycleViews() {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        if (this.mLayout != null) {
            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        }
        this.mRecycler.clear();
    }

    private void setAdapterInternal(Adapter adapter, boolean compatibleWithPrevious, boolean removeAndRecycleViews) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!compatibleWithPrevious || removeAndRecycleViews) {
            removeAndRecycleViews();
        }
        this.mAdapterHelper.reset();
        Adapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        if (this.mLayout != null) {
            this.mLayout.onAdapterChanged(oldAdapter, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(oldAdapter, this.mAdapter, compatibleWithPrevious);
        this.mState.mStructureChanged = true;
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecyclerListener = listener;
    }

    public int getBaseline() {
        if (this.mLayout != null) {
            return this.mLayout.getBaseline();
        }
        return super.getBaseline();
    }

    public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener listener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList();
        }
        this.mOnChildAttachStateListeners.add(listener);
    }

    public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener listener) {
        if (this.mOnChildAttachStateListeners != null) {
            this.mOnChildAttachStateListeners.remove(listener);
        }
    }

    public void clearOnChildAttachStateChangeListeners() {
        if (this.mOnChildAttachStateListeners != null) {
            this.mOnChildAttachStateListeners.clear();
        }
    }

    public void setLayoutManager(LayoutManager layout) {
        boolean z = true;
        if (layout != this.mLayout) {
            boolean isLinearLayoutManager = layout instanceof LinearLayoutManager;
            boolean z2 = this.mDrawRect && isLinearLayoutManager;
            this.mDrawRect = z2;
            if (this.mDrawOutlineStroke && isLinearLayoutManager) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mDrawOutlineStroke = z2;
            if (!(this.mDrawLastOutLineStroke && isLinearLayoutManager)) {
                z = false;
            }
            this.mDrawLastOutLineStroke = z;
            stopScroll();
            if (this.mLayout != null) {
                if (this.mItemAnimator != null) {
                    this.mItemAnimator.endAnimations();
                }
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
                this.mRecycler.clear();
                if (this.mIsAttached) {
                    this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
                }
                this.mLayout.setRecyclerView(null);
                this.mLayout = null;
            } else {
                this.mRecycler.clear();
            }
            this.mChildHelper.removeAllViewsUnfiltered();
            this.mLayout = layout;
            if (layout != null) {
                if (layout.mRecyclerView != null) {
                    throw new IllegalArgumentException("LayoutManager " + layout + " is already attached to a RecyclerView:" + layout.mRecyclerView.exceptionLabel());
                }
                this.mLayout.setRecyclerView(this);
                if (this.mIsAttached) {
                    this.mLayout.dispatchAttachedToWindow(this);
                }
            }
            this.mRecycler.updateViewCacheSize();
            requestLayout();
        }
    }

    public void setOnFlingListener(OnFlingListener onFlingListener) {
        this.mOnFlingListener = onFlingListener;
    }

    public OnFlingListener getOnFlingListener() {
        return this.mOnFlingListener;
    }

    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState != null) {
            state.copyFrom(this.mPendingSavedState);
        } else if (this.mLayout != null) {
            state.mLayoutState = this.mLayout.onSaveInstanceState();
        } else {
            state.mLayoutState = null;
        }
        return state;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            this.mPendingSavedState = (SavedState) state;
            super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
            if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
                this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
                return;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    private void addAnimatingView(ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        boolean alreadyParented = view.getParent() == this;
        this.mRecycler.unscrapView(getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
        } else if (alreadyParented) {
            this.mChildHelper.hide(view);
        } else {
            this.mChildHelper.addView(view, true);
        }
    }

    boolean removeAnimatingView(View view) {
        startInterceptRequestLayout();
        boolean removed = this.mChildHelper.removeViewIfHidden(view);
        if (removed) {
            ViewHolder viewHolder = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(viewHolder);
            this.mRecycler.recycleViewHolderInternal(viewHolder);
        }
        stopInterceptRequestLayout(!removed);
        return removed;
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public void setRecycledViewPool(RecycledViewPool pool) {
        this.mRecycler.setRecycledViewPool(pool);
    }

    public void setViewCacheExtension(ViewCacheExtension extension) {
        this.mRecycler.setViewCacheExtension(extension);
    }

    public void setItemViewCacheSize(int size) {
        this.mRecycler.setViewCacheSize(size);
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    void setScrollState(int state) {
        if (state != this.mScrollState) {
            Log.d(TAG, "setting scroll state to " + state + " from " + this.mScrollState);
            this.mScrollState = state;
            if (state != 2) {
                stopScrollersInternal();
            }
            dispatchOnScrollStateChanged(state);
        }
    }

    public void addItemDecoration(ItemDecoration decor, int index) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(false);
        }
        if (index < 0) {
            this.mItemDecorations.add(decor);
        } else {
            this.mItemDecorations.add(index, decor);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void addItemDecoration(ItemDecoration decor) {
        addItemDecoration(decor, -1);
    }

    public ItemDecoration getItemDecorationAt(int index) {
        int size = getItemDecorationCount();
        if (index >= 0 && index < size) {
            return (ItemDecoration) this.mItemDecorations.get(index);
        }
        throw new IndexOutOfBoundsException(index + " is an invalid index for size " + size);
    }

    public int getItemDecorationCount() {
        return this.mItemDecorations.size();
    }

    public void removeItemDecorationAt(int index) {
        int size = getItemDecorationCount();
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index + " is an invalid index for size " + size);
        }
        removeItemDecoration(getItemDecorationAt(index));
    }

    public void removeItemDecoration(ItemDecoration decor) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(decor);
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(getOverScrollMode() == 2);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        if (childDrawingOrderCallback != this.mChildDrawingOrderCallback) {
            this.mChildDrawingOrderCallback = childDrawingOrderCallback;
            setChildrenDrawingOrderEnabled(this.mChildDrawingOrderCallback != null);
        }
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList();
        }
        this.mScrollListeners.add(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.remove(listener);
        }
    }

    public void clearOnScrollListeners() {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.clear();
        }
    }

    public void scrollToPosition(int position) {
        if (!this.mLayoutFrozen) {
            stopScroll();
            if (this.mLayout == null) {
                Log.e(TAG, "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
                return;
            }
            this.mLayout.scrollToPosition(position);
            awakenScrollBars();
            if (this.mFastScroller != null && this.mAdapter != null) {
                this.mFastScroller.onScroll(findFirstVisibleItemPosition(), getChildCount(), this.mAdapter.getItemCount());
            }
        }
    }

    void jumpToPositionForSmoothScroller(int position) {
        if (this.mLayout != null) {
            this.mLayout.scrollToPosition(position);
            awakenScrollBars();
        }
    }

    public void smoothScrollToPosition(int position) {
        if (!this.mLayoutFrozen) {
            if (this.mLayout == null) {
                Log.e(TAG, "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            } else {
                this.mLayout.smoothScrollToPosition(this, this.mState, position);
            }
        }
    }

    public void scrollTo(int x, int y) {
        Log.w(TAG, "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    public void scrollBy(int x, int y) {
        if (this.mLayout == null) {
            Log.e(TAG, "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (!this.mLayoutFrozen) {
            boolean canScrollHorizontal = this.mLayout.canScrollHorizontally();
            boolean canScrollVertical = this.mLayout.canScrollVertically();
            if (canScrollHorizontal || canScrollVertical) {
                if (!canScrollHorizontal) {
                    x = 0;
                }
                if (!canScrollVertical) {
                    y = 0;
                }
                scrollByInternal(x, y, null);
            }
        }
    }

    void consumePendingUpdateOperations() {
        if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
            TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
            dispatchLayout();
            TraceCompat.endSection();
        } else if (!this.mAdapterHelper.hasPendingUpdates()) {
        } else {
            if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
                TraceCompat.beginSection(TRACE_HANDLE_ADAPTER_UPDATES_TAG);
                startInterceptRequestLayout();
                onEnterLayoutOrScroll();
                this.mAdapterHelper.preProcess();
                if (!this.mLayoutWasDefered) {
                    if (hasUpdatedView()) {
                        dispatchLayout();
                    } else {
                        this.mAdapterHelper.consumePostponedUpdates();
                    }
                }
                stopInterceptRequestLayout(true);
                onExitLayoutOrScroll();
                TraceCompat.endSection();
            } else if (this.mAdapterHelper.hasPendingUpdates()) {
                TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
                dispatchLayout();
                TraceCompat.endSection();
            }
        }
    }

    private boolean hasUpdatedView() {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (holder != null && !holder.shouldIgnore() && holder.isUpdated()) {
                return true;
            }
        }
        return false;
    }

    boolean scrollByInternal(int x, int y, MotionEvent ev) {
        boolean isMouse;
        int unconsumedX = 0;
        int unconsumedY = 0;
        int consumedX = 0;
        int consumedY = 0;
        consumePendingUpdateOperations();
        if (this.mAdapter != null) {
            startInterceptRequestLayout();
            onEnterLayoutOrScroll();
            TraceCompat.beginSection(TRACE_SCROLL_TAG);
            fillRemainingScrollValues(this.mState);
            if (x != 0) {
                consumedX = this.mLayout.scrollHorizontallyBy(x, this.mRecycler, this.mState);
                unconsumedX = x - consumedX;
            }
            if (y != 0) {
                consumedY = this.mLayout.scrollVerticallyBy(y, this.mRecycler, this.mState);
                unconsumedY = y - consumedY;
                if (this.mGoToTopState == 0) {
                    setupGoToTop(1);
                    autoHide(1);
                }
            }
            TraceCompat.endSection();
            repositionShadowingViews();
            onExitLayoutOrScroll();
            stopInterceptRequestLayout(false);
        }
        if (!this.mItemDecorations.isEmpty()) {
            invalidate();
        }
        boolean needNestedScroll = true;
        if (this.mIsMouseWheel && unconsumedY < 0) {
            needNestedScroll = false;
        }
        if (ev == null || !MotionEventCompat.isFromSource(ev, 8194)) {
            isMouse = false;
        } else {
            isMouse = true;
        }
        if (needNestedScroll) {
            int i;
            int[] iArr = this.mScrollOffset;
            if (isMouse) {
                i = 1;
            } else {
                i = 0;
            }
            if (dispatchNestedScroll(consumedX, consumedY, unconsumedX, unconsumedY, iArr, i)) {
                this.mLastTouchX -= this.mScrollOffset[0];
                this.mLastTouchY -= this.mScrollOffset[1];
                if (ev != null) {
                    ev.offsetLocation((float) this.mScrollOffset[0], (float) this.mScrollOffset[1]);
                }
                int[] iArr2 = this.mNestedOffsets;
                iArr2[0] = iArr2[0] + this.mScrollOffset[0];
                iArr2 = this.mNestedOffsets;
                iArr2[1] = iArr2[1] + this.mScrollOffset[1];
                this.mNestedScroll = true;
                if (!(consumedX == 0 && consumedY == 0)) {
                    dispatchOnScrolled(consumedX, consumedY);
                }
                if (!awakenScrollBars()) {
                    invalidate();
                }
                if ((this.mLayout instanceof StaggeredGridLayoutManager) && !(canScrollVertically(-1) && canScrollVertically(1))) {
                    this.mLayout.onScrollStateChanged(0);
                }
                if (consumedX == 0 || consumedY != 0) {
                    return true;
                }
                return false;
            }
        }
        if (getOverScrollMode() != 2) {
            if (!(ev == null || isMouse)) {
                pullGlows(ev.getX(), (float) unconsumedX, ev.getY(), (float) unconsumedY);
            }
            considerReleasingGlowsOnScroll(x, y);
        }
        dispatchOnScrolled(consumedX, consumedY);
        if (awakenScrollBars()) {
            invalidate();
        }
        this.mLayout.onScrollStateChanged(0);
        if (consumedX == 0) {
        }
        return true;
    }

    public int computeHorizontalScrollOffset() {
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return 0;
    }

    public int computeHorizontalScrollExtent() {
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return 0;
    }

    public int computeHorizontalScrollRange() {
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return 0;
    }

    public int computeVerticalScrollOffset() {
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return 0;
    }

    public int computeVerticalScrollExtent() {
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return 0;
    }

    public int computeVerticalScrollRange() {
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return 0;
    }

    void startInterceptRequestLayout() {
        this.mInterceptRequestLayoutDepth++;
        if (this.mInterceptRequestLayoutDepth == 1 && !this.mLayoutFrozen) {
            this.mLayoutWasDefered = false;
        }
    }

    void stopInterceptRequestLayout(boolean performLayoutChildren) {
        if (this.mInterceptRequestLayoutDepth < 1) {
            this.mInterceptRequestLayoutDepth = 1;
        }
        if (!(performLayoutChildren || this.mLayoutFrozen)) {
            this.mLayoutWasDefered = false;
        }
        if (this.mInterceptRequestLayoutDepth == 1) {
            if (!(!performLayoutChildren || !this.mLayoutWasDefered || this.mLayoutFrozen || this.mLayout == null || this.mAdapter == null)) {
                dispatchLayout();
            }
            if (!this.mLayoutFrozen) {
                this.mLayoutWasDefered = false;
            }
        }
        this.mInterceptRequestLayoutDepth--;
    }

    public void setLayoutFrozen(boolean frozen) {
        if (frozen != this.mLayoutFrozen) {
            assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
            if (frozen) {
                long now = SystemClock.uptimeMillis();
                onTouchEvent(MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0));
                this.mLayoutFrozen = true;
                this.mIgnoreMotionEventTillDown = true;
                stopScroll();
                return;
            }
            this.mLayoutFrozen = false;
            if (!(!this.mLayoutWasDefered || this.mLayout == null || this.mAdapter == null)) {
                requestLayout();
            }
            this.mLayoutWasDefered = false;
        }
    }

    public boolean isLayoutFrozen() {
        return this.mLayoutFrozen;
    }

    public void smoothScrollBy(int dx, int dy) {
        smoothScrollBy(dx, dy, null);
    }

    public void smoothScrollBy(int dx, int dy, Interpolator interpolator) {
        if (this.mLayout == null) {
            Log.e(TAG, "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (!this.mLayoutFrozen) {
            if (!this.mLayout.canScrollHorizontally()) {
                dx = 0;
            }
            if (!this.mLayout.canScrollVertically()) {
                dy = 0;
            }
            if (dx != 0 || dy != 0) {
                this.mViewFlinger.smoothScrollBy(dx, dy, interpolator);
                showGoToTop();
            }
        }
    }

    public boolean fling(int velocityX, int velocityY) {
        if (this.mLayout == null) {
            Log.e(TAG, "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        } else if (this.mLayoutFrozen) {
            return false;
        } else {
            boolean canScrollHorizontal = this.mLayout.canScrollHorizontally();
            boolean canScrollVertical = this.mLayout.canScrollVertically();
            if (!canScrollHorizontal || Math.abs(velocityX) < this.mMinFlingVelocity) {
                velocityX = 0;
            }
            if (!canScrollVertical || Math.abs(velocityY) < this.mMinFlingVelocity) {
                velocityY = 0;
            }
            if ((velocityX == 0 && velocityY == 0) || dispatchNestedPreFling((float) velocityX, (float) velocityY)) {
                return false;
            }
            boolean canScroll;
            if (canScrollHorizontal || canScrollVertical) {
                canScroll = true;
            } else {
                canScroll = false;
            }
            dispatchNestedFling((float) velocityX, (float) velocityY, canScroll);
            if (this.mOnFlingListener != null && this.mOnFlingListener.onFling(velocityX, velocityY)) {
                return true;
            }
            if (!canScroll) {
                return false;
            }
            int nestedScrollAxis = 0;
            if (canScrollHorizontal) {
                nestedScrollAxis = 0 | 1;
            }
            if (canScrollVertical) {
                nestedScrollAxis |= 2;
            }
            startNestedScroll(nestedScrollAxis, 1);
            this.mViewFlinger.fling(Math.max(-this.mMaxFlingVelocity, Math.min(velocityX, this.mMaxFlingVelocity)), Math.max(-this.mMaxFlingVelocity, Math.min(velocityY, this.mMaxFlingVelocity)));
            return true;
        }
    }

    public void stopScroll() {
        setScrollState(0);
        stopScrollersInternal();
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        if (this.mLayout != null) {
            this.mLayout.stopSmoothScroller();
        }
    }

    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }

    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }

    private void pullGlows(float x, float overscrollX, float y, float overscrollY) {
        boolean invalidate = false;
        if (overscrollX < 0.0f) {
            ensureLeftGlow();
            this.mLeftGlow.onPull((-overscrollX) / ((float) getWidth()), 1.0f - (y / ((float) getHeight())));
            invalidate = true;
        } else if (overscrollX > 0.0f) {
            ensureRightGlow();
            this.mRightGlow.onPull(overscrollX / ((float) getWidth()), y / ((float) getHeight()));
            invalidate = true;
        }
        if (overscrollY < 0.0f) {
            ensureTopGlow();
            this.mTopGlow.onPull((-overscrollY) / ((float) getHeight()), x / ((float) getWidth()));
            invalidate = true;
        } else if (overscrollY > 0.0f) {
            ensureBottomGlow();
            this.mBottomGlow.onPull(overscrollY / ((float) getHeight()), 1.0f - (x / ((float) getWidth())));
            invalidate = true;
        }
        if (invalidate || overscrollX != 0.0f || overscrollY != 0.0f) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void releaseGlows() {
        boolean needsInvalidate = false;
        if (this.mLeftGlow != null) {
            this.mLeftGlow.onRelease();
            needsInvalidate = this.mLeftGlow.isFinished();
        }
        if (this.mTopGlow != null) {
            this.mTopGlow.onRelease();
            needsInvalidate |= this.mTopGlow.isFinished();
        }
        if (this.mRightGlow != null) {
            this.mRightGlow.onRelease();
            needsInvalidate |= this.mRightGlow.isFinished();
        }
        if (this.mBottomGlow != null) {
            this.mBottomGlow.onRelease();
            needsInvalidate |= this.mBottomGlow.isFinished();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void considerReleasingGlowsOnScroll(int dx, int dy) {
        boolean needsInvalidate = false;
        if (!(this.mLeftGlow == null || this.mLeftGlow.isFinished() || dx <= 0)) {
            this.mLeftGlow.onRelease();
            needsInvalidate = this.mLeftGlow.isFinished();
        }
        if (!(this.mRightGlow == null || this.mRightGlow.isFinished() || dx >= 0)) {
            this.mRightGlow.onRelease();
            needsInvalidate |= this.mRightGlow.isFinished();
        }
        if (!(this.mTopGlow == null || this.mTopGlow.isFinished() || dy <= 0)) {
            this.mTopGlow.onRelease();
            needsInvalidate |= this.mTopGlow.isFinished();
        }
        if (!(this.mBottomGlow == null || this.mBottomGlow.isFinished() || dy >= 0)) {
            this.mBottomGlow.onRelease();
            needsInvalidate |= this.mBottomGlow.isFinished();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void absorbGlows(int velocityX, int velocityY) {
        if (velocityX < 0) {
            ensureLeftGlow();
            this.mLeftGlow.onAbsorb(-velocityX);
        } else if (velocityX > 0) {
            ensureRightGlow();
            this.mRightGlow.onAbsorb(velocityX);
        }
        if (velocityY < 0) {
            ensureTopGlow();
            this.mTopGlow.onAbsorb(-velocityY);
        } else if (velocityY > 0) {
            ensureBottomGlow();
            this.mBottomGlow.onAbsorb(velocityY);
        }
        if (velocityX != 0 || velocityY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void ensureLeftGlow() {
        if (this.mLeftGlow == null) {
            this.mLeftGlow = new SeslEdgeEffect(getContext());
            this.mLeftGlow.setSeslHostView(this);
            if (this.mClipToPadding) {
                this.mLeftGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    void ensureRightGlow() {
        if (this.mRightGlow == null) {
            this.mRightGlow = new SeslEdgeEffect(getContext());
            this.mRightGlow.setSeslHostView(this);
            if (this.mClipToPadding) {
                this.mRightGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    void ensureTopGlow() {
        if (this.mTopGlow == null) {
            this.mTopGlow = new SeslEdgeEffect(getContext());
            this.mTopGlow.setSeslHostView(this);
            if (this.mClipToPadding) {
                this.mTopGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    void ensureBottomGlow() {
        if (this.mBottomGlow == null) {
            this.mBottomGlow = new SeslEdgeEffect(getContext());
            this.mBottomGlow.setSeslHostView(this);
            if (this.mClipToPadding) {
                this.mBottomGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    public View focusSearch(View focused, int direction) {
        View result = this.mLayout.onInterceptFocusSearch(focused, direction);
        if (result != null) {
            return result;
        }
        View view;
        boolean canRunFocusFailure = (this.mAdapter == null || this.mLayout == null || isComputingLayout() || this.mLayoutFrozen) ? false : true;
        FocusFinder ff = FocusFinder.getInstance();
        if (canRunFocusFailure && (direction == 2 || direction == 1)) {
            int absDir;
            boolean needsFocusFailureLayout = false;
            if (this.mLayout.canScrollVertically()) {
                absDir = direction == 2 ? 130 : 33;
                needsFocusFailureLayout = ff.findNextFocus(this, focused, absDir) == null;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    direction = absDir;
                }
            }
            if (!needsFocusFailureLayout && this.mLayout.canScrollHorizontally()) {
                absDir = ((direction == 2 ? 1 : 0) ^ (this.mLayout.getLayoutDirection() == 1)) != 0 ? 66 : 17;
                needsFocusFailureLayout = ff.findNextFocus(this, focused, absDir) == null;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    direction = absDir;
                }
            }
            if (needsFocusFailureLayout) {
                consumePendingUpdateOperations();
                if (findContainingItemView(focused) == null) {
                    view = result;
                    return null;
                }
                startInterceptRequestLayout();
                this.mLayout.onFocusSearchFailed(focused, direction, this.mRecycler, this.mState);
                stopInterceptRequestLayout(false);
            }
            result = ff.findNextFocus(this, focused, direction);
        } else {
            result = ff.findNextFocus(this, focused, direction);
            if (result == null && canRunFocusFailure) {
                consumePendingUpdateOperations();
                if (findContainingItemView(focused) == null) {
                    view = result;
                    return null;
                }
                startInterceptRequestLayout();
                result = this.mLayout.onFocusSearchFailed(focused, direction, this.mRecycler, this.mState);
                stopInterceptRequestLayout(false);
            }
        }
        if (result == null || result.hasFocusable()) {
            if (!isPreferredNextFocus(focused, result, direction)) {
                result = super.focusSearch(focused, direction);
            }
            if (this.mIsArrowKeyPressed && result == null && (this.mLayout instanceof StaggeredGridLayoutManager)) {
                int distance = 0;
                if (direction == 130) {
                    distance = getFocusedChild().getBottom() - getBottom();
                } else if (direction == 33) {
                    distance = getFocusedChild().getTop() - getTop();
                }
                ((StaggeredGridLayoutManager) this.mLayout).scrollBy(distance, this.mRecycler, this.mState);
                this.mIsArrowKeyPressed = false;
            }
            view = result;
            return result;
        } else if (getFocusedChild() == null || (direction == 33 && focused != null && focused.getBottom() < result.getBottom() && !canScrollVertically(-1))) {
            view = result;
            return super.focusSearch(focused, direction);
        } else {
            requestChildOnScreen(result, null);
            view = result;
            return focused;
        }
    }

    private boolean isPreferredNextFocus(View focused, View next, int direction) {
        boolean z = false;
        if (next == null || next == this || focused == next) {
            return false;
        }
        if (findContainingItemView(next) == null) {
            return false;
        }
        if (focused == null || findContainingItemView(focused) == null) {
            return true;
        }
        this.mTempRect.set(0, 0, focused.getWidth(), focused.getHeight());
        this.mTempRect2.set(0, 0, next.getWidth(), next.getHeight());
        offsetDescendantRectToMyCoords(focused, this.mTempRect);
        offsetDescendantRectToMyCoords(next, this.mTempRect2);
        int rtl;
        if (this.mLayout.getLayoutDirection() == 1) {
            rtl = -1;
        } else {
            rtl = 1;
        }
        int rightness = 0;
        if ((this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right) {
            rightness = 1;
        } else if ((this.mTempRect.right > this.mTempRect2.right || this.mTempRect.left >= this.mTempRect2.right) && this.mTempRect.left > this.mTempRect2.left) {
            rightness = -1;
        }
        int downness = 0;
        if ((this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom) {
            downness = 1;
        } else if ((this.mTempRect.bottom > this.mTempRect2.bottom || this.mTempRect.top >= this.mTempRect2.bottom) && this.mTempRect.top > this.mTempRect2.top) {
            downness = -1;
        }
        switch (direction) {
            case 1:
                if (downness < 0 || (downness == 0 && rightness * rtl <= 0)) {
                    z = true;
                }
                return z;
            case 2:
                if (downness > 0 || (downness == 0 && rightness * rtl >= 0)) {
                    z = true;
                }
                return z;
            case 17:
                if (rightness >= 0) {
                    return false;
                }
                return true;
            case 33:
                if (downness >= 0) {
                    return false;
                }
                return true;
            case 66:
                if (rightness <= 0) {
                    return false;
                }
                return true;
            case 130:
                if (downness <= 0) {
                    return false;
                }
                return true;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction + exceptionLabel());
        }
    }

    public void requestChildFocus(View child, View focused) {
        if (!(this.mLayout.onRequestChildFocus(this, this.mState, child, focused) || focused == null)) {
            requestChildOnScreen(child, focused);
        }
        super.requestChildFocus(child, focused);
    }

    private void requestChildOnScreen(View child, View focused) {
        View rectView;
        boolean z;
        boolean z2 = true;
        if (focused != null) {
            rectView = focused;
        } else {
            rectView = child;
        }
        this.mTempRect.set(0, 0, rectView.getWidth(), rectView.getHeight());
        android.view.ViewGroup.LayoutParams focusedLayoutParams = rectView.getLayoutParams();
        if (focusedLayoutParams instanceof LayoutParams) {
            LayoutParams lp = (LayoutParams) focusedLayoutParams;
            if (!lp.mInsetsDirty) {
                Rect insets = lp.mDecorInsets;
                Rect rect = this.mTempRect;
                rect.left -= insets.left;
                rect = this.mTempRect;
                rect.right += insets.right;
                rect = this.mTempRect;
                rect.top -= insets.top;
                rect = this.mTempRect;
                rect.bottom += insets.bottom;
            }
        }
        if (focused != null) {
            offsetDescendantRectToMyCoords(focused, this.mTempRect);
            offsetRectIntoDescendantCoords(child, this.mTempRect);
        }
        LayoutManager layoutManager = this.mLayout;
        Rect rect2 = this.mTempRect;
        if (this.mFirstLayoutComplete) {
            z = false;
        } else {
            z = true;
        }
        if (focused != null) {
            z2 = false;
        }
        layoutManager.requestChildRectangleOnScreen(this, child, rect2, z, z2);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        return this.mLayout.requestChildRectangleOnScreen(this, child, rect, immediate);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.mLayout == null || !this.mLayout.onAddFocusables(this, views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (isComputingLayout()) {
            return false;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        this.mIsAttached = true;
        boolean z = this.mFirstLayoutComplete && !isLayoutRequested();
        this.mFirstLayoutComplete = z;
        if (this.mLayout != null) {
            this.mLayout.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
        if (ALLOW_THREAD_GAP_WORK) {
            this.mGapWorker = (GapWorker) GapWorker.sGapWorker.get();
            if (this.mGapWorker == null) {
                this.mGapWorker = new GapWorker();
                Display display = ViewCompat.getDisplay(this);
                float refreshRate = 60.0f;
                if (!(isInEditMode() || display == null)) {
                    float displayRefreshRate = display.getRefreshRate();
                    if (displayRefreshRate >= 30.0f) {
                        refreshRate = displayRefreshRate;
                    }
                }
                this.mGapWorker.mFrameIntervalNs = (long) (1.0E9f / refreshRate);
                GapWorker.sGapWorker.set(this.mGapWorker);
            }
            this.mGapWorker.add(this);
            if (this.mLayout != null && this.mLayout.getLayoutDirection() == 1 && this.mFastScroller != null) {
                this.mFastScroller.setScrollbarPosition(getVerticalScrollbarPosition());
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        stopScroll();
        this.mIsAttached = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.mPendingAccessibilityImportanceChange.clear();
        removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
        if (ALLOW_THREAD_GAP_WORK && this.mGapWorker != null) {
            this.mGapWorker.remove(this);
            this.mGapWorker = null;
        }
    }

    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    void assertInLayoutOrScroll(String message) {
        if (!isComputingLayout()) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling" + exceptionLabel());
            }
            throw new IllegalStateException(message + exceptionLabel());
        }
    }

    void assertNotInLayoutOrScroll(String message) {
        if (isComputingLayout()) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling" + exceptionLabel());
            }
            throw new IllegalStateException(message);
        } else if (this.mDispatchScrollCounter > 0) {
            Log.w(TAG, "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException("" + exceptionLabel()));
        }
    }

    private void adjustNestedScrollRange() {
        getLocationInWindow(this.mWindowOffsets);
        this.mRemainNestedScrollRange = this.mNestedScrollRange - (this.mInitialTopOffsetOfScreen - this.mWindowOffsets[1]);
        if (this.mInitialTopOffsetOfScreen - this.mWindowOffsets[1] < 0) {
            this.mNestedScrollRange = this.mRemainNestedScrollRange;
            this.mInitialTopOffsetOfScreen = this.mWindowOffsets[1];
        }
    }

    private void adjustNestedScrollRangeBy(int offset) {
        if (!this.mHasNestedScrollRange) {
            return;
        }
        if (!canScrollUp() || this.mRemainNestedScrollRange != 0) {
            this.mRemainNestedScrollRange -= offset;
            if (this.mRemainNestedScrollRange < 0) {
                this.mRemainNestedScrollRange = 0;
            } else if (this.mRemainNestedScrollRange > this.mNestedScrollRange) {
                this.mRemainNestedScrollRange = this.mNestedScrollRange;
            }
        }
    }

    public void seslSetGoToTopEnabled(boolean enable) {
        seslSetGoToTopEnabled(enable, true);
    }

    public void seslSetGoToTopEnabled(boolean enable, boolean isWhite) {
        this.mGoToTopImage = isWhite ? this.mGoToTopImageLight : this.mContext.getResources().getDrawable(C0270R.drawable.sesl_list_go_to_top_dark);
        if (this.mGoToTopImage != null) {
            this.mEnableGoToTop = enable;
            this.mGoToTopImage.setCallback(enable ? this : null);
            this.mGoToTopFadeInAnimator = ValueAnimator.ofInt(new int[]{0, 255});
            this.mGoToTopFadeInAnimator.setDuration(333);
            this.mGoToTopFadeInAnimator.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
            this.mGoToTopFadeInAnimator.addUpdateListener(new C03308());
            this.mGoToTopFadeOutAnimator = ValueAnimator.ofInt(new int[]{0, 255});
            this.mGoToTopFadeOutAnimator.setDuration(333);
            this.mGoToTopFadeOutAnimator.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f));
            this.mGoToTopFadeOutAnimator.addUpdateListener(new C03319());
            this.mGoToTopFadeOutAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    try {
                        RecyclerView.this.mShowFadeOutGTP = 1;
                    } catch (Exception e) {
                    }
                }

                public void onAnimationEnd(Animator animation) {
                    try {
                        RecyclerView.this.mShowFadeOutGTP = 2;
                        RecyclerView.this.setupGoToTop(0);
                    } catch (Exception e) {
                    }
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationCancel(Animator animation) {
                }
            });
        }
    }

    void showGoToTop() {
        if (this.mEnableGoToTop && canScrollUp() && this.mGoToTopState != 2) {
            setupGoToTop(1);
            autoHide(1);
        }
    }

    public void seslSetOutlineStrokeEnabled(boolean draw) {
        seslSetOutlineStrokeEnabled(draw, draw);
    }

    public void seslSetOutlineStrokeEnabled(boolean outline, boolean isWhite) {
        if (this.mLayout instanceof LinearLayoutManager) {
            this.mDrawOutlineStroke = outline;
            this.mDrawWhiteTheme = isWhite;
            this.mSeslRoundedCorner = new SeslSubheaderRoundedCorner(getContext(), isWhite);
            this.mSeslRoundedCorner.setRoundedCorners(12);
            if (!this.mDrawWhiteTheme) {
                this.mRectPaint.setColor(getResources().getColor(C0270R.color.sesl_round_and_bgcolor_dark));
            }
            requestLayout();
        }
    }

    public void seslSetLastItemOutlineStrokeEnabled(boolean outline) {
        if (this.mLayout instanceof LinearLayoutManager) {
            this.mDrawLastItemOutlineStoke = outline;
            requestLayout();
        }
    }

    public void seslSetLastOutlineStrokeEnabled(boolean draw) {
        this.mDrawLastOutLineStroke = draw;
    }

    public void seslSetFillBottomEnabled(boolean draw) {
        if (this.mLayout instanceof LinearLayoutManager) {
            this.mDrawRect = draw;
            if (!this.mDrawWhiteTheme) {
                this.mRectPaint.setColor(getResources().getColor(C0270R.color.sesl_round_and_bgcolor_dark));
            }
            requestLayout();
        }
    }

    public void seslSetFillBottomColor(int color) {
        this.mRectPaint.setColor(color);
        if (!this.mDrawWhiteTheme) {
            this.mSeslRoundedCorner.setRoundedCornerColor(15, color);
        }
    }

    public boolean verifyDrawable(Drawable dr) {
        return this.mGoToTopImage == dr || super.verifyDrawable(dr);
    }

    private boolean isTalkBackIsRunning() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        if (accessibilityManager != null && accessibilityManager.isEnabled()) {
            String enabledServices = Secure.getString(getContext().getContentResolver(), "enabled_accessibility_services");
            if (enabledServices != null && (enabledServices.matches("(?i).*com.samsung.accessibility/com.samsung.android.app.talkback.TalkBackService.*") || enabledServices.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*") || enabledServices.matches("(?i).*com.samsung.accessibility/com.samsung.accessibility.universalswitch.UniversalSwitchService.*"))) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupportGotoTop() {
        return !isTalkBackIsRunning() && this.mEnableGoToTop;
    }

    private void playGotoToFadeOut() {
        if (!this.mGoToTopFadeOutAnimator.isRunning()) {
            if (this.mGoToTopFadeInAnimator.isRunning()) {
                this.mGoToTopFadeOutAnimator.cancel();
            }
            this.mGoToTopFadeOutAnimator.setIntValues(new int[]{this.mGoToTopImage.getAlpha(), 0});
            this.mGoToTopFadeOutAnimator.start();
        }
    }

    private void playGotoToFadeIn() {
        if (!this.mGoToTopFadeInAnimator.isRunning()) {
            if (this.mGoToTopFadeOutAnimator.isRunning()) {
                this.mGoToTopFadeOutAnimator.cancel();
            }
            this.mGoToTopFadeInAnimator.setIntValues(new int[]{this.mGoToTopImage.getAlpha(), 255});
            this.mGoToTopFadeInAnimator.start();
        }
    }

    private void autoHide(int when) {
        if (!this.mEnableGoToTop) {
            return;
        }
        if (when == 0) {
            if (!seslIsFastScrollerEnabled()) {
                removeCallbacks(this.mAutoHide);
                postDelayed(this.mAutoHide, (long) this.GO_TO_TOP_HIDE);
            }
        } else if (when == 1) {
            removeCallbacks(this.mAutoHide);
            postDelayed(this.mAutoHide, (long) this.GO_TO_TOP_HIDE);
        }
    }

    private boolean isNavigationBarHide(Context context) {
        if (!isSupportSoftNavigationBar(context) || Global.getInt(context.getContentResolver(), "navigationbar_hide_bar_enabled", 0) == 1) {
            return true;
        }
        return false;
    }

    private boolean isSupportSoftNavigationBar(Context context) {
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && context.getResources().getBoolean(id);
    }

    private void setupGoToTop(int where) {
        if (this.mEnableGoToTop) {
            removeCallbacks(this.mAutoHide);
            int w;
            int h;
            int centerX;
            if (where != this.mGoToTopLastState) {
                if (where == 1 && !canScrollUp()) {
                    where = 0;
                }
                if (where == -1 || !this.mSizeChnage) {
                    if (where == -1 && (canScrollUp() || canScrollDown())) {
                        where = 1;
                    }
                } else if (canScrollUp() || canScrollDown()) {
                    where = this.mGoToTopLastState;
                } else {
                    where = 0;
                }
                if (where != 0) {
                    removeCallbacks(this.mGoToToFadeOutRunnable);
                } else if (where != 1) {
                    removeCallbacks(this.mGoToToFadeInRunnable);
                }
                if (this.mShowFadeOutGTP == 0 && where == 0 && this.mGoToTopLastState != 0) {
                    post(this.mGoToToFadeOutRunnable);
                }
                if (where != 2) {
                    this.mGoToTopImage.setState(StateSet.NOTHING);
                }
                this.mGoToTopState = where;
                w = getWidth();
                h = getHeight();
                centerX = getPaddingLeft() + (((w - getPaddingLeft()) - getPaddingRight()) / 2);
                switch (where) {
                    case 0:
                        if (this.mShowFadeOutGTP == 2) {
                            this.mGoToTopRect = new Rect(0, 0, 0, 0);
                            break;
                        }
                        break;
                    case 1:
                    case 2:
                        removeCallbacks(this.mGoToToFadeOutRunnable);
                        this.mGoToTopRect = new Rect(centerX - (this.mGoToTopSize / 2), (h - this.mGoToTopSize) - this.mGoToTopBottomPadding, (this.mGoToTopSize / 2) + centerX, h - this.mGoToTopBottomPadding);
                        break;
                }
                if (this.mShowFadeOutGTP == 2) {
                    this.mShowFadeOutGTP = 0;
                }
                this.mGoToTopImage.setBounds(this.mGoToTopRect);
                if (where == 1 && (this.mGoToTopLastState == 0 || this.mGoToTopImage.getAlpha() == 0 || this.mSizeChnage)) {
                    post(this.mGoToToFadeInRunnable);
                }
                this.mSizeChnage = false;
                this.mGoToTopLastState = this.mGoToTopState;
            }
            where = 0;
            if (where == -1) {
            }
            where = 1;
            if (where != 0) {
                removeCallbacks(this.mGoToToFadeOutRunnable);
            } else if (where != 1) {
                removeCallbacks(this.mGoToToFadeInRunnable);
            }
            post(this.mGoToToFadeOutRunnable);
            if (where != 2) {
                this.mGoToTopImage.setState(StateSet.NOTHING);
            }
            this.mGoToTopState = where;
            w = getWidth();
            h = getHeight();
            centerX = getPaddingLeft() + (((w - getPaddingLeft()) - getPaddingRight()) / 2);
            switch (where) {
                case 0:
                    if (this.mShowFadeOutGTP == 2) {
                        this.mGoToTopRect = new Rect(0, 0, 0, 0);
                        break;
                    }
                    break;
                case 1:
                case 2:
                    removeCallbacks(this.mGoToToFadeOutRunnable);
                    this.mGoToTopRect = new Rect(centerX - (this.mGoToTopSize / 2), (h - this.mGoToTopSize) - this.mGoToTopBottomPadding, (this.mGoToTopSize / 2) + centerX, h - this.mGoToTopBottomPadding);
                    break;
            }
            if (this.mShowFadeOutGTP == 2) {
                this.mShowFadeOutGTP = 0;
            }
            this.mGoToTopImage.setBounds(this.mGoToTopRect);
            post(this.mGoToToFadeInRunnable);
            this.mSizeChnage = false;
            this.mGoToTopLastState = this.mGoToTopState;
        }
    }

    private void drawGoToTop(Canvas canvas) {
        int scrollY = getScrollY();
        int restoreCount = canvas.save();
        canvas.translate(0.0f, (float) scrollY);
        if (!(this.mGoToTopState == 0 || canScrollUp())) {
            setupGoToTop(0);
        }
        this.mGoToTopImage.draw(canvas);
        canvas.restoreToCount(restoreCount);
    }

    public int seslGetHoverBottomPadding() {
        return this.mHoverBottomAreaHeight;
    }

    public void seslSetHoverBottomPadding(int padding) {
        this.mHoverBottomAreaHeight = padding;
    }

    public int seslGetHoverTopPadding() {
        return this.mHoverTopAreaHeight;
    }

    public void seslSetHoverTopPadding(int padding) {
        this.mHoverTopAreaHeight = padding;
    }

    public int seslGetGoToTopBottomPadding() {
        return this.mGoToTopBottomPadding;
    }

    public void seslSetGoToTopBottomPadding(int padding) {
        this.mGoToTopBottomPadding = padding;
        if (this.mGoToTopState == 1) {
            setupGoToTop(-1);
            autoHide(1);
        }
    }

    public void seslSetOnGoToTopClickListener(SeslOnGoToTopClickListener listener) {
        this.mSeslOnGoToTopClickListener = listener;
    }

    public void addOnItemTouchListener(OnItemTouchListener listener) {
        this.mOnItemTouchListeners.add(listener);
    }

    public void removeOnItemTouchListener(OnItemTouchListener listener) {
        this.mOnItemTouchListeners.remove(listener);
        if (this.mActiveOnItemTouchListener == listener) {
            this.mActiveOnItemTouchListener = null;
        }
    }

    private boolean dispatchOnItemTouchIntercept(MotionEvent e) {
        int action = e.getAction();
        if (action == 3 || action == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        int listenerCount = this.mOnItemTouchListeners.size();
        int i = 0;
        while (i < listenerCount) {
            OnItemTouchListener listener = (OnItemTouchListener) this.mOnItemTouchListeners.get(i);
            if (!listener.onInterceptTouchEvent(this, e) || action == 3) {
                i++;
            } else {
                this.mActiveOnItemTouchListener = listener;
                return true;
            }
        }
        return false;
    }

    private boolean dispatchOnItemTouch(MotionEvent e) {
        int action = e.getAction();
        if (this.mActiveOnItemTouchListener != null) {
            if (action == 0) {
                this.mActiveOnItemTouchListener = null;
            } else {
                this.mActiveOnItemTouchListener.onTouchEvent(this, e);
                if (action != 3 && action != 1) {
                    return true;
                }
                this.mActiveOnItemTouchListener = null;
                return true;
            }
        }
        if (action != 0) {
            int listenerCount = this.mOnItemTouchListeners.size();
            for (int i = 0; i < listenerCount; i++) {
                OnItemTouchListener listener = (OnItemTouchListener) this.mOnItemTouchListeners.get(i);
                if (listener.onInterceptTouchEvent(this, e)) {
                    this.mActiveOnItemTouchListener = listener;
                    return true;
                }
            }
        }
        return false;
    }

    public void seslStartLongPressMultiSelection() {
        this.mIsLongPressMultiSelection = true;
    }

    public void seslSetCtrlkeyPressed(boolean pressed) {
        this.mIsCtrlKeyPressed = pressed;
    }

    private void updateLongPressMultiSelection(int x, int y, boolean fromUserTouch) {
        int contentTop;
        int contentBottom;
        int count = this.mChildHelper.getChildCount();
        if (this.mIsFirstMultiSelectionMove) {
            this.mPenDragStartX = x;
            this.mPenDragStartY = y;
            this.mPenTrackedChild = findChildViewUnder((float) x, (float) y);
            if (this.mPenTrackedChild == null) {
                this.mPenTrackedChild = seslFindNearChildViewUnder((float) x, (float) y);
                if (this.mPenTrackedChild == null) {
                    Log.e(TAG, "updateLongPressMultiSelection, mPenTrackedChild is NULL");
                    this.mIsFirstMultiSelectionMove = false;
                    return;
                }
            }
            if (this.mLongPressMultiSelectionListener != null) {
                this.mLongPressMultiSelectionListener.onLongPressMultiSelectionStarted(x, y);
            }
            this.mPenTrackedChildPosition = getChildLayoutPosition(this.mPenTrackedChild);
            this.mPenDragSelectedViewPosition = this.mPenTrackedChildPosition;
            this.mPenDistanceFromTrackedChildTop = this.mPenDragStartY - this.mPenTrackedChild.getTop();
            this.mIsFirstMultiSelectionMove = false;
        }
        if (this.mIsEnabledPaddingInHoverScroll) {
            contentTop = this.mListPadding.top;
            contentBottom = getHeight() - this.mListPadding.bottom;
        } else {
            contentTop = 0;
            contentBottom = getHeight();
        }
        this.mPenDragEndX = x;
        this.mPenDragEndY = y;
        if (this.mPenDragEndY < 0) {
            this.mPenDragEndY = 0;
        } else if (this.mPenDragEndY > contentBottom) {
            this.mPenDragEndY = contentBottom;
        }
        View touchedView = findChildViewUnder((float) this.mPenDragEndX, (float) this.mPenDragEndY);
        if (touchedView == null) {
            touchedView = seslFindNearChildViewUnder((float) this.mPenDragEndX, (float) this.mPenDragEndY);
            if (touchedView == null) {
                Log.e(TAG, "updateLongPressMultiSelection, touchedView is NULL");
                return;
            }
        }
        int touchedPosition = getChildLayoutPosition(touchedView);
        if (touchedPosition != -1) {
            int startPosition;
            int i;
            this.mPenDragSelectedViewPosition = touchedPosition;
            int endPosition;
            if (this.mPenTrackedChildPosition < this.mPenDragSelectedViewPosition) {
                startPosition = this.mPenTrackedChildPosition;
                endPosition = this.mPenDragSelectedViewPosition;
            } else {
                startPosition = this.mPenDragSelectedViewPosition;
                endPosition = this.mPenTrackedChildPosition;
            }
            this.mPenDragBlockLeft = this.mPenDragStartX < this.mPenDragEndX ? this.mPenDragStartX : this.mPenDragEndX;
            this.mPenDragBlockTop = this.mPenDragStartY < this.mPenDragEndY ? this.mPenDragStartY : this.mPenDragEndY;
            this.mPenDragBlockRight = this.mPenDragEndX > this.mPenDragStartX ? this.mPenDragEndX : this.mPenDragStartX;
            if (this.mPenDragEndY > this.mPenDragStartY) {
                i = this.mPenDragEndY;
            } else {
                i = this.mPenDragStartY;
            }
            this.mPenDragBlockBottom = i;
            for (int i2 = 0; i2 < count; i2++) {
                View child = getChildAt(i2);
                if (child != null) {
                    this.mPenDragSelectedViewPosition = getChildLayoutPosition(child);
                    if (child.getVisibility() == 0) {
                        boolean needSelected = false;
                        if (startPosition <= this.mPenDragSelectedViewPosition && this.mPenDragSelectedViewPosition <= endPosition && this.mPenDragSelectedViewPosition != this.mPenTrackedChildPosition) {
                            needSelected = true;
                        }
                        if (needSelected) {
                            if (!(this.mPenDragSelectedViewPosition == -1 || this.mPenDragSelectedItemArray.contains(Integer.valueOf(this.mPenDragSelectedViewPosition)))) {
                                this.mPenDragSelectedItemArray.add(Integer.valueOf(this.mPenDragSelectedViewPosition));
                                if (this.mLongPressMultiSelectionListener != null) {
                                    this.mLongPressMultiSelectionListener.onItemSelected(this, child, this.mPenDragSelectedViewPosition, getChildItemId(child));
                                }
                            }
                        } else if (this.mPenDragSelectedViewPosition != -1 && this.mPenDragSelectedItemArray.contains(Integer.valueOf(this.mPenDragSelectedViewPosition))) {
                            this.mPenDragSelectedItemArray.remove(Integer.valueOf(this.mPenDragSelectedViewPosition));
                            if (this.mLongPressMultiSelectionListener != null) {
                                this.mLongPressMultiSelectionListener.onItemSelected(this, child, this.mPenDragSelectedViewPosition, getChildItemId(child));
                            }
                        }
                    }
                }
            }
            if (fromUserTouch) {
                if (y <= this.mHoverTopAreaHeight + contentTop) {
                    if (!this.mHoverAreaEnter) {
                        this.mHoverAreaEnter = true;
                        this.mHoverScrollStartTime = System.currentTimeMillis();
                        if (this.mScrollListener != null) {
                            this.mScrollListener.onScrollStateChanged(this, 1);
                        }
                    }
                    if (!this.mHoverHandler.hasMessages(0)) {
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        this.mHoverScrollDirection = 2;
                        this.mHoverHandler.sendEmptyMessage(0);
                    }
                } else if (y >= (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange) {
                    if (!this.mHoverAreaEnter) {
                        this.mHoverAreaEnter = true;
                        this.mHoverScrollStartTime = System.currentTimeMillis();
                        if (this.mScrollListener != null) {
                            this.mScrollListener.onScrollStateChanged(this, 1);
                        }
                    }
                    if (!this.mHoverHandler.hasMessages(0)) {
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendEmptyMessage(0);
                    }
                } else {
                    if (this.mHoverAreaEnter && this.mScrollListener != null) {
                        this.mScrollListener.onScrollStateChanged(this, 0);
                    }
                    this.mHoverScrollStartTime = 0;
                    this.mHoverRecognitionStartTime = 0;
                    this.mHoverAreaEnter = false;
                    if (this.mHoverHandler.hasMessages(0)) {
                        this.mHoverHandler.removeMessages(0);
                        if (this.mScrollState == 1) {
                            setScrollState(0);
                        }
                    }
                    this.mIsHoverOverscrolled = false;
                }
            }
            invalidate();
            return;
        }
        Log.e(TAG, "touchedPosition is NO_POSITION");
    }

    private void multiSelection(int x, int y, int contentTop, int contentBottom, boolean needToScroll) {
        if (this.mIsNeedPenSelection) {
            if (this.mIsFirstPenMoveEvent) {
                this.mPenDragStartX = x;
                this.mPenDragStartY = y;
                this.mIsPenPressed = true;
                this.mPenTrackedChild = findChildViewUnder((float) x, (float) y);
                if (this.mPenTrackedChild == null) {
                    this.mPenTrackedChild = seslFindNearChildViewUnder((float) x, (float) y);
                    if (this.mPenTrackedChild == null) {
                        Log.e(TAG, "multiSelection, mPenTrackedChild is NULL");
                        this.mIsPenPressed = false;
                        this.mIsFirstPenMoveEvent = false;
                        return;
                    }
                }
                if (this.mOnMultiSelectedListener != null) {
                    this.mOnMultiSelectedListener.onMultiSelectStart(x, y);
                }
                this.mPenTrackedChildPosition = getChildLayoutPosition(this.mPenTrackedChild);
                this.mPenDistanceFromTrackedChildTop = this.mPenDragStartY - this.mPenTrackedChild.getTop();
                this.mIsFirstPenMoveEvent = false;
            }
            if (this.mPenDragStartX == 0 && this.mPenDragStartY == 0) {
                this.mPenDragStartX = x;
                this.mPenDragStartY = y;
                if (this.mOnMultiSelectedListener != null) {
                    this.mOnMultiSelectedListener.onMultiSelectStart(x, y);
                }
                this.mIsPenPressed = true;
            }
            this.mPenDragEndX = x;
            this.mPenDragEndY = y;
            if (this.mPenDragEndY < 0) {
                this.mPenDragEndY = 0;
            } else if (this.mPenDragEndY > contentBottom) {
                this.mPenDragEndY = contentBottom;
            }
            this.mPenDragBlockLeft = this.mPenDragStartX < this.mPenDragEndX ? this.mPenDragStartX : this.mPenDragEndX;
            this.mPenDragBlockTop = this.mPenDragStartY < this.mPenDragEndY ? this.mPenDragStartY : this.mPenDragEndY;
            this.mPenDragBlockRight = this.mPenDragEndX > this.mPenDragStartX ? this.mPenDragEndX : this.mPenDragStartX;
            this.mPenDragBlockBottom = this.mPenDragEndY > this.mPenDragStartY ? this.mPenDragEndY : this.mPenDragStartY;
            needToScroll = true;
        }
        if (needToScroll) {
            if (y <= this.mHoverTopAreaHeight + contentTop) {
                if (!this.mHoverAreaEnter) {
                    this.mHoverAreaEnter = true;
                    this.mHoverScrollStartTime = System.currentTimeMillis();
                    if (this.mScrollListener != null) {
                        this.mScrollListener.onScrollStateChanged(this, 1);
                    }
                }
                if (!this.mHoverHandler.hasMessages(0)) {
                    this.mHoverRecognitionStartTime = System.currentTimeMillis();
                    this.mHoverScrollDirection = 2;
                    this.mHoverHandler.sendEmptyMessage(0);
                }
            } else if (y >= (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange) {
                if (!this.mHoverAreaEnter) {
                    this.mHoverAreaEnter = true;
                    this.mHoverScrollStartTime = System.currentTimeMillis();
                    if (this.mScrollListener != null) {
                        this.mScrollListener.onScrollStateChanged(this, 1);
                    }
                }
                if (!this.mHoverHandler.hasMessages(0)) {
                    this.mHoverRecognitionStartTime = System.currentTimeMillis();
                    this.mHoverScrollDirection = 1;
                    this.mHoverHandler.sendEmptyMessage(0);
                }
            } else {
                if (this.mHoverAreaEnter && this.mScrollListener != null) {
                    this.mScrollListener.onScrollStateChanged(this, 0);
                }
                this.mHoverScrollStartTime = 0;
                this.mHoverRecognitionStartTime = 0;
                this.mHoverAreaEnter = false;
                if (this.mHoverHandler.hasMessages(0)) {
                    this.mHoverHandler.removeMessages(0);
                    if (this.mScrollState == 1) {
                        setScrollState(0);
                    }
                }
                this.mIsHoverOverscrolled = false;
            }
            if (this.mIsPenDragBlockEnabled) {
                invalidate();
            }
        }
    }

    private void multiSelectionEnd(int x, int y) {
        if (this.mIsPenPressed && this.mOnMultiSelectedListener != null) {
            this.mOnMultiSelectedListener.onMultiSelectStop(x, y);
        }
        this.mIsPenPressed = false;
        this.mIsFirstPenMoveEvent = true;
        this.mPenDragSelectedViewPosition = -1;
        this.mPenDragSelectedItemArray.clear();
        this.mPenDragStartX = 0;
        this.mPenDragStartY = 0;
        this.mPenDragEndX = 0;
        this.mPenDragEndY = 0;
        this.mPenDragBlockLeft = 0;
        this.mPenDragBlockTop = 0;
        this.mPenDragBlockRight = 0;
        this.mPenDragBlockBottom = 0;
        this.mPenTrackedChild = null;
        this.mPenDistanceFromTrackedChildTop = 0;
        if (this.mIsPenDragBlockEnabled) {
            invalidate();
        }
        if (this.mHoverHandler.hasMessages(0)) {
            this.mHoverHandler.removeMessages(0);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchTouchEvent(android.view.MotionEvent r14) {
        /*
        r13 = this;
        r12 = 3;
        r9 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r10 = 2;
        r8 = 1;
        r11 = 0;
        r0 = r13.mItemAnimator;
        if (r0 == 0) goto L_0x0023;
    L_0x000a:
        r0 = r13.mItemAnimator;
        r0 = r0.isRunning();
        if (r0 == 0) goto L_0x0023;
    L_0x0012:
        r0 = r13.mItemAnimator;
        r0 = r0.getItemAnimationTypeInternal();
        if (r0 != r10) goto L_0x0023;
    L_0x001a:
        r0 = "SeslRecyclerView";
        r9 = "dispatchTouchEvent : itemAnimator is running, return..";
        android.util.Log.d(r0, r9);
        r0 = r8;
    L_0x0022:
        return r0;
    L_0x0023:
        r0 = r13.mLayout;
        if (r0 != 0) goto L_0x0033;
    L_0x0027:
        r0 = "SeslRecyclerView";
        r8 = "No layout manager attached; skipping gototop & multiselection";
        android.util.Log.d(r0, r8);
        r0 = super.dispatchTouchEvent(r14);
        goto L_0x0022;
    L_0x0033:
        r6 = r14.getActionMasked();
        r0 = r14.getX();
        r0 = r0 + r9;
        r1 = (int) r0;
        r0 = r14.getY();
        r0 = r0 + r9;
        r2 = (int) r0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r0 = r13.mPenDragSelectedItemArray;
        if (r0 != 0) goto L_0x0051;
    L_0x004a:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r13.mPenDragSelectedItemArray = r0;
    L_0x0051:
        r0 = r13.mIsEnabledPaddingInHoverScroll;
        if (r0 == 0) goto L_0x006b;
    L_0x0055:
        r0 = r13.mListPadding;
        r3 = r0.top;
        r0 = r13.getHeight();
        r9 = r13.mListPadding;
        r9 = r9.bottom;
        r4 = r0 - r9;
    L_0x0063:
        switch(r6) {
            case 0: goto L_0x0071;
            case 1: goto L_0x012f;
            case 2: goto L_0x00da;
            case 3: goto L_0x0118;
            case 211: goto L_0x00bf;
            case 212: goto L_0x0182;
            case 213: goto L_0x0112;
            default: goto L_0x0066;
        };
    L_0x0066:
        r0 = super.dispatchTouchEvent(r14);
        goto L_0x0022;
    L_0x006b:
        r3 = 0;
        r4 = r13.getHeight();
        goto L_0x0063;
    L_0x0071:
        r0 = r13.isSupportGotoTop();
        if (r0 == 0) goto L_0x007b;
    L_0x0077:
        r13.mGoToTopMoved = r11;
        r13.mGoToToping = r11;
    L_0x007b:
        r0 = r13.isSupportGotoTop();
        if (r0 == 0) goto L_0x00a3;
    L_0x0081:
        r0 = r13.mGoToTopState;
        if (r0 == r10) goto L_0x00a3;
    L_0x0085:
        r0 = r13.mGoToTopRect;
        r0 = r0.contains(r1, r2);
        if (r0 == 0) goto L_0x00a3;
    L_0x008d:
        r13.setupGoToTop(r10);
        r0 = r13.mGoToTopImage;
        r9 = (float) r1;
        r10 = (float) r2;
        r0.setHotspot(r9, r10);
        r0 = r13.mGoToTopImage;
        r9 = new int[r12];
        r9 = {16842919, 16842910, 16842913};
        r0.setState(r9);
        r0 = r8;
        goto L_0x0022;
    L_0x00a3:
        r0 = r13.mIsCtrlKeyPressed;
        if (r0 == 0) goto L_0x00b8;
    L_0x00a7:
        r0 = r14.getToolType(r11);
        if (r0 != r12) goto L_0x00b8;
    L_0x00ad:
        r13.mIsCtrlMultiSelection = r8;
        r13.mIsNeedPenSelection = r8;
        r0 = r13;
        r0.multiSelection(r1, r2, r3, r4, r5);
        r0 = r8;
        goto L_0x0022;
    L_0x00b8:
        r0 = r13.mIsLongPressMultiSelection;
        if (r0 == 0) goto L_0x0066;
    L_0x00bc:
        r13.mIsLongPressMultiSelection = r11;
        goto L_0x0066;
    L_0x00bf:
        r0 = android.support.v4.widget.SeslTextViewReflector.isTextSelectionProgressing();
        if (r0 != 0) goto L_0x00c9;
    L_0x00c5:
        r0 = r13.mIsPenSelectionEnabled;
        if (r0 != 0) goto L_0x00d7;
    L_0x00c9:
        r13.mIsNeedPenSelection = r11;
    L_0x00cb:
        r0 = r13.mPenDragSelectedItemArray;
        if (r0 != 0) goto L_0x0066;
    L_0x00cf:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r13.mPenDragSelectedItemArray = r0;
        goto L_0x0066;
    L_0x00d7:
        r13.mIsNeedPenSelection = r8;
        goto L_0x00cb;
    L_0x00da:
        r0 = r13.mIsCtrlMultiSelection;
        if (r0 == 0) goto L_0x00e5;
    L_0x00de:
        r0 = r13;
        r0.multiSelection(r1, r2, r3, r4, r5);
        r0 = r8;
        goto L_0x0022;
    L_0x00e5:
        r0 = r13.mIsLongPressMultiSelection;
        if (r0 == 0) goto L_0x00ef;
    L_0x00e9:
        r13.updateLongPressMultiSelection(r1, r2, r8);
        r0 = r8;
        goto L_0x0022;
    L_0x00ef:
        r0 = r13.isSupportGotoTop();
        if (r0 == 0) goto L_0x0066;
    L_0x00f5:
        r0 = r13.mGoToTopState;
        if (r0 != r10) goto L_0x0066;
    L_0x00f9:
        r0 = r13.mGoToTopRect;
        r0 = r0.contains(r1, r2);
        if (r0 != 0) goto L_0x010f;
    L_0x0101:
        r13.mGoToTopState = r8;
        r0 = r13.mGoToTopImage;
        r9 = android.util.StateSet.NOTHING;
        r0.setState(r9);
        r13.autoHide(r8);
        r13.mGoToTopMoved = r8;
    L_0x010f:
        r0 = r8;
        goto L_0x0022;
    L_0x0112:
        r0 = r13;
        r0.multiSelection(r1, r2, r3, r4, r5);
        goto L_0x0066;
    L_0x0118:
        r0 = r13.isSupportGotoTop();
        if (r0 == 0) goto L_0x012f;
    L_0x011e:
        r0 = r13.mGoToTopState;
        if (r0 == 0) goto L_0x012f;
    L_0x0122:
        r0 = r13.mGoToTopState;
        if (r0 != r10) goto L_0x0128;
    L_0x0126:
        r13.mGoToTopState = r8;
    L_0x0128:
        r0 = r13.mGoToTopImage;
        r9 = android.util.StateSet.NOTHING;
        r0.setState(r9);
    L_0x012f:
        r0 = r13.mIsCtrlMultiSelection;
        if (r0 == 0) goto L_0x013b;
    L_0x0133:
        r13.multiSelectionEnd(r1, r2);
        r13.mIsCtrlMultiSelection = r11;
        r0 = r8;
        goto L_0x0022;
    L_0x013b:
        r0 = r13.mIsLongPressMultiSelection;
        if (r0 == 0) goto L_0x0182;
    L_0x013f:
        r0 = r13.mLongPressMultiSelectionListener;
        if (r0 == 0) goto L_0x0148;
    L_0x0143:
        r0 = r13.mLongPressMultiSelectionListener;
        r0.onLongPressMultiSelectionEnded(r1, r2);
    L_0x0148:
        r13.mIsFirstMultiSelectionMove = r8;
        r0 = -1;
        r13.mPenDragSelectedViewPosition = r0;
        r13.mPenDragStartX = r11;
        r13.mPenDragStartY = r11;
        r13.mPenDragEndX = r11;
        r13.mPenDragEndY = r11;
        r13.mPenDragBlockLeft = r11;
        r13.mPenDragBlockTop = r11;
        r13.mPenDragBlockRight = r11;
        r13.mPenDragBlockBottom = r11;
        r0 = r13.mPenDragSelectedItemArray;
        r0.clear();
        r0 = 0;
        r13.mPenTrackedChild = r0;
        r13.mPenDistanceFromTrackedChildTop = r11;
        r0 = r13.mHoverHandler;
        r0 = r0.hasMessages(r11);
        if (r0 == 0) goto L_0x017b;
    L_0x016f:
        r0 = r13.mHoverHandler;
        r0.removeMessages(r11);
        r0 = r13.mScrollState;
        if (r0 != r8) goto L_0x017b;
    L_0x0178:
        r13.setScrollState(r11);
    L_0x017b:
        r13.mIsHoverOverscrolled = r11;
        r13.invalidate();
        r13.mIsLongPressMultiSelection = r11;
    L_0x0182:
        r0 = r13.isSupportGotoTop();
        if (r0 == 0) goto L_0x020f;
    L_0x0188:
        r0 = r13.mGoToTopState;
        if (r0 != r10) goto L_0x020f;
    L_0x018c:
        r0 = r13.canScrollUp();
        if (r0 == 0) goto L_0x01da;
    L_0x0192:
        r0 = r13.mSeslOnGoToTopClickListener;
        if (r0 == 0) goto L_0x01a1;
    L_0x0196:
        r0 = r13.mSeslOnGoToTopClickListener;
        r0 = r0.onGoToTopClick(r13);
        if (r0 == 0) goto L_0x01a1;
    L_0x019e:
        r0 = r8;
        goto L_0x0022;
    L_0x01a1:
        r0 = "SeslRecyclerView";
        r9 = " can scroll top ";
        android.util.Log.d(r0, r9);
        r7 = r13.getChildCount();
        r0 = r13.computeVerticalScrollOffset();
        if (r0 == 0) goto L_0x01da;
    L_0x01b2:
        r13.stopScroll();
        r0 = r13.mLayout;
        r0 = r0 instanceof android.support.v7.widget.StaggeredGridLayoutManager;
        if (r0 == 0) goto L_0x01e3;
    L_0x01bb:
        r0 = r13.mLayout;
        r0 = (android.support.v7.widget.StaggeredGridLayoutManager) r0;
        r0.scrollToPositionWithOffset(r11, r11);
    L_0x01c2:
        r0 = r13.mTopGlow;
        if (r0 == 0) goto L_0x0207;
    L_0x01c6:
        r0 = 1140457472; // 0x43fa0000 float:500.0 double:5.634608575E-315;
        r9 = r13.getHeight();
        r9 = (float) r9;
        r0 = r0 / r9;
        r9 = (float) r1;
        r10 = r13.getWidth();
        r10 = (float) r10;
        r9 = r9 / r10;
        r10 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        r13.seslShowGoToTopEdge(r0, r9, r10);
    L_0x01da:
        r13.seslHideGoToTop();
        r13.playSoundEffect(r11);
        r0 = r8;
        goto L_0x0022;
    L_0x01e3:
        r13.mGoToToping = r8;
        if (r7 <= 0) goto L_0x01fa;
    L_0x01e7:
        r0 = r13.findFirstVisibleItemPosition();
        if (r7 >= r0) goto L_0x01fa;
    L_0x01ed:
        r0 = r13.mLayout;
        r0 = r0 instanceof android.support.v7.widget.LinearLayoutManager;
        if (r0 == 0) goto L_0x0203;
    L_0x01f3:
        r0 = r13.mLayout;
        r0 = (android.support.v7.widget.LinearLayoutManager) r0;
        r0.scrollToPositionWithOffset(r7, r11);
    L_0x01fa:
        r0 = new android.support.v7.widget.RecyclerView$14;
        r0.<init>();
        r13.post(r0);
        goto L_0x01c2;
    L_0x0203:
        r13.scrollToPosition(r7);
        goto L_0x01fa;
    L_0x0207:
        r0 = "SeslRecyclerView";
        r9 = " There is no mTopGlow";
        android.util.Log.d(r0, r9);
        goto L_0x01da;
    L_0x020f:
        r0 = r13.mGoToTopMoved;
        if (r0 == 0) goto L_0x021e;
    L_0x0213:
        r13.mGoToTopMoved = r11;
        r0 = r13.mVelocityTracker;
        if (r0 == 0) goto L_0x021e;
    L_0x0219:
        r0 = r13.mVelocityTracker;
        r0.clear();
    L_0x021e:
        r13.multiSelectionEnd(r1, r2);
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.RecyclerView.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    public void seslShowGoToTopEdge(float deltaDistance, float displacement, int delayTime) {
        ensureTopGlow();
        this.mTopGlow.onPullCallOnRelease(deltaDistance, displacement, delayTime);
        invalidate(0, 0, getWidth(), 500);
    }

    public void seslHideGoToTop() {
        autoHide(0);
        this.mGoToTopImage.setState(StateSet.NOTHING);
    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (this.mLayoutFrozen) {
            return false;
        }
        if (dispatchOnItemTouchIntercept(e)) {
            cancelTouch();
            return true;
        } else if (this.mLayout == null) {
            return false;
        } else {
            boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
            boolean canScrollVertically = this.mLayout.canScrollVertically();
            boolean isMouse = e != null && MotionEventCompat.isFromSource(e, 8194);
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(e);
            int action = e.getActionMasked();
            int actionIndex = e.getActionIndex();
            MotionEvent vtev = MotionEvent.obtain(e);
            if (this.mFastScroller != null && this.mFastScroller.onInterceptTouchEvent(e)) {
                return true;
            }
            int x;
            switch (action) {
                case 0:
                    if (this.mIgnoreMotionEventTillDown) {
                        this.mIgnoreMotionEventTillDown = false;
                    }
                    this.mScrollPointerId = e.getPointerId(0);
                    x = (int) (e.getX() + 0.5f);
                    this.mLastTouchX = x;
                    this.mInitialTouchX = x;
                    x = (int) (e.getY() + 0.5f);
                    this.mLastTouchY = x;
                    this.mInitialTouchY = x;
                    if (this.mUsePagingTouchSlopForStylus) {
                        if (e.isFromSource(InputDeviceCompat.SOURCE_STYLUS)) {
                            this.mTouchSlop = this.mSeslPagingTouchSlop;
                        } else {
                            this.mTouchSlop = this.mSeslTouchSlop;
                        }
                    }
                    if (this.mScrollState == 2) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                    }
                    int[] iArr = this.mNestedOffsets;
                    this.mNestedOffsets[1] = 0;
                    iArr[0] = 0;
                    if (this.mHasNestedScrollRange) {
                        adjustNestedScrollRange();
                    }
                    int nestedScrollAxis = 0;
                    if (canScrollHorizontally) {
                        nestedScrollAxis = 0 | 1;
                    }
                    if (canScrollVertically) {
                        nestedScrollAxis |= 2;
                    }
                    if (isMouse) {
                        x = 1;
                    } else {
                        x = 0;
                    }
                    startNestedScroll(nestedScrollAxis, x);
                    break;
                case 1:
                    this.mVelocityTracker.clear();
                    stopNestedScroll(isMouse ? 1 : 0);
                    break;
                case 2:
                    int index = e.findPointerIndex(this.mScrollPointerId);
                    if (index >= 0) {
                        int x2 = (int) (e.getX(index) + 0.5f);
                        int y = (int) (e.getY(index) + 0.5f);
                        int dx = this.mLastTouchX - x2;
                        int dy = this.mLastTouchY - y;
                        if (this.mScrollState != 1) {
                            boolean startScroll = false;
                            if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop) {
                                if (dx > 0) {
                                    dx -= this.mTouchSlop;
                                } else {
                                    dx += this.mTouchSlop;
                                }
                                startScroll = true;
                            }
                            if (canScrollVertically && Math.abs(dy) > this.mTouchSlop) {
                                if (dy > 0) {
                                    dy -= this.mTouchSlop;
                                } else {
                                    dy += this.mTouchSlop;
                                }
                                startScroll = true;
                            }
                            if (startScroll) {
                                setScrollState(1);
                            }
                        }
                        if (this.mScrollState == 1) {
                            this.mLastTouchX = x2 - this.mScrollOffset[0];
                            this.mLastTouchY = y - this.mScrollOffset[1];
                            if (!this.mGoToTopMoved) {
                                int i;
                                if (canScrollHorizontally) {
                                    i = dx;
                                } else {
                                    i = 0;
                                }
                                if (canScrollVertically) {
                                    x = dy;
                                } else {
                                    x = 0;
                                }
                                if (scrollByInternal(i, x, vtev)) {
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                }
                            }
                            if (!(this.mGapWorker == null || (dx == 0 && dy == 0))) {
                                this.mGapWorker.postFromTraversal(this, dx, dy);
                            }
                        }
                        adjustNestedScrollRangeBy(dy);
                        break;
                    }
                    Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                case 3:
                    cancelTouch();
                    break;
                case 5:
                    this.mScrollPointerId = e.getPointerId(actionIndex);
                    x = (int) (e.getX(actionIndex) + 0.5f);
                    this.mLastTouchX = x;
                    this.mInitialTouchX = x;
                    x = (int) (e.getY(actionIndex) + 0.5f);
                    this.mLastTouchY = x;
                    this.mInitialTouchY = x;
                    break;
                case 6:
                    onPointerUp(e);
                    break;
            }
            if (this.mScrollState == 1) {
                return true;
            }
            return false;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        int listenerCount = this.mOnItemTouchListeners.size();
        for (int i = 0; i < listenerCount; i++) {
            ((OnItemTouchListener) this.mOnItemTouchListeners.get(i)).onRequestDisallowInterceptTouchEvent(disallowIntercept);
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r22) {
        /*
        r21 = this;
        r0 = r21;
        r1 = r0.mLayoutFrozen;
        if (r1 != 0) goto L_0x000c;
    L_0x0006:
        r0 = r21;
        r1 = r0.mIgnoreMotionEventTillDown;
        if (r1 == 0) goto L_0x000e;
    L_0x000c:
        r1 = 0;
    L_0x000d:
        return r1;
    L_0x000e:
        r1 = r21.dispatchOnItemTouch(r22);
        if (r1 == 0) goto L_0x0019;
    L_0x0014:
        r21.cancelTouch();
        r1 = 1;
        goto L_0x000d;
    L_0x0019:
        r0 = r21;
        r1 = r0.mLayout;
        if (r1 != 0) goto L_0x0021;
    L_0x001f:
        r1 = 0;
        goto L_0x000d;
    L_0x0021:
        r1 = 0;
        r0 = r21;
        r0.mIsMouseWheel = r1;
        r0 = r21;
        r1 = r0.mLayout;
        r9 = r1.canScrollHorizontally();
        r0 = r21;
        r1 = r0.mLayout;
        r10 = r1.canScrollVertically();
        r0 = r21;
        r1 = r0.mVelocityTracker;
        if (r1 != 0) goto L_0x0044;
    L_0x003c:
        r1 = android.view.VelocityTracker.obtain();
        r0 = r21;
        r0.mVelocityTracker = r1;
    L_0x0044:
        r11 = 0;
        r15 = android.view.MotionEvent.obtain(r22);
        r7 = r22.getActionMasked();
        r8 = r22.getActionIndex();
        if (r7 != 0) goto L_0x0063;
    L_0x0053:
        r0 = r21;
        r1 = r0.mNestedOffsets;
        r4 = 0;
        r0 = r21;
        r5 = r0.mNestedOffsets;
        r6 = 1;
        r20 = 0;
        r5[r6] = r20;
        r1[r4] = r20;
    L_0x0063:
        r0 = r21;
        r1 = r0.mNestedOffsets;
        r4 = 0;
        r1 = r1[r4];
        r1 = (float) r1;
        r0 = r21;
        r4 = r0.mNestedOffsets;
        r5 = 1;
        r4 = r4[r5];
        r4 = (float) r4;
        r15.offsetLocation(r1, r4);
        r0 = r21;
        r1 = r0.mFastScroller;
        if (r1 == 0) goto L_0x00e4;
    L_0x007c:
        r0 = r21;
        r1 = r0.mFastScroller;
        r0 = r22;
        r1 = r1.onTouchEvent(r0);
        if (r1 == 0) goto L_0x00e4;
    L_0x0088:
        r0 = r21;
        r1 = r0.mFastScrollerEventListener;
        if (r1 == 0) goto L_0x00b9;
    L_0x008e:
        r1 = r22.getActionMasked();
        if (r1 == 0) goto L_0x009b;
    L_0x0094:
        r1 = r22.getActionMasked();
        r4 = 2;
        if (r1 != r4) goto L_0x00bf;
    L_0x009b:
        r0 = r21;
        r1 = r0.mFastScroller;
        r1 = r1.getEffectState();
        r0 = r21;
        r4 = r0.mFastScroller;
        r4 = 1;
        if (r1 != r4) goto L_0x00bf;
    L_0x00aa:
        r0 = r21;
        r1 = r0.mFastScrollerEventListener;
        r0 = r21;
        r4 = r0.mFastScroller;
        r4 = r4.getScrollY();
        r1.onPressed(r4);
    L_0x00b9:
        r15.recycle();
        r1 = 1;
        goto L_0x000d;
    L_0x00bf:
        r1 = r22.getActionMasked();
        r4 = 1;
        if (r1 != r4) goto L_0x00b9;
    L_0x00c6:
        r0 = r21;
        r1 = r0.mFastScroller;
        r1 = r1.getEffectState();
        r0 = r21;
        r4 = r0.mFastScroller;
        if (r1 != 0) goto L_0x00b9;
    L_0x00d4:
        r0 = r21;
        r1 = r0.mFastScrollerEventListener;
        r0 = r21;
        r4 = r0.mFastScroller;
        r4 = r4.getScrollY();
        r1.onReleased(r4);
        goto L_0x00b9;
    L_0x00e4:
        switch(r7) {
            case 0: goto L_0x00f6;
            case 1: goto L_0x02ca;
            case 2: goto L_0x016a;
            case 3: goto L_0x0345;
            case 4: goto L_0x00e7;
            case 5: goto L_0x013a;
            case 6: goto L_0x02c5;
            default: goto L_0x00e7;
        };
    L_0x00e7:
        if (r11 != 0) goto L_0x00f0;
    L_0x00e9:
        r0 = r21;
        r1 = r0.mVelocityTracker;
        r1.addMovement(r15);
    L_0x00f0:
        r15.recycle();
        r1 = 1;
        goto L_0x000d;
    L_0x00f6:
        r1 = 0;
        r0 = r22;
        r1 = r0.getPointerId(r1);
        r0 = r21;
        r0.mScrollPointerId = r1;
        r1 = r22.getX();
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r4;
        r1 = (int) r1;
        r0 = r21;
        r0.mLastTouchX = r1;
        r0 = r21;
        r0.mInitialTouchX = r1;
        r1 = r22.getY();
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r4;
        r1 = (int) r1;
        r0 = r21;
        r0.mLastTouchY = r1;
        r0 = r21;
        r0.mInitialTouchY = r1;
        r0 = r21;
        r1 = r0.mHasNestedScrollRange;
        if (r1 == 0) goto L_0x012a;
    L_0x0127:
        r21.adjustNestedScrollRange();
    L_0x012a:
        r13 = 0;
        if (r9 == 0) goto L_0x012f;
    L_0x012d:
        r13 = r13 | 1;
    L_0x012f:
        if (r10 == 0) goto L_0x0133;
    L_0x0131:
        r13 = r13 | 2;
    L_0x0133:
        r1 = 0;
        r0 = r21;
        r0.startNestedScroll(r13, r1);
        goto L_0x00e7;
    L_0x013a:
        r0 = r22;
        r1 = r0.getPointerId(r8);
        r0 = r21;
        r0.mScrollPointerId = r1;
        r0 = r22;
        r1 = r0.getX(r8);
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r4;
        r1 = (int) r1;
        r0 = r21;
        r0.mLastTouchX = r1;
        r0 = r21;
        r0.mInitialTouchX = r1;
        r0 = r22;
        r1 = r0.getY(r8);
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r4;
        r1 = (int) r1;
        r0 = r21;
        r0.mLastTouchY = r1;
        r0 = r21;
        r0.mInitialTouchY = r1;
        goto L_0x00e7;
    L_0x016a:
        r0 = r21;
        r1 = r0.mScrollPointerId;
        r0 = r22;
        r12 = r0.findPointerIndex(r1);
        if (r12 >= 0) goto L_0x019b;
    L_0x0176:
        r1 = "SeslRecyclerView";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Error processing scroll; pointer index for id ";
        r4 = r4.append(r5);
        r0 = r21;
        r5 = r0.mScrollPointerId;
        r4 = r4.append(r5);
        r5 = " not found. Did any MotionEvents get skipped?";
        r4 = r4.append(r5);
        r4 = r4.toString();
        android.util.Log.e(r1, r4);
        r1 = 0;
        goto L_0x000d;
    L_0x019b:
        r0 = r22;
        r1 = r0.getX(r12);
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r4;
        r0 = (int) r1;
        r16 = r0;
        r0 = r22;
        r1 = r0.getY(r12);
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r4;
        r0 = (int) r1;
        r18 = r0;
        r0 = r21;
        r1 = r0.mLastTouchX;
        r2 = r1 - r16;
        r0 = r21;
        r1 = r0.mLastTouchY;
        r3 = r1 - r18;
        r0 = r21;
        r4 = r0.mScrollConsumed;
        r0 = r21;
        r5 = r0.mScrollOffset;
        r6 = 0;
        r1 = r21;
        r1 = r1.dispatchNestedPreScroll(r2, r3, r4, r5, r6);
        if (r1 == 0) goto L_0x02ad;
    L_0x01d0:
        r0 = r21;
        r1 = r0.mScrollConsumed;
        r4 = 0;
        r1 = r1[r4];
        r2 = r2 - r1;
        r0 = r21;
        r1 = r0.mScrollConsumed;
        r4 = 1;
        r1 = r1[r4];
        r3 = r3 - r1;
        r0 = r21;
        r1 = r0.mScrollOffset;
        r4 = 0;
        r1 = r1[r4];
        r1 = (float) r1;
        r0 = r21;
        r4 = r0.mScrollOffset;
        r5 = 1;
        r4 = r4[r5];
        r4 = (float) r4;
        r15.offsetLocation(r1, r4);
        r0 = r21;
        r1 = r0.mNestedOffsets;
        r4 = 0;
        r5 = r1[r4];
        r0 = r21;
        r6 = r0.mScrollOffset;
        r20 = 0;
        r6 = r6[r20];
        r5 = r5 + r6;
        r1[r4] = r5;
        r0 = r21;
        r1 = r0.mNestedOffsets;
        r4 = 1;
        r5 = r1[r4];
        r0 = r21;
        r6 = r0.mScrollOffset;
        r20 = 1;
        r6 = r6[r20];
        r5 = r5 + r6;
        r1[r4] = r5;
        r0 = r21;
        r1 = r0.mScrollConsumed;
        r4 = 1;
        r1 = r1[r4];
        r0 = r21;
        r0.adjustNestedScrollRangeBy(r1);
    L_0x0223:
        r0 = r21;
        r1 = r0.mScrollState;
        r4 = 1;
        if (r1 == r4) goto L_0x025b;
    L_0x022a:
        r14 = 0;
        if (r9 == 0) goto L_0x023f;
    L_0x022d:
        r1 = java.lang.Math.abs(r2);
        r0 = r21;
        r4 = r0.mTouchSlop;
        if (r1 <= r4) goto L_0x023f;
    L_0x0237:
        if (r2 <= 0) goto L_0x02b4;
    L_0x0239:
        r0 = r21;
        r1 = r0.mTouchSlop;
        r2 = r2 - r1;
    L_0x023e:
        r14 = 1;
    L_0x023f:
        if (r10 == 0) goto L_0x0253;
    L_0x0241:
        r1 = java.lang.Math.abs(r3);
        r0 = r21;
        r4 = r0.mTouchSlop;
        if (r1 <= r4) goto L_0x0253;
    L_0x024b:
        if (r3 <= 0) goto L_0x02ba;
    L_0x024d:
        r0 = r21;
        r1 = r0.mTouchSlop;
        r3 = r3 - r1;
    L_0x0252:
        r14 = 1;
    L_0x0253:
        if (r14 == 0) goto L_0x025b;
    L_0x0255:
        r1 = 1;
        r0 = r21;
        r0.setScrollState(r1);
    L_0x025b:
        r0 = r21;
        r1 = r0.mScrollState;
        r4 = 1;
        if (r1 != r4) goto L_0x00e7;
    L_0x0262:
        r0 = r21;
        r1 = r0.mScrollOffset;
        r4 = 0;
        r1 = r1[r4];
        r1 = r16 - r1;
        r0 = r21;
        r0.mLastTouchX = r1;
        r0 = r21;
        r1 = r0.mScrollOffset;
        r4 = 1;
        r1 = r1[r4];
        r1 = r18 - r1;
        r0 = r21;
        r0.mLastTouchY = r1;
        r0 = r21;
        r1 = r0.mGoToTopMoved;
        if (r1 != 0) goto L_0x0298;
    L_0x0282:
        if (r9 == 0) goto L_0x02c0;
    L_0x0284:
        r4 = r2;
    L_0x0285:
        if (r10 == 0) goto L_0x02c3;
    L_0x0287:
        r1 = r3;
    L_0x0288:
        r0 = r21;
        r1 = r0.scrollByInternal(r4, r1, r15);
        if (r1 == 0) goto L_0x0298;
    L_0x0290:
        r1 = r21.getParent();
        r4 = 1;
        r1.requestDisallowInterceptTouchEvent(r4);
    L_0x0298:
        r0 = r21;
        r1 = r0.mGapWorker;
        if (r1 == 0) goto L_0x00e7;
    L_0x029e:
        if (r2 != 0) goto L_0x02a2;
    L_0x02a0:
        if (r3 == 0) goto L_0x00e7;
    L_0x02a2:
        r0 = r21;
        r1 = r0.mGapWorker;
        r0 = r21;
        r1.postFromTraversal(r0, r2, r3);
        goto L_0x00e7;
    L_0x02ad:
        r0 = r21;
        r0.adjustNestedScrollRangeBy(r3);
        goto L_0x0223;
    L_0x02b4:
        r0 = r21;
        r1 = r0.mTouchSlop;
        r2 = r2 + r1;
        goto L_0x023e;
    L_0x02ba:
        r0 = r21;
        r1 = r0.mTouchSlop;
        r3 = r3 + r1;
        goto L_0x0252;
    L_0x02c0:
        r1 = 0;
        r4 = r1;
        goto L_0x0285;
    L_0x02c3:
        r1 = 0;
        goto L_0x0288;
    L_0x02c5:
        r21.onPointerUp(r22);
        goto L_0x00e7;
    L_0x02ca:
        r0 = r21;
        r1 = r0.mVelocityTracker;
        r1.addMovement(r15);
        r11 = 1;
        r0 = r21;
        r1 = r0.mVelocityTracker;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r21;
        r5 = r0.mMaxFlingVelocity;
        r5 = (float) r5;
        r1.computeCurrentVelocity(r4, r5);
        if (r9 == 0) goto L_0x033f;
    L_0x02e2:
        r0 = r21;
        r1 = r0.mVelocityTracker;
        r0 = r21;
        r4 = r0.mScrollPointerId;
        r1 = r1.getXVelocity(r4);
        r0 = -r1;
        r17 = r0;
    L_0x02f1:
        if (r10 == 0) goto L_0x0342;
    L_0x02f3:
        r0 = r21;
        r1 = r0.mVelocityTracker;
        r0 = r21;
        r4 = r0.mScrollPointerId;
        r1 = r1.getYVelocity(r4);
        r0 = -r1;
        r19 = r0;
    L_0x0302:
        r1 = 0;
        r1 = (r17 > r1 ? 1 : (r17 == r1 ? 0 : -1));
        if (r1 != 0) goto L_0x030c;
    L_0x0307:
        r1 = 0;
        r1 = (r19 > r1 ? 1 : (r19 == r1 ? 0 : -1));
        if (r1 == 0) goto L_0x031a;
    L_0x030c:
        r0 = r17;
        r1 = (int) r0;
        r0 = r19;
        r4 = (int) r0;
        r0 = r21;
        r1 = r0.fling(r1, r4);
        if (r1 != 0) goto L_0x0320;
    L_0x031a:
        r1 = 0;
        r0 = r21;
        r0.setScrollState(r1);
    L_0x0320:
        r1 = "SeslRecyclerView";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "onTouchUp() velocity : ";
        r4 = r4.append(r5);
        r0 = r19;
        r4 = r4.append(r0);
        r4 = r4.toString();
        android.util.Log.d(r1, r4);
        r21.resetTouch();
        goto L_0x00e7;
    L_0x033f:
        r17 = 0;
        goto L_0x02f1;
    L_0x0342:
        r19 = 0;
        goto L_0x0302;
    L_0x0345:
        r21.cancelTouch();
        goto L_0x00e7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.RecyclerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void resetTouch() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
        }
        stopNestedScroll(0);
        releaseGlows();
    }

    private void cancelTouch() {
        resetTouch();
        setScrollState(0);
    }

    private void onPointerUp(MotionEvent e) {
        int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == this.mScrollPointerId) {
            int newIndex = actionIndex == 0 ? 1 : 0;
            this.mScrollPointerId = e.getPointerId(newIndex);
            int x = (int) (e.getX(newIndex) + 0.5f);
            this.mLastTouchX = x;
            this.mInitialTouchX = x;
            x = (int) (e.getY(newIndex) + 0.5f);
            this.mLastTouchY = x;
            this.mInitialTouchY = x;
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (!(this.mLayout == null || this.mLayoutFrozen || event.getAction() != 8)) {
            float vScroll;
            float hScroll;
            this.mIsMouseWheel = true;
            if ((event.getSource() & 2) != 0) {
                if (this.mLayout.canScrollVertically()) {
                    vScroll = -event.getAxisValue(9);
                } else {
                    vScroll = 0.0f;
                }
                if (this.mLayout.canScrollHorizontally()) {
                    hScroll = event.getAxisValue(10);
                } else {
                    hScroll = 0.0f;
                }
            } else if ((event.getSource() & 4194304) != 0) {
                float axisScroll = event.getAxisValue(26);
                if (this.mLayout.canScrollVertically()) {
                    vScroll = -axisScroll;
                    hScroll = 0.0f;
                } else if (this.mLayout.canScrollHorizontally()) {
                    vScroll = 0.0f;
                    hScroll = axisScroll;
                } else {
                    vScroll = 0.0f;
                    hScroll = 0.0f;
                }
            } else {
                vScroll = 0.0f;
                hScroll = 0.0f;
            }
            if (!(vScroll == 0.0f && hScroll == 0.0f)) {
                int axis;
                if (vScroll != 0.0f) {
                    axis = 2;
                } else {
                    axis = 1;
                }
                startNestedScroll(axis, 1);
                if (!dispatchNestedPreScroll((int) (this.mScaledHorizontalScrollFactor * hScroll), (int) (this.mScaledVerticalScrollFactor * vScroll), null, null, 1)) {
                    scrollByInternal((int) (this.mScaledHorizontalScrollFactor * hScroll), (int) (this.mScaledVerticalScrollFactor * vScroll), event);
                }
            }
        }
        return false;
    }

    protected void onMeasure(int widthSpec, int heightSpec) {
        boolean measureSpecModeIsExactly = false;
        if (this.mLayout == null) {
            defaultOnMeasure(widthSpec, heightSpec);
            return;
        }
        Rect listPadding = this.mListPadding;
        listPadding.left = getPaddingLeft();
        listPadding.right = getPaddingRight();
        listPadding.top = getPaddingTop();
        listPadding.bottom = getPaddingBottom();
        if (getResources().getDisplayMetrics().heightPixels < getMeasuredHeight()) {
            Log.d(TAG, "h = " + getMeasuredHeight() + "auto = " + this.mLayout.isAutoMeasureEnabled() + ", fixedSize = " + this.mHasFixedSize);
            if (getParent() != null) {
                Log.d(TAG, "p = " + getParent() + ", ph =" + ((View) getParent()).getMeasuredHeight());
            }
        }
        if (this.mLayout.isAutoMeasureEnabled()) {
            int widthMode = MeasureSpec.getMode(widthSpec);
            int heightMode = MeasureSpec.getMode(heightSpec);
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
            if (widthMode == 1073741824 && heightMode == 1073741824) {
                measureSpecModeIsExactly = true;
            }
            if (!measureSpecModeIsExactly && this.mAdapter != null) {
                if (this.mState.mLayoutStep == 1) {
                    dispatchLayoutStep1();
                }
                this.mLayout.setMeasureSpecs(widthSpec, heightSpec);
                this.mState.mIsMeasuring = true;
                dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
                if (this.mLayout.shouldMeasureTwice()) {
                    this.mLayout.setMeasureSpecs(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
                    this.mState.mIsMeasuring = true;
                    dispatchLayoutStep2();
                    this.mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
                }
            }
        } else if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
        } else {
            if (this.mAdapterUpdateDuringMeasure) {
                startInterceptRequestLayout();
                onEnterLayoutOrScroll();
                processAdapterUpdatesAndSetAnimationFlags();
                onExitLayoutOrScroll();
                if (this.mState.mRunPredictiveAnimations) {
                    this.mState.mInPreLayout = true;
                } else {
                    this.mAdapterHelper.consumeUpdatesInOnePass();
                    this.mState.mInPreLayout = false;
                }
                this.mAdapterUpdateDuringMeasure = false;
                stopInterceptRequestLayout(false);
            } else if (this.mState.mRunPredictiveAnimations) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
                return;
            }
            if (this.mAdapter != null) {
                this.mState.mItemCount = this.mAdapter.getItemCount();
            } else {
                this.mState.mItemCount = 0;
            }
            startInterceptRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
            stopInterceptRequestLayout(false);
            this.mState.mInPreLayout = false;
        }
    }

    void defaultOnMeasure(int widthSpec, int heightSpec) {
        setMeasuredDimension(LayoutManager.chooseSize(widthSpec, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth(this)), LayoutManager.chooseSize(heightSpec, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight(this)));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!(w == oldw && h == oldh)) {
            invalidateGlows();
        }
        if (this.mFastScroller != null) {
            this.mFastScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void setItemAnimator(ItemAnimator animator) {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = animator;
        if (this.mItemAnimator != null) {
            this.mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }

    void onEnterLayoutOrScroll() {
        this.mLayoutOrScrollCounter++;
    }

    void onExitLayoutOrScroll() {
        onExitLayoutOrScroll(true);
    }

    void onExitLayoutOrScroll(boolean enableChangeEvents) {
        this.mLayoutOrScrollCounter--;
        if (this.mLayoutOrScrollCounter < 1) {
            this.mLayoutOrScrollCounter = 0;
            if (enableChangeEvents) {
                dispatchContentChangedIfNecessary();
                dispatchPendingImportantForAccessibilityChanges();
            }
        }
    }

    boolean isAccessibilityEnabled() {
        return this.mAccessibilityManager != null && this.mAccessibilityManager.isEnabled();
    }

    private void dispatchContentChangedIfNecessary() {
        int flags = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (flags != 0 && isAccessibilityEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain();
            event.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(event, flags);
            sendAccessibilityEventUnchecked(event);
        }
    }

    public boolean isComputingLayout() {
        return this.mLayoutOrScrollCounter > 0;
    }

    boolean shouldDeferAccessibilityEvent(AccessibilityEvent event) {
        if (!isComputingLayout()) {
            return false;
        }
        int type = 0;
        if (event != null) {
            type = AccessibilityEventCompat.getContentChangeTypes(event);
        }
        if (type == 0) {
            type = 0;
        }
        this.mEatenAccessibilityChangeFlags |= type;
        return true;
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (!shouldDeferAccessibilityEvent(event)) {
            super.sendAccessibilityEventUnchecked(event);
        }
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        boolean z;
        boolean z2 = true;
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            if (this.mDispatchItemsChangedEvent) {
                this.mLayout.onItemsChanged(this);
            }
        }
        if (predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        } else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean animationTypeSupported;
        if (this.mItemsAddedOrRemoved || this.mItemsChanged) {
            animationTypeSupported = true;
        } else {
            animationTypeSupported = false;
        }
        State state = this.mState;
        if (!this.mFirstLayoutComplete || this.mItemAnimator == null || (!(this.mDataSetHasChangedAfterLayout || animationTypeSupported || this.mLayout.mRequestedSimpleAnimations) || (this.mDataSetHasChangedAfterLayout && !this.mAdapter.hasStableIds()))) {
            z = false;
        } else {
            z = true;
        }
        state.mRunSimpleAnimations = z;
        State state2 = this.mState;
        if (!(this.mState.mRunSimpleAnimations && animationTypeSupported && !this.mDataSetHasChangedAfterLayout && predictiveItemAnimationsEnabled())) {
            z2 = false;
        }
        state2.mRunPredictiveAnimations = z2;
    }

    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e(TAG, "No adapter attached; skipping layout");
        } else if (this.mLayout == null) {
            Log.e(TAG, "No layout manager attached; skipping layout");
        } else {
            this.mState.mIsMeasuring = false;
            if (this.mState.mLayoutStep == 1) {
                dispatchLayoutStep1();
                this.mLayout.setExactMeasureSpecsFrom(this);
                dispatchLayoutStep2();
            } else if (!this.mAdapterHelper.hasUpdates() && this.mLayout.getWidth() == getWidth() && this.mLayout.getHeight() == getHeight()) {
                this.mLayout.setExactMeasureSpecsFrom(this);
            } else {
                this.mLayout.setExactMeasureSpecsFrom(this);
                dispatchLayoutStep2();
            }
            dispatchLayoutStep3();
        }
    }

    private void saveFocusInfo() {
        View child = null;
        if (this.mPreserveFocusAfterLayout && hasFocus() && this.mAdapter != null) {
            child = getFocusedChild();
        }
        ViewHolder focusedVh = child == null ? null : findContainingViewHolder(child);
        if (focusedVh == null) {
            resetFocusInfo();
            return;
        }
        int i;
        this.mState.mFocusedItemId = this.mAdapter.hasStableIds() ? focusedVh.getItemId() : -1;
        State state = this.mState;
        if (this.mDataSetHasChangedAfterLayout) {
            i = -1;
        } else if (focusedVh.isRemoved()) {
            i = focusedVh.mOldPosition;
        } else {
            i = focusedVh.getAdapterPosition();
        }
        state.mFocusedItemPosition = i;
        this.mState.mFocusedSubChildId = getDeepestFocusedViewWithId(focusedVh.itemView);
    }

    private void resetFocusInfo() {
        this.mState.mFocusedItemId = -1;
        this.mState.mFocusedItemPosition = -1;
        this.mState.mFocusedSubChildId = -1;
    }

    private View findNextViewToFocus() {
        int startFocusSearchIndex = this.mState.mFocusedItemPosition != -1 ? this.mState.mFocusedItemPosition : 0;
        int itemCount = this.mState.getItemCount();
        int i = startFocusSearchIndex;
        while (i < itemCount) {
            ViewHolder nextFocus = findViewHolderForAdapterPosition(i);
            if (nextFocus == null) {
                break;
            } else if (nextFocus.itemView.hasFocusable()) {
                return nextFocus.itemView;
            } else {
                i++;
            }
        }
        for (i = Math.min(itemCount, startFocusSearchIndex) - 1; i >= 0; i--) {
            nextFocus = findViewHolderForAdapterPosition(i);
            if (nextFocus == null) {
                return null;
            }
            if (nextFocus.itemView.hasFocusable()) {
                return nextFocus.itemView;
            }
        }
        return null;
    }

    private void recoverFocusFromState() {
        if (this.mPreserveFocusAfterLayout && this.mAdapter != null && hasFocus() && getDescendantFocusability() != 393216) {
            if (getDescendantFocusability() != 131072 || !isFocused()) {
                if (!isFocused()) {
                    View focusedChild = getFocusedChild();
                    if (!IGNORE_DETACHED_FOCUSED_CHILD || (focusedChild.getParent() != null && focusedChild.hasFocus())) {
                        if (!this.mChildHelper.isHidden(focusedChild)) {
                            return;
                        }
                    } else if (this.mChildHelper.getChildCount() == 0) {
                        requestFocus();
                        return;
                    }
                }
                ViewHolder focusTarget = null;
                if (this.mState.mFocusedItemId != -1 && this.mAdapter.hasStableIds()) {
                    focusTarget = findViewHolderForItemId(this.mState.mFocusedItemId);
                }
                View viewToFocus = null;
                if (focusTarget != null && !this.mChildHelper.isHidden(focusTarget.itemView) && focusTarget.itemView.hasFocusable()) {
                    viewToFocus = focusTarget.itemView;
                } else if (this.mChildHelper.getChildCount() > 0) {
                    viewToFocus = findNextViewToFocus();
                }
                if (viewToFocus != null) {
                    if (((long) this.mState.mFocusedSubChildId) != -1) {
                        View child = viewToFocus.findViewById(this.mState.mFocusedSubChildId);
                        if (child != null && child.isFocusable()) {
                            viewToFocus = child;
                        }
                    }
                    viewToFocus.requestFocus();
                }
            }
        }
    }

    private int getDeepestFocusedViewWithId(View view) {
        int lastKnownId = view.getId();
        while (!view.isFocused() && (view instanceof ViewGroup) && view.hasFocus()) {
            view = ((ViewGroup) view).getFocusedChild();
            if (view.getId() != -1) {
                lastKnownId = view.getId();
            }
        }
        return lastKnownId;
    }

    final void fillRemainingScrollValues(State state) {
        if (getScrollState() == 2) {
            SeslOverScroller scroller = this.mViewFlinger.mScroller;
            state.mRemainingScrollHorizontal = scroller.getFinalX() - scroller.getCurrX();
            state.mRemainingScrollVertical = scroller.getFinalY() - scroller.getCurrY();
            return;
        }
        state.mRemainingScrollHorizontal = 0;
        state.mRemainingScrollVertical = 0;
    }

    private void dispatchLayoutStep1() {
        int i;
        this.mState.assertLayoutStep(1);
        fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        startInterceptRequestLayout();
        this.mViewInfoStore.clear();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        saveFocusInfo();
        State state = this.mState;
        boolean z = this.mState.mRunSimpleAnimations && this.mItemsChanged;
        state.mTrackOldChangeHolders = z;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            int count = this.mChildHelper.getChildCount();
            for (i = 0; i < count; i++) {
                ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!holder.shouldIgnore() && (!holder.isInvalid() || this.mAdapter.hasStableIds())) {
                    this.mViewInfoStore.addToPreLayout(holder, this.mItemAnimator.recordPreLayoutInformation(this.mState, holder, ItemAnimator.buildAdapterChangeFlagsForAnimations(holder), holder.getUnmodifiedPayloads()));
                    if (!(!this.mState.mTrackOldChangeHolders || !holder.isUpdated() || holder.isRemoved() || holder.shouldIgnore() || holder.isInvalid())) {
                        this.mViewInfoStore.addToOldChangeHolders(getChangedHolderKey(holder), holder);
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            saveOldPositions();
            boolean didStructureChange = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = didStructureChange;
            for (i = 0; i < this.mChildHelper.getChildCount(); i++) {
                ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!(viewHolder.shouldIgnore() || this.mViewInfoStore.isInPreLayout(viewHolder))) {
                    int flags = ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder);
                    boolean wasHidden = viewHolder.hasAnyOfTheFlags(8192);
                    if (!wasHidden) {
                        flags |= 4096;
                    }
                    ItemHolderInfo animationInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, flags, viewHolder.getUnmodifiedPayloads());
                    if (wasHidden) {
                        recordAnimationInfoIfBouncedHiddenView(viewHolder, animationInfo);
                    } else {
                        this.mViewInfoStore.addToAppearedInPreLayoutHolders(viewHolder, animationInfo);
                    }
                }
            }
            clearOldPositions();
        } else {
            clearOldPositions();
        }
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }

    private void dispatchLayoutStep2() {
        boolean z;
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        State state = this.mState;
        if (!this.mState.mRunSimpleAnimations || this.mItemAnimator == null) {
            z = false;
        } else {
            z = true;
        }
        state.mRunSimpleAnimations = z;
        this.mState.mLayoutStep = 4;
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
    }

    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.mLayoutStep = 1;
        if (this.mState.mRunSimpleAnimations) {
            for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
                ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!holder.shouldIgnore()) {
                    long key = getChangedHolderKey(holder);
                    ItemHolderInfo animationInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, holder);
                    ViewHolder oldChangeViewHolder = this.mViewInfoStore.getFromOldChangeHolders(key);
                    if (oldChangeViewHolder == null || oldChangeViewHolder.shouldIgnore()) {
                        this.mViewInfoStore.addToPostLayout(holder, animationInfo);
                    } else {
                        boolean oldDisappearing = this.mViewInfoStore.isDisappearing(oldChangeViewHolder);
                        boolean newDisappearing = this.mViewInfoStore.isDisappearing(holder);
                        if (oldDisappearing && oldChangeViewHolder == holder) {
                            this.mViewInfoStore.addToPostLayout(holder, animationInfo);
                        } else {
                            ItemHolderInfo preInfo = this.mViewInfoStore.popFromPreLayout(oldChangeViewHolder);
                            this.mViewInfoStore.addToPostLayout(holder, animationInfo);
                            ItemHolderInfo postInfo = this.mViewInfoStore.popFromPostLayout(holder);
                            if (preInfo == null) {
                                handleMissingPreInfoForChangeError(key, holder, oldChangeViewHolder);
                            } else {
                                animateChange(oldChangeViewHolder, holder, preInfo, postInfo, oldDisappearing, newDisappearing);
                            }
                        }
                    }
                }
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLastBlackTop = this.mBlackTop;
        this.mBlackTop = -1;
        if (!(!this.mDrawRect || canScrollVertically(-1) || canScrollVertically(1))) {
            int lastPosition = this.mAdapter.getItemCount() - 1;
            LinearLayoutManager linearLayoutManager = this.mLayout;
            if (linearLayoutManager.mReverseLayout && linearLayoutManager.mStackFromEnd) {
                this.mDrawReverse = true;
                lastPosition = 0;
            } else if (linearLayoutManager.mReverseLayout || linearLayoutManager.mStackFromEnd) {
                this.mDrawRect = false;
                lastPosition = -1;
            }
            if (lastPosition >= 0 && lastPosition <= findLastVisibleItemPosition()) {
                View view = this.mChildHelper.getChildAt(lastPosition);
                if (view != null) {
                    this.mBlackTop = view.getBottom();
                    View tempView = getChildAt(lastPosition);
                    int bottom = -1;
                    if (tempView != null) {
                        bottom = tempView.getBottom();
                    }
                    Log.d(TAG, "dispatchLayoutStep3, lastPosition : " + lastPosition + ", mBlackTop : " + this.mBlackTop + " tempView bottom : " + bottom + ", mDrawReverse : " + this.mDrawReverse);
                }
            }
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
            this.mLayout.mPrefetchMaxCountObserved = 0;
            this.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
            this.mRecycler.updateViewCacheSize();
        }
        this.mLayout.onLayoutCompleted(this.mState);
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        this.mViewInfoStore.clear();
        if (didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1])) {
            dispatchOnScrolled(0, 0);
        }
        recoverFocusFromState();
        resetFocusInfo();
    }

    private void handleMissingPreInfoForChangeError(long key, ViewHolder holder, ViewHolder oldChangeViewHolder) {
        int childCount = this.mChildHelper.getChildCount();
        int i = 0;
        while (i < childCount) {
            ViewHolder other = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (other == holder || getChangedHolderKey(other) != key) {
                i++;
            } else if (this.mAdapter == null || !this.mAdapter.hasStableIds()) {
                throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + other + " \n View Holder 2:" + holder + exceptionLabel());
            } else {
                throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + other + " \n View Holder 2:" + holder + exceptionLabel());
            }
        }
        Log.e(TAG, "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + oldChangeViewHolder + " cannot be found but it is necessary for " + holder + exceptionLabel());
    }

    void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemHolderInfo animationInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            this.mViewInfoStore.addToOldChangeHolders(getChangedHolderKey(viewHolder), viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, animationInfo);
    }

    private void findMinMaxChildLayoutPositions(int[] into) {
        int count = this.mChildHelper.getChildCount();
        if (count == 0) {
            into[0] = -1;
            into[1] = -1;
            return;
        }
        int minPositionPreLayout = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        int maxPositionPreLayout = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (!holder.shouldIgnore()) {
                int pos = holder.getLayoutPosition();
                if (pos < minPositionPreLayout) {
                    minPositionPreLayout = pos;
                }
                if (pos > maxPositionPreLayout) {
                    maxPositionPreLayout = pos;
                }
            }
        }
        into[0] = minPositionPreLayout;
        into[1] = maxPositionPreLayout;
    }

    private boolean didChildRangeChange(int minPositionPreLayout, int maxPositionPreLayout) {
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mMinMaxLayoutPositions[0] == minPositionPreLayout && this.mMinMaxLayoutPositions[1] == maxPositionPreLayout) {
            return false;
        }
        return true;
    }

    protected void removeDetachedView(View child, boolean animate) {
        ViewHolder vh = getChildViewHolderInt(child);
        if (vh != null) {
            if (vh.isTmpDetached()) {
                vh.clearTmpDetachFlag();
            } else if (!vh.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + vh + exceptionLabel());
            }
        }
        child.clearAnimation();
        dispatchChildDetached(child);
        super.removeDetachedView(child, animate);
    }

    long getChangedHolderKey(ViewHolder holder) {
        return this.mAdapter.hasStableIds() ? holder.getItemId() : (long) holder.mPosition;
    }

    void animateAppearance(ViewHolder itemHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        itemHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(itemHolder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    void animateDisappearance(ViewHolder holder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        addAnimatingView(holder);
        holder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(holder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    private void animateChange(ViewHolder oldHolder, ViewHolder newHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo, boolean oldHolderDisappearing, boolean newHolderDisappearing) {
        oldHolder.setIsRecyclable(false);
        if (oldHolderDisappearing) {
            addAnimatingView(oldHolder);
        }
        if (oldHolder != newHolder) {
            if (newHolderDisappearing) {
                addAnimatingView(newHolder);
            }
            oldHolder.mShadowedHolder = newHolder;
            addAnimatingView(oldHolder);
            this.mRecycler.unscrapView(oldHolder);
            newHolder.setIsRecyclable(false);
            newHolder.mShadowingHolder = oldHolder;
        }
        if (this.mItemAnimator.animateChange(oldHolder, newHolder, preInfo, postInfo)) {
            postAnimationRunner();
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        TraceCompat.beginSection(TRACE_ON_LAYOUT_TAG);
        dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
        if (!(this.mFastScroller == null || this.mAdapter == null)) {
            this.mFastScroller.onItemCountChanged(getChildCount(), this.mAdapter.getItemCount());
        }
        if (changed) {
            this.mSizeChnage = true;
            setupGoToTop(-1);
            autoHide(1);
            this.mHasNestedScrollRange = false;
            ViewParent vp = getParent();
            while (vp != null && (vp instanceof ViewGroup)) {
                if (vp instanceof NestedScrollingParent2) {
                    ((ViewGroup) vp).getLocationInWindow(this.mWindowOffsets);
                    int coordinatorBottomY = this.mWindowOffsets[1] + ((ViewGroup) vp).getHeight();
                    getLocationInWindow(this.mWindowOffsets);
                    this.mInitialTopOffsetOfScreen = this.mWindowOffsets[1];
                    this.mRemainNestedScrollRange = getHeight() - (coordinatorBottomY - this.mInitialTopOffsetOfScreen);
                    if (this.mRemainNestedScrollRange < 0) {
                        this.mRemainNestedScrollRange = 0;
                    }
                    this.mNestedScrollRange = this.mRemainNestedScrollRange;
                    this.mHasNestedScrollRange = true;
                } else {
                    vp = vp.getParent();
                }
            }
            if (!this.mHasNestedScrollRange) {
                this.mInitialTopOffsetOfScreen = 0;
                this.mRemainNestedScrollRange = 0;
                this.mNestedScrollRange = 0;
            }
        }
    }

    public void requestLayout() {
        if (this.mInterceptRequestLayoutDepth != 0 || this.mLayoutFrozen) {
            this.mLayoutWasDefered = true;
        } else {
            super.requestLayout();
        }
    }

    void markItemDecorInsetsDirty() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ((LayoutParams) this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }

    public void draw(Canvas c) {
        int padding;
        int i;
        int i2 = 1;
        super.draw(c);
        int count = this.mItemDecorations.size();
        for (int i3 = 0; i3 < count; i3++) {
            ((ItemDecoration) this.mItemDecorations.get(i3)).onDrawOver(c, this, this.mState);
        }
        boolean needsInvalidate = false;
        if (!(this.mLeftGlow == null || this.mLeftGlow.isFinished())) {
            int restore = c.save();
            if (this.mClipToPadding) {
                padding = getPaddingBottom();
            } else {
                padding = 0;
            }
            c.rotate(270.0f);
            c.translate((float) ((-getHeight()) + padding), 0.0f);
            if (this.mLeftGlow == null || !this.mLeftGlow.draw(c)) {
                needsInvalidate = false;
            } else {
                needsInvalidate = true;
            }
            c.restoreToCount(restore);
        }
        if (!(this.mTopGlow == null || this.mTopGlow.isFinished())) {
            restore = c.save();
            if (this.mClipToPadding) {
                c.translate((float) getPaddingLeft(), (float) getPaddingTop());
            }
            if (this.mTopGlow == null || !this.mTopGlow.draw(c)) {
                i = 0;
            } else {
                i = 1;
            }
            needsInvalidate |= i;
            c.restoreToCount(restore);
        }
        if (!(this.mRightGlow == null || this.mRightGlow.isFinished())) {
            restore = c.save();
            int width = getWidth();
            if (this.mClipToPadding) {
                padding = getPaddingTop();
            } else {
                padding = 0;
            }
            c.rotate(90.0f);
            c.translate((float) (-padding), (float) (-width));
            if (this.mRightGlow == null || !this.mRightGlow.draw(c)) {
                i = 0;
            } else {
                i = 1;
            }
            needsInvalidate |= i;
            c.restoreToCount(restore);
        }
        if (!(this.mBottomGlow == null || this.mBottomGlow.isFinished())) {
            restore = c.save();
            c.rotate(180.0f);
            if (this.mClipToPadding) {
                c.translate((float) ((-getWidth()) + getPaddingRight()), (float) ((-getHeight()) + getPaddingBottom()));
            } else {
                c.translate((float) (-getWidth()), (float) (-getHeight()));
            }
            if (this.mBottomGlow == null || !this.mBottomGlow.draw(c)) {
                i2 = 0;
            }
            needsInvalidate |= i2;
            c.restoreToCount(restore);
        }
        if (!needsInvalidate && this.mItemAnimator != null && this.mItemDecorations.size() > 0 && this.mItemAnimator.isRunning()) {
            needsInvalidate = true;
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (this.mEnableGoToTop) {
            drawGoToTop(c);
        }
        if (this.mIsPenDragBlockEnabled && !this.mIsLongPressMultiSelection && this.mLayout != null) {
            if (this.mPenDragBlockLeft != 0 || this.mPenDragBlockTop != 0) {
                int firstChildLayoutPosition = findFirstVisibleItemPosition();
                int lastChildLayoutPosition = (this.mLayout.getChildCount() + firstChildLayoutPosition) - 1;
                if (this.mPenTrackedChildPosition >= firstChildLayoutPosition && this.mPenTrackedChildPosition <= lastChildLayoutPosition) {
                    this.mPenTrackedChild = this.mLayout.getChildAt(this.mPenTrackedChildPosition - firstChildLayoutPosition);
                    int penTrackChildTop = 0;
                    if (this.mPenTrackedChild != null) {
                        penTrackChildTop = this.mPenTrackedChild.getTop();
                    }
                    this.mPenDragStartY = this.mPenDistanceFromTrackedChildTop + penTrackChildTop;
                }
                this.mPenDragBlockTop = this.mPenDragStartY < this.mPenDragEndY ? this.mPenDragStartY : this.mPenDragEndY;
                this.mPenDragBlockBottom = this.mPenDragEndY > this.mPenDragStartY ? this.mPenDragEndY : this.mPenDragStartY;
                this.mPenDragBlockRect.set(this.mPenDragBlockLeft, this.mPenDragBlockTop, this.mPenDragBlockRight, this.mPenDragBlockBottom);
                this.mPenDragBlockImage.setBounds(this.mPenDragBlockRect);
                this.mPenDragBlockImage.draw(c);
            }
        }
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (this.mDrawOutlineStroke) {
            c.drawRect(0.0f, 0.0f, (float) this.mStrokeHeight, (float) getBottom(), this.mStrokePaint);
            c.drawRect((float) (getRight() - this.mStrokeHeight), 0.0f, (float) getRight(), (float) getBottom(), this.mStrokePaint);
        }
        int count = this.mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            ((ItemDecoration) this.mItemDecorations.get(i)).onDraw(c, this, this.mState);
        }
    }

    protected void dispatchDraw(Canvas c) {
        super.dispatchDraw(c);
        int count = this.mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            ((ItemDecoration) this.mItemDecorations.get(i)).seslOnDispatchDraw(c, this, this.mState);
        }
        if (this.mDrawRect && ((this.mBlackTop != -1 || this.mLastBlackTop != -1) && !canScrollVertically(-1) && !canScrollVertically(1))) {
            this.mAnimatedBlackTop = this.mBlackTop;
            if (isAnimating()) {
                View v;
                if (this.mDrawReverse) {
                    v = this.mBlackTop != -1 ? this.mChildHelper.getChildAt(0) : getChildAt(0);
                } else if (this.mBlackTop != -1) {
                    v = this.mChildHelper.getChildAt(this.mChildHelper.getChildCount() - 1);
                } else {
                    v = getChildAt(getChildCount() - 1);
                }
                if (v != null) {
                    this.mAnimatedBlackTop = Math.round(v.getY()) + v.getHeight();
                }
            }
            if (this.mBlackTop != -1 || this.mAnimatedBlackTop != this.mBlackTop) {
                c.drawRect(0.0f, (float) this.mAnimatedBlackTop, (float) getRight(), (float) getBottom(), this.mRectPaint);
                if (this.mDrawLastOutLineStroke) {
                    this.mSeslRoundedCorner.drawRoundedCorner(0, this.mAnimatedBlackTop, getRight(), getBottom(), c);
                }
            }
        } else if (this.mDrawRect && this.mDrawLastItemOutlineStoke && !canScrollVertically(1)) {
            this.mSeslRoundedCorner.drawRoundedCorner(0, getBottom(), getRight(), this.mSeslRoundedCorner.getRoundedCornerRadius() + getBottom(), c);
        }
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && this.mLayout.checkLayoutParams((LayoutParams) p);
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout != null) {
            return this.mLayout.generateDefaultLayoutParams();
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        if (this.mLayout != null) {
            return this.mLayout.generateLayoutParams(getContext(), attrs);
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (this.mLayout != null) {
            return this.mLayout.generateLayoutParams(p);
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    public boolean isAnimating() {
        return this.mItemAnimator != null && this.mItemAnimator.isRunning();
    }

    void saveOldPositions() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.saveOldPosition();
            }
        }
    }

    void clearOldPositions() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }

    void offsetPositionRecordsForMove(int from, int to) {
        int inBetweenOffset;
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        int start;
        int end;
        if (from < to) {
            start = from;
            end = to;
            inBetweenOffset = -1;
        } else {
            start = to;
            end = from;
            inBetweenOffset = 1;
        }
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && holder.mPosition >= start && holder.mPosition <= end) {
                if (holder.mPosition == from) {
                    holder.offsetPosition(to - from, false);
                } else {
                    holder.offsetPosition(inBetweenOffset, false);
                }
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(from, to);
        requestLayout();
    }

    void offsetPositionRecordsForInsert(int positionStart, int itemCount) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!(holder == null || holder.shouldIgnore() || holder.mPosition < positionStart)) {
                holder.offsetPosition(itemCount, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(positionStart, itemCount);
        requestLayout();
    }

    void offsetPositionRecordsForRemove(int positionStart, int itemCount, boolean applyToPreLayout) {
        int positionEnd = positionStart + itemCount;
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!(holder == null || holder.shouldIgnore())) {
                if (holder.mPosition >= positionEnd) {
                    holder.offsetPosition(-itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                } else if (holder.mPosition >= positionStart) {
                    holder.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(positionStart, itemCount, applyToPreLayout);
        requestLayout();
    }

    void viewRangeUpdate(int positionStart, int itemCount, Object payload) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        int positionEnd = positionStart + itemCount;
        for (int i = 0; i < childCount; i++) {
            View child = this.mChildHelper.getUnfilteredChildAt(i);
            ViewHolder holder = getChildViewHolderInt(child);
            if (holder != null && !holder.shouldIgnore() && holder.mPosition >= positionStart && holder.mPosition < positionEnd) {
                holder.addFlags(2);
                holder.addChangePayload(payload);
                ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
            }
        }
        this.mRecycler.viewRangeUpdate(positionStart, itemCount);
    }

    boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        return this.mItemAnimator == null || this.mItemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }

    void processDataSetCompletelyChanged(boolean dispatchItemsChanged) {
        this.mDispatchItemsChangedEvent |= dispatchItemsChanged;
        this.mDataSetHasChangedAfterLayout = true;
        markKnownViewsInvalid();
    }

    void markKnownViewsInvalid() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!(holder == null || holder.shouldIgnore())) {
                holder.addFlags(6);
            }
        }
        markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() != 0) {
            if (this.mLayout != null) {
                this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
            }
            markItemDecorInsetsDirty();
            requestLayout();
        }
    }

    public boolean getPreserveFocusAfterLayout() {
        return this.mPreserveFocusAfterLayout;
    }

    public void setPreserveFocusAfterLayout(boolean preserveFocusAfterLayout) {
        this.mPreserveFocusAfterLayout = preserveFocusAfterLayout;
    }

    public ViewHolder getChildViewHolder(View child) {
        Object parent = child.getParent();
        if (parent == null || parent == this) {
            return getChildViewHolderInt(child);
        }
        throw new IllegalArgumentException("View " + child + " is not a direct child of " + this);
    }

    public View findContainingItemView(View view) {
        View parent = view.getParent();
        while (parent != null && parent != this && (parent instanceof View)) {
            view = parent;
            parent = view.getParent();
        }
        return parent == this ? view : null;
    }

    public ViewHolder findContainingViewHolder(View view) {
        View itemView = findContainingItemView(view);
        return itemView == null ? null : getChildViewHolder(itemView);
    }

    static ViewHolder getChildViewHolderInt(View child) {
        if (child == null) {
            return null;
        }
        return ((LayoutParams) child.getLayoutParams()).mViewHolder;
    }

    @Deprecated
    public int getChildPosition(View child) {
        return getChildAdapterPosition(child);
    }

    public int getChildAdapterPosition(View child) {
        ViewHolder holder = getChildViewHolderInt(child);
        return holder != null ? holder.getAdapterPosition() : -1;
    }

    public int getChildLayoutPosition(View child) {
        ViewHolder holder = getChildViewHolderInt(child);
        return holder != null ? holder.getLayoutPosition() : -1;
    }

    public long getChildItemId(View child) {
        if (this.mAdapter == null || !this.mAdapter.hasStableIds()) {
            return -1;
        }
        ViewHolder holder = getChildViewHolderInt(child);
        if (holder != null) {
            return holder.getItemId();
        }
        return -1;
    }

    @Deprecated
    public ViewHolder findViewHolderForPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    public ViewHolder findViewHolderForLayoutPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    public ViewHolder findViewHolderForAdapterPosition(int position) {
        if (this.mDataSetHasChangedAfterLayout) {
            return null;
        }
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!(holder == null || holder.isRemoved() || getAdapterPositionFor(holder) != position)) {
                if (!this.mChildHelper.isHidden(holder.itemView)) {
                    return holder;
                }
                hidden = holder;
            }
        }
        return hidden;
    }

    ViewHolder findViewHolderForPosition(int position, boolean checkNewPosition) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!(childViewHolderInt == null || childViewHolderInt.isRemoved())) {
                if (checkNewPosition) {
                    if (childViewHolderInt.mPosition != position) {
                        continue;
                    }
                } else if (childViewHolderInt.getLayoutPosition() != position) {
                    continue;
                }
                if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                    return childViewHolderInt;
                }
                hidden = childViewHolderInt;
            }
        }
        return hidden;
    }

    public ViewHolder findViewHolderForItemId(long id) {
        if (this.mAdapter == null || !this.mAdapter.hasStableIds()) {
            return null;
        }
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!(holder == null || holder.isRemoved() || holder.getItemId() != id)) {
                if (!this.mChildHelper.isHidden(holder.itemView)) {
                    return holder;
                }
                hidden = holder;
            }
        }
        return hidden;
    }

    public View findChildViewUnder(float x, float y) {
        for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
            View child = this.mChildHelper.getChildAt(i);
            float translationX = child.getTranslationX();
            float translationY = child.getTranslationY();
            if (x >= ((float) child.getLeft()) + translationX && x <= ((float) child.getRight()) + translationX && y >= ((float) child.getTop()) + translationY && y <= ((float) child.getBottom()) + translationY) {
                return child;
            }
        }
        return null;
    }

    public View seslFindNearChildViewUnder(float x, float y) {
        int i;
        int count = this.mChildHelper.getChildCount();
        int positionX = (int) (0.5f + x);
        int positionY = (int) (0.5f + y);
        int adjustY = positionY;
        int oldDistanceY = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        int previousChildCenter = 0;
        for (i = count - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child != null) {
                int childCenter = (child.getTop() + child.getBottom()) / 2;
                if (previousChildCenter != childCenter) {
                    previousChildCenter = childCenter;
                    int newDistanceY = Math.abs(positionY - childCenter);
                    if (newDistanceY >= oldDistanceY) {
                        break;
                    }
                    oldDistanceY = newDistanceY;
                    adjustY = childCenter;
                } else {
                    continue;
                }
            }
        }
        int oldDistanceFromLeft = 0;
        int oldDistanceFromRight = 0;
        int closeIndexByLeft = 0;
        int closeIndexByRight = 0;
        i = count - 1;
        while (i >= 0) {
            child = getChildAt(i);
            if (child != null) {
                int childTop = child.getTop();
                int childBottom = child.getBottom();
                int childLeft = child.getLeft();
                int childRight = child.getRight();
                if (i == count - 1) {
                    closeIndexByLeft = count - 1;
                    closeIndexByRight = count - 1;
                    oldDistanceFromLeft = Math.abs(positionX - childLeft);
                    oldDistanceFromRight = Math.abs(positionX - childRight);
                }
                if (adjustY >= childTop && adjustY <= childBottom) {
                    int newDistanceFromLeft = Math.abs(positionX - childLeft);
                    int newDistanceFromRight = Math.abs(positionX - childRight);
                    if (newDistanceFromLeft <= oldDistanceFromLeft) {
                        closeIndexByLeft = i;
                        oldDistanceFromLeft = newDistanceFromLeft;
                    }
                    if (newDistanceFromRight <= oldDistanceFromRight) {
                        closeIndexByRight = i;
                        oldDistanceFromRight = newDistanceFromRight;
                    }
                }
                if (adjustY > childBottom || i == 0) {
                    if (oldDistanceFromLeft < oldDistanceFromRight) {
                        return this.mChildHelper.getChildAt(closeIndexByLeft);
                    }
                    return this.mChildHelper.getChildAt(closeIndexByRight);
                }
            }
            i--;
        }
        Log.e(TAG, "findNearChildViewUnder didn't find valid child view! " + x + ", " + y);
        return null;
    }

    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    public void offsetChildrenVertical(int dy) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(dy);
        }
    }

    public void onChildAttachedToWindow(View child) {
    }

    public void onChildDetachedFromWindow(View child) {
    }

    public void offsetChildrenHorizontal(int dx) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(dx);
        }
    }

    public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
        getDecoratedBoundsWithMarginsInt(view, outBounds);
    }

    static void getDecoratedBoundsWithMarginsInt(View view, Rect outBounds) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        Rect insets = lp.mDecorInsets;
        outBounds.set((view.getLeft() - insets.left) - lp.leftMargin, (view.getTop() - insets.top) - lp.topMargin, (view.getRight() + insets.right) + lp.rightMargin, (view.getBottom() + insets.bottom) + lp.bottomMargin);
    }

    Rect getItemDecorInsetsForChild(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (!lp.mInsetsDirty) {
            return lp.mDecorInsets;
        }
        if (this.mState.isPreLayout() && (lp.isItemChanged() || lp.isViewInvalid())) {
            return lp.mDecorInsets;
        }
        Rect insets = lp.mDecorInsets;
        insets.set(0, 0, 0, 0);
        int decorCount = this.mItemDecorations.size();
        for (int i = 0; i < decorCount; i++) {
            this.mTempRect.set(0, 0, 0, 0);
            ((ItemDecoration) this.mItemDecorations.get(i)).getItemOffsets(this.mTempRect, child, this, this.mState);
            insets.left += this.mTempRect.left;
            insets.top += this.mTempRect.top;
            insets.right += this.mTempRect.right;
            insets.bottom += this.mTempRect.bottom;
        }
        lp.mInsetsDirty = false;
        return insets;
    }

    public void onScrolled(int dx, int dy) {
    }

    void dispatchOnScrolled(int hresult, int vresult) {
        this.mDispatchScrollCounter++;
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        onScrollChanged(scrollX, scrollY, scrollX, scrollY);
        onScrolled(hresult, vresult);
        if (!(this.mFastScroller == null || this.mAdapter == null)) {
            this.mFastScroller.onScroll(findFirstVisibleItemPosition(), getChildCount(), this.mAdapter.getItemCount());
        }
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrolled(this, hresult, vresult);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; i--) {
                ((OnScrollListener) this.mScrollListeners.get(i)).onScrolled(this, hresult, vresult);
            }
        }
        this.mDispatchScrollCounter--;
    }

    public void onScrollStateChanged(int state) {
    }

    void dispatchOnScrollStateChanged(int state) {
        if (this.mLayout != null) {
            this.mLayout.onScrollStateChanged(state);
        }
        onScrollStateChanged(state);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(this, state);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; i--) {
                ((OnScrollListener) this.mScrollListeners.get(i)).onScrollStateChanged(this, state);
            }
        }
    }

    public boolean hasPendingAdapterUpdates() {
        return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
    }

    void repositionShadowingViews() {
        int count = this.mChildHelper.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.mChildHelper.getChildAt(i);
            ViewHolder holder = getChildViewHolder(view);
            if (!(holder == null || holder.mShadowingHolder == null)) {
                View shadowingView = holder.mShadowingHolder.itemView;
                int left = view.getLeft();
                int top = view.getTop();
                if (left != shadowingView.getLeft() || top != shadowingView.getTop()) {
                    shadowingView.layout(left, top, shadowingView.getWidth() + left, shadowingView.getHeight() + top);
                }
            }
        }
    }

    static RecyclerView findNestedRecyclerView(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof RecyclerView) {
            return (RecyclerView) view;
        }
        ViewGroup parent = (ViewGroup) view;
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View descendant = findNestedRecyclerView(parent.getChildAt(i));
            if (descendant != null) {
                return descendant;
            }
        }
        return null;
    }

    static void clearNestedRecyclerViewIfNotNested(ViewHolder holder) {
        if (holder.mNestedRecyclerView != null) {
            View item = (View) holder.mNestedRecyclerView.get();
            while (item != null) {
                if (item != holder.itemView) {
                    ViewParent parent = item.getParent();
                    if (parent instanceof View) {
                        item = (View) parent;
                    } else {
                        item = null;
                    }
                } else {
                    return;
                }
            }
            holder.mNestedRecyclerView = null;
        }
    }

    long getNanoTime() {
        if (ALLOW_THREAD_GAP_WORK) {
            return System.nanoTime();
        }
        return 0;
    }

    void dispatchChildDetached(View child) {
        ViewHolder viewHolder = getChildViewHolderInt(child);
        onChildDetachedFromWindow(child);
        if (!(this.mAdapter == null || viewHolder == null)) {
            this.mAdapter.onViewDetachedFromWindow(viewHolder);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; i--) {
                ((OnChildAttachStateChangeListener) this.mOnChildAttachStateListeners.get(i)).onChildViewDetachedFromWindow(child);
            }
        }
    }

    void dispatchChildAttached(View child) {
        ViewHolder viewHolder = getChildViewHolderInt(child);
        onChildAttachedToWindow(child);
        if (!(this.mAdapter == null || viewHolder == null)) {
            this.mAdapter.onViewAttachedToWindow(viewHolder);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; i--) {
                ((OnChildAttachStateChangeListener) this.mOnChildAttachStateListeners.get(i)).onChildViewAttachedToWindow(child);
            }
        }
    }

    boolean setChildImportantForAccessibilityInternal(ViewHolder viewHolder, int importantForAccessibility) {
        if (isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = importantForAccessibility;
            this.mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        ViewCompat.setImportantForAccessibility(viewHolder.itemView, importantForAccessibility);
        return true;
    }

    void dispatchPendingImportantForAccessibilityChanges() {
        for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; i--) {
            ViewHolder viewHolder = (ViewHolder) this.mPendingAccessibilityImportanceChange.get(i);
            if (viewHolder.itemView.getParent() == this && !viewHolder.shouldIgnore()) {
                int state = viewHolder.mPendingAccessibilityState;
                if (state != -1) {
                    ViewCompat.setImportantForAccessibility(viewHolder.itemView, state);
                    viewHolder.mPendingAccessibilityState = -1;
                }
            }
        }
        this.mPendingAccessibilityImportanceChange.clear();
    }

    int getAdapterPositionFor(ViewHolder viewHolder) {
        if (viewHolder.hasAnyOfTheFlags(524) || !viewHolder.isBound()) {
            return -1;
        }
        return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
    }

    void initFastScroller(StateListDrawable verticalThumbDrawable, Drawable verticalTrackDrawable, StateListDrawable horizontalThumbDrawable, Drawable horizontalTrackDrawable) {
        if (verticalThumbDrawable == null || verticalTrackDrawable == null || horizontalThumbDrawable == null || horizontalTrackDrawable == null) {
            throw new IllegalArgumentException("Trying to set fast scroller without both required drawables." + exceptionLabel());
        }
        Resources resources = getContext().getResources();
        FastScroller fastScroller = new FastScroller(this, verticalThumbDrawable, verticalTrackDrawable, horizontalThumbDrawable, horizontalTrackDrawable, resources.getDimensionPixelSize(C0270R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(C0270R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(C0270R.dimen.fastscroll_margin));
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    public boolean startNestedScroll(int axes, int type) {
        return getScrollingChildHelper().startNestedScroll(axes, type);
    }

    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    public void stopNestedScroll(int type) {
        getScrollingChildHelper().stopNestedScroll(type);
    }

    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    public boolean hasNestedScrollingParent(int type) {
        return getScrollingChildHelper().hasNestedScrollingParent(type);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (this.mChildDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(childCount, i);
        }
        return this.mChildDrawingOrderCallback.onGetChildDrawingOrder(childCount, i);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper == null) {
            this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return this.mScrollingChildHelper;
    }

    public void seslSetFastScrollerEnabled(boolean enabled) {
        if (this.mFastScroller != null) {
            this.mFastScroller.setEnabled(enabled);
        } else if (enabled) {
            this.mFastScroller = new SeslRecyclerViewFastScroller(this, 1);
            this.mFastScroller.setEnabled(true);
            this.mFastScroller.setScrollbarPosition(getVerticalScrollbarPosition());
        }
        this.mFastScrollerEnabled = enabled;
        if (this.mFastScroller != null) {
            this.mFastScroller.updateLayout();
        }
    }

    public boolean seslIsFastScrollerEnabled() {
        return this.mFastScrollerEnabled;
    }

    public boolean isVerticalScrollBarEnabled() {
        return !this.mFastScrollerEnabled && super.isVerticalScrollBarEnabled();
    }

    protected boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && (p instanceof ViewGroup)) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    public void seslSetFastScrollerEventListener(SeslFastScrollerEventListener l) {
        this.mFastScrollerEventListener = l;
    }

    private boolean contentFits() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        if (childCount != this.mAdapter.getItemCount()) {
            return false;
        }
        if (getChildAt(0).getTop() < this.mListPadding.top || getChildAt(childCount - 1).getBottom() > getHeight() - this.mListPadding.bottom) {
            return false;
        }
        return true;
    }

    private boolean isInDialog() {
        boolean isInDialog;
        boolean isInDialog2 = false;
        if (this.mRootViewCheckForDialog == null) {
            this.mRootViewCheckForDialog = getRootView();
            if (this.mRootViewCheckForDialog == null) {
                isInDialog = false;
                return false;
            }
            Context context = this.mRootViewCheckForDialog.getContext();
            if ((context instanceof Activity) && ((Activity) context).getWindow().getAttributes().type == 1) {
                isInDialog2 = false;
            } else {
                isInDialog2 = true;
            }
        }
        isInDialog = isInDialog2;
        return isInDialog2;
    }

    private boolean showPointerIcon(MotionEvent ev, int iconId) {
        SeslInputDeviceReflector.setPointerType(ev.getDevice(), iconId);
        return true;
    }

    private boolean canScrollUp() {
        boolean canScrollUp;
        if (findFirstChildPosition() > 0) {
            canScrollUp = true;
        } else {
            canScrollUp = false;
        }
        if (canScrollUp || getChildCount() <= 0) {
            return canScrollUp;
        }
        if (getChildAt(0).getTop() < getPaddingTop()) {
            return true;
        }
        return false;
    }

    private boolean canScrollDown() {
        int count = getChildCount();
        if (this.mAdapter == null) {
            Log.e(TAG, "No adapter attached; skipping canScrollDown");
            return false;
        }
        boolean canScrollDown;
        if (findFirstChildPosition() + count < this.mAdapter.getItemCount()) {
            canScrollDown = true;
        } else {
            canScrollDown = false;
        }
        if (!canScrollDown && count > 0) {
            if (getChildAt(count - 1).getBottom() > getBottom() - this.mListPadding.bottom) {
                canScrollDown = true;
            } else {
                canScrollDown = false;
            }
        }
        return canScrollDown;
    }

    private int findFirstChildPosition() {
        int firstPosition = 0;
        if (this.mLayout instanceof LinearLayoutManager) {
            firstPosition = ((LinearLayoutManager) this.mLayout).findFirstVisibleItemPosition();
        } else if (this.mLayout instanceof StaggeredGridLayoutManager) {
            firstPosition = ((StaggeredGridLayoutManager) this.mLayout).findFirstVisibleItemPositions(null)[this.mLayout.getLayoutDirection() == 1 ? ((StaggeredGridLayoutManager) this.mLayout).getSpanCount() - 1 : 0];
        }
        if (firstPosition == -1) {
            return 0;
        }
        return firstPosition;
    }

    public boolean isLockScreenMode() {
        Context context = this.mContext;
        Context context2 = this.mContext;
        return ((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode() || !true;
    }

    public void seslSetHoverScrollEnabled(boolean enabled) {
        this.mHoverScrollEnable = enabled;
        this.mHoverScrollStateChanged = true;
    }

    public int findFirstVisibleItemPosition() {
        if (this.mLayout instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) this.mLayout).findFirstVisibleItemPosition();
        }
        if (this.mLayout instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) this.mLayout).findFirstVisibleItemPositions(null)[0];
        }
        return -1;
    }

    public int findLastVisibleItemPosition() {
        if (this.mLayout instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) this.mLayout).findLastVisibleItemPosition();
        }
        if (this.mLayout instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) this.mLayout).findLastVisibleItemPositions(null)[0];
        }
        return -1;
    }

    private boolean pageScroll(int direction) {
        if (this.mAdapter == null) {
            Log.e(TAG, "No adapter attached; skipping pageScroll");
            return false;
        }
        int itemCount = this.mAdapter.getItemCount();
        if (itemCount <= 0) {
            return false;
        }
        int pos;
        switch (direction) {
            case 0:
                pos = findFirstVisibleItemPosition() - getChildCount();
                break;
            case 1:
                pos = findLastVisibleItemPosition() + getChildCount();
                break;
            case 2:
                pos = 0;
                break;
            case 3:
                pos = itemCount - 1;
                break;
            default:
                return false;
        }
        if (pos > itemCount - 1) {
            pos = itemCount - 1;
        } else if (pos < 0) {
            pos = 0;
        }
        this.mLayout.mRecyclerView.scrollToPosition(pos);
        this.mLayout.mRecyclerView.post(new Runnable() {
            public void run() {
                View view = RecyclerView.this.getChildAt(0);
                if (view != null) {
                    view.requestFocus();
                }
            }
        });
        return true;
    }

    protected boolean dispatchHoverEvent(MotionEvent ev) {
        if (this.mAdapter == null) {
            Log.d(TAG, "No adapter attached; skipping hover scroll");
            return super.dispatchHoverEvent(ev);
        }
        int action = ev.getAction();
        int toolType = ev.getToolType(0);
        this.mIsMouseWheel = false;
        if ((action == 7 || action == 9) && toolType == 2) {
            this.mIsPenHovered = true;
        } else if (action == 10) {
            this.mIsPenHovered = false;
        }
        this.mNewTextViewHoverState = SeslTextViewReflector.isTextViewHovered();
        if (!this.mNewTextViewHoverState && this.mOldTextViewHoverState && this.mIsPenDragBlockEnabled && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) {
            this.mIsNeedPenSelectIconSet = true;
        } else {
            this.mIsNeedPenSelectIconSet = false;
        }
        this.mOldTextViewHoverState = this.mNewTextViewHoverState;
        if (action == 9 || this.mHoverScrollStateChanged) {
            this.mNeedsHoverScroll = true;
            this.mHoverScrollStateChanged = false;
            if (this.mHasNestedScrollRange) {
                adjustNestedScrollRange();
            }
            if (!(SeslViewReflector.isHoveringUIEnabled(this) && this.mHoverScrollEnable)) {
                this.mNeedsHoverScroll = false;
            }
            if (this.mNeedsHoverScroll && toolType == 2) {
                boolean isHoveringOn = System.getInt(this.mContext.getContentResolver(), SeslSystemReflector.getField_SEM_PEN_HOVERING(), 0) == 1;
                boolean isCarModeOn = false;
                try {
                    isCarModeOn = System.getInt(this.mContext.getContentResolver(), "car_mode_on") == 1;
                } catch (SettingNotFoundException e) {
                    Log.i(TAG, "dispatchHoverEvent car_mode_on SettingNotFoundException");
                }
                if (!isHoveringOn || isCarModeOn) {
                    this.mNeedsHoverScroll = false;
                }
                if (isHoveringOn && this.mIsPenDragBlockEnabled && !this.mIsPenSelectPointerSetted && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) {
                    showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_PEN_SELECT());
                    this.mIsPenSelectPointerSetted = true;
                }
            }
            if (this.mNeedsHoverScroll && toolType == 3) {
                this.mNeedsHoverScroll = false;
            }
        } else if (action == 7) {
            if ((this.mIsPenDragBlockEnabled && !this.mIsPenSelectPointerSetted && ev.getToolType(0) == 2 && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) || this.mIsNeedPenSelectIconSet) {
                showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_PEN_SELECT());
                this.mIsPenSelectPointerSetted = true;
            } else if (this.mIsPenDragBlockEnabled && this.mIsPenSelectPointerSetted && ev.getButtonState() != 32 && ev.getButtonState() != 2) {
                showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
                this.mIsPenSelectPointerSetted = false;
            }
        } else if (action == 10 && this.mIsPenSelectPointerSetted) {
            showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
            this.mIsPenSelectPointerSetted = false;
        }
        if (!this.mNeedsHoverScroll) {
            return super.dispatchHoverEvent(ev);
        }
        int contentTop;
        int contentBottom;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int childCount = getChildCount();
        if (this.mIsEnabledPaddingInHoverScroll) {
            contentTop = this.mListPadding.top;
            contentBottom = getHeight() - this.mListPadding.bottom;
        } else {
            contentTop = this.mExtraPaddingInTopHoverArea;
            contentBottom = getHeight() - this.mExtraPaddingInBottomHoverArea;
        }
        boolean canScrollDown = findFirstChildPosition() + childCount < this.mAdapter.getItemCount();
        if (!canScrollDown && childCount > 0) {
            View child = getChildAt(childCount - 1);
            canScrollDown = child.getBottom() > getBottom() - this.mListPadding.bottom || child.getBottom() > getHeight() - this.mListPadding.bottom;
        }
        boolean canScrollUp = findFirstChildPosition() > 0;
        if (!canScrollUp && childCount > 0) {
            canScrollUp = getChildAt(0).getTop() < this.mListPadding.top;
        }
        boolean isPossibleTooltype = ev.getToolType(0) == 2;
        if ((y <= this.mHoverTopAreaHeight + contentTop || y >= (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange) && x > 0 && x <= getRight() && ((canScrollUp || canScrollDown) && ((y < contentTop || y > this.mHoverTopAreaHeight + contentTop || canScrollUp || !this.mIsHoverOverscrolled) && ((y < (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange || y > contentBottom - this.mRemainNestedScrollRange || canScrollDown || !this.mIsHoverOverscrolled) && !((isPossibleTooltype && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) || !isPossibleTooltype || isLockScreenMode()))))) {
            if (this.mHasNestedScrollRange && this.mRemainNestedScrollRange > 0 && this.mRemainNestedScrollRange != this.mNestedScrollRange) {
                adjustNestedScrollRange();
            }
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = System.currentTimeMillis();
            }
            switch (action) {
                case 7:
                    if (this.mHoverAreaEnter) {
                        if (y >= contentTop && y <= this.mHoverTopAreaHeight + contentTop) {
                            if (!this.mHoverHandler.hasMessages(0)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 1) {
                                    showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_SCROLL_UP());
                                }
                                this.mHoverScrollDirection = 2;
                                this.mHoverHandler.sendEmptyMessage(0);
                                break;
                            }
                        } else if (y >= (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange && y <= contentBottom - this.mRemainNestedScrollRange) {
                            if (!this.mHoverHandler.hasMessages(0)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 2) {
                                    showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_SCROLL_DOWN());
                                }
                                this.mHoverScrollDirection = 1;
                                this.mHoverHandler.sendEmptyMessage(0);
                                break;
                            }
                        } else {
                            if (this.mHoverHandler.hasMessages(0)) {
                                this.mHoverHandler.removeMessages(0);
                                if (this.mScrollState == 1) {
                                    setScrollState(0);
                                }
                            }
                            showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
                            this.mHoverRecognitionStartTime = 0;
                            this.mHoverScrollStartTime = 0;
                            this.mIsHoverOverscrolled = false;
                            this.mHoverAreaEnter = false;
                            this.mIsSendHoverScrollState = false;
                            break;
                        }
                    }
                    this.mHoverAreaEnter = true;
                    ev.setAction(10);
                    return super.dispatchHoverEvent(ev);
                    break;
                case 9:
                    this.mHoverAreaEnter = true;
                    if (y >= contentTop && y <= this.mHoverTopAreaHeight + contentTop) {
                        if (!this.mHoverHandler.hasMessages(0)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_SCROLL_UP());
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(0);
                            break;
                        }
                    } else if (y >= (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange && y <= contentBottom - this.mRemainNestedScrollRange && !this.mHoverHandler.hasMessages(0)) {
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_SCROLL_DOWN());
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendEmptyMessage(0);
                        break;
                    }
                    break;
                case 10:
                    if (this.mHoverHandler.hasMessages(0)) {
                        this.mHoverHandler.removeMessages(0);
                    }
                    if (this.mScrollState == 1) {
                        setScrollState(0);
                    }
                    showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
                    this.mHoverRecognitionStartTime = 0;
                    this.mHoverScrollStartTime = 0;
                    this.mIsHoverOverscrolled = false;
                    this.mHoverAreaEnter = false;
                    this.mIsSendHoverScrollState = false;
                    if (this.mHoverScrollStateForListener != 0) {
                        this.mHoverScrollStateForListener = 0;
                        if (this.mScrollListener != null) {
                            this.mScrollListener.onScrollStateChanged(this, 0);
                        }
                    }
                    return super.dispatchHoverEvent(ev);
            }
            return true;
        }
        if (this.mHasNestedScrollRange && this.mRemainNestedScrollRange > 0 && this.mRemainNestedScrollRange != this.mNestedScrollRange) {
            adjustNestedScrollRange();
        }
        if (this.mHoverHandler.hasMessages(0)) {
            this.mHoverHandler.removeMessages(0);
            showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
            if (this.mScrollState == 1) {
                setScrollState(0);
            }
        }
        if ((y > this.mHoverTopAreaHeight + contentTop && y < (contentBottom - this.mHoverBottomAreaHeight) - this.mRemainNestedScrollRange) || x <= 0 || x > getRight()) {
            this.mIsHoverOverscrolled = false;
        }
        if (this.mHoverAreaEnter || this.mHoverScrollStartTime != 0) {
            showPointerIcon(ev, SeslPointerIconReflector.getField_SEM_TYPE_STYLUS_DEFAULT());
        }
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollStartTime = 0;
        this.mHoverAreaEnter = false;
        this.mIsSendHoverScrollState = false;
        if (action == 10 && this.mHoverScrollStateForListener != 0) {
            this.mHoverScrollStateForListener = 0;
            if (this.mScrollListener != null) {
                this.mScrollListener.onScrollStateChanged(this, 0);
            }
        }
        return super.dispatchHoverEvent(ev);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 92:
                if (event.hasNoModifiers()) {
                    pageScroll(0);
                    break;
                }
                break;
            case 93:
                if (event.hasNoModifiers()) {
                    pageScroll(1);
                    break;
                }
                break;
            case 113:
            case 114:
                this.mIsCtrlKeyPressed = true;
                break;
            case 122:
                if (event.hasNoModifiers()) {
                    pageScroll(2);
                    break;
                }
                break;
            case 123:
                if (event.hasNoModifiers()) {
                    pageScroll(3);
                    break;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 113:
            case 114:
                this.mIsCtrlKeyPressed = false;
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void seslSetOnMultiSelectedListener(SeslOnMultiSelectedListener listener) {
        this.mOnMultiSelectedListener = listener;
    }

    public final SeslOnMultiSelectedListener seslGetOnMultiSelectedListener() {
        return this.mOnMultiSelectedListener;
    }

    public void seslSetPenSelectionEnabled(boolean enabled) {
        this.mIsPenSelectionEnabled = enabled;
    }

    public void seslSetLongPressMultiSelectionListener(SeslLongPressMultiSelectionListener listener) {
        this.mLongPressMultiSelectionListener = listener;
    }

    public final SeslLongPressMultiSelectionListener getLongPressMultiSelectionListener() {
        return this.mLongPressMultiSelectionListener;
    }

    public void seslSetSmoothScrollEnabled(boolean enabled) {
        if (this.mViewFlinger != null) {
            this.mViewFlinger.mScroller.setSmoothScrollEnabled(enabled);
        }
    }

    public void seslSetRegulationEnabled(boolean enabled) {
        if (this.mViewFlinger != null) {
            this.mViewFlinger.mScroller.setRegulationEnabled(enabled);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 19:
            case 20:
                if (event.getAction() == 0) {
                    this.mIsArrowKeyPressed = true;
                    break;
                }
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public void seslSetPagingTouchSlopForStylus(boolean enabled) {
        this.mUsePagingTouchSlopForStylus = enabled;
    }

    public boolean seslIsPagingTouchSlopForStylusEnabled() {
        return this.mUsePagingTouchSlopForStylus;
    }
}
