package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.C0728R;

public class TimerAlarmNotificationHelper {
    public static Notification makeNotify(Context context, String timerName) {
        CharSequence title;
        if (StateUtils.isMirrorLinkRunning()) {
            title = context.getText(C0728R.string.timer_missed_alarm_notification);
        } else {
            title = context.getText(C0728R.string.timer);
        }
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.timer.viewmodel.TimerService");
        Intent notificationIntent = new Intent();
        notificationIntent.setComponent(cn);
        notificationIntent.setAction("com.sec.android.app.clockpackage.timer.SERVICE_START");
        notificationIntent.putExtra("com.sec.android.app.clockpackage.timer.TIMER_TIMER_NOTIFICATION_TOUCH", true);
        notificationIntent.putExtra("com.sec.android.clockpackage.timer.TIMER_NAME", timerName);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, notificationIntent, 134217728);
        Builder builder = new Builder(context, "notification_channel_firing_alarm_and_timer");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(C0728R.drawable.clock_noti_ic_timer);
        builder.setContentTitle(title);
        builder.setContentIntent(pendingIntent);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
        Notification notification = builder.build();
        notification.flags |= 32;
        notification.category = NotificationCompat.CATEGORY_ALARM;
        return notification;
    }

    public static void cancel(Context c) {
        ((NotificationManager) c.getApplicationContext().getSystemService("notification")).cancel(84637);
    }
}
