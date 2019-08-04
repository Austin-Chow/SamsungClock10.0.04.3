package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.ActionProvider.SubUiVisibilityListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.util.SeslShowButtonBackgroundHelper;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.ActionMenuItemView.PopupCallback;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter.Callback;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuView.ItemView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionMenuView.ActionMenuChildView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

class ActionMenuPresenter extends BaseMenuPresenter implements SubUiVisibilityListener {
    private static final float MENU_WIDTH_LIMIT_FACTOR = 0.7f;
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private boolean mHasNavigationBar = false;
    private boolean mIsLightTheme = false;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    private NumberFormat mNumberFormat = NumberFormat.getInstance(Locale.getDefault());
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private CharSequence mTooltipText;
    private boolean mUseTextItemMode;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    private class ActionButtonSubmenu extends MenuPopupHelper {
        public ActionButtonSubmenu(Context context, SubMenuBuilder subMenu, View anchorView) {
            super(context, subMenu, anchorView, false, C0247R.attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl) subMenu.getItem()).isActionButton()) {
                setAnchorView(ActionMenuPresenter.this.mOverflowButton == null ? (View) ActionMenuPresenter.this.mMenuView : ActionMenuPresenter.this.mOverflowButton);
            }
            setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        protected void onDismiss() {
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }

    private class ActionMenuPopupCallback extends PopupCallback {
        ActionMenuPopupCallback() {
        }

        public ShowableListMenu getPopup() {
            return ActionMenuPresenter.this.mActionButtonPopup != null ? ActionMenuPresenter.this.mActionButtonPopup.getPopup() : null;
        }
    }

    private class OpenOverflowRunnable implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup popup) {
            this.mPopup = popup;
        }

        public void run() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            View menuView = (View) ActionMenuPresenter.this.mMenuView;
            if (!(menuView == null || menuView.getWindowToken() == null || !this.mPopup.tryShow())) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    private class OverflowImageView extends AppCompatImageView implements ActionMenuChildView {
        private CharSequence mContentDescription;
        private int mNavigationBarHeight = 0;
        private SeslShowButtonBackgroundHelper mSBBHelper;

        public OverflowImageView(Context context) {
            super(context, null, C0247R.attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            setLongClickable(true);
            ActionMenuPresenter.this.mTooltipText = getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_description);
            TooltipCompat.setTooltipText(this, ActionMenuPresenter.this.mTooltipText);
            if (VERSION.SDK_INT <= 27) {
                this.mSBBHelper = new SeslShowButtonBackgroundHelper(this, getResources().getDrawable(C0247R.drawable.sesl_more_button_show_button_background, null), getBackground());
            }
        }

        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            TypedArray a = getContext().obtainStyledAttributes(null, C0247R.styleable.View, C0247R.attr.actionOverflowButtonStyle, 0);
            setMinimumHeight(a.getDimensionPixelSize(C0247R.styleable.View_android_minHeight, 0));
            ActionMenuPresenter.this.mTooltipText = getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_description);
            a.recycle();
            a = getContext().obtainStyledAttributes(null, C0247R.styleable.AppCompatImageView, C0247R.attr.actionOverflowButtonStyle, 0);
            Drawable d = getContext().getDrawable(a.getResourceId(C0247R.styleable.AppCompatImageView_android_src, -1));
            if (d != null) {
                setImageDrawable(d);
            }
            a.recycle();
            if (this.mSBBHelper != null) {
                this.mSBBHelper.updateOverflowButtonBackground(getContext().getDrawable(C0247R.drawable.sesl_more_button_show_button_background));
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (this.mSBBHelper != null) {
                this.mSBBHelper.updateButtonBackground();
            }
        }

        public boolean performClick() {
            if (!super.performClick()) {
                playSoundEffect(0);
                if (ActionMenuPresenter.this.showOverflowMenu() && isHovered()) {
                    TooltipCompat.setTooltipNull(true);
                }
            }
            return true;
        }

        public boolean dispatchGenericMotionEvent(MotionEvent event) {
            switch (event.getAction()) {
                case 7:
                    break;
                case 9:
                    TooltipCompat.setTooltipText(this, ActionMenuPresenter.this.mTooltipText);
                    break;
                case 10:
                    TooltipCompat.setTooltipNull(true);
                    break;
            }
            if (!ActionMenuPresenter.this.isOverflowMenuShowing()) {
                TooltipCompat.setTooltipNull(false);
            }
            setTooltipOffset();
            return super.dispatchGenericMotionEvent(event);
        }

        public boolean performLongClick() {
            setTooltipOffset();
            return super.performLongClick();
        }

        protected void setTooltipOffset() {
            int xOffset;
            Context context = getContext();
            Resources res = context.getResources();
            int[] screenPos = new int[2];
            getLocationOnScreen(screenPos);
            int width = getWidth();
            int height = getHeight();
            int paddingStart = getPaddingStart();
            int paddingEnd = getPaddingEnd();
            int[] windowPos = new int[2];
            getLocationInWindow(windowPos);
            Rect displayFrame = new Rect();
            getWindowVisibleDisplayFrame(displayFrame);
            Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            display.getRealMetrics(realDisplayMetrics);
            int diff = 0;
            View toolBar = ActionMenuPresenter.this.mMenuView != null ? (View) ((View) ActionMenuPresenter.this.mMenuView).getParent() : null;
            if ((toolBar instanceof Toolbar) && toolBar.getWidth() < displayFrame.right - displayFrame.left) {
                diff = (screenPos[0] - windowPos[0]) - displayFrame.left;
            }
            int yOffset = windowPos[1] + height;
            int layoutDirection = getLayoutDirection();
            if (layoutDirection == 0) {
                xOffset = (((displayFrame.right - displayFrame.left) - (windowPos[0] + width)) + (((width - paddingStart) - paddingEnd) / 2)) - diff;
                if (checkNaviBarForLandscape()) {
                    xOffset += (int) ((((float) getNavigationBarHeight()) / res.getDisplayMetrics().density) * realDisplayMetrics.density);
                }
            } else {
                xOffset = (windowPos[0] + paddingStart) + ((paddingEnd - paddingStart) / 2);
            }
            TooltipCompat.setTooltipPosition(xOffset, yOffset, layoutDirection);
        }

        private boolean checkNaviBarForLandscape() {
            Context context = getContext();
            Resources res = context.getResources();
            Rect displayFrame = new Rect();
            getWindowVisibleDisplayFrame(displayFrame);
            Point displaySize = new Point();
            Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            display.getRealSize(displaySize);
            int rotate = display.getRotation();
            int navigationBarHeight = (int) res.getDimension(C0247R.dimen.sesl_navigation_bar_height);
            if (rotate == 1 && displayFrame.right + navigationBarHeight >= displaySize.x) {
                setNavigationBarHeight(displaySize.x - displayFrame.right);
                return true;
            } else if (rotate != 3 || displayFrame.left > navigationBarHeight) {
                return false;
            } else {
                setNavigationBarHeight(displayFrame.left);
                return true;
            }
        }

        private void setNavigationBarHeight(int height) {
            this.mNavigationBarHeight = height;
        }

        private int getNavigationBarHeight() {
            return this.mNavigationBarHeight;
        }

        protected boolean setFrame(int l, int t, int r, int b) {
            boolean changed = super.setFrame(l, t, r, b);
            Drawable d = getDrawable();
            Drawable bg = getBackground();
            if (!(d == null || bg == null)) {
                int width = getWidth();
                int halfOffsetX = (getPaddingLeft() - getPaddingRight()) / 2;
                bg.setHotspotBounds(halfOffsetX, 0, halfOffsetX + width, getHeight());
            }
            return changed;
        }

        public boolean needsDividerBefore() {
            return false;
        }

        public boolean needsDividerAfter() {
            return false;
        }
    }

    class OverflowMenuButton extends FrameLayout implements ActionMenuChildView {
        private static final int BADGE_LIMIT_NUMBER = 99;
        private ViewGroup mBadgeBackground;
        private CharSequence mBadgeContentDescription;
        private TextView mBadgeText;
        private CharSequence mContentDescription;
        private View mInnerView;
        private final float[] mTempPts = new float[2];

        public OverflowMenuButton(Context context) {
            super(context);
            this.mInnerView = ActionMenuPresenter.this.mUseTextItemMode ? new OverflowTextView(ActionMenuPresenter.this, context) : new OverflowImageView(context);
            addView(this.mInnerView, new LayoutParams(-2, -2));
            if (this.mInnerView instanceof OverflowImageView) {
                this.mContentDescription = this.mInnerView.getContentDescription();
                this.mBadgeContentDescription = this.mContentDescription + " , " + getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_badge_description);
            }
            if (TextUtils.isEmpty(this.mContentDescription)) {
                this.mContentDescription = getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_description);
                if (this.mInnerView != null) {
                    this.mInnerView.setContentDescription(this.mContentDescription);
                }
            }
            this.mBadgeBackground = (ViewGroup) ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0247R.layout.sesl_action_menu_item_badge, this, false);
            this.mBadgeText = (TextView) this.mBadgeBackground.getChildAt(0);
            addView(this.mBadgeBackground);
        }

        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            this.mBadgeText.setTextSize(0, (float) ((int) getResources().getDimension(C0247R.dimen.sesl_menu_item_badge_text_size)));
            MarginLayoutParams lp = (MarginLayoutParams) this.mBadgeBackground.getLayoutParams();
            lp.width = (int) (getResources().getDimension(C0247R.dimen.sesl_badge_default_width) + (((float) this.mBadgeText.getText().length()) * getResources().getDimension(C0247R.dimen.sesl_badge_additional_width)));
            lp.height = (int) getResources().getDimension(C0247R.dimen.sesl_menu_item_badge_size);
            lp.setMarginEnd((int) getResources().getDimension(C0247R.dimen.sesl_menu_item_badge_end_margin));
            this.mBadgeBackground.setLayoutParams(lp);
            if (this.mInnerView instanceof OverflowImageView) {
                this.mContentDescription = getContentDescription();
                this.mBadgeContentDescription = this.mContentDescription + " , " + getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_badge_description);
            }
            if (TextUtils.isEmpty(this.mContentDescription)) {
                this.mContentDescription = getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_description);
                this.mBadgeContentDescription = this.mContentDescription + " , " + getContext().getResources().getString(C0247R.string.sesl_action_menu_overflow_badge_description);
            }
            if (this.mBadgeBackground.getVisibility() == 0) {
                if (this.mInnerView instanceof OverflowImageView) {
                    this.mInnerView.setContentDescription(this.mBadgeContentDescription);
                }
            } else if (this.mInnerView instanceof OverflowImageView) {
                this.mInnerView.setContentDescription(this.mContentDescription);
            }
        }

        public View getInnerView() {
            return this.mInnerView;
        }

        public boolean needsDividerBefore() {
            return false;
        }

        public boolean needsDividerAfter() {
            return false;
        }

        public void setBadgeText(String badgeString, int number) {
            String mBadgeString;
            int mBadgeNumber = 99;
            if (number <= 99) {
                mBadgeNumber = number;
            }
            if (badgeString != null || badgeString.equals("")) {
                mBadgeString = badgeString;
            } else {
                mBadgeString = ActionMenuPresenter.this.mNumberFormat.format((long) mBadgeNumber);
            }
            this.mBadgeText.setText(mBadgeString);
            int width = (int) (getResources().getDimension(C0247R.dimen.sesl_badge_default_width) + (((float) mBadgeString.length()) * getResources().getDimension(C0247R.dimen.sesl_badge_additional_width)));
            ViewGroup.LayoutParams lp = this.mBadgeBackground.getLayoutParams();
            lp.width = width;
            this.mBadgeBackground.setLayoutParams(lp);
            this.mBadgeBackground.setVisibility(mBadgeNumber > 0 ? 0 : 8);
            if (this.mBadgeBackground.getVisibility() == 0) {
                if (this.mInnerView instanceof OverflowImageView) {
                    this.mInnerView.setContentDescription(this.mBadgeContentDescription);
                }
            } else if (this.mInnerView instanceof OverflowImageView) {
                this.mInnerView.setContentDescription(this.mContentDescription);
            }
        }
    }

    private class OverflowPopup extends MenuPopupHelper {
        public OverflowPopup(Context context, MenuBuilder menu, View anchorView, boolean overflowOnly) {
            super(context, menu, anchorView, overflowOnly, C0247R.attr.actionOverflowMenuStyle);
            setGravity(GravityCompat.END);
            setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        protected void onDismiss() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
            super.onDismiss();
        }
    }

    private class OverflowTextView extends AppCompatTextView {
        private SeslShowButtonBackgroundHelper mSBBHelper;
        final /* synthetic */ ActionMenuPresenter this$0;

        public OverflowTextView(ActionMenuPresenter actionMenuPresenter, Context context) {
            boolean z = false;
            this.this$0 = actionMenuPresenter;
            super(context, null, C0247R.attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            TypedArray a = context.getTheme().obtainStyledAttributes(null, C0247R.styleable.AppCompatTheme, 0, 0);
            TextViewCompat.setTextAppearance(this, a.getResourceId(C0247R.styleable.AppCompatTheme_actionMenuTextAppearance, 0));
            a.recycle();
            setText(getResources().getString(C0247R.string.sesl_more_item_label));
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
            if (outValue.data != 0) {
                z = true;
            }
            actionMenuPresenter.mIsLightTheme = z;
            if (actionMenuPresenter.mIsLightTheme) {
                setBackgroundResource(C0247R.drawable.sesl_action_bar_item_text_background);
            } else {
                setBackgroundResource(C0247R.drawable.sesl_action_bar_item_text_background_dark);
            }
            if (VERSION.SDK_INT > 27) {
                seslSetButtonShapeEnabled(true);
            } else {
                this.mSBBHelper = new SeslShowButtonBackgroundHelper(this, getResources().getDrawable(C0247R.drawable.sesl_action_text_button_show_button_background, null), getBackground());
            }
        }

        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (this.mSBBHelper != null) {
                this.mSBBHelper.updateButtonBackground();
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (this.mSBBHelper != null) {
                this.mSBBHelper.updateButtonBackground();
            }
        }

        public boolean performClick() {
            if (!super.performClick()) {
                playSoundEffect(0);
                this.this$0.showOverflowMenu();
            }
            return true;
        }
    }

    private class PopupPresenterCallback implements Callback {
        PopupPresenterCallback() {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) subMenu).getItem().getItemId();
            Callback cb = ActionMenuPresenter.this.getCallback();
            return cb != null ? cb.onOpenSubMenu(subMenu) : false;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu instanceof SubMenuBuilder) {
                menu.getRootMenu().close(false);
            }
            Callback cb = ActionMenuPresenter.this.getCallback();
            if (cb != null) {
                cb.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    private static class SavedState implements Parcelable {
        public static final Creator<SavedState> CREATOR = new C02861();
        public int openSubMenuId;

        /* renamed from: android.support.v7.widget.ActionMenuPresenter$SavedState$1 */
        static class C02861 implements Creator<SavedState> {
            C02861() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        SavedState() {
        }

        SavedState(Parcel in) {
            this.openSubMenuId = in.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.openSubMenuId);
        }
    }

    public ActionMenuPresenter(Context context) {
        super(context, C0247R.layout.sesl_action_menu_layout, C0247R.layout.sesl_action_menu_item_layout);
        this.mUseTextItemMode = context.getResources().getBoolean(C0247R.bool.sesl_action_bar_text_item_mode);
        this.mHasNavigationBar = ActionBarPolicy.get(context).hasNavigationBar();
    }

    public void initForMenu(Context context, MenuBuilder menu) {
        super.initForMenu(context, menu);
        Resources res = context.getResources();
        ActionBarPolicy abp = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = abp.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = abp.getEmbeddedMenuWidthLimit();
            this.mWidthLimit = (int) (((float) this.mContext.getResources().getDisplayMetrics().widthPixels) * MENU_WIDTH_LIMIT_FACTOR);
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = abp.getMaxActionButtons();
        }
        int width = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    if (this.mUseTextItemMode) {
                        ((AppCompatImageView) this.mOverflowButton.getInnerView()).setImageDrawable(this.mPendingOverflowIcon);
                    }
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                int spec = MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(spec, spec);
            }
            width -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = width;
        this.mMinCellSize = (int) (56.0f * res.getDisplayMetrics().density);
        this.mScrapActionButtonView = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = (int) (((float) this.mContext.getResources().getDisplayMetrics().widthPixels) * MENU_WIDTH_LIMIT_FACTOR);
        }
        if (!this.mReserveOverflow || this.mOverflowButton == null) {
            this.mActionItemWidthLimit = this.mWidthLimit;
        } else {
            this.mActionItemWidthLimit = this.mWidthLimit - this.mOverflowButton.getMeasuredWidth();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    public void setWidthLimit(int width, boolean strict) {
        this.mWidthLimit = width;
        this.mStrictWidthLimit = strict;
        this.mWidthLimitSet = true;
    }

    public void setReserveOverflow(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
        this.mReserveOverflowSet = true;
    }

    public void setItemLimit(int itemCount) {
        this.mMaxItems = itemCount;
        this.mMaxItemsSet = true;
    }

    public void setExpandedActionViewsExclusive(boolean isExclusive) {
        this.mExpandedActionViewsExclusive = isExclusive;
    }

    public void setOverflowIcon(Drawable icon) {
        if (!this.mUseTextItemMode) {
            if (this.mOverflowButton != null) {
                ((AppCompatImageView) this.mOverflowButton.getInnerView()).setImageDrawable(icon);
                return;
            }
            this.mPendingOverflowIconSet = true;
            this.mPendingOverflowIcon = icon;
        }
    }

    public Drawable getOverflowIcon() {
        if (this.mUseTextItemMode) {
            return null;
        }
        if (this.mOverflowButton != null) {
            return ((AppCompatImageView) this.mOverflowButton.getInnerView()).getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    public MenuView getMenuView(ViewGroup root) {
        MenuView oldMenuView = this.mMenuView;
        MenuView result = super.getMenuView(root);
        if (oldMenuView != result) {
            ((ActionMenuView) result).setPresenter(this);
        }
        return result;
    }

    public View getItemView(MenuItemImpl item, View convertView, ViewGroup parent) {
        View actionView = item.getActionView();
        if (actionView == null || item.hasCollapsibleActionView()) {
            actionView = super.getItemView(item, convertView, parent);
        }
        actionView.setVisibility(item.isActionViewExpanded() ? 8 : 0);
        ActionMenuView menuParent = (ActionMenuView) parent;
        ViewGroup.LayoutParams lp = actionView.getLayoutParams();
        if (!menuParent.checkLayoutParams(lp)) {
            actionView.setLayoutParams(menuParent.generateLayoutParams(lp));
        }
        return actionView;
    }

    public void bindItemView(MenuItemImpl item, ItemView itemView) {
        itemView.initialize(item, 0);
        ActionMenuItemView actionItemView = (ActionMenuItemView) itemView;
        actionItemView.setItemInvoker(this.mMenuView);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionItemView.setPopupCallback(this.mPopupCallback);
    }

    public boolean shouldIncludeItem(int childIndex, MenuItemImpl item) {
        return item.isActionButton();
    }

    public void updateMenuView(boolean cleared) {
        int count;
        ActionMenuView menuView;
        super.updateMenuView(cleared);
        if (this.mMenuView != null) {
            ((View) this.mMenuView).requestLayout();
        }
        if (this.mMenu != null) {
            ArrayList<MenuItemImpl> actionItems = this.mMenu.getActionItems();
            count = actionItems.size();
            for (int i = 0; i < count; i++) {
                ActionProvider provider = ((MenuItemImpl) actionItems.get(i)).getSupportActionProvider();
                if (provider != null) {
                    provider.setSubUiVisibilityListener(this);
                }
            }
        }
        ArrayList<MenuItemImpl> nonActionItems = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        boolean hasOverflow = false;
        if (this.mReserveOverflow && nonActionItems != null) {
            count = nonActionItems.size();
            hasOverflow = count == 1 ? !((MenuItemImpl) nonActionItems.get(0)).isActionViewExpanded() : count > 0;
        }
        if (hasOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            ViewGroup parent = (ViewGroup) this.mOverflowButton.getParent();
            if (parent != this.mMenuView) {
                if (parent != null) {
                    parent.removeView(this.mOverflowButton);
                }
                menuView = this.mMenuView;
                if (menuView != null) {
                    menuView.addView(this.mOverflowButton, menuView.generateOverflowButtonLayoutParams());
                }
            }
        } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            if (this.mMenuView != null) {
                ((ViewGroup) this.mMenuView).removeView(this.mOverflowButton);
            }
            if (isOverflowMenuShowing()) {
                hideOverflowMenu();
            }
        }
        if (!(this.mOverflowButton == null || this.mMenuView == null)) {
            menuView = (ActionMenuView) this.mMenuView;
            this.mOverflowButton.setBadgeText(menuView.getOverflowBadgeText(), menuView.getSumOfDigitsInBadges());
        }
        if ((this.mOverflowButton == null || this.mOverflowButton.getVisibility() != 0) && isOverflowMenuShowing()) {
            hideOverflowMenu();
        }
        if (this.mMenuView != null) {
            ((ActionMenuView) this.mMenuView).setOverflowReserved(this.mReserveOverflow);
        }
    }

    public boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(parent, childIndex);
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (subMenu == null || !subMenu.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != this.mMenu) {
            topSubMenu = (SubMenuBuilder) topSubMenu.getParentMenu();
        }
        View anchor = findViewForItem(topSubMenu.getItem());
        if (anchor == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenu.getItem().getItemId();
        boolean preserveIconSpacing = false;
        int count = subMenu.size();
        for (int i = 0; i < count; i++) {
            MenuItem childItem = subMenu.getItem(i);
            if (childItem.isVisible() && childItem.getIcon() != null) {
                preserveIconSpacing = true;
                break;
            }
        }
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenu, anchor);
        this.mActionButtonPopup.setForceShowIcon(preserveIconSpacing);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenu);
        return true;
    }

    private View findViewForItem(MenuItem item) {
        ViewGroup parent = this.mMenuView;
        if (parent == null) {
            return null;
        }
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            if ((child instanceof ItemView) && ((ItemView) child).getItemData() == item) {
                return child;
            }
        }
        return null;
    }

    public boolean showOverflowMenu() {
        if (!this.mReserveOverflow || isOverflowMenuShowing() || this.mMenu == null || this.mMenuView == null || this.mPostedOpenRunnable != null || this.mMenu.getNonActionItems().isEmpty()) {
            return false;
        }
        this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true));
        ((View) this.mMenuView).post(this.mPostedOpenRunnable);
        super.onSubMenuSelected(null);
        return true;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable == null || this.mMenuView == null) {
            MenuPopupHelper popup = this.mOverflowPopup;
            if (popup == null) {
                return false;
            }
            popup.dismiss();
            return true;
        }
        ((View) this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
        this.mPostedOpenRunnable = null;
        return true;
    }

    public boolean dismissPopupMenus() {
        return hideOverflowMenu() | hideSubMenus();
    }

    public boolean hideSubMenus() {
        if (this.mActionButtonPopup == null) {
            return false;
        }
        this.mActionButtonPopup.dismiss();
        return true;
    }

    public boolean isOverflowMenuShowing() {
        return this.mOverflowPopup != null && this.mOverflowPopup.isShowing();
    }

    public boolean isOverflowMenuShowPending() {
        return this.mPostedOpenRunnable != null || isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public boolean flagActionItems() {
        ArrayList<MenuItemImpl> visibleItems;
        int itemsSize;
        if (this.mMenu != null) {
            visibleItems = this.mMenu.getVisibleItems();
            itemsSize = visibleItems.size();
        } else {
            visibleItems = null;
            itemsSize = 0;
        }
        int maxActions = this.mMaxItems;
        int widthLimit = this.mActionItemWidthLimit;
        int querySpec = MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup parent = (ViewGroup) this.mMenuView;
        if (this.mMenuView == null) {
            Log.d(TAG, "mMenuView is null, maybe Menu has not been initialized.");
            return false;
        }
        int i;
        int requiredItems = 0;
        int requestedItems = 0;
        int firstActionWidth = 0;
        boolean hasOverflow = false;
        for (i = 0; i < itemsSize; i++) {
            MenuItemImpl item = (MenuItemImpl) visibleItems.get(i);
            if (item.requiresActionButton()) {
                requiredItems++;
            } else if (item.requestsActionButton()) {
                requestedItems++;
            } else {
                hasOverflow = true;
            }
            if (this.mExpandedActionViewsExclusive && item.isActionViewExpanded()) {
                maxActions = 0;
            }
        }
        if (this.mReserveOverflow && (hasOverflow || requiredItems + requestedItems > maxActions)) {
            maxActions--;
        }
        maxActions -= requiredItems;
        SparseBooleanArray seenGroups = this.mActionButtonGroups;
        seenGroups.clear();
        int cellSize = 0;
        int cellsRemaining = 0;
        if (this.mStrictWidthLimit) {
            cellsRemaining = widthLimit / this.mMinCellSize;
            cellSize = this.mMinCellSize + ((widthLimit % this.mMinCellSize) / cellsRemaining);
        }
        for (i = 0; i < itemsSize; i++) {
            item = (MenuItemImpl) visibleItems.get(i);
            View v;
            int measuredWidth;
            int groupId;
            if (item.requiresActionButton()) {
                v = getItemView(item, this.mScrapActionButtonView, parent);
                if (this.mScrapActionButtonView == null) {
                    this.mScrapActionButtonView = v;
                }
                if (this.mStrictWidthLimit) {
                    cellsRemaining -= ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                } else {
                    v.measure(querySpec, querySpec);
                }
                measuredWidth = v.getMeasuredWidth();
                widthLimit -= measuredWidth;
                if (firstActionWidth == 0) {
                    firstActionWidth = measuredWidth;
                }
                groupId = item.getGroupId();
                if (groupId != 0) {
                    seenGroups.put(groupId, true);
                }
                item.setIsActionButton(true);
            } else if (item.requestsActionButton()) {
                groupId = item.getGroupId();
                boolean inGroup = seenGroups.get(groupId);
                boolean isAction = (maxActions > 0 || inGroup) && widthLimit > 0 && (!this.mStrictWidthLimit || cellsRemaining > 0);
                if (isAction) {
                    v = getItemView(item, this.mScrapActionButtonView, parent);
                    if (this.mScrapActionButtonView == null) {
                        this.mScrapActionButtonView = v;
                    }
                    if (this.mStrictWidthLimit) {
                        int cells = ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                        cellsRemaining -= cells;
                        if (cells == 0) {
                            isAction = false;
                        }
                    } else {
                        v.measure(querySpec, querySpec);
                    }
                    measuredWidth = v.getMeasuredWidth();
                    widthLimit -= measuredWidth;
                    if (firstActionWidth == 0) {
                        firstActionWidth = measuredWidth;
                    }
                    isAction &= widthLimit >= 0 ? 1 : 0;
                }
                if (isAction && groupId != 0) {
                    seenGroups.put(groupId, true);
                } else if (inGroup) {
                    seenGroups.put(groupId, false);
                    for (int j = 0; j < i; j++) {
                        MenuItemImpl areYouMyGroupie = (MenuItemImpl) visibleItems.get(j);
                        if (areYouMyGroupie.getGroupId() == groupId) {
                            if (areYouMyGroupie.isActionButton()) {
                                maxActions++;
                            }
                            areYouMyGroupie.setIsActionButton(false);
                        }
                    }
                }
                if (isAction) {
                    maxActions--;
                }
                item.setIsActionButton(isAction);
            } else {
                item.setIsActionButton(false);
            }
        }
        return true;
    }

    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        dismissPopupMenus();
        super.onCloseMenu(menu, allMenusAreClosing);
    }

    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState();
        state.openSubMenuId = this.mOpenSubMenuId;
        return state;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState saved = (SavedState) state;
            if (saved.openSubMenuId > 0) {
                MenuItem item = this.mMenu.findItem(saved.openSubMenuId);
                if (item != null) {
                    onSubMenuSelected((SubMenuBuilder) item.getSubMenu());
                }
            }
        }
    }

    public void onSubUiVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            super.onSubMenuSelected(null);
        } else if (this.mMenu != null) {
            this.mMenu.close(false);
        }
    }

    public void setMenuView(ActionMenuView menuView) {
        this.mMenuView = menuView;
        menuView.initialize(this.mMenu);
    }
}
