package android.support.design.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.C0011R;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.support.v4.math.MathUtils;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@DefaultBehavior(Behavior.class)
public class AppBarLayout extends LinearLayout {
    static final Interpolator SINE_OUT_80_INTERPOLATOR = new PathInterpolator(0.17f, 0.17f, 0.2f, 1.0f);
    private static float mAppBarHeight;
    private Drawable mBackground;
    private int mBottomPadding;
    private boolean mCollapsed;
    private boolean mCollapsible;
    private int mCurrentOrientation;
    private int mDownPreScrollRange;
    private int mDownScrollRange;
    private boolean mHaveChildWithInterpolator;
    private float mHeightCustom;
    private float mHeightPercent;
    private boolean mIsCollapsed;
    public boolean mIsSetCollapsedHeight;
    private WindowInsetsCompat mLastInsets;
    private List<OnOffsetChangedListener> mListeners;
    private int mPendingAction;
    private int[] mTmpStatesArray;
    private int mTotalScrollRange;

    /* renamed from: android.support.design.widget.AppBarLayout$1 */
    class C00181 implements OnApplyWindowInsetsListener {
        C00181() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return AppBarLayout.this.onWindowInsetChanged(insets);
        }
    }

    public static class Behavior extends HeaderBehavior<AppBarLayout> {
        private int lastStartedType;
        private float mDiffY_Touch;
        private boolean mIsCollapsed;
        private boolean mIsFlingScrollDown = false;
        private boolean mIsFlingScrollUp = false;
        private boolean mIsScrollHold = false;
        boolean mIsSetStaticDuration = false;
        private float mLastMotionY_Touch;
        private WeakReference<View> mLastNestedScrollingChildRef;
        private ValueAnimator mOffsetAnimator;
        private int mOffsetDelta;
        private int mOffsetToChildIndexOnLayout = -1;
        private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
        private float mOffsetToChildIndexOnLayoutPerc;
        private DragCallback mOnDragCallback;
        private boolean mToolisMouse;
        private int mTouchSlop = -1;
        private float mVelocity = 0.0f;
        private float touchX;
        private float touchY;

        public static abstract class DragCallback {
            public abstract boolean canDrag(AppBarLayout appBarLayout);
        }

        protected static class SavedState extends AbsSavedState {
            public static final Creator<SavedState> CREATOR = new C00201();
            boolean firstVisibleChildAtMinimumHeight;
            int firstVisibleChildIndex;
            float firstVisibleChildPercentageShown;

            /* renamed from: android.support.design.widget.AppBarLayout$Behavior$SavedState$1 */
            static class C00201 implements ClassLoaderCreator<SavedState> {
                C00201() {
                }

                public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                    return new SavedState(source, loader);
                }

                public SavedState createFromParcel(Parcel source) {
                    return new SavedState(source, null);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            }

            public SavedState(Parcel source, ClassLoader loader) {
                super(source, loader);
                this.firstVisibleChildIndex = source.readInt();
                this.firstVisibleChildPercentageShown = source.readFloat();
                this.firstVisibleChildAtMinimumHeight = source.readByte() != (byte) 0;
            }

            public SavedState(Parcelable superState) {
                super(superState);
            }

            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(this.firstVisibleChildIndex);
                dest.writeFloat(this.firstVisibleChildPercentageShown);
                dest.writeByte((byte) (this.firstVisibleChildAtMinimumHeight ? 1 : 0));
            }
        }

        public /* bridge */ /* synthetic */ int getLastInterceptTouchEventEvent() {
            return super.getLastInterceptTouchEventEvent();
        }

        public /* bridge */ /* synthetic */ int getLastTouchEventEvent() {
            return super.getLastTouchEventEvent();
        }

        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i) {
            return super.setTopAndBottomOffset(i);
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
            boolean z = true;
            if (this.mTouchSlop < 0) {
                this.mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
            }
            switch (ev.getAction()) {
                case 0:
                    this.touchX = ev.getX();
                    this.touchY = ev.getY();
                    this.mLastMotionY_Touch = this.touchY;
                    this.mDiffY_Touch = 0.0f;
                    break;
                case 1:
                case 3:
                    if (Math.abs(this.mDiffY_Touch) <= 21.0f) {
                        this.touchX = 0.0f;
                        this.touchY = 0.0f;
                        this.mIsFlingScrollUp = false;
                        this.mIsFlingScrollDown = false;
                        this.mLastMotionY_Touch = 0.0f;
                    } else if (this.mDiffY_Touch < 0.0f) {
                        this.mIsFlingScrollUp = true;
                        this.mIsFlingScrollDown = false;
                    } else if (this.mDiffY_Touch > 0.0f) {
                        this.mIsFlingScrollUp = false;
                        this.mIsFlingScrollDown = true;
                    }
                    snapToChildIfNeeded(parent, child);
                    break;
                case 2:
                    if (ev == null || !MotionEventCompat.isFromSource(ev, 8194)) {
                        z = false;
                    }
                    this.mToolisMouse = z;
                    float y = ev.getY();
                    if (y - this.mLastMotionY_Touch != 0.0f) {
                        this.mDiffY_Touch = y - this.mLastMotionY_Touch;
                    }
                    if (Math.abs(this.mDiffY_Touch) > ((float) this.mTouchSlop)) {
                        this.mLastMotionY_Touch = y;
                        break;
                    }
                    break;
            }
            return super.onTouchEvent(parent, child, ev);
        }

        public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
            boolean started;
            if ((nestedScrollAxes & 2) == 0 || !child.hasScrollableChildren() || parent.getHeight() - directTargetChild.getHeight() > child.getHeight()) {
                started = false;
            } else {
                started = true;
            }
            if (started && this.mOffsetAnimator != null && directTargetChild.getTop() == child.getBottom()) {
                this.mOffsetAnimator.cancel();
            }
            if (((float) child.getBottom()) <= AppBarLayout.mAppBarHeight) {
                this.mIsCollapsed = true;
                child.mIsCollapsed = this.mIsCollapsed;
            } else {
                this.mIsCollapsed = false;
                child.mIsCollapsed = this.mIsCollapsed;
            }
            this.mLastNestedScrollingChildRef = null;
            this.lastStartedType = type;
            return started;
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
            if (dy != 0) {
                int min;
                int max;
                if (dy < 0) {
                    min = -child.getTotalScrollRange();
                    max = min + child.getDownNestedPreScrollRange();
                    this.mIsFlingScrollDown = true;
                    this.mIsFlingScrollUp = false;
                    if (((double) child.getBottom()) >= ((double) child.getHeight()) * 0.52d) {
                        this.mIsSetStaticDuration = true;
                    }
                    if (dy < -30) {
                        this.mIsFlingScrollDown = true;
                    } else {
                        this.mVelocity = 0.0f;
                        this.mIsFlingScrollDown = false;
                    }
                } else {
                    min = -child.getUpNestedPreScrollRange();
                    max = 0;
                    this.mIsFlingScrollDown = false;
                    this.mIsFlingScrollUp = true;
                    if (((double) child.getBottom()) <= ((double) child.getHeight()) * 0.43d) {
                        this.mIsSetStaticDuration = true;
                    }
                    if (dy > 30) {
                        this.mIsFlingScrollUp = true;
                    } else {
                        this.mVelocity = 0.0f;
                        this.mIsFlingScrollUp = false;
                    }
                    if (getTopAndBottomOffset() == min) {
                        this.mIsScrollHold = true;
                    }
                }
                if (min != max) {
                    consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
                }
            }
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
            if (isScrollHoldMode(child)) {
                if (dyUnconsumed >= 0 || this.mIsScrollHold) {
                    ViewCompat.stopNestedScroll(target, 1);
                    return;
                }
                scroll(coordinatorLayout, child, dyUnconsumed, -child.getDownNestedScrollRange(), 0);
                stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
            } else if (dyUnconsumed < 0) {
                scroll(coordinatorLayout, child, dyUnconsumed, -child.getDownNestedScrollRange(), 0);
                stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
            }
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
            if (getLastInterceptTouchEventEvent() == 3 || getLastInterceptTouchEventEvent() == 1 || getLastTouchEventEvent() == 3 || getLastTouchEventEvent() == 1) {
                snapToChildIfNeeded(coordinatorLayout, abl);
            }
            if ((this.lastStartedType == 0 || type == 1) && this.mIsScrollHold) {
                this.mIsScrollHold = false;
            }
            this.mLastNestedScrollingChildRef = new WeakReference(target);
        }

        private void stopNestedScrollIfNeeded(int dy, AppBarLayout child, View target, int type) {
            if (type == 1) {
                int currOffset = getTopAndBottomOffset();
                if ((dy < 0 && currOffset == 0) || (dy > 0 && currOffset == (-child.getDownNestedScrollRange()))) {
                    ViewCompat.stopNestedScroll(target, 1);
                }
            }
        }

        private boolean isScrollHoldMode(AppBarLayout abl) {
            if (this.mToolisMouse) {
                return false;
            }
            int offsetChildIndex = getChildIndexOnOffset(abl, getTopBottomOffsetForScrollingSibling());
            if (offsetChildIndex < 0 || (((LayoutParams) abl.getChildAt(offsetChildIndex).getLayoutParams()).getScrollFlags() & 296) != 296) {
                return true;
            }
            return false;
        }

        private void animateOffsetTo(CoordinatorLayout coordinatorLayout, AppBarLayout child, int offset, float velocity) {
            int duration;
            if (Math.abs(this.mVelocity) <= 0.0f || Math.abs(this.mVelocity) > 3000.0f) {
                duration = Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
            } else {
                duration = (int) (((double) (3000.0f - Math.abs(this.mVelocity))) * 0.4d);
            }
            if (duration <= Callback.DEFAULT_SWIPE_ANIMATION_DURATION) {
                duration = Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
            }
            if (this.mIsSetStaticDuration) {
                duration = Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
                this.mIsSetStaticDuration = false;
            }
            if (this.mVelocity < 2000.0f) {
                animateOffsetWithDuration(coordinatorLayout, child, offset, duration);
            }
            this.mVelocity = 0.0f;
        }

        private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, int offset, int duration) {
            if (getTopBottomOffsetForScrollingSibling() != offset) {
                if (this.mOffsetAnimator == null) {
                    this.mOffsetAnimator = new ValueAnimator();
                    this.mOffsetAnimator.setInterpolator(AppBarLayout.SINE_OUT_80_INTERPOLATOR);
                    this.mOffsetAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Behavior.this.setHeaderTopBottomOffset(coordinatorLayout, child, ((Integer) animation.getAnimatedValue()).intValue());
                        }
                    });
                } else {
                    this.mOffsetAnimator.cancel();
                }
                this.mOffsetAnimator.setDuration((long) Math.min(duration, 700));
                this.mOffsetAnimator.setIntValues(new int[]{currentOffset, offset});
                this.mOffsetAnimator.start();
            } else if (this.mOffsetAnimator != null && this.mOffsetAnimator.isRunning()) {
                this.mOffsetAnimator.cancel();
            }
        }

        private int getChildIndexOnOffset(AppBarLayout abl, int offset) {
            int count = abl.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = abl.getChildAt(i);
                if (child.getTop() <= (-offset) && child.getBottom() >= (-offset)) {
                    return i;
                }
            }
            return -1;
        }

        private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, AppBarLayout abl) {
            int offset = getTopBottomOffsetForScrollingSibling();
            int offsetChildIndex = getChildIndexOnOffset(abl, offset);
            View childView = coordinatorLayout.getChildAt(1);
            if (offsetChildIndex >= 0) {
                View offsetChild = abl.getChildAt(offsetChildIndex);
                int flags = ((LayoutParams) offsetChild.getLayoutParams()).getScrollFlags();
                if ((flags & 100) != 100) {
                    int newOffset;
                    int snapTop = -offsetChild.getTop();
                    int snapBottom = -offsetChild.getBottom();
                    if (offsetChildIndex == abl.getChildCount() - 1) {
                        snapBottom += abl.getTopInset();
                    }
                    if (checkFlag(flags, 2)) {
                        snapBottom += ViewCompat.getMinimumHeight(offsetChild);
                    } else if (checkFlag(flags, 5)) {
                        int seam = snapBottom + ViewCompat.getMinimumHeight(offsetChild);
                        if (offset < seam) {
                            snapTop = seam;
                        } else {
                            snapBottom = seam;
                        }
                    }
                    if (((double) offset) < ((double) (snapBottom + snapTop)) * 0.25d) {
                        newOffset = snapBottom;
                    } else {
                        newOffset = snapTop;
                    }
                    if (!this.mIsCollapsed) {
                        newOffset = ((double) offset) < ((double) (snapBottom + snapTop)) * 0.43d ? snapBottom : snapTop;
                    } else if (((double) offset) >= ((double) (snapBottom + snapTop)) * 0.52d) {
                        newOffset = snapTop;
                    } else {
                        newOffset = snapBottom;
                    }
                    if (isScrollHoldMode(abl)) {
                        if (this.mIsFlingScrollUp) {
                            newOffset = snapBottom;
                            this.mIsFlingScrollUp = false;
                            this.mIsFlingScrollDown = false;
                        }
                        if (this.mIsFlingScrollDown && childView != null && ((float) childView.getTop()) > AppBarLayout.mAppBarHeight) {
                            newOffset = snapTop;
                            this.mIsFlingScrollDown = false;
                        }
                    } else {
                        if (this.mIsFlingScrollUp) {
                            newOffset = snapBottom;
                            this.mIsFlingScrollUp = false;
                            this.mIsFlingScrollDown = false;
                        }
                        if (this.mIsFlingScrollDown && childView != null && ((float) childView.getTop()) > AppBarLayout.mAppBarHeight) {
                            newOffset = snapTop;
                            this.mIsFlingScrollDown = false;
                        }
                    }
                    animateOffsetTo(coordinatorLayout, abl, MathUtils.clamp(newOffset, -abl.getTotalScrollRange(), 0), 0.0f);
                }
            }
        }

        private static boolean checkFlag(int flags, int check) {
            return (flags & check) == check;
        }

        public boolean onMeasureChild(CoordinatorLayout parent, AppBarLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            if (((android.support.design.widget.CoordinatorLayout.LayoutParams) child.getLayoutParams()).height != -2) {
                return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
            }
            parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, MeasureSpec.makeMeasureSpec(0, 0), heightUsed);
            return true;
        }

        public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
            boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
            int pendingAction = abl.getPendingAction();
            int offset;
            if (this.mOffsetToChildIndexOnLayout >= 0 && (pendingAction & 8) == 0) {
                View child = abl.getChildAt(this.mOffsetToChildIndexOnLayout);
                offset = -child.getBottom();
                if (this.mOffsetToChildIndexOnLayoutIsMinHeight) {
                    offset += ViewCompat.getMinimumHeight(child) + abl.getTopInset();
                } else {
                    offset += Math.round(((float) child.getHeight()) * this.mOffsetToChildIndexOnLayoutPerc);
                }
                setHeaderTopBottomOffset(parent, abl, offset);
            } else if (pendingAction != 0) {
                boolean animate;
                if ((pendingAction & 4) != 0) {
                    animate = true;
                } else {
                    animate = false;
                }
                if ((pendingAction & 2) != 0) {
                    offset = -abl.getUpNestedPreScrollRange();
                    if (animate) {
                        animateOffsetTo(parent, abl, offset, 0.0f);
                    } else {
                        setHeaderTopBottomOffset(parent, abl, offset);
                    }
                } else if ((pendingAction & 1) != 0) {
                    if (animate) {
                        animateOffsetTo(parent, abl, 0, 0.0f);
                    } else {
                        setHeaderTopBottomOffset(parent, abl, 0);
                    }
                }
            }
            abl.resetPendingAction();
            this.mOffsetToChildIndexOnLayout = -1;
            setTopAndBottomOffset(MathUtils.clamp(getTopAndBottomOffset(), -abl.getTotalScrollRange(), 0));
            updateAppBarLayoutDrawableState(parent, abl, getTopAndBottomOffset(), 0, false);
            abl.dispatchOffsetUpdates(getTopAndBottomOffset());
            return handled;
        }

        boolean canDragView(AppBarLayout view) {
            if (this.mOnDragCallback != null) {
                return this.mOnDragCallback.canDrag(view);
            }
            if (this.mLastNestedScrollingChildRef == null) {
                return true;
            }
            View scrollingView = (View) this.mLastNestedScrollingChildRef.get();
            if (scrollingView == null || !scrollingView.isShown() || scrollingView.canScrollVertically(-1)) {
                return false;
            }
            return true;
        }

        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, float velocityX, float velocityY, boolean consumed) {
            return super.onNestedFling(coordinatorLayout, abl, target, velocityX, velocityY, consumed);
        }

        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, float velocityX, float velocityY) {
            this.mVelocity = velocityY;
            if (velocityY < -300.0f) {
                this.mIsFlingScrollDown = true;
                this.mIsFlingScrollUp = false;
            } else if (velocityY > 300.0f) {
                this.mIsFlingScrollDown = false;
                this.mIsFlingScrollUp = true;
            } else {
                this.mVelocity = 0.0f;
                this.mIsFlingScrollDown = false;
                this.mIsFlingScrollUp = false;
                return true;
            }
            return super.onNestedPreFling(coordinatorLayout, abl, target, velocityX, velocityY);
        }

        int getMaxDragOffset(AppBarLayout view) {
            return -view.getDownNestedScrollRange();
        }

        int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int newOffset, int minOffset, int maxOffset) {
            int curOffset = getTopBottomOffsetForScrollingSibling();
            int consumed = 0;
            if (minOffset == 0 || curOffset < minOffset || curOffset > maxOffset) {
                this.mOffsetDelta = 0;
            } else {
                newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);
                if (curOffset != newOffset) {
                    int interpolatedOffset = appBarLayout.hasChildWithInterpolator() ? interpolateOffset(appBarLayout, newOffset) : newOffset;
                    boolean offsetChanged = setTopAndBottomOffset(interpolatedOffset);
                    consumed = curOffset - newOffset;
                    this.mOffsetDelta = newOffset - interpolatedOffset;
                    if (!offsetChanged && appBarLayout.hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged(appBarLayout);
                    }
                    appBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
                    updateAppBarLayoutDrawableState(coordinatorLayout, appBarLayout, newOffset, newOffset < curOffset ? -1 : 1, false);
                }
            }
            return consumed;
        }

        private int interpolateOffset(AppBarLayout layout, int offset) {
            int absOffset = Math.abs(offset);
            int i = 0;
            int z = layout.getChildCount();
            while (i < z) {
                View child = layout.getChildAt(i);
                LayoutParams childLp = (LayoutParams) child.getLayoutParams();
                Interpolator interpolator = childLp.getScrollInterpolator();
                if (absOffset < child.getTop() || absOffset > child.getBottom()) {
                    i++;
                } else if (interpolator == null) {
                    return offset;
                } else {
                    int childScrollableHeight = 0;
                    int flags = childLp.getScrollFlags();
                    if ((flags & 1) != 0) {
                        childScrollableHeight = 0 + ((child.getHeight() + childLp.topMargin) + childLp.bottomMargin);
                        if ((flags & 2) != 0) {
                            childScrollableHeight -= ViewCompat.getMinimumHeight(child);
                        }
                    }
                    if (ViewCompat.getFitsSystemWindows(child)) {
                        childScrollableHeight -= layout.getTopInset();
                    }
                    if (childScrollableHeight <= 0) {
                        return offset;
                    }
                    return Integer.signum(offset) * (child.getTop() + Math.round(((float) childScrollableHeight) * interpolator.getInterpolation(((float) (absOffset - child.getTop())) / ((float) childScrollableHeight))));
                }
            }
            return offset;
        }

        private void updateAppBarLayoutDrawableState(CoordinatorLayout parent, AppBarLayout layout, int offset, int direction, boolean forceJump) {
            View child = getAppBarChildOnOffset(layout, offset);
            if (child != null) {
                int flags = ((LayoutParams) child.getLayoutParams()).getScrollFlags();
                boolean collapsed = false;
                if ((flags & 1) != 0) {
                    int minHeight = ViewCompat.getMinimumHeight(child);
                    if (direction > 0 && (flags & 12) != 0) {
                        collapsed = (-offset) >= (child.getBottom() - minHeight) - layout.getTopInset();
                    } else if ((flags & 2) != 0) {
                        collapsed = (-offset) >= (child.getBottom() - minHeight) - layout.getTopInset();
                    }
                }
                boolean changed = layout.setCollapsedState(collapsed);
                if (VERSION.SDK_INT < 11) {
                    return;
                }
                if (forceJump || (changed && shouldJumpElevationState(parent, layout))) {
                    layout.jumpDrawablesToCurrentState();
                }
            }
        }

        private boolean shouldJumpElevationState(CoordinatorLayout parent, AppBarLayout layout) {
            return false;
        }

        private static View getAppBarChildOnOffset(AppBarLayout layout, int offset) {
            int absOffset = Math.abs(offset);
            int z = layout.getChildCount();
            for (int i = 0; i < z; i++) {
                View child = layout.getChildAt(i);
                if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
                    return child;
                }
            }
            return null;
        }

        int getTopBottomOffsetForScrollingSibling() {
            return getTopAndBottomOffset() + this.mOffsetDelta;
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout parent, AppBarLayout abl) {
            Parcelable superState = super.onSaveInstanceState(parent, abl);
            int offset = getTopAndBottomOffset();
            int i = 0;
            int count = abl.getChildCount();
            while (i < count) {
                View child = abl.getChildAt(i);
                int visBottom = child.getBottom() + offset;
                if (child.getTop() + offset > 0 || visBottom < 0) {
                    i++;
                } else {
                    SavedState ss = new SavedState(superState);
                    ss.firstVisibleChildIndex = i;
                    ss.firstVisibleChildAtMinimumHeight = visBottom == ViewCompat.getMinimumHeight(child) + abl.getTopInset();
                    ss.firstVisibleChildPercentageShown = ((float) visBottom) / ((float) child.getHeight());
                    return ss;
                }
            }
            return superState;
        }

        public void onRestoreInstanceState(CoordinatorLayout parent, AppBarLayout appBarLayout, Parcelable state) {
            if (state instanceof SavedState) {
                SavedState ss = (SavedState) state;
                super.onRestoreInstanceState(parent, appBarLayout, ss.getSuperState());
                this.mOffsetToChildIndexOnLayout = ss.firstVisibleChildIndex;
                this.mOffsetToChildIndexOnLayoutPerc = ss.firstVisibleChildPercentageShown;
                this.mOffsetToChildIndexOnLayoutIsMinHeight = ss.firstVisibleChildAtMinimumHeight;
                return;
            }
            super.onRestoreInstanceState(parent, appBarLayout, state);
            this.mOffsetToChildIndexOnLayout = -1;
        }
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        int mScrollFlags = 1;
        Interpolator mScrollInterpolator;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, C0011R.styleable.AppBarLayout_Layout);
            this.mScrollFlags = a.getInt(C0011R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            if (a.hasValue(C0011R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
                this.mScrollInterpolator = AnimationUtils.loadInterpolator(c, a.getResourceId(C0011R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
            }
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.LinearLayout.LayoutParams source) {
            super(source);
        }

        public int getScrollFlags() {
            return this.mScrollFlags;
        }

        public Interpolator getScrollInterpolator() {
            return this.mScrollInterpolator;
        }

        boolean isCollapsible() {
            return (this.mScrollFlags & 1) == 1 && (this.mScrollFlags & 10) != 0;
        }
    }

    public interface OnOffsetChangedListener {
        void onOffsetChanged(AppBarLayout appBarLayout, int i);
    }

    public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            return super.onLayoutChild(coordinatorLayout, view, i);
        }

        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int i4) {
            return super.onMeasureChild(coordinatorLayout, view, i, i2, i3, i4);
        }

        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i) {
            return super.setTopAndBottomOffset(i);
        }

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, C0011R.styleable.ScrollingViewBehavior_Layout);
            setOverlayTop(a.getDimensionPixelSize(C0011R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            a.recycle();
        }

        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            offsetChildAsNeeded(parent, child, dependency);
            return false;
        }

        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent, View child, Rect rectangle, boolean immediate) {
            AppBarLayout header = findFirstDependency(parent.getDependencies(child));
            if (header != null) {
                rectangle.offset(child.getLeft(), child.getTop());
                Rect parentRect = this.mTempRect1;
                parentRect.set(0, 0, parent.getWidth(), parent.getHeight());
                if (!parentRect.contains(rectangle)) {
                    boolean z;
                    if (immediate) {
                        z = false;
                    } else {
                        z = true;
                    }
                    header.setExpanded(false, z);
                    return true;
                }
            }
            return false;
        }

        private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
            android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
            if (behavior instanceof Behavior) {
                ViewCompat.offsetTopAndBottom(child, (((dependency.getBottom() - child.getTop()) + ((Behavior) behavior).mOffsetDelta) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(dependency));
            }
        }

        float getOverlapRatioForOffset(View header) {
            if (!(header instanceof AppBarLayout)) {
                return 0.0f;
            }
            AppBarLayout abl = (AppBarLayout) header;
            int totalScrollRange = abl.getTotalScrollRange();
            int preScrollDown = abl.getDownNestedPreScrollRange();
            int offset = getAppBarLayoutOffset(abl);
            if (preScrollDown != 0 && totalScrollRange + offset <= preScrollDown) {
                return 0.0f;
            }
            int availScrollRange = totalScrollRange - preScrollDown;
            if (availScrollRange != 0) {
                return 1.0f + (((float) offset) / ((float) availScrollRange));
            }
            return 0.0f;
        }

        private static int getAppBarLayoutOffset(AppBarLayout abl) {
            android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) abl.getLayoutParams()).getBehavior();
            if (behavior instanceof Behavior) {
                return ((Behavior) behavior).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }

        AppBarLayout findFirstDependency(List<View> views) {
            int z = views.size();
            for (int i = 0; i < z; i++) {
                View view = (View) views.get(i);
                if (view instanceof AppBarLayout) {
                    return (AppBarLayout) view;
                }
            }
            return null;
        }

        int getScrollRange(View v) {
            if (v instanceof AppBarLayout) {
                return ((AppBarLayout) v).getTotalScrollRange();
            }
            return super.getScrollRange(v);
        }
    }

    public AppBarLayout(Context context) {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownScrollRange = -1;
        this.mPendingAction = 0;
        this.mIsCollapsed = false;
        this.mHeightCustom = 0.0f;
        this.mHeightPercent = 0.0f;
        this.mBottomPadding = 0;
        this.mIsSetCollapsedHeight = false;
        setOrientation(1);
        ThemeUtils.checkAppCompatTheme(context);
        if (VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, attrs, 0, C0011R.style.Widget_Design_AppBarLayout);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, C0011R.styleable.AppBarLayout, 0, C0011R.style.Widget_Design_AppBarLayout);
        if (a.hasValue(C0011R.styleable.AppBarLayout_android_background)) {
            this.mBackground = a.getDrawable(C0011R.styleable.AppBarLayout_android_background);
            ViewCompat.setBackground(this, this.mBackground);
        } else if (getBackground() != null) {
            this.mBackground = getBackground();
            ViewCompat.setBackground(this, this.mBackground);
        } else {
            this.mBackground = null;
            if (isLightTheme()) {
                setBackgroundColor(getResources().getColor(C0011R.color.sesl_action_bar_background_color_light));
            } else {
                setBackgroundColor(getResources().getColor(C0011R.color.sesl_action_bar_background_color_dark));
            }
        }
        if (a.hasValue(C0011R.styleable.AppBarLayout_expanded)) {
            setExpanded(a.getBoolean(C0011R.styleable.AppBarLayout_expanded, false));
        }
        if (VERSION.SDK_INT >= 21 && a.hasValue(C0011R.styleable.AppBarLayout_elevation)) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, (float) a.getDimensionPixelSize(C0011R.styleable.AppBarLayout_elevation, 0));
        }
        if (a.hasValue(C0011R.styleable.AppBarLayout_sesl_layout_heightPercent)) {
            this.mHeightCustom = a.getFloat(C0011R.styleable.AppBarLayout_sesl_layout_heightPercent, 0.3967f);
        } else {
            this.mHeightCustom = 0.3967f;
        }
        TypedValue typedValue = new TypedValue();
        getResources().getValue(C0011R.dimen.sesl_abl_height_proportion, typedValue, true);
        this.mHeightPercent = typedValue.getFloat();
        if (a.hasValue(C0011R.styleable.AppBarLayout_android_paddingBottom)) {
            this.mBottomPadding = a.getDimensionPixelSize(C0011R.styleable.AppBarLayout_android_paddingBottom, 0);
            setPadding(0, 0, 0, this.mBottomPadding);
        } else {
            this.mBottomPadding = 0;
        }
        if (VERSION.SDK_INT >= 26) {
            if (a.hasValue(C0011R.styleable.AppBarLayout_android_keyboardNavigationCluster)) {
                setKeyboardNavigationCluster(a.getBoolean(C0011R.styleable.AppBarLayout_android_keyboardNavigationCluster, false));
            }
            if (a.hasValue(C0011R.styleable.AppBarLayout_android_touchscreenBlocksFocus)) {
                setTouchscreenBlocksFocus(a.getBoolean(C0011R.styleable.AppBarLayout_android_touchscreenBlocksFocus, false));
            }
        }
        a.recycle();
        if (this.mBottomPadding > 0) {
            mAppBarHeight = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
        } else {
            mAppBarHeight = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height);
        }
        ViewCompat.setOnApplyWindowInsetsListener(this, new C00181());
        this.mCurrentOrientation = getContext().getResources().getConfiguration().orientation;
    }

    public void setCollapsedHeight(float height) {
        Log.d("Sesl_AppBarLayout", "setCollapsedHeight: height :" + height);
        this.mIsSetCollapsedHeight = true;
        mAppBarHeight = height;
    }

    public float getCollapsedHeight() {
        return mAppBarHeight;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mBackground != null) {
            if (this.mBackground == getBackground()) {
                setBackgroundDrawable(this.mBackground);
            } else {
                setBackgroundDrawable(getBackground());
            }
        } else if (getBackground() != null) {
            this.mBackground = getBackground();
            setBackgroundDrawable(this.mBackground);
        } else {
            this.mBackground = null;
            if (isLightTheme()) {
                setBackgroundColor(getResources().getColor(C0011R.color.sesl_action_bar_background_color_light));
            } else {
                setBackgroundColor(getResources().getColor(C0011R.color.sesl_action_bar_background_color_dark));
            }
        }
        if (this.mBottomPadding > 0) {
            this.mBottomPadding = getContext().getResources().getDimensionPixelSize(C0011R.dimen.sesl_extended_appbar_bottom_padding);
            setPadding(0, 0, 0, this.mBottomPadding);
        }
        TypedValue typedValue = new TypedValue();
        getResources().getValue(C0011R.dimen.sesl_abl_height_proportion, typedValue, true);
        this.mHeightPercent = typedValue.getFloat();
        if (this.mHeightCustom > 0.0f) {
            Log.d("Sesl_AppBarLayout", "onConfigurationChanged");
            updateInternalHeight();
        }
        if (this.mCollapsed || (this.mCurrentOrientation == 1 && newConfig.orientation == 2)) {
            setExpanded(false, false, true);
        } else {
            setExpanded(true, false, true);
        }
        this.mCurrentOrientation = newConfig.orientation;
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        if (listener != null && !this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener listener) {
        if (this.mListeners != null && listener != null) {
            this.mListeners.remove(listener);
        }
    }

    private Activity findActivityOfContext(Context context) {
        Activity activity = null;
        Context tempContext = context;
        while (activity == null && tempContext != null) {
            if (tempContext instanceof Activity) {
                activity = (Activity) tempContext;
            } else {
                tempContext = tempContext instanceof ContextWrapper ? ((ContextWrapper) tempContext).getBaseContext() : null;
            }
        }
        return activity;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.mIsSetCollapsedHeight) {
            if (this.mBottomPadding > 0) {
                mAppBarHeight = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
            } else {
                mAppBarHeight = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height);
            }
        }
        if (this.mHeightCustom > 0.0f) {
            updateInternalHeight();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        invalidateScrollRanges();
    }

    private void updateInternalHeight() {
        android.support.design.widget.CoordinatorLayout.LayoutParams lp;
        int windowHeight = getWindowHeight();
        float heightDp = ((float) windowHeight) * this.mHeightPercent;
        if (heightDp == 0.0f) {
            heightDp = mAppBarHeight;
        }
        Log.d("Sesl_AppBarLayout", "updateInternalHeight: context:" + getContext() + ", orientation:" + getContext().getResources().getConfiguration().orientation + " density:" + getContext().getResources().getConfiguration().densityDpi + " ,mHeightPercent" + this.mHeightPercent + " windowHeight:" + windowHeight + " activity:" + findActivityOfContext(getContext()));
        try {
            lp = (android.support.design.widget.CoordinatorLayout.LayoutParams) getLayoutParams();
        } catch (ClassCastException cce) {
            lp = null;
            Log.e("Sesl_AppBarLayout", Log.getStackTraceString(cce));
        }
        if (lp != null) {
            lp.height = (int) heightDp;
            Log.d("Sesl_AppBarLayout", "updateInternalHeight: LayoutParams :" + lp + " ,lp.height :" + lp.height);
            setLayoutParams(lp);
        }
    }

    private int getWindowHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        invalidateScrollRanges();
        this.mHaveChildWithInterpolator = false;
        int z = getChildCount();
        for (int i = 0; i < z; i++) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).getScrollInterpolator() != null) {
                this.mHaveChildWithInterpolator = true;
                break;
            }
        }
        updateCollapsible();
    }

    private void updateCollapsible() {
        boolean haveCollapsibleChild = false;
        int z = getChildCount();
        for (int i = 0; i < z; i++) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).isCollapsible()) {
                haveCollapsibleChild = true;
                break;
            }
        }
        setCollapsibleState(haveCollapsibleChild);
    }

    private void invalidateScrollRanges() {
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownScrollRange = -1;
    }

    public void setOrientation(int orientation) {
        if (orientation != 1) {
            throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
        }
        super.setOrientation(orientation);
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, ViewCompat.isLaidOut(this));
    }

    public void setExpanded(boolean expanded, boolean animate) {
        setExpanded(expanded, animate, true);
    }

    private void setExpanded(boolean expanded, boolean animate, boolean force) {
        int i = 1;
        int i2 = 0;
        setCollapsedState(!expanded);
        if (!expanded) {
            i = 2;
        }
        int i3 = (animate ? 4 : 0) | i;
        if (force) {
            i2 = 8;
        }
        this.mPendingAction = i3 | i2;
        requestLayout();
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (VERSION.SDK_INT >= 19 && (p instanceof android.widget.LinearLayout.LayoutParams)) {
            return new LayoutParams((android.widget.LinearLayout.LayoutParams) p);
        }
        if (p instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    boolean hasChildWithInterpolator() {
        return this.mHaveChildWithInterpolator;
    }

    public final int getTotalScrollRange() {
        if (this.mTotalScrollRange != -1) {
            return this.mTotalScrollRange;
        }
        int range = 0;
        int z = getChildCount();
        for (int i = 0; i < z; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight();
            int flags = lp.mScrollFlags;
            if ((flags & 1) == 0) {
                break;
            }
            range += (lp.topMargin + childHeight) + lp.bottomMargin;
            if ((flags & 2) != 0) {
                range -= ViewCompat.getMinimumHeight(child);
                break;
            }
        }
        int max = Math.max(0, range - getTopInset());
        this.mTotalScrollRange = max;
        return max;
    }

    boolean hasScrollableChildren() {
        return getTotalScrollRange() != 0;
    }

    int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    int getDownNestedPreScrollRange() {
        if (this.mDownPreScrollRange != -1) {
            return this.mDownPreScrollRange;
        }
        int range = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight();
            int flags = lp.mScrollFlags;
            if ((flags & 5) == 5) {
                range += lp.topMargin + lp.bottomMargin;
                if ((flags & 8) != 0) {
                    range += ViewCompat.getMinimumHeight(child);
                } else if ((flags & 2) != 0) {
                    range += childHeight - ViewCompat.getMinimumHeight(child);
                } else {
                    range += childHeight - getTopInset();
                }
            } else if (range > 0) {
                break;
            }
        }
        int max = Math.max(0, range);
        this.mDownPreScrollRange = max;
        return max;
    }

    int getDownNestedScrollRange() {
        if (this.mDownScrollRange != -1) {
            return this.mDownScrollRange;
        }
        int range = 0;
        int z = getChildCount();
        for (int i = 0; i < z; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight() + (lp.topMargin + lp.bottomMargin);
            int flags = lp.mScrollFlags;
            if ((flags & 1) == 0) {
                break;
            }
            range += childHeight;
            if ((flags & 2) != 0) {
                range -= ViewCompat.getMinimumHeight(child) + getTopInset();
                break;
            }
        }
        int max = Math.max(0, range);
        this.mDownScrollRange = max;
        return max;
    }

    void dispatchOffsetUpdates(int offset) {
        if (this.mListeners != null) {
            int z = this.mListeners.size();
            for (int i = 0; i < z; i++) {
                OnOffsetChangedListener listener = (OnOffsetChangedListener) this.mListeners.get(i);
                if (listener != null) {
                    listener.onOffsetChanged(this, offset);
                }
            }
        }
    }

    final int getMinimumHeightForVisibleOverlappingContent() {
        int topInset = getTopInset();
        int minHeight = ViewCompat.getMinimumHeight(this);
        if (minHeight != 0) {
            return (minHeight * 2) + topInset;
        }
        int childCount = getChildCount();
        int lastChildMinHeight = childCount >= 1 ? ViewCompat.getMinimumHeight(getChildAt(childCount - 1)) : 0;
        if (lastChildMinHeight != 0) {
            return (lastChildMinHeight * 2) + topInset;
        }
        return getHeight() / 3;
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        if (this.mTmpStatesArray == null) {
            this.mTmpStatesArray = new int[2];
        }
        int[] extraStates = this.mTmpStatesArray;
        int[] states = super.onCreateDrawableState(extraStates.length + extraSpace);
        extraStates[0] = this.mCollapsible ? C0011R.attr.state_collapsible : -C0011R.attr.state_collapsible;
        int i = (this.mCollapsible && this.mCollapsed) ? C0011R.attr.state_collapsed : -C0011R.attr.state_collapsed;
        extraStates[1] = i;
        return mergeDrawableStates(states, extraStates);
    }

    private boolean setCollapsibleState(boolean collapsible) {
        if (this.mCollapsible == collapsible) {
            return false;
        }
        this.mCollapsible = collapsible;
        refreshDrawableState();
        return true;
    }

    boolean setCollapsedState(boolean collapsed) {
        if (this.mCollapsed == collapsed) {
            return false;
        }
        this.mCollapsed = collapsed;
        refreshDrawableState();
        return true;
    }

    @Deprecated
    public void setTargetElevation(float elevation) {
        if (VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, elevation);
        }
    }

    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }

    int getPendingAction() {
        return this.mPendingAction;
    }

    void resetPendingAction() {
        this.mPendingAction = 0;
    }

    final int getTopInset() {
        return this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
    }

    public boolean isCollapsed() {
        return this.mIsCollapsed;
    }

    WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat insets) {
        WindowInsetsCompat newInsets = null;
        if (ViewCompat.getFitsSystemWindows(this)) {
            newInsets = insets;
        }
        if (!ObjectsCompat.equals(this.mLastInsets, newInsets)) {
            this.mLastInsets = newInsets;
            invalidateScrollRanges();
        }
        return insets;
    }

    private boolean isLightTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            return true;
        }
        return false;
    }
}
