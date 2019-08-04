package android.support.design.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.design.C0011R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView.ItemView;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class BottomNavigationItemView extends FrameLayout implements ItemView {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private ContentResolver mContentResolver;
    private int mDefaultMargin;
    private boolean mHasIcon;
    private ImageView mIcon;
    private ColorStateList mIconTint;
    private MenuItemImpl mItemData;
    private int mItemPosition;
    private TextView mLargeLabel;
    private int mLargeLabelAppearance;
    private float mScaleDownFactor;
    private float mScaleUpFactor;
    private int mShiftAmount;
    private boolean mShiftingMode;
    private TextView mSmallLabel;
    private int mSmallLabelAppearance;

    public BottomNavigationItemView(Context context) {
        this(context, null, true);
    }

    public BottomNavigationItemView(Context context, boolean hasIcon) {
        this(context, null, hasIcon);
    }

    public BottomNavigationItemView(Context context, AttributeSet attrs, boolean hasIcon) {
        this(context, attrs, 0, hasIcon);
    }

    public BottomNavigationItemView(Context context, AttributeSet attrs, int defStyleAttr, boolean hasIcon) {
        TypedArray a;
        super(context, attrs, defStyleAttr);
        this.mItemPosition = -1;
        this.mHasIcon = true;
        Resources res = getResources();
        this.mHasIcon = hasIcon;
        this.mDefaultMargin = res.getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_margin);
        if (this.mShiftingMode) {
            int inactiveLabelSize = res.getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_text_size);
            int activeLabelSize = res.getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_active_text_size);
            this.mShiftAmount = inactiveLabelSize - activeLabelSize;
            this.mScaleUpFactor = (((float) activeLabelSize) * 1.0f) / ((float) inactiveLabelSize);
            this.mScaleDownFactor = (((float) inactiveLabelSize) * 1.0f) / ((float) activeLabelSize);
        }
        if (this.mHasIcon) {
            LayoutInflater.from(context).inflate(C0011R.layout.sesl_bottom_navigation_item, this, true);
            a = context.obtainStyledAttributes(attrs, C0011R.styleable.BottomNavigationView, defStyleAttr, C0011R.style.Widget_Design_BottomNavigationView);
        } else {
            LayoutInflater.from(context).inflate(C0011R.layout.sesl_bottom_navigation_item_text, this, true);
            a = context.obtainStyledAttributes(attrs, C0011R.styleable.BottomNavigationView, defStyleAttr, C0011R.style.Widget_Design_BottomNavigationView_Text);
        }
        this.mLargeLabelAppearance = a.getResourceId(C0011R.styleable.BottomNavigationView_bottomTextAppearance, 0);
        this.mSmallLabelAppearance = a.getResourceId(C0011R.styleable.BottomNavigationView_bottomTextAppearance, 0);
        a.recycle();
        this.mIcon = (ImageView) findViewById(C0011R.id.icon);
        this.mLargeLabel = (TextView) findViewById(C0011R.id.largeLabel);
        this.mSmallLabel = (TextView) findViewById(C0011R.id.smallLabel);
        this.mLargeLabel.setTextAppearance(context, this.mLargeLabelAppearance);
        this.mSmallLabel.setTextAppearance(context, this.mSmallLabelAppearance);
        setLargeTextSize(this.mLargeLabelAppearance, this.mLargeLabel);
        setLargeTextSize(this.mSmallLabelAppearance, this.mSmallLabel);
        this.mContentResolver = context.getContentResolver();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLargeTextSize(this.mLargeLabelAppearance, this.mLargeLabel);
        setLargeTextSize(this.mSmallLabelAppearance, this.mSmallLabel);
        if (this.mHasIcon && this.mIcon != null) {
            BaselineLayout baseLineLayout = (BaselineLayout) findViewById(C0011R.id.baseLineLayout);
            this.mDefaultMargin = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_icon_inset);
            int iconSize = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_icon_size);
            int baseLineMarginTop = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_baseline_margin_top);
            if (baseLineLayout != null) {
                LayoutParams baseParams = (LayoutParams) baseLineLayout.getLayoutParams();
                baseParams.topMargin = baseLineMarginTop;
                baseLineLayout.setLayoutParams(baseParams);
            }
            LayoutParams iconParams = (LayoutParams) this.mIcon.getLayoutParams();
            iconParams.width = iconSize;
            iconParams.height = iconSize;
            iconParams.gravity = 49;
            iconParams.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(iconParams);
            refreshDrawableState();
        }
    }

    private void setLargeTextSize(int appearance, TextView tv) {
        TypedArray a = getContext().obtainStyledAttributes(appearance, C0011R.styleable.TextAppearance);
        TypedValue value = a.peekValue(C0011R.styleable.TextAppearance_android_textSize);
        a.recycle();
        float textSize = TypedValue.complexToFloat(value.data);
        float fontScale = getContext().getResources().getConfiguration().fontScale;
        if (fontScale > 1.3f) {
            fontScale = 1.3f;
        }
        if (tv != null) {
            tv.setTextSize(1, textSize * fontScale);
        }
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        setCheckable(itemData.isCheckable());
        setChecked(itemData.isChecked());
        setEnabled(itemData.isEnabled());
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitle());
        setId(itemData.getItemId());
        setContentDescription(itemData.getContentDescription());
        TooltipCompat.setTooltipText(this, itemData.getTooltipText());
    }

    public void setItemPosition(int position) {
        this.mItemPosition = position;
    }

    public int getItemPosition() {
        return this.mItemPosition;
    }

    public void setShiftingMode(boolean enabled) {
        this.mShiftingMode = enabled;
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void setTitle(CharSequence title) {
        this.mSmallLabel.setText(title);
        this.mLargeLabel.setText(title);
    }

    public void setCheckable(boolean checkable) {
        refreshDrawableState();
    }

    public void setChecked(boolean checked) {
        this.mLargeLabel.setPivotX((float) (this.mLargeLabel.getWidth() / 2));
        this.mLargeLabel.setPivotY((float) this.mLargeLabel.getBaseline());
        this.mSmallLabel.setPivotX((float) (this.mSmallLabel.getWidth() / 2));
        this.mSmallLabel.setPivotY((float) this.mSmallLabel.getBaseline());
        if (this.mHasIcon) {
            this.mDefaultMargin = getResources().getDimensionPixelSize(C0011R.dimen.sesl_bottom_navigation_icon_inset);
        }
        LayoutParams iconParams;
        if (this.mShiftingMode) {
            if (checked) {
                iconParams = (LayoutParams) this.mIcon.getLayoutParams();
                iconParams.gravity = 49;
                iconParams.topMargin = this.mDefaultMargin;
                this.mIcon.setLayoutParams(iconParams);
                this.mLargeLabel.setVisibility(0);
                this.mLargeLabel.setScaleX(1.0f);
                this.mLargeLabel.setScaleY(1.0f);
            } else {
                iconParams = (LayoutParams) this.mIcon.getLayoutParams();
                iconParams.gravity = 17;
                iconParams.topMargin = this.mDefaultMargin;
                this.mIcon.setLayoutParams(iconParams);
                this.mLargeLabel.setVisibility(4);
                this.mLargeLabel.setScaleX(0.5f);
                this.mLargeLabel.setScaleY(0.5f);
            }
            this.mSmallLabel.setVisibility(4);
        } else if (checked) {
            iconParams = (LayoutParams) this.mIcon.getLayoutParams();
            iconParams.gravity = 49;
            iconParams.topMargin = this.mDefaultMargin + this.mShiftAmount;
            this.mIcon.setLayoutParams(iconParams);
            this.mLargeLabel.setVisibility(0);
            this.mSmallLabel.setVisibility(4);
            this.mLargeLabel.setScaleX(1.0f);
            this.mLargeLabel.setScaleY(1.0f);
            this.mSmallLabel.setScaleX(this.mScaleUpFactor);
            this.mSmallLabel.setScaleY(this.mScaleUpFactor);
        } else {
            iconParams = (LayoutParams) this.mIcon.getLayoutParams();
            iconParams.gravity = 49;
            iconParams.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(iconParams);
            this.mLargeLabel.setVisibility(4);
            this.mSmallLabel.setVisibility(0);
            this.mLargeLabel.setScaleX(this.mScaleDownFactor);
            this.mLargeLabel.setScaleY(this.mScaleDownFactor);
            this.mSmallLabel.setScaleX(1.0f);
            this.mSmallLabel.setScaleY(1.0f);
        }
        refreshDrawableState();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mSmallLabel.setEnabled(enabled);
        this.mLargeLabel.setEnabled(enabled);
        this.mIcon.setEnabled(enabled);
        if (enabled) {
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        } else {
            ViewCompat.setPointerIcon(this, null);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Button.class.getName());
    }

    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (this.mItemData != null && this.mItemData.isCheckable() && this.mItemData.isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void setIcon(Drawable icon) {
        if (icon != null) {
            ConstantState state = icon.getConstantState();
            if (state != null) {
                icon = state.newDrawable();
            }
            icon = DrawableCompat.wrap(icon).mutate();
            DrawableCompat.setTintList(icon, this.mIconTint);
        }
        this.mIcon.setImageDrawable(icon);
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setIconTintList(ColorStateList tint) {
        this.mIconTint = tint;
        if (this.mItemData != null) {
            setIcon(this.mItemData.getIcon());
        }
    }

    public void setTextColor(ColorStateList color) {
        this.mSmallLabel.setTextColor(color);
        this.mLargeLabel.setTextColor(color);
    }

    void setShowButtonShape(int textColor, ColorStateList background) {
        if (VERSION.SDK_INT > 27) {
            Drawable shapeBackground = getResources().getDrawable(C0011R.drawable.sesl_bottomnavigation_show_button_background);
            this.mSmallLabel.setTextColor(textColor);
            this.mLargeLabel.setTextColor(textColor);
            this.mSmallLabel.setBackground(shapeBackground);
            this.mLargeLabel.setBackground(shapeBackground);
            this.mSmallLabel.setBackgroundTintList(background);
            this.mLargeLabel.setBackgroundTintList(background);
            return;
        }
        setBackground(getResources().getDrawable(C0011R.drawable.sesl_bottom_navigation_show_background_o));
    }

    public void setItemBackground(int background) {
        Drawable backgroundDrawable;
        if (background == 0) {
            backgroundDrawable = null;
        } else {
            backgroundDrawable = ContextCompat.getDrawable(getContext(), background);
        }
        ViewCompat.setBackground(this, backgroundDrawable);
    }
}
