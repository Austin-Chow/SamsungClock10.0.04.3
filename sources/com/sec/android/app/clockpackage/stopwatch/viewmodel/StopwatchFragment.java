package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.StopwatchTimerCommonFragment;
import com.sec.android.app.clockpackage.stopwatch.C0706R;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchBtnViewModelListener;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchListViewModelListener;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchManagerListener;
import com.sec.android.app.clockpackage.stopwatch.model.StopwatchData;
import com.sec.android.app.clockpackage.stopwatch.view.StopwatchLapTimeView;
import com.sec.android.app.clockpackage.stopwatch.view.StopwatchTimeView;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class StopwatchFragment extends StopwatchTimerCommonFragment {
    private String mCurrentLocale;
    private ImageView mDivider;
    private RelativeLayout mEmptyTextLayout;
    private Handler mHandler = new Handler();
    private StopwatchLapTimeView mLapTimeView;
    private LayoutParams mLapTimeViewParam;
    private RelativeLayout mMainLayout;
    private LayoutParams mMainParam;
    private int mOrientation;
    private SharedPreferences mPref = null;
    private OnSharedPreferenceChangeListener mSharedPreferenceChangeListener = null;
    private StopwatchBtnViewModel mStopwatchBtnViewModel;
    private final StopwatchBtnViewModelListener mStopwatchBtnViewModelListener = new C07176();
    private StopwatchListViewModel mStopwatchListViewModel = null;
    private StopwatchListViewModelListener mStopwatchListViewModelListener = new C07165();
    private StopwatchManager mStopwatchManager;
    StopwatchManagerListenerImpl mStopwatchManagerListener = new StopwatchManagerListenerImpl();
    private StopwatchTimeView mTimeView;
    private int mTimeViewCollapseHeight;
    private int mTimeViewExpandHeight;
    private RelativeLayout mTimeViewLayout;
    private int mTimeViewMoveEnd;
    private int mTimeViewMoveStart;
    private LayoutParams mTimeViewParam;

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchFragment$1 */
    class C07121 implements OnSharedPreferenceChangeListener {
        C07121() {
        }

        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            Log.secD("StopwatchFragment", "onSharedPreferenceChanged()");
            if (prefs.equals(StopwatchFragment.this.mPref) && !key.equals("stopwatch_lapcount") && !key.startsWith("stopwatch_elapsed_time")) {
                Log.secD("StopwatchFragment", "onSharedPreferenceChanged() : STOPWATCH_LAPCOUNT, STOPWATCH_ELAPSED_TIME");
                StopwatchFragment.this.mStopwatchManager.restoreSharedPreference();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchFragment$2 */
    class C07132 implements Runnable {
        C07132() {
        }

        public void run() {
            StopwatchFragment.this.mEmptyTextLayout.setVisibility(0);
            StopwatchFragment.this.mDivider.setVisibility(0);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchFragment$3 */
    class C07143 implements Runnable {
        C07143() {
        }

        public void run() {
            if (StopwatchFragment.this.mStopwatchListViewModel != null && StopwatchFragment.this.mStopwatchListViewModel.getArrayAdapter() != null) {
                StopwatchFragment.this.mStopwatchListViewModel.getArrayAdapter().notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchFragment$5 */
    class C07165 implements StopwatchListViewModelListener {
        C07165() {
        }

        public void onExceededMaxCount() {
            if (StopwatchFragment.this.mStopwatchBtnViewModel != null) {
                StopwatchFragment.this.mStopwatchBtnViewModel.exceededMaxCount();
            }
        }

        public int onGetVerticalLocation() {
            return ((StopwatchFragment.this.mTimeViewExpandHeight - StopwatchFragment.this.mTimeViewCollapseHeight) / 2) - (StopwatchFragment.this.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_list_copypopup_layout_height) / 2);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchFragment$6 */
    class C07176 implements StopwatchBtnViewModelListener {
        C07176() {
        }

        public void onStartBtn() {
            StopwatchFragment.this.start();
        }

        public void onStopBtn() {
            StopwatchFragment.this.stop();
        }

        public void onResumeBtn() {
            StopwatchFragment.this.resume();
        }

        public void onResetBtn() {
            StopwatchFragment.this.reset();
        }

        public boolean isFragmentAdded() {
            return StopwatchFragment.this.isAdded();
        }

        public void onSetViewState() {
            StopwatchFragment.this.setViewState(false);
        }

        public void onDismissCopyPopupWindow() {
            if (StopwatchFragment.this.mStopwatchListViewModel != null) {
                StopwatchFragment.this.mStopwatchListViewModel.dismissPopupWindow();
            }
        }
    }

    static class StopwatchManagerListenerImpl implements StopwatchManagerListener {
        private WeakReference<StopwatchFragment> mFragment;

        private StopwatchManagerListenerImpl(StopwatchFragment fragment) {
            this.mFragment = new WeakReference(fragment);
        }

        public void onUpdateTimeView() {
            StopwatchFragment fragment = (StopwatchFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.updateTimeView();
            }
        }

        public void onSetViewState() {
            StopwatchFragment fragment = (StopwatchFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.setViewState(false);
            }
        }

        public void onReset() {
            StopwatchFragment fragment = (StopwatchFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.reset();
            }
        }

        public void onAddLap() {
            StopwatchFragment fragment = (StopwatchFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.addLap();
            }
        }

        public void onSetLapList() {
            StopwatchFragment fragment = (StopwatchFragment) this.mFragment.get();
            if (fragment != null && fragment.mStopwatchListViewModel != null) {
                fragment.mStopwatchListViewModel.setLapList();
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("StopwatchFragment", "onCreate()");
        TAG_SUB = "StopwatchFragment";
        this.mStopwatchManager = StopwatchManager.getInstance();
        this.mStopwatchManager.setContext(this.mActivity.getApplicationContext());
        this.mStopwatchManager.setListener(this.mStopwatchManagerListener);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        Log.secD("StopwatchFragment", "onCreateView()");
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(C0706R.layout.stopwatch, container, false);
        this.mFragmentView = viewGroup;
        if (savedInstanceState != null) {
            this.mStopwatchManager.setListItems(savedInstanceState.getParcelableArrayList("stopwatch_list"));
        } else {
            Log.secD("StopwatchFragment", "onCreate() /savedInstanceStat is null");
        }
        initViews();
        this.mCurrentLocale = Locale.getDefault().getLanguage();
        this.mStopwatchManager.restoreSharedPreference();
        if (StopwatchData.getLapCount() > 0) {
            this.mLapTimeView.setVisibility(0);
            if (this.mStopwatchListViewModel != null) {
                this.mStopwatchListViewModel.inflateListLayout();
            }
        }
        if (Feature.isBixbySupported()) {
            String bixbyAction = this.mActivity.getIntent().getStringExtra("StopwatchTimerAction");
            Log.secD("StopwatchFragment", "Bixby 2.0 Action : " + bixbyAction);
            if (bixbyAction != null) {
                new StopwatchBixbyActionHandler(new StopwatchBixbyActionListenerImpl(this.mActivity.getApplicationContext())).onAction(bixbyAction);
            }
        }
        Toolbar toolbar = (Toolbar) this.mFragmentView.findViewById(C0706R.id.toolbar);
        AppCompatActivity activity = this.mActivity;
        if (activity == null) {
            return viewGroup;
        }
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            return viewGroup;
        }
        actionBar.setDisplayShowTitleEnabled(false);
        return viewGroup;
    }

    public void onStart() {
        super.onStart();
        Log.secD("StopwatchFragment", "onStart()");
        this.mStopwatchManager.fragmentStarted();
        this.mTimeView.init();
        updateStopwatchTimeViewPosition();
        setButtonRule();
        setViewState(true);
    }

    public void onResume() {
        super.onResume();
        Log.secD("StopwatchFragment", "onResume()");
        this.mPref = this.mActivity.getSharedPreferences("Stopwatch", 0);
        this.mSharedPreferenceChangeListener = new C07121();
        this.mPref.registerOnSharedPreferenceChangeListener(this.mSharedPreferenceChangeListener);
        if (StopwatchData.getLapCount() >= 999 && this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.setLapBtnState(false);
        }
        timeViewFocusable(false);
        if (this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.setStopBtnText();
        }
    }

    public void onStop() {
        super.onStop();
        Log.secD("StopwatchFragment", "onStop()");
        if (!StopwatchManager.sIsRebootSequence) {
            this.mStopwatchManager.saveSharedPreference(this.mCurrentLocale);
        }
        this.mStopwatchManager.fragmentStopped(this.mStopwatchManagerListener);
    }

    private void updateStopwatchTimeViewPosition() {
        Log.secD("StopwatchFragment", "updateStopwatchTimeViewPosition()");
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        this.mOrientation = getResources().getConfiguration().orientation;
        int upperTabHeight = ClockUtils.getActionBarHeight(this.mActivity) + getResources().getDimensionPixelSize(C0706R.dimen.clock_tab_height);
        int navigationBarHeight = 0;
        int buttonHeight = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_layout_height) + res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_margin_top);
        if (this.mActivity.isInMultiWindowMode()) {
            if (ClockUtils.convertDpToPixel(this.mActivity, config.screenHeightDp) < res.getDimensionPixelSize(C0706R.dimen.timer_common_multiwindow_phase_3)) {
                buttonHeight = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_multiwindow_height);
            } else {
                buttonHeight = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_height);
            }
        }
        if (this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.updateButtonLayout(res);
        }
        this.mStopwatchBtnViewModel.resizeButtonText();
        if (!(this.mActivity.isInMultiWindowMode() || this.mOrientation != 1 || StateUtils.isVisibleNaviBar(this.mActivity.getApplicationContext()))) {
            navigationBarHeight = ClockUtils.getNavigationBarHeight(this.mActivity);
        }
        int convertDpToPixel = ClockUtils.convertDpToPixel(this.mActivity, config.screenHeightDp);
        int statusBarHeight = (this.mActivity.isInMultiWindowMode() || this.mOrientation != 2) ? 0 : ClockUtils.getStatusBarHeight(this.mActivity);
        this.mTimeViewExpandHeight = (((statusBarHeight + convertDpToPixel) + navigationBarHeight) - upperTabHeight) - buttonHeight;
        setTimeViewRatioPosition();
        setTimeViewMultiWindowPosition(config);
        if (this.mStopwatchListViewModel != null) {
            this.mStopwatchListViewModel.updateStopwatchListPosition();
            this.mStopwatchListViewModel.getListHeight(getListViewHeight());
        }
        if (StateUtils.isTalkBackEnabled(this.mActivity.getApplicationContext())) {
            this.mTimeViewCollapseHeight = (this.mTimeViewExpandHeight - getListViewHeight()) + 3;
        } else {
            this.mTimeViewCollapseHeight = (this.mTimeViewExpandHeight - getListViewHeight()) - 2;
        }
        if (this.mTimeView.getTimeViewHeight() + this.mLapTimeView.getLapTimeViewHeight() > this.mTimeViewCollapseHeight) {
            this.mTimeView.updateTextSize();
            this.mLapTimeView.updateTextSize(true);
            this.mTimeViewCollapseHeight = this.mTimeView.getTimeViewHeight() + this.mLapTimeView.getLapTimeViewHeight();
        }
        updateTimeViewParams(config);
        changeLayoutByOrientation();
        if (this.mOrientation == 2 && StateUtils.isTalkBackEnabled(this.mActivity.getApplicationContext())) {
            this.mTimeViewLayout.setFocusable(false);
        } else {
            this.mTimeViewLayout.setFocusable(true);
        }
    }

    private void setButtonRule() {
        if (!StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            this.mStopwatchBtnViewModel.setPortraitButtonRule();
        } else if (StopwatchData.getStopwatchState() != 3) {
            this.mStopwatchBtnViewModel.setLandscapeButtonRule();
        } else {
            this.mStopwatchBtnViewModel.setPortraitButtonRule();
        }
    }

    private int getListViewHeight() {
        int listItemHeight = getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_listview_line_height);
        int listItemNumber = (this.mTimeViewExpandHeight - this.mTimeViewCollapseHeight) / listItemHeight;
        if (((double) ((((float) (this.mTimeViewExpandHeight - this.mTimeViewCollapseHeight)) / ((float) listItemHeight)) - ((float) listItemNumber))) > 0.5d) {
            listItemNumber++;
        }
        if (this.mTimeView != null && this.mLapTimeView != null && this.mActivity.isInMultiWindowMode() && this.mTimeView.getTimeViewHeight() + this.mLapTimeView.getLapTimeViewHeight() > this.mTimeViewCollapseHeight) {
            listItemNumber--;
            if (listItemNumber == 0) {
                listItemNumber = 1;
            }
        }
        return listItemNumber * listItemHeight;
    }

    private void updateTimeViewParams(Configuration config) {
        if (this.mTimeViewLayout != null) {
            this.mTimeViewParam = (LayoutParams) this.mTimeViewLayout.getLayoutParams();
        }
        if (this.mMainLayout != null) {
            this.mMainParam = (LayoutParams) this.mMainLayout.getLayoutParams();
        }
        if (this.mLapTimeView != null) {
            this.mLapTimeViewParam = (LayoutParams) this.mLapTimeView.getLayoutParams();
        }
        if (StopwatchData.getLapCount() == 0 || StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            this.mTimeViewParam.height = -1;
        } else {
            this.mTimeViewParam.height = this.mTimeViewCollapseHeight;
        }
        if (!StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext()) || StopwatchData.getStopwatchState() == 3) {
            this.mTimeViewParam.width = -1;
        } else {
            this.mTimeViewParam.width = ClockUtils.convertDpToPixel(this.mActivity, config.screenWidthDp) / 2;
        }
        if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            this.mTimeViewParam.setMarginStart(getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_landscape_timeview_margin));
            this.mTimeViewParam.topMargin = getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_timeview_margin_top);
            this.mTimeViewMoveEnd = ClockUtils.convertDpToPixel(this.mActivity, config.screenWidthDp) / 2;
            this.mTimeViewMoveStart = ClockUtils.convertDpToPixel(this.mActivity, config.screenWidthDp);
            return;
        }
        if (this.mActivity.isInMultiWindowMode()) {
            this.mLapTimeViewParam.topMargin = 0;
        } else {
            this.mLapTimeViewParam.topMargin = getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_laptime_margin_top);
        }
        this.mTimeViewParam.topMargin = 0;
        this.mTimeViewParam.width = -1;
        this.mTimeViewParam.setMarginStart(0);
        this.mTimeViewMoveEnd = this.mTimeViewCollapseHeight;
        this.mTimeViewMoveStart = this.mTimeViewExpandHeight;
    }

    private void changeLayoutByOrientation() {
        if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            this.mMainParam.removeRule(2);
            this.mMainParam.addRule(16, C0706R.id.stopwatch_no_list_items_text_layout);
            if (StopwatchData.getLapCount() != 0 || StopwatchData.getStopwatchState() == 3) {
                this.mEmptyTextLayout.setVisibility(8);
            } else {
                this.mEmptyTextLayout.setVisibility(0);
            }
            if (StopwatchData.getStopwatchState() != 3) {
                this.mDivider.setVisibility(0);
                return;
            }
            return;
        }
        this.mMainParam.removeRule(16);
        this.mMainParam.addRule(2, C0706R.id.stopwatch_button);
        this.mEmptyTextLayout.setVisibility(8);
        this.mDivider.setVisibility(8);
    }

    public void onPause() {
        Log.secD("StopwatchFragment", "onPause()");
        super.onPause();
        this.mPref = this.mActivity.getSharedPreferences("Stopwatch", 0);
        this.mPref.unregisterOnSharedPreferenceChangeListener(this.mSharedPreferenceChangeListener);
        this.mSharedPreferenceChangeListener = null;
        if (!StopwatchManager.sIsRebootSequence) {
            this.mStopwatchManager.saveSharedPreference(this.mCurrentLocale);
        }
        if (this.mStopwatchListViewModel != null) {
            this.mStopwatchListViewModel.dismissPopupWindow();
        }
    }

    public void onTabSelected() {
        super.onTabSelected();
        this.mStopwatchBtnViewModel.resizeButtonText();
    }

    public void onDestroy() {
        Log.secD("StopwatchFragment", "onDestroy()");
        if (this.mTimeView != null) {
            this.mTimeView.clearAnimation();
            this.mTimeView.destroy();
            this.mTimeView = null;
        }
        if (this.mLapTimeView != null) {
            this.mLapTimeView.clearAnimation();
            this.mLapTimeView.destroy();
            this.mLapTimeView = null;
        }
        if (this.mTimeViewLayout != null) {
            this.mTimeViewLayout.removeAllViews();
            this.mTimeViewLayout.destroyDrawingCache();
            this.mTimeViewLayout = null;
        }
        if (this.mEmptyTextLayout != null) {
            this.mEmptyTextLayout.removeAllViews();
            this.mEmptyTextLayout.destroyDrawingCache();
            this.mEmptyTextLayout = null;
        }
        if (this.mMainLayout != null) {
            this.mMainLayout.removeAllViews();
            this.mMainLayout.destroyDrawingCache();
            this.mMainLayout = null;
        }
        if (this.mStopwatchListViewModel != null) {
            this.mStopwatchListViewModel.releaseInstance();
        }
        if (this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.releaseInstance();
        }
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.secD("StopwatchFragment", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        try {
            if (!(this.mStopwatchListViewModel == null || this.mStopwatchListViewModel.getLapsList() == null)) {
                this.mStopwatchListViewModel.getLapsList().setAdapter(this.mStopwatchListViewModel.getArrayAdapter());
                this.mStopwatchListViewModel.getLapsList().setSelection(this.mStopwatchListViewModel.getLastScrollPosition());
                this.mStopwatchListViewModel.dismissPopupWindow();
            }
            updateStopwatchTimeViewPosition();
            setButtonRule();
        } catch (NullPointerException e) {
            Log.secE("StopwatchFragment", "Exception : " + e.toString());
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Log.secD("StopwatchFragment", "onSaveInstanceState() called");
        outState.putInt("stopwatch_current_state", StopwatchData.getStopwatchState());
        outState.putInt("stopwatch_lapcount", StopwatchData.getLapCount());
        outState.putLong("stopwatch_elapsed_realtime", SystemClock.elapsedRealtime());
        outState.putLong("stopwatch_elapsed_time", StopwatchData.getElapsedMillis());
        outState.putLong("stopwatch_elapsed_time", StopwatchData.sElapsedMillis);
        outState.putLong("stopwatch_elapsed_time_before", StopwatchData.sElapsedMillisBefore);
        outState.putParcelableArrayList("stopwatch_list", this.mStopwatchManager.getListItems());
        super.onSaveInstanceState(outState);
    }

    private void setTimeViewRatioPosition() {
        if (Feature.isTablet(this.mActivity.getApplicationContext()) || StateUtils.isContextInDexMode(this.mActivity)) {
            if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
                this.mTimeViewCollapseHeight = this.mTimeViewExpandHeight;
            } else if (this.mOrientation == 2) {
                this.mTimeViewCollapseHeight = (this.mTimeViewExpandHeight * 2) / 5;
            } else {
                this.mTimeViewCollapseHeight = this.mTimeViewExpandHeight / 3;
            }
        } else if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            this.mTimeViewCollapseHeight = this.mTimeViewExpandHeight;
        } else if (this.mOrientation == 2) {
            this.mTimeViewCollapseHeight = this.mTimeViewExpandHeight / 2;
        } else {
            this.mTimeViewCollapseHeight = (this.mTimeViewExpandHeight * 100) / 225;
        }
    }

    private void setTimeViewMultiWindowPosition(Configuration config) {
        if (this.mActivity.isInMultiWindowMode()) {
            initForMultiWindowMode();
            if (this.mTimeViewCollapseHeight < getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_common_multiwindow_min_timeview_height)) {
                this.mTimeViewCollapseHeight = getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_common_multiwindow_min_timeview_height);
            }
            if (getResources().getDisplayMetrics().heightPixels <= getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_common_min_height) || getResources().getDisplayMetrics().widthPixels <= getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_common_min_width) || (StateUtils.isContextInDexMode(this.mActivity) && config.screenWidthDp < getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_common_dex_boundary_for_textsize))) {
                this.mTimeView.updateTextSize();
                this.mLapTimeView.updateTextSize(true);
            }
        }
    }

    private void initViews() {
        Log.secD("StopwatchFragment", "initViews()");
        this.mDivider = (ImageView) this.mFragmentView.findViewById(C0706R.id.stopwatch_divider);
        this.mTimeView = (StopwatchTimeView) this.mFragmentView.findViewById(C0706R.id.stopwatch_timeview);
        this.mTimeViewLayout = (RelativeLayout) this.mFragmentView.findViewById(C0706R.id.stopwatch_timeview_layout);
        this.mStopwatchBtnViewModel = new StopwatchBtnViewModel(this.mActivity, this.mFragmentView, this.mStopwatchManager, this.mStopwatchBtnViewModelListener);
        this.mEmptyTextLayout = (RelativeLayout) this.mFragmentView.findViewById(C0706R.id.stopwatch_no_list_items_text_layout);
        this.mLapTimeView = (StopwatchLapTimeView) this.mFragmentView.findViewById(C0706R.id.stopwatch_laptime_layout);
        this.mStopwatchListViewModel = new StopwatchListViewModel(this.mActivity, this.mFragmentView, this.mStopwatchManager, this.mStopwatchListViewModelListener);
        this.mMainLayout = (RelativeLayout) this.mFragmentView.findViewById(C0706R.id.stopwatch_main_layout);
        this.mLapTimeView.init();
    }

    private void initForMultiWindowMode() {
        Log.secD("StopwatchFragment", "initForMultiWindowMode()");
        if (this.mTimeView != null) {
            this.mTimeView.init();
            this.mTimeView.updateTimeView(StopwatchData.getHour(), StopwatchData.getMinute(), StopwatchData.getSecond(), StopwatchData.getMillis());
        }
        if (this.mLapTimeView != null) {
            this.mLapTimeView.init();
            this.mLapTimeView.updateLapTime(StopwatchData.getLapHour(), StopwatchData.getLapMinute(), StopwatchData.getLapSecond(), StopwatchData.getLapMillis());
        }
    }

    public void onMultiWindowModeChanged(boolean isMultiWindowMode) {
        Log.secD("StopwatchFragment", "onMultiWindowModeChanged()");
        if (!(this.mStopwatchListViewModel == null || this.mStopwatchListViewModel.getLapsList() == null)) {
            this.mStopwatchListViewModel.getLapsList().setAdapter(this.mStopwatchListViewModel.getArrayAdapter());
            this.mStopwatchListViewModel.getLapsList().setSelection(this.mStopwatchListViewModel.getLastScrollPosition());
        }
        if (!isMultiWindowMode) {
            initForMultiWindowMode();
            updateStopwatchTimeViewPosition();
        }
    }

    public boolean onClockDispatchKeyEvent(KeyEvent event, View tab) {
        Log.secD("StopwatchFragment", "onClockDispatchKeyEvent event : " + event);
        int keyCode = event.getKeyCode();
        if (this.mActivity == null) {
            return false;
        }
        if (!(this.mStopwatchListViewModel == null || this.mStopwatchListViewModel.getLapsList() == null || !this.mStopwatchListViewModel.getLapsList().hasFocus())) {
            ((ListAdapter) this.mStopwatchListViewModel.getArrayAdapter()).setAnimate(false);
        }
        if (event.getAction() == 0) {
            switch (keyCode) {
                case 19:
                case 21:
                case 22:
                    if (this.mStopwatchListViewModel == null || !this.mStopwatchListViewModel.isPopupWindowShowing()) {
                        return false;
                    }
                    this.mStopwatchListViewModel.dismissPopupWindow();
                    return true;
                case 42:
                    if (StateUtils.isRemoteAction(event)) {
                        if (StopwatchData.getStopwatchState() == 3) {
                            this.mStopwatchManager.start();
                            start();
                            return true;
                        } else if (StopwatchData.getStopwatchState() == 1 && StopwatchData.getLapCount() < 999) {
                            this.mStopwatchManager.addLap();
                            addLap();
                            return true;
                        }
                    }
                    return false;
                default:
                    if (this.mStopwatchListViewModel != null && this.mStopwatchListViewModel.isPopupWindowShowing()) {
                        this.mStopwatchListViewModel.dismissPopupWindow();
                        return true;
                    }
            }
        }
        return false;
    }

    private void updateTimeView() {
        if (StopwatchData.getElapsedMillis() >= 359999990) {
            if (this.mStopwatchBtnViewModel != null) {
                this.mStopwatchBtnViewModel.setStartBtnState(false);
                this.mStopwatchBtnViewModel.setBtnAfterStarted(false);
                this.mStopwatchBtnViewModel.setBtnAfterStopped(true);
            }
            reset();
        }
        StopwatchData.setInputTime(StopwatchData.getElapsedMillis());
        int hour = StopwatchData.getHour();
        int minute = StopwatchData.getMinute();
        int second = StopwatchData.getSecond();
        int millis = StopwatchData.getMillis();
        if (this.mTimeView == null || this.mLapTimeView == null) {
            Log.secD("StopwatchFragment", "updateTimeView() NullPointerException");
            return;
        }
        this.mTimeView.updateTimeView(hour, minute, second, millis);
        StopwatchData.setInputLapTime(StopwatchData.getElapsedMillis() - StopwatchData.getElapsedMillisBefore());
        this.mLapTimeView.updateLapTime(StopwatchData.getLapHour(), StopwatchData.getLapMinute(), StopwatchData.getLapSecond(), StopwatchData.getLapMillis());
        if (this.mStopwatchManager.getListItems().size() != 0) {
            this.mTimeView.setContentDescription(StopwatchUtils.getDescriptionString(this.mActivity.getApplicationContext(), hour, minute, second, millis) + ' ' + this.mActivity.getResources().getString(C0706R.string.stopwatch_list_split));
            this.mLapTimeView.setContentDescription(StopwatchUtils.getDescriptionString(this.mActivity.getApplicationContext(), StopwatchData.getLapHour(), StopwatchData.getLapMinute(), StopwatchData.getLapSecond(), StopwatchData.getLapMillis()));
            return;
        }
        this.mTimeView.setContentDescription(StopwatchUtils.getDescriptionString(this.mActivity.getApplicationContext(), hour, minute, second, millis));
    }

    private void setViewState(boolean isResume) {
        Log.secD("StopwatchFragment", "setViewState state = " + StopwatchData.getStopwatchState());
        if (this.mActivity != null) {
            switch (StopwatchData.getStopwatchState()) {
                case 1:
                    if (this.mStopwatchListViewModel != null) {
                        this.mStopwatchListViewModel.inflateListLayout();
                    }
                    if (this.mStopwatchBtnViewModel != null) {
                        this.mStopwatchBtnViewModel.setStartedViewState(isResume);
                    }
                    if (StopwatchData.getLapCount() > 0 && this.mLapTimeView != null) {
                        this.mLapTimeView.setVisibility(0);
                        break;
                    }
                case 2:
                    if (this.mStopwatchBtnViewModel != null) {
                        this.mStopwatchBtnViewModel.setStoppedViewState(isResume);
                    }
                    if (StopwatchData.getLapCount() > 0 && this.mLapTimeView != null) {
                        this.mLapTimeView.setVisibility(0);
                        break;
                    }
                case 3:
                    if (this.mStopwatchBtnViewModel != null) {
                        this.mStopwatchBtnViewModel.setResetViewState(isResume);
                    }
                    if (this.mLapTimeView != null) {
                        this.mLapTimeView.setVisibility(4);
                        break;
                    }
                    break;
            }
        }
        updateTimeView();
    }

    private void start() {
        if (this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.setLapBtnState(true);
        }
        if (Feature.isFolder(this.mActivity.getApplicationContext()) || this.mStopwatchBtnViewModel == null) {
            setViewState(false);
        } else {
            this.mStopwatchBtnViewModel.startButtonAnimation((RelativeLayout) this.mFragmentView.findViewById(C0706R.id.stopwatch_button), true);
        }
        if (StopwatchData.getLapCount() <= 1 && StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            collapse(this.mTimeViewLayout);
            this.mHandler.postDelayed(new C07132(), 200);
            setButtonRule();
        }
        acquireDim();
    }

    private void stop() {
        releaseDim();
    }

    private void resume() {
        acquireDim();
    }

    private void reset() {
        if (this.mActivity == null) {
            Log.secD("StopwatchFragment", "reset is null");
            return;
        }
        if (this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.setLapBtnState(false);
        }
        if (!(this.mLapTimeView == null || this.mStopwatchListViewModel == null || this.mStopwatchListViewModel.getListLayout() == null)) {
            this.mLapTimeView.setVisibility(4);
            this.mStopwatchListViewModel.getListLayout().setVisibility(4);
        }
        if (this.mLapTimeView != null) {
            this.mLapTimeView.updateLapTime(StopwatchData.getLapHour(), StopwatchData.getLapMinute(), StopwatchData.getLapSecond(), StopwatchData.getLapMillis());
        }
        if (this.mStopwatchBtnViewModel != null) {
            this.mStopwatchBtnViewModel.setBtnAfterReseted();
        }
        if (this.mTimeView != null) {
            this.mTimeView.updateTimeView(StopwatchData.getHour(), StopwatchData.getMinute(), StopwatchData.getSecond(), StopwatchData.getMillis());
        }
        if (!(!Feature.isTablet(this.mActivity.getApplicationContext()) || this.mTimeView == null || this.mLapTimeView == null)) {
            this.mTimeView.enableMultiSelection(true);
            this.mLapTimeView.enableMultiSelection(true);
        }
        this.mActivity.runOnUiThread(new C07143());
        if (Feature.isFolder(this.mActivity.getApplicationContext()) || this.mStopwatchBtnViewModel == null) {
            setViewState(false);
        } else {
            this.mStopwatchBtnViewModel.startButtonAnimation((RelativeLayout) this.mFragmentView.findViewById(C0706R.id.stopwatch_button), false);
        }
        releaseDim();
        if (StopwatchData.getLapCount() != 0) {
            expand(this.mTimeViewLayout);
        }
        if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            expand(this.mTimeViewLayout);
            this.mEmptyTextLayout.setVisibility(8);
            this.mDivider.setVisibility(8);
            setButtonRule();
        }
        timeViewFocusable(true);
    }

    private void addLap() {
        Log.secD("StopwatchFragment", "addLap");
        if (this.mActivity == null) {
            Log.secD("StopwatchFragment", "addLap is null");
        } else if (this.mTimeView == null) {
            Log.secD("StopwatchFragment", "addLap : mTimeView is null - return");
        } else {
            if (StopwatchData.getLapCount() <= 1 && (this.mActivity.isInMultiWindowMode() || this.mOrientation == 1)) {
                collapse(this.mTimeViewLayout);
            }
            if (this.mLapTimeView != null) {
                this.mLapTimeView.setVisibility(0);
            }
            timeViewFocusable(false);
            if (StopwatchData.getLapCount() == 1) {
                updateStopwatchTimeViewPosition();
            }
            updateTimeView();
            if (this.mStopwatchListViewModel != null) {
                this.mStopwatchListViewModel.addLapList();
            }
        }
    }

    private void expand(View view) {
        slideAnimator(this.mTimeViewMoveEnd, this.mTimeViewMoveStart, view).start();
    }

    private void collapse(View view) {
        slideAnimator(this.mTimeViewMoveStart, this.mTimeViewMoveEnd, view).start();
    }

    private ValueAnimator slideAnimator(int start, int end, final View view) {
        ValueAnimator animator = ValueAnimator.ofInt(new int[]{start, end});
        animator.setDuration(300);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (StopwatchUtils.checkForLandscape(StopwatchFragment.this.mActivity.getApplicationContext())) {
                    layoutParams.width = value;
                } else {
                    layoutParams.height = value;
                }
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void timeViewFocusable(boolean isReset) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mActivity.getSystemService("accessibility");
        if (this.mTimeView == null) {
            return;
        }
        if (!accessibilityManager.isEnabled()) {
            this.mTimeView.setFocusable(false);
        } else if (this.mStopwatchManager.getListItems().size() == 0 || isReset) {
            this.mTimeView.setFocusable(true);
        } else {
            this.mTimeView.setFocusable(false);
        }
    }

    protected boolean isStartedState() {
        return StopwatchData.getStopwatchState() == 1;
    }

    protected void insertSaLogCurrentTab() {
        ClockUtils.insertSaLog("120");
    }
}
