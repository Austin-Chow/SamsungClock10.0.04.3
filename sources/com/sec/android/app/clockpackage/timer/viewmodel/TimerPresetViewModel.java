package com.sec.android.app.clockpackage.timer.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.sqlite.SQLiteFullException;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorFinishedListener;
import android.support.v7.widget.RecyclerView.SeslLongPressMultiSelectionListener;
import android.support.v7.widget.RecyclerView.SeslOnMultiSelectedListener;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowInsets;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.callback.ActionModeListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerPresetAdapterListener;
import com.sec.android.app.clockpackage.timer.callback.TimerPresetViewActionModeListener;
import com.sec.android.app.clockpackage.timer.callback.TimerPresetViewListener;
import com.sec.android.app.clockpackage.timer.model.TimerData;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import java.util.ArrayList;
import java.util.Collections;

public class TimerPresetViewModel extends RelativeLayout {
    private TimerPresetViewActionModeListener mActionModeListener = new C08141();
    private TimerPresetAdapterListener mAdapterListener = new C08152();
    private int mBackupHour;
    private int mBackupMinute;
    private int mBackupSecond;
    private BottomNavigationView mBottomNavigationView;
    private ArrayList<Long> mCheckedIds;
    private ArrayList<Integer> mCheckedPositions;
    private Context mContext;
    private boolean mHasFocus = false;
    private boolean mIsMultiWindowMode = false;
    private boolean mIsUpdateActionModeMenu = true;
    private int mOrientation = 0;
    private TimerPresetAdapter mPresetAdapter;
    private ArrayList<TimerPresetItem> mPresetList = new ArrayList();
    private RecyclerView mPresetListView;
    private TimerPresetViewActionMode mPresetViewActionMode;
    private TimerPresetViewListener mTimerPresetViewListener;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$1 */
    class C08141 implements TimerPresetViewActionModeListener {
        C08141() {
        }

        public void onUpdateActionModeMenu() {
            TimerPresetViewModel.this.updateActionModeMenu();
        }

        public void onSetViewEnabled(boolean enabled) {
            TimerPresetViewModel.this.mTimerPresetViewListener.onSetViewEnabled(enabled);
        }

        public Toolbar onGetToolbar() {
            return TimerPresetViewModel.this.mTimerPresetViewListener.onGetToolbar();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$2 */
    class C08152 implements TimerPresetAdapterListener {
        C08152() {
        }

        public boolean isActionMode() {
            return TimerPresetViewModel.this.mPresetViewActionMode != null && TimerPresetViewModel.this.mPresetViewActionMode.isActionMode();
        }

        public void onItemClick(View v, int position) {
            Log.secD("TimerPresetViewModel", "onItemClick() / position = " + position);
            if (position == -1 || !TimerData.isTimerStateResetedOrNone()) {
                return;
            }
            if (TimerPresetViewModel.this.mPresetViewActionMode == null || !TimerPresetViewModel.this.mPresetViewActionMode.isActionMode()) {
                int hour;
                int minute;
                int second;
                TimerPresetItem item = TimerPresetViewModel.this.mPresetAdapter.getItem(position);
                long id = TimerPresetViewModel.this.mPresetAdapter.getItemId(position);
                if (item == null || TimerPresetViewModel.this.getSelectedPresetId() == id) {
                    TimerPresetViewModel.this.setSelectedPresetId(-1);
                    hour = TimerPresetViewModel.this.mBackupHour;
                    minute = TimerPresetViewModel.this.mBackupMinute;
                    second = TimerPresetViewModel.this.mBackupSecond;
                } else {
                    TimerPresetViewModel.this.setSelectedPresetId(id);
                    TimerPresetViewModel.this.mBackupHour = TimerData.getInputHour();
                    TimerPresetViewModel.this.mBackupMinute = TimerData.getInputMin();
                    TimerPresetViewModel.this.mBackupSecond = TimerData.getInputSec();
                    hour = item.getHour();
                    minute = item.getMinute();
                    second = item.getSecond();
                    TimerPresetViewModel.this.scrollToPresetId(id);
                }
                TimerPresetViewModel.this.mTimerPresetViewListener.onSetPickerTime(hour, minute, second, true);
                ClockUtils.insertSaLog("130", "1131");
                return;
            }
            TimerPresetViewModel.this.mPresetAdapter.toggleCheck(position);
            TimerPresetViewModel.this.mPresetViewActionMode.updateSelectModeActionBar();
        }

        public void onItemLongClick(View v, int position) {
            Log.secD("TimerPresetViewModel", "onItemLongClick() / position = " + position);
            if (TimerPresetViewModel.this.mPresetViewActionMode != null && TimerPresetViewModel.this.mPresetListView != null && !TimerPresetViewModel.this.mPresetViewActionMode.isActionMode() && TimerPresetViewModel.this.mContext != null) {
                TimerPresetViewModel.this.mIsUpdateActionModeMenu = false;
                ((AppCompatActivity) TimerPresetViewModel.this.mContext).startSupportActionMode(TimerPresetViewModel.this.mPresetViewActionMode);
                TimerPresetViewModel.this.mPresetListView.seslStartLongPressMultiSelection();
                TimerPresetViewModel.this.mPresetAdapter.toggleCheck(position);
                TimerPresetViewModel.this.mPresetViewActionMode.updateSelectModeActionBar();
            }
        }

        public void onDeleteContextMenuClick(int position) {
            Log.secD("TimerPresetViewModel", "onDeleteContextMenuClick() / position = " + position);
            TimerPresetViewModel.this.deletePreset(position);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$3 */
    class C08163 implements SeslLongPressMultiSelectionListener {
        C08163() {
        }

        public void onItemSelected(RecyclerView recyclerView, View view, int position, long id) {
            Log.secD("TimerPresetViewModel", "onItemSelected()");
        }

        public void onLongPressMultiSelectionStarted(int startX, int startY) {
            Log.secD("TimerPresetViewModel", "onLongPressMultiSelectionStarted()");
        }

        public void onLongPressMultiSelectionEnded(int endX, int endY) {
            Log.secD("TimerPresetViewModel", "onLongPressMultiSelectionEnded()");
            TimerPresetViewModel.this.enableUpdateActionModeMenu();
            ViewHolder holder = TimerPresetViewModel.this.mPresetListView.findViewHolderForAdapterPosition(TimerPresetViewModel.this.mPresetAdapter.getPressedItemPosition());
            if (holder != null) {
                TimerPresetViewModel.this.mPresetAdapter.startScaleUpAnimation(holder);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$4 */
    class C08174 implements SeslOnMultiSelectedListener {
        private int mSelectionStartPosition = 0;

        C08174() {
        }

        public void onMultiSelected(RecyclerView recyclerView, View view, int position, long id) {
            Log.secD("TimerPresetViewModel", "onMultiSelected() / position = " + position + ", id = " + id);
        }

        public void onMultiSelectStart(int startX, int startY) {
            Log.secD("TimerPresetViewModel", "onMultiSelectStart()");
            this.mSelectionStartPosition = TimerPresetViewModel.this.mPresetListView.getChildLayoutPosition(TimerPresetViewModel.this.mPresetListView.findChildViewUnder((float) startX, (float) startY));
            if (this.mSelectionStartPosition == -1) {
                this.mSelectionStartPosition = TimerPresetViewModel.this.mPresetListView.getChildLayoutPosition(TimerPresetViewModel.this.mPresetListView.seslFindNearChildViewUnder((float) startX, (float) startY));
            }
        }

        public void onMultiSelectStop(int endX, int endY) {
            Log.secD("TimerPresetViewModel", "onMultiSelectStop()");
            if (!TimerPresetViewModel.this.mPresetViewActionMode.isActionMode() && TimerPresetViewModel.this.mPresetAdapter.getItemCount() > 0) {
                TimerPresetViewModel.this.startActionMode(false);
            }
            int selectionEndPosition = TimerPresetViewModel.this.mPresetListView.getChildLayoutPosition(TimerPresetViewModel.this.mPresetListView.findChildViewUnder((float) endX, (float) endY));
            if (selectionEndPosition == -1) {
                selectionEndPosition = TimerPresetViewModel.this.mPresetListView.getChildLayoutPosition(TimerPresetViewModel.this.mPresetListView.seslFindNearChildViewUnder((float) endX, (float) endY));
            }
            if (this.mSelectionStartPosition > selectionEndPosition) {
                int temp = this.mSelectionStartPosition;
                this.mSelectionStartPosition = selectionEndPosition;
                selectionEndPosition = temp;
            }
            int position = this.mSelectionStartPosition;
            while (position <= selectionEndPosition) {
                if (position > -1 && position < TimerPresetViewModel.this.mPresetAdapter.getItemCount()) {
                    TimerPresetViewModel.this.mPresetAdapter.toggleCheck(position);
                }
                position++;
            }
            TimerPresetViewModel.this.mPresetViewActionMode.updateSelectModeActionBar();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$5 */
    class C08185 implements OnNavigationItemSelectedListener {
        C08185() {
        }

        public boolean onNavigationItemSelected(MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == C0728R.id.edit) {
                TimerPresetViewModel.this.modifyPreset();
            } else if (id == C0728R.id.delete) {
                TimerPresetViewModel.this.deletePreset();
            }
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$6 */
    class C08196 implements OnGlobalLayoutListener {
        C08196() {
        }

        public void onGlobalLayout() {
            if (TimerPresetViewModel.this.setListViewPadding()) {
                TimerPresetViewModel.this.mPresetListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$7 */
    class C08207 implements Runnable {
        C08207() {
        }

        public void run() {
            TimerPresetViewModel.this.startActionMode(true);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$8 */
    class C08228 extends DefaultItemAnimator {
        private boolean isRemoved = false;

        /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$8$1 */
        class C08211 implements ItemAnimatorFinishedListener {
            C08211() {
            }

            public void onAnimationsFinished() {
                if (!C08228.this.isRemoved) {
                    TimerPresetViewModel.this.asyncDeletePreset();
                    C08228.this.isRemoved = true;
                }
            }
        }

        C08228() {
        }

        public void onRemoveFinished(ViewHolder item) {
            super.onRemoveFinished(item);
            isRunning(new C08211());
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetViewModel$9 */
    class C08239 extends AsyncTask<Void, Void, Void> {
        C08239() {
        }

        protected Void doInBackground(Void... parameters) {
            try {
                TimerPresetItem.deletePreset(TimerPresetViewModel.this.mContext.getContentResolver(), TimerPresetViewModel.this.mCheckedIds);
            } catch (SQLiteFullException e) {
                Toast.makeText(TimerPresetViewModel.this.mContext, TimerPresetViewModel.this.mContext.getString(C0728R.string.memory_full), 1).show();
            }
            return null;
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Void aVoid) {
            if (TimerPresetViewModel.this.mPresetViewActionMode.isActionMode()) {
                TimerPresetViewModel.this.finishActionMode();
            }
            TimerPresetViewModel.this.updatePresetListView(false);
            TimerPresetViewModel.this.clearCheckedPresetList();
            TimerPresetViewModel.this.mPresetListView.setItemAnimator(null);
        }
    }

    public class RecyclerLayoutManager extends LinearLayoutManager {
        private CenterSmoothScroller mCenterSmoothScroller;

        private class CenterSmoothScroller extends LinearSmoothScroller {
            public CenterSmoothScroller(Context context) {
                super(context);
            }

            public PointF computeScrollVectorForPosition(int targetPosition) {
                return RecyclerLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                return (((boxEnd - boxStart) / 2) + boxStart) - (((viewEnd - viewStart) / 2) + viewStart);
            }

            protected int calculateTimeForScrolling(int dx) {
                return 300;
            }
        }

        public RecyclerLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
            this.mCenterSmoothScroller = new CenterSmoothScroller(context);
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            this.mCenterSmoothScroller.setTargetPosition(position);
            startSmoothScroll(this.mCenterSmoothScroller);
        }
    }

    public TimerPresetViewModel(Context context) {
        super(context);
        init(context);
    }

    public TimerPresetViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimerPresetViewModel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    public void setTimerPresetViewListener(TimerPresetViewListener i) {
        this.mTimerPresetViewListener = i;
    }

    public void initView() {
        this.mPresetListView = (RecyclerView) findViewById(C0728R.id.timer_preset_list);
        this.mPresetList = TimerPresetItem.getAllPreset(this.mContext.getContentResolver(), null, new String[0]);
        this.mPresetAdapter = new TimerPresetAdapter(this.mPresetList, this.mContext, this.mAdapterListener);
        this.mPresetListView.setAdapter(this.mPresetAdapter);
        SharedPreferences sharedPref = this.mContext.getSharedPreferences("TIMER", 0);
        if (sharedPref != null) {
            setSelectedPresetId(sharedPref.getLong("selectedPresetId", -1));
        }
        setPresetListView();
        scrollToPresetId(getSelectedPresetId());
    }

    private void setPresetListView() {
        this.mPresetViewActionMode = new TimerPresetViewActionMode(this.mContext, this.mPresetListView, this.mPresetAdapter, this.mActionModeListener);
        this.mPresetAdapter.getDndHelper().attachToRecyclerView(this.mPresetListView);
        this.mPresetListView.seslSetLongPressMultiSelectionListener(new C08163());
        this.mPresetListView.setLayoutManager(new RecyclerLayoutManager(this.mContext, 0, false));
        this.mPresetListView.seslSetOutlineStrokeEnabled(false);
        this.mPresetListView.seslSetHoverScrollEnabled(false);
        this.mPresetListView.seslSetOnMultiSelectedListener(new C08174());
    }

    public void initBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.mBottomNavigationView = bottomNavigationView;
        this.mBottomNavigationView.findViewById(C0728R.id.edit).setVisibility(0);
        this.mBottomNavigationView.setOnNavigationItemSelectedListener(new C08185());
    }

    public void removeBottomNavigationView() {
        if (this.mBottomNavigationView != null) {
            this.mBottomNavigationView.setOnNavigationItemSelectedListener(null);
            this.mBottomNavigationView = null;
        }
    }

    public void enableUpdateActionModeMenu() {
        if (!this.mIsUpdateActionModeMenu) {
            this.mIsUpdateActionModeMenu = true;
            updateActionModeMenu();
        }
    }

    private void updateActionModeMenu() {
        if (this.mPresetAdapter != null && this.mIsUpdateActionModeMenu) {
            int checkedCount = this.mPresetAdapter.getCheckedItemCount();
            if (checkedCount != 0) {
                if (this.mContext instanceof ActionModeListener) {
                    ((ActionModeListener) this.mContext).onShowBottomNavigationView();
                }
                if (this.mBottomNavigationView != null) {
                    MenuItem editMenu = this.mBottomNavigationView.getMenu().findItem(C0728R.id.edit);
                    if (editMenu != null) {
                        editMenu.setEnabled(checkedCount == 1);
                    }
                }
            } else if (this.mContext instanceof ActionModeListener) {
                ((ActionModeListener) this.mContext).onHideBottomNavigationView();
            }
        }
    }

    public void scrollToPresetId(long id) {
        int position = getPresetPosition(id);
        RecyclerView recyclerView = this.mPresetListView;
        if (position == -1) {
            position = 0;
        }
        recyclerView.smoothScrollToPosition(position);
    }

    public void destroyView() {
        if (this.mPresetViewActionMode != null) {
            this.mPresetViewActionMode.finishActionMode();
            this.mPresetViewActionMode.releaseInstance();
            this.mPresetViewActionMode = null;
        }
        if (this.mPresetListView != null) {
            this.mPresetListView.addOnItemTouchListener(null);
            this.mPresetListView.setItemAnimator(null);
            this.mPresetListView.seslSetOnMultiSelectedListener(null);
            this.mPresetListView.setAdapter(null);
            this.mPresetListView.destroyDrawingCache();
            this.mPresetListView = null;
        }
        if (this.mPresetAdapter != null) {
            this.mPresetAdapter.destroy();
            this.mPresetAdapter = null;
        }
        this.mContext = null;
        this.mPresetList = null;
        this.mCheckedIds = null;
        this.mCheckedPositions = null;
        removeAllViews();
    }

    public long getSelectedPresetId() {
        return this.mPresetAdapter.getSelectedPresetId();
    }

    public void setSelectedPresetId(long id) {
        if (id != getSelectedPresetId()) {
            this.mPresetAdapter.setSelectedPresetId(id);
            SharedPreferences sharedPref = this.mContext.getSharedPreferences("TIMER", 0);
            if (sharedPref != null) {
                Editor ed = sharedPref.edit();
                ed.putLong("selectedPresetId", id);
                ed.apply();
            }
        }
    }

    public void setBackupTime(int hour, int minute, int second) {
        this.mBackupHour = hour;
        this.mBackupMinute = minute;
        this.mBackupSecond = second;
    }

    public int getPresetCount() {
        return this.mPresetList.size();
    }

    public TimerPresetItem getPresetItemById(long id) {
        if (id != -1) {
            int position = getPresetPosition(id);
            if (position != -1) {
                return this.mPresetAdapter.getItem(position);
            }
        }
        return null;
    }

    public TimerPresetItem getPresetItemByPosition(int position) {
        if (position != -1) {
            return this.mPresetAdapter.getItem(position);
        }
        return null;
    }

    private int getPresetPosition(long id) {
        if (id == -1) {
            return -1;
        }
        for (int i = 0; i < this.mPresetAdapter.getItemCount(); i++) {
            if (id == this.mPresetAdapter.getItemId(i)) {
                return i;
            }
        }
        return -1;
    }

    public long getDuplicatedPresetId(String name, int hour, int minute, int second) {
        if (name != null) {
            for (int i = 0; i < this.mPresetAdapter.getItemCount(); i++) {
                TimerPresetItem item = this.mPresetAdapter.getItem(i);
                if (item != null && name.equals(item.getName()) && hour == item.getHour() && minute == item.getMinute() && second == item.getSecond()) {
                    return item.getId();
                }
            }
        }
        return -1;
    }

    public void updatePresetListView(boolean isAdded) {
        View parent = (View) this.mPresetListView.getParent();
        boolean hasFocus = this.mPresetListView.hasFocus();
        if (hasFocus) {
            parent.setFocusable(true);
            parent.requestFocus();
        }
        this.mPresetList = TimerPresetItem.getAllPreset(this.mContext.getContentResolver(), null, new String[0]);
        this.mPresetAdapter.setPresetList(this.mPresetList);
        this.mTimerPresetViewListener.onSetPresetViewVisibility();
        if (hasFocus) {
            this.mPresetListView.requestFocus();
            parent.setFocusable(false);
            if (isAdded) {
                getPresetPosition(this.mPresetAdapter.getSelectedPresetId());
            }
        } else if (this.mPresetViewActionMode.isActionMode() && this.mPresetAdapter.getSelectedPresetId() != -1) {
            getPresetPosition(this.mPresetAdapter.getSelectedPresetId());
        }
    }

    public void setListViewPadding(int orientation, boolean isMultiWindowMode) {
        if (this.mPresetListView.getHeight() == 0 || this.mOrientation != orientation || this.mIsMultiWindowMode != isMultiWindowMode || ((orientation == 1 || StateUtils.isScreenDp(this.mContext, 512) || this.mIsMultiWindowMode) && this.mPresetListView.getHeight() != getPresetLayoutHeight())) {
            this.mOrientation = orientation;
            this.mIsMultiWindowMode = isMultiWindowMode;
            this.mPresetListView.getViewTreeObserver().addOnGlobalLayoutListener(new C08196());
            return;
        }
        this.mOrientation = orientation;
        this.mIsMultiWindowMode = isMultiWindowMode;
        setListViewPadding();
    }

    private boolean setListViewPadding() {
        if (this.mPresetAdapter == null) {
            return false;
        }
        int topBottomPadding;
        int presetHeight = getResources().getDimensionPixelSize(C0728R.dimen.timer_common_preset_item_height);
        if (this.mOrientation != 1 && !StateUtils.isScreenDp(this.mContext, 512) && !this.mIsMultiWindowMode) {
            Point display = new Point();
            ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getRealSize(display);
            if (this.mPresetListView.getHeight() >= display.y || this.mPresetListView.getHeight() <= 0) {
                return false;
            }
            topBottomPadding = Math.max(0, (this.mPresetListView.getHeight() - presetHeight) / 2);
        } else if (this.mPresetListView.getHeight() <= 0) {
            return false;
        } else {
            topBottomPadding = Math.max(0, (this.mPresetListView.getHeight() - presetHeight) / 2);
        }
        int leftRightPadding = getLeftRightPadding();
        this.mPresetListView.setPadding(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding);
        return true;
    }

    private int getLeftRightPadding() {
        int count = this.mPresetAdapter.getItemCount();
        int presetLayoutWidth = (this.mOrientation == 1 || StateUtils.isScreenDp(this.mContext, 512) || this.mIsMultiWindowMode) ? getResources().getDisplayMetrics().widthPixels : (int) (((float) getResources().getDisplayMetrics().widthPixels) * 0.493f);
        int presetWidth = getResources().getDimensionPixelSize(C0728R.dimen.timer_common_preset_item_width);
        int maxVisibleCount = presetLayoutWidth / presetWidth;
        if (count <= maxVisibleCount || this.mIsMultiWindowMode) {
            return Math.max(0, (presetLayoutWidth - (count * presetWidth)) / 2);
        }
        return Math.max(0, (presetLayoutWidth - (maxVisibleCount * presetWidth)) / 2);
    }

    public void setEnabledListView(boolean enabled) {
        this.mPresetAdapter.setEnabled(enabled);
        View parent = (View) this.mPresetListView.getParent();
        if (enabled) {
            this.mPresetListView.setFocusable(true);
            if (this.mHasFocus) {
                this.mPresetListView.requestFocus();
                parent.setFocusable(false);
                this.mHasFocus = false;
            }
        } else {
            if (this.mPresetListView.hasFocus()) {
                this.mHasFocus = true;
                parent.setFocusable(true);
                parent.requestFocus();
            }
            this.mPresetListView.setFocusable(false);
        }
        this.mPresetListView.setAlpha(enabled ? 1.0f : 0.4f);
    }

    public void saveActionMode(Bundle outState) {
        outState.putBoolean("timer_action_mode", this.mPresetViewActionMode.isActionMode());
        if (this.mPresetViewActionMode.isActionMode()) {
            getCheckedPresetList();
            if (this.mCheckedPositions != null && this.mCheckedPositions.size() > 0) {
                outState.putIntegerArrayList("timer_action_mode_checked_item", (ArrayList) this.mCheckedPositions.clone());
            }
            clearCheckedPresetList();
        }
    }

    public void restoreActionMode(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean("timer_action_mode", false)) {
            startActionMode(false);
            setCheckedPreset(savedInstanceState.getIntegerArrayList("timer_action_mode_checked_item"));
        }
    }

    public boolean isActionMode() {
        return this.mPresetViewActionMode != null && this.mPresetViewActionMode.isActionMode();
    }

    private void startActionMode(boolean checked) {
        if (this.mPresetViewActionMode != null && this.mPresetListView != null && !this.mPresetViewActionMode.isActionMode() && this.mContext != null) {
            ((AppCompatActivity) this.mContext).startSupportActionMode(this.mPresetViewActionMode);
            if (!checked) {
                return;
            }
            if (getPresetCount() == 1) {
                this.mPresetAdapter.toggleCheck(0);
                this.mPresetViewActionMode.updateSelectModeActionBar();
            } else if (getSelectedPresetId() != -1 && getPresetPosition(getSelectedPresetId()) != -1) {
                this.mPresetAdapter.toggleCheck(getPresetPosition(getSelectedPresetId()));
                this.mPresetViewActionMode.updateSelectModeActionBar();
            }
        }
    }

    public void finishActionMode() {
        if (this.mPresetViewActionMode != null && this.mPresetViewActionMode.isActionMode()) {
            this.mPresetViewActionMode.finishActionMode();
        }
    }

    public void editPreset() {
        if (this.mTimerPresetViewListener.isEditMode()) {
            this.mTimerPresetViewListener.onDisablePickerEditMode();
            new Handler().postDelayed(new C08207(), 500);
        } else {
            startActionMode(true);
        }
        ClockUtils.insertSaLog("130", "1135");
    }

    private void modifyPreset() {
        getCheckedPresetList();
        if (this.mTimerPresetViewListener != null && this.mCheckedIds.size() == 1) {
            this.mTimerPresetViewListener.onCreateModifyPresetPopup(((Long) this.mCheckedIds.get(0)).longValue());
        }
        ClockUtils.insertSaLog("137", "1343");
    }

    private void deletePreset() {
        getCheckedPresetList();
        if (this.mPresetListView.getHeight() == 0 || !isDeleteAnimationStart()) {
            asyncDeletePreset();
        } else {
            startDeleteAnimation();
        }
        ClockUtils.insertSaLog("137", "1342");
    }

    public void deletePreset(int position) {
        this.mCheckedIds = new ArrayList();
        this.mCheckedPositions = new ArrayList();
        this.mCheckedPositions.add(Integer.valueOf(position));
        this.mCheckedIds.add(Long.valueOf(this.mPresetAdapter.getItemId(position)));
        if (this.mPresetListView.getHeight() == 0 || !isDeleteAnimationStart()) {
            asyncDeletePreset();
        } else {
            startDeleteAnimation();
        }
        ClockUtils.insertSaLog("130", "1342");
    }

    boolean isDeleteAnimationStart() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.mPresetListView.getLayoutManager();
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        for (int i = 0; i < this.mCheckedPositions.size(); i++) {
            int position = ((Integer) this.mCheckedPositions.get(i)).intValue();
            if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
                return true;
            }
        }
        return false;
    }

    void startDeleteAnimation() {
        this.mPresetListView.setItemAnimator(new C08228());
        Collections.sort(this.mCheckedPositions);
        for (int i = this.mCheckedPositions.size() - 1; i >= 0; i--) {
            this.mPresetAdapter.remove(((Integer) this.mCheckedPositions.get(i)).intValue());
        }
    }

    private void getCheckedPresetList() {
        SparseBooleanArray sp = this.mPresetAdapter.getCheckedItems();
        this.mCheckedIds = new ArrayList();
        this.mCheckedPositions = new ArrayList();
        for (int i = 0; i < sp.size(); i++) {
            if (sp.valueAt(i)) {
                int position = sp.keyAt(i);
                this.mCheckedPositions.add(Integer.valueOf(position));
                this.mCheckedIds.add(Long.valueOf(this.mPresetAdapter.getItemId(position)));
            }
        }
    }

    private void clearCheckedPresetList() {
        if (this.mCheckedIds != null) {
            this.mCheckedIds.clear();
        }
        if (this.mCheckedPositions != null) {
            this.mCheckedPositions.clear();
        }
    }

    private void setCheckedPreset(ArrayList<Integer> checkedPositions) {
        if (this.mPresetViewActionMode != null && this.mPresetViewActionMode.isActionMode() && checkedPositions != null && checkedPositions.size() > 0) {
            this.mPresetAdapter.setCheckedItems(checkedPositions);
            this.mPresetViewActionMode.updateSelectModeActionBar();
        }
    }

    private void asyncDeletePreset() {
        new C08239().execute(new Void[0]);
    }

    public int getPresetLayoutHeight() {
        Resources res = this.mContext.getResources();
        int buttonLayoutHeight = res.getDimensionPixelSize(((Activity) this.mContext).isInMultiWindowMode() ? C0728R.dimen.stopwatch_button_height : C0728R.dimen.stopwatch_button_layout_height);
        TypedValue biasValue = new TypedValue();
        getResources().getValue(C0728R.dimen.timer_common_picker_vertical_bias, biasValue, true);
        return Math.max((int) (((float) (((((getWindowHeight() - buttonLayoutHeight) - this.mTimerPresetViewListener.onGetPickerHeight()) - ClockUtils.getActionBarHeight(this.mContext)) - res.getDimensionPixelSize(C0728R.dimen.clock_tab_height)) - (Feature.isSupportTimerResetButton() ? res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_max_height) + res.getDimensionPixelSize(C0728R.dimen.timer_common_reset_button_margin_top) : 0))) * (StateUtils.isMobileKeyboard(this.mContext.getApplicationContext()) ? 0.9230769f : 1.0f - biasValue.getFloat())), res.getDimensionPixelSize(C0728R.dimen.timer_common_preset_list_min_height));
    }

    public int getWindowHeight() {
        Point size = new Point();
        boolean isMultiWindowMode = ((Activity) this.mContext).isInMultiWindowMode();
        if (isMultiWindowMode || StateUtils.isContextInDexMode(this.mContext) || StateUtils.isMobileKeyboard(this.mContext)) {
            ((Activity) this.mContext).getWindow().getWindowManager().getDefaultDisplay().getSize(size);
        } else {
            ((Activity) this.mContext).getWindow().getWindowManager().getDefaultDisplay().getRealSize(size);
        }
        int statusBarHeight = 0;
        int navigationBarHeight = 0;
        if (!isMultiWindowMode) {
            WindowInsets windowInsets = ((Activity) this.mContext).getWindow().getDecorView().getRootWindowInsets();
            if (windowInsets != null) {
                statusBarHeight = windowInsets.getSystemWindowInsetTop();
                navigationBarHeight = windowInsets.getSystemWindowInsetBottom();
            }
        }
        return (size.y - statusBarHeight) - navigationBarHeight;
    }
}
