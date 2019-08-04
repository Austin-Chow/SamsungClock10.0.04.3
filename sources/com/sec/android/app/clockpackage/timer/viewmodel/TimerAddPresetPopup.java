package com.sec.android.app.clockpackage.timer.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.database.sqlite.SQLiteFullException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.util.TextLengthFilter;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerAddPresetPopupListener;
import com.sec.android.app.clockpackage.timer.model.Timer;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import java.util.ArrayList;
import java.util.Locale;

public class TimerAddPresetPopup {
    private AlertDialog mAddPresetDialog;
    private Context mContext;
    private AccessibilityDelegate mEditorAccessibilityDelegate = new C07482();
    private OnEditorActionListener mEditorActionListener = new C07471();
    private TimeTextWatcher mHourTextWatcher;
    private TimeTextWatcher mMinuteTextWatcher;
    private EditText mPresetHourEditText;
    private EditText mPresetMinuteEditText;
    private EditText mPresetNameEditText;
    private EditText mPresetSecondEditText;
    private int mPreviousFocus = 0;
    private int mPreviousHour;
    private long mPreviousId;
    private int mPreviousMinute;
    private String mPreviousName;
    private int mPreviousSecond;
    private TimeTextWatcher mSecondTextWatcher;
    private EditText[] mTimeEditTexts = new EditText[4];
    private TimerAddPresetPopupListener mTimerAddPresetPopupListener;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$1 */
    class C07471 implements OnEditorActionListener {
        C07471() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId != 5) {
                return false;
            }
            if (v == TimerAddPresetPopup.this.mPresetHourEditText) {
                TimerAddPresetPopup.this.mPresetMinuteEditText.requestFocus();
            } else if (v == TimerAddPresetPopup.this.mPresetMinuteEditText) {
                TimerAddPresetPopup.this.mPresetSecondEditText.requestFocus();
            } else if (v == TimerAddPresetPopup.this.mPresetSecondEditText) {
                TimerAddPresetPopup.this.mPresetNameEditText.requestFocus();
            }
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$2 */
    class C07482 extends AccessibilityDelegate {
        C07482() {
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            int stringId;
            super.onInitializeAccessibilityNodeInfo(host, info);
            if (host.getId() == C0728R.id.hour) {
                stringId = C0728R.string.timer_hour;
            } else if (host.getId() == C0728R.id.minute) {
                stringId = C0728R.string.timer_minute;
            } else {
                stringId = C0728R.string.timer_second;
            }
            info.setText(((EditText) host).getText() + ", " + TimerAddPresetPopup.this.mContext.getResources().getString(stringId));
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$5 */
    class C07525 implements OnDismissListener {

        /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$5$1 */
        class C07511 implements Runnable {
            C07511() {
            }

            public void run() {
                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onSetSoftInputMode(true);
            }
        }

        C07525() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            if (TimerAddPresetPopup.this.mTimerAddPresetPopupListener.isMultiWindowMode()) {
                new Handler().postDelayed(new C07511(), 100);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$6 */
    class C07536 implements OnKeyListener {
        C07536() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case 19:
                case 21:
                case 22:
                    if (TimerAddPresetPopup.this.mPresetNameEditText.hasFocus() || event.getAction() != 0) {
                        return false;
                    }
                    int direction;
                    if (keyCode == 22) {
                        direction = 66;
                    } else if (keyCode == 21) {
                        direction = 17;
                    } else {
                        direction = 33;
                    }
                    View currentView = ((AlertDialog) dialog).getCurrentFocus();
                    if (currentView != null) {
                        View nextView = currentView.focusSearch(direction);
                        if (nextView != null) {
                            nextView.requestFocus(direction);
                        }
                    }
                    return true;
                case 66:
                    if (!TimerAddPresetPopup.this.mPresetNameEditText.hasFocus()) {
                        return false;
                    }
                    if (event.getAction() == 1) {
                        Button addButton = ((AlertDialog) dialog).getButton(-1);
                        Button cancelButton = ((AlertDialog) dialog).getButton(-2);
                        if (addButton.isEnabled()) {
                            addButton.callOnClick();
                        } else {
                            cancelButton.requestFocus();
                        }
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$7 */
    class C07547 implements OnTouchListener {
        C07547() {
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View v, MotionEvent event) {
            if (v == null || event.getActionMasked() != 0) {
                return false;
            }
            v.requestFocus();
            ((EditText) v).selectAll();
            InputMethodManager inputMethodManager = (InputMethodManager) TimerAddPresetPopup.this.mContext.getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(v, 0);
            }
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerAddPresetPopup$8 */
    class C07558 implements OnFocusChangeListener {
        C07558() {
        }

        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                ((TextView) view).setText(ClockUtils.toTwoDigitString(TimerAddPresetPopup.this.parseInt(((TextView) view).getText().toString())));
            }
        }
    }

    private class TimeTextWatcher implements TextWatcher {
        private int mChangedLen;
        private int mId;
        private String mPrevText;

        private TimeTextWatcher(int id) {
            this.mChangedLen = 0;
            this.mId = id;
        }

        public void afterTextChanged(Editable view) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.mPrevText = s.toString();
            this.mChangedLen = after;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (this.mPrevText.length() < s.length() && s.length() == 2 && TimerAddPresetPopup.this.mTimeEditTexts != null && TimerAddPresetPopup.this.mTimeEditTexts[this.mId] != null && TimerAddPresetPopup.this.mTimeEditTexts[this.mId].isFocused()) {
                changeFocus();
            }
            if (this.mId == 1 || this.mId == 2) {
                if (this.mChangedLen == 1 && s.length() > 0 && ((s.toString().equals("6") || s.toString().equals("7") || s.toString().equals("8") || s.toString().equals("9")) && TimerAddPresetPopup.this.mTimeEditTexts != null && TimerAddPresetPopup.this.mTimeEditTexts[this.mId] != null && TimerAddPresetPopup.this.mTimeEditTexts[this.mId].isFocused())) {
                    changeFocus();
                } else if (this.mChangedLen == 2 && s.length() > 1 && TimerAddPresetPopup.this.parseInt(s.toString()) > 59) {
                    TimerAddPresetPopup.this.mTimeEditTexts[this.mId].setText("59");
                    changeFocus();
                }
            }
            TimerAddPresetPopup.this.setEnabledAddPresetPopupButton();
        }

        private void changeFocus() {
            if (!((AccessibilityManager) TimerAddPresetPopup.this.mContext.getSystemService("accessibility")).isTouchExplorationEnabled()) {
                int next = this.mId + 1 > 3 ? -1 : this.mId + 1;
                if (next >= 0) {
                    TimerAddPresetPopup.this.mTimeEditTexts[next].requestFocus();
                    if (TimerAddPresetPopup.this.mTimeEditTexts[this.mId].isFocused()) {
                        TimerAddPresetPopup.this.mTimeEditTexts[this.mId].clearFocus();
                    }
                }
            }
        }
    }

    public TimerAddPresetPopup(Context context) {
        this.mContext = context;
    }

    public void setTimerAddPresetPopupListener(TimerAddPresetPopupListener i) {
        this.mTimerAddPresetPopupListener = i;
    }

    public void destroy() {
        this.mContext = null;
        if (this.mAddPresetDialog != null) {
            this.mAddPresetDialog.cancel();
            this.mAddPresetDialog = null;
        }
        this.mPresetNameEditText = null;
        if (this.mPresetHourEditText != null) {
            this.mPresetHourEditText.setOnTouchListener(null);
            this.mPresetHourEditText.setOnFocusChangeListener(null);
            this.mPresetHourEditText = null;
        }
        if (this.mPresetMinuteEditText != null) {
            this.mPresetMinuteEditText.setOnTouchListener(null);
            this.mPresetMinuteEditText.setOnFocusChangeListener(null);
            this.mPresetMinuteEditText = null;
        }
        if (this.mPresetSecondEditText != null) {
            this.mPresetSecondEditText.setOnTouchListener(null);
            this.mPresetSecondEditText.setOnFocusChangeListener(null);
            this.mPresetSecondEditText = null;
        }
        if (this.mTimeEditTexts != null) {
            if (this.mTimeEditTexts[0] != null) {
                this.mTimeEditTexts[0].removeTextChangedListener(this.mHourTextWatcher);
            }
            if (this.mTimeEditTexts[1] != null) {
                this.mTimeEditTexts[1].removeTextChangedListener(this.mMinuteTextWatcher);
            }
            if (this.mTimeEditTexts[2] != null) {
                this.mTimeEditTexts[2].removeTextChangedListener(this.mSecondTextWatcher);
            }
            this.mTimeEditTexts = null;
        }
        this.mHourTextWatcher = null;
        this.mMinuteTextWatcher = null;
        this.mSecondTextWatcher = null;
    }

    public boolean isAddPresetDialogShowing() {
        return this.mAddPresetDialog != null && this.mAddPresetDialog.isShowing();
    }

    public void dismissAddPresetPopup() {
        if (this.mAddPresetDialog != null && this.mAddPresetDialog.isShowing()) {
            this.mAddPresetDialog.dismiss();
        }
    }

    public void createAddPresetPopup(boolean isRecreate, boolean isErrorEnable, long presetId) {
        Log.secD("TimerAddPresetPopup", "createAddPresetPopup() / presetId = " + presetId + ", isRecreate = " + isRecreate);
        if (this.mAddPresetDialog == null || !this.mAddPresetDialog.isShowing()) {
            final int presetCount = TimerPresetItem.getPresetCount(this.mContext);
            if (presetId != -1 || presetCount < Timer.PRESET_MAX_COUNT) {
                int hour;
                int minute;
                int second;
                String name;
                int i;
                View view = LayoutInflater.from(this.mContext).inflate(C0728R.layout.timer_preset_add_popup, null);
                Builder builder = new Builder(this.mContext, C0728R.style.MyCustomThemeForDialog);
                builder.setView(view);
                final TimerPresetItem presetItem = this.mTimerAddPresetPopupListener.onGetPresetItemById(presetId);
                this.mPreviousId = presetId;
                builder.setTitle(presetId != -1 ? C0728R.string.timer_modify_preset : C0728R.string.timer_add_preset);
                if (isRecreate) {
                    hour = this.mPreviousHour;
                    minute = this.mPreviousMinute;
                    second = this.mPreviousSecond;
                    name = this.mPreviousName;
                } else if (presetId == -1 || presetItem == null) {
                    hour = TimerData.getInputHour();
                    minute = TimerData.getInputMin();
                    second = TimerData.getInputSec();
                    name = "";
                } else {
                    hour = presetItem.getHour();
                    minute = presetItem.getMinute();
                    second = presetItem.getSecond();
                    name = presetItem.getName();
                }
                TextInputLayout presetNameTextInputLayout = (TextInputLayout) view.findViewById(C0728R.id.preset_name_input_layout);
                this.mPresetNameEditText = (EditText) view.findViewById(C0728R.id.preset_name);
                this.mPresetNameEditText.setHint(this.mContext.getResources().getString(C0728R.string.timer_preset_add_popup_name));
                this.mPresetNameEditText.setText(name);
                TextLengthFilter textLengthFilter = new TextLengthFilter(this.mContext, presetNameTextInputLayout, 20, null);
                this.mPresetNameEditText.setFilters(new InputFilter[]{textLengthFilter});
                this.mPresetNameEditText.setSelection(this.mPresetNameEditText.length());
                this.mPresetHourEditText = (EditText) view.findViewById(C0728R.id.hour);
                this.mPresetMinuteEditText = (EditText) view.findViewById(C0728R.id.minute);
                this.mPresetSecondEditText = (EditText) view.findViewById(C0728R.id.second);
                this.mPresetHourEditText.setText(ClockUtils.toTwoDigitString(hour));
                this.mPresetMinuteEditText.setText(ClockUtils.toTwoDigitString(minute));
                this.mPresetSecondEditText.setText(ClockUtils.toTwoDigitString(second));
                this.mPresetHourEditText.setAccessibilityDelegate(this.mEditorAccessibilityDelegate);
                this.mPresetMinuteEditText.setAccessibilityDelegate(this.mEditorAccessibilityDelegate);
                this.mPresetSecondEditText.setAccessibilityDelegate(this.mEditorAccessibilityDelegate);
                if (StateUtils.isContextInDexMode(this.mContext)) {
                    int highlightColor = this.mContext.getColorStateList(C0728R.color.primary_dark_color).withAlpha(102).getDefaultColor();
                    this.mPresetHourEditText.setHighlightColor(highlightColor);
                    this.mPresetMinuteEditText.setHighlightColor(highlightColor);
                    this.mPresetSecondEditText.setHighlightColor(highlightColor);
                }
                if (!isRecreate) {
                    this.mPresetHourEditText.requestFocus();
                } else if (this.mPreviousFocus == 0) {
                    this.mPresetHourEditText.requestFocus();
                } else if (this.mPreviousFocus == 1) {
                    this.mPresetMinuteEditText.requestFocus();
                } else if (this.mPreviousFocus == 2) {
                    this.mPresetSecondEditText.requestFocus();
                } else {
                    this.mPresetNameEditText.requestFocus();
                }
                setEditTextListener(this.mPresetHourEditText);
                setEditTextListener(this.mPresetMinuteEditText);
                setEditTextListener(this.mPresetSecondEditText);
                setTextWatcher();
                String timeSeparator = ClockUtils.getTimeSeparatorText(this.mContext);
                ((TextView) view.findViewById(C0728R.id.hms_colon)).setText(timeSeparator);
                ((TextView) view.findViewById(C0728R.id.ms_colon)).setText(timeSeparator);
                if (presetId == -1) {
                    i = C0728R.string.add;
                } else {
                    i = C0728R.string.edit;
                }
                final long j = presetId;
                final long j2 = presetId;
                builder.setPositiveButton(i, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TimerAddPresetPopup.this.hideSoftInput();
                        dialog.dismiss();
                        int inputHour = TimerAddPresetPopup.this.parseInt(TimerAddPresetPopup.this.mPresetHourEditText.getText().toString());
                        int inputMinute = TimerAddPresetPopup.this.parseInt(TimerAddPresetPopup.this.mPresetMinuteEditText.getText().toString());
                        int inputSecond = TimerAddPresetPopup.this.parseInt(TimerAddPresetPopup.this.mPresetSecondEditText.getText().toString());
                        String inputName = TimerAddPresetPopup.this.mPresetNameEditText.getText().toString().replaceAll(System.lineSeparator(), " ");
                        TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onSetBackupTime(TimerData.getInputHour(), TimerData.getInputMin(), TimerData.getInputSec());
                        TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onDisablePickerEditMode();
                        TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onSetPickerTime(inputHour, inputMinute, inputSecond);
                        try {
                            long duplicatedPresetId = TimerAddPresetPopup.this.mTimerAddPresetPopupListener.getDuplicatedPresetId(inputName, inputHour, inputMinute, inputSecond);
                            if (duplicatedPresetId != -1) {
                                if (duplicatedPresetId != j) {
                                    String presetText;
                                    String message;
                                    if (inputName.length() > 0) {
                                        presetText = inputName;
                                    } else {
                                        presetText = TimerAddPresetPopup.this.getDisplayTime(inputHour, inputMinute, inputSecond);
                                    }
                                    if ("ja".equals(Locale.getDefault().getLanguage())) {
                                        message = TimerAddPresetPopup.this.mContext.getResources().getString(C0728R.string.timer_preset_already_in_list_ja);
                                    } else {
                                        message = TimerAddPresetPopup.this.mContext.getResources().getString(C0728R.string.timer_preset_already_in_list, new Object[]{presetText});
                                    }
                                    TimerAddPresetPopup.this.showToast(message);
                                    if (j != -1) {
                                        ArrayList<Long> presetIds = new ArrayList();
                                        presetIds.add(Long.valueOf(j));
                                        TimerPresetItem.deletePreset(TimerAddPresetPopup.this.mContext.getContentResolver(), presetIds);
                                    }
                                }
                                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onSetSelectedPresetId(duplicatedPresetId);
                                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onUpdatePresetView(j == -1);
                                ClockUtils.insertSaLog("130", j == -1 ? "1346" : "1347");
                            } else if (j == -1 || presetItem == null) {
                                int i;
                                TimerPresetItem item = new TimerPresetItem();
                                item.setName(inputName);
                                item.setTime(inputHour, inputMinute, inputSecond);
                                TimerPresetItem lastItem = TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onGetPresetItemByPosition(presetCount - 1);
                                if (lastItem == null) {
                                    i = 0;
                                } else {
                                    i = lastItem.getOrder() + 1;
                                }
                                item.setOrder(i);
                                TimerPresetItem.addPreset(TimerAddPresetPopup.this.mContext.getContentResolver(), item);
                                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onSetSelectedPresetId(item.getId());
                                if (!TimerData.isTimerStateResetedOrNone()) {
                                    TimerAddPresetPopup.this.showToast(TimerAddPresetPopup.this.mContext.getResources().getString(C0728R.string.timer_added));
                                }
                                if (j == -1) {
                                }
                                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onUpdatePresetView(j == -1);
                                if (j == -1) {
                                }
                                ClockUtils.insertSaLog("130", j == -1 ? "1346" : "1347");
                            } else {
                                presetItem.setName(inputName);
                                presetItem.setTime(inputHour, inputMinute, inputSecond);
                                TimerPresetItem.updatePreset(TimerAddPresetPopup.this.mContext.getContentResolver(), presetItem);
                                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onSetSelectedPresetId(j);
                                if (j == -1) {
                                }
                                TimerAddPresetPopup.this.mTimerAddPresetPopupListener.onUpdatePresetView(j == -1);
                                if (j == -1) {
                                }
                                ClockUtils.insertSaLog("130", j == -1 ? "1346" : "1347");
                            }
                        } catch (SQLiteFullException e) {
                            TimerAddPresetPopup.this.showToast(TimerAddPresetPopup.this.mContext.getString(C0728R.string.memory_full));
                        }
                    }
                }).setNegativeButton(C0728R.string.cancel, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TimerAddPresetPopup.this.hideSoftInput();
                        dialog.cancel();
                        if (j2 == -1) {
                            ClockUtils.insertSaLog("130", "1345");
                        }
                    }
                });
                builder.setOnDismissListener(new C07525());
                builder.setOnKeyListener(new C07536());
                this.mAddPresetDialog = builder.show();
                this.mAddPresetDialog.getWindow().setSoftInputMode(5);
                setEnabledAddPresetPopupButton();
                setFontFromOpenTheme();
                if (isRecreate && isErrorEnable) {
                    presetNameTextInputLayout.setError(this.mContext.getResources().getString(C0728R.string.input_max_message, new Object[]{Integer.valueOf(20)}));
                    presetNameTextInputLayout.setErrorEnabled(true);
                }
                if (this.mTimerAddPresetPopupListener.isMultiWindowMode()) {
                    this.mTimerAddPresetPopupListener.onSetSoftInputMode(false);
                    return;
                }
                return;
            }
            Log.secD("TimerAddPresetPopup", "createAddPresetPopup() / Preset count is max");
            Toast.makeText(this.mContext, this.mContext.getResources().getString(C0728R.string.timer_max_preset, new Object[]{Integer.valueOf(Timer.PRESET_MAX_COUNT)}), 1).show();
            return;
        }
        Log.secD("TimerAddPresetPopup", "createAddPresetPopup() / mAddPresetDialog is showing");
    }

    public void saveAddPresetPopup(Bundle outState) {
        if (this.mAddPresetDialog != null && this.mPresetNameEditText != null && this.mPresetHourEditText != null && this.mPresetMinuteEditText != null && this.mPresetSecondEditText != null && this.mAddPresetDialog.isShowing()) {
            outState.putBoolean("timer_add_popup_show", true);
            outState.putBoolean("timer_add_popup_error_enable", ((TextInputLayout) this.mAddPresetDialog.findViewById(C0728R.id.preset_name_input_layout)).isErrorEnabled());
            outState.putString("timer_add_popup_name", this.mPresetNameEditText.getText().toString());
            outState.putInt("timer_add_popup_hour", parseInt(this.mPresetHourEditText.getText().toString()));
            outState.putInt("timer_add_popup_minute", parseInt(this.mPresetMinuteEditText.getText().toString()));
            outState.putInt("timer_add_popup_second", parseInt(this.mPresetSecondEditText.getText().toString()));
            outState.putLong("timer_add_popup_preset_id", this.mPreviousId);
            outState.putInt("timer_add_popup_focus", getCurrentFocus());
        }
    }

    public void restoreAddPresetPopup(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean("timer_add_popup_show", false)) {
            boolean isErrorEnable = savedInstanceState.getBoolean("timer_add_popup_error_enable");
            this.mPreviousName = savedInstanceState.getString("timer_add_popup_name");
            this.mPreviousHour = savedInstanceState.getInt("timer_add_popup_hour");
            this.mPreviousMinute = savedInstanceState.getInt("timer_add_popup_minute");
            this.mPreviousSecond = savedInstanceState.getInt("timer_add_popup_second");
            this.mPreviousId = savedInstanceState.getLong("timer_add_popup_preset_id");
            this.mPreviousFocus = savedInstanceState.getInt("timer_add_popup_focus");
            createAddPresetPopup(true, isErrorEnable, this.mPreviousId);
        }
    }

    public void recreateAddPresetPopup() {
        if (this.mAddPresetDialog != null && this.mPresetNameEditText != null && this.mPresetHourEditText != null && this.mPresetMinuteEditText != null && this.mPresetSecondEditText != null && this.mAddPresetDialog.isShowing()) {
            boolean isErrorEnable = ((TextInputLayout) this.mAddPresetDialog.findViewById(C0728R.id.preset_name_input_layout)).isErrorEnabled();
            this.mPreviousName = this.mPresetNameEditText.getText().toString();
            this.mPreviousHour = parseInt(this.mPresetHourEditText.getText().toString());
            this.mPreviousMinute = parseInt(this.mPresetMinuteEditText.getText().toString());
            this.mPreviousSecond = parseInt(this.mPresetSecondEditText.getText().toString());
            this.mPreviousFocus = getCurrentFocus();
            this.mAddPresetDialog.dismiss();
            createAddPresetPopup(true, isErrorEnable, this.mPreviousId);
        }
    }

    private int getCurrentFocus() {
        if (this.mPresetHourEditText != null && this.mPresetHourEditText.hasSelection()) {
            return 0;
        }
        if (this.mPresetMinuteEditText != null && this.mPresetMinuteEditText.hasSelection()) {
            return 1;
        }
        if (this.mPresetSecondEditText == null || !this.mPresetSecondEditText.hasSelection()) {
            return 3;
        }
        return 2;
    }

    private int parseInt(String input) {
        input = input.replace(",", "");
        return (TextUtils.isEmpty(input) || !TextUtils.isDigitsOnly(input)) ? 0 : Integer.parseInt(input);
    }

    private void setEditTextListener(EditText view) {
        view.setCursorVisible(false);
        view.setOnTouchListener(new C07547());
        view.setOnFocusChangeListener(new C07558());
        view.setOnEditorActionListener(this.mEditorActionListener);
    }

    private void setTextWatcher() {
        this.mTimeEditTexts[0] = this.mPresetHourEditText;
        this.mTimeEditTexts[1] = this.mPresetMinuteEditText;
        this.mTimeEditTexts[2] = this.mPresetSecondEditText;
        this.mTimeEditTexts[3] = this.mPresetNameEditText;
        this.mHourTextWatcher = new TimeTextWatcher(0);
        this.mMinuteTextWatcher = new TimeTextWatcher(1);
        this.mSecondTextWatcher = new TimeTextWatcher(2);
        this.mTimeEditTexts[0].addTextChangedListener(this.mHourTextWatcher);
        this.mTimeEditTexts[1].addTextChangedListener(this.mMinuteTextWatcher);
        this.mTimeEditTexts[2].addTextChangedListener(this.mSecondTextWatcher);
    }

    public void showSoftInput() {
        if (this.mAddPresetDialog != null && this.mAddPresetDialog.isShowing()) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            if (inputMethodManager != null) {
                int i = 0;
                while (i <= 3) {
                    if (this.mTimeEditTexts[i] != null && this.mTimeEditTexts[i].hasFocus()) {
                        inputMethodManager.showSoftInput(this.mTimeEditTexts[i], 0);
                    }
                    i++;
                }
            }
        }
    }

    public void hideSoftInput() {
        if (this.mAddPresetDialog != null && this.mAddPresetDialog.isShowing()) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            if (inputMethodManager != null && this.mPresetNameEditText != null) {
                inputMethodManager.hideSoftInputFromWindow(this.mPresetNameEditText.getWindowToken(), 2);
            }
        }
    }

    private void setEnabledAddPresetPopupButton() {
        if (this.mAddPresetDialog != null && this.mPresetHourEditText != null && this.mPresetMinuteEditText != null && this.mPresetSecondEditText != null) {
            boolean enabled = (parseInt(this.mPresetHourEditText.getText().toString()) == 0 && parseInt(this.mPresetMinuteEditText.getText().toString()) == 0 && parseInt(this.mPresetSecondEditText.getText().toString()) == 0) ? false : true;
            Button addButton = this.mAddPresetDialog.getButton(-1);
            addButton.setEnabled(enabled);
            addButton.setFocusable(enabled);
        }
    }

    private void setFontFromOpenTheme() {
        if (this.mAddPresetDialog != null && this.mPresetHourEditText != null && this.mPresetMinuteEditText != null && this.mPresetSecondEditText != null) {
            Typeface font = ClockUtils.getFontFromOpenTheme(this.mContext);
            if (font == null) {
                font = Typeface.create("sans-serif-light", 0);
            }
            this.mPresetHourEditText.setTypeface(font);
            this.mPresetMinuteEditText.setTypeface(font);
            this.mPresetSecondEditText.setTypeface(font);
            ((TextView) this.mAddPresetDialog.findViewById(C0728R.id.hms_colon)).setTypeface(font);
            ((TextView) this.mAddPresetDialog.findViewById(C0728R.id.ms_colon)).setTypeface(font);
        }
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this.mContext, message, 1);
        toast.setGravity(81, 0, this.mContext.getResources().getDimensionPixelSize(Resources.getSystem().getIdentifier("toast_y_offset", "dimen", "android")));
        toast.show();
    }

    private String getDisplayTime(int hour, int minute, int second) {
        String timeSeparator = ClockUtils.getTimeSeparatorText(this.mContext);
        if (hour == 0) {
            return ClockUtils.toTwoDigitString(minute) + timeSeparator + ClockUtils.toTwoDigitString(second);
        }
        return ClockUtils.toTwoDigitString(hour) + timeSeparator + ClockUtils.toTwoDigitString(minute) + timeSeparator + ClockUtils.toTwoDigitString(second);
    }
}
