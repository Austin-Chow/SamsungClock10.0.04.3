package com.sec.android.app.clockpackage.bixby;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.NotificationManagerCompat;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.AlarmRingtoneManager;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmNotificationHelper;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.BixbyConstants;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BixbyAlarmDataHandler {
    private JSONArray mAlarmDataArray;
    private final TimeZone mTimeZone = TimeZone.getTimeZone("UTC");

    public JSONArray getAlarmInfoJSON() {
        return this.mAlarmDataArray;
    }

    public synchronized int createOrUpdateAlarm(Context context, int alarmId, boolean isAmbiguousAMPM, String alarmTime, String alarmDate, ArrayList<Integer> alarmDays, String alarmName) {
        int i;
        Log.secD("BixbyAlarmDataHandler", "alarmDate = " + alarmDate + ", alarmTime = " + alarmTime + ", alarmName = " + alarmName);
        int hour = -1;
        int minute = -1;
        int result = -1;
        int changeCnt = 0;
        AlarmItem alarmItem;
        int i2;
        Calendar alarmTimeCalendar;
        boolean isSupportCelebrity;
        boolean isSupportBixbyBriefing;
        AlarmRingtoneManager alarmRingtoneManager;
        String defaultAlarmTone;
        int totalTime;
        Calendar dateCalendar;
        Calendar currentCal;
        int tempRepeatType;
        int sameAlarmId;
        int duplicationId;
        long transactionResult;
        if (alarmId != -1) {
            alarmItem = AlarmProvider.getAlarm(context, alarmId);
            if (alarmItem == null) {
                Log.secE("BixbyAlarmDataHandler", "try to edit alarm but there is no alarm for alarmId~!!");
                i2 = -1;
                i = -1;
            }
            alarmItem.mActivate = 1;
            alarmItem.mSnoozeDoneCount = 0;
            alarmItem.setCreateTime();
            if (alarmTime != null) {
                changeCnt = 0 + 1;
                alarmTimeCalendar = Calendar.getInstance(this.mTimeZone);
                alarmTimeCalendar.setTimeInMillis(Long.parseLong(alarmTime));
                hour = alarmTimeCalendar.get(11);
                minute = alarmTimeCalendar.get(12);
                isSupportCelebrity = Feature.isSupportCelebrityAlarm();
                isSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(context);
                if (alarmId == -1) {
                    alarmRingtoneManager = new AlarmRingtoneManager(context);
                    defaultAlarmTone = AlarmUtil.getAlarmDefaultSoundUriString(context, alarmRingtoneManager);
                    alarmItem.mAlarmSoundTone = alarmRingtoneManager.getRingtoneIndex(defaultAlarmTone);
                    alarmItem.mSoundUri = defaultAlarmTone;
                    alarmRingtoneManager.removeInstance();
                }
                totalTime = (hour * 60) + minute;
                if (isSupportCelebrity) {
                    alarmItem.setSoundModeNewCeleb();
                    alarmItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
                } else if (isSupportBixbyBriefing) {
                    alarmItem.setSoundModeNewBixby();
                }
                if (isAmbiguousAMPM) {
                    hour %= 12;
                }
                alarmItem.mAlarmTime = (hour * 100) + minute;
            } else if (alarmId != -1) {
                hour = alarmItem.mAlarmTime / 100;
                minute = alarmItem.mAlarmTime % 100;
            }
            Log.secD("BixbyAlarmDataHandler", "createOrUpdateAlarm() hour = " + hour + ", minute = " + minute);
            if (alarmDays == null) {
            }
            if (alarmDate == null) {
            }
            dateCalendar = Calendar.getInstance();
            if (alarmDate == null) {
                dateCalendar.setTimeInMillis(alarmItem.mAlarmAlertTime);
            } else {
                changeCnt++;
                dateCalendar.setTimeInMillis(getUtcTimeToLocal(Long.parseLong(alarmDate)));
            }
            dateCalendar.set(11, hour);
            dateCalendar.set(12, minute);
            dateCalendar.set(13, 0);
            dateCalendar.set(14, 0);
            currentCal = Calendar.getInstance();
            currentCal.set(13, 59);
            if (dateCalendar.getTimeInMillis() <= currentCal.getTimeInMillis()) {
                dateCalendar.setTimeInMillis(currentCal.getTimeInMillis());
                dateCalendar.add(6, 1);
                dateCalendar.set(11, hour);
                dateCalendar.set(12, minute);
                dateCalendar.set(13, 0);
                dateCalendar.set(14, 0);
            }
            alarmItem.mAlarmAlertTime = dateCalendar.getTimeInMillis();
            if (dateCalendar.get(6) == currentCal.get(6)) {
            }
            alarmItem.setSpecificDate(true);
            tempRepeatType = 0 & 15;
            alarmItem.mRepeatType |= (((((1 << (((7 - dateCalendar.get(7)) + 1) * 4)) & -16) | 0) >> 4) << 4) & -16;
            alarmItem.setOneTimeAlarm();
            changeCnt++;
            if (alarmName.length() <= 20) {
                alarmItem.mAlarmName = alarmName;
            } else {
                alarmItem.mAlarmName = alarmName.substring(0, 20);
            }
            if (changeCnt > 0) {
                sameAlarmId = findCheckSameAlarmId(context, alarmItem);
                if (sameAlarmId == -1) {
                    duplicationId = findDuplicationAlarmID(context, alarmItem);
                    if (duplicationId != -1) {
                        if (AlarmDataHandler.deleteAlarm(context, duplicationId)) {
                            AlarmNotificationHelper.clearNotification(context, duplicationId);
                        }
                        result = 3;
                    }
                    if (alarmId != -1) {
                        transactionResult = (long) context.getContentResolver().update(AlarmProvider.CONTENT_URI, alarmItem.getContentValues(), "_id = " + alarmItem.mId, null);
                    } else {
                        transactionResult = AlarmProvider.getId(context.getContentResolver().insert(AlarmProvider.CONTENT_URI, alarmItem.getContentValues()));
                        alarmItem.mId = (int) transactionResult;
                    }
                    if (transactionResult > 0) {
                        result = 1;
                        this.mAlarmDataArray = new JSONArray();
                        setAlarmInfoJSON(alarmItem);
                        AlarmProvider.enableNextAlert(context);
                        AlarmProvider.sendAlarmChangedIntent(context);
                    }
                } else {
                    Log.secD("BixbyAlarmDataHandler", "Exist same alarm, do not insert/update db");
                    AlarmUtil.sendAlarmDeleteModeUpdate(context);
                    result = 2;
                    this.mAlarmDataArray = new JSONArray();
                    alarmItem.mId = sameAlarmId;
                    setAlarmInfoJSON(alarmItem);
                }
            }
            i2 = result;
            i = result;
        } else if (AlarmProvider.getAlarmCount(context) >= 50) {
            Log.secD("BixbyAlarmDataHandler", "createOrUpdateAlarm() Not able to create New alarm , ALARM_COUNT_MAX");
            i = -2;
            i2 = -1;
        } else {
            alarmItem = new AlarmItem();
            alarmItem.mActivate = 1;
            alarmItem.mSnoozeDoneCount = 0;
            alarmItem.setCreateTime();
            if (alarmTime != null) {
                changeCnt = 0 + 1;
                alarmTimeCalendar = Calendar.getInstance(this.mTimeZone);
                alarmTimeCalendar.setTimeInMillis(Long.parseLong(alarmTime));
                hour = alarmTimeCalendar.get(11);
                minute = alarmTimeCalendar.get(12);
                isSupportCelebrity = Feature.isSupportCelebrityAlarm();
                isSupportBixbyBriefing = Feature.isSupportBixbyBriefingMenu(context);
                if (alarmId == -1) {
                    alarmRingtoneManager = new AlarmRingtoneManager(context);
                    defaultAlarmTone = AlarmUtil.getAlarmDefaultSoundUriString(context, alarmRingtoneManager);
                    alarmItem.mAlarmSoundTone = alarmRingtoneManager.getRingtoneIndex(defaultAlarmTone);
                    alarmItem.mSoundUri = defaultAlarmTone;
                    alarmRingtoneManager.removeInstance();
                }
                if (alarmId == -1 && (isSupportCelebrity || isSupportBixbyBriefing)) {
                    totalTime = (hour * 60) + minute;
                    if (totalTime >= 240 && totalTime <= 600) {
                        if (isSupportCelebrity) {
                            alarmItem.setSoundModeNewCeleb();
                            alarmItem.mCelebVoicePath = "android.resource://com.sec.android.app.clockpackage/raw/sca_default_v01";
                        } else if (isSupportBixbyBriefing) {
                            alarmItem.setSoundModeNewBixby();
                        }
                    }
                }
                if (isAmbiguousAMPM) {
                    hour %= 12;
                }
                alarmItem.mAlarmTime = (hour * 100) + minute;
            } else if (alarmId != -1) {
                hour = alarmItem.mAlarmTime / 100;
                minute = alarmItem.mAlarmTime % 100;
            }
            Log.secD("BixbyAlarmDataHandler", "createOrUpdateAlarm() hour = " + hour + ", minute = " + minute);
            if (alarmDays == null && alarmDays.size() > 0 && alarmDays.size() < 8) {
                Log.secD("BixbyAlarmDataHandler", "createOrUpdateAlarm() alarmDays = " + alarmDays.toString());
                changeCnt++;
                int alarmRepeat = 0;
                int alarmDaySize = alarmDays.size();
                int[] repeatDays = new int[alarmDaySize];
                for (int indexItem = 0; indexItem < alarmDaySize; indexItem++) {
                    repeatDays[indexItem] = ((Integer) alarmDays.get(indexItem)).intValue();
                    alarmRepeat |= (1 << (((7 - repeatDays[indexItem]) + 1) * 4)) & -16;
                }
                alarmItem.setSpecificDate(false);
                alarmItem.mRepeatType = alarmRepeat;
                alarmItem.setWeeklyAlarm();
                alarmItem.mAlarmAlertTime = alarmItem.mCreateTime;
                alarmItem.calculateFirstAlertTime(context);
            } else if (alarmDate == null || !alarmItem.isWeeklyAlarm()) {
                dateCalendar = Calendar.getInstance();
                if (alarmDate == null) {
                    changeCnt++;
                    dateCalendar.setTimeInMillis(getUtcTimeToLocal(Long.parseLong(alarmDate)));
                } else if (alarmId != -1 && alarmItem.mAlarmAlertTime > 0) {
                    dateCalendar.setTimeInMillis(alarmItem.mAlarmAlertTime);
                }
                dateCalendar.set(11, hour);
                dateCalendar.set(12, minute);
                dateCalendar.set(13, 0);
                dateCalendar.set(14, 0);
                currentCal = Calendar.getInstance();
                currentCal.set(13, 59);
                if (dateCalendar.getTimeInMillis() <= currentCal.getTimeInMillis()) {
                    dateCalendar.setTimeInMillis(currentCal.getTimeInMillis());
                    dateCalendar.add(6, 1);
                    dateCalendar.set(11, hour);
                    dateCalendar.set(12, minute);
                    dateCalendar.set(13, 0);
                    dateCalendar.set(14, 0);
                }
                alarmItem.mAlarmAlertTime = dateCalendar.getTimeInMillis();
                if (dateCalendar.get(6) == currentCal.get(6) || dateCalendar.get(1) != currentCal.get(1)) {
                    alarmItem.setSpecificDate(true);
                } else {
                    alarmItem.setSpecificDate(false);
                }
                tempRepeatType = 0 & 15;
                alarmItem.mRepeatType |= (((((1 << (((7 - dateCalendar.get(7)) + 1) * 4)) & -16) | 0) >> 4) << 4) & -16;
                alarmItem.setOneTimeAlarm();
            } else {
                alarmItem.mAlarmAlertTime = alarmItem.mCreateTime;
                alarmItem.calculateFirstAlertTime(context);
            }
            if (alarmName != null && alarmName.length() > 0) {
                changeCnt++;
                if (alarmName.length() <= 20) {
                    alarmItem.mAlarmName = alarmName.substring(0, 20);
                } else {
                    alarmItem.mAlarmName = alarmName;
                }
            }
            if (changeCnt > 0) {
                sameAlarmId = findCheckSameAlarmId(context, alarmItem);
                if (sameAlarmId == -1) {
                    Log.secD("BixbyAlarmDataHandler", "Exist same alarm, do not insert/update db");
                    AlarmUtil.sendAlarmDeleteModeUpdate(context);
                    result = 2;
                    this.mAlarmDataArray = new JSONArray();
                    alarmItem.mId = sameAlarmId;
                    setAlarmInfoJSON(alarmItem);
                } else {
                    duplicationId = findDuplicationAlarmID(context, alarmItem);
                    if (duplicationId != -1) {
                        if (AlarmDataHandler.deleteAlarm(context, duplicationId)) {
                            AlarmNotificationHelper.clearNotification(context, duplicationId);
                        }
                        result = 3;
                    }
                    if (alarmId != -1) {
                        transactionResult = AlarmProvider.getId(context.getContentResolver().insert(AlarmProvider.CONTENT_URI, alarmItem.getContentValues()));
                        alarmItem.mId = (int) transactionResult;
                    } else {
                        transactionResult = (long) context.getContentResolver().update(AlarmProvider.CONTENT_URI, alarmItem.getContentValues(), "_id = " + alarmItem.mId, null);
                    }
                    if (transactionResult > 0) {
                        result = 1;
                        this.mAlarmDataArray = new JSONArray();
                        setAlarmInfoJSON(alarmItem);
                        AlarmProvider.enableNextAlert(context);
                        AlarmProvider.sendAlarmChangedIntent(context);
                    }
                }
            }
            i2 = result;
            i = result;
        }
        return i;
    }

    public synchronized ArrayList<Integer> findAlarms(Context context, String[] timeStrings, ArrayList<Integer> alarmRepeatDays, String alarmName, String alarmState) {
        ArrayList<Integer> findAlarmIds;
        findAlarmIds = new ArrayList();
        String selection = "";
        ArrayList<String> selectionArgList = new ArrayList();
        String[] selectionArg = null;
        String isAmbiguousAMPM = timeStrings[0];
        String dateTime = timeStrings[1];
        String startAlarmTime = timeStrings[2];
        String endAlarmTime = timeStrings[3];
        int searchYear = -1;
        int searchDayOfYear = -1;
        Log.secD("BixbyAlarmDataHandler", "findAlarms() isAmbiguousAMPM = " + isAmbiguousAMPM + ", dateTime = " + dateTime + ", startAlarmTime = " + startAlarmTime + ", endAlarmTime = " + endAlarmTime);
        Log.secD("BixbyAlarmDataHandler", "findAlarms() alarmName = " + alarmName + ", alarmState = " + alarmState);
        if (alarmRepeatDays != null) {
            Log.secD("BixbyAlarmDataHandler", "findAlarms() repeatType =  " + alarmRepeatDays.toString());
        }
        if (AlarmProvider.getAlarmCount(context) <= 0) {
            findAlarmIds.add(0, Integer.valueOf(NotificationManagerCompat.IMPORTANCE_UNSPECIFIED));
        } else {
            int a;
            if (startAlarmTime != null) {
                Calendar tempCal = Calendar.getInstance(this.mTimeZone);
                tempCal.setTimeInMillis(Long.parseLong(startAlarmTime));
                int startAlarmHour = tempCal.get(11);
                int startAlarmMin = tempCal.get(12);
                int startAlarmHourMin = (startAlarmHour * 100) + startAlarmMin;
                Log.secD("BixbyAlarmDataHandler", "startAlarmHour = " + startAlarmHour + " , startAlarmMin = " + startAlarmMin);
                if (endAlarmTime != null && !startAlarmTime.equals(endAlarmTime)) {
                    tempCal.setTimeInMillis(Long.parseLong(endAlarmTime));
                    int endAlarmHourMin = (tempCal.get(11) * 100) + tempCal.get(12);
                    Log.secD("BixbyAlarmDataHandler", "endAlarmHourMin = " + endAlarmHourMin);
                    if (endAlarmHourMin < startAlarmHourMin) {
                        selectionArgList.add(Integer.toString(startAlarmHourMin));
                        selectionArgList.add(Integer.toString(2400));
                        selection = ("alarmtime >= ? AND alarmtime <= ?" + " OR ") + "alarmtime >= ? AND alarmtime <= ?";
                        selectionArgList.add("0");
                        selectionArgList.add(Integer.toString(endAlarmHourMin));
                    } else {
                        selection = "alarmtime >= ? AND alarmtime <= ?";
                        selectionArgList.add(Integer.toString(startAlarmHourMin));
                        selectionArgList.add(Integer.toString(endAlarmHourMin));
                    }
                } else if ("True".equalsIgnoreCase(isAmbiguousAMPM)) {
                    selection = "(alarmtime = ? OR alarmtime = ?)";
                    selectionArgList.add(Integer.toString(((startAlarmHour % 12) * 100) + startAlarmMin));
                    if (startAlarmHour % 12 == 0) {
                        selectionArgList.add(Integer.toString((startAlarmHour * 100) + startAlarmMin));
                    } else {
                        selectionArgList.add(Integer.toString(((startAlarmHour + 12) * 100) + startAlarmMin));
                    }
                } else {
                    selection = "alarmtime = ?";
                    selectionArgList.add(Integer.toString(startAlarmHourMin));
                }
            }
            if (alarmRepeatDays != null && !alarmRepeatDays.isEmpty()) {
                selection = (selection + (selection.isEmpty() ? "" : " AND ")) + "repeattype = ?";
                selectionArgList.add(String.valueOf(convertDayOfWeekToRepeatType(alarmRepeatDays)));
            } else if (dateTime != null) {
                Calendar dateCalendar = Calendar.getInstance(this.mTimeZone);
                dateCalendar.setTimeInMillis(Long.parseLong(dateTime));
                searchYear = dateCalendar.get(1);
                searchDayOfYear = dateCalendar.get(6);
            }
            if (!(alarmState == null || alarmState.isEmpty())) {
                if (selection.isEmpty() && "0".equals(alarmState)) {
                    selection = null;
                } else if (!"0".equals(alarmState)) {
                    selection = (selection + (selection.isEmpty() ? "" : " AND ")) + "active = ?";
                    if ("1".equals(alarmState)) {
                        selectionArgList.add(alarmState);
                    } else {
                        selectionArgList.add("0");
                    }
                }
            }
            if (selection != null) {
                selectionArg = (String[]) selectionArgList.toArray(new String[selectionArgList.size()]);
                Log.secD("BixbyAlarmDataHandler", "findAlarms() , selection " + selection);
                for (a = 0; a < selectionArg.length; a++) {
                    Log.secD("BixbyAlarmDataHandler", "findAlarms() , selectionArg[" + a + "] = " + selectionArg[a]);
                }
            }
            Cursor alarmCursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, selection, selectionArg, "alarmtime ASC , alerttime ASC");
            if (alarmCursor != null) {
                while (alarmCursor.moveToNext()) {
                    boolean bMachingName = true;
                    if (searchYear == -1 || searchDayOfYear == -1) {
                        if (!(alarmName == null || alarmName.isEmpty())) {
                            if (!ClockUtils.isStringContainAndStartMatchWithoutSpaceForBixby(alarmName, alarmCursor.getString(20))) {
                                bMachingName = false;
                            }
                        }
                        if (bMachingName) {
                            findAlarmIds.add(Integer.valueOf(alarmCursor.getInt(0)));
                        }
                    } else {
                        AlarmItem alarmItem = AlarmProvider.getAlarm(context, alarmCursor.getInt(0));
                        if (!(alarmItem == null || alarmItem.isWeeklyAlarm())) {
                            Calendar calendar = Calendar.getInstance();
                            int curHour = calendar.get(11);
                            int curMinute = calendar.get(12);
                            if (calendar.getTimeInMillis() < alarmItem.mAlarmAlertTime) {
                                calendar.setTimeInMillis(alarmItem.mAlarmAlertTime);
                            } else if ((curHour * 100) + curMinute >= alarmItem.mAlarmTime) {
                                calendar.add(6, 1);
                            }
                            int itemYear = calendar.get(1);
                            int itemDayOfYear = calendar.get(6);
                            if (!(alarmName == null || alarmName.isEmpty())) {
                                if (!ClockUtils.isStringContainAndStartMatchWithoutSpaceForBixby(alarmName, alarmItem.mAlarmName)) {
                                    bMachingName = false;
                                }
                            }
                            if (searchDayOfYear == itemDayOfYear && searchYear == itemYear && bMachingName) {
                                findAlarmIds.add(Integer.valueOf(alarmCursor.getInt(0)));
                            }
                        }
                    }
                }
                alarmCursor.close();
            }
            if (findAlarmIds.size() == 0) {
                findAlarmIds.add(Integer.valueOf(-2000));
            } else {
                int findSize = findAlarmIds.size();
                this.mAlarmDataArray = new JSONArray();
                for (a = 0; a < findSize; a++) {
                    setAlarmInfoJSON(AlarmProvider.getAlarm(context, ((Integer) findAlarmIds.get(a)).intValue()));
                }
            }
        }
        return findAlarmIds;
    }

    public ArrayList<Integer> getRepeatTypeList(String repeatType) {
        ArrayList<Integer> findRepeatIndex = new ArrayList();
        for (String repeatIndex : repeatType.replaceAll("\\p{Z}", "").split(",")) {
            findRepeatIndex.add(Integer.valueOf(Integer.parseInt(repeatIndex)));
        }
        return findRepeatIndex;
    }

    private synchronized ArrayList<Integer> getWholeAlarmIds(Context context) {
        ArrayList<Integer> findAlarmIds;
        findAlarmIds = new ArrayList();
        Cursor tempCursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, null, null, "alarmtime ASC , alerttime ASC");
        if (tempCursor != null) {
            while (tempCursor.moveToNext()) {
                findAlarmIds.add(Integer.valueOf(tempCursor.getInt(0)));
            }
            tempCursor.close();
        }
        return findAlarmIds;
    }

    public ArrayList<Integer> getAlarmIdsFromString(Context context, Boolean isAll, String inputStrings) {
        ArrayList<Integer> findAlarmIds;
        Log.secD("BixbyAlarmDataHandler", "getAlarmIdsFromString() inputStrings = " + inputStrings);
        if (isAll.booleanValue()) {
            findAlarmIds = getWholeAlarmIds(context);
        } else {
            findAlarmIds = new ArrayList();
            String[] array = inputStrings.replaceAll("\\p{Z}", "").split(",");
            int length = array.length;
            int i = 0;
            while (i < length) {
                String inputId = array[i];
                try {
                    findAlarmIds.add(Integer.valueOf(Integer.parseInt(inputId)));
                    i++;
                } catch (NumberFormatException e) {
                    Log.secE("BixbyAlarmDataHandler", "getAlarmIdsFromString() NumberFormatException!! inputId = " + inputId);
                    ArrayList<Integer> arrayList = findAlarmIds;
                    return null;
                }
            }
        }
        return findAlarmIds;
    }

    public synchronized int turnOnOffAlarmByAlarmIds(Context context, boolean isAlarmTurnOn, ArrayList<Integer> findAlarmIds) {
        int result;
        Log.secD("BixbyAlarmDataHandler", "========turnOnOffAlarmByAlarmIds()========= isAlarmTurnOn = " + isAlarmTurnOn);
        result = 0;
        int findSize = findAlarmIds.size();
        this.mAlarmDataArray = new JSONArray();
        for (int a = 0; a < findSize; a++) {
            AlarmItem alarmItem = AlarmProvider.getAlarm(context, ((Integer) findAlarmIds.get(a)).intValue());
            if (alarmItem != null && ((isAlarmTurnOn && alarmItem.mActivate == 0) || (!isAlarmTurnOn && alarmItem.mActivate != 0))) {
                if (isAlarmTurnOn) {
                    alarmItem.mActivate = 1;
                    if (alarmItem.isSpecificDate()) {
                        alarmItem.mSnoozeDoneCount = 0;
                        if (System.currentTimeMillis() > alarmItem.mAlarmAlertTime) {
                            alarmItem.setSpecificDate(false);
                        }
                    }
                    if (!alarmItem.isSpecificDate()) {
                        if (alarmItem.isOneTimeAlarm()) {
                            alarmItem.mSnoozeDoneCount = 0;
                            Calendar c = Calendar.getInstance();
                            int curHour = c.get(11);
                            int curMinute = c.get(12);
                            int curDay = c.get(7);
                            if ((curHour * 100) + curMinute >= alarmItem.mAlarmTime) {
                                c.add(6, 1);
                                curDay = c.get(7);
                            }
                            alarmItem.mRepeatType &= 15;
                            alarmItem.mRepeatType |= (1 << (((7 - curDay) + 1) * 4)) & -16;
                            alarmItem.mRepeatType = (0 | ((alarmItem.getAlarmRepeat() << 4) & -16)) | 1;
                            Log.secD("BixbyAlarmDataHandler", "alarmItem.mRepeatType = 0x" + Integer.toHexString(alarmItem.mRepeatType));
                        } else {
                            alarmItem.mSnoozeDoneCount = 0;
                        }
                        long oldCreationTime = alarmItem.getCreateTime();
                        alarmItem.setCreateTime();
                        alarmItem.calculateOriginalAlertTime();
                        alarmItem.calculateFirstAlertTime(context);
                        alarmItem.setCreateTime(oldCreationTime);
                    }
                } else {
                    if (alarmItem.mActivate != 2) {
                        alarmItem.mActivate = 0;
                    } else if (!alarmItem.isOneTimeAlarm()) {
                        alarmItem.mActivate = 0;
                        alarmItem.mSnoozeDoneCount = 0;
                        alarmItem.calculateOriginalAlertTime();
                        alarmItem.calculateFirstAlertTime(context);
                    } else if (alarmItem.getAlertDayCount() == 1) {
                        alarmItem.mActivate = 0;
                        alarmItem.mSnoozeDoneCount = 0;
                        alarmItem.mAlarmAlertTime = -1;
                        if (alarmItem.isSpecificDate()) {
                            alarmItem.setSpecificDate(false);
                        }
                    } else {
                        alarmItem.clearRepeatDay(Calendar.getInstance());
                        alarmItem.calculateOriginalAlertTime();
                        alarmItem.mActivate = 0;
                        alarmItem.calculateFirstAlertTime(context);
                        alarmItem.mSnoozeDoneCount = 0;
                    }
                    AlarmNotificationHelper.clearNotification(context, alarmItem.mId);
                }
                int rowUpdateCnt = context.getContentResolver().update(AlarmProvider.CONTENT_URI, alarmItem.getContentValues(), "_id = " + alarmItem.mId, null);
                Log.secD("BixbyAlarmDataHandler", "enableAlarms() rowUpdateCnt = " + rowUpdateCnt);
                if (rowUpdateCnt == 1) {
                    result++;
                    setAlarmInfoJSON(alarmItem);
                }
            } else if (alarmItem != null) {
                setAlarmInfoJSON(alarmItem);
            }
        }
        if (result != 0) {
            AlarmProvider.enableNextAlert(context);
            AlarmProvider.sendAlarmChangedIntent(context);
        }
        return result;
    }

    public synchronized int deleteAlarmByAlarmIds(Context context, ArrayList<Integer> findAlarmIds) {
        int deletedAlarmCount;
        Log.secD("BixbyAlarmDataHandler", "========deleteAlarmByAlarmIds()=========");
        int findSize = findAlarmIds.size();
        this.mAlarmDataArray = new JSONArray();
        String selection = "";
        ArrayList<String> selectionArgsList = new ArrayList();
        for (int a = 0; a < findSize; a++) {
            selection = selection + (selection.isEmpty() ? "_id = ?" : " OR _id = ?");
            int alarmId = ((Integer) findAlarmIds.get(a)).intValue();
            selectionArgsList.add(String.valueOf(alarmId));
            AlarmNotificationHelper.clearNotification(context, alarmId);
        }
        deletedAlarmCount = context.getContentResolver().delete(AlarmProvider.CONTENT_URI, selection, (String[]) selectionArgsList.toArray(new String[selectionArgsList.size()]));
        if (deletedAlarmCount > 0) {
            AlarmProvider.enableNextAlert(context);
            AlarmProvider.sendAlarmChangedIntent(context);
        }
        Log.secD("BixbyAlarmDataHandler", "Delete result : " + deletedAlarmCount);
        return deletedAlarmCount;
    }

    public void dismissAlarmAlert(Context context) {
        Intent mAlarmAlert = new Intent();
        mAlarmAlert.setAction("AlarmStopAlert");
        context.sendBroadcast(mAlarmAlert);
    }

    public int snoozeDismissAlarmControl(Context context, boolean isSnooze) {
        int result = 0;
        this.mAlarmDataArray = null;
        if (AlarmService.sAlarmAlertId != -1) {
            AlarmItem alarmItem = AlarmProvider.getAlarm(context, AlarmService.sAlarmAlertId);
            if (alarmItem != null) {
                Intent intent = new Intent();
                intent.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP");
                if (alarmItem.mSnoozeActivate && isSnooze) {
                    intent.putExtra("bDismiss", false);
                    result = 1;
                } else {
                    if (isSnooze) {
                        result = -1;
                    } else {
                        result = 1;
                    }
                    intent.putExtra("bDismiss", true);
                }
                context.sendBroadcast(intent);
                this.mAlarmDataArray = new JSONArray();
                setAlarmInfoJSON(alarmItem);
            }
        }
        return result;
    }

    private long getLocalTimeToUtc(long alarmAlertTime) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTimeInMillis(alarmAlertTime);
        return tempCal.getTimeInMillis() + ((long) tempCal.getTimeZone().getOffset(alarmAlertTime));
    }

    private long getUtcTimeToLocal(long alarmAlertTime) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTimeInMillis(alarmAlertTime);
        return tempCal.getTimeInMillis() - ((long) tempCal.getTimeZone().getOffset(alarmAlertTime));
    }

    private int findCheckSameAlarmId(Context context, AlarmItem alarmItem) {
        int i;
        Cursor alarmCursor;
        int sameAlarmItemId = -1;
        StringBuilder append = new StringBuilder().append("_id != ").append(alarmItem.mId).append(" AND ").append("active").append(" = ").append(1).append(" AND ").append("alarmtime").append(" = ").append(alarmItem.mAlarmTime).append(" AND ").append("alerttime").append(" = ").append(alarmItem.mAlarmAlertTime).append(" AND ").append("repeattype").append(" = ").append(alarmItem.mRepeatType).append(" AND ").append("snzactive").append(" = ");
        if (alarmItem.mSnoozeActivate) {
            i = 1;
        } else {
            i = 0;
        }
        String selectionClause = append.append(i).append(" AND ").append("snzduration").append(" = ").append(alarmItem.mSnoozeDuration).append(" AND ").append("snzrepeat").append(" = ").append(alarmItem.mSnoozeRepeat).append(" AND ").append("dailybrief").append(" = ").append(alarmItem.mDailyBriefing).append(" AND ").append("alarmsound").append(" = ").append(alarmItem.mAlarmSoundType).append(" AND ").append("alarmtone").append(" = ").append(alarmItem.mAlarmSoundTone).append(" AND ").append("volume").append(" = ").append(alarmItem.mAlarmVolume).append(" AND ").append("vibrationpattern").append(" = ").append(alarmItem.mVibrationPattern).toString();
        try {
            alarmCursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, selectionClause + " AND " + "name" + "= '" + alarmItem.mAlarmName.replace("'", "''") + '\'', null, null);
        } catch (SQLiteException e) {
            Log.secE("BixbyAlarmDataHandler", "findCheckSameAlarmId() - android.database.sqlite.SQLiteException!");
            alarmItem.mAlarmName = alarmItem.mAlarmName.substring(0, 19);
            alarmCursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, selectionClause + " AND " + "name" + "= '" + alarmItem.mAlarmName.replace("'", "''") + '\'', null, "createtime DESC");
        }
        if (alarmCursor != null) {
            if (alarmCursor.moveToFirst()) {
                sameAlarmItemId = alarmCursor.getInt(0);
            }
            alarmCursor.close();
        }
        return sameAlarmItemId;
    }

    private int findDuplicationAlarmID(Context context, AlarmItem alarmItem) {
        int duplicationId = -1;
        int alarmTime = alarmItem.mAlarmTime;
        int alarmRepeatType = alarmItem.mRepeatType;
        String alarmName = alarmItem.mAlarmName;
        int searchYear = -1;
        int searchDayOfYear = -1;
        String selection = "_id != ?";
        ArrayList<String> selectionArgList = new ArrayList();
        selectionArgList.add(Integer.toString(alarmItem.mId));
        selection = selection + (selection.isEmpty() ? "" : " AND ");
        selectionArgList.add(Integer.toString(alarmTime));
        if (alarmItem.isWeeklyAlarm()) {
            selection = selection + "alarmtime = ? AND repeattype = ? AND name = ?";
            selectionArgList.add(Integer.toString(alarmRepeatType));
        } else {
            selection = selection + "alarmtime = ? AND name = ?";
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTimeInMillis(alarmItem.mAlarmAlertTime);
            searchYear = dateCalendar.get(1);
            searchDayOfYear = dateCalendar.get(6);
        }
        selectionArgList.add(alarmName);
        String[] selectionArg = (String[]) selectionArgList.toArray(new String[selectionArgList.size()]);
        Log.secD("BixbyAlarmDataHandler", "checkDuplicationAlarm() , selection " + selection);
        for (int a = 0; a < selectionArg.length; a++) {
            Log.secD("BixbyAlarmDataHandler", "checkDuplicationAlarm() , selectionArg[" + a + "] = " + selectionArg[a]);
        }
        Cursor alarmCursor = context.getContentResolver().query(AlarmProvider.CONTENT_URI, null, selection, selectionArg, "alarmtime ASC , alerttime ASC");
        if (alarmCursor != null) {
            while (alarmCursor.moveToNext()) {
                if (searchYear != -1 && searchDayOfYear != -1) {
                    AlarmItem similarItem = AlarmProvider.getAlarm(context, alarmCursor.getInt(0));
                    int cursorRepeatType = alarmCursor.getInt(5);
                    if (!(similarItem == null || AlarmItem.isWeeklyAlarm(cursorRepeatType))) {
                        Calendar calendar = Calendar.getInstance();
                        int curHour = calendar.get(11);
                        int curMinute = calendar.get(12);
                        if (calendar.getTimeInMillis() < similarItem.mAlarmAlertTime) {
                            calendar.setTimeInMillis(similarItem.mAlarmAlertTime);
                        } else if ((curHour * 100) + curMinute >= alarmTime) {
                            calendar.add(6, 1);
                        }
                        int itemYear = calendar.get(1);
                        if (searchDayOfYear == calendar.get(6) && searchYear == itemYear) {
                            duplicationId = alarmCursor.getInt(0);
                            break;
                        }
                    }
                }
                duplicationId = alarmCursor.getInt(0);
            }
            alarmCursor.close();
        }
        return duplicationId;
    }

    private void setAlarmInfoJSON(AlarmItem alarmItem) {
        boolean z = true;
        int snoozeDuration = 0;
        Log.secD("BixbyAlarmDataHandler", "setAlarmInfoJSON() : alarmItem id = " + alarmItem.mId);
        JSONObject alarmData = new JSONObject();
        try {
            alarmData.put("id", alarmItem.mId);
            Calendar calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis() >= alarmItem.mAlarmAlertTime) {
                if ((calendar.get(11) * 100) + calendar.get(12) >= alarmItem.mAlarmTime) {
                    calendar.add(6, 1);
                }
                int hour = alarmItem.mAlarmTime / 100;
                int minute = alarmItem.mAlarmTime % 100;
                calendar.set(11, hour);
                calendar.set(12, minute);
                calendar.set(13, 0);
                calendar.set(14, 0);
                Log.secD("BixbyAlarmDataHandler", "setAlarmInfoJSON() mAlarmAlertTime = " + alarmItem.mAlarmAlertTime + "hour = " + hour + ", minute = " + minute);
                Log.secD("BixbyAlarmDataHandler", "setAlarmInfoJSON() LOCAL getTimeInMillis => " + calendar.getTimeInMillis());
                Log.secD("BixbyAlarmDataHandler", "setAlarmInfoJSON() UTC getTimeInMillis => " + getLocalTimeToUtc(calendar.getTimeInMillis()));
                alarmData.put("time", getLocalTimeToUtc(calendar.getTimeInMillis()));
            } else {
                Log.secD("BixbyAlarmDataHandler", "setAlarmInfoJSON() LOCAL alarmItem.mAlarmAlertTime => " + alarmItem.mAlarmAlertTime);
                Log.secD("BixbyAlarmDataHandler", "setAlarmInfoJSON() UTC AlarmAlertTime => " + getLocalTimeToUtc(alarmItem.mAlarmAlertTime));
                alarmData.put("time", getLocalTimeToUtc(alarmItem.mAlarmAlertTime));
            }
            alarmData.put("name", alarmItem.mAlarmName);
            alarmData.put("isRepeat", alarmItem.isWeeklyAlarm());
            if (alarmItem.isWeeklyAlarm()) {
                alarmData.put("repeatDays", getRepeatDays(alarmItem.getAlarmRepeat()));
            }
            String str = "enabled";
            if (alarmItem.mActivate == 0) {
                z = false;
            }
            alarmData.put(str, z);
            if (alarmItem.mSnoozeActivate) {
                snoozeDuration = alarmItem.getSnoozeDuration();
            }
            alarmData.put("snoozeDuration", snoozeDuration);
            this.mAlarmDataArray.put(alarmData);
        } catch (JSONException e) {
            Log.secE("BixbyAlarmDataHandler", "setAlarmInfoJSON() - JSONException!!");
        }
    }

    private JSONArray getRepeatDays(int repeatDays) {
        JSONArray result = new JSONArray();
        int operator = 16777216;
        for (int i = 6; i >= 0; i--) {
            if ((repeatDays & operator) > 0) {
                try {
                    result.put(new JSONObject().put("day", BixbyConstants.WEEKDAY[i]));
                } catch (JSONException e) {
                    Log.secE("BixbyAlarmDataHandler", "getRepeatDays() JSONException!!");
                }
            }
            operator >>= 4;
        }
        return result;
    }

    private int convertDayOfWeekToRepeatType(ArrayList<Integer> dayOfWeek) {
        int result = 0;
        Iterator it = dayOfWeek.iterator();
        while (it.hasNext()) {
            result |= (1 << (((7 - ((Integer) it.next()).intValue()) + 1) * 4)) & -16;
        }
        return (result & -16) | 5;
    }
}
