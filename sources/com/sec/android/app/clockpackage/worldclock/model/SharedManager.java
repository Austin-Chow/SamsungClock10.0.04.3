package com.sec.android.app.clockpackage.worldclock.model;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharedManager {
    private Context mCtx;

    public SharedManager(Context ctx) {
        this.mCtx = ctx;
    }

    public void setPrefDBLocale(String locale) {
        Editor ed = this.mCtx.getSharedPreferences("DBLocale", 0).edit();
        ed.putString("DBLocale", locale);
        ed.apply();
    }

    public String getPrefDBLocale() {
        return this.mCtx.getSharedPreferences("DBLocale", 0).getString("DBLocale", null);
    }

    public int getPrefLastZoomLevel() {
        return this.mCtx.getSharedPreferences("LastZoomLevel", 0).getInt("LastZoomLevel", 1);
    }

    public void setNeedUpdateList(boolean needUpdateList) {
        Editor editor = this.mCtx.getSharedPreferences("ClocksTabStatus", 0).edit();
        editor.putBoolean("need_update_list", needUpdateList);
        editor.apply();
    }

    public boolean getNeedUpdateList() {
        return this.mCtx.getSharedPreferences("ClocksTabStatus", 0).getBoolean("need_update_list", false);
    }
}
