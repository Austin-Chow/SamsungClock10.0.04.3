package com.sec.android.app.clockpackage.common.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.samsung.android.widget.SemTipPopup;
import com.samsung.android.widget.SemTipPopup.OnStateChangeListener;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;

public class ClockSmartTip {
    private Handler mRestoreSmartTipHandler = new Handler();
    private SemTipPopup mSmartTip;
    private int mSmartTipState = 0;
    private Handler mUpdateSmartTipPositionHandler = new Handler();

    /* renamed from: com.sec.android.app.clockpackage.common.viewmodel.ClockSmartTip$3 */
    class C06563 implements OnStateChangeListener {
        C06563() {
        }

        public void onStateChanged(int state) {
            ClockSmartTip.this.mSmartTipState = state;
        }
    }

    private void releaseInstance() {
        if (this.mSmartTip != null) {
            this.mSmartTip.setOnStateChangeListener(null);
            this.mSmartTip = null;
        }
        this.mRestoreSmartTipHandler.removeCallbacksAndMessages(null);
        this.mUpdateSmartTipPositionHandler.removeCallbacksAndMessages(null);
    }

    public void saveSmartTip(Bundle outState) {
        String str = "smart_tip_show";
        boolean z = this.mSmartTip != null && this.mSmartTip.isShowing();
        outState.putBoolean(str, z);
        outState.putInt("smart_tip_state", this.mSmartTipState);
    }

    public void restoreSmartTip(Bundle savedInstanceState, final Context context, final int actionModule, final Toolbar parentView) {
        if (savedInstanceState.getBoolean("smart_tip_show", false)) {
            this.mSmartTipState = savedInstanceState.getInt("smart_tip_state", 1);
            this.mRestoreSmartTipHandler.postDelayed(new Runnable() {
                public void run() {
                    ClockSmartTip.this.launchSmartTip(context, true, actionModule, parentView);
                }
            }, 50);
        }
    }

    public void launchSmartTip(Context context, boolean isRecreate, int actionModule, Toolbar parentView) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
        if (needShowSmartTip(sharedPref, isRecreate, actionModule)) {
            boolean isSetShowCount = showSmartTip(context, actionModule, parentView) && !isRecreate;
            if (isSetShowCount) {
                Editor ed = sharedPref.edit();
                String key = getPreferenceKey(actionModule);
                ed.putInt(key, sharedPref.getInt(key, 0) + 1);
                ed.apply();
            }
        }
    }

    public void updateSmartTipPosition(final Context context, final int actionModule, final Toolbar parentView) {
        if (this.mSmartTip != null && this.mSmartTip.isShowing()) {
            this.mUpdateSmartTipPositionHandler.postDelayed(new Runnable() {
                public void run() {
                    if (ClockSmartTip.this.mSmartTip != null) {
                        int[] pos = ClockSmartTip.this.getCoordinates(context, actionModule, parentView);
                        ClockSmartTip.this.mSmartTip.setTargetPosition(pos[0], pos[1]);
                        ClockSmartTip.this.mSmartTip.update();
                    }
                }
            }, 50);
        }
    }

    private boolean showSmartTip(Context context, int actionModule, Toolbar parentView) {
        boolean z = false;
        if (this.mSmartTip != null && this.mSmartTip.isShowing()) {
            return false;
        }
        Resources res = context.getResources();
        if (parentView == null) {
            return false;
        }
        int i;
        this.mSmartTip = new SemTipPopup(parentView);
        this.mSmartTip.setMessage(getSmartTipMessage(res, actionModule));
        this.mSmartTip.setOnStateChangeListener(new C06563());
        int[] pos = getCoordinates(context, actionModule, parentView);
        this.mSmartTip.setTargetPosition(pos[0], pos[1]);
        SemTipPopup semTipPopup = this.mSmartTip;
        if (this.mSmartTipState == 2) {
            z = true;
        }
        semTipPopup.setExpanded(z);
        semTipPopup = this.mSmartTip;
        if (StateUtils.isRtl()) {
            i = 3;
        } else {
            i = 2;
        }
        semTipPopup.show(i);
        return true;
    }

    @SuppressLint({"NewApi"})
    private int[] getCoordinates(Context context, int actionModule, Toolbar parentView) {
        int xPosMargin;
        Resources res = context.getResources();
        View decorContentParent = (View) parentView.getParent();
        int[] outLocation = new int[2];
        int[] coordinate = new int[2];
        if (decorContentParent != null) {
            decorContentParent.getLocationInWindow(outLocation);
        }
        Log.secD("ClockSmartTip", "parentView.getMeasuredWidth() :  " + parentView.getMeasuredWidth());
        Log.secD("ClockSmartTip", "parentView.getY() :  " + parentView.getY());
        Log.secD("ClockSmartTip", "outLocation[POS_Y] :  " + outLocation[1]);
        if (actionModule == 0) {
            xPosMargin = res.getDimensionPixelSize(C0645R.dimen.clock_smart_tips_timer_margin_end);
        } else {
            xPosMargin = res.getDimensionPixelSize(C0645R.dimen.clock_smart_tips_worldclock_margin_end);
        }
        if (!StateUtils.isRtl()) {
            xPosMargin = parentView.getMeasuredWidth() - xPosMargin;
        }
        coordinate[0] = xPosMargin;
        coordinate[1] = (((int) parentView.getY()) + res.getDimensionPixelSize(C0645R.dimen.clock_smart_tips_margin_top)) + outLocation[1];
        Log.secD("ClockSmartTip", "coordinate[POS_X] :  " + coordinate[0]);
        Log.secD("ClockSmartTip", "coordinate[POS_Y] :  " + coordinate[1]);
        int displayRotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
        if (!StateUtils.isInMultiwindow()) {
            if (StateUtils.isVisibleNaviBar(context) && displayRotation == 3) {
                coordinate[0] = coordinate[0] + ClockUtils.getNavigationBarHeight(context);
                Log.secD("ClockSmartTip", "coordinate[POS_X] + ClockUtils.getNavigationBarHeight(context) :  " + coordinate[0]);
            } else if (VERSION.SDK_INT >= 28 && displayRotation == 1) {
                WindowInsets windowInsets = parentView.getRootWindowInsets();
                if (windowInsets != null) {
                    DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                    if (displayCutout != null) {
                        coordinate[0] = coordinate[0] + displayCutout.getSafeInsetLeft();
                        Log.secD("ClockSmartTip", "coordinate[POS_X] + displayCutout.getSafeInsetLeft() :  " + coordinate[0]);
                    }
                }
            }
        }
        return coordinate;
    }

    public void dismissSmartTips() {
        if (this.mSmartTip != null) {
            this.mSmartTip.dismiss(true);
            releaseInstance();
        }
    }

    private boolean needShowSmartTip(SharedPreferences sharedPref, boolean isRecreate, int actionModule) {
        switch (actionModule) {
            case 0:
                boolean isAddPresetExecuted = sharedPref.getBoolean("timer_add_preset_executed", false);
                int smartTipShowCount = sharedPref.getInt("timer_smart_tips_show_count", 0);
                int timerStartCount = sharedPref.getInt("timer_start_count", 0);
                if (!isAddPresetExecuted) {
                    if (isRecreate || smartTipShowCount == 0) {
                        return true;
                    }
                    if (smartTipShowCount == 1 && timerStartCount >= 5) {
                        return true;
                    }
                    if (smartTipShowCount == 2 && timerStartCount >= 10) {
                        return true;
                    }
                }
                return false;
            case 1:
                if (isRecreate) {
                    return true;
                }
                int worldclockSmartTipsShowCount = sharedPref.getInt("worldclock_smart_tips_show_count", 0);
                Editor editor = sharedPref.edit();
                if (worldclockSmartTipsShowCount < 3) {
                    if (worldclockSmartTipsShowCount == 0) {
                        return true;
                    }
                    int firstLaunchCount = sharedPref.getInt("worldclock_first_launch_count_for_smart_tip", 0);
                    if (firstLaunchCount < 5) {
                        editor.putInt("worldclock_first_launch_count_for_smart_tip", firstLaunchCount + 1);
                        editor.apply();
                    }
                    firstLaunchCount = sharedPref.getInt("worldclock_first_launch_count_for_smart_tip", 0);
                    if (worldclockSmartTipsShowCount == 1 && firstLaunchCount == 3) {
                        editor.putInt("worldclock_first_launch_count_for_smart_tip", 0);
                        editor.apply();
                        return true;
                    } else if (worldclockSmartTipsShowCount == 2 && firstLaunchCount == 5) {
                        return true;
                    }
                }
                break;
            case 2:
                return sharedPref.getInt("worldclock_smart_tips_show_count", 0) == 0;
        }
        return false;
    }

    private String getPreferenceKey(int actionModule) {
        switch (actionModule) {
            case 0:
                return "timer_smart_tips_show_count";
            case 1:
            case 2:
                return "worldclock_smart_tips_show_count";
            default:
                return null;
        }
    }

    private String getSmartTipMessage(Resources res, int actionModule) {
        switch (actionModule) {
            case 0:
                return res.getString(C0645R.string.timer_smart_tips);
            case 1:
            case 2:
                return res.getString(C0645R.string.worldclock_smart_tips);
            default:
                return null;
        }
    }

    public void addPresetExecuted(Context context) {
        Editor ed = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
        ed.putBoolean("timer_add_preset_executed", true);
        ed.apply();
    }

    public void addTimerStartCount(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0);
        Editor ed = sharedPref.edit();
        ed.putInt("timer_start_count", sharedPref.getInt("timer_start_count", 0) + 1);
        ed.apply();
    }

    public boolean isShowSmartTip(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean("smart_tip_show", false);
    }

    public void disableWorldclockSmartTips(Context context) {
        Editor editor = context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
        editor.putBoolean("worldclock_need_show_smart_tips", false);
        editor.apply();
    }

    public boolean getWorldclockSmartTips(Context context) {
        return context.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getBoolean("worldclock_need_show_smart_tips", true);
    }
}
