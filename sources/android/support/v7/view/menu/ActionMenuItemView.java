package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v7.appcompat.C0247R;
import android.support.v7.util.SeslShowButtonBackgroundHelper;
import android.support.v7.view.menu.MenuBuilder.ItemInvoker;
import android.support.v7.view.menu.MenuView.ItemView;
import android.support.v7.widget.ActionMenuView.ActionMenuChildView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

public class ActionMenuItemView extends AppCompatTextView implements ItemView, ActionMenuChildView, OnClickListener, OnLongClickListener {
    private boolean mAllowTextWithIcon;
    private float mDefaultTextSize;
    private boolean mExpandedFormat;
    private ForwardingListener mForwardingListener;
    private Drawable mIcon;
    private boolean mIsChangedRelativePadding;
    private boolean mIsLastItem;
    private boolean mIsLightTheme;
    MenuItemImpl mItemData;
    ItemInvoker mItemInvoker;
    private int mMaxIconSize;
    private int mMinWidth;
    private int mNavigationBarHeight;
    PopupCallback mPopupCallback;
    private SeslShowButtonBackgroundHelper mSBBHelper;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;

    private class ActionMenuItemForwardingListener extends ForwardingListener {
        public ActionMenuItemForwardingListener() {
            super(ActionMenuItemView.this);
        }

        public ShowableListMenu getPopup() {
            if (ActionMenuItemView.this.mPopupCallback != null) {
                return ActionMenuItemView.this.mPopupCallback.getPopup();
            }
            return null;
        }

        protected boolean onForwardingStarted() {
            if (ActionMenuItemView.this.mItemInvoker == null || !ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
                return false;
            }
            ShowableListMenu popup = getPopup();
            if (popup == null || !popup.isShowing()) {
                return false;
            }
            return true;
        }
    }

    public static abstract class PopupCallback {
        public abstract ShowableListMenu getPopup();
    }

    public ActionMenuItemView(Context context) {
        this(context, null);
    }

    public ActionMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsChangedRelativePadding = false;
        this.mDefaultTextSize = 0.0f;
        this.mIsLastItem = false;
        this.mIsLightTheme = false;
        this.mNavigationBarHeight = 0;
        Resources res = context.getResources();
        this.mAllowTextWithIcon = shouldAllowTextWithIcon();
        TypedArray a = context.obtainStyledAttributes(attrs, C0247R.styleable.ActionMenuItemView, defStyle, 0);
        this.mMinWidth = a.getDimensionPixelSize(C0247R.styleable.ActionMenuItemView_android_minWidth, 0);
        a.recycle();
        this.mMaxIconSize = (int) ((32.0f * res.getDisplayMetrics().density) + 0.5f);
        setOnClickListener(this);
        setOnLongClickListener(this);
        this.mSavedPaddingLeft = -1;
        setSaveEnabled(false);
        a = context.getTheme().obtainStyledAttributes(null, C0247R.styleable.AppCompatTheme, 0, 0);
        int actionMeneTextAppearnceId = a.getResourceId(C0247R.styleable.AppCompatTheme_actionMenuTextAppearance, 0);
        a.recycle();
        a = getContext().obtainStyledAttributes(actionMeneTextAppearnceId, C0247R.styleable.TextAppearance);
        TypedValue outValue = a.peekValue(C0247R.styleable.TextAppearance_android_textSize);
        a.recycle();
        if (outValue != null) {
            this.mDefaultTextSize = TypedValue.complexToFloat(outValue.data);
        }
        if (VERSION.SDK_INT > 27) {
            seslSetButtonShapeEnabled(true);
        } else {
            this.mSBBHelper = new SeslShowButtonBackgroundHelper(this, getResources().getDrawable(C0247R.drawable.sesl_action_text_button_show_button_background, null), getBackground());
        }
        this.mIsLightTheme = isLightTheme();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mAllowTextWithIcon = shouldAllowTextWithIcon();
        updateTextButtonVisibility();
    }

    public void setBackground(Drawable background) {
        super.setBackground(background);
        if (this.mSBBHelper != null) {
            this.mSBBHelper.setBackgroundOff(background);
        }
    }

    private boolean shouldAllowTextWithIcon() {
        Configuration config = getContext().getResources().getConfiguration();
        int widthDp = config.screenWidthDp;
        return widthDp >= 480 || ((widthDp >= 640 && config.screenHeightDp >= 480) || config.orientation == 2);
    }

    public void setPaddingRelative(int l, int t, int r, int b) {
        this.mSavedPaddingLeft = l;
        this.mIsChangedRelativePadding = true;
        super.setPaddingRelative(l, t, r, b);
    }

    public void setPadding(int l, int t, int r, int b) {
        this.mSavedPaddingLeft = l;
        super.setPadding(l, t, r, b);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitleForItemView(this));
        setId(itemData.getItemId());
        setVisibility(itemData.isVisible() ? 0 : 8);
        setEnabled(itemData.isEnabled());
        if (itemData.hasSubMenu() && this.mForwardingListener == null) {
            this.mForwardingListener = new ActionMenuItemForwardingListener();
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        if (this.mItemData.hasSubMenu() && this.mForwardingListener != null && this.mForwardingListener.onTouch(this, e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public void onClick(View v) {
        if (this.mItemInvoker != null) {
            this.mItemInvoker.invokeItem(this.mItemData);
        }
    }

    public void setItemInvoker(ItemInvoker invoker) {
        this.mItemInvoker = invoker;
    }

    public void setPopupCallback(PopupCallback popupCallback) {
        this.mPopupCallback = popupCallback;
    }

    public boolean prefersCondensedTitle() {
        return true;
    }

    public void setCheckable(boolean checkable) {
    }

    public void setChecked(boolean checked) {
    }

    public void setExpandedFormat(boolean expandedFormat) {
        if (this.mExpandedFormat != expandedFormat) {
            this.mExpandedFormat = expandedFormat;
            if (this.mItemData != null) {
                this.mItemData.actionFormatChanged();
            }
        }
    }

    private void updateTextButtonVisibility() {
        boolean visible;
        CharSequence charSequence;
        int i = 0;
        CharSequence charSequence2 = null;
        if (TextUtils.isEmpty(this.mTitle)) {
            visible = false;
        } else {
            visible = true;
        }
        if (this.mIcon == null || (this.mItemData.showsTextAsAction() && (this.mAllowTextWithIcon || this.mExpandedFormat))) {
            i = 1;
        }
        visible &= i;
        if (visible) {
            charSequence = this.mTitle;
        } else {
            charSequence = null;
        }
        setText(charSequence);
        if (visible) {
            if (this.mIsLightTheme) {
                setBackgroundResource(C0247R.drawable.sesl_action_bar_item_text_background);
            } else {
                setBackgroundResource(C0247R.drawable.sesl_action_bar_item_text_background_dark);
            }
        }
        CharSequence contentDescription = this.mItemData.getContentDescription();
        if (TextUtils.isEmpty(contentDescription)) {
            setContentDescription(visible ? null : this.mItemData.getTitle());
        } else {
            setContentDescription(contentDescription);
        }
        CharSequence tooltipText = this.mItemData.getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            TooltipCompat.setTooltipText(this, visible ? null : this.mItemData.getTitle());
        } else {
            TooltipCompat.setTooltipText(this, tooltipText);
        }
        if (this.mDefaultTextSize > 0.0f) {
            float fontScale = getContext().getResources().getConfiguration().fontScale;
            if (fontScale > 1.2f) {
                fontScale = 1.2f;
            }
            setTextSize(1, this.mDefaultTextSize * fontScale);
        }
        if (visible) {
            charSequence2 = this.mTitle;
        }
        setText(charSequence2);
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (icon != null) {
            float scale;
            int width = icon.getIntrinsicWidth();
            int height = icon.getIntrinsicHeight();
            if (width > this.mMaxIconSize) {
                scale = ((float) this.mMaxIconSize) / ((float) width);
                width = this.mMaxIconSize;
                height = (int) (((float) height) * scale);
            }
            if (height > this.mMaxIconSize) {
                scale = ((float) this.mMaxIconSize) / ((float) height);
                height = this.mMaxIconSize;
                width = (int) (((float) width) * scale);
            }
            icon.setBounds(0, 0, width, height);
        }
        if (hasText() && getLayoutDirection() == 1) {
            setCompoundDrawables(null, null, icon, null);
        } else {
            setCompoundDrawables(icon, null, null, null);
        }
        updateTextButtonVisibility();
    }

    public boolean hasText() {
        return !TextUtils.isEmpty(getText());
    }

    public void setIsLastItem(boolean isLastItem) {
        this.mIsLastItem = isLastItem;
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        setContentDescription(this.mTitle);
        updateTextButtonVisibility();
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Button.class.getName());
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        CharSequence cdesc = getContentDescription();
        if (!TextUtils.isEmpty(cdesc)) {
            event.getText().add(cdesc);
        }
    }

    public boolean needsDividerBefore() {
        return hasText() && this.mItemData.getIcon() == null;
    }

    public boolean needsDividerAfter() {
        return hasText();
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        TooltipCompat.setTooltipNull(false);
        if (this.mIcon == null) {
            return super.dispatchGenericMotionEvent(event);
        }
        switch (event.getAction()) {
            case 9:
                TooltipCompat.setTooltipText(this, this.mItemData.getTitle());
                break;
            case 10:
                TooltipCompat.setTooltipNull(true);
                break;
        }
        setTooltipOffset();
        return super.dispatchGenericMotionEvent(event);
    }

    public void onHoverChanged(boolean hovered) {
        TooltipCompat.setTooltipNull(!hovered);
        setTooltipOffset();
        super.onHoverChanged(hovered);
    }

    public boolean performLongClick() {
        if (this.mIcon == null) {
            TooltipCompat.setTooltipNull(true);
            return true;
        }
        setTooltipOffset();
        return super.performLongClick();
    }

    public boolean onLongClick(View v) {
        return false;
    }

    protected void setTooltipOffset() {
        if (hasText() || getContentDescription() == null) {
            TooltipCompat.setTooltipNull(true);
            return;
        }
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
        View menuView = (View) getParent();
        View toolbar = menuView != null ? (View) menuView.getParent() : null;
        if ((toolbar instanceof Toolbar) && toolbar.getWidth() < displayFrame.right - displayFrame.left) {
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

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean textVisible = hasText();
        if (textVisible && this.mSavedPaddingLeft >= 0) {
            super.setPadding(this.mSavedPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        if (this.mSBBHelper != null) {
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            if (hasText()) {
                this.mSBBHelper.setBackgroundOn(getContext().getDrawable(C0247R.drawable.sesl_action_text_button_show_button_background));
            } else if (this.mIsLastItem) {
                this.mSBBHelper.setBackgroundOn(getContext().getDrawable(C0247R.drawable.sesl_more_button_show_button_background));
            } else {
                this.mSBBHelper.setBackgroundOn(getContext().getDrawable(C0247R.drawable.sesl_action_icon_button_show_button_background));
            }
            this.mSBBHelper.updateButtonBackground();
            setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int oldMeasuredWidth = getMeasuredWidth();
        int targetWidth = widthMode == Integer.MIN_VALUE ? Math.min(widthSize, this.mMinWidth) : this.mMinWidth;
        if (widthMode != 1073741824 && this.mMinWidth > 0 && oldMeasuredWidth < targetWidth) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(targetWidth, 1073741824), heightMeasureSpec);
        }
        if (!textVisible && this.mIcon != null) {
            int w = getMeasuredWidth();
            int dw = this.mIcon.getBounds().width();
            if (!this.mIsChangedRelativePadding) {
                super.setPadding((w - dw) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
        }
    }

    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(null);
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (this.mIsChangedRelativePadding) {
            Drawable bg = getBackground();
            if (this.mIcon != null && bg != null) {
                int width = getWidth();
                int halfOffsetX = (getPaddingLeft() - getPaddingRight()) / 2;
                bg.setHotspotBounds(halfOffsetX, 0, halfOffsetX + width, getHeight());
            } else if (bg != null) {
                bg.setHotspotBounds(0, 0, getWidth(), getHeight());
            }
        }
        return changed;
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
