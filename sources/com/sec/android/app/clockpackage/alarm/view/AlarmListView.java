package com.sec.android.app.clockpackage.alarm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorFinishedListener;
import android.support.v7.widget.RecyclerView.SeslLongPressMultiSelectionListener;
import android.support.v7.widget.RecyclerView.SeslOnMultiSelectedListener;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.Alarm;
import com.sec.android.app.clockpackage.alarm.model.AlarmDataHandler;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.model.AlarmProvider;
import com.sec.android.app.clockpackage.alarm.model.HolidayUtil;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCursorAdapter;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCursorAdapter.OnAlarmListViewListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmItemViewHolder;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListClickEvent;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListClickEvent.OnAlarmItemClickListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListClickEvent.OnAlarmItemLongClickListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmUtil;
import com.sec.android.app.clockpackage.common.callback.ActionModeListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockAddButton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class AlarmListView extends CoordinatorLayout {
    private AlarmActionModeCallback mActionModeCallBack;
    private Activity mActivity;
    private ClockAddButton mAddAlarmButton;
    private int mAlarmLaunchMode = 2;
    private Toolbar mAlarmToolbar;
    private final Context mContext;
    private AlarmCursorAdapter mCursorAdapter;
    private View mCustomView;
    private BottomNavigationView mDeleteBottom;
    private int mFirstItemClickedPosition;
    public boolean mIsAlarmEditStarted = false;
    public boolean mIsDeletingFlag = false;
    private boolean mIsMultiSelectStarted;
    public boolean mIsNeedRestoreActionMode;
    private RecyclerView mList;
    private String mSetAsUri;
    private TextView mSubtitle;
    private TextView mTitle;
    private int mToolbarContentInsetStart;
    private int mWidgetID = -1;

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$1 */
    class C05561 implements OnAlarmListViewListener {
        C05561() {
        }

        public void onDeleteMenuItemClick() {
            AlarmListView.this.startDeleteAnimation();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$2 */
    class C05572 implements Runnable {
        C05572() {
        }

        public void run() {
            if (AlarmListView.this.mList != null && AlarmListView.this.mActivity != null) {
                try {
                    AlarmListView.this.semSetRoundedCorners(12);
                    AlarmListView.this.semSetRoundedCornerColor(12, AlarmListView.this.mActivity.getColor(C0490R.color.window_background_color));
                    AlarmListView.this.mList.semSetRoundedCorners(3);
                    AlarmListView.this.mList.semSetRoundedCornerColor(3, AlarmListView.this.mActivity.getColor(C0490R.color.window_background_color));
                } catch (NoSuchMethodError e) {
                    Log.secE("AlarmListView", "No semSetRoundedCorners, it's supported by P os");
                }
                AlarmListView.this.mActivity.registerForContextMenu(AlarmListView.this.mList);
                AlarmListView.this.mList.setEnabled(false);
                AlarmListView.this.mList.setHasFixedSize(true);
                AlarmListView.this.mList.setItemAnimator(new DefaultItemAnimator());
                AlarmListView.this.setAlarmListSeslCustomApi();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$3 */
    class C05583 implements OnGlobalLayoutListener {
        C05583() {
        }

        public void onGlobalLayout() {
            if (AlarmListView.this.mCursorAdapter != null) {
                AlarmListView.this.mCursorAdapter.mToggleLock = false;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$4 */
    class C05594 implements OnAlarmItemClickListener {
        C05594() {
        }

        public void onItemClicked(RecyclerView recyclerView, int position, View view) {
            if (AlarmListView.this.mCursorAdapter.isAlarmListDeleteMode()) {
                AlarmListView.this.mList.setSoundEffectsEnabled(false);
                AlarmListView.this.toggleCheckBox(position, false);
                AlarmListView.this.mCursorAdapter.setAlarmDeleteModeCheckedState((AlarmItemViewHolder) recyclerView.getChildViewHolder(view), position);
                AlarmListView.this.mList.setSoundEffectsEnabled(true);
                AlarmListView.this.showHideBottomNavigationView();
                return;
            }
            AlarmListView.this.alarmListItemSelected(position);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$5 */
    class C05605 implements OnAlarmItemLongClickListener {
        C05605() {
        }

        public void onItemLongClicked(RecyclerView recyclerView, int position, View view) {
            if (AlarmListView.this.mActionModeCallBack != null) {
                if (!AlarmListView.this.mCursorAdapter.isAlarmListDeleteMode()) {
                    ClockUtils.insertSaLog("101", "1010");
                    if (AlarmListView.this.mActivity != null) {
                        ((AppCompatActivity) AlarmListView.this.mActivity).startSupportActionMode(AlarmListView.this.mActionModeCallBack);
                    }
                }
                AlarmListView.this.toggleCheckBox(position, true);
                AlarmListView.this.mCursorAdapter.setAlarmDeleteModeCheckedState((AlarmItemViewHolder) recyclerView.getChildViewHolder(view), position);
                AlarmListView.this.mFirstItemClickedPosition = position;
                AlarmListView.this.mList.seslStartLongPressMultiSelection();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$6 */
    class C05616 implements SeslLongPressMultiSelectionListener {
        private HashSet<Integer> selectedItems;

        C05616() {
        }

        public void onItemSelected(RecyclerView recyclerView, View view, int position, long id) {
            if (this.selectedItems != null) {
                if (this.selectedItems.contains(Integer.valueOf(position))) {
                    AlarmListView.this.toggleCheckBox(position, false);
                    this.selectedItems.remove(Integer.valueOf(position));
                } else {
                    AlarmListView.this.toggleCheckBox(position, true);
                    this.selectedItems.add(Integer.valueOf(position));
                }
                AlarmListView.this.mCursorAdapter.notifyDataSetChanged();
            }
        }

        public void onLongPressMultiSelectionStarted(int startX, int startY) {
            AlarmListView.this.mIsMultiSelectStarted = true;
            if (this.selectedItems == null) {
                this.selectedItems = new HashSet();
            } else {
                this.selectedItems.clear();
            }
        }

        public void onLongPressMultiSelectionEnded(int endX, int endY) {
            AlarmListView.this.mIsMultiSelectStarted = false;
            AlarmListView.this.showHideBottomNavigationView();
            if (this.selectedItems != null) {
                this.selectedItems.clear();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$7 */
    class C05627 implements SeslOnMultiSelectedListener {
        private int mSelectionStartPosition = 0;

        C05627() {
        }

        public void onMultiSelected(RecyclerView mRecyclerView, View mView, int mI, long mL) {
        }

        public void onMultiSelectStart(int startX, int startY) {
            this.mSelectionStartPosition = AlarmListView.this.mList.getChildLayoutPosition(AlarmListView.this.mList.findChildViewUnder((float) startX, (float) startY));
            if (this.mSelectionStartPosition == -1) {
                this.mSelectionStartPosition = AlarmListView.this.mList.getChildLayoutPosition(AlarmListView.this.mList.seslFindNearChildViewUnder((float) startX, (float) startY));
            }
        }

        public void onMultiSelectStop(int endX, int endY) {
            if (!(AlarmListView.this.mCursorAdapter.isAlarmListDeleteMode() || AlarmListView.this.mCursorAdapter.getItemCount() <= 0 || AlarmListView.this.mActivity == null)) {
                ((AppCompatActivity) AlarmListView.this.mActivity).startSupportActionMode(AlarmListView.this.mActionModeCallBack);
            }
            int selectionEndPosition = AlarmListView.this.mList.getChildLayoutPosition(AlarmListView.this.mList.findChildViewUnder((float) endX, (float) endY));
            if (selectionEndPosition == -1) {
                selectionEndPosition = AlarmListView.this.mList.getChildLayoutPosition(AlarmListView.this.mList.seslFindNearChildViewUnder((float) endX, (float) endY));
            }
            if (this.mSelectionStartPosition > selectionEndPosition) {
                int temp = this.mSelectionStartPosition;
                this.mSelectionStartPosition = selectionEndPosition;
                selectionEndPosition = temp;
            }
            int position = this.mSelectionStartPosition;
            while (position <= selectionEndPosition) {
                if (position > -1 && position < AlarmListView.this.mCursorAdapter.getItemCount()) {
                    AlarmListView.this.toggleCheckBox(position, true);
                    AlarmListView.this.mCursorAdapter.setAlarmDeleteModeCheckedState((AlarmItemViewHolder) AlarmListView.this.mList.findViewHolderForAdapterPosition(position), position);
                }
                position++;
            }
            AlarmListView.this.updateSelectionTitle();
            AlarmListView.this.showHideBottomNavigationView();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$8 */
    class C05638 implements OnNavigationItemSelectedListener {
        C05638() {
        }

        public boolean onNavigationItemSelected(MenuItem item) {
            if (item.getItemId() == C0490R.id.delete) {
                AlarmListView.this.startDeleteAnimation();
                ClockUtils.insertSaLog("102", "1212");
            }
            return true;
        }
    }

    class AlarmActionModeCallback implements Callback {
        private ActionMode mListActionMode;
        private CheckBox mSelectAll;
        private ViewGroup mSelectAllLayout;
        private TextView mSelectionTitle;
        private int mToolbarChildCount;
        private final View[] mToolbarChildView = new View[2];

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$AlarmActionModeCallback$1 */
        class C05661 implements Runnable {
            C05661() {
            }

            public void run() {
                AlarmActionModeCallback.this.mToolbarChildView[1].setVisibility(8);
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$AlarmActionModeCallback$2 */
        class C05672 implements Runnable {
            C05672() {
            }

            public void run() {
                AlarmActionModeCallback.this.mSelectAll.setScaleX(0.5f);
                AlarmActionModeCallback.this.mSelectAll.setScaleY(0.5f);
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$AlarmActionModeCallback$3 */
        class C05683 implements Runnable {
            C05683() {
            }

            public void run() {
                if (AlarmActionModeCallback.this.mToolbarChildCount == 2) {
                    AlarmListView.this.mAlarmToolbar.setContentInsetsRelative(AlarmListView.this.mToolbarContentInsetStart, 0);
                    AlarmActionModeCallback.this.mToolbarChildView[0].setVisibility(0);
                }
                AlarmListView.this.mAlarmToolbar.removeView(AlarmListView.this.mCustomView);
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$AlarmActionModeCallback$4 */
        class C05694 implements OnClickListener {
            C05694() {
            }

            public void onClick(View arg0) {
                boolean isToggleCheck = false;
                if (AlarmActionModeCallback.this.mSelectAll.isChecked()) {
                    AlarmActionModeCallback.this.mSelectAll.setChecked(false);
                } else {
                    AlarmActionModeCallback.this.mSelectAll.setChecked(true);
                    isToggleCheck = true;
                }
                if (!AlarmListView.this.mIsDeletingFlag) {
                    AlarmListView.this.toggleAllCheckBox(isToggleCheck);
                    AlarmListView.this.updateSelectionTitle();
                }
                ClockUtils.insertSaLog("102", "1211");
            }
        }

        AlarmActionModeCallback() {
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Log.secD("AlarmListView", "onCreateActionMode()");
            AlarmListView.this.mToolbarContentInsetStart = AlarmListView.this.mAlarmToolbar.getContentInsetStart();
            this.mToolbarChildCount = AlarmListView.this.mAlarmToolbar.getChildCount();
            if (this.mToolbarChildCount == 2) {
                AlarmListView.this.mAlarmToolbar.setContentInsetsRelative(0, 0);
                this.mToolbarChildView[0] = AlarmListView.this.mAlarmToolbar.getChildAt(0);
                this.mToolbarChildView[1] = AlarmListView.this.mAlarmToolbar.getChildAt(1);
                this.mToolbarChildView[0].setVisibility(8);
                this.mToolbarChildView[1].animate().alphaBy(1.0f).alpha(0.0f).setDuration(150).setInterpolator(new SineInOut90()).withEndAction(new C05661()).start();
            } else {
                Log.m42e("AlarmListView", "onCreateActionMode() toolbarChildCount = " + this.mToolbarChildCount);
                AlarmListView.this.setToolbar(8, 0);
            }
            int layoutId = C0490R.layout.clock_multi_select_mode_actionbar;
            this.mListActionMode = actionMode;
            AlarmListView.this.mCustomView = LayoutInflater.from(AlarmListView.this.mActivity).inflate(layoutId, null);
            this.mSelectAllLayout = (ViewGroup) AlarmListView.this.mCustomView.findViewById(C0490R.id.select_all_layout);
            this.mSelectionTitle = (TextView) AlarmListView.this.mCustomView.findViewById(C0490R.id.selection_title);
            this.mSelectAll = (CheckBox) AlarmListView.this.mCustomView.findViewById(C0490R.id.select_all_cb);
            ClockUtils.setLargeTextSize(AlarmListView.this.mContext, this.mSelectionTitle, (float) AlarmListView.this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.clock_list_select_item_text_size));
            setEventListener();
            Animation scaleAnim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnim.setInterpolator(new SineInOut90());
            scaleAnim.setDuration(400);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setInterpolator(new SineInOut90());
            anim.setDuration(400);
            this.mSelectionTitle.setVisibility(0);
            AlarmListView.this.mAlarmToolbar.addView(AlarmListView.this.mCustomView);
            this.mSelectAll.startAnimation(scaleAnim);
            this.mSelectAllLayout.startAnimation(anim);
            if (!AlarmListView.this.mCursorAdapter.isAlarmListDeleteMode()) {
                AlarmListView.this.mCursorAdapter.setAlarmListMode(1);
                AlarmListView.this.showCheckBox(true);
            }
            AlarmListView.this.mIsDeletingFlag = false;
            AlarmListView.this.updateClosestAlertText();
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            Log.secD("AlarmListView", "onPrepareActionMode()");
            AlarmListView.this.updateSelectionTitle();
            return false;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            Log.secD("AlarmListView", "onDestroyActionMode()");
            AlarmListView.this.mCursorAdapter.removeSelection();
            AlarmListView.this.mCursorAdapter.setAlarmListMode(0);
            AlarmListView.this.showCheckBox(false);
            if (AlarmListView.this.mCustomView != null) {
                this.mSelectAll.animate().alphaBy(1.0f).alpha(0.0f).setDuration(150).setInterpolator(new SineInOut90()).withEndAction(new C05672()).start();
                this.mSelectAllLayout.animate().alphaBy(1.0f).alpha(0.0f).setDuration(150).setInterpolator(new SineInOut90()).withEndAction(new C05683()).start();
                this.mSelectionTitle.setVisibility(8);
                if (this.mToolbarChildCount == 2) {
                    this.mToolbarChildView[1].setVisibility(0);
                    this.mToolbarChildView[1].animate().alphaBy(0.0f).alpha(1.0f).setDuration(1050).setInterpolator(new SineInOut90());
                } else {
                    AlarmListView.this.setToolbar(0, AlarmListView.this.mToolbarContentInsetStart);
                }
            }
            AlarmListView.this.updateSelectionTitle();
            AlarmListView.this.mIsDeletingFlag = false;
        }

        private void setEventListener() {
            this.mSelectAllLayout.setOnClickListener(new C05694());
        }

        void finishActionMode() {
            if (this.mListActionMode != null) {
                this.mListActionMode.finish();
            }
        }

        boolean tabKeyProcess() {
            if (this.mSelectAllLayout == null) {
                return false;
            }
            this.mSelectAllLayout.requestFocus();
            this.mSelectAllLayout.setNextFocusForwardId(AlarmListView.this.mDeleteBottom.getId());
            return true;
        }

        void setActionBarTitle(float alpha) {
            if (this.mSelectionTitle != null) {
                this.mSelectionTitle.setAlpha(alpha);
            }
        }

        String updateSelectionMenu(int checkedCount, int alarmItemCount) {
            String selectTitle;
            boolean checkSelectAll = false;
            boolean isNotAbleToSelectAll;
            if (!AlarmListView.this.mIsMultiSelectStarted || AlarmListView.this.mFirstItemClickedPosition == 0 || AlarmListView.this.mFirstItemClickedPosition == alarmItemCount - 1) {
                isNotAbleToSelectAll = false;
            } else {
                isNotAbleToSelectAll = true;
            }
            StringBuilder allCheckboxDescription = new StringBuilder();
            if (checkedCount > 0) {
                selectTitle = AlarmListView.this.mContext.getString(C0490R.string.pd_selected, new Object[]{Integer.valueOf(checkedCount)});
                if (checkedCount != alarmItemCount || isNotAbleToSelectAll) {
                    allCheckboxDescription.append(selectTitle).append(". ").append(AlarmListView.this.mContext.getResources().getString(C0490R.string.double_tap_to_select_all_tts));
                } else {
                    checkSelectAll = true;
                    allCheckboxDescription.append(selectTitle).append(". ").append(AlarmListView.this.mContext.getResources().getString(C0490R.string.double_tab_to_deselect_all_tts));
                }
            } else {
                selectTitle = AlarmListView.this.mContext.getString(C0490R.string.select_alarms);
                allCheckboxDescription.append(AlarmListView.this.mContext.getResources().getString(C0490R.string.nothing_selected_tts)).append(" ").append(AlarmListView.this.mContext.getResources().getString(C0490R.string.double_tap_to_select_all_tts));
            }
            this.mSelectAll.setChecked(checkSelectAll);
            this.mSelectAll.setContentDescription(allCheckboxDescription);
            this.mSelectionTitle.setText(selectTitle);
            return selectTitle;
        }
    }

    public AlarmListView(Context context) {
        super(context);
        this.mContext = context;
    }

    public AlarmListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public AlarmListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void onCreateView(Activity activity) {
        this.mActivity = activity;
        this.mAlarmToolbar = (Toolbar) findViewById(C0490R.id.toolbar);
        ((AppCompatActivity) this.mActivity).setSupportActionBar(this.mAlarmToolbar);
        this.mTitle = (TextView) findViewById(C0490R.id.alarm_app_bar_title);
        this.mSubtitle = (TextView) findViewById(C0490R.id.alarm_app_bar_subtitle);
        if (Locale.getDefault().getLanguage().equals("kn")) {
            this.mTitle.setLineSpacing(0.0f, 1.0f);
        }
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mTitle, (float) getResources().getDimensionPixelSize(C0490R.dimen.alarm_appbar_title_text_size));
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mSubtitle, (float) getResources().getDimensionPixelSize(C0490R.dimen.alarm_appbar_sub_title_text_size));
        this.mCursorAdapter = new AlarmCursorAdapter(this.mContext, AlarmDataHandler.getAlarmData(this.mContext));
        this.mCursorAdapter.setOnViewHolderListener(new C05561());
        this.mActionModeCallBack = new AlarmActionModeCallback();
        Intent intent = this.mActivity.getIntent();
        this.mAlarmLaunchMode = intent.getIntExtra("AlarmLaunchMode", 0);
        String setAsUri = intent.getStringExtra("alarm_uri");
        boolean bDeleteMode = intent.getBooleanExtra("AlarmDeleteMode", false);
        if (setAsUri != null) {
            this.mSetAsUri = setAsUri;
        }
        intent.putExtra("alarm_uri", this.mSetAsUri);
        String intentAction = intent.getAction();
        if (intentAction != null && "com.sec.android.widgetapp.alarmclock.ALARM_APPWIDGET_ADDNEW".equals(intentAction)) {
            if (intent.hasExtra("from") && intent.getStringExtra("from").equals("SimpleClockAlarmWidget")) {
                this.mAlarmLaunchMode = 2;
            } else {
                this.mAlarmLaunchMode = 1;
            }
            this.mWidgetID = intent.getIntExtra("widgetId", -1);
        }
        Log.secD("AlarmListView", "onCreateView() mAlarmLaunchMode=" + this.mAlarmLaunchMode);
        if (bDeleteMode) {
            startDeleteActionMode();
        }
        initListViewInAlarm();
        setAlarmListWidth(new Configuration(getResources().getConfiguration()));
    }

    public void onResume() {
        this.mIsAlarmEditStarted = false;
    }

    public void onDestroy() {
        finishActionMode();
        this.mActionModeCallBack = null;
        this.mAddAlarmButton = null;
        if (this.mCursorAdapter != null) {
            this.mCursorAdapter.changeCursor(null);
            this.mCursorAdapter = null;
        }
        if (this.mList != null) {
            AlarmListClickEvent.removeFrom(this.mList);
            this.mList.removeAllViewsInLayout();
            this.mList.setAdapter(null);
            this.mList = null;
        }
    }

    private void setMainSubTitleSize() {
        if (this.mTitle == null) {
            this.mTitle = (TextView) findViewById(C0490R.id.alarm_app_bar_title);
        }
        if (this.mSubtitle == null) {
            this.mSubtitle = (TextView) findViewById(C0490R.id.alarm_app_bar_subtitle);
        }
        Resources res = getResources();
        LayoutParams params = (LayoutParams) findViewById(C0490R.id.alarm_app_bar_text).getLayoutParams();
        params.setMarginStart(res.getDimensionPixelSize(C0490R.dimen.alarm_appbar_main_title_margin));
        params.setMarginEnd(res.getDimensionPixelSize(C0490R.dimen.alarm_appbar_main_title_margin));
        if (Locale.getDefault().getLanguage().equals("kn")) {
            this.mTitle.setLineSpacing(0.0f, 1.0f);
        } else {
            this.mTitle.setLineSpacing(res.getDimension(C0490R.dimen.alarm_appbar_title_text_line_spacing), 1.0f);
        }
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mTitle, (float) res.getDimensionPixelSize(C0490R.dimen.alarm_appbar_title_text_size));
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mSubtitle, (float) res.getDimensionPixelSize(C0490R.dimen.alarm_appbar_sub_title_text_size));
    }

    private void updateSelectionTitle() {
        if (this.mActionModeCallBack != null) {
            this.mActionModeCallBack.updateSelectionMenu(this.mCursorAdapter.getSelectedItemsSize(), this.mCursorAdapter.getItemCount());
            updateClosestAlertText();
        }
    }

    public void setMainTitleWithPresetAlarm() {
        if (this.mTitle != null && this.mContext != null) {
            this.mTitle.setText(this.mContext.getResources().getString(C0490R.string.alarm_main_alert_all_off));
        }
    }

    public void updateClosestAlertText() {
        String mainAlertString;
        String subAlertString = " ";
        if (this.mAlarmLaunchMode == 2) {
            mainAlertString = getResources().getString(C0490R.string.select_alarm);
        } else if (this.mCursorAdapter != null && this.mCursorAdapter.isAlarmListDeleteMode() && !this.mIsDeletingFlag) {
            mainAlertString = this.mActionModeCallBack.updateSelectionMenu(this.mCursorAdapter.getSelectedItemsSize(), this.mCursorAdapter.getItemCount());
        } else if (this.mCursorAdapter == null || this.mCursorAdapter.getItemCount() <= 0) {
            mainAlertString = getResources().getString(C0490R.string.alarm);
        } else if (Alarm.isStopAlarmAlert) {
            String sortOrder = "alerttime asc  , active asc limit 1";
            Cursor alarmCursor = this.mContext.getContentResolver().query(AlarmProvider.CONTENT_URI, new String[]{"active", "alerttime", "alarmtime", "dailybrief"}, "active > ?", new String[]{"0"}, "alerttime asc  , active asc limit 1");
            Resources res = getResources();
            mainAlertString = res.getString(C0490R.string.alarm_main_alert_all_off);
            if (alarmCursor != null) {
                if (alarmCursor.moveToFirst()) {
                    int active = alarmCursor.getInt(alarmCursor.getColumnIndex("active"));
                    int dailyBriefing = alarmCursor.getInt(alarmCursor.getColumnIndex("dailybrief"));
                    long closestAlertTime = alarmCursor.getLong(alarmCursor.getColumnIndex("alerttime"));
                    Calendar currentCal = Calendar.getInstance();
                    long currentTime = currentCal.getTimeInMillis();
                    int currentHour = currentCal.get(11);
                    int diffMin = (int) Math.ceil((double) (((float) (closestAlertTime - currentTime)) / 60000.0f));
                    if (diffMin <= 0) {
                        Log.m42e("AlarmListView", "updateClosestAlertText() currentTime = " + currentTime + " , closestAlertTime = " + closestAlertTime);
                        Log.m42e("AlarmListView", "active = " + active + " , alarmTime = " + alarmCursor.getInt(alarmCursor.getColumnIndex("alarmtime")) + ", dailyBriefing = " + dailyBriefing);
                    }
                    if (active > 1) {
                        mainAlertString = res.getString(C0490R.string.alarm_main_alert_snoozed_alarm);
                        subAlertString = res.getQuantityString(C0490R.plurals.alarm_main_alert_snooze_in_min_plurals, diffMin, new Object[]{Integer.valueOf(diffMin)});
                    } else if (currentHour < 21 && (dailyBriefing & 4) == 4 && Feature.isSupportSubstituteHolidayMenu() && HolidayUtil.isHolidayInCalendarForKorea(currentTime, AlarmItem.isSubstituteHoliday(dailyBriefing))) {
                        mainAlertString = res.getString(C0490R.string.alarm_main_alert_not_ringing_on_holidays_kor);
                    } else if (currentHour < 21 && (dailyBriefing & 4) == 4 && !Feature.isSupportSubstituteHolidayMenu() && HolidayUtil.isHolidayInCalendarForJapan(currentTime)) {
                        mainAlertString = res.getString(C0490R.string.alarm_main_alert_not_ringing_on_public_holidays_jp);
                    } else if (currentHour < 21 && (dailyBriefing & 8) == 8 && Feature.isSupportAlarmOptionMenuForWorkingDay() && HolidayUtil.isHolidayInCalendarForChina(this.mContext, currentTime)) {
                        mainAlertString = res.getString(C0490R.string.alarm_main_alert_ringing_only_workdays_chn);
                    } else {
                        if (diffMin > 1440) {
                            Calendar midnight = Calendar.getInstance();
                            midnight.add(5, 1);
                            midnight.set(11, 0);
                            midnight.set(12, 0);
                            midnight.set(13, 0);
                            midnight.set(14, 0);
                            int diffHourFromMidnight = (int) ((closestAlertTime - midnight.getTimeInMillis()) / 3600000);
                            mainAlertString = res.getQuantityString(C0490R.plurals.alarm_main_alert_in_day_plurals, (diffHourFromMidnight / 24) + 1, new Object[]{Integer.valueOf((diffHourFromMidnight / 24) + 1)});
                        } else if (diffMin > 60) {
                            int hours = diffMin / 60;
                            switch (diffMin % 60) {
                                case 0:
                                    mainAlertString = res.getQuantityString(C0490R.plurals.alarm_main_alert_in_hour_plurals, hours, new Object[]{Integer.valueOf(hours)});
                                    break;
                                case 1:
                                    if (hours != 1) {
                                        mainAlertString = res.getString(C0490R.string.alarm_main_alert_in_hours_one_min_plurals, new Object[]{Integer.valueOf(hours)});
                                        break;
                                    }
                                    mainAlertString = res.getString(C0490R.string.alarm_main_alert_in_one_hour_one_min_plurals);
                                    break;
                                default:
                                    if (hours != 1) {
                                        mainAlertString = res.getString(C0490R.string.alarm_main_alert_hours_minutes, new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes)});
                                        break;
                                    }
                                    mainAlertString = res.getString(C0490R.string.alarm_main_alert_one_hour_minutes, new Object[]{Integer.valueOf(minutes)});
                                    break;
                            }
                        } else if (diffMin > 1) {
                            mainAlertString = res.getQuantityString(C0490R.plurals.alarm_main_alert_in_min_plurals, diffMin, new Object[]{Integer.valueOf(diffMin)});
                        } else {
                            mainAlertString = res.getString(C0490R.string.alarm_main_alert_alarm_will_sound);
                        }
                        subAlertString = subTextViewString(closestAlertTime);
                    }
                }
                alarmCursor.close();
            }
        } else {
            mainAlertString = getResources().getString(C0490R.string.alarm_main_alert_alarm_now_ringing);
            subAlertString = subTextViewString(System.currentTimeMillis());
        }
        this.mTitle.setText(mainAlertString);
        this.mSubtitle.setText(subAlertString);
    }

    private String subTextViewString(long alertTime) {
        return DateUtils.formatDateTime(this.mContext, alertTime, 98323);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setMainSubTitleSize();
        setAlarmListLayoutManager(false);
        if (this.mCursorAdapter != null) {
            this.mCursorAdapter.setAlertTextViewLetterSpace();
            this.mCursorAdapter.notifyDataSetChanged();
        }
        setAlarmListWidth(newConfig);
    }

    private void setAlarmListWidth(Configuration newConfig) {
        if (this.mActivity == null) {
            Log.secE("AlarmListView", "setAlarmListWidth() mActivity is null");
            return;
        }
        boolean isTabletLandScapeList = newConfig.orientation == 2 && StateUtils.isScreenDp(this.mActivity, 600);
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) this.mList.getLayoutParams();
        if (isTabletLandScapeList) {
            Point point = new Point();
            if (StateUtils.isContextInDexMode(this.mActivity) || StateUtils.isInMultiwindow()) {
                this.mActivity.getWindowManager().getDefaultDisplay().getSize(point);
            } else {
                this.mActivity.getWindowManager().getDefaultDisplay().getRealSize(point);
            }
            param.width = point.y;
            return;
        }
        param.width = -1;
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        if (this.mIsDeletingFlag) {
            finishActionMode();
        } else if ((this.mCursorAdapter != null && this.mCursorAdapter.isAlarmListDeleteMode()) || this.mIsNeedRestoreActionMode) {
            bundle.putString("mode", "delete");
            bundle.putBoolean("alarm_list_is_delete_mode", true);
            bundle.putIntegerArrayList("alarm_list_checked_items", this.mCursorAdapter.toggleSelectedAlarmPositions());
            this.mIsNeedRestoreActionMode = true;
        }
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.getBoolean("alarm_list_is_delete_mode", false) && this.mCursorAdapter != null) {
                this.mIsNeedRestoreActionMode = true;
                ArrayList<Integer> selectedItems = bundle.getIntegerArrayList("alarm_list_checked_items");
                if (selectedItems != null && selectedItems.size() > 0) {
                    this.mCursorAdapter.setSelectedAlarmIds(selectedItems);
                }
            }
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    public void startAlarmEdit(String type, int index) {
        if (!this.mIsAlarmEditStarted) {
            this.mIsAlarmEditStarted = true;
            Log.secD("AlarmListView", "startAlarmEdit type = " + type + ", index = " + index);
            int alarmCnt = this.mCursorAdapter.getItemCount();
            Intent intent = new Intent();
            intent.setClassName(this.mContext.getApplicationContext(), "com.sec.android.app.clockpackage.alarm.activity.AlarmEditActivity");
            intent.setType(type);
            intent.putExtra("AlarmLaunchMode", this.mAlarmLaunchMode);
            intent.putExtra("widgetId", this.mWidgetID);
            if (this.mSetAsUri != null) {
                intent.putExtra("alarm_uri", this.mSetAsUri);
            }
            if (index != -1) {
                intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", (int) this.mCursorAdapter.getItemId(index));
                intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_POSITION", index);
                this.mContext.startActivity(intent);
            } else if (alarmCnt >= 50) {
                AlarmUtil.showMaxCountToast(this.mContext);
                this.mIsAlarmEditStarted = false;
            } else {
                intent.putExtra("com.samsung.sec.android.clockpackage.alarm.ALARM_ID", alarmCnt);
                ClockUtils.insertSaLog("101", "1008");
                this.mContext.startActivity(intent);
            }
            if (this.mAlarmLaunchMode == 2) {
                this.mActivity.finish();
            }
        }
    }

    private void initListViewInAlarm() {
        this.mList = (RecyclerView) findViewById(C0490R.id.alarm_list);
        if (this.mList == null) {
            Log.secE("AlarmListView", "mList is null. something is wrong!!");
            return;
        }
        setAlarmListLayoutManager(true);
        this.mList.setAdapter(this.mCursorAdapter);
        this.mList.postDelayed(new C05572(), 500);
        this.mList.getViewTreeObserver().addOnGlobalLayoutListener(new C05583());
        AlarmListClickEvent.addTo(this.mList).setOnItemClickListener(new C05594());
        if (this.mAlarmLaunchMode != 2) {
            AlarmListClickEvent.addTo(this.mList).setOnItemLongClickListener(new C05605());
        }
        this.mList.setOverScrollMode(1);
        this.mCursorAdapter.setAlarmListMode(0);
    }

    private void setAlarmListSeslCustomApi() {
        this.mList.seslSetGoToTopEnabled(true);
        this.mList.seslSetPenSelectionEnabled(true);
        this.mList.seslSetOutlineStrokeEnabled(false);
        this.mList.seslSetLongPressMultiSelectionListener(new C05616());
        if (this.mAlarmLaunchMode != 2) {
            this.mList.seslSetOnMultiSelectedListener(new C05627());
        }
    }

    private void setAlarmListLayoutManager(boolean isDefault) {
        if (this.mActivity != null && this.mList != null) {
            if (!StateUtils.isScreenDp(this.mActivity, 512) || StateUtils.isInMultiwindow()) {
                if (!isDefault && (this.mList.getLayoutManager() instanceof GridLayoutManager)) {
                    this.mList.setLayoutManager(new LinearLayoutManager(this.mActivity));
                }
            } else if (!(this.mList.getLayoutManager() instanceof GridLayoutManager)) {
                this.mList.setLayoutManager(new GridLayoutManager(this.mActivity, 2));
            }
        }
    }

    public RecyclerView getAlarmRecyclerView() {
        return this.mList;
    }

    public void scrollToAlarm(long alarmId) {
        if (this.mCursorAdapter == null || this.mCursorAdapter.getCursor().isClosed()) {
            Log.secE("AlarmListView", "mCursorAdapter.getCursor() is closed");
            return;
        }
        int alarmPosition = -1;
        int curItemCount = this.mCursorAdapter.getItemCount();
        for (int i = 0; i < curItemCount; i++) {
            if (this.mCursorAdapter.getItemId(i) == alarmId) {
                alarmPosition = i;
                break;
            }
        }
        if (alarmPosition >= 0) {
            this.mList.smoothScrollToPosition(alarmPosition);
        }
    }

    public int getAlarmItemCount() {
        if (this.mCursorAdapter != null) {
            return this.mCursorAdapter.getItemCount();
        }
        Log.secE("AlarmListView", "getAlarmItemCount() The cursorAdapter is not available");
        this.mActivity.finish();
        return -1;
    }

    private void alarmListItemSelected(int position) {
        if (this.mAlarmLaunchMode == 2) {
            AlarmUtil.sendSelctionToAlarmWidget(this.mActivity.getApplicationContext(), this.mWidgetID, (int) this.mCursorAdapter.getItemId(position), -1, position);
            this.mActivity.finish();
            return;
        }
        startAlarmEdit("alarm_edit", position);
    }

    public void startDeleteActionMode() {
        if (!this.mCursorAdapter.isAlarmListDeleteMode() && this.mCursorAdapter.getItemCount() > 0) {
            try {
                if (this.mActivity instanceof AppCompatActivity) {
                    ((AppCompatActivity) this.mActivity).startSupportActionMode(this.mActionModeCallBack);
                }
                if (this.mCursorAdapter.getItemCount() == 1) {
                    toggleCheckBox(0, true);
                    this.mCursorAdapter.setAlarmDeleteModeCheckedState((AlarmItemViewHolder) this.mList.findViewHolderForAdapterPosition(0), 0);
                }
                showHideBottomNavigationView();
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.secE("AlarmListView", "StartDeleteActionMode() ArrayIndexOutOfBoundsException");
            }
        }
    }

    public void showHideBottomNavigationView() {
        if (!(this.mActivity instanceof ActionModeListener)) {
            return;
        }
        if (this.mCursorAdapter.getSelectedItemsSize() > 0) {
            ((ActionModeListener) this.mActivity).onShowBottomNavigationView();
        } else {
            ((ActionModeListener) this.mActivity).onHideBottomNavigationView();
        }
    }

    private void toggleCheckBox(int position, boolean isToggleMustCheck) {
        if (this.mCursorAdapter.isAlarmListDeleteMode() && !this.mIsDeletingFlag) {
            ArrayList<Integer> selectedAlarmItems = new ArrayList();
            selectedAlarmItems.add(Integer.valueOf(position));
            this.mCursorAdapter.toggleSelectCheckBox(selectedAlarmItems, isToggleMustCheck);
            updateSelectionTitle();
        }
    }

    public void toggleAllCheckBox(boolean isToggleCheck) {
        if (this.mCursorAdapter.isAlarmListDeleteMode() && !this.mIsDeletingFlag) {
            this.mCursorAdapter.toggleAllSelectCheckBox(isToggleCheck);
            updateSelectionTitle();
            int childCount = this.mCursorAdapter.getItemCount();
            for (int position = 0; position < childCount; position++) {
                AlarmItemViewHolder itemViewHolder = (AlarmItemViewHolder) this.mList.findViewHolderForAdapterPosition(position);
                if (itemViewHolder != null) {
                    this.mCursorAdapter.setAlarmDeleteModeCheckedState(itemViewHolder, position);
                } else {
                    this.mCursorAdapter.notifyItemChanged(position);
                }
            }
            showHideBottomNavigationView();
        }
    }

    public void finishActionMode() {
        if (this.mActionModeCallBack != null) {
            this.mActionModeCallBack.finishActionMode();
        }
    }

    public void updateAlarmDataForList(Cursor alarmCursor) {
        Log.secD("AlarmListView", "updateAlarmDataForList()");
        if (alarmCursor == null || this.mCursorAdapter == null) {
            Log.secE("AlarmListView", "updateAlarmDataForList() Kill to force . because Null Ptr");
            this.mActivity.finish();
            return;
        }
        this.mCursorAdapter.changeCursor(alarmCursor);
    }

    public void updateAllAlarmListView() {
        if (this.mCursorAdapter != null) {
            this.mCursorAdapter.notifyDataSetChanged();
        }
    }

    public boolean tabKeyProcess() {
        if (this.mCursorAdapter.isAlarmListDeleteMode() && this.mList != null && this.mList.isFocused()) {
            return this.mActionModeCallBack.tabKeyProcess();
        }
        return false;
    }

    public boolean isAppBarExpandNeedCheck(int minListViewHeight, int alarmId) {
        int totalHeight = 0;
        int itemCountOfRow = 1;
        int curItemCount = this.mCursorAdapter.getItemCount();
        if (curItemCount != 0 && ((LinearLayoutManager) this.mList.getLayoutManager()).findFirstCompletelyVisibleItemPosition() != 0) {
            return false;
        }
        if (StateUtils.isScreenDp(this.mActivity, 512)) {
            itemCountOfRow = 2;
        }
        int i = 0;
        while (i < curItemCount) {
            if (i > 0 && i == curItemCount - 1) {
                totalHeight += this.mList.getLayoutManager().findViewByPosition(i - 1).getMeasuredHeight();
            } else if (this.mList.getLayoutManager().findViewByPosition(i) == null) {
                return true;
            } else {
                totalHeight += this.mList.getLayoutManager().findViewByPosition(i).getMeasuredHeight();
            }
            if (minListViewHeight < totalHeight) {
                return true;
            }
            if (this.mCursorAdapter.getItemId(i) == ((long) alarmId)) {
                return false;
            }
            i += itemCountOfRow;
        }
        return false;
    }

    public boolean isDeleteActionMode() {
        return this.mCursorAdapter.isAlarmListDeleteMode();
    }

    private void showCheckBox(boolean isNeedToShowCheckbox) {
        Log.secD("AlarmListView", "showCheckBox() isNeedToShowCheckbox = " + isNeedToShowCheckbox);
        if (this.mIsNeedRestoreActionMode) {
            this.mCursorAdapter.notifyDataSetChanged();
            return;
        }
        int childCount = this.mCursorAdapter.getItemCount();
        for (int i = 0; i < childCount; i++) {
            AlarmItemViewHolder itemViewHolder = null;
            if (this.mList != null) {
                itemViewHolder = (AlarmItemViewHolder) this.mList.findViewHolderForAdapterPosition(i);
            }
            if (itemViewHolder != null) {
                this.mCursorAdapter.startAnimation(i, itemViewHolder);
            } else {
                this.mCursorAdapter.notifyItemChanged(i);
            }
        }
    }

    public void restoreActionMode() {
        if (!(!this.mIsNeedRestoreActionMode || this.mActivity == null || this.mCursorAdapter.isAlarmListDeleteMode())) {
            ((AppCompatActivity) this.mActivity).startSupportActionMode(this.mActionModeCallBack);
            showHideBottomNavigationView();
        }
        this.mIsNeedRestoreActionMode = false;
    }

    public void initBottomNavigationView(BottomNavigationView bottomView) {
        this.mDeleteBottom = bottomView;
        this.mDeleteBottom.setOnNavigationItemSelectedListener(new C05638());
        updateSelectionTitle();
    }

    public void addAnimationFinished() {
        final long startAnimationDuration = this.mList.getItemAnimator().getMoveDuration() + this.mList.getItemAnimator().getAddDuration();
        this.mList.getItemAnimator().isRunning(new ItemAnimatorFinishedListener() {

            /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$9$1 */
            class C05641 implements Runnable {
                C05641() {
                }

                public void run() {
                    if (AlarmListView.this.mCursorAdapter != null) {
                        AlarmListView.this.mCursorAdapter.notifyDataSetChanged();
                    }
                }
            }

            public void onAnimationsFinished() {
                AlarmListView.this.mList.postDelayed(new C05641(), startAnimationDuration);
            }
        });
    }

    public void startDeleteAnimation() {
        long deleteAnimationDuration;
        boolean isDeleteAll = true;
        this.mIsDeletingFlag = true;
        if (this.mCursorAdapter.getSelectedItemsSize() != this.mCursorAdapter.getItemCount()) {
            isDeleteAll = false;
        }
        if (isDeleteAll) {
            deleteAnimationDuration = this.mList.getItemAnimator().getMoveDuration() + this.mList.getItemAnimator().getRemoveDuration();
        } else {
            deleteAnimationDuration = (this.mList.getItemAnimator().getMoveDuration() + this.mList.getItemAnimator().getRemoveDuration()) + 150;
        }
        this.mCursorAdapter.deleteContent();
        this.mCursorAdapter.removeSelection();
        if (isDeleteAll) {
            this.mList.postDelayed(new Runnable() {
                public void run() {
                    AlarmListView.this.mAlarmToolbar.removeView(AlarmListView.this.mCustomView);
                    AlarmListView.this.mCustomView = null;
                    AlarmListView.this.setToolbar(0, AlarmListView.this.mToolbarContentInsetStart);
                }
            }, this.mList.getItemAnimator().getRemoveDuration());
        }
        this.mList.getItemAnimator().isRunning(new ItemAnimatorFinishedListener() {

            /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$11$1 */
            class C05541 implements Runnable {
                C05541() {
                }

                public void run() {
                    if (isDeleteAll) {
                        AlarmListView.this.setNoItemViewVisibility(0);
                    }
                    AlarmListView.this.mIsDeletingFlag = false;
                    if (AlarmListView.this.mCursorAdapter != null) {
                        AlarmListView.this.mCursorAdapter.notifyDataSetChanged();
                    }
                }
            }

            /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmListView$11$2 */
            class C05552 implements Runnable {
                C05552() {
                }

                public void run() {
                    AlarmListView.this.finishActionMode();
                }
            }

            public void onAnimationsFinished() {
                AlarmListView.this.mList.postDelayed(new C05541(), deleteAnimationDuration);
                AlarmListView.this.mList.postDelayed(new C05552(), deleteAnimationDuration + 50);
            }
        });
    }

    public void setNoItemViewVisibility(int alarmCnt) {
        Log.secD("AlarmListView", "setNoItemViewVisibility alarmCnt = " + alarmCnt);
        if (alarmCnt <= 0) {
            if (this.mAddAlarmButton == null) {
                ((ViewStub) findViewById(C0490R.id.no_alarm_add_button_stub)).inflate();
                this.mAddAlarmButton = (ClockAddButton) findViewById(C0490R.id.no_alarm_add_button);
            }
            this.mAddAlarmButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AlarmListView.this.startAlarmEdit("alarm_create", -1);
                }
            });
            this.mAddAlarmButton.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (AlarmListView.this.mList != null) {
                        AlarmListView.this.mList.dispatchTouchEvent(event);
                    }
                    if (event.getAction() == 1) {
                        Rect addButtonRect = new Rect();
                        v.getHitRect(addButtonRect);
                        if (addButtonRect.contains((int) event.getX(), (int) event.getY())) {
                            v.performClick();
                        }
                    }
                    return false;
                }
            });
            this.mAddAlarmButton.setVisibility(0);
        } else if (this.mAddAlarmButton != null) {
            this.mAddAlarmButton.setVisibility(8);
        }
    }

    public void removeBottomNavigationView() {
        if (this.mDeleteBottom != null) {
            this.mDeleteBottom.setOnNavigationItemSelectedListener(null);
            this.mDeleteBottom = null;
        }
    }

    public void setActionBarTitle(float alpha) {
        if (this.mActionModeCallBack != null && this.mCursorAdapter.isAlarmListDeleteMode()) {
            this.mActionModeCallBack.setActionBarTitle(alpha);
        }
    }

    private void setToolbar(int visibility, int insetStart) {
        int toolbarChildCount = this.mAlarmToolbar.getChildCount();
        this.mAlarmToolbar.setContentInsetsRelative(insetStart, 0);
        for (int i = 0; i < toolbarChildCount; i++) {
            this.mAlarmToolbar.getChildAt(i).setVisibility(visibility);
            if (visibility == 0) {
                this.mAlarmToolbar.getChildAt(i).animate().alphaBy(0.0f).alpha(1.0f).setDuration(150).setInterpolator(new SineInOut90()).start();
            } else {
                this.mAlarmToolbar.getChildAt(i).animate().alphaBy(1.0f).alpha(0.0f).setDuration(150).setInterpolator(new SineInOut90()).start();
            }
        }
    }
}
