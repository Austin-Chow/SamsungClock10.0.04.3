package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.app.Activity;
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
import android.os.Message;
import android.support.design.C0011R;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.activity.ClockFragment;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockAddButton;
import com.sec.android.app.clockpackage.common.viewmodel.ClockSmartTip;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.WorldclockMainListViewModelListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public abstract class WorldclockCommonFragment extends ClockFragment {
    protected int mActionType = 0;
    protected FragmentActivity mActivity;
    private AppBarLayout mAppbarLayout;
    protected ArrayList<WorldclockCityWeatherInfo> mCityWeatherInfo = new ArrayList();
    private ConnectivityManager mConnectivityManager = null;
    protected String mCurrentTimeZone;
    private WorldclockCommonMessageHandler mHandler = new WorldclockCommonMessageHandler();
    public final BroadcastReceiver mIntentReceiver = new C08912();
    public final BroadcastReceiver mIntentReceiverInternal = new C08901();
    protected boolean mIsBroadcastReceiverRegistered = false;
    private boolean mIsCollapsed = false;
    protected boolean mIsFirstLaunch = true;
    private int mLastOrientationState;
    private boolean mNeedInvalidateOptionsMenu;
    private NetworkCallback mNetworkCallback = new C08923();
    protected final ClockSmartTip mSmartTip = new ClockSmartTip();
    private WorldclockAppBarContentView mWorldclockAppBarContent;
    protected WorldclockMainListViewModel mWorldclockMainListViewModel = null;
    private final WorldclockMainListViewModelListener mWorldclockMainListViewModelListener = new C08945();
    protected Toolbar mWorldclockToolbar;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCommonFragment$1 */
    class C08901 extends BroadcastReceiver {
        C08901() {
        }

        public void onReceive(Context context, Intent intent) {
            if (WorldclockCommonFragment.this.mWorldclockMainListViewModel != null) {
                String action = intent.getAction();
                Log.secD("WorldclockCommonFragment", "Internal Intent action : " + action);
                Object obj = -1;
                switch (action.hashCode()) {
                    case -1291838386:
                        if (action.equals("com.sec.android.app.clockpackage.worldclock.NOTIFY_WEATHER_SETTING_CHANGE")) {
                            obj = 2;
                            break;
                        }
                        break;
                    case 360522543:
                        if (action.equals("com.sec.android.app.clockpackage.worldclock.NOTIFY_WORLDCLOCK_CHANGE")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 477301094:
                        if (action.equals("com.sec.android.app.clockpackage.world.WORLD_ADDCITY_SET")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 1407475043:
                        if (action.equals("com.sec.android.app.clockpackage.worldclock.FINISH_WORLDCLOCK_ACTION_MODE")) {
                            obj = 3;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        WorldclockCommonFragment.this.startWorldclockCityListActivity();
                        return;
                    case 1:
                        WorldclockCommonFragment.this.mWorldclockMainListViewModel.worldclockRestore();
                        return;
                    case 2:
                        if (WorldclockUtils.isWorldclockTab() && WorldclockCommonFragment.this.isResumed() && WorldclockCommonFragment.this.mWorldclockMainListViewModel.getItems() != null && !WorldclockCommonFragment.this.mWorldclockMainListViewModel.getItems().isEmpty()) {
                            WorldclockWeatherUtils.showNetworkUnavailableToast(WorldclockCommonFragment.this.mActivity);
                        }
                        WorldclockCommonFragment.this.mWorldclockMainListViewModel.refreshWeatherInfo();
                        return;
                    case 3:
                        WorldclockCommonFragment.this.mWorldclockMainListViewModel.finishActionMode();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCommonFragment$2 */
    class C08912 extends BroadcastReceiver {
        C08912() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("WorldclockCommonFragment", "Intent action : " + action);
            if (WorldclockCommonFragment.this.mWorldclockMainListViewModel != null) {
                int i = -1;
                switch (action.hashCode()) {
                    case -1513032534:
                        if (action.equals("android.intent.action.TIME_TICK")) {
                            i = 2;
                            break;
                        }
                        break;
                    case 502473491:
                        if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                            i = 1;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            i = 0;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                    case 1:
                        WorldclockCommonFragment.this.mWorldclockMainListViewModel.refreshCityList();
                        WorldclockCommonFragment.this.mWorldclockAppBarContent.updateTimeZoneInfo();
                        return;
                    case 2:
                        WorldclockCommonFragment.this.mHandler.sendEmptyMessageDelayed(1, 200);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCommonFragment$3 */
    class C08923 extends NetworkCallback {
        C08923() {
        }

        public void onAvailable(Network network) {
            Log.secD("WorldclockCommonFragment", "mNetworkCallback onAvailable");
            WorldclockCommonFragment.this.refreshWeatherInfoOnUiThread();
        }

        public void onLost(Network network) {
            Log.secD("WorldclockCommonFragment", "mNetworkCallback onLost");
            WorldclockCommonFragment.this.refreshWeatherInfoOnUiThread();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCommonFragment$4 */
    class C08934 implements Runnable {
        C08934() {
        }

        public void run() {
            if (WorldclockCommonFragment.this.mWorldclockMainListViewModel != null) {
                WorldclockCommonFragment.this.mWorldclockMainListViewModel.refreshWeatherInfo();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCommonFragment$5 */
    class C08945 implements WorldclockMainListViewModelListener {
        C08945() {
        }

        public void onStartActivityForResult(Intent intent) {
            WorldclockCommonFragment.this.startActivityForResult(intent, 0);
        }

        public View onGetFragmentView() {
            return WorldclockCommonFragment.this.mFragmentView;
        }

        public Toolbar onGetToolbar() {
            return WorldclockCommonFragment.this.mWorldclockToolbar;
        }

        public void onLaunchSmartTip() {
            WorldclockCommonFragment.this.launchSmartTip();
        }

        public void onActionModeUpdate(int selectCount) {
            if (WorldclockCommonFragment.this.mWorldclockAppBarContent != null) {
                WorldclockCommonFragment.this.mWorldclockAppBarContent.onActionModeUpdate(selectCount);
            }
        }

        public void onActionModeFinished() {
            if (WorldclockCommonFragment.this.mWorldclockAppBarContent != null) {
                WorldclockCommonFragment.this.mWorldclockAppBarContent.onActionModeFinished();
            }
            if (WorldclockCommonFragment.this.mNeedInvalidateOptionsMenu) {
                WorldclockCommonFragment.this.mActivity.invalidateOptionsMenu();
                WorldclockCommonFragment.this.mNeedInvalidateOptionsMenu = false;
            }
            WorldclockCommonFragment.this.setMenuItem();
        }
    }

    private static final class WorldclockCommonMessageHandler extends Handler {
        private final WeakReference<WorldclockCommonFragment> mWeakReference;

        private WorldclockCommonMessageHandler(WorldclockCommonFragment parent) {
            this.mWeakReference = new WeakReference(parent);
        }

        public void handleMessage(Message msg) {
            WorldclockCommonFragment refs = (WorldclockCommonFragment) this.mWeakReference.get();
            if (refs != null) {
                refs.handleMessage(msg);
            }
        }
    }

    protected abstract void launchSmartTip();

    protected abstract void setMenuItem();

    protected abstract void startWorldclockCityListActivity();

    protected abstract void startWorldclockGlobeMain();

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mActivity = (FragmentActivity) context;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("WorldclockCommonFragment", "onCreate()");
        initCity();
        initDefaultZone();
        initReceiver();
        this.mCurrentTimeZone = Calendar.getInstance().getTimeZone().getID();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.secD("WorldclockCommonFragment", "onCreateView()");
        this.mFragmentView = inflater.inflate(C0836R.layout.worldclock_main, container, false);
        this.mAppbarLayout = (AppBarLayout) this.mFragmentView.findViewById(C0836R.id.worldclock_app_bar);
        this.mWorldclockMainListViewModel = new WorldclockMainListViewModel(this.mAppbarLayout, (AppCompatActivity) this.mActivity, this.mWorldclockMainListViewModelListener, this.mCityWeatherInfo);
        this.mWorldclockMainListViewModel.updateList(savedInstanceState == null);
        WorldclockDBManager.updateDBLocale(this.mActivity.getApplicationContext());
        this.mWorldclockToolbar = (Toolbar) this.mFragmentView.findViewById(C0836R.id.toolbar);
        ((AppCompatActivity) this.mActivity).setSupportActionBar(this.mWorldclockToolbar);
        ((CollapsingToolbarLayout) this.mFragmentView.findViewById(C0836R.id.collapsing_toolbar)).setCustomAccessibility(true);
        this.mWorldclockAppBarContent = (WorldclockAppBarContentView) this.mAppbarLayout.findViewById(C0836R.id.worldclock_app_bar_title);
        CoordinatorLayout coordinator = (CoordinatorLayout) this.mFragmentView.findViewById(C0836R.id.worldclock_main_coordinator);
        try {
            coordinator.semSetRoundedCorners(12);
            coordinator.semSetRoundedCornerColor(12, this.mActivity.getColor(C0836R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("WorldclockCommonFragment", "NoSuchMethodError : " + e.toString());
        }
        float defaultCollapseHeight = (float) getResources().getDimensionPixelSize(C0011R.dimen.sesl_action_bar_default_height_padding);
        this.mAppbarLayout.addOnOffsetChangedListener(WorldclockCommonFragment$$Lambda$1.lambdaFactory$(this));
        this.mLastOrientationState = this.mActivity.getResources().getConfiguration().orientation;
        onUpdateAppbarLayout();
        this.mWorldclockAppBarContent.setOrientation();
        this.mConnectivityManager = (ConnectivityManager) this.mActivity.getApplicationContext().getSystemService("connectivity");
        this.mConnectivityManager.registerNetworkCallback(new Builder().build(), this.mNetworkCallback);
        ((ClockAddButton) this.mFragmentView.findViewById(C0836R.id.worldclock_empty_view)).setOnClickListener(WorldclockCommonFragment$$Lambda$2.lambdaFactory$(this));
        return this.mFragmentView;
    }

    private /* synthetic */ void lambda$onCreateView$0(AppBarLayout layout, int offset) {
        View actionModeTitle = null;
        int i = 1;
        int layoutPosition = Math.abs(layout.getTop());
        if (layoutPosition >= layout.getTotalScrollRange()) {
            if (!this.mIsCollapsed) {
                this.mSmartTip.dismissSmartTips();
            }
            this.mWorldclockAppBarContent.setVisibility(8);
            this.mIsCollapsed = true;
        } else {
            if (this.mIsCollapsed) {
                this.mSmartTip.dismissSmartTips();
            }
            this.mWorldclockAppBarContent.setVisibility(0);
            this.mIsCollapsed = false;
        }
        TextView selectItemText = this.mWorldclockMainListViewModel == null ? null : this.mWorldclockMainListViewModel.getSelectItemText();
        if (selectItemText != null) {
            if (!this.mIsCollapsed) {
                i = 2;
            }
            selectItemText.setImportantForAccessibility(i);
        }
        if (this.mWorldclockMainListViewModel != null) {
            actionModeTitle = this.mWorldclockMainListViewModel.getMultiSelectModeTitle();
        }
        this.mWorldclockAppBarContent.updateColor(actionModeTitle, layoutPosition, this.mIsCollapsed);
    }

    private /* synthetic */ void lambda$onCreateView$1(View v) {
        ClockUtils.insertSaLog("110", "1252");
        startWorldclockGlobeMain();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mWorldclockAppBarContent.setVisibility(8);
    }

    public void onResume() {
        super.onResume();
        Log.secD("WorldclockCommonFragment", "onResume()");
        if (!(Calendar.getInstance().getTimeZone() == null || Calendar.getInstance().getTimeZone().getID().equals(this.mCurrentTimeZone))) {
            this.mCurrentTimeZone = Calendar.getInstance().getTimeZone().getID();
        }
        Intent i = this.mActivity.getIntent();
        if (i != null) {
            if ("com.sec.android.app.clockpackage.WORLD_ACTION".equals(i.getAction())) {
                int addCity = i.getIntExtra("ADD_CITY", 0);
                i.removeExtra("ADD_CITY");
                if (addCity == 100) {
                    LocalBroadcastManager.getInstance(this.mActivity.getApplicationContext()).sendBroadcast(new Intent("com.sec.android.app.clockpackage.world.WORLD_ADDCITY_SET"));
                }
            }
        }
        if (CityManager.sIsCityRestored && this.mWorldclockMainListViewModel != null) {
            this.mWorldclockMainListViewModel.worldclockRestore();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            this.mActionType = data.getIntExtra("city_result_type", 0);
            if (this.mWorldclockMainListViewModel != null) {
                if (this.mActionType == 1) {
                    this.mWorldclockMainListViewModel.setListWeatherInfo(data);
                } else {
                    this.mWorldclockMainListViewModel.updateListWeatherInfo(data);
                }
                this.mWorldclockMainListViewModel.makeListItem();
                this.mWorldclockMainListViewModel.bindList();
                if (this.mActionType == 1) {
                    this.mWorldclockMainListViewModel.scrollToFirst();
                }
            }
            this.mActionType = 0;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation != this.mLastOrientationState || StateUtils.isCanNotExpandAboutExtendedAppBar(this.mActivity)) {
            this.mIsCollapsed = true;
            this.mLastOrientationState = newConfig.orientation;
        } else {
            this.mIsCollapsed = newConfig.orientation == 2;
        }
        this.mHandler.sendEmptyMessageDelayed(0, 100);
        if (StateUtils.isContextInDexMode(this.mActivity) && !this.mActivity.isInMultiWindowMode()) {
            if (this.mAppbarLayout.isCollapsed()) {
                this.mAppbarLayout.setExpanded(false);
            } else {
                this.mAppbarLayout.setExpanded(true);
            }
        }
        this.mWorldclockAppBarContent.updateTextSize();
        this.mWorldclockMainListViewModel.setWorldClockListWidth(newConfig);
        if (!this.mWorldclockMainListViewModel.isActionMode()) {
            this.mWorldclockAppBarContent.setOrientation();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onPause() {
        super.onPause();
        ClockUtils.setPrefDefault(this.mActivity.getApplicationContext(), "worldclockPreset");
        Log.secD("WorldclockCommonFragment", "onPause()");
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (this.mWorldclockMainListViewModel.isActionMode()) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
            this.mNeedInvalidateOptionsMenu = true;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mConnectivityManager != null) {
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.secD("WorldclockCommonFragment", "onDestroy()");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
        }
        if (this.mWorldclockMainListViewModel != null) {
            this.mWorldclockMainListViewModel.releaseInstance();
        }
        this.mWorldclockMainListViewModel = null;
        deleteReceiver();
    }

    public void onStartActionMode(BottomNavigationView bottomView) {
        if (this.mWorldclockMainListViewModel != null && bottomView != null) {
            this.mWorldclockMainListViewModel.initBottomNavigationView(bottomView);
        }
    }

    public void onFinishActionMode() {
        if (this.mWorldclockMainListViewModel != null) {
            this.mWorldclockMainListViewModel.removeBottomNavigationView();
        }
    }

    public void onTabSelected() {
        this.mHandler.sendEmptyMessageDelayed(0, 100);
        if (this.mWorldclockMainListViewModel != null) {
            this.mWorldclockMainListViewModel.setFocusable(true);
        }
    }

    public void onTabUnselected() {
        if (this.mWorldclockMainListViewModel != null) {
            this.mWorldclockMainListViewModel.setFocusable(false);
        }
    }

    private void onUpdateAppbarLayout() {
        if (this.mIsCollapsed) {
            this.mAppbarLayout.setExpanded(false, false);
        }
        this.mAppbarLayout.requestLayout();
    }

    public void initDefaultZone() {
        if (!ClockUtils.getPrefDefault(this.mActivity.getApplication(), "worldclockPreset") && WorldclockDBManager.getDBCursorCnt(this.mActivity.getApplicationContext()) == 0) {
            setDBFactory();
        }
    }

    public void initReceiver() {
        Log.secD("WorldclockCommonFragment", "initReceiver()");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        filter.addAction("android.intent.action.TIME_TICK");
        this.mActivity.registerReceiver(this.mIntentReceiver, filter, null, null);
        IntentFilter filterInternal = new IntentFilter();
        filterInternal.addAction("com.sec.android.app.clockpackage.world.WORLD_ADDCITY_SET");
        filterInternal.addAction("com.sec.android.app.clockpackage.worldclock.NOTIFY_WORLDCLOCK_CHANGE");
        filterInternal.addAction("com.sec.android.app.clockpackage.worldclock.NOTIFY_WEATHER_SETTING_CHANGE");
        filterInternal.addAction("com.sec.android.app.clockpackage.worldclock.FINISH_WORLDCLOCK_ACTION_MODE");
        LocalBroadcastManager.getInstance(this.mActivity.getApplicationContext()).registerReceiver(this.mIntentReceiverInternal, filterInternal);
        this.mIsBroadcastReceiverRegistered = true;
    }

    public void deleteReceiver() {
        Log.secD("WorldclockCommonFragment", "deleteReceiver()");
        if (this.mIsBroadcastReceiverRegistered) {
            this.mActivity.unregisterReceiver(this.mIntentReceiver);
            LocalBroadcastManager.getInstance(this.mActivity.getApplicationContext()).unregisterReceiver(this.mIntentReceiverInternal);
            this.mIsBroadcastReceiverRegistered = false;
        }
    }

    public void setDBFactory() {
        ArrayList<City> defaultCities = new ArrayList();
        defaultCities.add(CityManager.getCityOfDefaultTime(this.mActivity.getApplicationContext()));
        defaultCities.add(CityManager.get2ndDefaultCity(this.mActivity.getApplicationContext()));
        Iterator it = defaultCities.iterator();
        while (it.hasNext()) {
            City city = (City) it.next();
            if (city != null && WorldclockDBManager.isDuplication(this.mActivity.getApplicationContext(), city.getUniqueId())) {
                Log.secD("WorldclockCommonFragment", "Default city already existed." + city.getName());
            } else if (!(city == null || WorldclockDBManager.saveDB(this.mActivity.getApplicationContext(), city))) {
                Log.secD("WorldclockCommonFragment", "Adding default city is failed.");
            }
        }
    }

    private void initCity() {
        CityManager.initCity(this.mActivity.getApplicationContext());
        WorldclockDBManager.updateCityChoice(this.mActivity.getApplicationContext());
    }

    private void refreshWeatherInfoOnUiThread() {
        this.mFragmentView.findViewById(C0836R.id.worldclock_list).post(new C08934());
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                onUpdateAppbarLayout();
                return;
            case 1:
                this.mWorldclockMainListViewModel.refreshCityTimeDiffInfo();
                return;
            default:
                return;
        }
    }
}
