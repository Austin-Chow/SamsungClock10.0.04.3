package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.NotificationManagerCompat;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmSharedManager;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmNotificationHelper {
    private static AlarmNotificationHelper sInstance = null;
    private static ArrayList<NotificationAlarmItem> sMissedAlarm = null;
    private static int sSnoozedAlarmCount = 0;
    private static int[] sSnoozedAlarmIds = new int[50];
    private Timer mNormalTimer = null;
    private MyTimerTask mNormalTimerTask = null;

    private static class MyTimerTask extends TimerTask {
        private final Context mContext;
        private final int mId;

        private MyTimerTask(Context context) {
            this.mContext = context;
            this.mId = 268439552;
        }

        public void run() {
            AlarmNotificationHelper.clearNotification(this.mContext, this.mId);
        }
    }

    private static class NotificationAlarmItem {
        final String alarmName;
        final int alarmTime;

        NotificationAlarmItem(AlarmItem item) {
            this.alarmTime = item.mAlarmTime;
            this.alarmName = item.mAlarmName;
        }
    }

    public static AlarmNotificationHelper getInstance() {
        if (sInstance == null) {
            sInstance = new AlarmNotificationHelper();
        }
        return sInstance;
    }

    public static void clearMissedAlarm() {
        if (sMissedAlarm != null) {
            Log.secD("AlarmNotificationHelper", "clearMissedAlarm");
            sMissedAlarm.clear();
            sMissedAlarm = null;
        }
    }

    public static void clearAllSnoozedNotification(Context context) {
        Log.secD("AlarmNotificationHelper", "clearAllSnoozedNotification");
        getNotificationManager(context).cancel(268468224);
        sSnoozedAlarmCount = AlarmSharedManager.getSnoozeAlarmCount(context);
        if (sSnoozedAlarmCount > 0) {
            while (0 < AlarmSharedManager.getSnoozeAlarmCount(context)) {
                Log.secD("AlarmNotificationHelper", "count = " + AlarmSharedManager.getSnoozeAlarmCount(context));
                sSnoozedAlarmIds = AlarmSharedManager.getSnoozeAlarmIds(context);
                AlarmItem item = AlarmProvider.getAlarm(context, sSnoozedAlarmIds[0]);
                if (item != null) {
                    AlarmViewModelUtil.dismissAlarm(context, item, null);
                } else {
                    AlarmSharedManager.removeSnoozeAlarmId(context, sSnoozedAlarmIds[0]);
                }
            }
        }
    }

    public void notify(Context context, AlarmItem item) {
        String alarmName;
        int duration;
        clearNotification(context, item.mId);
        Calendar.getInstance().setTimeInMillis(item.mAlarmAlertTime);
        Intent contentIntent = new Intent();
        Parcel data = Parcel.obtain();
        item.writeToParcel(data);
        contentIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity");
        contentIntent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", data.marshall());
        contentIntent.putExtra("fromNotification", true);
        PendingIntent launch = PendingIntent.getActivity(context, item.hashCode(), contentIntent, 0);
        if (item.mAlarmName == null || item.mAlarmName.length() == 0) {
            alarmName = context.getString(C0490R.string.alarm);
        } else {
            alarmName = item.mAlarmName;
        }
        Notification n = new Builder(context, "notification_channel_firing_alarm_and_timer").setWhen(item.mAlarmAlertTime).setSmallIcon(C0490R.drawable.stat_sys_alarm).setContentTitle(alarmName).setContentIntent(launch).setContentText(context.getString(C0490R.string.alarm_notify_snooze_text, new Object[]{AlarmUtil.formatTime(context, c)})).setCategory(NotificationCompat.CATEGORY_ALARM).setGroup("ALARM_GROUP_KEY").build();
        n.flags |= 32;
        getNotificationManager(context).notify(268439552, n);
        data.recycle();
        if (this.mNormalTimerTask != null) {
            if (this.mNormalTimer != null) {
                this.mNormalTimer.cancel();
                this.mNormalTimer = null;
            }
            this.mNormalTimerTask = null;
        }
        if (item.isFirstAlarm()) {
            duration = item.getSnoozeDurationMinutes();
        } else {
            duration = 60000;
        }
        if (duration > 0) {
            this.mNormalTimerTask = new MyTimerTask(context);
            this.mNormalTimer = new Timer();
            this.mNormalTimer.schedule(this.mNormalTimerTask, (long) duration);
        }
    }

    public void cancel(Context context, int id) {
        Log.secD("AlarmNotificationHelper", "cancel notification : " + Integer.toString(id));
        if (id == 268439552) {
            Log.secD("AlarmNotificationHelper", "Delete normal timer task.");
            if (this.mNormalTimer != null) {
                Log.secD("AlarmNotificationHelper", "Delete normal timer.");
                this.mNormalTimer.cancel();
                this.mNormalTimer = null;
            }
            this.mNormalTimerTask = null;
            getNotificationManager(context).cancel(268439552);
        }
    }

    public static void clearChinaDbUpdatingNotification(Context context) {
        getNotificationManager(context).cancel(16850944);
    }

    public static void showToAddCalendarNotification(Context context) {
        Log.secD("AlarmNotificationHelper", "showToAddCalendarNotification");
        NotificationManager nm = getNotificationManager(context);
        String contentText = context.getString(C0490R.string.alarm_upsm_guide_message);
        Builder notification = new Builder(context, "notification_channel_other").setContentTitle(context.getString(C0490R.string.alarm_upsm_guide_title)).setSmallIcon(C0490R.drawable.stat_sys_alarm).setAutoCancel(true).setContentText(contentText).setStyle(new BigTextStyle().bigText(contentText)).setTicker(contentText).setContentIntent(PendingIntent.getActivity(context, 0, new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 134217728)).setCategory(NotificationCompat.CATEGORY_ALARM).setGroup("ALARM_GROUP_KEY");
        nm.cancel(16846848);
        nm.notify(16846848, notification.build());
    }

    public static void showChinaDbUpdatingNotification(Context context) {
        Log.secD("AlarmNotificationHelper", "showChinaDbUpdatingNotification");
        NotificationManager nm = getNotificationManager(context);
        String contentText = context.getString(C0490R.string.alarm_update_calendar_popup_message) + "\n" + context.getString(C0490R.string.alarm_update_calendar_go_setting);
        Builder notification = new Builder(context, "notification_channel_other").setContentTitle(context.getString(C0490R.string.alarm_update_calendar_popup_title)).setSmallIcon(C0490R.drawable.stat_sys_alarm).setWhen(0).setAutoCancel(false).setContentText(contentText).setStyle(new BigTextStyle().bigText(contentText)).addAction(0, context.getString(C0490R.string.later), PendingIntent.getActivity(context, 0, new Intent("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_CHINA_DB_NO_UPDATE"), 134217728)).addAction(0, context.getString(C0490R.string.setting), PendingIntent.getActivity(context, 0, new Intent("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_NOTIFICATION_CHINA_DB_UPDATE"), 134217728)).setContentIntent(PendingIntent.getActivity(context, 0, new Intent("com.sec.android.intent.calendar.setting"), 134217728)).setTicker(contentText).setCategory(NotificationCompat.CATEGORY_ALARM).setGroup("ALARM_GROUP_KEY");
        nm.cancel(16850944);
        nm.notify(16850944, notification.build());
    }

    public static void showUpcomingNotification(Context context, int id) {
        AlarmItem item = AlarmProvider.getAlarm(context, id);
        AlarmSharedManager.removePreDismissedAlarmId(context, id);
        if (item == null || item.mAlarmAlertTime < System.currentTimeMillis()) {
            String str;
            String str2 = "AlarmNotificationHelper";
            if (("showUpcomingNotification return " + item) == null) {
                str = "item is null";
            } else {
                str = "old alarm";
            }
            Log.m42e(str2, str);
            return;
        }
        Log.secD("AlarmNotificationHelper", "showUpcomingNotification id = " + id);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(item.mAlarmAlertTime);
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(0, new Builder(context, "notification_channel_upcoming_alarm").setSmallIcon(C0490R.drawable.stat_sys_alarm).setGroupSummary(true).setWhen(item.mAlarmAlertTime).setShowWhen(false).setSortKey(String.valueOf(Long.MAX_VALUE - item.mAlarmAlertTime)).setGroup("ALARM_UPCOMING_GROUP_KEY").build());
        boolean bCoverClosed = StateUtils.isCoverClosed(context);
        Log.secD("AlarmNotificationHelper", "showUpcomingNotification item.mAlarmAlertTime = " + AlarmItem.digitToAlphabetStr(AlarmItemUtil.getTimeString(item.mAlarmAlertTime)));
        Intent dismissIntent = new Intent();
        dismissIntent.setPackage("com.sec.android.app.clockpackage");
        dismissIntent.setAction(bCoverClosed ? "com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_DISMISS_COVERSTATE" : "com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_DISMISS");
        Parcel out = Parcel.obtain();
        try {
            item.writeToParcel(out);
            out.setDataPosition(0);
            dismissIntent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", out.marshall());
            Intent viewAlarmIntent = new Intent();
            viewAlarmIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM");
            String contextText = AlarmUtil.formatTime(context, c);
            if (item.mAlarmName != null && item.mAlarmName.length() > 0) {
                contextText = contextText + ' ' + item.mAlarmName;
            }
            Builder notification = new Builder(context, "notification_channel_upcoming_alarm").setContentTitle(context.getString(C0490R.string.alarm_notification_upcoming)).setContentText(contextText).setSmallIcon(C0490R.drawable.stat_sys_alarm).setWhen(0).setAutoCancel(false).setStyle(new BigTextStyle().setBigContentTitle(context.getString(C0490R.string.alarm_notification_upcoming))).setContentIntent(PendingIntent.getActivity(context, item.mId, viewAlarmIntent, 134217728)).setTicker(contextText).setCategory(NotificationCompat.CATEGORY_ALARM).setGroup("ALARM_UPCOMING_GROUP_KEY").setSortKey(createSortKey(c));
            int requestCode = item.mId + 0;
            if (bCoverClosed) {
                notification.addAction(0, context.getString(C0490R.string.dismiss), PendingIntent.getBroadcast(context, requestCode, dismissIntent, 134217728));
            } else {
                notification.addAction(0, context.getString(C0490R.string.dismiss), PendingIntent.getActivity(context, requestCode, dismissIntent, 134217728));
            }
            nm.notify(requestCode, notification.build());
            AlarmSharedManager.addUpcomingAlarmIds(context, item.mId);
        } finally {
            out.recycle();
        }
    }

    public static void showMissedNotification(Context context, AlarmItem item) {
        Notification notification;
        NotificationAlarmItem notificationItem = new NotificationAlarmItem(item);
        if (sMissedAlarm == null) {
            Log.secD("AlarmNotificationHelper", "sMissedAlarm = null");
            sMissedAlarm = new ArrayList();
        }
        sMissedAlarm.add(notificationItem);
        int nCount = sMissedAlarm.size();
        Log.secD("AlarmNotificationHelper", "showMissedNotification Count:" + nCount);
        Intent viewAlarmIntent = new Intent();
        viewAlarmIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM");
        Builder builder = new Builder(context, "notification_channel_missed_alarm").setSmallIcon(C0490R.drawable.stat_sys_alarm).setContentIntent(PendingIntent.getActivity(context, 0, viewAlarmIntent, 0)).setWhen(0).setCategory(NotificationCompat.CATEGORY_ALARM).setGroup("ALARM_GROUP_KEY").setNumber(sMissedAlarm.size());
        NotificationAlarmItem notificationAlarmItem = (NotificationAlarmItem) sMissedAlarm.get(nCount - 1);
        String strAlarmTime = AlarmUtil.getAlarmTimeString(context, notificationAlarmItem.alarmTime);
        Log.secD("AlarmNotificationHelper", "showMissedNotification alertTime: " + strAlarmTime);
        if (notificationAlarmItem.alarmName == null || notificationAlarmItem.alarmName.length() == 0) {
            builder.setContentText(strAlarmTime);
        } else {
            builder.setContentText(strAlarmTime + ", " + notificationAlarmItem.alarmName);
        }
        if (nCount == 1) {
            builder.setContentTitle(context.getString(C0490R.string.missed_alarm_notification));
            notification = builder.build();
        } else {
            builder.setContentTitle(context.getString(C0490R.string.missed_alarm_notifications, new Object[]{Integer.valueOf(nCount)}));
            InboxStyle inboxStyle = new InboxStyle(builder);
            for (int index = sMissedAlarm.size() - 1; index >= 0; index--) {
                notificationAlarmItem = (NotificationAlarmItem) sMissedAlarm.get(index);
                strAlarmTime = AlarmUtil.getAlarmTimeString(context, notificationAlarmItem.alarmTime);
                if (notificationAlarmItem.alarmName == null || notificationAlarmItem.alarmName.length() == 0) {
                    inboxStyle.addLine(strAlarmTime);
                } else {
                    inboxStyle.addLine(strAlarmTime + ", " + notificationAlarmItem.alarmName);
                }
            }
            notification = inboxStyle.build();
        }
        Intent deleteIntent = new Intent();
        deleteIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_CLEAR");
        notification.flags |= 16;
        notification.deleteIntent = PendingIntent.getActivity(context, 0, deleteIntent, 134217728);
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.cancel(268451840);
        notificationManager.notify(268451840, notification);
    }

    private static void remainValidSnoozedAlarmIds(Context context) {
        Log.secD("AlarmNotificationHelper", "remainValidSnoozedAlarmIds");
        sSnoozedAlarmCount = AlarmSharedManager.getSnoozeAlarmCount(context);
        if (sSnoozedAlarmCount > 0) {
            long time = System.currentTimeMillis();
            sSnoozedAlarmIds = AlarmSharedManager.getSnoozeAlarmIds(context);
            int index = sSnoozedAlarmCount - 1;
            while (index >= 0) {
                AlarmItem item = AlarmProvider.getAlarm(context, sSnoozedAlarmIds[index]);
                if ((item == null || item.mAlarmAlertTime < time || time < item.mAlarmAlertTime - ((long) item.getSnoozeDurationMinutes())) && AlarmSharedManager.removeSnoozeAlarmId(context, sSnoozedAlarmIds[index])) {
                    Log.secD("AlarmNotificationHelper", "removeSnoozeAlarmId sSnoozedAlarmIds[" + index + "] = " + sSnoozedAlarmIds[index]);
                }
                index--;
            }
        }
    }

    public static void showSnoozeNotification(Context context, AlarmItem item, int bixbyBriefWeatherConditionCode, int bixbyBriefDaytime) {
        if (item != null) {
            Log.secD("AlarmNotificationHelper", "showSnoozeNotification item.mId = " + item.mId);
            AlarmSharedManager.addSnoozedAlarmId(context, item.mId, bixbyBriefWeatherConditionCode, bixbyBriefDaytime);
        }
        remainValidSnoozedAlarmIds(context);
        sSnoozedAlarmCount = AlarmSharedManager.getSnoozeAlarmCount(context);
        Log.secD("AlarmNotificationHelper", "showSnoozeNotification Count:" + sSnoozedAlarmCount);
        if (sSnoozedAlarmCount == 0) {
            getNotificationManager(context).cancel(268468224);
            return;
        }
        sSnoozedAlarmIds = AlarmSharedManager.getSnoozeAlarmIds(context);
        boolean bCoverClosed = StateUtils.isCoverClosed(context);
        Intent dismissIntent = new Intent();
        dismissIntent.setPackage("com.sec.android.app.clockpackage");
        if (bCoverClosed) {
            dismissIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_CLEAR_SNOOZE_ALL_COVERSTATE");
        } else {
            dismissIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_NOTIFICATION_CLEAR_SNOOZE_ALL");
        }
        Intent viewAlarmIntent = new Intent();
        viewAlarmIntent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM");
        Builder builder = new Builder(context, "notification_channel_snoozed_alarm");
        builder.setSmallIcon(C0490R.drawable.stat_sys_snooze);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, viewAlarmIntent, 0));
        builder.setOngoing(true);
        builder.setWhen(0);
        builder.setAutoCancel(false);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
        builder.setGroup("ALARM_GROUP_KEY");
        AlarmItem itemReal = AlarmProvider.getAlarm(context, sSnoozedAlarmIds[0]);
        if (itemReal == null) {
            getNotificationManager(context).cancel(268468224);
            return;
        }
        Notification notification;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(itemReal.mAlarmAlertTime);
        Log.secD("AlarmNotificationHelper", "alertT : " + itemReal.mAlarmAlertTime + ' ' + c.getTime().toString());
        builder.setContentText(context.getString(C0490R.string.alarm_notification_snoozed, new Object[]{AlarmUtil.formatTime(context, c)}));
        if (sSnoozedAlarmCount == 1) {
            String snoozeContentTitle;
            if (bCoverClosed) {
                builder.addAction(0, context.getString(C0490R.string.dismiss), PendingIntent.getBroadcast(context, itemReal.mId, dismissIntent, 134217728));
            } else {
                builder.addAction(0, context.getString(C0490R.string.dismiss), PendingIntent.getActivity(context, itemReal.mId, dismissIntent, 134217728));
            }
            if (itemReal.mAlarmName == null || itemReal.mAlarmName.length() == 0 || itemReal.mAlarmName.isEmpty()) {
                snoozeContentTitle = context.getString(C0490R.string.alarm_notification_snoozed_title, new Object[]{context.getString(C0490R.string.alarm)});
            } else if (itemReal.mAlarmName.length() < 15) {
                snoozeContentTitle = context.getString(C0490R.string.alarm_notification_snoozed_title, new Object[]{itemReal.mAlarmName});
            } else {
                snoozeContentTitle = itemReal.mAlarmName;
            }
            builder.setContentTitle(snoozeContentTitle);
            notification = builder.build();
        } else {
            if (bCoverClosed) {
                builder.addAction(0, context.getString(C0490R.string.dismiss_all), PendingIntent.getBroadcast(context, itemReal.mId, dismissIntent, 134217728));
            } else {
                builder.addAction(0, context.getString(C0490R.string.dismiss_all), PendingIntent.getActivity(context, itemReal.mId, dismissIntent, 134217728));
            }
            builder.setContentTitle(context.getString(C0490R.string.snoozed_alarm_notifications, new Object[]{Integer.valueOf(sSnoozedAlarmCount)}));
            StringBuilder strTimes = new StringBuilder();
            for (int index = 0; index < sSnoozedAlarmCount; index++) {
                itemReal = AlarmProvider.getAlarm(context, sSnoozedAlarmIds[index]);
                if (itemReal != null) {
                    c.setTimeInMillis(itemReal.mAlarmAlertTime);
                    if (strTimes.toString().isEmpty()) {
                        strTimes.append(AlarmUtil.formatTime(context, c));
                    } else {
                        strTimes.append(", ").append(AlarmUtil.formatTime(context, c));
                    }
                    Log.secD("AlarmNotificationHelper", "strTimes = " + strTimes);
                }
            }
            builder.setStyle(new BigTextStyle().bigText(context.getString(C0490R.string.alarm_notification_snoozed, new Object[]{strTimes.toString()})));
            notification = builder.build();
        }
        NotificationManager nm = getNotificationManager(context);
        nm.cancel(268468224);
        nm.notify(268468224, notification);
    }

    public static void clearNotification(Context context, int id) {
        Log.secD("AlarmNotificationHelper", "clearNotification id = " + id);
        clearUpcomingNotification(context, id);
        AlarmSharedManager.removePreDismissedAlarmId(context, id);
        if (AlarmSharedManager.removeSnoozeAlarmId(context, id)) {
            showSnoozeNotification(context, null, AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
        }
    }

    public static void clearUpcomingNotification(Context context, int id) {
        Log.secD("AlarmNotificationHelper", "clearUpcomingNotification id = " + id);
        getNotificationManager(context).cancel(id);
        AlarmSharedManager.removeUpcomingAlarmId(context, id);
        updateUpcomingAlarmGroupNotification(context, id, null);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService("notification");
    }

    public static Notification makeNotify(Context context, AlarmItem item) {
        Builder builder = new Builder(context, "notification_channel_firing_alarm_and_timer");
        builder.setSmallIcon(C0490R.drawable.stat_sys_alarm);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
        builder.setGroup("ALARM_GROUP_KEY");
        if (item == null) {
            builder.setWhen(0).setAutoCancel(false);
            return builder.build();
        }
        String alarmName;
        clearNotification(context, item.mId);
        Intent contentIntent = new Intent();
        Parcel data = Parcel.obtain();
        item.writeToParcel(data);
        data.setDataPosition(0);
        contentIntent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmAlertActivity");
        contentIntent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_DATA", data.marshall());
        contentIntent.putExtra("fromNotification", true);
        PendingIntent launch = PendingIntent.getActivity(context, item.hashCode(), contentIntent, 0);
        if (item.mAlarmName == null || item.mAlarmName.length() == 0) {
            alarmName = context.getString(C0490R.string.alarm);
        } else {
            alarmName = item.mAlarmName;
        }
        builder.setWhen(item.mAlarmAlertTime);
        builder.setContentTitle(alarmName);
        builder.setContentIntent(launch);
        Calendar c = Calendar.getInstance();
        builder.setContentText(context.getString(C0490R.string.alarm_notify_snooze_text, new Object[]{AlarmUtil.formatTime(context, c)}));
        data.recycle();
        return builder.build();
    }

    public static Notification makeEcbmNotify(Context context, AlarmItem item) {
        Builder builder = new Builder(context, "notification_channel_other");
        builder.setSmallIcon(C0490R.drawable.stat_sys_alarm);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
        builder.setGroup("ALARM_GROUP_KEY");
        if (item == null) {
            builder.setWhen(0).setAutoCancel(false);
            return builder.build();
        }
        Calendar.getInstance().setTimeInMillis(item.mAlarmAlertTime);
        String message = context.getString(C0490R.string.alarm_notification_during_emergency_call_back_mode, new Object[]{AlarmUtil.formatTime(context, c)});
        builder.setWhen(0).setAutoCancel(false);
        builder.setContentTitle(context.getString(C0490R.string.alarm));
        builder.setContentText(message);
        builder.setStyle(new BigTextStyle().bigText(message));
        builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent("com.samsung.sec.android.clockpackage.alarm.ALARM_VIEWALARM"), 134217728));
        return builder.build();
    }

    private static void updateUpcomingAlarmGroupNotification(Context context, int canceledNotificationId, Notification postedNotification) {
        Log.secD("AlarmNotificationHelper", "updateUpcomingAlarmGroupNotification");
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        if (getFirstActiveNotification(context, "ALARM_UPCOMING_GROUP_KEY", canceledNotificationId, postedNotification) == null) {
            nm.cancel(0);
        }
    }

    @TargetApi(24)
    private static Notification getFirstActiveNotification(Context context, String group, int canceledNotificationId, Notification postedNotification) {
        Notification firstActiveNotification = postedNotification;
        for (StatusBarNotification statusBarNotification : ((NotificationManager) context.getSystemService("notification")).getActiveNotifications()) {
            Notification n = statusBarNotification.getNotification();
            if (!isGroupSummary(n) && group.equals(n.getGroup()) && statusBarNotification.getId() != canceledNotificationId && (firstActiveNotification == null || n.getSortKey().compareTo(firstActiveNotification.getSortKey()) < 0)) {
                firstActiveNotification = n;
                Log.secD("AlarmNotificationHelper", "getFirstActiveNotification n = " + n);
            }
        }
        return firstActiveNotification;
    }

    @TargetApi(24)
    private static boolean isGroupSummary(Notification n) {
        return (n.flags & 512) == 512;
    }

    private static String createSortKey(Calendar c) {
        String timeKey = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(c.getTime());
        Log.secD("AlarmNotificationHelper", "createSortKey timeKey = " + timeKey);
        return timeKey;
    }
}
