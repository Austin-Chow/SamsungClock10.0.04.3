package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.samsung.android.widget.SemAnimationListener;
import com.samsung.android.widget.SemTimePicker;
import com.samsung.android.widget.SemTimePicker.OnEditTextModeChangedListener;
import com.samsung.android.widget.SemTimePicker.OnTimeChangedListener;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.TimeZoneConvertorControlViewListener;

public class TimeZoneConvertorPicker extends LinearLayout {
    private final Context mContext;
    private SemTimePicker mConvertorPicker;
    private int mFocusedSpinner = -1;
    private EditText mHourSpinner;
    private boolean mIsEditMode;
    private int mLastHour = 0;
    private int mLastMinute = 0;
    private EditText mMinuteSpinner;
    private TimeZoneConvertorControlViewListener mTimeZoneConvertorControlViewListener;
    private int mWhereIsSpinnerFocus = 0;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorPicker$1 */
    class C08811 implements SemAnimationListener {
        C08811() {
        }

        public void onAnimationEnd() {
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorPicker$2 */
    class C08822 implements OnTimeChangedListener {
        C08822() {
        }

        public void onTimeChanged(SemTimePicker semTimePicker, int hour, int minute) {
            Log.secD("TimeZoneConvertorPicker", "onTimeChanged() : " + hour + ":" + minute);
            ClockUtils.insertSaLog("115", "1292", (long) hour);
            TimeZoneConvertorPicker.this.mTimeZoneConvertorControlViewListener.onTimeChanged(hour, minute);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorPicker$3 */
    class C08833 implements OnEditTextModeChangedListener {
        C08833() {
        }

        public void onEditTextModeChanged(SemTimePicker semTimePicker, boolean isEditMode) {
            TimeZoneConvertorPicker.this.mTimeZoneConvertorControlViewListener.onEditTextModeChanged(isEditMode);
            if (TimeZoneConvertorPicker.this.mIsEditMode) {
                EditText editText = TimeZoneConvertorPicker.this.mWhereIsSpinnerFocus == 1 ? TimeZoneConvertorPicker.this.mMinuteSpinner : TimeZoneConvertorPicker.this.mHourSpinner;
                editText.requestFocus();
                editText.selectAll();
                TimeZoneConvertorPicker.this.mIsEditMode = false;
            }
        }
    }

    public TimeZoneConvertorPicker(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimeZoneConvertorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public TimeZoneConvertorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void initPicker(boolean isMultiWindow, TimeZoneConvertorControlViewListener listener) {
        this.mTimeZoneConvertorControlViewListener = listener;
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_timezone_convertor_picker, this, true);
        this.mConvertorPicker = (SemTimePicker) findViewById(C0836R.id.worldclock_timezone_convertor_picker);
        this.mHourSpinner = this.mConvertorPicker.getEditText(0);
        this.mHourSpinner.setNextFocusLeftId(C0836R.id.timezone_convertor_main_city_spinner);
        this.mHourSpinner.setNextFocusDownId(C0836R.id.timezone_convertor_main_city_spinner);
        this.mMinuteSpinner = this.mConvertorPicker.getEditText(1);
        this.mMinuteSpinner.setNextFocusDownId(C0836R.id.worldclock_timezone_convertor_reset_button);
        setPickerTextSize();
        if (StateUtils.isScreenDp(this.mContext, 600)) {
            if (isMultiWindow) {
                this.mConvertorPicker.getLayoutParams().height = -2;
                this.mConvertorPicker.getLayoutParams().width = -2;
            } else {
                this.mConvertorPicker.getLayoutParams().height = getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_timezone_convertor_picker_height_tablet);
                this.mConvertorPicker.getLayoutParams().width = getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_timezone_convertor_picker_width);
            }
        }
        initTimeZoneConvertorListener();
    }

    private void setPickerTextSize() {
        float textSize = (float) getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_timezone_convertor_picker_number_text_size);
        setTextSize(0, textSize);
        setTextSize(1, textSize);
        setTextSize(3, textSize);
        setTextSize(2, (float) getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_timezone_convertor_picker_ampm_text_size));
    }

    private void setTextSize(int picker, float textSize) {
        try {
            this.mConvertorPicker.setNumberPickerTextSize(picker, textSize);
        } catch (NoSuchMethodError e) {
            Log.secE("TimeZoneConvertorPicker", "NoSuchMethodError : " + e);
        }
    }

    public void setHour(int hour) {
        this.mConvertorPicker.setHour(hour);
    }

    public void setMinute(int minute) {
        this.mConvertorPicker.setMinute(minute);
    }

    public void setEditTextMode(boolean isEditMode) {
        if (this.mConvertorPicker != null) {
            this.mConvertorPicker.setEditTextMode(isEditMode);
        }
    }

    public void hideInputMethod() {
        if (this.mConvertorPicker != null && this.mHourSpinner != null && this.mMinuteSpinner != null) {
            if (isEditMode()) {
                if (this.mHourSpinner.isInputMethodTarget()) {
                    this.mFocusedSpinner = 0;
                } else if (this.mMinuteSpinner.isInputMethodTarget()) {
                    this.mFocusedSpinner = 1;
                } else {
                    this.mFocusedSpinner = -1;
                }
                setEditTextMode(false);
            } else {
                this.mFocusedSpinner = -1;
            }
            this.mLastHour = this.mConvertorPicker.getHour();
            this.mLastMinute = this.mConvertorPicker.getMinute();
        }
    }

    public void restoreLastTime() {
        this.mLastHour = this.mConvertorPicker.getHour();
        this.mLastMinute = this.mConvertorPicker.getMinute();
    }

    public void showInputMethod(boolean needShow) {
        if (this.mConvertorPicker != null && this.mHourSpinner != null && this.mMinuteSpinner != null) {
            if (needShow) {
                InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
                this.mConvertorPicker.setHour(this.mLastHour);
                this.mConvertorPicker.setMinute(this.mLastMinute);
                if (this.mFocusedSpinner != -1) {
                    setEditTextMode(true);
                    if (this.mFocusedSpinner == 1) {
                        this.mMinuteSpinner.requestFocus();
                        inputMethodManager.showSoftInput(this.mMinuteSpinner, 0);
                    } else {
                        this.mHourSpinner.requestFocus();
                        inputMethodManager.showSoftInput(this.mHourSpinner, 0);
                    }
                }
                this.mFocusedSpinner = -1;
            } else if (isEditMode()) {
                this.mHourSpinner.requestFocus();
                this.mHourSpinner.setSelection(0, this.mHourSpinner.length());
            }
        }
    }

    public void startFlipAnimation(int duration, int hour, int min) {
        if (this.mConvertorPicker != null) {
            this.mConvertorPicker.setHour(hour);
            this.mConvertorPicker.setMinute(min);
            this.mConvertorPicker.startAnimation(duration + 110, new C08811());
        }
    }

    private void initTimeZoneConvertorListener() {
        this.mConvertorPicker.setOnTimeChangedListener(new C08822());
        this.mConvertorPicker.setOnEditTextModeChangedListener(new C08833());
    }

    public void setPickerHeightForMultiWindow(boolean isMinSize) {
        if (this.mConvertorPicker != null) {
            LayoutParams layoutParams = (LayoutParams) this.mConvertorPicker.getLayoutParams();
            layoutParams.height = isMinSize ? getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_picker_height_smallest) : getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_picker_height);
            this.mConvertorPicker.setLayoutParams(layoutParams);
        }
    }

    public void setIs24HourView() {
        this.mConvertorPicker.setIs24HourView(Boolean.valueOf(DateFormat.is24HourFormat(this.mContext)));
    }

    public int getHour() {
        return this.mConvertorPicker.getHour();
    }

    public int getMinute() {
        return this.mConvertorPicker.getMinute();
    }

    public boolean isEditMode() {
        return this.mConvertorPicker != null && this.mConvertorPicker.isEditTextMode();
    }

    public void destroyListener() {
        this.mConvertorPicker.setOnTimeChangedListener(null);
        this.mHourSpinner.addTextChangedListener(null);
        this.mMinuteSpinner.addTextChangedListener(null);
        this.mTimeZoneConvertorControlViewListener = null;
        this.mMinuteSpinner = null;
        this.mHourSpinner = null;
        this.mConvertorPicker = null;
    }

    public void requestPickerFocus(int focusTarget) {
        if (this.mMinuteSpinner != null && this.mHourSpinner != null && isEditMode()) {
            EditText targetText = focusTarget == 1 ? this.mMinuteSpinner : this.mHourSpinner;
            targetText.requestFocus();
            targetText.selectAll();
        }
    }

    public void clearPickerFocus() {
        if (this.mMinuteSpinner != null && this.mHourSpinner != null && isEditMode()) {
            this.mMinuteSpinner.clearFocus();
            this.mHourSpinner.clearFocus();
        }
    }

    public void saveInstance(Bundle state) {
        int focusedSpinner = 0;
        if (isEditMode()) {
            focusedSpinner = this.mMinuteSpinner.hasSelection() ? 1 : 0;
            state.putBoolean("timezone_convertor_eidt_mode", true);
        }
        state.putInt("timezone_convertor_hour", this.mConvertorPicker.getHour());
        state.putInt("timezone_convertor_min", this.mConvertorPicker.getMinute());
        state.putInt("timezone_convertor_eidt_mode_focused", focusedSpinner);
    }

    public void restoreInstance(Bundle state) {
        int hour = state.getInt("timezone_convertor_hour", 0);
        int min = state.getInt("timezone_convertor_min", 0);
        this.mIsEditMode = state.getBoolean("timezone_convertor_eidt_mode", false);
        this.mWhereIsSpinnerFocus = state.getInt("timezone_convertor_eidt_mode_focused", 0);
        setHour(hour);
        setMinute(min);
        restoreLastTime();
        setEditTextMode(this.mIsEditMode);
    }
}
