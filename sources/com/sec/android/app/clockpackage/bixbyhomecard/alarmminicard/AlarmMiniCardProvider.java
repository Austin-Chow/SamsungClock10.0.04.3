package com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import com.samsung.android.sdk.spage.card.CardContent;
import com.samsung.android.sdk.spage.card.CardContentManager;
import com.samsung.android.sdk.spage.card.CardProvider;
import com.samsung.android.sdk.spage.card.ControllerData;
import com.samsung.android.sdk.spage.card.ImageData;
import com.samsung.android.sdk.spage.card.RectData;
import com.samsung.android.sdk.spage.card.TextData;
import com.samsung.android.sdk.spage.card.event.Event;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmItemUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.bixbyhomecard.C0644R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AlarmMiniCardProvider extends CardProvider {
    private static final Uri ALARM_CONTENT_URI = Uri.parse("content://com.samsung.sec.android.clockpackage/alarm");
    private static AlarmItem mItem = null;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void queryWakeupAlarmItem(android.content.Context r11) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x008d in list [B:15:0x0063]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1603177117.run(Unknown Source)
*/
        /*
        com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard.AlarmBixbyCardUtils.setContext(r11);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard.AlarmBixbyCardUtils.setStartYourDayHour();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r3 = setSelectionForQuery();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r5 = "active DESC, alarmtime ASC, alerttime ASC, createtime DESC";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r0 = r11.getContentResolver();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = ALARM_CONTENT_URI;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r2 = 0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r4 = 0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        if (r6 == 0) goto L_0x0097;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x001a:
        r0 = r6.getCount();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        if (r0 <= 0) goto L_0x0097;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x0020:
        r0 = "AlarmMiniCardProvider";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1.<init>();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r2 = "Cursor getCount: ";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = r1.append(r2);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r2 = r6.getCount();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = r1.append(r2);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = r1.toString();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r6.moveToFirst();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r10 = 0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x0040:
        r0 = r6.getCount();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        if (r10 >= r0) goto L_0x005c;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x0046:
        r0 = com.sec.android.app.clockpackage.alarm.model.AlarmItem.createItemFromCursor(r6);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        mItem = r0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r8 = getNextAlertTime();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r0 = com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard.AlarmBixbyCardUtils.mStartYourDayStartMillis;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1));	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        if (r0 < 0) goto L_0x008e;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x0056:
        r0 = com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard.AlarmBixbyCardUtils.mStartYourDayEndMillis;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1));	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        if (r0 > 0) goto L_0x008e;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x005c:
        r6.close();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x005f:
        r0 = mItem;
        if (r0 == 0) goto L_0x008d;
    L_0x0063:
        r0 = "AlarmMiniCardProvider";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "queryWakeupAlarmItem() / id = ";
        r1 = r1.append(r2);
        r2 = mItem;
        r2 = r2.mId;
        r1 = r1.append(r2);
        r2 = "/ time = ";
        r1 = r1.append(r2);
        r2 = mItem;
        r2 = r2.mAlarmTime;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.sec.android.app.clockpackage.common.util.Log.m41d(r0, r1);
    L_0x008d:
        return;
    L_0x008e:
        r0 = 0;
        mItem = r0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r6.moveToNext();	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r10 = r10 + 1;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        goto L_0x0040;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
    L_0x0097:
        r0 = "AlarmMiniCardProvider";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = "There is no AlarmItem for query";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r0 = 0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        mItem = r0;	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        goto L_0x005f;
    L_0x00a2:
        r7 = move-exception;
        r0 = "AlarmMiniCardProvider";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r1 = "NullPointerException";	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        com.sec.android.app.clockpackage.common.util.Log.secD(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, all -> 0x00d9 }
        r0 = mItem;
        if (r0 == 0) goto L_0x008d;
    L_0x00ae:
        r0 = "AlarmMiniCardProvider";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "queryWakeupAlarmItem() / id = ";
        r1 = r1.append(r2);
        r2 = mItem;
        r2 = r2.mId;
        r1 = r1.append(r2);
        r2 = "/ time = ";
        r1 = r1.append(r2);
        r2 = mItem;
        r2 = r2.mAlarmTime;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.sec.android.app.clockpackage.common.util.Log.m41d(r0, r1);
        goto L_0x008d;
    L_0x00d9:
        r0 = move-exception;
        r1 = mItem;
        if (r1 == 0) goto L_0x0108;
    L_0x00de:
        r1 = "AlarmMiniCardProvider";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "queryWakeupAlarmItem() / id = ";
        r2 = r2.append(r4);
        r4 = mItem;
        r4 = r4.mId;
        r2 = r2.append(r4);
        r4 = "/ time = ";
        r2 = r2.append(r4);
        r4 = mItem;
        r4 = r4.mAlarmTime;
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.sec.android.app.clockpackage.common.util.Log.m41d(r1, r2);
    L_0x0108:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.bixbyhomecard.alarmminicard.AlarmMiniCardProvider.queryWakeupAlarmItem(android.content.Context):void");
    }

    protected void onEnabled(Context context, int[] cardIds) {
        Log.secD("AlarmMiniCardProvider", "onEnabled()");
    }

    protected void onDisabled(Context context, int[] cardIds) {
        Log.secD("AlarmMiniCardProvider", "onDisabled()");
    }

    protected void onUpdate(Context context, CardContentManager cardContentManager, int[] cardIds) {
        Log.m41d("AlarmMiniCardProvider", "onUpdate()");
        for (int id : cardIds) {
            switch (id) {
                case 80054:
                case 99999907:
                    Log.secD("AlarmMiniCardProvider", "onUpdate() / id = " + id);
                    showAlarmCardContentInitial(context, cardContentManager, id);
                    break;
                default:
                    break;
            }
        }
    }

    protected void onReceiveEvent(Context context, CardContentManager cardContentManager, int cardId, Event event) {
        Object obj = 1;
        Log.secD("AlarmMiniCardProvider", "onReceiveEvent() / cardId = " + cardId + " / event = " + event);
        super.onReceiveEvent(context, cardContentManager, cardId, event);
        if (event != null && event.getEventName() != null) {
            Log.m41d("AlarmMiniCardProvider", "onReceiveEvent  " + event.getEventName());
            String eventName = event.getEventName();
            Object obj2 = -1;
            switch (eventName.hashCode()) {
                case -218873817:
                    if (eventName.equals("SPAGE_ON_SWITCH_ON")) {
                        int i = 1;
                        break;
                    }
                    break;
                case 1804846087:
                    if (eventName.equals("SPAGE_ON_SWITCH_OFF")) {
                        obj2 = null;
                        break;
                    }
                    break;
            }
            switch (obj2) {
                case null:
                case 1:
                    boolean z;
                    if ("SPAGE_ON_SWITCH_ON".equals(event.getEventName())) {
                        z = true;
                    } else {
                        z = false;
                    }
                    Boolean toBeActive = Boolean.valueOf(z);
                    if (mItem != null) {
                        AlarmDataHandler.setAlarmActive(context, mItem.mId, toBeActive.booleanValue(), mItem.mActivate);
                        if (!toBeActive.booleanValue()) {
                            obj = null;
                        }
                        if (obj == null) {
                            AlarmNotificationHelper.clearNotification(context, mItem.mId);
                        }
                        int currentId = mItem.mId;
                        queryWakeupAlarmItem(context);
                        if (mItem == null || currentId != mItem.mId) {
                            cardContentManager.updateCardContent(context, getContent(context, cardId));
                            return;
                        }
                        return;
                    }
                    showAlarmCardContentInitial(context, cardContentManager, cardId);
                    return;
                default:
                    Log.m41d("AlarmMiniCardProvider", "invalid event");
                    return;
            }
        }
    }

    private static CardContent getContent(Context context, int cardId) {
        Log.m41d("AlarmMiniCardProvider", "getContent() / cardId = " + cardId);
        CardContent content = new CardContent(cardId);
        if (mItem == null) {
            content.setExtraState("NO_CONTENTS");
        } else {
            content.put("tag_data_1", 2, new ImageData().setImageUri(getDrawableUri(context, C0644R.drawable.bixby_alarm_icon).toString()));
            String alarmTime = ClockUtils.toTwoDigitString(mItem.mAlarmTime / 100) + ":" + ClockUtils.toTwoDigitString(mItem.mAlarmTime % 100);
            if (!DateFormat.is24HourFormat(context)) {
                String amPmText;
                String[] amPmTexts = new DateFormatSymbols().getAmPmStrings();
                if ((mItem.mAlarmTime / 100) % 12 != 0 && mItem.mAlarmTime / 100 > 12) {
                    alarmTime = ClockUtils.toTwoDigitString((mItem.mAlarmTime / 100) - 12) + ":" + ClockUtils.toTwoDigitString(mItem.mAlarmTime % 100);
                }
                if (mItem.mAlarmTime / 100 >= 12) {
                    amPmText = amPmTexts[1];
                } else {
                    amPmText = amPmTexts[0];
                }
                if (StateUtils.isLeftAmPm()) {
                    alarmTime = amPmText + alarmTime;
                } else {
                    alarmTime = alarmTime + amPmText;
                }
            }
            content.put("tag_data_3", new TextData().setText(alarmTime));
            content.put("tag_data_5", new TextData().setText(mItem.mAlarmName != null ? mItem.mAlarmName : "Alarm"));
            ControllerData controllerData = new ControllerData("Switch");
            controllerData.setState(mItem.mActivate == 1 ? 1 : 0);
            content.put("tag_data_6", 3, controllerData);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.alarm.activity.AlarmWidgetListActivity");
            intent.putExtra("AlarmLaunchMode", 0);
            intent.setFlags(268468224);
            content.put("tag_data_7", new RectData().setIntent(intent));
            Log.m41d("AlarmMiniCardProvider", "getContent() / id = " + mItem.mId + "/ time=" + alarmTime);
        }
        return content;
    }

    public static Uri getDrawableUri(Context context, int drawableId) {
        return Uri.parse("android.resource://" + context.getResources().getResourcePackageName(drawableId) + '/' + context.getResources().getResourceTypeName(drawableId) + '/' + context.getResources().getResourceEntryName(drawableId));
    }

    private static String setSelectionForQuery() {
        String selection;
        String strStartHour = ClockUtils.toTwoDigitStringUSLocale(AlarmBixbyCardUtils.mStartYourDayStartHour) + ClockUtils.toTwoDigitStringUSLocale(0);
        String strEndHour = ClockUtils.toTwoDigitStringUSLocale(AlarmBixbyCardUtils.mStartYourDayEndHour) + ClockUtils.toTwoDigitStringUSLocale(0);
        if (AlarmBixbyCardUtils.mStartYourDayStartHour < AlarmBixbyCardUtils.mStartYourDayEndHour) {
            selection = "alarmtime >= " + strStartHour + " AND " + "alarmtime" + " <= " + strEndHour;
        } else {
            selection = "(alarmtime >= " + strStartHour + " AND " + "alarmtime" + " <= " + "2359) OR " + "(" + "alarmtime" + " >= " + 0 + " AND " + "alarmtime" + " <= " + strEndHour + ")";
        }
        Log.secD("AlarmMiniCardProvider", "selection = " + selection);
        return selection;
    }

    private static long getNextAlertTime() {
        Calendar c = Calendar.getInstance();
        if ((c.get(11) * 100) + c.get(12) < mItem.mAlarmTime) {
            c.add(6, -1);
        }
        Log.secD("AlarmMiniCardProvider", "1 getNextAlertTime : " + c.getTime().toString());
        if (mItem.isSpecificDate() || !mItem.isOneTimeAlarm()) {
            c.add(6, AlarmItemUtil.getNextAlertDayOffset(c, mItem.mRepeatType));
        } else {
            c.add(6, 1);
        }
        Log.secD("AlarmMiniCardProvider", "2 getNextAlertTime : " + c.getTime().toString());
        c.set(11, mItem.mAlarmTime / 100);
        c.set(12, mItem.mAlarmTime % 100);
        c.set(13, 0);
        c.set(14, 0);
        Log.secD("AlarmMiniCardProvider", "3 getNextAlertTime : " + c.getTime().toString());
        return c.getTimeInMillis();
    }

    private void showAlarmCardContentInitial(Context context, CardContentManager cardContentManager, int cardId) {
        Log.secD("AlarmMiniCardProvider", "showAlarmCardContentInitial()");
        queryWakeupAlarmItem(context);
        cardContentManager.updateCardContent(context, getContent(context, cardId));
    }

    public static void forceUpdate(Context context) {
        Log.m41d("AlarmMiniCardProvider", "forceUpdate() / start - notifyCardContentChange");
        CardContentManager.getInstance().notifyCardContentChange(context, 99999907);
        Log.m41d("AlarmMiniCardProvider", "forceUpdate() / end");
    }
}
