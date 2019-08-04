package com.sec.android.app.clockpackage.common.viewmodel;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;

public abstract class NotificationService extends Service {
    protected Context mContext = null;
    protected Notification mNotification = null;
    protected NotificationManagerCompat mNotificationManager = null;
    private final BroadcastReceiver mNotificationServiceReceiver = new NotificationServiceReceiver();

    private final class NotificationServiceReceiver extends BroadcastReceiver {
        private NotificationServiceReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            NotificationService.this.updateNotification();
        }
    }

    protected abstract boolean canStart();

    protected abstract int getNotificationId();

    protected abstract boolean handleAction(String str);

    protected abstract void updateNotification();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.secD("NotificationService", "onStartCommand() intent : " + intent);
        if (intent == null) {
            stopSelf();
            return 2;
        }
        this.mContext = getApplicationContext();
        if (canStart()) {
            String action = intent.getAction();
            if (action == null || handleAction(action)) {
                unregisterReceiver();
                registerReceiver();
                if (this.mNotificationManager == null) {
                    this.mNotificationManager = NotificationManagerCompat.from(this.mContext);
                }
                updateNotification();
                Log.secD("NotificationService", "mNotification : " + this.mNotification);
                if (this.mNotification == null) {
                    stopSelf();
                    return 2;
                }
                startForeground(getNotificationId(), this.mNotification);
                return 1;
            }
            stopSelf();
            return 2;
        }
        Log.secD("NotificationService", "cannot start");
        stopSelf();
        return 2;
    }

    private void unregisterReceiver() {
        Log.secD("NotificationService", "unregisterReceiver");
        try {
            unregisterReceiver(this.mNotificationServiceReceiver);
        } catch (IllegalArgumentException e) {
            Log.secW("NotificationService", "unregisterReceiver catch IllegalArgumentException and ignore it");
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.LOCALE_CHANGED");
        registerReceiver(this.mNotificationServiceReceiver, filter);
    }

    public void onDestroy() {
        Log.secD("NotificationService", "onDestroy()");
        cancelNotification();
        unregisterReceiver();
        stopForeground(true);
        this.mNotificationManager = null;
        this.mNotification = null;
        super.onDestroy();
    }

    private void cancelNotification() {
        Log.secD("NotificationService", "cancelNotification");
        if (this.mNotificationManager == null && this.mContext != null) {
            this.mNotificationManager = NotificationManagerCompat.from(this.mContext);
        }
        if (this.mNotificationManager != null) {
            this.mNotificationManager.cancel(getNotificationId());
        }
    }

    protected void notifyNotification() {
        Log.secD("NotificationService", "notifyNotification");
        if (this.mNotificationManager == null && this.mContext != null) {
            this.mNotificationManager = NotificationManagerCompat.from(this.mContext);
        }
        if (this.mNotificationManager != null && this.mNotification != null) {
            this.mNotificationManager.notify(getNotificationId(), this.mNotification);
        }
    }

    private String buildTimeRemaining(long timeLeft) {
        if (timeLeft < 0) {
            Log.secV("NotificationService", "Will not show notification for timer already expired.");
            stopSelf();
            return null;
        }
        String b = "";
        int hours = ((int) timeLeft) / 3600000;
        int tmp = (int) (timeLeft - (((long) hours) * 3600000));
        int minutes = tmp / 60000;
        int seconds = (tmp - (minutes * 60000)) / 1000;
        if (hours > 0) {
            return ClockUtils.toTwoDigitString(hours) + ':' + ClockUtils.toTwoDigitString(minutes) + ':' + ClockUtils.toTwoDigitString(seconds);
        }
        return ClockUtils.toTwoDigitString(minutes) + ':' + ClockUtils.toTwoDigitString(seconds);
    }

    private RemoteViews getChronometerViews(int layoutId, boolean isCountDown, boolean running, long base) {
        RemoteViews views = new RemoteViews(getPackageName(), layoutId);
        if (running) {
            views.setChronometerCountDown(C0645R.id.chronometer, isCountDown);
            if (!isCountDown) {
                base = SystemClock.elapsedRealtime() - base;
            }
            views.setChronometer(C0645R.id.chronometer, base, null, true);
            views.setViewVisibility(C0645R.id.chronometer, 0);
            views.setViewVisibility(C0645R.id.clock, 8);
        } else {
            views.setTextViewText(C0645R.id.clock, buildTimeRemaining(base));
            views.setViewVisibility(C0645R.id.clock, 0);
            views.setViewVisibility(C0645R.id.chronometer, 8);
        }
        return views;
    }

    protected RemoteViews getChronometerViews(boolean isCountDown, boolean running, long base) {
        return getChronometerViews(C0645R.layout.chronometer_notification_content, isCountDown, running, base);
    }

    protected RemoteViews getChronometerCollapsedViews(boolean isCountDown, boolean running, long base) {
        return getChronometerViews(C0645R.layout.chronometer_notification_content_collapsed, isCountDown, running, base);
    }
}
