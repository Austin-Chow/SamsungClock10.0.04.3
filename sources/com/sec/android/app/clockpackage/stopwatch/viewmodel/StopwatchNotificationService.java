package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.viewmodel.NotificationService;
import com.sec.android.app.clockpackage.stopwatch.C0706R;
import com.sec.android.app.clockpackage.stopwatch.model.StopwatchData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StopwatchNotificationService extends NotificationService {
    private StopwatchManager mStopwatchManager;

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.secD("StopwatchNotificationService", "onStartCommand() intent : " + intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected boolean canStart() {
        this.mStopwatchManager = StopwatchManager.getInstance();
        if (this.mStopwatchManager == null) {
            return false;
        }
        this.mStopwatchManager.setContext(this.mContext);
        if (StopwatchData.getElapsedMillis() <= 0 || this.mStopwatchManager.isClockPackageResumed()) {
            return false;
        }
        return true;
    }

    protected boolean handleAction(String action) {
        Log.secD("StopwatchNotificationService", "handleAction : " + action);
        String locale = Locale.getDefault().getLanguage();
        Object obj = -1;
        switch (action.hashCode()) {
            case 4939151:
                if (action.equals("com.sec.android.app.clockpackageSTOPWATCH_RESET")) {
                    obj = 3;
                    break;
                }
                break;
            case 153128909:
                if (action.equals("com.sec.android.app.clockpackageSTOPWATCH_RESUME")) {
                    obj = null;
                    break;
                }
                break;
            case 277298082:
                if (action.equals("com.sec.android.app.clockpackageSTOPWATCH_STOP")) {
                    obj = 1;
                    break;
                }
                break;
            case 1255863771:
                if (action.equals("com.sec.android.app.clockpackageSTOPWATCH_LAP")) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                this.mStopwatchManager.resume();
                this.mStopwatchManager.saveSharedPreference(locale);
                return true;
            case 1:
                this.mStopwatchManager.stop();
                this.mStopwatchManager.saveSharedPreference(locale);
                return true;
            case 2:
                if (StopwatchData.getLapCount() >= 999) {
                    return true;
                }
                StopwatchData.setInputTime(StopwatchData.getElapsedMillis());
                this.mStopwatchManager.addLap();
                Log.secD("StopwatchNotificationService", "StopwatchData.getLapCount() : " + StopwatchData.getLapCount());
                this.mStopwatchManager.saveSharedPreference(locale);
                return true;
            case 3:
                this.mStopwatchManager.reset();
                return false;
            default:
                return false;
        }
    }

    protected int getNotificationId() {
        return 2147483644;
    }

    protected void updateNotification() {
        Log.secD("StopwatchNotificationService", "updateNotification");
        if (this.mStopwatchManager != null && !StopwatchManager.sIsRebootSequence && StopwatchData.getElapsedMillis() > 0) {
            ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
            Intent showApp = new Intent("android.intent.action.MAIN");
            showApp.putExtra("clockpackage.select.tab", 2);
            showApp.addCategory("android.intent.category.LAUNCHER");
            showApp.setFlags(268468224);
            showApp.setComponent(cn);
            buildNotification(PendingIntent.getActivity(this.mContext, 2, showApp, 1073741824));
            notifyNotification();
            StopwatchData.setInputTime(StopwatchData.getElapsedMillis());
            StopwatchData.setInputLapTime(StopwatchData.getElapsedMillis() - StopwatchData.getElapsedMillisBefore());
        }
    }

    private void buildNotification(PendingIntent pendingShowApp) {
        String title1;
        Intent intent1;
        String stopwatchGroupKey = "STOPWATCH_GROUP_KEY";
        Builder notification = new Builder(this.mContext, "notification_channel_stopwatch").setAutoCancel(false).setOngoing(true).setShowWhen(true).setContentIntent(pendingShowApp).setGroup("STOPWATCH_GROUP_KEY").setSmallIcon(C0706R.drawable.stat_notify_stopwatch).setColorized(true).setColor(ContextCompat.getColor(this.mContext, C0706R.color.chronometer_notification_bg_color)).setStyle(new DecoratedCustomViewStyle());
        List<Action> actions = new ArrayList(2);
        String title2 = null;
        Intent intent2 = null;
        if (StopwatchData.getStopwatchState() == 1) {
            title1 = getString("zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) ? C0706R.string.pause : C0706R.string.stop);
            intent1 = new Intent(this.mContext, StopwatchNotificationService.class).setAction("com.sec.android.app.clockpackageSTOPWATCH_STOP");
            if (StopwatchData.getLapCount() < 999) {
                title2 = getString(C0706R.string.stopwatch_lap);
                intent2 = new Intent(this.mContext, StopwatchNotificationService.class).setAction("com.sec.android.app.clockpackageSTOPWATCH_LAP");
            }
        } else {
            title1 = getString(C0706R.string.resume);
            intent1 = new Intent(this.mContext, StopwatchNotificationService.class).setAction("com.sec.android.app.clockpackageSTOPWATCH_RESUME");
            title2 = getString(C0706R.string.reset_full);
            intent2 = new Intent(this.mContext, StopwatchNotificationService.class).setAction("com.sec.android.app.clockpackageSTOPWATCH_RESET");
        }
        actions.add(new Action.Builder(0, title1, PendingIntent.getService(this.mContext, 0, intent1, 134217728)).build());
        if (intent2 != null) {
            actions.add(new Action.Builder(0, title2, PendingIntent.getService(this.mContext, 0, intent2, 134217728)).build());
        }
        notification.setCustomContentView(buildChronometer(StopwatchData.getStopwatchState() == 1, true));
        notification.setCustomBigContentView(buildChronometer(StopwatchData.getStopwatchState() == 1, false));
        for (Action action : actions) {
            notification.addAction(action);
        }
        this.mNotification = notification.build();
    }

    private String buildLapsCount() {
        String b = "";
        int laps = StopwatchData.getLapCount();
        if (StopwatchData.getStopwatchState() == 2) {
            return getString(C0706R.string.notification_paused);
        }
        if (laps >= 999) {
            return String.format(getString(C0706R.string.stopwatch_maxlap), new Object[]{Integer.valueOf(999)});
        } else if (laps > 99) {
            return getString(C0706R.string.stopwatch_lap) + String.format("%4s", new Object[]{ClockUtils.toDigitString(laps)});
        } else if (laps <= 0) {
            return b;
        } else {
            return getString(C0706R.string.stopwatch_lap) + String.format("%3s", new Object[]{ClockUtils.toDigitString(laps)});
        }
    }

    private RemoteViews buildChronometer(boolean running, boolean isCollapsed) {
        RemoteViews content;
        if (isCollapsed) {
            content = getChronometerCollapsedViews(false, running, StopwatchData.getElapsedMillis());
        } else {
            content = getChronometerViews(false, running, StopwatchData.getElapsedMillis());
        }
        content.setTextViewText(C0706R.id.title, getString(C0706R.string.stopwatch));
        String state = buildLapsCount();
        if (state == null || state.length() <= 0) {
            content.setViewVisibility(C0706R.id.state, 8);
        } else {
            content.setTextViewText(C0706R.id.state, state);
            content.setViewVisibility(C0706R.id.state, 0);
        }
        return content;
    }
}
