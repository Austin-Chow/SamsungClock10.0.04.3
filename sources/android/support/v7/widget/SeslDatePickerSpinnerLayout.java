package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslNumberPicker.OnEditTextModeChangedListener;
import android.support.v7.widget.SeslNumberPicker.OnValueChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import dalvik.system.PathClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

class SeslDatePickerSpinnerLayout extends LinearLayout {
    private static final int HUNGARIAN_MONTH_TEXT_SIZE_DIFF = 4;
    private static final boolean SESL_DEBUG = false;
    private static final String TAG = SeslDatePickerSpinnerLayout.class.getSimpleName();
    private final int FORMAT_DDMMYYYY;
    private final int FORMAT_MMDDYYYY;
    private final int FORMAT_YYYYDDMM;
    private final int FORMAT_YYYYMMDD;
    private final int PICKER_DAY;
    private final int PICKER_MONTH;
    private final int PICKER_YEAR;
    private Context mContext;
    private Calendar mCurrentDate;
    private Locale mCurrentLocale;
    private SeslDatePicker mDatePicker;
    private final SeslNumberPicker mDaySpinner;
    private final EditText mDaySpinnerInput;
    private OnEditorActionListener mEditorActionListener;
    private Method mGetDayLengthOfMethod;
    private boolean mIsEditTextMode;
    private boolean mIsLeapMonth;
    private Method mIsLeapMonthMethod;
    private boolean mIsLunar;
    private int mLunarCurrentDay;
    private int mLunarCurrentMonth;
    private int mLunarCurrentYear;
    private int mLunarTempDay;
    private int mLunarTempMonth;
    private int mLunarTempYear;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private OnEditTextModeChangedListener mModeChangeListener;
    private final SeslNumberPicker mMonthSpinner;
    private final EditText mMonthSpinnerInput;
    private int mNumberOfMonths;
    private SeslDatePicker.OnEditTextModeChangedListener mOnEditTextModeChangedListener;
    private OnSpinnerDateChangedListener mOnSpinnerDateChangedListener;
    PathClassLoader mPathClassLoader;
    private EditText[] mPickerTexts;
    private final View mPrimaryEmptyView;
    private final View mSecondaryEmptyView;
    private String[] mShortMonths;
    private Object mSolarLunarTables;
    private final LinearLayout mSpinners;
    private Calendar mTempDate;
    private Toast mToast;
    private String mToastText;
    private final SeslNumberPicker mYearSpinner;
    private final EditText mYearSpinnerInput;

    public interface OnSpinnerDateChangedListener {
        void onDateChanged(SeslDatePickerSpinnerLayout seslDatePickerSpinnerLayout, int i, int i2, int i3);
    }

    /* renamed from: android.support.v7.widget.SeslDatePickerSpinnerLayout$1 */
    class C03661 implements OnEditTextModeChangedListener {
        C03661() {
        }

        public void onEditTextModeChanged(SeslNumberPicker picker, boolean mode) {
            SeslDatePickerSpinnerLayout.this.setEditTextMode(mode);
            SeslDatePickerSpinnerLayout.this.updateModeState(mode);
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePickerSpinnerLayout$2 */
    class C03672 implements OnValueChangeListener {
        C03672() {
        }

        public void onValueChange(SeslNumberPicker picker, int oldVal, int newVal) {
            int maxDayOfMonth;
            SeslDatePickerSpinnerLayout.this.mTempDate.setTimeInMillis(SeslDatePickerSpinnerLayout.this.mCurrentDate.getTimeInMillis());
            if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                SeslDatePickerSpinnerLayout.this.mLunarTempYear = SeslDatePickerSpinnerLayout.this.mLunarCurrentYear;
                SeslDatePickerSpinnerLayout.this.mLunarTempMonth = SeslDatePickerSpinnerLayout.this.mLunarCurrentMonth;
                SeslDatePickerSpinnerLayout.this.mLunarTempDay = SeslDatePickerSpinnerLayout.this.mLunarCurrentDay;
            }
            boolean monthChanged = false;
            boolean dayChanged = false;
            if (picker.equals(SeslDatePickerSpinnerLayout.this.mDaySpinner)) {
                maxDayOfMonth = SeslDatePickerSpinnerLayout.this.mTempDate.getActualMaximum(5);
                if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                    maxDayOfMonth = SeslDatePickerSpinnerLayout.this.getLunarMaxDayOfMonth(SeslDatePickerSpinnerLayout.this.mTempDate.get(1), SeslDatePickerSpinnerLayout.this.mTempDate.get(2), SeslDatePickerSpinnerLayout.this.mIsLeapMonth);
                }
                if ((oldVal == maxDayOfMonth && newVal == 1) || (oldVal == 1 && newVal == maxDayOfMonth)) {
                    SeslDatePickerSpinnerLayout.this.mTempDate.set(5, newVal);
                    if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                        SeslDatePickerSpinnerLayout.this.mLunarTempDay = newVal;
                    }
                } else {
                    SeslDatePickerSpinnerLayout.this.mTempDate.add(5, newVal - oldVal);
                    if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                        SeslDatePickerSpinnerLayout.this.mLunarTempDay = SeslDatePickerSpinnerLayout.this.mLunarTempDay + (newVal - oldVal);
                    }
                }
            } else {
                if (picker.equals(SeslDatePickerSpinnerLayout.this.mMonthSpinner)) {
                    if ((oldVal == 11 && newVal == 0) || (oldVal == 0 && newVal == 11)) {
                        SeslDatePickerSpinnerLayout.this.mTempDate.set(2, newVal);
                        if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                            SeslDatePickerSpinnerLayout.this.mLunarTempMonth = newVal;
                        }
                    } else {
                        SeslDatePickerSpinnerLayout.this.mTempDate.add(2, newVal - oldVal);
                        if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                            SeslDatePickerSpinnerLayout.this.mLunarTempMonth = SeslDatePickerSpinnerLayout.this.mLunarTempMonth + (newVal - oldVal);
                        }
                    }
                    dayChanged = true;
                } else {
                    if (picker.equals(SeslDatePickerSpinnerLayout.this.mYearSpinner)) {
                        SeslDatePickerSpinnerLayout.this.mTempDate.add(1, newVal - oldVal);
                        if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                            SeslDatePickerSpinnerLayout.this.mLunarTempYear = SeslDatePickerSpinnerLayout.this.mLunarTempYear + (newVal - oldVal);
                        }
                        monthChanged = true;
                        dayChanged = true;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }
            if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                maxDayOfMonth = SeslDatePickerSpinnerLayout.this.getLunarMaxDayOfMonth(SeslDatePickerSpinnerLayout.this.mLunarTempYear, SeslDatePickerSpinnerLayout.this.mLunarTempMonth, SeslDatePickerSpinnerLayout.this.mIsLeapMonth);
                if (SeslDatePickerSpinnerLayout.this.mLunarTempMonth > maxDayOfMonth) {
                    SeslDatePickerSpinnerLayout.this.mLunarTempDay = maxDayOfMonth;
                }
                if (SeslDatePickerSpinnerLayout.this.mIsLeapMonth) {
                    Object isLeap = SeslDatePickerSpinnerLayout.this.invoke(SeslDatePickerSpinnerLayout.this.mSolarLunarTables, SeslDatePickerSpinnerLayout.this.mIsLeapMonthMethod, Integer.valueOf(SeslDatePickerSpinnerLayout.this.mLunarTempYear), Integer.valueOf(SeslDatePickerSpinnerLayout.this.mLunarTempMonth));
                    if ((isLeap instanceof Boolean) && !((Boolean) isLeap).booleanValue()) {
                        SeslDatePickerSpinnerLayout.this.mIsLeapMonth = false;
                    }
                }
            }
            int year = SeslDatePickerSpinnerLayout.this.mTempDate.get(1);
            int month = SeslDatePickerSpinnerLayout.this.mTempDate.get(2);
            int dayOfMonth = SeslDatePickerSpinnerLayout.this.mTempDate.get(5);
            if (SeslDatePickerSpinnerLayout.this.mIsLunar) {
                year = SeslDatePickerSpinnerLayout.this.mLunarTempYear;
                month = SeslDatePickerSpinnerLayout.this.mLunarTempMonth;
                dayOfMonth = SeslDatePickerSpinnerLayout.this.mLunarTempDay;
            }
            SeslDatePickerSpinnerLayout.this.setDate(year, month, dayOfMonth);
            if (null != null || monthChanged || dayChanged) {
                SeslDatePickerSpinnerLayout.this.updateSpinners(false, false, monthChanged, dayChanged);
            }
            SeslDatePickerSpinnerLayout.this.notifyDateChanged();
        }
    }

    /* renamed from: android.support.v7.widget.SeslDatePickerSpinnerLayout$3 */
    class C03683 implements OnEditorActionListener {
        C03683() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                SeslDatePickerSpinnerLayout.this.updateInputState();
                SeslDatePickerSpinnerLayout.this.setEditTextMode(false);
            }
            return false;
        }
    }

    private class SeslKeyListener implements OnKeyListener {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            SeslDatePickerSpinnerLayout.this.seslLog(event.toString());
            if (event.getAction() != 1) {
                return false;
            }
            switch (keyCode) {
                case 23:
                    if (SeslDatePickerSpinnerLayout.this.getResources().getConfiguration().keyboard == 3) {
                        return false;
                    }
                    return false;
                case 61:
                    return true;
                case 66:
                    if (SeslDatePickerSpinnerLayout.this.isEditTextMode()) {
                        if ((((EditText) v).getImeOptions() & 5) == 5) {
                            View next = FocusFinder.getInstance().findNextFocus(SeslDatePickerSpinnerLayout.this.mDatePicker, v, 2);
                            if (next == null) {
                                return true;
                            }
                            next.requestFocus();
                        } else if ((((EditText) v).getImeOptions() & 6) == 6) {
                            SeslDatePickerSpinnerLayout.this.updateInputState();
                            SeslDatePickerSpinnerLayout.this.setEditTextMode(false);
                        }
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    public class SeslTextWatcher implements TextWatcher {
        private final int INVALID_POSITION_ID = -1;
        private final int LAST_POSITION_ID = 2;
        private int mChangedLen = 0;
        private int mCheck;
        private int mId;
        private boolean mIsMonth;
        private int mMaxLen;
        private int mNext;
        private String mPrevText;
        final /* synthetic */ SeslDatePickerSpinnerLayout this$0;

        public SeslTextWatcher(SeslDatePickerSpinnerLayout this$0, int maxLen, int id, boolean month) {
            int i = -1;
            this.this$0 = this$0;
            this.mMaxLen = maxLen;
            this.mId = id;
            this.mIsMonth = month;
            this.mCheck = this.mId - 1;
            if (this.mCheck < 0) {
                this.mCheck = 2;
            }
            if (this.mId + 1 <= 2) {
                i = this.mId + 1;
            }
            this.mNext = i;
        }

        public void afterTextChanged(Editable view) {
            this.this$0.seslLog("[" + this.mId + "] afterTextChanged: " + view.toString());
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.this$0.seslLog("[" + this.mId + "] beforeTextChanged: " + s + ", " + start + ", " + count + ", " + after);
            this.mPrevText = s.toString();
            this.mChangedLen = after;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.this$0.seslLog("[" + this.mId + "] onTextChanged: " + this.mPrevText + " -> " + s);
            int length = s.length();
            String text = s.toString();
            String tag = (String) this.this$0.mPickerTexts[this.mId].getTag();
            if (tag != null && ("onClick".equals(tag) || "onLongClick".equals(tag))) {
                this.this$0.seslLog("[" + this.mId + "] TAG exists: " + tag);
            } else if (!this.this$0.mPickerTexts[this.mId].isFocused()) {
            } else {
                int month;
                if (this.mIsMonth) {
                    if (this.this$0.usingNumericMonths() && this.mChangedLen == 1) {
                        this.this$0.seslLog("[" + this.mId + "] Samsung Keypad Num Month");
                        int monthMinValue = this.this$0.mMonthSpinner.getMinValue();
                        month = Integer.parseInt(text);
                        if (length == this.mMaxLen) {
                            if (month >= monthMinValue) {
                                changeFocus();
                                return;
                            } else if (Character.getNumericValue(text.charAt(0)) < 2) {
                                showInvalidValueEnteredToast(Character.toString(text.charAt(0)), 1);
                                return;
                            } else {
                                showInvalidValueEnteredToast("", 0);
                                return;
                            }
                        } else if (length <= 0) {
                            return;
                        } else {
                            if (monthMinValue >= 10 && "0".equals(text)) {
                                showInvalidValueEnteredToast("", 0);
                                return;
                            } else if (!"1".equals(text) && !"0".equals(text)) {
                                if (month < monthMinValue) {
                                    showInvalidValueEnteredToast("", 0);
                                    return;
                                } else {
                                    changeFocus();
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                    }
                    if (!isNumericStr(this.mPrevText)) {
                        if (length >= this.mMaxLen) {
                            if (!isMeaLanguage()) {
                                changeFocus();
                            } else if (TextUtils.isEmpty(this.mPrevText) && isMonthStr(text)) {
                                changeFocus();
                            }
                        } else if ((isSwaLanguage() || isFarsiLanguage()) && length > 0 && !isNumericStr(text)) {
                            changeFocus();
                        }
                    }
                } else if (this.mChangedLen != 1) {
                } else {
                    if (this.mMaxLen < 3) {
                        int dayMinValue = this.this$0.mDaySpinner.getMinValue();
                        int day = Integer.parseInt(text);
                        if (this.mPrevText.length() >= length || length != this.mMaxLen) {
                            if ((dayMinValue >= 10 && day == 0) || ((dayMinValue >= 20 && (day == 0 || day == 1)) || (dayMinValue >= 30 && (day == 0 || day == 1 || day == 2)))) {
                                showInvalidValueEnteredToast("", 0);
                                return;
                            } else if (day <= 3) {
                                return;
                            } else {
                                if (day < dayMinValue) {
                                    showInvalidValueEnteredToast("", 0);
                                    return;
                                } else {
                                    changeFocus();
                                    return;
                                }
                            }
                        } else if (day >= dayMinValue) {
                            changeFocus();
                            return;
                        } else if (Character.getNumericValue(text.charAt(0)) < 4) {
                            showInvalidValueEnteredToast(Character.toString(text.charAt(0)), 1);
                            return;
                        } else {
                            showInvalidValueEnteredToast("", 0);
                            return;
                        }
                    }
                    int yearMinValue = this.this$0.mYearSpinner.getMinValue();
                    int yearMaxValue = this.this$0.mYearSpinner.getMaxValue();
                    int year = Integer.parseInt(text);
                    if (this.mPrevText.length() >= length || length != this.mMaxLen) {
                        int divider = (int) (1000.0d / Math.pow(10.0d, (double) (length - 1)));
                        String setValue = "";
                        if (length != 1) {
                            setValue = text.substring(0, length - 1);
                        }
                        if (year < yearMinValue / divider || year > yearMaxValue / divider) {
                            showInvalidValueEnteredToast(setValue, length - 1);
                        }
                    } else if (year < yearMinValue || year > yearMaxValue) {
                        showInvalidValueEnteredToast(text.substring(0, 3), 3);
                    } else {
                        if (this.this$0.usingNumericMonths()) {
                            month = this.this$0.mMonthSpinner.getValue() - 1;
                        } else {
                            month = this.this$0.mMonthSpinner.getValue();
                        }
                        this.this$0.mTempDate.clear();
                        this.this$0.mTempDate.set(year, month, this.this$0.mDaySpinner.getValue());
                        Calendar minDate = Calendar.getInstance();
                        minDate.clear();
                        minDate.set(this.this$0.mMinDate.get(1), this.this$0.mMinDate.get(2), this.this$0.mMinDate.get(5));
                        if (this.this$0.mTempDate.before(minDate) || this.this$0.mTempDate.after(this.this$0.mMaxDate)) {
                            showInvalidValueEnteredToast(text.substring(0, 3), 3);
                            return;
                        }
                        changeFocus();
                    }
                }
            }
        }

        private void showInvalidValueEnteredToast(String setValue, int selection) {
            this.this$0.mPickerTexts[this.mId].setText(setValue);
            if (selection != 0) {
                this.this$0.mPickerTexts[this.mId].setSelection(selection);
            }
            if (this.this$0.mToast == null) {
                this.this$0.mToast = Toast.makeText(this.this$0.mContext, this.this$0.mToastText, 0);
            }
            this.this$0.mToast.show();
        }

        private boolean isSwaLanguage() {
            String language = this.this$0.mCurrentLocale.getLanguage();
            return "hi".equals(language) || "ta".equals(language) || "ml".equals(language) || "te".equals(language) || "or".equals(language) || "ne".equals(language) || "as".equals(language) || "bn".equals(language) || "gu".equals(language) || "si".equals(language) || "pa".equals(language) || "kn".equals(language) || "mr".equals(language);
        }

        private boolean isMeaLanguage() {
            String language = this.this$0.mCurrentLocale.getLanguage();
            return "ar".equals(language) || "fa".equals(language) || "ur".equals(language);
        }

        private boolean isFarsiLanguage() {
            return "fa".equals(this.this$0.mCurrentLocale.getLanguage());
        }

        private boolean isNumericStr(String s) {
            return !TextUtils.isEmpty(s) && Character.isDigit(s.charAt(0));
        }

        private boolean isMonthStr(String s) {
            for (int i = 0; i < this.this$0.mNumberOfMonths; i++) {
                if (s.equals(this.this$0.mShortMonths[i])) {
                    return true;
                }
            }
            return false;
        }

        private void changeFocus() {
            AccessibilityManager manager = (AccessibilityManager) this.this$0.mContext.getSystemService("accessibility");
            if (manager == null || !manager.isTouchExplorationEnabled()) {
                this.this$0.seslLog("[" + this.mId + "] changeFocus() mNext : " + this.mNext + ", mCheck : " + this.mCheck);
                if (this.mNext >= 0) {
                    if (!this.this$0.mPickerTexts[this.mCheck].isFocused()) {
                        this.this$0.mPickerTexts[this.mNext].requestFocus();
                    }
                    if (this.this$0.mPickerTexts[this.mId].isFocused()) {
                        this.this$0.mPickerTexts[this.mId].clearFocus();
                    }
                }
            }
        }
    }

    public SeslDatePickerSpinnerLayout(Context context) {
        this(context, null);
    }

    public SeslDatePickerSpinnerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 16843612);
    }

    public SeslDatePickerSpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslDatePickerSpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIsLunar = false;
        this.mIsLeapMonth = false;
        this.mPathClassLoader = null;
        this.mModeChangeListener = new C03661();
        this.mPickerTexts = new EditText[3];
        this.PICKER_DAY = 0;
        this.PICKER_MONTH = 1;
        this.PICKER_YEAR = 2;
        this.FORMAT_MMDDYYYY = 0;
        this.FORMAT_DDMMYYYY = 1;
        this.FORMAT_YYYYMMDD = 2;
        this.FORMAT_YYYYDDMM = 3;
        this.mEditorActionListener = new C03683();
        this.mContext = context;
        LayoutInflater.from(this.mContext).inflate(C0247R.layout.sesl_date_picker_spinner, this, true);
        this.mCurrentLocale = Locale.getDefault();
        setCurrentLocale(this.mCurrentLocale);
        OnValueChangeListener onChangeListener = new C03672();
        this.mSpinners = (LinearLayout) findViewById(C0247R.id.sesl_date_picker_pickers);
        this.mPrimaryEmptyView = findViewById(C0247R.id.sesl_date_picker_primary_empty);
        this.mSecondaryEmptyView = findViewById(C0247R.id.sesl_date_picker_secondary_empty);
        this.mDaySpinner = (SeslNumberPicker) findViewById(C0247R.id.sesl_date_picker_spinner_day);
        this.mDaySpinnerInput = (EditText) this.mDaySpinner.findViewById(C0247R.id.numberpicker_input);
        this.mDaySpinner.setFormatter(SeslNumberPicker.getTwoDigitFormatter());
        this.mDaySpinner.setOnValueChangedListener(onChangeListener);
        this.mDaySpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mDaySpinner.setMaxInputLength(2);
        this.mDaySpinner.setYearDateTimeInputMode();
        this.mMonthSpinner = (SeslNumberPicker) findViewById(C0247R.id.sesl_date_picker_spinner_month);
        this.mMonthSpinnerInput = (EditText) this.mMonthSpinner.findViewById(C0247R.id.numberpicker_input);
        if (usingNumericMonths()) {
            this.mMonthSpinner.setMinValue(1);
            this.mMonthSpinner.setMaxValue(12);
            this.mMonthSpinner.setYearDateTimeInputMode();
            this.mMonthSpinner.setMaxInputLength(2);
        } else {
            this.mMonthSpinner.setMinValue(0);
            this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
            this.mMonthSpinner.setFormatter(null);
            this.mMonthSpinner.setDisplayedValues(this.mShortMonths);
            this.mMonthSpinnerInput.setInputType(1);
            this.mMonthSpinner.setMonthInputMode();
        }
        this.mMonthSpinner.setOnValueChangedListener(onChangeListener);
        this.mMonthSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mYearSpinner = (SeslNumberPicker) findViewById(C0247R.id.sesl_date_picker_spinner_year);
        this.mYearSpinnerInput = (EditText) this.mYearSpinner.findViewById(C0247R.id.numberpicker_input);
        this.mYearSpinner.setOnValueChangedListener(onChangeListener);
        this.mYearSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mYearSpinner.setMaxInputLength(4);
        this.mYearSpinner.setYearDateTimeInputMode();
        Typeface datePickerTypeface = Typeface.create("sec-roboto-light", 0);
        this.mDaySpinner.setTextTypeface(datePickerTypeface);
        this.mMonthSpinner.setTextTypeface(datePickerTypeface);
        this.mYearSpinner.setTextTypeface(datePickerTypeface);
        Resources res = context.getResources();
        int numberTextSize = res.getInteger(C0247R.integer.sesl_date_picker_spinner_number_text_size);
        int textSize = res.getInteger(C0247R.integer.sesl_date_picker_spinner_number_text_size_small);
        this.mToastText = res.getString(C0247R.string.sesl_number_picker_invalid_value_entered);
        this.mDaySpinner.setTextSize((float) numberTextSize);
        this.mYearSpinner.setTextSize((float) textSize);
        String language = this.mCurrentLocale.getLanguage();
        if ("my".equals(language) || "ml".equals(language) || "bn".equals(language) || "ar".equals(language) || "fa".equals(language)) {
            textSize = res.getInteger(C0247R.integer.sesl_date_picker_spinner_long_month_text_size);
        } else if ("ga".equals(language)) {
            textSize = res.getInteger(C0247R.integer.sesl_date_picker_spinner_long_month_text_size) - 1;
        } else if ("hu".equals(language)) {
            textSize -= 4;
        }
        if (usingNumericMonths()) {
            this.mMonthSpinner.setTextSize((float) numberTextSize);
        } else {
            this.mMonthSpinner.setTextSize((float) textSize);
        }
        this.mDaySpinner.setPickerContentDescription(context.getResources().getString(C0247R.string.sesl_date_picker_day));
        this.mMonthSpinner.setPickerContentDescription(context.getResources().getString(C0247R.string.sesl_date_picker_month));
        this.mYearSpinner.setPickerContentDescription(context.getResources().getString(C0247R.string.sesl_date_picker_year));
        this.mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        init(this.mCurrentDate.get(1), this.mCurrentDate.get(2), this.mCurrentDate.get(5));
        reorderSpinners();
    }

    public void init(int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
        updateSpinners(true, true, true, true);
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        if (isNewDate(year, month, dayOfMonth)) {
            setDate(year, month, dayOfMonth);
            updateSpinners(true, true, true, true);
        }
    }

    public int getYear() {
        if (this.mIsLunar) {
            return this.mLunarCurrentYear;
        }
        return this.mCurrentDate.get(1);
    }

    public int getMonth() {
        if (this.mIsLunar) {
            return this.mLunarCurrentMonth;
        }
        return this.mCurrentDate.get(2);
    }

    public int getDayOfMonth() {
        if (this.mIsLunar) {
            return this.mLunarCurrentDay;
        }
        return this.mCurrentDate.get(5);
    }

    public void setMinDate(long minDate) {
        this.mMinDate.setTimeInMillis(minDate);
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
        }
        updateSpinners(true, true, true, true);
    }

    public Calendar getMinDate() {
        return this.mMinDate;
    }

    public void setMaxDate(long maxDate) {
        this.mMaxDate.setTimeInMillis(maxDate);
        if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
        updateSpinners(true, true, true, true);
    }

    public Calendar getMaxDate() {
        return this.mMaxDate;
    }

    public void setEnabled(boolean enabled) {
        this.mDaySpinner.setEnabled(enabled);
        this.mMonthSpinner.setEnabled(enabled);
        this.mYearSpinner.setEnabled(enabled);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCurrentLocale(newConfig.locale);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.getText().add(DateUtils.formatDateTime(this.mContext, this.mCurrentDate.getTimeInMillis(), 20));
    }

    protected void setCurrentLocale(Locale locale) {
        int i;
        this.mTempDate = getCalendarForLocale(this.mTempDate, locale);
        this.mMinDate = getCalendarForLocale(this.mMinDate, locale);
        this.mMaxDate = getCalendarForLocale(this.mMaxDate, locale);
        this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, locale);
        this.mNumberOfMonths = this.mTempDate.getActualMaximum(2) + 1;
        this.mShortMonths = new DateFormatSymbols().getShortMonths();
        for (i = 0; i < this.mShortMonths.length; i++) {
            this.mShortMonths[i] = this.mShortMonths[i].toUpperCase();
        }
        if (usingNumericMonths()) {
            this.mShortMonths = new String[this.mNumberOfMonths];
            for (i = 0; i < this.mNumberOfMonths; i++) {
                this.mShortMonths[i] = String.format("%d", new Object[]{Integer.valueOf(i + 1)});
            }
        }
    }

    private boolean usingNumericMonths() {
        return Character.isDigit(this.mShortMonths[0].charAt(0));
    }

    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        }
        long currentTimeMillis = oldCalendar.getTimeInMillis();
        Calendar newCalendar = Calendar.getInstance(locale);
        newCalendar.setTimeInMillis(currentTimeMillis);
        return newCalendar;
    }

    private void reorderSpinners() {
        this.mSpinners.removeAllViews();
        char[] order = DateFormat.getDateFormatOrder(this.mContext);
        int spinnerCount = order.length;
        for (int i = 0; i < spinnerCount; i++) {
            switch (order[i]) {
                case 'M':
                    this.mSpinners.addView(this.mMonthSpinner);
                    setImeOptions(this.mMonthSpinner, spinnerCount, i);
                    break;
                case 'd':
                    this.mSpinners.addView(this.mDaySpinner);
                    setImeOptions(this.mDaySpinner, spinnerCount, i);
                    break;
                case 'y':
                    this.mSpinners.addView(this.mYearSpinner);
                    setImeOptions(this.mYearSpinner, spinnerCount, i);
                    break;
                default:
                    throw new IllegalArgumentException(Arrays.toString(order));
            }
            switch (i) {
                case 0:
                    this.mSpinners.addView(this.mPrimaryEmptyView);
                    break;
                case 1:
                    this.mSpinners.addView(this.mSecondaryEmptyView);
                    break;
                default:
                    break;
            }
        }
        char c = order[0];
        char c2 = order[1];
        switch (c) {
            case 'M':
                setTextWatcher(0);
                return;
            case 'd':
                setTextWatcher(1);
                return;
            case 'y':
                if (c2 == 'd') {
                    setTextWatcher(3);
                    return;
                } else {
                    setTextWatcher(2);
                    return;
                }
            default:
                return;
        }
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        if (this.mCurrentDate.get(1) == year && this.mCurrentDate.get(2) == month && this.mCurrentDate.get(5) == dayOfMonth) {
            return false;
        }
        return true;
    }

    private void setDate(int year, int month, int dayOfMonth) {
        this.mCurrentDate.set(year, month, dayOfMonth);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = year;
            this.mLunarCurrentMonth = month;
            this.mLunarCurrentDay = dayOfMonth;
        }
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
        } else if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
    }

    private void updateSpinners(boolean set, boolean yearChanged, boolean monthChanged, boolean dayChanged) {
        int year;
        int month;
        if (yearChanged) {
            this.mYearSpinner.setMinValue(this.mMinDate.get(1));
            this.mYearSpinner.setMaxValue(this.mMaxDate.get(1));
            this.mYearSpinner.setWrapSelectorWheel(false);
        }
        if (monthChanged) {
            int minMonth;
            int maxMonth;
            if (this.mMaxDate.get(1) - this.mMinDate.get(1) == 0) {
                minMonth = this.mMinDate.get(2);
                maxMonth = this.mMaxDate.get(2);
            } else {
                year = this.mCurrentDate.get(1);
                if (this.mIsLunar) {
                    year = this.mLunarCurrentYear;
                }
                if (year == this.mMinDate.get(1)) {
                    minMonth = this.mMinDate.get(2);
                    maxMonth = 11;
                } else if (year == this.mMaxDate.get(1)) {
                    minMonth = 0;
                    maxMonth = this.mMaxDate.get(2);
                } else {
                    minMonth = 0;
                    maxMonth = 11;
                }
            }
            if (usingNumericMonths()) {
                minMonth++;
                maxMonth++;
            }
            this.mMonthSpinner.setDisplayedValues(null);
            this.mMonthSpinner.setMinValue(minMonth);
            this.mMonthSpinner.setMaxValue(maxMonth);
            if (!usingNumericMonths()) {
                this.mMonthSpinner.setDisplayedValues((String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1));
            }
        }
        if (dayChanged) {
            int minDay;
            int maxDay;
            int diffMonth = this.mMaxDate.get(2) - this.mMinDate.get(2);
            if (this.mMaxDate.get(1) - this.mMinDate.get(1) == 0 && diffMonth == 0) {
                minDay = this.mMinDate.get(5);
                maxDay = this.mMaxDate.get(5);
            } else {
                year = this.mCurrentDate.get(1);
                month = this.mCurrentDate.get(2);
                if (this.mIsLunar) {
                    year = this.mLunarCurrentYear;
                    month = this.mLunarCurrentMonth;
                }
                if (year == this.mMinDate.get(1) && month == this.mMinDate.get(2)) {
                    minDay = this.mMinDate.get(5);
                    maxDay = this.mCurrentDate.getActualMaximum(5);
                    if (this.mIsLunar) {
                        maxDay = getLunarMaxDayOfMonth(year, month, this.mIsLeapMonth);
                    }
                } else if (year == this.mMaxDate.get(1) && month == this.mMaxDate.get(2)) {
                    minDay = 1;
                    maxDay = this.mMaxDate.get(5);
                    if (this.mIsLunar) {
                        maxDay = Math.min(maxDay, getLunarMaxDayOfMonth(year, month, this.mIsLeapMonth));
                    }
                } else {
                    minDay = 1;
                    maxDay = this.mCurrentDate.getActualMaximum(5);
                    if (this.mIsLunar) {
                        maxDay = getLunarMaxDayOfMonth(year, month, this.mIsLeapMonth);
                    }
                }
            }
            this.mDaySpinner.setMinValue(minDay);
            this.mDaySpinner.setMaxValue(maxDay);
        }
        if (set) {
            this.mYearSpinner.setValue(this.mCurrentDate.get(1));
            month = this.mCurrentDate.get(2);
            if (this.mIsLunar) {
                month = this.mLunarCurrentMonth;
            }
            if (usingNumericMonths()) {
                this.mMonthSpinner.setValue(month + 1);
            } else {
                this.mMonthSpinner.setValue(month);
            }
            int dayOfMonth = this.mCurrentDate.get(5);
            if (this.mIsLunar) {
                dayOfMonth = this.mLunarCurrentDay;
            }
            this.mDaySpinner.setValue(dayOfMonth);
            if (usingNumericMonths()) {
                this.mMonthSpinnerInput.setRawInputType(2);
            }
            if (this.mIsEditTextMode && this.mPickerTexts != null) {
                for (EditText spinnerInput : this.mPickerTexts) {
                    if (spinnerInput.hasFocus()) {
                        spinnerInput.setSelection(0, 0);
                        spinnerInput.selectAll();
                        return;
                    }
                }
            }
        }
    }

    private void notifyDateChanged() {
        sendAccessibilityEvent(4);
        if (this.mOnSpinnerDateChangedListener != null) {
            this.mOnSpinnerDateChangedListener.onDateChanged(this, getYear(), getMonth(), getDayOfMonth());
        }
    }

    private void setImeOptions(SeslNumberPicker spinner, int spinnerCount, int spinnerIndex) {
        int imeOptions;
        if (spinnerIndex < spinnerCount - 1) {
            imeOptions = 33554437;
        } else {
            imeOptions = 33554438;
        }
        ((TextView) spinner.findViewById(C0247R.id.numberpicker_input)).setImeOptions(imeOptions);
    }

    public void updateInputState() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
        if (inputMethodManager == null) {
            return;
        }
        if (inputMethodManager.isActive(this.mYearSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            this.mYearSpinnerInput.clearFocus();
        } else if (inputMethodManager.isActive(this.mMonthSpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            this.mMonthSpinnerInput.clearFocus();
        } else if (inputMethodManager.isActive(this.mDaySpinnerInput)) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            this.mDaySpinnerInput.clearFocus();
        }
    }

    private void setTextWatcher(int format) {
        seslLog("setTextWatcher() usingNumericMonths  : " + usingNumericMonths() + "format  : " + format);
        int yIdx = -1;
        int mIdx = -1;
        int dIdx = -1;
        switch (format) {
            case 0:
                mIdx = 0;
                dIdx = 1;
                yIdx = 2;
                break;
            case 1:
                dIdx = 0;
                mIdx = 1;
                yIdx = 2;
                break;
            case 2:
                yIdx = 0;
                mIdx = 1;
                dIdx = 2;
                break;
            case 3:
                yIdx = 0;
                dIdx = 1;
                mIdx = 2;
                break;
        }
        this.mPickerTexts[yIdx] = this.mYearSpinner.getEditText();
        this.mPickerTexts[mIdx] = this.mMonthSpinner.getEditText();
        this.mPickerTexts[dIdx] = this.mDaySpinner.getEditText();
        this.mPickerTexts[yIdx].addTextChangedListener(new SeslTextWatcher(this, 4, yIdx, false));
        if (usingNumericMonths()) {
            this.mPickerTexts[mIdx].addTextChangedListener(new SeslTextWatcher(this, 2, mIdx, true));
        } else {
            this.mPickerTexts[mIdx].addTextChangedListener(new SeslTextWatcher(this, 3, mIdx, true));
        }
        this.mPickerTexts[dIdx].addTextChangedListener(new SeslTextWatcher(this, 2, dIdx, false));
        if (format != 3 || usingNumericMonths()) {
            this.mPickerTexts[this.mPickerTexts.length - 1].setOnEditorActionListener(this.mEditorActionListener);
        }
        this.mPickerTexts[yIdx].setOnKeyListener(new SeslKeyListener());
        this.mPickerTexts[mIdx].setOnKeyListener(new SeslKeyListener());
        this.mPickerTexts[dIdx].setOnKeyListener(new SeslKeyListener());
    }

    private void seslLog(String msg) {
    }

    public void setOnSpinnerDateChangedListener(SeslDatePicker picker, OnSpinnerDateChangedListener listener) {
        if (this.mDatePicker == null) {
            this.mDatePicker = picker;
        }
        this.mOnSpinnerDateChangedListener = listener;
    }

    private void updateModeState(boolean mode) {
        if (this.mIsEditTextMode != mode && !mode) {
            if (this.mDaySpinner.isEditTextMode()) {
                this.mDaySpinner.setEditTextMode(false);
            }
            if (this.mMonthSpinner.isEditTextMode()) {
                this.mMonthSpinner.setEditTextMode(false);
            }
            if (this.mYearSpinner.isEditTextMode()) {
                this.mYearSpinner.setEditTextMode(false);
            }
        }
    }

    public void setEditTextMode(boolean editTextMode) {
        if (this.mIsEditTextMode != editTextMode) {
            this.mIsEditTextMode = editTextMode;
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            this.mDaySpinner.setEditTextMode(editTextMode);
            this.mMonthSpinner.setEditTextMode(editTextMode);
            this.mYearSpinner.setEditTextMode(editTextMode);
            if (inputMethodManager != null) {
                if (this.mIsEditTextMode) {
                    inputMethodManager.showSoftInput(this.mDaySpinner, 0);
                } else {
                    inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
                }
            }
            if (this.mOnEditTextModeChangedListener != null) {
                this.mOnEditTextModeChangedListener.onEditTextModeChanged(this.mDatePicker, editTextMode);
            }
        }
    }

    public boolean isEditTextMode() {
        return this.mIsEditTextMode;
    }

    public void setOnEditTextModeChangedListener(SeslDatePicker picker, SeslDatePicker.OnEditTextModeChangedListener onEditTextModeChangedListener) {
        if (this.mDatePicker == null) {
            this.mDatePicker = picker;
        }
        this.mOnEditTextModeChangedListener = onEditTextModeChangedListener;
    }

    public EditText getEditText(int picker) {
        switch (picker) {
            case 0:
                return this.mDaySpinner.getEditText();
            case 1:
                return this.mMonthSpinner.getEditText();
            case 2:
                return this.mYearSpinner.getEditText();
            default:
                return this.mDaySpinner.getEditText();
        }
    }

    public SeslNumberPicker getNumberPicker(int picker) {
        switch (picker) {
            case 0:
                return this.mDaySpinner;
            case 1:
                return this.mMonthSpinner;
            case 2:
                return this.mYearSpinner;
            default:
                return this.mDaySpinner;
        }
    }

    public void setLunar(boolean isLunar, boolean isLeapMonth, PathClassLoader pathClassLoader) {
        this.mIsLunar = isLunar;
        this.mIsLeapMonth = isLeapMonth;
        if (this.mIsLunar && this.mPathClassLoader == null) {
            this.mPathClassLoader = pathClassLoader;
            try {
                Class<?> calendarFeatureClass = Class.forName("com.android.calendar.Feature", true, this.mPathClassLoader);
                if (calendarFeatureClass == null) {
                    Log.e(TAG, "setLunar, Calendar Feature class is null");
                    return;
                }
                this.mSolarLunarTables = invoke(null, getMethod(calendarFeatureClass, "getSolarLunarTables", new Class[0]), new Object[0]);
                try {
                    Class<?> solarLunarTablesClass = Class.forName("com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarTables", true, this.mPathClassLoader);
                    if (solarLunarTablesClass == null) {
                        Log.e(TAG, "setLunar, Calendar Tables class is null");
                        return;
                    }
                    this.mIsLeapMonthMethod = getMethod(solarLunarTablesClass, "isLeapMonth", Integer.TYPE, Integer.TYPE);
                    this.mGetDayLengthOfMethod = getMethod(solarLunarTablesClass, "getDayLengthOf", Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "setLunar, Calendar Tables class not found");
                    return;
                }
            } catch (ClassNotFoundException e2) {
                Log.e(TAG, "setLunar, Calendar Feature class not found");
                return;
            }
        }
        updateSpinners(false, true, true, true);
    }

    public void setIsLeapMonth(boolean isLeapMonth) {
        this.mIsLeapMonth = isLeapMonth;
    }

    private int getLunarMaxDayOfMonth(int year, int month, boolean isLeapMonth) {
        if (this.mSolarLunarTables == null) {
            return 0;
        }
        Object max = invoke(this.mSolarLunarTables, this.mGetDayLengthOfMethod, Integer.valueOf(year), Integer.valueOf(month), Boolean.valueOf(isLeapMonth));
        if (max instanceof Integer) {
            return ((Integer) max).intValue();
        }
        return 0;
    }

    private <T> Method getMethod(Class<T> className, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = className.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, methodName + " NoSuchMethodException", e);
        }
        return method;
    }

    private Object invoke(Object callerInstance, Method method, Object... args) {
        Object obj = null;
        if (method == null) {
            Log.e(TAG, "method is null");
        } else {
            try {
                obj = method.invoke(callerInstance, args);
            } catch (IllegalAccessException e) {
                Log.e(TAG, method.getName() + " IllegalAccessException", e);
            } catch (IllegalArgumentException e2) {
                Log.e(TAG, method.getName() + " IllegalArgumentException", e2);
            } catch (InvocationTargetException e3) {
                Log.e(TAG, method.getName() + " InvocationTargetException", e3);
            }
        }
        return obj;
    }
}
