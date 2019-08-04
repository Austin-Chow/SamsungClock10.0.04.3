package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslDatePicker;
import android.support.v7.widget.SeslDatePicker.OnDateChangedListener;
import android.support.v7.widget.SeslDatePicker.ValidationCallback;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class SeslDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private final OnFocusChangeListener mBtnFocusChangeListener;
    private final SeslDatePicker mDatePicker;
    private final OnDateSetListener mDateSetListener;
    private InputMethodManager mImm;
    private final ValidationCallback mValidationCallback;

    /* renamed from: android.support.v7.app.SeslDatePickerDialog$1 */
    class C02371 implements OnFocusChangeListener {
        C02371() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (SeslDatePickerDialog.this.mDatePicker.isEditTextMode() && hasFocus) {
                SeslDatePickerDialog.this.mDatePicker.setEditTextMode(false);
            }
        }
    }

    /* renamed from: android.support.v7.app.SeslDatePickerDialog$2 */
    class C02382 implements ValidationCallback {
        C02382() {
        }

        public void onValidationChanged(boolean valid) {
            Button positive = SeslDatePickerDialog.this.getButton(-1);
            if (positive != null) {
                positive.setEnabled(valid);
            }
        }
    }

    public interface OnDateSetListener {
        void onDateSet(SeslDatePicker seslDatePicker, int i, int i2, int i3);
    }

    public SeslDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        this(context, 0, listener, year, month, dayOfMonth);
    }

    public SeslDatePickerDialog(Context context, int themeResId, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, resolveDialogTheme(context, themeResId));
        this.mBtnFocusChangeListener = new C02371();
        this.mValidationCallback = new C02382();
        Context themeContext = getContext();
        View view = LayoutInflater.from(themeContext).inflate(C0247R.layout.sesl_date_picker_dialog, null);
        setView(view);
        setButton(-1, themeContext.getString(C0247R.string.sesl_picker_done), (OnClickListener) this);
        setButton(-2, themeContext.getString(17039360), (OnClickListener) this);
        this.mDatePicker = (SeslDatePicker) view.findViewById(C0247R.id.sesl_datePicker);
        this.mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        this.mDatePicker.setValidationCallback(this.mValidationCallback);
        this.mDateSetListener = listener;
        this.mImm = (InputMethodManager) themeContext.getSystemService("input_method");
    }

    static int resolveDialogTheme(Context context, int themeResId) {
        if (themeResId != 0) {
            return themeResId;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        return outValue.data != 0 ? C0247R.style.Theme_AppCompat_Light_PickerDialog_DatePicker : C0247R.style.Theme_AppCompat_PickerDialog_DatePicker;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getButton(-1).setOnFocusChangeListener(this.mBtnFocusChangeListener);
        getButton(-2).setOnFocusChangeListener(this.mBtnFocusChangeListener);
    }

    public void onDateChanged(SeslDatePicker view, int year, int month, int day) {
    }

    public void onClick(DialogInterface dialog, int which) {
        if (this.mImm != null) {
            this.mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        switch (which) {
            case -2:
                cancel();
                return;
            case -1:
                if (this.mDateSetListener != null) {
                    this.mDatePicker.clearFocus();
                    this.mDateSetListener.onDateSet(this.mDatePicker, this.mDatePicker.getYear(), this.mDatePicker.getMonth(), this.mDatePicker.getDayOfMonth());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public SeslDatePicker getDatePicker() {
        return this.mDatePicker;
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        this.mDatePicker.updateDate(year, month, dayOfMonth);
    }

    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, this.mDatePicker.getYear());
        state.putInt(MONTH, this.mDatePicker.getMonth());
        state.putInt(DAY, this.mDatePicker.getDayOfMonth());
        return state;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mDatePicker.init(savedInstanceState.getInt(YEAR), savedInstanceState.getInt(MONTH), savedInstanceState.getInt(DAY), this);
    }
}
