package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarViewListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;

public class WorldclockCityListActivity extends ClockActivity {
    private final String TAG = "WorldclockCityListActivity";
    private String mCityCountryName;
    private String mFromWhere = "";
    private int mHomeZone;
    private int mIndex;
    private SearchBarViewListener mSearchBarListener = new C08891();
    private SearchBarViewModel mSearchBarViewModel;
    private int mWidgetID;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCityListActivity$1 */
    class C08891 implements SearchBarViewListener {
        C08891() {
        }

        public void onMoveToCity(City city) {
        }

        public void onCityTouched(int uniqueId) {
        }

        public void onClearPopupTalkBackFocus() {
        }

        public void onHideAllPopup(boolean withAnim) {
        }

        public void onUpdateWeatherLogo() {
        }

        public void onSetZoomInOutVisibility() {
        }

        public void setSgiVisibility(int visibility) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("WorldclockCityListActivity", "onCreate()");
        setContentView(C0836R.layout.worldclock_list_activity_main);
        initMapData();
        if (this.mIndex == 0) {
            setTitle(C0836R.string.add_city);
        } else {
            setTitle(C0836R.string.edit_city);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && WorldclockUtils.isFromExternal(this.mFromWhere)) {
            actionBar.hide();
        }
        this.mSearchBarViewModel = new SearchBarViewModel(this, this.mSearchBarListener, this.mFromWhere, 2, this.mIndex);
        this.mSearchBarViewModel.initMassData(this.mCityCountryName, this.mHomeZone, this.mWidgetID);
    }

    private void initMapData() {
        Intent i = getIntent();
        this.mFromWhere = i.getStringExtra("where");
        this.mIndex = i.getIntExtra("index", 0);
        Log.secD("WorldclockCityListActivity", "Intent mFromWhere: " + this.mFromWhere);
        if (!CityManager.sIsCityManagerLoad) {
            CityManager.initCity(getApplicationContext());
        }
        if (CityManager.sCitiesByRawOffsetEn == null) {
            CityManager.loadCitiesEn(getApplicationContext());
        }
        if (this.mIndex != 0) {
            setTitle(C0836R.string.edit_city);
        }
        this.mCityCountryName = i.getStringExtra("cityname");
        int cityUniqueID = i.getIntExtra("uniqueid", 44);
        this.mHomeZone = i.getIntExtra("homezone", 0);
        this.mWidgetID = i.getIntExtra("wid", 0);
        if (CityManager.findCityObjectByName(this.mCityCountryName) == null) {
            this.mCityCountryName = CityManager.findCityCountryNameByUniqueId(Integer.valueOf(cityUniqueID));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 34:
            case 84:
                if ((keyCode != 34 || event.isCtrlPressed()) && this.mSearchBarViewModel.getAutoText() != null) {
                    if (!this.mSearchBarViewModel.getAutoText().isFocusableInTouchMode()) {
                        this.mSearchBarViewModel.getAutoText().setFocusableInTouchMode(true);
                    }
                    this.mSearchBarViewModel.getAutoText().requestFocus();
                    this.mSearchBarViewModel.getAutoText().setCursorVisible(true);
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();
        Log.secD("WorldclockCityListActivity", "onResume()");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onPause() {
        super.onPause();
        Log.secD("WorldclockCityListActivity", "onPause()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.secD("WorldclockCityListActivity", "onDestroy()");
        if (this.mSearchBarViewModel != null) {
            this.mSearchBarViewModel.releaseInstance();
            this.mSearchBarViewModel = null;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        if (this.mSearchBarViewModel != null) {
            this.mSearchBarViewModel.releaseInstance();
        }
        finish();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mSearchBarViewModel != null) {
            this.mSearchBarViewModel.onActivityResultParent(requestCode, resultCode, data);
        }
    }
}
