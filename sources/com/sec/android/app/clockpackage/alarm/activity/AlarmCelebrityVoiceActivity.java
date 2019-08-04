package com.sec.android.app.clockpackage.alarm.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.view.AlarmCelebrityListView;
import com.sec.android.app.clockpackage.alarm.view.AlarmCelebrityListView.CelebrityListViewListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoice;
import com.sec.android.app.clockpackage.alarm.viewmodel.IAlarmCelebVoiceCallback.Stub;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.callback.VolumeProgressListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.VolumeBar;
import com.sec.android.app.clockpackage.common.view.VolumeBar.VolumeIconPressListener;
import com.sec.android.app.clockpackage.ringtonepicker.C0680R;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;

public class AlarmCelebrityVoiceActivity extends ClockActivity implements Runnable {
    private final String TAG = "AlarmCelebrityVoiceActivity";
    private Stub mAlarmCelebVoiceCallBack = new C05025();
    private int mAlarmVolume;
    private VolumeBar mAlarmVolumeBar;
    private BottomNavigationView mBottomNavigationView;
    private IAlarmCelebVoice mCelebVoice;
    private String mCelebrityVoicePath = "";
    private int mCheckedCursorPos = -1;
    private ServiceConnection mConnection = new C05014();
    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.secD("AlarmCelebrityVoiceActivity", "update celeb voice list : " + uri);
            AlarmCelebrityVoiceActivity.this.mListView.changeCursor();
            AlarmCelebrityVoiceActivity.this.invalidateOptionsMenu();
            String[] updatedUri = uri.toString().split("#");
            if (updatedUri.length == 6) {
                AlarmCelebrityVoiceActivity.this.setBixbyVoiceOn(AlarmCelebrityVoiceActivity.this.mListView.getPositionFromCelebrityPath(updatedUri[3]));
                AlarmCelebrityVoiceActivity.this.mListView.updateActionMode();
                return;
            }
            int position = 0;
            while (position < AlarmCelebrityVoiceActivity.this.mListView.getItemCount()) {
                if (AlarmUtil.isExpiredCelebVoice(AlarmCelebrityVoiceActivity.this.mContext, AlarmCelebrityVoiceActivity.this.mListView.getCelebrityPath(position))) {
                    position++;
                } else {
                    AlarmCelebrityVoiceActivity.this.setBixbyVoiceOn(position);
                    return;
                }
            }
            AlarmCelebrityVoiceActivity.this.setBixbyVoiceOn(-3);
        }
    };
    private Context mContext;
    private final Handler mHandler = new Handler();
    private boolean mIsBixbyVoiceOn = false;
    private boolean mIsBound;
    private boolean mIsCelebrityVoiceOn = true;
    private boolean mIsTalkBackEnabled = false;
    private AlarmCelebrityListView mListView;
    private MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();
    private Toast mToast;
    private int mToolbarContentInsetStart = 0;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity$1 */
    class C04981 implements OnOffsetChangedListener {
        C04981() {
        }

        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            float offsetAlpha = appBarLayout.getY() / ((float) appBarLayout.getTotalScrollRange());
            TextView textView = (TextView) appBarLayout.findViewById(C0490R.id.selection_title);
            if (textView != null) {
                textView.setAlpha(-offsetAlpha);
                ((CollapsingToolbarLayout) appBarLayout.findViewById(C0490R.id.collapsing_toolbar)).setTitle(textView.getText());
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity$3 */
    class C05003 implements OnNavigationItemSelectedListener {
        C05003() {
        }

        public boolean onNavigationItemSelected(MenuItem menuItem) {
            ClockUtils.insertSaLog("614", "6142");
            AlarmCelebrityVoiceActivity.this.mListView.deleteItems();
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity$4 */
    class C05014 implements ServiceConnection {
        C05014() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlarmCelebrityVoiceActivity.this.mCelebVoice = IAlarmCelebVoice.Stub.asInterface(iBinder);
            AlarmCelebrityVoiceActivity.this.mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            AlarmCelebrityVoiceActivity.this.mCelebVoice = null;
            AlarmCelebrityVoiceActivity.this.mIsBound = false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity$5 */
    class C05025 extends Stub {
        C05025() {
        }

        public void procedureResult(String packageName, int process, int result) {
            Log.secD("AlarmCelebrityVoiceActivity", "procedureResult, packageName : " + packageName + ", process : " + process + ", result : " + result);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity$6 */
    class C05036 implements VolumeProgressListener {
        C05036() {
        }

        public void onProgressChanged(int progress) {
            AlarmCelebrityVoiceActivity.this.mAlarmVolume = progress;
            if (AlarmCelebrityVoiceActivity.this.isPlaying()) {
                RingtonePlayer.setStreamVolume(AlarmCelebrityVoiceActivity.this.mContext, progress, false);
            } else {
                AlarmCelebrityVoiceActivity.this.startPlayer();
            }
            ClockUtils.insertSaLog("613", "6134", Integer.toString(AlarmCelebrityVoiceActivity.this.mAlarmVolume));
        }

        public void onStartTrackingTouch() {
            Log.secD("AlarmCelebrityVoiceActivity", "onStartTrackingTouch start");
            AlarmCelebrityVoiceActivity.this.startPlayer();
        }

        public void onStopTrackingTouch() {
            Log.secD("AlarmCelebrityVoiceActivity", "onStopTrackingTouch");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmCelebrityVoiceActivity$7 */
    class C05047 implements VolumeIconPressListener {
        C05047() {
        }

        public void onStopPlay() {
            AlarmCelebrityVoiceActivity.this.stopPlayer();
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Object obj = -1;
            switch (action.hashCode()) {
                case -1513032534:
                    if (action.equals("android.intent.action.TIME_TICK")) {
                        obj = null;
                        break;
                    }
                    break;
                case 502473491:
                    if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 505380757:
                    if (action.equals("android.intent.action.TIME_SET")) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                case 1:
                case 2:
                    AlarmCelebrityVoiceActivity.this.mListView.updateExpiredDate();
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlarmCelebrityVoiceActivity", "onCreate");
        setVolumeControlStream(4);
        setContentView(C0490R.layout.alarm_celebrity_voice_layout);
        Intent bindIntent = new Intent();
        bindIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebVoiceService");
        bindService(bindIntent, this.mConnection, 1);
        getContentResolver().registerContentObserver(Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*"), true, this.mContentObserver);
        Intent intent = getIntent();
        this.mIsBixbyVoiceOn = intent.getBooleanExtra("alarm_bixby_voice_active", false);
        this.mIsCelebrityVoiceOn = intent.getBooleanExtra("alarm_bixby_celebrity_active", true);
        this.mCelebrityVoicePath = intent.getStringExtra("alarm_bixby_celebrity_path");
        this.mAlarmVolume = intent.getIntExtra("alarm_volume_value", 11);
        this.mContext = this;
        this.mListView = (AlarmCelebrityListView) findViewById(C0490R.id.celebrity_list);
        this.mListView.setContext(this.mContext);
        this.mListView.setEnabled(true);
        try {
            findViewById(C0490R.id.coordinator).semSetRoundedCorners(12);
            findViewById(C0490R.id.coordinator).semSetRoundedCornerColor(12, getColor(C0490R.color.window_background_color));
            this.mListView.semSetRoundedCorners(15);
            this.mListView.semSetRoundedCornerColor(15, this.mContext.getColor(C0680R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmCelebrityVoiceActivity", "NoSuchMethodError : " + e);
        }
        if (!(StateUtils.isContextInDexMode(this) || StateUtils.isCustomTheme(this))) {
            this.mListView.setBackground(getDrawable(C0490R.drawable.contents_area_background));
        }
        ((CollapsingToolbarLayout) findViewById(C0490R.id.collapsing_toolbar)).setCustomAccessibility(true);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(C0490R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new C04981());
        this.mListView.setOnCelebrityListViewListener(new CelebrityListViewListener() {
            public void startPlay() {
                AlarmCelebrityVoiceActivity.this.startPlayer();
            }

            public void stopPlay(boolean isExceptAbandon) {
                if (isExceptAbandon) {
                    AlarmCelebrityVoiceActivity.this.stopPlayerExceptAbandon();
                } else {
                    AlarmCelebrityVoiceActivity.this.stopPlayer();
                }
            }

            public void deleteContent(String type, String packageName) {
                if (AlarmCelebrityVoiceActivity.this.mCelebVoice != null) {
                    try {
                        AlarmCelebrityVoiceActivity.this.mCelebVoice.deleteContent(type, packageName, AlarmCelebrityVoiceActivity.this.mAlarmCelebVoiceCallBack);
                    } catch (RemoteException e) {
                        Log.secE("AlarmCelebrityVoiceActivity", "happen RemoteException : " + e);
                    }
                }
            }

            public void changeCelebrityPath(int position) {
                AlarmCelebrityVoiceActivity.this.setBixbyVoiceOn(position);
            }

            public void setBottomBarVisibility(boolean isVisible) {
                float toYValue = 1.0f;
                int visibility = isVisible ? 0 : 8;
                if (AlarmCelebrityVoiceActivity.this.mBottomNavigationView.getVisibility() != visibility) {
                    float fromYValue;
                    if (isVisible) {
                        fromYValue = 1.0f;
                    } else {
                        fromYValue = 0.0f;
                    }
                    if (isVisible) {
                        toYValue = 0.0f;
                    }
                    int duration = isVisible ? 400 : 300;
                    TranslateAnimation translate = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, fromYValue, 1, toYValue);
                    translate.setInterpolator(new SineInOut90());
                    translate.setDuration((long) duration);
                    AlarmCelebrityVoiceActivity.this.mBottomNavigationView.startAnimation(translate);
                    AlarmCelebrityVoiceActivity.this.mBottomNavigationView.setVisibility(visibility);
                }
            }

            public void addViewOnToolBar(View view) {
                Toolbar toolbar = (Toolbar) AlarmCelebrityVoiceActivity.this.findViewById(C0490R.id.toolbar);
                AlarmCelebrityVoiceActivity.this.mToolbarContentInsetStart = toolbar.getContentInsetStart();
                AlarmCelebrityVoiceActivity.this.setToolbarVisible(toolbar, false, 0);
                toolbar.addView(view);
            }

            public void removeViewOnToolBar(View view) {
                Toolbar toolbar = (Toolbar) AlarmCelebrityVoiceActivity.this.findViewById(C0490R.id.toolbar);
                toolbar.removeView(view);
                AlarmCelebrityVoiceActivity.this.setToolbarVisible(toolbar, true, AlarmCelebrityVoiceActivity.this.mToolbarContentInsetStart);
                ((CollapsingToolbarLayout) appBarLayout.findViewById(C0490R.id.collapsing_toolbar)).setTitle(AlarmCelebrityVoiceActivity.this.getTitle());
            }
        });
        addVolumeBarView();
        addBottomNavigationView();
        initVoiceData();
    }

    private void setToolbarVisible(Toolbar toolbar, boolean isVisible, int insetStart) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isVisible);
            actionBar.setDisplayShowHomeEnabled(isVisible);
        }
        int toolbarChildCount = toolbar.getChildCount();
        toolbar.setContentInsetsRelative(insetStart, 0);
        for (int i = 0; i < toolbarChildCount; i++) {
            toolbar.getChildAt(i).setVisibility(isVisible ? 0 : 8);
        }
    }

    private void initVoiceData() {
        int pos = this.mListView.getPositionFromCelebrityPath(this.mCelebrityVoicePath);
        if (this.mIsBixbyVoiceOn) {
            if (Feature.isSupportBixbyBriefingMenu(this.mContext)) {
                pos = -2;
            } else {
                pos = -3;
            }
        } else if (AlarmUtil.isNewCelebDefault(this.mCelebrityVoicePath)) {
            pos = -3;
        }
        setBixbyVoiceOn(pos);
    }

    private void setBixbyVoiceOn(int position) {
        resetVoiceData();
        if (position == -2) {
            this.mIsBixbyVoiceOn = true;
        } else if (position == -3) {
            this.mIsCelebrityVoiceOn = true;
            this.mCelebrityVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
        } else {
            this.mIsCelebrityVoiceOn = true;
            this.mCelebrityVoicePath = this.mListView.getCelebrityPath(position);
            this.mCheckedCursorPos = this.mListView.getPositionFromCelebrityPath(this.mCelebrityVoicePath);
            this.mCelebrityVoicePath = this.mListView.getCelebrityPath(this.mCheckedCursorPos);
            this.mListView.setCheckItem(this.mCheckedCursorPos);
        }
        this.mListView.checkDefaultPreview(position);
    }

    private void resetVoiceData() {
        this.mIsBixbyVoiceOn = false;
        this.mIsCelebrityVoiceOn = false;
        this.mCelebrityVoicePath = "";
        this.mCheckedCursorPos = -1;
    }

    public void onSupportActionModeStarted(ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        this.mAlarmVolumeBar.setVisibility(8);
        invalidateOptionsMenu();
    }

    public void onSupportActionModeFinished(ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        this.mBottomNavigationView.setVisibility(8);
        this.mAlarmVolumeBar.setVisibility(0);
        invalidateOptionsMenu();
    }

    private void addBottomNavigationView() {
        this.mBottomNavigationView = (BottomNavigationView) findViewById(C0490R.id.bottom_navigation);
        this.mBottomNavigationView.setOnNavigationItemSelectedListener(new C05003());
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("alarm_bixby_voice_active", this.mIsBixbyVoiceOn);
        outState.putBoolean("alarm_bixby_celebrity_active", this.mIsCelebrityVoiceOn);
        outState.putString("alarm_bixby_celebrity_path", this.mCelebrityVoicePath);
        outState.putInt("alarm_volume_value", this.mAlarmVolume);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mIsBixbyVoiceOn = savedInstanceState.getBoolean("alarm_bixby_voice_active");
        this.mIsCelebrityVoiceOn = savedInstanceState.getBoolean("alarm_bixby_celebrity_active");
        this.mCelebrityVoicePath = savedInstanceState.getString("alarm_bixby_celebrity_path");
        this.mAlarmVolume = savedInstanceState.getInt("alarm_volume_value");
        initVoiceData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0490R.menu.alarm_list_delete_mode, menu);
        menu.findItem(C0490R.id.menu_alarm_add).setShowAsAction(2);
        menu.findItem(C0490R.id.mitem_delete_action_mode).setShowAsAction(2);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean hasItem;
        boolean isNormalMode;
        boolean z;
        boolean z2 = true;
        if (this.mListView == null || this.mListView.getItemCount() == 0) {
            hasItem = false;
        } else {
            hasItem = true;
        }
        if (this.mListView == null || this.mListView.isActionMode()) {
            isNormalMode = false;
        } else {
            isNormalMode = true;
        }
        MenuItem deleteItem = menu.findItem(C0490R.id.mitem_delete_action_mode);
        if (hasItem && isNormalMode) {
            z = true;
        } else {
            z = false;
        }
        deleteItem.setVisible(z);
        if (hasItem && isNormalMode) {
            z = true;
        } else {
            z = false;
        }
        deleteItem.setEnabled(z);
        boolean isInUltraSavingMode = StateUtils.isUltraPowerSavingMode(this.mContext);
        MenuItem addItem = menu.findItem(C0490R.id.menu_alarm_add);
        if (!isNormalMode || isInUltraSavingMode) {
            z = false;
        } else {
            z = true;
        }
        addItem.setVisible(z);
        if (!isNormalMode || isInUltraSavingMode) {
            z2 = false;
        }
        addItem.setEnabled(z2);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 16908332) {
            sendActivityResult();
            ClockUtils.insertSaLog("613", "6130");
        } else if (id == C0490R.id.mitem_delete_action_mode) {
            ClockUtils.insertSaLog("613", "6131");
            stopPlayer();
            this.mListView.startActionMode(true);
        } else if (id != C0490R.id.menu_alarm_add) {
            return false;
        } else {
            AlarmUtil.startGalaxyAppsForCelebrityVoice(this.mContext);
            ClockUtils.insertSaLog("613", "6133");
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        Log.secD("AlarmCelebrityVoiceActivity", "onResume");
        this.mIsTalkBackEnabled = StateUtils.isTalkBackEnabled(getApplicationContext());
        if (this.mIsTalkBackEnabled) {
            this.mAlarmVolumeBar.registerVolumeReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        registerReceiver(this.mReceiver, filter);
        this.mListView.updateBixbyMenu();
        ClockUtils.insertSaLog("613");
    }

    protected void onPause() {
        super.onPause();
        stopPlayer();
        this.mAlarmVolumeBar.unregisterVolumeReceiver();
    }

    private void addVolumeBarView() {
        Log.secD("AlarmCelebrityVoiceActivity", "addVolumeBarView");
        this.mAlarmVolumeBar = (VolumeBar) findViewById(C0490R.id.alarm_volume_bar);
        this.mAlarmVolumeBar.init(this.mAlarmVolume);
        this.mAlarmVolumeBar.setOnVolumeBarListener(new C05036());
        this.mAlarmVolumeBar.setVolumeIconPressListener(new C05047());
    }

    public void run() {
        AlarmUtil.playCelebPreview(this.mContext, this.mCelebrityVoicePath, this.mAlarmVolume, false);
    }

    private void startPlayer() {
        Log.secD("AlarmCelebrityVoiceActivity", "startPlayer()");
        if (semIsResumed() && !isPlaying() && !StateUtils.isInCallState(this.mContext) && RingtonePlayer.requestAudioFocus(this.mContext)) {
            this.mHandler.removeCallbacks(this);
            this.mHandler.postDelayed(this, RingtonePlayer.isActiveStreamAlarm() ? 200 : 0);
        }
    }

    private boolean isPlaying() {
        return RingtonePlayer.getMediaPlayer().isPlaying();
    }

    private void stopPlayer() {
        Log.secD("AlarmCelebrityVoiceActivity", "stopPlayer");
        this.mHandler.removeCallbacks(this);
        RingtonePlayer.stopMediaPlayer();
    }

    private void stopPlayerExceptAbandon() {
        Log.secD("AlarmCelebrityVoiceActivity", "stopPlayerExceptAbandon");
        this.mHandler.removeCallbacks(this);
        RingtonePlayer.stopMediaPlayerExceptAbandon();
    }

    private void sendActivityResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("alarm_bixby_voice_active", this.mIsBixbyVoiceOn);
        resultIntent.putExtra("alarm_bixby_celebrity_active", this.mIsCelebrityVoiceOn);
        resultIntent.putExtra("alarm_bixby_celebrity_path", this.mCelebrityVoicePath);
        resultIntent.putExtra("alarm_volume_value", this.mAlarmVolume);
        setResult(-1, resultIntent);
        finish();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isVolumeUp = false;
        int keyCode = event.getKeyCode();
        Log.secD("AlarmCelebrityVoiceActivity", "dispatchKeyEvent () keyCode = " + keyCode);
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
                if (this.mListView.isActionMode()) {
                    this.mListView.finishActionMode();
                    invalidateOptionsMenu();
                    return true;
                }
                sendActivityResult();
                ClockUtils.insertSaLog("613", "6130");
                return true;
            case 24:
            case 25:
                if (StateUtils.isTurnOffAllSoundMode(getApplicationContext())) {
                    return false;
                }
                if (this.mIsTalkBackEnabled) {
                    return true;
                }
                if (keyCode == 24) {
                    isVolumeUp = true;
                }
                this.mAlarmVolumeBar.onVolumeKeyPressed(isVolumeUp);
                return true;
            case 164:
                this.mAlarmVolumeBar.changeVolumeIcon();
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mIsBound) {
            unbindService(this.mConnection);
        }
        if (this.mListView != null) {
            this.mListView.removeInstance();
        }
        if (this.mContentObserver != null) {
            getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
        if (this.mToast != null) {
            this.mToast.cancel();
            this.mToast = null;
        }
        if (this.mReceiver != null) {
            try {
                unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secW("AlarmCelebrityVoiceActivity", "catch ignore / " + e);
            }
            this.mReceiver = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this);
        }
        if (this.mAlarmVolumeBar != null) {
            this.mAlarmVolumeBar.removeInstance();
            this.mAlarmVolumeBar = null;
        }
        ClockUtils.insertSaStatusLog(getApplicationContext(), "6504", String.valueOf(this.mListView.getItemCount()));
    }
}
