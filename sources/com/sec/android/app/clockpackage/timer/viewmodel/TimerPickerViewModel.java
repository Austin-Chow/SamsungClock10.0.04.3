package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.samsung.android.widget.SemAnimationListener;
import com.samsung.android.widget.SemNumberPicker;
import com.samsung.android.widget.SemNumberPicker.OnEditTextModeChangedListener;
import com.samsung.android.widget.SemNumberPicker.OnValueChangeListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerPickerViewListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.view.TimerNumberPicker;

public class TimerPickerViewModel extends LinearLayout {
    private TextView mColonHMS;
    private TextView mColonMS;
    private Context mContext;
    private OnEditorActionListener mEditorActionListener;
    OnFocusChangeListener mFocusChangeListener;
    private View mFragmentView;
    private Handler mHandler;
    private int mHour;
    private TimerNumberPicker mHourSpinner;
    private EditText mHourSpinnerInput;
    private TextView mHourText;
    private PickerTextWatcher mHourTextWatcher;
    private boolean mIsEditMode;
    private boolean mIsFlipAnimationWorking;
    private int mMinute;
    private TimerNumberPicker mMinuteSpinner;
    private EditText mMinuteSpinnerInput;
    private TextView mMinuteText;
    private PickerTextWatcher mMinuteTextWatcher;
    private OnEditTextModeChangedListener mModeChangeListener;
    private LinearLayout mPickerLayout;
    private EditText[] mPickerTexts;
    private int mSecond;
    private TimerNumberPicker mSecondSpinner;
    private EditText mSecondSpinnerInput;
    private TextView mSecondText;
    private PickerTextWatcher mSecondTextWatcher;
    private TimerManager mTimerManager;
    private TimerPickerViewListener mTimerPickerViewListener;
    private boolean mUpdateLayoutAfterAnimation;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$1 */
    class C07981 implements OnFocusChangeListener {
        C07981() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (TimerPickerViewModel.this.isEditMode() && hasFocus) {
                TimerPickerViewModel.this.updateInputState();
                TimerPickerViewModel.this.setEditMode(false, false);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$2 */
    class C07992 implements OnEditorActionListener {
        C07992() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                TimerPickerViewModel.this.setEditMode(false, false);
                return false;
            } else if (actionId != 5) {
                return false;
            } else {
                if (v == TimerPickerViewModel.this.mHourSpinnerInput) {
                    TimerPickerViewModel.this.mMinuteSpinnerInput.requestFocus();
                } else if (v == TimerPickerViewModel.this.mMinuteSpinnerInput) {
                    TimerPickerViewModel.this.mSecondSpinnerInput.requestFocus();
                }
                return true;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$3 */
    class C08003 implements OnEditTextModeChangedListener {
        C08003() {
        }

        public void onEditTextModeChanged(SemNumberPicker picker, boolean mode) {
            if (ClockTab.isTimerTab()) {
                TimerPickerViewModel.this.setEditMode(mode, false);
                if (!TimerPickerViewModel.this.mIsEditMode) {
                    InputMethodManager inputMethodManager = (InputMethodManager) TimerPickerViewModel.this.mContext.getSystemService("input_method");
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(TimerPickerViewModel.this.getWindowToken(), 0);
                    }
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$4 */
    class C08014 implements OnValueChangeListener {
        C08014() {
        }

        public void onValueChange(SemNumberPicker spinner, int oldVal, int newVal) {
            if (TimerData.getTimerState() != 1 && !TimerPickerViewModel.this.mIsFlipAnimationWorking) {
                TimerPickerViewModel.this.mHour = newVal;
                TimerPickerViewModel.this.setPickerContentDescription();
                TimerPickerViewModel.this.mTimerManager.setInputTimeForPicker(TimerPickerViewModel.this.mHour, TimerPickerViewModel.this.mMinute, TimerPickerViewModel.this.mSecond);
                TimerPickerViewModel.this.mTimerPickerViewListener.onCheckInputData();
                TimerPickerViewModel.this.mTimerPickerViewListener.onTimeChanged();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$5 */
    class C08025 implements OnValueChangeListener {
        C08025() {
        }

        public void onValueChange(SemNumberPicker spinner, int oldVal, int newVal) {
            if (TimerData.getTimerState() != 1 && !TimerPickerViewModel.this.mIsFlipAnimationWorking) {
                TimerPickerViewModel.this.mMinute = newVal;
                TimerPickerViewModel.this.setPickerContentDescription();
                TimerPickerViewModel.this.mTimerManager.setInputTimeForPicker(TimerPickerViewModel.this.mHour, TimerPickerViewModel.this.mMinute, TimerPickerViewModel.this.mSecond);
                TimerPickerViewModel.this.mTimerPickerViewListener.onCheckInputData();
                TimerPickerViewModel.this.mTimerPickerViewListener.onTimeChanged();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$6 */
    class C08036 implements OnValueChangeListener {
        C08036() {
        }

        public void onValueChange(SemNumberPicker spinner, int oldVal, int newVal) {
            if (TimerData.getTimerState() != 1 && !TimerPickerViewModel.this.mIsFlipAnimationWorking) {
                TimerPickerViewModel.this.mSecond = newVal;
                TimerPickerViewModel.this.setPickerContentDescription();
                TimerPickerViewModel.this.mTimerManager.setInputTimeForPicker(TimerPickerViewModel.this.mHour, TimerPickerViewModel.this.mMinute, TimerPickerViewModel.this.mSecond);
                TimerPickerViewModel.this.mTimerPickerViewListener.onCheckInputData();
                TimerPickerViewModel.this.mTimerPickerViewListener.onTimeChanged();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$7 */
    class C08047 implements Runnable {
        C08047() {
        }

        public void run() {
            TimerPickerViewModel.this.showSoftInput();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPickerViewModel$9 */
    class C08069 implements SemAnimationListener {
        C08069() {
        }

        public void onAnimationEnd() {
            Log.secD("TimerPickerViewModel", "FlipAnimationEndListener onAnimationEnd()");
            TimerPickerViewModel.this.mIsFlipAnimationWorking = false;
            TimerPickerViewModel.this.mTimerPickerViewListener.onCheckInputData();
            if (TimerPickerViewModel.this.mUpdateLayoutAfterAnimation) {
                TimerPickerViewModel.this.updateLayout();
                TimerPickerViewModel.this.mUpdateLayoutAfterAnimation = false;
            }
        }
    }

    private class PickerKeyListener implements OnKeyListener {
        private PickerKeyListener() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 1) {
                return false;
            }
            switch (keyCode) {
                case 23:
                    if (TimerPickerViewModel.this.getResources().getConfiguration().keyboard == 3) {
                        return false;
                    }
                    break;
                case 61:
                    return true;
                case 66:
                    break;
                default:
                    return false;
            }
            if (TimerPickerViewModel.this.mIsEditMode) {
                if ((((EditText) v).getImeOptions() & 5) == 5) {
                    View next = FocusFinder.getInstance().findNextFocus(TimerPickerViewModel.this.mPickerLayout, v, 2);
                    if (next == null) {
                        return true;
                    }
                    next.requestFocus();
                } else if ((((EditText) v).getImeOptions() & 6) == 6) {
                    TimerPickerViewModel.this.updateInputState();
                    TimerPickerViewModel.this.setEditTextMode(false);
                }
            }
            return true;
        }
    }

    private class PickerTextWatcher implements TextWatcher {
        private int mChangedLen;
        private int mId;
        private String mNewText;
        private int mNext;
        private String mPrevText;

        private PickerTextWatcher(int id) {
            this.mChangedLen = 0;
            this.mId = id;
            this.mNext = this.mId + 1 > 2 ? -1 : this.mId + 1;
        }

        public void afterTextChanged(Editable view) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.mPrevText = s.toString();
            this.mChangedLen = after;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String tag = (TimerPickerViewModel.this.mPickerTexts == null || TimerPickerViewModel.this.mPickerTexts[this.mId] == null) ? null : (String) TimerPickerViewModel.this.mPickerTexts[this.mId].getTag();
            if (tag == null || !(tag.equals("onClick") || tag.equals("onLongClick"))) {
                this.mNewText = s.toString();
                if (TimerData.isTimerStateResetedOrNone()) {
                    Log.secD("TimerPickerViewModel", "PickerTextWatcher onTextChanged() / newText = " + this.mNewText + ", prevText = " + this.mPrevText);
                    if (this.mNewText.length() == 0) {
                        if (this.mId == 0) {
                            this.mNewText = Integer.toString(TimerPickerViewModel.this.mHourSpinner.getValue());
                        }
                        if (this.mId == 1) {
                            this.mNewText = Integer.toString(TimerPickerViewModel.this.mMinuteSpinner.getValue());
                        }
                        if (this.mId == 2) {
                            this.mNewText = Integer.toString(TimerPickerViewModel.this.mSecondSpinner.getValue());
                        }
                    }
                    if (this.mNewText.length() > 0 && !this.mNewText.equals(this.mPrevText) && TimerPickerViewModel.this.mIsEditMode) {
                        if (this.mId == 0) {
                            TimerPickerViewModel.this.mHour = Integer.parseInt(this.mNewText);
                        }
                        if (this.mId == 1) {
                            TimerPickerViewModel.this.mMinute = Integer.parseInt(this.mNewText);
                        }
                        if (this.mId == 2) {
                            TimerPickerViewModel.this.mSecond = Integer.parseInt(this.mNewText);
                        }
                        TimerPickerViewModel.this.mTimerManager.setInputTime(TimerPickerViewModel.this.mHour, TimerPickerViewModel.this.mMinute, TimerPickerViewModel.this.mSecond);
                        TimerPickerViewModel.this.mTimerPickerViewListener.onCheckInputData();
                        TimerPickerViewModel.this.mTimerPickerViewListener.onTimeChanged();
                    }
                }
                if (this.mPrevText.length() < s.length() && s.length() == 2 && TimerPickerViewModel.this.mPickerTexts != null && TimerPickerViewModel.this.mPickerTexts[this.mId] != null && TimerPickerViewModel.this.mPickerTexts[this.mId].isFocused()) {
                    changeFocus();
                }
                if ((this.mId != 1 && this.mId != 2) || this.mChangedLen != 1 || s.length() <= 0) {
                    return;
                }
                if ((s.toString().equals("6") || s.toString().equals("7") || s.toString().equals("8") || s.toString().equals("9")) && TimerPickerViewModel.this.mPickerTexts != null && TimerPickerViewModel.this.mPickerTexts[this.mId] != null && TimerPickerViewModel.this.mPickerTexts[this.mId].isFocused()) {
                    changeFocus();
                    return;
                }
                return;
            }
            TimerPickerViewModel.this.mPickerTexts[this.mId].setTag("");
        }

        private void changeFocus() {
            Log.secD("TimerPickerViewModel", "PickerTextWatcher changeFocus()");
            if (!((AccessibilityManager) TimerPickerViewModel.this.mContext.getSystemService("accessibility")).isTouchExplorationEnabled()) {
                if (this.mNext >= 0) {
                    TimerPickerViewModel.this.mPickerTexts[this.mNext].requestFocus();
                    if (TimerPickerViewModel.this.mPickerTexts[this.mId].isFocused()) {
                        TimerPickerViewModel.this.mPickerTexts[this.mId].clearFocus();
                        return;
                    }
                    return;
                }
                TimerPickerViewModel.this.mPickerTexts[this.mId].selectAll();
            }
        }
    }

    public TimerPickerViewModel(Context context) {
        super(context);
        this.mPickerTexts = new EditText[4];
        this.mIsFlipAnimationWorking = false;
        this.mUpdateLayoutAfterAnimation = false;
        this.mFocusChangeListener = new C07981();
        this.mHandler = new Handler();
        this.mEditorActionListener = new C07992();
        this.mModeChangeListener = new C08003();
        init(context);
    }

    public TimerPickerViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPickerTexts = new EditText[4];
        this.mIsFlipAnimationWorking = false;
        this.mUpdateLayoutAfterAnimation = false;
        this.mFocusChangeListener = new C07981();
        this.mHandler = new Handler();
        this.mEditorActionListener = new C07992();
        this.mModeChangeListener = new C08003();
        init(context);
    }

    public TimerPickerViewModel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPickerTexts = new EditText[4];
        this.mIsFlipAnimationWorking = false;
        this.mUpdateLayoutAfterAnimation = false;
        this.mFocusChangeListener = new C07981();
        this.mHandler = new Handler();
        this.mEditorActionListener = new C07992();
        this.mModeChangeListener = new C08003();
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mTimerManager = TimerManager.getInstance();
    }

    public void initViews(View fragment) {
        this.mFragmentView = fragment;
    }

    public void setTimePickerView() {
        Log.secD("TimerPickerViewModel", "setTimePickerView()");
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        removeAllViews();
        inflater.inflate(C0728R.layout.timer_timepicker_hms, this);
        this.mPickerLayout = (LinearLayout) findViewById(C0728R.id.timer_picker_layout);
        this.mHourText = (TextView) this.mFragmentView.findViewById(C0728R.id.timer_hour_text);
        this.mMinuteText = (TextView) this.mFragmentView.findViewById(C0728R.id.timer_min_text);
        this.mSecondText = (TextView) this.mFragmentView.findViewById(C0728R.id.timer_sec_text);
        this.mHourText.setFocusable(StateUtils.isTalkBackEnabled(this.mContext));
        this.mMinuteText.setFocusable(StateUtils.isTalkBackEnabled(this.mContext));
        this.mSecondText.setFocusable(StateUtils.isTalkBackEnabled(this.mContext));
        this.mHourText.setOnFocusChangeListener(this.mFocusChangeListener);
        this.mMinuteText.setOnFocusChangeListener(this.mFocusChangeListener);
        this.mSecondText.setOnFocusChangeListener(this.mFocusChangeListener);
        this.mHourSpinner = (TimerNumberPicker) findViewById(C0728R.id.timer_timepicker_hours_picker);
        this.mMinuteSpinner = (TimerNumberPicker) findViewById(C0728R.id.timer_timepicker_minutes_picker);
        this.mSecondSpinner = (TimerNumberPicker) findViewById(C0728R.id.timer_timepicker_seconds_picker);
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setOnLongPressUpdateInterval(100);
            this.mHourSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
            this.mHourSpinner.setPickerContentDescription(this.mContext.getResources().getString(C0728R.string.timer_hour));
            this.mHourSpinner.setOnValueChangedListener(new C08014());
            this.mHourSpinnerInput = this.mHourSpinner.getEditText();
            this.mHourSpinnerInput.setImeOptions(33554437);
            this.mHourSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
            this.mHourSpinnerInput.setText("");
            this.mHourSpinnerInput.setTag("hourSpinnerInput");
            this.mHourSpinner.setMinValue(0);
            this.mHourSpinner.setMaxValue(99);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setSkipValuesOnLongPressEnabled(true);
            this.mMinuteSpinner.setOnLongPressUpdateInterval(100);
            this.mMinuteSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
            this.mMinuteSpinner.setPickerContentDescription(this.mContext.getResources().getString(C0728R.string.timer_minute));
            this.mMinuteSpinner.setOnValueChangedListener(new C08025());
            this.mMinuteSpinnerInput = this.mMinuteSpinner.getEditText();
            this.mMinuteSpinnerInput.setImeOptions(33554437);
            this.mMinuteSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
            this.mMinuteSpinnerInput.setText("");
            this.mMinuteSpinnerInput.setTag("minuteSpinnerInput");
            this.mMinuteSpinner.setMinValue(0);
            this.mMinuteSpinner.setMaxValue(59);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setSkipValuesOnLongPressEnabled(true);
            this.mSecondSpinner.setOnLongPressUpdateInterval(100);
            this.mSecondSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
            this.mSecondSpinner.setPickerContentDescription(this.mContext.getResources().getString(C0728R.string.timer_second));
            this.mSecondSpinner.setOnValueChangedListener(new C08036());
            this.mSecondSpinnerInput = this.mSecondSpinner.getEditText();
            this.mSecondSpinnerInput.setImeOptions(33554438);
            this.mSecondSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
            this.mSecondSpinnerInput.setText("");
            this.mSecondSpinnerInput.setTag("secondSpinnerInput");
            this.mSecondSpinner.setMinValue(0);
            this.mSecondSpinner.setMaxValue(59);
        }
        setPrivateImeOptions();
        setTextWatcher();
        initTimePickerView();
        this.mColonHMS = (TextView) findViewById(C0728R.id.timer_hms_colon);
        this.mColonMS = (TextView) findViewById(C0728R.id.timer_ms_colon);
        String timeSeparator = ClockUtils.getTimeSeparatorText(this.mContext);
        this.mColonHMS.setText(timeSeparator);
        this.mColonMS.setText(timeSeparator);
        setFontFromOpenTheme();
    }

    public void initTimePickerView() {
        Log.secD("TimerPickerViewModel", "initTimePickerView() / inputMillis = " + TimerData.getInputMillis());
        long inputMillis = TimerData.getInputMillis();
        this.mHour = (int) (inputMillis / 3600000);
        this.mMinute = (int) ((inputMillis % 3600000) / 60000);
        this.mSecond = (int) ((inputMillis % 60000) / 1000);
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setValue(this.mHour);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setValue(this.mMinute);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setValue(this.mSecond);
        }
        setPickerContentDescription();
        this.mTimerPickerViewListener.onCheckInputData();
    }

    public void setEnabledNumberPicker(boolean enabled) {
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setEnabled(enabled);
            this.mHourSpinner.setFocusable(enabled);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setEnabled(enabled);
            this.mMinuteSpinner.setFocusable(enabled);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setEnabled(enabled);
            this.mSecondSpinner.setFocusable(enabled);
        }
    }

    public void setTime(int hour, int minute, int second) {
        this.mHour = hour;
        this.mMinute = minute;
        this.mSecond = second;
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setValue(this.mHour);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setValue(this.mMinute);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setValue(this.mSecond);
        }
        setPickerContentDescription();
        this.mTimerManager.setInputTime(this.mHour, this.mMinute, this.mSecond);
        TimerData.savePreviousInput(this.mHour, this.mMinute, this.mSecond);
    }

    public void setPrivateImeOptions() {
        if (StateUtils.isMobileKeyboard(this.mContext)) {
            this.mHourSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            this.mMinuteSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            this.mSecondSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            return;
        }
        this.mHourSpinnerInput.setPrivateImeOptions("inputType=YearDateTime_edittext");
        this.mMinuteSpinnerInput.setPrivateImeOptions("inputType=YearDateTime_edittext");
        this.mSecondSpinnerInput.setPrivateImeOptions("inputType=YearDateTime_edittext");
    }

    public void resumeView() {
        if (this.mIsEditMode) {
            initTimePickerView();
            this.mHandler.postDelayed(new C08047(), 200);
            return;
        }
        setTimePickerView();
        updateLayout();
        if (this.mTimerPickerViewListener.isActionMode()) {
            setEnabledNumberPicker(false);
        }
    }

    public void showSoftInput() {
        if (ClockTab.isTimerTab() && this.mIsEditMode) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            if (inputMethodManager != null) {
                int i = 0;
                while (i <= 2) {
                    if (this.mPickerTexts[i] != null && this.mPickerTexts[i].hasFocus()) {
                        this.mPickerTexts[i].selectAll();
                        inputMethodManager.showSoftInput(this.mPickerTexts[i], 0);
                    }
                    i++;
                }
            }
        }
    }

    public void pauseView() {
        if (this.mIsEditMode) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void destroyView() {
        if (this.mPickerLayout != null) {
            this.mPickerLayout.removeAllViews();
            this.mPickerLayout = null;
        }
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setOnEditTextModeChangedListener(null);
            this.mHourSpinner.setOnValueChangedListener(null);
            this.mHourSpinner.removeAllViews();
            this.mHourSpinner.destroyDrawingCache();
            this.mHourSpinner = null;
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setOnEditTextModeChangedListener(null);
            this.mMinuteSpinner.setOnValueChangedListener(null);
            this.mMinuteSpinner.removeAllViews();
            this.mMinuteSpinner.destroyDrawingCache();
            this.mMinuteSpinner = null;
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setOnEditTextModeChangedListener(null);
            this.mSecondSpinner.setOnValueChangedListener(null);
            this.mSecondSpinner.removeAllViews();
            this.mSecondSpinner.destroyDrawingCache();
            this.mSecondSpinner = null;
        }
        if (this.mHourSpinnerInput != null) {
            this.mHourSpinnerInput.setOnEditorActionListener(null);
            this.mHourSpinnerInput = null;
        }
        if (this.mMinuteSpinnerInput != null) {
            this.mMinuteSpinnerInput.setOnEditorActionListener(null);
            this.mMinuteSpinnerInput = null;
        }
        if (this.mSecondSpinnerInput != null) {
            this.mSecondSpinnerInput.setOnEditorActionListener(null);
            this.mSecondSpinnerInput = null;
        }
        if (this.mPickerTexts != null) {
            this.mPickerTexts[0].removeTextChangedListener(this.mHourTextWatcher);
            this.mPickerTexts[1].removeTextChangedListener(this.mMinuteTextWatcher);
            this.mPickerTexts[2].removeTextChangedListener(this.mSecondTextWatcher);
            this.mPickerTexts[0].setOnKeyListener(null);
            this.mPickerTexts[1].setOnKeyListener(null);
            this.mPickerTexts[2].setOnKeyListener(null);
            this.mPickerTexts = null;
        }
        if (this.mHourText != null) {
            this.mHourText.setOnFocusChangeListener(null);
            this.mHourText = null;
        }
        if (this.mMinuteText != null) {
            this.mMinuteText.setOnFocusChangeListener(null);
        }
        if (this.mSecondText != null) {
            this.mSecondText.setOnFocusChangeListener(null);
            this.mSecondText = null;
        }
        this.mModeChangeListener = null;
        this.mEditorActionListener = null;
        this.mHourTextWatcher = null;
        this.mMinuteTextWatcher = null;
        this.mSecondTextWatcher = null;
        this.mHandler = null;
        this.mFragmentView = null;
        removeAllViews();
    }

    public boolean isSpinnerFocused(View focusedView) {
        return focusedView != null && (focusedView.equals(this.mHourSpinnerInput) || focusedView.equals(this.mMinuteSpinnerInput) || focusedView.equals(this.mSecondSpinnerInput));
    }

    private void setFontFromOpenTheme() {
        Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (font == null) {
            font = Typeface.create("sans-serif-light", 0);
        }
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setTextTypeface(font);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setTextTypeface(font);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setTextTypeface(font);
        }
        if (this.mColonHMS != null) {
            this.mColonHMS.setTypeface(font);
        }
        if (this.mColonMS != null) {
            this.mColonMS.setTypeface(font);
        }
    }

    public void setEditMode(final boolean editMode, boolean isDelay) {
        boolean z = false;
        if (this.mIsEditMode != editMode) {
            this.mIsEditMode = editMode;
            TextView textView = this.mHourText;
            boolean z2 = StateUtils.isTalkBackEnabled(this.mContext) || editMode;
            textView.setFocusable(z2);
            textView = this.mMinuteText;
            if (StateUtils.isTalkBackEnabled(this.mContext) || editMode) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setFocusable(z2);
            TextView textView2 = this.mSecondText;
            if (StateUtils.isTalkBackEnabled(this.mContext) || editMode) {
                z = true;
            }
            textView2.setFocusable(z);
            if (isDelay) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        TimerPickerViewModel.this.setEditTextMode(editMode);
                    }
                }, 50);
            } else {
                setEditTextMode(editMode);
            }
            this.mTimerPickerViewListener.onEditModeChanged();
            if (TimerUtils.isShowIme(this.mContext) && !StateUtils.isMobileKeyboard(this.mContext)) {
                this.mTimerPickerViewListener.onKeypadAnimation(this.mIsEditMode);
            }
        }
    }

    private void setEditTextMode(boolean editMode) {
        if (this.mHourSpinner != null) {
            this.mHourSpinner.setEditTextMode(editMode);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.setEditTextMode(editMode);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.setEditTextMode(editMode);
        }
    }

    public boolean isEditMode() {
        return this.mIsEditMode;
    }

    public void updateInputState() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        if (inputMethodManager == null) {
            return;
        }
        if (this.mHourSpinnerInput != null && inputMethodManager.isActive(this.mHourSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mHourSpinnerInput.getWindowToken(), 0);
            this.mHourSpinnerInput.clearFocus();
        } else if (this.mMinuteSpinnerInput != null && inputMethodManager.isActive(this.mMinuteSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mMinuteSpinnerInput.getWindowToken(), 0);
            this.mMinuteSpinnerInput.clearFocus();
        } else if (this.mSecondSpinnerInput != null && inputMethodManager.isActive(this.mSecondSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mSecondSpinnerInput.getWindowToken(), 0);
            this.mSecondSpinnerInput.clearFocus();
        }
    }

    private void setTextWatcher() {
        if (this.mHourSpinner != null && this.mMinuteSpinner != null && this.mSecondSpinner != null) {
            this.mPickerTexts[0] = this.mHourSpinner.getEditText();
            this.mPickerTexts[1] = this.mMinuteSpinner.getEditText();
            this.mPickerTexts[2] = this.mSecondSpinner.getEditText();
            this.mHourTextWatcher = new PickerTextWatcher(0);
            this.mMinuteTextWatcher = new PickerTextWatcher(1);
            this.mSecondTextWatcher = new PickerTextWatcher(2);
            this.mPickerTexts[0].addTextChangedListener(this.mHourTextWatcher);
            this.mPickerTexts[1].addTextChangedListener(this.mMinuteTextWatcher);
            this.mPickerTexts[2].addTextChangedListener(this.mSecondTextWatcher);
            this.mPickerTexts[0].setOnKeyListener(new PickerKeyListener());
            this.mPickerTexts[1].setOnKeyListener(new PickerKeyListener());
            this.mPickerTexts[2].setOnKeyListener(new PickerKeyListener());
        }
    }

    private void setPickerContentDescription() {
        setContentDescription(Integer.toString(this.mHour) + ' ' + this.mContext.getResources().getString(C0728R.string.timer_hour) + ' ' + Integer.toString(this.mMinute) + ' ' + this.mContext.getResources().getString(C0728R.string.timer_minute) + ' ' + Integer.toString(this.mSecond) + ' ' + this.mContext.getResources().getString(C0728R.string.timer_second));
    }

    public void startFlipAnimation(int duration) {
        if (this.mHourSpinner != null) {
            this.mHourSpinner.startAnimation(duration, null);
        }
        if (this.mMinuteSpinner != null) {
            this.mMinuteSpinner.startAnimation(duration + 55, null);
        }
        if (this.mSecondSpinner != null) {
            this.mSecondSpinner.startAnimation(duration + 110, new C08069());
        }
        this.mIsFlipAnimationWorking = true;
        this.mUpdateLayoutAfterAnimation = false;
    }

    public boolean isFlipAnimationWorking() {
        return this.mIsFlipAnimationWorking;
    }

    public int getHour() {
        return this.mHour;
    }

    public int getMinute() {
        return this.mMinute;
    }

    public int getSecond() {
        return this.mSecond;
    }

    public void updateLayout() {
        Log.secD("TimerPickerViewModel", "updateLayout()");
        setTextLayout(this.mHourText);
        setTextLayout(this.mMinuteText);
        setTextLayout(this.mSecondText);
        if (this.mIsFlipAnimationWorking) {
            this.mUpdateLayoutAfterAnimation = true;
            return;
        }
        setPickerLayout(this.mHourSpinner, false);
        setPickerLayout(this.mMinuteSpinner, false);
        setPickerLayout(this.mSecondSpinner, false);
        setPickerLayout(this.mColonHMS, true);
        setPickerLayout(this.mColonMS, true);
    }

    private void setPickerLayout(View view, boolean isColon) {
        if (view != null) {
            Resources res = getResources();
            LayoutParams pickerParam = (LayoutParams) view.getLayoutParams();
            if (pickerParam != null) {
                if (isColon) {
                    pickerParam.height = -2;
                } else {
                    pickerParam.height = getPickerHeight();
                }
                pickerParam.width = getPickerWidth(isColon);
            }
            if (isColon) {
                ((TextView) view).setTextSize(0, (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_colon_textsize));
                return;
            }
            float textSize = (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_time_textsize);
            ((TimerNumberPicker) view).setTextSize(textSize);
            ((TimerNumberPicker) view).setSubTextSize(textSize);
        }
    }

    private void setTextLayout(TextView view) {
        if (view != null) {
            Resources res = getResources();
            RelativeLayout.LayoutParams textParam = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (textParam != null) {
                textParam.width = getPickerWidth(true) + getPickerWidth(false);
                textParam.height = -2;
            }
            ClockUtils.setLargeTextSize(this.mContext, view, (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_hms_textview_textsize));
        }
    }

    public int getPickerWidth(boolean isColon) {
        int leftRightMargin;
        Resources res = this.mContext.getResources();
        int pickerLayoutWidth = Math.min(res.getConfiguration().orientation == 1 ? res.getDisplayMetrics().widthPixels : (int) (((float) res.getDisplayMetrics().widthPixels) * 0.507f), res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_layout_max_width));
        if (((Activity) this.mContext).isInMultiWindowMode()) {
            leftRightMargin = 0;
        } else {
            leftRightMargin = res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_layout_margin_left_right) * 2;
        }
        int pickerWidth = Math.max((int) ((((float) (pickerLayoutWidth - leftRightMargin)) * 3.0f) / 13.0f), res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_twtimepicker_min_width));
        return isColon ? pickerWidth / 3 : pickerWidth;
    }

    public int getPickerHeight() {
        Resources res = this.mContext.getResources();
        boolean isMultiWindowMode = ((Activity) this.mContext).isInMultiWindowMode();
        boolean isMobileKeyboard = StateUtils.isMobileKeyboard(this.mContext);
        if (!isMultiWindowMode && res.getConfiguration().orientation != 1 && !StateUtils.isScreenDp(this.mContext, 512)) {
            return res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_layout_min_height);
        }
        int statusBarHeight;
        int dimensionPixelSize;
        int buttonLayoutHeight = res.getDimensionPixelSize(isMultiWindowMode ? C0728R.dimen.stopwatch_button_height : C0728R.dimen.stopwatch_button_layout_height);
        if (isMultiWindowMode || Feature.isTablet(this.mContext) || StateUtils.isContextInDexMode(this.mContext) || res.getConfiguration().orientation != 2) {
            statusBarHeight = ClockUtils.getStatusBarHeight(this.mContext);
        } else {
            statusBarHeight = 0;
        }
        float pickerHeightRatio = isMobileKeyboard ? 0.563f : 0.4301075f;
        int actionBarHeight = (((res.getDisplayMetrics().heightPixels - buttonLayoutHeight) - ClockUtils.getActionBarHeight(this.mContext)) - statusBarHeight) - res.getDimensionPixelSize(C0728R.dimen.clock_tab_height);
        if (Feature.isSupportTimerResetButton()) {
            dimensionPixelSize = res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_max_height) + res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_margin_top);
        } else {
            dimensionPixelSize = 0;
        }
        int pickerHeight = (int) (((float) (actionBarHeight - dimensionPixelSize)) * pickerHeightRatio);
        dimensionPixelSize = (isMultiWindowMode || isMobileKeyboard) ? C0728R.dimen.timer_common_timepicker_layout_min_height_for_multiwindow : C0728R.dimen.timer_common_timepicker_layout_min_height;
        return Math.min(Math.max(pickerHeight, res.getDimensionPixelSize(dimensionPixelSize)), res.getDimensionPixelSize(C0728R.dimen.timer_common_timepicker_layout_max_height));
    }

    public void setTimerPickerViewListener(TimerPickerViewListener i) {
        this.mTimerPickerViewListener = i;
    }

    public void saveEditMode(Bundle outState) {
        outState.putBoolean("timer_edit_mode", this.mIsEditMode);
        if (this.mIsEditMode) {
            int focusedSpinnerInput;
            if (this.mSecondSpinnerInput != null && this.mSecondSpinnerInput.hasSelection()) {
                focusedSpinnerInput = 2;
            } else if (this.mMinuteSpinnerInput == null || !this.mMinuteSpinnerInput.hasSelection()) {
                focusedSpinnerInput = 0;
            } else {
                focusedSpinnerInput = 1;
            }
            outState.putInt("timer_edit_mode_focus", focusedSpinnerInput);
        }
    }

    public void restoreEditMode(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean("timer_edit_mode", false)) {
            long inputMillis = TimerData.getInputMillis();
            final int hour = (int) (inputMillis / 3600000);
            final int minute = (int) ((inputMillis % 3600000) / 60000);
            final int second = (int) ((inputMillis % 60000) / 1000);
            final int focusedSpinnerInput = savedInstanceState.getInt("timer_edit_mode_focus", 0);
            final long selectedPresetId = this.mTimerPickerViewListener.getSelectedPresetId();
            setEditMode(true, false);
            if (focusedSpinnerInput == 2) {
                this.mMinuteSpinnerInput.requestFocus();
                this.mSecondSpinnerInput.requestFocus();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    TimerPickerViewModel.this.setTime(hour, minute, second);
                    if (focusedSpinnerInput == 2) {
                        if (TimerPickerViewModel.this.mSecondSpinnerInput != null) {
                            TimerPickerViewModel.this.mSecondSpinnerInput.requestFocus();
                            TimerPickerViewModel.this.mSecondSpinnerInput.selectAll();
                        }
                    } else if (focusedSpinnerInput == 1) {
                        if (TimerPickerViewModel.this.mMinuteSpinnerInput != null) {
                            TimerPickerViewModel.this.mMinuteSpinnerInput.requestFocus();
                            TimerPickerViewModel.this.mMinuteSpinnerInput.selectAll();
                        }
                    } else if (TimerPickerViewModel.this.mHourSpinnerInput != null) {
                        TimerPickerViewModel.this.mHourSpinnerInput.requestFocus();
                        TimerPickerViewModel.this.mHourSpinnerInput.selectAll();
                    }
                    TimerPickerViewModel.this.mTimerPickerViewListener.onSetSelectedPresetId(selectedPresetId);
                }
            }, 500);
        }
    }
}
