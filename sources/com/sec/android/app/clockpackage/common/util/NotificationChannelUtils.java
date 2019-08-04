package com.sec.android.app.clockpackage.common.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.util.Pair;
import java.util.List;

public class NotificationChannelUtils {
    private static void createNotificationChannel(Context context, String channelId, int importantType) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager.getNotificationChannel(channelId) == null) {
            Pair<Integer, Integer> channelInfo = getChannelInfo(channelId);
            if (channelInfo != null) {
                NotificationChannel channel = new NotificationChannel(channelId, context.getString(((Integer) channelInfo.first).intValue()), importantType);
                channel.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                channel.setLockscreenVisibility(1);
                channel.setShowBadge(false);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void createAllChannels(Context context) {
        Log.m41d("NotificationChannelUtils", "createAllChannels");
        createNotificationChannel(context, "notification_channel_upcoming_alarm", 3);
        createNotificationChannel(context, "notification_channel_firing_alarm_and_timer", 3);
        createNotificationChannel(context, "notification_channel_snoozed_alarm", 3);
        createNotificationChannel(context, "notification_channel_missed_alarm", 3);
        createNotificationChannel(context, "notification_channel_timer", 3);
        createNotificationChannel(context, "notification_channel_stopwatch", 3);
        createNotificationChannel(context, "notification_channel_other", 3);
    }

    public static void updateAllChannels(Context context) {
        Log.m41d("NotificationChannelUtils", "updateAllChannels");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        List<NotificationChannel> channels = notificationManager.getNotificationChannels();
        if (channels != null) {
            for (NotificationChannel channel : channels) {
                String channelId = channel.getId();
                Log.m41d("NotificationChannelUtils", "channelId : " + channelId);
                Pair<Integer, Integer> channelInfo = getChannelInfo(channelId);
                if (channelInfo != null) {
                    channel.setName(context.getString(((Integer) channelInfo.first).intValue()));
                    if (((Integer) channelInfo.second).intValue() != -1) {
                        channel.setDescription(context.getString(((Integer) channelInfo.second).intValue()));
                    }
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.support.v4.util.Pair<java.lang.Integer, java.lang.Integer> getChannelInfo(java.lang.String r4) {
        /*
        r3 = -1;
        r1 = -1;
        r0 = -1;
        r2 = r4.hashCode();
        switch(r2) {
            case -2006389891: goto L_0x004f;
            case -838847360: goto L_0x0059;
            case -834552651: goto L_0x0045;
            case -621049346: goto L_0x001d;
            case -279680128: goto L_0x0031;
            case 1143779379: goto L_0x0027;
            case 1351117885: goto L_0x003b;
            default: goto L_0x000a;
        };
    L_0x000a:
        r2 = r3;
    L_0x000b:
        switch(r2) {
            case 0: goto L_0x0063;
            case 1: goto L_0x0067;
            case 2: goto L_0x006b;
            case 3: goto L_0x006f;
            case 4: goto L_0x0073;
            case 5: goto L_0x0077;
            case 6: goto L_0x007b;
            default: goto L_0x000e;
        };
    L_0x000e:
        if (r1 == r3) goto L_0x007f;
    L_0x0010:
        r2 = java.lang.Integer.valueOf(r1);
        r3 = java.lang.Integer.valueOf(r0);
        r2 = android.support.v4.util.Pair.create(r2, r3);
    L_0x001c:
        return r2;
    L_0x001d:
        r2 = "notification_channel_upcoming_alarm";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x0025:
        r2 = 0;
        goto L_0x000b;
    L_0x0027:
        r2 = "notification_channel_firing_alarm_and_timer";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x002f:
        r2 = 1;
        goto L_0x000b;
    L_0x0031:
        r2 = "notification_channel_snoozed_alarm";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x0039:
        r2 = 2;
        goto L_0x000b;
    L_0x003b:
        r2 = "notification_channel_missed_alarm";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x0043:
        r2 = 3;
        goto L_0x000b;
    L_0x0045:
        r2 = "notification_channel_timer";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x004d:
        r2 = 4;
        goto L_0x000b;
    L_0x004f:
        r2 = "notification_channel_stopwatch";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x0057:
        r2 = 5;
        goto L_0x000b;
    L_0x0059:
        r2 = "notification_channel_other";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x000a;
    L_0x0061:
        r2 = 6;
        goto L_0x000b;
    L_0x0063:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.notification_channel_upcoming_alarm;
        r0 = -1;
        goto L_0x000e;
    L_0x0067:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.notification_channel_firing_alarm_and_timer;
        r0 = -1;
        goto L_0x000e;
    L_0x006b:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.notification_channel_snoozed_alarm;
        r0 = -1;
        goto L_0x000e;
    L_0x006f:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.notification_channel_missed_alarm;
        r0 = -1;
        goto L_0x000e;
    L_0x0073:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.timer;
        r0 = -1;
        goto L_0x000e;
    L_0x0077:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.stopwatch;
        r0 = -1;
        goto L_0x000e;
    L_0x007b:
        r1 = com.sec.android.app.clockpackage.common.C0645R.string.notification_channel_other;
        r0 = -1;
        goto L_0x000e;
    L_0x007f:
        r2 = 0;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.common.util.NotificationChannelUtils.getChannelInfo(java.lang.String):android.support.v4.util.Pair<java.lang.Integer, java.lang.Integer>");
    }
}
