package com.sec.android.app.clockpackage.common.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;

public class ClockTab {
    private static int sPrevTab = sSelectedTab;
    private static int sSelectedTab = 0;

    private static class LazyHolder {
        private static final ClockTab INSTANCE = new ClockTab();
    }

    private ClockTab() {
    }

    public static ClockTab getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static boolean isAlarmTab() {
        return sSelectedTab == 0;
    }

    public static boolean isWorldclockTab() {
        return sSelectedTab == 1;
    }

    public static boolean isStopWatchTab() {
        return sSelectedTab == 2;
    }

    public static boolean isTimerTab() {
        return sSelectedTab == 3;
    }

    public static int getCurrentTab() {
        return sSelectedTab;
    }

    public static int getPrevTab() {
        return sPrevTab;
    }

    public static void sendCurrentTabIndexIntent(Context context) {
        String currentTabIndex = "com.sec.android.app.clockpackage.CURRENT_TAB_INDEX";
        if (Feature.DEBUG_ENG) {
            Log.secV("ClockTab", "sendCurrentTabIndexIntent");
        }
        Intent intent = new Intent(currentTabIndex);
        intent.putExtra("current_tab", getCurrentTab());
        context.sendBroadcast(intent);
    }

    public void initTabPosition(Context context, Intent intent) {
        int initialPosition;
        if (intent != null) {
            initialPosition = getInstance().getTabPositionFromIntent(context, intent);
            if (initialPosition == -1) {
                initialPosition = getTabFromSharedPreference(context);
            }
        } else {
            initialPosition = getTabFromSharedPreference(context);
        }
        setTabPosition(context, initialPosition);
    }

    public void setTabPosition(Context context, int position) {
        setTabPositionToSharedPreference(context, position);
        sPrevTab = sSelectedTab;
        sSelectedTab = position;
    }

    private int getTabPositionFromIntent(Context context, Intent intent) {
        String edgeAction;
        int tab = intent.getIntExtra("clockpackage.select.tab", -1);
        intent.removeExtra("clockpackage.select.tab");
        if (StateUtils.isContextInDexMode(context)) {
            edgeAction = null;
        } else {
            edgeAction = intent.getStringExtra("clockpackage.select.tab.edge");
        }
        if (edgeAction != null) {
            Log.secD("ClockTab", "Enter from TaskEdge");
            tab = checkEdgePosition(edgeAction);
            intent.removeExtra("clockpackage.select.tab.edge");
        }
        Log.secD("ClockTab", "getTabPositionFromIntent tab = " + tab + ", action = " + intent.getAction());
        return tab;
    }

    private int checkEdgePosition(String action) {
        int edgePosition = -1;
        Object obj = -1;
        switch (action.hashCode()) {
            case -1575441174:
                if (action.equals("com.sec.android.app.clockpackage.INTENT_STOPWATCH")) {
                    obj = 1;
                    break;
                }
                break;
            case -1013955422:
                if (action.equals("com.sec.android.app.clockpackage.INTENT_TIMER")) {
                    obj = 2;
                    break;
                }
                break;
            case -796669857:
                if (action.equals("com.sec.android.app.clockpackage.INTENT_WORLDCLOCK")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                edgePosition = 1;
                break;
            case 1:
                edgePosition = 2;
                break;
            case 2:
                edgePosition = 3;
                break;
        }
        Log.secD("ClockTab", "checkEdgePosition action = " + action + ", edgePosition = " + edgePosition);
        return edgePosition;
    }

    private int getTabFromSharedPreference(Context context) {
        int position = context.getSharedPreferences("ClocksTabStatus", 0).getInt("CurrentTab", -1);
        if (position == -1) {
            return 0;
        }
        Log.secD("ClockTab", "sSelectedTab Preference : " + position);
        return position;
    }

    private void setTabPositionToSharedPreference(Context context, int position) {
        Editor editor = context.getSharedPreferences("ClocksTabStatus", 0).edit();
        editor.putInt("CurrentTab", position);
        editor.apply();
    }
}
