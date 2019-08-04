package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import java.util.Locale;

public class SeslTimePicker extends FrameLayout {
    public static final int PICKER_AMPM = 2;
    public static final int PICKER_HOUR = 0;
    public static final int PICKER_MINUTE = 1;
    private SeslTimePickerDelegate mDelegate;

    public interface OnTimeChangedListener {
        void onTimeChanged(SeslTimePicker seslTimePicker, int i, int i2);
    }

    interface SeslTimePickerDelegate {
        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        int getBaseline();

        int getDefaultHeight();

        int getDefaultWidth();

        EditText getEditText(int i);

        int getHour();

        int getMinute();

        SeslNumberPicker getNumberPicker(int i);

        boolean is24Hour();

        boolean isEditTextMode();

        boolean isEnabled();

        void onConfigurationChanged(Configuration configuration);

        void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo);

        void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onRestoreInstanceState(Parcelable parcelable);

        Parcelable onSaveInstanceState(Parcelable parcelable);

        void setCurrentLocale(Locale locale);

        void setEditTextMode(boolean z);

        void setEnabled(boolean z);

        void setHour(int i);

        void setIs24Hour(boolean z);

        void setMinute(int i);

        void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener);

        void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);

        void startAnimation(int i, SeslAnimationListener seslAnimationListener);
    }

    static abstract class AbstractTimePickerDelegate implements SeslTimePickerDelegate {
        protected Context mContext;
        protected Locale mCurrentLocale;
        protected SeslTimePicker mDelegator;
        protected OnEditTextModeChangedListener mOnEditTextModeChangedListener;
        protected OnTimeChangedListener mOnTimeChangedListener;

        public AbstractTimePickerDelegate(SeslTimePicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
        }

        public void setCurrentLocale(Locale locale) {
            if (!locale.equals(this.mCurrentLocale)) {
                this.mCurrentLocale = locale;
            }
        }
    }

    public interface OnEditTextModeChangedListener {
        void onEditTextModeChanged(SeslTimePicker seslTimePicker, boolean z);
    }

    public SeslTimePicker(Context context) {
        this(context, null);
    }

    public SeslTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 16843933);
    }

    public SeslTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mDelegate = new SeslTimePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mDelegate.setOnEditTextModeChangedListener(onEditTextModeChangedListener);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == Integer.MIN_VALUE) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.mDelegate.getDefaultWidth(), 1073741824);
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mDelegate.getDefaultHeight(), 1073741824);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setEditTextMode(boolean editTextMode) {
        this.mDelegate.setEditTextMode(editTextMode);
    }

    public boolean isEditTextMode() {
        return this.mDelegate.isEditTextMode();
    }

    public void setHour(int hour) {
        this.mDelegate.setHour(MathUtils.constrain(hour, 0, 23));
    }

    public int getHour() {
        return this.mDelegate.getHour();
    }

    public void setMinute(int minute) {
        this.mDelegate.setMinute(MathUtils.constrain(minute, 0, 59));
    }

    public int getMinute() {
        return this.mDelegate.getMinute();
    }

    public void setIs24HourView(Boolean is24HourView) {
        if (is24HourView != null) {
            this.mDelegate.setIs24Hour(is24HourView.booleanValue());
        }
    }

    public boolean is24HourView() {
        return this.mDelegate.is24Hour();
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mDelegate.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    public int getBaseline() {
        return this.mDelegate.getBaseline();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDelegate.onConfigurationChanged(newConfig);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    protected Parcelable onSaveInstanceState() {
        return this.mDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    protected void onRestoreInstanceState(Parcelable state) {
        BaseSavedState ss = (BaseSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mDelegate.onRestoreInstanceState(ss);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        this.mDelegate.onPopulateAccessibilityEvent(event);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        this.mDelegate.onInitializeAccessibilityEvent(event);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        this.mDelegate.onInitializeAccessibilityNodeInfo(info);
    }

    public void setLocale(Locale locale) {
        this.mDelegate.setCurrentLocale(locale);
    }

    public void startAnimation(int delayTime, SeslAnimationListener listener) {
        this.mDelegate.startAnimation(delayTime, listener);
    }

    public EditText getEditText(int picker) {
        return this.mDelegate.getEditText(picker);
    }

    public SeslNumberPicker getNumberPicker(int picker) {
        return this.mDelegate.getNumberPicker(picker);
    }
}
