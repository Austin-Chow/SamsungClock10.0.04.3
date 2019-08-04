package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import com.samsung.android.os.SemDvfsManager;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.GlobeViewModelListener;
import com.sec.android.app.clockpackage.worldclock.callback.PopupViewListener;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarViewListener;
import com.sec.android.app.clockpackage.worldclock.callback.SgiPlayerListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockGlobeMainData;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;
import java.util.ArrayList;

public class WorldclockGlobeMain extends ClockActivity {
    private Handler mCityCardHandler;
    private ConnectivityManager mConnectivityManager = null;
    private Runnable mDayNightUpdateRunnable = new C08951();
    private final GlobeViewModelListener mGlobeViewModelListener = new GlobeViewModelListener() {
        public void onHideAllPopup(boolean needAnimation) {
            if (WorldclockGlobeMain.this.mPopupViewModel != null) {
                WorldclockGlobeMain.this.mPopupViewModel.hideAllPopup(needAnimation);
            }
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.showCityUnderSelection(Integer.valueOf(-1));
            }
            if (WorldclockGlobeMain.this.mCityCardHandler != null) {
                WorldclockGlobeMain.this.mCityCardHandler.removeCallbacksAndMessages(null);
            }
        }
    };
    private Handler mHandler = new Handler();
    private final BroadcastReceiver mIntentReceiver = new C08995();
    private final BroadcastReceiver mIntentReceiverInternal = new C08984();
    private boolean mIsFirstLaunch = true;
    private Configuration mLastConfiguration;
    private NetworkCallback mNetworkCallback = new C09006();
    private final PopupViewListener mPopupViewListener = new C09039();
    private PopupViewModel mPopupViewModel;
    private Handler mRestoreViewHandler;
    private final SearchBarViewListener mSearchBarListener = new C09028();
    private SearchBarViewModel mSearchBarViewModel;
    private final SgiPlayerListener mSgiPlayerListener = new SgiPlayerListener() {
        public City onCityUnderSelection() {
            return CityManager.findCityByUniqueId(Integer.valueOf(WorldclockGlobeMain.this.mWorldclockGlobeMainData.getUniqueId()));
        }

        public boolean touch(View arg0, MotionEvent evt) {
            return WorldclockGlobeMain.this.mSurfaceFrame == null || WorldclockGlobeMain.this.mSurfaceFrame.onTouch(arg0, evt);
        }

        public void cityTouchedInGlobe(int uniqueId) {
            WorldclockGlobeMain.this.onCityTouchedInGlobe(uniqueId, true, null);
        }
    };
    private GlobeViewModel mSurfaceFrame;
    private final ArrayList<WorldclockCityWeatherInfo> mWeatherInfoList = new ArrayList();
    private WorldclockGlobeMainData mWorldclockGlobeMainData = new WorldclockGlobeMainData();

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$1 */
    class C08951 implements Runnable {
        C08951() {
        }

        public void run() {
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.updateNightImage();
            }
            WorldclockGlobeMain.this.mHandler.postDelayed(this, 6000);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$4 */
    class C08984 extends BroadcastReceiver {
        C08984() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("WorldclockGlobeMain", "BroadcastReceiverInternal = " + action.substring(action.lastIndexOf(46)));
            if ("com.sec.android.app.clockpackage.worldclock.NOTIFY_WORLDCLOCK_CHANGE".equals(action)) {
                WorldclockGlobeMain.this.worldclockRestore();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$5 */
    class C08995 extends BroadcastReceiver {
        C08995() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("WorldclockGlobeMain", "BroadcastReceiver = " + action.substring(action.lastIndexOf(46)));
            Object obj = -1;
            switch (action.hashCode()) {
                case -1513032534:
                    if (action.equals("android.intent.action.TIME_TICK")) {
                        obj = null;
                        break;
                    }
                    break;
                case -267605282:
                    if (action.equals("com.samsung.universalswitch.REQUEST_CLOCK_CURRENT_TAB")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 505380757:
                    if (action.equals("android.intent.action.TIME_SET")) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                case 1:
                    if (CityManager.sIsCityManagerLoad) {
                        WorldclockGlobeMain.this.mSurfaceFrame.updateCityTime();
                    }
                    if (WorldclockGlobeMain.this.mPopupViewModel != null && WorldclockGlobeMain.this.mPopupViewModel.isShowCityPopup()) {
                        Log.secD("WorldclockGlobeMain", "BroadcastReceiver(), update popup time");
                        WorldclockGlobeMain.this.mPopupViewModel.updateCityInfoPopupTime();
                        return;
                    }
                    return;
                case 2:
                    ClockTab.sendCurrentTabIndexIntent(WorldclockGlobeMain.this.getApplicationContext());
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$6 */
    class C09006 extends NetworkCallback {
        C09006() {
        }

        public void onAvailable(Network network) {
            Log.secD("WorldclockGlobeMain", "mNetworkCallback onAvailable");
            WorldclockGlobeMain.this.refreshWeatherInfoOnUiThread();
        }

        public void onLost(Network network) {
            Log.secD("WorldclockGlobeMain", "mNetworkCallback onLost");
            WorldclockGlobeMain.this.refreshWeatherInfoOnUiThread();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$7 */
    class C09017 implements Runnable {
        C09017() {
        }

        public void run() {
            if (WorldclockGlobeMain.this.mPopupViewModel != null && WorldclockGlobeMain.this.mPopupViewModel.isShowCityPopup()) {
                WorldclockGlobeMain.this.mPopupViewModel.updateWeather(WorldclockGlobeMain.this.mWorldclockGlobeMainData.getUniqueId(), null);
                WorldclockGlobeMain.this.mPopupViewModel.updateCityInfoPopupTime();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$8 */
    class C09028 implements SearchBarViewListener {
        C09028() {
        }

        public void onMoveToCity(City city) {
            if (city != null && WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.moveToCity(city, 1.37f);
            }
        }

        public void onCityTouched(int uniqueId) {
            WorldclockGlobeMain.this.onCityTouchedInGlobe(uniqueId, true, null);
        }

        public void onClearPopupTalkBackFocus() {
            if (WorldclockGlobeMain.this.mPopupViewModel != null) {
                WorldclockGlobeMain.this.mPopupViewModel.clearCityPopupTalkbackFocus();
            }
        }

        public void onHideAllPopup(boolean withAnim) {
            if (WorldclockGlobeMain.this.mPopupViewModel != null) {
                WorldclockGlobeMain.this.mPopupViewModel.hideAllPopup(withAnim);
            }
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.showCityUnderSelection(Integer.valueOf(-1));
            }
        }

        public void onUpdateWeatherLogo() {
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.updateWeatherLogo(WorldclockGlobeMain.this.mWorldclockGlobeMainData.getFromWhere());
            }
        }

        public void onSetZoomInOutVisibility() {
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.updateZoomInOut();
            }
        }

        public void setSgiVisibility(int visibility) {
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.setSgiVisibility(visibility);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain$9 */
    class C09039 implements PopupViewListener {
        C09039() {
        }

        public int onGetCityUniqueIdSel() {
            return WorldclockGlobeMain.this.mWorldclockGlobeMainData != null ? WorldclockGlobeMain.this.mWorldclockGlobeMainData.getUniqueId() : -1;
        }

        public void onHideSoftInput() {
            if (WorldclockGlobeMain.this.mSearchBarViewModel != null) {
                WorldclockGlobeMain.this.mSearchBarViewModel.hideSoftInput();
            }
        }

        public void onShowCityUnderSelection(int uniqueID) {
            if (WorldclockGlobeMain.this.mSurfaceFrame != null) {
                WorldclockGlobeMain.this.mSurfaceFrame.showCityUnderSelection(Integer.valueOf(uniqueID));
            }
        }

        public void onSaveDB(City city, WorldclockCityWeatherInfo weatherInfo) {
            addCity(city, weatherInfo);
        }

        public float onCityCardOffset(City city) {
            return WorldclockGlobeMain.this.mSurfaceFrame != null ? WorldclockGlobeMain.this.mSurfaceFrame.cityCardOffset(city) : 0.0f;
        }

        private void addCity(City city, WorldclockCityWeatherInfo weatherInfo) {
            if (WorldclockGlobeMain.this.mWorldclockGlobeMainData == null) {
                Log.secE("WorldclockGlobeMain", "cannot addCity mWorldclockGlobeMainData is null");
                return;
            }
            String fromWhere = WorldclockGlobeMain.this.mWorldclockGlobeMainData.getFromWhere();
            int homeZone = WorldclockGlobeMain.this.mWorldclockGlobeMainData.getHomeZone();
            int widgetId = WorldclockGlobeMain.this.mWorldclockGlobeMainData.getWidgetId();
            if (WorldclockUtils.isFromDualClockDigitalWidget(fromWhere)) {
                WorldclockUtils.addCityToDualClockDigitalWidget(WorldclockGlobeMain.this.getApplicationContext(), city.getUniqueId(), homeZone, widgetId);
                WorldclockGlobeMain.this.finish();
            } else if (WorldclockUtils.isFromDualClockDigitalLaunch(fromWhere)) {
                WorldclockGlobeMain.this.setResult(-1, WorldclockUtils.addCityToDualClockDigitalLaunch(WorldclockGlobeMain.this.getApplicationContext(), city.getUniqueId(), homeZone, widgetId));
                WorldclockGlobeMain.this.finish();
            } else if (WorldclockUtils.isFromDualClockDigitalAOD(fromWhere)) {
                WorldclockUtils.addCityToDualClockAod(WorldclockGlobeMain.this.getApplicationContext(), city.getUniqueId(), homeZone, widgetId);
                WorldclockGlobeMain.this.finish();
            } else if (WorldclockUtils.isFromDualClockDigitalWatch(fromWhere)) {
                WorldclockGlobeMain.this.setResult(-1, WorldclockUtils.addCityDualClockDigitalWatch(WorldclockGlobeMain.this.getApplicationContext(), city));
                WorldclockGlobeMain.this.finish();
            } else if (WorldclockUtils.isFromDualClockDigitalPremiumWatch(fromWhere)) {
                WorldclockGlobeMain.this.setResult(-1, WorldclockUtils.addCityDualClockDigitalPremiumWatch(city));
                WorldclockGlobeMain.this.finish();
            } else if (WorldclockUtils.isFromWorldclockList(fromWhere)) {
                Intent intent = WorldclockUtils.addCity(WorldclockGlobeMain.this.getApplicationContext(), city, WorldclockUtils.isFromWorldclockListWhereCity(fromWhere), WorldclockGlobeMain.this.mWorldclockGlobeMainData.getListPosition(), WorldclockGlobeMain.this.mWorldclockGlobeMainData.getIndex(), WorldclockGlobeMain.this.mWeatherInfoList, weatherInfo);
                if (intent != null) {
                    WorldclockGlobeMain.this.setResult(-1, intent);
                    WorldclockGlobeMain.this.finish();
                }
            } else {
                WorldclockDBManager.saveDB(WorldclockGlobeMain.this.getApplicationContext(), city);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.secD("WorldclockGlobeMain", "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        boolean isSmallestScreenSizeChanged = (this.mLastConfiguration == null || (this.mLastConfiguration.diff(newConfig) & 2048) == 0) ? false : true;
        this.mLastConfiguration = new Configuration(newConfig);
        if (isSmallestScreenSizeChanged) {
            this.mSearchBarViewModel.bindSearchBarList();
            if (this.mSearchBarViewModel.isDropdownListShown()) {
                this.mSearchBarViewModel.changeList();
            }
        }
        if (WorldclockUtils.isFromExternal(this.mWorldclockGlobeMainData.getFromWhere())) {
            getWindow().clearFlags(1024);
        }
        if (this.mPopupViewModel != null && this.mPopupViewModel.isShowCityPopup()) {
            onCityTouchedInGlobe(this.mWorldclockGlobeMainData.getUniqueId(), true, null);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar;
        super.onCreate(savedInstanceState);
        setTitle(C0836R.string.worldclock);
        setContentView(C0836R.layout.worldclock_globe_main);
        Intent i = getIntent();
        ArrayList<WorldclockCityWeatherInfo> weatherInfo = new ArrayList();
        if (savedInstanceState != null) {
            this.mWorldclockGlobeMainData = (WorldclockGlobeMainData) savedInstanceState.getParcelable("WorldclockGlobeMainData");
            weatherInfo = savedInstanceState.getParcelableArrayList("WorldclockWeatherListInfoKey");
            this.mIsFirstLaunch = false;
        } else if (i != null) {
            this.mWorldclockGlobeMainData.setFromWhere(i.getStringExtra("where"));
            this.mWorldclockGlobeMainData.setHomeZone(i.getIntExtra("homezone", 0));
            this.mWorldclockGlobeMainData.setWidgetId(i.getIntExtra("wid", 0));
            this.mWorldclockGlobeMainData.setIndex(i.getIntExtra("index", 0));
            this.mWorldclockGlobeMainData.setUniqueId(i.getIntExtra("uniqueid", 44));
            this.mWorldclockGlobeMainData.setListPosition(i.getIntExtra("ListPosition", -1));
            weatherInfo = i.getParcelableArrayListExtra("WorldclockWeatherListInfoKey");
        }
        if (!(weatherInfo == null || weatherInfo.isEmpty())) {
            this.mWeatherInfoList.clear();
            this.mWeatherInfoList.addAll(weatherInfo);
        }
        final String fromWhere = this.mWorldclockGlobeMainData.getFromWhere();
        final int uniqueId = this.mWorldclockGlobeMainData.getUniqueId();
        Log.secD("WorldclockGlobeMain", "onCreate mFromWhere: " + fromWhere);
        if (WorldclockUtils.isFromExternal(fromWhere)) {
            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            getWindow().clearFlags(1024);
        }
        if (WorldclockUtils.isFromWorldclockList(fromWhere)) {
            actionBar = getSupportActionBar();
            actionBar.setTitle(WorldclockUtils.isFromWorldclockListWhereCity(fromWhere) ? C0836R.string.edit_city : C0836R.string.add_city);
            setTitle(actionBar.getTitle());
        }
        SemDvfsManager gpuMinFreqBooster = SemDvfsManager.createInstance(getApplicationContext(), 16);
        int[] supportedGPUFreqTable = gpuMinFreqBooster.getSupportedFrequency();
        if (supportedGPUFreqTable != null) {
            gpuMinFreqBooster.setDvfsValue(supportedGPUFreqTable[0]);
            gpuMinFreqBooster.acquire(2000);
        }
        initCity(fromWhere);
        this.mRestoreViewHandler = new Handler();
        this.mSurfaceFrame = (GlobeViewModel) findViewById(C0836R.id.worldclock_globe_view_layout);
        this.mPopupViewModel = new PopupViewModel(this, this.mPopupViewListener);
        this.mSearchBarViewModel = new SearchBarViewModel(this, this.mSearchBarListener, fromWhere, 1, 0);
        this.mPopupViewModel.initPopup();
        LayoutParams params = (LayoutParams) this.mSurfaceFrame.getLayoutParams();
        params.topMargin = (WorldclockUtils.isFromExternal(fromWhere) ? 0 : ClockUtils.getActionBarHeight(this)) + getResources().getDimensionPixelSize(C0836R.dimen.worldclock_search_searchbox_layout_height_include_margin_bottom);
        this.mSurfaceFrame.setLayoutParams(params);
        initSearchBarViewModel(savedInstanceState);
        initGlobeViewModel(savedInstanceState, fromWhere, uniqueId);
        getWindow().getDecorView().semSetRoundedCorners(0);
        restoreView(savedInstanceState);
        initReceiver();
        WorldclockDBManager.updateDBLocale(getApplicationContext());
        if (savedInstanceState == null) {
            this.mCityCardHandler = new Handler();
            this.mCityCardHandler.postDelayed(new Runnable() {
                public void run() {
                    if (WorldclockUtils.isFromWorldclockListWhereCity(fromWhere)) {
                        WorldclockGlobeMain.this.onCityTouchedInGlobe(uniqueId, true, null);
                    }
                }
            }, 700);
        }
        if (!WorldclockUtils.isFromExternal(fromWhere)) {
            this.mConnectivityManager = (ConnectivityManager) getSystemService("connectivity");
            this.mConnectivityManager.registerNetworkCallback(new Builder().build(), this.mNetworkCallback);
        }
        this.mLastConfiguration = new Configuration(getResources().getConfiguration());
    }

    public void onPause() {
        super.onPause();
        Log.secD("WorldclockGlobeMain", "onPause");
        this.mHandler.removeCallbacks(this.mDayNightUpdateRunnable);
    }

    public void onStop() {
        super.onStop();
        Log.secD("WorldclockGlobeMain", "onStop");
        if (this.mSearchBarViewModel != null) {
            this.mSearchBarViewModel.cancelLocationToast();
        }
    }

    public void onResume() {
        super.onResume();
        Log.secD("WorldclockGlobeMain", "onResume");
        ClockUtils.insertSaLog("112");
        if (!CityManager.sIsCityManagerLoadForGlobe) {
            initCity(this.mWorldclockGlobeMainData.getFromWhere());
        }
        this.mHandler.postDelayed(this.mDayNightUpdateRunnable, 0);
        if (CityManager.sIsCityRestored) {
            worldclockRestore();
        }
        if (this.mIsFirstLaunch) {
            if (WorldclockUtils.isFromWorldclockList(this.mWorldclockGlobeMainData.getFromWhere()) && WorldclockDBManager.getDBCursorCnt(getApplicationContext()) == 0) {
                WorldclockWeatherUtils.showNetworkUnavailableToast(this);
            }
            this.mIsFirstLaunch = false;
        }
        new Handler().postDelayed(WorldclockGlobeMain$$Lambda$1.lambdaFactory$(this), 50);
        if (StateUtils.isTalkBackEnabled(this)) {
            findViewById(C0836R.id.worldclock_search_edit_box).setFocusableInTouchMode(true);
        } else {
            findViewById(C0836R.id.worldclock_search_edit_box).setFocusableInTouchMode(false);
        }
    }

    private /* synthetic */ void lambda$onResume$0() {
        if (this.mSearchBarViewModel != null && this.mSearchBarViewModel.getAutoText().isCursorVisible()) {
            this.mSearchBarViewModel.showSoftInput();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mSearchBarViewModel.onActivityResultParent(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        if (!this.mSearchBarViewModel.onBackPressedParent(getApplicationContext())) {
            this.mPopupViewModel.hideAllPopup(false);
            insertSaLogNavigateUp();
            super.onBackPressed();
        }
    }

    public void finish() {
        super.finish();
        if (!StateUtils.isContextInDexMode(this)) {
            overridePendingTransition(C0836R.anim.worldclock_animation_hold, C0836R.anim.worldclock_animation_fade_out);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.secD("WorldclockGlobeMain", "onDestroy()");
        deleteReceiver();
        String fromWhere = this.mWorldclockGlobeMainData.getFromWhere();
        if (WorldclockUtils.isFromExternal(fromWhere)) {
            CityManager.sRunActivityInDualclock = false;
        } else if (!WorldclockUtils.isFromWorldclockList(fromWhere)) {
            CityManager.removeCity();
        }
        if (this.mConnectivityManager != null) {
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mDayNightUpdateRunnable);
            this.mDayNightUpdateRunnable = null;
            this.mHandler = null;
        }
        if (this.mSurfaceFrame != null) {
            this.mSurfaceFrame.releaseInstance();
            this.mSurfaceFrame = null;
        }
        if (this.mSearchBarViewModel != null) {
            this.mSearchBarViewModel.releaseInstance();
            this.mSearchBarViewModel = null;
        }
        if (this.mPopupViewModel != null) {
            this.mPopupViewModel.releaseInstance();
            this.mPopupViewModel = null;
        }
        if (this.mCityCardHandler != null) {
            this.mCityCardHandler.removeCallbacksAndMessages(null);
            this.mCityCardHandler = null;
        }
        if (this.mRestoreViewHandler != null) {
            this.mRestoreViewHandler.removeCallbacksAndMessages(null);
            this.mRestoreViewHandler = null;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        insertSaLogNavigateUp();
        finish();
        return true;
    }

    protected void onSaveInstanceState(Bundle outState) {
        Log.secD("WorldclockGlobeMain", "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        this.mSurfaceFrame.saveInstanceState(outState, this.mPopupViewModel.isShowCityPopup());
        outState.putParcelable("WorldclockGlobeMainData", this.mWorldclockGlobeMainData);
        outState.putParcelableArrayList("WorldclockWeatherListInfoKey", this.mWeatherInfoList);
        this.mPopupViewModel.onSaveInstance(outState);
        this.mSearchBarViewModel.onSaveInstance(outState);
    }

    private void insertSaLogNavigateUp() {
        if (this.mSearchBarViewModel.isDropdownListShown()) {
            ClockUtils.insertSaLog("113", "1241");
        } else {
            ClockUtils.insertSaLog("112", "1241");
        }
    }

    private void initSearchBarViewModel(Bundle savedInstanceState) {
        this.mSearchBarViewModel.onRestoreInstance(savedInstanceState);
        if (this.mSearchBarViewModel.isDropdownListShown()) {
            this.mSearchBarViewModel.changeList();
        }
    }

    private void initGlobeViewModel(Bundle saveInstanceState, String fromWhere, int uniqueId) {
        this.mSurfaceFrame.setGlobeViewModel(this.mSearchBarViewModel, this.mGlobeViewModelListener);
        City startCity = CityManager.getCityOfDefaultTime(getApplicationContext());
        if (WorldclockUtils.isFromWorldclockListWhereCity(fromWhere) || ((WorldclockUtils.isFromDualClockDigitalWidget(fromWhere) || WorldclockUtils.isFromDualClockDigitalLaunch(fromWhere)) && uniqueId != -1)) {
            startCity = CityManager.findCityByUniqueId(Integer.valueOf(uniqueId));
        }
        if (saveInstanceState != null) {
            City restoreCity = getRestoreCity(saveInstanceState);
            if (restoreCity != null) {
                startCity = restoreCity;
            }
        }
        if (startCity != null) {
            this.mWorldclockGlobeMainData.setUniqueId(startCity.getUniqueId());
            this.mSurfaceFrame.initSgiView(startCity, saveInstanceState, this.mSgiPlayerListener, fromWhere);
        }
        WorldclockDBManager.updateCityChoice(getApplicationContext());
    }

    private void restoreView(final Bundle savedInstanceState) {
        Log.secD("WorldclockGlobeMain", "restoreView()");
        if (savedInstanceState != null && savedInstanceState.getBoolean("IsShowCityPopup", false)) {
            this.mRestoreViewHandler.postDelayed(new Runnable() {
                public void run() {
                    WorldclockGlobeMain.this.onCityTouchedInGlobe(savedInstanceState.getInt("CityPopupLastCityUniqueId", -1), false, savedInstanceState);
                }
            }, 300);
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("com.samsung.universalswitch.REQUEST_CLOCK_CURRENT_TAB");
        registerReceiver(this.mIntentReceiver, filter, null, null);
        IntentFilter filterInternal = new IntentFilter();
        filterInternal.addAction("com.sec.android.app.clockpackage.worldclock.NOTIFY_WORLDCLOCK_CHANGE");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mIntentReceiverInternal, filterInternal);
    }

    private void deleteReceiver() {
        unregisterReceiver(this.mIntentReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.mIntentReceiverInternal);
    }

    private void worldclockRestore() {
        WorldclockDBManager.updateCityChoice(getApplicationContext());
        this.mSurfaceFrame.reloadCitiesLayer();
        if (this.mPopupViewModel != null && this.mPopupViewModel.isShowCityPopup()) {
            int actionType = WorldclockUtils.getActionType(getApplicationContext(), this.mWorldclockGlobeMainData.getUniqueId(), this.mWorldclockGlobeMainData.getFromWhere());
            this.mPopupViewModel.hideAllPopup(false);
            this.mPopupViewModel.showCityInfoPopup(this.mWorldclockGlobeMainData.getUniqueId(), false, null, this.mWorldclockGlobeMainData.getFromWhere(), actionType);
        }
        CityManager.setIsCityRestored(false);
    }

    private void onCityTouchedInGlobe(int uniqueId, boolean withAnimation, Bundle instance) {
        if (this.mSearchBarViewModel == null || !this.mSearchBarViewModel.isDropdownListShown()) {
            int uniqueIdForAction;
            this.mWorldclockGlobeMainData.setUniqueId(uniqueId);
            if (WorldclockUtils.isFromExternal(this.mWorldclockGlobeMainData.getFromWhere())) {
                uniqueIdForAction = getIntent().getIntExtra("uniqueid", -1);
            } else {
                uniqueIdForAction = uniqueId;
            }
            int actionType = WorldclockUtils.getActionType(getApplicationContext(), uniqueIdForAction, this.mWorldclockGlobeMainData.getFromWhere());
            if (this.mPopupViewModel != null) {
                this.mPopupViewModel.hideAllPopup(withAnimation);
                this.mPopupViewModel.showCityInfoPopup(uniqueId, withAnimation, instance, this.mWorldclockGlobeMainData.getFromWhere(), actionType);
            }
        }
    }

    private void initCity(String fromWhere) {
        if (WorldclockUtils.isFromExternal(fromWhere)) {
            CityManager.initCityForGlobe(getApplicationContext(), true);
            return;
        }
        CityManager.initCityForGlobe(getApplicationContext());
        WorldclockDBManager.updateCityChoice(getApplicationContext());
    }

    private City getRestoreCity(Bundle savedInstanceState) {
        City startCity = null;
        if (savedInstanceState.getBoolean("IsShowCityPopup", false)) {
            startCity = CityManager.findCityByUniqueId(Integer.valueOf(savedInstanceState.getInt("CityPopupLastCityUniqueId", -1)));
            if (startCity != null && savedInstanceState.getBoolean("CurrentLocationPopup", false)) {
                startCity.setDBCurrentLocation(true);
            }
        }
        return startCity;
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

    private void refreshWeatherInfoOnUiThread() {
        findViewById(C0836R.id.pupup_view_layout_id).post(new C09017());
    }
}
