package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.TimeZoneConvertorControlViewListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneConvertorControlViewModel extends LinearLayout {
    private final Context mContext;
    private TextView mDefaultTimeZoneDate;
    private boolean mIsChangedList = false;
    private boolean mIsResetEnabled = false;
    private final OnItemSelectedListener mOnItemSelectedListener = new C08803();
    private Button mResetButton;
    private int mSelectedPosition = 0;
    private Spinner mSpinner;
    private ArrayAdapter<String> mSpinnerAdapter;
    private int mStartHour;
    private int mStartMin;
    private TimeZoneConvertorControlViewListener mTimeZoneConvertorControlViewListener;
    private TimeZoneConvertorPicker mTimeZoneConvertorPicker;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorControlViewModel$1 */
    class C08781 implements OnFocusChangeListener {
        C08781() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorPicker.isEditMode()) {
                TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorPicker.setEditTextMode(false);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorControlViewModel$3 */
    class C08803 implements OnItemSelectedListener {
        C08803() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorControlViewListener != null) {
                TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorPicker.clearPickerFocus();
                TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorControlViewListener.onResetSelectedState();
                TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorControlViewListener.onSelectCityItem(position);
                TimeZoneConvertorControlViewModel.this.mTimeZoneConvertorPicker.showInputMethod(TimeZoneConvertorControlViewModel.this.mIsChangedList);
                TimeZoneConvertorControlViewModel.this.mIsChangedList = false;
                TimeZoneConvertorControlViewModel.this.mSelectedPosition = position;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public TimeZoneConvertorControlViewModel(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TimeZoneConvertorControlViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TimeZoneConvertorControlViewModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    public void initView() {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_timezone_convertor_control, this, true);
        this.mSpinner = (Spinner) findViewById(C0836R.id.timezone_convertor_main_city_spinner);
        this.mTimeZoneConvertorPicker = (TimeZoneConvertorPicker) findViewById(C0836R.id.timezone_convertor_picker);
        this.mDefaultTimeZoneDate = (TextView) findViewById(C0836R.id.worldclock_timezone_convertor_main_city_date);
        this.mResetButton = (Button) findViewById(C0836R.id.worldclock_timezone_convertor_reset_button);
        try {
            this.mResetButton.semSetButtonShapeEnabled(true);
        } catch (NoSuchMethodError e) {
            Log.secE("TimeZoneConvertorControlViewModel", "NoSuchMethodError : " + e);
        }
        OnFocusChangeListener convertorFocusChangeListener = new C08781();
        this.mSpinner.setOnFocusChangeListener(convertorFocusChangeListener);
        this.mResetButton.setOnFocusChangeListener(convertorFocusChangeListener);
    }

    public void initPicker(boolean isMultiWindow, TimeZoneConvertorControlViewListener mTimeZoneConvertorControlViewListener) {
        this.mTimeZoneConvertorControlViewListener = mTimeZoneConvertorControlViewListener;
        this.mTimeZoneConvertorPicker.initPicker(isMultiWindow, mTimeZoneConvertorControlViewListener);
        setPickerMarginForOrientation(mTimeZoneConvertorControlViewListener.getIsPortraitLayout());
    }

    public void setSpinner(int defaultCityIndex) {
        if (this.mSpinner != null) {
            this.mSelectedPosition = defaultCityIndex;
            this.mSpinnerAdapter = new ArrayAdapter<String>(this.mContext, C0836R.layout.spinner_text, this.mTimeZoneConvertorControlViewListener.getSpinnerArrayList()) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(C0836R.id.timezone_convertor_spinner_text);
                    if (StateUtils.isMultiWindowMinSize(TimeZoneConvertorControlViewModel.this.mContext, 232, false)) {
                        textView.setTextSize(0, (float) TimeZoneConvertorControlViewModel.this.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_spinner_text_size_smallest));
                    } else {
                        ClockUtils.setLargeTextSize(TimeZoneConvertorControlViewModel.this.mContext, textView, (float) TimeZoneConvertorControlViewModel.this.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_spinner_text_size));
                    }
                    return view;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    Typeface font;
                    View view = super.getDropDownView(position, convertView, parent);
                    CheckedTextView tv = (CheckedTextView) view.findViewById(C0836R.id.convertor_spinner_item);
                    if (position == TimeZoneConvertorControlViewModel.this.mSelectedPosition) {
                        font = Typeface.create("sec-roboto-light", 1);
                    } else {
                        font = Typeface.create("sec-roboto-light", 0);
                    }
                    tv.setTypeface(font);
                    return view;
                }
            };
            this.mSpinnerAdapter.setDropDownViewResource(C0836R.layout.spinner_list_item);
            this.mSpinner.setAdapter(this.mSpinnerAdapter);
            this.mSpinner.setSelection(defaultCityIndex, false);
            this.mTimeZoneConvertorControlViewListener.onSelectCityItem(defaultCityIndex);
            this.mSpinner.setOnItemSelectedListener(this.mOnItemSelectedListener);
            this.mSpinner.setDropDownVerticalOffset(getResources().getDimensionPixelSize(C0836R.dimen.f25xf6ab83d7));
            this.mSpinner.setDropDownHorizontalOffset(getResources().getDimensionPixelSize(C0836R.dimen.f24x9de934a9));
        }
    }

    public void updatePicker(TimeZone timezone) {
        if (this.mTimeZoneConvertorPicker != null) {
            Log.secD("TimeZoneConvertorControlViewModel", "updatePicker()");
            GregorianCalendar cl = new GregorianCalendar();
            cl.setTimeZone(timezone);
            this.mStartHour = cl.get(11);
            this.mStartMin = cl.get(12);
            Log.secD("TimeZoneConvertorControlViewModel", "mStartHour : " + this.mStartHour + " mStartMin : " + this.mStartMin);
            this.mTimeZoneConvertorPicker.setHour(this.mStartHour);
            this.mTimeZoneConvertorPicker.setMinute(this.mStartMin);
            this.mTimeZoneConvertorControlViewListener.initItemValue(false);
            setIsResetEnabled();
            updateResetButton();
            if (this.mDefaultTimeZoneDate != null) {
                this.mDefaultTimeZoneDate.setText(getDefaultTimeZoneDate("EEE d MMM"));
                this.mDefaultTimeZoneDate.setContentDescription(getDefaultTimeZoneDate("EEEE d MMMM"));
                ClockUtils.setLargeTextSize(this.mContext, this.mDefaultTimeZoneDate, (float) this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_date_text_size));
            }
            this.mTimeZoneConvertorControlViewListener.onRefreshCityListDelayed(100);
            String str = (String) this.mSpinner.getSelectedItem();
            StringBuilder contentDescription = new StringBuilder();
            contentDescription.append(str).append(',').append(' ').append(this.mContext.getString(C0836R.string.select_city));
            this.mSpinner.setContentDescription(contentDescription);
        }
    }

    private void updateResetButton() {
        if (this.mResetButton != null && this.mResetButton.isEnabled() != this.mIsResetEnabled) {
            this.mResetButton.setEnabled(this.mIsResetEnabled);
            this.mResetButton.setAlpha(this.mIsResetEnabled ? 1.0f : 0.4f);
        }
    }

    public void initResetButton() {
        if (this.mResetButton != null) {
            this.mResetButton.setOnClickListener(TimeZoneConvertorControlViewModel$$Lambda$1.lambdaFactory$(this));
            ClockUtils.setLargeTextSize(this.mContext, this.mResetButton, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_reset_text_size));
        }
    }

    private /* synthetic */ void lambda$initResetButton$0(View view) {
        updatePicker(this.mTimeZoneConvertorControlViewListener.getDefaultTimeZone());
        if (this.mTimeZoneConvertorPicker != null) {
            this.mTimeZoneConvertorPicker.requestPickerFocus(0);
        }
        ClockUtils.insertSaLog("115", "1291");
    }

    public void setPickerHeightForMultiWindow(boolean isMultiWindow) {
        if (!isMultiWindow) {
            this.mTimeZoneConvertorPicker.setPickerHeightForMultiWindow(StateUtils.isMultiWindowMinSize(this.mContext, 232, false));
        }
    }

    public void setPickerMarginForOrientation(boolean isPotrait) {
        if (this.mTimeZoneConvertorPicker != null) {
            LayoutParams layoutParams = (LayoutParams) this.mTimeZoneConvertorPicker.getLayoutParams();
            layoutParams.topMargin = isPotrait ? getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_picker_margin_top) : getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_picker_margin_top_land);
            layoutParams.bottomMargin = isPotrait ? getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_picker_margin_bottom) : getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_picker_margin_bottom_land);
            this.mTimeZoneConvertorPicker.setLayoutParams(layoutParams);
        }
    }

    public void saveInstance(Bundle state) {
        this.mTimeZoneConvertorPicker.saveInstance(state);
    }

    public void restoreInstance(Bundle state) {
        this.mTimeZoneConvertorPicker.restoreInstance(state);
    }

    public int getCurrentItemIndex() {
        return this.mSpinner.getSelectedItemPosition();
    }

    public void onConfigurationChanged(boolean isMultiWindow) {
        setPickerMarginForOrientation(this.mTimeZoneConvertorControlViewListener.getIsPortraitLayout());
        setPickerHeightForMultiWindow(isMultiWindow);
        if (this.mSpinnerAdapter != null) {
            this.mSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void setIsResetEnabled() {
        GregorianCalendar cl = new GregorianCalendar();
        cl.setTimeZone(this.mTimeZoneConvertorControlViewListener.getDefaultTimeZone());
        boolean z = (cl.get(11) == this.mTimeZoneConvertorPicker.getHour() && cl.get(12) == this.mTimeZoneConvertorPicker.getMinute()) ? false : true;
        this.mIsResetEnabled = z;
    }

    public void onChagnedResetButtonState() {
        setIsResetEnabled();
        updateResetButton();
    }

    public void onChangedTimeData() {
        if (this.mDefaultTimeZoneDate != null) {
            this.mDefaultTimeZoneDate.setText(getDefaultTimeZoneDate("EEE d MMM"));
            this.mDefaultTimeZoneDate.setContentDescription(getDefaultTimeZoneDate("EEEE d MMMM"));
        }
        this.mTimeZoneConvertorPicker.setIs24HourView();
    }

    @SuppressLint({"SimpleDateFormat"})
    private String getDefaultTimeZoneDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), format));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(this.mTimeZoneConvertorControlViewListener.getDefaultTimeZone());
        dateFormat.setCalendar(calendar);
        return dateFormat.format(dateFormat.getCalendar().getTime());
    }

    public int getStartHour() {
        return this.mStartHour;
    }

    public int getStartMin() {
        return this.mStartMin;
    }

    public void setChangedList(boolean changedList) {
        this.mIsChangedList = changedList;
    }

    public void dismissSpinner() {
        if (this.mSpinner != null) {
            this.mSpinner.semDismissPopup();
        }
    }

    public void releaseInstance() {
        if (this.mTimeZoneConvertorPicker != null) {
            this.mTimeZoneConvertorPicker.destroyListener();
            this.mTimeZoneConvertorPicker = null;
        }
        if (this.mResetButton != null) {
            this.mResetButton.setOnFocusChangeListener(null);
            this.mResetButton.setOnClickListener(null);
            this.mResetButton = null;
        }
        if (this.mSpinner != null) {
            this.mSpinner.setOnFocusChangeListener(null);
            this.mSpinner.setOnItemSelectedListener(null);
            this.mSpinner = null;
        }
        this.mSpinnerAdapter = null;
    }
}
