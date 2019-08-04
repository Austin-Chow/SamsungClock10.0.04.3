package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.SeslLongPressMultiSelectionListener;
import android.support.v7.widget.RecyclerView.SeslOnMultiSelectedListener;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.callback.ActionModeListener;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockAddButton;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.WorldclockListViewActionModeListener;
import com.sec.android.app.clockpackage.worldclock.callback.WorldclockMainListListener;
import com.sec.android.app.clockpackage.worldclock.callback.WorldclockMainListViewModelListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.ListItem;
import com.sec.android.app.clockpackage.worldclock.model.Worldclock;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockMainListViewModelData;
import com.sec.android.app.clockpackage.worldclock.view.WorldclockMainListItemDecoration;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint({"ClickableViewAccessibility"})
public class WorldclockMainListViewModel {
    private boolean isEnterKeyLongPress = false;
    private final AppCompatActivity mActivity;
    private final WorldclockMainListAdapter mAdapter;
    private BindListAsyncTask mBindListAsyncTask;
    private BottomNavigationView mBottomNavigationView;
    private final ArrayList<WorldclockCityWeatherInfo> mCityWeatherInfo;
    private final Context mContext;
    private final ArrayList<Integer> mDeleteList = new ArrayList();
    private int mFirstItemClickedPosition;
    private int mFirstPosition = -1;
    private View mFragmentView = null;
    private boolean mIsMinimumSize;
    private boolean mIsMultiSelectStarted;
    private boolean mIsRecreate = false;
    private List<ListItem> mItems;
    private ArrayList<Integer> mListIds;
    private WorldclockMainListItemDecoration mListItemDecoration;
    private final WorldclockMainListViewModelData mListViewModelData = new WorldclockMainListViewModelData();
    private RecyclerView mRecyclerView = null;
    private ArrayList<Integer> mSelectedPosList = new ArrayList();
    private WorldclockListViewActionMode mWorldclockListViewActionMode;
    private final WorldclockListViewActionModeListener mWorldclockListViewActionModeListener = new C09104();
    private final WorldclockMainListListener mWorldclockMainListListener = new C09093();
    private final WorldclockMainListViewModelListener mWorldclockMainListViewModelListener;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockMainListViewModel$1 */
    class C09071 implements SeslLongPressMultiSelectionListener {
        private final Map<Integer, Boolean> mDragIndex = new HashMap();
        private boolean mNeedUpdateList = true;

        C09071() {
        }

        public void onItemSelected(RecyclerView mRecyclerView, View mView, int position, long id) {
            boolean z = true;
            try {
                ListItem item = (ListItem) WorldclockMainListViewModel.this.mItems.get(position);
                if (item != null && WorldclockMainListViewModel.this.mListViewModelData.isActionMode()) {
                    if (this.mDragIndex.containsKey(Integer.valueOf(position))) {
                        WorldclockListViewActionMode access$200 = WorldclockMainListViewModel.this.mWorldclockListViewActionMode;
                        if (item.getSelected()) {
                            z = false;
                        }
                        access$200.setCheckedState(position, z, false);
                    } else {
                        WorldclockMainListViewModel.this.mWorldclockListViewActionMode.setCheckedState(position, true, false);
                        this.mDragIndex.put(Integer.valueOf(position), Boolean.valueOf(true));
                    }
                    if (this.mNeedUpdateList) {
                        WorldclockMainListViewModel.this.mAdapter.notifyDataSetChanged();
                        this.mNeedUpdateList = false;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                Log.secE("WorldclockMainListViewModel", "seslSetLongPressMultiSelectionListener Exception : " + e.toString());
            }
        }

        public void onLongPressMultiSelectionStarted(int mI, int mI1) {
            this.mDragIndex.clear();
            this.mNeedUpdateList = true;
            WorldclockMainListViewModel.this.mIsMultiSelectStarted = true;
            if (!WorldclockMainListViewModel.this.mListViewModelData.isActionMode()) {
                ClockUtils.insertSaLog("110", "1297");
                WorldclockMainListViewModel.this.mActivity.startSupportActionMode(WorldclockMainListViewModel.this.mWorldclockListViewActionMode);
            }
        }

        public void onLongPressMultiSelectionEnded(int mI, int mI1) {
            WorldclockMainListViewModel.this.mIsMultiSelectStarted = false;
            this.mDragIndex.clear();
            WorldclockMainListViewModel.this.updateDeleteButton();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockMainListViewModel$2 */
    class C09082 implements SeslOnMultiSelectedListener {
        private int mSelectionStartPosition = 0;

        C09082() {
        }

        public void onMultiSelected(RecyclerView mRecyclerView, View mView, int mI, long mL) {
        }

        public void onMultiSelectStart(int startX, int startY) {
            this.mSelectionStartPosition = WorldclockMainListViewModel.this.mRecyclerView.getChildLayoutPosition(WorldclockMainListViewModel.this.mRecyclerView.findChildViewUnder((float) startX, (float) startY));
            if (this.mSelectionStartPosition == -1) {
                this.mSelectionStartPosition = WorldclockMainListViewModel.this.mRecyclerView.getChildLayoutPosition(WorldclockMainListViewModel.this.mRecyclerView.seslFindNearChildViewUnder((float) startX, (float) startY));
            }
        }

        public void onMultiSelectStop(int endX, int endY) {
            if (!WorldclockMainListViewModel.this.mListViewModelData.isActionMode() && WorldclockMainListViewModel.this.mAdapter.getItemCount() > 0) {
                WorldclockMainListViewModel.this.mActivity.startSupportActionMode(WorldclockMainListViewModel.this.mWorldclockListViewActionMode);
            }
            int selectionEndPosition = WorldclockMainListViewModel.this.mRecyclerView.getChildLayoutPosition(WorldclockMainListViewModel.this.mRecyclerView.findChildViewUnder((float) endX, (float) endY));
            if (selectionEndPosition == -1) {
                selectionEndPosition = WorldclockMainListViewModel.this.mRecyclerView.getChildLayoutPosition(WorldclockMainListViewModel.this.mRecyclerView.seslFindNearChildViewUnder((float) endX, (float) endY));
            }
            if (this.mSelectionStartPosition > selectionEndPosition) {
                int temp = this.mSelectionStartPosition;
                this.mSelectionStartPosition = selectionEndPosition;
                selectionEndPosition = temp;
            }
            int position = this.mSelectionStartPosition;
            while (position <= selectionEndPosition) {
                if (position > -1 && position < WorldclockMainListViewModel.this.mAdapter.getItemCount()) {
                    boolean z;
                    WorldclockViewHolder holder = (WorldclockViewHolder) WorldclockMainListViewModel.this.mRecyclerView.findViewHolderForAdapterPosition(position);
                    WorldclockMainListViewModel.this.mAdapter.initCheckBox(holder);
                    WorldclockMainListAdapter access$300 = WorldclockMainListViewModel.this.mAdapter;
                    if (((ListItem) WorldclockMainListViewModel.this.mItems.get(position)).getSelected()) {
                        z = false;
                    } else {
                        z = true;
                    }
                    access$300.setChecked(position, z, holder);
                }
                position++;
            }
            WorldclockMainListViewModel.this.mWorldclockListViewActionMode.updateActionModeActionBar();
            WorldclockMainListViewModel.this.updateDeleteButton();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockMainListViewModel$3 */
    class C09093 implements WorldclockMainListListener {
        C09093() {
        }

        public void onContextItemSelected(int position) {
            WorldclockMainListViewModel.this.contextItemSelected(position);
        }

        public boolean isActionMode() {
            return WorldclockMainListViewModel.this.mListViewModelData.isActionMode();
        }

        public void onDrop() {
            onDropEnd();
        }

        public int getCurrentPosition(View v) {
            return WorldclockMainListViewModel.this.mRecyclerView.getChildAdapterPosition(v);
        }

        public void onClick(View view, int position) {
            if (position != -1) {
                ListItem item = (ListItem) WorldclockMainListViewModel.this.mItems.get(position);
                if (item == null) {
                    return;
                }
                if (!WorldclockMainListViewModel.this.mListViewModelData.isActionMode() || WorldclockMainListViewModel.this.mListViewModelData.getActionMode() == null) {
                    Intent i = new Intent();
                    i.setClass(WorldclockMainListViewModel.this.mContext, WorldclockGlobeMain.class);
                    int listId = (WorldclockMainListViewModel.this.mListIds == null || WorldclockMainListViewModel.this.mListIds.get(position) == null) ? item.getId() : ((Integer) WorldclockMainListViewModel.this.mListIds.get(position)).intValue();
                    City city = CityManager.findCityByUniqueId(Integer.valueOf(item.getUniqueId()));
                    if (city != null) {
                        i.putExtra("where", "city");
                        i.putExtra("index", listId);
                        i.putExtra("uniqueid", city.getUniqueId());
                        i.putExtra("zoomlevel", city.getZoomLevel());
                        i.putExtra("ListPosition", position);
                        i.putParcelableArrayListExtra("WorldclockWeatherListInfoKey", WorldclockMainListViewModel.this.mCityWeatherInfo);
                        i.putExtra("NUMBER_OF_CITIES_IN_MENU", WorldclockDBManager.getDBCursorCnt(WorldclockMainListViewModel.this.mContext));
                        i.setFlags(603979776);
                        WorldclockMainListViewModel.this.mWorldclockMainListViewModelListener.onStartActivityForResult(i);
                        WorldclockMainListViewModel.this.mActivity.overridePendingTransition(C0836R.anim.worldclock_animation_fade_in, C0836R.anim.worldclock_animation_fade_out);
                    }
                    ClockUtils.insertSaLog("110", "1293");
                    return;
                }
                boolean z;
                WorldclockListViewActionMode access$200 = WorldclockMainListViewModel.this.mWorldclockListViewActionMode;
                if (item.getSelected()) {
                    z = false;
                } else {
                    z = true;
                }
                access$200.setCheckedState(position, z, false);
                WorldclockMainListViewModel.this.updateDeleteButton();
            }
        }

        public boolean onLongClick(View view, int position) {
            WorldclockMainListViewModel.this.mRecyclerView.seslStartLongPressMultiSelection();
            if (WorldclockMainListViewModel.this.mWorldclockListViewActionMode == null) {
                return false;
            }
            if (!WorldclockMainListViewModel.this.mListViewModelData.isActionMode()) {
                ClockUtils.insertSaLog("110", "1297");
                WorldclockMainListViewModel.this.mAdapter.initCheckBox((WorldclockViewHolder) WorldclockMainListViewModel.this.mRecyclerView.findViewHolderForAdapterPosition(position));
                WorldclockMainListViewModel.this.mActivity.startSupportActionMode(WorldclockMainListViewModel.this.mWorldclockListViewActionMode);
            }
            WorldclockMainListViewModel.this.mFirstItemClickedPosition = position;
            WorldclockMainListViewModel.this.mWorldclockListViewActionMode.setCheckedState(position, true, false);
            return true;
        }

        private void updateReorderDb(ArrayList<ListItem> items) {
            if (!WorldclockDBManager.deleteDB(WorldclockMainListViewModel.this.mContext)) {
                WorldclockUtils.showNotEnoughSpaceToast(WorldclockMainListViewModel.this.mActivity);
            } else if (!WorldclockDBManager.saveDB(WorldclockMainListViewModel.this.mContext, (ArrayList) items)) {
                WorldclockUtils.showNotEnoughSpaceToast(WorldclockMainListViewModel.this.mActivity);
            }
            Cursor dbBCursor = WorldclockDBManager.getDBAll(WorldclockMainListViewModel.this.mContext);
            WorldclockMainListViewModel.this.mListIds.clear();
            if (dbBCursor != null) {
                dbBCursor.moveToLast();
                while (!dbBCursor.isBeforeFirst()) {
                    WorldclockMainListViewModel.this.mListIds.add(Integer.valueOf(dbBCursor.getInt(0)));
                    dbBCursor.moveToPrevious();
                }
                dbBCursor.close();
            }
        }

        public void requestFocusToSelectAll() {
            if (WorldclockMainListViewModel.this.mListViewModelData.getSelectAllLayout() != null) {
                WorldclockMainListViewModel.this.mListViewModelData.getSelectAllLayout().semRequestAccessibilityFocus();
            }
        }

        private void onDropEnd() {
            if (WorldclockMainListViewModel.this.mAdapter != null && WorldclockMainListViewModel.this.mItems != null) {
                if (WorldclockMainListViewModel.this.mItems.size() == WorldclockMainListViewModel.this.mAdapter.mCityItems.size()) {
                    WorldclockMainListViewModel.this.mItems = WorldclockMainListViewModel.this.mAdapter.mCityItems;
                }
                ArrayList<ListItem> arrayDataSort = new ArrayList(WorldclockMainListViewModel.this.mItems);
                Collections.reverse(arrayDataSort);
                updateReorderDb(arrayDataSort);
                arrayDataSort.clear();
                ClockUtils.insertSaLog("111", "1294");
            }
        }

        public void onEnterKeyEvent(View v, int keyCode, KeyEvent event) {
            if (event.isLongPress()) {
                WorldclockMainListViewModel.this.isEnterKeyLongPress = true;
            }
            if (WorldclockMainListViewModel.this.isEnterKeyLongPress && event.getAction() == 1) {
                WorldclockMainListViewModel.this.updateDeleteButton();
                WorldclockMainListViewModel.this.isEnterKeyLongPress = false;
            }
        }

        public boolean isMinimumSize() {
            return WorldclockMainListViewModel.this.mIsMinimumSize;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockMainListViewModel$4 */
    class C09104 implements WorldclockListViewActionModeListener {
        C09104() {
        }

        public void onUpdateDeleteButton() {
            WorldclockMainListViewModel.this.updateDeleteButton();
        }

        public void onActionModeUpdate(int selectCount) {
            WorldclockMainListViewModel.this.mWorldclockMainListViewModelListener.onActionModeUpdate(selectCount);
        }

        public void onActionModeFinished() {
            WorldclockMainListViewModel.this.mWorldclockMainListViewModelListener.onActionModeFinished();
        }

        public Toolbar onGetToolbar() {
            return WorldclockMainListViewModel.this.mWorldclockMainListViewModelListener.onGetToolbar();
        }

        public boolean canNotCheckSelectAll() {
            return (!WorldclockMainListViewModel.this.mIsMultiSelectStarted || WorldclockMainListViewModel.this.mFirstItemClickedPosition == 0 || WorldclockMainListViewModel.this.mFirstItemClickedPosition == WorldclockMainListViewModel.this.mAdapter.getItemCount() - 1) ? false : true;
        }
    }

    private static class BindListAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<WorldclockMainListViewModel> mParent;

        BindListAsyncTask(WorldclockMainListViewModel context) {
            this.mParent = new WeakReference(context);
        }

        protected void onPreExecute() {
            WorldclockMainListViewModel ref = (WorldclockMainListViewModel) this.mParent.get();
            if (ref != null) {
                ref.initList();
            }
        }

        protected Void doInBackground(Void... voids) {
            WorldclockMainListViewModel ref = (WorldclockMainListViewModel) this.mParent.get();
            if (ref != null) {
                ref.makeListItem();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            WorldclockMainListViewModel ref = (WorldclockMainListViewModel) this.mParent.get();
            if (ref != null) {
                ref.bindList();
            }
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<WorldclockMainListViewModel> mParent;

        DeleteAsyncTask(WorldclockMainListViewModel context) {
            this.mParent = new WeakReference(context);
        }

        protected Void doInBackground(Void... voids) {
            WorldclockMainListViewModel ref = (WorldclockMainListViewModel) this.mParent.get();
            if (ref != null) {
                ref.deleteBackground();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            WorldclockMainListViewModel ref = (WorldclockMainListViewModel) this.mParent.get();
            if (ref != null) {
                ref.deletePostExecute();
            }
        }
    }

    WorldclockMainListViewModel(AppBarLayout appBarLayout, AppCompatActivity activity, WorldclockMainListViewModelListener worldclockMainListViewModelListener, ArrayList<WorldclockCityWeatherInfo> weatherInfo) {
        this.mActivity = activity;
        this.mContext = this.mActivity.getApplicationContext();
        this.mWorldclockMainListViewModelListener = worldclockMainListViewModelListener;
        this.mCityWeatherInfo = weatherInfo;
        this.mAdapter = new WorldclockMainListAdapter(appBarLayout, this.mActivity, this.mWorldclockMainListListener);
        this.mBindListAsyncTask = new BindListAsyncTask(this);
        this.mListItemDecoration = new WorldclockMainListItemDecoration(this.mActivity);
    }

    void worldclockRestore() {
        WorldclockDBManager.updateDBLocale(this.mActivity.getApplicationContext());
        WorldclockDBManager.updateCityChoice(this.mActivity.getApplicationContext());
        updateList(false);
        refreshWeatherInfo();
        CityManager.setIsCityRestored(false);
    }

    void makeListItem() {
        Cursor dbCursor = WorldclockDBManager.getDBAll(this.mActivity.getApplicationContext());
        this.mItems = new ArrayList();
        this.mListIds = new ArrayList();
        if (dbCursor != null) {
            dbCursor.moveToLast();
            int count = 0;
            while (!dbCursor.isBeforeFirst() && count < 20) {
                ListItem item = new ListItem(dbCursor.getInt(0), dbCursor.getString(1), dbCursor.getString(2), dbCursor.getInt(4), (float) dbCursor.getInt(5), (float) dbCursor.getInt(6));
                City city = CityManager.findCityByUniqueId(Integer.valueOf(item.getUniqueId()));
                if (city != null) {
                    item.setPlaceId(city.getCityPlaceId());
                }
                this.mItems.add(item);
                this.mListIds.add(Integer.valueOf(item.getId()));
                dbCursor.moveToPrevious();
                count++;
            }
            dbCursor.close();
        }
    }

    void updateList(boolean needAsyncDb) {
        this.mIsRecreate = !needAsyncDb;
        Log.secD("WorldclockMainListViewModel", "updateList needAsyncDb : " + needAsyncDb);
        if (!needAsyncDb || this.mBindListAsyncTask == null) {
            if (this.mBindListAsyncTask != null && this.mBindListAsyncTask.getStatus() == Status.RUNNING) {
                this.mBindListAsyncTask.cancel(true);
            }
            if (this.mActivity != null) {
                initList();
                makeListItem();
                bindList();
                return;
            }
            return;
        }
        this.mBindListAsyncTask.execute(new Void[0]);
    }

    void scrollToFirst() {
        this.mRecyclerView.scrollToPosition(0);
    }

    void initList() {
        this.mIsMinimumSize = !StateUtils.isScreenDp(this.mActivity, 281);
        this.mFragmentView = this.mWorldclockMainListViewModelListener.onGetFragmentView();
        if (this.mFragmentView != null) {
            this.mRecyclerView = (RecyclerView) this.mFragmentView.findViewById(C0836R.id.worldclock_list);
            this.mAdapter.setRecyclerView(this.mRecyclerView);
            try {
                this.mRecyclerView.semSetRoundedCorners(3);
                this.mRecyclerView.semSetRoundedCornerColor(3, this.mActivity.getColor(C0836R.color.window_background_color));
            } catch (NoSuchMethodError e) {
                Log.secE("WorldclockMainListViewModel", "NoSuchMethodError : " + e.toString());
            }
            setWorldClockListWidth(new Configuration(this.mActivity.getResources().getConfiguration()));
            this.mRecyclerView.setItemViewCacheSize(20);
            this.mRecyclerView.setAdapter(this.mAdapter);
            this.mRecyclerView.setHasFixedSize(true);
            this.mRecyclerView.seslSetGoToTopEnabled(true);
            this.mRecyclerView.seslSetOutlineStrokeEnabled(false);
            this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            addItemDecoration();
            setLayoutManager();
            if (this.mFirstPosition != -1) {
                this.mRecyclerView.scrollToPosition(this.mFirstPosition);
                this.mFirstPosition = -1;
            }
            this.mAdapter.getItemTouchHelper().attachToRecyclerView(this.mRecyclerView);
            this.mRecyclerView.seslSetLongPressMultiSelectionListener(new C09071());
            this.mRecyclerView.seslSetOnMultiSelectedListener(new C09082());
        }
    }

    void setListWeatherInfo(Intent data) {
        ArrayList<WorldclockCityWeatherInfo> info = data.getParcelableArrayListExtra("WorldclockWeatherListInfoKey");
        if (info != null && !info.isEmpty()) {
            this.mCityWeatherInfo.add(0, info.get(0));
        }
    }

    void updateListWeatherInfo(Intent data) {
        int position = data.getIntExtra("ListPosition", -1);
        if (position != -1) {
            ArrayList<WorldclockCityWeatherInfo> info = data.getParcelableArrayListExtra("WorldclockWeatherListInfoKey");
            if (info != null && position < this.mCityWeatherInfo.size() && position < info.size() && !info.isEmpty()) {
                this.mCityWeatherInfo.remove(position);
                this.mCityWeatherInfo.add(position, info.get(position));
            }
        }
    }

    void bindList() {
        this.mAdapter.updateListItem(this.mItems);
        this.mAdapter.updateWeatherListItem(this.mCityWeatherInfo);
        this.mWorldclockListViewActionMode = new WorldclockListViewActionMode(this.mListViewModelData, this.mActivity, this.mAdapter, this.mRecyclerView, this.mWorldclockListViewActionModeListener);
        if (!this.mIsRecreate) {
            this.mWorldclockMainListViewModelListener.onLaunchSmartTip();
        }
        onUpdateEmptyView();
        this.mAdapter.notifyDataSetChanged();
        this.mActivity.invalidateOptionsMenu();
    }

    void startDeleteAnimation() {
        int selectedPositions = this.mSelectedPosList.size();
        Collections.sort(this.mSelectedPosList);
        this.mRecyclerView.getItemAnimator().isRunning(WorldclockMainListViewModel$$Lambda$1.lambdaFactory$(this));
        if (selectedPositions == this.mItems.size()) {
            this.mAdapter.notifyItemRangeRemoved(0, this.mAdapter.getItemCount());
            this.mItems.clear();
            this.mListIds.clear();
            if (this.mCityWeatherInfo != null) {
                this.mCityWeatherInfo.clear();
            }
        } else {
            for (int i = selectedPositions - 1; i >= 0 && !this.mItems.isEmpty(); i--) {
                int adjustedPosition = ((Integer) this.mSelectedPosList.get(i)).intValue();
                if (adjustedPosition < this.mItems.size()) {
                    this.mItems.remove(adjustedPosition);
                    this.mAdapter.notifyItemRemoved(adjustedPosition);
                    this.mListIds.remove(adjustedPosition);
                    if (this.mCityWeatherInfo.size() > adjustedPosition) {
                        this.mCityWeatherInfo.remove(adjustedPosition);
                    }
                }
            }
        }
        this.mSelectedPosList.clear();
        addItemDecoration();
    }

    private /* synthetic */ void lambda$startDeleteAnimation$1() {
        this.mRecyclerView.postDelayed(WorldclockMainListViewModel$$Lambda$5.lambdaFactory$(this), this.mRecyclerView.getItemAnimator().getMoveDuration() + this.mRecyclerView.getItemAnimator().getRemoveDuration());
    }

    private /* synthetic */ void lambda$null$0() {
        asyncDeleteCity();
    }

    void finishActionMode() {
        if (this.mListViewModelData.getActionMode() != null) {
            this.mListViewModelData.getActionMode().finish();
        }
    }

    private void asyncDeleteCity() {
        new DeleteAsyncTask(this).execute(new Void[0]);
    }

    private void deleteBackground() {
        int count = this.mDeleteList.size();
        if (count > 0) {
            StringBuilder where = new StringBuilder(Worldclock.WC_COLUMNS[4] + " IN ( ");
            String[] selectionArgs = new String[count];
            int i = count - 1;
            while (i >= 0) {
                where.append(i > 0 ? "?," : "?)");
                selectionArgs[i] = this.mDeleteList.get(i) + "";
                i--;
            }
            WorldclockDBManager.deleteDB(this.mContext, where.toString(), selectionArgs);
            this.mDeleteList.clear();
        }
    }

    private void deletePostExecute() {
        WorldclockDBManager.updateDBLocale(this.mContext);
        WorldclockDBManager.updateCityChoice(this.mContext);
        if (this.mListViewModelData.getActionMode() != null) {
            this.mListViewModelData.getActionMode().finish();
        }
        onUpdateEmptyView();
    }

    private void onUpdateEmptyView() {
        ClockAddButton addButton = (ClockAddButton) this.mFragmentView.findViewById(C0836R.id.worldclock_empty_view);
        if (this.mAdapter.getItemCount() == 0) {
            addButton.setVisibility(0);
            setFocusable(false);
            this.mRecyclerView.setImportantForAccessibility(2);
            addButton.setOnTouchListener(WorldclockMainListViewModel$$Lambda$2.lambdaFactory$(this));
            return;
        }
        setFocusable(true);
        addButton.setVisibility(8);
        this.mRecyclerView.setImportantForAccessibility(1);
    }

    private /* synthetic */ boolean lambda$onUpdateEmptyView$2(View v, MotionEvent event) {
        if (this.mRecyclerView != null) {
            this.mRecyclerView.dispatchTouchEvent(event);
        }
        return false;
    }

    void recreateStartActionMode(Bundle savedInstanceState) {
        if (savedInstanceState != null && this.mWorldclockListViewActionMode != null) {
            if (this.mSelectedPosList == null) {
                this.mSelectedPosList = new ArrayList();
            }
            ArrayList<Integer> selectedPosition = savedInstanceState.getIntegerArrayList("worldclock_checked_item_position");
            if (!(selectedPosition == null || selectedPosition.isEmpty())) {
                this.mSelectedPosList.addAll(selectedPosition);
            }
            if (this.mAdapter != null) {
                for (int index = 0; index < this.mSelectedPosList.size(); index++) {
                    this.mAdapter.toggleCheckBox(((Integer) this.mSelectedPosList.get(index)).intValue());
                }
            }
            this.mActivity.startSupportActionMode(this.mWorldclockListViewActionMode);
            if (!this.mSelectedPosList.isEmpty() && (this.mActivity instanceof ActionModeListener)) {
                ((ActionModeListener) this.mActivity).onShowBottomNavigationView();
            }
        }
    }

    void launchWeatherInfo() {
        if (this.mAdapter != null && this.mAdapter.getItemCount() > 0) {
            WorldclockWeatherUtils.showNetworkUnavailableToast(this.mActivity);
            if (!WorldclockWeatherUtils.isDisableWeather(this.mContext)) {
                this.mAdapter.setWeatherState(0);
                this.mAdapter.sendWeatherHandler(-1);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    void refreshWeatherInfo() {
        if (this.mAdapter != null) {
            if (WorldclockWeatherUtils.isDisableWeather(this.mContext) || this.mAdapter.getItemCount() == 0) {
                this.mAdapter.resetWeatherInfoList(-1);
            } else {
                this.mAdapter.resetWeatherInfoList(0);
                this.mAdapter.sendWeatherHandler(-1);
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    void refreshCityList() {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    void refreshCityTimeDiffInfo() {
        if (this.mAdapter != null && this.mItems != null) {
            int childCount = this.mAdapter.getItemCount();
            for (int i = 0; i < childCount; i++) {
                this.mAdapter.refreshCityTimeDiffInfo((WorldclockViewHolder) this.mRecyclerView.findViewHolderForAdapterPosition(i), i);
                this.mAdapter.setDescription((WorldclockViewHolder) this.mRecyclerView.findViewHolderForAdapterPosition(i), i);
            }
        }
    }

    private /* synthetic */ void lambda$refreshCityListDelayed$3() {
        refreshCityList();
    }

    void refreshCityListDelayed(long delayTime) {
        new Handler().postDelayed(WorldclockMainListViewModel$$Lambda$3.lambdaFactory$(this), delayTime);
    }

    WorldclockMainListAdapter getAdapter() {
        return this.mAdapter;
    }

    public RecyclerView getList() {
        return this.mRecyclerView;
    }

    public boolean isActionMode() {
        return this.mListViewModelData.isActionMode();
    }

    WorldclockListViewActionMode getWorldclockListViewActionMode() {
        return this.mWorldclockListViewActionMode;
    }

    boolean isCheckedActionMode() {
        return this.mListViewModelData.isCheckedActionMode();
    }

    void setCheckedActionMode(boolean checkedActionMode) {
        this.mListViewModelData.setCheckedActionMode(checkedActionMode);
    }

    ArrayList<Integer> getSelectedPosList() {
        return this.mSelectedPosList;
    }

    ViewGroup getSelectAllLayout() {
        return this.mListViewModelData.getSelectAllLayout();
    }

    CheckBox getSelectAllCheckBox() {
        return this.mListViewModelData.getSelectAllCheckBox();
    }

    View getMultiSelectModeTitle() {
        return this.mListViewModelData.getMultiSelectModeTitle();
    }

    TextView getSelectItemText() {
        return this.mListViewModelData.getSelectItemText();
    }

    ArrayList<Integer> getDeleteList() {
        return this.mDeleteList;
    }

    public List<ListItem> getItems() {
        return this.mItems;
    }

    void doActionMode() {
        if (this.mWorldclockListViewActionMode != null) {
            this.mActivity.startSupportActionMode(this.mWorldclockListViewActionMode);
        }
        if (this.mAdapter != null && this.mWorldclockListViewActionMode != null && this.mListViewModelData.getSelectAllCheckBox() != null && this.mRecyclerView != null && this.mAdapter.getItemCount() == 1) {
            this.mAdapter.initCheckBox((WorldclockViewHolder) this.mRecyclerView.findViewHolderForAdapterPosition(0));
            this.mListViewModelData.getSelectAllCheckBox().setChecked(true);
            this.mWorldclockListViewActionMode.setCheckedState(0, true, false);
            updateDeleteButton();
        }
    }

    private void contextItemSelected(int index) {
        this.mDeleteList.add(Integer.valueOf(((ListItem) this.mItems.get(index)).getUniqueId()));
        this.mSelectedPosList.add(Integer.valueOf(index));
        if (this.mCityWeatherInfo.size() > index) {
            this.mCityWeatherInfo.remove(index);
        }
        startDeleteAnimation();
    }

    public void releaseInstance() {
        if (this.mBindListAsyncTask.getStatus() == Status.RUNNING) {
            this.mBindListAsyncTask.cancel(true);
        }
        this.mBindListAsyncTask = null;
        if (this.mItems != null) {
            this.mItems.clear();
            this.mItems = null;
        }
        if (this.mListIds != null) {
            this.mListIds.clear();
            this.mListIds = null;
        }
        finishActionMode();
        this.mWorldclockListViewActionMode = null;
        this.mListItemDecoration = null;
        if (this.mRecyclerView != null) {
            this.mRecyclerView.setItemAnimator(null);
            this.mRecyclerView.addOnItemTouchListener(null);
        }
    }

    public void setWorldClockListWidth(Configuration newConfig) {
        if (this.mActivity == null) {
            Log.secE("WorldclockMainListViewModel", "setWorldClockListWidth() mActivity is null");
            return;
        }
        boolean isTabletLandScapeList = newConfig.orientation == 2 && StateUtils.isScreenDp(this.mActivity, 600);
        LayoutParams param = (LayoutParams) this.mRecyclerView.getLayoutParams();
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

    public void initBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.mBottomNavigationView = bottomNavigationView;
        updateDeleteButton();
        this.mBottomNavigationView.setOnNavigationItemSelectedListener(WorldclockMainListViewModel$$Lambda$4.lambdaFactory$(this));
    }

    private /* synthetic */ boolean lambda$initBottomNavigationView$4(MenuItem menuItem) {
        if (menuItem.getItemId() == C0836R.id.delete) {
            this.mSelectedPosList.clear();
            for (int i = this.mAdapter.getItemCount() - 1; i >= 0; i--) {
                ListItem selectedItem = (ListItem) this.mItems.get(i);
                if (selectedItem != null && selectedItem.getSelected()) {
                    this.mDeleteList.add(Integer.valueOf(selectedItem.getUniqueId()));
                    this.mSelectedPosList.add(Integer.valueOf(i));
                }
            }
            startDeleteAnimation();
            ClockUtils.insertSaLog("111", "1262");
        }
        return false;
    }

    private void updateDeleteButton() {
        if (!(this.mActivity instanceof ActionModeListener)) {
            return;
        }
        if (this.mAdapter.getCheckedCount() > 0) {
            ((ActionModeListener) this.mActivity).onShowBottomNavigationView();
        } else {
            ((ActionModeListener) this.mActivity).onHideBottomNavigationView();
        }
    }

    public void removeBottomNavigationView() {
        if (this.mBottomNavigationView != null) {
            this.mBottomNavigationView.setOnNavigationItemSelectedListener(null);
            this.mBottomNavigationView = null;
        }
    }

    void setFocusable(boolean focusable) {
        RecyclerView recyclerView = this.mRecyclerView;
        boolean z = this.mAdapter.getItemCount() > 0 && focusable;
        recyclerView.setFocusable(z);
    }

    private void addItemDecoration() {
        if (this.mRecyclerView.getItemDecorationCount() > 0) {
            this.mRecyclerView.removeItemDecoration(this.mListItemDecoration);
        }
        this.mRecyclerView.addItemDecoration(this.mListItemDecoration);
    }

    private void setLayoutManager() {
        if (!StateUtils.isScreenDp(this.mActivity, 512) || StateUtils.isInMultiwindow()) {
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mActivity));
        } else {
            this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.mActivity, 2));
        }
    }

    boolean isGridLayout() {
        return this.mRecyclerView.getLayoutManager() instanceof GridLayoutManager;
    }

    void setFirstVisibileItemPosition() {
        if (this.mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            this.mFirstPosition = ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } else {
            this.mFirstPosition = ((LinearLayoutManager) this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }
        Log.secD("WorldclockMainListViewModel", "setFirstVisibileItemPosition mFirstPosition : " + this.mFirstPosition);
    }
}
