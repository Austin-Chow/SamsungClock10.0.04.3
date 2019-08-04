package com.sec.android.app.clockpackage.alarm.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.app.SemDatePickerDialog;
import com.samsung.android.app.SemDatePickerDialog.OnDateSetListener;
import com.samsung.android.widget.SemDatePicker;
import com.samsung.android.widget.SemTimePicker;
import com.samsung.android.widget.SemTimePicker.OnEditTextModeChangedListener;
import com.samsung.android.widget.SemTimePicker.OnTimeChangedListener;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmTimeControl;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import java.util.Calendar;

public class AlarmTimeSetting extends RelativeLayout {
    private int lastTimePickerFocus;
    private LinearLayout mAlarmDateLayout;
    private final OnDateSetListener mAlarmDateListener = new C05761();
    private AlarmTimeEditListener mAlarmEditListener;
    private final AlarmTimeControl mAlarmTimeControl;
    private final Context mContext;
    private SemDatePickerDialog mDateDialog;
    private SemDatePicker mDatePickerAtDialog;
    private EditText mHourSpinnerInput;
    private EditText mMinSpinnerInput;
    private TextView mNotiAlarmTimeText;
    private SemTimePicker mTwPicker;

    public interface AlarmTimeEditListener {
        void initRepeat();

        void setSpecialDateAlarm(boolean z);
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting$1 */
    class C05761 implements OnDateSetListener {
        C05761() {
        }

        public void onDateSet(SemDatePicker view, int year, int monthOfYear, int dayOfMonth) {
            AlarmTimeSetting.this.mAlarmDateLayout.setEnabled(true);
            if (AlarmTimeSetting.this.mAlarmTimeControl.mRepeatDateWorkingState == 1) {
                if (AlarmTimeSetting.this.mAlarmEditListener != null) {
                    AlarmTimeSetting.this.mAlarmEditListener.initRepeat();
                }
            } else if (AlarmTimeSetting.this.mAlarmTimeControl.mRepeatDateWorkingState == 3) {
                Toast.makeText(AlarmTimeSetting.this.mContext, AlarmTimeSetting.this.getResources().getString(C0490R.string.alarm_cancel_workingday), 0).show();
            }
            if (!AlarmTimeSetting.this.mAlarmTimeControl.setAlertDateByCalendar(year, monthOfYear, dayOfMonth)) {
                Toast.makeText(AlarmTimeSetting.this.mContext, AlarmTimeSetting.this.getResources().getString(C0490R.string.alarm_date_unable_to_set_today), 0).show();
            }
            AlarmTimeSetting.this.mAlarmTimeControl.mRepeatDateWorkingState = 2;
            if (AlarmTimeSetting.this.mAlarmEditListener != null) {
                AlarmTimeSetting.this.mAlarmEditListener.setSpecialDateAlarm(true);
            }
            AlarmTimeSetting.this.calculateNotidaysAndSetText();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting$2 */
    class C05772 implements OnClickListener {
        C05772() {
        }

        public void onClick(View v) {
            AlarmTimeSetting.this.hideKeyBoard();
            ClockUtils.insertSaLog("103", "1020");
            AlarmTimeSetting.this.onDateButtonClick();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting$3 */
    class C05783 implements OnFocusChangeListener {
        C05783() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                AlarmTimeSetting.this.changeToEditMode(false);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting$4 */
    class C05794 implements OnTimeChangedListener {
        C05794() {
        }

        public void onTimeChanged(SemTimePicker view, int hour, int min) {
            AlarmTimeSetting.this.mAlarmTimeControl.mHourValue = hour;
            AlarmTimeSetting.this.mAlarmTimeControl.mMinValue = min;
            AlarmTimeSetting.this.calculateNotidaysAndSetText();
            RingtonePlayer.stopMediaPlayer();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting$5 */
    class C05805 implements OnEditTextModeChangedListener {
        C05805() {
        }

        public void onEditTextModeChanged(SemTimePicker view, boolean mode) {
            AlarmTimeSetting.this.invalidate();
            if (mode) {
                AlarmTimeSetting.this.calculateNotidaysAndSetText();
                ClockUtils.insertSaLog("103", "1223");
            }
            AlarmTimeSetting.this.mAlarmTimeControl.mHourValue = view.getHour();
            AlarmTimeSetting.this.mAlarmTimeControl.mMinValue = view.getMinute();
            RingtonePlayer.stopMediaPlayer();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting$6 */
    class C05816 implements TextWatcher {
        C05816() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            Log.secD("AlarmTimeSetting", "mMinSpinnerInput afterTextChanged = " + s.toString());
            if (s.toString().length() > 0) {
                if (s.toString().length() > 1) {
                    AlarmTimeSetting.this.mMinSpinnerInput.selectAll();
                }
                try {
                    AlarmTimeSetting.this.mAlarmTimeControl.mMinValue = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    Log.secE("AlarmTimeSetting", "NumberFormatException ");
                }
            }
        }
    }

    public void setAlarmEditInterface(AlarmTimeEditListener alarmTimeListener) {
        if (this.mAlarmEditListener == null) {
            this.mAlarmEditListener = alarmTimeListener;
        }
    }

    public AlarmTimeSetting(Context context) {
        super(context);
        this.mContext = context;
        this.mAlarmTimeControl = new AlarmTimeControl(context);
    }

    public AlarmTimeSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mAlarmTimeControl = new AlarmTimeControl(context);
    }

    public AlarmTimeSetting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.mAlarmTimeControl = new AlarmTimeControl(context);
    }

    public void createView(View timePicker) {
        createView();
        inflateTimePicker(timePicker);
    }

    private void createView() {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0490R.layout.alarm_time_setting, this, true);
        this.mNotiAlarmTimeText = (TextView) findViewById(C0490R.id.noti_alarm_time);
        ClockUtils.setLargeTextSize(this.mContext, this.mNotiAlarmTimeText, (float) getResources().getDimensionPixelSize(C0490R.dimen.alarm_noti_alarm_time_text_size));
        this.mAlarmDateLayout = (LinearLayout) findViewById(C0490R.id.alarm_datepicker);
        this.mAlarmDateLayout.setContentDescription(getResources().getString(C0490R.string.alarm_set_date) + "," + getResources().getString(C0490R.string.button));
        this.mAlarmDateLayout.setOnClickListener(new C05772());
        this.mAlarmDateLayout.setEnabled(true);
        this.mAlarmDateLayout.setOnFocusChangeListener(new C05783());
    }

    private void inflateTimePicker(View timePicker) {
        this.mTwPicker = (SemTimePicker) timePicker;
        this.mTwPicker.setIs24HourView(Boolean.valueOf(DateFormat.is24HourFormat(this.mContext)));
        this.mTwPicker.setOnTimeChangedListener(new C05794());
        this.mTwPicker.setOnEditTextModeChangedListener(new C05805());
        this.mHourSpinnerInput = this.mTwPicker.getEditText(0);
        this.mMinSpinnerInput = this.mTwPicker.getEditText(1);
        this.mHourSpinnerInput.setImeOptions(33554437);
        this.mMinSpinnerInput.setImeOptions(33554438);
        if (StateUtils.isMobileKeyboard(this.mContext)) {
            this.mHourSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            this.mMinSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
        }
        this.mMinSpinnerInput.addTextChangedListener(new C05816());
    }

    public void clearFocusHour() {
        if (this.mHourSpinnerInput != null && this.mHourSpinnerInput.hasFocus()) {
            this.mHourSpinnerInput.clearFocus();
        }
    }

    public void initData() {
        this.mTwPicker.setHour(6);
        this.mTwPicker.setMinute(0);
        this.mTwPicker.setEditTextMode(false);
        this.mTwPicker.startAnimation(0, null);
        if (this.mAlarmDateLayout != null) {
            this.mAlarmDateLayout.setEnabled(true);
        }
    }

    public void resetTime(int hour, int min) {
        this.mAlarmTimeControl.mHourValue = hour;
        this.mAlarmTimeControl.mMinValue = min;
        this.mTwPicker.setIs24HourView(Boolean.valueOf(DateFormat.is24HourFormat(this.mContext)));
        this.mTwPicker.setHour(hour);
        this.mTwPicker.setMinute(min);
        if (this.mAlarmDateLayout != null) {
            this.mAlarmDateLayout.setEnabled(true);
        }
        calculateNotidaysAndSetText();
    }

    public void setDateByAlertTimeMilliseconds(long changedAlertTime) {
        this.mAlarmTimeControl.mAlertDateCalendar.setTimeInMillis(changedAlertTime);
        this.mAlarmTimeControl.mNextAlertTimeMillis = changedAlertTime;
        this.mAlarmTimeControl.mRepeatDateWorkingState = 2;
    }

    private void onDateButtonClick() {
        if (this.mDateDialog == null || !this.mDateDialog.isShowing()) {
            Calendar curCal = Calendar.getInstance();
            long minDate = curCal.getTimeInMillis();
            curCal.set(curCal.get(1) + 2, curCal.get(2), curCal.get(5), 23, 59);
            long maxDate = curCal.getTimeInMillis();
            if (this.mAlarmTimeControl.mRepeatDateWorkingState == 1 || this.mAlarmTimeControl.mNextAlertTimeMillis > maxDate) {
                this.mAlarmTimeControl.setAlertDateToDefault();
            }
            this.mDateDialog = new SemDatePickerDialog(this.mContext, StateUtils.isCustomTheme(this.mContext) ? C0490R.style.MyCustomThemeTWDatePicker : C0490R.style.DefaultThemeTWDatePicker, this.mAlarmDateListener, this.mAlarmTimeControl.mAlertDateCalendar.get(1), this.mAlarmTimeControl.mAlertDateCalendar.get(2), this.mAlarmTimeControl.mAlertDateCalendar.get(5));
            SemDatePicker datePickerAtDialog = this.mDateDialog.getDatePicker();
            datePickerAtDialog.setMinDate(minDate);
            datePickerAtDialog.setMaxDate(maxDate);
            this.mDatePickerAtDialog = this.mDateDialog.getDatePicker();
            this.mDatePickerAtDialog.setFirstDayOfWeek(ClockUtils.getStartDayOfWeek());
            if (!this.mDateDialog.isShowing()) {
                changeToEditMode(false);
                this.mDateDialog.show();
            }
            RingtonePlayer.stopMediaPlayer();
        }
    }

    public void changeToEditMode(boolean isEditMode) {
        if (this.mTwPicker != null) {
            this.mTwPicker.setEditTextMode(isEditMode);
        }
    }

    public void calculateNotidaysAndSetText() {
        if (this.mNotiAlarmTimeText == null) {
            return;
        }
        if (this.mAlarmTimeControl.mRepeatDateWorkingState == 0 || this.mAlarmTimeControl.mRepeatDateWorkingState == 2) {
            this.mAlarmTimeControl.getAlarmAlertDateText(this.mNotiAlarmTimeText);
        }
    }

    public void calculateAlarmRepeatText(String repeatText) {
        String nextAlarmNotidays = "";
        if (repeatText != null) {
            nextAlarmNotidays = repeatText;
            Log.secD("AlarmTimeSetting", "calculateAlarmRepeatText() - " + repeatText);
        }
        if (this.mNotiAlarmTimeText == null || nextAlarmNotidays.equals("") || nextAlarmNotidays.length() == 0) {
            this.mAlarmTimeControl.mRepeatDateWorkingState = 0;
            calculateNotidaysAndSetText();
            return;
        }
        this.mAlarmTimeControl.mRepeatDateWorkingState = 1;
        this.mNotiAlarmTimeText.setText(nextAlarmNotidays);
        nextAlarmNotidays = nextAlarmNotidays.replaceAll("-", "");
        if (!nextAlarmNotidays.equals(this.mContext.getResources().getString(C0490R.string.every_day))) {
            for (int i = 1; i <= 7; i++) {
                nextAlarmNotidays = nextAlarmNotidays.replace(ClockUtils.getDayOfWeekString(this.mContext, i, 2), ClockUtils.getDayOfWeekString(this.mContext, i, 4));
            }
        }
        this.mNotiAlarmTimeText.setContentDescription(nextAlarmNotidays);
    }

    public void setWorkingDayAlarm() {
        if (this.mAlarmTimeControl.mRepeatDateWorkingState == 2) {
            this.mAlarmTimeControl.setAlertDateToDefault();
        }
        this.mAlarmTimeControl.mRepeatDateWorkingState = 3;
        this.mNotiAlarmTimeText.setText(C0490R.string.alarm_workdays);
    }

    public void saveLastFocusPositionInTimePicker() {
        if (this.mHourSpinnerInput == null || this.mMinSpinnerInput == null || this.mTwPicker == null || !this.mTwPicker.isEditTextMode()) {
            this.lastTimePickerFocus = 0;
        } else if (this.mHourSpinnerInput.hasFocus()) {
            this.lastTimePickerFocus = 1;
        } else if (this.mMinSpinnerInput.hasFocus()) {
            this.lastTimePickerFocus = 2;
        } else {
            this.lastTimePickerFocus = 0;
        }
    }

    public void hideDialog() {
        if (this.mDateDialog != null && this.mDateDialog.isShowing()) {
            this.mDateDialog.hide();
        }
        saveLastFocusPositionInTimePicker();
        this.mTwPicker.setEditTextMode(false);
    }

    public void showDialogAgain() {
        if (this.mDateDialog != null && this.mDateDialog.isShowing()) {
            this.mDateDialog.show();
            this.mDatePickerAtDialog.getLayoutParams().width = -1;
            this.mDatePickerAtDialog.invalidate();
        }
        setLastFocusToTimePicker();
    }

    public void setLastFocusToTimePicker() {
        if (this.lastTimePickerFocus != 0 && this.mTwPicker != null && this.mHourSpinnerInput != null && this.mMinSpinnerInput != null) {
            Log.secD("AlarmTimeSetting", "setLastFocusToTimePicker() lastTimePickerFocus = " + this.lastTimePickerFocus);
            this.mTwPicker.setEditTextMode(true);
            if (this.lastTimePickerFocus == 1) {
                this.mHourSpinnerInput.requestFocus();
            } else if (this.lastTimePickerFocus == 2) {
                this.mMinSpinnerInput.requestFocus();
            }
        }
    }

    public void hideKeyBoard() {
        if (this.mContext != null) {
            ((InputMethodManager) this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setTimeViewHeight();
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superSavedState", super.onSaveInstanceState());
        bundle.putInt("savedHour", this.mAlarmTimeControl.mHourValue);
        bundle.putInt("savedMinutes", this.mAlarmTimeControl.mMinValue);
        bundle.putLong("savedAlertTimeMillis", this.mAlarmTimeControl.mNextAlertTimeMillis);
        bundle.putInt("saveRepeatDateWorkingState", this.mAlarmTimeControl.mRepeatDateWorkingState);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mAlarmTimeControl.mHourValue = bundle.getInt("savedHour");
            this.mAlarmTimeControl.mMinValue = bundle.getInt("savedMinutes");
            this.mAlarmTimeControl.mNextAlertTimeMillis = bundle.getLong("savedAlertTimeMillis");
            setDateByAlertTimeMilliseconds(this.mAlarmTimeControl.mNextAlertTimeMillis);
            this.mAlarmTimeControl.mRepeatDateWorkingState = bundle.getInt("saveRepeatDateWorkingState");
            state = bundle.getParcelable("superSavedState");
        }
        super.onRestoreInstanceState(state);
    }

    public void reloadTimeSettingViewForFreeformMode(Boolean isShowButtonBackground) {
        setShowBtnBackground(isShowButtonBackground);
        setRepeatDateWorkingState(this.mAlarmTimeControl.mRepeatDateWorkingState);
        resetTime(this.mAlarmTimeControl.mHourValue, this.mAlarmTimeControl.mMinValue);
        if (this.mDateDialog != null && this.mDateDialog.isShowing()) {
            int dYear = this.mDateDialog.getDatePicker().getYear();
            int dMonth = this.mDateDialog.getDatePicker().getMonth();
            int dayOfMonth = this.mDateDialog.getDatePicker().getDayOfMonth();
            this.mDateDialog.cancel();
            this.mDateDialog = null;
            onDateButtonClick();
            this.mDateDialog.updateDate(dYear, dMonth, dayOfMonth);
        }
    }

    private void setTimeViewHeight() {
        if (this.mTwPicker != null) {
            setPadding(0, getResources().getDimensionPixelOffset(C0490R.dimen.alarm_noti_text_top_padding), 0, 0);
            requestLayout();
        }
    }

    public void setShowBtnBackground(Boolean isShowButtonBackground) {
        this.mAlarmDateLayout.setBackgroundResource(isShowButtonBackground.booleanValue() ? C0490R.drawable.common_action_btn_material_light : C0490R.drawable.common_action_btn_bg);
    }

    public void resetIs24HourView() {
        this.mTwPicker.setIs24HourView(Boolean.valueOf(DateFormat.is24HourFormat(this.mContext)));
    }

    public long getNextAlertTime() {
        return this.mAlarmTimeControl.mNextAlertTimeMillis;
    }

    public int getHourValue() {
        return this.mAlarmTimeControl.mHourValue;
    }

    public int getMinValue() {
        return this.mAlarmTimeControl.mMinValue;
    }

    public int getAlarmTime() {
        return (this.mAlarmTimeControl.mHourValue * 100) + this.mAlarmTimeControl.mMinValue;
    }

    public boolean isDateSelectedState() {
        return this.mAlarmTimeControl.mRepeatDateWorkingState == 2;
    }

    public void setRepeatDateWorkingState(int value) {
        this.mAlarmTimeControl.mRepeatDateWorkingState = value;
    }

    public int getRepeatDateWorkingState() {
        return this.mAlarmTimeControl.mRepeatDateWorkingState;
    }

    public int getCheckDayForDateAlarm() {
        return this.mAlarmTimeControl.getCheckDayForDateAlarm();
    }

    public void removeInstance(boolean isDestroy) {
        hideKeyBoard();
        this.mNotiAlarmTimeText = null;
        this.mAlarmDateLayout = null;
        if (this.mTwPicker != null) {
            this.mTwPicker.removeAllViews();
            this.mTwPicker = null;
        }
        if (this.mHourSpinnerInput != null) {
            this.mHourSpinnerInput.addTextChangedListener(null);
            this.mHourSpinnerInput.setOnFocusChangeListener(null);
            this.mHourSpinnerInput = null;
        }
        if (this.mMinSpinnerInput != null) {
            this.mMinSpinnerInput.addTextChangedListener(null);
            this.mMinSpinnerInput.setOnFocusChangeListener(null);
            this.mMinSpinnerInput = null;
        }
        if (isDestroy) {
            if (this.mDateDialog != null) {
                this.mDateDialog.dismiss();
                this.mDateDialog = null;
            }
            this.mAlarmEditListener = null;
        }
        removeAllViews();
        destroyDrawingCache();
    }
}
