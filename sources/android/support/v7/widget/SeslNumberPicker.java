package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.icu.SeslLocaleDataReflector;
import android.support.v4.view.SeslViewReflector;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class SeslNumberPicker extends LinearLayout {
    private static final TwoDigitFormatter sTwoDigitFormatter = new TwoDigitFormatter();
    private SeslNumberPickerDelegate mDelegate;

    public interface OnEditTextModeChangedListener {
        void onEditTextModeChanged(SeslNumberPicker seslNumberPicker, boolean z);
    }

    public interface OnValueChangeListener {
        void onValueChange(SeslNumberPicker seslNumberPicker, int i, int i2);
    }

    interface SeslNumberPickerDelegate {
        void computeScroll();

        int computeVerticalScrollExtent();

        int computeVerticalScrollOffset();

        int computeVerticalScrollRange();

        boolean dispatchHoverEvent(MotionEvent motionEvent);

        boolean dispatchKeyEvent(KeyEvent keyEvent);

        boolean dispatchKeyEventPreIme(KeyEvent keyEvent);

        boolean dispatchTouchEvent(MotionEvent motionEvent);

        void dispatchTrackballEvent(MotionEvent motionEvent);

        AccessibilityNodeProvider getAccessibilityNodeProvider();

        boolean getAmPm();

        String[] getDisplayedValues();

        EditText getEditText();

        int getMaxHeight();

        int getMaxValue();

        int getMaxWidth();

        int getMinHeight();

        int getMinValue();

        int getMinWidth();

        int getValue();

        boolean getWrapSelectorWheel();

        boolean isEditTextMode();

        boolean isEditTextModeNotAmPm();

        void onAttachedToWindow();

        void onConfigurationChanged(Configuration configuration);

        void onDetachedFromWindow();

        void onDraw(Canvas canvas);

        void onFocusChanged(boolean z, int i, Rect rect);

        boolean onGenericMotionEvent(MotionEvent motionEvent);

        void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        boolean onInterceptTouchEvent(MotionEvent motionEvent);

        void onLayout(boolean z, int i, int i2, int i3, int i4);

        void onMeasure(int i, int i2);

        boolean onTouchEvent(MotionEvent motionEvent);

        void onWindowFocusChanged(boolean z);

        void onWindowVisibilityChanged(int i);

        void performClick();

        void performClick(boolean z);

        void performLongClick();

        void scrollBy(int i, int i2);

        void setAmPm(boolean z);

        void setDisplayedValues(String[] strArr);

        void setEditTextMode(boolean z);

        void setEnabled(boolean z);

        void setFormatter(Formatter formatter);

        void setImeOptions(int i);

        void setMaxInputLength(int i);

        void setMaxValue(int i);

        void setMinValue(int i);

        void setMonthInputMode();

        void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener);

        void setOnLongPressUpdateInterval(long j);

        void setOnScrollListener(OnScrollListener onScrollListener);

        void setOnValueChangedListener(OnValueChangeListener onValueChangeListener);

        void setPickerContentDescription(String str);

        void setSkipValuesOnLongPressEnabled(boolean z);

        void setSubTextSize(float f);

        void setTextSize(float f);

        void setTextTypeface(Typeface typeface);

        void setValue(int i);

        void setWrapSelectorWheel(boolean z);

        void setYearDateTimeInputMode();

        void startAnimation(int i, SeslAnimationListener seslAnimationListener);
    }

    static abstract class AbstractSeslNumberPickerDelegate implements SeslNumberPickerDelegate {
        protected Context mContext;
        protected SeslNumberPicker mDelegator;

        public AbstractSeslNumberPickerDelegate(SeslNumberPicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
        }
    }

    public static class CustomEditText extends EditText {
        private int mAdjustEditTextPosition;
        private String mPickerContentDescription = "";

        public CustomEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setPickerContentDescription(String name) {
            this.mPickerContentDescription = name;
        }

        public void setEditTextPosition(int pixel) {
            this.mAdjustEditTextPosition = pixel;
        }

        public void onEditorAction(int actionCode) {
            super.onEditorAction(actionCode);
            if (actionCode == 6) {
                clearFocus();
            }
        }

        public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
            CharSequence text;
            int oldSize = event.getText().size();
            super.onPopulateAccessibilityEvent(event);
            int newSize = event.getText().size();
            if (newSize > oldSize) {
                event.getText().remove(newSize - 1);
            }
            int eventType = event.getEventType();
            if (eventType == 16 || eventType == 8192) {
                text = getText();
            } else {
                text = getTextForAccessibility();
            }
            if (!TextUtils.isEmpty(text)) {
                event.getText().add(text);
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setText(getTextForAccessibility());
        }

        private CharSequence getTextForAccessibility() {
            CharSequence text = getText();
            if (this.mPickerContentDescription.equals("")) {
                return text;
            }
            if (TextUtils.isEmpty(text)) {
                return ", " + this.mPickerContentDescription;
            }
            return text.toString() + ", " + this.mPickerContentDescription;
        }

        protected void onDraw(Canvas canvas) {
            canvas.translate(0.0f, (float) this.mAdjustEditTextPosition);
            super.onDraw(canvas);
        }
    }

    public interface Formatter {
        String format(int i);
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ScrollState {
        }

        void onScrollStateChange(SeslNumberPicker seslNumberPicker, int i);
    }

    private static class TwoDigitFormatter implements Formatter {
        final Object[] mArgs = new Object[1];
        final StringBuilder mBuilder = new StringBuilder();
        java.util.Formatter mFmt;
        char mZeroDigit;

        TwoDigitFormatter() {
            init(Locale.getDefault());
        }

        private void init(Locale locale) {
            this.mFmt = createFormatter(locale);
            this.mZeroDigit = getZeroDigit(locale);
        }

        public String format(int value) {
            Locale currentLocale = Locale.getDefault();
            if (this.mZeroDigit != getZeroDigit(currentLocale)) {
                init(currentLocale);
            }
            this.mArgs[0] = Integer.valueOf(value);
            this.mBuilder.delete(0, this.mBuilder.length());
            this.mFmt.format("%02d", this.mArgs);
            return this.mFmt.toString();
        }

        private static char getZeroDigit(Locale locale) {
            Object localeData = SeslLocaleDataReflector.get(locale);
            if (localeData == null) {
                return '0';
            }
            return SeslLocaleDataReflector.getField_zeroDigit(localeData);
        }

        private java.util.Formatter createFormatter(Locale locale) {
            return new java.util.Formatter(this.mBuilder, locale);
        }
    }

    public static final Formatter getTwoDigitFormatter() {
        return sTwoDigitFormatter;
    }

    public SeslNumberPicker(Context context) {
        this(context, null);
    }

    public SeslNumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mDelegate = new SeslNumberPickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    public void setPickerContentDescription(String name) {
        this.mDelegate.setPickerContentDescription(name);
    }

    public void setImeOptions(int imeOptions) {
        this.mDelegate.setImeOptions(imeOptions);
    }

    public void setAmPm(boolean value) {
        this.mDelegate.setAmPm(value);
    }

    public boolean getAmPm() {
        return this.mDelegate.getAmPm();
    }

    public void setEditTextMode(boolean isEditTextMode) {
        this.mDelegate.setEditTextMode(isEditTextMode);
    }

    public boolean isEditTextMode() {
        return this.mDelegate.isEditTextMode();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        this.mDelegate.onWindowVisibilityChanged(visibility);
        super.onWindowVisibilityChanged(visibility);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mDelegate.onLayout(changed, left, top, right, bottom);
    }

    void superOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMeasuredDimensionWrapper(int measuredWidth, int measuredHeight) {
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mDelegate.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (this.mDelegate.dispatchKeyEventPreIme(event)) {
            return true;
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.mDelegate.onWindowFocusChanged(hasWindowFocus);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.mDelegate.onInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mDelegate.onTouchEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        this.mDelegate.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (this.mDelegate.onGenericMotionEvent(event)) {
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDelegate.onConfigurationChanged(newConfig);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mDelegate.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mDelegate.dispatchKeyEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        this.mDelegate.dispatchTrackballEvent(event);
        return super.dispatchTrackballEvent(event);
    }

    protected boolean dispatchHoverEvent(MotionEvent event) {
        if (this.mDelegate.isEditTextModeNotAmPm()) {
            return super.dispatchHoverEvent(event);
        }
        return this.mDelegate.dispatchHoverEvent(event);
    }

    public void setSkipValuesOnLongPressEnabled(boolean enabled) {
        this.mDelegate.setSkipValuesOnLongPressEnabled(enabled);
    }

    public void computeScroll() {
        this.mDelegate.computeScroll();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mDelegate.setEnabled(enabled);
    }

    public void scrollBy(int x, int y) {
        this.mDelegate.scrollBy(x, y);
    }

    protected int computeVerticalScrollOffset() {
        return this.mDelegate.computeVerticalScrollOffset();
    }

    protected int computeVerticalScrollRange() {
        return this.mDelegate.computeVerticalScrollRange();
    }

    protected int computeVerticalScrollExtent() {
        return this.mDelegate.computeVerticalScrollExtent();
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangedListener) {
        this.mDelegate.setOnValueChangedListener(onValueChangedListener);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mDelegate.setOnScrollListener(onScrollListener);
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mDelegate.setOnEditTextModeChangedListener(onEditTextModeChangedListener);
    }

    public void setFormatter(Formatter formatter) {
        this.mDelegate.setFormatter(formatter);
    }

    public void setValue(int value) {
        this.mDelegate.setValue(value);
    }

    public boolean performClick() {
        if (this.mDelegate.isEditTextModeNotAmPm()) {
            return super.performClick();
        }
        if (!super.performClick()) {
            this.mDelegate.performClick();
        }
        return true;
    }

    public void performClick(boolean toIncrement) {
        this.mDelegate.performClick(toIncrement);
    }

    public boolean performLongClick() {
        if (!super.performLongClick()) {
            this.mDelegate.performLongClick();
        }
        return true;
    }

    public boolean getWrapSelectorWheel() {
        return this.mDelegate.getWrapSelectorWheel();
    }

    public void setWrapSelectorWheel(boolean wrapSelectorWheel) {
        this.mDelegate.setWrapSelectorWheel(wrapSelectorWheel);
    }

    public void setOnLongPressUpdateInterval(long intervalMillis) {
        this.mDelegate.setOnLongPressUpdateInterval(intervalMillis);
    }

    public int getValue() {
        return this.mDelegate.getValue();
    }

    public int getMinValue() {
        return this.mDelegate.getMinValue();
    }

    public void setMinValue(int minValue) {
        this.mDelegate.setMinValue(minValue);
    }

    public int getMaxValue() {
        return this.mDelegate.getMaxValue();
    }

    public void setMaxValue(int maxValue) {
        this.mDelegate.setMaxValue(maxValue);
    }

    public String[] getDisplayedValues() {
        return this.mDelegate.getDisplayedValues();
    }

    public void setDisplayedValues(String[] displayedValues) {
        this.mDelegate.setDisplayedValues(displayedValues);
    }

    public void setTextSize(float size) {
        this.mDelegate.setTextSize(size);
    }

    public void setSubTextSize(float size) {
        this.mDelegate.setSubTextSize(size);
    }

    public void setTextTypeface(Typeface typeface) {
        this.mDelegate.setTextTypeface(typeface);
    }

    public void startAnimation(int delayTime, SeslAnimationListener listener) {
        this.mDelegate.startAnimation(delayTime, listener);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDelegate.onDetachedFromWindow();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mDelegate.onAttachedToWindow();
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDelegate.isEditTextModeNotAmPm()) {
            super.onDraw(canvas);
        } else {
            this.mDelegate.onDraw(canvas);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        this.mDelegate.onInitializeAccessibilityEvent(event);
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (this.mDelegate.isEditTextModeNotAmPm()) {
            return super.getAccessibilityNodeProvider();
        }
        return this.mDelegate.getAccessibilityNodeProvider();
    }

    public void setMaxInputLength(int limit) {
        this.mDelegate.setMaxInputLength(limit);
    }

    public EditText getEditText() {
        return this.mDelegate.getEditText();
    }

    public void setMonthInputMode() {
        this.mDelegate.setMonthInputMode();
    }

    public void setYearDateTimeInputMode() {
        this.mDelegate.setYearDateTimeInputMode();
    }

    int[] getEnableStateSet() {
        return ENABLED_STATE_SET;
    }

    boolean isVisibleToUserWrapper() {
        return SeslViewReflector.isVisibleToUser(this);
    }

    boolean isVisibleToUserWrapper(Rect boundInView) {
        return SeslViewReflector.isVisibleToUser(this, boundInView);
    }
}
