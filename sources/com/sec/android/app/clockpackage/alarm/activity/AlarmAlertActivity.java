package com.sec.android.app.clockpackage.alarm.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.alarm.view.AlarmCover;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmWeatherUtil;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.activity.FullAlertActivity;
import com.sec.android.app.clockpackage.commonalert.util.AlertUtils;
import com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab;
import com.sec.android.app.clockpackage.commonalert.view.AlertSlidingTab.OnTriggerListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import sec.color.gradient.view.RadialGradientView;

@SuppressLint({"ServiceCast"})
public class AlarmAlertActivity extends FullAlertActivity {
    private static int mCoverType = 1;
    private final boolean SNOOZED = true;
    private final boolean STOPPED = false;
    private final String TAG = "AlarmAlertActivity";
    private View mAlarmSnooze;
    private AlarmCover mCover = null;
    private InternalReceiver mInternalReceiver;
    private boolean mIsFromHUN = false;
    private boolean mIsFromNotification = false;
    private boolean mIsStopAlertByChange = false;
    private Boolean mIsTimeOut = Boolean.valueOf(false);
    private final AlarmItem mItem = new AlarmItem();
    private long mLastTimeOfCalledHun = 0;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$1 */
    class C04911 implements OnTriggerListener {
        C04911() {
        }

        public void onGrabbedStateChange(View arg0, int isPressed) {
            Log.secD("AlarmAlertActivity", "onGrabbedStateChange : " + isPressed);
            if (AlarmAlertActivity.this.mAlarmSnooze == null) {
                return;
            }
            if (isPressed == 1) {
                AlarmAlertActivity.this.mAlarmSnooze.startAnimation(AnimationUtils.loadAnimation(AlarmAlertActivity.this.mContext, C0490R.anim.fade_out));
                AlarmAlertActivity.this.mAlarmSnooze.setAlpha(0.0f);
            } else if (!Alarm.isStopAlarmAlert && !AlarmAlertActivity.this.mItem.isDefaultStop()) {
                AlarmAlertActivity.this.mAlarmSnooze.startAnimation(AnimationUtils.loadAnimation(AlarmAlertActivity.this.mContext, C0490R.anim.fade_in));
                AlarmAlertActivity.this.mAlarmSnooze.setAlpha(1.0f);
            }
        }

        public void onTrigger(View arg0, int arg1) {
            Log.secD("AlarmAlertActivity", "onTrigger arg1 = " + arg1);
            if (1 == arg1) {
                Logger.m47f("AlarmAlertActivity", "DISMISS_HANDLE is over");
                AlarmAlertActivity.this.finishAlarm(false);
                ClockUtils.insertSaLog("107", "1242");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$2 */
    class C04922 extends BroadcastReceiver {
        C04922() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r11, android.content.Intent r12) {
            /*
            r10 = this;
            r6 = -1;
            r7 = 1;
            r5 = 0;
            r0 = r12.getAction();
            r4 = "AlarmAlertActivity";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "MyReceiver receive action : ";
            r8 = r8.append(r9);
            r8 = r8.append(r0);
            r8 = r8.toString();
            com.sec.android.app.clockpackage.common.util.Log.m41d(r4, r8);
            if (r0 != 0) goto L_0x0022;
        L_0x0021:
            return;
        L_0x0022:
            r4 = r0.hashCode();
            switch(r4) {
                case -1439628154: goto L_0x0048;
                case -23421476: goto L_0x003e;
                case 183811913: goto L_0x0052;
                case 279267191: goto L_0x005c;
                case 795315170: goto L_0x0066;
                case 1730993015: goto L_0x0034;
                default: goto L_0x0029;
            };
        L_0x0029:
            r4 = r6;
        L_0x002a:
            switch(r4) {
                case 0: goto L_0x002e;
                case 1: goto L_0x0070;
                case 2: goto L_0x00a2;
                case 3: goto L_0x00df;
                case 4: goto L_0x00e6;
                case 5: goto L_0x00fc;
                default: goto L_0x002d;
            };
        L_0x002d:
            goto L_0x0021;
        L_0x002e:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r7);
            goto L_0x0021;
        L_0x0034:
            r4 = "AlarmSnooze";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0029;
        L_0x003c:
            r4 = r5;
            goto L_0x002a;
        L_0x003e:
            r4 = "com.samsung.flipfolder.OPEN";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0029;
        L_0x0046:
            r4 = r7;
            goto L_0x002a;
        L_0x0048:
            r4 = "com.samsung.sec.android.clockpackage.alarm.ALARM_STOP";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0029;
        L_0x0050:
            r4 = 2;
            goto L_0x002a;
        L_0x0052:
            r4 = "AlarmStopAlert";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0029;
        L_0x005a:
            r4 = 3;
            goto L_0x002a;
        L_0x005c:
            r4 = "com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0029;
        L_0x0064:
            r4 = 4;
            goto L_0x002a;
        L_0x0066:
            r4 = "com.samsung.sec.android.clockpacakge.alarm.ALARM_EDIT_MESSAGE";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0029;
        L_0x006e:
            r4 = 5;
            goto L_0x002a;
        L_0x0070:
            r4 = "flipOpen";
            r3 = r12.getBooleanExtra(r4, r5);
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4 = r4.mIsPreFlipOpen;
            if (r4 == 0) goto L_0x0096;
        L_0x007e:
            if (r3 != 0) goto L_0x0096;
        L_0x0080:
            r4 = "AlarmAlertActivity";
            r6 = "FlipFolder close";
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r6);
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4 = r4.mItem;
            r4 = r4.mSnoozeActivate;
            if (r4 == 0) goto L_0x009c;
        L_0x0091:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r7);
        L_0x0096:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.mIsPreFlipOpen = r3;
            goto L_0x0021;
        L_0x009c:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r5);
            goto L_0x0096;
        L_0x00a2:
            r4 = "bDismiss";
            r1 = r12.getBooleanExtra(r4, r5);
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r6 = "bisTimeOut";
            r6 = r12.getBooleanExtra(r6, r5);
            r6 = java.lang.Boolean.valueOf(r6);
            r4.mIsTimeOut = r6;
            r4 = "AlarmAlertActivity";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r8 = "bDismiss = ";
            r6 = r6.append(r8);
            r6 = r6.append(r1);
            r6 = r6.toString();
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r6);
            if (r1 == 0) goto L_0x00d8;
        L_0x00d1:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r5);
            goto L_0x0021;
        L_0x00d8:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r7);
            goto L_0x0021;
        L_0x00df:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r5);
            goto L_0x0021;
        L_0x00e6:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4 = r4.mCallState;
            if (r4 != 0) goto L_0x0021;
        L_0x00ee:
            r4 = "AlarmAlertActivity";
            r5 = "EXTRA_STATE_IDLE, isPause = true";
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r5);
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.updateTimeDisplay();
            goto L_0x0021;
        L_0x00fc:
            r4 = "AlarmID";
            r2 = r12.getIntExtra(r4, r6);
            r4 = "AlarmAlertActivity";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r8 = "id = ";
            r6 = r6.append(r8);
            r6 = r6.append(r2);
            r8 = " mItem.mId = ";
            r6 = r6.append(r8);
            r8 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r8 = r8.mItem;
            r8 = r8.mId;
            r6 = r6.append(r8);
            r6 = r6.toString();
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r6);
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4 = r4.mItem;
            r4 = r4.mId;
            if (r2 != r4) goto L_0x0021;
        L_0x0136:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.mIsStopAlertByChange = r7;
            r4 = com.sec.android.app.clockpackage.alarm.model.Alarm.isStopAlarmAlert;
            if (r4 != 0) goto L_0x0144;
        L_0x013f:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finishAlarm(r5);
        L_0x0144:
            r4 = com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.this;
            r4.finish();
            goto L_0x0021;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity.2.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$3 */
    class C04933 implements OnClickListener {
        C04933() {
        }

        public void onClick(View v) {
            Log.secD("AlarmAlertActivity", "mAlarmSnooze onClick");
            if (AlarmAlertActivity.this.mItem != null && AlarmAlertActivity.this.mItem.mSnoozeActivate) {
                AlarmAlertActivity.this.finishAlarm(true);
                if (StateUtils.isUserUnlockedDevice(AlarmAlertActivity.this.mContext)) {
                    ClockUtils.insertSaLog("107", "1052");
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$4 */
    class C04944 implements OnClickListener {
        C04944() {
        }

        public void onClick(View v) {
            Log.m41d("AlarmAlertActivity", "weatherIcon onClick");
            AlarmAlertActivity.this.finishAlarm(true);
            AlarmService.startCpLink(AlarmAlertActivity.this.mContext);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$5 */
    class C04955 implements OnClickListener {
        C04955() {
        }

        public void onClick(View v) {
            Log.m41d("AlarmAlertActivity", "poweredByTextAndWeatherCpLogo onClick");
            AlarmAlertActivity.this.finishAlarm(true);
            AlarmService.startCpLink(AlarmAlertActivity.this.mContext);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$6 */
    class C04966 extends StateListener {
        C04966() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if (state.getSwitchState()) {
                Log.m41d("AlarmAlertActivity", "mCoverStateListener cover is close -> open");
                if (AlarmAlertActivity.this.mCoverManager != null) {
                    AlarmAlertActivity.this.mCoverManager.setCoverModeToWindow(AlarmAlertActivity.this.getWindow(), 0);
                }
                AlarmAlertActivity.this.mIsCoverOpen = true;
                if (AlarmAlertActivity.mCoverType != 0) {
                    if (AlarmAlertActivity.this.mCover != null) {
                        AlarmAlertActivity.this.mCover.dismissDialog();
                        AlarmAlertActivity.this.mCover = null;
                    }
                    AlarmAlertActivity.this.setVisibillityforView(true);
                    AlarmAlertActivity.this.setSystemUIFlagForFullScreen();
                    AlarmAlertActivity.this.initContentView();
                    AlarmAlertActivity.this.setWindowOnTop();
                    AlarmAlertActivity.this.setBixbyBriefingWeatherInfo();
                    return;
                }
                return;
            }
            Log.m41d("AlarmAlertActivity", "mCoverStateListener cover is open -> close mIsCoverOpen = " + AlarmAlertActivity.this.mIsCoverOpen);
            AlarmAlertActivity.this.mIsCoverOpen = false;
            if (AlarmAlertActivity.this.mItem.isDefaultStop()) {
                AlarmAlertActivity.this.finishAlarm(false);
            } else {
                AlarmAlertActivity.this.finishAlarm(true);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity$7 */
    class C04977 implements Runnable {
        C04977() {
        }

        public void run() {
            if (!Alarm.isStopAlarmAlert) {
                String currentTop = ClockUtils.getTopActivity(AlarmAlertActivity.this.getApplication());
                Log.secI("AlarmAlertActivity", "currentTop = " + currentTop);
                if (!"com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity".equals(currentTop) && !currentTop.contains("InCallActivity") && !AlarmAlertActivity.this.mIsFromNotification) {
                    Log.secD("AlarmAlertActivity", "HUN should be shown. AlarmAlertActivity killed");
                    AlarmAlertActivity.this.callHun();
                    AlarmAlertActivity.this.finish();
                }
            }
        }
    }

    private class InternalReceiver extends BroadcastReceiver {
        private InternalReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("AlarmAlertActivity", "InternalReceiver receive action : " + action);
            if (action != null) {
                Object obj = -1;
                switch (action.hashCode()) {
                    case 1744853738:
                        if (action.equals("com.samsung.android.bixby.intent.action.REQUEST_SHOW_WEATHER_ICON")) {
                            obj = null;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        Log.secD("AlarmAlertActivity", "mIsCoverOpen : " + AlarmAlertActivity.this.mIsCoverOpen);
                        if (AlarmAlertActivity.this.mIsCoverOpen) {
                            AlarmAlertActivity.this.setBixbyBriefingWeatherInfo();
                            return;
                        } else if (AlarmAlertActivity.this.mCover != null) {
                            AlarmAlertActivity.this.mCover.updateWeatherIcon();
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlarmAlertActivity", "onCreate()");
        AlarmUtil.setStopAlarmAlertValue(false);
        getAlarmDataFromIntent();
        sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.START_CLOCKPACKAGE").putExtra("FROM_ALARM_ALERT", true));
        setCoverStateManager();
        Log.secD("AlarmAlertActivity", "mIsCoverOpen = " + this.mIsCoverOpen + ", mCoverType = " + mCoverType);
        if (this.mCoverViewSize != 3) {
            setWindowOnTop();
        }
        acquireDim("AlarmAlertActivity");
        if (StateUtils.isUserUnlockedDevice(this.mContext)) {
            ClockUtils.insertSaLog("107");
        }
        AlertSlidingTab.setType(0);
        boolean isDexMode = StateUtils.isContextInDexMode(this.mContext);
        setContentView(C0490R.layout.alarm_alert);
        if (this.mIsCoverOpen || isDexMode || mCoverType == 0) {
            setWindowOnTop();
            initContentView();
            setBixbyBriefingWeatherInfo();
            return;
        }
        setVisibillityforView(false);
        showCoverAlarmByDialog();
    }

    private void setVisibillityforView(boolean flag) {
        int i;
        int i2 = 0;
        View view = findViewById(C0490R.id.alarm_alert_top_layout);
        if (flag) {
            i = 0;
        } else {
            i = 4;
        }
        view.setVisibility(i);
        view = findViewById(C0490R.id.gradient_bg);
        if (!flag) {
            i2 = 4;
        }
        view.setVisibility(i2);
    }

    protected void onResume() {
        super.onResume();
        Log.secD("AlarmAlertActivity", "onResume()");
        if (this.mCover != null) {
            this.mCover.setSystemUIFlagForFullScreen(this);
        }
        setRegisterInternalReceiver();
        setRegisterReceiver();
        if (!this.mIsFromHUN) {
            AlertUtils.sendAlarmStartedIntent(this.mContext, "alertAlarmID", this.mItem.mId);
        }
    }

    protected void onPause() {
        super.onPause();
        Log.secD("AlarmAlertActivity", "onPause()");
    }

    protected void onDestroy() {
        Log.secD("AlarmAlertActivity", "onDestroy()");
        finishAlarmAlert();
        releaseDim();
        setStatusBarState(false);
        unregisterCoverManager();
        resetTelephonyListener();
        removeInstance();
        super.onDestroy();
    }

    private void getAlarmDataFromIntent() {
        Log.secD("AlarmAlertActivity", "getAlarmDataFromIntent");
        Intent intent = getIntent();
        if (this.mItem != null) {
            Log.secD("AlarmAlertActivity", "if (mItem != null) {");
            this.mItem.readFromIntent(intent);
            this.mIsFromNotification = intent.getBooleanExtra("fromNotification", false);
            Log.secD("AlarmAlertActivity", "mIsFromNotification = " + this.mIsFromNotification);
            this.mIsFromHUN = intent.getBooleanExtra("FROM_ALARM_HUN", false);
            Log.secD("AlarmAlertActivity", "mIsFromHUN = " + this.mIsFromHUN);
            if (this.mIsFromNotification) {
                sendBroadcast(new Intent("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_TOUCH"));
            }
        }
    }

    private void setSlidingView() {
        Log.secD("AlarmAlertActivity", "setSlidingView()");
        this.mSelector = (AlertSlidingTab) findViewById(C0490R.id.tab_selector);
        this.mSelector.setOnTriggerListener(new C04911());
    }

    private void setRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("AlarmSnooze");
        if (Feature.isFolder(this.mContext)) {
            filter.addAction("com.samsung.flipfolder.OPEN");
        }
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP");
        filter.addAction("AlarmStopAlert");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
        filter.addAction("com.samsung.sec.android.clockpacakge.alarm.ALARM_EDIT_MESSAGE");
        if (this.mReceiver == null) {
            this.mReceiver = new C04922();
        }
        registerReceiver(this.mReceiver, filter);
    }

    private void setRegisterInternalReceiver() {
        this.mInternalReceiver = new InternalReceiver();
        IntentFilter internalFilter = new IntentFilter();
        internalFilter.addAction("com.samsung.android.bixby.intent.action.REQUEST_SHOW_WEATHER_ICON");
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mInternalReceiver, internalFilter);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.secD("AlarmAlertActivity", "onConfigurationChanged()" + newConfig.orientation);
        super.onConfigurationChanged(newConfig);
        setWindowOnTop();
        setContentView(C0490R.layout.alarm_alert);
        initContentView();
        setBixbyBriefingWeatherInfo();
    }

    private void removeInstance() {
        Log.secD("AlarmAlertActivity", "removeInstance()");
        if (this.mCover != null) {
            this.mCover.dismissDialog();
            this.mCover = null;
        }
        resetSelector();
        if (this.mContext != null) {
            this.mContext = null;
        }
        setUnregisterReceiver();
    }

    private void finishAlarm(boolean isStopBySnoozeBtn) {
        Logger.m47f("AlarmAlertActivity", "finishAlarm BySnooze:" + isStopBySnoozeBtn);
        if (this.mContext == null) {
            this.mContext = this;
        }
        ClockUtils.alarmAlertTimeInCall = 0;
        AlertUtils.sendStopAlarmAlertIntent(this.mContext, this.mIsTimeOut.booleanValue());
        AlarmNotificationHelper.getInstance().cancel(this, 268439552);
        if (this.mItem != null) {
            if (this.mItem.mSnoozeActivate && isStopBySnoozeBtn) {
                AlarmNotificationHelper.showSnoozeNotification(this.mContext, this.mItem, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
            } else {
                AlarmNotificationHelper.clearNotification(this.mContext, this.mItem.mId);
                if (this.mIsTimeOut.booleanValue()) {
                    AlarmNotificationHelper.showMissedNotification(this.mContext, this.mItem);
                }
                if (!this.mIsStopAlertByChange) {
                    if (this.mItem.mAlarmName != null && this.mItem.isOneTimeAlarm() && this.mItem.mAlarmName.equals("Start Alarm Test")) {
                        AlarmNotificationHelper.clearNotification(this.mContext, this.mItem.mId);
                        AlarmDataHandler.deleteAlarm(this.mContext, this.mItem.mId);
                    } else {
                        ArrayList<Integer> changedAlarmIds = AlarmDataHandler.updateDismissedAlarm(this.mContext, this.mItem);
                        int size = changedAlarmIds.size();
                        Log.secD("AlarmAlertActivity", "changedAlarmIds.size : " + size);
                        if (size > 0) {
                            for (int indexItem = 0; indexItem < size; indexItem++) {
                                AlarmNotificationHelper.clearNotification(this.mContext, ((Integer) changedAlarmIds.get(indexItem)).intValue());
                            }
                        }
                    }
                }
            }
        }
        AlarmUtil.setStopAlarmAlertValue(true);
        setStatusBarState(false);
        unregisterCoverManager();
        resetTelephonyListener();
        setUnregisterReceiver();
        finish();
    }

    private void initContentView() {
        Log.secD("AlarmAlertActivity", "initContentView()");
        TextClock currentTime = (TextClock) findViewById(C0490R.id.alarm_alert_current_time);
        TextView alarmNameView = (TextView) findViewById(C0490R.id.alarm_alert_name1);
        Typeface tf = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (tf == null) {
            tf = Typeface.create("sans-serif-light", 0);
        }
        if (!(tf == null || currentTime == null)) {
            currentTime.setTypeface(tf);
        }
        if (alarmNameView != null) {
            alarmNameView.setSingleLine(true);
            if (this.mItem == null || this.mItem.mAlarmName == null || this.mItem.mAlarmName.length() <= 0) {
                alarmNameView.setText(C0490R.string.alarm);
            } else {
                alarmNameView.setVisibility(0);
                alarmNameView.setText(this.mItem.mAlarmName);
            }
        }
        this.mAlarmSnooze = findViewById(C0490R.id.alarm_btn_snooze);
        if (this.mAlarmSnooze != null) {
            this.mAlarmSnooze.setContentDescription(getResources().getString(C0490R.string.snooze) + ' ' + getResources().getString(C0490R.string.button));
            this.mAlarmSnooze.setOnClickListener(new C04933());
        }
        ClockUtils.setLargeTextSize(this.mContext, new TextView[]{(TextView) findViewById(C0490R.id.alarm_textview_snooze), (TextView) findViewById(C0490R.id.alarm_alert_top_icon)}, 1.3f);
        setGradientBackground((RadialGradientView) findViewById(C0490R.id.gradient_bg));
        updateTimeDisplay();
        setSlidingView();
    }

    private void setBixbyBriefingWeatherInfo() {
        Log.secD("AlarmAlertActivity", "setBixbyBriefingWeatherInfo");
        if (!this.mItem.isPossibleBixbyBriefingAlarm() || AlarmService.sBixbyBriefWeatherConditionCode == 999) {
            Log.secD("AlarmAlertActivity", "setBixbyBriefingWeatherInfo return 1");
        } else if (this.mContext == null) {
            Log.secD("AlarmAlertActivity", "setBixbyBriefingWeatherInfo return 2");
        } else {
            int weatherIconNumber = AlarmWeatherUtil.getWeatherIconNumber(AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
            if (weatherIconNumber == 115) {
                Log.secD("AlarmAlertActivity", "setBixbyBriefingWeatherInfo return 3");
                return;
            }
            ImageView weatherIcon = (ImageView) findViewById(C0490R.id.alarm_alert_currenttime_layout).findViewById(C0490R.id.alarm_weather_icon);
            if (weatherIcon != null) {
                weatherIcon.setVisibility(0);
                AlarmWeatherUtil.setWeatherImg(this.mContext, weatherIcon, weatherIconNumber, AlarmService.sBixbyBriefDaytime);
                weatherIcon.setOnClickListener(new C04944());
            }
            RelativeLayout weatherCpLogo = (RelativeLayout) findViewById(C0490R.id.alarm_weather_cp_logo);
            if (weatherCpLogo != null) {
                weatherCpLogo.setVisibility(0);
            }
            View poweredByTextAndWeatherCpLogo = findViewById(C0490R.id.alarm_poweredby_text_and_weather_cp_logo);
            if (poweredByTextAndWeatherCpLogo != null) {
                poweredByTextAndWeatherCpLogo.setVisibility(0);
                poweredByTextAndWeatherCpLogo.setOnClickListener(new C04955());
            }
            ImageView weatherCpImage = (ImageView) findViewById(C0490R.id.alarm_weather_cp_image);
            if (weatherCpImage != null) {
                AlarmWeatherUtil.setCpLogo(this.mContext, weatherCpImage);
            }
        }
    }

    private void updateTimeDisplay() {
        TextClock currentTimeAmPm;
        boolean is24HMode = DateFormat.is24HourFormat(this);
        if (StateUtils.isLeftAmPm()) {
            currentTimeAmPm = (TextClock) findViewById(C0490R.id.alarm_alert_current_time_ampm_kor);
        } else {
            currentTimeAmPm = (TextClock) findViewById(C0490R.id.alarm_alert_current_time_ampm);
        }
        if (!(currentTimeAmPm == null || is24HMode)) {
            currentTimeAmPm.setVisibility(0);
        }
        if (!(this.mAlarmSnooze == null || this.mItem == null || !this.mItem.isDefaultStop())) {
            this.mAlarmSnooze.setVisibility(4);
        }
        TextClock currentDate = (TextClock) findViewById(C0490R.id.current_date);
        if (currentDate != null) {
            currentDate.setContentDescription(LocalDateTime.now().format(DateTimeFormatter.ofPattern(this.mContext.getResources().getString(C0490R.string.alarm_alert_date_format_for_tts))));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Feature.DEBUG_ENG) {
            Log.secD("AlarmAlertActivity", "..onKeyDown.. keyCode : " + keyCode);
        }
        switch (keyCode) {
            case 3:
            case 6:
            case 26:
            case 27:
            case 82:
                return true;
            case 4:
                if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Log.m41d("AlarmAlertActivity", "onBackPressed()");
        finishAlarm(false);
        if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
            super.onBackPressed();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager == null || packageManager.queryIntentActivities(intent, 0).size() <= 0) {
            Log.secD("AlarmAlertActivity", "Activity Not Found !");
        } else {
            startActivity(intent);
        }
    }

    private void setCoverStateManager() {
        initScover();
        if (this.mDeviceSupportCoverSDK) {
            this.mCoverManager = new ScoverManager(this);
            this.mCoverStateListener = new C04966();
            registerCoverListener();
            initCoverState();
        }
    }

    protected int getNfcTouchListenerEventType() {
        return 1;
    }

    private void showCoverAlarmByDialog() {
        if (this.mItem == null) {
            return;
        }
        if (this.mCoverViewSize == 3) {
            Log.secD("AlarmAlertActivity", "isForSmartCover");
            return;
        }
        if (this.mCover == null) {
            this.mCover = new AlarmCover(this, this.mCoverViewSize, this.mCoverManager.getCoverState(), 0);
        }
        switch (mCoverType) {
            case 1:
            case 3:
            case 6:
            case 7:
            case 8:
                setRequestedOrientation(1);
                this.mCover.setAlarmValues(this.mItem);
                if (this.mItem.mSnoozeActivate) {
                    this.mCover.displayDialog(this, 0, true);
                    Log.secD("AlarmAlertActivity", "showCoverAlarmByDialog() - isSnoozeAlarm");
                    return;
                }
                this.mCover.displayDialog(this, 0, false);
                Log.secD("AlarmAlertActivity", "showCoverAlarmByDialog() - isNormalAlarm");
                return;
            default:
                Log.secD("AlarmAlertActivity", "showCoverAlarmByDialog() - Not support cover");
                return;
        }
    }

    protected void changedPhoneState(int callState) {
        if (callState == 0) {
            if (System.currentTimeMillis() < (this.mItem.mAlarmAlertTime + (this.mItem.isFirstAlarm() ? ((60 * ((long) (AlarmItemUtil.ALARM_SNOOZE_DURATION_TABLE[1] - 1))) + 58) * 1000 : 59000)) - 2000) {
                new Handler().postDelayed(new C04977(), 2000);
            }
        }
    }

    private void callHun() {
        if (this.mItem != null) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - this.mLastTimeOfCalledHun;
            Log.secD("AlarmAlertActivity", "callHun _____diffTime = " + diffTime + "\n currentTime = " + currentTime + "\n mLastTimeOfCalledHun = " + this.mLastTimeOfCalledHun);
            if (diffTime < 0) {
                diffTime *= -1;
            }
            if (diffTime > 2000) {
                Intent alert = new Intent();
                alert.setClass(getApplicationContext(), AlarmAlertPopupService.class);
                Parcel out = Parcel.obtain();
                this.mItem.writeToParcel(out);
                out.setDataPosition(0);
                alert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
                out.recycle();
                getApplicationContext().startService(alert);
                Log.secD("AlarmAlertActivity", "startService AlarmAlertPopupService");
            }
            this.mLastTimeOfCalledHun = currentTime;
        }
    }

    private void finishAlarmAlert() {
        if (!Alarm.isStopAlarmAlert && !isChangingConfigurations() && StateUtils.isContextInDexMode(this.mContext)) {
            finishAlarm(false);
        }
    }

    protected void finishByLedCover() {
        finishAlarm(false);
    }

    protected void finishByCoverPowerKey() {
        if (this.mItem.isDefaultStop()) {
            finishAlarm(false);
        } else {
            finishAlarm(true);
        }
        ClockUtils.insertSaLog("107", "1140", "KEYCODE_COVER_POWER");
    }

    protected void finishByKey(int keyCode) {
        if (this.mItem.isDefaultStop()) {
            finishAlarm(false);
        } else {
            finishAlarm(true);
        }
        if (StateUtils.isUserUnlockedDevice(this.mContext)) {
            ClockUtils.insertSaLog("107", "1140", KeyEvent.keyCodeToString(keyCode));
        }
    }
}
