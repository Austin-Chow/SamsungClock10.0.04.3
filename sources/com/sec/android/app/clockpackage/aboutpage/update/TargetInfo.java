package com.sec.android.app.clockpackage.aboutpage.update;

import android.os.Environment;
import android.os.SemSystemProperties;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.File;

public class TargetInfo {
    private static final String TARGET_PATH_PD_TEST = (Environment.getExternalStorageDirectory().getPath() + File.separator + "go_to_andromeda.test");

    public static String getCsc() {
        String csc = SemSystemProperties.getSalesCode();
        Log.secD("TargetInfo", "csc from SemSystemProperties.getSalesCode(): " + csc);
        if (csc == null || csc.isEmpty()) {
            return "NONE";
        }
        return csc;
    }

    public static String getPd() {
        return isPdEnabled() ? "1" : "0";
    }

    private static boolean isPdEnabled() {
        return new File(TARGET_PATH_PD_TEST).exists();
    }
}
