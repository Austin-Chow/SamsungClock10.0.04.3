package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.sec.android.app.clockpackage.common.activity.ClockActivity;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.TimeZoneConvertorControlViewListener;
import com.sec.android.app.clockpackage.worldclock.callback.TimeZoneListViewModelListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.ListItem;
import com.sec.android.app.clockpackage.worldclock.model.SharedManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneConvertorActivity extends ClockActivity {
    private TimeZoneConvertorControlViewModel mConvertorControlViewModel;
    private int mDefaultCityIndex = 0;
    private TimeZone mDefaultTimeZone = null;
    private final BroadcastReceiver mIntentReceiver = new C08751();
    private boolean mIsRecreate = false;
    private NestedScrollView mMainScrollView;
    private Menu mMenu;
    private boolean mNeedAnimation = true;
    private final TimeZoneConvertorControlViewListener mTimeZoneConvertorControlViewListener = new C08762();
    private TimeZoneConvertorPicker mTimeZoneConvertorPicker;
    private TimeZoneListViewModel mTimeZoneListViewModel = null;
    private final TimeZoneListViewModelListener mTimeZoneListViewModelListener = new C08773();

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorActivity$1 */
    class C08751 extends BroadcastReceiver {
        C08751() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                boolean z = true;
                switch (action.hashCode()) {
                    case -1513032534:
                        if (action.equals("android.intent.action.TIME_TICK")) {
                            z = true;
                            break;
                        }
                        break;
                    case 502473491:
                        if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                            z = true;
                            break;
                        }
                        break;
                    case 505380757:
                        if (action.equals("android.intent.action.TIME_SET")) {
                            z = false;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                    case true:
                        if ("android.intent.action.TIMEZONE_CHANGED".equals(action) && TimeZoneConvertorActivity.this.mTimeZoneListViewModel != null) {
                            conserveCurrentInfo();
                            TimeZoneConvertorActivity.this.mTimeZoneListViewModel.initItemValue(true);
                        }
                        if (TimeZoneConvertorActivity.this.mConvertorControlViewModel != null) {
                            TimeZoneConvertorActivity.this.mConvertorControlViewModel.onChangedTimeData();
                            TimeZoneConvertorActivity.this.mConvertorControlViewModel.onChagnedResetButtonState();
                        }
                        if (TimeZoneConvertorActivity.this.mTimeZoneListViewModel != null) {
                            TimeZoneConvertorActivity.this.mTimeZoneListViewModel.refreshCityList();
                            return;
                        }
                        return;
                    case true:
                        if (TimeZoneConvertorActivity.this.mConvertorControlViewModel != null) {
                            TimeZoneConvertorActivity.this.mConvertorControlViewModel.onChagnedResetButtonState();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        private void conserveCurrentInfo() {
            List<String> spinnerArrayList = TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getSpinnerArrayList();
            if (spinnerArrayList != null) {
                String originalCityName = (String) spinnerArrayList.get(TimeZoneConvertorActivity.this.mDefaultCityIndex);
                int hour = TimeZoneConvertorActivity.this.mTimeZoneConvertorPicker.getHour();
                int minute = TimeZoneConvertorActivity.this.mTimeZoneConvertorPicker.getMinute();
                TimeZoneConvertorActivity.this.mTimeZoneListViewModel.clearList();
                TimeZoneConvertorActivity.this.mTimeZoneListViewModel.bindList();
                String newTimeZoneCityName = TimeZoneConvertorActivity.this.getDefaultCityName();
                int originalCityIndex = spinnerArrayList.indexOf(originalCityName);
                int newTimeZoneCityIndex = spinnerArrayList.indexOf(newTimeZoneCityName);
                TimeZoneConvertorActivity timeZoneConvertorActivity = TimeZoneConvertorActivity.this;
                if (originalCityIndex < 0) {
                    originalCityIndex = newTimeZoneCityIndex;
                }
                timeZoneConvertorActivity.mDefaultCityIndex = originalCityIndex;
                if (TimeZoneConvertorActivity.this.mDefaultCityIndex < 0) {
                    TimeZoneConvertorActivity.this.mDefaultCityIndex = 0;
                }
                TimeZoneConvertorActivity.this.mConvertorControlViewModel.setSpinner(TimeZoneConvertorActivity.this.mDefaultCityIndex);
                TimeZoneConvertorActivity.this.mIsRecreate = true;
                TimeZoneConvertorActivity.this.mTimeZoneConvertorPicker.setHour(hour);
                TimeZoneConvertorActivity.this.mTimeZoneConvertorPicker.setMinute(minute);
                TimeZoneConvertorActivity.this.mTimeZoneConvertorPicker.restoreLastTime();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorActivity$2 */
    class C08762 implements TimeZoneConvertorControlViewListener {
        C08762() {
        }

        public List<String> getSpinnerArrayList() {
            return TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getSpinnerArrayList();
        }

        public TimeZone getDefaultTimeZone() {
            return TimeZoneConvertorActivity.this.mDefaultTimeZone;
        }

        public void initItemValue(boolean isSetOnlyHour) {
            TimeZoneConvertorActivity.this.mTimeZoneListViewModel.initItemValue(isSetOnlyHour);
        }

        public void onEditTextModeChanged(boolean isEditMode) {
            boolean z = true;
            if (TimeZoneConvertorActivity.this.mTimeZoneListViewModel != null && TimeZoneConvertorActivity.this.isPortraitLayout(TimeZoneConvertorActivity.this.getResources().getConfiguration())) {
                boolean z2;
                RecyclerView list = TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getList();
                if (isEditMode) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                list.seslSetGoToTopEnabled(z2);
                TimeZoneListViewModel access$000 = TimeZoneConvertorActivity.this.mTimeZoneListViewModel;
                if (TimeZoneConvertorActivity.this.mIsRecreate) {
                    z = false;
                }
                access$000.controlListHead(z, TimeZoneConvertorActivity.this.isInMultiWindowMode());
                TimeZoneConvertorActivity.this.mMainScrollView.scrollTo(0, 0);
            }
        }

        public void onSelectCityItem(int defaultCityIndex) {
            if (TimeZoneConvertorActivity.this.mTimeZoneListViewModel != null) {
                if (TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getConvertorItems() != null && TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getConvertorItems().size() > defaultCityIndex) {
                    TimeZoneConvertorActivity.this.mDefaultTimeZone = ((ListItem) TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getConvertorItems().get(defaultCityIndex)).getTimeZone();
                    ((ListItem) TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getConvertorItems().get(defaultCityIndex)).setSelected(true);
                }
                if (!(((String) TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getSpinnerArrayList().get(0)).equals(TimeZoneConvertorActivity.this.getResources().getString(C0836R.string.local_time)) && defaultCityIndex == 0)) {
                    int i;
                    List items = TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getItems();
                    if (((String) TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getSpinnerArrayList().get(0)).equals(TimeZoneConvertorActivity.this.getResources().getString(C0836R.string.local_time))) {
                        i = defaultCityIndex - 1;
                    } else {
                        i = defaultCityIndex;
                    }
                    ((ListItem) items.get(i)).setSelected(true);
                    RecyclerView list = TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getList();
                    if (((String) TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getSpinnerArrayList().get(0)).equals(TimeZoneConvertorActivity.this.getResources().getString(C0836R.string.local_time))) {
                        defaultCityIndex--;
                    }
                    list.scrollToPosition(defaultCityIndex);
                }
            }
            if (TimeZoneConvertorActivity.this.mConvertorControlViewModel != null) {
                TimeZoneConvertorActivity.this.mDefaultCityIndex = TimeZoneConvertorActivity.this.mConvertorControlViewModel.getCurrentItemIndex();
            }
            if (!(TimeZoneConvertorActivity.this.mIsRecreate || TimeZoneConvertorActivity.this.mConvertorControlViewModel == null)) {
                TimeZoneConvertorActivity.this.mConvertorControlViewModel.updatePicker(TimeZoneConvertorActivity.this.mDefaultTimeZone);
            }
            TimeZoneConvertorActivity.this.mIsRecreate = false;
        }

        public void onResetSelectedState() {
            for (ListItem item : TimeZoneConvertorActivity.this.mTimeZoneListViewModel.getItems()) {
                item.setSelected(false);
            }
        }

        public void onRefreshCityListDelayed(long delay) {
            TimeZoneConvertorActivity.this.mTimeZoneListViewModel.refreshCityListDelayed(delay);
        }

        public void onTimeChanged(int hour, int minute) {
            if (TimeZoneConvertorActivity.this.mTimeZoneListViewModel != null) {
                TimeZoneConvertorActivity.this.mTimeZoneListViewModel.updateTimeOfList(hour, minute, TimeZoneConvertorActivity.this.mConvertorControlViewModel.getStartHour(), TimeZoneConvertorActivity.this.mConvertorControlViewModel.getStartMin());
            }
            TimeZoneConvertorActivity.this.mConvertorControlViewModel.onChagnedResetButtonState();
        }

        public boolean getIsPortraitLayout() {
            return TimeZoneConvertorActivity.this.isPortraitLayout(TimeZoneConvertorActivity.this.getResources().getConfiguration());
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorActivity$3 */
    class C08773 implements TimeZoneListViewModelListener {
        C08773() {
        }

        public boolean isEditMode() {
            return TimeZoneConvertorActivity.this.mTimeZoneConvertorPicker.isEditMode();
        }

        public void setFabButtonVisible(boolean visible) {
            if (TimeZoneConvertorActivity.this.mMenu != null) {
                TimeZoneConvertorActivity.this.mMenu.findItem(C0836R.id.add_menu).setVisible(visible);
            }
        }

        public void startAddCityActivity() {
            TimeZoneConvertorActivity.this.startCityActivity(0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("TimeZoneConvertorActivity", "onCreate");
        setContentView(C0836R.layout.worldclock_timezone_convertor);
        SharedManager sm = new SharedManager(getApplicationContext());
        if (savedInstanceState != null) {
            this.mNeedAnimation = false;
        } else {
            sm.setNeedUpdateList(false);
        }
        initCity();
        initView();
        initReceiver();
        this.mConvertorControlViewModel.initPicker(isInMultiWindowMode(), this.mTimeZoneConvertorControlViewListener);
        startConvertorMode();
        setSpinnerData(savedInstanceState);
        setOrientationLayout(getResources().getConfiguration());
        this.mConvertorControlViewModel.initResetButton();
        this.mConvertorControlViewModel.setPickerHeightForMultiWindow(isInMultiWindowMode());
    }

    protected void onResume() {
        super.onResume();
        this.mIsRecreate = false;
        this.mMainScrollView.post(TimeZoneConvertorActivity$$Lambda$1.lambdaFactory$(this));
        ClockUtils.insertSaLog("115");
    }

    private /* synthetic */ void lambda$onResume$0() {
        this.mMainScrollView.scrollTo(0, 0);
    }

    protected void onPause() {
        super.onPause();
        this.mConvertorControlViewModel.dismissSpinner();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mDefaultTimeZone = null;
        if (this.mConvertorControlViewModel != null) {
            this.mConvertorControlViewModel.releaseInstance();
        }
        if (this.mTimeZoneListViewModel != null) {
            this.mTimeZoneListViewModel.releaseInstance();
        }
        deleteReceiver();
    }

    public void onBackPressed() {
        setResult(10);
        ClockUtils.insertSaLog("115", "1241");
        super.onBackPressed();
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean("timezone_convertor_recreate", true);
        state.putInt("timezone_convertor_current_index", this.mDefaultCityIndex);
        if (this.mConvertorControlViewModel != null) {
            this.mConvertorControlViewModel.saveInstance(state);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.mIsRecreate = savedInstanceState.getBoolean("timezone_convertor_recreate", false);
            if (this.mConvertorControlViewModel != null) {
                this.mConvertorControlViewModel.restoreInstance(savedInstanceState);
            }
        }
    }

    private void setSpinnerData(Bundle savedInstanceState) {
        int i;
        if (savedInstanceState != null) {
            i = savedInstanceState.getInt("timezone_convertor_current_index", 0);
        } else {
            i = this.mTimeZoneListViewModel.getSpinnerArrayList().indexOf(getDefaultCityName());
        }
        this.mDefaultCityIndex = i;
        if (this.mDefaultCityIndex < 0) {
            this.mDefaultCityIndex = 0;
        }
        this.mConvertorControlViewModel.setSpinner(this.mDefaultCityIndex);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0836R.menu.timezone_convertor_menu, menu);
        menu.findItem(C0836R.id.add_menu).setShowAsAction(2);
        if (this.mTimeZoneListViewModel.getItems().size() == 0) {
            menu.findItem(C0836R.id.add_menu).setVisible(false);
        }
        this.mMenu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            setResult(10);
            finish();
            ClockUtils.insertSaLog("115", "1241");
            return true;
        }
        if (item.getItemId() == C0836R.id.add_menu) {
            startCityActivity(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mConvertorControlViewModel != null) {
            this.mConvertorControlViewModel.onConfigurationChanged(isInMultiWindowMode());
        }
        if (this.mTimeZoneListViewModel != null) {
            this.mTimeZoneListViewModel.controlListHead(false, isInMultiWindowMode());
        }
        setOrientationLayout(newConfig);
    }

    private void setOrientationLayout(Configuration newConfig) {
        ConstraintLayout convertorLayout = (ConstraintLayout) findViewById(C0836R.id.worldclock_timezone_convertor_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        int rootLayoutId = C0836R.id.worldclock_timezone_convertor_layout;
        int toolbarId = C0836R.id.timezone_convertor_toolbar;
        int controlViewId = C0836R.id.worldclock_timezone_convertor_control_view;
        int listViewId = C0836R.id.worldclock_timezone_convertor_list_view;
        constraintSet.clear(rootLayoutId);
        if (isPortraitLayout(newConfig)) {
            constraintSet.clone(convertorLayout);
            constraintSet.connect(toolbarId, 3, rootLayoutId, 3);
            constraintSet.connect(toolbarId, 4, controlViewId, 3);
            constraintSet.setVerticalBias(toolbarId, 0.0f);
            constraintSet.connect(controlViewId, 3, toolbarId, 4);
            constraintSet.connect(controlViewId, 2, rootLayoutId, 2);
            constraintSet.connect(controlViewId, 4, listViewId, 3);
            constraintSet.connect(controlViewId, 1, rootLayoutId, 1);
            constraintSet.clear(listViewId);
            constraintSet.connect(listViewId, 3, controlViewId, 4);
            constraintSet.connect(listViewId, 2, rootLayoutId, 2);
            constraintSet.connect(listViewId, 4, rootLayoutId, 4);
            constraintSet.connect(listViewId, 1, rootLayoutId, 1);
            constraintSet.constrainMinHeight(listViewId, getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_timezone_convertor_list_item_layout_height));
            constraintSet.addToVerticalChain(controlViewId, toolbarId, listViewId);
            constraintSet.setVerticalChainStyle(controlViewId, 1);
            if (!StateUtils.isScreenDp(this, 512) || newConfig.orientation != 2) {
                constraintSet.constrainPercentWidth(controlViewId, 1.0f);
                constraintSet.constrainPercentWidth(listViewId, 1.0f);
            } else if (!StateUtils.isMultiWindowMinSize(this, 481, true)) {
                Point size = new Point();
                if (StateUtils.isContextInDexMode(this)) {
                    getWindowManager().getDefaultDisplay().getSize(size);
                } else {
                    getWindowManager().getDefaultDisplay().getRealSize(size);
                }
                int screenWidth = (int) (((float) size.x) * 0.75f);
                int maxWidth = getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_timezone_convertor_picker_max_width_tablet);
                if (screenWidth < maxWidth) {
                    constraintSet.constrainWidth(controlViewId, screenWidth);
                } else {
                    constraintSet.constrainWidth(controlViewId, maxWidth);
                }
                constraintSet.constrainWidth(listViewId, size.y);
            }
            constraintSet.applyTo(convertorLayout);
            return;
        }
        constraintSet.clone(convertorLayout);
        constraintSet.constrainDefaultHeight(rootLayoutId, 0);
        constraintSet.connect(toolbarId, 3, rootLayoutId, 3);
        constraintSet.connect(toolbarId, 4, rootLayoutId, 4);
        constraintSet.connect(toolbarId, 1, rootLayoutId, 1);
        constraintSet.connect(toolbarId, 2, rootLayoutId, 2);
        constraintSet.setVerticalBias(toolbarId, 0.0f);
        constraintSet.connect(controlViewId, 3, toolbarId, 4);
        constraintSet.connect(controlViewId, 2, listViewId, 1);
        constraintSet.connect(controlViewId, 4, rootLayoutId, 4);
        constraintSet.connect(controlViewId, 1, rootLayoutId, 1);
        constraintSet.setMargin(controlViewId, 3, 0);
        constraintSet.constrainPercentWidth(controlViewId, 0.5f);
        constraintSet.connect(listViewId, 3, toolbarId, 4);
        constraintSet.connect(listViewId, 2, rootLayoutId, 2);
        constraintSet.connect(listViewId, 4, rootLayoutId, 4);
        constraintSet.connect(listViewId, 1, controlViewId, 2);
        constraintSet.setMargin(listViewId, 3, 0);
        constraintSet.constrainPercentWidth(listViewId, 0.5f);
        constraintSet.addToHorizontalChain(controlViewId, controlViewId, listViewId);
        constraintSet.setHorizontalChainStyle(controlViewId, 1);
        constraintSet.applyTo(convertorLayout);
    }

    private void initView() {
        this.mMainScrollView = (NestedScrollView) findViewById(C0836R.id.worldclock_timezone_convertor_scroll_view);
        this.mConvertorControlViewModel = (TimeZoneConvertorControlViewModel) findViewById(C0836R.id.worldclock_timezone_convertor_control_view);
        this.mTimeZoneListViewModel = (TimeZoneListViewModel) findViewById(C0836R.id.worldclock_timezone_convertor_list_view);
        this.mTimeZoneConvertorPicker = (TimeZoneConvertorPicker) this.mConvertorControlViewModel.findViewById(C0836R.id.timezone_convertor_picker);
        this.mTimeZoneListViewModel.initTimeZoneListViewModel(this, this.mTimeZoneListViewModelListener);
        this.mTimeZoneListViewModel.bindList();
        this.mTimeZoneListViewModel.controlListHead(false, isInMultiWindowMode());
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        filter.addAction("android.intent.action.TIME_TICK");
        registerReceiver(this.mIntentReceiver, filter, null, null);
    }

    private void deleteReceiver() {
        Log.secD("TimeZoneConvertorActivity", "deleteReceiver()");
        unregisterReceiver(this.mIntentReceiver);
    }

    private void startConvertorMode() {
        if (this.mTimeZoneConvertorPicker != null) {
            new GregorianCalendar().setTimeZone(TimeZone.getDefault());
            this.mDefaultTimeZone = TimeZone.getDefault();
            this.mConvertorControlViewModel.updatePicker(TimeZone.getDefault());
            if (this.mNeedAnimation) {
                this.mTimeZoneConvertorPicker.startFlipAnimation(0, this.mConvertorControlViewModel.getStartHour(), this.mConvertorControlViewModel.getStartMin());
            }
            this.mNeedAnimation = true;
            this.mTimeZoneConvertorPicker.setIs24HourView();
            if (this.mTimeZoneListViewModel != null) {
                this.mTimeZoneListViewModel.initItemValue(false);
            }
        }
    }

    private String getDefaultCityName() {
        City cityInfo = CityManager.findCityObjectByName(CityManager.findDefaultCityByCapital(TimeZone.getDefault()));
        return cityInfo != null ? cityInfo.getName() : "";
    }

    private void startWorldclockGlobeMain(int requestCode) {
        SharedManager sm = new SharedManager(getApplicationContext());
        Intent i = new Intent();
        i.setClassName(this, "com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain");
        i.addFlags(603979776);
        i.putExtra("where", "add");
        i.putExtra("index", 0);
        i.putExtra("zoomlevel", sm.getPrefLastZoomLevel());
        i.putExtra("NUMBER_OF_CITIES_IN_MENU", WorldclockDBManager.getDBCursorCnt(getApplicationContext()));
        startActivityForResult(i, requestCode);
        if (!StateUtils.isContextInDexMode(this)) {
            overridePendingTransition(C0836R.anim.worldclock_animation_fade_in, C0836R.anim.worldclock_animation_hold);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean z = true;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (this.mTimeZoneListViewModel != null) {
                int defaultCityIndex;
                this.mTimeZoneListViewModel.clearList();
                this.mTimeZoneListViewModel.bindList();
                setOrientationLayout(getResources().getConfiguration());
                if (((String) this.mTimeZoneListViewModel.getSpinnerArrayList().get(0)).equals(getResources().getString(C0836R.string.local_time))) {
                    defaultCityIndex = 1;
                } else {
                    defaultCityIndex = 0;
                }
                ClockUtils.insertSaLog("115", "1122", CityManager.findEngOnlyCityNameByUniqueId(Integer.valueOf(((ListItem) this.mTimeZoneListViewModel.getConvertorItems().get(defaultCityIndex)).getUniqueId())));
                this.mConvertorControlViewModel.setSpinner(defaultCityIndex);
                new SharedManager(getApplicationContext()).setNeedUpdateList(true);
                this.mTimeZoneListViewModel.controlListHead(false, isInMultiWindowMode());
            }
        } else if (resultCode == 0) {
            TimeZoneConvertorControlViewModel timeZoneConvertorControlViewModel = this.mConvertorControlViewModel;
            if (this.mIsRecreate) {
                z = false;
            }
            timeZoneConvertorControlViewModel.setChangedList(z);
            this.mConvertorControlViewModel.setSpinner(this.mDefaultCityIndex);
        }
    }

    private void startCityActivity(int requestCode) {
        if (this.mTimeZoneConvertorPicker != null) {
            this.mTimeZoneConvertorPicker.hideInputMethod();
        }
        ClockUtils.insertSaLog("115", "1120");
        startWorldclockGlobeMain(requestCode);
    }

    private void initCity() {
        String systemLocale = Locale.getDefault().getDisplayName();
        String dbLocale = new SharedManager(getApplicationContext()).getPrefDBLocale();
        if (!(CityManager.sIsCityManagerLoad && dbLocale != null && systemLocale.equals(dbLocale))) {
            CityManager.initCity(getApplicationContext());
        }
        WorldclockDBManager.updateDBLocale(getApplicationContext());
    }

    private boolean isPortraitLayout(Configuration configuration) {
        return StateUtils.isContextInDexMode(this) || isInMultiWindowMode() || StateUtils.isScreenDp(getApplicationContext(), 512) || configuration.orientation != 2;
    }
}
