package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Parcel;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.common.util.Log;

public class AlarmEcbmService extends Service {
    private final String TAG = "AlarmEcbmService";
    private Context mContext;
    private CountDownTimer mEcbmTimer;
    private AlarmItem mItem = new AlarmItem();

    public AlarmEcbmService() {
        Log.secD("AlarmEcbmService", "AlarmEcbmService");
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.secD("AlarmEcbmService", "onStartCommand");
        this.mContext = this;
        this.mItem = new AlarmItem();
        this.mItem.readFromIntent(intent);
        startForeground(16855040, AlarmNotificationHelper.makeEcbmNotify(this.mContext, this.mItem));
        if (intent == null) {
            Log.m41d("AlarmEcbmService", "onStartCommand stopSelf()");
            stopSelf();
            return 2;
        }
        Log.secD("AlarmEcbmService", "mItem = " + (this.mItem != null ? this.mItem.toString() : null));
        startTimer();
        return 1;
    }

    public void onDestroy() {
        Log.secD("AlarmEcbmService", "onDestroy");
        stopForeground(true);
        stopTimer();
        super.onDestroy();
    }

    private void startTimer() {
        Log.secD("AlarmEcbmService", "startTimer");
        stopTimer();
        this.mEcbmTimer = new CountDownTimer(1200000, 2000) {
            public void onTick(long millisUntilFinished) {
                Log.secD("AlarmEcbmService", "onTick");
                if (!AlarmProvider.isEcbm(AlarmEcbmService.this.mContext)) {
                    if (AlarmEcbmService.this.mItem != null && AlarmProvider.hasAlarm(AlarmEcbmService.this.mContext, AlarmEcbmService.this.mItem.mId)) {
                        Intent intent = new Intent("com.samsung.sec.android.clockpackage.alarm.EXPLICIT_ALARM_ALERT");
                        intent.setPackage("com.sec.android.app.clockpackage");
                        Parcel out = Parcel.obtain();
                        AlarmEcbmService.this.mItem.writeToParcel(out);
                        out.setDataPosition(0);
                        intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
                        intent.putExtra("skip_to_check_old_alarm", true);
                        Log.secD("AlarmEcbmService", "EXTRA_SKIP_TO_CHECK_OLD_ALARM true");
                        AlarmViewModelUtil.startAlarmService(AlarmEcbmService.this.mContext, intent);
                        out.recycle();
                    }
                    AlarmEcbmService.this.mItem = null;
                    AlarmEcbmService.this.stopSelf();
                }
            }

            public void onFinish() {
                Log.secD("AlarmEcbmService", "mEcbmTimer onFinish");
                AlarmEcbmService.this.stopSelf();
            }
        };
        if (this.mEcbmTimer != null) {
            this.mEcbmTimer.start();
        }
    }

    private void stopTimer() {
        if (this.mEcbmTimer != null) {
            this.mEcbmTimer.cancel();
            Log.secD("AlarmEcbmService", "stopTimer cancel");
            this.mEcbmTimer = null;
        }
    }
}
