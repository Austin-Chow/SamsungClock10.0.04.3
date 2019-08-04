package com.sec.android.app.clockpackage.backuprestore.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Base64;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    public static JSONObject toJSON(Cursor cursor) throws JSONException {
        String[] columnNames = cursor.getColumnNames();
        JSONObject jsonObj = new JSONObject();
        for (String column : columnNames) {
            String column2;
            int columnIndex = cursor.getColumnIndex(column2);
            if (columnIndex != -1) {
                switch (cursor.getType(columnIndex)) {
                    case 0:
                    case 2:
                        break;
                    case 1:
                        if (!"_id".equals(column2)) {
                            if (!"vibrationpattern".equals(column2)) {
                                if (!"dailybrief".equals(column2)) {
                                    jsonObj.put(column2, Long.toString(cursor.getLong(columnIndex)));
                                    break;
                                }
                                int val = cursor.getInt(columnIndex);
                                Log.secD("BNR_CLOCK_JSONParser", "daily briefing 0 / column = " + column2 + " / val = " + val);
                                val = AlarmItem.initRecommendWeatherBg(AlarmItem.initIncreasingVolume(val));
                                if (!AlarmItem.isMasterSoundOn(val)) {
                                    val = AlarmItem.setAlarmToneOn(AlarmItem.setBixbyBriefingOn(AlarmItem.setMasterSoundOn(val, true), false), false);
                                } else if (AlarmItem.isNewBixbyOn(val)) {
                                    val = AlarmItem.setAlarmToneOn(AlarmItem.setBixbyCelebVoice(AlarmItem.setBixbyVoiceOn(AlarmItem.setBixbyBriefingOn(val, true), true), false), false);
                                } else {
                                    val = AlarmItem.setAlarmToneOn(AlarmItem.setBixbyCelebVoice(AlarmItem.setBixbyVoiceOn(AlarmItem.setBixbyBriefingOn(val, false), true), false), true);
                                }
                                Log.secD("BNR_CLOCK_JSONParser", "daily briefing 1 / column = " + column2 + " / val = " + val);
                                jsonObj.put(column2, Integer.toString(val));
                                column2 = "dailybrief_BACKUP_VER_8";
                                val = cursor.getInt(columnIndex);
                                Log.secD("BNR_CLOCK_JSONParser", "daily briefing 2 / column = " + column2 + " / val = " + val);
                                jsonObj.put(column2, Integer.toString(val));
                                break;
                            }
                            Log.secD("BNR_CLOCK_JSONParser", "BNR_ALARM_VIB : adjust alarm vibration pattern column = " + column2 + "/ original user value : " + cursor.getLong(columnIndex));
                            int originalPattern = (int) cursor.getLong(columnIndex);
                            if (AlarmUtil.isNewVibrationList(originalPattern)) {
                                jsonObj.put(column2, Integer.toString(50035));
                            } else {
                                jsonObj.put(column2, Integer.toString(originalPattern));
                            }
                            jsonObj.put("vibrationpattern_user", Integer.toString(originalPattern));
                            break;
                        }
                        jsonObj.put("BackupVersion", 8);
                        jsonObj.put(column2, Long.toString(cursor.getLong(columnIndex)));
                        break;
                    case 3:
                        if (!"locationtext".equals(column2)) {
                            jsonObj.put(column2, cursor.getString(columnIndex));
                            break;
                        }
                        jsonObj.put(column2, "");
                        break;
                    case 4:
                        jsonObj.put(column2, Base64.encodeToString(cursor.getBlob(columnIndex), 0));
                        break;
                    default:
                        Log.secD("BNR_CLOCK_JSONParser", "NO SUCH COLUMN : " + column2);
                        break;
                }
            }
        }
        return jsonObj;
    }

    public static ContentValues fromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues contentValues = new ContentValues();
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = jsonObject.get(key);
            if (value instanceof Integer) {
                contentValues.put(key, Integer.valueOf(Integer.parseInt(value.toString())));
            } else if (value instanceof String) {
                contentValues.put(key, value.toString());
            } else if (value instanceof Float) {
                contentValues.put(key, (Float) value);
            } else if (value instanceof Boolean) {
                contentValues.put(key, (Boolean) value);
            } else {
                Log.secD("BNR_CLOCK_JSONParser", "JASONParser(): value Type is unknown");
            }
        }
        return contentValues;
    }
}
