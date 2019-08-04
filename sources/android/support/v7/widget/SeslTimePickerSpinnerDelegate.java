package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.System;
import android.support.v4.icu.SeslLocaleDataReflector;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslNumberPicker.OnEditTextModeChangedListener;
import android.support.v7.widget.SeslNumberPicker.OnValueChangeListener;
import android.support.v7.widget.SeslTimePicker.OnTimeChangedListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import java.io.File;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

class SeslTimePickerSpinnerDelegate extends AbstractTimePickerDelegate {
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final char[] DIGIT_CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩', '۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹', '०', '१', '२', '३', '४', '५', '६', '७', '८', '९', '০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯', '೦', '೧', '೨', '೩', '೪', '೫', '೬', '೭', '೮', '೯', '၀', '၁', '၂', '၃', '၄', '၅', '၆', '၇', '၈', '၉'};
    private static final int HOURS_IN_HALF_DAY = 12;
    private static final String TAG = "SeslTimePickerSpinnerDelegate";
    private boolean SESL_DEBUG = false;
    private final View mAmPmMarginInside;
    private final View mAmPmMarginOutside;
    private final SeslNumberPicker mAmPmSpinner;
    private final EditText mAmPmSpinnerInput;
    private final String[] mAmPmStrings;
    private final int mDefaultHeight;
    private final int mDefaultWidth;
    private final TextView mDivider;
    private OnEditorActionListener mEditorActionListener = new C03935();
    private final View mEmpty1;
    private final View mEmpty2;
    private char mHourFormat;
    private final SeslNumberPicker mHourSpinner;
    private final EditText mHourSpinnerInput;
    private boolean mHourWithTwoDigit;
    private boolean mIs24HourView;
    private boolean mIsAm;
    private boolean mIsAmPmAutoFlipped = false;
    private boolean mIsEditTextMode;
    private boolean mIsEnabled = true;
    private final SeslNumberPicker mMinuteSpinner;
    private final EditText mMinuteSpinnerInput;
    private OnEditTextModeChangedListener mModeChangeListener = new C03924();
    private EditText[] mPickerTexts = new EditText[3];
    private Calendar mTempCalendar;

    /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$1 */
    class C03891 implements OnValueChangeListener {

        /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$1$1 */
        class C03881 implements Runnable {
            C03881() {
            }

            public void run() {
                SeslTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped = false;
                if (SeslTimePickerSpinnerDelegate.this.mAmPmSpinner != null) {
                    SeslTimePickerSpinnerDelegate.this.mAmPmSpinner.setEnabled(true);
                }
            }
        }

        C03891() {
        }

        public void onValueChange(SeslNumberPicker spinner, int oldVal, int newVal) {
            if (!(SeslTimePickerSpinnerDelegate.this.is24Hour() || SeslTimePickerSpinnerDelegate.this.mIsEditTextMode)) {
                int newValueNeedAmPmChange = 12;
                if (SeslTimePickerSpinnerDelegate.this.mHourFormat == 'K') {
                    newValueNeedAmPmChange = 0;
                }
                if ((oldVal == 11 && newVal == newValueNeedAmPmChange) || (oldVal == newValueNeedAmPmChange && newVal == 11)) {
                    boolean z;
                    SeslTimePickerSpinnerDelegate seslTimePickerSpinnerDelegate = SeslTimePickerSpinnerDelegate.this;
                    if (SeslTimePickerSpinnerDelegate.this.mAmPmSpinner.getValue() != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    seslTimePickerSpinnerDelegate.mIsAm = z;
                    SeslTimePickerSpinnerDelegate.this.mAmPmSpinner.performClick(false);
                    SeslTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped = true;
                    SeslTimePickerSpinnerDelegate.this.mAmPmSpinner.setEnabled(false);
                    new Handler().postDelayed(new C03881(), 500);
                }
            }
            SeslTimePickerSpinnerDelegate.this.onTimeChanged();
        }
    }

    /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$2 */
    class C03902 implements OnValueChangeListener {
        C03902() {
        }

        public void onValueChange(SeslNumberPicker spinner, int oldVal, int newVal) {
            SeslTimePickerSpinnerDelegate.this.onTimeChanged();
        }
    }

    /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$3 */
    class C03913 implements OnValueChangeListener {
        C03913() {
        }

        public void onValueChange(SeslNumberPicker picker, int oldVal, int newVal) {
            boolean z = true;
            if (!SeslTimePickerSpinnerDelegate.this.mAmPmSpinner.isEnabled()) {
                SeslTimePickerSpinnerDelegate.this.mAmPmSpinner.setEnabled(true);
            }
            if (SeslTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped) {
                SeslTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped = false;
            } else if (!SeslTimePickerSpinnerDelegate.this.mIsAm || newVal != 0) {
                if (SeslTimePickerSpinnerDelegate.this.mIsAm || newVal != 1) {
                    SeslTimePickerSpinnerDelegate seslTimePickerSpinnerDelegate = SeslTimePickerSpinnerDelegate.this;
                    if (newVal != 0) {
                        z = false;
                    }
                    seslTimePickerSpinnerDelegate.mIsAm = z;
                    SeslTimePickerSpinnerDelegate.this.updateAmPmControl();
                    SeslTimePickerSpinnerDelegate.this.onTimeChanged();
                    SeslTimePickerSpinnerDelegate.this.validCheck();
                }
            }
        }
    }

    /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$4 */
    class C03924 implements OnEditTextModeChangedListener {
        C03924() {
        }

        public void onEditTextModeChanged(SeslNumberPicker picker, boolean mode) {
            SeslTimePickerSpinnerDelegate.this.setEditTextMode(mode);
            SeslTimePickerSpinnerDelegate.this.updateModeState(mode);
        }
    }

    /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$5 */
    class C03935 implements OnEditorActionListener {
        C03935() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                SeslTimePickerSpinnerDelegate.this.updateInputState();
                SeslTimePickerSpinnerDelegate.this.setEditTextMode(false);
            }
            return false;
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C03941();
        private final int mHour;
        private final int mMinute;

        /* renamed from: android.support.v7.widget.SeslTimePickerSpinnerDelegate$SavedState$1 */
        static class C03941 implements Creator<SavedState> {
            C03941() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        private SavedState(Parcelable superState, int hour, int minute) {
            super(superState);
            this.mHour = hour;
            this.mMinute = minute;
        }

        private SavedState(Parcel in) {
            super(in);
            this.mHour = in.readInt();
            this.mMinute = in.readInt();
        }

        public int getHour() {
            return this.mHour;
        }

        public int getMinute() {
            return this.mMinute;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mHour);
            dest.writeInt(this.mMinute);
        }
    }

    private class SeslKeyListener implements OnKeyListener {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (SeslTimePickerSpinnerDelegate.this.SESL_DEBUG) {
                Log.d(SeslTimePickerSpinnerDelegate.TAG, event.toString());
            }
            if (event.getAction() != 1) {
                return false;
            }
            switch (keyCode) {
                case 23:
                    if (SeslTimePickerSpinnerDelegate.this.mDelegator.getResources().getConfiguration().keyboard == 3) {
                        return false;
                    }
                    break;
                case 61:
                case 66:
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    public class SeslTextWatcher implements TextWatcher {
        private int changedLen = 0;
        private int mId;
        private int mMaxLen;
        private int mNext;
        private String prevText;

        public SeslTextWatcher(int maxLen, int id) {
            this.mMaxLen = maxLen;
            this.mId = id;
            this.mNext = this.mId + 1 >= 2 ? -1 : this.mId + 1;
        }

        public void afterTextChanged(Editable view) {
            if (SeslTimePickerSpinnerDelegate.this.SESL_DEBUG) {
                Log.d("Picker", "aftertextchanged: " + view.toString());
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (SeslTimePickerSpinnerDelegate.this.SESL_DEBUG) {
                Log.d("Picker", "beforeTextChanged: " + s + ", " + start + ", " + count + ", " + after);
            }
            this.prevText = s.toString();
            this.changedLen = after;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (SeslTimePickerSpinnerDelegate.this.SESL_DEBUG) {
                Log.d("Picker", "onTextChanged: " + this.prevText);
                Log.d("Picker", "onTextChanged: " + s + ", " + start + ", " + before + ", " + count);
            }
            String tag = (String) SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].getTag();
            if (tag == null || !(tag.equals("onClick") || tag.equals("onLongClick"))) {
                int number;
                switch (this.mId) {
                    case 0:
                        if (this.changedLen != 1) {
                            return;
                        }
                        if (s.length() == this.mMaxLen) {
                            if (SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                                return;
                            }
                            return;
                        } else if (s.length() > 0) {
                            number = convertDigitCharacterToNumber(s.toString());
                            if ((number > 2 || (number > 1 && !SeslTimePickerSpinnerDelegate.this.is24Hour())) && SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    case 1:
                        if (this.changedLen != 1) {
                            return;
                        }
                        if (s.length() == this.mMaxLen) {
                            if (SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                                return;
                            }
                            return;
                        } else if (s.length() > 0) {
                            number = convertDigitCharacterToNumber(s.toString());
                            if (number >= 6 && number <= 9 && SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    default:
                        if (this.prevText.length() < s.length() && s.length() == this.mMaxLen && SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                            changeFocus();
                            return;
                        }
                        return;
                }
            }
            SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].setTag("");
        }

        private void changeFocus() {
            if (!((AccessibilityManager) SeslTimePickerSpinnerDelegate.this.mContext.getSystemService("accessibility")).isTouchExplorationEnabled()) {
                if (this.mNext >= 0) {
                    SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mNext].requestFocus();
                    if (SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                        SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].clearFocus();
                    }
                } else if (this.mId == 1) {
                    SeslTimePickerSpinnerDelegate.this.setMinute(Integer.parseInt(String.valueOf(SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].getText())));
                    SeslTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].selectAll();
                }
            }
        }

        private int convertDigitCharacterToNumber(String digitCharacter) {
            int idx = 0;
            for (char val : SeslTimePickerSpinnerDelegate.DIGIT_CHARACTERS) {
                if (digitCharacter.equals(Character.toString(val))) {
                    return idx % 10;
                }
                idx++;
            }
            return -1;
        }
    }

    public SeslTimePickerSpinnerDelegate(SeslTimePicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        LayoutInflater.from(this.mContext).inflate(C0247R.layout.sesl_time_picker_spinner, this.mDelegator, true);
        this.mHourSpinner = (SeslNumberPicker) delegator.findViewById(C0247R.id.hour);
        this.mHourSpinner.setPickerContentDescription(context.getResources().getString(C0247R.string.sesl_time_picker_hour));
        this.mHourSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mHourSpinner.setOnValueChangedListener(new C03891());
        this.mHourSpinnerInput = (EditText) this.mHourSpinner.findViewById(C0247R.id.numberpicker_input);
        this.mHourSpinner.setYearDateTimeInputMode();
        this.mHourSpinnerInput.setImeOptions(33554437);
        this.mHourSpinner.setMaxInputLength(2);
        this.mHourSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
        this.mDivider = (TextView) this.mDelegator.findViewById(C0247R.id.divider);
        if (this.mDivider != null) {
            setDividerText();
        }
        Resources res = this.mDelegator.getResources();
        int smallestScreenWidthDp = res.getConfiguration().smallestScreenWidthDp;
        if (smallestScreenWidthDp >= 600) {
            this.mDefaultWidth = res.getDimensionPixelSize(C0247R.dimen.sesl_time_picker_dialog_min_width);
        } else {
            this.mDefaultWidth = (int) (TypedValue.applyDimension(1, (float) smallestScreenWidthDp, res.getDisplayMetrics()) + 0.5f);
        }
        this.mDefaultHeight = res.getDimensionPixelSize(C0247R.dimen.sesl_time_picker_spinner_height);
        this.mMinuteSpinner = (SeslNumberPicker) this.mDelegator.findViewById(C0247R.id.minute);
        this.mMinuteSpinner.setYearDateTimeInputMode();
        this.mMinuteSpinner.setMinValue(0);
        this.mMinuteSpinner.setMaxValue(59);
        this.mMinuteSpinner.setOnLongPressUpdateInterval(100);
        this.mMinuteSpinner.setSkipValuesOnLongPressEnabled(true);
        this.mMinuteSpinner.setFormatter(SeslNumberPicker.getTwoDigitFormatter());
        this.mMinuteSpinner.setPickerContentDescription(context.getResources().getString(C0247R.string.sesl_time_picker_minute));
        this.mMinuteSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mMinuteSpinner.setOnValueChangedListener(new C03902());
        this.mMinuteSpinnerInput = (EditText) this.mMinuteSpinner.findViewById(C0247R.id.numberpicker_input);
        this.mMinuteSpinnerInput.setImeOptions(33554438);
        this.mMinuteSpinner.setMaxInputLength(2);
        this.mMinuteSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
        this.mAmPmStrings = getAmPmStrings(context);
        View amPmView = this.mDelegator.findViewById(C0247R.id.amPm);
        this.mEmpty1 = this.mDelegator.findViewById(C0247R.id.sesl_timepicker_empty_1);
        this.mEmpty2 = this.mDelegator.findViewById(C0247R.id.sesl_timepicker_empty_2);
        this.mAmPmMarginInside = this.mDelegator.findViewById(C0247R.id.sesl_timepicker_ampm_picker_margin_left);
        this.mAmPmMarginOutside = this.mDelegator.findViewById(C0247R.id.sesl_timepicker_ampm_picker_margin_right);
        this.mAmPmSpinner = (SeslNumberPicker) amPmView;
        this.mAmPmSpinner.setAmPm(true);
        this.mAmPmSpinner.setMinValue(0);
        this.mAmPmSpinner.setMaxValue(1);
        this.mAmPmSpinner.setDisplayedValues(this.mAmPmStrings);
        this.mAmPmSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mAmPmSpinner.setOnValueChangedListener(new C03913());
        this.mAmPmSpinnerInput = (EditText) this.mAmPmSpinner.findViewById(C0247R.id.numberpicker_input);
        this.mAmPmSpinnerInput.setInputType(0);
        this.mAmPmSpinnerInput.setCursorVisible(false);
        this.mAmPmSpinnerInput.setFocusable(false);
        this.mAmPmSpinnerInput.setFocusableInTouchMode(false);
        if (isAmPmAtStart()) {
            ViewGroup amPmParent = (ViewGroup) delegator.findViewById(C0247R.id.sesl_timepicker_layout);
            amPmParent.removeView(this.mAmPmMarginInside);
            amPmParent.removeView(this.mAmPmSpinner);
            amPmParent.removeView(this.mAmPmMarginOutside);
            amPmParent.addView(this.mAmPmMarginInside, 0);
            amPmParent.addView(this.mAmPmSpinner, 0);
            amPmParent.addView(this.mAmPmMarginOutside, 0);
        }
        getHourFormatData();
        updateHourControl();
        updateAmPmControl();
        setHour(this.mTempCalendar.get(11));
        setMinute(this.mTempCalendar.get(12));
        if (!isEnabled()) {
            setEnabled(false);
        }
        if (this.mDelegator.getImportantForAccessibility() == 0) {
            this.mDelegator.setImportantForAccessibility(1);
        }
        setTextWatcher();
    }

    private void updateModeState(boolean mode) {
        if (this.mIsEditTextMode != mode && !mode) {
            if (this.mHourSpinner.isEditTextMode()) {
                this.mHourSpinner.setEditTextMode(false);
            }
            if (this.mMinuteSpinner.isEditTextMode()) {
                this.mMinuteSpinner.setEditTextMode(false);
            }
            if (this.mAmPmSpinner.isEditTextMode()) {
                this.mAmPmSpinner.setEditTextMode(false);
            }
        }
    }

    private void getHourFormatData() {
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int lengthPattern = bestDateTimePattern.length();
        this.mHourWithTwoDigit = false;
        int i = 0;
        while (i < lengthPattern) {
            char c = bestDateTimePattern.charAt(i);
            if (c == 'H' || c == 'h' || c == 'K' || c == 'k') {
                this.mHourFormat = c;
                if (i + 1 < lengthPattern && c == bestDateTimePattern.charAt(i + 1)) {
                    this.mHourWithTwoDigit = true;
                    return;
                }
                return;
            }
            i++;
        }
    }

    private boolean isAmPmAtStart() {
        return DateFormat.getBestDateTimePattern(this.mCurrentLocale, "hm").startsWith("a");
    }

    private void setDividerText() {
        String separatorText;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int hourIndex = bestDateTimePattern.lastIndexOf(72);
        if (hourIndex == -1) {
            hourIndex = bestDateTimePattern.lastIndexOf(104);
        }
        if (hourIndex == -1) {
            separatorText = ":";
        } else {
            int minuteIndex = bestDateTimePattern.indexOf(109, hourIndex + 1);
            if (minuteIndex == -1) {
                separatorText = Character.toString(bestDateTimePattern.charAt(hourIndex + 1));
            } else {
                separatorText = bestDateTimePattern.substring(hourIndex + 1, minuteIndex);
            }
        }
        this.mDivider.setText(separatorText);
        Typeface defaultTypeface = Typeface.defaultFromStyle(0);
        Typeface legacyTypeface = Typeface.create("sec-roboto-condensed-light", 0);
        Typeface pickerTypeface = Typeface.create("sec-roboto-light", 0);
        if (defaultTypeface.equals(pickerTypeface)) {
            if (legacyTypeface.equals(pickerTypeface)) {
                pickerTypeface = Typeface.create("sans-serif-thin", 0);
            } else {
                pickerTypeface = legacyTypeface;
            }
        }
        String themeTypeFace = System.getString(this.mContext.getContentResolver(), "theme_font_clock");
        if (!(themeTypeFace == null || themeTypeFace.equals(""))) {
            this.mDivider.setTypeface(getFontTypeface(themeTypeFace));
        }
        this.mDivider.setTypeface(pickerTypeface);
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

    public void setHour(int hour) {
        setCurrentHour(hour, true);
    }

    private void setCurrentHour(int currentHour, boolean notifyTimeChanged) {
        if (currentHour != getHour()) {
            if (!is24Hour()) {
                if (currentHour >= 12) {
                    this.mIsAm = false;
                    if (currentHour > 12) {
                        currentHour -= 12;
                    }
                } else {
                    this.mIsAm = true;
                    if (currentHour == 0) {
                        currentHour = 12;
                    }
                }
                updateAmPmControl();
            }
            this.mHourSpinner.setValue(currentHour);
            if (notifyTimeChanged) {
                onTimeChanged();
            }
        }
    }

    public int getHour() {
        int currentHour = this.mHourSpinner.getValue();
        if (is24Hour()) {
            return currentHour;
        }
        if (this.mIsAm) {
            return currentHour % 12;
        }
        return (currentHour % 12) + 12;
    }

    public void setMinute(int minute) {
        if (minute != getMinute()) {
            this.mMinuteSpinner.setValue(minute);
            onTimeChanged();
        } else if (isCharacterNumberLanguage()) {
            this.mMinuteSpinner.setValue(minute);
        }
    }

    public int getMinute() {
        return this.mMinuteSpinner.getValue();
    }

    public void setIs24Hour(boolean is24HourView) {
        if (this.mIs24HourView != is24HourView) {
            int currentHour = getHour();
            this.mIs24HourView = is24HourView;
            getHourFormatData();
            updateHourControl();
            setCurrentHour(currentHour, false);
            updateAmPmControl();
        }
    }

    public boolean is24Hour() {
        return this.mIs24HourView;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mOnTimeChangedListener = onTimeChangedListener;
    }

    public void setEnabled(boolean enabled) {
        this.mMinuteSpinner.setEnabled(enabled);
        if (this.mDivider != null) {
            this.mDivider.setEnabled(enabled);
        }
        this.mHourSpinner.setEnabled(enabled);
        this.mAmPmSpinner.setEnabled(enabled);
        this.mIsEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public int getBaseline() {
        return this.mHourSpinner.getBaseline();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    public Parcelable onSaveInstanceState(Parcelable superState) {
        return new SavedState(superState, getHour(), getMinute());
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setHour(ss.getHour());
        setMinute(ss.getMinute());
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        int flags;
        if (this.mIs24HourView) {
            flags = 1 | 128;
        } else {
            flags = 1 | 64;
        }
        this.mTempCalendar.set(11, getHour());
        this.mTempCalendar.set(12, getMinute());
        event.getText().add(DateUtils.formatDateTime(this.mContext, this.mTempCalendar.getTimeInMillis(), flags));
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(TimePicker.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        info.setClassName(TimePicker.class.getName());
    }

    private void updateInputState() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        if (inputMethodManager == null) {
            return;
        }
        if (inputMethodManager.isActive(this.mHourSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mHourSpinnerInput != null) {
                this.mHourSpinnerInput.clearFocus();
            }
        } else if (inputMethodManager.isActive(this.mMinuteSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mMinuteSpinnerInput != null) {
                this.mMinuteSpinnerInput.clearFocus();
            }
        } else if (inputMethodManager.isActive(this.mAmPmSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mAmPmSpinnerInput != null) {
                this.mAmPmSpinnerInput.clearFocus();
            }
        }
    }

    private void updateAmPmControl() {
        if (is24Hour()) {
            this.mAmPmSpinner.setVisibility(8);
            this.mAmPmMarginInside.setVisibility(8);
            this.mAmPmMarginOutside.setVisibility(8);
            this.mEmpty1.setVisibility(0);
            this.mEmpty2.setVisibility(0);
        } else {
            this.mAmPmSpinner.setValue(this.mIsAm ? 0 : 1);
            this.mAmPmSpinner.setVisibility(0);
            this.mAmPmMarginInside.setVisibility(0);
            this.mAmPmMarginOutside.setVisibility(0);
            this.mEmpty1.setVisibility(8);
            this.mEmpty2.setVisibility(8);
        }
        this.mDelegator.sendAccessibilityEvent(4);
    }

    private void validCheck() {
        if (this.mIsEditTextMode) {
            if (this.mHourSpinnerInput != null && this.mHourSpinnerInput.hasFocus()) {
                if (!TextUtils.isEmpty(this.mHourSpinnerInput.getText())) {
                    int text = Integer.parseInt(String.valueOf(this.mHourSpinnerInput.getText()));
                    if (!is24Hour()) {
                        if (!this.mIsAm && text != 12) {
                            text += 12;
                        } else if (this.mIsAm && text == 12) {
                            text = 0;
                        }
                    }
                    setHour(text);
                    this.mHourSpinnerInput.selectAll();
                } else {
                    return;
                }
            }
            if (this.mMinuteSpinnerInput != null && this.mMinuteSpinnerInput.hasFocus() && !TextUtils.isEmpty(this.mMinuteSpinnerInput.getText())) {
                setMinute(Integer.parseInt(String.valueOf(this.mMinuteSpinnerInput.getText())));
                this.mMinuteSpinnerInput.selectAll();
            }
        }
    }

    public void setCurrentLocale(Locale locale) {
        super.setCurrentLocale(locale);
        this.mTempCalendar = Calendar.getInstance(locale);
    }

    private void onTimeChanged() {
        this.mDelegator.sendAccessibilityEvent(4);
        if (this.mOnTimeChangedListener != null) {
            this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getHour(), getMinute());
        }
    }

    private void updateHourControl() {
        if (is24Hour()) {
            if (this.mHourFormat == 'k') {
                this.mHourSpinner.setMinValue(1);
                this.mHourSpinner.setMaxValue(24);
            } else {
                this.mHourSpinner.setMinValue(0);
                this.mHourSpinner.setMaxValue(23);
            }
        } else if (this.mHourFormat == 'K') {
            this.mHourSpinner.setMinValue(0);
            this.mHourSpinner.setMaxValue(11);
        } else {
            this.mHourSpinner.setMinValue(1);
            this.mHourSpinner.setMaxValue(12);
        }
        this.mHourSpinner.setFormatter(this.mHourWithTwoDigit ? SeslNumberPicker.getTwoDigitFormatter() : null);
    }

    public static String[] getAmPmStrings(Context context) {
        String[] result = new String[2];
        Object localeData = SeslLocaleDataReflector.get(context.getResources().getConfiguration().locale);
        if (localeData != null) {
            String[] amPm = SeslLocaleDataReflector.getField_amPm(localeData);
            String narrowAm = SeslLocaleDataReflector.getField_narrowAm(localeData);
            String narrowPm = SeslLocaleDataReflector.getField_narrowPm(localeData);
            String amPm0 = amPm[0];
            String amPm1 = amPm[1];
            if (isMeaLanguage()) {
                result[0] = amPm0;
                result[1] = amPm1;
                return result;
            }
            if (amPm0.length() <= 4) {
                narrowAm = amPm0;
            }
            result[0] = narrowAm;
            if (amPm1.length() <= 4) {
                narrowPm = amPm1;
            }
            result[1] = narrowPm;
            return result;
        }
        Log.e(TAG, "LocaleData failed. Use DateFormatSymbols for ampm");
        return new DateFormatSymbols().getAmPmStrings();
    }

    private static boolean isMeaLanguage() {
        String language = Locale.getDefault().getLanguage();
        return "lo".equals(language) || "ar".equals(language) || "fa".equals(language) || "ur".equals(language);
    }

    private static boolean isCharacterNumberLanguage() {
        String language = Locale.getDefault().getLanguage();
        return "lo".equals(language) || "ar".equals(language) || "fa".equals(language) || "ur".equals(language) || "my".equals(language);
    }

    public void setOnEditTextModeChangedListener(SeslTimePicker.OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mOnEditTextModeChangedListener = onEditTextModeChangedListener;
    }

    public void setEditTextMode(boolean editTextMode) {
        if (this.mIsEditTextMode != editTextMode) {
            this.mIsEditTextMode = editTextMode;
            InputMethodManager inputMethodManager = (InputMethodManager) this.mDelegator.getContext().getSystemService("input_method");
            this.mAmPmSpinner.setEditTextMode(editTextMode);
            this.mHourSpinner.setEditTextMode(editTextMode);
            this.mMinuteSpinner.setEditTextMode(editTextMode);
            if (inputMethodManager != null) {
                if (this.mIsEditTextMode) {
                    inputMethodManager.showSoftInput(this.mHourSpinnerInput, 0);
                } else {
                    inputMethodManager.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
                }
            }
            if (this.mOnEditTextModeChangedListener != null) {
                this.mOnEditTextModeChangedListener.onEditTextModeChanged(this.mDelegator, editTextMode);
            }
        }
    }

    public boolean isEditTextMode() {
        return this.mIsEditTextMode;
    }

    public void startAnimation(int delayTime, SeslAnimationListener listener) {
        if (isAmPmAtStart()) {
            this.mAmPmSpinner.startAnimation(delayTime, null);
            this.mHourSpinner.startAnimation(delayTime + 55, null);
            this.mMinuteSpinner.startAnimation(delayTime + 110, listener);
            return;
        }
        this.mHourSpinner.startAnimation(delayTime, null);
        this.mMinuteSpinner.startAnimation(delayTime + 55, listener);
        this.mAmPmSpinner.startAnimation(delayTime + 110, null);
    }

    public EditText getEditText(int picker) {
        switch (picker) {
            case 0:
                return this.mHourSpinner.getEditText();
            case 2:
                return this.mAmPmSpinner.getEditText();
            default:
                return this.mMinuteSpinner.getEditText();
        }
    }

    public SeslNumberPicker getNumberPicker(int picker) {
        switch (picker) {
            case 0:
                return this.mHourSpinner;
            case 2:
                return this.mAmPmSpinner;
            default:
                return this.mMinuteSpinner;
        }
    }

    public int getDefaultWidth() {
        return this.mDefaultWidth;
    }

    public int getDefaultHeight() {
        return this.mDefaultHeight;
    }

    private void setTextWatcher() {
        this.mPickerTexts[0] = this.mHourSpinner.getEditText();
        this.mPickerTexts[1] = this.mMinuteSpinner.getEditText();
        this.mPickerTexts[0].addTextChangedListener(new SeslTextWatcher(2, 0));
        this.mPickerTexts[1].addTextChangedListener(new SeslTextWatcher(2, 1));
        this.mPickerTexts[0].setOnKeyListener(new SeslKeyListener());
        this.mPickerTexts[1].setOnKeyListener(new SeslKeyListener());
    }
}
