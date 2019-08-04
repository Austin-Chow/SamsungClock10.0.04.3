package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.util.TextLengthFilter;
import com.sec.android.app.clockpackage.common.util.TextLengthFilter.onErrorTextListener;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import java.util.Locale;

public class AlarmListDetail extends LinearLayout implements Runnable {
    private AlarmListClickListener mAlarmListClickListener;
    private AlarmRingtoneManager mAlarmRingtoneManager;
    private Switch mAlarmSoundSwitch;
    private TextView mAlarmSoundText;
    private Context mContext;
    private final Handler mHandler = new Handler();
    private View mHolidayWorkingday;
    private Switch mHolidayWorkingdayBtn;
    private boolean mIsErrorEnabled = false;
    private boolean mIsSupportBixbyAlarm = false;
    private boolean mIsSupportHolidayAlarm = false;
    private boolean mIsSupportNewCelebFeature = false;
    private boolean mIsSupportSubstituteHoliday = false;
    private boolean mIsWorkingDayFeature = false;
    private AlarmItem mItem;
    private Switch mSnoozeBtn;
    private Toast mToast;
    private TextView mVibPatternText;
    private Switch mVibrationBtn;

    public interface AlarmListClickListener {
        void onAlarmClickEvent();

        void setWorkingDayAlarm(boolean z);
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$1 */
    class C06051 implements OnClickListener {
        C06051() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            ClockUtils.insertSaLog("920", "6202", AlarmListDetail.this.getResources().getString(C0490R.string.later));
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$2 */
    class C06062 implements OnClickListener {
        C06062() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            AlarmUtil.sendOpenCalendarSettingIntent(AlarmListDetail.this.mContext);
            ClockUtils.insertSaLog("920", "6202", AlarmListDetail.this.getResources().getString(C0490R.string.setting));
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$3 */
    class C06073 implements View.OnClickListener {
        C06073() {
        }

        public void onClick(View v) {
            Log.secD("AlarmListDetail", "onClick : " + v);
            if (AlarmListDetail.this.mAlarmListClickListener != null) {
                AlarmListDetail.this.mAlarmListClickListener.onAlarmClickEvent();
            }
            if (v.isLaidOut()) {
                int id = v.getId();
                if (id == C0490R.id.alarm_holiday_boz || id == C0490R.id.alarm_workingday_boz) {
                    AlarmListDetail.this.stopPlayer();
                    if (AlarmListDetail.this.mHolidayWorkingdayBtn != null && AlarmListDetail.this.mHolidayWorkingdayBtn.isEnabled()) {
                        Log.secD("AlarmListDetail", "mAlarmHoliday onClick");
                        AlarmListDetail.this.mHolidayWorkingdayBtn.performClick();
                        return;
                    }
                    return;
                }
                return;
            }
            Log.secE("AlarmListDetail", "block OnClickListener");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$4 */
    class C06084 implements OnCheckedChangeListener {
        C06084() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.secD("AlarmListDetail", "onCheckedChanged : " + buttonView);
            if (AlarmListDetail.this.mAlarmListClickListener != null) {
                AlarmListDetail.this.mAlarmListClickListener.onAlarmClickEvent();
            }
            AlarmListDetail.this.stopPlayer();
            String event = null;
            int id = buttonView.getId();
            if (id == C0490R.id.alarm_sound_switch) {
                Log.secD("AlarmListDetail", "alarm_sound_switch : isChecked = " + isChecked);
                AlarmListDetail.this.mItem.setMasterSoundOn(isChecked);
                AlarmListDetail.this.setSoundSubText();
                event = "1036";
            } else if (id == C0490R.id.alarm_snooze_switch) {
                AlarmListDetail.this.mItem.mSnoozeActivate = isChecked;
                AlarmListDetail.this.setSnoozeTextValues();
                event = "1023";
            } else if (id == C0490R.id.alarm_vibrate_switch) {
                AlarmListDetail.this.setAlarmType(isChecked ? 2 : 0);
                AlarmListDetail.this.setVibPatternValues(AlarmListDetail.this.mItem.mVibrationPattern);
                event = "1025";
            } else if (id == C0490R.id.alarm_workingday_switch) {
                AlarmListDetail.this.mItem.setWorkingDay(isChecked);
                AlarmListDetail.this.mAlarmListClickListener.setWorkingDayAlarm(isChecked);
                if (AlarmListDetail.this.mHolidayWorkingdayBtn.isLaidOut() && isChecked) {
                    AlarmListDetail.this.onWorkingDayBtnTurnOn();
                }
                event = "6201";
            } else if (id == C0490R.id.alarm_holiday_kor_switch || id == C0490R.id.alarm_holiday_switch) {
                AlarmListDetail.this.mItem.setHoliday(isChecked);
                event = "6001";
            }
            if (event != null && buttonView.isLaidOut()) {
                ClockUtils.insertSaLog("103", event, isChecked ? "1" : "0");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$5 */
    class C06095 implements OnClickListener {
        C06095() {
        }

        public void onClick(DialogInterface dialog, int arg1) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$6 */
    class C06116 implements onErrorTextListener {
        C06116() {
        }

        public void onErrorText(boolean isErrorText) {
            if (isErrorText) {
                final View errorTextView = AlarmListDetail.this.findViewById(C0490R.id.textinput_error);
                if (errorTextView != null) {
                    errorTextView.post(new Runnable() {
                        public void run() {
                            View scrollView = AlarmListDetail.this.getRootView().findViewById(C0490R.id.nestedscrollview);
                            int scrollY = scrollView.getScrollY();
                            ViewGroup errorTextLayout = (ViewGroup) errorTextView.getParent();
                            int bottomOfErrorTextView = (AlarmListDetail.this.getRelativeTop(errorTextLayout) + errorTextLayout.getHeight()) - scrollY;
                            int topOfBottomView = AlarmListDetail.this.getRelativeTop(AlarmListDetail.this.getRootView().findViewById(C0490R.id.cancel_and_done_layout));
                            if (topOfBottomView <= bottomOfErrorTextView) {
                                scrollView.scrollTo(0, (scrollY + bottomOfErrorTextView) - topOfBottomView);
                            }
                        }
                    });
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$7 */
    class C06127 implements TextWatcher {
        C06127() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        public void afterTextChanged(Editable editable) {
            EditText userInputAlarmNameText = (EditText) AlarmListDetail.this.findViewById(C0490R.id.alarm_name);
            AlarmListDetail.this.mItem.mAlarmName = userInputAlarmNameText.getText().toString();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail$8 */
    class C06138 implements OnFocusChangeListener {
        C06138() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && AlarmListDetail.this.mItem != null) {
                ClockUtils.insertSaLog("103", "1230", AlarmListDetail.this.mItem.mAlarmName.isEmpty() ? "0" : "1");
            }
        }
    }

    private static final class SavedState extends BaseSavedState {
        AlarmItem item;

        SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public AlarmListDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContext(Context context) {
        this.mContext = new ContextThemeWrapper(context, C0490R.style.ClockTheme_AlarmEdit);
        this.mItem = new AlarmItem();
        initViews();
    }

    public void setOnAlarmListClickListener(AlarmListClickListener listener) {
        this.mAlarmListClickListener = listener;
    }

    private void initViews() {
        Log.secD("AlarmListDetail", "initViews()");
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C0490R.layout.alarm_list_detail_phone, this, true);
        this.mIsSupportHolidayAlarm = Feature.isSupportHolidayAlarm();
        this.mIsSupportSubstituteHoliday = Feature.isSupportSubstituteHolidayMenu();
        this.mIsWorkingDayFeature = Feature.isSupportAlarmOptionMenuForWorkingDay();
        this.mIsSupportBixbyAlarm = Feature.isSupportBixbyBriefingMenu(this.mContext);
        this.mIsSupportNewCelebFeature = Feature.isSupportCelebrityAlarm();
        ViewStub stub;
        if (this.mIsSupportHolidayAlarm) {
            stub = (ViewStub) findViewById(C0490R.id.stub_workingday_holiday);
            if (this.mIsSupportSubstituteHoliday) {
                stub.setLayoutResource(C0490R.layout.alarm_detail_kor_holiday_viewstub);
                stub.inflate();
                this.mHolidayWorkingday = findViewById(C0490R.id.alarm_holiday_kor_boz);
                this.mHolidayWorkingdayBtn = (Switch) findViewById(C0490R.id.alarm_holiday_kor_switch);
            } else {
                stub.setLayoutResource(C0490R.layout.alarm_detail_holiday_viewstub);
                stub.inflate();
                this.mHolidayWorkingday = findViewById(C0490R.id.alarm_holiday_boz);
                this.mHolidayWorkingdayBtn = (Switch) findViewById(C0490R.id.alarm_holiday_switch);
            }
        } else if (this.mIsWorkingDayFeature) {
            stub = (ViewStub) findViewById(C0490R.id.stub_workingday_holiday);
            stub.setLayoutResource(C0490R.layout.alarm_detail_workingday_viewstub);
            stub.inflate();
            this.mHolidayWorkingday = findViewById(C0490R.id.alarm_workingday_boz);
            this.mHolidayWorkingdayBtn = (Switch) findViewById(C0490R.id.alarm_workingday_switch);
        }
        this.mSnoozeBtn = (Switch) findViewById(C0490R.id.alarm_snooze_switch);
        this.mAlarmSoundSwitch = (Switch) findViewById(C0490R.id.alarm_sound_switch);
        this.mVibrationBtn = (Switch) findViewById(C0490R.id.alarm_vibrate_switch);
        this.mAlarmSoundText = (TextView) findViewById(C0490R.id.alarm_sound);
        this.mVibPatternText = (TextView) findViewById(C0490R.id.alarm_pattern_value);
        this.mAlarmRingtoneManager = new AlarmRingtoneManager(this.mContext);
        setOnClickListener();
        if (!Feature.isSupportVibration(this.mContext)) {
            findViewById(C0490R.id.alarm_pattern_boz).setVisibility(8);
            findViewById(C0490R.id.line6).setVisibility(8);
        }
        initAlarmName();
        View dummyView = findViewById(C0490R.id.dummy_view_to_take_focus_from_edittext);
        if (dummyView.isInTouchMode()) {
            dummyView.setFocusable(true);
        } else {
            dummyView.setFocusable(false);
        }
    }

    public void resumeViewState() {
        updateRingtoneStr();
        setAccessibilityEnable(StateUtils.isTalkBackEnabled(this.mContext));
        setSoundSubText();
    }

    public void pauseViewState() {
        stopPlayer();
    }

    protected Parcelable onSaveInstanceState() {
        Log.secD("AlarmListDetail", "onSaveInstanceState");
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        this.mItem.mSoundUri = getAlarmToneStr();
        savedState.item = this.mItem;
        return savedState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            Log.secD("AlarmListDetail", "onRestoreInstanceState " + state);
            SavedState savedState = (SavedState) state;
            initData(savedState.item);
            super.onRestoreInstanceState(savedState.getSuperState());
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public boolean isWorkingDayAlarm() {
        return this.mItem.isWorkingDay();
    }

    public AlarmItem getAlarmItem() {
        return this.mItem;
    }

    public int getAlarmToneIndex() {
        return this.mItem.mAlarmSoundTone;
    }

    public String getAlarmToneStr() {
        return this.mItem.mSoundUri;
    }

    public int getAlarmVolume() {
        return this.mItem.mAlarmVolume;
    }

    public void setAlarmType(int mType) {
        Log.secD("AlarmListDetail", "setAlarmType " + mType);
        this.mItem.mAlarmSoundType = mType;
        this.mVibrationBtn.setChecked(this.mItem.isVibrationAlarm());
    }

    public void setAlarmVolume(int mVolume) {
        Log.secD("AlarmListDetail", "setAlarmVolume " + mVolume);
        this.mItem.mAlarmVolume = mVolume;
        setSoundSubText();
    }

    private void initErrorText() {
        if (this.mIsErrorEnabled) {
            TextInputLayout txp = (TextInputLayout) findViewById(C0490R.id.alarm_name_input_layout);
            txp.setError(this.mContext.getResources().getString(C0490R.string.input_max_message, new Object[]{Integer.valueOf(20)}));
            txp.setErrorEnabled(true);
        }
    }

    public void setIsErrorEnabled(boolean flag) {
        this.mIsErrorEnabled = flag;
    }

    public boolean getErrorEnableState() {
        return ((TextInputLayout) findViewById(C0490R.id.alarm_name_input_layout)).isErrorEnabled();
    }

    public void initData() {
        Log.secD("AlarmListDetail", "initData()");
        if (this.mIsSupportSubstituteHoliday) {
            setSubstituteValue(this.mItem.isSubstituteHoliday());
        }
        setHolidayEnable(this.mItem.isWeeklyAlarm());
        if (this.mIsSupportNewCelebFeature && Feature.isCelebrityAlarmAsDefaultSoundMode()) {
            this.mItem.setSoundModeNewCeleb();
            this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        } else if (this.mIsSupportBixbyAlarm) {
            this.mItem.setSoundModeNewBixby();
        } else {
            this.mItem.setSoundModeAlarmTone();
        }
        this.mItem.initIncreasingVolume();
        this.mItem.initWeatherBg();
        this.mAlarmSoundSwitch.setChecked(this.mItem.isMasterSoundOn());
        setVibPatternValues(this.mItem.mVibrationPattern);
        setAlarmNameText(this.mItem.mAlarmName);
        setSnoozeValues(this.mItem.mSnoozeActivate, 1, 2);
        initRingtoneString();
        setSoundSubText();
    }

    public void initData(AlarmItem item) {
        Log.secD("AlarmListDetail", "initData(AlarmItem item)");
        this.mItem = (AlarmItem) item.clone();
        setAlarmNameText(item.mAlarmName);
        setSnoozeActiveValue(item.mSnoozeActivate);
        setSnoozeTextValues();
        if (!AlarmUtil.isValidVibPattern(this.mContext, item.mVibrationPattern)) {
            Log.secD("AlarmListDetail", "invalid vibration pattern = " + item.mVibrationPattern);
            item.mVibrationPattern = 50035;
        }
        setVibPatternValues(item.mVibrationPattern);
        Log.secD("AlarmListDetail", "item.mVibrationPattern = " + item.mVibrationPattern);
        if (item.mAlarmSoundType == 1) {
            Log.secE("AlarmListDetail", "saved alarmType : vibrate type. not support vibrate type.");
            item.mAlarmSoundType = 2;
            this.mItem.setMasterSoundOn(false);
        }
        setAlarmType(item.mAlarmSoundType);
        Log.secD("AlarmListDetail", "item.mAlarmSoundType = " + item.mAlarmSoundType);
        if (this.mHolidayWorkingdayBtn != null) {
            if (this.mIsSupportHolidayAlarm) {
                this.mHolidayWorkingdayBtn.setChecked(item.isHoliday());
                setHolidayEnable(item.isWeeklyAlarm());
            } else if (this.mIsWorkingDayFeature) {
                this.mHolidayWorkingdayBtn.setChecked(item.isWorkingDay());
            }
        }
        if (this.mIsSupportSubstituteHoliday) {
            setSubstituteValue(item.isSubstituteHoliday());
        }
        if (!PermissionUtils.hasPermissionExternalStorage(this.mContext) && AlarmRingtoneManager.isExternalRingtone(item.mSoundUri)) {
            item.mSoundUri = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(this.mContext).toString());
        }
        if (item.mSoundUri.equals("alarm_silent_ringtone")) {
            Log.m41d("AlarmListDetail", "Silent Ringtone. set master sound off");
            this.mItem.setMasterSoundOn(false);
            this.mItem.setSoundModeAlarmTone();
        }
        setRingtoneString(item.mSoundUri);
        if (!this.mIsSupportBixbyAlarm && item.isNewBixbyOn()) {
            if (this.mIsSupportNewCelebFeature) {
                Log.m41d("AlarmListDetail", "Not Support Bixby briefing & Bixby On => Celeb ON");
                this.mItem.setSoundModeNewCeleb();
                this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
            } else {
                Log.m41d("AlarmListDetail", "Not Support Bixby briefing & Bixby On => Sound ON");
                this.mItem.setSoundModeAlarmTone();
            }
        }
        if (this.mIsSupportNewCelebFeature) {
            setBixbyVoiceCelebValue(this.mItem.isNewBixbyOn(), this.mItem.isNewCelebOn(), this.mItem.mCelebVoicePath);
        }
        this.mItem.initIncreasingVolume();
        this.mItem.initWeatherBg();
        Log.secD("AlarmListDetail", "mIsAlarmToneOn : " + this.mItem.isAlarmToneOn());
        this.mAlarmSoundSwitch.setChecked(this.mItem.isMasterSoundOn());
        setSoundSubText();
    }

    public void setAlarmNameText(String name) {
        Log.secD("AlarmListDetail", "setAlarmNameText = " + name);
        EditText userInputAlarmNameText = (EditText) findViewById(C0490R.id.alarm_name);
        if (name.isEmpty()) {
            this.mItem.mAlarmName = "";
        } else {
            if (name.length() > 20) {
                name = name.substring(0, 20);
            }
            this.mItem.mAlarmName = name;
        }
        userInputAlarmNameText.setText(this.mItem.mAlarmName);
        userInputAlarmNameText.setSelection(this.mItem.mAlarmName.length());
        userInputAlarmNameText.setHint(getResources().getString(C0490R.string.alarmname));
    }

    public void removeInstance(boolean isDestroy) {
        if (this.mToast != null) {
            this.mToast.cancel();
            this.mToast = null;
        }
        if (this.mAlarmRingtoneManager != null) {
            this.mAlarmRingtoneManager.removeInstance();
            this.mAlarmRingtoneManager = null;
        }
        if (this.mHolidayWorkingday != null) {
            this.mHolidayWorkingday.setOnClickListener(null);
            this.mHolidayWorkingday = null;
        }
        if (this.mHolidayWorkingdayBtn != null) {
            this.mHolidayWorkingdayBtn.setOnCheckedChangeListener(null);
            this.mHolidayWorkingdayBtn = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this);
        }
        this.mItem = null;
        this.mContext = null;
        removeAllViews();
        destroyDrawingCache();
    }

    public void setHolidayEnable(boolean isEnable) {
        Log.secD("AlarmListDetail", "setHolidayEnable :" + isEnable + ", " + this.mIsSupportSubstituteHoliday);
        if (this.mIsSupportHolidayAlarm) {
            if (this.mHolidayWorkingday != null) {
                this.mHolidayWorkingday.setEnabled(isEnable);
            }
            if (this.mHolidayWorkingdayBtn != null) {
                this.mHolidayWorkingdayBtn.setEnabled(isEnable);
                if (!isEnable) {
                    this.mHolidayWorkingdayBtn.setChecked(false);
                }
            }
            findViewById(C0490R.id.alarm_holiday_subject).setEnabled(isEnable);
        }
    }

    public void setVibPatternValues(int vibPattern) {
        Log.secD("AlarmListDetail", "setVibPatternValues vibPattern = " + vibPattern);
        this.mItem.mVibrationPattern = vibPattern;
        if (this.mItem.isVibrationAlarm()) {
            Cursor mCursor = this.mContext.getContentResolver().query(Uri.parse("content://com.android.settings.personalvibration.PersonalVibrationProvider"), new String[]{"vibration_name"}, "vibration_pattern=" + vibPattern, null, null, null);
            if (mCursor != null && mCursor.moveToFirst()) {
                this.mVibPatternText.setText(mCursor.getString(mCursor.getColumnIndex("vibration_name")));
                mCursor.close();
                return;
            }
            return;
        }
        this.mVibPatternText.setText(getResources().getString(C0490R.string.switch_off));
    }

    public void setMasterSoundOn(boolean masterSoundOn) {
        this.mAlarmSoundSwitch.setChecked(masterSoundOn);
    }

    public void setAlarmToneOn(boolean isAlarmToneOn) {
        this.mItem.setAlarmToneOn(isAlarmToneOn);
    }

    public void setAlarmTts(boolean isTtsOn) {
        this.mItem.setTtsAlarm(isTtsOn);
    }

    public void setNewBixbyOn(boolean isBixbyOn) {
        this.mItem.setNewBixbyOn(isBixbyOn);
    }

    public String getNewCelebText(String celebVoicePath) {
        boolean isValid = AlarmUtil.isValidCelebrityVoicePath(this.mContext, celebVoicePath);
        Log.secD("AlarmListDetail", "getNewCelebText() / celeb path = " + celebVoicePath + "/ isValid = " + isValid);
        if (isValid) {
            return AlarmUtil.getCelebVoiceSubTextValue(this.mContext, celebVoicePath, false, false);
        }
        return getResources().getString(C0490R.string.default_celeb_title);
    }

    public void setBixbyVoiceCelebValue(boolean isNewBixbyOn, boolean isNewCelebOn, String celebVoicePath) {
        Log.secD("AlarmListDetail", "setBixbyVoiceCelebValue() / isNewCelebOn = " + isNewCelebOn + "/ isNewCelebOn = " + isNewCelebOn + "/ celeb path = " + celebVoicePath);
        if (isNewCelebOn && celebVoicePath != null && celebVoicePath.equals("")) {
            this.mItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        }
        this.mItem.setNewBixbyOn(isNewBixbyOn);
        this.mItem.setNewCelebOn(isNewCelebOn);
        this.mItem.mCelebVoicePath = celebVoicePath;
    }

    public void setSnoozeActiveValue(boolean isSnoozeActive) {
        if (this.mSnoozeBtn != null) {
            this.mSnoozeBtn.setChecked(isSnoozeActive);
        }
    }

    public void setSnoozeValues(boolean isSnoozeActive, int snoDuration, int snoRepeat) {
        Log.secD("AlarmListDetail", "setSnoozeValues isSnoozeActive = " + isSnoozeActive + ", snoDuration = " + snoDuration + ", snoRepeat = " + snoRepeat);
        this.mItem.mSnoozeActivate = isSnoozeActive;
        this.mItem.mSnoozeDuration = snoDuration;
        this.mItem.mSnoozeRepeat = snoRepeat;
        setSnoozeActiveValue(isSnoozeActive);
        setSnoozeTextValues();
    }

    @SuppressLint({"SetTextI18n"})
    private void setSnoozeTextValues() {
        TextView snoozeText = (TextView) findViewById(C0490R.id.alarm_snooze_value);
        if (this.mItem.mSnoozeActivate) {
            int duration;
            int repeat;
            switch (this.mItem.mSnoozeDuration) {
                case 1:
                    duration = C0490R.string.alarm_snooze_5min;
                    break;
                case 2:
                    duration = C0490R.string.alarm_snooze_10min;
                    break;
                case 3:
                    duration = C0490R.string.alarm_snooze_15min;
                    break;
                case 4:
                    duration = C0490R.string.alarm_snooze_30min;
                    break;
                default:
                    duration = C0490R.string.alarm_snooze_5min;
                    break;
            }
            switch (this.mItem.mSnoozeRepeat) {
                case 2:
                    repeat = C0490R.string.alarm_snooze_3times;
                    break;
                case 3:
                    repeat = C0490R.string.alarm_snooze_5times;
                    break;
                case 4:
                    repeat = C0490R.string.alarm_snooze_infinity;
                    break;
                default:
                    repeat = C0490R.string.alarm_snooze_3times;
                    break;
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                snoozeText.setText(getResources().getString(duration) + "، " + getResources().getString(repeat));
                return;
            } else if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage())) {
                snoozeText.setText(getResources().getString(duration) + "、" + getResources().getString(repeat));
                return;
            } else {
                snoozeText.setText(getResources().getString(duration) + ", " + getResources().getString(repeat));
                return;
            }
        }
        snoozeText.setText(getResources().getString(C0490R.string.switch_off));
    }

    public void setSubstituteValue(boolean isSubstituteHoliday) {
        String value;
        Log.secD("AlarmListDetail", "setSubstituteValue isSubstituteHoliday = " + isSubstituteHoliday);
        this.mItem.setSubstituteHoliday(isSubstituteHoliday);
        if (isSubstituteHoliday) {
            value = getResources().getString(C0490R.string.alarm_include_substitute);
        } else {
            value = getResources().getString(C0490R.string.alarm_exclude_substitute);
        }
        ((TextView) findViewById(C0490R.id.alarm_holiday_kor_value)).setText(value);
    }

    public void setHolidayWorkingdayValue(boolean isActive) {
        Log.secD("AlarmListDetail", "setHolidayWorkingdayValue isActive = " + isActive);
        if (this.mHolidayWorkingdayBtn != null) {
            this.mHolidayWorkingdayBtn.setChecked(isActive);
        }
    }

    private void showToastPopup(String toast) {
        if (this.mToast == null) {
            this.mToast = Toast.makeText(this.mContext, null, 1);
        }
        if (!this.mToast.getView().isShown()) {
            this.mToast.setText(toast);
            this.mToast.show();
        }
    }

    public void showVolumePopup(boolean volumeUp) {
        if (this.mItem.isMasterSoundOn()) {
            int i = this.mItem.mAlarmVolume;
            if (volumeUp) {
                i++;
                if (i > 15) {
                    i = 15;
                }
            } else {
                i--;
                if (i < 1) {
                    i = 1;
                }
            }
            Log.secD("AlarmListDetail", "showVolumePopup : " + i);
            setAlarmVolume(i);
            if (isPlaying()) {
                RingtonePlayer.setStreamVolume(this.mContext, i, true);
                return;
            } else {
                startPlayer();
                return;
            }
        }
        showToastPopup(this.mContext.getResources().getString(C0490R.string.alarm_tone_set_silent));
    }

    public Intent getSoundMainIntent() {
        Intent intent = new Intent();
        intent.setClassName(this.mContext, "com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity");
        intent.putExtra("alarm_master_sound_active", this.mItem.isMasterSoundOn());
        intent.putExtra("alarm_tone_off_option", !this.mItem.isAlarmToneOn());
        Uri existingUri = Uri.parse(Uri.decode(this.mItem.mSoundUri));
        Uri uri = RingtoneManager.getDefaultUri(4);
        Ringtone ringtone = RingtoneManager.getRingtone(this.mContext, existingUri);
        if (ringtone != null && ringtone.semIsUriValid()) {
            uri = existingUri;
        }
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
        intent.putExtra("alarm_volume_value", this.mItem.mAlarmVolume);
        intent.putExtra("alarm_tts_active", this.mItem.isTtsAlarm());
        intent.putExtra("alarm_bixby_voice_active", this.mItem.isNewBixbyOn());
        intent.putExtra("alarm_bixby_celebrity_active", this.mItem.isNewCelebOn());
        intent.putExtra("alarm_bixby_celebrity_path", this.mItem.mCelebVoicePath);
        intent.setFlags(393216);
        ClockUtils.insertSaLog("103", "1024");
        return intent;
    }

    public Intent getVibrationIntent() {
        Intent intent = new Intent();
        intent.setClassName(this.mContext, "com.sec.android.app.clockpackage.alarm.activity.AlarmVibPatternActivity");
        intent.putExtra("vibration_pattern", this.mItem.mVibrationPattern);
        intent.putExtra("alarm_type", this.mItem.mAlarmSoundType);
        intent.setFlags(393216);
        ClockUtils.insertSaLog("103", "1231");
        return intent;
    }

    public void setSoundSubText() {
        Log.secD("AlarmListDetail", "setSoundSubText() / isNewBixbyOn = " + this.mItem.isNewBixbyOn() + "/isNewCelebOn = " + this.mItem.isNewCelebOn() + "/isAlarmToneOn = " + this.mItem.isAlarmToneOn());
        String subText = "";
        if (!this.mItem.isMasterSoundOn()) {
            subText = getResources().getString(C0490R.string.switch_off);
        } else if (this.mItem.isAlarmToneOn()) {
            subText = this.mAlarmRingtoneManager.getRingtoneTitle(this.mItem.mSoundUri.toString());
        } else if (this.mIsSupportNewCelebFeature) {
            if (this.mIsSupportBixbyAlarm) {
                if (this.mItem.isNewCelebOn()) {
                    subText = getResources().getString(C0490R.string.alarm_bixby_alarm) + "(" + getNewCelebText(this.mItem.mCelebVoicePath) + ")";
                } else if (this.mItem.isNewBixbyOn()) {
                    subText = getResources().getString(C0490R.string.alarm_bixby_alarm) + "(" + getResources().getString(C0490R.string.bixby) + ")";
                }
            } else if (this.mItem.isNewCelebOn()) {
                subText = getNewCelebText(this.mItem.mCelebVoicePath);
            }
        } else if (this.mIsSupportBixbyAlarm && this.mItem.isNewBixbyOn()) {
            subText = getResources().getString(C0490R.string.alarm_bixby_alarm);
        }
        this.mAlarmSoundText.setVisibility(0);
        this.mAlarmSoundText.setText(subText);
        Log.secD("AlarmListDetail", "setSoundSubText() / " + this.mAlarmSoundText.getText());
    }

    public void updateRingtoneList() {
        this.mAlarmRingtoneManager.createRingtoneList();
    }

    public void setRingtoneString(String tone) {
        int count = this.mAlarmRingtoneManager.getRingtoneListSize();
        int index = this.mAlarmRingtoneManager.getRingtoneIndex(tone);
        Log.secD("AlarmListDetail", "setRingtoneString:" + tone + "index: " + index + " mItem.mAlarmSoundTone:" + this.mItem.mAlarmSoundTone + " count : " + count);
        if (index < 0 || index >= count) {
            initRingtoneString();
            return;
        }
        this.mItem.mSoundUri = tone;
        this.mItem.mAlarmSoundTone = index;
    }

    public int getDefaultRingtoneIndex() {
        return this.mAlarmRingtoneManager.getRingtoneIndex(this.mAlarmRingtoneManager.getAlarmTonePreference(this.mContext));
    }

    private void initRingtoneString() {
        String defaultAlarmTone;
        if (this.mAlarmRingtoneManager.hasCustomThemeRingtone()) {
            defaultAlarmTone = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(this.mContext).toString());
        } else {
            defaultAlarmTone = this.mAlarmRingtoneManager.getAlarmTonePreference(this.mContext);
        }
        this.mItem.mAlarmSoundTone = this.mAlarmRingtoneManager.getRingtoneIndex(defaultAlarmTone);
        this.mItem.mSoundUri = defaultAlarmTone;
        Log.secD("AlarmListDetail", "..initRingtoneString.. mSoundUri= " + this.mItem.mSoundUri);
    }

    public void updateRingtoneStr() {
        if (!AlarmRingtoneManager.validRingtoneStr(this.mContext, this.mItem.mSoundUri)) {
            this.mAlarmRingtoneManager.createRingtoneList();
            initRingtoneString();
        }
    }

    public Intent getAlarmSnoozeIntent() {
        Intent intent = new Intent();
        intent.setClassName(this.mContext, "com.sec.android.app.clockpackage.alarm.activity.AlarmSnoozeActivity");
        intent.putExtra("alarm_snooze_active", this.mItem.mSnoozeActivate);
        intent.putExtra("alarm_snooze_duration", this.mItem.mSnoozeDuration);
        intent.putExtra("alarm_snooze_repeat", this.mItem.mSnoozeRepeat);
        intent.setFlags(393216);
        ClockUtils.insertSaLog("103", "1022");
        return intent;
    }

    public Intent getSubstituteHolidayIntent() {
        Intent intent = new Intent();
        intent.setClassName(this.mContext, "com.sec.android.app.clockpackage.alarm.activity.AlarmHolidayActivity");
        intent.putExtra("alarm_holiday_active", this.mItem.isHoliday());
        intent.putExtra("alarm_substitute_holiday", this.mItem.isSubstituteHoliday());
        intent.setFlags(393216);
        return intent;
    }

    public void showUpdatePopup() {
        Builder updateNoticeDialog = new Builder(this.mContext);
        updateNoticeDialog.setTitle(C0490R.string.alarm_update_calendar_popup_title).setMessage(this.mContext.getString(C0490R.string.alarm_update_calendar_popup_message) + "\n" + this.mContext.getString(C0490R.string.alarm_update_calendar_go_setting)).setCancelable(true).setPositiveButton(C0490R.string.setting, new C06062()).setNegativeButton(C0490R.string.later, new C06051());
        ClockUtils.insertSaLog("920");
        updateNoticeDialog.show();
    }

    private void setOnClickListener() {
        View.OnClickListener clickListener = new C06073();
        if ((this.mIsSupportHolidayAlarm && !this.mIsSupportSubstituteHoliday) || this.mIsWorkingDayFeature) {
            this.mHolidayWorkingday.setOnClickListener(clickListener);
        }
        OnCheckedChangeListener checkedChangeListener = new C06084();
        this.mSnoozeBtn.setOnCheckedChangeListener(checkedChangeListener);
        this.mAlarmSoundSwitch.setOnCheckedChangeListener(checkedChangeListener);
        this.mVibrationBtn.setOnCheckedChangeListener(checkedChangeListener);
        if (this.mIsSupportHolidayAlarm || this.mIsWorkingDayFeature) {
            this.mHolidayWorkingdayBtn.setOnCheckedChangeListener(checkedChangeListener);
        }
    }

    private void onWorkingDayBtnTurnOn() {
        Log.secD("AlarmListDetail", "onWorkingDayBtnTurnOn");
        if (StateUtils.isUltraPowerSavingMode(this.mContext) && !Feature.hasActivity(this.mContext, new Intent("com.sec.android.intent.calendar.setting"))) {
            Builder myAlertDialog = new Builder(this.mContext);
            myAlertDialog.setTitle(C0490R.string.alarm_upsm_guide_title);
            myAlertDialog.setMessage(C0490R.string.alarm_upsm_guide_message);
            myAlertDialog.setPositiveButton(C0490R.string.okay, new C06095());
            myAlertDialog.show();
        } else if (!AlarmUtil.isValidChinaDB(this.mContext)) {
            showUpdatePopup();
        }
    }

    public void stopPlayer() {
        Log.secD("AlarmListDetail", "stop Player");
        this.mHandler.removeCallbacks(this);
        RingtonePlayer.stopMediaPlayer();
    }

    public void setAccessibilityEnable(boolean isEnable) {
        Log.secD("AlarmListDetail", "setAccessibilityEnable : " + isEnable);
        if (this.mHolidayWorkingdayBtn != null && !this.mIsSupportSubstituteHoliday) {
            this.mHolidayWorkingdayBtn.setClickable(!isEnable);
        }
    }

    public void run() {
        if (this.mItem.isAlarmToneOn()) {
            AlarmUtil.playRingtonePreview(this.mContext, Uri.parse(Uri.decode(this.mItem.mSoundUri)), this.mItem.mAlarmVolume, true);
        } else if (this.mIsSupportBixbyAlarm && this.mItem.isNewBixbyOn()) {
            AlarmUtil.playRingtonePreview(this.mContext, Uri.parse(Uri.decode("android.resource://com.sec.android.app.clockpackage/raw/default_sound")), this.mItem.mAlarmVolume, true);
        } else if (this.mIsSupportNewCelebFeature && this.mItem.isNewCelebOn()) {
            AlarmUtil.playCelebPreview(this.mContext, this.mItem.mCelebVoicePath, this.mItem.mAlarmVolume, true);
        } else {
            AlarmUtil.playRingtonePreview(this.mContext, Uri.parse(Uri.decode(this.mItem.mSoundUri)), this.mItem.mAlarmVolume, true);
            Log.secE("AlarmListDetail", "INVALID CASE");
        }
    }

    public boolean isPlaying() {
        return RingtonePlayer.getMediaPlayer().isPlaying();
    }

    private void startPlayer() {
        if (this.mItem.mAlarmSoundType != 1 && !isPlaying() && !StateUtils.isInCallState(this.mContext) && RingtonePlayer.requestAudioFocus(this.mContext)) {
            this.mHandler.removeCallbacks(this);
            this.mHandler.postDelayed(this, RingtonePlayer.isActiveStreamAlarm() ? 200 : 0);
        }
    }

    private int getRelativeTop(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        }
        return getRelativeTop((View) view.getParent()) + view.getTop();
    }

    private void initAlarmName() {
        Log.secE("AlarmListDetail", "initAlarmName");
        TextInputLayout textInputLayoutId = (TextInputLayout) findViewById(C0490R.id.alarm_name_input_layout);
        EditText userInputAlarmNameText = (EditText) findViewById(C0490R.id.alarm_name);
        if (this.mItem.mAlarmName.isEmpty()) {
            userInputAlarmNameText.setText("");
        } else {
            userInputAlarmNameText.setText(this.mItem.mAlarmName);
            userInputAlarmNameText.setSelection(this.mItem.mAlarmName.length());
        }
        TextLengthFilter textLengthFilter = new TextLengthFilter(this.mContext, textInputLayoutId, 20, new C06116());
        userInputAlarmNameText.setFilters(new InputFilter[]{textLengthFilter});
        userInputAlarmNameText.addTextChangedListener(new C06127());
        userInputAlarmNameText.setOnFocusChangeListener(new C06138());
    }

    public void reloadAlarmListDetailViewForFreeformMode(Context context, AlarmItem alarmitem) {
        setContext(context);
        initData(alarmitem);
        initErrorText();
    }
}
