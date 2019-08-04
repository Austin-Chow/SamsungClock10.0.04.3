package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslAnimationListener;
import android.support.v7.widget.SeslTimePicker;
import android.support.v7.widget.SeslTimePicker.OnTimeChangedListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

public class SeslTimePickerDialog extends AlertDialog implements OnClickListener, OnTimeChangedListener {
    private static final String HOUR = "hour";
    private static final String IS_24_HOUR = "is24hour";
    private static final String MINUTE = "minute";
    private final OnFocusChangeListener mBtnFocusChangeListener;
    private InputMethodManager mImm;
    private final int mInitialHourOfDay;
    private final int mInitialMinute;
    private final boolean mIs24HourView;
    private boolean mIsStartAnimation;
    private final SeslTimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;

    /* renamed from: android.support.v7.app.SeslTimePickerDialog$1 */
    class C02401 implements OnFocusChangeListener {
        C02401() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (SeslTimePickerDialog.this.mTimePicker.isEditTextMode() && hasFocus) {
                SeslTimePickerDialog.this.mTimePicker.setEditTextMode(false);
            }
        }
    }

    /* renamed from: android.support.v7.app.SeslTimePickerDialog$2 */
    class C02412 implements SeslAnimationListener {
        C02412() {
        }

        public void onAnimationEnd() {
            SeslTimePickerDialog.this.mIsStartAnimation = false;
        }
    }

    public interface OnTimeSetListener {
        void onTimeSet(SeslTimePicker seslTimePicker, int i, int i2);
    }

    public SeslTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        this(context, 0, listener, hourOfDay, minute, is24HourView);
    }

    static int resolveDialogTheme(Context context, int resId) {
        if (resId != 0) {
            return resId;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        return outValue.data != 0 ? C0247R.style.Theme_AppCompat_Light_PickerDialog_TimePicker : C0247R.style.Theme_AppCompat_PickerDialog_TimePicker;
    }

    public SeslTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, resolveDialogTheme(context, themeResId));
        this.mBtnFocusChangeListener = new C02401();
        this.mTimeSetListener = listener;
        this.mInitialHourOfDay = hourOfDay;
        this.mInitialMinute = minute;
        this.mIs24HourView = is24HourView;
        Context themeContext = getContext();
        View view = LayoutInflater.from(themeContext).inflate(C0247R.layout.sesl_time_picker_spinner_dialog, null);
        setView(view);
        setButton(-1, themeContext.getString(C0247R.string.sesl_picker_done), (OnClickListener) this);
        setButton(-2, themeContext.getString(17039360), (OnClickListener) this);
        this.mTimePicker = (SeslTimePicker) view.findViewById(C0247R.id.timePicker);
        this.mTimePicker.setIs24HourView(Boolean.valueOf(this.mIs24HourView));
        this.mTimePicker.setHour(this.mInitialHourOfDay);
        this.mTimePicker.setMinute(this.mInitialMinute);
        this.mTimePicker.setOnTimeChangedListener(this);
        setTitle(C0247R.string.sesl_time_picker_set_title);
        this.mImm = (InputMethodManager) getContext().getSystemService("input_method");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getButton(-1).setOnFocusChangeListener(this.mBtnFocusChangeListener);
        getButton(-2).setOnFocusChangeListener(this.mBtnFocusChangeListener);
        this.mIsStartAnimation = true;
        this.mTimePicker.startAnimation(283, new C02412());
    }

    public void onTimeChanged(SeslTimePicker view, int hourOfDay, int minute) {
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case -2:
                if (this.mImm != null) {
                    this.mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                cancel();
                return;
            case -1:
                if (!this.mIsStartAnimation) {
                    if (this.mTimeSetListener != null) {
                        this.mTimePicker.clearFocus();
                        this.mTimeSetListener.onTimeSet(this.mTimePicker, this.mTimePicker.getHour(), this.mTimePicker.getMinute());
                    }
                    if (this.mImm != null) {
                        this.mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    dismiss();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void updateTime(int hourOfDay, int minuteOfHour) {
        this.mTimePicker.setHour(hourOfDay);
        this.mTimePicker.setMinute(minuteOfHour);
    }

    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, this.mTimePicker.getHour());
        state.putInt(MINUTE, this.mTimePicker.getMinute());
        state.putBoolean(IS_24_HOUR, this.mTimePicker.is24HourView());
        return state;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        this.mTimePicker.setIs24HourView(Boolean.valueOf(savedInstanceState.getBoolean(IS_24_HOUR)));
        this.mTimePicker.setHour(hour);
        this.mTimePicker.setMinute(minute);
    }
}
