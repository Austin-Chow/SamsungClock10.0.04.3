package com.sec.android.app.clockpackage;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.SeslMenuItem;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.aboutpage.activity.AboutClockActivity;
import com.sec.android.app.clockpackage.aboutpage.update.CheckForUpdates;
import com.sec.android.app.clockpackage.aboutpage.update.CheckForUpdates.StubListener;
import com.sec.android.app.clockpackage.alarm.activity.AlarmMainFragment;
import com.sec.android.app.clockpackage.bixby.BixbyActionHandler;
import com.sec.android.app.clockpackage.common.activity.ClockFragment;
import com.sec.android.app.clockpackage.common.callback.ActionModeListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockTabLayout;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;
import com.sec.android.app.clockpackage.common.viewmodel.CoverManager;
import com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchFragment;
import com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchManager;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment;
import com.sec.android.app.clockpackage.timer.viewmodel.TimerManager;
import com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockFragment;
import java.util.Calendar;

public class ClockPackage extends AppCompatActivity implements ActionModeListener {
    private static boolean sIsClockPackageCreated = false;
    private BixbyActionHandler mBixbyActionHandler;
    private CoverManager mCoverManager;
    private ClockFragment mFragment;
    private FragmentManager mFragmentManager;
    private final Handler mHandler = new Handler();
    private boolean mIsActionMode = false;
    private boolean mIsBottomNavigationVisible = false;
    private Menu mMenu;
    private boolean mNeedToShowBadge = false;
    private StopwatchManager mStopwatchManager;
    private final StubListener mStubListener = new C04751();
    private ClockTabLayout mTabLayout;
    private final String[] mTabName = new String[]{NotificationCompat.CATEGORY_ALARM, "worldclock", "stopwatch", "timer"};
    private TimerManager mTimerManager;

    /* renamed from: com.sec.android.app.clockpackage.ClockPackage$1 */
    class C04751 implements StubListener {
        C04751() {
        }

        public void onUpdateCheckCompleted(int result, String serverVersionCode) {
            boolean z = false;
            int currentAppVerCode = ClockUtils.getVersionCode(ClockPackage.this);
            String savedVersionCode = ClockPackage.this.getSharedPreferences("com.sec.android.app.clockpackage_preferences", 0).getString("marketVersionCode", null);
            Log.secD("ClockPackage", "serverVer= " + serverVersionCode + ", savedVer= " + savedVersionCode + ", appVer= " + currentAppVerCode);
            if (serverVersionCode != null) {
                int serverVerCodeInt = Integer.parseInt(serverVersionCode);
                ClockPackage clockPackage = ClockPackage.this;
                if (currentAppVerCode < serverVerCodeInt && !serverVersionCode.equals(savedVersionCode)) {
                    z = true;
                }
                clockPackage.mNeedToShowBadge = z;
                ClockPackage.this.initMenuBadge();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ClockPackage$2 */
    class C04762 implements Runnable {
        C04762() {
        }

        public void run() {
            CheckForUpdates.setCheckForUpdatesListener(ClockPackage.this, ClockPackage.this.mStubListener, false);
            ClockUtils.insertSaLog("101", "3006", (long) Calendar.getInstance().get(11));
            if (ClockPackage.this.mCoverManager == null) {
                ClockPackage.this.mCoverManager = new CoverManager(ClockPackage.this.getApplicationContext());
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ClockPackage$3 */
    class C04773 implements OnTabSelectedListener {
        C04773() {
        }

        public void onTabSelected(Tab tab) {
            int tabPosition = tab.getPosition();
            Log.secD("ClockPackage", "onTabSelected() : " + tabPosition);
            ClockTab.getInstance().setTabPosition(ClockPackage.this.getApplicationContext(), tabPosition);
            ClockPackage.this.setCurrentItem();
            ClockFragment fragment = (ClockFragment) ClockPackage.this.mFragmentManager.findFragmentByTag(ClockPackage.this.mTabName[tabPosition]);
            if (fragment != null) {
                Log.secD("ClockPackage", "mFragment.onTabSelected() : " + tabPosition);
                fragment.onTabSelected();
            }
            ClockPackage.this.changeToolBar();
        }

        public void onTabUnselected(Tab tab) {
            int tabPosition = tab.getPosition();
            Log.secD("ClockPackage", "onTabUnselected() : " + tabPosition);
            if (ClockPackage.this.mMenu != null) {
                ClockPackage.this.mMenu.close();
            }
            ClockPackage.this.closeContextMenu();
            ClockFragment fragment = (ClockFragment) ClockPackage.this.mFragmentManager.findFragmentByTag(ClockPackage.this.mTabName[tabPosition]);
            if (fragment != null) {
                Log.secD("ClockPackage", "mFragment.onTabUnselected() : " + tabPosition);
                fragment.onTabUnselected();
            }
        }

        public void onTabReselected(Tab tab) {
            String screenId = "";
            switch (ClockTab.getPrevTab()) {
                case 0:
                    screenId = "101";
                    break;
                case 1:
                    screenId = "110";
                    break;
                case 2:
                    screenId = "120";
                    break;
                case 3:
                    screenId = "130";
                    break;
            }
            String eventId = "";
            switch (ClockTab.getCurrentTab()) {
                case 0:
                    eventId = "1001";
                    break;
                case 1:
                    eventId = "1002";
                    break;
                case 2:
                    eventId = "1003";
                    break;
                case 3:
                    eventId = "1004";
                    break;
            }
            ClockUtils.insertSaLog(screenId, eventId);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ClockPackage$4 */
    class C04784 implements Runnable {
        C04784() {
        }

        public void run() {
            if (!ClockPackage.sIsClockPackageCreated) {
                if (ClockPackage.this.mCoverManager == null || !ClockPackage.this.mCoverManager.isCoverClosed()) {
                    ClockPackage.this.setIsClockPackageResumed(true);
                }
                ClockPackage.sIsClockPackageCreated = true;
            }
            StateUtils.setClockPackageInDexMode(StateUtils.isContextInDexMode(ClockPackage.this));
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ClockPackage$5 */
    class C04795 implements AnimationListener {
        C04795() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            if (!ClockPackage.this.mIsActionMode) {
                ClockPackage.this.mTabLayout.setVisibility(0);
                ClockPackage.this.mTabLayout.startAnimation(ClockPackage.this.getTranslationAnimation(true));
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.m43i("VerificationLog", "-- onCreate --");
        Intent intent = getIntent();
        ClockTab.getInstance().initTabPosition(getApplicationContext(), intent);
        Log.secD("ClockPackage", "onCreate tab : " + ClockTab.getCurrentTab());
        setContentView((int) R.layout.clock_main);
        createTabs();
        hideStatusBarForLandscape();
        if (ClockTab.getCurrentTab() != 0) {
            try {
                getWindow().getDecorView().semSetRoundedCorners(0);
            } catch (NoSuchMethodError e) {
                Log.secE("ClockPackage", "NoSuchMethodError : " + e);
            }
        }
        this.mBixbyActionHandler = new BixbyActionHandler();
        if (intent != null) {
            this.mBixbyActionHandler.executeAction(getApplicationContext(), intent);
        }
        this.mHandler.postDelayed(new C04762(), 1000);
    }

    private void createTabs() {
        Log.secD("ClockPackage", "createTabs()");
        this.mFragmentManager = getSupportFragmentManager();
        this.mTabLayout = (ClockTabLayout) findViewById(R.id.tabs);
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.alarm)));
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.worldclock)));
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.stopwatch)));
        this.mTabLayout.addTab(this.mTabLayout.newTab().setText(getString(R.string.timer)));
        setCurrentItem();
        this.mTabLayout.setOnTabSelectedListener(new C04773());
        this.mTabLayout.setup(this);
    }

    private void setFragment(int tabPosition) {
        FragmentTransaction transaction = this.mFragmentManager.beginTransaction();
        String tabName = this.mTabName[tabPosition];
        Fragment fragment = this.mFragmentManager.findFragmentByTag(tabName);
        if (this.mFragment != null) {
            transaction.hide(this.mFragment);
        }
        if (fragment != null) {
            this.mFragment = (ClockFragment) fragment;
            transaction.show(fragment);
        } else {
            if (tabPosition == 0) {
                this.mFragment = new AlarmMainFragment();
            } else if (tabPosition == 1) {
                this.mFragment = new WorldclockFragment();
            } else if (tabPosition == 2) {
                this.mFragment = new StopwatchFragment();
            } else {
                this.mFragment = new TimerFragment();
            }
            transaction.add(R.id.fragment_container, this.mFragment, tabName);
        }
        transaction.commit();
    }

    private void changeToolBar() {
        if (this.mFragment != null && this.mFragment.getView() != null) {
            Toolbar toolbar = (Toolbar) this.mFragment.getView().findViewById(R.id.toolbar);
            CharSequence title = toolbar.getTitle();
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null && title == null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    private void setCurrentItem() {
        if (this.mTabLayout.isEnabled()) {
            int tabPosition = ClockTab.getCurrentTab();
            Tab tab = this.mTabLayout.getTabAt(tabPosition);
            if (tab != null) {
                tab.select();
                setFragment(tabPosition);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        Log.secD("ClockPackage", "onResume()");
        Log.m43i("VerificationLog", "-- onResume --");
        if (this.mTabLayout != null) {
            this.mTabLayout.setResumeStatus(true);
        }
        changeToolBar();
        this.mHandler.postDelayed(new C04784(), 1000);
        if (sIsClockPackageCreated && (this.mCoverManager == null || !this.mCoverManager.isCoverClosed())) {
            setIsClockPackageResumed(true);
        }
        Log.m43i("VerificationLog", "-- Executed --");
    }

    public void onPause() {
        super.onPause();
        Log.secD("ClockPackage", "onPause()");
        if (this.mTabLayout != null) {
            this.mTabLayout.setResumeStatus(false);
        }
        if (!isChangingConfigurations()) {
            setIsClockPackageResumed(false);
        }
    }

    public void onStop() {
        super.onStop();
        Log.secD("ClockPackage", "onStop()");
    }

    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.secD("ClockPackage", "onSaveInstanceState()");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.secD("ClockPackage", "onRequestPermissionsResult");
    }

    protected void onDestroy() {
        Log.secD("ClockPackage", "onDestroy()");
        CheckForUpdates.setCheckForUpdatesListener(this, null, false);
        setSupportActionBar(null);
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public void onMultiWindowModeChanged(boolean isMultiWindowMode) {
        super.onMultiWindowModeChanged(isMultiWindowMode);
        for (String tabName : this.mTabName) {
            ClockFragment fragment = (ClockFragment) this.mFragmentManager.findFragmentByTag(tabName);
            if (fragment != null) {
                fragment.onMultiWindowModeChanged(isMultiWindowMode);
            }
        }
    }

    public void onShowBottomNavigationView() {
        if (!this.mIsBottomNavigationVisible) {
            this.mIsBottomNavigationVisible = true;
            BottomNavigationView bottomNavigation = getBottomNavigation();
            bottomNavigation.setVisibility(0);
            bottomNavigation.startAnimation(getTranslationAnimation(true));
        }
    }

    public void onHideBottomNavigationView() {
        if (this.mIsBottomNavigationVisible) {
            this.mIsBottomNavigationVisible = false;
            BottomNavigationView bottomNavigation = getBottomNavigation();
            Animation animation = getTranslationAnimation(false);
            animation.setAnimationListener(new C04795());
            bottomNavigation.setVisibility(8);
            bottomNavigation.startAnimation(animation);
        }
    }

    public void onSupportActionModeStarted(ActionMode mode) {
        Log.secD("ClockPackage", "onSupportActionModeStarted");
        super.onSupportActionModeStarted(mode);
        this.mIsActionMode = true;
        this.mTabLayout.setVisibility(8);
        this.mTabLayout.startAnimation(getTranslationAnimation(false));
        BottomNavigationView bottomNavigation = getBottomNavigation();
        ClockFragment fragment = (ClockFragment) getSupportFragmentManager().findFragmentByTag(this.mTabName[ClockTab.getCurrentTab()]);
        if (fragment != null) {
            fragment.onStartActionMode(bottomNavigation);
        }
    }

    public void onSupportActionModeFinished(ActionMode mode) {
        Log.secD("ClockPackage", "onSupportActionModeFinished");
        super.onSupportActionModeFinished(mode);
        this.mIsActionMode = false;
        if (this.mIsBottomNavigationVisible) {
            onHideBottomNavigationView();
        } else {
            this.mTabLayout.setVisibility(0);
            this.mTabLayout.startAnimation(getTranslationAnimation(true));
        }
        ClockFragment fragment = (ClockFragment) getSupportFragmentManager().findFragmentByTag(this.mTabName[ClockTab.getCurrentTab()]);
        if (fragment != null) {
            fragment.onFinishActionMode();
        }
    }

    private BottomNavigationView getBottomNavigation() {
        ViewStub bottomNavigationViewStub;
        int tabPosition = ClockTab.getCurrentTab();
        if (tabPosition == 3) {
            bottomNavigationViewStub = (ViewStub) findViewById(R.id.clock_bottom_navigation_edit_delete_viewstub);
        } else {
            bottomNavigationViewStub = (ViewStub) findViewById(R.id.clock_bottom_navigation_delete_viewstub);
        }
        if (bottomNavigationViewStub != null) {
            bottomNavigationViewStub.inflate();
        }
        if (tabPosition == 3) {
            return (BottomNavigationView) findViewById(R.id.bottom_navigation_edit_delete);
        }
        return (BottomNavigationView) findViewById(R.id.bottom_navigation_delete);
    }

    private Animation getTranslationAnimation(boolean isTransitionIn) {
        float fromYValue;
        float toYValue = 1.0f;
        if (isTransitionIn) {
            fromYValue = 1.0f;
        } else {
            fromYValue = 0.0f;
        }
        if (isTransitionIn) {
            toYValue = 0.0f;
        }
        int duration = isTransitionIn ? 400 : 300;
        TranslateAnimation translate = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, fromYValue, 1, toYValue);
        translate.setInterpolator(new SineInOut90());
        translate.setDuration((long) duration);
        return translate;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        View focusedTab = this.mTabLayout.getFocusedChild();
        int keyCode = event.getKeyCode();
        Log.secD("ClockPackage", "dispatchKeyEvent() event keyCode =" + keyCode + ", focusedTab = " + focusedTab);
        if (this.mFragment == null || !this.mFragment.isResumed()) {
            Log.secE("ClockPackage", "Current fragment is null. or not resumed");
            return super.dispatchKeyEvent(event);
        } else if (focusedTab == null) {
            if (this.mFragment.onClockDispatchKeyEvent(event, null) || super.dispatchKeyEvent(event)) {
                return true;
            }
            return false;
        } else if ((keyCode != 42 && keyCode != 29 && keyCode != 32 && keyCode != 112) || (!ClockTab.isAlarmTab() && !ClockTab.isWorldclockTab())) {
            return super.dispatchKeyEvent(event);
        } else {
            if (this.mFragment.onClockDispatchKeyEvent(event, focusedTab) || super.dispatchKeyEvent(event)) {
                return true;
            }
            return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clock_contact_us_menu, menu);
        this.mMenu = menu;
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.secD("ClockPackage", "onPrepareOptionsMenu current tab : " + ClockTab.getCurrentTab());
        String VOC_PACKAGE_NAME = "com.samsung.android.voc";
        if (!StateUtils.isUltraPowerSavingMode(getApplicationContext()) && Feature.hasPackage(getApplicationContext(), VOC_PACKAGE_NAME) && isSupportedVersion(VOC_PACKAGE_NAME)) {
            menu.findItem(R.id.menu_contact_us).setVisible(true);
        } else {
            menu.findItem(R.id.menu_contact_us).setVisible(false);
        }
        menu.findItem(R.id.menu_about_clock).setVisible(true);
        initMenuBadge();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_contact_us:
                Log.secD("ClockPackage", "menu_contact_us Clicked.");
                String packageName = getPackageName();
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("voc://view/contactUs"));
                intent.putExtra("packageName", packageName);
                intent.putExtra("appId", "s0xnr23u43");
                intent.putExtra("appName", "Clock");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    insertSaLogForMenuButton("1007");
                    startActivity(intent);
                    break;
                }
                break;
            case R.id.menu_about_clock:
                Log.secD("ClockPackage", "menu_about_clock Clicked.");
                startActivityForResult(new Intent(this, AboutClockActivity.class), 4);
                insertSaLogForMenuButton("1201");
                break;
            default:
                return false;
        }
        return true;
    }

    private void insertSaLogForMenuButton(String eventId) {
        if (ClockTab.isAlarmTab()) {
            ClockUtils.insertSaLog("101", eventId);
        } else if (ClockTab.isWorldclockTab()) {
            ClockUtils.insertSaLog("110", eventId);
        } else if (ClockTab.isStopWatchTab()) {
            ClockUtils.insertSaLog("120", eventId);
        } else if (ClockTab.isTimerTab()) {
            ClockUtils.insertSaLog("130", eventId);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            this.mNeedToShowBadge = false;
            initMenuBadge();
        }
    }

    private boolean isSupportedVersion(String packageName) {
        try {
            if (getPackageManager().getPackageInfo(packageName, 0).versionCode >= 170001000) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.secD("ClockPackage", "onConfigurationChanged newConfig.orientation = " + newConfig.orientation);
        hideStatusBarForLandscape();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            ClockTab.getInstance().initTabPosition(getApplicationContext(), intent);
            setCurrentItem();
            Log.secD("ClockPackage", "onNewIntent() /tab = " + ClockTab.getCurrentTab() + ", action = " + intent.getAction());
            this.mBixbyActionHandler.executeAction(getApplicationContext(), intent);
        }
    }

    private void initMenuBadge() {
        if (this.mMenu != null) {
            SeslMenuItem menuBadge = (SeslMenuItem) this.mMenu.findItem(R.id.menu_about_clock);
            if (menuBadge != null) {
                menuBadge.setBadgeText(this.mNeedToShowBadge ? "1" : null);
            }
        }
    }

    private void hideStatusBarForLandscape() {
        if (!Feature.isTablet(getApplicationContext()) && !StateUtils.isContextInDexMode(this)) {
            LayoutParams lp = getWindow().getAttributes();
            if (getResources().getConfiguration().orientation == 2) {
                if (isInMultiWindowMode()) {
                    lp.flags &= -1025;
                } else {
                    lp.flags |= 1024;
                }
                lp.semAddExtensionFlags(1);
            } else {
                lp.flags &= -1025;
                lp.semClearExtensionFlags(1);
            }
            getWindow().setAttributes(lp);
        }
    }

    private void setIsClockPackageResumed(boolean isResumed) {
        if (this.mStopwatchManager == null) {
            this.mStopwatchManager = StopwatchManager.getInstance();
            this.mStopwatchManager.setContext(getApplicationContext());
        }
        if (this.mTimerManager == null) {
            this.mTimerManager = TimerManager.getInstance();
            this.mTimerManager.setContext(getApplicationContext());
        }
        if (this.mStopwatchManager != null) {
            this.mStopwatchManager.setIsClockPackageResumed(isResumed);
        }
        if (this.mTimerManager != null) {
            this.mTimerManager.setIsClockPackageResumed(isResumed);
        }
    }
}
