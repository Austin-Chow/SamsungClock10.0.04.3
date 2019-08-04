package com.sec.android.app.clockpackage.alarm.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SemSystemProperties;
import android.provider.MediaStore.Audio.Media;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;
import java.io.File;

public class AlarmRingtoneManager {
    private final Context mContext;
    private Cursor mCursor;
    private int mRingtoneCount = -1;
    private RingtoneManager mRingtoneManager;

    public AlarmRingtoneManager(Context context) {
        this.mContext = context;
        createRingtoneList();
    }

    public void createRingtoneList() {
        this.mRingtoneManager = new RingtoneManager(this.mContext);
        Log.secD("AlarmRingtoneManager", "createRingtoneList()");
        this.mRingtoneManager.setType(4);
        this.mRingtoneManager.setIncludeDrm(true);
        if (this.mCursor != null) {
            this.mCursor.close();
            this.mCursor = null;
        }
        try {
            this.mCursor = this.mRingtoneManager.getCursor();
            if (this.mCursor != null) {
                this.mRingtoneCount = this.mCursor.getCount();
            }
        } catch (Exception e) {
            Log.secE("AlarmRingtoneManager", "Exception : " + e.toString());
        }
    }

    private String getRecommendedRingtoneStr(String tone) {
        if (tone == null) {
            return "";
        }
        int i = tone.indexOf(63);
        if (i == -1) {
            i = tone.length();
        }
        return tone.substring(0, i);
    }

    public static Uri getDefaultRingtoneUri(Context context) {
        return RingtonePlayer.getDefaultRingtoneUri(context);
    }

    public String getRingtoneTitle(String tone) {
        Log.secD("AlarmRingtoneManager", "getAlarmToneTitle() / tone  = " + tone);
        if (this.mRingtoneManager == null) {
            createRingtoneList();
        }
        int count = getRingtoneListSize();
        int index = getRingtoneIndex(tone);
        Log.secD("AlarmRingtoneManager", "getAlarmToneTitle() / " + tone + "index = " + index + " count = " + count);
        if (index < 0 || index >= count) {
            createRingtoneList();
            if (hasCustomThemeRingtone()) {
                tone = Uri.encode(getDefaultRingtoneUri(this.mContext).toString());
            } else {
                tone = getAlarmTonePreference(this.mContext);
            }
            index = getRingtoneIndex(tone);
        }
        String toneTitle = getRingtoneTitle(index);
        Log.secD("AlarmRingtoneManager", "getAlarmToneTitle() / " + tone + "index = " + index + " / toneTitle = " + toneTitle);
        return toneTitle;
    }

    public String getRingtoneTitle(int index) {
        if (this.mRingtoneManager == null) {
            createRingtoneList();
        }
        try {
            return this.mRingtoneManager.getRingtone(index).getTitle(this.mContext);
        } catch (NullPointerException e) {
            Log.secD("AlarmRingtoneManager", "NullPointerException!! Permission Check!!");
            return "";
        }
    }

    public int getRingtoneListSize() {
        return this.mRingtoneCount;
    }

    public int getRingtoneIndex(String tone) {
        Log.secD("AlarmRingtoneManager", "getRingtoneIndex tone : " + tone);
        if (this.mRingtoneManager == null || tone.isEmpty()) {
            return -1;
        }
        Uri uri = Uri.parse(getRecommendedRingtoneStr(Uri.decode(tone)));
        if (!"content".equals(uri.getScheme())) {
            String soundFilePath = uri.toString().replaceAll("'", "''");
            if (PermissionUtils.hasPermissionExternalStorage(this.mContext)) {
                Cursor externalCursor = this.mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data='" + soundFilePath + '\'', null, null);
                if (externalCursor != null) {
                    if (externalCursor.getCount() == 1) {
                        externalCursor.moveToFirst();
                        uri = Uri.parse(Media.EXTERNAL_CONTENT_URI + "/" + externalCursor.getLong(0));
                        externalCursor.close();
                        return this.mRingtoneManager.getRingtonePosition(uri);
                    }
                    externalCursor.close();
                }
            }
            Cursor internal_cursor = this.mContext.getContentResolver().query(Media.INTERNAL_CONTENT_URI, new String[]{"_id"}, "_data='" + soundFilePath + '\'', null, null);
            if (internal_cursor != null) {
                internal_cursor.moveToFirst();
                if (internal_cursor.getCount() == 1) {
                    uri = Uri.parse(Media.INTERNAL_CONTENT_URI + "/" + internal_cursor.getLong(0));
                    internal_cursor.close();
                    return this.mRingtoneManager.getRingtonePosition(uri);
                }
                internal_cursor.close();
            }
        }
        try {
            return this.mRingtoneManager.getRingtonePosition(uri);
        } catch (CursorIndexOutOfBoundsException e) {
            Log.secE("AlarmRingtoneManager", "Exception : " + e.toString());
            return -1;
        } catch (Exception e2) {
            Log.secE("AlarmRingtoneManager", "Exception : " + e2.toString());
            return -1;
        }
    }

    public boolean hasCustomThemeRingtone() {
        int defaultIndex = getRingtoneIndex(getDefaultRingtoneUri(this.mContext).toString());
        if (!StateUtils.isCustomTheme(this.mContext)) {
            return false;
        }
        String[] alarmToneTitle = SemSystemProperties.get("ro.config.alarm_alert").replace("_", " ").split("\\.");
        Log.secD("AlarmRingtoneManager", "defaultRingtone Title : " + getRingtoneTitle(defaultIndex));
        if (alarmToneTitle == null || !getRingtoneTitle(defaultIndex).equalsIgnoreCase(alarmToneTitle[0])) {
            Log.secD("AlarmRingtoneManager", "it has theme alarm tone. " + defaultIndex);
            return true;
        }
        Log.secD("AlarmRingtoneManager", "it doesn't has theme alarm tone - preference alarm tone");
        return false;
    }

    public static boolean isExternalRingtone(String uri) {
        Log.secD("AlarmRingtoneManager", "isExternalRingtone() / decoded uri = " + Uri.parse(Uri.decode(uri)));
        return uri != null && Uri.parse(Uri.decode(uri)).toString().startsWith(Media.EXTERNAL_CONTENT_URI.toString());
    }

    public static boolean validRingtoneStr(Context context, String str) {
        boolean retVal = false;
        Uri uri = Uri.parse(Uri.decode(str));
        String scheme = uri.getScheme();
        Log.secD("AlarmRingtoneManager", "validRingtoneStr uri = " + uri + " scheme:" + scheme);
        if (scheme == null || scheme.equals("file")) {
            File fp = new File(uri.getPath());
            Log.secD("AlarmRingtoneManager", uri.getPath());
            if (fp.exists() && fp.isFile()) {
                Log.secD("AlarmRingtoneManager", "File exist : " + uri.toString());
                retVal = true;
            } else {
                Log.secD("AlarmRingtoneManager", "fp is null");
            }
        }
        if (uri.toString().contains("content://media/")) {
            try {
                Cursor c = context.getContentResolver().query(uri, null, null, null, null);
                if (c != null) {
                    Log.secD("AlarmRingtoneManager", "getCount:" + c.getCount() + "getColumnCount" + c.getColumnCount());
                    if (c.getCount() > 0 && c.getColumnCount() > 0) {
                        retVal = true;
                    }
                    c.close();
                }
            } catch (SecurityException e) {
                Log.secE("AlarmRingtoneManager", "This is External ringtone");
                boolean z = retVal;
                return false;
            }
        }
        Log.secD("AlarmRingtoneManager", "validRingtoneStr() / ret = " + retVal);
        return retVal;
    }

    public static void setAlarmTonePreference(Context context, String soundUri) {
        Log.secD("AlarmRingtoneManager", "saveAlarmTonePreference :" + soundUri);
        Editor editor = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
        editor.putString("alarm_tone", soundUri);
        editor.apply();
    }

    public String getAlarmTonePreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
        String defaultUri = Uri.encode(getDefaultRingtoneUri(context).toString());
        String soundUri = pref.getString("alarm_tone", defaultUri);
        if (!validRingtoneStr(context, soundUri)) {
            soundUri = defaultUri;
        }
        Log.secD("AlarmRingtoneManager", "getAlarmTonePreference : " + soundUri);
        return soundUri;
    }

    public static String getAlarmTonePreferenceOrDefaultRingtoneUri(Context context) {
        SharedPreferences pref = ClockUtils.getBootAwareContext(context).getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
        String defaultUri = Uri.encode(getDefaultRingtoneUri(context).toString());
        String soundUri = pref.getString("alarm_tone", defaultUri);
        if (!validRingtoneStr(context, soundUri)) {
            soundUri = defaultUri;
        }
        Log.secD("AlarmRingtoneManager", "getAlarmTonePreference : " + soundUri);
        Log.secD("AlarmRingtoneManager", "getAlarmToneDefaulltPreference : " + defaultUri);
        return soundUri;
    }

    public static boolean isAlarmTypeRingtone(Context context, String uriStr) {
        if (uriStr.equals("alarm_silent_ringtone")) {
            Log.secD("AlarmRingtoneManager", "isAlarmTypeRingtone : This is silent alarm tone");
            return true;
        }
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(4);
        int index = ringtoneManager.getRingtonePosition(Uri.parse(Uri.decode(uriStr)));
        Log.secD("AlarmRingtoneManager", "isAlarmTypeRingtone / index=" + index);
        if (index == -1) {
            return false;
        }
        return true;
    }

    public void removeInstance() {
        Log.secD("AlarmRingtoneManager", "removeInstance()");
        if (this.mCursor != null) {
            this.mCursor.close();
            this.mCursor = null;
        }
        this.mRingtoneManager = null;
    }
}
