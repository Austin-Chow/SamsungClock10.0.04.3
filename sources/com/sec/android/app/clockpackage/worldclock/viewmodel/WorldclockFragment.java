package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.ListItem;
import com.sec.android.app.clockpackage.worldclock.model.SharedManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import java.util.ArrayList;

public class WorldclockFragment extends WorldclockCommonFragment {
    private MenuItem mAddMenu;
    private Context mContext;
    private MenuItem mEditMenu;
    private boolean mIsChangedSwBoundary;

    public void onTabSelected() {
        super.onTabSelected();
        ClockUtils.insertSaLog("110");
    }

    public void onTabUnselected() {
        super.onTabUnselected();
        this.mSmartTip.dismissSmartTips();
    }

    public boolean onClockDispatchKeyEvent(KeyEvent event, View tab) {
        Log.secD("WorldclockFragment", "onClockDispatchKeyEvent Action : " + event.getAction() + " KEY CODE : " + event.getKeyCode());
        if (event.getAction() != 0 || StateUtils.isRemoteAction(event) || this.mWorldclockMainListViewModel == null) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
            case 22:
                if (this.mWorldclockMainListViewModel.getList() != null && this.mWorldclockMainListViewModel.getList().isFocused() && this.mWorldclockMainListViewModel.isActionMode()) {
                    return true;
                }
                if (this.mWorldclockMainListViewModel.getList() != null && this.mWorldclockMainListViewModel.getList().isFocused()) {
                    setFocusedWorldClockTab(tab);
                    return true;
                }
                break;
            case 29:
                if (event.isCtrlPressed() && this.mWorldclockMainListViewModel.isActionMode() && this.mWorldclockMainListViewModel.getSelectAllCheckBox() != null) {
                    this.mWorldclockMainListViewModel.getSelectAllCheckBox().setChecked(true);
                    this.mWorldclockMainListViewModel.getWorldclockListViewActionMode().setCheckedState(0, this.mWorldclockMainListViewModel.getSelectAllCheckBox().isChecked(), true);
                    this.mWorldclockMainListViewModel.getWorldclockListViewActionMode().setSubtitle();
                    return true;
                }
            case 32:
                return event.isCtrlPressed();
            case 42:
                if (event.isCtrlPressed() && !this.mWorldclockMainListViewModel.isActionMode()) {
                    startWorldclockGlobeMain();
                    return true;
                }
            case 61:
                if (this.mWorldclockMainListViewModel.getList() != null && this.mWorldclockMainListViewModel.getList().isFocused() && this.mWorldclockMainListViewModel.isActionMode() && this.mWorldclockMainListViewModel.getSelectAllLayout() != null) {
                    this.mWorldclockMainListViewModel.getSelectAllLayout().requestFocus();
                    return true;
                }
            case 112:
                if (this.mWorldclockMainListViewModel.isActionMode()) {
                    for (int i = this.mWorldclockMainListViewModel.getAdapter().getItemCount() - 1; i >= 0; i--) {
                        ListItem selectedItem = (ListItem) this.mWorldclockMainListViewModel.getItems().get(i);
                        if (selectedItem.getSelected()) {
                            this.mWorldclockMainListViewModel.getDeleteList().add(Integer.valueOf(selectedItem.getUniqueId()));
                            this.mWorldclockMainListViewModel.getSelectedPosList().add(Integer.valueOf(i));
                        }
                    }
                    this.mWorldclockMainListViewModel.startDeleteAnimation();
                    return true;
                }
                break;
        }
        return false;
    }

    private void setFocusedWorldClockTab(View tab) {
        if (tab != null) {
            tab.requestFocus();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!(this.mWorldclockMainListViewModel == null || this.mWorldclockMainListViewModel.isGridLayout() == StateUtils.isScreenDp(this.mActivity, 512))) {
            Log.secD("WorldclockFragment", "onConfigurationChanged() change layout");
            this.mWorldclockMainListViewModel.setFirstVisibileItemPosition();
            this.mWorldclockMainListViewModel.initList();
        }
        if (this.mIsChangedSwBoundary != StateUtils.isScreenDp(this.mActivity, 281)) {
            this.mIsChangedSwBoundary = StateUtils.isScreenDp(this.mActivity, 281);
            this.mWorldclockMainListViewModel.initList();
        }
        this.mSmartTip.dismissSmartTips();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("WorldclockFragment", "onCreate()");
        this.mContext = this.mActivity.getApplicationContext();
        this.mIsChangedSwBoundary = StateUtils.isScreenDp(this.mActivity, 281);
    }

    public void onResume() {
        super.onResume();
        Log.secD("WorldclockFragment", "onResume()");
        ClockUtils.insertSaLog("110");
    }

    public void onPause() {
        super.onPause();
        Log.secD("WorldclockFragment", "onPause()");
        if (this.mWorldclockMainListViewModel != null && this.mWorldclockMainListViewModel.getAdapter() != null) {
            ClockUtils.insertSaStatusLog(this.mContext, "5101", (long) this.mWorldclockMainListViewModel.getAdapter().getItemCount());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.secD("WorldclockFragment", "onCreateView mIsFirstLaunch = " + this.mIsFirstLaunch);
        setHasOptionsMenu(true);
        if (container == null) {
            return null;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0836R.menu.worldclock_menu, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        Log.secD("WorldclockFragment", "onPrepareOptionsMenu");
        initMenuItem(menu);
        setMenuItem();
        super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == C0836R.id.menu_add) {
            ClockUtils.insertSaLog("110", "1252");
            startWorldclockGlobeMain();
        } else if (i == C0836R.id.menu_edit) {
            if (this.mWorldclockMainListViewModel == null || this.mWorldclockMainListViewModel.isActionMode()) {
                return super.onOptionsItemSelected(item);
            }
            ClockUtils.insertSaLog("110", "1101");
            this.mWorldclockMainListViewModel.doActionMode();
        } else if (i == C0836R.id.menu_weather_information) {
            Log.secD("WorldclockFragment", "Weather Settings");
            ClockUtils.insertSaLog("110", "1103");
            Intent intent = new Intent();
            try {
                intent.setClass(this.mContext, WeatherSettingsActivity.class);
                intent.setFlags(393216);
                this.mWorldclockMainListViewModel.finishActionMode();
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.secE("WorldclockFragment", "ActivityNotFoundException : " + e.toString());
            }
        } else if (i != C0836R.id.menu_time_zone_convertor) {
            return false;
        } else {
            Intent convertIntent = new Intent();
            try {
                this.mSmartTip.disableWorldclockSmartTips(this.mContext);
                convertIntent.setClass(this.mContext, TimeZoneConvertorActivity.class);
                convertIntent.setFlags(393216);
                ClockUtils.insertSaLog("110", "1105");
                startActivityForResult(convertIntent, 10);
            } catch (ActivityNotFoundException e2) {
                Log.secE("WorldclockFragment", "ActivityNotFoundException : " + e2.toString());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDestroy() {
        Log.secD("WorldclockFragment", "onDestroy()");
        super.onDestroy();
        if (!ClockUtils.isRunningClass(this.mContext, "com.sec.android.app.clockpackage.ClockPackage")) {
            CityManager.removeCity();
        }
        this.mSmartTip.dismissSmartTips();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.secD("WorldclockFragment", "onSaveInstanceState");
        if (!(this.mWorldclockMainListViewModel == null || this.mWorldclockMainListViewModel.getList() == null || this.mWorldclockMainListViewModel.getAdapter() == null)) {
            ArrayList<Integer> selectedPosition = new ArrayList();
            for (int i = this.mWorldclockMainListViewModel.getAdapter().getItemCount() - 1; i >= 0; i--) {
                if (((ListItem) this.mWorldclockMainListViewModel.getItems().get(i)).getSelected()) {
                    selectedPosition.add(Integer.valueOf(i));
                }
            }
            if (!selectedPosition.isEmpty()) {
                outState.putIntegerArrayList("worldclock_checked_item_position", selectedPosition);
            }
            outState.putBoolean("worldclock_checked_action_mode", this.mWorldclockMainListViewModel.isActionMode());
        }
        outState.putBoolean("IsFirstLaunch", this.mIsFirstLaunch);
        this.mSmartTip.saveSmartTip(outState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.secD("WorldclockFragment", "onActivityCreated");
        if (savedInstanceState != null) {
            if (this.mWorldclockMainListViewModel != null) {
                this.mWorldclockMainListViewModel.setCheckedActionMode(savedInstanceState.getBoolean("worldclock_checked_action_mode", false));
                this.mIsFirstLaunch = savedInstanceState.getBoolean("IsFirstLaunch", true);
                if (this.mWorldclockMainListViewModel.isCheckedActionMode()) {
                    this.mWorldclockMainListViewModel.recreateStartActionMode(savedInstanceState);
                }
            }
            if (this.mSmartTip.isShowSmartTip(savedInstanceState)) {
                this.mSmartTip.restoreSmartTip(savedInstanceState, this.mActivity, 1, this.mWorldclockToolbar);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mWorldclockMainListViewModel != null) {
            Log.secD("WorldclockFragment", "onActivityResult requestCode : " + requestCode);
            switch (requestCode) {
                case 0:
                    this.mWorldclockMainListViewModel.refreshCityListDelayed(500);
                    if (resultCode == -1 && checkShowSmartTipsCondition()) {
                        this.mSmartTip.launchSmartTip(this.mActivity, false, 2, this.mWorldclockToolbar);
                        return;
                    }
                    return;
                case 10:
                    if (new SharedManager(this.mContext).getNeedUpdateList()) {
                        this.mWorldclockMainListViewModel.updateList(false);
                        this.mWorldclockMainListViewModel.refreshWeatherInfo();
                        this.mWorldclockMainListViewModel.refreshCityListDelayed(500);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    protected void startWorldclockCityListActivity() {
        if (WorldclockDBManager.getDBCursorCnt(this.mContext) >= 20) {
            WorldclockUtils.showMaxCountToast(this.mActivity);
            return;
        }
        SharedManager sm = new SharedManager(this.mContext);
        Intent i = new Intent();
        i.setClass(this.mContext, WorldclockCityListActivity.class);
        i.addFlags(67108864);
        i.putExtra("where", "add");
        i.putExtra("index", 0);
        i.putExtra("zoomlevel", sm.getPrefLastZoomLevel());
        i.putExtra("NUMBER_OF_CITIES_IN_MENU", WorldclockDBManager.getDBCursorCnt(this.mContext));
        startActivityForResult(i, 0);
    }

    protected void startWorldclockGlobeMain() {
        SharedManager sm = new SharedManager(this.mContext);
        Intent i = new Intent();
        i.setClass(this.mContext, WorldclockGlobeMain.class);
        i.addFlags(603979776);
        i.putExtra("where", "add");
        i.putExtra("index", 0);
        i.putExtra("zoomlevel", sm.getPrefLastZoomLevel());
        i.putExtra("ListPosition", 0);
        i.putParcelableArrayListExtra("WorldclockWeatherListInfoKey", this.mCityWeatherInfo);
        i.putExtra("NUMBER_OF_CITIES_IN_MENU", WorldclockDBManager.getDBCursorCnt(this.mContext));
        startActivityForResult(i, 0);
        this.mActivity.overridePendingTransition(C0836R.anim.worldclock_animation_fade_in, C0836R.anim.worldclock_animation_hold);
    }

    private void initMenuItem(Menu menu) {
        this.mEditMenu = menu.findItem(C0836R.id.menu_edit);
        this.mAddMenu = menu.findItem(C0836R.id.menu_add);
    }

    protected void setMenuItem() {
        if (this.mWorldclockMainListViewModel != null && this.mEditMenu != null && this.mAddMenu != null) {
            boolean needHideMenu = this.mWorldclockMainListViewModel.getAdapter() != null && this.mWorldclockMainListViewModel.getAdapter().getItemCount() > 0;
            this.mEditMenu.setVisible(needHideMenu);
            this.mAddMenu.setVisible(needHideMenu);
        }
    }

    private boolean checkShowSmartTipsCondition() {
        if (!this.mSmartTip.getWorldclockSmartTips(this.mContext) || this.mWorldclockMainListViewModel == null || this.mWorldclockMainListViewModel.getItems() == null || this.mWorldclockMainListViewModel.getItems().size() <= 1) {
            return false;
        }
        return true;
    }

    protected void launchSmartTip() {
        if (this.mIsFirstLaunch && WorldclockUtils.isWorldclockTab()) {
            this.mWorldclockMainListViewModel.launchWeatherInfo();
            if (checkShowSmartTipsCondition()) {
                this.mFragmentView.findViewById(C0836R.id.toolbar).post(WorldclockFragment$$Lambda$1.lambdaFactory$(this));
            }
            this.mIsFirstLaunch = false;
        }
    }

    private /* synthetic */ void lambda$launchSmartTip$0() {
        Log.secD("WorldclockFragment", "launchSmartTip : SMART_TIP_WORLDCLOCK_TAB_SELECTED");
        this.mSmartTip.launchSmartTip(this.mActivity, false, 1, this.mWorldclockToolbar);
    }
}
