package com.sec.android.app.clockpackage.timer.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.PermissionUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.viewmodel.ClockSmartTip;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;
import com.sec.android.app.clockpackage.common.viewmodel.StopwatchTimerCommonFragment;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerAddPresetPopupListener;
import com.sec.android.app.clockpackage.timer.callback.TimerAnimationViewModelListener;
import com.sec.android.app.clockpackage.timer.callback.TimerBtnViewModelListener;
import com.sec.android.app.clockpackage.timer.callback.TimerManagerListener;
import com.sec.android.app.clockpackage.timer.callback.TimerPickerViewListener;
import com.sec.android.app.clockpackage.timer.callback.TimerPresetViewListener;
import com.sec.android.app.clockpackage.timer.callback.TimerTimeViewListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import com.sec.android.app.clockpackage.timer.view.TimerCircleView;
import com.sec.android.app.clockpackage.timer.view.TimerTimeView;
import java.lang.ref.WeakReference;

public class TimerFragment extends StopwatchTimerCommonFragment {
    private TimerAddPresetPopup mAddPresetPopup = null;
    private TimerBtnViewModel mButtonViewModel = null;
    private TimerCircleView mCircleView;
    private View mInflatedOngoing = null;
    private View mInflatedPreset = null;
    private boolean mIsFirst = true;
    private boolean mIsMultiWindowMode = false;
    private boolean mIsNeverAskAgain = false;
    private boolean mIsRestarted = false;
    private TextView mNameTextView;
    private boolean mNeedFlipAnimation = true;
    private boolean mNeedInvalidateOptionsMenu = false;
    private TimerPickerViewModel mPickerView;
    private TimerPresetViewModel mPresetView;
    private int mPreviousPresetViewHeight = -1;
    private Button mResetBtn;
    private final ClockSmartTip mSmartTip = new ClockSmartTip();
    private TimerTimeView mTimeView;
    private TimerAddPresetPopupListener mTimerAddPresetPopupListener = new TimerAddPresetPopupListener() {
        public boolean isMultiWindowMode() {
            return TimerFragment.this.mIsMultiWindowMode;
        }

        public void onSetSoftInputMode(boolean isInMultiWindowMode) {
            TimerFragment.this.setSoftInputMode(isInMultiWindowMode);
        }

        public void onSetPickerTime(int hour, int minute, int second) {
            TimerFragment.this.mPickerView.setTime(hour, minute, second);
            TimerFragment.this.checkInputData();
        }

        public void onDisablePickerEditMode() {
            TimerFragment.this.disablePickerEditMode(false);
        }

        public long getDuplicatedPresetId(String name, int hour, int minute, int second) {
            return TimerFragment.this.mPresetView != null ? TimerFragment.this.mPresetView.getDuplicatedPresetId(name, hour, minute, second) : -1;
        }

        public void onSetBackupTime(int hour, int minute, int second) {
            if (TimerFragment.this.mPresetView == null) {
                TimerFragment.this.inflatePresetLayout();
            }
            TimerFragment.this.mPresetView.setBackupTime(hour, minute, second);
        }

        public void onSetSelectedPresetId(long id) {
            if (TimerFragment.this.mPresetView == null) {
                TimerFragment.this.inflatePresetLayout();
            }
            TimerFragment.this.mPresetView.setSelectedPresetId(id);
        }

        public TimerPresetItem onGetPresetItemById(long id) {
            if (TimerFragment.this.mPresetView != null) {
                return TimerFragment.this.mPresetView.getPresetItemById(id);
            }
            return null;
        }

        public TimerPresetItem onGetPresetItemByPosition(int position) {
            if (TimerFragment.this.mPresetView != null) {
                return TimerFragment.this.mPresetView.getPresetItemByPosition(position);
            }
            return null;
        }

        public void onUpdatePresetView(boolean isAdded) {
            if (TimerFragment.this.mPresetView == null || !TimerFragment.this.mPresetView.isActionMode()) {
                if (TimerFragment.this.mPresetView == null) {
                    TimerFragment.this.inflatePresetLayout();
                }
                TimerFragment.this.mPresetView.updatePresetListView(isAdded);
                TimerFragment.this.mPresetView.scrollToPresetId(TimerFragment.this.mPresetView.getSelectedPresetId());
                return;
            }
            TimerFragment.this.mPresetView.finishActionMode();
        }
    };
    private TimerAnimationViewModel mTimerAnimationViewModel = null;
    private TimerAnimationViewModelListener mTimerAnimationViewModelListener = new TimerAnimationViewModelListener() {
        public void onSetPresetViewVisibility(int visibility) {
            TimerFragment.this.setPresetViewVisibility(visibility);
        }

        public void onSetViewState(boolean isResume, boolean isUpdateTimeViewVisibility) {
            TimerFragment.this.setViewState(isResume, isUpdateTimeViewVisibility);
        }

        public void onSetTimerNameVisibility() {
            TimerFragment.this.setTimerNameVisibility();
        }

        public int onGetButtonLayoutBottomMargin() {
            if (TimerFragment.this.mButtonViewModel == null) {
                return 0;
            }
            return TimerFragment.this.mButtonViewModel.getButtonLayoutBottomMargin();
        }

        public boolean onIsDisplayTitleView() {
            return TimerFragment.this.isDisplayTitleView();
        }
    };
    private TimerBtnViewModelListener mTimerBtnViewModelListener = new TimerBtnViewModelListener() {
        public boolean isEditMode() {
            return TimerFragment.this.mPickerView != null && TimerFragment.this.mPickerView.isEditMode();
        }

        public void onDisablePickerEditMode() {
            TimerFragment.this.disablePickerEditMode(false);
        }

        public void onStartBtn() {
            TimerFragment.this.mIsRestarted = false;
            TimerFragment.this.start();
        }

        public void onPauseBtn() {
            TimerFragment.this.pause();
        }

        public void onResumeBtn() {
            TimerFragment.this.resume();
        }

        public void onCancelBtn() {
            TimerFragment.this.cancel();
        }

        public void onCheckInputData() {
            TimerFragment.this.checkInputData();
        }
    };
    private Configuration mTimerLastConfiguration;
    private TimerManager mTimerManager;
    TimerManagerListenerImpl mTimerManagerListener = new TimerManagerListenerImpl();
    private String mTimerName;
    private TimerPickerViewListener mTimerPickerViewListener = new TimerPickerViewListener() {

        /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$14$1 */
        class C07871 implements Runnable {
            C07871() {
            }

            public void run() {
                TimerFragment.this.setResetButtonVisibility();
                TimerFragment.this.setPresetViewVisibility();
                TimerFragment.this.setTitleViewVisibility();
            }
        }

        public void onCheckInputData() {
            TimerFragment.this.checkInputData();
        }

        public void onKeypadAnimation(boolean isEditMode) {
            TimerFragment.this.keypadAnimation(isEditMode);
        }

        public void onEditModeChanged() {
            if (StateUtils.isMobileKeyboard(TimerFragment.this.mActivity.getApplicationContext()) || !TimerUtils.isShowIme(TimerFragment.this.mActivity.getApplicationContext())) {
                TimerFragment.this.setResetButtonVisibility();
                TimerFragment.this.setPresetViewVisibility();
            } else if (TimerFragment.this.mIsMultiWindowMode || StateUtils.isContextInDexMode(TimerFragment.this.mActivity)) {
                long j;
                Handler handler = new Handler();
                Runnable c07871 = new C07871();
                if (TimerFragment.this.mPickerView.isEditMode()) {
                    j = 0;
                } else {
                    j = 50;
                }
                handler.postDelayed(c07871, j);
            } else {
                TimerFragment.this.setTitleViewVisibility();
            }
        }

        public boolean isActionMode() {
            return TimerFragment.this.mPresetView != null && TimerFragment.this.mPresetView.isActionMode();
        }

        public void onTimeChanged() {
            if (TimerFragment.this.mPresetView != null) {
                TimerFragment.this.mPresetView.setSelectedPresetId(-1);
            }
            if (TimerFragment.this.mPickerView != null && TimerData.isTimerStateResetedOrNone()) {
                TimerData.savePreviousInput(TimerFragment.this.mPickerView.getHour(), TimerFragment.this.mPickerView.getMinute(), TimerFragment.this.mPickerView.getSecond());
            }
        }

        public long getSelectedPresetId() {
            return TimerFragment.this.mPresetView != null ? TimerFragment.this.mPresetView.getSelectedPresetId() : -1;
        }

        public void onSetSelectedPresetId(long id) {
            if (TimerFragment.this.mPresetView != null) {
                TimerFragment.this.mPresetView.setSelectedPresetId(id);
            }
        }
    };
    private TimerPresetViewListener mTimerPresetViewListener = new TimerPresetViewListener() {
        public void onCreateModifyPresetPopup(long presetId) {
            if (TimerFragment.this.mAddPresetPopup == null) {
                TimerFragment.this.mAddPresetPopup = new TimerAddPresetPopup(TimerFragment.this.mActivity);
                TimerFragment.this.mAddPresetPopup.setTimerAddPresetPopupListener(TimerFragment.this.mTimerAddPresetPopupListener);
            }
            TimerFragment.this.mAddPresetPopup.createAddPresetPopup(false, false, presetId);
        }

        public void onSetPresetViewVisibility() {
            TimerFragment.this.setPresetViewVisibility();
        }

        public void onSetViewEnabled(boolean enabled) {
            TimerFragment.this.setViewEnabled(enabled);
        }

        public void onSetPickerTime(int hour, int minute, int second, boolean needFlipAnimation) {
            TimerFragment.this.mPickerView.setTime(hour, minute, second);
            if (needFlipAnimation) {
                TimerFragment.this.startFlipAnimation(TimerFragment.this.mPickerView.getAlpha() != 1.0f ? Callback.DEFAULT_DRAG_ANIMATION_DURATION : 0);
            }
        }

        public void onDisablePickerEditMode() {
            TimerFragment.this.disablePickerEditMode(false);
        }

        public boolean isEditMode() {
            return TimerFragment.this.mPickerView != null && TimerFragment.this.mPickerView.isEditMode();
        }

        public Toolbar onGetToolbar() {
            return TimerFragment.this.mToolbar;
        }

        public int onGetPickerHeight() {
            return TimerFragment.this.mPickerView != null ? TimerFragment.this.mPickerView.getPickerHeight() : 0;
        }
    };
    private TimerTimeViewListener mTimerTimeViewListener = new TimerTimeViewListener() {
        public int onGetCircleWidth() {
            return TimerFragment.this.mCircleView != null ? TimerFragment.this.mCircleView.getCircleWidth() : 0;
        }

        public int onGetPickerWidth(boolean isColon) {
            return TimerFragment.this.mPickerView != null ? TimerFragment.this.mPickerView.getPickerWidth(isColon) : 0;
        }
    };
    private TimerToneViewModel mTimerToneViewModel = null;
    private RelativeLayout mTitleView;
    private Toolbar mToolbar;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$1 */
    class C07881 implements Runnable {
        C07881() {
        }

        public void run() {
            Log.secD("TimerFragment", "launchSmartTip : SMART_TIP_TIMER");
            TimerFragment.this.mSmartTip.launchSmartTip(TimerFragment.this.mActivity, false, 0, TimerFragment.this.mToolbar);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$2 */
    class C07892 implements Runnable {
        C07892() {
        }

        public void run() {
            boolean z = true;
            if (TimerFragment.this.mPickerView != null) {
                TimerFragment.this.mPickerView.showSoftInput();
            }
            if (TimerFragment.this.mCircleView != null) {
                TimerFragment.this.mCircleView.updateTime(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis());
                TimerCircleView access$500 = TimerFragment.this.mCircleView;
                if (TimerData.getTimerState() != 1) {
                    z = false;
                }
                access$500.resumeView(z);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$3 */
    class C07903 implements Runnable {
        C07903() {
        }

        public void run() {
            TimerFragment.this.mPresetView.setListViewPadding(TimerFragment.this.getResources().getConfiguration().orientation, TimerFragment.this.mIsMultiWindowMode);
            TimerFragment.this.mPickerView.updateLayout();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$4 */
    class C07914 implements Runnable {
        C07914() {
        }

        public void run() {
            if (TimerFragment.this.mPickerView != null) {
                TimerFragment.this.mPickerView.showSoftInput();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$5 */
    class C07925 extends BroadcastReceiver {
        C07925() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("com.samsung.axt9info.close".equals(intent.getAction())) {
                TimerFragment.this.disablePickerEditMode(false);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$6 */
    class C07936 extends BroadcastReceiver {
        C07936() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("TimerFragment", "onReceive() / action = " + action);
            if ("com.sec.android.app.clockpackage.timer.NOTIFY_TIMER_PRESET_CHANGED".equals(action)) {
                if (TimerFragment.this.mPresetView == null) {
                    TimerFragment.this.inflatePresetLayout();
                }
                TimerFragment.this.mPresetView.updatePresetListView(false);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$7 */
    class C07947 implements OnClickListener {
        C07947() {
        }

        public void onClick(View v) {
            if (TimerData.isTimerStateResetedOrNone() && TimerData.getRemainMillis() != 0) {
                TimerFragment.this.mPickerView.setTime(0, 0, 0);
                TimerFragment.this.checkInputData();
                if (TimerFragment.this.mPresetView != null) {
                    TimerFragment.this.mPresetView.setSelectedPresetId(-1);
                }
                ClockUtils.insertSaLog("130", "6003");
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$8 */
    class C07958 implements OnFocusChangeListener {
        C07958() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                TimerFragment.this.disablePickerEditMode(false);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerFragment$9 */
    class C07969 implements Runnable {
        C07969() {
        }

        public void run() {
            TimerFragment.this.setCircleViewVisibility();
            if (TimerFragment.this.mPickerView != null && TimerFragment.this.mPickerView.getVisibility() == 8) {
                TimerFragment.this.setTimeViewVisibility();
            }
            if (TimerFragment.this.mIsMultiWindowMode) {
                TimerFragment.this.setTimerNameVisibility();
            }
        }
    }

    static class TimerManagerListenerImpl implements TimerManagerListener {
        private WeakReference<TimerFragment> mFragment;

        private TimerManagerListenerImpl(TimerFragment fragment) {
            this.mFragment = new WeakReference(fragment);
        }

        public void onResetViewToStart() {
            TimerFragment fragment = (TimerFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.disablePickerEditMode(false);
                if (fragment.mPresetView != null) {
                    fragment.mPresetView.finishActionMode();
                }
                if (fragment.mAddPresetPopup != null) {
                    fragment.mAddPresetPopup.dismissAddPresetPopup();
                }
            }
        }

        public void onSetViewState() {
            TimerFragment fragment = (TimerFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.setViewState(false, true);
            }
        }

        public void onStart() {
            TimerFragment fragment = (TimerFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.start();
            }
        }

        public void onSetStartMode(int mode, String timerName) {
            TimerFragment fragment = (TimerFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.setStartMode(mode, timerName);
            }
        }

        public void onCancel() {
            TimerFragment fragment = (TimerFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.cancel();
            }
        }

        public void onUpdateTime() {
            TimerFragment fragment = (TimerFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.updateTime();
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.secD("TimerFragment", "onCreate()");
        TAG_SUB = "TimerFragment";
        this.mTimerManager = TimerManager.getInstance();
        this.mTimerManager.setTimerManagerListener(this.mTimerManagerListener);
        this.mTimerManager.setContext(this.mActivity.getApplicationContext());
        this.mTimerManager.restoreSharedPreference();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.samsung.axt9info.close");
        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction("com.sec.android.app.clockpackage.timer.NOTIFY_TIMER_PRESET_CHANGED");
        initBroadcastReceiver();
        registerReceiver(filter, localFilter);
        ViewCompat.setOnApplyWindowInsetsListener(this.mActivity.getWindow().getDecorView(), TimerFragment$$Lambda$1.lambdaFactory$(this));
        this.mTimerLastConfiguration = new Configuration(getResources().getConfiguration());
        if (Feature.isBixbySupported()) {
            String bixbyAction = this.mActivity.getIntent().getStringExtra("StopwatchTimerAction");
            this.mActivity.getIntent().removeExtra("StopwatchTimerAction");
            Log.secD("TimerFragment", "Bixby 2.0 Action : " + bixbyAction);
            if (bixbyAction != null) {
                TimerBixbyActionHandler timerBixbyActionHandler = new TimerBixbyActionHandler(new TimerBixbyActionListenerImpl(this.mActivity.getApplicationContext()));
                if ("StartTimer".equals(bixbyAction)) {
                    String presetName = this.mActivity.getIntent().getStringExtra("TimerPresetName");
                    long inputMillis = this.mActivity.getIntent().getLongExtra("TimerInputTime", 0);
                    if (ClockUtils.isEnableString(presetName) || inputMillis > 0) {
                        timerBixbyActionHandler.setSelectedPreset(presetName, inputMillis);
                        if (inputMillis > 0) {
                            if (!TimerData.isTimerStateResetedOrNone()) {
                                this.mTimerManager.setTimerState(3);
                                this.mTimerManager.cancelTimer();
                            }
                            timerBixbyActionHandler.setTimerTime(inputMillis);
                        } else if (this.mActivity.getSharedPreferences("TIMER", 0).getLong("selectedPresetId", -1) == -1) {
                            bixbyAction = "OpenClock";
                        } else if (!TimerData.isTimerStateResetedOrNone()) {
                            this.mTimerManager.setTimerState(3);
                            this.mTimerManager.cancelTimer();
                        }
                    } else if (ClockUtils.sIsTimerAlertStarted) {
                        bixbyAction = "RestartTimer";
                    } else if (TimerData.getTimerState() != 2) {
                        bixbyAction = "OpenClock";
                    }
                }
                timerBixbyActionHandler.onAction(bixbyAction);
            }
        }
    }

    private /* synthetic */ WindowInsetsCompat lambda$onCreate$0(View view, WindowInsetsCompat windowInsetsCompat) {
        updatePresetLayout();
        return ViewCompat.onApplyWindowInsets(view, windowInsetsCompat);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        Log.secD("TimerFragment", "onCreateView()");
        this.mFragmentView = inflater.inflate(C0728R.layout.timer, container, false);
        initViews();
        this.mButtonViewModel = new TimerBtnViewModel(this.mActivity, this.mFragmentView, this.mTimerBtnViewModelListener);
        if (this.mPickerView != null) {
            this.mPickerView.resumeView();
        }
        this.mIsMultiWindowMode = this.mActivity.isInMultiWindowMode();
        setHasOptionsMenu(true);
        this.mToolbar = (Toolbar) this.mFragmentView.findViewById(C0728R.id.toolbar);
        AppCompatActivity activity = this.mActivity;
        if (activity != null) {
            activity.setSupportActionBar(this.mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
        return this.mFragmentView;
    }

    private void inflatePresetLayout() {
        Log.secD("TimerFragment", "inflatePresetLayout()");
        this.mInflatedPreset = ((ViewStub) this.mFragmentView.findViewById(C0728R.id.timer_preset_stub)).inflate();
        this.mPresetView = (TimerPresetViewModel) this.mInflatedPreset.findViewById(C0728R.id.timer_preset_layout);
        this.mPresetView.initView();
        this.mPresetView.setTimerPresetViewListener(this.mTimerPresetViewListener);
        updatePresetLayout();
    }

    private void inflateCircleView() {
        Log.secD("TimerFragment", "inflateCircleView()");
        if (this.mInflatedOngoing == null) {
            this.mInflatedOngoing = ((ViewStub) this.mFragmentView.findViewById(C0728R.id.timer_ongoing_stub)).inflate();
        }
        if (this.mCircleView == null) {
            this.mCircleView = (TimerCircleView) this.mInflatedOngoing.findViewById(C0728R.id.timer_circle_view);
            this.mCircleView.init(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis());
        }
    }

    private void inflateTimeView() {
        Log.secD("TimerFragment", "inflateTimeView()");
        if (this.mInflatedOngoing == null) {
            this.mInflatedOngoing = ((ViewStub) this.mFragmentView.findViewById(C0728R.id.timer_ongoing_stub)).inflate();
        }
        this.mTimeView = (TimerTimeView) this.mInflatedOngoing.findViewById(C0728R.id.timer_time_view);
        this.mTimeView.setTimerTimeViewListener(this.mTimerTimeViewListener);
        this.mTimeView.setTimeTextView(TimerData.getRemainMillis());
    }

    private void inflateNameView() {
        Log.secD("TimerFragment", "inflateNameView()");
        if (this.mInflatedOngoing == null) {
            this.mInflatedOngoing = ((ViewStub) this.mFragmentView.findViewById(C0728R.id.timer_ongoing_stub)).inflate();
        }
        this.mNameTextView = (TextView) this.mInflatedOngoing.findViewById(C0728R.id.timer_name_view);
        this.mNameTextView.setSingleLine(true);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.secD("TimerFragment", "onActivityCreated()");
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("timer_action_mode", false)) {
                if (this.mPresetView == null) {
                    inflatePresetLayout();
                }
                this.mPresetView.restoreActionMode(savedInstanceState);
            }
            if (savedInstanceState.getBoolean("timer_add_popup_show", false)) {
                if (this.mAddPresetPopup == null) {
                    this.mAddPresetPopup = new TimerAddPresetPopup(this.mActivity);
                    this.mAddPresetPopup.setTimerAddPresetPopupListener(this.mTimerAddPresetPopupListener);
                }
                this.mAddPresetPopup.restoreAddPresetPopup(savedInstanceState);
            }
            if (this.mPickerView != null) {
                this.mPickerView.restoreEditMode(savedInstanceState);
            }
            if (this.mSmartTip.isShowSmartTip(savedInstanceState)) {
                this.mSmartTip.restoreSmartTip(savedInstanceState, this.mActivity, 0, this.mToolbar);
            }
            setIsFirstLaunch(savedInstanceState.getBoolean("timer_is_first_launch", true));
            this.mNeedFlipAnimation = savedInstanceState.getBoolean("timer_need_flip_animation", true);
        }
        if (TimerData.isTimerStateResetedOrNone() && this.mNeedFlipAnimation) {
            startFlipAnimation(0);
            this.mNeedFlipAnimation = false;
        }
        if (isFirstLaunch()) {
            this.mFragmentView.findViewById(C0728R.id.toolbar).post(new C07881());
            setIsFirstLaunch(false);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.secD("TimerFragment", "onActivityResult() / requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == SGKeyCode.CODE_BACK_SURROUND && this.mTimerToneViewModel != null) {
            this.mTimerToneViewModel.onActivityResult(resultCode, data, this.mTimerManager);
        }
    }

    public void onStart() {
        super.onStart();
        Log.secD("TimerFragment", "onStart()");
        this.mTimerManager = TimerManager.getInstance();
        this.mTimerManager.setTimerManagerListener(this.mTimerManagerListener);
        this.mTimerManager.setContext(this.mActivity.getApplicationContext());
        this.mTimerManager.fragmentStarted();
        this.mIsMultiWindowMode = this.mActivity.isInMultiWindowMode();
        setViewState(true, true);
        updateLayout(false);
    }

    public void onResume() {
        super.onResume();
        Log.secD("TimerFragment", "onResume()");
        new Handler().postDelayed(new C07892(), 50);
        if (this.mAddPresetPopup != null) {
            this.mAddPresetPopup.showSoftInput();
        }
        setSoftInputMode(this.mIsMultiWindowMode);
    }

    public void onStop() {
        super.onStop();
        Log.secD("TimerFragment", "onStop()");
        this.mTimerManager.fragmentStopped(this.mTimerManagerListener);
    }

    public void onPause() {
        long j;
        super.onPause();
        Log.secD("TimerFragment", "onPause()");
        if (this.mPickerView != null) {
            this.mPickerView.pauseView();
            if (TimerData.isTimerStateResetedOrNone()) {
                this.mTimerManager.setInputTime(this.mPickerView.getHour(), this.mPickerView.getMinute(), this.mPickerView.getSecond());
            }
        }
        Context applicationContext = this.mActivity.getApplicationContext();
        String str = "5111";
        if (this.mPresetView == null) {
            j = 0;
        } else {
            j = (long) this.mPresetView.getPresetCount();
        }
        ClockUtils.insertSaStatusLog(applicationContext, str, j);
        setSoftInputMode(false);
        if (this.mTimerToneViewModel.getHighlightDialogBuilder() != null) {
            this.mTimerToneViewModel.getHighlightDialogBuilder().stopAnyPlayingRingtone(true);
        }
        if (this.mAddPresetPopup != null) {
            this.mAddPresetPopup.hideSoftInput();
        }
    }

    public void onTabSelected() {
        super.onTabSelected();
        if (!(this.mPresetView == null || this.mPresetView.getVisibility() != 0 || this.mPreviousPresetViewHeight == this.mFragmentView.findViewById(C0728R.id.timer_preset_stub_layout).getLayoutParams().height)) {
            new Handler().postDelayed(new C07903(), 30);
        }
        new Handler().postDelayed(new C07914(), 30);
        this.mButtonViewModel.resizeButtonText();
    }

    public void onTabUnselected() {
        super.onTabUnselected();
        if (this.mPresetView != null && this.mPresetView.getVisibility() == 0) {
            this.mPreviousPresetViewHeight = this.mFragmentView.findViewById(C0728R.id.timer_preset_stub_layout).getLayoutParams().height;
        }
        if (this.mPickerView != null) {
            this.mPickerView.pauseView();
        }
        this.mSmartTip.dismissSmartTips();
    }

    public void onDestroyView() {
        Log.secD("TimerFragment", "onDestroyView()");
        if (this.mButtonViewModel != null) {
            this.mButtonViewModel.releaseInstance();
            this.mButtonViewModel = null;
        }
        if (this.mTimeView != null) {
            this.mTimeView.destroyView();
            this.mTimeView = null;
        }
        if (this.mPickerView != null) {
            this.mPickerView.destroyView();
            this.mPickerView = null;
        }
        if (this.mTitleView != null) {
            this.mTitleView.removeAllViews();
            this.mTitleView = null;
        }
        if (this.mResetBtn != null) {
            this.mResetBtn.setOnClickListener(null);
            this.mResetBtn.setBackgroundResource(0);
            this.mResetBtn = null;
        }
        if (this.mPresetView != null) {
            this.mPresetView.destroyView();
            this.mPresetView = null;
        }
        if (this.mTimerToneViewModel != null) {
            this.mTimerToneViewModel.releaseInstance();
            this.mTimerToneViewModel = null;
        }
        if (this.mAddPresetPopup != null) {
            this.mAddPresetPopup.destroy();
            this.mAddPresetPopup = null;
        }
        this.mSmartTip.dismissSmartTips();
        this.mInflatedPreset = null;
        this.mCircleView = null;
        this.mNameTextView = null;
        this.mInflatedOngoing = null;
        this.mToolbar = null;
        super.onDestroyView();
    }

    public void onDestroy() {
        Log.secD("TimerFragment", "onDestroy()");
        if (this.mTimerManager != null) {
            this.mTimerManager.removeInstance();
            if (!(TimerManager.sCountDownTimer == null || TimerData.getTimerState() == 1)) {
                this.mTimerManager.cancelCountDownTimer();
            }
        }
        super.onDestroy();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(C0728R.menu.timer_menu, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        boolean z = true;
        super.onPrepareOptionsMenu(menu);
        if (menu == null) {
            return;
        }
        if (this.mPresetView == null || !this.mPresetView.isActionMode()) {
            MenuItem addMenu = menu.findItem(C0728R.id.menu_add_preset);
            MenuItem editMenu = menu.findItem(C0728R.id.menu_edit_preset);
            MenuItem settingMenu = menu.findItem(C0728R.id.menu_setting);
            if (addMenu != null) {
                addMenu.setShowAsAction(1);
            }
            if (editMenu != null) {
                if (this.mPresetView == null || this.mPresetView.getPresetCount() <= 0 || !TimerData.isTimerStateResetedOrNone()) {
                    z = false;
                }
                editMenu.setVisible(z);
            }
            if (settingMenu != null) {
                settingMenu.setVisible(Feature.isSupportAlarmSoundMenu());
                return;
            }
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }
        this.mNeedInvalidateOptionsMenu = true;
    }

    public void onStartActionMode(BottomNavigationView bottomNavigationView) {
        super.onStartActionMode(bottomNavigationView);
        if (this.mPresetView != null) {
            this.mPresetView.initBottomNavigationView(bottomNavigationView);
        }
    }

    public void onFinishActionMode() {
        super.onFinishActionMode();
        if (this.mPresetView != null) {
            this.mPresetView.removeBottomNavigationView();
        }
    }

    private void callTimerToneActivity() {
        if (this.mTimerToneViewModel != null) {
            startActivityForResult(this.mTimerToneViewModel.getTimerToneIntent(this.mTimerManager), SGKeyCode.CODE_BACK_SURROUND);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0728R.id.menu_timer_tone) {
            if (PermissionUtils.hasPermissionExternalStorage(this.mActivity)) {
                callTimerToneActivity();
            } else {
                PermissionUtils.requestPermissions((Fragment) this, 1);
            }
            ClockUtils.insertSaLog("130", "1326");
        } else if (id == C0728R.id.menu_add_preset) {
            if (this.mAddPresetPopup == null) {
                this.mAddPresetPopup = new TimerAddPresetPopup(this.mActivity);
                this.mAddPresetPopup.setTimerAddPresetPopupListener(this.mTimerAddPresetPopupListener);
            }
            this.mAddPresetPopup.createAddPresetPopup(false, false, -1);
            this.mSmartTip.addPresetExecuted(this.mActivity);
            ClockUtils.insertSaLog("130", "1132");
        } else if (id == C0728R.id.menu_edit_preset) {
            if (this.mPresetView == null) {
                inflatePresetLayout();
            }
            this.mPresetView.editPreset();
        } else if (id != C0728R.id.menu_setting) {
            return false;
        } else {
            ClockUtils.startAlertSettingActivity(this.mActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.secD("TimerFragment", "onRequestPermissionsResult() / mIsNeverAskAgain = " + this.mIsNeverAskAgain);
        switch (requestCode) {
            case 1:
                Log.secD("TimerFragment", "Received response for storage permissions request.");
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    callTimerToneActivity();
                    return;
                } else if (this.mIsNeverAskAgain || shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {
                    this.mIsNeverAskAgain = shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE");
                    return;
                } else {
                    PermissionUtils.showPermissionPopup(this.mActivity, getResources().getString(C0728R.string.timer_set_timer_sound), C0728R.string.permission_popup_body_open, "android.permission.READ_EXTERNAL_STORAGE");
                    return;
                }
            default:
                throw new IllegalArgumentException("Invalid permission.");
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.secD("TimerFragment", "onConfigurationChanged() / newConfig = " + newConfig);
        this.mIsMultiWindowMode = this.mActivity.isInMultiWindowMode();
        boolean isSmallestScreenSizeChanged = (this.mTimerLastConfiguration == null || (this.mTimerLastConfiguration.diff(newConfig) & 2048) == 0) ? false : true;
        this.mSmartTip.dismissSmartTips();
        updateLayout(isSmallestScreenSizeChanged);
        this.mTimerLastConfiguration = new Configuration(newConfig);
        setCircleViewVisibility();
        setTimeViewVisibility();
        setTitleViewVisibility();
        setResetButtonVisibility();
        setTimerNameVisibility();
        setPresetViewVisibility();
        if (this.mPickerView != null) {
            this.mPickerView.setPrivateImeOptions();
        }
    }

    private void updateLayout(boolean isScreenSizeChanged) {
        Log.secD("TimerFragment", "updateLayout() / isScreenSizeChanged = " + isScreenSizeChanged);
        Resources res = getResources();
        updateTitleLayout();
        if (this.mPresetView != null) {
            updatePresetLayout();
        }
        if (this.mNameTextView != null) {
            LayoutParams nameParam = (LayoutParams) this.mNameTextView.getLayoutParams();
            if (nameParam != null) {
                nameParam.width = res.getDimensionPixelSize(C0728R.dimen.timer_common_name_width);
            }
            ClockUtils.setLargeTextSize(this.mActivity.getApplicationContext(), this.mNameTextView, (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_name_textview_textsize));
        }
        if (this.mButtonViewModel != null) {
            this.mButtonViewModel.updateLayout();
        }
        updateResetLayout();
        setScrollableLayoutMinHeight();
        if (this.mCircleView != null) {
            this.mCircleView.updateLayout();
        }
        if (this.mPickerView != null) {
            this.mPickerView.updateLayout();
            updatePickerLayout();
        }
        if (this.mTimeView != null) {
            this.mTimeView.updateLayout();
        }
        if (isScreenSizeChanged && this.mAddPresetPopup != null) {
            this.mAddPresetPopup.recreateAddPresetPopup();
        }
        if (ClockTab.isTimerTab()) {
            this.mSmartTip.updateSmartTipPosition(this.mActivity, 0, this.mToolbar);
        }
    }

    private void initBroadcastReceiver() {
        this.mBroadcastReceiver = new C07925();
        this.mLocalIntentReceiver = new C07936();
    }

    private void setViewVisibility(boolean isUpdateTimeViewVisibility) {
        if (isUpdateTimeViewVisibility) {
            if (!(this.mPickerView == null || this.mPickerView.isEditMode())) {
                setCircleViewVisibility();
                setTimeViewVisibility();
            }
            setTitleViewVisibility();
            setPresetViewVisibility();
        }
        setResetButtonVisibility();
        if (!this.mIsMultiWindowMode || (this.mPickerView != null && !this.mPickerView.isEditMode())) {
            setTimerNameVisibility();
        }
    }

    private void setTimeViewVisibility() {
        Log.secD("TimerFragment", "setTimeViewVisibility() / TimerState = " + TimerData.getTimerState());
        if (TimerData.isTimerStateResetedOrNone()) {
            if (this.mPickerView != null) {
                this.mPickerView.setVisibility(0);
            }
            if (this.mTimeView != null) {
                this.mTimeView.setVisibility(8);
                return;
            }
            return;
        }
        if (this.mTimeView == null) {
            inflateTimeView();
        }
        this.mTimeView.setVisibility(0);
        if (this.mPickerView != null) {
            this.mPickerView.setVisibility(8);
        }
    }

    private boolean isDisplayTitleViewMultiWindowMode() {
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        int screenZoomIndex = StateUtils.getScreenDensityIndex(this.mActivity.getApplicationContext());
        boolean isTabletScreenDp = StateUtils.isScreenDp(this.mActivity, 600);
        if (((isTabletScreenDp && screenZoomIndex == 4) || (!isTabletScreenDp && config.orientation == 2)) && this.mPickerView != null && this.mPickerView.isEditMode()) {
            return false;
        }
        int i;
        int convertDpToPixel = ClockUtils.convertDpToPixel(this.mActivity, config.screenHeightDp);
        int dimensionPixelSize = res.getDimensionPixelSize(C0728R.dimen.timer_common_multiwindow_phase_2);
        if (this.mPresetView == null || this.mPresetView.getPresetCount() <= 0) {
            i = 0;
        } else {
            i = this.mPresetView.getPresetLayoutHeight();
        }
        dimensionPixelSize += i;
        if (this.mPickerView != null && this.mPickerView.isEditMode() && StateUtils.isSplitMode()) {
            i = res.getDimensionPixelSize(C0728R.dimen.timer_common_keypad_layout_height);
        } else {
            i = 0;
        }
        dimensionPixelSize += i;
        if (Feature.isSupportTimerResetButton()) {
            i = res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_max_height) + res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_margin_top);
        } else {
            i = 0;
        }
        if (convertDpToPixel > i + dimensionPixelSize) {
            return true;
        }
        return false;
    }

    private boolean isDisplayTitleViewNormalMode() {
        boolean z = true;
        Configuration config = getResources().getConfiguration();
        int screenZoomIndex = StateUtils.getScreenDensityIndex(this.mActivity.getApplicationContext());
        if (Feature.isTablet(this.mActivity.getApplicationContext())) {
            if (config.orientation == 1 || (this.mPickerView != null && !this.mPickerView.isEditMode())) {
                return true;
            }
            return false;
        } else if (!StateUtils.isMobileKeyboard(this.mActivity.getApplicationContext())) {
            if (this.mPickerView == null || this.mPickerView.isEditMode() || (config.orientation != 1 && screenZoomIndex >= 1 && Feature.isSupportTimerResetButton())) {
                z = false;
            }
            return z;
        } else if (this.mPresetView == null || this.mPresetView.getPresetCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDisplayTitleView() {
        if (this.mIsMultiWindowMode) {
            return isDisplayTitleViewMultiWindowMode();
        }
        return isDisplayTitleViewNormalMode();
    }

    private void setTitleViewVisibility() {
        if (this.mTitleView == null) {
            return;
        }
        if (TimerData.isTimerStateResetedOrNone() && isDisplayTitleView()) {
            this.mTitleView.setVisibility(0);
        } else {
            this.mTitleView.setVisibility(8);
        }
    }

    private void setResetButtonVisibility() {
        if (this.mResetBtn != null) {
            Button button = this.mResetBtn;
            int i = (!Feature.isSupportTimerResetButton() || !TimerData.isTimerStateResetedOrNone() || this.mPickerView == null || this.mPickerView.isEditMode()) ? 8 : 0;
            button.setVisibility(i);
        }
    }

    private boolean isDisplayCircleView() {
        return (TimerData.isTimerStateResetedOrNone() || StateUtils.isMobileKeyboard(this.mActivity.getApplicationContext()) || this.mCircleView.getCircleWidth() < getResources().getDimensionPixelSize(C0728R.dimen.timer_circle_bg_min_width)) ? false : true;
    }

    private void setCircleViewVisibility() {
        Log.secD("TimerFragment", "setCircleViewVisibility() / TimerState = " + TimerData.getTimerState());
        if (this.mFragmentView != null) {
            try {
                if (!TimerData.isTimerStateResetedOrNone()) {
                    if (this.mCircleView == null) {
                        inflateCircleView();
                    }
                    this.mCircleView.setVisibility(isDisplayCircleView() ? 0 : 4);
                    this.mCircleView.updateLayout();
                } else if (this.mCircleView != null) {
                    this.mCircleView.setVisibility(8);
                }
            } catch (IllegalStateException e) {
                Log.secE("TimerFragment", "Exception : " + e.toString());
            }
        }
    }

    private void setTimerName() {
        if (TimerData.isTimerStateResetedOrNone()) {
            this.mTimerName = null;
        } else if (!this.mIsRestarted) {
            if (TimerData.getOnGoingTimerName() != null) {
                this.mTimerName = TimerData.getOnGoingTimerName();
            } else if (this.mTimerName == null && this.mPresetView != null) {
                TimerPresetItem item = this.mPresetView.getPresetItemById(this.mPresetView.getSelectedPresetId());
                if (item != null) {
                    this.mTimerName = item.getName();
                }
            }
            if (this.mTimerName == null) {
                this.mTimerName = "";
            }
        }
        Log.secD("TimerFragment", "setTimerName() / mTimerName = " + this.mTimerName);
    }

    private void setTimerNameVisibility() {
        if (this.mFragmentView != null) {
            setTimerName();
            if (!TimerData.isTimerStateResetedOrNone() && this.mTimerName != null) {
                if (this.mNameTextView == null) {
                    inflateNameView();
                }
                this.mNameTextView.setText(this.mTimerName);
                this.mNameTextView.setVisibility(0);
            } else if (this.mNameTextView != null) {
                this.mNameTextView.setVisibility(8);
            }
        }
    }

    private boolean isDisplaySplitView() {
        return (getResources().getConfiguration().orientation != 2 || StateUtils.isScreenDp(this.mActivity, 512) || this.mIsMultiWindowMode) ? false : true;
    }

    private void setPresetViewVisibility() {
        if (!TimerData.isTimerStateResetedOrNone() || this.mPickerView == null || this.mPickerView.isEditMode() || this.mPresetView == null || this.mPresetView.getPresetCount() <= 0) {
            setPresetViewVisibility(8);
        } else {
            setPresetViewVisibility(0);
        }
    }

    private void setPresetViewVisibility(int visibility) {
        Log.secD("TimerFragment", "setPresetViewVisibility() / visibility = " + visibility);
        if (this.mFragmentView != null) {
            if (this.mPresetView == null) {
                if (visibility == 0) {
                    inflatePresetLayout();
                } else {
                    return;
                }
            }
            this.mPresetView.setVisibility(visibility);
            if (this.mPickerView != null && isDisplaySplitView()) {
                this.mPickerView.updateLayout();
            }
            this.mFragmentView.findViewById(C0728R.id.timer_preset_stub_layout).setVisibility(visibility);
            if (this.mButtonViewModel != null) {
                RelativeLayout.LayoutParams buttonParam = this.mButtonViewModel.getButtonLayoutParam();
                ConstraintLayout layout = (ConstraintLayout) this.mFragmentView.findViewById(C0728R.id.timer_scrollable_layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(layout);
                setScrollableLayoutMinHeight();
                if (buttonParam != null) {
                    if (visibility == 0) {
                        this.mPresetView.setListViewPadding(getResources().getConfiguration().orientation, this.mIsMultiWindowMode);
                        if (isDisplaySplitView()) {
                            constraintSet.connect(C0728R.id.timer_main_layout, 7, C0728R.id.timer_preset_stub_layout, 6);
                            constraintSet.connect(C0728R.id.timer_main_layout, 4, 0, 4);
                            constraintSet.connect(C0728R.id.timer_preset_stub_layout, 6, C0728R.id.timer_main_layout, 7);
                            constraintSet.connect(C0728R.id.timer_preset_stub_layout, 3, 0, 3);
                            constraintSet.connect(C0728R.id.timer_ongoing_layout, 7, C0728R.id.timer_preset_stub_layout, 6);
                            buttonParam.removeRule(14);
                            buttonParam.addRule(20);
                        } else {
                            constraintSet.connect(C0728R.id.timer_main_layout, 7, 0, 7);
                            constraintSet.connect(C0728R.id.timer_main_layout, 4, C0728R.id.timer_preset_stub_layout, 3);
                            constraintSet.connect(C0728R.id.timer_preset_stub_layout, 6, 0, 6);
                            constraintSet.connect(C0728R.id.timer_preset_stub_layout, 3, C0728R.id.timer_main_layout, 4);
                            constraintSet.connect(C0728R.id.timer_ongoing_layout, 7, 0, 7);
                            buttonParam.removeRule(20);
                            buttonParam.addRule(14);
                        }
                    } else if (visibility == 8) {
                        constraintSet.connect(C0728R.id.timer_main_layout, 7, 0, 7);
                        constraintSet.connect(C0728R.id.timer_main_layout, 4, 0, 4);
                        constraintSet.connect(C0728R.id.timer_ongoing_layout, 7, 0, 7);
                        buttonParam.removeRule(20);
                        buttonParam.addRule(14);
                    }
                    constraintSet.applyTo(layout);
                    updatePickerLayout();
                    updateResetLayout();
                }
            }
            setTitleViewVisibility();
        }
    }

    private void setScrollableLayoutMinHeight() {
        Resources res = getResources();
        ConstraintLayout layout = (ConstraintLayout) this.mFragmentView.findViewById(C0728R.id.timer_scrollable_layout);
        int minHeight = res.getDimensionPixelOffset(C0728R.dimen.timer_common_timepicker_layout_min_height_for_multiwindow);
        if (!(this.mPresetView == null || this.mPresetView.getVisibility() != 0 || isDisplaySplitView())) {
            minHeight += res.getDimensionPixelOffset(C0728R.dimen.timer_common_preset_list_min_height);
        }
        if (Feature.isSupportTimerResetButton() && this.mIsMultiWindowMode) {
            minHeight += res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_max_height) + res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_margin_top);
        }
        layout.setMinHeight(minHeight);
    }

    private void setViewEnabled(boolean enabled) {
        float f = 1.0f;
        if (this.mPickerView != null) {
            this.mPickerView.setEnabledNumberPicker(enabled);
            this.mPickerView.setFocusable(enabled);
            this.mPickerView.setAlpha(enabled ? 1.0f : 0.4f);
        }
        if (this.mTitleView != null) {
            RelativeLayout relativeLayout = this.mTitleView;
            if (!enabled) {
                f = 0.4f;
            }
            relativeLayout.setAlpha(f);
            this.mTitleView.setFocusable(StateUtils.isTalkBackEnabled(this.mActivity.getApplicationContext()));
        }
        checkInputData();
        if (this.mNeedInvalidateOptionsMenu) {
            this.mActivity.invalidateOptionsMenu();
            this.mNeedInvalidateOptionsMenu = false;
        }
    }

    private void initViews() {
        Log.secD("TimerFragment", "initViews()");
        this.mPickerView = (TimerPickerViewModel) this.mFragmentView.findViewById(C0728R.id.timer_picker_view);
        this.mTitleView = (RelativeLayout) this.mFragmentView.findViewById(C0728R.id.timer_title_layout);
        this.mResetBtn = (Button) this.mFragmentView.findViewById(C0728R.id.timer_reset_button);
        this.mTimerAnimationViewModel = new TimerAnimationViewModel(this.mActivity);
        this.mTimerAnimationViewModel.setTimerAnimationViewModelListener(this.mTimerAnimationViewModelListener);
        this.mTimerToneViewModel = new TimerToneViewModel(this.mActivity);
        if (this.mPickerView != null) {
            this.mPickerView.initViews(this.mFragmentView);
            this.mPickerView.setTimerPickerViewListener(this.mTimerPickerViewListener);
        }
        if (TimerPresetItem.getPresetCount(this.mActivity.getApplicationContext()) > 0 && this.mPresetView == null) {
            inflatePresetLayout();
        }
        if (this.mResetBtn != null) {
            try {
                this.mResetBtn.semSetButtonShapeEnabled(true);
            } catch (NoSuchMethodError e) {
                Log.secE("TimerFragment", "NoSuchMethodError : " + e);
            }
            this.mResetBtn.setOnClickListener(new C07947());
            this.mResetBtn.setOnFocusChangeListener(new C07958());
        }
    }

    private void updateTitleLayout() {
        if (this.mTitleView != null) {
            LayoutParams titleParams = (LayoutParams) this.mTitleView.getLayoutParams();
            if (titleParams != null) {
                TypedValue value = new TypedValue();
                getResources().getValue(C0728R.dimen.timer_common_title_vertical_bias, value, true);
                titleParams.verticalBias = value.getFloat();
            }
        }
    }

    private void updatePickerLayout() {
        LinearLayout pickerLayout = (LinearLayout) this.mFragmentView.findViewById(C0728R.id.timer_picker_layout);
        if (pickerLayout != null) {
            LayoutParams pickerParam = (LayoutParams) pickerLayout.getLayoutParams();
            if (this.mPresetView == null || this.mPresetView.getVisibility() != 0 || isDisplaySplitView()) {
                TypedValue value = new TypedValue();
                getResources().getValue(C0728R.dimen.timer_common_picker_vertical_bias, value, true);
                pickerParam.verticalBias = value.getFloat();
            } else {
                pickerParam.verticalBias = 1.0f;
            }
            pickerLayout.setLayoutParams(pickerParam);
        }
    }

    private void updatePresetLayout() {
        Resources res = getResources();
        if (this.mPresetView != null) {
            LayoutParams presetParam = (LayoutParams) this.mFragmentView.findViewById(C0728R.id.timer_preset_stub_layout).getLayoutParams();
            if (presetParam == null) {
                return;
            }
            if (isDisplaySplitView()) {
                presetParam.height = -1;
                presetParam.width = (int) (((float) res.getDisplayMetrics().widthPixels) * 0.493f);
                return;
            }
            presetParam.height = this.mPresetView.getPresetLayoutHeight();
            presetParam.width = -1;
        }
    }

    private void updateResetLayout() {
        if (this.mResetBtn != null) {
            Resources res = getResources();
            LinearLayout.LayoutParams resetBtnParam = (LinearLayout.LayoutParams) this.mResetBtn.getLayoutParams();
            if (resetBtnParam != null) {
                resetBtnParam.topMargin = res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_margin_top);
                int layoutWidth = (this.mPresetView == null || this.mPresetView.getPresetCount() == 0 || !isDisplaySplitView()) ? res.getDisplayMetrics().widthPixels : (int) (((float) res.getDisplayMetrics().widthPixels) * 0.507f);
                resetBtnParam.setMarginEnd((layoutWidth - (this.mPickerView.getPickerWidth(true) * 13)) / 2);
                this.mResetBtn.setLayoutParams(resetBtnParam);
            }
            ClockUtils.setLargeTextSize(this.mActivity.getApplicationContext(), this.mResetBtn, (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_textsize));
        }
    }

    private void setViewState(boolean isResume, boolean isUpdateTimeViewVisibility) {
        int state = TimerData.getTimerState();
        long inputMillis = TimerData.getInputMillis();
        Log.secD("TimerFragment", "setViewState() / TimerData.getTimerState() = " + state + ", isResume = " + isResume + ", isFirst = " + this.mIsFirst);
        if (isAdded() && this.mActivity != null) {
            setButtonViewState(isResume, state);
            setCircleViewState(state);
            if (state == 3) {
                if (inputMillis >= 1000) {
                    TimerData.setRemainMillis(inputMillis);
                    TimerData.setOngoingInputMillis(inputMillis);
                }
                if (this.mIsFirst) {
                    this.mIsFirst = false;
                }
            }
            if (state == 0 && this.mIsFirst) {
                this.mTimerManager.setInputTime(inputMillis);
                this.mIsFirst = false;
            }
            if (this.mTimeView != null) {
                this.mTimeView.initTimeTextView(TimerData.getRemainMillis());
            }
            if (this.mPickerView != null) {
                this.mPickerView.initTimePickerView();
            }
            setViewVisibility(isUpdateTimeViewVisibility);
            checkInputData();
        }
    }

    private void setButtonViewState(boolean isResume, int state) {
        boolean z = true;
        Log.secD("TimerFragment", "setButtonViewState() / isResume = " + isResume + ", state = " + state);
        if (state == 1 || state == 3 || state == 0) {
            if (this.mButtonViewModel != null) {
                TimerBtnViewModel timerBtnViewModel = this.mButtonViewModel;
                if (state != 1) {
                    z = false;
                }
                timerBtnViewModel.setStartedViewState(z, isResume, this.mTimeView, this.mPickerView, this.mPresetView);
            }
        } else if (state == 2 && this.mButtonViewModel != null) {
            this.mButtonViewModel.setStoppedViewState(isResume);
        }
    }

    private void setCircleViewState(int state) {
        Log.secD("TimerFragment", "setCircleViewState() / state = " + state);
        if (state == 1) {
            if (this.mCircleView == null) {
                inflateCircleView();
            }
            this.mCircleView.updateTime(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis());
            this.mCircleView.start();
        } else if (state == 2) {
            if (this.mCircleView != null) {
                this.mCircleView.stop();
            }
        } else if (state == 3 && this.mCircleView != null) {
            this.mCircleView.reset();
        }
    }

    private void updateTime() {
        long remainMillis = TimerData.getRemainMillis();
        int hour = (int) (remainMillis / 3600000);
        int minute = (int) ((remainMillis % 3600000) / 60000);
        int second = (int) ((remainMillis % 60000) / 1000);
        int milliSecond = (int) (remainMillis % 1000);
        if (this.mButtonViewModel != null) {
            TimerBtnViewModel timerBtnViewModel = this.mButtonViewModel;
            boolean z = (hour == 0 && minute == 0 && second == 0 && milliSecond > 0 && milliSecond < Callback.DEFAULT_DRAG_ANIMATION_DURATION) ? false : true;
            timerBtnViewModel.setPauseBtnClickable(z);
        }
        if (milliSecond <= Callback.DEFAULT_DRAG_ANIMATION_DURATION && this.mTimeView != null) {
            this.mTimeView.changeTime(hour, minute, second);
        }
        if (this.mCircleView != null) {
            this.mCircleView.updateTime(TimerData.getOngoingInputMillis(), remainMillis);
        }
        if (this.mCircleView == null) {
            return;
        }
        if (milliSecond <= Callback.DEFAULT_DRAG_ANIMATION_DURATION && hour == 0 && minute == 0 && second == 5) {
            this.mCircleView.startColorChangedAnimator();
        } else if (hour == 0 && minute == 0 && second <= 4) {
            this.mCircleView.resetColorChangedAnimator();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Log.secD("TimerFragment", "onSaveInstanceState() / TimerData.getTimerState() = " + TimerData.getTimerState() + ", TimerData.getInputMillis() = " + TimerData.getInputMillis() + ", mTimerManager.mIsRebootSequence = " + TimerManager.sIsRebootSequence);
        if (TimerData.getTimerState() == 1 && !TimerManager.sIsRebootSequence) {
            this.mTimerManager.setContext(this.mActivity.getApplicationContext());
            this.mTimerManager.saveSharedPreference();
        }
        if (this.mPresetView != null) {
            this.mPresetView.saveActionMode(outState);
        }
        if (this.mAddPresetPopup != null) {
            this.mAddPresetPopup.saveAddPresetPopup(outState);
        }
        if (this.mPickerView != null) {
            this.mPickerView.saveEditMode(outState);
        }
        this.mSmartTip.saveSmartTip(outState);
        outState.putBoolean("timer_is_first_launch", isFirstLaunch());
        outState.putBoolean("timer_need_flip_animation", this.mNeedFlipAnimation);
        super.onSaveInstanceState(outState);
    }

    private void disablePickerEditMode(boolean isDelay) {
        if (this.mPickerView != null && this.mPickerView.isEditMode()) {
            this.mPickerView.updateInputState();
            this.mPickerView.setEditMode(false, isDelay);
        }
    }

    private void start() {
        Log.secD("TimerFragment", "start()");
        if (TimerData.getTimerState() != 1 && TimerData.getInputMillis() != 0) {
            if (this.mPresetView != null) {
                long id = this.mActivity.getSharedPreferences("TIMER", 0).getLong("selectedPresetId", -1);
                if (this.mPresetView.getSelectedPresetId() != id) {
                    this.mPresetView.setSelectedPresetId(id);
                }
                this.mPresetView.finishActionMode();
            }
            if (this.mAddPresetPopup != null) {
                this.mAddPresetPopup.dismissAddPresetPopup();
            }
            this.mActivity.closeOptionsMenu();
            if (this.mPickerView.isEditMode()) {
                this.mPickerView.updateInputState();
            }
            if (this.mIsRestarted) {
                this.mTimerManager.setInputTime(TimerData.getRestartMillis());
            } else {
                this.mTimerManager.setInputTime(this.mPickerView.getHour(), this.mPickerView.getMinute(), this.mPickerView.getSecond());
            }
            this.mTimerManager.setTimerState(1);
            setViewState(false, this.mPickerView.isEditMode());
            setTimerName();
            this.mTimerManager.startTimer(TimerData.getInputMillis(), TimerData.getInputMillis(), this.mTimerName);
            acquireDim();
            startAnimation(true);
            int delayMillisForCircleView = 10;
            if (this.mPickerView.isEditMode()) {
                disablePickerEditMode(true);
                delayMillisForCircleView = this.mIsMultiWindowMode ? Callback.DEFAULT_DRAG_ANIMATION_DURATION : 100;
                this.mPickerView.setVisibility(8);
            }
            new Handler().postDelayed(new C07969(), (long) delayMillisForCircleView);
            if (StateUtils.isDndModeAlarmMuted(this.mActivity.getApplicationContext())) {
                Toast.makeText(this.mActivity, getResources().getString(C0728R.string.timer_zen_mode), 1).show();
            }
            this.mSmartTip.addTimerStartCount(this.mActivity);
            this.mTimerManager.sendTimerState();
        }
    }

    private void setStartMode(int mode, String timerName) {
        if (mode == 1) {
            this.mIsRestarted = true;
            this.mTimerName = timerName;
            return;
        }
        this.mIsRestarted = false;
        this.mTimerName = null;
    }

    private void pause() {
        Log.secD("TimerFragment", "pause()");
        releaseDim();
        this.mTimerManager.stopTimer();
        this.mTimerManager.setTimerState(2);
        setViewState(false, true);
        this.mTimerManager.sendTimerState();
    }

    private void resume() {
        Log.secD("TimerFragment", "resume()");
        this.mTimerManager.setTimerState(1);
        acquireDim();
        setViewState(false, true);
        this.mTimerManager.startTimer(TimerData.getOngoingInputMillis(), TimerData.getRemainMillis(), TimerData.getOnGoingTimerName());
        this.mTimerManager.sendTimerState();
    }

    private void cancel() {
        Log.secD("TimerFragment", "cancel()");
        this.mTimerManager.setTimerState(3);
        updateLayout(false);
        releaseDim();
        this.mTimerManager.cancelTimer();
        if (this.mButtonViewModel != null) {
            this.mButtonViewModel.setBtnCancelState();
        }
        startAnimation(false);
        this.mIsRestarted = false;
        this.mTimerManager.sendTimerState();
    }

    private void checkInputData() {
        boolean z = true;
        if (this.mPickerView != null) {
            boolean isButtonEnable;
            boolean isValidTime = TimerData.convertMillis(this.mPickerView.getHour(), this.mPickerView.getMinute(), this.mPickerView.getSecond()) != 0;
            boolean isFlipAnimationWorking = this.mPickerView.isFlipAnimationWorking();
            boolean isActionMode;
            if (this.mPresetView == null || !this.mPresetView.isActionMode()) {
                isActionMode = false;
            } else {
                isActionMode = true;
            }
            if (isFlipAnimationWorking || isActionMode || !isValidTime) {
                isButtonEnable = false;
            } else {
                isButtonEnable = true;
            }
            if (this.mButtonViewModel != null) {
                this.mButtonViewModel.setStartBtnEnable(isButtonEnable);
            }
            if (this.mResetBtn != null) {
                this.mResetBtn.setEnabled(isButtonEnable);
                this.mResetBtn.setAlpha(isButtonEnable ? 1.0f : 0.4f);
                this.mResetBtn.setFocusable(isButtonEnable);
            }
            if (this.mPresetView != null) {
                TimerPresetViewModel timerPresetViewModel = this.mPresetView;
                if (isFlipAnimationWorking) {
                    z = false;
                }
                timerPresetViewModel.setEnabledListView(z);
            }
        }
    }

    public boolean onClockDispatchKeyEvent(KeyEvent event, View tab) {
        boolean z = true;
        Log.secD("TimerFragment", "onClockDispatchKeyEvent() / event = " + event);
        if (this.mActivity == null) {
            return false;
        }
        View cView = this.mActivity.getCurrentFocus();
        if (event.getAction() == 1) {
            switch (event.getKeyCode()) {
                case 4:
                case 111:
                    disablePickerEditMode(false);
                    return false;
                case 66:
                    if (this.mPresetView == null || !this.mPresetView.isActionMode()) {
                        return false;
                    }
                    this.mPresetView.enableUpdateActionModeMenu();
                    return false;
                case PointerIconCompat.TYPE_CELL /*1006*/:
                    InputMethodManager inputMethodManager = (InputMethodManager) this.mActivity.getSystemService("input_method");
                    if (inputMethodManager == null || this.mPickerView == null || !this.mPickerView.isEditMode()) {
                        return false;
                    }
                    keypadAnimation(inputMethodManager.semIsInputMethodShown());
                    return false;
                default:
                    return false;
            }
        } else if (event.getAction() != 0) {
            return false;
        } else {
            switch (event.getKeyCode()) {
                case 20:
                    if (cView == null || this.mPickerView == null || !this.mPickerView.isSpinnerFocused(cView) || this.mButtonViewModel == null || this.mButtonViewModel.getStartBtn().isEnabled()) {
                        z = false;
                    }
                    return z;
                case 21:
                    if (cView == null || !"hourSpinnerInput".equals(cView.getTag())) {
                        return false;
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            TimerFragment.this.disablePickerEditMode(false);
                        }
                    }, 100);
                    return false;
                case 22:
                    if (cView == null || !"secondSpinnerInput".equals(cView.getTag())) {
                        return false;
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            TimerFragment.this.disablePickerEditMode(false);
                        }
                    }, 100);
                    return false;
                default:
                    return false;
            }
        }
    }

    public void onMultiWindowModeChanged(boolean isMultiWindowMode) {
        Log.secD("TimerFragment", "onMultiWindowModeChanged() / isMultiWindowMode = " + isMultiWindowMode);
        this.mIsMultiWindowMode = isMultiWindowMode;
        setSoftInputMode(isMultiWindowMode);
    }

    private void setSoftInputMode(boolean isInMultiWindowMode) {
        if (this.mActivity != null && this.mActivity.getWindow() != null) {
            if ((isInMultiWindowMode || StateUtils.isContextInDexMode(this.mActivity)) && (this.mAddPresetPopup == null || !this.mAddPresetPopup.isAddPresetDialogShowing())) {
                this.mActivity.getWindow().setSoftInputMode(35);
            } else {
                this.mActivity.getWindow().setSoftInputMode(51);
            }
        }
    }

    private void startFlipAnimation(int duration) {
        Log.secD("TimerFragment", "startFlipAnimation()");
        if (this.mPickerView != null) {
            this.mPickerView.startFlipAnimation(duration);
        }
        checkInputData();
    }

    private void keypadAnimation(boolean isEditMode) {
        if (this.mTimerAnimationViewModel != null) {
            this.mTimerAnimationViewModel.keypadAnimation(isEditMode, this.mButtonViewModel.getButtonLayout(), this.mButtonViewModel.getButtonLayoutParam(), this.mResetBtn, this.mIsMultiWindowMode);
        }
    }

    private void startAnimation(boolean isStart) {
        if (this.mTimeView == null) {
            inflateTimeView();
        }
        if (this.mNameTextView == null) {
            inflateNameView();
        }
        if (this.mTimerAnimationViewModel != null) {
            this.mTimerAnimationViewModel.startAnimation(isStart, this.mPickerView, this.mTimeView, this.mCircleView, this.mTitleView, this.mPresetView, (RelativeLayout) this.mFragmentView.findViewById(C0728R.id.timer_preset_stub_layout), (RelativeLayout) this.mFragmentView.findViewById(C0728R.id.timer_button_layout), this.mButtonViewModel, this.mResetBtn, this.mNameTextView, this.mIsMultiWindowMode, isDisplaySplitView());
        }
    }

    protected boolean isStartedState() {
        return TimerData.getTimerState() == 1;
    }

    protected void insertSaLogCurrentTab() {
        ClockUtils.insertSaLog("130");
    }
}
