package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import com.samsung.android.view.animation.SineInOut70;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.ClockAddButton;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.TimeZoneListViewModelListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.ListItem;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneConvertorListAdapter.TimeZoneConvertorViewHolder;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class TimeZoneListViewModel extends LinearLayout {
    private Activity mActivity;
    private TimeZoneConvertorListAdapter mAdapter;
    private ClockAddButton mAddCityButton;
    private Context mContext;
    private ArrayList<ListItem> mConvertorItems;
    private DividerItemDecoration mDividerItemDecoration;
    private ArrayList<ListItem> mItems;
    private RecyclerView mList;
    private ArrayList<String> mSpinnerArrayList;
    private TimeZoneListViewModelListener mTimeZoneListViewModelListener = null;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneListViewModel$1 */
    class C08841 implements Runnable {
        C08841() {
        }

        public void run() {
            if (TimeZoneListViewModel.this.mAdapter != null) {
                TimeZoneListViewModel.this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.TimeZoneListViewModel$2 */
    class C08852 implements Runnable {
        C08852() {
        }

        public void run() {
            TimeZoneListViewModel.this.refreshCityList();
        }
    }

    public TimeZoneListViewModel(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TimeZoneListViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public TimeZoneListViewModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_timezone_convertor_list, this, true);
        this.mDividerItemDecoration = new DividerItemDecoration(this.mContext, 1);
        this.mAddCityButton = (ClockAddButton) findViewById(C0836R.id.worldclock_timezone_convertor_add_button);
        this.mAddCityButton.setOnClickListener(TimeZoneListViewModel$$Lambda$1.lambdaFactory$(this));
        setOrientationLayout(getResources().getConfiguration());
    }

    private /* synthetic */ void lambda$initView$0(View view) {
        this.mTimeZoneListViewModelListener.startAddCityActivity();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setOrientationLayout(newConfig);
    }

    private void setOrientationLayout(Configuration newConfig) {
        ConstraintLayout convertorLayout = (ConstraintLayout) findViewById(C0836R.id.worldclock_timezone_convertor_list_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        int addButton = C0836R.id.worldclock_timezone_convertor_add_button;
        constraintSet.clone(convertorLayout);
        if (newConfig.orientation == 1) {
            constraintSet.setVerticalBias(addButton, 0.0f);
            constraintSet.setMargin(addButton, 3, getResources().getDimensionPixelOffset(C0836R.dimen.common_add_button_margin_top));
        } else {
            constraintSet.setVerticalBias(addButton, 0.5f);
            constraintSet.setMargin(addButton, 3, 0);
        }
        constraintSet.applyTo(convertorLayout);
    }

    public void initTimeZoneListViewModel(Activity activity, TimeZoneListViewModelListener timeZoneListViewModelListener) {
        this.mActivity = activity;
        this.mContext = this.mActivity.getApplicationContext();
        this.mList = (RecyclerView) findViewById(C0836R.id.worldclock_timezone_convertor_list);
        this.mList.seslSetGoToTopEnabled(true);
        this.mTimeZoneListViewModelListener = timeZoneListViewModelListener;
    }

    public void controlListHead(boolean isNeedAnimation, boolean isMultiWindowMode) {
        if (this.mItems != null && this.mList != null) {
            boolean isEditMode = this.mTimeZoneListViewModelListener.isEditMode();
            if (this.mItems.isEmpty()) {
                this.mList.setVisibility(8);
                this.mAddCityButton.setVisibility(0);
                return;
            }
            int i;
            this.mAddCityButton.setVisibility(8);
            this.mTimeZoneListViewModelListener.setFabButtonVisible(true);
            RecyclerView recyclerView = this.mList;
            float f = (!isEditMode || getResources().getConfiguration().orientation == 2 || StateUtils.isContextInDexMode(this.mActivity)) ? 1.0f : 0.0f;
            recyclerView.setAlpha(f);
            recyclerView = this.mList;
            if (!isEditMode || getResources().getConfiguration().orientation == 2 || StateUtils.isContextInDexMode(this.mActivity)) {
                i = 0;
            } else {
                i = 4;
            }
            recyclerView.setVisibility(i);
            if (isNeedAnimation && !isMultiWindowMode && !StateUtils.isContextInDexMode(this.mActivity)) {
                animateList(isEditMode);
            }
        }
    }

    public void bindList() {
        if (this.mList != null) {
            makeListItem();
            this.mAdapter = new TimeZoneConvertorListAdapter(this.mActivity, this.mItems);
            this.mList.setAdapter(this.mAdapter);
            this.mList.addItemDecoration(this.mDividerItemDecoration);
            this.mList.setHasFixedSize(true);
            this.mList.setLayoutManager(new LinearLayoutManager(this.mContext));
            this.mList.seslSetOutlineStrokeEnabled(false);
            try {
                this.mList.semSetRoundedCorners(15);
                this.mList.semSetRoundedCornerColor(15, this.mActivity.getColor(C0836R.color.window_background_color));
            } catch (NoSuchMethodError e) {
                Log.secE("TimeZoneListViewModel", "NoSuchMethodError : " + e);
            }
        }
    }

    private void makeListItem() {
        Cursor dbCursor = WorldclockDBManager.getDBAll(this.mContext);
        this.mItems = new ArrayList();
        this.mSpinnerArrayList = new ArrayList();
        this.mConvertorItems = new ArrayList();
        if (dbCursor != null) {
            dbCursor.moveToLast();
            int count = 0;
            while (!dbCursor.isBeforeFirst() && count < 20) {
                ListItem item = new ListItem(dbCursor.getInt(0), dbCursor.getString(1), dbCursor.getString(2), dbCursor.getInt(4), (float) dbCursor.getInt(5), (float) dbCursor.getInt(6));
                this.mItems.add(item);
                this.mSpinnerArrayList.add(getCityName(item));
                dbCursor.moveToPrevious();
                count++;
            }
            dbCursor.close();
        }
        this.mConvertorItems.addAll(this.mItems);
        City defaultCityInfo = CityManager.findCityObjectByName(CityManager.findDefaultCityByCapital(TimeZone.getDefault()));
        if (defaultCityInfo != null && !this.mSpinnerArrayList.contains(defaultCityInfo.getName())) {
            this.mConvertorItems.add(0, new ListItem(0, defaultCityInfo.getName(), defaultCityInfo.getTimeZoneId(), defaultCityInfo.getUniqueId(), defaultCityInfo.getLatitude(), defaultCityInfo.getLongitude()));
            this.mSpinnerArrayList.add(0, this.mActivity.getResources().getString(C0836R.string.local_time));
        }
    }

    public void refreshCityList() {
        if (this.mList != null) {
            this.mList.post(new C08841());
        }
    }

    public void refreshCityListDelayed(long delayTime) {
        new Handler().postDelayed(new C08852(), delayTime);
    }

    public void initItemValue(boolean isSetOnlyHour) {
        for (int i = 0; i < this.mAdapter.getItemCount(); i++) {
            TimeZone tz = ((ListItem) this.mAdapter.mCityItems.get(i)).getTimeZone();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeZone(tz);
            ((ListItem) this.mAdapter.mCityItems.get(i)).setStartHour(calendar.get(11));
            if (!isSetOnlyHour) {
                ((ListItem) this.mAdapter.mCityItems.get(i)).setStartMin(calendar.get(12));
                ((ListItem) this.mAdapter.mCityItems.get(i)).setHourDiff(0);
                ((ListItem) this.mAdapter.mCityItems.get(i)).setMinDiff(0);
            }
        }
    }

    public void updateTimeOfList(int hour, int minute, int startHour, int startMin) {
        for (int j = 0; j < this.mAdapter.getItemCount(); j++) {
            ((ListItem) this.mAdapter.mCityItems.get(j)).setHourDiff(hour - startHour);
            ((ListItem) this.mAdapter.mCityItems.get(j)).setMinDiff(minute - startMin);
        }
        refreshListItem();
    }

    private void refreshListItem() {
        if (this.mList != null) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) this.mList.getLayoutManager();
            this.mList.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    TimeZoneListViewModel.this.mList.getViewTreeObserver().removeOnPreDrawListener(this);
                    int childCount = TimeZoneListViewModel.this.mList.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View parentView = TimeZoneListViewModel.this.mList.getChildAt(i);
                        if (parentView != null) {
                            try {
                                ListItem item = (ListItem) TimeZoneListViewModel.this.mAdapter.mCityItems.get(layoutManager.getPosition(parentView));
                                TimeZoneListViewModel.this.mAdapter.setConvertorInfo((TimeZoneConvertorViewHolder) TimeZoneListViewModel.this.mList.getChildViewHolder(parentView), item, item.getTimeZone());
                            } catch (IndexOutOfBoundsException e) {
                                Log.secE("TimeZoneListViewModel", "getItemAtPosition exception : " + e.toString());
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void animateList(final boolean isEditMode) {
        long j;
        ViewPropertyAnimator listener = this.mList.animate().alpha(isEditMode ? 0.0f : 1.0f).setInterpolator(new SineInOut70()).setDuration(200).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                if (TimeZoneListViewModel.this.mList != null) {
                    TimeZoneListViewModel.this.mList.setVisibility(0);
                }
                TimeZoneListViewModel.this.refreshListItem();
            }

            public void onAnimationEnd(Animator animation) {
                if (TimeZoneListViewModel.this.mList != null) {
                    TimeZoneListViewModel.this.mList.setVisibility(isEditMode ? 8 : 0);
                }
                TimeZoneListViewModel.this.refreshCityList();
            }
        });
        if (isEditMode) {
            j = 0;
        } else {
            j = 80;
        }
        listener.setStartDelay(j);
    }

    public void clearList() {
        if (this.mConvertorItems != null) {
            this.mConvertorItems.clear();
            this.mConvertorItems = null;
        }
        if (this.mSpinnerArrayList != null) {
            this.mSpinnerArrayList.clear();
            this.mSpinnerArrayList = null;
        }
        if (this.mItems != null) {
            this.mItems.clear();
            this.mItems = null;
        }
    }

    public void releaseInstance() {
        if (this.mList != null) {
            this.mList.setAdapter(null);
            this.mList.clearAnimation();
            this.mList = null;
        }
        this.mAdapter = null;
    }

    public List<ListItem> getConvertorItems() {
        return this.mConvertorItems;
    }

    public RecyclerView getList() {
        return this.mList;
    }

    public List<String> getSpinnerArrayList() {
        return this.mSpinnerArrayList;
    }

    public List<ListItem> getItems() {
        return this.mItems;
    }

    private String getCityName(ListItem item) {
        if (item.getTopLabel() == null) {
            return "";
        }
        return item.getTopLabel().split(" / ")[0];
    }
}
