package com.sec.android.app.clockpackage.alarm.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.view.AlarmListView;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.activity.ClockFragment;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;

public class AlarmMainFragment extends ClockFragment implements LoaderCallbacks<Cursor> {
    private FragmentActivity mActivity;
    private MenuItem mAddMenu;
    private int mAlarmLaunchMode = 2;
    private AlarmListView mAlarmListView;
    private int mAlarmPresetPrefState = 2;
    private AppBarLayout mAppBarLayout;
    private final BackgroundThread mBackGroundThread = new BackgroundThread();
    private Context mContext;
    private int mCreatedIdFromAlarmEdit;
    private MenuItem mCustomizeServiceMenu;
    private MenuItem mDeleteMenu;
    private InternalReceiver mInternalIntentReceiver;
    private boolean mIsConfigChanged = false;
    private boolean mIsCursorLoadFinished = false;
    private boolean mIsFirstRestart;
    private boolean mIsSelectTab = false;
    private MenuItem mPowerOnOffMenu;
    private MyReceiver mReceiver;
    private View mRootView;
    private MenuItem mSettingMenu;

    /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmMainFragment$1 */
    class C05171 implements OnOffsetChangedListener {
        private final int APP_BAR_COLLAPSED = 1;
        private final int APP_BAR_EXPANDED = 0;
        private final int APP_BAR_SCROLLING = 2;
        private int AppBarState = 0;

        C05171() {
        }

        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            float offsetAlpha = appBarLayout.getY() / ((float) appBarLayout.getTotalScrollRange());
            if (AlarmMainFragment.this.mAlarmListView != null) {
                if (offsetAlpha < -0.5f) {
                    AlarmMainFragment.this.mAlarmListView.setActionBarTitle(-1.0f * offsetAlpha);
                } else if (StateUtils.isCanNotExpandAboutExtendedAppBar(AlarmMainFragment.this.mActivity)) {
                    AlarmMainFragment.this.mAlarmListView.setActionBarTitle(1.0f);
                } else {
                    AlarmMainFragment.this.mAlarmListView.setActionBarTitle(0.0f);
                }
            }
            if (AlarmMainFragment.this.mIsSelectTab) {
                AlarmMainFragment.this.mAppBarLayout.requestLayout();
                AlarmMainFragment.this.mIsSelectTab = false;
            } else if (AlarmMainFragment.this.mIsConfigChanged) {
                AlarmMainFragment.this.mAppBarLayout.requestLayout();
                AlarmMainFragment.this.mIsConfigChanged = false;
            }
            if (!StateUtils.isCanNotExpandAboutExtendedAppBar(AlarmMainFragment.this.mActivity)) {
                if (verticalOffset == 0) {
                    if (this.AppBarState != 0) {
                        ClockUtils.insertSaLog("101", "1011", "Expand");
                    }
                    this.AppBarState = 0;
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (this.AppBarState != 1) {
                        ClockUtils.insertSaLog("101", "1011", "Collapse");
                    }
                    this.AppBarState = 1;
                } else {
                    this.AppBarState = 2;
                }
            }
        }
    }

    class BackgroundThread extends Thread {
        BackgroundThread() {
        }

        public void run() {
            AlarmMainFragment.this.mAlarmPresetPrefState = AlarmMainFragment.this.mContext.getSharedPreferences("isSetDefault", 0).getInt("alarmBootState", 0);
            if (AlarmMainFragment.this.mAlarmPresetPrefState == 0) {
                if (AlarmProvider.getAlarmCount(AlarmMainFragment.this.mContext) == 0) {
                    AlarmMainFragment.this.mAlarmPresetPrefState = 1;
                    AlarmUtil.setPresetAlarm(AlarmMainFragment.this.mContext);
                } else {
                    AlarmMainFragment.this.mAlarmPresetPrefState = 2;
                }
            }
            AlarmMainFragment.this.intentRegisterReceiver();
            AlarmProvider.enableNextAlert(AlarmMainFragment.this.mContext);
            AlarmMainFragment.this.setExtendedAppbarListener();
        }
    }

    private class InternalReceiver extends BroadcastReceiver {

        /* renamed from: com.sec.android.app.clockpackage.alarm.activity.AlarmMainFragment$InternalReceiver$1 */
        class C05181 implements Runnable {
            C05181() {
            }

            public void run() {
                if (AlarmMainFragment.this.mAlarmListView != null && !AlarmMainFragment.this.mAlarmListView.mIsDeletingFlag) {
                    AlarmMainFragment.this.mAlarmListView.finishActionMode();
                    AlarmMainFragment.this.mAlarmListView.mIsNeedRestoreActionMode = false;
                }
            }
        }

        private InternalReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("AlarmMainFragment", "InternalReceiver onReceive action = " + action);
            if (action != null) {
                int i = -1;
                switch (action.hashCode()) {
                    case -1388165946:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_DELETE_MODE_CHANGE")) {
                            i = 2;
                            break;
                        }
                        break;
                    case -1337803714:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_CHANGE")) {
                            i = 1;
                            break;
                        }
                        break;
                    case 2028463135:
                        if (action.equals("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_EDIT_FINISHED_AND_SAVED_ALARM")) {
                            i = 0;
                            break;
                        }
                        break;
                }
                switch (i) {
                    case 0:
                        AlarmMainFragment.this.mCreatedIdFromAlarmEdit = intent.getIntExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", 0);
                        if (AlarmMainFragment.this.mCreatedIdFromAlarmEdit > 0) {
                            AlarmMainFragment.this.setNormalModePreference();
                            return;
                        }
                        return;
                    case 1:
                    case 2:
                        AlarmMainFragment.this.setNormalModePreference();
                        new Handler().postDelayed(new C05181(), 50);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        private MyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("AlarmMainFragment", "MyReceiver onReceive action= " + action);
            if (action != null && AlarmMainFragment.this.mAlarmListView != null) {
                Object obj = -1;
                switch (action.hashCode()) {
                    case -1566616968:
                        if (action.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case -1513032534:
                        if (action.equals("android.intent.action.TIME_TICK")) {
                            int i = 2;
                            break;
                        }
                        break;
                    case 502473491:
                        if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            obj = null;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                    case 1:
                        AlarmMainFragment.this.setMenuItem();
                        AlarmMainFragment.this.mAlarmListView.updateAllAlarmListView();
                        return;
                    case 2:
                    case 3:
                        if (AlarmMainFragment.this.mAlarmPresetPrefState == 2) {
                            AlarmMainFragment.this.updateCloasestAlertText();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (FragmentActivity) context;
        this.mContext = this.mActivity.getApplicationContext();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        this.mIsFirstRestart = true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.secD("AlarmMainFragment", "onCreateView()");
        this.mRootView = inflater.inflate(C0490R.layout.alarm_main_list, container, false);
        this.mBackGroundThread.setDaemon(true);
        this.mBackGroundThread.start();
        this.mAlarmListView = (AlarmListView) this.mRootView.findViewById(C0490R.id.alarm_main_layout);
        this.mAlarmListView.onCreateView(this.mActivity);
        this.mAlarmLaunchMode = this.mActivity.getIntent().getIntExtra("AlarmLaunchMode", 0);
        setHasOptionsMenu(true);
        if (StateUtils.isContextInDexMode(this.mActivity)) {
            registerForContextMenu(this.mRootView.findViewById(C0490R.id.alarm_list));
        }
        if (this.mAlarmLaunchMode == 2) {
            ((Toolbar) this.mRootView.findViewById(C0490R.id.toolbar)).setTitle(C0490R.string.select_alarm);
        }
        return this.mRootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mRootView == null) {
            this.mRootView = getView();
        }
        if (this.mContext == null) {
            this.mContext = this.mActivity.getApplicationContext();
        }
    }

    public void onStart() {
        super.onStart();
        if (!this.mIsFirstRestart && this.mAlarmListView != null && !this.mAlarmListView.mIsAlarmEditStarted) {
            getLoaderManager().restartLoader(0, null, this);
            this.mAlarmListView.updateAllAlarmListView();
            intentRegisterReceiver();
        }
    }

    public void onResume() {
        super.onResume();
        this.mAlarmListView.onResume();
        if (this.mAlarmPresetPrefState != 2) {
            this.mAlarmListView.setMainTitleWithPresetAlarm();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mIsConfigChanged = true;
        if (!(this.mAlarmListView == null || this.mAlarmListView.getAlarmRecyclerView() == null)) {
            this.mAlarmListView.getAlarmRecyclerView().invalidate();
        }
        if (StateUtils.isContextInDexMode(this.mActivity) && !this.mActivity.isInMultiWindowMode()) {
            if (this.mAppBarLayout == null) {
                this.mAppBarLayout = (AppBarLayout) this.mRootView.findViewById(C0490R.id.alarm_app_bar);
            }
            if (this.mAppBarLayout.isCollapsed()) {
                this.mAppBarLayout.setExpanded(false);
            } else {
                this.mAppBarLayout.setExpanded(true);
            }
        }
    }

    public void onStartActionMode(BottomNavigationView bottomView) {
        super.onStartActionMode(bottomView);
        if (this.mAlarmListView != null) {
            this.mAlarmListView.initBottomNavigationView(bottomView);
        }
    }

    public void onFinishActionMode() {
        super.onFinishActionMode();
        if (this.mAlarmListView != null) {
            this.mAlarmListView.removeBottomNavigationView();
        }
    }

    public void onPause() {
        super.onPause();
        Log.secD("AlarmMainFragment", "onPause()");
        setNormalModePreference();
        int alarmItemCnt = AlarmProvider.getAlarmCount(this.mContext);
        if (ClockTab.isAlarmTab()) {
            ClockUtils.insertSaLog("101");
        }
        ClockUtils.insertSaStatusLog(this.mContext, "5001", (long) alarmItemCnt);
    }

    public void onStop() {
        Log.secD("AlarmMainFragment", "onStop()");
        if (!(this.mAlarmListView == null || this.mAlarmListView.mIsAlarmEditStarted)) {
            intentUnRegisterReceiver();
        }
        this.mIsFirstRestart = false;
        super.onStop();
    }

    public void onDestroyView() {
        Log.secD("AlarmMainFragment", "onDestroyView()");
        unregisterForContextMenu(this.mRootView.findViewById(C0490R.id.alarm_list));
        getLoaderManager().destroyLoader(0);
        if (this.mAlarmListView != null) {
            this.mAlarmListView.onDestroy();
            this.mAlarmListView = null;
        }
        this.mRootView = null;
        if (this.mBackGroundThread != null) {
            this.mBackGroundThread.interrupt();
        }
        super.onDestroyView();
    }

    public void onDestroy() {
        Log.secD("AlarmMainFragment", "onDestroy()");
        intentUnRegisterReceiver();
        if (this.mContext == null) {
            super.onDestroy();
            return;
        }
        insertSaLogging();
        super.onDestroy();
    }

    private void insertSaLogging() {
        int itemCount = -1;
        Cursor cursor = AlarmDataHandler.getAlarmData(this.mContext);
        if (cursor != null) {
            itemCount = cursor.getCount();
        }
        if (itemCount <= 0) {
            String noAlarmString = "No alarm";
            if (Feature.isSupportSubstituteHolidayMenu()) {
                ClockUtils.insertSaStatusLog(this.mContext, "6501", "No alarm");
                return;
            }
            return;
        }
        boolean isInclude = false;
        boolean isExclude = false;
        cursor.moveToFirst();
        do {
            AlarmItem item = AlarmItem.createItemFromCursor(cursor);
            if (item.isHoliday()) {
                if (item.isSubstituteHoliday()) {
                    isInclude = true;
                } else {
                    isExclude = true;
                }
            }
        } while (cursor.moveToNext());
        cursor.close();
        if (Feature.isSupportSubstituteHolidayMenu()) {
            String holidayUsage = "Turned off this option";
            if (isInclude) {
                holidayUsage = isExclude ? "both Exclude/Include substitue holidays" : "Include substitue holidays";
            } else if (isExclude) {
                holidayUsage = "Exclude substitue holidays";
            }
            ClockUtils.insertSaStatusLog(this.mContext, "6501", holidayUsage);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0490R.menu.alarm_main_list_menu, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        initMenuItem(menu);
        setMenuItem();
        this.mAlarmListView.restoreActionMode();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0490R.id.menu_alarm_add) {
            this.mAlarmListView.startAlarmEdit("alarm_create", -1);
        } else if (item.getItemId() == C0490R.id.menu_alarm_delete) {
            ClockUtils.insertSaLog("101", "1006");
            this.mAlarmListView.startDeleteActionMode();
        } else if (item.getItemId() == C0490R.id.menu_alarm_setting) {
            ClockUtils.startAlertSettingActivity(this.mActivity);
        } else if (item.getItemId() == C0490R.id.menu_alarm_prepower_onoff) {
            Intent intent = new Intent();
            try {
                intent.setClass(this.mContext, AlarmPrePowerOnActivity.class);
                intent.setFlags(393216);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.secE("AlarmMainFragment", "ActivityNotFoundException : " + e.toString());
            }
        } else if (item.getItemId() == C0490R.id.menu_alarm_customization_service_onoff) {
            ClockUtils.startCustomizationServiceSettingActivity(this.mActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuItem(Menu menu) {
        if (menu != null) {
            this.mAddMenu = menu.findItem(C0490R.id.menu_alarm_add);
            if (this.mAddMenu != null) {
                this.mAddMenu.setShowAsAction(2);
            }
            this.mDeleteMenu = menu.findItem(C0490R.id.menu_alarm_delete);
            if (Feature.isAutoPowerOnOffMenuSupported()) {
                this.mPowerOnOffMenu = menu.findItem(C0490R.id.menu_alarm_prepower_onoff);
            }
            if (Feature.isSupportAlarmSoundMenu()) {
                this.mSettingMenu = menu.findItem(C0490R.id.menu_alarm_setting);
            }
            if (StateUtils.isShowCustomizeService(this.mContext)) {
                this.mCustomizeServiceMenu = menu.findItem(C0490R.id.menu_alarm_customization_service_onoff);
            }
        }
    }

    private void setMenuItem() {
        boolean z = true;
        if (this.mAddMenu != null) {
            this.mAddMenu.setVisible(this.mAlarmListView.getAlarmItemCount() > 0);
        }
        if (this.mDeleteMenu != null) {
            if (this.mAlarmLaunchMode != 2) {
                MenuItem menuItem = this.mDeleteMenu;
                if (this.mAlarmListView.getAlarmItemCount() <= 0) {
                    z = false;
                }
                menuItem.setVisible(z);
            } else {
                this.mDeleteMenu.setVisible(false);
            }
        }
        if (this.mPowerOnOffMenu != null) {
            if (this.mAlarmLaunchMode != 2) {
                this.mPowerOnOffMenu.setVisible(Feature.isAutoPowerOnOffMenuSupported());
            } else {
                this.mPowerOnOffMenu.setVisible(false);
            }
        }
        if (this.mSettingMenu != null) {
            if (this.mAlarmLaunchMode != 2) {
                this.mSettingMenu.setVisible(Feature.isSupportAlarmSoundMenu());
            } else {
                this.mSettingMenu.setVisible(false);
            }
        }
        if (this.mCustomizeServiceMenu == null) {
            return;
        }
        if (this.mAlarmLaunchMode != 2) {
            this.mCustomizeServiceMenu.setVisible(StateUtils.isShowCustomizeService(this.mContext));
        } else {
            this.mCustomizeServiceMenu.setVisible(false);
        }
    }

    private void intentRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");
        this.mReceiver = new MyReceiver();
        this.mActivity.registerReceiver(this.mReceiver, filter);
        IntentFilter internalFilter = new IntentFilter();
        internalFilter.addAction("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_CHANGE");
        internalFilter.addAction("com.sec.android.app.clockpackage.alarm.ACTION_ALARM_EDIT_FINISHED_AND_SAVED_ALARM");
        internalFilter.addAction("com.samsung.sec.android.clockpackage.alarm.NOTIFY_ALARM_DELETE_MODE_CHANGE");
        this.mInternalIntentReceiver = new InternalReceiver();
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mInternalIntentReceiver, internalFilter);
    }

    private void intentUnRegisterReceiver() {
        if (this.mInternalIntentReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInternalIntentReceiver);
            this.mInternalIntentReceiver = null;
        }
        if (this.mReceiver != null) {
            try {
                this.mActivity.unregisterReceiver(this.mReceiver);
            } catch (IllegalArgumentException e) {
                Log.secE("AlarmMainFragment", "IllegalArgumentException - unregisterReceiver(MyReceiver)");
            }
            this.mReceiver = null;
        }
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.secD("AlarmMainFragment", "onCreateLoader()");
        this.mIsCursorLoadFinished = false;
        if (this.mContext == null) {
            this.mContext = this.mActivity.getApplicationContext();
        }
        return new CursorLoader(this.mContext, AlarmProvider.CONTENT_URI, null, null, null, "alarmtime ASC , alerttime ASC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.secD("AlarmMainFragment", "onLoadFinished() mAlarmPresetPrefState = " + this.mAlarmPresetPrefState);
        if (this.mAlarmListView == null || cursor == null) {
            Log.secD("AlarmMainFragment", "activity finish because there is no Alarm DB");
            this.mActivity.finish();
            return;
        }
        this.mAlarmListView.updateAlarmDataForList(cursor);
        if (this.mAlarmPresetPrefState == 2) {
            int alarmItemCnt = cursor.getCount();
            if (!this.mAlarmListView.mIsDeletingFlag) {
                this.mAlarmListView.setNoItemViewVisibility(alarmItemCnt);
            }
            if (this.mIsCursorLoadFinished && this.mCreatedIdFromAlarmEdit > 0) {
                if (!(this.mAppBarLayout == null || StateUtils.isCanNotExpandAboutExtendedAppBar(this.mActivity) || !this.mAlarmListView.isAppBarExpandNeedCheck(this.mAlarmListView.getMeasuredHeight() - this.mAppBarLayout.getMeasuredHeight(), this.mCreatedIdFromAlarmEdit))) {
                    this.mAppBarLayout.setExpanded(false);
                }
                this.mAlarmListView.scrollToAlarm((long) this.mCreatedIdFromAlarmEdit);
                this.mCreatedIdFromAlarmEdit = 0;
                this.mAlarmListView.addAnimationFinished();
            }
            setMenuItem();
            updateCloasestAlertText();
            if (alarmItemCnt == 0) {
                AlarmProvider.disableAlert(this.mContext);
            }
        }
        this.mAlarmPresetPrefState = 2;
        this.mIsCursorLoadFinished = true;
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        Log.secD("AlarmMainFragment", "onLoaderReset() ");
        if (this.mAlarmListView != null) {
            this.mAlarmListView.onDestroy();
            this.mAlarmListView = null;
        }
    }

    private void updateCloasestAlertText() {
        if (this.mAlarmListView != null) {
            this.mAlarmListView.updateClosestAlertText();
        }
    }

    private void setNormalModePreference() {
        this.mAlarmPresetPrefState = 2;
        Editor ed = this.mContext.getSharedPreferences("isSetDefault", 0).edit();
        ed.putInt("alarmBootState", 2);
        ed.apply();
    }

    public void onTabSelected() {
        ClockUtils.insertSaLog("101");
        setAlarmListFocusable(true);
        this.mIsSelectTab = true;
    }

    public void onTabUnselected() {
        if (this.mAlarmListView != null) {
            this.mAlarmListView.finishActionMode();
        }
        setAlarmListFocusable(false);
    }

    private void setAlarmListFocusable(boolean enable) {
        if (this.mRootView != null && this.mRootView.findViewById(C0490R.id.alarm_list) != null) {
            this.mRootView.findViewById(C0490R.id.alarm_list).setFocusable(enable);
        }
    }

    private void setExtendedAppbarListener() {
        if (this.mRootView != null) {
            this.mAppBarLayout = (AppBarLayout) this.mRootView.findViewById(C0490R.id.alarm_app_bar);
            ((CollapsingToolbarLayout) this.mRootView.findViewById(C0490R.id.collapsing_toolbar)).setCustomAccessibility(true);
            this.mAppBarLayout.addOnOffsetChangedListener(new C05171());
        }
    }

    public boolean onClockDispatchKeyEvent(KeyEvent event, View tab) {
        boolean z = true;
        if (this.mActivity == null || StateUtils.isRemoteAction(event)) {
            return false;
        }
        if (event.getAction() == 1) {
            switch (event.getKeyCode()) {
                case 66:
                    if (!this.mAlarmListView.isDeleteActionMode()) {
                        return false;
                    }
                    this.mAlarmListView.showHideBottomNavigationView();
                    return false;
                default:
                    return false;
            }
        } else if (event.getAction() != 0) {
            return false;
        } else {
            boolean isAlarmDeleteMode = this.mAlarmListView.isDeleteActionMode();
            Log.secD("AlarmMainFragment", "onClockDispatchKeyEvent() keyCode = " + event.getKeyCode() + ", isAlarmDeleteMode = " + isAlarmDeleteMode);
            switch (event.getKeyCode()) {
                case 21:
                case 22:
                case 61:
                    if (this.mAlarmListView == null || !this.mAlarmListView.tabKeyProcess()) {
                        z = false;
                    }
                    return z;
                case 29:
                    if (!event.isCtrlPressed() || !isAlarmDeleteMode) {
                        return false;
                    }
                    this.mAlarmListView.toggleAllCheckBox(true);
                    return true;
                case 32:
                    if (!event.isCtrlPressed()) {
                        return false;
                    }
                    break;
                case 42:
                    if (!event.isCtrlPressed() || isAlarmDeleteMode) {
                        return false;
                    }
                    this.mAlarmListView.startAlarmEdit("alarm_create", -1);
                    return true;
                case 112:
                    break;
                default:
                    return false;
            }
            if (isAlarmDeleteMode) {
                this.mAlarmListView.startDeleteAnimation();
            } else {
                this.mAlarmListView.startDeleteActionMode();
            }
            return true;
        }
    }
}
