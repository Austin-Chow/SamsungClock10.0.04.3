package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockSubAppBar;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;

public class WeatherSettingsActivity extends ClockActivity {
    private final String TAG = "WeatherSettingsActivity";
    private Context mContext = null;
    private boolean mCurrentSwitchStatus;
    private int mCurrentUnit;
    private SharedPreferences mPref = null;
    private boolean mPreviousSwitchStatus;
    private int mPreviousUnit;
    private ListPopupWindow mUnitType;
    private View mWeatherUnitLayout;
    private TextView mWeatherUnitSecondary;

    private static class RoleDescriptionAccessibilityDelegate extends AccessibilityDelegateCompat {
        private final String mRoleDescription;

        private RoleDescriptionAccessibilityDelegate(String roleDescription) {
            this.mRoleDescription = roleDescription;
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setRoleDescription(this.mRoleDescription);
        }
    }

    public class WeatherUnitPopupAdapter extends ArrayAdapter<String> {
        final Context context;
        String[] items;

        private WeatherUnitPopupAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Typeface font;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.context).inflate(C0836R.layout.worldclock_weather_unit_list_popup, parent, false);
            }
            CheckedTextView tv = (CheckedTextView) convertView.findViewById(16908308);
            tv.setText(this.items[position]);
            if (position == WeatherSettingsActivity.this.mCurrentUnit) {
                font = Typeface.create("sec-roboto-light", 1);
                tv.setTextColor(WeatherSettingsActivity.this.mContext.getColor(C0836R.color.primary_dark_color));
                tv.setChecked(true);
            } else {
                font = Typeface.create("sec-roboto-light", 0);
                tv.setTextColor(WeatherSettingsActivity.this.mContext.getColor(C0836R.color.worldclock_spinner_item_unselected_color));
                tv.setChecked(false);
            }
            tv.setTypeface(font);
            return convertView;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        boolean z;
        super.onCreate(savedInstanceState);
        Log.secD("WeatherSettingsActivity", "onCreate");
        this.mContext = this;
        setContentView(C0836R.layout.worldclock_weather_settings);
        setSupportActionBar((Toolbar) findViewById(C0836R.id.worldclock_weather_setting_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.mWeatherUnitLayout = findViewById(C0836R.id.unit_layout);
        try {
            this.mWeatherUnitLayout.semSetRoundedCorners(15);
            this.mWeatherUnitLayout.semSetRoundedCornerColor(15, getColor(C0836R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("WeatherSettingsActivity", "NoSuchMethodError : " + e);
        }
        this.mWeatherUnitSecondary = (TextView) findViewById(C0836R.id.unit_secondary_text);
        ViewCompat.setAccessibilityDelegate(this.mWeatherUnitSecondary, new RoleDescriptionAccessibilityDelegate(getString(C0836R.string.dropdown_list)));
        this.mUnitType = new ListPopupWindow(this, null);
        this.mUnitType.setInputMethodMode(2);
        this.mPref = getSharedPreferences("ClocksTabStatus", 0);
        SharedPreferences sharedPreferences = this.mPref;
        String str = "WeatherSwitch";
        if (Feature.isWeatherDefaultOff()) {
            z = false;
        } else {
            z = true;
        }
        z = sharedPreferences.getBoolean(str, z);
        this.mCurrentSwitchStatus = z;
        this.mPreviousSwitchStatus = z;
        int i = this.mPref.getInt("WeatherUnit", WorldclockWeatherUtils.getDefaultTempScale(this));
        this.mCurrentUnit = i;
        this.mPreviousUnit = i;
        this.mWeatherUnitSecondary.setText(this.mCurrentUnit == 1 ? getString(C0836R.string.weather_unit_type_fahrenheit) : getString(C0836R.string.weather_unit_type_celsius));
        StringBuilder contentDescription = new StringBuilder();
        contentDescription.append(this.mCurrentUnit == 1 ? getString(C0836R.string.weather_unit_type_fahrenheit) : getString(C0836R.string.weather_unit_type_celsius)).append(',').append(' ').append(getString(C0836R.string.change_temperature_unit));
        this.mWeatherUnitSecondary.setContentDescription(contentDescription);
        addSubAppBarView();
        setWeatherUnitEnable();
        updateListBackground();
    }

    protected void onResume() {
        super.onResume();
        ClockUtils.insertSaLog("114");
        setClickListener();
    }

    protected void onPause() {
        super.onPause();
        if (this.mUnitType != null && this.mUnitType.isShowing()) {
            this.mUnitType.dismiss();
        }
        ClockUtils.insertSaStatusLog(getApplicationContext(), "5102", this.mCurrentSwitchStatus ? 1 : 0);
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.secD("WeatherSettingsActivity", "onDestroy()");
        if (this.mWeatherUnitLayout != null) {
            this.mWeatherUnitLayout.setOnClickListener(null);
            this.mWeatherUnitLayout = null;
        }
    }

    private void addSubAppBarView() {
        ClockSubAppBar subAppBar = (ClockSubAppBar) findViewById(C0836R.id.sub_appbar_layout);
        subAppBar.setSubAppBarPressListener(WeatherSettingsActivity$$Lambda$1.lambdaFactory$(this));
        subAppBar.setChecked(this.mCurrentSwitchStatus);
    }

    private /* synthetic */ void lambda$addSubAppBarView$0(boolean isChecked) {
        String str;
        this.mCurrentSwitchStatus = isChecked;
        Log.secD("WeatherSettingsActivity", "Weather settings switch isChecked : " + isChecked);
        if (!StateUtils.isNetWorkConnected(getApplicationContext()) && isChecked) {
            Toast.makeText(this.mContext, this.mContext.getString(C0836R.string.ss_weather_services_not_available), 1).show();
        }
        if (isChecked) {
            str = getResources().getString(C0836R.string.switch_on);
        } else {
            str = getResources().getString(C0836R.string.switch_off);
        }
        ((TextView) findViewById(C0836R.id.sub_appbar_textview)).setText(str);
        setWeatherPreference();
        setWeatherUnitEnable();
        ClockUtils.insertSaLog("114", "1106", isChecked ? "1" : "0");
    }

    private /* synthetic */ void lambda$setClickListener$1(View view) {
        showPopup();
    }

    private void setClickListener() {
        this.mWeatherUnitLayout.setOnClickListener(WeatherSettingsActivity$$Lambda$2.lambdaFactory$(this));
        this.mUnitType.setAdapter(new WeatherUnitPopupAdapter(this.mContext, C0836R.layout.worldclock_weather_unit_list_popup, getResources().getStringArray(C0836R.array.weather_unit_type)));
        this.mUnitType.setModal(true);
        this.mUnitType.setAnchorView(this.mWeatherUnitLayout);
        this.mUnitType.setOnItemClickListener(WeatherSettingsActivity$$Lambda$3.lambdaFactory$(this));
    }

    private /* synthetic */ void lambda$setClickListener$2(AdapterView parent, View view, int position, long id) {
        Log.secD("WeatherSettingsActivity", "onItemClick : " + position + ", id : " + id);
        changeUnitType(position);
        dismissPopup();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.secD("WeatherSettingsActivity", "..onKeyUp.. keyCode : " + keyCode);
        switch (keyCode) {
            case 4:
                if (!(this.mPreviousUnit == this.mCurrentUnit && this.mPreviousSwitchStatus == this.mCurrentSwitchStatus)) {
                    updateWeatherRequest();
                }
                ClockUtils.insertSaLog("114", "1241");
                break;
            case 24:
            case 25:
            case 82:
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void setWeatherPreference() {
        Log.secD("WeatherSettingsActivity", "setWeatherPreference");
        this.mPref = getSharedPreferences("ClocksTabStatus", 0);
        Editor sPrefEditor = this.mPref.edit();
        try {
            sPrefEditor.putBoolean("WeatherSwitch", this.mCurrentSwitchStatus);
            Log.secD("WeatherSettingsActivity", "WEATHER_SWITCH : " + this.mCurrentSwitchStatus);
            sPrefEditor.putInt("WeatherUnit", this.mCurrentUnit);
            Log.secD("WeatherSettingsActivity", "WEATHER_UNIT(0-C 1-F) : " + this.mCurrentUnit);
            sPrefEditor.apply();
        } catch (NullPointerException e) {
            Log.secD("WeatherSettingsActivity", "setWeatherPreference NullPointException" + e.toString());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        if (!(this.mPreviousUnit == this.mCurrentUnit && this.mPreviousSwitchStatus == this.mCurrentSwitchStatus)) {
            updateWeatherRequest();
        }
        finish();
        ClockUtils.insertSaLog("114", "1241");
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateListBackground();
    }

    private void updateListBackground() {
        ClockUtils.updateListBothSideMargin(this.mContext, (LinearLayout) findViewById(C0836R.id.worldclock_list_background));
    }

    private void updateWeatherRequest() {
        Log.secD("WeatherSettingsActivity", "updateWeatherRequest()");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("com.sec.android.app.clockpackage.worldclock.NOTIFY_WEATHER_SETTING_CHANGE"));
    }

    public void changeUnitType(int unitType) {
        Log.secD("WeatherSettingsActivity", "changeUnitType 0F1C : " + unitType);
        if (this.mUnitType != null) {
            Log.secD("WeatherSettingsActivity", "changeUnitType before : " + this.mUnitType.getSelectedItemPosition() + " mCurrentUnit :" + this.mCurrentUnit);
            this.mCurrentUnit = unitType;
            Log.secD("WeatherSettingsActivity", "changeUnitType after : " + this.mUnitType.getSelectedItemPosition() + " mCurrentUnit :" + this.mCurrentUnit);
        }
        this.mWeatherUnitSecondary.setText(this.mCurrentUnit == 1 ? getString(C0836R.string.weather_unit_type_fahrenheit) : getString(C0836R.string.weather_unit_type_celsius));
        StringBuilder contentDescription = new StringBuilder();
        contentDescription.append(this.mCurrentUnit == 1 ? getString(C0836R.string.weather_unit_type_fahrenheit) : getString(C0836R.string.weather_unit_type_celsius)).append(',').append(' ').append(getString(C0836R.string.change_temperature_unit));
        this.mWeatherUnitSecondary.setContentDescription(contentDescription);
        if (this.mPreviousUnit != this.mCurrentUnit) {
            setWeatherPreference();
        }
        ClockUtils.insertSaLog("114", "1281", this.mWeatherUnitSecondary.getText().toString());
    }

    private void showPopup() {
        if (this.mUnitType != null) {
            int marginTop = getResources().getDimensionPixelSize(C0836R.dimen.worldclock_weather_settings_popup_top_offset);
            this.mUnitType.setDropDownGravity(GravityCompat.START);
            this.mUnitType.setVerticalOffset(marginTop);
            this.mUnitType.setContentWidth(getResources().getDimensionPixelSize(C0836R.dimen.worldclock_weather_settings_popup_width));
            if (!isFinishing()) {
                this.mUnitType.show();
            }
        }
    }

    private void dismissPopup() {
        Log.secD("WeatherSettingsActivity", "dismissPopup");
        if (this.mUnitType != null && this.mUnitType.isShowing()) {
            this.mUnitType.dismiss();
        }
    }

    private void setWeatherUnitEnable() {
        this.mWeatherUnitLayout = findViewById(C0836R.id.unit_layout);
        if (this.mCurrentSwitchStatus) {
            this.mWeatherUnitLayout.setVisibility(0);
        } else {
            this.mWeatherUnitLayout.setVisibility(8);
        }
    }
}
