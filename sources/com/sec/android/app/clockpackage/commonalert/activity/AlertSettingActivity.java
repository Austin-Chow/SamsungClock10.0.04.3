package com.sec.android.app.clockpackage.commonalert.activity;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.C0661R;

public class AlertSettingActivity extends ClockActivity {

    public static class PrefsFragment extends PreferenceFragmentCompat {

        /* renamed from: com.sec.android.app.clockpackage.commonalert.activity.AlertSettingActivity$PrefsFragment$1 */
        class C06621 implements OnPreferenceClickListener {
            C06621() {
            }

            public boolean onPreferenceClick(Preference preference) {
                boolean z;
                Editor ed = StateUtils.getAlarmDBContext(PrefsFragment.this.getContext()).getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).edit();
                String str = "sound_mode_jpn";
                if (((SwitchPreferenceCompat) preference).isChecked()) {
                    z = true;
                } else {
                    z = false;
                }
                ed.putBoolean(str, z);
                ed.apply();
                ClockUtils.insertSaLog("900", "9002", ((SwitchPreferenceCompat) preference).isChecked() ? 1 : 0);
                return false;
            }
        }

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Log.secD("PrefsFragment", "onCreatePreferences");
            setPreferencesFromResource(C0661R.xml.sound_mode_setting_jpn, rootKey);
            SwitchPreferenceCompat soundMode = (SwitchPreferenceCompat) findPreference("sound_mode_jpn");
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (preferenceScreen == null) {
                return;
            }
            if (Feature.isSupportAlarmSoundMenu()) {
                soundMode.setOnPreferenceClickListener(new C06621());
            } else {
                preferenceScreen.removePreference(soundMode);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("AlertSettingActivity", "onCreate");
        setContentView(C0661R.layout.alert_setting_main);
        getSupportFragmentManager().beginTransaction().replace(C0661R.id.fragment_container, new PrefsFragment()).commit();
        setSupportActionBar((Toolbar) findViewById(C0661R.id.alert_setting_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void onResume() {
        super.onResume();
        ClockUtils.insertSaLog("900");
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (Feature.DEBUG_ENG) {
            Log.secD("AlertSettingActivity", "..onKeyUp.. keyCode : " + keyCode);
        }
        switch (keyCode) {
            case 4:
                finish();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
