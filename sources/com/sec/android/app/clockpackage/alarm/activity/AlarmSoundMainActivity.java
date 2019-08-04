package com.sec.android.app.clockpackage.alarm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SemSystemProperties;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmTtsUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmWeatherUtil;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.callback.VolumeProgressListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar.SubAppBarPressListener;
import com.sec.android.app.clockpackage.common.view.VolumeBar;
import com.sec.android.app.clockpackage.common.view.VolumeBar.VolumeIconPressListener;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtoneRecommender;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtoneRecommender.OnHighlightResultListener;
import com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder;
import com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder.HighlightButtonClickListener;

public class AlarmSoundMainActivity extends ClockActivity implements Runnable {
    private final int MODE_ALARMTONE = 1;
    private final int MODE_BIXBY = 2;
    private final int MODE_VOICE = 3;
    private final String TAG = "AlarmSoundMainActivity";
    private AlarmRingtoneManager mAlarmRingtoneManager;
    private RadioButton mAlarmToneBtn;
    private View mAlarmToneLayout;
    private TextView mAlarmToneText;
    private View mAlarmTtsLayout;
    private Switch mAlarmTtsSwitch;
    private AlarmTtsUtil mAlarmTtsUtil;
    private int mAlarmVolume;
    private VolumeBar mAlarmVolumeBar;
    private RadioButton mBixbyBtn;
    private String mCelebrityVoicePath;
    private Context mContext;
    private int mExtraSupportMenu;
    private final Handler mHandler = new Handler();
    private boolean mIsAlarmTonedOn = false;
    private boolean mIsDialogShown = false;
    private boolean mIsMasterSoundOn = false;
    private boolean mIsNewBixbyOn = false;
    private boolean mIsNewCelebOn = false;
    private boolean mIsPermissionPopup = false;
    private boolean mIsSupportBixbyBriefing = false;
    private boolean mIsSupportNewCelebVoice = false;
    private boolean mIsTalkBackEnabled = false;
    private boolean mIsTtsOn = false;
    private int mOffset = 0;
    private AlertDialog mRecommendDialog;
    private RingtoneRecommender mRecommender;
    private OnHighlightResultListener mResultListener = new OnHighlightResultListener() {
        public void onResult(int status, final int offset) {
            Log.secD("AlarmSoundMainActivity", "mResultListener() - status: " + status + " offset: " + offset);
            if (status == 1 && offset != 0) {
                AlarmSoundMainActivity.this.mRingtoneRecommendHandler.post(new Runnable() {
                    public void run() {
                        AlarmSoundMainActivity.this.showRecommendRingtoneDialog(Uri.parse(Uri.decode(AlarmSoundMainActivity.this.mUri.toString())), offset);
                    }
                });
            }
            AlarmSoundMainActivity.this.mRecommender.close();
        }
    };
    private final Handler mRingtoneRecommendHandler = new Handler();
    private int mSelectedMode = 1;
    private Toast mToast;
    private Uri mUri;
    private RadioButton mVoiceBtn;
    private TextView mVoiceRadioTitle;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$1 */
    class C05251 implements Runnable {
        C05251() {
        }

        public void run() {
            AlarmSoundMainActivity.this.showRecommendRingtoneDialog(AlarmSoundMainActivity.this.mUri, AlarmSoundMainActivity.this.mOffset);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$2 */
    class C05262 implements SubAppBarPressListener {
        C05262() {
        }

        public void setChecked(boolean isChecked) {
            AlarmSoundMainActivity.this.stopPlayer();
            AlarmSoundMainActivity.this.mIsMasterSoundOn = isChecked;
            AlarmSoundMainActivity.this.mAlarmVolumeBar.setEnableVolumeOption(AlarmSoundMainActivity.this.mIsMasterSoundOn);
            AlarmSoundMainActivity.this.setEnableLayout(isChecked);
            Log.secD("AlarmSoundMainActivity", "mIsMasterSoundOn : " + AlarmSoundMainActivity.this.mIsMasterSoundOn);
            ClockUtils.insertSaLog(AlarmSoundMainActivity.this.getCurrentScreenId(), "1036", isChecked ? "1" : "0");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$3 */
    class C05273 implements VolumeProgressListener {
        C05273() {
        }

        public void onProgressChanged(int progress) {
            AlarmSoundMainActivity.this.mAlarmVolume = progress;
            if (AlarmSoundMainActivity.this.isPlaying()) {
                RingtonePlayer.setStreamVolume(AlarmSoundMainActivity.this.mContext, progress, false);
            } else {
                AlarmSoundMainActivity.this.startPlayer();
            }
            ClockUtils.insertSaLog(AlarmSoundMainActivity.this.getCurrentScreenId(), "1057", Integer.toString(AlarmSoundMainActivity.this.mAlarmVolume));
        }

        public void onStartTrackingTouch() {
            Log.secD("AlarmSoundMainActivity", "mAlarmVolumeBar / onStartTrackingTouch start / mAlarmVolume = " + AlarmSoundMainActivity.this.mAlarmVolume);
            AlarmSoundMainActivity.this.startPlayer();
        }

        public void onStopTrackingTouch() {
            Log.secD("AlarmSoundMainActivity", "mAlarmVolumeBar / onStopTrackingTouch");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$4 */
    class C05284 implements VolumeIconPressListener {
        C05284() {
        }

        public void onStopPlay() {
            AlarmSoundMainActivity.this.stopPlayer();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$5 */
    class C05295 implements OnCheckedChangeListener {
        C05295() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.secD("AlarmSoundMainActivity", buttonView + " isChecked : " + isChecked);
            AlarmSoundMainActivity.this.stopPlayer();
            if (buttonView.getId() == C0490R.id.alarm_tts_switch) {
                AlarmSoundMainActivity.this.mIsTtsOn = isChecked;
                if (isChecked && AlarmSoundMainActivity.this.mAlarmTtsSwitch.isLaidOut() && AlarmSoundMainActivity.this.mAlarmTtsUtil != null && AlarmSoundMainActivity.this.mAlarmTtsUtil.isOpenTextToSpeech() && AlarmSoundMainActivity.this.mAlarmTtsUtil.getTtsDialogErrorCode() != -1) {
                    AlarmSoundMainActivity.this.showTtsDialog();
                }
                ClockUtils.insertSaLog(AlarmSoundMainActivity.this.getCurrentScreenId(), isChecked ? "1" : "0");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$6 */
    class C05306 implements Runnable {
        C05306() {
        }

        public void run() {
            AlarmSoundMainActivity.this.openTextToSpeech();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$7 */
    class C05317 implements OnClickListener {
        C05317() {
        }

        public void onClick(DialogInterface dialog, int arg1) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$8 */
    class C05328 implements OnClickListener {
        C05328() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            if (StateUtils.isUltraPowerSavingMode(AlarmSoundMainActivity.this.mContext)) {
                AlarmSoundMainActivity.this.showToastPopup(AlarmSoundMainActivity.this.getResources().getString(C0490R.string.cant_open_maximum_power_saving, new Object[]{AlarmSoundMainActivity.this.getResources().getString(C0490R.string.setting)}));
                return;
            }
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.Settings$TextToSpeechSettingsActivity");
            try {
                AlarmSoundMainActivity.this.mContext.startActivity(intent);
            } catch (Exception e) {
                Log.secE("AlarmSoundMainActivity", "Exception : " + e.toString());
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmSoundMainActivity$9 */
    class C05339 implements OnClickListener {
        C05339() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlarmSoundMainActivity", "onCreate() / savedInstanceState = " + savedInstanceState);
        setVolumeControlStream(4);
        setContentView(C0490R.layout.alarm_sound_main);
        ScrollView scrollView = (ScrollView) findViewById(C0490R.id.scrollView);
        try {
            scrollView.semSetRoundedCorners(15);
            scrollView.semSetRoundedCornerColor(15, getColor(C0490R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmSoundMainActivity", "NoSuchMethodError : " + e);
        }
        if (!(StateUtils.isContextInDexMode(this) || StateUtils.isCustomTheme(this))) {
            scrollView.setBackground(getDrawable(C0490R.drawable.contents_area_background));
        }
        initData(savedInstanceState);
        TextView radioTitle = (TextView) findViewById(C0490R.id.ringtone_radio_title);
        this.mAlarmToneBtn = (RadioButton) findViewById(C0490R.id.ringtone_radio_button);
        this.mAlarmToneBtn.setContentDescription(radioTitle.getText().toString());
        this.mAlarmToneLayout = findViewById(C0490R.id.alarm_tone_box);
        this.mAlarmToneText = (TextView) findViewById(C0490R.id.alarm_tone_text);
        this.mAlarmTtsLayout = findViewById(C0490R.id.alarm_tts_box);
        this.mAlarmTtsSwitch = (Switch) findViewById(C0490R.id.alarm_tts_switch);
        this.mAlarmToneBtn.setChecked(this.mIsAlarmTonedOn);
        this.mAlarmToneBtn.setVisibility(0);
        this.mAlarmTtsSwitch.setChecked(this.mIsTtsOn);
        this.mBixbyBtn = (RadioButton) findViewById(C0490R.id.bixby_radio_button);
        TextView bixbyDescription = (TextView) findViewById(C0490R.id.bixby_description);
        if ("CN".equalsIgnoreCase(SemSystemProperties.getCountryIso())) {
            bixbyDescription.setText(getString(C0490R.string.alarm_bixby_description_chn));
        } else {
            CharSequence string;
            if (Feature.isSupportNewsCpForBixbyBriefing()) {
                string = getString(C0490R.string.alarm_bixby_description);
            } else {
                string = getString(C0490R.string.alarm_bixby_description_not_include_news);
            }
            bixbyDescription.setText(string);
        }
        this.mBixbyBtn.setContentDescription(bixbyDescription.getText().toString());
        findViewById(C0490R.id.voice_box);
        this.mVoiceRadioTitle = (TextView) findViewById(C0490R.id.voice_radio_title);
        this.mVoiceBtn = (RadioButton) findViewById(C0490R.id.voice_radio_button);
        String voiceWeatherLogoTts = "";
        if (this.mIsSupportBixbyBriefing) {
            this.mVoiceRadioTitle.setText(getString(C0490R.string.alarm_bixby_alarm));
            ImageView voiceWeatherLogo = (ImageView) findViewById(C0490R.id.voice_weather_logo);
            if (voiceWeatherLogo != null) {
                AlarmWeatherUtil.setCpLogo(this.mContext, voiceWeatherLogo);
                voiceWeatherLogoTts = (getString(C0490R.string.powered_by) + " ") + voiceWeatherLogo.getContentDescription().toString();
            }
        } else {
            this.mVoiceRadioTitle.setText(getString(C0490R.string.alarm_celebrity_alarm));
            findViewById(C0490R.id.voice_weather_logo_layout).setVisibility(8);
        }
        this.mVoiceBtn.setContentDescription(this.mVoiceRadioTitle.getText() + getResources().getString(C0490R.string.alarm_celeb_voice_description) + voiceWeatherLogoTts);
        addVolumeBarView();
        addSubAppBarView();
        setClickListener();
        if (AlarmRingtoneManager.isExternalRingtone(this.mUri.toString()) && !PermissionUtils.hasPermissionExternalStorage(this)) {
            findViewById(C0490R.id.alarm_sound_body).setVisibility(4);
            this.mIsPermissionPopup = true;
            PermissionUtils.requestPermissions((Activity) this, 1);
        }
        if (this.mIsSupportNewCelebVoice) {
            this.mExtraSupportMenu = 3;
            setBixbyVisibility(8);
            setVoiceVisibility(0);
            setCelebVoicePathText();
        } else if (this.mIsSupportBixbyBriefing) {
            this.mExtraSupportMenu = 2;
            setBixbyVisibility(0);
            setVoiceVisibility(8);
        } else {
            setBixbyVisibility(8);
            setVoiceVisibility(8);
            findViewById(C0490R.id.ringtone_radio_box).setVisibility(8);
            findViewById(C0490R.id.alarm_ringtone_line).setVisibility(8);
            findViewById(C0490R.id.alarm_tone_layout).setPadding(0, 0, 0, 0);
            findViewById(C0490R.id.alarm_tts_layout).setPadding(0, 0, 0, 0);
            ((LayoutParams) findViewById(C0490R.id.alarm_tone_line).getLayoutParams()).setMarginStart(0);
            select(1);
        }
        int mode = 1;
        if (this.mIsAlarmTonedOn) {
            mode = 1;
        } else if (this.mIsSupportNewCelebVoice && (this.mIsNewCelebOn || this.mIsNewBixbyOn)) {
            mode = 3;
        } else if (this.mIsSupportBixbyBriefing && this.mIsNewBixbyOn) {
            mode = 2;
        }
        select(mode);
        setEnableLayout(this.mIsMasterSoundOn);
        setAlarmToneUri(this.mUri);
    }

    private void initData(Bundle savedInstanceState) {
        Log.secD("AlarmSoundMainActivity", "initData()");
        this.mContext = this;
        this.mAlarmRingtoneManager = new AlarmRingtoneManager(this.mContext);
        this.mIsSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(this.mContext);
        this.mIsSupportNewCelebVoice = Feature.isSupportCelebrityAlarm();
        Log.secD("AlarmSoundMainActivity", "initData() / mIsSupportBixbyBriefing = " + this.mIsSupportBixbyBriefing + "/ mIsSupportBixbyCelebVoice = " + this.mIsSupportNewCelebVoice);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            this.mIsMasterSoundOn = savedInstanceState.getBoolean("alarm_master_sound_active");
            this.mIsAlarmTonedOn = !savedInstanceState.getBoolean("alarm_tone_off_option");
            this.mIsTtsOn = savedInstanceState.getBoolean("alarm_tts_active");
            this.mAlarmVolume = savedInstanceState.getInt("alarm_volume_value");
            this.mUri = (Uri) savedInstanceState.getParcelable("android.intent.extra.ringtone.EXISTING_URI");
            this.mOffset = savedInstanceState.getInt("offset", 0);
            if (this.mIsSupportNewCelebVoice) {
                this.mIsNewCelebOn = savedInstanceState.getBoolean("alarm_bixby_celebrity_active");
                this.mCelebrityVoicePath = savedInstanceState.getString("alarm_bixby_celebrity_path");
            }
            if (this.mIsSupportBixbyBriefing) {
                this.mIsNewBixbyOn = savedInstanceState.getBoolean("alarm_bixby_voice_active");
            }
            if (savedInstanceState.getBoolean("alert_dialog", false)) {
                this.mRingtoneRecommendHandler.post(new C05251());
            }
        } else {
            this.mIsMasterSoundOn = intent.getBooleanExtra("alarm_master_sound_active", false);
            this.mIsAlarmTonedOn = !intent.getBooleanExtra("alarm_tone_off_option", this.mIsSupportBixbyBriefing);
            this.mIsTtsOn = intent.getBooleanExtra("alarm_tts_active", false);
            this.mAlarmVolume = intent.getIntExtra("alarm_volume_value", 11);
            this.mUri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
            if (this.mIsSupportBixbyBriefing) {
                this.mIsNewBixbyOn = intent.getBooleanExtra("alarm_bixby_voice_active", true);
            }
            if (this.mIsSupportNewCelebVoice) {
                this.mIsNewCelebOn = intent.getBooleanExtra("alarm_bixby_celebrity_active", true);
                this.mCelebrityVoicePath = intent.getStringExtra("alarm_bixby_celebrity_path");
            }
        }
        if (this.mUri == null) {
            initAlarmTone();
        }
        Log.secD("AlarmSoundMainActivity", "initData() / mIsMasterSoundOn = " + this.mIsMasterSoundOn + "/ mIsNewBixbyOn = " + this.mIsNewBixbyOn + "/ mIsNewCelebOn = " + this.mIsNewCelebOn + "/ mCelebrityVoicePath = " + this.mCelebrityVoicePath);
        Log.secD("AlarmSoundMainActivity", "mUri = " + this.mUri);
    }

    private void setCelebVoicePathText() {
        Log.secD("AlarmSoundMainActivity", "setCelebVoicePathText() / mIsNewBixbyOn = " + this.mIsNewBixbyOn + "/mIsNewCelebOn =" + this.mIsNewCelebOn + "/mCelebrityVoicePath =" + this.mCelebrityVoicePath);
        TextView celebrityValue = (TextView) findViewById(C0490R.id.selected_voice_value);
        if (this.mIsNewCelebOn && !ClockUtils.isEnableString(this.mCelebrityVoicePath)) {
            this.mCelebrityVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        }
        boolean isValid = AlarmUtil.isValidCelebrityVoicePath(this.mContext, this.mCelebrityVoicePath);
        if (this.mIsNewBixbyOn) {
            celebrityValue.setText(getString(C0490R.string.bixby));
        } else if (isValid) {
            celebrityValue.setText(AlarmUtil.getCelebVoiceSubTextValue(this.mContext, this.mCelebrityVoicePath, false, false));
        } else {
            this.mCelebrityVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
            celebrityValue.setText(getResources().getString(C0490R.string.default_celeb_title));
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.secD("AlarmSoundMainActivity", "onSaveInstanceState()");
        outState.putBoolean("alarm_master_sound_active", this.mIsMasterSoundOn);
        outState.putBoolean("alarm_tone_off_option", !this.mIsAlarmTonedOn);
        outState.putBoolean("alarm_tts_active", this.mIsTtsOn);
        outState.putParcelable("android.intent.extra.ringtone.EXISTING_URI", this.mUri);
        outState.putBoolean("alert_dialog", this.mIsDialogShown);
        outState.putInt("offset", this.mOffset);
        outState.putInt("alarm_volume_value", this.mAlarmVolume);
        outState.putBoolean("alarm_bixby_voice_active", this.mIsNewBixbyOn);
        outState.putBoolean("alarm_bixby_celebrity_active", this.mIsNewCelebOn);
        outState.putString("alarm_bixby_celebrity_path", this.mCelebrityVoicePath);
    }

    private void addSubAppBarView() {
        ClockSubAppBar subAppBar = (ClockSubAppBar) findViewById(C0490R.id.sub_appbar_layout);
        subAppBar.setSubAppBarPressListener(new C05262());
        subAppBar.setChecked(this.mIsMasterSoundOn);
    }

    private String getCurrentScreenId() {
        if (this.mIsSupportNewCelebVoice) {
            return "612";
        }
        if (this.mIsSupportBixbyBriefing) {
            return "202";
        }
        return "201";
    }

    private void addVolumeBarView() {
        Log.secD("AlarmSoundMainActivity", "addVolumeBarView");
        this.mAlarmVolumeBar = (VolumeBar) findViewById(C0490R.id.alarm_volume_bar);
        this.mAlarmVolumeBar.init(this.mAlarmVolume);
        this.mAlarmVolumeBar.setEnableVolumeOption(this.mIsMasterSoundOn);
        this.mAlarmVolumeBar.setOnVolumeBarListener(new C05273());
        this.mAlarmVolumeBar.setVolumeIconPressListener(new C05284());
    }

    public void run() {
        Uri previewUri = null;
        if (this.mSelectedMode == 1 && this.mIsAlarmTonedOn) {
            previewUri = Uri.parse(Uri.decode(this.mUri.toString()));
            AlarmUtil.playRingtonePreview(this.mContext, previewUri, this.mAlarmVolume, false);
        } else if (this.mIsSupportBixbyBriefing && this.mIsNewBixbyOn) {
            previewUri = Uri.parse(Uri.decode("android.resource://com.sec.android.app.clockpackage/raw/default_sound"));
            AlarmUtil.playRingtonePreview(this.mContext, previewUri, this.mAlarmVolume, false);
        } else if (this.mSelectedMode == 3 && this.mIsSupportNewCelebVoice) {
            AlarmUtil.playCelebPreview(this.mContext, this.mCelebrityVoicePath, this.mAlarmVolume, false);
        } else {
            Log.m42e("AlarmSoundMainActivity", "INVALID CASE! PLEASE CHECK");
        }
        Log.secD("AlarmSoundMainActivity", "run() / preViewUri = " + previewUri + "/ mAlarmVolume = " + this.mAlarmVolume);
    }

    private void startPlayer() {
        Log.secD("AlarmSoundMainActivity", "startPlayer()");
        if (semIsResumed() && this.mIsMasterSoundOn && !isPlaying() && !StateUtils.isInCallState(this.mContext) && RingtonePlayer.requestAudioFocus(getApplicationContext())) {
            this.mHandler.removeCallbacks(this);
            this.mHandler.postDelayed(this, RingtonePlayer.isActiveStreamAlarm() ? 200 : 0);
        }
    }

    private boolean isPlaying() {
        return RingtonePlayer.getMediaPlayer().isPlaying();
    }

    private void stopPlayer() {
        Log.secD("AlarmSoundMainActivity", "stopPlayer");
        this.mHandler.removeCallbacks(this);
        RingtonePlayer.stopMediaPlayer();
    }

    public void optionClicked(View view) {
        Log.secD("AlarmSoundMainActivity", "optionClicked() : " + view);
        int id = view.getId();
        if (id == C0490R.id.bixby_box || id == C0490R.id.bixby_radio_button) {
            select(2);
            ClockUtils.insertSaLog(getCurrentScreenId(), "1060", "Bixby alarm");
        } else if (id == C0490R.id.voice_box || id == C0490R.id.voice_radio_button) {
            select(3);
            ClockUtils.insertSaLog(getCurrentScreenId(), "1060", "Voice alarm");
        } else if (id == C0490R.id.ringtone_radio_box || id == C0490R.id.ringtone_radio_button) {
            select(1);
            ClockUtils.insertSaLog(getCurrentScreenId(), "1060", "Alarm sound");
        } else if (id == C0490R.id.alarm_tts_box) {
            this.mAlarmTtsSwitch.performClick();
        } else if (id == C0490R.id.alarm_tone_box) {
            if (PermissionUtils.hasPermissionExternalStorage(this) || this.mIsPermissionPopup) {
                startActivityForResult(getRingtonePickerIntent(), 10004);
            } else {
                PermissionUtils.requestPermissions((Activity) this, 1);
                this.mIsPermissionPopup = true;
            }
            ClockUtils.insertSaLog(getCurrentScreenId(), "1061");
        } else if (id == C0490R.id.selected_voice_box) {
            startCelebrityVoiceActivity();
        }
    }

    private void setClickListener() {
        this.mAlarmTtsSwitch.setOnCheckedChangeListener(new C05295());
    }

    private void startCelebrityVoiceActivity() {
        Intent intent = new Intent();
        intent.setClassName(this.mContext, "com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity");
        intent.putExtra("alarm_bixby_voice_active", this.mIsNewBixbyOn);
        intent.putExtra("alarm_bixby_celebrity_active", this.mIsNewCelebOn);
        intent.putExtra("alarm_bixby_celebrity_path", this.mCelebrityVoicePath);
        intent.putExtra("alarm_volume_value", this.mAlarmVolume);
        intent.setFlags(393216);
        startActivityForResult(intent, 10013);
    }

    private void select(int selection) {
        Log.secD("AlarmSoundMainActivity", "select() / selection = " + selection);
        this.mSelectedMode = selection;
        stopPlayer();
        switch (selection) {
            case 1:
                if (this.mExtraSupportMenu == 2) {
                    selectBixby(false);
                } else if (this.mExtraSupportMenu == 3) {
                    selectVoice(false);
                }
                selectAlarmTone(true);
                return;
            case 2:
                selectAlarmTone(false);
                selectBixby(true);
                return;
            case 3:
                selectAlarmTone(false);
                selectVoice(true);
                return;
            default:
                Log.secD("AlarmSoundMainActivity", "Invaild selection value");
                return;
        }
    }

    private void selectAlarmTone(boolean isSelected) {
        Log.secD("AlarmSoundMainActivity", "selectAlarmTone() / isSelcted = " + isSelected);
        this.mIsAlarmTonedOn = isSelected;
        this.mAlarmToneBtn.setChecked(isSelected);
        enableAlarmToneLayout(isSelected);
        enableTtsLayout(isSelected);
    }

    private void selectBixby(boolean isSelected) {
        Log.secD("AlarmSoundMainActivity", "selectBixby() / isSelcted = " + isSelected);
        this.mIsNewBixbyOn = isSelected;
        this.mBixbyBtn.setChecked(isSelected);
    }

    private void selectVoice(boolean isSelected) {
        Log.secD("AlarmSoundMainActivity", "selectVoice() / isSelcted = " + isSelected);
        if (!(!isSelected || this.mIsNewCelebOn || this.mIsNewBixbyOn)) {
            this.mIsNewCelebOn = true;
            setCelebVoicePathText();
        }
        this.mVoiceBtn.setChecked(isSelected);
        findViewById(C0490R.id.selected_voice_box).setEnabled(isSelected);
    }

    private void setEnableLayout(boolean isEnable) {
        boolean z;
        boolean z2 = true;
        Log.secD("AlarmSoundMainActivity", "setEnableLayout() / isEnable = " + isEnable);
        float alpha = isEnable ? 1.0f : 0.4f;
        this.mAlarmToneBtn.setEnabled(isEnable);
        findViewById(C0490R.id.ringtone_radio_box).setEnabled(isEnable);
        if (isEnable && this.mIsAlarmTonedOn) {
            z = true;
        } else {
            z = false;
        }
        enableAlarmToneLayout(z);
        if (isEnable && this.mIsAlarmTonedOn) {
            z = true;
        } else {
            z = false;
        }
        enableTtsLayout(z);
        this.mBixbyBtn.setEnabled(isEnable);
        findViewById(C0490R.id.bixby_box).setEnabled(isEnable);
        findViewById(C0490R.id.bixby_box).setAlpha(alpha);
        findViewById(C0490R.id.bixby_radio_title).setEnabled(isEnable);
        this.mVoiceBtn.setEnabled(isEnable);
        findViewById(C0490R.id.voice_box).setEnabled(isEnable);
        findViewById(C0490R.id.voice_box).setAlpha(alpha);
        findViewById(C0490R.id.voice_radio_title).setEnabled(isEnable);
        if (!(isEnable && (this.mIsNewCelebOn || this.mIsNewBixbyOn))) {
            z2 = false;
        }
        enableVoiceLayout(z2);
    }

    private void enableAlarmToneLayout(boolean isEnable) {
        Log.secD("AlarmSoundMainActivity", "enableAlarmToneLayout() / isEnable = " + isEnable);
        this.mAlarmToneLayout.setEnabled(isEnable);
        findViewById(C0490R.id.alarm_tone_text).setEnabled(isEnable);
        findViewById(C0490R.id.alarm_tone_layout).setEnabled(isEnable);
    }

    private void enableVoiceLayout(boolean isEnable) {
        Log.secD("AlarmSoundMainActivity", "enableVoiceLayout() / isEnable = " + isEnable);
        findViewById(C0490R.id.selected_voice_box).setEnabled(isEnable);
    }

    private void enableTtsLayout(boolean isEnable) {
        Log.secD("AlarmSoundMainActivity", "enableTtsLayout() / isEnable = " + isEnable);
        this.mAlarmTtsLayout.setEnabled(isEnable);
        this.mAlarmTtsSwitch.setEnabled(isEnable);
    }

    private void setBixbyVisibility(int visibility) {
        Log.secD("AlarmSoundMainActivity", "setBixbyVisibility BixbyBriefing() / visibility = " + visibility);
        this.mBixbyBtn.setVisibility(visibility);
        findViewById(C0490R.id.bixby_box).setVisibility(visibility);
        findViewById(C0490R.id.bixby_subheader_line).setVisibility(visibility);
    }

    private void setVoiceVisibility(int visibility) {
        Log.secD("AlarmSoundMainActivity", "setVoiceVisibility() / visibility = " + visibility);
        findViewById(C0490R.id.voice_box).setVisibility(visibility);
        findViewById(C0490R.id.selected_voice_box).setVisibility(visibility);
        findViewById(C0490R.id.voice_line).setVisibility(visibility);
        findViewById(C0490R.id.voice_subheader_line).setVisibility(visibility);
    }

    protected void onResume() {
        Log.secD("AlarmSoundMainActivity", "onResume()");
        super.onResume();
        this.mIsTalkBackEnabled = StateUtils.isTalkBackEnabled(getApplicationContext());
        setAccessibilityEnable(this.mIsTalkBackEnabled);
        if (this.mIsTalkBackEnabled) {
            this.mAlarmVolumeBar.registerVolumeReceiver();
        }
        new Thread(new C05306()).start();
        setAlarmToneUri(this.mUri);
        ClockUtils.insertSaLog(getCurrentScreenId());
    }

    private void setAccessibilityEnable(boolean isEnable) {
        if (this.mAlarmTtsSwitch != null) {
            this.mAlarmTtsSwitch.setClickable(!isEnable);
        }
    }

    protected void onPause() {
        super.onPause();
        stopPlayer();
        this.mAlarmVolumeBar.unregisterVolumeReceiver();
        stopTextToSpeech();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        sendActivityResult();
        ClockUtils.insertSaLog(getCurrentScreenId(), "1052");
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isVolumeUp = false;
        int keyCode = event.getKeyCode();
        Log.secD("AlarmSoundMainActivity", "dispatchKeyEvent () keyCode = " + keyCode);
        if (keyCode == 168 && event.getScanCode() == 546) {
            keyCode = 24;
        } else if (keyCode == 169 && event.getScanCode() == 545) {
            keyCode = 25;
        }
        if (event.getAction() != 0) {
            switch (keyCode) {
                case 24:
                case 25:
                    return this.mIsTalkBackEnabled ? true : true;
                case 164:
                    return true;
                default:
                    break;
            }
        }
        switch (keyCode) {
            case 4:
                sendActivityResult();
                ClockUtils.insertSaLog(getCurrentScreenId(), "1052");
                return true;
            case 24:
            case 25:
                if (StateUtils.isTurnOffAllSoundMode(getApplicationContext())) {
                    return false;
                }
                if (this.mIsTalkBackEnabled || !this.mIsMasterSoundOn) {
                    return true;
                }
                if (keyCode == 24) {
                    isVolumeUp = true;
                }
                this.mAlarmVolumeBar.onVolumeKeyPressed(isVolumeUp);
                return true;
            case 164:
                if (!this.mIsMasterSoundOn) {
                    return true;
                }
                this.mAlarmVolumeBar.changeVolumeIcon();
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mToast != null) {
            this.mToast.cancel();
            this.mToast = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this);
        }
        if (this.mAlarmVolumeBar != null) {
            this.mAlarmVolumeBar.removeInstance();
            this.mAlarmVolumeBar = null;
        }
        if (this.mAlarmTtsUtil != null) {
            this.mAlarmTtsUtil.removeInstance();
            this.mAlarmTtsUtil = null;
        }
        if (this.mAlarmTtsSwitch != null) {
            this.mAlarmTtsSwitch.setOnCheckedChangeListener(null);
            this.mAlarmTtsSwitch = null;
        }
        if (this.mRecommendDialog != null) {
            this.mRecommendDialog.cancel();
            this.mRecommendDialog = null;
        }
        if (this.mRingtoneRecommendHandler != null) {
            this.mRingtoneRecommendHandler.removeCallbacksAndMessages(null);
        }
        this.mResultListener = null;
        this.mRecommender = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.secD("AlarmSoundMainActivity", "onActivityResult : " + requestCode);
        if (resultCode != -1 || data == null) {
            Log.secD("AlarmSoundMainActivity", "resultCode != RESULT_OK");
            return;
        }
        switch (requestCode) {
            case 10004:
                Uri pickedUri = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
                int alarmVolume = data.getIntExtra("ringtone_volume_value", 11);
                boolean bNeedHighlightPopUp = data.getBooleanExtra("ringtone_highlight_popup", false);
                Log.secD("AlarmSoundMainActivity", "pickedUri = " + pickedUri + " / alarmVolume = " + alarmVolume + "/ bNeedHighlightPopUp = " + bNeedHighlightPopUp);
                setAlarmToneUri(pickedUri);
                this.mAlarmVolume = alarmVolume;
                this.mAlarmVolumeBar.setAlarmVolume(this.mAlarmVolume);
                if (bNeedHighlightPopUp && this.mIsAlarmTonedOn) {
                    callRecommendRingtoneDialog();
                    return;
                }
                return;
            case 10013:
                this.mIsNewBixbyOn = data.getBooleanExtra("alarm_bixby_voice_active", false);
                this.mIsNewCelebOn = data.getBooleanExtra("alarm_bixby_celebrity_active", false);
                this.mCelebrityVoicePath = data.getStringExtra("alarm_bixby_celebrity_path");
                this.mAlarmVolume = data.getIntExtra("alarm_volume_value", 11);
                this.mAlarmVolumeBar.setAlarmVolume(this.mAlarmVolume);
                Log.secD("AlarmSoundMainActivity", "REQUEST_ALARM_CELEBRITY_VOICE() / mIsNewBixbyOn = " + this.mIsNewBixbyOn + " / mIsNewCelebOn = " + this.mIsNewCelebOn + "/ mCelebrityVoicePath =" + this.mCelebrityVoicePath);
                setCelebVoicePathText();
                return;
            default:
                return;
        }
    }

    public Intent getRingtonePickerIntent() {
        Log.secD("AlarmSoundMainActivity", "getRingtonePickerIntent() mSoundUri = " + this.mUri);
        Intent intent = new Intent();
        intent.setClassName(this.mContext, "com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity");
        Uri existingUri = Uri.parse(Uri.decode(this.mUri.toString()));
        Uri uri = RingtoneManager.getDefaultUri(4);
        Ringtone ringtone = RingtoneManager.getRingtone(this.mContext, existingUri);
        if (ringtone != null && ringtone.semIsUriValid()) {
            uri = existingUri;
        }
        intent.putExtra("ringtone_mode", 0);
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", uri);
        intent.putExtra("ringtone_volume_value", this.mAlarmVolume);
        intent.setFlags(393216);
        return intent;
    }

    private void initAlarmTone() {
        String defaultAlarmTone;
        if (this.mAlarmRingtoneManager.hasCustomThemeRingtone()) {
            defaultAlarmTone = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(this.mContext).toString());
        } else {
            defaultAlarmTone = this.mAlarmRingtoneManager.getAlarmTonePreference(this.mContext);
        }
        this.mUri = Uri.parse(Uri.decode(defaultAlarmTone));
        Log.secD("AlarmSoundMainActivity", "initAlarmTone() / mUri= " + this.mUri);
    }

    private void setAlarmToneUri(Uri uri) {
        if (this.mIsPermissionPopup) {
            Log.secD("AlarmSoundMainActivity", "setAlarmToneUri() / PermissionPopup =" + this.mIsPermissionPopup);
            return;
        }
        this.mUri = uri;
        if (this.mUri == null || !AlarmRingtoneManager.validRingtoneStr(this.mContext, this.mUri.toString())) {
            Log.secD("AlarmSoundMainActivity", "setAlarmToneUri() / invalid ringtone.-> init ringtone");
            if (this.mRecommendDialog != null && this.mRecommendDialog.isShowing()) {
                this.mRecommendDialog.dismiss();
            }
            initAlarmTone();
        }
        setAlarmToneText();
    }

    private void setAlarmToneText() {
        Log.secD("AlarmSoundMainActivity", "setAlarmToneText() / mUri = " + this.mUri);
        this.mAlarmToneText.setVisibility(0);
        this.mAlarmToneText.setText(this.mAlarmRingtoneManager.getRingtoneTitle(this.mUri.toString()));
    }

    private void sendActivityResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("alarm_master_sound_active", this.mIsMasterSoundOn);
        resultIntent.putExtra("alarm_tone_active", this.mIsAlarmTonedOn);
        resultIntent.putExtra("alarm_tts_active", this.mIsTtsOn);
        resultIntent.putExtra("android.intent.extra.ringtone.PICKED_URI", this.mUri);
        resultIntent.putExtra("alarm_volume_value", this.mAlarmVolume);
        if (this.mIsAlarmTonedOn) {
            this.mIsNewBixbyOn = false;
            this.mIsNewCelebOn = false;
        } else if (this.mSelectedMode == 2) {
            this.mIsNewBixbyOn = true;
        }
        resultIntent.putExtra("alarm_bixby_voice_active", this.mIsNewBixbyOn);
        resultIntent.putExtra("alarm_bixby_celebrity_active", this.mIsNewCelebOn);
        resultIntent.putExtra("alarm_bixby_celebrity_path", this.mCelebrityVoicePath);
        setResult(-1, resultIntent);
        Log.secD("AlarmSoundMainActivity", "sendActivityResult() / mIsMasterSoundOn = " + this.mIsMasterSoundOn);
        Log.secD("AlarmSoundMainActivity", "sendActivityResult() / mIsAlarmTonedOn = " + this.mIsAlarmTonedOn);
        Log.secD("AlarmSoundMainActivity", "sendActivityResult() / mIsNewBixbyOn = " + this.mIsNewBixbyOn);
        Log.secD("AlarmSoundMainActivity", "sendActivityResult() / mIsNewCelebOn = " + this.mIsNewCelebOn);
        Log.secD("AlarmSoundMainActivity", "sendActivityResult() / mCelebrityVoicePath = " + this.mCelebrityVoicePath);
        Log.secD("AlarmSoundMainActivity", "mUri = " + this.mUri);
        finish();
    }

    public void openTextToSpeech() {
        Log.secD("AlarmSoundMainActivity", "openTextToSpeech()");
        if (this.mAlarmTtsUtil == null) {
            this.mAlarmTtsUtil = new AlarmTtsUtil(this.mContext);
        }
    }

    public void stopTextToSpeech() {
        if (this.mAlarmTtsUtil != null) {
            this.mAlarmTtsUtil.stopTextToSpeech();
        }
    }

    private void showTtsDialog() {
        if (this.mAlarmTtsUtil != null) {
            String message = this.mAlarmTtsUtil.getTtsMessage();
            Builder myAlertDialog = new Builder(this.mContext, C0490R.style.MyCustomThemeForDialog);
            myAlertDialog.setTitle(this.mContext.getResources().getString(C0490R.string.alarm_tts_dialog_title, new Object[]{this.mContext.getResources().getString(C0490R.string.tts_language_default)}));
            myAlertDialog.setMessage(message);
            myAlertDialog.setPositiveButton(this.mContext.getResources().getString(C0490R.string.alarm_use_tts, new Object[]{this.mContext.getResources().getString(C0490R.string.tts_language_default)}), new C05317());
            myAlertDialog.setNegativeButton(C0490R.string.setting, new C05328());
            myAlertDialog.setNeutralButton(C0490R.string.cancel, new C05339());
            myAlertDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    AlarmSoundMainActivity.this.mAlarmTtsSwitch.setChecked(false);
                }
            });
            myAlertDialog.show();
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

    private String getSongInfo(Uri uri) {
        Cursor c = null;
        String filePath = null;
        if (uri != null) {
            try {
                c = this.mContext.getContentResolver().query(uri, new String[]{"_data", "title"}, null, null, null);
            } catch (RuntimeException e) {
                Log.secE("AlarmSoundMainActivity", "RuntimeException : " + e.toString());
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        if (c != null && c.moveToFirst()) {
            filePath = c.getString(c.getColumnIndex("_data"));
        }
        if (c != null) {
            c.close();
        }
        return filePath;
    }

    public void callRecommendRingtoneDialog() {
        Log.secI("AlarmSoundMainActivity", "callRecommendRingtoneDialog() / mUri = " + this.mUri);
        Uri uri = Uri.parse(Uri.decode(this.mUri.toString()));
        if (Feature.isMusicAutoRecommendationSupported()) {
            String filePath = getSongInfo(uri);
            this.mRecommender = new RingtoneRecommender();
            Log.secI("AlarmSoundMainActivity", "filePath = " + filePath);
            if (filePath == null) {
                Log.secE("AlarmSoundMainActivity", "There is no file!! Set default URI");
                setAlarmToneUri(null);
            } else if (this.mRecommender.checkFile(filePath)) {
                this.mRecommender.doExtract(filePath, this.mResultListener);
            }
        }
    }

    private void showRecommendRingtoneDialog(Uri pickedUri, int offset) {
        Log.secD("AlarmSoundMainActivity", "showRecommendRingtoneDialog() pickedUri = " + pickedUri + ", offset : " + offset);
        HighlightDialogBuilder highlightDialogBuilder = new HighlightDialogBuilder(this.mContext, pickedUri, offset, 0, this.mAlarmVolume);
        highlightDialogBuilder.setOnHighlightButtonClickListener(new HighlightButtonClickListener() {
            public void onPositiveButtonClick(Uri uri) {
                Log.secD("AlarmSoundMainActivity", "onPositiveButtonClick uri : " + uri);
                AlarmSoundMainActivity.this.setAlarmToneUri(uri);
            }

            public void onDismiss(int volume) {
                Log.secD("AlarmSoundMainActivity", "save alarm volume : " + volume);
                AlarmSoundMainActivity.this.mIsDialogShown = false;
                AlarmSoundMainActivity.this.mAlarmVolume = volume;
            }
        });
        this.mIsDialogShown = true;
        this.mOffset = offset;
        this.mRecommendDialog = highlightDialogBuilder.create();
        this.mRecommendDialog.setVolumeControlStream(4);
        this.mRecommendDialog.show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.secD("AlarmSoundMainActivity", "onRequestPermissionsResult()");
        switch (requestCode) {
            case 1:
                boolean bStayHere;
                if (findViewById(C0490R.id.alarm_sound_body).getVisibility() == 4) {
                    bStayHere = true;
                } else {
                    bStayHere = false;
                }
                Log.secD("AlarmSoundMainActivity", "Received response for storage permissions request. / bStayHere = " + bStayHere);
                if (bStayHere) {
                    findViewById(C0490R.id.alarm_sound_body).setVisibility(0);
                } else if (PermissionUtils.verifyPermissions(grantResults)) {
                    startActivityForResult(getRingtonePickerIntent(), 10004);
                } else if (!shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {
                    PermissionUtils.showPermissionPopup(this, getResources().getString(C0490R.string.selected_ringtone), C0490R.string.permission_popup_body_open, "android.permission.READ_EXTERNAL_STORAGE");
                }
                this.mIsPermissionPopup = false;
                return;
            default:
                throw new IllegalArgumentException("Invalid permission.");
        }
    }
}
