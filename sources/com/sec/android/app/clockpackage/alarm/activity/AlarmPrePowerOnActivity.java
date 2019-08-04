package com.sec.android.app.clockpackage.alarm.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar.SubAppBarPressListener;

public class AlarmPrePowerOnActivity extends ClockActivity {
    private final String KEY_AUTO_POWER_UP = "auto_power_up";
    private final String TAG = "AlarmPrePowerOnActivity";
    private Context mContext;
    private boolean mCurrentSwitchStatus;
    private SharedPreferences mPref;
    private boolean mPreviousSwitchStatus;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmPrePowerOnActivity$1 */
    class C05191 implements SubAppBarPressListener {
        C05191() {
        }

        public void setChecked(boolean isChecked) {
            String str;
            AlarmPrePowerOnActivity.this.mCurrentSwitchStatus = isChecked;
            Log.secD("AlarmPrePowerOnActivity", "mAlarmPrePowerOn isChecked : " + isChecked);
            if (isChecked) {
                str = AlarmPrePowerOnActivity.this.getResources().getString(C0490R.string.switch_on);
            } else {
                str = AlarmPrePowerOnActivity.this.getResources().getString(C0490R.string.switch_off);
            }
            ((TextView) AlarmPrePowerOnActivity.this.findViewById(C0490R.id.sub_appbar_textview)).setText(str);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlarmPrePowerOnActivity", "onCreate");
        this.mContext = new ContextThemeWrapper(this, C0490R.style.ClockTheme);
        setContentView(C0490R.layout.alarm_power_onoff_settings);
        this.mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean z = this.mPref.getBoolean("auto_power_up", Feature.isAutoPowerOnOffMenuSupported());
        this.mCurrentSwitchStatus = z;
        this.mPreviousSwitchStatus = z;
        addSubAppBarView();
        updateListBackground();
    }

    protected void onResume() {
        super.onResume();
        ClockUtils.insertSaLog("921");
    }

    protected void onPause() {
        super.onPause();
        Log.secD("AlarmPrePowerOnActivity", "onPause");
        setAlarmPowerPreference();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.secD("AlarmPrePowerOnActivity", "onDestroy");
        setAlarmPowerPreference();
    }

    private void addSubAppBarView() {
        ClockSubAppBar subAppBar = (ClockSubAppBar) findViewById(C0490R.id.sub_appbar_layout);
        subAppBar.setSubAppBarPressListener(new C05191());
        subAppBar.setChecked(this.mCurrentSwitchStatus);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.secD("AlarmPrePowerOnActivity", "..onKeyUp.. keyCode : " + keyCode);
        switch (keyCode) {
            case 4:
                setAlarmPowerPreference();
                ClockUtils.insertSaLog("921", "1241");
                break;
            case 24:
            case 25:
            case 82:
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void setAlarmPowerPreference() {
        Log.secD("AlarmPrePowerOnActivity", "setAlarmPowerPreference");
        Editor sPrefEditor = this.mPref.edit();
        try {
            if (this.mCurrentSwitchStatus != this.mPreviousSwitchStatus) {
                sPrefEditor.putBoolean("auto_power_up", this.mCurrentSwitchStatus);
                Log.secD("AlarmPrePowerOnActivity", "KEY_AUTO_POWER_UP : " + this.mCurrentSwitchStatus);
            } else {
                sPrefEditor.putBoolean("auto_power_up", this.mPreviousSwitchStatus);
                Log.secD("AlarmPrePowerOnActivity", "KEY_AUTO_POWER_UP : " + this.mPreviousSwitchStatus);
            }
            sPrefEditor.apply();
            AlarmProvider.enableNextAlert(getApplicationContext());
        } catch (NullPointerException e) {
            Log.secD("AlarmPrePowerOnActivity", "setAlarmPowerPreference NullPointException" + e.toString());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        setAlarmPowerPreference();
        finish();
        ClockUtils.insertSaLog("921", "1241");
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateListBackground();
    }

    private void updateListBackground() {
        LinearLayout layout = (LinearLayout) findViewById(C0490R.id.alarm_power_list_background);
        layout.setGravity(17);
        ClockUtils.updateListBothSideMargin(this.mContext, layout);
    }
}
