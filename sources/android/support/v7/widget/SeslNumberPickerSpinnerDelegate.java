package android.support.v7.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.System;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.SeslCompatibilityInfoReflector;
import android.support.v4.content.res.SeslConfigurationReflector;
import android.support.v4.graphics.SeslPaintReflector;
import android.support.v4.graphics.SeslRectReflector;
import android.support.v4.media.SeslAudioManagerReflector;
import android.support.v4.view.SeslHapticFeedbackConstantsReflector;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SeslHoverPopupWindowReflector;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslNumberPicker.CustomEditText;
import android.support.v7.widget.SeslNumberPicker.Formatter;
import android.support.v7.widget.SeslNumberPicker.OnEditTextModeChangedListener;
import android.support.v7.widget.SeslNumberPicker.OnScrollListener;
import android.support.v7.widget.SeslNumberPicker.OnValueChangeListener;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class SeslNumberPickerSpinnerDelegate extends AbstractSeslNumberPickerDelegate {
    private static final int DECREASE_BUTTON = 1;
    private static final int DEFAULT_CHANGE_VALUE_BY = 1;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final char[] DIGIT_CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩', '۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹', '०', '१', '२', '३', '४', '५', '६', '७', '८', '९', '০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯', '೦', '೧', '೨', '೩', '೪', '೫', '೬', '೭', '೮', '೯', '၀', '၁', '၂', '၃', '၄', '၅', '၆', '၇', '၈', '၉'};
    private static final int INCREASE_BUTTON = 3;
    private static final int INPUT = 2;
    private static final String INPUT_TYPE_MONTH = "inputType=month_edittext";
    private static final String INPUT_TYPE_YEAR_DATE_TIME = "inputType=YearDateTime_edittext";
    private static final int LONG_PRESSED_SCROLL_COUNT = 10;
    private static final int PICKER_VIBRATE_INDEX = 32;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 300;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 4;
    private static final int SELECTOR_MIDDLE_ITEM_INDEX = 2;
    private static final int SELECTOR_WHEEL_ITEM_COUNT = 5;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 500;
    private static final int START_ANIMATION_SCROLL_DURATION = 857;
    private static final int START_ANIMATION_SCROLL_DURATION_2016B = 557;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
    private final PathInterpolator ALPHA_PATH_INTERPOLATOR = new PathInterpolator(0.17f, 0.17f, 0.83f, 0.83f);
    private final PathInterpolator SIZE_PATH_INTERPOLATOR = new PathInterpolator(0.5f, 0.0f, 0.4f, 1.0f);
    private AccessibilityManager mAccessibilityManager;
    private AccessibilityNodeProviderImpl mAccessibilityNodeProvider;
    private float mActivatedAlpha = 0.4f;
    private final Scroller mAdjustScroller;
    private float mAlpha = this.mIdleAlpha;
    private SeslAnimationListener mAnimationListener;
    private AudioManager mAudioManager;
    private BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
    private int mBottomSelectionDividerBottom;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private int mChangeValueBy = 1;
    private final boolean mComputeMaxWidth;
    private int mCurrentScrollOffset;
    private final Scroller mCustomScroller;
    private boolean mCustomTypefaceSet = false;
    private boolean mDecrementVirtualButtonPressed;
    private final Typeface mDefaultTypeface;
    private String[] mDisplayedValues;
    private ValueAnimator mFadeInAnimator;
    private ValueAnimator mFadeOutAnimator;
    private Scroller mFlingScroller;
    private Formatter mFormatter;
    private HapticPreDrawListener mHapticPreDrawListener;
    private final float mHeightRatio;
    private float mIdleAlpha = 0.1f;
    private boolean mIgnoreMoveEvents;
    private boolean mIncrementVirtualButtonPressed;
    private int mInitialScrollOffset = Integer.MIN_VALUE;
    private final EditText mInputText;
    private boolean mIsAmPm;
    private boolean mIsEditTextMode;
    private boolean mIsLongPressed = false;
    private boolean mIsStartingAnimation = false;
    private boolean mIsValueChanged = false;
    private long mLastDownEventTime;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventY;
    private int mLastFocusedChildVirtualViewId;
    private int mLastHoveredChildVirtualViewId;
    private final Typeface mLegacyTypeface;
    private final Scroller mLinearScroller;
    private int mLongPressCount;
    private long mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
    private boolean mLongPressed_FIRST_SCROLL;
    private boolean mLongPressed_SECOND_SCROLL;
    private boolean mLongPressed_THIRD_SCROLL;
    private final int mMaxHeight;
    private int mMaxValue;
    private int mMaxWidth;
    private int mMaximumFlingVelocity;
    private final int mMinHeight;
    private int mMinValue;
    private final int mMinWidth;
    private int mMinimumFlingVelocity;
    private int mModifiedTxtHeight;
    private OnEditTextModeChangedListener mOnEditTextModeChangedListener;
    private OnScrollListener mOnScrollListener;
    private OnValueChangeListener mOnValueChangeListener;
    private boolean mPerformClickOnTap;
    private String mPickerContentDescription;
    private int mPickerSoundIndex;
    private Typeface mPickerTypeface;
    private int mPickerVibrateIndex;
    private final PressedStateHelper mPressedStateHelper;
    private int mPreviousScrollerY;
    private boolean mReservedStartAnimation = false;
    private int mScrollState = 0;
    private final int mSelectionDividerHeight;
    private int mSelectorElementHeight;
    private final SparseArray<String> mSelectorIndexToStringCache = new SparseArray();
    private final int[] mSelectorIndices = new int[5];
    private int mSelectorTextGapHeight;
    private Paint mSelectorWheelPaint;
    private boolean mSkipNumbers;
    private final int mTextColor;
    private int mTextSize;
    private Toast mToast;
    private String mToastText;
    private int mTopSelectionDividerTop;
    private int mTouchSlop;
    private AnimatorUpdateListener mUpdateListener = new C03795();
    private int mValue;
    private int mValueChangeOffset;
    private VelocityTracker mVelocityTracker;
    private boolean mVibratePermission = false;
    private final Drawable mVirtualButtonFocusedDrawable;
    private boolean mWrapSelectorWheel;

    /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$1 */
    class C03711 implements OnFocusChangeListener {
        C03711() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SeslNumberPickerSpinnerDelegate.this.setEditTextMode(true);
                SeslNumberPickerSpinnerDelegate.this.mInputText.selectAll();
                return;
            }
            SeslNumberPickerSpinnerDelegate.this.mInputText.setSelection(0, 0);
            SeslNumberPickerSpinnerDelegate.this.validateInputTextView(v);
        }
    }

    /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$2 */
    class C03722 implements OnTouchListener {
        C03722() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (!(v instanceof EditText) || event.getActionMasked() != 0) {
                return false;
            }
            ((EditText) v).selectAll();
            SeslNumberPickerSpinnerDelegate.this.showSoftInput();
            return true;
        }
    }

    /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$3 */
    class C03743 implements Runnable {

        /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$3$1 */
        class C03731 implements Runnable {
            C03731() {
            }

            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) SeslNumberPickerSpinnerDelegate.this.mContext.getSystemService("input_method");
                if (inputMethodManager != null && SeslNumberPickerSpinnerDelegate.this.mIsEditTextMode && SeslNumberPickerSpinnerDelegate.this.mInputText.isFocused()) {
                    inputMethodManager.showSoftInput(SeslNumberPickerSpinnerDelegate.this.mInputText, 0);
                }
            }
        }

        C03743() {
        }

        public void run() {
            InputMethodManager inputMethodManager = (InputMethodManager) SeslNumberPickerSpinnerDelegate.this.mContext.getSystemService("input_method");
            if (inputMethodManager != null && SeslNumberPickerSpinnerDelegate.this.mIsEditTextMode && SeslNumberPickerSpinnerDelegate.this.mInputText.isFocused() && !inputMethodManager.showSoftInput(SeslNumberPickerSpinnerDelegate.this.mInputText, 0)) {
                SeslNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(new C03731(), 20);
            }
        }
    }

    /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$5 */
    class C03795 implements AnimatorUpdateListener {
        C03795() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SeslNumberPickerSpinnerDelegate.this.mAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
        }
    }

    class AccessibilityNodeProviderImpl extends AccessibilityNodeProvider {
        private static final int UNDEFINED = Integer.MIN_VALUE;
        private static final int VIRTUAL_VIEW_ID_DECREMENT = 1;
        private static final int VIRTUAL_VIEW_ID_INCREMENT = 3;
        private static final int VIRTUAL_VIEW_ID_INPUT = 2;
        private int mAccessibilityFocusedView = Integer.MIN_VALUE;
        private final int[] mTempArray = new int[2];
        private final Rect mTempRect = new Rect();

        AccessibilityNodeProviderImpl() {
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
            int left = SeslNumberPickerSpinnerDelegate.this.mDelegator.getLeft();
            int right = SeslNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int top = SeslNumberPickerSpinnerDelegate.this.mDelegator.getTop();
            int bottom = SeslNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            int scrollX = SeslNumberPickerSpinnerDelegate.this.mDelegator.getScrollX();
            int scrollY = SeslNumberPickerSpinnerDelegate.this.mDelegator.getScrollY();
            if (!(SeslNumberPickerSpinnerDelegate.this.mLastFocusedChildVirtualViewId == -1 && SeslNumberPickerSpinnerDelegate.this.mLastHoveredChildVirtualViewId == Integer.MIN_VALUE)) {
                switch (virtualViewId) {
                    case -1:
                        return createAccessibilityNodeInfoForNumberPicker(scrollX, scrollY, (right - left) + scrollX, (bottom - top) + scrollY);
                    case 1:
                        return createAccessibilityNodeInfoForVirtualButton(1, getVirtualDecrementButtonText(), scrollX, scrollY, scrollX + (right - left), SeslNumberPickerSpinnerDelegate.this.mSelectionDividerHeight + SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                    case 2:
                        return createAccessibiltyNodeInfoForInputText(scrollX, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop + SeslNumberPickerSpinnerDelegate.this.mSelectionDividerHeight, (right - left) + scrollX, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom - SeslNumberPickerSpinnerDelegate.this.mSelectionDividerHeight);
                    case 3:
                        return createAccessibilityNodeInfoForVirtualButton(3, getVirtualIncrementButtonText(), scrollX, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom - SeslNumberPickerSpinnerDelegate.this.mSelectionDividerHeight, scrollX + (right - left), scrollY + (bottom - top));
                }
            }
            AccessibilityNodeInfo info = super.createAccessibilityNodeInfo(virtualViewId);
            if (info == null) {
                return AccessibilityNodeInfo.obtain();
            }
            return info;
        }

        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String searched, int virtualViewId) {
            if (TextUtils.isEmpty(searched)) {
                return Collections.emptyList();
            }
            String searchedLowerCase = searched.toLowerCase();
            List<AccessibilityNodeInfo> result = new ArrayList();
            switch (virtualViewId) {
                case -1:
                    findAccessibilityNodeInfosByTextInChild(searchedLowerCase, 1, result);
                    findAccessibilityNodeInfosByTextInChild(searchedLowerCase, 2, result);
                    findAccessibilityNodeInfosByTextInChild(searchedLowerCase, 3, result);
                    return result;
                case 1:
                case 2:
                case 3:
                    findAccessibilityNodeInfosByTextInChild(searchedLowerCase, virtualViewId, result);
                    return result;
                default:
                    return super.findAccessibilityNodeInfosByText(searched, virtualViewId);
            }
        }

        public boolean performAction(int virtualViewId, int action, Bundle arguments) {
            if (SeslNumberPickerSpinnerDelegate.this.mIsStartingAnimation) {
                return false;
            }
            int right = SeslNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int bottom = SeslNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            switch (virtualViewId) {
                case -1:
                    switch (action) {
                        case 64:
                            if (this.mAccessibilityFocusedView == virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = virtualViewId;
                            SeslViewReflector.requestAccessibilityFocus(SeslNumberPickerSpinnerDelegate.this.mDelegator);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            SeslViewReflector.clearAccessibilityFocus(SeslNumberPickerSpinnerDelegate.this.mDelegator);
                            return true;
                        case 4096:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            if (!SeslNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() && SeslNumberPickerSpinnerDelegate.this.getValue() >= SeslNumberPickerSpinnerDelegate.this.getMaxValue()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(false);
                            SeslNumberPickerSpinnerDelegate.this.changeValueByOne(true);
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(true);
                            return true;
                        case 8192:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            if (!SeslNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() && SeslNumberPickerSpinnerDelegate.this.getValue() <= SeslNumberPickerSpinnerDelegate.this.getMinValue()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(false);
                            SeslNumberPickerSpinnerDelegate.this.changeValueByOne(false);
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(true);
                            return true;
                        default:
                            break;
                    }
                case 1:
                    switch (action) {
                        case 16:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(false);
                            SeslNumberPickerSpinnerDelegate.this.changeValueByOne(false);
                            sendAccessibilityEventForVirtualView(virtualViewId, 1);
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(true);
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = virtualViewId;
                            sendAccessibilityEventForVirtualView(virtualViewId, 32768);
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(virtualViewId, 65536);
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return true;
                        default:
                            return false;
                    }
                case 2:
                    switch (action) {
                        case 1:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled() || SeslNumberPickerSpinnerDelegate.this.mInputText.isFocused()) {
                                return false;
                            }
                            return SeslNumberPickerSpinnerDelegate.this.mInputText.requestFocus();
                        case 2:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled() || !SeslNumberPickerSpinnerDelegate.this.mInputText.isFocused()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.mInputText.clearFocus();
                            return true;
                        case 16:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.performClick();
                            return true;
                        case 32:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.performLongClick();
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = virtualViewId;
                            sendAccessibilityEventForVirtualView(virtualViewId, 32768);
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop, right, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(virtualViewId, 65536);
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop, right, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom);
                            return true;
                        default:
                            return SeslNumberPickerSpinnerDelegate.this.mInputText.performAccessibilityAction(action, arguments);
                    }
                case 3:
                    switch (action) {
                        case 16:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(false);
                            SeslNumberPickerSpinnerDelegate.this.changeValueByOne(true);
                            sendAccessibilityEventForVirtualView(virtualViewId, 1);
                            SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(true);
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = virtualViewId;
                            sendAccessibilityEventForVirtualView(virtualViewId, 32768);
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != virtualViewId) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(virtualViewId, 65536);
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
                            return true;
                        default:
                            return false;
                    }
            }
            return super.performAction(virtualViewId, action, arguments);
        }

        public void sendAccessibilityEventForVirtualView(int virtualViewId, int eventType) {
            switch (virtualViewId) {
                case 1:
                    if (hasVirtualDecrementButton()) {
                        sendAccessibilityEventForVirtualButton(virtualViewId, eventType, getVirtualDecrementButtonText());
                        return;
                    }
                    return;
                case 2:
                    sendAccessibilityEventForVirtualText(eventType);
                    return;
                case 3:
                    if (hasVirtualIncrementButton()) {
                        sendAccessibilityEventForVirtualButton(virtualViewId, eventType, getVirtualIncrementButtonText());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void sendAccessibilityEventForVirtualText(int eventType) {
            if (SeslNumberPickerSpinnerDelegate.this.mAccessibilityManager.isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
                SeslNumberPickerSpinnerDelegate.this.mInputText.onInitializeAccessibilityEvent(event);
                SeslNumberPickerSpinnerDelegate.this.mInputText.onPopulateAccessibilityEvent(event);
                event.setSource(SeslNumberPickerSpinnerDelegate.this.mDelegator, 2);
                SeslNumberPickerSpinnerDelegate.this.mDelegator.requestSendAccessibilityEvent(SeslNumberPickerSpinnerDelegate.this.mDelegator, event);
            }
        }

        private void sendAccessibilityEventForVirtualButton(int virtualViewId, int eventType, String text) {
            if (SeslNumberPickerSpinnerDelegate.this.mAccessibilityManager.isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
                event.setClassName(Button.class.getName());
                event.setPackageName(SeslNumberPickerSpinnerDelegate.this.mContext.getPackageName());
                event.getText().add(text);
                event.setEnabled(SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled());
                event.setSource(SeslNumberPickerSpinnerDelegate.this.mDelegator, virtualViewId);
                SeslNumberPickerSpinnerDelegate.this.mDelegator.requestSendAccessibilityEvent(SeslNumberPickerSpinnerDelegate.this.mDelegator, event);
            }
        }

        private void findAccessibilityNodeInfosByTextInChild(String searchedLowerCase, int virtualViewId, List<AccessibilityNodeInfo> outResult) {
            String text;
            switch (virtualViewId) {
                case 1:
                    text = getVirtualDecrementButtonText();
                    if (!TextUtils.isEmpty(text) && text.toLowerCase().contains(searchedLowerCase)) {
                        outResult.add(createAccessibilityNodeInfo(1));
                        return;
                    }
                    return;
                case 2:
                    CharSequence text2 = SeslNumberPickerSpinnerDelegate.this.mInputText.getText();
                    if (!TextUtils.isEmpty(text2) && text2.toString().toLowerCase().contains(searchedLowerCase)) {
                        outResult.add(createAccessibilityNodeInfo(2));
                        return;
                    }
                    return;
                case 3:
                    text = getVirtualIncrementButtonText();
                    if (!TextUtils.isEmpty(text) && text.toLowerCase().contains(searchedLowerCase)) {
                        outResult.add(createAccessibilityNodeInfo(3));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private AccessibilityNodeInfo createAccessibiltyNodeInfoForInputText(int left, int top, int right, int bottom) {
            AccessibilityNodeInfo info = SeslNumberPickerSpinnerDelegate.this.mInputText.createAccessibilityNodeInfo();
            info.setSource(SeslNumberPickerSpinnerDelegate.this.mDelegator, 2);
            if (this.mAccessibilityFocusedView != 2) {
                info.setAccessibilityFocused(false);
                info.addAction(64);
            } else {
                info.setAccessibilityFocused(true);
                info.addAction(128);
            }
            if (SeslNumberPickerSpinnerDelegate.this.mIsAmPm) {
                info.setClassName(TextView.class.getName());
                info.setText(getVirtualCurrentButtonText());
                info.setSelected(true);
                info.setAccessibilityFocused(false);
            }
            Rect boundsInParent = this.mTempRect;
            boundsInParent.set(left, top, right, bottom);
            info.setVisibleToUser(SeslNumberPickerSpinnerDelegate.this.mDelegator.isVisibleToUserWrapper(boundsInParent));
            info.setBoundsInParent(boundsInParent);
            Rect boundsInScreen = boundsInParent;
            int[] locationOnScreen = this.mTempArray;
            SeslNumberPickerSpinnerDelegate.this.mDelegator.getLocationOnScreen(locationOnScreen);
            boundsInScreen.offset(locationOnScreen[0], locationOnScreen[1]);
            info.setBoundsInScreen(boundsInScreen);
            return info;
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForVirtualButton(int virtualViewId, String text, int left, int top, int right, int bottom) {
            AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
            info.setClassName(Button.class.getName());
            info.setPackageName(SeslNumberPickerSpinnerDelegate.this.mContext.getPackageName());
            info.setSource(SeslNumberPickerSpinnerDelegate.this.mDelegator, virtualViewId);
            info.setParent(SeslNumberPickerSpinnerDelegate.this.mDelegator);
            info.setText(text);
            info.setClickable(true);
            info.setLongClickable(true);
            info.setEnabled(SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled());
            Rect boundsInParent = this.mTempRect;
            boundsInParent.set(left, top, right, bottom);
            info.setVisibleToUser(SeslNumberPickerSpinnerDelegate.this.mDelegator.isVisibleToUserWrapper(boundsInParent));
            info.setBoundsInParent(boundsInParent);
            Rect boundsInScreen = boundsInParent;
            int[] locationOnScreen = this.mTempArray;
            SeslNumberPickerSpinnerDelegate.this.mDelegator.getLocationOnScreen(locationOnScreen);
            boundsInScreen.offset(locationOnScreen[0], locationOnScreen[1]);
            info.setBoundsInScreen(boundsInScreen);
            if (this.mAccessibilityFocusedView != virtualViewId) {
                info.addAction(64);
            } else {
                info.addAction(128);
            }
            if (SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                info.addAction(16);
            }
            return info;
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(int left, int top, int right, int bottom) {
            AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
            info.setClassName(NumberPicker.class.getName());
            info.setPackageName(SeslNumberPickerSpinnerDelegate.this.mContext.getPackageName());
            info.setSource(SeslNumberPickerSpinnerDelegate.this.mDelegator);
            if (hasVirtualDecrementButton()) {
                info.addChild(SeslNumberPickerSpinnerDelegate.this.mDelegator, 1);
            }
            info.addChild(SeslNumberPickerSpinnerDelegate.this.mDelegator, 2);
            if (hasVirtualIncrementButton()) {
                info.addChild(SeslNumberPickerSpinnerDelegate.this.mDelegator, 3);
            }
            info.setParent((View) SeslNumberPickerSpinnerDelegate.this.mDelegator.getParentForAccessibility());
            info.setEnabled(SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled());
            info.setScrollable(true);
            float applicationScale = SeslCompatibilityInfoReflector.getField_applicationScale(SeslNumberPickerSpinnerDelegate.this.mContext.getResources());
            Rect boundsInParent = this.mTempRect;
            boundsInParent.set(left, top, right, bottom);
            SeslRectReflector.scale(boundsInParent, applicationScale);
            info.setBoundsInParent(boundsInParent);
            info.setVisibleToUser(SeslNumberPickerSpinnerDelegate.this.mDelegator.isVisibleToUserWrapper());
            Rect boundsInScreen = boundsInParent;
            int[] locationOnScreen = this.mTempArray;
            SeslNumberPickerSpinnerDelegate.this.mDelegator.getLocationOnScreen(locationOnScreen);
            boundsInScreen.offset(locationOnScreen[0], locationOnScreen[1]);
            SeslRectReflector.scale(boundsInScreen, applicationScale);
            info.setBoundsInScreen(boundsInScreen);
            if (this.mAccessibilityFocusedView != -1) {
                info.addAction(64);
            } else {
                info.addAction(128);
            }
            if (SeslNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                if (SeslNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SeslNumberPickerSpinnerDelegate.this.getValue() < SeslNumberPickerSpinnerDelegate.this.getMaxValue()) {
                    info.addAction(4096);
                }
                if (SeslNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SeslNumberPickerSpinnerDelegate.this.getValue() > SeslNumberPickerSpinnerDelegate.this.getMinValue()) {
                    info.addAction(8192);
                }
            }
            return info;
        }

        private boolean hasVirtualDecrementButton() {
            return SeslNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SeslNumberPickerSpinnerDelegate.this.getValue() > SeslNumberPickerSpinnerDelegate.this.getMinValue();
        }

        private boolean hasVirtualIncrementButton() {
            return SeslNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SeslNumberPickerSpinnerDelegate.this.getValue() < SeslNumberPickerSpinnerDelegate.this.getMaxValue();
        }

        private String getVirtualDecrementButtonText() {
            int value = SeslNumberPickerSpinnerDelegate.this.mValue - 1;
            if (SeslNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                value = SeslNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(value);
            }
            if (value < SeslNumberPickerSpinnerDelegate.this.mMinValue) {
                return null;
            }
            String access$2400;
            StringBuilder stringBuilder = new StringBuilder();
            if (SeslNumberPickerSpinnerDelegate.this.mDisplayedValues == null) {
                access$2400 = SeslNumberPickerSpinnerDelegate.this.formatNumber(value);
            } else {
                access$2400 = SeslNumberPickerSpinnerDelegate.this.mDisplayedValues[value - SeslNumberPickerSpinnerDelegate.this.mMinValue];
            }
            return stringBuilder.append(access$2400).append(", ").append(SeslNumberPickerSpinnerDelegate.this.mPickerContentDescription).append(", ").toString();
        }

        private String getVirtualIncrementButtonText() {
            int value = SeslNumberPickerSpinnerDelegate.this.mValue + 1;
            if (SeslNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                value = SeslNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(value);
            }
            if (value > SeslNumberPickerSpinnerDelegate.this.mMaxValue) {
                return null;
            }
            String access$2400;
            StringBuilder stringBuilder = new StringBuilder();
            if (SeslNumberPickerSpinnerDelegate.this.mDisplayedValues == null) {
                access$2400 = SeslNumberPickerSpinnerDelegate.this.formatNumber(value);
            } else {
                access$2400 = SeslNumberPickerSpinnerDelegate.this.mDisplayedValues[value - SeslNumberPickerSpinnerDelegate.this.mMinValue];
            }
            return stringBuilder.append(access$2400).append(", ").append(SeslNumberPickerSpinnerDelegate.this.mPickerContentDescription).append(", ").toString();
        }

        private String getVirtualCurrentButtonText() {
            int value = SeslNumberPickerSpinnerDelegate.this.mValue;
            if (SeslNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                value = SeslNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(value);
            }
            if (value > SeslNumberPickerSpinnerDelegate.this.mMaxValue) {
                return null;
            }
            String access$2400;
            StringBuilder stringBuilder = new StringBuilder();
            if (SeslNumberPickerSpinnerDelegate.this.mDisplayedValues == null) {
                access$2400 = SeslNumberPickerSpinnerDelegate.this.formatNumber(value);
            } else {
                access$2400 = SeslNumberPickerSpinnerDelegate.this.mDisplayedValues[value - SeslNumberPickerSpinnerDelegate.this.mMinValue];
            }
            return stringBuilder.append(access$2400).append(", ").append(SeslNumberPickerSpinnerDelegate.this.mPickerContentDescription).append(" ").toString();
        }
    }

    class BeginSoftInputOnLongPressCommand implements Runnable {
        BeginSoftInputOnLongPressCommand() {
        }

        public void run() {
            SeslNumberPickerSpinnerDelegate.this.performLongClick();
        }
    }

    class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        ChangeCurrentByOneFromLongPressCommand() {
        }

        private void setStep(boolean increment) {
            this.mIncrement = increment;
        }

        public void run() {
            SeslNumberPickerSpinnerDelegate.this.changeValueByOne(this.mIncrement);
            SeslNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, SeslNumberPickerSpinnerDelegate.this.mLongPressUpdateInterval);
        }
    }

    private static class HapticPreDrawListener implements OnPreDrawListener {
        public boolean mSkipHapticCalls;

        private HapticPreDrawListener() {
            this.mSkipHapticCalls = false;
        }

        public boolean onPreDraw() {
            this.mSkipHapticCalls = false;
            return true;
        }
    }

    class InputTextFilter extends NumberKeyListener {
        InputTextFilter() {
        }

        public int getInputType() {
            return 1;
        }

        protected char[] getAcceptedChars() {
            return SeslNumberPickerSpinnerDelegate.DIGIT_CHARACTERS;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            CharSequence filtered;
            String result;
            CharSequence charSequence;
            if (SeslNumberPickerSpinnerDelegate.this.mDisplayedValues == null) {
                filtered = super.filter(source, start, end, dest, dstart, dend);
                if (filtered == null) {
                    filtered = source.subSequence(start, end);
                }
                result = String.valueOf(dest.subSequence(0, dstart)) + filtered + dest.subSequence(dend, dest.length());
                if ("".equals(result)) {
                    return result;
                } else if (SeslNumberPickerSpinnerDelegate.this.getSelectedPos(result) > SeslNumberPickerSpinnerDelegate.this.mMaxValue || result.length() > SeslNumberPickerSpinnerDelegate.this.formatNumber(SeslNumberPickerSpinnerDelegate.this.mMaxValue).length()) {
                    if (SeslNumberPickerSpinnerDelegate.this.mIsEditTextMode) {
                        if (SeslNumberPickerSpinnerDelegate.this.mToast == null) {
                            SeslNumberPickerSpinnerDelegate.this.mToast = Toast.makeText(SeslNumberPickerSpinnerDelegate.this.mContext, SeslNumberPickerSpinnerDelegate.this.mToastText, 0);
                        }
                        SeslNumberPickerSpinnerDelegate.this.mToast.show();
                    }
                    String str = result;
                    charSequence = filtered;
                    return "";
                } else {
                    charSequence = filtered;
                    return filtered;
                }
            }
            filtered = String.valueOf(source.subSequence(start, end));
            result = String.valueOf(dest.subSequence(0, dstart)) + filtered + dest.subSequence(dend, dest.length());
            String str2 = String.valueOf(result).toLowerCase();
            for (String val : SeslNumberPickerSpinnerDelegate.this.mDisplayedValues) {
                String val2 = val2.toLowerCase();
                if (SeslNumberPickerSpinnerDelegate.this.needCompareEqualMonthLanguage()) {
                    if (val2.equals(str2)) {
                        str = result;
                        charSequence = filtered;
                        return filtered;
                    }
                } else if (val2.startsWith(str2)) {
                    str = result;
                    charSequence = filtered;
                    return filtered;
                }
            }
            if (SeslNumberPickerSpinnerDelegate.this.mIsEditTextMode && !TextUtils.isEmpty(str2)) {
                if (SeslNumberPickerSpinnerDelegate.this.mToast == null) {
                    SeslNumberPickerSpinnerDelegate.this.mToast = Toast.makeText(SeslNumberPickerSpinnerDelegate.this.mContext, SeslNumberPickerSpinnerDelegate.this.mToastText, 0);
                }
                SeslNumberPickerSpinnerDelegate.this.mToast.show();
            }
            str = result;
            charSequence = filtered;
            return "";
        }
    }

    class PressedStateHelper implements Runnable {
        public static final int BUTTON_DECREMENT = 2;
        public static final int BUTTON_INCREMENT = 1;
        private final int MODE_PRESS = 1;
        private final int MODE_TAPPED = 2;
        private int mManagedButton;
        private int mMode;

        PressedStateHelper() {
        }

        public void cancel() {
            int right = SeslNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int bottom = SeslNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            this.mMode = 0;
            this.mManagedButton = 0;
            SeslNumberPickerSpinnerDelegate.this.mDelegator.removeCallbacks(this);
            if (SeslNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed) {
                SeslNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed = false;
                SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
            }
            if (SeslNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed) {
                SeslNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed = false;
                SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
            }
        }

        public void buttonPressDelayed(int button) {
            cancel();
            this.mMode = 1;
            this.mManagedButton = button;
            SeslNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, (long) ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int button) {
            cancel();
            this.mMode = 2;
            this.mManagedButton = button;
            SeslNumberPickerSpinnerDelegate.this.mDelegator.post(this);
        }

        public void run() {
            int right = SeslNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int bottom = SeslNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            switch (this.mMode) {
                case 1:
                    switch (this.mManagedButton) {
                        case 1:
                            SeslNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed = true;
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
                            return;
                        case 2:
                            SeslNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed = true;
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return;
                        default:
                            return;
                    }
                case 2:
                    switch (this.mManagedButton) {
                        case 1:
                            if (!SeslNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed) {
                                SeslNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                            }
                            SeslNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed = SeslNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed ^ 1;
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SeslNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
                            return;
                        case 2:
                            if (!SeslNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed) {
                                SeslNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                            }
                            SeslNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed = SeslNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed ^ 1;
                            SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SeslNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return;
                        default:
                            return;
                    }
                default:
                    return;
            }
        }
    }

    public SeslNumberPickerSpinnerDelegate(SeslNumberPicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        Resources res = this.mContext.getResources();
        int defaultHeight = res.getDimensionPixelSize(C0247R.dimen.sesl_number_picker_spinner_height);
        int defaultWidth = res.getDimensionPixelSize(C0247R.dimen.sesl_number_picker_spinner_width);
        this.mHeightRatio = ((float) res.getDimensionPixelSize(C0247R.dimen.sesl_number_picker_spinner_edit_text_height)) / ((float) defaultHeight);
        TypedArray attributesArray = context.obtainStyledAttributes(attrs, C0247R.styleable.NumberPicker, defStyleAttr, defStyleRes);
        this.mMinHeight = attributesArray.getDimensionPixelSize(C0247R.styleable.NumberPicker_internalMinHeight, -1);
        this.mMaxHeight = attributesArray.getDimensionPixelSize(C0247R.styleable.NumberPicker_internalMaxHeight, defaultHeight);
        this.mMinWidth = attributesArray.getDimensionPixelSize(C0247R.styleable.NumberPicker_internalMinWidth, defaultWidth);
        this.mMaxWidth = attributesArray.getDimensionPixelSize(C0247R.styleable.NumberPicker_internalMaxWidth, -1);
        attributesArray.recycle();
        if (this.mMinHeight != -1 && this.mMaxHeight != -1 && this.mMinHeight > this.mMaxHeight) {
            throw new IllegalArgumentException("minHeight > maxHeight");
        } else if (this.mMinWidth == -1 || this.mMaxWidth == -1 || this.mMinWidth <= this.mMaxWidth) {
            int selectedPickerColor;
            this.mSelectionDividerHeight = (int) TypedValue.applyDimension(1, 2.0f, res.getDisplayMetrics());
            this.mComputeMaxWidth = this.mMaxWidth == -1;
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(C0247R.attr.colorPrimaryDark, outValue, true);
            if (outValue.resourceId != 0) {
                selectedPickerColor = (ResourcesCompat.getColor(res, outValue.resourceId, null) & ViewCompat.MEASURED_SIZE_MASK) | 855638016;
            } else {
                selectedPickerColor = (outValue.data & ViewCompat.MEASURED_SIZE_MASK) | 855638016;
            }
            this.mVirtualButtonFocusedDrawable = new ColorDrawable(selectedPickerColor);
            this.mContext.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
            if (outValue.data == 0) {
                this.mIdleAlpha = 0.2f;
                this.mAlpha = 0.2f;
            }
            this.mPressedStateHelper = new PressedStateHelper();
            this.mDelegator.setWillNotDraw(false);
            ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0247R.layout.sesl_number_picker_spinner, this.mDelegator, true);
            this.mInputText = (EditText) this.mDelegator.findViewById(C0247R.id.numberpicker_input);
            this.mInputText.setLongClickable(false);
            this.mInputText.setIncludeFontPadding(false);
            this.mDefaultTypeface = Typeface.defaultFromStyle(0);
            this.mLegacyTypeface = Typeface.create("sec-roboto-condensed-light", 0);
            this.mPickerTypeface = Typeface.create("sec-roboto-light", 0);
            if (this.mDefaultTypeface.equals(this.mPickerTypeface)) {
                if (this.mLegacyTypeface.equals(this.mPickerTypeface)) {
                    this.mPickerTypeface = Typeface.create("sans-serif-thin", 0);
                } else {
                    this.mPickerTypeface = this.mLegacyTypeface;
                }
            }
            if (SeslConfigurationReflector.getField_semDesktopModeEnabled(res.getConfiguration()) == SeslConfigurationReflector.getField_SEM_DESKTOP_MODE_ENABLED(res.getConfiguration())) {
                this.mIdleAlpha = 0.2f;
                this.mAlpha = 0.2f;
            } else {
                String themeTypeFace = System.getString(this.mContext.getContentResolver(), "theme_font_clock");
                if (!(themeTypeFace == null || themeTypeFace.isEmpty())) {
                    this.mPickerTypeface = getFontTypeface(themeTypeFace);
                }
            }
            if (isCharacterNumberLanguage()) {
                this.mInputText.setIncludeFontPadding(true);
                this.mPickerTypeface = this.mDefaultTypeface;
            }
            this.mInputText.setTypeface(this.mPickerTypeface);
            this.mTextColor = this.mInputText.getTextColors().getColorForState(this.mDelegator.getEnableStateSet(), -1);
            this.mInputText.setOnFocusChangeListener(new C03711());
            this.mInputText.setOnTouchListener(new C03722());
            this.mInputText.setFilters(new InputFilter[]{new InputTextFilter()});
            this.mInputText.setRawInputType(2);
            this.mInputText.setImeOptions(33554438);
            this.mInputText.setCursorVisible(false);
            this.mInputText.setHighlightColor(selectedPickerColor);
            SeslViewReflector.semSetHoverPopupType(this.mInputText, SeslHoverPopupWindowReflector.getField_TYPE_NONE());
            ViewConfiguration configuration = ViewConfiguration.get(context);
            this.mTouchSlop = configuration.getScaledTouchSlop();
            this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity() * 2;
            this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity() / 4;
            this.mTextSize = (int) this.mInputText.getTextSize();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize((float) this.mTextSize);
            paint.setTypeface(this.mPickerTypeface);
            paint.setColor(this.mTextColor);
            this.mSelectorWheelPaint = paint;
            this.mCustomScroller = new Scroller(this.mContext, this.SIZE_PATH_INTERPOLATOR, true);
            this.mLinearScroller = new Scroller(this.mContext, null, true);
            this.mFlingScroller = this.mLinearScroller;
            this.mAdjustScroller = new Scroller(this.mContext, new PathInterpolator(0.4f, 0.0f, 0.3f, 1.0f));
            setFormatter(SeslNumberPicker.getTwoDigitFormatter());
            updateInputTextView();
            if (this.mDelegator.getImportantForAccessibility() == 0) {
                this.mDelegator.setImportantForAccessibility(1);
            }
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
            if (this.mContext.checkCallingOrSelfPermission("android.permission.VIBRATE") == 0) {
                this.mVibratePermission = true;
            }
            this.mHapticPreDrawListener = new HapticPreDrawListener();
            this.mPickerVibrateIndex = SeslHapticFeedbackConstantsReflector.semGetVibrationIndex(32);
            this.mPickerSoundIndex = SeslAudioManagerReflector.getField_SOUND_TIME_PICKER_SCROLL();
            this.mDelegator.setFocusableInTouchMode(false);
            this.mDelegator.setDescendantFocusability(131072);
            if (VERSION.SDK_INT >= 26) {
                this.mDelegator.setDefaultFocusHighlightEnabled(false);
            }
            this.mPickerContentDescription = "";
            this.mToastText = res.getString(C0247R.string.sesl_number_picker_invalid_value_entered);
            SeslViewReflector.semSetDirectPenInputEnabled(this.mInputText, false);
            this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
            this.mFadeOutAnimator = ValueAnimator.ofFloat(new float[]{this.mActivatedAlpha, this.mIdleAlpha});
            this.mFadeOutAnimator.setInterpolator(this.ALPHA_PATH_INTERPOLATOR);
            this.mFadeOutAnimator.setDuration(500);
            this.mFadeOutAnimator.setStartDelay(500);
            this.mFadeOutAnimator.addUpdateListener(this.mUpdateListener);
            this.mFadeInAnimator = ValueAnimator.ofFloat(new float[]{this.mIdleAlpha, this.mActivatedAlpha});
            this.mFadeInAnimator.setInterpolator(this.ALPHA_PATH_INTERPOLATOR);
            this.mFadeInAnimator.setDuration(100);
            this.mFadeInAnimator.addUpdateListener(this.mUpdateListener);
        } else {
            throw new IllegalArgumentException("minWidth > maxWidth");
        }
    }

    public void setPickerContentDescription(String name) {
        this.mPickerContentDescription = name;
        ((CustomEditText) this.mInputText).setPickerContentDescription(name);
    }

    public void setImeOptions(int imeOptions) {
        this.mInputText.setImeOptions(imeOptions);
    }

    public void setAmPm(boolean value) {
        this.mIsAmPm = value;
        if (this.mIsAmPm) {
            this.mTextSize = this.mContext.getResources().getDimensionPixelSize(C0247R.dimen.sesl_time_picker_spinner_am_pm_text_size);
            this.mSelectorWheelPaint.setTextSize((float) this.mTextSize);
            this.mInputText.setTextSize(0, (float) this.mTextSize);
            this.mInputText.setAccessibilityDelegate(null);
            setTextTypeface(Typeface.create("sec-roboto-light", 0));
        }
    }

    public boolean getAmPm() {
        return this.mIsAmPm;
    }

    public void setEditTextMode(boolean isEditTextMode) {
        if (this.mIsEditTextMode != isEditTextMode) {
            this.mIsEditTextMode = isEditTextMode;
            if (!this.mIsEditTextMode || this.mIsAmPm) {
                if (this.mFadeOutAnimator.isRunning()) {
                    this.mFadeOutAnimator.cancel();
                }
                if (this.mFadeInAnimator.isRunning()) {
                    this.mFadeInAnimator.cancel();
                }
                this.mAlpha = this.mIdleAlpha;
                this.mInputText.setVisibility(4);
                this.mDelegator.setDescendantFocusability(131072);
            } else {
                tryComputeMaxWidth();
                removeAllCallbacks();
                if (!this.mIsStartingAnimation) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    this.mFlingScroller.abortAnimation();
                    onScrollStateChange(0);
                }
                this.mDelegator.setDescendantFocusability(262144);
                updateInputTextView();
                this.mInputText.setVisibility(0);
                if (this.mAccessibilityManager.isEnabled()) {
                    AccessibilityNodeProviderImpl provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                    if (provider != null) {
                        provider.performAction(2, 128, null);
                    }
                }
            }
            this.mLastFocusedChildVirtualViewId = -1;
            this.mDelegator.invalidate();
            if (this.mOnEditTextModeChangedListener != null) {
                this.mOnEditTextModeChangedListener.onEditTextModeChanged(this.mDelegator, this.mIsEditTextMode);
            }
        }
    }

    public boolean isEditTextMode() {
        return this.mIsEditTextMode;
    }

    public void onWindowVisibilityChanged(int visibility) {
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int msrdWdth = this.mDelegator.getMeasuredWidth();
        int msrdHght = this.mDelegator.getMeasuredHeight();
        int inptTxtMsrdWdth = this.mInputText.getMeasuredWidth();
        int inptTxtMsrdHght = Math.max(this.mInputText.getMeasuredHeight(), (int) Math.floor((double) (((float) msrdHght) * this.mHeightRatio)));
        this.mModifiedTxtHeight = inptTxtMsrdHght;
        int inptTxtLeft = (msrdWdth - inptTxtMsrdWdth) / 2;
        int inptTxtTop = (msrdHght - inptTxtMsrdHght) / 2;
        int inptTxtBottom = inptTxtTop + inptTxtMsrdHght;
        this.mInputText.layout(inptTxtLeft, inptTxtTop, inptTxtLeft + inptTxtMsrdWdth, inptTxtBottom);
        if (changed) {
            initializeSelectorWheel();
            if (this.mModifiedTxtHeight > this.mSelectorElementHeight) {
                this.mTopSelectionDividerTop = this.mValueChangeOffset;
                this.mBottomSelectionDividerBottom = this.mValueChangeOffset * 2;
                return;
            }
            this.mTopSelectionDividerTop = inptTxtTop;
            this.mBottomSelectionDividerBottom = inptTxtBottom;
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mDelegator.superOnMeasure(makeMeasureSpec(widthMeasureSpec, this.mMaxWidth), makeMeasureSpec(heightMeasureSpec, this.mMaxHeight));
        this.mDelegator.setMeasuredDimensionWrapper(resolveSizeAndStateRespectingMinSize(this.mMinWidth, this.mDelegator.getMeasuredWidth(), widthMeasureSpec), resolveSizeAndStateRespectingMinSize(this.mMinHeight, this.mDelegator.getMeasuredHeight(), heightMeasureSpec));
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if ((!this.mInputText.hasFocus() && (!this.mIsAmPm || !this.mDelegator.hasFocus())) || event.getKeyCode() != 4 || event.getAction() != 0) {
            return false;
        }
        hideSoftInput();
        setEditTextMode(false);
        return true;
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        int amountToScroll = scroller.getFinalY() - scroller.getCurrY();
        if (this.mSelectorElementHeight == 0) {
            return false;
        }
        int overshootAdjustment = this.mInitialScrollOffset - (this.mCurrentScrollOffset + amountToScroll);
        if (overshootAdjustment == 0) {
            return false;
        }
        overshootAdjustment %= this.mSelectorElementHeight;
        if (Math.abs(overshootAdjustment) > this.mSelectorElementHeight / 2) {
            if (overshootAdjustment > 0) {
                overshootAdjustment -= this.mSelectorElementHeight;
            } else {
                overshootAdjustment += this.mSelectorElementHeight;
            }
        }
        scrollBy(0, amountToScroll + overshootAdjustment);
        return true;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus && this.mIsEditTextMode && this.mInputText.isFocused()) {
            showSoftInputForWindowFocused();
        }
        if (!this.mIsStartingAnimation) {
            if (!this.mFlingScroller.isFinished()) {
                this.mFlingScroller.forceFinished(true);
            }
            if (!this.mAdjustScroller.isFinished()) {
                this.mAdjustScroller.forceFinished(true);
            }
            ensureScrollWheelAdjusted();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!this.mDelegator.isEnabled() || ((this.mIsEditTextMode && !this.mIsAmPm) || this.mIsStartingAnimation)) {
            return false;
        }
        switch (event.getActionMasked()) {
            case 0:
                removeAllCallbacks();
                this.mInputText.setVisibility(4);
                float y = event.getY();
                this.mLastDownEventY = y;
                this.mLastDownOrMoveEventY = y;
                this.mLastDownEventTime = event.getEventTime();
                this.mIgnoreMoveEvents = false;
                this.mPerformClickOnTap = false;
                this.mIsValueChanged = false;
                if (this.mLastDownEventY < ((float) this.mTopSelectionDividerTop)) {
                    startFadeAnimation(false);
                    if (this.mScrollState == 0) {
                        this.mPressedStateHelper.buttonPressDelayed(2);
                    }
                } else if (this.mLastDownEventY > ((float) this.mBottomSelectionDividerBottom)) {
                    startFadeAnimation(false);
                    if (this.mScrollState == 0) {
                        this.mPressedStateHelper.buttonPressDelayed(1);
                    }
                }
                this.mDelegator.getParent().requestDisallowInterceptTouchEvent(true);
                if (!this.mFlingScroller.isFinished()) {
                    this.mFlingScroller.forceFinished(true);
                    this.mAdjustScroller.forceFinished(true);
                    if (this.mScrollState == 2) {
                        this.mFlingScroller.abortAnimation();
                        this.mAdjustScroller.abortAnimation();
                    }
                    onScrollStateChange(0);
                    return true;
                } else if (!this.mAdjustScroller.isFinished()) {
                    this.mFlingScroller.forceFinished(true);
                    this.mAdjustScroller.forceFinished(true);
                    return true;
                } else if (this.mLastDownEventY < ((float) this.mTopSelectionDividerTop)) {
                    hideSoftInput();
                    postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
                    return true;
                } else if (this.mLastDownEventY > ((float) this.mBottomSelectionDividerBottom)) {
                    hideSoftInput();
                    postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
                    return true;
                } else {
                    this.mPerformClickOnTap = true;
                    postBeginSoftInputOnLongPressCommand();
                    return true;
                }
            default:
                return false;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mDelegator.isEnabled() || ((this.mIsEditTextMode && !this.mIsAmPm) || this.mIsStartingAnimation)) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case 1:
                removeBeginSoftInputCommand();
                removeChangeCurrentByOneFromLongPress();
                this.mPressedStateHelper.cancel();
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                int eventY = (int) event.getY();
                int deltaMoveY = (int) Math.abs(((float) eventY) - this.mLastDownEventY);
                if (this.mIsAmPm && this.mIgnoreMoveEvents) {
                    ensureScrollWheelAdjusted();
                    startFadeAnimation(true);
                    onScrollStateChange(0);
                } else if (Math.abs(initialVelocity) <= this.mMinimumFlingVelocity) {
                    long deltaTime = event.getEventTime() - this.mLastDownEventTime;
                    if (deltaMoveY > this.mTouchSlop || deltaTime >= ((long) ViewConfiguration.getLongPressTimeout())) {
                        ensureScrollWheelAdjusted(deltaMoveY);
                        startFadeAnimation(true);
                    } else if (this.mPerformClickOnTap) {
                        this.mPerformClickOnTap = false;
                        performClick();
                    } else {
                        if (eventY > this.mBottomSelectionDividerBottom) {
                            changeValueByOne(true);
                            this.mPressedStateHelper.buttonTapped(1);
                        } else if (eventY < this.mTopSelectionDividerTop) {
                            changeValueByOne(false);
                            this.mPressedStateHelper.buttonTapped(2);
                        } else {
                            ensureScrollWheelAdjusted(deltaMoveY);
                        }
                        startFadeAnimation(true);
                    }
                    this.mIsValueChanged = false;
                    onScrollStateChange(0);
                } else if (deltaMoveY > this.mTouchSlop || !this.mPerformClickOnTap) {
                    fling(initialVelocity);
                    onScrollStateChange(2);
                    startFadeAnimation(true);
                } else {
                    this.mPerformClickOnTap = false;
                    performClick();
                    onScrollStateChange(0);
                }
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                break;
            case 2:
                if (!this.mIgnoreMoveEvents) {
                    float currentMoveY = event.getY();
                    if (this.mScrollState == 1) {
                        scrollBy(0, (int) (currentMoveY - this.mLastDownOrMoveEventY));
                        this.mDelegator.invalidate();
                    } else if (((int) Math.abs(currentMoveY - this.mLastDownEventY)) > this.mTouchSlop) {
                        removeAllCallbacks();
                        startFadeAnimation(false);
                        onScrollStateChange(1);
                    }
                    this.mLastDownOrMoveEventY = currentMoveY;
                    break;
                }
                break;
            case 3:
                ensureScrollWheelAdjusted();
                startFadeAnimation(true);
                onScrollStateChange(0);
                break;
        }
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 1:
            case 3:
                removeAllCallbacks();
                break;
        }
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        boolean z = false;
        if (!this.mDelegator.isEnabled()) {
            return false;
        }
        if ((this.mIsEditTextMode && !this.mIsAmPm) || this.mIsStartingAnimation || (event.getSource() & 2) == 0) {
            return false;
        }
        switch (event.getAction()) {
            case 8:
                float vscroll = event.getAxisValue(9);
                if (vscroll == 0.0f) {
                    return false;
                }
                startFadeAnimation(false);
                if (vscroll < 0.0f) {
                    z = true;
                }
                changeValueByOne(z);
                startFadeAnimation(true);
                return true;
            default:
                return false;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (!this.mCustomTypefaceSet) {
            if (isCharacterNumberLanguage()) {
                this.mInputText.setIncludeFontPadding(true);
                this.mPickerTypeface = this.mDefaultTypeface;
                this.mInputText.setTypeface(this.mPickerTypeface);
                return;
            }
            this.mInputText.setIncludeFontPadding(false);
            this.mInputText.setTypeface(this.mPickerTypeface);
            tryComputeMaxWidth();
        }
    }

    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        AccessibilityNodeProviderImpl provider;
        if (gainFocus) {
            if (!this.mIsEditTextMode || this.mIsAmPm) {
                this.mLastFocusedChildVirtualViewId = 1;
                if (!this.mWrapSelectorWheel && getValue() == getMinValue()) {
                    this.mLastFocusedChildVirtualViewId = 2;
                }
            } else {
                this.mLastFocusedChildVirtualViewId = -1;
                if (this.mInputText.getVisibility() == 0) {
                    this.mInputText.requestFocus();
                }
            }
            if (this.mAccessibilityManager.isEnabled()) {
                provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                if (provider != null) {
                    if (this.mIsEditTextMode && !this.mIsAmPm) {
                        this.mLastFocusedChildVirtualViewId = 2;
                    }
                    provider.performAction(this.mLastFocusedChildVirtualViewId, 64, null);
                }
            }
        } else {
            if (this.mAccessibilityManager.isEnabled()) {
                provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                if (provider != null) {
                    if (this.mIsEditTextMode && !this.mIsAmPm) {
                        this.mLastFocusedChildVirtualViewId = 2;
                    }
                    provider.performAction(this.mLastFocusedChildVirtualViewId, 128, null);
                }
            }
            this.mLastFocusedChildVirtualViewId = -1;
            this.mLastHoveredChildVirtualViewId = Integer.MIN_VALUE;
        }
        this.mDelegator.invalidate();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case 19:
            case 20:
                if (this.mIsEditTextMode && !this.mIsAmPm) {
                    return false;
                }
                if (action == 0) {
                    if (keyCode == 20) {
                        switch (this.mLastFocusedChildVirtualViewId) {
                            case 1:
                                this.mLastFocusedChildVirtualViewId = 2;
                                this.mDelegator.invalidate();
                                return true;
                            case 2:
                                if (!this.mWrapSelectorWheel && getValue() == getMaxValue()) {
                                    return false;
                                }
                                this.mLastFocusedChildVirtualViewId = 3;
                                this.mDelegator.invalidate();
                                return true;
                            case 3:
                                return false;
                            default:
                                return false;
                        }
                    } else if (keyCode != 19) {
                        return false;
                    } else {
                        switch (this.mLastFocusedChildVirtualViewId) {
                            case 1:
                                return false;
                            case 2:
                                if (!this.mWrapSelectorWheel && getValue() == getMinValue()) {
                                    return false;
                                }
                                this.mLastFocusedChildVirtualViewId = 1;
                                this.mDelegator.invalidate();
                                return true;
                            case 3:
                                this.mLastFocusedChildVirtualViewId = 2;
                                this.mDelegator.invalidate();
                                return true;
                            default:
                                return false;
                        }
                    }
                } else if (action != 1 || !this.mAccessibilityManager.isEnabled()) {
                    return false;
                } else {
                    AccessibilityNodeProviderImpl provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                    if (provider != null) {
                        provider.performAction(this.mLastFocusedChildVirtualViewId, 64, null);
                    }
                    return true;
                }
            case 21:
            case 22:
                if (action != 0) {
                    return false;
                }
                View v;
                switch (keyCode) {
                    case 21:
                        v = this.mDelegator.focusSearch(17);
                        if (v != null) {
                            v.requestFocus(17);
                        }
                        return true;
                    case 22:
                        v = this.mDelegator.focusSearch(66);
                        if (v != null) {
                            v.requestFocus(66);
                        }
                        return true;
                    default:
                        return false;
                }
            case 23:
            case 66:
                if ((this.mIsEditTextMode && !this.mIsAmPm) || action != 0) {
                    return false;
                }
                if (this.mLastFocusedChildVirtualViewId == 2) {
                    if (this.mIsAmPm) {
                        return false;
                    }
                    this.mInputText.setVisibility(0);
                    this.mInputText.requestFocus();
                    showSoftInput();
                    removeAllCallbacks();
                    return false;
                } else if (!this.mFlingScroller.isFinished()) {
                    return false;
                } else {
                    switch (this.mLastFocusedChildVirtualViewId) {
                        case 1:
                            startFadeAnimation(false);
                            changeValueByOne(false);
                            if (!this.mWrapSelectorWheel && getValue() == getMinValue() + 1) {
                                this.mLastFocusedChildVirtualViewId = 2;
                            }
                            startFadeAnimation(true);
                            return false;
                        case 3:
                            startFadeAnimation(false);
                            changeValueByOne(true);
                            if (!this.mWrapSelectorWheel && getValue() == getMaxValue() - 1) {
                                this.mLastFocusedChildVirtualViewId = 2;
                            }
                            startFadeAnimation(true);
                            return false;
                        default:
                            return false;
                    }
                }
            default:
                return false;
        }
    }

    public void dispatchTrackballEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 1:
            case 3:
                removeAllCallbacks();
                return;
            default:
                return;
        }
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        boolean z = true;
        if (!this.mAccessibilityManager.isEnabled()) {
            return false;
        }
        int hoveredVirtualViewId;
        int eventY = (int) event.getY();
        if (this.mIsEditTextMode && !this.mIsAmPm) {
            hoveredVirtualViewId = 2;
        } else if (eventY <= this.mTopSelectionDividerTop) {
            hoveredVirtualViewId = 1;
        } else if (this.mBottomSelectionDividerBottom <= eventY) {
            hoveredVirtualViewId = 3;
        } else {
            hoveredVirtualViewId = 2;
        }
        switch (event.getActionMasked()) {
            case 7:
            case 9:
                updateHoveredVirtualView(hoveredVirtualViewId);
                if (hoveredVirtualViewId == Integer.MIN_VALUE) {
                    z = false;
                }
                return z;
            case 10:
                if (this.mLastHoveredChildVirtualViewId == Integer.MIN_VALUE) {
                    return false;
                }
                updateHoveredVirtualView(Integer.MIN_VALUE);
                return true;
            default:
                return false;
        }
    }

    private void updateHoveredVirtualView(int virtualViewId) {
        if (this.mLastHoveredChildVirtualViewId != virtualViewId) {
            int previousVirtualViewId = this.mLastHoveredChildVirtualViewId;
            this.mLastHoveredChildVirtualViewId = virtualViewId;
            AccessibilityNodeProviderImpl provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
            provider.sendAccessibilityEventForVirtualView(virtualViewId, 128);
            provider.sendAccessibilityEventForVirtualView(previousVirtualViewId, 256);
        }
    }

    public void setSkipValuesOnLongPressEnabled(boolean flag) {
        this.mSkipNumbers = flag;
    }

    private void playSoundAndHapticFeedback() {
        this.mAudioManager.playSoundEffect(this.mPickerSoundIndex);
        if (this.mVibratePermission && !this.mHapticPreDrawListener.mSkipHapticCalls) {
            this.mDelegator.performHapticFeedback(this.mPickerVibrateIndex);
            this.mHapticPreDrawListener.mSkipHapticCalls = true;
        }
    }

    public void computeScroll() {
        Scroller scroller = this.mFlingScroller;
        if (scroller.isFinished()) {
            scroller = this.mAdjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }
        scroller.computeScrollOffset();
        int currentScrollerY = scroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = scroller.getStartY();
        }
        scrollBy(0, currentScrollerY - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currentScrollerY;
        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            this.mDelegator.invalidate();
        }
    }

    public void setEnabled(boolean enabled) {
        this.mInputText.setEnabled(enabled);
        if (!enabled && this.mScrollState != 0) {
            stopScrollAnimation();
            onScrollStateChange(0);
        }
    }

    public void scrollBy(int x, int y) {
        int[] selectorIndices = this.mSelectorIndices;
        if (y != 0 && this.mSelectorElementHeight > 0) {
            if (!this.mWrapSelectorWheel && this.mCurrentScrollOffset + y > this.mInitialScrollOffset && selectorIndices[2] <= this.mMinValue) {
                y = this.mInitialScrollOffset - this.mCurrentScrollOffset;
                if (this.mIsAmPm && this.mLastDownOrMoveEventY > ((float) this.mDelegator.getBottom())) {
                    this.mIgnoreMoveEvents = true;
                    return;
                }
            }
            if (!this.mWrapSelectorWheel && this.mCurrentScrollOffset + y < this.mInitialScrollOffset && selectorIndices[2] >= this.mMaxValue) {
                y = this.mInitialScrollOffset - this.mCurrentScrollOffset;
                if (this.mIsAmPm && this.mLastDownOrMoveEventY < ((float) this.mDelegator.getTop())) {
                    this.mIgnoreMoveEvents = true;
                    return;
                }
            }
            this.mCurrentScrollOffset += y;
            while (this.mCurrentScrollOffset - this.mInitialScrollOffset >= this.mValueChangeOffset) {
                this.mCurrentScrollOffset -= this.mSelectorElementHeight;
                decrementSelectorIndices(selectorIndices);
                if (!this.mIsStartingAnimation) {
                    setValueInternal(selectorIndices[2], true);
                    this.mIsValueChanged = true;
                    if (this.mLongPressCount > 0) {
                        this.mLongPressCount--;
                    } else {
                        playSoundAndHapticFeedback();
                    }
                }
                if (!this.mWrapSelectorWheel && selectorIndices[2] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
            while (this.mCurrentScrollOffset - this.mInitialScrollOffset <= (-this.mValueChangeOffset)) {
                this.mCurrentScrollOffset += this.mSelectorElementHeight;
                incrementSelectorIndices(selectorIndices);
                if (!this.mIsStartingAnimation) {
                    setValueInternal(selectorIndices[2], true);
                    this.mIsValueChanged = true;
                    if (this.mLongPressCount > 0) {
                        this.mLongPressCount--;
                    } else {
                        playSoundAndHapticFeedback();
                    }
                }
                if (!this.mWrapSelectorWheel && selectorIndices[2] >= this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
        }
    }

    public int computeVerticalScrollOffset() {
        return this.mCurrentScrollOffset;
    }

    public int computeVerticalScrollRange() {
        return ((this.mMaxValue - this.mMinValue) + 1) * this.mSelectorElementHeight;
    }

    public int computeVerticalScrollExtent() {
        return this.mDelegator.getHeight();
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        this.mOnValueChangeListener = onValueChangedListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mOnEditTextModeChangedListener = onEditTextModeChangedListener;
    }

    public void setFormatter(Formatter formatter) {
        if (formatter != this.mFormatter) {
            this.mFormatter = formatter;
            initializeSelectorWheelIndices();
            updateInputTextView();
        }
    }

    public void setValue(int value) {
        if (!this.mFlingScroller.isFinished()) {
            stopScrollAnimation();
        }
        setValueInternal(value, false);
    }

    public boolean isEditTextModeNotAmPm() {
        return this.mIsEditTextMode && !this.mIsAmPm;
    }

    public void performClick() {
        if (!this.mIsAmPm) {
            showSoftInput();
        }
    }

    public void performClick(boolean toIncrement) {
        if (this.mIsAmPm) {
            toIncrement = this.mValue != this.mMaxValue;
        }
        changeValueByOne(toIncrement);
    }

    public void performLongClick() {
        this.mIgnoreMoveEvents = true;
        if (!this.mIsAmPm) {
            showSoftInput();
        }
    }

    private void showSoftInputForWindowFocused() {
        this.mDelegator.postDelayed(new C03743(), 20);
    }

    private void showSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        if (inputMethodManager != null) {
            this.mInputText.setVisibility(0);
            this.mInputText.requestFocus();
            inputMethodManager.viewClicked(this.mInputText);
            inputMethodManager.showSoftInput(this.mInputText, 0);
        }
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        if (inputMethodManager != null && inputMethodManager.isActive(this.mInputText)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            this.mInputText.setVisibility(4);
        }
    }

    private void tryComputeMaxWidth() {
        if (this.mComputeMaxWidth) {
            int maxTextWidth = 0;
            int maxTextLength = 0;
            int i;
            if (this.mDisplayedValues == null) {
                float maxDigitWidth = 0.0f;
                for (i = 0; i <= 9; i++) {
                    float digitWidth = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(i));
                    if (digitWidth > maxDigitWidth) {
                        maxDigitWidth = digitWidth;
                    }
                }
                int numberOfDigits = 0;
                for (int current = this.mMaxValue; current > 0; current /= 10) {
                    numberOfDigits++;
                }
                maxTextWidth = (int) (((float) numberOfDigits) * maxDigitWidth);
                maxTextLength = numberOfDigits;
            } else {
                int valueCount = this.mDisplayedValues.length;
                for (i = 0; i < valueCount; i++) {
                    float textWidth = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[i]);
                    if (textWidth > ((float) maxTextWidth)) {
                        maxTextWidth = (int) textWidth;
                        maxTextLength = this.mDisplayedValues[i].length();
                    }
                }
            }
            maxTextWidth += this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight();
            if (SeslViewReflector.isHighContrastTextEnabled(this.mInputText)) {
                maxTextWidth += ((int) Math.ceil((double) (SeslPaintReflector.getHCTStrokeWidth(this.mSelectorWheelPaint) / 2.0f))) * (maxTextLength + 2);
            }
            if (this.mMaxWidth != maxTextWidth) {
                if (maxTextWidth > this.mMinWidth) {
                    this.mMaxWidth = maxTextWidth;
                } else {
                    this.mMaxWidth = this.mMinWidth;
                }
                this.mDelegator.invalidate();
            }
        }
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        boolean wrappingAllowed = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length;
        if ((!wrapSelectorWheel || wrappingAllowed) && wrapSelectorWheel != this.mWrapSelectorWheel) {
            this.mWrapSelectorWheel = wrapSelectorWheel;
        }
    }

    public void setOnLongPressUpdateInterval(long intervalMillis) {
        this.mLongPressUpdateInterval = intervalMillis;
    }

    public int getValue() {
        return this.mValue;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int minValue) {
        if (this.mMinValue != minValue) {
            if (minValue < 0) {
                throw new IllegalArgumentException("minValue must be >= 0");
            }
            this.mMinValue = minValue;
            if (this.mMinValue > this.mValue) {
                this.mValue = this.mMinValue;
            }
            setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
            initializeSelectorWheelIndices();
            updateInputTextView();
            tryComputeMaxWidth();
            this.mDelegator.invalidate();
        }
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        if (this.mMaxValue != maxValue) {
            if (maxValue < 0) {
                throw new IllegalArgumentException("maxValue must be >= 0");
            }
            this.mMaxValue = maxValue;
            if (this.mMaxValue < this.mValue) {
                this.mValue = this.mMaxValue;
            }
            setWrapSelectorWheel(this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
            initializeSelectorWheelIndices();
            updateInputTextView();
            tryComputeMaxWidth();
            this.mDelegator.invalidate();
        }
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public void setDisplayedValues(String[] displayedValues) {
        if (this.mDisplayedValues != displayedValues) {
            this.mDisplayedValues = displayedValues;
            if (this.mDisplayedValues != null) {
                this.mInputText.setRawInputType(524289);
            } else {
                this.mInputText.setRawInputType(2);
            }
            updateInputTextView();
            initializeSelectorWheelIndices();
            tryComputeMaxWidth();
        }
    }

    public void setTextSize(float size) {
        this.mTextSize = (int) TypedValue.applyDimension(1, size, this.mContext.getResources().getDisplayMetrics());
        this.mSelectorWheelPaint.setTextSize((float) this.mTextSize);
        this.mInputText.setTextSize(0, (float) this.mTextSize);
        tryComputeMaxWidth();
    }

    public void setSubTextSize(float size) {
    }

    public void setTextTypeface(Typeface typeface) {
        this.mCustomTypefaceSet = true;
        this.mPickerTypeface = typeface;
        this.mSelectorWheelPaint.setTypeface(this.mPickerTypeface);
        this.mInputText.setTypeface(this.mPickerTypeface);
        tryComputeMaxWidth();
    }

    private static Typeface getFontTypeface(String ft) {
        if (!new File(ft).exists()) {
            return null;
        }
        try {
            return Typeface.createFromFile(ft);
        } catch (Exception e) {
            return null;
        }
    }

    public void startAnimation(int delayTime, SeslAnimationListener listener) {
        this.mAnimationListener = listener;
        this.mAlpha = this.mActivatedAlpha;
        if (!this.mIsEditTextMode) {
            final int delay = delayTime;
            this.mDelegator.post(new Runnable() {
                public void run() {
                    if (SeslNumberPickerSpinnerDelegate.this.mSelectorElementHeight == 0) {
                        SeslNumberPickerSpinnerDelegate.this.mReservedStartAnimation = true;
                        return;
                    }
                    SeslNumberPickerSpinnerDelegate.this.mIsStartingAnimation = true;
                    SeslNumberPickerSpinnerDelegate.this.mFlingScroller = SeslNumberPickerSpinnerDelegate.this.mCustomScroller;
                    int ampmDistance = SeslNumberPickerSpinnerDelegate.this.getValue() != SeslNumberPickerSpinnerDelegate.this.getMinValue() ? SeslNumberPickerSpinnerDelegate.this.mSelectorElementHeight : -SeslNumberPickerSpinnerDelegate.this.mSelectorElementHeight;
                    int backwardDistance = SeslNumberPickerSpinnerDelegate.this.mIsAmPm ? ampmDistance : SeslNumberPickerSpinnerDelegate.this.mSelectorElementHeight * 5;
                    final int forwardDistance = SeslNumberPickerSpinnerDelegate.this.mIsAmPm ? ampmDistance : (int) (((double) SeslNumberPickerSpinnerDelegate.this.mSelectorElementHeight) * 5.4d);
                    SeslNumberPickerSpinnerDelegate.this.scrollBy(0, backwardDistance);
                    SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
                    new Handler().postDelayed(new Runnable() {

                        /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$4$1$1 */
                        class C03761 implements Runnable {

                            /* renamed from: android.support.v7.widget.SeslNumberPickerSpinnerDelegate$4$1$1$1 */
                            class C03751 implements Runnable {
                                C03751() {
                                }

                                public void run() {
                                    SeslNumberPickerSpinnerDelegate.this.moveToFinalScrollerPosition(SeslNumberPickerSpinnerDelegate.this.mFlingScroller);
                                    SeslNumberPickerSpinnerDelegate.this.mFlingScroller.abortAnimation();
                                    SeslNumberPickerSpinnerDelegate.this.mAdjustScroller.abortAnimation();
                                    SeslNumberPickerSpinnerDelegate.this.ensureScrollWheelAdjusted();
                                    SeslNumberPickerSpinnerDelegate.this.mFlingScroller = SeslNumberPickerSpinnerDelegate.this.mLinearScroller;
                                    SeslNumberPickerSpinnerDelegate.this.mIsStartingAnimation = false;
                                    SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
                                    SeslNumberPickerSpinnerDelegate.this.startFadeAnimation(true);
                                    if (SeslNumberPickerSpinnerDelegate.this.mAnimationListener != null) {
                                        SeslNumberPickerSpinnerDelegate.this.mAnimationListener.onAnimationEnd();
                                    }
                                }
                            }

                            C03761() {
                            }

                            public void run() {
                                if (!SeslNumberPickerSpinnerDelegate.this.moveToFinalScrollerPosition(SeslNumberPickerSpinnerDelegate.this.mFlingScroller)) {
                                    SeslNumberPickerSpinnerDelegate.this.moveToFinalScrollerPosition(SeslNumberPickerSpinnerDelegate.this.mAdjustScroller);
                                }
                                SeslNumberPickerSpinnerDelegate.this.mPreviousScrollerY = 0;
                                SeslNumberPickerSpinnerDelegate.this.mFlingScroller.startScroll(0, 0, 0, -forwardDistance, SeslNumberPickerSpinnerDelegate.this.mIsAmPm ? SeslNumberPickerSpinnerDelegate.START_ANIMATION_SCROLL_DURATION : SeslNumberPickerSpinnerDelegate.START_ANIMATION_SCROLL_DURATION_2016B);
                                SeslNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
                                new Handler().postDelayed(new C03751(), 857);
                            }
                        }

                        public void run() {
                            new Handler().postDelayed(new C03761(), 100);
                        }
                    }, (long) delay);
                }
            });
        }
    }

    private void stopScrollAnimation() {
        this.mFlingScroller.abortAnimation();
        this.mAdjustScroller.abortAnimation();
        if (!(this.mIsStartingAnimation || moveToFinalScrollerPosition(this.mFlingScroller))) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        ensureScrollWheelAdjusted();
    }

    private void startFadeAnimation(boolean isFadeOut) {
        if (isFadeOut) {
            this.mFadeOutAnimator.setStartDelay((long) (this.mFlingScroller.getDuration() + SNAP_SCROLL_DURATION));
            this.mFadeOutAnimator.start();
            return;
        }
        this.mFadeInAnimator.setFloatValues(new float[]{this.mAlpha, this.mActivatedAlpha});
        this.mFadeOutAnimator.cancel();
        this.mFadeInAnimator.start();
    }

    public void onDetachedFromWindow() {
        removeAllCallbacks();
        this.mDelegator.getViewTreeObserver().removeOnPreDrawListener(this.mHapticPreDrawListener);
    }

    public void onAttachedToWindow() {
        this.mDelegator.getViewTreeObserver().addOnPreDrawListener(this.mHapticPreDrawListener);
    }

    public void onDraw(Canvas canvas) {
        int right = this.mDelegator.getRight();
        int left = this.mDelegator.getLeft();
        int bottom = this.mDelegator.getBottom();
        float x = ((float) (right - left)) / 2.0f;
        float y = (float) (this.mCurrentScrollOffset - this.mSelectorElementHeight);
        if (this.mVirtualButtonFocusedDrawable != null && this.mScrollState == 0) {
            switch (this.mLastFocusedChildVirtualViewId) {
                case 1:
                    this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                    this.mVirtualButtonFocusedDrawable.setBounds(0, 0, right, this.mTopSelectionDividerTop);
                    this.mVirtualButtonFocusedDrawable.draw(canvas);
                    break;
                case 2:
                    this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                    this.mVirtualButtonFocusedDrawable.setBounds(0, this.mTopSelectionDividerTop, right, this.mBottomSelectionDividerBottom);
                    this.mVirtualButtonFocusedDrawable.draw(canvas);
                    break;
                case 3:
                    this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                    this.mVirtualButtonFocusedDrawable.setBounds(0, this.mBottomSelectionDividerBottom, right, bottom);
                    this.mVirtualButtonFocusedDrawable.draw(canvas);
                    break;
            }
        }
        int[] selectorIndices = this.mSelectorIndices;
        for (int selectorIndex : selectorIndices) {
            String scrollSelectorValue = (String) this.mSelectorIndexToStringCache.get(selectorIndex);
            float alpha = this.mAlpha;
            if (alpha < this.mIdleAlpha) {
                alpha = this.mIdleAlpha;
            }
            int yPos = (int) ((((this.mSelectorWheelPaint.descent() - this.mSelectorWheelPaint.ascent()) / 2.0f) + y) - this.mSelectorWheelPaint.descent());
            if (y < ((float) (this.mTopSelectionDividerTop - this.mInitialScrollOffset)) || y > ((float) (this.mBottomSelectionDividerBottom + this.mInitialScrollOffset))) {
                canvas.save();
                this.mSelectorWheelPaint.setAlpha((int) (255.0f * alpha));
                canvas.drawText(scrollSelectorValue, x, (float) yPos, this.mSelectorWheelPaint);
                canvas.restore();
            } else if (y <= ((float) (this.mTopSelectionDividerTop + this.mBottomSelectionDividerBottom)) / 2.0f) {
                canvas.save();
                canvas.clipRect(0, this.mTopSelectionDividerTop, right, this.mBottomSelectionDividerBottom);
                this.mSelectorWheelPaint.setColor(this.mTextColor);
                canvas.drawText(scrollSelectorValue, x, (float) yPos, this.mSelectorWheelPaint);
                canvas.restore();
                canvas.save();
                canvas.clipRect(0, 0, right, this.mTopSelectionDividerTop);
                this.mSelectorWheelPaint.setAlpha((int) (255.0f * alpha));
                canvas.drawText(scrollSelectorValue, x, (float) yPos, this.mSelectorWheelPaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.clipRect(0, this.mTopSelectionDividerTop, right, this.mBottomSelectionDividerBottom);
                this.mSelectorWheelPaint.setColor(this.mTextColor);
                canvas.drawText(scrollSelectorValue, x, (float) yPos, this.mSelectorWheelPaint);
                canvas.restore();
                canvas.save();
                canvas.clipRect(0, this.mBottomSelectionDividerBottom, right, bottom);
                this.mSelectorWheelPaint.setAlpha((int) (255.0f * alpha));
                canvas.drawText(scrollSelectorValue, x, (float) yPos, this.mSelectorWheelPaint);
                canvas.restore();
            }
            y += (float) this.mSelectorElementHeight;
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(NumberPicker.class.getName());
        event.setScrollable(true);
        event.setScrollY((this.mMinValue + this.mValue) * this.mSelectorElementHeight);
        event.setMaxScrollY((this.mMaxValue - this.mMinValue) * this.mSelectorElementHeight);
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (this.mAccessibilityNodeProvider == null) {
            this.mAccessibilityNodeProvider = new AccessibilityNodeProviderImpl();
        }
        return this.mAccessibilityNodeProvider;
    }

    private int makeMeasureSpec(int measureSpec, int maxSize) {
        if (maxSize == -1) {
            return measureSpec;
        }
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        switch (mode) {
            case Integer.MIN_VALUE:
                return MeasureSpec.makeMeasureSpec(Math.min(size, maxSize), 1073741824);
            case 0:
                return MeasureSpec.makeMeasureSpec(maxSize, 1073741824);
            case 1073741824:
                return measureSpec;
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
    }

    private int resolveSizeAndStateRespectingMinSize(int minSize, int measuredSize, int measureSpec) {
        if (minSize != -1) {
            return View.resolveSizeAndState(Math.max(minSize, measuredSize), measureSpec, 0);
        }
        return measuredSize;
    }

    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] selectorIndices = this.mSelectorIndices;
        int current = getValue();
        for (int i = 0; i < this.mSelectorIndices.length; i++) {
            int selectorIndex = current + (i - 2);
            if (this.mWrapSelectorWheel) {
                selectorIndex = getWrappedSelectorIndex(selectorIndex);
            }
            selectorIndices[i] = selectorIndex;
            ensureCachedScrollSelectorValue(selectorIndices[i]);
        }
    }

    private void setValueInternal(int current, boolean notifyChange) {
        if (this.mValue != current) {
            if (this.mWrapSelectorWheel) {
                current = getWrappedSelectorIndex(current);
            } else {
                current = Math.min(Math.max(current, this.mMinValue), this.mMaxValue);
            }
            int previous = this.mValue;
            this.mValue = current;
            updateInputTextView();
            if (notifyChange) {
                notifyChange(previous, current);
            }
            initializeSelectorWheelIndices();
            this.mDelegator.invalidate();
        } else if (isCharacterNumberLanguage()) {
            updateInputTextView();
            this.mDelegator.invalidate();
        }
    }

    private void changeValueByOne(boolean increment) {
        this.mInputText.setVisibility(4);
        if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        this.mPreviousScrollerY = 0;
        this.mChangeValueBy = 1;
        if (this.mLongPressed_FIRST_SCROLL) {
            this.mLongPressed_FIRST_SCROLL = false;
            this.mLongPressed_SECOND_SCROLL = true;
        } else if (this.mLongPressed_SECOND_SCROLL) {
            this.mLongPressed_SECOND_SCROLL = false;
            this.mLongPressed_THIRD_SCROLL = true;
            if (getValue() % 10 == 0) {
                this.mChangeValueBy = 10;
            } else if (increment) {
                this.mChangeValueBy = 10 - (getValue() % 10);
            } else {
                this.mChangeValueBy = getValue() % 10;
            }
        } else if (this.mLongPressed_THIRD_SCROLL) {
            this.mChangeValueBy = 10;
        }
        int duration = SNAP_SCROLL_DURATION;
        if (this.mIsLongPressed && this.mSkipNumbers) {
            duration = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
            this.mLongPressUpdateInterval = 600;
        } else if (this.mIsLongPressed) {
            duration = 100;
            this.mChangeValueBy = 1;
            this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        }
        this.mLongPressCount = this.mChangeValueBy - 1;
        if (increment) {
            this.mFlingScroller.startScroll(0, 0, 0, (-this.mSelectorElementHeight) * this.mChangeValueBy, duration);
        } else {
            this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight * this.mChangeValueBy, duration);
        }
        this.mDelegator.invalidate();
    }

    private void initializeSelectorWheel() {
        if (this.mIsStartingAnimation) {
            if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
                moveToFinalScrollerPosition(this.mAdjustScroller);
            }
            stopScrollAnimation();
        }
        if (!this.mIsStartingAnimation) {
            initializeSelectorWheelIndices();
        }
        this.mSelectorTextGapHeight = (int) ((((float) ((this.mDelegator.getBottom() - this.mDelegator.getTop()) - (3 * this.mTextSize))) / ((float) 3)) + 0.5f);
        this.mSelectorElementHeight = this.mTextSize + this.mSelectorTextGapHeight;
        int height = (this.mModifiedTxtHeight > this.mSelectorElementHeight || this.mIsAmPm) ? this.mDelegator.getHeight() / 3 : this.mModifiedTxtHeight;
        this.mValueChangeOffset = height;
        this.mInitialScrollOffset = (this.mInputText.getTop() + (this.mModifiedTxtHeight / 2)) - this.mSelectorElementHeight;
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        ((CustomEditText) this.mInputText).setEditTextPosition(((int) (((this.mSelectorWheelPaint.descent() - this.mSelectorWheelPaint.ascent()) / 2.0f) - this.mSelectorWheelPaint.descent())) - (this.mInputText.getBaseline() - (this.mModifiedTxtHeight / 2)));
        if (this.mReservedStartAnimation) {
            startAnimation(0, this.mAnimationListener);
            this.mReservedStartAnimation = false;
        }
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            if (!ensureScrollWheelAdjusted()) {
                updateInputTextView();
            }
            onScrollStateChange(0);
        } else if (this.mScrollState != 1) {
            updateInputTextView();
        }
    }

    private void onScrollStateChange(int scrollState) {
        if (this.mScrollState != scrollState) {
            this.mScrollState = scrollState;
            if (this.mOnScrollListener != null) {
                this.mOnScrollListener.onScrollStateChange(this.mDelegator, scrollState);
            }
        }
    }

    private void fling(int velocityY) {
        int max;
        this.mPreviousScrollerY = 0;
        float velocityRatio = ((float) Math.abs(velocityY)) / ((float) this.mMaximumFlingVelocity);
        this.mFlingScroller.setFriction(velocityRatio * ViewConfiguration.getScrollFriction());
        this.mFlingScroller.fling(0, this.mCurrentScrollOffset, 0, Math.round(((float) velocityY) * velocityRatio), 0, 0, Integer.MIN_VALUE, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        int distance = (Math.round(((float) this.mFlingScroller.getFinalY()) / ((float) this.mSelectorElementHeight)) * this.mSelectorElementHeight) + this.mInitialScrollOffset;
        Scroller scroller = this.mFlingScroller;
        if (velocityY > 0) {
            max = Math.max(distance, this.mSelectorElementHeight + this.mInitialScrollOffset);
        } else {
            max = Math.min(distance, (-this.mSelectorElementHeight) + this.mInitialScrollOffset);
        }
        scroller.setFinalY(max);
        this.mDelegator.invalidate();
    }

    private int getWrappedSelectorIndex(int selectorIndex) {
        if (selectorIndex > this.mMaxValue) {
            return this.mMinValue + ((selectorIndex - this.mMinValue) % ((this.mMaxValue - this.mMinValue) + 1));
        }
        if (selectorIndex < this.mMinValue) {
            return this.mMaxValue - ((this.mMaxValue - selectorIndex) % ((this.mMaxValue - this.mMinValue) + 1));
        }
        return selectorIndex;
    }

    private void incrementSelectorIndices(int[] selectorIndices) {
        System.arraycopy(selectorIndices, 1, selectorIndices, 0, selectorIndices.length - 1);
        int nextScrollSelectorIndex = selectorIndices[selectorIndices.length - 2] + 1;
        if (this.mWrapSelectorWheel && nextScrollSelectorIndex > this.mMaxValue) {
            nextScrollSelectorIndex = this.mMinValue;
        }
        selectorIndices[selectorIndices.length - 1] = nextScrollSelectorIndex;
        ensureCachedScrollSelectorValue(nextScrollSelectorIndex);
    }

    private void decrementSelectorIndices(int[] selectorIndices) {
        System.arraycopy(selectorIndices, 0, selectorIndices, 1, selectorIndices.length - 1);
        int nextScrollSelectorIndex = selectorIndices[1] - 1;
        if (this.mWrapSelectorWheel && nextScrollSelectorIndex < this.mMinValue) {
            nextScrollSelectorIndex = this.mMaxValue;
        }
        selectorIndices[0] = nextScrollSelectorIndex;
        ensureCachedScrollSelectorValue(nextScrollSelectorIndex);
    }

    private void ensureCachedScrollSelectorValue(int selectorIndex) {
        SparseArray<String> cache = this.mSelectorIndexToStringCache;
        if (((String) cache.get(selectorIndex)) == null) {
            String scrollSelectorValue;
            if (selectorIndex < this.mMinValue || selectorIndex > this.mMaxValue) {
                scrollSelectorValue = "";
            } else if (this.mDisplayedValues != null) {
                scrollSelectorValue = this.mDisplayedValues[selectorIndex - this.mMinValue];
            } else {
                scrollSelectorValue = formatNumber(selectorIndex);
            }
            cache.put(selectorIndex, scrollSelectorValue);
        }
    }

    private String formatNumber(int value) {
        return this.mFormatter != null ? this.mFormatter.format(value) : formatNumberWithLocale(value);
    }

    private void validateInputTextView(View v) {
        String str = String.valueOf(((TextView) v).getText());
        int current = getSelectedPos(str);
        if (TextUtils.isEmpty(str) || this.mValue == current) {
            updateInputTextView();
        } else {
            setValueInternal(current, true);
        }
    }

    private boolean updateInputTextView() {
        String text = this.mDisplayedValues == null ? formatNumber(this.mValue) : this.mDisplayedValues[this.mValue - this.mMinValue];
        if (TextUtils.isEmpty(text) || text.equals(this.mInputText.getText().toString())) {
            return false;
        }
        this.mInputText.setText(text);
        Selection.setSelection(this.mInputText.getText(), this.mInputText.getText().length());
        return true;
    }

    private void notifyChange(int previous, int current) {
        if (this.mAccessibilityManager.isEnabled() && !this.mIsStartingAnimation) {
            int value = getWrappedSelectorIndex(this.mValue);
            String text = null;
            if (value <= this.mMaxValue) {
                text = this.mDisplayedValues == null ? formatNumber(value) : this.mDisplayedValues[value - this.mMinValue];
            }
            this.mDelegator.announceForAccessibility(text);
            if (this.mIsAmPm) {
                AccessibilityNodeProviderImpl provider = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                if (provider != null) {
                    provider.performAction(2, 64, null);
                }
            }
        }
        if (this.mOnValueChangeListener != null) {
            this.mOnValueChangeListener.onValueChange(this.mDelegator, previous, this.mValue);
        }
    }

    private void postChangeCurrentByOneFromLongPress(boolean increment, long delayMillis) {
        if (this.mChangeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            this.mDelegator.removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
        this.mIsLongPressed = true;
        this.mLongPressed_FIRST_SCROLL = true;
        this.mChangeCurrentByOneFromLongPressCommand.setStep(increment);
        this.mDelegator.postDelayed(this.mChangeCurrentByOneFromLongPressCommand, delayMillis);
    }

    private void removeChangeCurrentByOneFromLongPress() {
        if (this.mIsLongPressed) {
            this.mIsLongPressed = false;
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
        this.mLongPressed_FIRST_SCROLL = false;
        this.mLongPressed_SECOND_SCROLL = false;
        this.mLongPressed_THIRD_SCROLL = false;
        this.mChangeValueBy = 1;
        this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        if (this.mChangeCurrentByOneFromLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
    }

    private void postBeginSoftInputOnLongPressCommand() {
        if (this.mBeginSoftInputOnLongPressCommand == null) {
            this.mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
        } else {
            this.mDelegator.removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
        }
        this.mDelegator.postDelayed(this.mBeginSoftInputOnLongPressCommand, (long) ViewConfiguration.getLongPressTimeout());
    }

    private void removeBeginSoftInputCommand() {
        if (this.mBeginSoftInputOnLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
        }
    }

    private void removeAllCallbacks() {
        if (this.mIsLongPressed) {
            this.mIsLongPressed = false;
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
        this.mLongPressed_FIRST_SCROLL = false;
        this.mLongPressed_SECOND_SCROLL = false;
        this.mLongPressed_THIRD_SCROLL = false;
        this.mChangeValueBy = 1;
        this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        if (this.mChangeCurrentByOneFromLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
        if (this.mBeginSoftInputOnLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }

    private int getSelectedPos(String value) {
        if (this.mDisplayedValues == null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return this.mMinValue;
            }
        }
        for (int i = 0; i < this.mDisplayedValues.length; i++) {
            value = value.toLowerCase();
            if (this.mDisplayedValues[i].toLowerCase().startsWith(value)) {
                return this.mMinValue + i;
            }
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e2) {
            return this.mMinValue;
        }
    }

    private boolean ensureScrollWheelAdjusted() {
        return ensureScrollWheelAdjusted(0);
    }

    private boolean ensureScrollWheelAdjusted(int distance) {
        if (this.mInitialScrollOffset == Integer.MIN_VALUE) {
            return false;
        }
        int deltaY = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (deltaY != 0) {
            this.mPreviousScrollerY = 0;
            if (!this.mIsValueChanged && distance != 0 && Math.abs(distance) < this.mSelectorElementHeight) {
                deltaY += deltaY > 0 ? -this.mSelectorElementHeight : this.mSelectorElementHeight;
            } else if (Math.abs(deltaY) > this.mSelectorElementHeight / 2) {
                deltaY += deltaY > 0 ? -this.mSelectorElementHeight : this.mSelectorElementHeight;
            }
            this.mAdjustScroller.startScroll(0, 0, 0, deltaY, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
            this.mDelegator.invalidate();
            this.mIsValueChanged = false;
            return true;
        }
        this.mIsValueChanged = false;
        return false;
    }

    private static String formatNumberWithLocale(int value) {
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(value)});
    }

    public void setMaxInputLength(int limit) {
        InputFilter backupFilter = this.mInputText.getFilters()[0];
        InputFilter lengthFilter = new LengthFilter(limit);
        this.mInputText.setFilters(new InputFilter[]{backupFilter, lengthFilter});
    }

    public EditText getEditText() {
        return this.mInputText;
    }

    public void setMonthInputMode() {
        this.mInputText.setImeOptions(33554432);
        this.mInputText.setPrivateImeOptions(INPUT_TYPE_MONTH);
        this.mInputText.setText("");
    }

    public void setYearDateTimeInputMode() {
        this.mInputText.setImeOptions(33554432);
        this.mInputText.setPrivateImeOptions(INPUT_TYPE_YEAR_DATE_TIME);
        this.mInputText.setText("");
    }

    private boolean isCharacterNumberLanguage() {
        String language = Locale.getDefault().getLanguage();
        return "ar".equals(language) || "fa".equals(language) || "my".equals(language);
    }

    private boolean needCompareEqualMonthLanguage() {
        return "vi".equals(Locale.getDefault().getLanguage());
    }

    public int getMinWidth() {
        return 0;
    }

    public int getMinHeight() {
        return 0;
    }

    public int getMaxWidth() {
        return 0;
    }

    public int getMaxHeight() {
        return 0;
    }
}
