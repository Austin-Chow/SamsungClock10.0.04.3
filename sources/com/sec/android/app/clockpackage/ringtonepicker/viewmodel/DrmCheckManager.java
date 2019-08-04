package com.sec.android.app.clockpackage.ringtonepicker.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Locale;

class DrmCheckManager {
    private Context mContext;
    private DrmManagerClient mDrmManagerClient;

    DrmCheckManager(Context context) {
        Log.secD("DrmCheckManager", "DrmCheckManager");
        this.mDrmManagerClient = new DrmManagerClient(context);
        this.mContext = context;
    }

    private String getDrmMimeType(String file) {
        if (file == null) {
            return null;
        }
        file = file.toLowerCase(Locale.ROOT);
        if (file.endsWith(".dcf")) {
            return "application/vnd.oma.drm.content";
        }
        if (file.endsWith(".pya")) {
            return "audio/vnd.ms-playready.media.pya";
        }
        if (file.endsWith(".wmv")) {
            return "video/x-ms-wmv";
        }
        if (file.endsWith(".wma")) {
            return "audio/x-ms-wma";
        }
        if (file.endsWith(".pyv")) {
            return "video/vnd.ms-playready.media.pyv";
        }
        if (file.endsWith(".avi")) {
            return "video/mux/AVI";
        }
        if (file.endsWith(".mkv")) {
            return "video/mux/MKV";
        }
        if (file.endsWith(".divx")) {
            return "video/mux/DivX";
        }
        if (file.endsWith(".isma")) {
            return "audio/isma";
        }
        if (file.endsWith(".ismv")) {
            return "video/ismv";
        }
        return null;
    }

    private boolean isOmaForwardLockType(String filePath) {
        DrmInfo drmInfo;
        DrmInfoRequest infoRequest = new DrmInfoRequest(14, "application/vnd.oma.drm.content");
        infoRequest.put("drm_path", filePath);
        if (this.mDrmManagerClient == null) {
            drmInfo = null;
        } else {
            drmInfo = this.mDrmManagerClient.acquireDrmInfo(infoRequest);
        }
        if (drmInfo == null) {
            Log.secE("DrmCheckManager", "isOmaForwardLockType: acquireDrmInfo Fail");
            return false;
        } else if (drmInfo.get(NotificationCompat.CATEGORY_STATUS).toString().equals("success")) {
            Log.secI("DrmCheckManager", "isOmaForwardLockType: acquireDrmInfo Success");
            Object objType = drmInfo.get("type");
            if (objType == null) {
                Log.secE("DrmCheckManager", "isOmaForwardLockType: objType is null");
                return false;
            }
            int drmType = Integer.parseInt(objType.toString());
            Log.secI("DrmCheckManager", "drmType = " + drmType);
            if (drmType != 0) {
                return false;
            }
            Log.secI("DrmCheckManager", "isOmaForwardLockType: this is FL");
            return true;
        } else {
            Log.secE("DrmCheckManager", "isOmaForwardLockType: acquireDrmInfo Fail");
            return false;
        }
    }

    private String getAudioFilePath(Uri uri) {
        Cursor c = this.mContext.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        String path = null;
        if (c != null) {
            try {
                if (c.getCount() == 1) {
                    c.moveToFirst();
                    path = c.getString(c.getColumnIndexOrThrow("_data"));
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        if (c != null) {
            c.close();
        }
        return path;
    }

    boolean canSetRingtone(Uri ringtoneUri) {
        if (ringtoneUri != null && ringtoneUri.toString().contains("content://media/")) {
            String path = getAudioFilePath(ringtoneUri);
            String mimeType = getDrmMimeType(path);
            if (this.mDrmManagerClient == null) {
                Log.secE("DrmCheckManager", "mDrmClient is null!!");
            } else if (path == null || mimeType == null) {
                Log.secE("DrmCheckManager", "path or mimeType is null!!");
            } else if (this.mDrmManagerClient.canHandle(path, mimeType)) {
                if (isOmaForwardLockType(path)) {
                    Log.secE("DrmCheckManager", "FL DRM File. Check if RingtoneManager.getRingtone() is null!!");
                } else if (!Feature.isDCM(this.mContext) || !"audio/isma".equals(mimeType)) {
                    return false;
                } else {
                    if (this.mDrmManagerClient.checkRightsStatus(ringtoneUri, 2) == 0) {
                        return true;
                    }
                    Log.secE("DrmCheckManager", "PR DRM File Ringtone Rights Invalid !!!");
                    return false;
                }
            }
            if (RingtoneManager.getRingtone(this.mContext, ringtoneUri) == null) {
                Log.secE("DrmCheckManager", "getRingtone() null");
                return false;
            }
        }
        return true;
    }

    void removeInstance() {
        if (this.mDrmManagerClient != null) {
            this.mDrmManagerClient.close();
            this.mDrmManagerClient = null;
        }
    }
}
