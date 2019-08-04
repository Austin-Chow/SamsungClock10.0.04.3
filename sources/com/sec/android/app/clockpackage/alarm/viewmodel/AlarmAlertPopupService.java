package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.Logger;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.util.AlertUtils;
import com.sec.android.app.clockpackage.commonalert.util.VRHelper.Global;
import com.sec.android.app.clockpackage.commonalert.viewmodel.HeadUpNotificationService;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAlertPopupService extends HeadUpNotificationService {
    private final boolean SNOOZED = true;
    private final boolean STOPPED = false;
    private final String TAG = "AlarmAlertPopupService";
    private RelativeLayout mAlarmAlertPopup;
    private TextView mAlarmName;
    private final OnClickListener mClickListener = new C05841();
    private ScoverManager mCoverManager;
    private StateListener mCoverStateListener;
    private TextClock mCurrentTime;
    private boolean mDialog_phoneState = false;
    private InternalReceiver mInternalReceiver;
    private boolean mIsHideByTimer = false;
    private boolean mIsPaused = false;
    private boolean mIsStopAlertByChange = false;
    private Boolean mIsTimeOut = Boolean.valueOf(false);
    private AlarmItem mItem = null;
    private final BroadcastReceiver mReceiver = new C05884();
    private final BroadcastReceiver mScreenOffReceiver = new C05863();
    private boolean mSnoozeActive;
    private Button mSnoozeBtn;
    private ImageView mWeatherIcon;
    private int mWeatherImageNumber = 115;

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$1 */
    class C05841 implements OnClickListener {

        /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$1$1 */
        class C05821 implements Runnable {
            C05821() {
            }

            public void run() {
                AlarmAlertPopupService.this.finishAlarm(false);
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$1$2 */
        class C05832 implements Runnable {
            C05832() {
            }

            public void run() {
                AlarmAlertPopupService.this.finishAlarm(true);
            }
        }

        C05841() {
        }

        public void onClick(View v) {
            int id = v.getId();
            Log.secD("AlarmAlertPopupService", "onClick(View " + v + ", mId " + id + ")...");
            if (AlarmAlertPopupService.this.mIsAnimationRunning) {
                Log.secD("AlarmAlertPopupService", "animation is running");
            } else if (id == C0490R.id.popup_dismissBtn) {
                AlarmAlertPopupService.this.animateForHide();
                new Handler().postDelayed(new C05821(), 250);
                ClockUtils.insertSaLog("108", "1053");
            } else if (id == C0490R.id.popup_snoozeBtn) {
                AlarmAlertPopupService.this.animateForHide();
                new Handler().postDelayed(new C05832(), 250);
                ClockUtils.insertSaLog("108", "1054");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$2 */
    class C05852 extends StateListener {
        C05852() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if (!state.getSwitchState()) {
                Log.secD("AlarmAlertPopupService", "mCoverStateListener cover is open -> close");
                if (AlarmAlertPopupService.this.mItem.isDefaultStop()) {
                    AlarmAlertPopupService.this.finishAlarm(false);
                } else {
                    AlarmAlertPopupService.this.finishAlarm(true);
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$3 */
    class C05863 extends BroadcastReceiver {
        C05863() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                boolean fromTimerAlert = intent.getBooleanExtra("FROM_TIMER_ALARM", false);
                boolean fromAlarmAlert = intent.getBooleanExtra("FROM_ALARM_ALERT", false);
                Log.secD("AlarmAlertPopupService", "onReceive() : action = " + action.substring(action.lastIndexOf(46)));
                boolean bHmtDocked = Global.getBoolean(AlarmAlertPopupService.this.mContext.getContentResolver(), "hmt_dock", false);
                Log.secD("AlarmAlertPopupService", "mScreenOffReceiver bHmtDocked = " + bHmtDocked);
                if (!bHmtDocked && ((action.equals("android.intent.action.SCREEN_OFF") && !StateUtils.isInCallState(AlarmAlertPopupService.this.mContext)) || action.equals("com.samsung.sec.android.clockpackage.START_CLOCKPACKAGE"))) {
                    AlarmAlertPopupService.this.startAlarmAlertFullScreen(intent, fromAlarmAlert, fromTimerAlert);
                    Logger.m47f("AlarmAlertPopupService", "call Full");
                } else if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                    AlarmAlertPopupService.this.stopSelf();
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$4 */
    class C05884 extends BroadcastReceiver {
        C05884() {
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
            r4 = "AlarmAlertPopupService";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "MyReceiver receive action : ";
            r8 = r8.append(r9);
            r8 = r8.append(r0);
            r8 = r8.toString();
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r8);
            r4 = r0.hashCode();
            switch(r4) {
                case -1439628154: goto L_0x002b;
                case -1303321789: goto L_0x0049;
                case -23421476: goto L_0x0067;
                case 183811913: goto L_0x003f;
                case 279267191: goto L_0x0053;
                case 795315170: goto L_0x005d;
                case 1730993015: goto L_0x0035;
                default: goto L_0x0026;
            };
        L_0x0026:
            r4 = r6;
        L_0x0027:
            switch(r4) {
                case 0: goto L_0x0071;
                case 1: goto L_0x009b;
                case 2: goto L_0x00a1;
                case 3: goto L_0x00a7;
                case 4: goto L_0x00b3;
                case 5: goto L_0x00f7;
                case 6: goto L_0x013d;
                default: goto L_0x002a;
            };
        L_0x002a:
            return;
        L_0x002b:
            r4 = "com.samsung.sec.android.clockpackage.alarm.ALARM_STOP";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x0033:
            r4 = r5;
            goto L_0x0027;
        L_0x0035:
            r4 = "AlarmSnooze";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x003d:
            r4 = r7;
            goto L_0x0027;
        L_0x003f:
            r4 = "AlarmStopAlert";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x0047:
            r4 = 2;
            goto L_0x0027;
        L_0x0049:
            r4 = "com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x0051:
            r4 = 3;
            goto L_0x0027;
        L_0x0053:
            r4 = "com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x005b:
            r4 = 4;
            goto L_0x0027;
        L_0x005d:
            r4 = "com.samsung.sec.android.clockpacakge.alarm.ALARM_EDIT_MESSAGE";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x0065:
            r4 = 5;
            goto L_0x0027;
        L_0x0067:
            r4 = "com.samsung.flipfolder.OPEN";
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0026;
        L_0x006f:
            r4 = 6;
            goto L_0x0027;
        L_0x0071:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.animateForHide();
            r4 = "bDismiss";
            r1 = r12.getBooleanExtra(r4, r5);
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r6 = "bisTimeOut";
            r5 = r12.getBooleanExtra(r6, r5);
            r5 = java.lang.Boolean.valueOf(r5);
            r4.mIsTimeOut = r5;
            r4 = new android.os.Handler;
            r4.<init>();
            r5 = new com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$4$1;
            r5.<init>(r1);
            r6 = 250; // 0xfa float:3.5E-43 double:1.235E-321;
            r4.postDelayed(r5, r6);
            goto L_0x002a;
        L_0x009b:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.finishAlarm(r7);
            goto L_0x002a;
        L_0x00a1:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.finishAlarm(r5);
            goto L_0x002a;
        L_0x00a7:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.mIsHideByTimer = r7;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.removeHeadUpNotification();
            goto L_0x002a;
        L_0x00b3:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4 = r4.mIsHideByTimer;
            if (r4 == 0) goto L_0x00f0;
        L_0x00bb:
            r4 = com.sec.android.app.clockpackage.alarm.model.Alarm.isStopAlarmAlert;
            if (r4 == 0) goto L_0x00c6;
        L_0x00bf:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.stopSelf();
            goto L_0x002a;
        L_0x00c6:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4 = r4.mContext;
            r4 = com.sec.android.app.clockpackage.common.util.StateUtils.needToShowAsFullScreen(r4);
            if (r4 == 0) goto L_0x00e6;
        L_0x00d2:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r5 = new android.content.Intent;
            r6 = "com.samsung.sec.android.clockpackage.START_CLOCKPACKAGE";
            r5.<init>(r6);
            r6 = "FROM_ALARM_ALERT";
            r5 = r5.putExtra(r6, r7);
            r4.sendBroadcast(r5);
            goto L_0x002a;
        L_0x00e6:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.showHeadUpNotification();
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.updateTimeDisplay();
        L_0x00f0:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.mIsHideByTimer = r5;
            goto L_0x002a;
        L_0x00f7:
            r4 = "AlarmID";
            r2 = r12.getIntExtra(r4, r6);
            r4 = "AlarmAlertPopupService";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r8 = "id = ";
            r6 = r6.append(r8);
            r6 = r6.append(r2);
            r8 = " mItem.mId = ";
            r6 = r6.append(r8);
            r8 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r8 = r8.mItem;
            r8 = r8.mId;
            r6 = r6.append(r8);
            r6 = r6.toString();
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r6);
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4 = r4.mItem;
            r4 = r4.mId;
            if (r2 != r4) goto L_0x002a;
        L_0x0131:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.mIsStopAlertByChange = r7;
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.finishAlarm(r5);
            goto L_0x002a;
        L_0x013d:
            r4 = "flipOpen";
            r3 = r12.getBooleanExtra(r4, r5);
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4 = r4.mIsPreFlipOpen;
            if (r4 == 0) goto L_0x0163;
        L_0x014b:
            if (r3 != 0) goto L_0x0163;
        L_0x014d:
            r4 = "AlarmAlertPopupService";
            r6 = "FlipFolder close";
            com.sec.android.app.clockpackage.common.util.Log.secD(r4, r6);
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4 = r4.mItem;
            r4 = r4.mSnoozeActivate;
            if (r4 == 0) goto L_0x016a;
        L_0x015e:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.finishAlarm(r7);
        L_0x0163:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.mIsPreFlipOpen = r3;
            goto L_0x002a;
        L_0x016a:
            r4 = com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.this;
            r4.finishAlarm(r5);
            goto L_0x0163;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService.4.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmAlertPopupService$5 */
    class C05895 implements OnClickListener {
        C05895() {
        }

        public void onClick(View v) {
            Log.secD("AlarmAlertPopupService", "mWeatherIcon onClick");
            AlarmAlertPopupService.this.finishAlarm(true);
            AlarmService.startCpLink(AlarmAlertPopupService.this.mContext);
        }
    }

    private class InternalReceiver extends BroadcastReceiver {
        private InternalReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("AlarmAlertPopupService", "InternalReceiver receive action : " + action);
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
                    AlarmAlertPopupService.this.setBixbyBriefingWeatherInfo();
                    return;
                default:
                    return;
            }
        }
    }

    protected void changedViewByPhoneState(int callState) {
        switch (callState) {
            case 0:
                Log.secD("AlarmAlertPopupService", "mDialog_phoneState = " + this.mDialog_phoneState);
                Log.secD("AlarmAlertPopupService", "mIsPaused = " + this.mIsPaused);
                if (this.mDialog_phoneState) {
                    this.mDialog_phoneState = false;
                    if (StateUtils.needToShowAsFullScreen(this.mContext)) {
                        sendBroadcast(new Intent("com.samsung.sec.android.clockpackage.START_CLOCKPACKAGE"));
                        return;
                    } else if (!StateUtils.isVideoCall(this.mContext)) {
                        animateForShow();
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 1:
            case 2:
                this.mDialog_phoneState = true;
                if (!StateUtils.isVideoCall(this.mContext)) {
                    animateForHide();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onCreate() {
        Log.secD("AlarmAlertPopupService", "onCreate()");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.secD("AlarmAlertPopupService", "onStartCommand()");
        if (intent == null) {
            stopSelf();
            return 2;
        } else if (this.mItem != null) {
            pressStopWithoutKilling();
            stopSelf();
            return 2;
        } else {
            this.mItem = new AlarmItem();
            this.mItem.readFromIntent(intent);
            getAlarmDataFromIntent();
            setRegisterInternalReceiver();
            setCoverStateManager();
            init();
            updateTimeDisplay();
            this.mIsPaused = false;
            this.mDialog_phoneState = false;
            setRegisterReceiver();
            setTelephonyListener();
            AlertUtils.sendAlarmStartedIntent(this.mContext, "alertAlarmID", this.mItem.mId);
            ClockUtils.insertSaLog("108");
            setBixbyBriefingWeatherInfo();
            return 1;
        }
    }

    public void onDestroy() {
        Log.secD("AlarmAlertPopupService", "onDestroy()");
        this.mIsPaused = true;
        setUnregisterReceiver();
        resetTelephonyListener();
        unregisterCoverManager();
        removeInstance();
        if (this.mAlarmAlertPopup != null) {
            this.mAlarmAlertPopup.removeAllViews();
            this.mAlarmAlertPopup = null;
        }
        super.onDestroy();
    }

    private void getAlarmDataFromIntent() {
        if (this.mItem != null) {
            this.mSnoozeActive = this.mItem.mSnoozeActivate;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            if (this.mItem.isFirstAlarm()) {
                c.add(12, this.mItem.getSnoozeDuration() - 1);
                Log.secD("AlarmAlertPopupService", "finishAlarmHM = " + ((c.get(11) * 100) + c.get(12)));
            }
        }
    }

    private void setTelephonyListener() {
        ((TelephonyManager) getSystemService("phone")).listen(this.phoneStateListener, 32);
    }

    private void resetTelephonyListener() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService("phone");
        if (TelephonyMgr != null) {
            TelephonyMgr.listen(this.phoneStateListener, 0);
        }
    }

    private void setRegisterInternalReceiver() {
        this.mInternalReceiver = new InternalReceiver();
        IntentFilter internalFilter = new IntentFilter();
        internalFilter.addAction("com.samsung.android.bixby.intent.action.REQUEST_SHOW_WEATHER_ICON");
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mInternalReceiver, internalFilter);
    }

    private void setRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP");
        filter.addAction("AlarmSnooze");
        filter.addAction("AlarmStopAlert");
        filter.addAction("com.sec.android.app.clockpackage.STOP_FLASH_NOTIFICATION");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT");
        filter.addAction("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT");
        filter.addAction("com.samsung.sec.android.clockpacakge.alarm.ALARM_EDIT_MESSAGE");
        filter.addAction("android.intent.action.TIME_SET");
        if (Feature.isFolder(this.mContext)) {
            filter.addAction("com.samsung.flipfolder.OPEN");
        }
        registerReceiver(this.mReceiver, filter);
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction("android.intent.action.SCREEN_OFF");
        filter3.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter3.addAction("com.samsung.sec.android.clockpackage.START_CLOCKPACKAGE");
        registerReceiver(this.mScreenOffReceiver, filter3);
    }

    private void setUnregisterReceiver() {
        if (this.mReceiver != null) {
            Log.secD("AlarmAlertPopupService", "setUnregisterReceiver");
            try {
                unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secE("AlarmAlertPopupService", "setUnregisterReceiver IllegalArgumentException");
            }
        }
        if (this.mScreenOffReceiver != null) {
            try {
                unregisterReceiver(this.mScreenOffReceiver);
            } catch (IllegalArgumentException e2) {
                Log.secE("AlarmAlertPopupService", "setUnregisterReceiver mScreenOffReceiver IllegalArgumentException");
            }
        }
        if (this.mInternalReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInternalReceiver);
        }
    }

    private void removeInstance() {
        Log.secD("AlarmAlertPopupService", "removeInstance()");
        if (this.mContext != null) {
            this.mContext = null;
        }
    }

    private void finishAlarm(boolean isStopBySnoozeBtn) {
        if (!Alarm.isStopAlarmAlert) {
            Logger.m47f("AlarmAlertPopupService", "finishAlarm BySnooze:" + isStopBySnoozeBtn);
            if (this.mContext == null) {
                this.mContext = this;
            }
            ClockUtils.alarmAlertTimeInCall = 0;
            AlertUtils.sendStopAlarmAlertIntent(this.mContext, this.mIsTimeOut.booleanValue());
            AlarmNotificationHelper.getInstance().cancel(this, 268439552);
            if (this.mItem != null) {
                if (this.mSnoozeActive && isStopBySnoozeBtn) {
                    AlarmNotificationHelper.showSnoozeNotification(this.mContext, this.mItem, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
                } else {
                    AlarmNotificationHelper.clearNotification(this.mContext, this.mItem.mId);
                    if (this.mIsTimeOut.booleanValue()) {
                        AlarmNotificationHelper.showMissedNotification(this.mContext, this.mItem);
                    }
                    if (!(this.mIsStopAlertByChange || this.mItem == null)) {
                        if (this.mItem.mAlarmName != null && this.mItem.isOneTimeAlarm() && this.mItem.mAlarmName.equals("Start Alarm Test")) {
                            AlarmNotificationHelper.clearNotification(this.mContext, this.mItem.mId);
                            AlarmDataHandler.deleteAlarm(this.mContext, this.mItem.mId);
                        } else {
                            ArrayList<Integer> changedAlarmIds = AlarmDataHandler.updateDismissedAlarm(this.mContext, this.mItem);
                            int size = changedAlarmIds.size();
                            Log.secD("AlarmAlertPopupService", "changedAlarmIds.size : " + size);
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
            stopSelf();
        }
    }

    public void animateForShow() {
        Log.secD("AlarmAlertPopupService", "animateForShow");
        super.animateForShow();
    }

    public void animateForHide() {
        Log.secD("AlarmAlertPopupService", "animateForHide");
        super.animateForHide();
    }

    public void animateForSlideOut(boolean toLeft) {
        if (!this.mIsAnimationRunning) {
            ClockUtils.insertSaLog("108", "1243");
        }
        super.animateForSlideOut(toLeft);
        Log.secD("AlarmAlertPopupService", "animateForSlideOut");
        if (this.mItem.isDefaultStop()) {
            finishAlarm(false);
        } else {
            finishAlarm(true);
        }
    }

    private void setFontFromOpenTheme() {
        Typeface tf = ClockUtils.getFontFromOpenTheme(this.mContext);
        if (tf == null) {
            if (Feature.isTablet(this.mContext)) {
                tf = Typeface.create("sec-roboto-light", 0);
            } else if (StateUtils.isGameModeOn()) {
                tf = Typeface.create("sec-roboto-condensed", 1);
            } else {
                tf = Typeface.create("sans-serif-light", 0);
            }
        }
        if (tf != null) {
            this.mCurrentTime.setTypeface(tf);
        }
    }

    private void init() {
        Log.secD("AlarmAlertPopupService", "onCreate orientation " + getResources().getConfiguration().orientation);
    }

    private void updateTimeDisplay() {
        TextClock currentTimeAmPm;
        boolean m24HMode = DateFormat.is24HourFormat(this);
        if (StateUtils.isLeftAmPm()) {
            currentTimeAmPm = (TextClock) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_alarm_alert_current_time_ampm_kor);
        } else {
            currentTimeAmPm = (TextClock) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_alarm_alert_current_time_ampm);
        }
        if (currentTimeAmPm != null) {
            if (m24HMode) {
                currentTimeAmPm.setVisibility(8);
            } else {
                currentTimeAmPm.setVisibility(0);
            }
        }
        if (this.mItem.isDefaultStop()) {
            if (!StateUtils.isGameModeOn()) {
                View buttonDivider = this.mAlarmAlertPopup.findViewById(C0490R.id.alarm_hun_divider);
                if (buttonDivider != null) {
                    buttonDivider.setVisibility(8);
                }
            }
            this.mSnoozeBtn.setVisibility(8);
        }
        if (this.mAlarmName == null) {
            return;
        }
        if (this.mItem == null || this.mItem.mAlarmName == null || this.mItem.mAlarmName.length() <= 0) {
            this.mAlarmName.setVisibility(8);
            return;
        }
        this.mAlarmName.setVisibility(0);
        this.mAlarmName.setText(this.mItem.mAlarmName);
    }

    protected void onCreateCustomView(ViewGroup root) {
        Log.secD("AlarmAlertPopupService", "onCreateCustomView");
        AlarmUtil.setStopAlarmAlertValue(false);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (StateUtils.isGameModeOn()) {
            this.mAlarmAlertPopup = (RelativeLayout) inflater.inflate(C0490R.layout.alarm_alert_popup_view_game, root);
            this.mAlarmName = null;
        } else {
            this.mAlarmAlertPopup = (RelativeLayout) inflater.inflate(C0490R.layout.alarm_alert_popup_view, root);
            this.mAlarmName = (TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.alarm_name_popup);
        }
        this.mCurrentTime = (TextClock) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_alarm_alert_current_time);
        Button dismissBtn = (Button) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_dismissBtn);
        dismissBtn.setOnClickListener(this.mClickListener);
        this.mSnoozeBtn = (Button) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_snoozeBtn);
        this.mSnoozeBtn.setOnClickListener(this.mClickListener);
        setFontFromOpenTheme();
        try {
            dismissBtn.semSetButtonShapeEnabled(true);
            this.mSnoozeBtn.semSetButtonShapeEnabled(true);
        } catch (NoSuchMethodError e) {
            Log.secE("AlarmAlertPopupService", "NoSuchMethodError : " + e);
        }
        this.mWeatherIcon = (ImageView) this.mAlarmAlertPopup.findViewById(C0490R.id.alarm_weather_icon);
        if (this.mWeatherImageNumber != 115) {
            setBixbyBriefingWeatherInfo();
        }
        if (StateUtils.isGameModeOn()) {
            ClockUtils.setLargeTextSize(this.mContext, new TextView[]{(TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_dismissBtn), (TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_snoozeBtn)}, 1.3f);
            return;
        }
        ClockUtils.setLargeTextSize(this.mContext, new TextView[]{(TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.alarm_title_popup), (TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.alarm_name_popup), (TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_dismissBtn), (TextView) this.mAlarmAlertPopup.findViewById(C0490R.id.popup_snoozeBtn)}, 1.3f);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.secD("AlarmAlertPopupService", "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        updateTimeDisplay();
    }

    private void pressStopWithoutKilling() {
        Log.secD("AlarmAlertPopupService", "pressStopWithoutKilling()");
        if (this.mItem != null) {
            AlarmNotificationHelper.clearNotification(this.mContext, this.mItem.mId);
        }
        finishAlarm(false);
        AlarmUtil.setStopAlarmAlertValue(true);
    }

    private void setCoverStateManager() {
        this.mCoverManager = new ScoverManager(this.mContext);
        this.mCoverStateListener = new C05852();
        this.mCoverManager.registerListener(this.mCoverStateListener);
    }

    private void startAlarmAlertFullScreen(Intent intent, boolean fromAlarmAlert, boolean fromTimerAlert) {
        if (fromTimerAlert) {
            this.mIsHideByTimer = true;
        } else if (fromAlarmAlert) {
            this.mIsHideByTimer = false;
        }
        Log.secD("AlarmAlertPopupService", "mIsHidByTimer after: " + this.mIsHideByTimer);
        if (!StateUtils.isInLockTaskMode(this.mContext) && !this.mIsHideByTimer) {
            animateForHide();
            if (this.mItem != null) {
                Intent alert = new Intent();
                alert.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity");
                Parcel out = Parcel.obtain();
                this.mItem.writeToParcel(out);
                out.setDataPosition(0);
                alert.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
                alert.putExtra("FROM_ALARM_HUN", true);
                alert.setFlags(268697600);
                this.mContext.startActivity(alert);
                out.recycle();
            }
            stopSelf();
        }
    }

    private void unregisterCoverManager() {
        if (this.mCoverManager != null) {
            this.mCoverManager.unregisterListener(this.mCoverStateListener);
            this.mCoverManager = null;
        }
    }

    private void setBixbyBriefingWeatherInfo() {
        Log.secD("AlarmAlertPopupService", "setBixbyBriefingWeatherInfo");
        if (!this.mItem.isPossibleBixbyBriefingAlarm() || AlarmService.sBixbyBriefWeatherConditionCode == 999) {
            Log.secD("AlarmAlertPopupService", "setBixbyBriefingWeatherInfo return 1");
        } else if (this.mContext == null) {
            Log.secD("AlarmAlertPopupService", "setBixbyBriefingWeatherInfo return 2");
        } else if (StateUtils.isGameModeOn()) {
            Log.secD("AlarmAlertPopupService", "setBixbyBriefingWeatherInfo return 3");
        } else {
            this.mWeatherImageNumber = AlarmWeatherUtil.getWeatherIconNumber(AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
            if (this.mWeatherImageNumber == 115) {
                Log.secD("AlarmAlertPopupService", "setBixbyBriefingWeatherInfo return 3");
            } else if (this.mWeatherIcon != null) {
                this.mWeatherIcon.setVisibility(0);
                AlarmWeatherUtil.setWeatherImg(this.mContext, this.mWeatherIcon, this.mWeatherImageNumber, AlarmService.sBixbyBriefDaytime);
                this.mWeatherIcon.setOnClickListener(new C05895());
            }
        }
    }
}
