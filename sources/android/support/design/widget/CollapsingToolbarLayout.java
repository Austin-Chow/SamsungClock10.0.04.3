package android.support.design.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.C0011R;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.math.MathUtils;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollapsingToolbarLayout extends FrameLayout {
    static final Interpolator SINE_OUT_80_INTERPOLATOR = new PathInterpolator(0.17f, 0.17f, 0.2f, 1.0f);
    final CollapsingTextHelper mCollapsingTextHelper;
    private boolean mCollapsingTitleEnabled;
    private LinearLayout mCollapsingTitleLayout;
    private LinearLayout mCollapsingTitleLayoutParent;
    private TextView mCollapsingToolbarExtendedSubTitle;
    private TextView mCollapsingToolbarExtendedTitle;
    private boolean mCollapsingToolbarLayoutSubTitleEnabled;
    private boolean mCollapsingToolbarLayoutTitleEnabled;
    private Drawable mContentScrim;
    int mCurrentOffset;
    private float mDefaultHeightDp;
    private boolean mDrawCollapsingTitle;
    private View mDummyView;
    private int mExpandedMarginBottom;
    private int mExpandedMarginEnd;
    private int mExpandedMarginStart;
    private int mExpandedMarginTop;
    private int mExtendSubTitleAppearance;
    private int mExtendTitleAppearance;
    private float mHeightPercent;
    private boolean mIsCollapsingToolbarTitleCustom;
    private boolean mIsCustomAccessibility;
    WindowInsetsCompat mLastInsets;
    private OnOffsetChangedListener mOnOffsetChangedListener;
    private boolean mRefreshToolbar;
    private int mScrimAlpha;
    private long mScrimAnimationDuration;
    private ValueAnimator mScrimAnimator;
    private int mScrimVisibleHeightTrigger;
    private boolean mScrimsAreShown;
    private int mStatsusBarHeight;
    Drawable mStatusBarScrim;
    private final Rect mTmpRect;
    private Toolbar mToolbar;
    private View mToolbarDirectChild;
    private int mToolbarId;
    private ViewStubCompat mViewStubCompat;

    /* renamed from: android.support.design.widget.CollapsingToolbarLayout$1 */
    class C00341 implements OnApplyWindowInsetsListener {
        C00341() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return CollapsingToolbarLayout.this.onWindowInsetChanged(insets);
        }
    }

    /* renamed from: android.support.design.widget.CollapsingToolbarLayout$3 */
    class C00363 implements AnimatorUpdateListener {
        C00363() {
        }

        public void onAnimationUpdate(ValueAnimator animator) {
            CollapsingToolbarLayout.this.setScrimAlpha(((Integer) animator.getAnimatedValue()).intValue());
        }
    }

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        private boolean isTitleCustom;
        int mCollapseMode = 0;
        float mParallaxMult = 0.5f;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, C0011R.styleable.CollapsingToolbarLayout_Layout);
            this.mCollapseMode = a.getInt(C0011R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
            setParallaxMultiplier(a.getFloat(C0011R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5f));
            this.isTitleCustom = a.getBoolean(C0011R.styleable.CollapsingToolbarLayout_Layout_layout_isTitleCustom, false);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        public boolean getTitleIsCustom() {
            return this.isTitleCustom;
        }

        public void setParallaxMultiplier(float multiplier) {
            this.mParallaxMult = multiplier;
        }
    }

    private class OffsetUpdateListener implements OnOffsetChangedListener {
        OffsetUpdateListener() {
            if (CollapsingToolbarLayout.this.getParent() instanceof AppBarLayout) {
                AppBarLayout abl = (AppBarLayout) CollapsingToolbarLayout.this.getParent();
                CollapsingToolbarLayout.this.mDefaultHeightDp = abl.getCollapsedHeight();
                if (CollapsingToolbarLayout.this.mDefaultHeightDp == 0.0f && !abl.mIsSetCollapsedHeight) {
                    if (abl.getPaddingBottom() > 0) {
                        CollapsingToolbarLayout.this.mDefaultHeightDp = (float) CollapsingToolbarLayout.this.getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
                        return;
                    } else {
                        CollapsingToolbarLayout.this.mDefaultHeightDp = (float) CollapsingToolbarLayout.this.getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height);
                        return;
                    }
                }
                return;
            }
            CollapsingToolbarLayout.this.mDefaultHeightDp = (float) CollapsingToolbarLayout.this.getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
        }

        @SuppressLint({"Range"})
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            layout.getWindowVisibleDisplayFrame(new Rect());
            int layoutPosition = Math.abs(layout.getTop());
            float alphaRange = ((float) CollapsingToolbarLayout.this.getHeight()) * 0.17999999f;
            float toolbartitleAlphaStart = ((float) CollapsingToolbarLayout.this.getHeight()) * 0.35f;
            CollapsingToolbarLayout.this.mCurrentOffset = verticalOffset;
            CollapsingToolbarLayout.this.mCollapsingTitleLayout.setTranslationY((float) ((-CollapsingToolbarLayout.this.mCurrentOffset) / 3));
            int insetTop = CollapsingToolbarLayout.this.mLastInsets != null ? CollapsingToolbarLayout.this.mLastInsets.getSystemWindowInsetTop() : 0;
            int expandRange = (CollapsingToolbarLayout.this.getHeight() - CollapsingToolbarLayout.this.getMinimumHeight()) - insetTop;
            int z = CollapsingToolbarLayout.this.getChildCount();
            for (int i = 0; i < z; i++) {
                View child = CollapsingToolbarLayout.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                ViewOffsetHelper offsetHelper = CollapsingToolbarLayout.getViewOffsetHelper(child);
                if (!(CollapsingToolbarLayout.this.mToolbar == null || !(child instanceof ActionBarContextView) || CollapsingToolbarLayout.this.mIsCustomAccessibility)) {
                    if (((ActionBarContextView) child).getIsActionModeAccessibilityOn()) {
                        CollapsingToolbarLayout.this.mToolbar.setImportantForAccessibility(4);
                    } else {
                        CollapsingToolbarLayout.this.mToolbar.setImportantForAccessibility(1);
                    }
                }
                switch (lp.mCollapseMode) {
                    case 1:
                        offsetHelper.setTopAndBottomOffset(MathUtils.clamp(-verticalOffset, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(child)));
                        break;
                    case 2:
                        offsetHelper.setTopAndBottomOffset(Math.round(((float) (-verticalOffset)) * lp.mParallaxMult));
                        break;
                    default:
                        break;
                }
            }
            CollapsingToolbarLayout.this.updateScrimVisibility();
            if (CollapsingToolbarLayout.this.mStatusBarScrim != null && insetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
            }
            if (CollapsingToolbarLayout.this.mCollapsingToolbarLayoutTitleEnabled) {
                float titleAlpha = 255.0f - ((100.0f / alphaRange) * (((float) layoutPosition) - 0.0f));
                if (titleAlpha < 0.0f) {
                    titleAlpha = 0.0f;
                } else if (titleAlpha > 255.0f) {
                    titleAlpha = 255.0f;
                }
                titleAlpha /= 255.0f;
                CollapsingToolbarLayout.this.mCollapsingTitleLayout.setAlpha(titleAlpha);
                if (CollapsingToolbarLayout.this.mToolbar != null) {
                    if (titleAlpha == 1.0f) {
                        CollapsingToolbarLayout.this.mToolbar.setTitleAccessibilityEnabled(false);
                    } else if (titleAlpha == 0.0f) {
                        CollapsingToolbarLayout.this.mToolbar.setTitleAccessibilityEnabled(true);
                    }
                }
                if (layout.getHeight() <= ((int) CollapsingToolbarLayout.this.mDefaultHeightDp)) {
                    CollapsingToolbarLayout.this.mCollapsingTitleLayout.setAlpha(0.0f);
                    if (CollapsingToolbarLayout.this.mToolbar != null) {
                        CollapsingToolbarLayout.this.mToolbar.setTitleAccessibilityEnabled(true);
                        CollapsingToolbarLayout.this.mToolbar.setTitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getTitleTextColor(), 255));
                        if (!TextUtils.isEmpty(CollapsingToolbarLayout.this.mToolbar.getSubtitle())) {
                            CollapsingToolbarLayout.this.mToolbar.setSubtitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getSubtitleTextColor(), 255));
                        }
                    }
                } else if (CollapsingToolbarLayout.this.mToolbar != null) {
                    double toolbartitleAlpha = (double) ((150.0f / alphaRange) * (((float) layoutPosition) - toolbartitleAlphaStart));
                    if (toolbartitleAlpha >= 0.0d && toolbartitleAlpha <= 255.0d) {
                        CollapsingToolbarLayout.this.mToolbar.setTitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getTitleTextColor(), (int) toolbartitleAlpha));
                        if (!TextUtils.isEmpty(CollapsingToolbarLayout.this.mToolbar.getSubtitle())) {
                            CollapsingToolbarLayout.this.mToolbar.setSubtitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getSubtitleTextColor(), (int) toolbartitleAlpha));
                        }
                    } else if (toolbartitleAlpha < 0.0d) {
                        CollapsingToolbarLayout.this.mToolbar.setTitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getTitleTextColor(), (int) 0.0d));
                        if (!TextUtils.isEmpty(CollapsingToolbarLayout.this.mToolbar.getSubtitle())) {
                            CollapsingToolbarLayout.this.mToolbar.setSubtitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getSubtitleTextColor(), (int) 0.0d));
                        }
                    } else {
                        CollapsingToolbarLayout.this.mToolbar.setTitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getTitleTextColor(), 255));
                        if (!TextUtils.isEmpty(CollapsingToolbarLayout.this.mToolbar.getSubtitle())) {
                            CollapsingToolbarLayout.this.mToolbar.setSubtitleTextColor(ColorUtils.setAlphaComponent(CollapsingToolbarLayout.this.mToolbar.getSubtitleTextColor(), 255));
                        }
                    }
                }
            } else if (CollapsingToolbarLayout.this.mCollapsingTitleEnabled) {
                CollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction(((float) Math.abs(verticalOffset)) / ((float) expandRange));
            }
        }
    }

    public CollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mRefreshToolbar = true;
        this.mTmpRect = new Rect();
        this.mScrimVisibleHeightTrigger = -1;
        this.mIsCustomAccessibility = false;
        this.mStatsusBarHeight = 0;
        this.mHeightPercent = 0.0f;
        ThemeUtils.checkAppCompatTheme(context);
        TypedArray a = context.obtainStyledAttributes(attrs, C0011R.styleable.CollapsingToolbarLayout, defStyleAttr, C0011R.style.Widget_Design_CollapsingToolbar);
        this.mCollapsingTitleLayout = new LinearLayout(context, attrs, defStyleAttr);
        this.mCollapsingTitleLayout.setId(C0011R.id.collpasing_app_bar_title_layout);
        this.mCollapsingTitleLayout.setBackgroundColor(0);
        this.mCollapsingTitleLayoutParent = new LinearLayout(context, attrs, defStyleAttr);
        this.mCollapsingTitleLayoutParent.setId(C0011R.id.collpasing_app_bar_title_layout_parent);
        this.mCollapsingTitleLayoutParent.setBackgroundColor(0);
        this.mCollapsingTitleEnabled = a.getBoolean(C0011R.styleable.CollapsingToolbarLayout_titleEnabled, false);
        this.mCollapsingToolbarLayoutTitleEnabled = a.getBoolean(C0011R.styleable.CollapsingToolbarLayout_extendTitleEnabled, true);
        if (this.mCollapsingTitleEnabled == this.mCollapsingToolbarLayoutTitleEnabled && this.mCollapsingTitleEnabled) {
            boolean z;
            if (this.mCollapsingToolbarLayoutTitleEnabled) {
                z = false;
            } else {
                z = true;
            }
            this.mCollapsingTitleEnabled = z;
        }
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper = new CollapsingTextHelper(this);
            this.mCollapsingTextHelper.setTextSizeInterpolator(SINE_OUT_80_INTERPOLATOR);
            this.mCollapsingTextHelper.setExpandedTextGravity(a.getInt(C0011R.styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
            this.mCollapsingTextHelper.setCollapsedTextGravity(a.getInt(C0011R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
        } else {
            this.mCollapsingTextHelper = null;
        }
        this.mExtendTitleAppearance = a.getResourceId(C0011R.styleable.CollapsingToolbarLayout_extendTitleTextAppearance, 0);
        this.mExtendSubTitleAppearance = a.getResourceId(C0011R.styleable.CollapsingToolbarLayout_extendSubTitleTextAppearance, 0);
        if (a.hasValue(C0011R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
            this.mExtendTitleAppearance = a.getResourceId(C0011R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0);
        }
        CharSequence subtitle = a.getText(C0011R.styleable.CollapsingToolbarLayout_subtitle);
        if (!this.mCollapsingToolbarLayoutTitleEnabled || TextUtils.isEmpty(subtitle)) {
            this.mCollapsingToolbarLayoutSubTitleEnabled = false;
        } else {
            this.mCollapsingToolbarLayoutSubTitleEnabled = true;
        }
        if (this.mCollapsingTitleLayoutParent != null) {
            addView(this.mCollapsingTitleLayoutParent, new android.widget.FrameLayout.LayoutParams(-1, -1, 17));
        }
        if (this.mCollapsingTitleLayout != null) {
            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(-1, -2, 16.0f);
            params.gravity = 16;
            this.mCollapsingTitleLayout.setOrientation(1);
            this.mStatsusBarHeight = getStatusbarHeight();
            if (this.mStatsusBarHeight > 0) {
                this.mCollapsingTitleLayout.setPadding(0, 0, 0, this.mStatsusBarHeight / 2);
            }
            this.mCollapsingTitleLayoutParent.addView(this.mCollapsingTitleLayout, params);
        }
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            this.mCollapsingToolbarExtendedTitle = new TextView(context);
            this.mCollapsingToolbarExtendedTitle.setId(C0011R.id.collpasing_app_bar_extended_title);
            this.mCollapsingTitleLayout.addView(this.mCollapsingToolbarExtendedTitle);
            this.mCollapsingToolbarExtendedTitle.setEllipsize(TruncateAt.END);
            this.mCollapsingToolbarExtendedTitle.setGravity(17);
            this.mCollapsingToolbarExtendedTitle.setTextAppearance(getContext(), this.mExtendTitleAppearance);
            int extendedTitlePadding = (int) getResources().getDimension(C0011R.dimen.sesl_extended_appbar_title_padding);
            this.mCollapsingToolbarExtendedTitle.setPadding(extendedTitlePadding, 0, extendedTitlePadding, 0);
        }
        if (this.mCollapsingToolbarLayoutSubTitleEnabled) {
            setSubtitle(subtitle);
        }
        updateDefaultHeightDP();
        updateTitleLayout();
        int dimensionPixelSize = a.getDimensionPixelSize(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
        this.mExpandedMarginBottom = dimensionPixelSize;
        this.mExpandedMarginEnd = dimensionPixelSize;
        this.mExpandedMarginTop = dimensionPixelSize;
        this.mExpandedMarginStart = dimensionPixelSize;
        if (a.hasValue(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
            this.mExpandedMarginStart = a.getDimensionPixelSize(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
        }
        if (a.hasValue(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
            this.mExpandedMarginEnd = a.getDimensionPixelSize(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
        }
        if (a.hasValue(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
            this.mExpandedMarginTop = a.getDimensionPixelSize(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
        }
        if (a.hasValue(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
            this.mExpandedMarginBottom = a.getDimensionPixelSize(C0011R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
        }
        setTitle(a.getText(C0011R.styleable.CollapsingToolbarLayout_title));
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setExpandedTextAppearance(C0011R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
            this.mCollapsingTextHelper.setCollapsedTextAppearance(C0247R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
            if (a.hasValue(C0011R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
                this.mCollapsingTextHelper.setExpandedTextAppearance(a.getResourceId(C0011R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0));
            }
        }
        this.mScrimVisibleHeightTrigger = a.getDimensionPixelSize(C0011R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
        this.mScrimAnimationDuration = (long) a.getInt(C0011R.styleable.CollapsingToolbarLayout_scrimAnimationDuration, 600);
        setContentScrim(a.getDrawable(C0011R.styleable.CollapsingToolbarLayout_contentScrim));
        setStatusBarScrim(a.getDrawable(C0011R.styleable.CollapsingToolbarLayout_statusBarScrim));
        this.mToolbarId = a.getResourceId(C0011R.styleable.CollapsingToolbarLayout_toolbarId, -1);
        a.recycle();
        a = getContext().obtainStyledAttributes(C0247R.styleable.AppCompatTheme);
        if (!Boolean.valueOf(a.getBoolean(C0247R.styleable.AppCompatTheme_windowActionModeOverlay, false)).booleanValue()) {
            LayoutInflater.from(context).inflate(C0011R.layout.sesl_action_mode_view_stub, this, true);
            this.mViewStubCompat = (ViewStubCompat) findViewById(C0011R.id.action_mode_bar_stub);
        }
        a.recycle();
        setWillNotDraw(false);
        ViewCompat.setOnApplyWindowInsetsListener(this, new C00341());
    }

    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams != null) {
                this.mIsCollapsingToolbarTitleCustom = layoutParams.getTitleIsCustom();
                if (this.mIsCollapsingToolbarTitleCustom) {
                    if (this.mCollapsingToolbarExtendedTitle != null && this.mCollapsingToolbarExtendedTitle.getParent() == this.mCollapsingTitleLayout) {
                        this.mCollapsingTitleLayout.removeView(this.mCollapsingToolbarExtendedTitle);
                    }
                    if (this.mCollapsingToolbarExtendedSubTitle != null && this.mCollapsingToolbarExtendedSubTitle.getParent() == this.mCollapsingTitleLayout) {
                        this.mCollapsingTitleLayout.removeView(this.mCollapsingToolbarExtendedSubTitle);
                    }
                    if (child.getParent() != null) {
                        ((ViewGroup) child.getParent()).removeView(child);
                    }
                    this.mCollapsingTitleLayout.addView(child, params);
                }
            }
        }
    }

    private void updateTitleLayout() {
        TypedValue typedValue = new TypedValue();
        getResources().getValue(C0011R.dimen.sesl_abl_height_proportion, typedValue, true);
        this.mHeightPercent = typedValue.getFloat();
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            TypedArray appearance = getContext().obtainStyledAttributes(this.mExtendTitleAppearance, C0011R.styleable.TextAppearance);
            float textSize = TypedValue.complexToFloat(appearance.peekValue(C0011R.styleable.TextAppearance_android_textSize).data);
            float fontScale = getContext().getResources().getConfiguration().fontScale;
            if (fontScale > 1.1f) {
                fontScale = 1.1f;
            }
            Log.d("Sesl_CTL", "updateTitleLayout: context:" + getContext() + ", orientation:" + getContext().getResources().getConfiguration().orientation + " density:" + getContext().getResources().getConfiguration().densityDpi + " ,testSize : " + textSize + "fontScale : " + fontScale + ", mCollapsingToolbarLayoutSubTitleEnabled :" + this.mCollapsingToolbarLayoutSubTitleEnabled);
            if (this.mCollapsingToolbarLayoutSubTitleEnabled) {
                this.mCollapsingToolbarExtendedTitle.setTextSize(0, (float) getContext().getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_text_size_title_extend_with_subtitle));
                this.mCollapsingToolbarExtendedSubTitle.setTextSize(0, (float) getContext().getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_text_size_subtitle));
            } else {
                this.mCollapsingToolbarExtendedTitle.setTextSize(1, textSize * fontScale);
            }
            if (this.mHeightPercent != 0.3f) {
                this.mCollapsingToolbarExtendedTitle.setSingleLine(false);
                this.mCollapsingToolbarExtendedTitle.setMaxLines(2);
            } else if (this.mCollapsingToolbarLayoutSubTitleEnabled) {
                this.mCollapsingToolbarExtendedTitle.setSingleLine(true);
                this.mCollapsingToolbarExtendedTitle.setMaxLines(1);
            } else {
                this.mCollapsingToolbarExtendedTitle.setSingleLine(false);
                this.mCollapsingToolbarExtendedTitle.setMaxLines(2);
            }
            appearance.recycle();
        }
    }

    private void updateDefaultHeightDP() {
        if (getParent() instanceof AppBarLayout) {
            AppBarLayout abl = (AppBarLayout) getParent();
            this.mDefaultHeightDp = abl.getCollapsedHeight();
            if (!abl.mIsSetCollapsedHeight) {
                if (abl.getPaddingBottom() > 0) {
                    this.mDefaultHeightDp = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
                    return;
                } else {
                    this.mDefaultHeightDp = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height);
                    return;
                }
            }
            return;
        }
        this.mDefaultHeightDp = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
            ViewCompat.requestApplyInsets(this);
        }
    }

    protected void onDetachedFromWindow() {
        ViewParent parent = getParent();
        if (this.mOnOffsetChangedListener != null && (parent instanceof AppBarLayout)) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }

    WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat insets) {
        WindowInsetsCompat newInsets = null;
        if (ViewCompat.getFitsSystemWindows(this)) {
            newInsets = insets;
        }
        if (!ObjectsCompat.equals(this.mLastInsets, newInsets)) {
            this.mLastInsets = newInsets;
            requestLayout();
        }
        return insets.consumeSystemWindowInsets();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        ensureToolbar();
        if (this.mToolbar == null && this.mContentScrim != null && this.mScrimAlpha > 0) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
        }
        if (this.mCollapsingTitleEnabled && this.mDrawCollapsingTitle) {
            this.mCollapsingTextHelper.draw(canvas);
        }
        if (this.mStatusBarScrim != null && this.mScrimAlpha > 0) {
            int topInset = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
            if (topInset > 0) {
                this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, getWidth(), topInset - this.mCurrentOffset);
                this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
                this.mStatusBarScrim.draw(canvas);
            }
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean invalidated = false;
        if (this.mContentScrim != null && this.mScrimAlpha > 0 && isToolbarChild(child)) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
            invalidated = true;
        }
        return super.drawChild(canvas, child, drawingTime) || invalidated;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mContentScrim != null) {
            this.mContentScrim.setBounds(0, 0, w, h);
        }
    }

    private void ensureToolbar() {
        if (this.mRefreshToolbar) {
            this.mToolbar = null;
            this.mToolbarDirectChild = null;
            if (this.mToolbarId != -1) {
                this.mToolbar = (Toolbar) findViewById(this.mToolbarId);
                if (this.mToolbar != null) {
                    this.mToolbarDirectChild = findDirectChild(this.mToolbar);
                }
            }
            if (this.mToolbar == null) {
                Toolbar toolbar = null;
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (child instanceof Toolbar) {
                        toolbar = (Toolbar) child;
                        break;
                    }
                }
                this.mToolbar = toolbar;
                if (this.mViewStubCompat != null) {
                    this.mViewStubCompat.bringToFront();
                    this.mViewStubCompat.invalidate();
                }
            }
            updateDummyView();
            this.mRefreshToolbar = false;
        }
    }

    private boolean isToolbarChild(View child) {
        return (this.mToolbarDirectChild == null || this.mToolbarDirectChild == this) ? child == this.mToolbar : child == this.mToolbarDirectChild;
    }

    private View findDirectChild(View descendant) {
        View directChild = descendant;
        View p = descendant.getParent();
        while (p != this && p != null) {
            if (p instanceof View) {
                directChild = p;
            }
            p = p.getParent();
        }
        return directChild;
    }

    private void updateDummyView() {
        if (!(this.mCollapsingTitleEnabled || this.mDummyView == null)) {
            ViewParent parent = this.mDummyView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mDummyView);
            }
        }
        if (this.mCollapsingTitleEnabled && this.mToolbar != null) {
            if (this.mDummyView == null) {
                this.mDummyView = new View(getContext());
            }
            if (this.mDummyView.getParent() == null) {
                this.mToolbar.addView(this.mDummyView, -1, -1);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureToolbar();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int topInset = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
        if (mode == 0 && topInset > 0) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight() + topInset, 1073741824));
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int z;
        int i;
        super.onLayout(changed, left, top, right, bottom);
        if (this.mLastInsets != null) {
            int insetTop = this.mLastInsets.getSystemWindowInsetTop();
            z = getChildCount();
            for (i = 0; i < z; i++) {
                View child = getChildAt(i);
                if (!ViewCompat.getFitsSystemWindows(child) && child.getTop() < insetTop) {
                    ViewCompat.offsetTopAndBottom(child, insetTop);
                }
            }
        }
        if (this.mCollapsingTitleEnabled && this.mDummyView != null) {
            boolean z2 = ViewCompat.isAttachedToWindow(this.mDummyView) && this.mDummyView.getVisibility() == 0;
            this.mDrawCollapsingTitle = z2;
            if (this.mDrawCollapsingTitle) {
                int titleMarginEnd;
                int i2;
                boolean isRtl = ViewCompat.getLayoutDirection(this) == 1;
                int maxOffset = getMaxOffsetForPinChild(this.mToolbarDirectChild != null ? this.mToolbarDirectChild : this.mToolbar);
                ViewGroupUtils.getDescendantRect(this, this.mDummyView, this.mTmpRect);
                CollapsingTextHelper collapsingTextHelper = this.mCollapsingTextHelper;
                int i3 = this.mTmpRect.left;
                if (isRtl) {
                    titleMarginEnd = this.mToolbar.getTitleMarginEnd();
                } else {
                    titleMarginEnd = this.mToolbar.getTitleMarginStart();
                }
                i3 += titleMarginEnd;
                int titleMarginTop = this.mToolbar.getTitleMarginTop() + (this.mTmpRect.top + maxOffset);
                int i4 = this.mTmpRect.right;
                if (isRtl) {
                    titleMarginEnd = this.mToolbar.getTitleMarginStart();
                } else {
                    titleMarginEnd = this.mToolbar.getTitleMarginEnd();
                }
                collapsingTextHelper.setCollapsedBounds(i3, titleMarginTop, titleMarginEnd + i4, (this.mTmpRect.bottom + maxOffset) - this.mToolbar.getTitleMarginBottom());
                CollapsingTextHelper collapsingTextHelper2 = this.mCollapsingTextHelper;
                titleMarginEnd = isRtl ? this.mExpandedMarginEnd : this.mExpandedMarginStart;
                titleMarginTop = this.mExpandedMarginTop + this.mTmpRect.top;
                i4 = right - left;
                if (isRtl) {
                    i2 = this.mExpandedMarginStart;
                } else {
                    i2 = this.mExpandedMarginEnd;
                }
                collapsingTextHelper2.setExpandedBounds(titleMarginEnd, titleMarginTop, i4 - i2, (bottom - top) - this.mExpandedMarginBottom);
                this.mCollapsingTextHelper.recalculate();
            }
        }
        z = getChildCount();
        for (i = 0; i < z; i++) {
            getViewOffsetHelper(getChildAt(i)).onViewLayout();
        }
        if (this.mToolbar != null) {
            int toolbar_height;
            if (this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText())) {
                this.mCollapsingTextHelper.setText(this.mToolbar.getTitle());
            }
            if (this.mToolbarDirectChild == null || this.mToolbarDirectChild == this) {
                toolbar_height = getHeightWithMargins(this.mToolbar);
            } else {
                toolbar_height = getHeightWithMargins(this.mToolbarDirectChild);
            }
            if (getMinimumHeight() != toolbar_height) {
                post(new Runnable() {
                    public void run() {
                        CollapsingToolbarLayout.this.setMinimumHeight(toolbar_height);
                    }
                });
            }
        }
        updateScrimVisibility();
    }

    private static int getHeightWithMargins(View view) {
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof MarginLayoutParams)) {
            return view.getHeight();
        }
        MarginLayoutParams mlp = (MarginLayoutParams) lp;
        return (view.getHeight() + mlp.topMargin) + mlp.bottomMargin;
    }

    static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(C0011R.id.view_offset_helper);
        if (offsetHelper != null) {
            return offsetHelper;
        }
        offsetHelper = new ViewOffsetHelper(view);
        view.setTag(C0011R.id.view_offset_helper, offsetHelper);
        return offsetHelper;
    }

    public void setTitle(CharSequence title) {
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setText(title);
        } else if (this.mCollapsingToolbarExtendedTitle != null) {
            this.mCollapsingToolbarExtendedTitle.setText(title);
        }
        updateTitleLayout();
    }

    public CharSequence getTitle() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getText();
        }
        return this.mCollapsingToolbarExtendedTitle.getText();
    }

    public void setTitleEnabled(boolean enabled) {
        if (!enabled) {
            this.mCollapsingToolbarLayoutTitleEnabled = false;
            this.mCollapsingTitleEnabled = false;
        } else if (this.mCollapsingToolbarExtendedTitle != null) {
            this.mCollapsingToolbarLayoutTitleEnabled = true;
            this.mCollapsingTitleEnabled = false;
        } else if (this.mCollapsingTextHelper != null) {
            this.mCollapsingTitleEnabled = true;
            this.mCollapsingToolbarLayoutTitleEnabled = false;
        } else {
            this.mCollapsingToolbarLayoutTitleEnabled = false;
            this.mCollapsingTitleEnabled = false;
        }
        if (!(enabled || this.mCollapsingToolbarLayoutTitleEnabled || this.mCollapsingToolbarExtendedTitle == null)) {
            this.mCollapsingToolbarExtendedTitle.setVisibility(4);
        }
        if (enabled && this.mCollapsingTitleEnabled) {
            updateDummyView();
            requestLayout();
        }
    }

    public void setSubtitle(int resId) {
        setSubtitle(getContext().getText(resId));
    }

    public void setSubtitle(CharSequence subtitle) {
        if (!this.mCollapsingToolbarLayoutTitleEnabled || TextUtils.isEmpty(subtitle)) {
            this.mCollapsingToolbarLayoutSubTitleEnabled = false;
            if (this.mCollapsingToolbarExtendedSubTitle != null) {
                ((ViewGroup) this.mCollapsingToolbarExtendedSubTitle.getParent()).removeView(this.mCollapsingToolbarExtendedSubTitle);
                this.mCollapsingToolbarExtendedSubTitle = null;
            }
        } else {
            this.mCollapsingToolbarLayoutSubTitleEnabled = true;
            if (this.mCollapsingToolbarExtendedSubTitle == null) {
                this.mCollapsingToolbarExtendedSubTitle = new TextView(getContext());
                this.mCollapsingToolbarExtendedSubTitle.setId(C0011R.id.collpasing_app_bar_extended_sub_title);
                android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(-2, -2);
                this.mCollapsingToolbarExtendedSubTitle.setText(subtitle);
                params.gravity = 1;
                this.mCollapsingTitleLayout.addView(this.mCollapsingToolbarExtendedSubTitle, params);
                this.mCollapsingToolbarExtendedSubTitle.setSingleLine(false);
                this.mCollapsingToolbarExtendedSubTitle.setMaxLines(1);
                this.mCollapsingToolbarExtendedSubTitle.setGravity(1);
                this.mCollapsingToolbarExtendedSubTitle.setTextAppearance(getContext(), this.mExtendSubTitleAppearance);
            } else {
                this.mCollapsingToolbarExtendedSubTitle.setText(subtitle);
            }
            if (this.mCollapsingToolbarExtendedTitle != null) {
                this.mCollapsingToolbarExtendedTitle.setTextSize(0, (float) getContext().getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_text_size_title_extend_with_subtitle));
            }
        }
        requestLayout();
        updateTitleLayout();
    }

    public CharSequence getSubTitle() {
        return this.mCollapsingToolbarExtendedSubTitle != null ? this.mCollapsingToolbarExtendedSubTitle.getText() : null;
    }

    public void setScrimsShown(boolean shown) {
        boolean z = ViewCompat.isLaidOut(this) && !isInEditMode();
        setScrimsShown(shown, z);
    }

    public void setScrimsShown(boolean shown, boolean animate) {
        int i = 255;
        if (this.mScrimsAreShown != shown) {
            if (animate) {
                if (!shown) {
                    i = 0;
                }
                animateScrim(i);
            } else {
                if (!shown) {
                    i = 0;
                }
                setScrimAlpha(i);
            }
            this.mScrimsAreShown = shown;
        }
    }

    private void animateScrim(int targetAlpha) {
        ensureToolbar();
        if (this.mScrimAnimator == null) {
            this.mScrimAnimator = new ValueAnimator();
            this.mScrimAnimator.setDuration(this.mScrimAnimationDuration);
            this.mScrimAnimator.setInterpolator(targetAlpha > this.mScrimAlpha ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrimAnimator.addUpdateListener(new C00363());
        } else if (this.mScrimAnimator.isRunning()) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimator.setIntValues(new int[]{this.mScrimAlpha, targetAlpha});
        this.mScrimAnimator.start();
    }

    void setScrimAlpha(int alpha) {
        if (alpha != this.mScrimAlpha) {
            if (!(this.mContentScrim == null || this.mToolbar == null)) {
                ViewCompat.postInvalidateOnAnimation(this.mToolbar);
            }
            this.mScrimAlpha = alpha;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    int getScrimAlpha() {
        return this.mScrimAlpha;
    }

    public void setContentScrim(Drawable drawable) {
        Drawable drawable2 = null;
        if (this.mContentScrim != drawable) {
            if (this.mContentScrim != null) {
                this.mContentScrim.setCallback(null);
            }
            if (drawable != null) {
                drawable2 = drawable.mutate();
            }
            this.mContentScrim = drawable2;
            if (this.mContentScrim != null) {
                this.mContentScrim.setBounds(0, 0, getWidth(), getHeight());
                this.mContentScrim.setCallback(this);
                this.mContentScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrimColor(int color) {
        setContentScrim(new ColorDrawable(color));
    }

    public void setContentScrimResource(int resId) {
        setContentScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    public Drawable getContentScrim() {
        return this.mContentScrim;
    }

    public void setStatusBarScrim(Drawable drawable) {
        Drawable drawable2 = null;
        if (this.mStatusBarScrim != drawable) {
            if (this.mStatusBarScrim != null) {
                this.mStatusBarScrim.setCallback(null);
            }
            if (drawable != null) {
                drawable2 = drawable.mutate();
            }
            this.mStatusBarScrim = drawable2;
            if (this.mStatusBarScrim != null) {
                if (this.mStatusBarScrim.isStateful()) {
                    this.mStatusBarScrim.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarScrim, ViewCompat.getLayoutDirection(this));
                this.mStatusBarScrim.setVisible(getVisibility() == 0, false);
                this.mStatusBarScrim.setCallback(this);
                this.mStatusBarScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable d = this.mStatusBarScrim;
        if (d != null && d.isStateful()) {
            changed = false | d.setState(state);
        }
        d = this.mContentScrim;
        if (d != null && d.isStateful()) {
            changed |= d.setState(state);
        }
        if (this.mCollapsingTextHelper != null) {
            changed |= this.mCollapsingTextHelper.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mContentScrim || who == this.mStatusBarScrim;
    }

    public void setVisibility(int visibility) {
        boolean visible;
        super.setVisibility(visibility);
        if (visibility == 0) {
            visible = true;
        } else {
            visible = false;
        }
        if (!(this.mStatusBarScrim == null || this.mStatusBarScrim.isVisible() == visible)) {
            this.mStatusBarScrim.setVisible(visible, false);
        }
        if (this.mContentScrim != null && this.mContentScrim.isVisible() != visible) {
            this.mContentScrim.setVisible(visible, false);
        }
    }

    public void setStatusBarScrimColor(int color) {
        setStatusBarScrim(new ColorDrawable(color));
    }

    public void setStatusBarScrimResource(int resId) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    public Drawable getStatusBarScrim() {
        return this.mStatusBarScrim;
    }

    public void setCollapsedTitleTextAppearance(int resId) {
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setCollapsedTextAppearance(resId);
        }
    }

    public void setCollapsedTitleTextColor(int color) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setCollapsedTitleTextColor(ColorStateList colors) {
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setCollapsedTextColor(colors);
        }
    }

    public void setCollapsedTitleGravity(int gravity) {
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setCollapsedTextGravity(gravity);
        }
    }

    public int getCollapsedTitleGravity() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getCollapsedTextGravity();
        }
        return -1;
    }

    public void setExpandedTitleTextAppearance(int resId) {
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            this.mCollapsingToolbarExtendedTitle.setTextAppearance(getContext(), resId);
        } else if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setExpandedTextAppearance(resId);
        }
    }

    public void setExpandedTitleColor(int color) {
        setExpandedTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setExpandedTitleTextColor(ColorStateList colors) {
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            this.mCollapsingToolbarExtendedTitle.setTextColor(colors);
        } else if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setExpandedTextColor(colors);
        }
    }

    public void setExpandedTitleGravity(int gravity) {
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            this.mCollapsingToolbarExtendedTitle.setGravity(gravity);
        } else if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setExpandedTextGravity(gravity);
        }
    }

    public int getExpandedTitleGravity() {
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            return this.mCollapsingToolbarExtendedTitle.getGravity();
        }
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getExpandedTextGravity();
        }
        return -1;
    }

    public void setCollapsedTitleTypeface(Typeface typeface) {
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setCollapsedTypeface(typeface);
        }
    }

    public Typeface getCollapsedTitleTypeface() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getCollapsedTypeface();
        }
        return null;
    }

    public void setExpandedTitleTypeface(Typeface typeface) {
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            this.mCollapsingToolbarExtendedTitle.setTypeface(typeface);
        } else if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.setExpandedTypeface(typeface);
        }
    }

    public Typeface getExpandedTitleTypeface() {
        if (this.mCollapsingToolbarLayoutTitleEnabled) {
            return this.mCollapsingToolbarExtendedTitle.getTypeface();
        }
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getExpandedTypeface();
        }
        return null;
    }

    public int getExpandedTitleMarginStart() {
        return this.mExpandedMarginStart;
    }

    public void setExpandedTitleMarginStart(int margin) {
        this.mExpandedMarginStart = margin;
        requestLayout();
    }

    public int getExpandedTitleMarginTop() {
        return this.mExpandedMarginTop;
    }

    public void setExpandedTitleMarginTop(int margin) {
        this.mExpandedMarginTop = margin;
        requestLayout();
    }

    public int getExpandedTitleMarginEnd() {
        return this.mExpandedMarginEnd;
    }

    public void setExpandedTitleMarginEnd(int margin) {
        this.mExpandedMarginEnd = margin;
        requestLayout();
    }

    public int getExpandedTitleMarginBottom() {
        return this.mExpandedMarginBottom;
    }

    public void setExpandedTitleMarginBottom(int margin) {
        this.mExpandedMarginBottom = margin;
        requestLayout();
    }

    public void setScrimVisibleHeightTrigger(int height) {
        if (this.mScrimVisibleHeightTrigger != height) {
            this.mScrimVisibleHeightTrigger = height;
            updateScrimVisibility();
        }
    }

    public int getScrimVisibleHeightTrigger() {
        if (this.mScrimVisibleHeightTrigger >= 0) {
            return this.mScrimVisibleHeightTrigger;
        }
        int insetTop = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
        int minHeight = ViewCompat.getMinimumHeight(this);
        if (minHeight > 0) {
            return Math.min((minHeight * 2) + insetTop, getHeight());
        }
        return getHeight() / 3;
    }

    private int getStatusbarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelOffset(resourceId);
        }
        return 0;
    }

    public void setScrimAnimationDuration(long duration) {
        this.mScrimAnimationDuration = duration;
    }

    public long getScrimAnimationDuration() {
        return this.mScrimAnimationDuration;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public android.widget.FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected android.widget.FrameLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    final void updateScrimVisibility() {
        if (this.mContentScrim != null || this.mStatusBarScrim != null) {
            setScrimsShown(getHeight() + this.mCurrentOffset < getScrimVisibleHeightTrigger());
        }
    }

    final int getMaxOffsetForPinChild(View child) {
        return ((getHeight() - getViewOffsetHelper(child).getLayoutTop()) - child.getHeight()) - ((LayoutParams) child.getLayoutParams()).bottomMargin;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TypedValue typedValue = new TypedValue();
        getResources().getValue(C0011R.dimen.sesl_abl_height_proportion, typedValue, true);
        this.mHeightPercent = typedValue.getFloat();
        updateDefaultHeightDP();
        updateTitleLayout();
    }

    public void setCustomAccessibility(boolean usedCustom) {
        this.mIsCustomAccessibility = usedCustom;
    }

    public boolean getCustomAccessibility() {
        return this.mIsCustomAccessibility;
    }
}
