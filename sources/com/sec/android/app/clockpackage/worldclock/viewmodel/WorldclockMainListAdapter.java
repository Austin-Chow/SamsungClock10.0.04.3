package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import com.samsung.android.view.animation.SineInOut33;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.common.callback.DndHelper;
import com.sec.android.app.clockpackage.common.callback.DndHelper.ItemListener;
import com.sec.android.app.clockpackage.common.callback.OnSingleClickListener;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.WeatherHandlerListener;
import com.sec.android.app.clockpackage.worldclock.callback.WorldclockMainListListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.ListItem;
import com.sec.android.app.clockpackage.worldclock.model.TimeZoneFinder;
import com.sec.android.app.clockpackage.worldclock.model.Worldclock;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherHandler;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherInfoView;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherUrlManager;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

@SuppressLint({"ClickableViewAccessibility"})
public class WorldclockMainListAdapter extends Adapter<WorldclockViewHolder> implements ItemListener {
    private final AccessibilityDelegate mAccessibilityDelegate = new C09063();
    private AppBarLayout mAppBarLayout;
    List<ListItem> mCityItems = new ArrayList();
    private ArrayList<WorldclockCityWeatherInfo> mCityWeatherInfo = new ArrayList();
    private final Context mContext;
    private DndHelper mDndHelper = new DndHelper(this);
    private final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(this.mDndHelper);
    private boolean mNeedSave = false;
    private RecyclerView mRecyclerView;
    private TimeZoneFinder mTimeZoneFinder;
    private final TransitionSet mTransitionSet = new TransitionSet();
    private WeatherHandler mWeatherHandler;
    private final WorldclockMainListListener mWorldclockMainListListener;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockMainListAdapter$1 */
    class C09041 implements WeatherHandlerListener {
        C09041() {
        }

        public void onTimeOut(int cityId, int position) {
            Log.secD("WorldclockMainListAdapter", "onTimeOut");
            WorldclockMainListAdapter.this.mWeatherHandler.stopThreadAndMessage();
            if (position == -1) {
                if (!WorldclockMainListAdapter.this.mCityWeatherInfo.isEmpty()) {
                    WorldclockMainListAdapter.this.mCityWeatherInfo.clear();
                }
                int count = WorldclockMainListAdapter.this.getItemCount();
                for (int i = 0; i < count; i++) {
                    WorldclockMainListAdapter.this.mCityWeatherInfo.add(new WorldclockCityWeatherInfo());
                    ((WorldclockCityWeatherInfo) WorldclockMainListAdapter.this.mCityWeatherInfo.get(i)).setCurrentState(1);
                }
            } else if (WorldclockMainListAdapter.this.mCityWeatherInfo.size() > position) {
                ((WorldclockCityWeatherInfo) WorldclockMainListAdapter.this.mCityWeatherInfo.get(position)).setCurrentState(1);
            }
            WorldclockMainListAdapter.this.setWeatherInfoList();
            WorldclockMainListAdapter.this.notifyDataSetChanged();
        }

        public void onSaveData(Object obj, int position) {
            Log.secD("WorldclockMainListAdapter", "onSaveData");
            ArrayList<WorldclockCityWeatherInfo> weatherInfoList = (ArrayList) obj;
            if (position == -1) {
                WorldclockMainListAdapter.this.mCityWeatherInfo.clear();
                WorldclockMainListAdapter.this.mCityWeatherInfo.addAll(weatherInfoList);
            } else {
                WorldclockMainListAdapter.this.mCityWeatherInfo.set(position, weatherInfoList.get(0));
            }
            WorldclockMainListAdapter.this.mWeatherHandler.stopThreadAndMessage();
            WorldclockMainListAdapter.this.setWeatherInfoList();
            WorldclockMainListAdapter.this.notifyDataSetChanged();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockMainListAdapter$3 */
    class C09063 extends AccessibilityDelegate {
        C09063() {
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.addAction(new AccessibilityAction(C0836R.string.reorder_move_up, WorldclockMainListAdapter.this.mContext.getResources().getString(C0836R.string.reorder_move_up)));
            info.addAction(new AccessibilityAction(C0836R.string.reorder_move_to_top, WorldclockMainListAdapter.this.mContext.getResources().getString(C0836R.string.reorder_move_to_top)));
            info.addAction(new AccessibilityAction(C0836R.string.reorder_move_down, WorldclockMainListAdapter.this.mContext.getResources().getString(C0836R.string.reorder_move_down)));
            info.addAction(new AccessibilityAction(C0836R.string.reorder_move_to_bottom, WorldclockMainListAdapter.this.mContext.getResources().getString(C0836R.string.reorder_move_to_bottom)));
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            int from = WorldclockMainListAdapter.this.mWorldclockMainListListener.getCurrentPosition(host);
            int to = -1;
            boolean isReorder = false;
            if (action == C0836R.string.reorder_move_up) {
                to = from - 1;
                isReorder = true;
            } else if (action == C0836R.string.reorder_move_to_top) {
                to = 0;
                isReorder = true;
            } else if (action == C0836R.string.reorder_move_down) {
                to = from + 1;
                isReorder = true;
            } else if (action == C0836R.string.reorder_move_to_bottom) {
                to = WorldclockMainListAdapter.this.getItemCount() - 1;
                isReorder = true;
            }
            if (!isReorder) {
                return super.performAccessibilityAction(host, action, args);
            }
            WorldclockMainListAdapter.this.onItemMoved(from, to);
            WorldclockMainListAdapter.this.mWorldclockMainListListener.onDrop();
            WorldclockMainListAdapter.this.mWorldclockMainListListener.requestFocusToSelectAll();
            return true;
        }
    }

    WorldclockMainListAdapter(AppBarLayout appBarLayout, Context context, WorldclockMainListListener worldclockMainListListener) {
        this.mContext = context;
        this.mAppBarLayout = appBarLayout;
        this.mWorldclockMainListListener = worldclockMainListListener;
        setAnimationValue();
        this.mTimeZoneFinder = new TimeZoneFinder(context);
        this.mTimeZoneFinder.updateCitiesIdFromIso();
    }

    protected void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public WorldclockViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (this.mWorldclockMainListListener.isMinimumSize()) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(C0836R.layout.worldclock_list_item_layout_min, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(C0836R.layout.worldclock_list_item_layout, viewGroup, false);
        }
        WorldclockViewHolder holder = new WorldclockViewHolder(itemView);
        holder.itemView.setOnClickListener(WorldclockMainListAdapter$$Lambda$1.lambdaFactory$(this, holder));
        holder.itemView.setOnLongClickListener(WorldclockMainListAdapter$$Lambda$2.lambdaFactory$(this, holder));
        holder.itemView.setOnKeyListener(WorldclockMainListAdapter$$Lambda$3.lambdaFactory$(this));
        if (StateUtils.isLeftAmPm()) {
            holder.mAmPmTextClock.setVisibility(8);
            holder.mAmPmTextClockLeft.setVisibility(0);
        } else {
            holder.mAmPmTextClock.setVisibility(0);
            holder.mAmPmTextClockLeft.setVisibility(8);
        }
        setDndDragFlag();
        if (StateUtils.isContextInDexMode(holder.itemView.getContext())) {
            holder.itemView.setOnCreateContextMenuListener(WorldclockMainListAdapter$$Lambda$4.lambdaFactory$(this, holder));
        }
        return holder;
    }

    private /* synthetic */ void lambda$onCreateViewHolder$0(WorldclockViewHolder holder, View view) {
        this.mWorldclockMainListListener.onClick(view, holder.getAdapterPosition());
    }

    private /* synthetic */ boolean lambda$onCreateViewHolder$1(WorldclockViewHolder holder, View view) {
        return this.mWorldclockMainListListener.onLongClick(view, holder.getAdapterPosition());
    }

    private /* synthetic */ boolean lambda$onCreateViewHolder$2(View v, int keyCode, KeyEvent event) {
        if (keyCode == 66) {
            this.mWorldclockMainListListener.onEnterKeyEvent(v, keyCode, event);
        }
        return false;
    }

    private /* synthetic */ void lambda$onCreateViewHolder$4(WorldclockViewHolder holder, ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        int position = holder.getAdapterPosition();
        contextMenu.setHeaderTitle(holder.mCityNameView.getText());
        contextMenu.add(0, 1, 0, C0836R.string.delete).setOnMenuItemClickListener(WorldclockMainListAdapter$$Lambda$8.lambdaFactory$(this, position));
    }

    private /* synthetic */ boolean lambda$null$3(int position, MenuItem menuItem) {
        if (menuItem.getItemId() == 1) {
            this.mWorldclockMainListListener.onContextItemSelected(position);
        }
        return true;
    }

    public void onBindViewHolder(WorldclockViewHolder holder, int position) {
        setCityInfo(holder, (ListItem) this.mCityItems.get(position), ((ListItem) this.mCityItems.get(position)).getTimeZone());
        setCheckBoxState(holder, ((ListItem) this.mCityItems.get(position)).getSelected());
        drawCityInfo(holder);
        setWeatherInfoView(holder, (ListItem) this.mCityItems.get(position), position);
        setSelectionBg(holder, position);
        setReorder(holder, this.mWorldclockMainListListener.isActionMode());
        initCheckBox(holder);
        setDescription(holder, position);
        holder.mTimeInfoGuideline.setGuidelineEnd(getTimeInfoGuidelinePos(this.mWorldclockMainListListener.isActionMode()));
        holder.mCityInfoGuideline.setGuidelineBegin(getCityInfoGuidelinePos(this.mWorldclockMainListListener.isActionMode()));
        if (this.mWorldclockMainListListener.isActionMode() && StateUtils.isTalkBackEnabled(this.mContext)) {
            holder.itemView.setAccessibilityDelegate(this.mAccessibilityDelegate);
        } else {
            holder.itemView.setAccessibilityDelegate(null);
        }
    }

    private void setReorder(WorldclockViewHolder holder, boolean isEditMode) {
        FrameLayout frameLayout = holder.mReorderFrameLayout;
        int i = (!isEditMode || getItemCount() <= 1) ? 8 : 0;
        frameLayout.setVisibility(i);
        holder.mReorderFrameLayout.setOnTouchListener(WorldclockMainListAdapter$$Lambda$5.lambdaFactory$(this, holder));
    }

    private /* synthetic */ boolean lambda$setReorder$5(WorldclockViewHolder holder, View view, MotionEvent event) {
        if (event.getActionMasked() == 0) {
            holder.itemView.setHapticFeedbackEnabled(false);
            this.mItemTouchHelper.startDrag(holder);
            holder.itemView.animate().alpha(0.8f).setDuration(120).setInterpolator(new SineInOut33()).start();
            view.setSelected(true);
        }
        return false;
    }

    private void setAnimationValue() {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(Worldclock.LIST_ANIMATION_DURATION.longValue());
        Fade fadeOut = new Fade(2);
        fadeOut.setDuration(Worldclock.WEATHER_ANIMATION_DURATION.longValue());
        Fade fadeIn = new Fade(1);
        fadeIn.setStartDelay(Worldclock.WEATHER_ANIMATION_DURATION.longValue());
        fadeIn.setDuration(Worldclock.WEATHER_ANIMATION_DURATION.longValue());
        this.mTransitionSet.addTransition(changeBounds).addTransition(fadeIn).addTransition(fadeOut);
        this.mTransitionSet.setInterpolator(new SineInOut90());
    }

    private void setWeatherHandler() {
        this.mWeatherHandler = new WeatherHandler(this.mContext, new C09041());
    }

    private void setWeatherInfoView(WorldclockViewHolder holder, ListItem item, int position) {
        boolean isDisableWeather;
        int i = 0;
        if (WorldclockWeatherUtils.isDisableWeather(this.mContext) || this.mWorldclockMainListListener.isActionMode()) {
            isDisableWeather = true;
        } else {
            isDisableWeather = false;
        }
        WeatherInfoView weatherInfoView = holder.mWeatherInfoView;
        if (isDisableWeather) {
            i = 8;
        }
        weatherInfoView.setVisibility(i);
        switch (item.getCurrentWeatherState()) {
            case 0:
                drawProgressBar(holder);
                break;
            case 1:
                drawReload(holder);
                break;
            case 2:
                drawWeatherInfo(holder, item);
                break;
            case 3:
                drawProgressBar(holder);
                sendWeatherHandler(position);
                break;
        }
        if (holder.mWeatherInfoView.getVisibility() == 0) {
            holder.mWeatherInfoView.setAlpha(1.0f);
            setListener(holder, position, item.getCurrentWeatherState(), item.getMobileLink());
        }
    }

    public int getItemCount() {
        return this.mCityItems.size();
    }

    public long getItemId(int position) {
        return (long) ((ListItem) this.mCityItems.get(position)).getId();
    }

    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public void setDescription(WorldclockViewHolder holder, int position) {
        if (holder != null) {
            ListItem item = (ListItem) this.mCityItems.get(position);
            String description = holder.mCityNameView.getText() + ", " + holder.mTimeDiff.getContentDescription() + ", ";
            if (StateUtils.isLeftAmPm()) {
                description = description + holder.mAmPmTextClock.getText() + ", " + holder.mDigitalClock.getText();
            } else {
                description = description + holder.mDigitalClock.getText() + ", " + holder.mAmPmTextClock.getText();
            }
            if (!this.mWorldclockMainListListener.isActionMode() && holder.mWeatherInfoView.getVisibility() == 0) {
                description = description + ", ";
                if (item.getCurrentWeatherState() == 1) {
                    description = description + this.mContext.getResources().getString(C0836R.string.worldclock_weather_reload);
                } else if (item.getCurrentWeatherState() == 2) {
                    description = description + item.getWeatherLayoutDescription();
                }
                holder.mListItem.setImportantForAccessibility(1);
                holder.mCheckBox.setImportantForAccessibility(2);
                holder.mListItem.setContentDescription(description);
            } else if (this.mWorldclockMainListListener.isActionMode()) {
                holder.mCheckBox.setImportantForAccessibility(1);
                holder.mListItem.setImportantForAccessibility(2);
                holder.mCheckBox.setContentDescription(description);
            } else {
                holder.mListItem.setImportantForAccessibility(0);
                holder.mCheckBox.setImportantForAccessibility(2);
                holder.mListItem.setContentDescription(null);
            }
        }
    }

    private void drawCityInfo(WorldclockViewHolder holder) {
        ClockUtils.setLargeTextSize(this.mContext, holder.mCityNameView, (float) holder.mCityNameView.getContext().getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_city_name_text_size));
        holder.mCityNameView.setMarqueeRepeatLimit(0);
    }

    private void setCityInfo(WorldclockViewHolder holder, ListItem item, TimeZone tz) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(tz);
        if (!CityManager.sIsCityManagerLoad) {
            CityManager.initCity(this.mContext);
        }
        City city = CityManager.findCityByUniqueId(Integer.valueOf(item.getUniqueId()));
        holder.mCityNameView.setText(city != null ? city.getName() : "");
        holder.mDigitalClock.setTimeZone(calendar.getTimeZone().getID());
        holder.mDigitalClock.setVisibility(0);
        holder.mAmPmTextClock.setTimeZone(calendar.getTimeZone().getID());
        holder.mAmPmTextClockLeft.setTimeZone(calendar.getTimeZone().getID());
        holder.mTimeDiff.setVisibility(0);
        WorldclockUtils.getCityDayTimeDiffString(this.mContext, this.mTimeZoneFinder.isLocalCity(item.getUniqueId()), item.getId(), holder.mTimeDiff, holder.mTimeDiff, tz, true);
        holder.mTimeDiff.setHorizontallyScrolling(true);
    }

    private void setCheckBoxState(WorldclockViewHolder holder, boolean isSelected) {
        if (this.mWorldclockMainListListener.isActionMode()) {
            holder.mCheckBox.setChecked(isSelected);
        } else {
            holder.mCheckBox.setChecked(false);
        }
    }

    ItemTouchHelper getItemTouchHelper() {
        return this.mItemTouchHelper;
    }

    public void onItemMoved(int from, int to) {
        if (((LinearLayoutManager) this.mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() - 1 == to) {
            this.mAppBarLayout.setExpanded(false, true);
        }
        if (to != this.mCityItems.size() && to >= 0) {
            boolean hasWeatherInfo = false;
            if (this.mCityWeatherInfo != null && this.mCityWeatherInfo.size() == this.mCityItems.size()) {
                hasWeatherInfo = true;
            }
            int i;
            if (from < to) {
                for (i = from; i < to; i++) {
                    Collections.swap(this.mCityItems, i, i + 1);
                    if (hasWeatherInfo) {
                        Collections.swap(this.mCityWeatherInfo, i, i + 1);
                    }
                }
            } else {
                for (i = from; i > to; i--) {
                    Collections.swap(this.mCityItems, i, i - 1);
                    if (hasWeatherInfo) {
                        Collections.swap(this.mCityWeatherInfo, i, i - 1);
                    }
                }
            }
            notifyItemMoved(from, to);
            this.mNeedSave = true;
        }
    }

    public void onDrag(ViewHolder viewHolder) {
    }

    public void onDrop() {
        if (this.mNeedSave) {
            this.mWorldclockMainListListener.onDrop();
            this.mNeedSave = false;
        }
    }

    public void onClearView(ViewHolder viewHolder) {
        if (viewHolder != null) {
            viewHolder.itemView.animate().alpha(1.0f).setDuration(250).setInterpolator(new SineInOut33()).start();
            viewHolder.itemView.findViewById(C0836R.id.list_reorder_layout).setSelected(false);
            viewHolder.itemView.setHapticFeedbackEnabled(false);
        }
    }

    void refreshCityTimeDiffInfo(WorldclockViewHolder holder, int position) {
        if (holder != null) {
            ListItem item = (ListItem) this.mCityItems.get(position);
            if (item != null) {
                WorldclockUtils.getCityDayTimeDiffString(this.mContext, this.mTimeZoneFinder.isLocalCity(item.getUniqueId()), item.getId(), holder.mTimeDiff, holder.mTimeDiff, item.getTimeZone(), true);
            }
        }
    }

    void initCheckBox(WorldclockViewHolder holder) {
        if (holder != null) {
            if (this.mWorldclockMainListListener.isActionMode()) {
                holder.mCheckBox.setScaleX(1.0f);
                holder.mCheckBox.setScaleY(1.0f);
                holder.mCheckBox.setVisibility(0);
            } else {
                holder.mCheckBox.setScaleX(0.5f);
                holder.mCheckBox.setScaleY(0.5f);
                holder.mCheckBox.setVisibility(8);
            }
            holder.mCheckBox.setOnCheckedChangeListener(WorldclockMainListAdapter$$Lambda$6.lambdaFactory$(holder));
        }
    }

    void setSelectionBg(WorldclockViewHolder holder, int position) {
        if (holder != null && position >= 0) {
            try {
                if (position < getItemCount()) {
                    holder.mListItem.setChecked(((ListItem) this.mCityItems.get(position)).getSelected());
                }
            } catch (IndexOutOfBoundsException e) {
                Log.secE("WorldclockMainListAdapter", "setSelectionBg exception : " + e.toString());
            }
        }
    }

    void toggleCheckBox(int position) {
        selectView(position, !((ListItem) this.mCityItems.get(position)).getSelected());
    }

    private void selectView(int position, boolean value) {
        ((ListItem) this.mCityItems.get(position)).setSelected(value);
        notifyDataSetChanged();
    }

    int getCheckedCount() {
        int count = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (((ListItem) this.mCityItems.get(i)).getSelected()) {
                count++;
            }
        }
        return count;
    }

    public void setChecked(int position, boolean isChecked, WorldclockViewHolder holder) {
        if (holder != null && holder.mCheckBox != null) {
            holder.mCheckBox.setChecked(isChecked);
            ((ListItem) this.mCityItems.get(position)).setSelected(isChecked);
        }
    }

    void setCheckedAll(WorldclockViewHolder holder, boolean isAllSelect) {
        if (holder != null && holder.mCheckBox != null) {
            holder.mCheckBox.setChecked(isAllSelect);
        }
    }

    void setSelectedAll(boolean isAllSelect) {
        for (int i = 0; i < getItemCount(); i++) {
            ((ListItem) this.mCityItems.get(i)).setSelected(isAllSelect);
        }
    }

    private void drawProgressBar(WorldclockViewHolder holder) {
        if (holder != null && holder.mWeatherInfoView != null) {
            holder.mWeatherInfoView.startWeatherProgress();
        }
    }

    private void drawReload(WorldclockViewHolder holder) {
        if (holder != null && holder.mWeatherInfoView != null) {
            holder.mWeatherInfoView.setVisibleWeatherReloadIcon();
        }
    }

    private void drawWeatherInfo(WorldclockViewHolder holder, ListItem item) {
        if (holder != null && holder.mWeatherInfoView != null) {
            holder.mWeatherInfoView.setDisplayWeatherData(item.getWeatherTemp(), item.getWeatherIconNum(), item.getWeatherDescription(), item.getDayOrNight());
            holder.mWeatherInfoView.setContentDescription(item.getWeatherLayoutDescription());
        }
    }

    private void setListener(WorldclockViewHolder holder, int position, int currentState, String mobileLink) {
        if (holder != null && holder.mWeatherInfoView != null) {
            final int i = currentState;
            final WorldclockViewHolder worldclockViewHolder = holder;
            final int i2 = position;
            final String str = mobileLink;
            holder.mWeatherInfoView.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View v) {
                    if (i == 1) {
                        worldclockViewHolder.mWeatherInfoView.startWeatherProgress();
                        WorldclockMainListAdapter.this.sendWeatherHandler(i2);
                    } else if (!TextUtils.isEmpty(str)) {
                        try {
                            Uri uri = new WeatherUrlManager().setViewUri("DETAIL_HOME", str);
                            Log.secD("WorldclockMainListAdapter", "mWeatherInfoView onSingleClick() => final uri : " + uri);
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.setData(uri);
                            WorldclockMainListAdapter.this.mContext.startActivity(intent);
                            ClockUtils.insertSaLog("112", "1271");
                        } catch (ActivityNotFoundException e) {
                            Log.secE("WorldclockMainListAdapter", "mWeatherInfoView ActivityNotFoundException :" + e.toString());
                        }
                    }
                }
            });
            holder.mWeatherInfoView.setOnLongClickListener(WorldclockMainListAdapter$$Lambda$7.lambdaFactory$());
        }
    }

    void sendWeatherHandler(int position) {
        if (!WorldclockWeatherUtils.isDisableWeather(this.mContext)) {
            ArrayList<String> placeId = new ArrayList();
            if (position == -1) {
                for (ListItem item : this.mCityItems) {
                    placeId.add(item.getPlaceId());
                }
            } else if (this.mCityItems.size() > position) {
                placeId.add(((ListItem) this.mCityItems.get(position)).getPlaceId());
            }
            if (placeId.size() > 0) {
                this.mWeatherHandler.stopThreadAndMessage();
                this.mWeatherHandler.sendMessageDelayed(100, 0, position, Integer.valueOf(0));
                this.mWeatherHandler.sendMessage(800, 0, position, placeId.clone());
            }
        }
    }

    private void setWeatherInfoList() {
        RuntimeException e;
        try {
            int count = getItemCount();
            if (this.mCityWeatherInfo.size() == count) {
                for (int i = 0; i < count; i++) {
                    WorldclockCityWeatherInfo info = (WorldclockCityWeatherInfo) this.mCityWeatherInfo.get(i);
                    ((ListItem) this.mCityItems.get(i)).setWeatherTemp(info.getCurrentTemperature());
                    ((ListItem) this.mCityItems.get(i)).setWeatherIconNum(info.getWeatherIconNum());
                    ((ListItem) this.mCityItems.get(i)).setMobileLink(info.getMobileLink());
                    ((ListItem) this.mCityItems.get(i)).setWeatherDescription(info.getWeatherDescription());
                    ((ListItem) this.mCityItems.get(i)).setWeatherLayoutDescription(WorldclockWeatherUtils.getWeatherLayoutDescription(this.mContext, info));
                    ((ListItem) this.mCityItems.get(i)).setDayOrNight(info.getDayOrNight());
                    ((ListItem) this.mCityItems.get(i)).setCurrentWeatherState(info.getCurrentState());
                }
            }
        } catch (NullPointerException e2) {
            e = e2;
            Log.secE("WorldclockMainListAdapter", "setWeatherInfoList : " + e.toString());
        } catch (IndexOutOfBoundsException e3) {
            e = e3;
            Log.secE("WorldclockMainListAdapter", "setWeatherInfoList : " + e.toString());
        }
    }

    void resetWeatherInfoList(int state) {
        RuntimeException e;
        try {
            int count = getItemCount();
            for (int i = 0; i < count; i++) {
                ((ListItem) this.mCityItems.get(i)).setCurrentWeatherState(state);
            }
        } catch (NullPointerException e2) {
            e = e2;
            Log.secE("WorldclockMainListAdapter", "resetWeatherInfoList : " + e.toString());
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e3) {
            e = e3;
            Log.secE("WorldclockMainListAdapter", "resetWeatherInfoList : " + e.toString());
            notifyDataSetChanged();
        }
        notifyDataSetChanged();
    }

    void setWeatherState(int state) {
        for (ListItem item : this.mCityItems) {
            item.setCurrentWeatherState(state);
        }
    }

    void startEditModeAnimation(WorldclockViewHolder holder, boolean isEditMode) {
        int i = 0;
        float f = 1.0f;
        if (holder != null) {
            float f2;
            TransitionManager.beginDelayedTransition(holder.mListItem, this.mTransitionSet);
            holder.mTimeInfoGuideline.setGuidelineEnd(getTimeInfoGuidelinePos(isEditMode));
            holder.mCityInfoGuideline.setGuidelineBegin(getCityInfoGuidelinePos(isEditMode));
            WeatherInfoView weatherInfoView = holder.mWeatherInfoView;
            int i2 = (WorldclockWeatherUtils.isDisableWeather(this.mContext) || isEditMode) ? 8 : 0;
            weatherInfoView.setVisibility(i2);
            FrameLayout frameLayout = holder.mReorderFrameLayout;
            if (!isEditMode || getItemCount() <= 1) {
                i2 = 8;
            } else {
                i2 = 0;
            }
            frameLayout.setVisibility(i2);
            CheckBox checkBox = holder.mCheckBox;
            if (!isEditMode) {
                i = 8;
            }
            checkBox.setVisibility(i);
            ViewPropertyAnimator animate = holder.mCheckBox.animate();
            if (isEditMode) {
                f2 = 0.5f;
            } else {
                f2 = 1.0f;
            }
            ViewPropertyAnimator scaleYBy = animate.scaleXBy(f2).scaleX(isEditMode ? 1.0f : 0.5f).scaleYBy(isEditMode ? 0.5f : 1.0f);
            if (!isEditMode) {
                f = 0.5f;
            }
            scaleYBy.scaleY(f).setDuration(Worldclock.LIST_ANIMATION_DURATION.longValue()).start();
            if (this.mWorldclockMainListListener.isActionMode() && StateUtils.isTalkBackEnabled(this.mContext)) {
                holder.itemView.setAccessibilityDelegate(this.mAccessibilityDelegate);
            } else {
                holder.itemView.setAccessibilityDelegate(null);
            }
        }
    }

    private int getTimeInfoGuidelinePos(boolean isEditMode) {
        int editModeEndValue;
        int normalEndValue;
        boolean isDisableWeather = WorldclockWeatherUtils.isDisableWeather(this.mContext);
        if (!StateUtils.isScreenDp(this.mContext, 512) || isEditMode) {
            editModeEndValue = this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_time_info_guideline_end);
        } else {
            editModeEndValue = this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.f21xb3d7ee08);
        }
        if (isDisableWeather) {
            normalEndValue = this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_layout_padding_left_right);
        } else {
            normalEndValue = editModeEndValue;
        }
        if (isEditMode) {
            return editModeEndValue;
        }
        return normalEndValue;
    }

    private int getCityInfoGuidelinePos(boolean isEditMode) {
        return isEditMode ? this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_city_info_guideline_start) : this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_layout_padding_left_right);
    }

    void updateListItem(List<ListItem> list) {
        this.mCityItems = list;
    }

    void updateWeatherListItem(ArrayList<WorldclockCityWeatherInfo> weatherInfo) {
        this.mCityWeatherInfo = weatherInfo;
        setWeatherHandler();
        if (this.mCityWeatherInfo != null && !this.mCityWeatherInfo.isEmpty()) {
            setWeatherInfoList();
        }
    }

    private void setDndDragFlag() {
        if (StateUtils.isScreenDp(this.mContext, 512)) {
            this.mDndHelper.setDragFlag(51);
        } else {
            this.mDndHelper.setDragFlag(3);
        }
    }
}
