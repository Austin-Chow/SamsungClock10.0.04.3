package android.support.design.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.support.design.C0011R;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SynchronizedPool;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.appcompat.C0247R;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class BottomNavigationMenuView extends ViewGroup implements MenuView {
    private int mActiveItemMaxWidth;
    private BottomNavigationItemView[] mButtons;
    private ContentResolver mContentResolver;
    private boolean mHasIcon;
    private final int mInactiveItemMaxWidth;
    private final int mInactiveItemMinWidth;
    private int mItemBackgroundRes;
    private int mItemHeight;
    private ColorStateList mItemIconTint;
    private final Pool<BottomNavigationItemView> mItemPool;
    private ColorStateList mItemTextColor;
    private MenuBuilder mMenu;
    private final OnClickListener mOnClickListener;
    private BottomNavigationPresenter mPresenter;
    private ColorDrawable mSBBTextColorDrawable;
    private int mSelectedItemId;
    private int mSelectedItemPosition;
    private final TransitionSet mSet;
    private boolean mShiftingMode;
    private int[] mTempChildWidths;
    private final float mWidthPercent;

    /* renamed from: android.support.design.internal.BottomNavigationMenuView$1 */
    class C00121 implements OnClickListener {
        C00121() {
        }

        public void onClick(View v) {
            MenuItem item = ((BottomNavigationItemView) v).getItemData();
            if (!BottomNavigationMenuView.this.mMenu.performItemAction(item, BottomNavigationMenuView.this.mPresenter, 0)) {
                item.setChecked(true);
            }
        }
    }

    public BottomNavigationMenuView(Context context) {
        this(context, null);
    }

    public BottomNavigationMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mItemPool = new SynchronizedPool(5);
        this.mShiftingMode = false;
        this.mSelectedItemId = 0;
        this.mSelectedItemPosition = 0;
        this.mWidthPercent = 0.9f;
        Resources res = getResources();
        this.mInactiveItemMaxWidth = res.getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_item_max_width);
        this.mInactiveItemMinWidth = res.getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_item_min_width);
        this.mActiveItemMaxWidth = (int) (((float) getResources().getDisplayMetrics().widthPixels) * 0.9f);
        this.mItemHeight = res.getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_height);
        this.mSet = new AutoTransition();
        this.mSet.setOrdering(0);
        this.mSet.setDuration(0);
        this.mSet.setInterpolator(new FastOutSlowInInterpolator());
        this.mSet.addTransition(new TextScale());
        this.mOnClickListener = new C00121();
        this.mTempChildWidths = new int[5];
        this.mContentResolver = context.getContentResolver();
    }

    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int count = getChildCount();
        if (this.mHasIcon) {
            this.mItemHeight = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_height);
        } else {
            this.mItemHeight = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_height_text);
        }
        int heightSpec = MeasureSpec.makeMeasureSpec(this.mItemHeight, 1073741824);
        int extra;
        int i2;
        int[] iArr;
        if (this.mShiftingMode) {
            int inactiveCount = count - 1;
            int activeWidth = Math.min(width - (this.mInactiveItemMinWidth * inactiveCount), this.mActiveItemMaxWidth);
            int inactiveWidth = Math.min((width - activeWidth) / inactiveCount, this.mInactiveItemMaxWidth);
            extra = (width - activeWidth) - (inactiveWidth * inactiveCount);
            for (i = 0; i < count; i++) {
                int[] iArr2 = this.mTempChildWidths;
                if (i == this.mSelectedItemPosition) {
                    i2 = activeWidth;
                } else {
                    i2 = inactiveWidth;
                }
                iArr2[i] = i2;
                if (extra > 0) {
                    iArr = this.mTempChildWidths;
                    iArr[i] = iArr[i] + 1;
                    extra--;
                }
            }
        } else {
            int childWidth;
            if (count == 0) {
                i2 = 1;
            } else {
                i2 = count;
            }
            int maxAvailable = width / i2;
            if (count != 2) {
                childWidth = Math.min(maxAvailable, this.mActiveItemMaxWidth);
            } else {
                childWidth = maxAvailable;
            }
            extra = width - (childWidth * count);
            for (i = 0; i < count; i++) {
                this.mTempChildWidths[i] = childWidth;
                if (extra > 0) {
                    iArr = this.mTempChildWidths;
                    iArr[i] = iArr[i] + 1;
                    extra--;
                }
            }
        }
        int totalWidth = 0;
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                child.measure(MeasureSpec.makeMeasureSpec(this.mTempChildWidths[i], 1073741824), heightSpec);
                child.getLayoutParams().width = child.getMeasuredWidth();
                totalWidth += child.getMeasuredWidth();
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(totalWidth, MeasureSpec.makeMeasureSpec(totalWidth, 1073741824), 0), View.resolveSizeAndState(this.mItemHeight, heightSpec, 0));
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mActiveItemMaxWidth = (int) (((float) getResources().getDisplayMetrics().widthPixels) * 0.9f);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int itemPadding;
        int count = getChildCount();
        int width = right - left;
        int height = bottom - top;
        int used = 0;
        if (this.mHasIcon) {
            itemPadding = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_padding);
        } else {
            itemPadding = 0;
        }
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                if (ViewCompat.getLayoutDirection(this) == 1) {
                    child.layout(((width - used) - child.getMeasuredWidth()) - itemPadding, 0, (width - used) - itemPadding, height);
                } else {
                    child.layout(used + itemPadding, 0, (child.getMeasuredWidth() + used) - itemPadding, height);
                }
                used += child.getMeasuredWidth();
            }
        }
    }

    public int getWindowAnimations() {
        return 0;
    }

    public void setIconTintList(ColorStateList tint) {
        this.mItemIconTint = tint;
        if (this.mButtons != null) {
            for (BottomNavigationItemView item : this.mButtons) {
                item.setIconTintList(tint);
            }
        }
    }

    public ColorStateList getIconTintList() {
        return this.mItemIconTint;
    }

    public void setItemTextColor(ColorStateList color) {
        this.mItemTextColor = color;
        if (this.mButtons != null) {
            for (BottomNavigationItemView item : this.mButtons) {
                item.setTextColor(color);
            }
        }
    }

    public ColorStateList getItemTextColor() {
        return this.mItemTextColor;
    }

    public void setItemBackgroundRes(int background) {
        this.mItemBackgroundRes = background;
        if (this.mButtons != null) {
            for (BottomNavigationItemView item : this.mButtons) {
                item.setItemBackground(background);
            }
        }
    }

    public void setBackgroundColorDrawable(ColorDrawable colorDrawable) {
        this.mSBBTextColorDrawable = colorDrawable;
    }

    public ColorDrawable getBackgroundColorDrawable() {
        return this.mSBBTextColorDrawable;
    }

    public int getItemBackgroundRes() {
        return this.mItemBackgroundRes;
    }

    public void setPresenter(BottomNavigationPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void buildMenuView() {
        int i;
        removeAllViews();
        if (this.mButtons != null) {
            for (BottomNavigationItemView item : this.mButtons) {
                this.mItemPool.release(item);
            }
        }
        if (this.mMenu.size() == 0) {
            this.mSelectedItemId = 0;
            this.mSelectedItemPosition = 0;
            this.mButtons = null;
            return;
        }
        this.mButtons = new BottomNavigationItemView[this.mMenu.size()];
        if (this.mMenu.size() > 3) {
            i = 1;
        } else {
            i = 0;
        }
        this.mShiftingMode = i & getShiftMode();
        for (int i2 = 0; i2 < this.mMenu.size(); i2++) {
            this.mPresenter.setUpdateSuspended(true);
            this.mMenu.getItem(i2).setCheckable(true);
            this.mPresenter.setUpdateSuspended(false);
            BottomNavigationItemView child = getNewItem();
            if (((MenuItemImpl) this.mMenu.getItem(i2)).getIcon() == null) {
                child = getNewItem(false);
            } else {
                child = getNewItem(true);
            }
            this.mButtons[i2] = child;
            child.setIconTintList(this.mItemIconTint);
            child.setTextColor(this.mItemTextColor);
            child.setItemBackground(this.mItemBackgroundRes);
            child.setShiftingMode(this.mShiftingMode);
            child.initialize((MenuItemImpl) this.mMenu.getItem(i2), 0);
            child.setItemPosition(i2);
            child.setOnClickListener(this.mOnClickListener);
            addView(child);
            ColorStateList background = this.mItemTextColor;
            if (VERSION.SDK_INT > 26) {
                if (seslIsShowButtonEnabled()) {
                    int textcolor;
                    if (this.mSBBTextColorDrawable != null) {
                        textcolor = this.mSBBTextColorDrawable.getColor();
                    } else if (isLightTheme()) {
                        textcolor = getResources().getColor(C0011R.color.sesl_bottom_navigation_background_light);
                    } else {
                        textcolor = getResources().getColor(C0011R.color.sesl_bottom_navigation_background_dark);
                    }
                    child.setShowButtonShape(textcolor, background);
                }
            } else if (seslIsShowButtonEnabled()) {
                child.setShowButtonShape(0, background);
            }
        }
        this.mSelectedItemPosition = Math.min(this.mMenu.size() - 1, this.mSelectedItemPosition);
        this.mMenu.getItem(this.mSelectedItemPosition).setChecked(true);
    }

    public void updateMenuView() {
        int menuSize = this.mMenu.size();
        if (menuSize != this.mButtons.length) {
            buildMenuView();
            return;
        }
        int i;
        int previousSelectedId = this.mSelectedItemId;
        for (i = 0; i < menuSize; i++) {
            MenuItem item = this.mMenu.getItem(i);
            if (item.isChecked()) {
                this.mSelectedItemId = item.getItemId();
                this.mSelectedItemPosition = i;
            }
        }
        if (previousSelectedId != this.mSelectedItemId) {
            TransitionManager.beginDelayedTransition(this, this.mSet);
        }
        for (i = 0; i < menuSize; i++) {
            this.mPresenter.setUpdateSuspended(true);
            this.mButtons[i].initialize((MenuItemImpl) this.mMenu.getItem(i), 0);
            this.mPresenter.setUpdateSuspended(false);
        }
    }

    public void setShiftMode(boolean isShiftMode) {
        this.mShiftingMode = isShiftMode;
    }

    public boolean getShiftMode() {
        return this.mShiftingMode;
    }

    private BottomNavigationItemView getNewItem(boolean hasIcon) {
        BottomNavigationItemView item = (BottomNavigationItemView) this.mItemPool.acquire();
        this.mHasIcon = hasIcon;
        if (item == null) {
            return new BottomNavigationItemView(getContext(), hasIcon);
        }
        return item;
    }

    private BottomNavigationItemView getNewItem() {
        BottomNavigationItemView item = (BottomNavigationItemView) this.mItemPool.acquire();
        if (item == null) {
            return new BottomNavigationItemView(getContext());
        }
        return item;
    }

    public int getSelectedItemId() {
        return this.mSelectedItemId;
    }

    void tryRestoreSelectedItemId(int itemId) {
        int size = this.mMenu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = this.mMenu.getItem(i);
            if (itemId == item.getItemId()) {
                this.mSelectedItemId = itemId;
                this.mSelectedItemPosition = i;
                item.setChecked(true);
                return;
            }
        }
    }

    private boolean seslIsShowButtonEnabled() {
        return System.getInt(this.mContentResolver, "show_button_background", 0) == 1;
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
