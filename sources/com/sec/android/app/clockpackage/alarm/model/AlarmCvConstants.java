package com.sec.android.app.clockpackage.alarm.model;

import android.os.UserHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AlarmCvConstants {
    public static final ArrayList<String> AUTHORIZED_PACKAGES = new ArrayList(Arrays.asList(new String[]{"com.sec.android.app.clockpackage", "com.sec.android.app.samsungapps"}));
    public static final HashMap<String, String[]> CELEBVOICE_JSON_FORMAT = new C05361();

    /* renamed from: com.sec.android.app.clockpackage.alarm.model.AlarmCvConstants$1 */
    static class C05361 extends HashMap<String, String[]> {
        C05361() {
            put("celebvoice_version", new String[]{"celebvoice_version"});
            put("title", new String[]{"header", "title"});
            put("package_name", new String[]{"header", "package_name"});
            put("type", new String[]{"header", "type"});
            put("attribute", new String[]{"header", "attribute"});
            put("version_code", new String[]{"header", "version_code"});
            put("version_name", new String[]{"header", "version_name"});
            put("file", new String[]{"header", "file"});
            put("bitrate", new String[]{"header", "bitrate"});
            put("stereo", new String[]{"header", "stereo"});
            put("khz", new String[]{"header", "khz"});
            put("static", new String[]{"clockvoice_files", "representative", "static"});
            put("dynamic", new String[]{"clockvoice_files", "representative", "dynamic"});
            put("preview", new String[]{"clockvoice_files", "preview"});
            put("original", new String[]{"clockvoice_files", "original"});
        }
    }

    public static String getUIDRootPath() {
        return "/data/user/" + UserHandle.semGetMyUserId() + "/com.sec.android.app.clockpackage/files/";
    }
}
