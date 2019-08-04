package com.sec.android.app.clockpackage;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;

public class ClockPackageExecutable extends ClockPackage {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage");
        Intent intent = getIntent();
        Intent reloadIntent = new Intent();
        reloadIntent.setComponent(cn);
        reloadIntent.setDataAndType(intent.getData(), intent.getType());
        String action = intent.getAction();
        reloadIntent.putExtra("clockpackage.select.tab.edge", action);
        Log.m43i("ClockPackageExecutable", "" + action);
        reloadIntent.setAction("android.intent.action.MAIN");
        reloadIntent.addCategory("android.intent.category.LAUNCHER");
        if (ClockUtils.isRunningClass(getBaseContext(), "com.sec.android.app.clockpackage.ClockPackage")) {
            Log.m43i("ClockPackageExecutable", "clockpackage was already running");
            reloadIntent.setFlags(268468224);
        } else {
            Log.m43i("ClockPackageExecutable", "this is first launch");
            reloadIntent.setFlags(335544320);
        }
        startActivityIfNeeded(reloadIntent, 0);
        finish();
    }
}
