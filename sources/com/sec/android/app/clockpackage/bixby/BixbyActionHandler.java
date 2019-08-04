package com.sec.android.app.clockpackage.bixby;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import com.samsung.android.sdk.bixby2.action.ActionHandler;
import com.samsung.android.sdk.bixby2.action.ResponseCallback;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BixbyActionHandler extends ActionHandler {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeAction(android.content.Context r10, java.lang.String r11, android.os.Bundle r12, com.samsung.android.sdk.bixby2.action.ResponseCallback r13) {
        /*
        r9 = this;
        r5 = 3;
        r4 = 2;
        r1 = -1;
        r3 = 1;
        r2 = 0;
        r6 = "BixbyActionHandler";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "executeAction() actionName  : ";
        r7 = r7.append(r8);
        r7 = r7.append(r11);
        r7 = r7.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r6 = "params";
        r0 = r12.getSerializable(r6);
        r0 = (java.util.HashMap) r0;
        r6 = r11.hashCode();
        switch(r6) {
            case -2143389662: goto L_0x0060;
            case 568996359: goto L_0x0056;
            case 586465019: goto L_0x006a;
            case 2010866667: goto L_0x004c;
            default: goto L_0x002c;
        };
    L_0x002c:
        r6 = r1;
    L_0x002d:
        switch(r6) {
            case 0: goto L_0x0074;
            case 1: goto L_0x0078;
            case 2: goto L_0x007c;
            case 3: goto L_0x0080;
            default: goto L_0x0030;
        };
    L_0x0030:
        if (r0 == 0) goto L_0x0038;
    L_0x0032:
        r6 = r0.isEmpty();
        if (r6 == 0) goto L_0x0084;
    L_0x0038:
        r1 = "BixbyActionHandler";
        r2 = "params/type cannot be null or empty.";
        com.sec.android.app.clockpackage.common.util.Log.secE(r1, r2);
        r1 = "failure";
        r2 = "NoParameters";
        r3 = 0;
        r1 = r9.getJsonStringForCapsule(r1, r2, r3);
        r13.onComplete(r1);
    L_0x004b:
        return;
    L_0x004c:
        r6 = "SnoozeAlarm";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x002c;
    L_0x0054:
        r6 = r2;
        goto L_0x002d;
    L_0x0056:
        r6 = "DismissAlarm";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x002c;
    L_0x005e:
        r6 = r3;
        goto L_0x002d;
    L_0x0060:
        r6 = "GetTimerState";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x002c;
    L_0x0068:
        r6 = r4;
        goto L_0x002d;
    L_0x006a:
        r6 = "DismissTimer";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x002c;
    L_0x0072:
        r6 = r5;
        goto L_0x002d;
    L_0x0074:
        r9.handleSnoozeAlarmAction(r10, r13);
        goto L_0x004b;
    L_0x0078:
        r9.handleDismissAlarmAction(r10, r13);
        goto L_0x004b;
    L_0x007c:
        r9.handleGetTimerState(r11, r13);
        goto L_0x004b;
    L_0x0080:
        r9.handleDismissTimerAction(r10, r13);
        goto L_0x004b;
    L_0x0084:
        r6 = "BixbyActionHandler";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "actionName : ";
        r7 = r7.append(r8);
        r7 = r7.append(r11);
        r7 = r7.toString();
        com.sec.android.app.clockpackage.common.util.Log.secD(r6, r7);
        r6 = r11.hashCode();
        switch(r6) {
            case -2046048315: goto L_0x00bf;
            case -1118934547: goto L_0x00dd;
            case -565427926: goto L_0x00c9;
            case 109998663: goto L_0x00d3;
            case 1452252975: goto L_0x00ab;
            case 1575067515: goto L_0x00b5;
            default: goto L_0x00a3;
        };
    L_0x00a3:
        switch(r1) {
            case 0: goto L_0x00a7;
            case 1: goto L_0x00e7;
            case 2: goto L_0x00ec;
            case 3: goto L_0x00f1;
            case 4: goto L_0x00ff;
            case 5: goto L_0x0104;
            default: goto L_0x00a6;
        };
    L_0x00a6:
        goto L_0x004b;
    L_0x00a7:
        r9.handleSetAlarmAction(r10, r0, r13);
        goto L_0x004b;
    L_0x00ab:
        r4 = "SetAlarm";
        r4 = r11.equals(r4);
        if (r4 == 0) goto L_0x00a3;
    L_0x00b3:
        r1 = r2;
        goto L_0x00a3;
    L_0x00b5:
        r4 = "FindAlarms";
        r4 = r11.equals(r4);
        if (r4 == 0) goto L_0x00a3;
    L_0x00bd:
        r1 = r3;
        goto L_0x00a3;
    L_0x00bf:
        r5 = "EnableAlarms";
        r5 = r11.equals(r5);
        if (r5 == 0) goto L_0x00a3;
    L_0x00c7:
        r1 = r4;
        goto L_0x00a3;
    L_0x00c9:
        r4 = "DisableAlarms";
        r4 = r11.equals(r4);
        if (r4 == 0) goto L_0x00a3;
    L_0x00d1:
        r1 = r5;
        goto L_0x00a3;
    L_0x00d3:
        r4 = "EditAlarm";
        r4 = r11.equals(r4);
        if (r4 == 0) goto L_0x00a3;
    L_0x00db:
        r1 = 4;
        goto L_0x00a3;
    L_0x00dd:
        r4 = "DeleteAlarms";
        r4 = r11.equals(r4);
        if (r4 == 0) goto L_0x00a3;
    L_0x00e5:
        r1 = 5;
        goto L_0x00a3;
    L_0x00e7:
        r9.handleFindAlarmAction(r10, r0, r13);
        goto L_0x004b;
    L_0x00ec:
        r9.handleTurnOnOffAlarmAction(r10, r3, r0, r13);
        goto L_0x004b;
    L_0x00f1:
        r1 = com.sec.android.app.clockpackage.alarm.model.Alarm.isStopAlarmAlert;
        if (r1 != 0) goto L_0x00fa;
    L_0x00f5:
        r9.handleDismissAlarmAction(r10, r13);
        goto L_0x004b;
    L_0x00fa:
        r9.handleTurnOnOffAlarmAction(r10, r2, r0, r13);
        goto L_0x004b;
    L_0x00ff:
        r9.handleEditAlarmAction(r10, r0, r13);
        goto L_0x004b;
    L_0x0104:
        r9.handleDeleteAlarmAction(r10, r0, r13);
        goto L_0x004b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.bixby.BixbyActionHandler.executeAction(android.content.Context, java.lang.String, android.os.Bundle, com.samsung.android.sdk.bixby2.action.ResponseCallback):void");
    }

    public void executeAction(Context context, Intent intent) {
        Uri appLinkData = intent.getData();
        Log.secD("BixbyActionHandler", "Punch-out executeAction() appLinkData : " + appLinkData);
        if (appLinkData != null && appLinkData.getLastPathSegment() != null) {
            String lastPathSegment = appLinkData.getLastPathSegment();
            Object obj = -1;
            switch (lastPathSegment.hashCode()) {
                case -770751548:
                    if (lastPathSegment.equals("OpenClock")) {
                        obj = null;
                        break;
                    }
                    break;
                case -671358909:
                    if (lastPathSegment.equals("StopTimer")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 85757131:
                    if (lastPathSegment.equals("CancelTimer")) {
                        obj = 3;
                        break;
                    }
                    break;
                case 409836579:
                    if (lastPathSegment.equals("StartTimer")) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    handleOpenClockAction(context, appLinkData);
                    return;
                case 1:
                case 2:
                case 3:
                    handleStopwatchTimerAction(context, appLinkData);
                    return;
                default:
                    return;
            }
        }
    }

    private String getJsonStringForCapsule(String stateKey, String messageKey, JSONArray jsonArray) {
        JSONObject jsonResult = new JSONObject();
        Log.secD("BixbyActionHandler", "stateKey = " + stateKey + ", messageKey = " + messageKey);
        try {
            jsonResult.put("result", stateKey);
            jsonResult.put("description", messageKey);
            if (jsonArray != null) {
                Log.secD("BixbyActionHandler", "jsonArray = " + jsonArray.toString());
                jsonResult.put("alarms", jsonArray);
            }
        } catch (JSONException e) {
            Log.secE("BixbyActionHandler", "getJsonStringForCapsule() JSONException~!!");
        }
        return jsonResult.toString();
    }

    private String getJsonStringForCapsule(String stateKey, String messageKey, String alarmAlertState, JSONArray jsonArray) {
        JSONObject jsonResult = new JSONObject();
        Log.secD("BixbyActionHandler", "stateKey = " + stateKey + ", messageKey = " + messageKey);
        try {
            jsonResult.put("result", stateKey);
            jsonResult.put("description", messageKey);
            if (jsonArray != null) {
                Log.secD("BixbyActionHandler", "jsonArray = " + jsonArray.toString());
                jsonResult.put("alarms", jsonArray);
            }
            jsonResult.put("alarmAlert", alarmAlertState);
        } catch (JSONException e) {
            Log.secE("BixbyActionHandler", "getJsonStringForCapsule() JSONException~!!");
        }
        return jsonResult.toString();
    }

    private void handleOpenClockAction(Context context, Uri appLinkData) {
        Log.secD("BixbyActionHandler", "OpenClock tabID : " + appLinkData.getQueryParameter("clockType"));
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
        Intent i = new Intent("android.intent.action.MAIN");
        i.putExtra("clockpackage.select.tab", Integer.valueOf(appLinkData.getQueryParameter("clockType")));
        i.setFlags(335544320);
        i.setComponent(cn);
        context.startActivity(i);
    }

    private void handleSetAlarmAction(Context context, Map<String, List<String>> paramsMap, ResponseCallback callback) {
        String alarmAlertDate;
        String alarmTime;
        String alarmName;
        ArrayList<Integer> alarmRepeat;
        BixbyAlarmDataHandler bixbyAlarmDataHandler = new BixbyAlarmDataHandler();
        if (paramsMap.get("alarmDate") != null) {
            alarmAlertDate = (String) ((List) paramsMap.get("alarmDate")).get(0);
        } else {
            alarmAlertDate = null;
        }
        if (paramsMap.get("alarmTime") != null) {
            alarmTime = (String) ((List) paramsMap.get("alarmTime")).get(0);
        } else {
            alarmTime = null;
        }
        if (paramsMap.get("alarmName") != null) {
            alarmName = (String) ((List) paramsMap.get("alarmName")).get(0);
        } else {
            alarmName = null;
        }
        if (paramsMap.get("alarmRepeat") != null) {
            alarmRepeat = bixbyAlarmDataHandler.getRepeatTypeList((String) ((List) paramsMap.get("alarmRepeat")).get(0));
        } else {
            alarmRepeat = null;
        }
        if (alarmTime == null) {
            Log.secE("BixbyActionHandler", "mandatory param(s) missing.");
            callback.onComplete(getJsonStringForCapsule("failure", "NoMandatoryParameters", null));
            return;
        }
        String stateKey;
        String messageKey;
        int alarmHandleResult = bixbyAlarmDataHandler.createOrUpdateAlarm(context, -1, false, alarmTime, alarmAlertDate, alarmRepeat, alarmName);
        switch (alarmHandleResult) {
            case -2:
                stateKey = "failure";
                messageKey = "AlarmMaxExceed";
                break;
            case -1:
                stateKey = "failure";
                messageKey = "OtherErrors";
                break;
            case 2:
                stateKey = "failure";
                messageKey = "SameAlarmExist";
                break;
            default:
                stateKey = "success";
                messageKey = "Set Alarm Success";
                break;
        }
        if (!(Alarm.isStopAlarmAlert || alarmHandleResult == -2)) {
            bixbyAlarmDataHandler.dismissAlarmAlert(context);
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, bixbyAlarmDataHandler.getAlarmInfoJSON()));
    }

    private void handleFindAlarmAction(Context context, Map<String, List<String>> paramsMap, ResponseCallback callback) {
        String stateKey;
        String messageKey;
        BixbyAlarmDataHandler bixbyAlarmDataHandler = new BixbyAlarmDataHandler();
        String alarmName = paramsMap.get("alarmName") != null ? (String) ((List) paramsMap.get("alarmName")).get(0) : null;
        String alarmState = paramsMap.get("alarmState") != null ? (String) ((List) paramsMap.get("alarmState")).get(0) : null;
        ArrayList<Integer> repeatDays = paramsMap.get("alarmRepeat") != null ? bixbyAlarmDataHandler.getRepeatTypeList((String) ((List) paramsMap.get("alarmRepeat")).get(0)) : null;
        String[] timeStrings = new String[4];
        timeStrings[0] = paramsMap.get("alarmTimeAmbiguous") != null ? (String) ((List) paramsMap.get("alarmTimeAmbiguous")).get(0) : null;
        timeStrings[1] = paramsMap.get("alarmDate") != null ? (String) ((List) paramsMap.get("alarmDate")).get(0) : null;
        timeStrings[2] = paramsMap.get("alarmStartTime") != null ? (String) ((List) paramsMap.get("alarmStartTime")).get(0) : null;
        timeStrings[3] = paramsMap.get("alarmEndTime") != null ? (String) ((List) paramsMap.get("alarmEndTime")).get(0) : null;
        String alarmAlertKey = "false";
        switch (((Integer) bixbyAlarmDataHandler.findAlarms(context, timeStrings, repeatDays, alarmName, alarmState).get(0)).intValue()) {
            case -2000:
                stateKey = "failure";
                messageKey = "NoMatchedAlarm";
                break;
            case NotificationManagerCompat.IMPORTANCE_UNSPECIFIED /*-1000*/:
                stateKey = "failure";
                messageKey = "NoAlarmExist";
                break;
            default:
                stateKey = "success";
                messageKey = "Find Alarm Success";
                break;
        }
        if (!Alarm.isStopAlarmAlert) {
            alarmAlertKey = "true";
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, alarmAlertKey, bixbyAlarmDataHandler.getAlarmInfoJSON()));
    }

    private void handleTurnOnOffAlarmAction(Context context, boolean isAlarmEnable, Map<String, List<String>> paramsMap, ResponseCallback callback) {
        String stateKey;
        String messageKey;
        boolean isAll = "true".equalsIgnoreCase(paramsMap.get("alarmIsAll") != null ? (String) ((List) paramsMap.get("alarmIsAll")).get(0) : null);
        BixbyAlarmDataHandler bixbyAlarmDataHandler = new BixbyAlarmDataHandler();
        ArrayList<Integer> findAlarmIds = bixbyAlarmDataHandler.getAlarmIdsFromString(context, Boolean.valueOf(isAll), (String) ((List) paramsMap.get("alarmFindIds")).get(0));
        if (findAlarmIds != null) {
            bixbyAlarmDataHandler.turnOnOffAlarmByAlarmIds(context, isAlarmEnable, findAlarmIds);
            stateKey = "success";
            messageKey = isAlarmEnable ? "Turn on Alarm Success" : "Turn off Alarm Success";
        } else {
            stateKey = "failure";
            messageKey = "OtherErrors";
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, bixbyAlarmDataHandler.getAlarmInfoJSON()));
    }

    private void handleEditAlarmAction(Context context, Map<String, List<String>> paramsMap, ResponseCallback callback) {
        String findAlarmId;
        String alarmAlertDate;
        String alarmTime;
        String alarmName;
        ArrayList<Integer> alarmRepeat;
        String stateKey;
        String messageKey;
        BixbyAlarmDataHandler bixbyAlarmDataHandler = new BixbyAlarmDataHandler();
        if (paramsMap.get("alarmFindIds") != null) {
            findAlarmId = (String) ((List) paramsMap.get("alarmFindIds")).get(0);
        } else {
            findAlarmId = null;
        }
        if (paramsMap.get("alarmDate") != null) {
            alarmAlertDate = (String) ((List) paramsMap.get("alarmDate")).get(0);
        } else {
            alarmAlertDate = null;
        }
        if (paramsMap.get("alarmTime") != null) {
            alarmTime = (String) ((List) paramsMap.get("alarmTime")).get(0);
        } else {
            alarmTime = null;
        }
        if (paramsMap.get("alarmName") != null) {
            alarmName = (String) ((List) paramsMap.get("alarmName")).get(0);
        } else {
            alarmName = null;
        }
        if (paramsMap.get("alarmRepeat") != null) {
            alarmRepeat = bixbyAlarmDataHandler.getRepeatTypeList((String) ((List) paramsMap.get("alarmRepeat")).get(0));
        } else {
            alarmRepeat = null;
        }
        int alarmHandleResult = 0;
        if (ClockUtils.isValidNumberString(findAlarmId)) {
            alarmHandleResult = bixbyAlarmDataHandler.createOrUpdateAlarm(context, Integer.valueOf(findAlarmId).intValue(), false, alarmTime, alarmAlertDate, alarmRepeat, alarmName);
        }
        switch (alarmHandleResult) {
            case 1:
                stateKey = "success";
                messageKey = "Edit Alarm Success";
                break;
            case 2:
                stateKey = "failure";
                messageKey = "SameAlarmExist";
                break;
            default:
                stateKey = "failure";
                messageKey = "OtherErrors";
                break;
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, bixbyAlarmDataHandler.getAlarmInfoJSON()));
    }

    private void handleDeleteAlarmAction(Context context, Map<String, List<String>> paramsMap, ResponseCallback callback) {
        String stateKey;
        String messageKey;
        boolean isAll = "true".equalsIgnoreCase(paramsMap.get("alarmIsAll") != null ? (String) ((List) paramsMap.get("alarmIsAll")).get(0) : null);
        BixbyAlarmDataHandler bixbyAlarmDataHandler = new BixbyAlarmDataHandler();
        if (bixbyAlarmDataHandler.deleteAlarmByAlarmIds(context, bixbyAlarmDataHandler.getAlarmIdsFromString(context, Boolean.valueOf(isAll), (String) ((List) paramsMap.get("alarmFindIds")).get(0))) > 0) {
            stateKey = "success";
            messageKey = "Delete Alarm Success";
        } else {
            stateKey = "failure";
            messageKey = "OtherErrors";
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, bixbyAlarmDataHandler.getAlarmInfoJSON()));
    }

    private void handleDismissAlarmAction(Context context, ResponseCallback callback) {
        String stateKey = "success";
        String messageKey = "AlarmDismissed";
        if (new BixbyAlarmDataHandler().snoozeDismissAlarmControl(context, false) == 0) {
            stateKey = "failure";
            messageKey = "OtherErrors";
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, null));
    }

    private void handleSnoozeAlarmAction(Context context, ResponseCallback callback) {
        BixbyAlarmDataHandler bixbyAlarmDataHandler = new BixbyAlarmDataHandler();
        String stateKey = "success";
        String messageKey = "Snooze Alarm Success";
        int snoozeResult = bixbyAlarmDataHandler.snoozeDismissAlarmControl(context, true);
        if (snoozeResult == 0) {
            stateKey = "failure";
            messageKey = "NoRingingAlarm";
        } else if (snoozeResult == -1) {
            stateKey = "failure";
            messageKey = "SnoozeUnavailable";
        }
        callback.onComplete(getJsonStringForCapsule(stateKey, messageKey, bixbyAlarmDataHandler.getAlarmInfoJSON()));
    }

    private void handleStopwatchTimerAction(Context context, Uri appLinkData) {
        float second;
        String actionName = appLinkData.getLastPathSegment();
        float hour = appLinkData.getQueryParameter("hour") == null ? 0.0f : Float.valueOf(appLinkData.getQueryParameter("hour")).floatValue();
        float minute = appLinkData.getQueryParameter("minute") == null ? 0.0f : Float.valueOf(appLinkData.getQueryParameter("minute")).floatValue();
        if (appLinkData.getQueryParameter("second") == null) {
            second = 0.0f;
        } else {
            second = Float.valueOf(appLinkData.getQueryParameter("second")).floatValue();
        }
        long inputMillis = (long) (((3600000.0f * hour) + (60000.0f * minute)) + (1000.0f * second));
        String presetName = appLinkData.getQueryParameter("title");
        Log.secD("BixbyActionHandler", "handleStopwatchTimerAction() actionName : " + actionName);
        Log.secD("BixbyActionHandler", "presetName : " + presetName + " hour : " + hour + " minute : " + minute + " second : " + second + " // inputMillis : " + inputMillis);
        try {
            ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.putExtra("StopwatchTimerAction", actionName);
            if (inputMillis > 0 || presetName != null) {
                intent.putExtra("TimerPresetName", presetName);
                intent.putExtra("TimerInputTime", inputMillis);
            }
            intent.putExtra("clockpackage.select.tab", 3);
            intent.setComponent(cn);
            intent.setFlags(268468224);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.secE("BixbyActionHandler", "ActivityNotFoundException : " + e.toString());
        }
    }

    private void handleGetTimerState(String actionName, ResponseCallback callback) {
        Log.secD("BixbyActionHandler", "handleGetTimerState() actionName : " + actionName);
        String timerState = null;
        JSONObject jsonResult = new JSONObject();
        switch (TimerData.getTimerState()) {
            case 0:
            case 3:
                timerState = "Reseted";
                break;
            case 1:
                timerState = "Started";
                break;
            case 2:
                timerState = "Stopped";
                break;
        }
        if (ClockUtils.sIsTimerAlertStarted) {
            timerState = "Alerted";
        }
        try {
            jsonResult.put("timerState", timerState);
        } catch (JSONException e) {
            Log.secE("BixbyActionHandler", "handle get timerState () JSONException");
        }
        callback.onComplete(jsonResult.toString());
    }

    private void handleDismissTimerAction(Context context, ResponseCallback callback) {
        if (ClockUtils.sIsTimerAlertStarted) {
            Intent intent = new Intent();
            intent.setAction("com.sec.android.clockpackage.timer.FINISH_ALERT");
            context.sendBroadcast(intent);
            callback.onComplete(getJsonStringForCapsule("success", "DismissTimer", null));
            return;
        }
        callback.onComplete(getJsonStringForCapsule("failure", "DismissTimer", null));
    }
}
