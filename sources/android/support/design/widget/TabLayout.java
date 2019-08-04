package android.support.design.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.design.C0011R;
import android.support.design.internal.SeslAbsIndicatorView;
import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.Pools.SynchronizedPool;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.DecorView;
import android.support.v4.view.ViewPager.OnAdapterChangeListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@DecorView
public class TabLayout extends HorizontalScrollView {
    public static final int SESL_TAB_ANIM_INTERPOLATOR = C0011R.interpolator.sine_in_out_80;
    private static final Pool<Tab> sTabPool = new SynchronizedPool(16);
    private AdapterChangeListener mAdapterChangeListener;
    private int mBadgeColor;
    private int mBadgeTextColor;
    private Typeface mBoldTypeface;
    private int mContentInsetStart;
    private ContentResolver mContentResolver;
    private OnTabSelectedListener mCurrentVpSelectedListener;
    private int mDepthStyle;
    private boolean mIsScaledTextSizeType;
    int mMode;
    private Typeface mNormalTypeface;
    private TabLayoutOnPageChangeListener mPageChangeListener;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private final int mRequestedTabMaxWidth;
    private final int mRequestedTabMinWidth;
    private int mRequestedTabWidth;
    private ValueAnimator mScrollAnimator;
    private final int mScrollableTabMinWidth;
    private OnTabSelectedListener mSelectedListener;
    private final ArrayList<OnTabSelectedListener> mSelectedListeners;
    private Tab mSelectedTab;
    private boolean mSetupViewPagerImplicitly;
    private int mSubTabIndicatorHeight;
    private int mSubTabSelectedIndicatorColor;
    final int mTabBackgroundResId;
    int mTabGravity;
    int mTabMaxWidth;
    int mTabPaddingBottom;
    int mTabPaddingEnd;
    int mTabPaddingStart;
    int mTabPaddingTop;
    private int mTabSelectedIndicatorColor;
    private final SlidingTabStrip mTabStrip;
    int mTabTextAppearance;
    ColorStateList mTabTextColors;
    float mTabTextMultiLineSize;
    float mTabTextSize;
    private final Pool<TabView> mTabViewPool;
    private final ArrayList<Tab> mTabs;
    ViewPager mViewPager;

    /* renamed from: android.support.design.widget.TabLayout$1 */
    class C00471 implements AnimatorUpdateListener {
        C00471() {
        }

        public void onAnimationUpdate(ValueAnimator animator) {
            TabLayout.this.scrollTo(((Integer) animator.getAnimatedValue()).intValue(), 0);
        }
    }

    private class AdapterChangeListener implements OnAdapterChangeListener {
        private boolean mAutoRefresh;

        AdapterChangeListener() {
        }

        public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter) {
            if (TabLayout.this.mViewPager == viewPager) {
                TabLayout.this.setPagerAdapter(newAdapter, this.mAutoRefresh);
            }
        }

        void setAutoRefresh(boolean autoRefresh) {
            this.mAutoRefresh = autoRefresh;
        }
    }

    public interface OnTabSelectedListener {
        void onTabReselected(Tab tab);

        void onTabSelected(Tab tab);

        void onTabUnselected(Tab tab);
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        public void onChanged() {
            TabLayout.this.populateFromPagerAdapter();
        }

        public void onInvalidated() {
            TabLayout.this.populateFromPagerAdapter();
        }
    }

    private class SlidingTabStrip extends LinearLayout {
        private ValueAnimator mIndicatorAnimator;
        private int mIndicatorLeft = -1;
        private int mIndicatorRight = -1;
        private int mIndicatorTop = -1;
        private int mInterval;
        private int mLayoutDirection = -1;
        private int mRadius;
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        int mSelectedPosition = -1;
        float mSelectionOffset;

        SlidingTabStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            this.mSelectedIndicatorPaint = new Paint();
            this.mRadius = (int) TypedValue.applyDimension(1, 1.0f, context.getResources().getDisplayMetrics());
            this.mInterval = this.mRadius * 2;
        }

        void setSelectedIndicatorColor(int color) {
            if (this.mSelectedIndicatorPaint.getColor() != color) {
                this.mSelectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        void setSelectedIndicatorHeight(int height) {
            if (this.mSelectedIndicatorHeight != height) {
                this.mSelectedIndicatorHeight = height;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        boolean childrenNeedLayout() {
            int z = getChildCount();
            for (int i = 0; i < z; i++) {
                if (getChildAt(i).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
            if (this.mIndicatorAnimator != null && this.mIndicatorAnimator.isRunning()) {
                this.mIndicatorAnimator.cancel();
            }
            this.mSelectedPosition = position;
            this.mSelectionOffset = positionOffset;
            updateIndicatorPosition();
        }

        float getIndicatorPosition() {
            return ((float) this.mSelectedPosition) + this.mSelectionOffset;
        }

        public void onRtlPropertiesChanged(int layoutDirection) {
            super.onRtlPropertiesChanged(layoutDirection);
            if (VERSION.SDK_INT < 23 && this.mLayoutDirection != layoutDirection) {
                requestLayout();
                this.mLayoutDirection = layoutDirection;
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && TabLayout.this.mMode == 1 && TabLayout.this.mTabGravity == 1) {
                int i;
                int count = getChildCount();
                int largestTabWidth = 0;
                int z = count;
                for (i = 0; i < z; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() == 0) {
                        largestTabWidth = Math.max(largestTabWidth, child.getMeasuredWidth());
                    }
                }
                if (largestTabWidth > 0) {
                    boolean remeasure = false;
                    if (largestTabWidth * count <= getMeasuredWidth() - (TabLayout.this.dpToPx(16) * 2)) {
                        for (i = 0; i < count; i++) {
                            LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                            if (lp.width != largestTabWidth || lp.weight != 0.0f) {
                                lp.width = largestTabWidth;
                                lp.weight = 0.0f;
                                remeasure = true;
                            }
                        }
                    } else {
                        TabLayout.this.mTabGravity = 0;
                        TabLayout.this.updateTabViews(false);
                        remeasure = true;
                    }
                    if (remeasure) {
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
            }
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (this.mIndicatorAnimator == null || !this.mIndicatorAnimator.isRunning()) {
                updateIndicatorPosition();
                return;
            }
            this.mIndicatorAnimator.cancel();
            animateIndicatorToPosition(this.mSelectedPosition, Math.round((1.0f - this.mIndicatorAnimator.getAnimatedFraction()) * ((float) this.mIndicatorAnimator.getDuration())));
        }

        private void updateIndicatorPosition() {
            int right;
            int left;
            View selectedTitle = getChildAt(this.mSelectedPosition);
            if (selectedTitle == null || selectedTitle.getWidth() <= 0) {
                right = -1;
                left = -1;
            } else {
                left = selectedTitle.getLeft();
                right = selectedTitle.getRight();
                if (this.mSelectionOffset > 0.0f && this.mSelectedPosition < getChildCount() - 1) {
                    View nextTitle = getChildAt(this.mSelectedPosition + 1);
                    left = (int) ((this.mSelectionOffset * ((float) nextTitle.getLeft())) + ((1.0f - this.mSelectionOffset) * ((float) left)));
                    right = (int) ((this.mSelectionOffset * ((float) nextTitle.getRight())) + ((1.0f - this.mSelectionOffset) * ((float) right)));
                }
                TabView selectedGroup = (TabView) selectedTitle;
                if (selectedGroup.mCustomView == null) {
                    int bottom = -1;
                    for (int i = 0; i < selectedGroup.getChildCount(); i++) {
                        View child = selectedGroup.getChildAt(i);
                        if (bottom < child.getBottom()) {
                            bottom = child.getBottom();
                        }
                        if (child instanceof TextView) {
                            left = selectedTitle.getLeft() + child.getLeft();
                            right = selectedTitle.getLeft() + child.getRight();
                        }
                    }
                    this.mIndicatorTop = bottom;
                }
            }
            setIndicatorPosition(left, right);
        }

        void setIndicatorPosition(int left, int right) {
            if (left != this.mIndicatorLeft || right != this.mIndicatorRight) {
                this.mIndicatorLeft = left;
                this.mIndicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        void animateIndicatorToPosition(int position, int duration) {
            if (this.mIndicatorAnimator != null && this.mIndicatorAnimator.isRunning()) {
                this.mIndicatorAnimator.cancel();
            }
            boolean isRtl = ViewCompat.getLayoutDirection(this) == 1;
            View targetView = getChildAt(position);
            if (targetView == null) {
                updateIndicatorPosition();
                return;
            }
            int tLeft = targetView.getLeft();
            int tRight = targetView.getRight();
            if (((TabView) targetView).mCustomView == null) {
                TabView tv = (TabView) targetView;
                for (int i = 0; i < tv.getChildCount(); i++) {
                    View child = tv.getChildAt(i);
                    if (child instanceof TextView) {
                        tLeft = child.getLeft() + targetView.getLeft();
                        tRight = child.getRight() + targetView.getLeft();
                    }
                }
                this.mSelectedPosition = position;
                this.mIndicatorLeft = tLeft;
                this.mIndicatorRight = tRight;
                invalidate();
                return;
            }
            int startLeft;
            int startRight;
            final int targetLeft = tLeft;
            final int targetRight = tRight;
            if (Math.abs(position - this.mSelectedPosition) <= 1) {
                startLeft = this.mIndicatorLeft;
                startRight = this.mIndicatorRight;
            } else {
                int offset = TabLayout.this.dpToPx(24);
                if (position < this.mSelectedPosition) {
                    if (isRtl) {
                        startRight = targetLeft - offset;
                        startLeft = startRight;
                    } else {
                        startRight = targetRight + offset;
                        startLeft = startRight;
                    }
                } else if (isRtl) {
                    startRight = targetRight + offset;
                    startLeft = startRight;
                } else {
                    startRight = targetLeft - offset;
                    startLeft = startRight;
                }
            }
            if (startLeft != targetLeft || startRight != targetRight) {
                ValueAnimator animator = new ValueAnimator();
                this.mIndicatorAnimator = animator;
                animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                animator.setDuration((long) duration);
                animator.setFloatValues(new float[]{0.0f, 1.0f});
                animator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animator) {
                        float fraction = animator.getAnimatedFraction();
                        SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(startLeft, targetLeft, fraction), AnimationUtils.lerp(startRight, targetRight, fraction));
                    }
                });
                final int i2 = position;
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        SlidingTabStrip.this.mSelectedPosition = i2;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }
                });
                animator.start();
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
        }
    }

    public static final class Tab {
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        TabLayout mParent;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;
        TabView mView;

        Tab() {
        }

        public View getCustomView() {
            return this.mCustomView;
        }

        public TextView seslGetTextView() {
            if (this.mCustomView != null || this.mView == null) {
                return null;
            }
            return this.mView.mTextView;
        }

        public Tab setCustomView(View view) {
            if (this.mView.mTextView != null) {
                this.mView.removeAllViews();
            }
            this.mCustomView = view;
            updateView();
            return this;
        }

        public Tab setCustomView(int resId) {
            return setCustomView(LayoutInflater.from(this.mView.getContext()).inflate(resId, this.mView, false));
        }

        public Drawable getIcon() {
            return this.mIcon;
        }

        public int getPosition() {
            return this.mPosition;
        }

        void setPosition(int position) {
            this.mPosition = position;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public Tab setIcon(Drawable icon) {
            this.mIcon = icon;
            updateView();
            return this;
        }

        public Tab setText(CharSequence text) {
            this.mText = text;
            updateView();
            return this;
        }

        public void select() {
            if (this.mParent == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            this.mParent.selectTab(this);
        }

        public boolean isSelected() {
            if (this.mParent != null) {
                return this.mParent.getSelectedTabPosition() == this.mPosition;
            } else {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
        }

        public Tab setContentDescription(CharSequence contentDesc) {
            this.mContentDesc = contentDesc;
            updateView();
            return this;
        }

        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        void updateView() {
            if (this.mView != null) {
                this.mView.update();
            }
        }

        void reset() {
            this.mParent = null;
            this.mView = null;
            this.mTag = null;
            this.mIcon = null;
            this.mText = null;
            this.mContentDesc = null;
            this.mPosition = -1;
            this.mCustomView = null;
        }
    }

    public static class TabLayoutOnPageChangeListener implements OnPageChangeListener {
        private int mPreviousScrollState;
        private int mScrollState;
        private final WeakReference<TabLayout> mTabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.mTabLayoutRef = new WeakReference(tabLayout);
        }

        public void onPageScrollStateChanged(int state) {
            this.mPreviousScrollState = this.mScrollState;
            this.mScrollState = state;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = (TabLayout) this.mTabLayoutRef.get();
            if (tabLayout != null) {
                boolean updateIndicator;
                boolean updateText = this.mScrollState != 2 || this.mPreviousScrollState == 1;
                if (this.mScrollState == 2 && this.mPreviousScrollState == 0) {
                    updateIndicator = false;
                } else {
                    updateIndicator = true;
                }
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator);
            }
        }

        public void onPageSelected(int position) {
            TabLayout tabLayout = (TabLayout) this.mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position && position < tabLayout.getTabCount()) {
                boolean updateIndicator = this.mScrollState == 0 || (this.mScrollState == 2 && this.mPreviousScrollState == 0);
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator);
            }
        }

        void reset() {
            this.mScrollState = 0;
            this.mPreviousScrollState = 0;
        }
    }

    class TabView extends LinearLayout {
        private ImageView mCustomIconView;
        private TextView mCustomTextView;
        private View mCustomView;
        private int mDefaultMaxLines = 1;
        TextView mDotBadgeView;
        private ImageView mIconView;
        private SeslAbsIndicatorView mIndicatorView;
        private boolean mIsCallPerformClick;
        private View mMainTabTouchBackground;
        TextView mNBadgeView;
        private Tab mTab;
        private RelativeLayout mTextParentView;
        private TextView mTextView;

        /* renamed from: android.support.design.widget.TabLayout$TabView$1 */
        class C00501 implements AnimationListener {
            C00501() {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                AnimationSet as_u = new AnimationSet(true);
                as_u.setFillAfter(true);
                AlphaAnimation aa_u = new AlphaAnimation(1.0f, 0.0f);
                aa_u.setDuration(400);
                aa_u.setFillAfter(true);
                as_u.addAnimation(aa_u);
                TabView.this.mMainTabTouchBackground.startAnimation(aa_u);
            }
        }

        public TabView(Context context) {
            super(context);
            if (!(TabLayout.this.mTabBackgroundResId == 0 || TabLayout.this.mDepthStyle == 2)) {
                ViewCompat.setBackground(this, AppCompatResources.getDrawable(context, TabLayout.this.mTabBackgroundResId));
            }
            if (TabLayout.this.mDepthStyle == 1) {
                setPaddingRelative(TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
            } else {
                setPaddingRelative(0, TabLayout.this.mTabPaddingTop, 0, TabLayout.this.mTabPaddingBottom);
            }
            setGravity(17);
            setOrientation(1);
            setClickable(true);
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (isEnabled()) {
                return startTabTouchAnimation(event);
            }
            return super.onTouchEvent(event);
        }

        private boolean startTabTouchAnimation(MotionEvent event) {
            int action = event.getAction() & 255;
            if (this.mTab.getCustomView() == null && this.mTextView != null) {
                Tab oldSelectedTab;
                switch (action) {
                    case 0:
                        this.mIsCallPerformClick = false;
                        if (this.mTab.mPosition != TabLayout.this.getSelectedTabPosition() && this.mTextView != null) {
                            this.mTextView.setTypeface(TabLayout.this.mBoldTypeface);
                            TabLayout.this.seslStartTextColorChangeAnimation(this.mTextView, TabLayout.this.mTabTextColors.getDefaultColor(), TabLayout.this.seslGetSelctedTabTextColor());
                            if (this.mIndicatorView != null) {
                                this.mIndicatorView.setPressed();
                            }
                            oldSelectedTab = TabLayout.this.getTabAt(TabLayout.this.getSelectedTabPosition());
                            if (oldSelectedTab != null) {
                                if (oldSelectedTab.mView.mTextView != null) {
                                    oldSelectedTab.mView.mTextView.setTypeface(TabLayout.this.mNormalTypeface);
                                    TabLayout.this.seslStartTextColorChangeAnimation(oldSelectedTab.mView.mTextView, TabLayout.this.seslGetSelctedTabTextColor(), TabLayout.this.mTabTextColors.getDefaultColor());
                                }
                                if (oldSelectedTab.mView.mIndicatorView != null) {
                                    oldSelectedTab.mView.mIndicatorView.setHide();
                                }
                            }
                        } else if (this.mTab.mPosition == TabLayout.this.getSelectedTabPosition() && this.mIndicatorView != null) {
                            this.mIndicatorView.setPressed();
                        }
                        showMainTabTouchBackground(0);
                        break;
                    case 1:
                        showMainTabTouchBackground(1);
                        if (this.mTab.mPosition == TabLayout.this.getSelectedTabPosition() && this.mIndicatorView != null) {
                            this.mIndicatorView.setReleased();
                            this.mIndicatorView.onTouchEvent(event);
                        }
                        performClick();
                        this.mIsCallPerformClick = true;
                        break;
                    case 3:
                        this.mTextView.setTypeface(TabLayout.this.mNormalTypeface);
                        TabLayout.this.seslStartTextColorChangeAnimation(this.mTextView, TabLayout.this.seslGetSelctedTabTextColor(), TabLayout.this.mTabTextColors.getDefaultColor());
                        if (!(this.mIndicatorView == null || this.mIndicatorView.isSelected())) {
                            this.mIndicatorView.setHide();
                        }
                        oldSelectedTab = TabLayout.this.getTabAt(TabLayout.this.getSelectedTabPosition());
                        if (oldSelectedTab != null) {
                            if (oldSelectedTab.mView.mTextView != null) {
                                oldSelectedTab.mView.mTextView.setTypeface(TabLayout.this.mBoldTypeface);
                                TabLayout.this.seslStartTextColorChangeAnimation(oldSelectedTab.mView.mTextView, TabLayout.this.mTabTextColors.getDefaultColor(), TabLayout.this.seslGetSelctedTabTextColor());
                            }
                            if (oldSelectedTab.mView.mIndicatorView != null) {
                                oldSelectedTab.mView.mIndicatorView.setShow();
                            }
                        }
                        showMainTabTouchBackground(3);
                        break;
                }
            }
            return super.onTouchEvent(event);
        }

        private void showMainTabTouchBackground(int action) {
            if (this.mMainTabTouchBackground != null && TabLayout.this.mDepthStyle == 1) {
                this.mMainTabTouchBackground.setAlpha(1.0f);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setFillAfter(true);
                switch (action) {
                    case 0:
                        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
                        aa.setDuration(100);
                        aa.setFillAfter(true);
                        animationSet.addAnimation(aa);
                        ScaleAnimation ca = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 1, 0.5f, 1, 0.5f);
                        ca.setDuration(350);
                        ca.setInterpolator(getContext(), TabLayout.SESL_TAB_ANIM_INTERPOLATOR);
                        ca.setFillAfter(true);
                        animationSet.addAnimation(ca);
                        this.mMainTabTouchBackground.startAnimation(animationSet);
                        return;
                    case 1:
                    case 3:
                        if (this.mMainTabTouchBackground.getAnimation() == null) {
                            return;
                        }
                        if (this.mMainTabTouchBackground.getAnimation().hasEnded()) {
                            AlphaAnimation aa_u = new AlphaAnimation(1.0f, 0.0f);
                            aa_u.setDuration(400);
                            aa_u.setFillAfter(true);
                            animationSet.addAnimation(aa_u);
                            this.mMainTabTouchBackground.startAnimation(animationSet);
                            return;
                        }
                        this.mMainTabTouchBackground.getAnimation().setAnimationListener(new C00501());
                        return;
                    default:
                        return;
                }
            }
        }

        public boolean performClick() {
            if (this.mIsCallPerformClick) {
                this.mIsCallPerformClick = false;
                return true;
            }
            boolean handled = super.performClick();
            if (this.mTab == null) {
                return handled;
            }
            playSoundEffect(0);
            this.mTab.select();
            return true;
        }

        public void setSelected(boolean selected) {
            if (isEnabled()) {
                boolean changed = isSelected() != selected;
                super.setSelected(selected);
                if (changed && selected && VERSION.SDK_INT < 16) {
                    sendAccessibilityEvent(4);
                }
                if (this.mTextView != null) {
                    this.mTextView.setSelected(selected);
                }
                if (this.mIconView != null) {
                    this.mIconView.setSelected(selected);
                }
                if (this.mCustomView != null) {
                    this.mCustomView.setSelected(selected);
                }
                if (this.mCustomView == null && this.mIndicatorView != null) {
                    if (selected) {
                        this.mIndicatorView.setShow();
                    } else {
                        this.mIndicatorView.setHideImmediatly();
                    }
                }
            }
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            event.setClassName(android.support.v7.app.ActionBar.Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(android.support.v7.app.ActionBar.Tab.class.getName());
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            if (this.mMainTabTouchBackground != null) {
                this.mMainTabTouchBackground.setLeft(0);
                this.mMainTabTouchBackground.setRight(right - left);
                if (this.mMainTabTouchBackground.getAnimation() != null && this.mMainTabTouchBackground.getAnimation().hasEnded()) {
                    this.mMainTabTouchBackground.setAlpha(0.0f);
                }
            }
            TabLayout.this.seslUpdateBadgePosition();
        }

        public void onMeasure(int origWidthMeasureSpec, int origHeightMeasureSpec) {
            int widthMeasureSpec;
            int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
            int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);
            int maxWidth = TabLayout.this.getTabMaxWidth();
            int heightMeasureSpec = origHeightMeasureSpec;
            if (TabLayout.this.mRequestedTabWidth != -1) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(TabLayout.this.mRequestedTabWidth, 1073741824);
            } else if (maxWidth <= 0 || (specWidthMode != 0 && specWidthSize <= maxWidth)) {
                widthMeasureSpec = origWidthMeasureSpec;
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(TabLayout.this.mTabMaxWidth, Integer.MIN_VALUE);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (this.mTextView != null && this.mCustomView == null) {
                Resources res = getResources();
                float textSize = TabLayout.this.mTabTextSize;
                TabLayout.this.checkMaxFontScale(this.mTextView, (int) textSize);
                int maxLines = this.mDefaultMaxLines;
                if (this.mIconView != null && this.mIconView.getVisibility() == 0) {
                    maxLines = 1;
                } else if (this.mTextView != null && this.mTextView.getLineCount() > 1) {
                    textSize = TabLayout.this.mTabTextMultiLineSize;
                }
                float curTextSize = this.mTextView.getTextSize();
                int curLineCount = this.mTextView.getLineCount();
                int curMaxLines = TextViewCompat.getMaxLines(this.mTextView);
                if (textSize != curTextSize || (curMaxLines >= 0 && maxLines != curMaxLines)) {
                    boolean updateTextView = true;
                    if (TabLayout.this.mMode == 1 && textSize > curTextSize && curLineCount == 1) {
                        Layout layout = this.mTextView.getLayout();
                        if (layout == null || approximateLineWidth(layout, 0, textSize) > ((float) ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight()))) {
                            updateTextView = false;
                        }
                    }
                    if (updateTextView) {
                        this.mTextView.setTextSize(0, textSize);
                        TabLayout.this.checkMaxFontScale(this.mTextView, (int) textSize);
                        this.mTextView.setMaxLines(maxLines);
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
            }
            if (this.mCustomTextView == null && this.mTextParentView != null && this.mTextView != null && this.mTab != null) {
                int textWidth;
                ViewGroup.LayoutParams param;
                if (this.mTab.getIcon() != null && this.mIconView != null) {
                    this.mTextView.measure(0, 0);
                    textWidth = this.mTextView.getMeasuredWidth();
                    param = this.mTextParentView.getLayoutParams();
                    param.width = textWidth;
                    this.mTextParentView.setLayoutParams(param);
                } else if (TabLayout.this.mMode == 0 && TabLayout.this.mDepthStyle == 2) {
                    this.mTextView.measure(0, 0);
                    textWidth = this.mTextView.getMeasuredWidth();
                    param = this.mTextParentView.getLayoutParams();
                    param.width = TabLayout.this.dpToPx(24) + textWidth;
                    this.mTextParentView.setLayoutParams(param);
                    View dotBadgeView = this.mDotBadgeView;
                    if (dotBadgeView != null) {
                        ViewGroup.LayoutParams params = dotBadgeView.getLayoutParams();
                        if (params instanceof RelativeLayout.LayoutParams) {
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) params;
                            rlp.setMarginStart(-TabLayout.this.dpToPx(7));
                            dotBadgeView.setLayoutParams(rlp);
                        }
                    }
                }
            }
        }

        void setTab(Tab tab) {
            if (tab != this.mTab) {
                this.mTab = tab;
                update();
            }
        }

        void reset() {
            setTab(null);
            setSelected(false);
        }

        final void update() {
            View custom;
            boolean z;
            Tab tab = this.mTab;
            if (tab != null) {
                custom = tab.getCustomView();
            } else {
                custom = null;
            }
            if (custom != null) {
                TabView customParent = custom.getParent();
                if (customParent != this) {
                    if (customParent != null) {
                        customParent.removeView(custom);
                    }
                    addView(custom);
                }
                this.mCustomView = custom;
                if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                }
                if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                this.mCustomTextView = (TextView) custom.findViewById(16908308);
                if (this.mCustomTextView != null) {
                    this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mCustomTextView);
                }
                this.mCustomIconView = (ImageView) custom.findViewById(16908294);
            } else {
                if (this.mCustomView != null) {
                    removeView(this.mCustomView);
                    this.mCustomView = null;
                }
                this.mCustomTextView = null;
                this.mCustomIconView = null;
            }
            if (this.mCustomView == null) {
                if (this.mIconView == null) {
                    ImageView iconView = (ImageView) LayoutInflater.from(getContext()).inflate(C0011R.layout.sesl_layout_tab_icon, this, false);
                    addView(iconView, 0);
                    this.mIconView = iconView;
                }
                if (!(this.mTab == null || this.mTab.mIcon == null || this.mTextParentView == null)) {
                    removeView(this.mTextParentView);
                    this.mTextView = null;
                }
                if (this.mTextView == null) {
                    RelativeLayout parentView;
                    int width_mode;
                    int height_mode;
                    if (TabLayout.this.mDepthStyle == 2) {
                        parentView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(C0011R.layout.sesl_layout_sub_tab_text, this, false);
                        if (TabLayout.this.mMode == 0) {
                            width_mode = -2;
                        } else {
                            width_mode = -1;
                        }
                        height_mode = TabLayout.this.mSubTabIndicatorHeight;
                        this.mIndicatorView = (SeslAbsIndicatorView) parentView.findViewById(C0011R.id.indicator);
                        if (!(this.mIndicatorView == null || TabLayout.this.mSubTabSelectedIndicatorColor == -1)) {
                            this.mIndicatorView.setSelectedIndicatorColor(TabLayout.this.mSubTabSelectedIndicatorColor);
                        }
                    } else {
                        parentView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(C0011R.layout.sesl_layout_tab_text, this, false);
                        width_mode = -1;
                        if (this.mTab.mIcon == null) {
                            height_mode = -1;
                        } else {
                            height_mode = -1;
                            width_mode = -2;
                        }
                        this.mIndicatorView = (SeslAbsIndicatorView) parentView.findViewById(C0011R.id.indicator);
                        if (this.mIndicatorView != null) {
                            this.mIndicatorView.setSelectedIndicatorColor(TabLayout.this.mTabSelectedIndicatorColor);
                        }
                        this.mMainTabTouchBackground = parentView.findViewById(C0011R.id.main_tab_touch_background);
                        if (this.mMainTabTouchBackground != null && this.mTab.mIcon == null) {
                            Drawable drawable;
                            View view = this.mMainTabTouchBackground;
                            if (TabLayout.this.isLightTheme()) {
                                drawable = getContext().getDrawable(C0011R.drawable.sesl_tablayout_maintab_touch_background_light);
                            } else {
                                drawable = getContext().getDrawable(C0011R.drawable.sesl_tablayout_maintab_touch_background_dark);
                            }
                            view.setBackground(drawable);
                            this.mMainTabTouchBackground.setAlpha(0.0f);
                        }
                    }
                    TextView textView = (TextView) parentView.findViewById(C0011R.id.title);
                    addView(parentView, width_mode, height_mode);
                    this.mTextView = textView;
                    this.mTextParentView = parentView;
                    this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mTextView);
                }
                TextViewCompat.setTextAppearance(this.mTextView, TabLayout.this.mTabTextAppearance);
                TabLayout.this.checkMaxFontScale(this.mTextView, (int) TabLayout.this.mTabTextSize);
                if (!(this.mTextView == null || TabLayout.this.mTabTextColors == null)) {
                    this.mTextView.setTextColor(TabLayout.this.mTabTextColors);
                }
                updateTextAndIcon(this.mTextView, this.mIconView);
            } else if (!(this.mCustomTextView == null && this.mCustomIconView == null)) {
                updateTextAndIcon(this.mCustomTextView, this.mCustomIconView);
            }
            if (tab == null || !tab.isSelected()) {
                z = false;
            } else {
                z = true;
            }
            setSelected(z);
        }

        private void updateTextAndIcon(TextView textView, ImageView iconView) {
            Drawable icon;
            CharSequence text;
            CharSequence contentDesc;
            boolean hasText;
            CharSequence charSequence = null;
            if (this.mTab != null) {
                icon = this.mTab.getIcon();
            } else {
                icon = null;
            }
            if (this.mTab != null) {
                text = this.mTab.getText();
            } else {
                text = null;
            }
            if (this.mTab != null) {
                contentDesc = this.mTab.getContentDescription();
            } else {
                contentDesc = null;
            }
            if (iconView != null) {
                if (icon != null) {
                    iconView.setImageDrawable(icon);
                    iconView.setVisibility(0);
                    setVisibility(0);
                } else {
                    iconView.setVisibility(8);
                    iconView.setImageDrawable(null);
                }
                iconView.setContentDescription(contentDesc);
            }
            if (TextUtils.isEmpty(text)) {
                hasText = false;
            } else {
                hasText = true;
            }
            if (textView != null) {
                if (hasText) {
                    textView.setText(text);
                    textView.setVisibility(0);
                    setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText(null);
                }
                textView.setContentDescription(contentDesc);
            }
            if (iconView != null) {
                MarginLayoutParams lp = (MarginLayoutParams) iconView.getLayoutParams();
                int bottomMargin = 0;
                if (hasText && iconView.getVisibility() == 0) {
                    bottomMargin = TabLayout.this.dpToPx(8);
                }
                if (bottomMargin != lp.bottomMargin) {
                    lp.bottomMargin = bottomMargin;
                    iconView.requestLayout();
                }
            }
            if (!hasText) {
                charSequence = contentDesc;
            }
            TooltipCompat.setTooltipText(this, charSequence);
        }

        private float approximateLineWidth(Layout layout, int line, float textSize) {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
        }
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        public void onTabSelected(Tab tab) {
            this.mViewPager.setCurrentItem(tab.getPosition());
        }

        public void onTabUnselected(Tab tab) {
        }

        public void onTabReselected(Tab tab) {
        }
    }

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a;
        super(context, attrs, defStyleAttr);
        this.mSubTabIndicatorHeight = 1;
        this.mTabs = new ArrayList();
        this.mTabMaxWidth = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mSelectedListeners = new ArrayList();
        this.mDepthStyle = 1;
        this.mBadgeColor = -1;
        this.mBadgeTextColor = -1;
        this.mRequestedTabWidth = -1;
        this.mSubTabSelectedIndicatorColor = -1;
        this.mIsScaledTextSizeType = false;
        this.mTabViewPool = new SimplePool(12);
        ThemeUtils.checkAppCompatTheme(context);
        setHorizontalScrollBarEnabled(false);
        this.mTabStrip = new SlidingTabStrip(context);
        super.addView(this.mTabStrip, 0, new FrameLayout.LayoutParams(-2, -1));
        if (isLightTheme()) {
            a = context.obtainStyledAttributes(attrs, C0011R.styleable.TabLayout, defStyleAttr, C0011R.style.Widget_Design_TabLayout);
        } else {
            a = context.obtainStyledAttributes(attrs, C0011R.styleable.TabLayout, defStyleAttr, C0011R.style.Widget_Design_TabLayout_Dark);
        }
        this.mTabStrip.setSelectedIndicatorHeight(a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabIndicatorHeight, 0));
        this.mTabSelectedIndicatorColor = a.getColor(C0011R.styleable.TabLayout_tabIndicatorColor, 0);
        this.mTabStrip.setSelectedIndicatorColor(this.mTabSelectedIndicatorColor);
        int dimensionPixelSize = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabPadding, 0);
        this.mTabPaddingBottom = dimensionPixelSize;
        this.mTabPaddingEnd = dimensionPixelSize;
        this.mTabPaddingTop = dimensionPixelSize;
        this.mTabPaddingStart = dimensionPixelSize;
        this.mTabPaddingStart = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
        this.mTabPaddingTop = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
        this.mTabPaddingEnd = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
        this.mTabPaddingBottom = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
        this.mTabTextAppearance = a.getResourceId(C0011R.styleable.TabLayout_tabTextAppearance, C0011R.style.TextAppearance_Design_Tab);
        TypedArray ta = context.obtainStyledAttributes(this.mTabTextAppearance, C0247R.styleable.TextAppearance);
        try {
            this.mTabTextSize = (float) ta.getDimensionPixelSize(C0247R.styleable.TextAppearance_android_textSize, 0);
            this.mIsScaledTextSizeType = ta.getText(C0247R.styleable.TextAppearance_android_textSize).toString().contains("sp");
            this.mTabTextColors = ta.getColorStateList(C0247R.styleable.TextAppearance_android_textColor);
            this.mBoldTypeface = Typeface.create("sec-roboto-light", 1);
            this.mNormalTypeface = Typeface.create("sec-roboto-light", 0);
            this.mContentResolver = context.getContentResolver();
            this.mSubTabIndicatorHeight = getContext().getResources().getDimensionPixelSize(C0011R.dimen.sesl_tablayout_subtab_indicator_height);
            if (a.hasValue(C0011R.styleable.TabLayout_tabTextColor)) {
                this.mTabTextColors = a.getColorStateList(C0011R.styleable.TabLayout_tabTextColor);
            }
            if (a.hasValue(C0011R.styleable.TabLayout_tabSelectedTextColor)) {
                this.mTabTextColors = createColorStateList(this.mTabTextColors.getDefaultColor(), a.getColor(C0011R.styleable.TabLayout_tabSelectedTextColor, 0));
            }
            this.mRequestedTabMinWidth = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabMinWidth, -1);
            this.mRequestedTabMaxWidth = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabMaxWidth, -1);
            this.mTabBackgroundResId = a.getResourceId(C0011R.styleable.TabLayout_tabBackground, 0);
            this.mContentInsetStart = a.getDimensionPixelSize(C0011R.styleable.TabLayout_tabContentStart, 0);
            this.mMode = a.getInt(C0011R.styleable.TabLayout_tabMode, 1);
            if (this.mMode == 16) {
                this.mMode = 1;
            }
            this.mTabGravity = a.getInt(C0011R.styleable.TabLayout_tabGravity, 0);
            a.recycle();
            Resources res = getResources();
            this.mTabTextMultiLineSize = (float) res.getDimensionPixelSize(C0011R.dimen.sesl_tab_text_size_2line);
            this.mScrollableTabMinWidth = res.getDimensionPixelSize(C0011R.dimen.sesl_tab_scrollable_min_width);
            applyModeAndGravity();
        } finally {
            ta.recycle();
        }
    }

    private boolean isLightTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            return true;
        }
        return false;
    }

    public void setSelectedTabIndicatorColor(int color) {
        this.mTabSelectedIndicatorColor = color;
        Iterator it = this.mTabs.iterator();
        while (it.hasNext()) {
            SeslAbsIndicatorView iv = ((Tab) it.next()).mView.mIndicatorView;
            if (iv != null) {
                if (this.mDepthStyle != 2 || this.mSubTabSelectedIndicatorColor == -1) {
                    iv.setSelectedIndicatorColor(color);
                } else {
                    iv.setSelectedIndicatorColor(this.mSubTabSelectedIndicatorColor);
                }
                iv.invalidate();
            }
        }
    }

    public void setSelectedTabIndicatorHeight(int height) {
        this.mTabStrip.setSelectedIndicatorHeight(height);
    }

    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText) {
        setScrollPosition(position, positionOffset, updateSelectedText, true);
    }

    void setScrollPosition(int position, float positionOffset, boolean updateSelectedText, boolean updateIndicatorPosition) {
        if (getTabAt(position) == null || getTabAt(position).mView == null || getTabAt(position).mView.isEnabled()) {
            int roundedPosition = Math.round(((float) position) + positionOffset);
            if (roundedPosition >= 0 && roundedPosition < this.mTabStrip.getChildCount()) {
                if (updateIndicatorPosition) {
                    this.mTabStrip.setIndicatorPositionFromTabPosition(position, positionOffset);
                }
                if (this.mScrollAnimator != null && this.mScrollAnimator.isRunning()) {
                    this.mScrollAnimator.cancel();
                }
                scrollTo(calculateScrollXForTab(position, positionOffset), 0);
                if (updateSelectedText) {
                    setSelectedTabView(roundedPosition);
                }
            }
        }
    }

    private float getScrollPosition() {
        return this.mTabStrip.getIndicatorPosition();
    }

    public void addTab(Tab tab) {
        addTab(tab, this.mTabs.isEmpty());
    }

    public void addTab(Tab tab, boolean setSelected) {
        addTab(tab, this.mTabs.size(), setSelected);
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        configureTab(tab, position);
        addTabView(tab);
        if (setSelected) {
            tab.select();
        }
    }

    private void addTabFromItemView(TabItem item) {
        Tab tab = newTab();
        if (item.mText != null) {
            tab.setText(item.mText);
        }
        if (item.mIcon != null) {
            tab.setIcon(item.mIcon);
        }
        if (item.mCustomLayout != 0) {
            tab.setCustomView(item.mCustomLayout);
        }
        if (!TextUtils.isEmpty(item.getContentDescription())) {
            tab.setContentDescription(item.getContentDescription());
        }
        addTab(tab);
    }

    @Deprecated
    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        if (this.mSelectedListener != null) {
            removeOnTabSelectedListener(this.mSelectedListener);
        }
        this.mSelectedListener = listener;
        if (listener != null) {
            addOnTabSelectedListener(listener);
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (!this.mSelectedListeners.contains(listener)) {
            this.mSelectedListeners.add(listener);
        }
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener listener) {
        this.mSelectedListeners.remove(listener);
    }

    public Tab newTab() {
        Tab tab = (Tab) sTabPool.acquire();
        if (tab == null) {
            tab = new Tab();
        }
        tab.mParent = this;
        tab.mView = createTabView(tab);
        return tab;
    }

    public int getTabCount() {
        return this.mTabs.size();
    }

    public Tab getTabAt(int index) {
        return (index < 0 || index >= getTabCount()) ? null : (Tab) this.mTabs.get(index);
    }

    public int getSelectedTabPosition() {
        return this.mSelectedTab != null ? this.mSelectedTab.getPosition() : -1;
    }

    public void removeAllTabs() {
        for (int i = this.mTabStrip.getChildCount() - 1; i >= 0; i--) {
            removeTabViewAt(i);
        }
        Iterator<Tab> i2 = this.mTabs.iterator();
        while (i2.hasNext()) {
            Tab tab = (Tab) i2.next();
            i2.remove();
            tab.reset();
            sTabPool.release(tab);
        }
        this.mSelectedTab = null;
    }

    public void setTabMode(int mode) {
        if (mode != this.mMode) {
            if (mode == 16) {
                this.mMode = 1;
            } else {
                this.mMode = mode;
            }
            applyModeAndGravity();
        }
    }

    public int getTabMode() {
        return this.mMode;
    }

    public void setTabGravity(int gravity) {
        if (this.mTabGravity != gravity) {
            this.mTabGravity = gravity;
            applyModeAndGravity();
        }
    }

    public int getTabGravity() {
        return this.mTabGravity;
    }

    public void setTabTextColors(ColorStateList textColor) {
        if (this.mTabTextColors != textColor) {
            this.mTabTextColors = textColor;
            updateAllTabs();
        }
    }

    public ColorStateList getTabTextColors() {
        return this.mTabTextColors;
    }

    public void setupWithViewPager(ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(ViewPager viewPager, boolean autoRefresh) {
        setupWithViewPager(viewPager, autoRefresh, false);
    }

    private void setupWithViewPager(ViewPager viewPager, boolean autoRefresh, boolean implicitSetup) {
        if (this.mViewPager != null) {
            if (this.mPageChangeListener != null) {
                this.mViewPager.removeOnPageChangeListener(this.mPageChangeListener);
            }
            if (this.mAdapterChangeListener != null) {
                this.mViewPager.removeOnAdapterChangeListener(this.mAdapterChangeListener);
            }
        }
        if (this.mCurrentVpSelectedListener != null) {
            removeOnTabSelectedListener(this.mCurrentVpSelectedListener);
            this.mCurrentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.mViewPager = viewPager;
            if (this.mPageChangeListener == null) {
                this.mPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.mPageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.mPageChangeListener);
            this.mCurrentVpSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            addOnTabSelectedListener(this.mCurrentVpSelectedListener);
            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                setPagerAdapter(adapter, autoRefresh);
            }
            if (this.mAdapterChangeListener == null) {
                this.mAdapterChangeListener = new AdapterChangeListener();
            }
            this.mAdapterChangeListener.setAutoRefresh(autoRefresh);
            viewPager.addOnAdapterChangeListener(this.mAdapterChangeListener);
            setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.mViewPager = null;
            setPagerAdapter(null, false);
        }
        this.mSetupViewPagerImplicitly = implicitSetup;
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter adapter) {
        setPagerAdapter(adapter, false);
    }

    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int i = 0;
        while (i < getTabCount()) {
            if (!(getTabAt(i) == null || getTabAt(i).mView == null)) {
                if (getTabAt(i).mView.mMainTabTouchBackground != null) {
                    getTabAt(i).mView.mMainTabTouchBackground.setAlpha(0.0f);
                }
                if (getTabAt(i).mView.mIndicatorView != null) {
                    if (getSelectedTabPosition() == i) {
                        getTabAt(i).mView.mIndicatorView.setShow();
                    } else {
                        getTabAt(i).mView.mIndicatorView.setHideImmediatly();
                    }
                }
            }
            i++;
        }
        if (this.mViewPager == null) {
            ViewParent vp = getParent();
            if (vp instanceof ViewPager) {
                setupWithViewPager((ViewPager) vp, true, true);
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mSetupViewPagerImplicitly) {
            setupWithViewPager(null);
            this.mSetupViewPagerImplicitly = false;
        }
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.mTabStrip.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    void setPagerAdapter(PagerAdapter adapter, boolean addObserver) {
        if (!(this.mPagerAdapter == null || this.mPagerAdapterObserver == null)) {
            this.mPagerAdapter.unregisterDataSetObserver(this.mPagerAdapterObserver);
        }
        this.mPagerAdapter = adapter;
        if (addObserver && adapter != null) {
            if (this.mPagerAdapterObserver == null) {
                this.mPagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(this.mPagerAdapterObserver);
        }
        populateFromPagerAdapter();
    }

    void populateFromPagerAdapter() {
        removeAllTabs();
        if (this.mPagerAdapter != null) {
            int adapterCount = this.mPagerAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                addTab(newTab().setText(this.mPagerAdapter.getPageTitle(i)), false);
            }
            if (this.mViewPager != null && adapterCount > 0) {
                int curItem = this.mViewPager.getCurrentItem();
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    selectTab(getTabAt(curItem));
                }
            }
        }
    }

    private void updateAllTabs() {
        int z = this.mTabs.size();
        for (int i = 0; i < z; i++) {
            ((Tab) this.mTabs.get(i)).updateView();
        }
    }

    private TabView createTabView(Tab tab) {
        TabView tabView = this.mTabViewPool != null ? (TabView) this.mTabViewPool.acquire() : null;
        if (tabView == null) {
            tabView = new TabView(getContext());
        }
        tabView.setTab(tab);
        tabView.setFocusable(true);
        tabView.setMinimumWidth(getTabMinWidth());
        return tabView;
    }

    private void configureTab(Tab tab, int position) {
        tab.setPosition(position);
        this.mTabs.add(position, tab);
        int count = this.mTabs.size();
        for (int i = position + 1; i < count; i++) {
            ((Tab) this.mTabs.get(i)).setPosition(i);
        }
    }

    private void addTabView(Tab tab) {
        this.mTabStrip.addView(tab.mView, tab.getPosition(), createLayoutParamsForTabs());
    }

    public void addView(View child) {
        addViewInternal(child);
    }

    public void addView(View child, int index) {
        addViewInternal(child);
    }

    public void addView(View child, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    private void addViewInternal(View child) {
        if (child instanceof TabItem) {
            addTabFromItemView((TabItem) child);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private LayoutParams createLayoutParamsForTabs() {
        LayoutParams lp = new LayoutParams(-2, -1);
        updateTabViewLayoutParams(lp);
        return lp;
    }

    private void updateTabViewLayoutParams(LayoutParams lp) {
        if (this.mMode == 1 && this.mTabGravity == 0) {
            lp.width = 0;
            lp.weight = 1.0f;
            return;
        }
        lp.width = -2;
        lp.weight = 0.0f;
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * ((float) dps));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int idealHeight = (dpToPx(getDefaultHeight()) + getPaddingTop()) + getPaddingBottom();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case Integer.MIN_VALUE:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)), 1073741824);
                break;
            case 0:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, 1073741824);
                break;
        }
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) != 0) {
            int i;
            if (this.mRequestedTabMaxWidth > 0) {
                i = this.mRequestedTabMaxWidth;
            } else {
                i = specWidth - dpToPx(0);
            }
            this.mTabMaxWidth = i;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 1) {
            View child = getChildAt(0);
            boolean remeasure = false;
            switch (this.mMode) {
                case 0:
                    if (child.getMeasuredWidth() < getMeasuredWidth()) {
                        remeasure = true;
                    } else {
                        remeasure = false;
                    }
                    break;
                case 1:
                    remeasure = child.getMeasuredWidth() != getMeasuredWidth();
                    break;
            }
            if (remeasure) {
                child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height));
            }
        }
    }

    private void removeTabViewAt(int position) {
        TabView view = (TabView) this.mTabStrip.getChildAt(position);
        this.mTabStrip.removeViewAt(position);
        if (view != null) {
            view.reset();
            this.mTabViewPool.release(view);
        }
        requestLayout();
    }

    private void animateToTab(int newPosition) {
        if (newPosition != -1) {
            if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || this.mTabStrip.childrenNeedLayout()) {
                setScrollPosition(newPosition, 0.0f, true);
                return;
            }
            if (getScrollX() != calculateScrollXForTab(newPosition, 0.0f)) {
                ensureScrollAnimator();
                this.mScrollAnimator.setIntValues(new int[]{startScrollX, targetScrollX});
                this.mScrollAnimator.start();
            }
            this.mTabStrip.animateIndicatorToPosition(newPosition, 300);
        }
    }

    private void ensureScrollAnimator() {
        if (this.mScrollAnimator == null) {
            this.mScrollAnimator = new ValueAnimator();
            this.mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrollAnimator.setDuration(300);
            this.mScrollAnimator.addUpdateListener(new C00471());
        }
    }

    void setScrollAnimatorListener(AnimatorListener listener) {
        ensureScrollAnimator();
        this.mScrollAnimator.addListener(listener);
    }

    private void setSelectedTabView(int position) {
        int oldSelected = getSelectedTabPosition();
        int tabCount = this.mTabStrip.getChildCount();
        if (position < tabCount) {
            int i;
            for (i = 0; i < tabCount; i++) {
                boolean z;
                View child = this.mTabStrip.getChildAt(i);
                if (i == position) {
                    z = true;
                } else {
                    z = false;
                }
                child.setSelected(z);
            }
            ((Tab) this.mTabs.get(position)).mView.setSelected(true);
            for (i = 0; i < getTabCount(); i++) {
                TabView tabView = ((Tab) this.mTabs.get(i)).mView;
                if (i == position) {
                    if (tabView.mTextView != null) {
                        if (tabView.mTextView.getCurrentTextColor() != seslGetSelctedTabTextColor()) {
                            seslStartTextColorChangeAnimation(tabView.mTextView, this.mTabTextColors.getDefaultColor(), seslGetSelctedTabTextColor());
                        } else {
                            seslStartTextColorChangeAnimation(tabView.mTextView, seslGetSelctedTabTextColor(), seslGetSelctedTabTextColor());
                        }
                        tabView.mTextView.setTypeface(this.mBoldTypeface);
                        tabView.mTextView.setSelected(true);
                    }
                    if (((Tab) this.mTabs.get(i)).mView.mIndicatorView != null) {
                        ((Tab) this.mTabs.get(i)).mView.mIndicatorView.setReleased();
                    }
                } else {
                    if (tabView.mIndicatorView != null) {
                        tabView.mIndicatorView.setHideImmediatly();
                    }
                    if (tabView.mTextView != null) {
                        tabView.mTextView.setTypeface(this.mNormalTypeface);
                        if (tabView.mTextView.getCurrentTextColor() != this.mTabTextColors.getDefaultColor()) {
                            seslStartTextColorChangeAnimation(tabView.mTextView, seslGetSelctedTabTextColor(), this.mTabTextColors.getDefaultColor());
                        } else {
                            seslStartTextColorChangeAnimation(tabView.mTextView, this.mTabTextColors.getDefaultColor(), this.mTabTextColors.getDefaultColor());
                        }
                        tabView.mTextView.setSelected(false);
                    }
                }
            }
        }
    }

    void selectTab(Tab tab) {
        selectTab(tab, true);
    }

    void selectTab(Tab tab, boolean updateIndicator) {
        if (tab.mView.isEnabled() || this.mViewPager == null) {
            Tab currentTab = this.mSelectedTab;
            if (currentTab != tab) {
                int newPosition;
                if (tab != null) {
                    newPosition = tab.getPosition();
                } else {
                    newPosition = -1;
                }
                if (updateIndicator) {
                    if ((currentTab == null || currentTab.getPosition() == -1) && newPosition != -1) {
                        setScrollPosition(newPosition, 0.0f, true);
                    } else {
                        animateToTab(newPosition);
                    }
                    if (newPosition != -1) {
                        setSelectedTabView(newPosition);
                    }
                }
                if (currentTab != null) {
                    dispatchTabUnselected(currentTab);
                }
                this.mSelectedTab = tab;
                if (tab != null) {
                    dispatchTabSelected(tab);
                    return;
                }
                return;
            } else if (currentTab != null) {
                dispatchTabReselected(tab);
                animateToTab(tab.getPosition());
                return;
            } else {
                return;
            }
        }
        this.mViewPager.setCurrentItem(getSelectedTabPosition());
    }

    private void dispatchTabSelected(Tab tab) {
        for (int i = this.mSelectedListeners.size() - 1; i >= 0; i--) {
            ((OnTabSelectedListener) this.mSelectedListeners.get(i)).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(Tab tab) {
        for (int i = this.mSelectedListeners.size() - 1; i >= 0; i--) {
            ((OnTabSelectedListener) this.mSelectedListeners.get(i)).onTabUnselected(tab);
        }
    }

    private void dispatchTabReselected(Tab tab) {
        for (int i = this.mSelectedListeners.size() - 1; i >= 0; i--) {
            ((OnTabSelectedListener) this.mSelectedListeners.get(i)).onTabReselected(tab);
        }
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        int nextWidth = 0;
        if (this.mMode != 0) {
            return 0;
        }
        int selectedWidth;
        View selectedChild = this.mTabStrip.getChildAt(position);
        View nextChild = position + 1 < this.mTabStrip.getChildCount() ? this.mTabStrip.getChildAt(position + 1) : null;
        if (selectedChild != null) {
            selectedWidth = selectedChild.getWidth();
        } else {
            selectedWidth = 0;
        }
        if (nextChild != null) {
            nextWidth = nextChild.getWidth();
        }
        int scrollBase = (selectedChild.getLeft() + (selectedWidth / 2)) - (getWidth() / 2);
        int scrollOffset = (int) ((((float) (selectedWidth + nextWidth)) * 0.5f) * positionOffset);
        return ViewCompat.getLayoutDirection(this) == 0 ? scrollBase + scrollOffset : scrollBase - scrollOffset;
    }

    private void applyModeAndGravity() {
        this.mTabStrip.setPaddingRelative(0, 0, 0, 0);
        switch (this.mMode) {
            case 0:
                this.mTabStrip.setGravity(GravityCompat.START);
                break;
            case 1:
                this.mTabStrip.setGravity(1);
                break;
        }
        updateTabViews(true);
    }

    void updateTabViews(boolean requestLayout) {
        for (int i = 0; i < this.mTabStrip.getChildCount(); i++) {
            View child = this.mTabStrip.getChildAt(i);
            child.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LayoutParams) child.getLayoutParams());
            if (requestLayout) {
                child.requestLayout();
            }
        }
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        states = new int[2][];
        int[] colors = new int[]{SELECTED_STATE_SET, selectedColor};
        int i = 0 + 1;
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;
        return new ColorStateList(states, colors);
    }

    private int getDefaultHeight() {
        boolean hasIconAndText = false;
        int count = this.mTabs.size();
        for (int i = 0; i < count; i++) {
            Tab tab = (Tab) this.mTabs.get(i);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                hasIconAndText = true;
                break;
            }
        }
        if (hasIconAndText) {
            return 72;
        }
        return 48;
    }

    private int getTabMinWidth() {
        if (this.mRequestedTabMinWidth != -1) {
            return this.mRequestedTabMinWidth;
        }
        return this.mMode == 0 ? this.mScrollableTabMinWidth : 0;
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return generateDefaultLayoutParams();
    }

    int getTabMaxWidth() {
        return this.mTabMaxWidth;
    }

    private void checkMaxFontScale(TextView textview, int baseSize) {
        float currentFontScale = getResources().getConfiguration().fontScale;
        if (textview != null && this.mIsScaledTextSizeType && currentFontScale > 1.3f) {
            textview.setTextSize(0, 1.3f * (((float) baseSize) / currentFontScale));
        }
    }

    private void seslUpdateBadgePosition() {
        if (this.mTabs != null && this.mTabs.size() > 0) {
            int i = 0;
            while (i < this.mTabs.size()) {
                if (!(this.mTabs.get(i) == null || ((Tab) this.mTabs.get(i)).mView == null)) {
                    TabView tabView = ((Tab) this.mTabs.get(i)).mView;
                    TextView title = tabView.mTextView;
                    if (tabView.getWidth() > 0 && title != null && title.getWidth() > 0) {
                        TextView badge = null;
                        if (tabView.mNBadgeView != null && tabView.mNBadgeView.getVisibility() == 0) {
                            badge = tabView.mNBadgeView;
                        } else if (tabView.mDotBadgeView != null && tabView.mDotBadgeView.getVisibility() == 0) {
                            badge = tabView.mDotBadgeView;
                        }
                        if (badge != null) {
                            badge.measure(0, 0);
                            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                            RelativeLayout.LayoutParams params;
                            if ((tabView.getWidth() - title.getWidth()) / 2 < badge.getMeasuredWidth()) {
                                params = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                                if (getRelativeLayoutRule(params, 17) != 0) {
                                    params.addRule(17, 0);
                                    params.addRule(21);
                                    badge.setLayoutParams(params);
                                }
                            } else {
                                params = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                                if (getRelativeLayoutRule(params, 17) != C0011R.id.title) {
                                    params.addRule(17, C0011R.id.title);
                                    params.removeRule(21);
                                    badge.setLayoutParams(params);
                                }
                            }
                        }
                    }
                }
                i++;
            }
        }
    }

    private int getRelativeLayoutRule(RelativeLayout.LayoutParams params, int verb) {
        int[] rules = params.getRules();
        if (verb == 17) {
            verb = isLayoutRTL() ? 16 : 1;
        }
        return rules[verb];
    }

    private boolean isLayoutRTL() {
        return getLayoutDirection() == 1;
    }

    private int seslGetSelctedTabTextColor() {
        if (this.mTabTextColors != null) {
            return this.mTabTextColors.getColorForState(new int[]{16842913, 16842910}, this.mTabTextColors.getDefaultColor());
        }
        return -1;
    }

    private void seslStartTextColorChangeAnimation(TextView textview, int fromColor, int toColor) {
        if (textview != null) {
            textview.setTextColor(toColor);
        }
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        int i = 0;
        while (i < getTabCount()) {
            if (!(getTabAt(i) == null || getTabAt(i).mView == null || getTabAt(i).mView.mMainTabTouchBackground == null)) {
                getTabAt(i).mView.mMainTabTouchBackground.setAlpha(0.0f);
            }
            i++;
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int i = 0;
        while (i < getTabCount()) {
            if (!(getTabAt(i) == null || getTabAt(i).mView == null || getTabAt(i).mView.mMainTabTouchBackground == null)) {
                getTabAt(i).mView.mMainTabTouchBackground.setAlpha(0.0f);
            }
            i++;
        }
    }
}
