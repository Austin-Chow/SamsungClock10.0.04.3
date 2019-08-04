package com.sec.android.app.clockpackage.ringtonepicker.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.callback.VolumeProgressListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.VolumeBar;
import com.sec.android.app.clockpackage.common.view.VolumeBar.VolumeIconPressListener;
import com.sec.android.app.clockpackage.ringtonepicker.C0680R;
import com.sec.android.app.clockpackage.ringtonepicker.callback.RingtoneListViewListener;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer.AlarmRingtone;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer.TimerRingtone;
import com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtoneListView.RingtoneListClickListener;
import com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtoneVibrationBar.VibrationSwitchListener;

public class RingtonePickerActivity extends ClockActivity {
    private Context mContext = null;
    private boolean mHasAddItem = false;
    private boolean mIsTalkBackEnabled = false;
    private boolean mIsVibOn = false;
    int mMode = 0;
    private RingtoneListView mRingtoneList;
    private RingtoneListViewListener mRingtoneListViewListener = new C07035();
    private int mRingtoneVolume;
    CommonStrategy mStrategy = null;
    private VolumeBar mVolumeBar;

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity$1 */
    class C06991 implements RingtoneListClickListener {
        C06991() {
        }

        public void onClickSilentItem(boolean isSelected) {
            if (RingtonePickerActivity.this.mVolumeBar != null) {
                RingtonePickerActivity.this.mVolumeBar.setEnableVolumeOption(!isSelected);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity$2 */
    class C07002 implements VolumeProgressListener {
        C07002() {
        }

        public void onProgressChanged(int progress) {
            RingtonePickerActivity.this.getStrategy().insertSaLogVolumeBarChanged(progress);
            RingtonePickerActivity.this.mRingtoneVolume = progress;
            RingtonePickerActivity.this.mRingtoneList.setRingtoneVolume(RingtonePickerActivity.this.mRingtoneVolume);
            if ((RingtonePickerActivity.this.mIsTalkBackEnabled || StateUtils.isContextInDexMode(RingtonePickerActivity.this.mContext)) && progress == 0) {
                RingtonePickerActivity.this.mRingtoneList.stopAnyPlayingRingtone();
            } else if (RingtonePickerActivity.this.mRingtoneList.isRingtonePlaying()) {
                RingtonePickerActivity.this.mRingtoneList.setStreamVolume(progress);
            } else {
                RingtonePickerActivity.this.mRingtoneList.playRingtone(RingtonePickerActivity.this.mRingtoneList.mClickedPos);
            }
        }

        public void onStartTrackingTouch() {
            Log.secD("RingtonePickerActivity", "onStartTrackingTouch start");
            RingtonePickerActivity.this.mRingtoneList.playRingtone(RingtonePickerActivity.this.mRingtoneList.mClickedPos);
        }

        public void onStopTrackingTouch() {
            Log.secD("RingtonePickerActivity", "onStopTrackingTouch");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity$3 */
    class C07013 implements VolumeIconPressListener {
        C07013() {
        }

        public void onStopPlay() {
            RingtonePickerActivity.this.mRingtoneList.stopAnyPlayingRingtone();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity$4 */
    class C07024 implements VibrationSwitchListener {
        C07024() {
        }

        public void onChanged(boolean isChecked) {
            Log.secD("RingtonePickerActivity", "onChanged");
            RingtonePickerActivity.this.mIsVibOn = isChecked;
            if (RingtonePickerActivity.this.semIsResumed()) {
                RingtonePickerActivity.this.getStrategy().insertSaLogVibration();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtonePickerActivity$5 */
    class C07035 implements RingtoneListViewListener {
        C07035() {
        }

        public Uri onGetDefaultRingtoneUri() {
            return RingtonePickerActivity.this.getStrategy().getDefaultRingtoneUri(RingtonePickerActivity.this.getApplicationContext());
        }
    }

    interface CommonStrategy {
        void addViews();

        Uri getDefaultRingtoneUri(Context context);

        void insertSaLogActivity();

        void insertSaLogActivityAdd();

        void insertSaLogNavigateUp();

        void insertSaLogVibration();

        void insertSaLogVolumeBarChanged(int i);

        void muteKeyEvent();
    }

    class AlarmStrategy implements CommonStrategy {
        AlarmStrategy() {
        }

        public void addViews() {
            RingtonePickerActivity.this.addVolumeBarView();
        }

        public Uri getDefaultRingtoneUri(Context context) {
            return AlarmRingtone.getDefaultRingtoneUri(RingtonePickerActivity.this.getApplicationContext());
        }

        public void muteKeyEvent() {
            RingtonePickerActivity.this.mRingtoneList.stopAnyPlayingRingtone();
        }

        public void insertSaLogActivity() {
            ClockUtils.insertSaLog("104");
        }

        public void insertSaLogNavigateUp() {
            String ringtoneName = RingtonePickerActivity.this.mRingtoneList.getRingtoneTitle(RingtonePickerActivity.this.mRingtoneList.mClickedPos);
            if (ClockUtils.isEnableString(ringtoneName)) {
                ClockUtils.insertSaLog("104", "1241", ringtoneName);
            }
        }

        public void insertSaLogVibration() {
        }

        public void insertSaLogVolumeBarChanged(int progress) {
            ClockUtils.insertSaLog("104", "1032", Integer.toString(progress));
        }

        public void insertSaLogActivityAdd() {
            ClockUtils.insertSaLog("104", "1031");
        }
    }

    class TimerStrategy implements CommonStrategy {
        TimerStrategy() {
        }

        public void addViews() {
            ((CollapsingToolbarLayout) RingtonePickerActivity.this.findViewById(C0680R.id.collapsing_toolbar)).setTitle(RingtonePickerActivity.this.getResources().getString(C0680R.string.timer_set_timer_sound));
            ((Toolbar) RingtonePickerActivity.this.findViewById(C0680R.id.toolbar)).setTitle(C0680R.string.timer_set_timer_sound);
            RingtonePickerActivity.this.addVolumeBarView();
            if (Feature.isSupportVibration(RingtonePickerActivity.this.getApplicationContext())) {
                RingtonePickerActivity.this.addVibrationLayoutView();
            }
            if (Feature.isTablet(RingtonePickerActivity.this.mContext) && RingtonePickerActivity.this.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                RingtonePickerActivity.this.setRingtoneListWidth();
            }
        }

        public Uri getDefaultRingtoneUri(Context context) {
            return TimerRingtone.getDefaultRingtoneUri(RingtonePickerActivity.this.getApplicationContext());
        }

        public void muteKeyEvent() {
            RingtonePickerActivity.this.mRingtoneList.performItemClickSilent();
            RingtonePickerActivity.this.mRingtoneList.stopAnyPlayingRingtone();
        }

        public void insertSaLogActivity() {
            ClockUtils.insertSaLog("131");
        }

        public void insertSaLogNavigateUp() {
            String ringtoneName = RingtonePickerActivity.this.mRingtoneList.getRingtoneTitle(RingtonePickerActivity.this.mRingtoneList.mClickedPos);
            if (ClockUtils.isEnableString(ringtoneName)) {
                ClockUtils.insertSaLog("131", "1241", ringtoneName);
            }
        }

        public void insertSaLogVibration() {
            ClockUtils.insertSaLog("131", "1349", RingtonePickerActivity.this.mIsVibOn ? "1" : "0");
        }

        public void insertSaLogVolumeBarChanged(int progress) {
            ClockUtils.insertSaLog("131", "1348", Integer.toString(progress));
        }

        public void insertSaLogActivityAdd() {
            ClockUtils.insertSaLog("131", "1341");
        }
    }

    public CommonStrategy getStrategy() {
        if (this.mStrategy == null) {
            if (this.mMode == 0) {
                this.mStrategy = new AlarmStrategy();
            } else {
                this.mStrategy = new TimerStrategy();
            }
        }
        return this.mStrategy;
    }

    protected void onCreate(Bundle savedInstanceState) {
        Uri existingUri;
        super.onCreate(savedInstanceState);
        Log.secV("RingtonePickerActivity", "onCreate()");
        this.mContext = this;
        setVolumeControlStream(4);
        setContentView(C0680R.layout.ringtone_picker_main);
        this.mRingtoneList = (RingtoneListView) findViewById(C0680R.id.ringtone_list);
        this.mRingtoneList.setContext(this);
        this.mRingtoneList.setListener(this.mRingtoneListViewListener);
        Intent intent = getIntent();
        this.mIsTalkBackEnabled = StateUtils.isTalkBackEnabled(getApplicationContext());
        Log.secD("RingtonePickerActivity", "init() / intent=" + intent);
        if (savedInstanceState != null) {
            this.mMode = savedInstanceState.getInt("ringtone_mode");
            existingUri = (Uri) savedInstanceState.getParcelable("android.intent.extra.ringtone.EXISTING_URI");
            this.mRingtoneVolume = savedInstanceState.getInt("ringtone_volume_value");
            this.mRingtoneList.mIsSetHighlight = savedInstanceState.getBoolean("ringtone_highlight_set");
            this.mIsVibOn = savedInstanceState.getBoolean("ringtone_vibration_on");
        } else {
            this.mMode = intent.getIntExtra("ringtone_mode", 0);
            existingUri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
            this.mRingtoneVolume = intent.getIntExtra("ringtone_volume_value", 11);
            this.mIsVibOn = intent.getBooleanExtra("ringtone_vibration_on", false);
        }
        if (PermissionUtils.hasPermissionExternalStorage(getApplicationContext()) && !StateUtils.isUltraPowerSavingMode(getApplicationContext())) {
            this.mHasAddItem = true;
        }
        getStrategy().addViews();
        this.mRingtoneList.setOnRingtoneListClickListener(new C06991());
        this.mRingtoneList.setRingtoneListOption(this.mMode, existingUri);
        this.mRingtoneList.setRingtoneVolume(this.mRingtoneVolume);
        this.mRingtoneList.setTalkBackEnable(this.mIsTalkBackEnabled);
    }

    protected void addVolumeBarView() {
        Log.secD("RingtonePickerActivity", "addVolumeBarView");
        this.mVolumeBar = (VolumeBar) findViewById(C0680R.id.ringtone_volume_bar);
        this.mVolumeBar.init(this.mRingtoneVolume);
        this.mVolumeBar.setOnVolumeBarListener(new C07002());
        this.mVolumeBar.setVolumeIconPressListener(new C07013());
    }

    protected void addVibrationLayoutView() {
        RingtoneVibrationBar vibrationBar = (RingtoneVibrationBar) findViewById(C0680R.id.ringtone_vibration_bar);
        vibrationBar.setVibrationSwitchListener(new C07024());
        vibrationBar.init(this.mIsVibOn);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            sendOptionValue();
            getStrategy().insertSaLogNavigateUp();
        } else if (id != C0680R.id.add_menu) {
            return false;
        } else {
            Intent intent = this.mRingtoneList.getSoundPickerIntent();
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 10006);
            }
            getStrategy().insertSaLogActivityAdd();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle outState) {
        Log.secD("RingtonePickerActivity", "onSaveInstanceState() / mClickedPos=" + this.mRingtoneList.mClickedPos + "/mRingtoneVolume =" + this.mRingtoneVolume);
        outState.putParcelable("android.intent.extra.ringtone.EXISTING_URI", this.mRingtoneList.getSelectedRingtoneUri());
        outState.putInt("ringtone_volume_value", this.mRingtoneVolume);
        outState.putBoolean("ringtone_highlight_set", this.mRingtoneList.mIsSetHighlight);
        outState.putBoolean("ringtone_vibration_on", this.mIsVibOn);
        outState.putInt("ringtone_mode", this.mMode);
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.secD("RingtonePickerActivity", "onActivityResult : " + requestCode);
        switch (requestCode) {
            case 10006:
                Log.secD("RingtonePickerActivity", "REQUEST_SOUND_PICKER_RINGTONE");
                if (resultCode != -1) {
                    Log.secD("RingtonePickerActivity", "resultCode != RESULT_OK");
                    return;
                } else if (PermissionUtils.hasPermissionExternalStorage(getApplicationContext())) {
                    Uri existingUri = data.getData();
                    Log.secD("RingtonePickerActivity", "existingUri = " + existingUri);
                    if (existingUri != null) {
                        this.mRingtoneList.addRingToneFromSoundPicker(existingUri);
                        this.mVolumeBar.setEnableVolumeOption(true);
                        return;
                    }
                    return;
                } else {
                    Log.secD("RingtonePickerActivity", "it doesn't have permission");
                    return;
                }
            default:
                return;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Feature.isTablet(this.mContext) && getResources().getConfiguration().smallestScreenWidthDp >= 600 && this.mMode == 1) {
            setRingtoneListWidth();
        }
    }

    private void setRingtoneListWidth() {
        if (this.mRingtoneList != null) {
            LayoutParams lp = (LayoutParams) this.mRingtoneList.getLayoutParams();
            if (getResources().getConfiguration().orientation == 2) {
                lp.width = getResources().getDisplayMetrics().heightPixels;
            } else {
                lp.width = 0;
            }
            this.mRingtoneList.setLayoutParams(lp);
        }
    }

    private void sendOptionValue() {
        Intent resultIntent = new Intent();
        Uri existingUri = this.mRingtoneList.getSelectedRingtoneUri();
        Log.secD("RingtonePickerActivity", "sendOptionValue : " + existingUri);
        resultIntent.putExtra("android.intent.extra.ringtone.PICKED_URI", existingUri);
        resultIntent.putExtra("ringtone_volume_value", this.mRingtoneVolume);
        resultIntent.putExtra("ringtone_highlight_popup", !this.mRingtoneList.mIsSetHighlight);
        resultIntent.putExtra("ringtone_vibration_on", this.mIsVibOn);
        if (existingUri == null) {
            setResult(0);
        } else {
            setResult(-1, resultIntent);
        }
        finish();
    }

    protected void onResume() {
        super.onResume();
        getStrategy().insertSaLogActivity();
        boolean currentTalkBackEnabled = StateUtils.isTalkBackEnabled(getApplicationContext());
        if (this.mIsTalkBackEnabled != currentTalkBackEnabled) {
            this.mIsTalkBackEnabled = currentTalkBackEnabled;
            this.mRingtoneList.setTalkBackEnable(this.mIsTalkBackEnabled);
        }
        if (this.mIsTalkBackEnabled || StateUtils.isContextInDexMode(this.mContext)) {
            this.mVolumeBar.registerVolumeReceiver();
        }
        if (!(this.mHasAddItem || !PermissionUtils.hasPermissionExternalStorage(getApplicationContext()) || StateUtils.isUltraPowerSavingMode(getApplicationContext()))) {
            this.mHasAddItem = true;
            this.mRingtoneList.updateRingtoneList();
        }
        invalidateOptionsMenu();
        Log.secD("RingtonePickerActivity", "onResume : " + this.mIsTalkBackEnabled);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0680R.menu.ringtone_add_menu, menu);
        menu.findItem(C0680R.id.add_menu).setShowAsAction(1);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addMenu = menu.findItem(C0680R.id.add_menu);
        addMenu.setVisible(this.mHasAddItem);
        if (Feature.isTablet(this)) {
            addMenu.setContentDescription(getResources().getString(C0680R.string.add_from_tablet_tts));
        } else {
            addMenu.setContentDescription(getResources().getString(C0680R.string.add_from_phone_tts));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    protected void onDestroy() {
        Log.secV("RingtonePickerActivity", "onDestroy()");
        if (this.mRingtoneList != null) {
            this.mRingtoneList.stopAnyPlayingRingtone();
            this.mRingtoneList.removeInstance();
            this.mRingtoneList = null;
        }
        if (this.mVolumeBar != null) {
            this.mVolumeBar.removeInstance();
            this.mVolumeBar = null;
        }
        this.mStrategy = null;
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        Log.secD("RingtonePickerActivity", "onPause()");
        this.mVolumeBar.unregisterVolumeReceiver();
        this.mRingtoneList.stopAnyPlayingRingtone();
        this.mRingtoneList.abandonAudioFocus();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isVolumeUp = false;
        int keyCode = event.getKeyCode();
        Log.secD("RingtonePickerActivity", "dispatchKeyEvent () keyCode = " + keyCode);
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
            case 24:
            case 25:
                if (StateUtils.isTurnOffAllSoundMode(getApplicationContext()) || this.mVolumeBar == null) {
                    return false;
                }
                if (this.mIsTalkBackEnabled || !this.mVolumeBar.getSeekBar().isEnabled()) {
                    return true;
                }
                if (keyCode == 24) {
                    isVolumeUp = true;
                }
                this.mVolumeBar.onVolumeKeyPressed(isVolumeUp);
                return true;
            case 164:
                getStrategy().muteKeyEvent();
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void onBackPressed() {
        sendOptionValue();
        getStrategy().insertSaLogNavigateUp();
        super.onBackPressed();
    }
}
