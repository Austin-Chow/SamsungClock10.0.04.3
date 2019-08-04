package com.sec.android.app.clockpackage.alarm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowInsets;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.view.AlarmRepeatButton;
import com.sec.android.app.clockpackage.alarm.view.AlarmRepeatButton.AlarmRepeatListener;
import com.sec.android.app.clockpackage.alarm.view.AlarmTimePickerLayout;
import com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting;
import com.sec.android.app.clockpackage.alarm.view.AlarmTimeSetting.AlarmTimeEditListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListDetail.AlarmListClickListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmViewModelUtil;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.Calendar;

public class AlarmEditActivity extends ClockActivity {
    private static final boolean mIsSupportWorkdayAlarm = Feature.isSupportAlarmOptionMenuForWorkingDay();
    private final int MIN_TABLET_LAYOUT_DP = 600;
    private AlarmListDetail mAlarmListDetail;
    private AlarmRepeatButton mAlarmRepeatButtonGroup;
    private AlarmTimePickerLayout mAlarmTimePicker;
    private AlarmTimeSetting mAlarmTimeSetting;
    private boolean mDataChanged = false;
    private InternalReceiver mInternalReceiver = new InternalReceiver();
    private boolean mIsAlarmEditMode = false;
    private boolean mIsAlarmLaunchByWidget = false;
    private boolean mIsDexMode = false;
    private boolean mIsMultiWindow = false;
    private boolean mIsPermissionPopup = false;
    private boolean mIsShowSaveDialog = false;
    private boolean mIsSupportBixbyBriefingMenu = false;
    private boolean mIsSupportNewCelebFeature = false;
    private boolean mIsTabletLayout = false;
    private Configuration mLastConfiguration;
    private OnGlobalLayoutListener mOnGlobalLayoutListener;
    private AlarmItem mOriginalAlarm = new AlarmItem();
    private MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();
    private AlertDialog mSaveDialog;
    private boolean mSubmitting = false;
    private int mWidgetId;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$1 */
    class C05061 implements OnSystemUiVisibilityChangeListener {
        C05061() {
        }

        public void onSystemUiVisibilityChange(int visibility) {
            AlarmEditActivity.this.setDetailViewHeight();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$2 */
    class C05072 implements OnGlobalLayoutListener {
        C05072() {
        }

        public void onGlobalLayout() {
            AlarmEditActivity.this.setDetailViewHeight();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$3 */
    class C05083 implements OnTouchListener {
        C05083() {
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View v, MotionEvent event) {
            return AlarmEditActivity.this.blockScrollForRepeatDragVI((int) event.getRawY(), event);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$4 */
    class C05094 implements OnTouchListener {
        C05094() {
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View v, MotionEvent event) {
            return AlarmEditActivity.this.blockScrollForRepeatDragVI((int) event.getRawY(), event);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$6 */
    class C05116 implements OnClickListener {
        C05116() {
        }

        public void onClick(DialogInterface dialog, int arg1) {
            ClockUtils.insertSaLog("103", "1229", AlarmEditActivity.this.getDetailForSaLog(AlarmEditActivity.this.mAlarmRepeatButtonGroup.getCheckDay()), (long) AlarmEditActivity.this.mAlarmTimeSetting.getHourValue());
            if (!AlarmEditActivity.this.mSubmitting) {
                AlarmEditActivity.this.mSubmitting = true;
                int returnValue = AlarmEditActivity.this.saveDetailChange();
                if (returnValue == 1 || returnValue == 2) {
                    AlarmEditActivity.this.mDataChanged = true;
                    dialog.dismiss();
                    return;
                }
                AlarmEditActivity.this.mSubmitting = false;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$7 */
    class C05127 implements OnClickListener {
        C05127() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ClockUtils.insertSaLog("103", "1227");
            dialog.dismiss();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$8 */
    class C05138 implements OnClickListener {
        C05138() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ClockUtils.insertSaLog("103", "1228");
            dialog.dismiss();
            AlarmEditActivity.this.activityFinished();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity$9 */
    class C05149 implements OnDismissListener {
        C05149() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            AlarmEditActivity.this.mIsShowSaveDialog = false;
        }
    }

    private class InternalReceiver extends BroadcastReceiver {
        private InternalReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("AlarmEditActivity", "InternalReceiver class action = " + action);
            Object obj = -1;
            switch (action.hashCode()) {
                case -1337803714:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_CHANGE")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    AlarmEditActivity.this.mDataChanged = true;
                    return;
                default:
                    return;
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("AlarmEditActivity", "MyBroadcastReceiver class action = " + action);
            boolean z = true;
            switch (action.hashCode()) {
                case -1940635523:
                    if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                        z = true;
                        break;
                    }
                    break;
                case -1662713978:
                    if (action.equals("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET")) {
                        z = false;
                        break;
                    }
                    break;
                case -588796427:
                    if (action.equals("com.samsung.axt9info.close")) {
                        z = true;
                        break;
                    }
                    break;
                case 155606184:
                    if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM")) {
                        z = true;
                        break;
                    }
                    break;
                case 502473491:
                    if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                        z = true;
                        break;
                    }
                    break;
                case 505380757:
                    if (action.equals("android.intent.action.TIME_SET")) {
                        z = true;
                        break;
                    }
                    break;
                case 2070024785:
                    if (action.equals("android.media.RINGER_MODE_CHANGED")) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    AlarmEditActivity.this.mDataChanged = true;
                    return;
                case true:
                    AlarmEditActivity.this.stopAlarmRingtonePlayer();
                    return;
                case true:
                    if (AlarmEditActivity.this.semIsResumed() && AlarmEditActivity.this.mAlarmListDetail.isPlaying() && intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", 4) == 4) {
                        int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 11);
                        if (volume != AlarmEditActivity.this.mAlarmListDetail.getAlarmVolume()) {
                            AlarmEditActivity.this.mAlarmListDetail.setAlarmVolume(volume);
                            return;
                        }
                        return;
                    }
                    return;
                case true:
                    AlarmEditActivity.this.finish();
                    return;
                case true:
                    if (AlarmEditActivity.this.mAlarmTimeSetting != null) {
                        AlarmEditActivity.this.mAlarmTimeSetting.resetIs24HourView();
                        AlarmEditActivity.this.mAlarmTimeSetting.calculateNotidaysAndSetText();
                        return;
                    }
                    return;
                case true:
                    if (AlarmEditActivity.this.mAlarmTimeSetting != null) {
                        AlarmEditActivity.this.mAlarmTimeSetting.calculateNotidaysAndSetText();
                        return;
                    }
                    return;
                case true:
                    AlarmEditActivity.this.changeTimePickerEditMode(false);
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        boolean z;
        Log.secD("AlarmEditActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(C0490R.layout.alarm_edit);
        this.mLastConfiguration = new Configuration(getResources().getConfiguration());
        Intent intent = getIntent();
        String alarmEditStartType = intent.getType();
        if (alarmEditStartType != null) {
            z = "alarm_edit".equals(alarmEditStartType) || "alarm_edit_direct".equals(alarmEditStartType);
            this.mIsAlarmEditMode = z;
        }
        int alarmLaunchMode = intent.getIntExtra("AlarmLaunchMode", 0);
        this.mIsAlarmLaunchByWidget = alarmLaunchMode == 2;
        this.mWidgetId = intent.getIntExtra("widgetId", -1);
        Log.secD("AlarmEditActivity", "onCreate() - alarmEditStartType = " + alarmEditStartType + ", mIsAlarmLaunchByWidget = " + this.mIsAlarmLaunchByWidget);
        this.mIsMultiWindow = isInMultiWindowMode();
        this.mIsDexMode = StateUtils.isContextInDexMode(this);
        z = (this.mLastConfiguration.screenWidthDp >= 600 && this.mIsDexMode) || Feature.isTablet(getApplicationContext());
        this.mIsTabletLayout = z;
        this.mIsSupportBixbyBriefingMenu = Feature.isSupportBixbyBriefingMenu(this);
        this.mIsSupportNewCelebFeature = Feature.isSupportCelebrityAlarm();
        if (!(this.mIsTabletLayout || this.mIsDexMode) || this.mIsAlarmLaunchByWidget) {
            semConvertFromTranslucent(false);
        }
        if (this.mIsAlarmLaunchByWidget) {
            setRequestedOrientation(2);
        }
        if (this.mIsAlarmLaunchByWidget) {
            String str;
            String str2 = "103";
            String str3 = "3005";
            if (this.mIsAlarmEditMode) {
                str = "f";
            } else {
                str = "e";
            }
            ClockUtils.insertSaLog(str2, str3, str);
        } else if (this instanceof AlarmAddExecutable) {
            ClockUtils.insertSaLog("103", "3005", "g");
        } else if (alarmLaunchMode == 0) {
            ClockUtils.insertSaLog("103", "3005", !this.mIsAlarmEditMode ? "a" : "b");
        } else if (alarmLaunchMode == 3) {
            ClockUtils.insertSaLog("103", "3005", "h");
        }
        if (this.mIsAlarmEditMode) {
            AlarmItem item = AlarmProvider.getAlarm(this, intent.getIntExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", 0));
            if (item == null) {
                Log.secE("AlarmEditActivity", "alarmItem is null, check alarmId again.");
                return;
            }
            item.mSnoozeDoneCount = 0;
            this.mOriginalAlarm = (AlarmItem) item.clone();
            if ("alarm_preset_default_uri".equals(this.mOriginalAlarm.mSoundUri)) {
                Log.m41d("AlarmEditActivity", "preset alarm : alarm_preset_default_uri");
                this.mOriginalAlarm.mSoundUri = Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(this).toString());
                this.mOriginalAlarm.mAlarmSoundTone = new AlarmRingtoneManager(this).getRingtoneIndex(item.mSoundUri);
            }
        } else {
            this.mOriginalAlarm.mSoundUri = getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getString("alarm_tone", Uri.encode(AlarmRingtoneManager.getDefaultRingtoneUri(this).toString()));
        }
        String setAsUri = intent.getStringExtra("alarm_uri");
        if (setAsUri != null) {
            this.mOriginalAlarm.mAlarmSoundTone = -3;
            this.mOriginalAlarm.mSoundUri = Uri.encode(setAsUri);
        }
        if (!AlarmRingtoneManager.isExternalRingtone(this.mOriginalAlarm.mSoundUri) || PermissionUtils.hasPermissionExternalStorage(this)) {
            init();
        } else {
            this.mIsPermissionPopup = true;
            PermissionUtils.requestPermissions((Activity) this, 1);
        }
        cancelAndDoneActionbar();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new C05061());
        this.mOnGlobalLayoutListener = new C05072();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        Toolbar toolbar = (Toolbar) findViewById(C0490R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0490R.menu.alarm_edit_bottom_menu, menu);
        menu.findItem(C0490R.id.menu_cancel).setShowAsAction(6);
        menu.findItem(C0490R.id.menu_done).setShowAsAction(6);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isPortrait;
        if (getResources().getConfiguration().orientation == 1) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }
        boolean isPhoneSize;
        if (getResources().getConfiguration().smallestScreenWidthDp < 600) {
            isPhoneSize = true;
        } else {
            isPhoneSize = false;
        }
        ActionBar actionBar = getSupportActionBar();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(C0490R.id.cancel_and_done_layout);
        if (isPortrait || !isPhoneSize) {
            menu.setGroupVisible(C0490R.id.edit_app_bar_group, false);
            bottomNavigationView.setVisibility(0);
            if (actionBar != null) {
                actionBar.hide();
            }
        } else {
            menu.setGroupVisible(C0490R.id.edit_app_bar_group, true);
            bottomNavigationView.setVisibility(8);
            if (actionBar != null) {
                actionBar.show();
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onSaveCancelButtonClicked(item.getItemId());
        return true;
    }

    private void setEditLayoutWidth() {
        boolean isPortrait;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int screenPortraitWidth = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        int screenOrientationHeight = (int) (((float) displayMetrics.heightPixels) / displayMetrics.density);
        if (screenPortraitWidth > screenOrientationHeight) {
            screenPortraitWidth = screenOrientationHeight;
        }
        View appbar = findViewById(C0490R.id.alarm_time_picker);
        if (this.mLastConfiguration.screenWidthDp <= screenPortraitWidth) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }
        if (isPortrait || (this.mIsMultiWindow && !this.mIsDexMode)) {
            appbar.setPadding(0, 0, 0, 0);
            return;
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int paddingWidth = (int) ((((double) point.x) * 0.375d) / 2.0d);
        appbar.setPadding(paddingWidth, 0, paddingWidth, 0);
    }

    protected void onStop() {
        super.onStop();
        Log.secD("AlarmEditActivity", "onStop()");
        if (this.mAlarmTimeSetting != null) {
            this.mAlarmTimeSetting.hideDialog();
        }
        this.mSubmitting = false;
    }

    private void init() {
        this.mAlarmTimePicker = (AlarmTimePickerLayout) findViewById(C0490R.id.alarm_time_picker);
        this.mAlarmListDetail = (AlarmListDetail) findViewById(C0490R.id.alarm_detail_control);
        this.mAlarmTimeSetting = (AlarmTimeSetting) findViewById(C0490R.id.alarm_time_group);
        this.mAlarmRepeatButtonGroup = (AlarmRepeatButton) findViewById(C0490R.id.custom_alarm_repeat_btn);
        if (this.mAlarmTimeSetting == null || this.mAlarmListDetail == null || this.mAlarmRepeatButtonGroup == null || this.mAlarmTimePicker == null) {
            finish();
            return;
        }
        ScrollView detailScrollView = (ScrollView) findViewById(C0490R.id.alarm_detail_scrollview);
        detailScrollView.setOnTouchListener(new C05083());
        if ((!StateUtils.isContextInDexMode(this) || (StateUtils.isContextInDexMode(this) && (this instanceof AlarmAddExecutable))) && !StateUtils.isCustomTheme(this)) {
            detailScrollView.setBackground(getDrawable(C0490R.drawable.contents_area_background));
        }
        try {
            detailScrollView.semSetRoundedCorners(15);
            detailScrollView.semSetRoundedCornerColor(15, getColor(C0490R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmEditActivity", "NoSuchMethodError : " + e);
        }
        this.mAlarmListDetail.setContext(this);
        initAlarmListDetailListener();
        this.mAlarmTimePicker.createView(this);
        this.mAlarmTimeSetting.createView(this.mAlarmTimePicker.getTimePicker());
        initAlarmTimeSettingListener();
        ScrollView scrollView = (ScrollView) findViewById(C0490R.id.nestedscrollview);
        scrollView.setOnTouchListener(new C05094());
        try {
            scrollView.semSetRoundedCorners(12);
            scrollView.semSetRoundedCornerColor(12, getColor(C0490R.color.window_background_color));
        } catch (NoSuchMethodError e2) {
            Log.secE("AlarmEditActivity", "NoSuchMethodError : " + e2);
        }
        this.mAlarmRepeatButtonGroup.setContext(this);
        initAlarmRepeatButtonListener();
        if (this.mIsTabletLayout) {
            boolean z;
            if (this.mLastConfiguration.orientation == 1) {
                z = true;
            } else {
                z = false;
            }
            setScreenSize(z);
        } else {
            setEditLayoutWidth();
        }
        if (this.mIsAlarmEditMode) {
            fillEditData();
        } else {
            this.mAlarmListDetail.initData();
            if (this.mOriginalAlarm.mAlarmSoundTone == -3) {
                Log.secD("AlarmEditActivity", "init() -  set AlarmURI from intent");
                this.mAlarmListDetail.setRingtoneString(this.mOriginalAlarm.mSoundUri);
                this.mAlarmListDetail.setAlarmToneOn(true);
                this.mAlarmListDetail.setBixbyVoiceCelebValue(false, false, "");
            }
            this.mOriginalAlarm = (AlarmItem) this.mAlarmListDetail.getAlarmItem().clone();
            this.mAlarmTimeSetting.initData();
            this.mOriginalAlarm.mAlarmTime = this.mAlarmTimeSetting.getAlarmTime();
            this.mOriginalAlarm.mRepeatType = getAlarmRepeatValue(0, this.mOriginalAlarm.mAlarmTime, false);
        }
        this.mOriginalAlarm.initIncreasingVolume();
        this.mOriginalAlarm.initWeatherBg();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sec.android.widgetapp.alarmclock.NOTIFY_ALARM_CHANGE_WIDGET");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("com.samsung.axt9info.close");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM");
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(this.mReceiver, filter);
        IntentFilter internalFilter = new IntentFilter();
        internalFilter.addAction("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_CHANGE");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mInternalReceiver, internalFilter);
    }

    protected void onSaveInstanceState(Bundle state) {
        if (this.mAlarmTimeSetting != null && this.mAlarmListDetail != null && this.mAlarmRepeatButtonGroup != null) {
            super.onSaveInstanceState(state);
            Log.secD("AlarmEditActivity", "onSaveInstanceState()");
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.secD("AlarmEditActivity", "onRestoreInstanceState()");
        if (savedInstanceState != null && this.mAlarmListDetail != null && this.mAlarmTimeSetting != null) {
            AlarmItem newItem = getNewAlarmItem();
            if (mIsSupportWorkdayAlarm && newItem.isWorkingDay()) {
                setCheckDayForWorkingDay();
            } else {
                changeNotidaysByRepeatCheckDay(newItem.isWeeklyAlarm() ? newItem.getAlarmRepeat() : 0);
                this.mAlarmTimeSetting.calculateNotidaysAndSetText();
            }
            this.mAlarmListDetail.setHolidayEnable(newItem.isWeeklyAlarm());
        }
    }

    public void onResume() {
        boolean isShowButtonBackground = true;
        super.onResume();
        Log.secD("AlarmEditActivity", "onResume()");
        if (this.mAlarmTimeSetting != null && this.mAlarmListDetail != null) {
            if (System.getInt(getContentResolver(), "show_button_background", 0) != 1) {
                isShowButtonBackground = false;
            }
            this.mAlarmTimeSetting.setShowBtnBackground(Boolean.valueOf(isShowButtonBackground));
            this.mAlarmTimeSetting.showDialogAgain();
            this.mAlarmListDetail.resumeViewState();
            ClockUtils.insertSaLog("103");
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setDetailViewHeight();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        boolean z = true;
        super.onConfigurationChanged(newConfig);
        boolean z2 = (newConfig.screenWidthDp >= 600 && this.mIsDexMode) || Feature.isTablet(getApplicationContext());
        this.mIsTabletLayout = z2;
        this.mIsMultiWindow = isInMultiWindowMode();
        if (this.mLastConfiguration != null) {
            int diff = this.mLastConfiguration.diff(newConfig);
            Log.secD("AlarmEditActivity", "onConfigurationChanged() diff = " + diff);
            this.mLastConfiguration.setTo(newConfig);
            if (((this.mIsTabletLayout || this.mIsDexMode) && (diff & 2048) != 0) || (diff & 4096) != 0) {
                reloadViewForFreeformMode();
            }
            if (!((diff & 128) == 0 && (diff & 1024) == 0)) {
                if (this.mIsTabletLayout || this.mIsDexMode) {
                    if (newConfig.orientation != 1) {
                        z = false;
                    }
                    setScreenSize(z);
                } else {
                    setEditLayoutWidth();
                }
                invalidateOptionsMenu();
            }
            if (!((diff & 128) == 0 && (diff & 4096) == 0)) {
                moveScrollTop();
            }
        }
        setDetailViewHeight();
        if (StateUtils.isCustomTheme(this)) {
            findViewById(C0490R.id.cancel_and_done_layout).setBackground(getDrawable(C0490R.drawable.alarm_edit_app_bar_bg));
        }
    }

    private void setScreenSize(boolean isPortrait) {
        Log.secD("AlarmEditActivity", "setScreenSize() isPortrait : " + isPortrait);
        LayoutParams param = getWindow().getAttributes();
        if (this.mIsAlarmLaunchByWidget || isPortrait || ((this.mIsMultiWindow && !this.mIsDexMode) || (this instanceof AlarmAddExecutable))) {
            param.height = -1;
            param.width = -1;
            param.semClearExtensionFlags(32);
            getWindow().setBackgroundDrawableResource(C0490R.drawable.window_content_area_for_clocktheme);
        } else {
            Point point = new Point();
            if (this.mIsDexMode) {
                getWindowManager().getDefaultDisplay().getSize(point);
            } else {
                getWindowManager().getDefaultDisplay().getRealSize(point);
            }
            param.gravity = 49;
            param.width = point.y;
            param.height = point.y;
            TypedValue typedValue = new TypedValue();
            getResources().getValue(C0490R.dimen.alarm_edit_tablet_window_alpha, typedValue, true);
            param.dimAmount = typedValue.getFloat();
            getWindow().addFlags(2);
            getWindow().setBackgroundDrawableResource(C0490R.drawable.alarm_edit_activity_bg);
            param.semAddExtensionFlags(32);
        }
        getWindow().setAttributes(param);
    }

    private int getWindowHeight() {
        Point size = new Point();
        if (this.mIsMultiWindow || this.mIsDexMode) {
            getWindow().getWindowManager().getDefaultDisplay().getSize(size);
        } else {
            getWindow().getWindowManager().getDefaultDisplay().getRealSize(size);
        }
        int statusBarHeight = 0;
        int navigationBarHeight = 0;
        WindowInsets windowInsets = getWindow().getDecorView().getRootWindowInsets();
        if (windowInsets != null) {
            statusBarHeight = windowInsets.getStableInsetTop();
            navigationBarHeight = windowInsets.getStableInsetBottom();
        }
        return ((size.y - statusBarHeight) - navigationBarHeight) - getResources().getDimensionPixelSize(C0490R.dimen.alarm_edit_bottom_bar_height);
    }

    private int getDetailHeight(int windowHeight) {
        int listHeight = ((getViewHeight(this.mAlarmTimeSetting) + getViewHeight(this.mAlarmRepeatButtonGroup)) + getViewHeight(this.mAlarmListDetail)) + getResources().getDimensionPixelSize(C0490R.dimen.alarm_repeat_btn_layout_bottom_margin);
        int maxListHeight = (int) (((double) windowHeight) * 0.56d);
        return listHeight <= maxListHeight ? listHeight : maxListHeight;
    }

    private void setDetailViewHeight() {
        boolean isPortrait = true;Task 'assemble' not found in root project 'clockpackage_10.0.04.3'.
        if (this.mAlarmTimeSetting == null || this.mAlarmRepeatButtonGroup == null || this.mAlarmListDetail == null) {
            Log.secD("AlarmEditActivity", "fail setDetailViewHeight");
            return;
        }
        int windowHeight = getWindowHeight();
        int detailHeight = getDetailHeight(windowHeight);
        int pickerHeight = windowHeight - detailHeight;
        if (this.mLastConfiguration.orientation != 1) {
            isPortrait = false;
        }
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService("input_method");
        if (StateUtils.isMobileKeyboard(getApplicationContext()) || ((imm != null && imm.semIsInputMethodShown()) || this.mIsMultiWindow || !isPortrait)) {
            int pickerMinHeight = getResources().getDimensionPixelSize(C0490R.dimen.alarm_timepicker_layout_min_height);
            if (pickerHeight <= pickerMinHeight) {
                pickerHeight = pickerMinHeight;
            }
            detailHeight = -2;
        }
        ViewGroup.LayoutParams params = ((ScrollView) findViewById(C0490R.id.alarm_detail_scrollview)).getLayoutParams();
        if (this.mAlarmTimePicker.getLayoutParams().height != pickerHeight || params.height != detailHeight) {
            params.height = detailHeight;
            this.mAlarmTimePicker.updateHeight(pickerHeight);
            this.mAlarmTimePicker.requestLayout();
        }
    }

    private int getViewHeight(View view) {
        int height = view.getHeight();
        if (height != 0) {
            return height;
        }
        view.measure(0, 0);
        return view.getMeasuredHeight();
    }

    private void reloadViewForFreeformMode() {
        boolean isShowButtonBackground = true;
        if (this.mAlarmTimeSetting != null && this.mAlarmListDetail != null && this.mAlarmRepeatButtonGroup != null) {
            Log.secD("AlarmEditActivity", "reloadViewForFreeformMode()");
            AlarmItem newItem = getNewAlarmItem();
            this.mAlarmTimeSetting.saveLastFocusPositionInTimePicker();
            boolean flag = this.mAlarmListDetail.getErrorEnableState();
            this.mAlarmTimePicker.removeInstance();
            this.mAlarmTimeSetting.removeInstance(false);
            this.mAlarmListDetail.removeInstance(false);
            this.mAlarmRepeatButtonGroup.removeInstance(false);
            cancelAndDoneActionbar();
            if (System.getInt(getContentResolver(), "show_button_background", 0) != 1) {
                isShowButtonBackground = false;
            }
            findViewById(C0490R.id.cancel_and_done_layout).getLayoutParams().height = ClockUtils.getActionBarHeight(this);
            this.mAlarmRepeatButtonGroup.setContext(this);
            this.mAlarmTimePicker.createView(this);
            this.mAlarmTimeSetting.createView(this.mAlarmTimePicker.getTimePicker());
            this.mAlarmListDetail.setIsErrorEnabled(flag);
            this.mAlarmTimeSetting.reloadTimeSettingViewForFreeformMode(Boolean.valueOf(isShowButtonBackground));
            this.mAlarmListDetail.reloadAlarmListDetailViewForFreeformMode(this, newItem);
            if (mIsSupportWorkdayAlarm && newItem.isWorkingDay()) {
                setCheckDayForWorkingDay();
            } else {
                changeNotidaysByRepeatCheckDay(newItem.isWeeklyAlarm() ? newItem.getAlarmRepeat() : 0);
            }
            this.mAlarmTimeSetting.setLastFocusToTimePicker();
            if (this.mIsShowSaveDialog && this.mSaveDialog != null) {
                this.mSaveDialog.dismiss();
                this.mIsShowSaveDialog = false;
                showSaveDialog();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mIsTabletLayout || ((this.mIsMultiWindow && !this.mIsDexMode) || event.getAction() != 0)) {
            return false;
        }
        int width = getWindow().getDecorView().getWidth();
        int height = getWindow().getDecorView().getHeight();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if ((x <= 0 || y <= 0 || x >= width || y >= height) && y >= 0) {
            stopAlarmRingtonePlayer();
            if (isAlarmDataChanged()) {
                showSaveDialog();
            } else {
                activityFinished();
            }
        }
        return super.onTouchEvent(event);
    }

    public void onPause() {
        super.onPause();
        Log.m41d("AlarmEditActivity", "onPause()");
        if (this.mAlarmListDetail != null) {
            this.mAlarmListDetail.pauseViewState();
        }
    }

    public void onDestroy() {
        Log.m41d("AlarmEditActivity", "onDestroy()");
        intentUnRegisterReceiver();
        if (this.mAlarmTimeSetting != null) {
            this.mAlarmTimeSetting.removeInstance(true);
            this.mAlarmTimeSetting = null;
        }
        if (this.mAlarmListDetail != null) {
            this.mAlarmListDetail.removeInstance(true);
            this.mAlarmListDetail = null;
        }
        if (this.mAlarmRepeatButtonGroup != null) {
            this.mAlarmRepeatButtonGroup.removeInstance(true);
            this.mAlarmRepeatButtonGroup = null;
        }
        if (this.mOnGlobalLayoutListener != null) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
        removeInstance();
        super.onDestroy();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.secD("AlarmEditActivity", "onRequestPermissionsResult()");
        switch (requestCode) {
            case 1:
                Log.secD("AlarmEditActivity", "Received response for storage permissions request.");
                if (this.mAlarmTimeSetting == null && this.mAlarmListDetail == null) {
                    init();
                    setDetailViewHeight();
                    onResume();
                } else if (PermissionUtils.verifyPermissions(grantResults)) {
                    startActivityForResult(this.mAlarmListDetail.getSoundMainIntent(), 10003);
                } else if (!shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {
                    PermissionUtils.showPermissionPopup(this, getResources().getString(C0490R.string.alarm_sound), C0490R.string.permission_popup_body_open, "android.permission.READ_EXTERNAL_STORAGE");
                }
                this.mIsPermissionPopup = false;
                return;
            default:
                throw new IllegalArgumentException("Invalid permission.");
        }
    }

    private void intentUnRegisterReceiver() {
        Log.m41d("AlarmEditActivity", "intentUnRegisterReceiver()");
        if (this.mReceiver != null) {
            try {
                unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secW("AlarmEditActivity", "catch ignore / " + e);
            }
            this.mReceiver = null;
        }
        if (this.mInternalReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mInternalReceiver);
            this.mInternalReceiver = null;
        }
    }

    private void moveScrollTop() {
        final ScrollView nestedScrollView = (ScrollView) findViewById(C0490R.id.nestedscrollview);
        if (nestedScrollView != null) {
            nestedScrollView.post(new Runnable() {
                public void run() {
                    if (nestedScrollView != null) {
                        nestedScrollView.smoothScrollTo(0, 0);
                    }
                }
            });
        }
    }

    private void removeInstance() {
        Log.secD("AlarmEditActivity", "removeInstance()");
        if (this.mOriginalAlarm != null) {
            this.mOriginalAlarm = null;
        }
    }

    private void activityFinished() {
        Log.secD("AlarmEditActivity", "activityFinished()");
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService("input_method");
        if (imm != null) {
            if (!(this.mAlarmTimeSetting == null || this.mAlarmTimeSetting.getWindowToken() == null)) {
                imm.hideSoftInputFromWindow(this.mAlarmTimeSetting.getWindowToken(), 0);
            }
            if (!(this.mAlarmTimePicker == null || this.mAlarmTimePicker.getWindowToken() == null)) {
                imm.hideSoftInputFromWindow(this.mAlarmTimePicker.getWindowToken(), 0);
            }
        }
        intentUnRegisterReceiver();
        finish();
        if (this.mIsAlarmLaunchByWidget) {
            AlarmUtil.sendFinishLaunchActivityToAlarmWidget(getApplicationContext(), this.mWidgetId);
        }
    }

    public void onBackPressed() {
        Log.secD("AlarmEditActivity", "onBackPressed()");
        stopAlarmRingtonePlayer();
        if (isAlarmDataChanged()) {
            showSaveDialog();
        } else {
            activityFinished();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int mIsVolumeUp = 0;
        int keyCode = event.getKeyCode();
        Log.secD("AlarmEditActivity", "dispatchKeyEvent() keyCode = " + keyCode);
        if (keyCode == 168 && event.getScanCode() == 546) {
            keyCode = 24;
        } else if (keyCode == 169 && event.getScanCode() == 545) {
            keyCode = 25;
        }
        if (event.getAction() != 0) {
            switch (keyCode) {
                case 19:
                case 20:
                case 21:
                case 22:
                case 24:
                case 25:
                case 164:
                    if (getCurrentFocus() != null && getCurrentFocus().getId() == C0490R.id.alarm_name) {
                        break;
                    }
                    return true;
                default:
                    break;
            }
        }
        switch (keyCode) {
            case 19:
                if (Feature.isFolder(this) && this.mAlarmTimeSetting.hasFocus()) {
                    moveScrollTop();
                    break;
                }
            case 21:
            case 22:
                boolean isChecked;
                if (keyCode == 22) {
                    isChecked = true;
                } else {
                    isChecked = false;
                }
                int id = getCurrentFocus().getId();
                if (id == C0490R.id.alarm_holiday_kor_boz) {
                    this.mAlarmListDetail.setHolidayWorkingdayValue(isChecked);
                    return true;
                } else if (id == C0490R.id.alarm_snooze_boz) {
                    this.mAlarmListDetail.setSnoozeActiveValue(isChecked);
                    return true;
                } else if (id == C0490R.id.alarm_sound_box) {
                    this.mAlarmListDetail.setMasterSoundOn(isChecked);
                    return true;
                } else if (id == C0490R.id.alarm_pattern_boz) {
                    AlarmListDetail alarmListDetail = this.mAlarmListDetail;
                    if (isChecked) {
                        mIsVolumeUp = 2;
                    }
                    alarmListDetail.setAlarmType(mIsVolumeUp);
                    return true;
                }
                break;
            case 24:
            case 25:
                if (StateUtils.isTurnOffAllSoundMode(this)) {
                    return false;
                }
                if (this.mAlarmListDetail != null) {
                    boolean mIsVolumeUp2;
                    if (keyCode == 24) {
                        mIsVolumeUp2 = true;
                    }
                    this.mAlarmListDetail.showVolumePopup(mIsVolumeUp2);
                    return true;
                }
                break;
            case 82:
                return true;
            case 164:
                int volume;
                if (this.mAlarmListDetail.getAlarmVolume() > 0) {
                    volume = 0;
                } else {
                    volume = 11;
                }
                this.mAlarmListDetail.setAlarmVolume(volume);
                AudioManager audioManager = (AudioManager) getSystemService("audio");
                audioManager.setStreamVolume(4, volume, 1);
                Log.m41d("AlarmEditActivity", "dispatchKeyEvent KEYCODE_VOLUME_MUTE setStreamVolume STREAM_ALARM volume = " + volume);
                audioManager.adjustStreamVolume(4, 0, 1);
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void showSaveDialog() {
        if (!this.mIsShowSaveDialog) {
            Builder myAlertDialog = new Builder(this);
            this.mIsShowSaveDialog = true;
            myAlertDialog.setMessage(C0490R.string.save_confirm_dialog_body);
            myAlertDialog.setPositiveButton(C0490R.string.action_bar_save, new C05116());
            myAlertDialog.setNeutralButton(C0490R.string.cancel, new C05127());
            myAlertDialog.setNegativeButton(C0490R.string.discard, new C05138());
            myAlertDialog.setOnDismissListener(new C05149());
            this.mSaveDialog = myAlertDialog.show();
        }
    }

    private boolean isAlarmDataChanged() {
        if (this.mAlarmTimeSetting == null) {
            return false;
        }
        AlarmItem newItem = getNewAlarmItem();
        if (this.mOriginalAlarm.isSpecificDate() && this.mOriginalAlarm.mAlarmAlertTime != this.mAlarmTimeSetting.getNextAlertTime()) {
            Log.secD("AlarmEditActivity", "mAlarmTimeSetting.getNextAlertTime() = " + this.mAlarmTimeSetting.getNextAlertTime());
            return true;
        } else if (this.mOriginalAlarm.isSpecificDate() != this.mAlarmTimeSetting.isDateSelectedState()) {
            return true;
        } else {
            if (newItem == null || !this.mOriginalAlarm.equals(newItem)) {
                return true;
            }
            return false;
        }
    }

    public void optionClicked(View view) {
        Log.secD("AlarmEditActivity", "optionClicked : " + view);
        int id = view.getId();
        if (id == C0490R.id.alarm_snooze_boz) {
            startActivityForResult(this.mAlarmListDetail.getAlarmSnoozeIntent(), 10008);
        } else if (id == C0490R.id.alarm_holiday_kor_boz) {
            startActivityForResult(this.mAlarmListDetail.getSubstituteHolidayIntent(), 10010);
        } else if (id == C0490R.id.alarm_sound_box) {
            startActivityForResult(this.mAlarmListDetail.getSoundMainIntent(), 10003);
        } else if (id == C0490R.id.alarm_pattern_boz) {
            startActivityForResult(this.mAlarmListDetail.getVibrationIntent(), 10009);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.secD("AlarmEditActivity", "onActivityResult : " + requestCode);
        if (resultCode != -1 || data == null) {
            Log.secD("AlarmEditActivity", "result code is invalid");
            return;
        }
        switch (requestCode) {
            case 10003:
                Log.secD("AlarmEditActivity", "REQUEST_SOUND");
                boolean masterSoundOn = data.getBooleanExtra("alarm_master_sound_active", true);
                boolean alarmTonedOn = data.getBooleanExtra("alarm_tone_active", true);
                boolean ttsOn = data.getBooleanExtra("alarm_tts_active", true);
                int alarmVolume = data.getIntExtra("alarm_volume_value", 11);
                Uri pickedUri = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
                Log.secD("AlarmEditActivity", "REQUEST_SOUND / masterSoundOn = " + masterSoundOn + " / alarmToneOn = " + alarmTonedOn + "/ ttsOn = " + ttsOn);
                Log.secD("AlarmEditActivity", "REQUEST_SOUND / mIsSupportBixbyBriefingMenu = " + this.mIsSupportBixbyBriefingMenu + " / mIsSupportNewCelebFeature = " + this.mIsSupportNewCelebFeature);
                if (this.mAlarmListDetail != null) {
                    this.mAlarmListDetail.setMasterSoundOn(masterSoundOn);
                    this.mAlarmListDetail.setAlarmTts(ttsOn);
                    this.mAlarmListDetail.setAlarmToneOn(alarmTonedOn);
                    this.mAlarmListDetail.updateRingtoneList();
                    this.mAlarmListDetail.setRingtoneString(Uri.encode(pickedUri.toString()));
                    boolean isNewBixbyOn = data.getBooleanExtra("alarm_bixby_voice_active", false);
                    Log.secD("AlarmEditActivity", "REQUEST_SOUND / isNewBixbyOn = " + isNewBixbyOn);
                    if (this.mIsSupportNewCelebFeature) {
                        boolean isNewCelebOn = data.getBooleanExtra("alarm_bixby_celebrity_active", false);
                        String celebVoicePath = data.getStringExtra("alarm_bixby_celebrity_path");
                        this.mAlarmListDetail.setBixbyVoiceCelebValue(isNewBixbyOn, isNewCelebOn, celebVoicePath);
                        Log.secD("AlarmEditActivity", "REQUEST_SOUND / isNewBixbyOn = " + isNewBixbyOn + " / isNewCelebOn =" + isNewCelebOn + " / celebVoicePath = " + celebVoicePath);
                        Log.secD("AlarmEditActivity", "REQUEST_SOUND / celebVoicePath = " + celebVoicePath);
                    } else {
                        this.mAlarmListDetail.setNewBixbyOn(isNewBixbyOn);
                    }
                    this.mAlarmListDetail.setAlarmVolume(alarmVolume);
                    this.mAlarmListDetail.setSoundSubText();
                    return;
                }
                return;
            case 10008:
                boolean isSnoozeActive = data.getBooleanExtra("alarm_snooze_active", true);
                int snoozeTimes = data.getIntExtra("alarm_snooze_duration", 1);
                int snoozeRepeatTimes = data.getIntExtra("alarm_snooze_repeat", 2);
                Log.secD("AlarmEditActivity", "REQUEST_ALARM_SNOOZE : " + snoozeTimes + ", " + snoozeRepeatTimes);
                if (this.mAlarmListDetail != null) {
                    this.mAlarmListDetail.setSnoozeValues(isSnoozeActive, snoozeTimes, snoozeRepeatTimes);
                    return;
                }
                return;
            case 10009:
                int vibrationType = data.getIntExtra("vibration_pattern", 50035);
                int alarmType = data.getIntExtra("alarm_type", 2);
                Log.secD("AlarmEditActivity", "REQUEST_ALARM_VIBRATION : " + vibrationType);
                if (this.mAlarmListDetail != null) {
                    this.mAlarmListDetail.setAlarmType(alarmType);
                    this.mAlarmListDetail.setVibPatternValues(vibrationType);
                    return;
                }
                return;
            case 10010:
                boolean holidayActive = data.getBooleanExtra("alarm_holiday_active", false);
                boolean substituteHoliday = data.getBooleanExtra("alarm_substitute_holiday", false);
                Log.secD("AlarmEditActivity", "REQUEST_ALARM_HOLIDAY_KOR : " + holidayActive);
                if (this.mAlarmListDetail != null) {
                    this.mAlarmListDetail.setHolidayWorkingdayValue(holidayActive);
                    this.mAlarmListDetail.setSubstituteValue(substituteHoliday);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void initAlarmListDetailListener() {
        this.mAlarmListDetail.setOnAlarmListClickListener(new AlarmListClickListener() {
            public void onAlarmClickEvent() {
                AlarmEditActivity.this.changeTimePickerEditMode(false);
            }

            public void setWorkingDayAlarm(boolean isWorkingDayAlarmOn) {
                if (isWorkingDayAlarmOn) {
                    AlarmEditActivity.this.setCheckDayForWorkingDay();
                } else if (AlarmEditActivity.this.mAlarmTimeSetting.getRepeatDateWorkingState() == 3) {
                    AlarmEditActivity.this.mAlarmTimeSetting.setRepeatDateWorkingState(0);
                    AlarmEditActivity.this.mAlarmTimeSetting.calculateNotidaysAndSetText();
                }
            }
        });
    }

    private void initAlarmTimeSettingListener() {
        this.mAlarmTimeSetting.setAlarmEditInterface(new AlarmTimeEditListener() {
            public void initRepeat() {
                Log.secD("AlarmEditActivity", "initRepeat()");
                AlarmEditActivity.this.changeNotidaysByRepeatCheckDay(0);
            }

            public void setSpecialDateAlarm(boolean isEnable) {
                Log.secD("AlarmEditActivity", "setSpecialDateAlarm : " + isEnable);
                if (AlarmEditActivity.this.mAlarmListDetail != null) {
                    AlarmEditActivity.this.mAlarmListDetail.setHolidayEnable(!isEnable);
                }
                if (AlarmEditActivity.mIsSupportWorkdayAlarm && AlarmEditActivity.this.mAlarmListDetail != null) {
                    AlarmEditActivity.this.mAlarmListDetail.setHolidayWorkingdayValue(false);
                }
            }
        });
    }

    private void initAlarmRepeatButtonListener() {
        this.mAlarmRepeatButtonGroup.setOnAlarmRepeatClickListener(new AlarmRepeatListener() {
            public void setAlarmRepeatClick(int alarmRepeat) {
                AlarmEditActivity.this.changeNotidaysByRepeatCheckDay(alarmRepeat);
                AlarmEditActivity.this.onAlarmRepeatClicked(alarmRepeat);
            }

            public void setRepeatFocus() {
                AlarmEditActivity.this.changeTimePickerEditMode(false);
            }
        });
    }

    private void cancelAndDoneActionbar() {
        ((BottomNavigationView) findViewById(C0490R.id.cancel_and_done_layout)).setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                AlarmEditActivity.this.onSaveCancelButtonClicked(menuItem.getItemId());
                return true;
            }
        });
    }

    private void onSaveCancelButtonClicked(int id) {
        if (id == C0490R.id.menu_cancel) {
            ClockUtils.insertSaLog("103", "1221");
            cancelDetailChange();
        } else if (id == C0490R.id.menu_done) {
            ClockUtils.insertSaLog("103", "1222", getDetailForSaLog(this.mAlarmRepeatButtonGroup.getCheckDay()), (long) this.mAlarmTimeSetting.getHourValue());
            if (!this.mSubmitting) {
                this.mSubmitting = true;
                stopAlarmRingtonePlayer();
                if (this.mAlarmTimeSetting != null) {
                    this.mAlarmTimeSetting.clearFocusHour();
                    int returnValue = saveDetailChange();
                    if (returnValue == 1 || returnValue == 2) {
                        this.mDataChanged = true;
                        return;
                    }
                    this.mSubmitting = false;
                    this.mAlarmTimeSetting.changeToEditMode(false);
                }
            }
        }
    }

    private void fillEditData() {
        Log.secD("AlarmEditActivity", "fillEditData");
        if (this.mAlarmTimeSetting == null) {
            this.mAlarmTimeSetting = (AlarmTimeSetting) findViewById(C0490R.id.alarm_time_group);
        }
        if (this.mOriginalAlarm.isSpecificDate()) {
            this.mAlarmTimeSetting.setDateByAlertTimeMilliseconds(this.mOriginalAlarm.mAlarmAlertTime);
        }
        this.mAlarmTimeSetting.resetTime(this.mOriginalAlarm.mAlarmTime / 100, this.mOriginalAlarm.mAlarmTime % 100);
        if (this.mAlarmListDetail != null) {
            this.mAlarmListDetail.initData(this.mOriginalAlarm);
            this.mOriginalAlarm.mAlarmSoundTone = this.mAlarmListDetail.getAlarmToneIndex();
            if (mIsSupportWorkdayAlarm && this.mOriginalAlarm.isWorkingDay()) {
                setCheckDayForWorkingDay();
            } else {
                changeNotidaysByRepeatCheckDay(this.mOriginalAlarm.isWeeklyAlarm() ? this.mOriginalAlarm.getAlarmRepeat() : 0);
            }
        }
        int checkDay = 0;
        if (this.mAlarmRepeatButtonGroup != null) {
            checkDay = this.mAlarmRepeatButtonGroup.getCheckDay();
        }
        this.mOriginalAlarm.mRepeatType = getAlarmRepeatValue(checkDay, this.mOriginalAlarm.mAlarmTime, this.mOriginalAlarm.isWorkingDay());
    }

    private synchronized int saveDetailChange() {
        int i;
        Log.secD("AlarmEditActivity", "saveDetailChange()");
        if (this.mAlarmTimeSetting != null) {
            this.mAlarmTimeSetting.hideKeyBoard();
        }
        AlarmItem saveAlarmItem = getNewAlarmItem();
        saveAlarmItem.mActivate = 1;
        saveAlarmItem.mId = this.mOriginalAlarm.mId;
        saveAlarmItem.setCreateTime();
        Log.secD("AlarmEditActivity", "saveDetailChange() / org_increasing = " + this.mOriginalAlarm.isIncreasingVolume() + "/ new_increasing = " + saveAlarmItem.isIncreasingVolume());
        if (this.mAlarmTimeSetting.isDateSelectedState()) {
            saveAlarmItem.mAlarmAlertTime = this.mAlarmTimeSetting.getNextAlertTime();
            saveAlarmItem.setSpecificDate(true);
        } else {
            Calendar c = Calendar.getInstance();
            c.set(11, saveAlarmItem.mAlarmTime / 100);
            c.set(12, saveAlarmItem.mAlarmTime % 100);
            c.set(13, 0);
            c.set(14, 0);
            saveAlarmItem.mAlarmAlertTime = c.getTimeInMillis();
            saveAlarmItem.setSpecificDate(false);
            saveAlarmItem.calculateFirstAlertTime(this);
        }
        setCelebVoicePathPreference(saveAlarmItem.mCelebVoicePath);
        insertSaLoggingForSave(saveAlarmItem);
        AlarmRingtoneManager.setAlarmTonePreference(this, saveAlarmItem.mSoundUri);
        if (!this.mIsAlarmEditMode) {
            int sameAlarmItemID = AlarmUtil.checkSameAlarm(this, saveAlarmItem);
            if (sameAlarmItemID != -1) {
                Log.secD("AlarmEditActivity", "saveDetailChange() Exist same alarm, do not insert/update db");
                if (this.mIsAlarmLaunchByWidget) {
                    AlarmUtil.sendSelctionToAlarmWidget(getApplicationContext(), this.mWidgetId, sameAlarmItemID, sameAlarmItemID, -1);
                }
                AlarmUtil.sendAlarmDeleteModeUpdate(getBaseContext());
                activityFinished();
                i = 2;
            }
        }
        int submittedAlarmId = -1;
        int duplicatedAlarmId = AlarmViewModelUtil.checkDuplicationAlarm(this, saveAlarmItem);
        if (duplicatedAlarmId == -2) {
            Log.secD("AlarmEditActivity", "saveDetailChange() Alarm.MaxCountAlarm");
            i = 50;
        } else {
            if (this.mIsAlarmEditMode) {
                int transactionResult = getContentResolver().update(AlarmProvider.CONTENT_URI, saveAlarmItem.getContentValues(), "_id = " + saveAlarmItem.mId, null);
                if (transactionResult > 0) {
                    submittedAlarmId = saveAlarmItem.mId;
                }
                if (this.mDataChanged && transactionResult == 0) {
                    duplicatedAlarmId = saveAlarmItem.mId;
                    submittedAlarmId = (int) AlarmProvider.getId(getContentResolver().insert(AlarmProvider.CONTENT_URI, saveAlarmItem.getContentValues()));
                }
            } else if (duplicatedAlarmId != -1) {
                if (getContentResolver().update(AlarmProvider.CONTENT_URI, saveAlarmItem.getContentValues(), "_id = " + duplicatedAlarmId, null) > 0) {
                    submittedAlarmId = duplicatedAlarmId;
                }
            } else if (AlarmProvider.getAlarmCount(this) >= 50) {
                AlarmUtil.showMaxCountToast(this);
                i = 50;
            } else {
                submittedAlarmId = (int) AlarmProvider.getId(getContentResolver().insert(AlarmProvider.CONTENT_URI, saveAlarmItem.getContentValues()));
            }
            Intent intent;
            if (submittedAlarmId > 0) {
                AlarmUtil.sendStopAlertByChangeIntent(this, submittedAlarmId);
                AlarmUtil.sendAlarmDeleteModeUpdate(this);
                intent = new Intent("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_EDIT_FINISHED_AND_SAVED_ALARM");
                intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", submittedAlarmId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                new Thread(new Runnable() {
                    public void run() {
                        AlarmProvider.enableNextAlert(AlarmEditActivity.this.getBaseContext());
                    }
                }).start();
                Log.m41d("AlarmEditActivity", "saveDetailChange mItem = " + saveAlarmItem.toString());
            } else {
                String memoryFull = getResources().getString(C0490R.string.memory_full);
                intent = new Intent("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_DIRECTSAVED");
                intent.putExtra("save_msg", memoryFull);
                sendBroadcast(intent);
                intent.setPackage("com.sec.android.app.clockpackage");
                sendBroadcast(intent);
            }
            AlarmNotificationHelper.clearNotification(this, saveAlarmItem.mId);
            if (this.mIsAlarmLaunchByWidget || duplicatedAlarmId != -1) {
                AlarmUtil.sendSelctionToAlarmWidget(getApplicationContext(), this.mWidgetId, submittedAlarmId, duplicatedAlarmId, -1);
            }
            Log.secD("AlarmEditActivity", "saveDetailChange() - submittedAlarmId = " + submittedAlarmId + " , duplicatedAlarmId = " + duplicatedAlarmId);
            activityFinished();
            i = 1;
        }
        return i;
    }

    private void insertSaLoggingForSave(AlarmItem saveAlarmItem) {
        ClockUtils.insertSaLog("103", "3000", ClockUtils.isEnableString(saveAlarmItem.mAlarmName) ? "1" : "0");
        ClockUtils.insertSaLog("103", "3001", saveAlarmItem.mSnoozeActivate ? "1" : "0");
        ClockUtils.insertSaLog("103", "3002", saveAlarmItem.isAlarmToneOn() ? "1" : "0");
        ClockUtils.insertSaLog("103", "3003", saveAlarmItem.isVibrationAlarm() ? "1" : "0");
        ClockUtils.insertSaLog("103", "3004", saveAlarmItem.isTtsAlarm() ? "1" : "0");
        ClockUtils.insertSaLog("103", "3008", saveAlarmItem.isSpecificDate() ? "specific" : "repeat");
        ClockUtils.insertSaStatusLog(getApplicationContext(), "5115", saveAlarmItem.mVibrationPattern == 50035 ? "default" : "other");
        boolean isRingtoneSelected = true;
        if (this.mIsSupportBixbyBriefingMenu || this.mIsSupportNewCelebFeature) {
            String detail = "";
            String contentName = "a";
            if (!saveAlarmItem.isMasterSoundOn()) {
                detail = "e";
            } else if (saveAlarmItem.isNewBixbyOn()) {
                detail = "a";
                isRingtoneSelected = false;
                contentName = "bixby default";
            } else if (saveAlarmItem.isNewCelebOn()) {
                detail = this.mIsSupportBixbyBriefingMenu ? "b" : "c";
                isRingtoneSelected = false;
                contentName = AlarmUtil.getCelebVoiceName(getApplicationContext(), saveAlarmItem.mCelebVoicePath);
            } else {
                detail = "d";
            }
            ClockUtils.insertSaLog("103", "6502", detail);
            ClockUtils.insertSaLog("613", "6503", contentName);
        }
        if (isRingtoneSelected) {
            String alarmTone = "Default Index";
            if (saveAlarmItem.mAlarmSoundTone == this.mAlarmListDetail.getDefaultRingtoneIndex()) {
                alarmTone = "Default";
            } else if (!saveAlarmItem.mSoundUri.contains("internal")) {
                alarmTone = "User";
            }
            ClockUtils.insertSaLog("103", "3009", alarmTone);
        }
    }

    private String getDetailForSaLog(int repeatDay) {
        switch (Integer.bitCount(repeatDay)) {
            case 0:
                return "Never";
            case 1:
                return "Single day";
            case 7:
                return "All day";
            default:
                if (repeatDay == 1118480) {
                    return "Mon to Fri";
                }
                return "Others";
        }
    }

    private void cancelDetailChange() {
        Log.secD("AlarmEditActivity", "cancelDetailChange()");
        stopAlarmRingtonePlayer();
        activityFinished();
    }

    private AlarmItem getNewAlarmItem() {
        Log.secD("AlarmEditActivity", "getNewAlarmItem");
        AlarmItem newItem = new AlarmItem();
        if (this.mAlarmListDetail != null) {
            newItem = this.mAlarmListDetail.getAlarmItem();
            newItem.mSoundUri = this.mAlarmListDetail.getAlarmToneStr();
        }
        newItem.mAlarmTime = this.mAlarmTimeSetting.getAlarmTime();
        int checkDay = 0;
        if (this.mAlarmRepeatButtonGroup != null) {
            checkDay = this.mAlarmRepeatButtonGroup.getCheckDay();
        }
        newItem.mRepeatType = getAlarmRepeatValue(checkDay, newItem.mAlarmTime, newItem.isWorkingDay());
        return newItem;
    }

    private int getAlarmRepeatValue(int checkDay, int alarmTime, boolean isWorkingDayOn) {
        int repeatType;
        boolean isEveryWeek = true;
        int result = 0;
        if (isWorkingDayOn) {
            Log.secD("AlarmEditActivity", "WorkingDay Alarm");
            repeatType = 286331157;
        } else {
            if (checkDay == 0) {
                Calendar c = Calendar.getInstance();
                int curHour = c.get(11);
                int curMinute = c.get(12);
                int curDay = c.get(7);
                if ((curHour * 100) + curMinute >= alarmTime) {
                    Log.secD("AlarmEditActivity", "getAlarmRepeatValue() - (curHour * 100 + (mHour * 100 + mMin) )");
                    c.add(6, 1);
                    curDay = c.get(7);
                    Log.secD("AlarmEditActivity", "curDay = " + curDay);
                }
                repeatType = 0 & 15;
                checkDay = (((1 << (((7 - curDay) + 1) * 4)) & -16) | 0) >> 4;
                isEveryWeek = false;
                Log.secD("AlarmEditActivity", "getAlarmRepeatValue() - checkDay = " + checkDay);
            }
            Log.secD("AlarmEditActivity", "getAlarmRepeatValue() : checkDay = 0x" + Integer.toHexString(checkDay));
            result = 0 | ((checkDay << 4) & -16);
            Log.secD("AlarmEditActivity", "result = 0x" + Integer.toHexString(result));
            if (isEveryWeek) {
                result |= 5;
            } else {
                result |= 1;
            }
            if (this.mAlarmTimeSetting != null) {
                int dateRepeat = this.mAlarmTimeSetting.getCheckDayForDateAlarm();
                if (dateRepeat != 0) {
                    result = dateRepeat;
                }
            }
            repeatType = result;
        }
        Log.secD("AlarmEditActivity", "result = 0x" + Integer.toHexString(result));
        Log.secD("AlarmEditActivity", "mItem.mRepeatType = 0x" + Integer.toHexString(repeatType));
        return repeatType;
    }

    private void onAlarmRepeatClicked(int alarmRepeat) {
        if (this.mAlarmListDetail != null) {
            if (alarmRepeat > 0) {
                if (mIsSupportWorkdayAlarm && this.mAlarmListDetail.isWorkingDayAlarm()) {
                    Toast.makeText(this, getResources().getString(C0490R.string.alarm_cancel_workingday), 0).show();
                }
                this.mAlarmListDetail.setHolidayEnable(true);
            } else {
                this.mAlarmListDetail.setHolidayEnable(false);
            }
            if (mIsSupportWorkdayAlarm) {
                this.mAlarmListDetail.setHolidayWorkingdayValue(false);
            }
        }
    }

    private boolean blockScrollForRepeatDragVI(int yValue, MotionEvent event) {
        if (this.mAlarmRepeatButtonGroup == null) {
            return false;
        }
        int xValue = (int) event.getRawX();
        int[] location = new int[2];
        this.mAlarmRepeatButtonGroup.getLocationOnScreen(location);
        if (yValue >= location[1] && yValue <= location[1] + this.mAlarmRepeatButtonGroup.getHeight() && this.mAlarmRepeatButtonGroup.mIsDragging) {
            Log.secD("AlarmEditActivity", "yValue = " + yValue + ", location[1] = " + location[1]);
            if (xValue < location[0] || xValue > location[0] + this.mAlarmRepeatButtonGroup.getWidth()) {
                return false;
            }
            MotionEvent myEvent = MotionEvent.obtain(event);
            myEvent.setLocation(event.getRawX() - ((float) location[0]), event.getRawY());
            this.mAlarmRepeatButtonGroup.onTouchEvent(myEvent);
            myEvent.recycle();
            return true;
        } else if (!this.mAlarmRepeatButtonGroup.mIsDragging || event.getAction() != 1) {
            return false;
        } else {
            this.mAlarmRepeatButtonGroup.onTouchEvent(event);
            return true;
        }
    }

    private void setCheckDayForWorkingDay() {
        Log.secD("AlarmEditActivity", "setCheckDayForWorkingDay()");
        if (this.mAlarmRepeatButtonGroup != null) {
            this.mAlarmRepeatButtonGroup.setCheckDay(0);
            for (int i = 0; i < 7; i++) {
                this.mAlarmRepeatButtonGroup.setSelectionMarkAnimator(i, false);
            }
        }
        if (this.mAlarmTimeSetting != null) {
            this.mAlarmTimeSetting.setWorkingDayAlarm();
        }
    }

    private void changeNotidaysByRepeatCheckDay(int mRepeatData) {
        if (this.mAlarmTimeSetting != null && this.mAlarmRepeatButtonGroup != null) {
            Log.secD("AlarmEditActivity", "changeNotidaysByRepeatCheckDay() - mRepeatData = " + mRepeatData);
            if (mRepeatData < 0) {
                mRepeatData = 0;
            }
            this.mAlarmTimeSetting.changeToEditMode(false);
            this.mAlarmRepeatButtonGroup.setCheckDay(mRepeatData);
            if (mRepeatData > 0) {
                this.mAlarmTimeSetting.calculateAlarmRepeatText(this.mAlarmRepeatButtonGroup.getAlarmRepeatText());
                return;
            }
            if (this.mAlarmTimeSetting.getRepeatDateWorkingState() == 1) {
                this.mAlarmTimeSetting.setRepeatDateWorkingState(0);
                this.mAlarmTimeSetting.calculateNotidaysAndSetText();
            }
            this.mAlarmRepeatButtonGroup.setAllRepeatBtn(false);
        }
    }

    private void setCelebVoicePathPreference(String celebVoicePath) {
        Log.secD("AlarmEditActivity", "setCelebVoicePathPreference() / celebVoicePath = " + celebVoicePath);
        Editor editor = getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
        editor.putString("alarm_bixby_celebrity_path", celebVoicePath);
        editor.apply();
    }

    private void changeTimePickerEditMode(boolean isEditMode) {
        if (this.mAlarmTimeSetting != null) {
            this.mAlarmTimeSetting.changeToEditMode(isEditMode);
        }
    }

    private void stopAlarmRingtonePlayer() {
        if (this.mAlarmListDetail != null) {
            this.mAlarmListDetail.stopPlayer();
        }
    }
}
