package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.viewmodel.NotificationService;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import java.util.ArrayList;
import java.util.List;

public class TimerNotificationService extends NotificationService {
    private TimerManager mTimerManager;

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.secD("TimerNotificationService", "onStartCommand() intent : " + intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected boolean canStart() {
        this.mTimerManager = TimerManager.getInstance();
        if (this.mTimerManager == null) {
            return false;
        }
        this.mTimerManager.setContext(this.mContext);
        if (TimerData.isTimerStateResetedOrNone() || TimerData.getRemainMillis() <= 0 || this.mTimerManager.isClockPackageResumed()) {
            return false;
        }
        return true;
    }

    protected boolean handleAction(String action) {
        Log.secD("TimerNotificationService", "handleAction : " + action);
        int i = -1;
        switch (action.hashCode()) {
            case -1387410206:
                if (action.equals("com.sec.android.app.clockpackageTIMER_CANCEL")) {
                    i = 2;
                    break;
                }
                break;
            case -954112363:
                if (action.equals("com.sec.android.app.clockpackageTIMER_RESUME")) {
                    i = 0;
                    break;
                }
                break;
            case 382899822:
                if (action.equals("com.sec.android.app.clockpackageTIMER_PAUSE")) {
                    i = 1;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                this.mTimerManager.startTimer(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis(), TimerData.getOnGoingTimerName());
                this.mTimerManager.setTimerState(1);
                this.mTimerManager.updateScreen();
                return true;
            case 1:
                this.mTimerManager.stopTimer();
                this.mTimerManager.setTimerState(2);
                this.mTimerManager.updateScreen();
                return true;
            case 2:
                this.mTimerManager.cancelTimer();
                this.mTimerManager.setTimerState(3);
                this.mTimerManager.updateScreen();
                return false;
            default:
                return false;
        }
    }

    protected int getNotificationId() {
        return 2147483645;
    }

    protected void updateNotification() {
        Log.secD("TimerNotificationService", "updateNotification");
        if (this.mTimerManager != null && !TimerManager.sIsRebootSequence) {
            if (TimerManager.sCountDownTimer == null) {
                this.mTimerManager.restoreSharedPreference();
            }
            if (TimerData.getRemainMillis() > 0) {
                ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
                Intent showApp = new Intent("android.intent.action.MAIN");
                showApp.putExtra("clockpackage.select.tab", 3);
                showApp.addCategory("android.intent.category.LAUNCHER");
                showApp.setFlags(268468224);
                showApp.setComponent(cn);
                buildNotification(PendingIntent.getActivity(this.mContext, 3, showApp, 1073741824));
                notifyNotification();
            }
        }
    }

    private void buildNotification(PendingIntent pendingShowApp) {
        String firstActionTitle;
        Intent firstActionIntent;
        String secondActionTitle;
        Intent secondActionIntent;
        String timerGroupKey = "TIMER_GROUP_KEY";
        Builder notification = new Builder(this.mContext, "notification_channel_timer").setAutoCancel(false).setOngoing(true).setShowWhen(true).setContentIntent(pendingShowApp).setGroup("TIMER_GROUP_KEY").setSmallIcon(C0728R.drawable.clock_noti_ic_timer).setCategory(NotificationCompat.CATEGORY_ALARM).setColorized(true).setColor(ContextCompat.getColor(this.mContext, C0728R.color.chronometer_notification_bg_color)).setStyle(new DecoratedCustomViewStyle());
        List<Action> actions = new ArrayList(2);
        if (TimerData.getTimerState() == 1) {
            firstActionTitle = getString(C0728R.string.pause);
            firstActionIntent = new Intent(this.mContext, TimerNotificationService.class).setAction("com.sec.android.app.clockpackageTIMER_PAUSE");
            secondActionTitle = getString(C0728R.string.cancel);
            secondActionIntent = new Intent(this.mContext, TimerNotificationService.class).setAction("com.sec.android.app.clockpackageTIMER_CANCEL");
        } else {
            firstActionTitle = getString(C0728R.string.resume);
            firstActionIntent = new Intent(this.mContext, TimerNotificationService.class).setAction("com.sec.android.app.clockpackageTIMER_RESUME");
            secondActionTitle = getString(C0728R.string.cancel);
            secondActionIntent = new Intent(this.mContext, TimerNotificationService.class).setAction("com.sec.android.app.clockpackageTIMER_CANCEL");
        }
        actions.add(new Action.Builder(0, firstActionTitle, PendingIntent.getService(this.mContext, 0, firstActionIntent, 134217728)).build());
        actions.add(new Action.Builder(0, secondActionTitle, PendingIntent.getService(this.mContext, 0, secondActionIntent, 134217728)).build());
        notification.setCustomContentView(buildChronometer(TimerData.getTimerState() == 1));
        for (Action action : actions) {
            notification.addAction(action);
        }
        this.mNotification = notification.build();
    }

    private long getChronometerBase(long time, boolean running) {
        long remainMillis = time < 0 ? time : time + 1000;
        return running ? remainMillis + SystemClock.elapsedRealtime() : remainMillis;
    }

    private RemoteViews buildChronometer(boolean running) {
        RemoteViews content = getChronometerViews(true, running, getChronometerBase(TimerData.getRemainMillis(), running));
        content.setTextViewText(C0728R.id.title, getString(C0728R.string.timer));
        content.setViewVisibility(C0728R.id.state, 8);
        return content;
    }
}
