package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SeslArrayIndexer;
import android.support.v7.widget.SeslIndexScrollView;
import android.support.v7.widget.SeslIndexScrollView.OnIndexBarEventListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarListViewModelListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.SearchBarData;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import java.util.ArrayList;
import java.util.Locale;

public class SearchBarListViewModel extends FrameLayout {
    private Activity mActivity;
    private SeslIndexScrollView mAlpIndex;
    private EditText mAutoText;
    private String mCityCountryName;
    private Context mContext;
    private DropdownListAdapter mDropdownAdapter;
    private FrameLayout mDropdownLayout;
    private ListView mDropdownList;
    private String mFromWhere = "";
    private ArrayList<String> mFullCityList = new ArrayList();
    private int mHomeZone;
    private int mIndex;
    private TextView mNoCityTextView;
    private SearchBarData mSearchBarData;
    private SearchBarListViewModelListener mSearchBarListViewModelListener;
    private ArrayList<String> mSearchCityList = new ArrayList();
    private ArrayList<String> mStrokesIndexList;
    private int mWidgetId;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarListViewModel$1 */
    class C08631 implements OnIndexBarEventListener {
        C08631() {
        }

        public void onPressed(float v) {
        }

        public void onReleased(float v) {
        }

        public void onIndexChanged(int positionOfData) {
            if (positionOfData < 0) {
                positionOfData = 0;
            }
            if (SearchBarListViewModel.this.isSpecificIndexLanguage()) {
                int index = positionOfData + 3;
                int cnt = SearchBarListViewModel.this.mDropdownList.getCount();
                for (int i = 0; i < cnt; i++) {
                    if (((String) SearchBarListViewModel.this.mStrokesIndexList.get(i)).substring(0, 2).contains(Integer.toString(index))) {
                        SearchBarListViewModel.this.mDropdownList.setSelection(i);
                        return;
                    }
                }
                return;
            }
            SearchBarListViewModel.this.mDropdownList.setSelection(positionOfData);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarListViewModel$2 */
    class C08642 implements OnScrollListener {
        C08642() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            SearchBarListViewModel.this.mAlpIndex.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case 1:
                    if (SearchBarListViewModel.this.mSearchBarData != null && SearchBarListViewModel.this.mSearchBarData.getImm() != null && SearchBarListViewModel.this.mSearchBarData.getImm().semIsInputMethodShown() && SearchBarListViewModel.this.mAutoText != null && SearchBarListViewModel.this.mSearchBarData.isDropdownListShown() && !StateUtils.isMobileKeyboard(SearchBarListViewModel.this.mContext)) {
                        if (Feature.isSupportMinimizedSip()) {
                            SearchBarListViewModel.this.mSearchBarData.getImm().semMinimizeSoftInput(SearchBarListViewModel.this.mAutoText.getWindowToken(), 22);
                            return;
                        } else {
                            SearchBarListViewModel.this.mSearchBarData.getImm().hideSoftInputFromWindow(SearchBarListViewModel.this.mAutoText.getWindowToken(), 0);
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private enum DROPDOWN_LISTENER_SELECT {
        HIDE_SOFT_INPUT,
        SELECT_CITY,
        SHOW_DROPDOWN_LIST,
        ON_CLEAR_POPUP_TALK_BACK_FOCUS
    }

    public SearchBarListViewModel(Context context) {
        super(context);
    }

    public SearchBarListViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchBarListViewModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Activity activity, String fromWhere, int index, SearchBarData searchBarData, SearchBarListViewModelListener searchBarListViewModelListener, EditText autoText) {
        this.mContext = activity.getApplicationContext();
        this.mActivity = activity;
        this.mFromWhere = fromWhere;
        this.mIndex = index;
        this.mSearchBarData = searchBarData;
        this.mAutoText = autoText;
        this.mSearchBarListViewModelListener = searchBarListViewModelListener;
        initView();
    }

    private void initView() {
        initDropdownListView();
        setDropdownList();
        bindList();
        initIndexScrollView();
    }

    private void initDropdownListView() {
        ((LayoutInflater) this.mActivity.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_search_bar_list_layout, this, true);
        this.mNoCityTextView = (TextView) findViewById(C0836R.id.no_city_text);
        this.mDropdownLayout = (FrameLayout) findViewById(C0836R.id.dropdown_list_layout);
        this.mDropdownList = (ListView) findViewById(C0836R.id.dropdown_list_view);
        try {
            this.mDropdownList.semSetRoundedCorners(15);
            this.mDropdownList.semSetRoundedCornerColor(15, this.mActivity.getColor(C0836R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("SearchBarListViewModel", "NoSuchMethodError : " + e);
        }
    }

    private void setDropdownList() {
        if (this.mDropdownList != null) {
            try {
                this.mDropdownList.semSetGoToTopEnabled(true, 0);
            } catch (NoSuchMethodError e) {
                Log.secE("SearchBarListViewModel", "NoSuchMethodError semEnableGoToTop");
            }
            this.mDropdownList.setOnItemClickListener(SearchBarListViewModel$$Lambda$1.lambdaFactory$(this));
        }
    }

    private /* synthetic */ void lambda$setDropdownList$0(AdapterView parent, View view, int position, long id) {
        if (this.mActivity != null && this.mContext != null) {
            String cityName = ((TextView) view.findViewById(C0836R.id.auto_list_city_name)).getText().toString();
            City city = CityManager.findCityObjectByName(cityName);
            if (city != null) {
                if (this.mDropdownList != null) {
                    this.mDropdownList.setSelector(C0836R.color.transparent);
                }
                if (this.mSearchBarData != null && this.mSearchBarData.getModelType() == 1) {
                    if (!StateUtils.isMobileKeyboard(this.mContext)) {
                        callDropdownListener(DROPDOWN_LISTENER_SELECT.HIDE_SOFT_INPUT, null);
                    }
                    callDropdownListener(DROPDOWN_LISTENER_SELECT.SELECT_CITY, city);
                    this.mSearchBarData.setCurrentLocationListShowing(false);
                } else if (WorldclockUtils.isFromDualClockDigitalWidget(this.mFromWhere)) {
                    WorldclockUtils.addCityToDualClockDigitalWidget(this.mContext, city.getUniqueId(), this.mHomeZone, this.mWidgetId);
                    this.mActivity.finish();
                } else if (WorldclockUtils.isFromDualClockDigitalLaunch(this.mFromWhere)) {
                    this.mActivity.setResult(-1, WorldclockUtils.addCityToDualClockDigitalLaunch(this.mContext, city.getUniqueId(), this.mHomeZone, this.mWidgetId));
                    this.mActivity.finish();
                } else if (WorldclockUtils.isFromDualClockDigitalAOD(this.mFromWhere)) {
                    WorldclockUtils.addCityToDualClockAod(this.mContext, city.getUniqueId(), this.mHomeZone, this.mWidgetId);
                    this.mActivity.finish();
                } else if (WorldclockUtils.isFromDualClockDigitalWatch(this.mFromWhere)) {
                    this.mActivity.setResult(-1, WorldclockUtils.addCityDualClockDigitalWatch(this.mContext, city));
                    callDropdownListener(DROPDOWN_LISTENER_SELECT.HIDE_SOFT_INPUT, null);
                    this.mActivity.finish();
                } else if (WorldclockUtils.isFromDualClockDigitalPremiumWatch(this.mFromWhere)) {
                    this.mActivity.setResult(-1, WorldclockUtils.addCityDualClockDigitalPremiumWatch(city));
                    this.mActivity.finish();
                } else {
                    checkAndSaveDb(city, cityName);
                }
            }
        }
    }

    private void checkAndSaveDb(City city, String cityName) {
        if (WorldclockDBManager.isDuplication(this.mContext, city.getUniqueId())) {
            WorldclockUtils.showDuplicationToast(this.mActivity, cityName);
            return;
        }
        Intent resultIntent = new Intent();
        if (WorldclockUtils.isFromWorldclockListWhereCity(this.mFromWhere)) {
            resultIntent.putExtra("city_result_type", 2);
            if (!WorldclockDBManager.updateDB(this.mContext, city, "_id = " + this.mIndex)) {
                WorldclockUtils.showNotEnoughSpaceToast(this.mActivity);
            }
        } else {
            resultIntent.putExtra("city_result_type", 1);
            if (!WorldclockDBManager.saveDB(this.mContext, city)) {
                WorldclockUtils.showNotEnoughSpaceToast(this.mActivity);
            }
        }
        this.mActivity.setResult(-1, resultIntent);
        callDropdownListener(DROPDOWN_LISTENER_SELECT.HIDE_SOFT_INPUT, null);
        this.mActivity.finish();
    }

    public void bindList() {
        City[] cities = CityManager.getCitiesByName();
        if (!this.mSearchCityList.isEmpty()) {
            this.mSearchCityList.clear();
        }
        for (City c : cities) {
            if (c != null) {
                this.mSearchCityList.add(c.getName() + (c.getCountry().length() > 0 ? " / " + c.getCountry() : ""));
            }
        }
        this.mFullCityList.addAll(this.mSearchCityList);
        this.mDropdownAdapter = new DropdownListAdapter(this.mActivity, C0836R.layout.worldclock_search_bar_list_item_layout, this.mSearchCityList, this.mAutoText);
        if (this.mDropdownList != null) {
            this.mDropdownList.setAdapter(this.mDropdownAdapter);
            this.mDropdownList.setDivider(null);
        }
    }

    public void initMassData(String cityCountrySel, int homeZone, int widgetID) {
        this.mCityCountryName = cityCountrySel;
        this.mHomeZone = homeZone;
        this.mWidgetId = widgetID;
    }

    public void setPositionList() {
        if (this.mDropdownList != null && this.mIndex != 0) {
            int cnt = this.mDropdownList.getCount();
            int i = 0;
            while (i < cnt) {
                String selectedCity = (String) this.mDropdownList.getItemAtPosition(i);
                if (selectedCity != null && this.mCityCountryName != null) {
                    if (this.mCityCountryName.equals(selectedCity)) {
                        this.mDropdownList.setSelection(i);
                        this.mDropdownList.requestFocus();
                        return;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void initIndexScrollView() {
        this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        if (this.mAlpIndex == null) {
            this.mAlpIndex = (SeslIndexScrollView) View.inflate(this.mActivity, C0836R.layout.worldclock_index_scroll, null);
            this.mAlpIndex.setIndexBarGravity(StateUtils.isRtl() ? 0 : 1);
            if (isSpecificIndexLanguage()) {
                this.mAlpIndex.setSimpleIndexScroll(this.mActivity.getResources().getStringArray(C0836R.array.arr_strokes_indexlist_clock), 30);
                this.mStrokesIndexList = CityManager.getCitiesOrderByName();
            } else {
                try {
                    this.mAlpIndex.setIndexer(new SeslArrayIndexer(CityManager.getCitiesOrderByName(), this.mActivity.getResources().getString(C0836R.string.wc_twstr_indexlist)));
                } catch (NoSuchMethodError e) {
                    Log.secE("SearchBarListViewModel", "NoSuchMethodError");
                }
            }
            this.mAlpIndex.setOnIndexBarEventListener(new C08631());
        }
        setIndexScrollVisibility();
        controlIndexScrollView();
        this.mDropdownList.setOnScrollListener(new C08642());
    }

    private void controlIndexScrollView() {
        if (this.mAlpIndex != null && this.mDropdownLayout != null) {
            this.mAlpIndex.clearAnimation();
            this.mDropdownLayout.removeView(this.mAlpIndex);
            this.mDropdownLayout.addView(this.mAlpIndex);
        }
    }

    public void changeList(ArrayList<String> currentLocationList) {
        if (this.mSearchBarData != null && this.mSearchBarData.getLastString() == null) {
            this.mSearchBarData.setLastString("");
        }
        City[] cities = CityManager.getCitiesByName();
        City[] citiesEn = CityManager.getCitiesByEngName();
        if (isSetSearchList(cities, currentLocationList) || isSetSearchList(citiesEn, currentLocationList)) {
            this.mDropdownList.setVisibility(0);
            this.mNoCityTextView.setVisibility(8);
        } else {
            this.mDropdownList.setVisibility(8);
            this.mNoCityTextView.setVisibility(0);
        }
        if (StateUtils.isMobileKeyboard(this.mContext) || this.mDropdownAdapter != null) {
            callDropdownListener(DROPDOWN_LISTENER_SELECT.SHOW_DROPDOWN_LIST, null);
            callDropdownListener(DROPDOWN_LISTENER_SELECT.ON_CLEAR_POPUP_TALK_BACK_FOCUS, null);
        }
        setIndexScrollVisibility();
        this.mDropdownList.setSelector(C0836R.drawable.worldclock_popup_ripple);
    }

    public void setIndexScrollVisibility() {
        String lastString = this.mSearchBarData != null ? this.mSearchBarData.getLastString() : null;
        if (this.mAlpIndex != null && lastString != null && this.mDropdownList != null) {
            int scrollPadding = this.mActivity.getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_list_indexscroll_view_width);
            if (!lastString.isEmpty() || this.mSearchBarData.isCurrentLocationListShowing()) {
                this.mAlpIndex.setVisibility(8);
                this.mDropdownList.setVerticalScrollBarEnabled(true);
                this.mDropdownList.setPadding(0, 0, 0, 0);
                return;
            }
            this.mDropdownList.setVerticalScrollBarEnabled(false);
            this.mAlpIndex.setVisibility(0);
            if (StateUtils.isRtl()) {
                this.mDropdownList.setPadding(scrollPadding, 0, 0, 0);
            } else {
                this.mDropdownList.setPadding(0, 0, scrollPadding, 0);
            }
        }
    }

    private boolean isSetSearchList(City[] cities, ArrayList<String> currentLocationList) {
        ArrayList<String> aList = new ArrayList();
        if (cities == null) {
            return false;
        }
        if (!this.mSearchCityList.isEmpty()) {
            this.mSearchCityList.clear();
        }
        String lastString = this.mSearchBarData != null ? this.mSearchBarData.getLastString() : "";
        String lowerCaseMatcher = lastString.toLowerCase().replace(" ", "").replace("-", "").replace(".", "");
        String cityString = null;
        boolean isSelectCurrentLocation = this.mSearchBarData != null && this.mSearchBarData.isCurrentLocationListShowing();
        setSelectCurrentLocation(isSelectCurrentLocation);
        if (isSelectCurrentLocation) {
            if (currentLocationList != null) {
                aList.addAll(currentLocationList);
            }
        } else if (TextUtils.isEmpty(lastString)) {
            aList.addAll(this.mFullCityList);
        } else {
            ArrayList<String> bList = new ArrayList();
            for (City c : cities) {
                if (c != null) {
                    int index = (c.getName() + (c.getCountry().length() > 0 ? " / " + c.getCountry() : "")).replace(" ", "").replace("-", "").replace(".", "").toLowerCase().indexOf(lowerCaseMatcher);
                    if (index != -1) {
                        cityString = CityManager.findCityCountryNameByUniqueId(Integer.valueOf(c.getUniqueId()));
                    }
                    if (cityString != null && index == 0) {
                        aList.add(cityString);
                    } else if (!(cityString == null || index == -1)) {
                        bList.add(cityString);
                    }
                }
            }
            aList.addAll(bList);
        }
        this.mSearchCityList.addAll(aList);
        this.mDropdownAdapter.notifyDataSetChanged();
        if (aList.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isSpecificIndexLanguage() {
        String languageTag = Locale.getDefault().toLanguageTag();
        return "zh-Hant-MO".equalsIgnoreCase(languageTag) || "zh-Hant-HK".equalsIgnoreCase(languageTag);
    }

    public void setSelectCurrentLocation(boolean selectCurrentLocation) {
        if (this.mDropdownAdapter != null) {
            this.mDropdownAdapter.setSelectCurrentLocation(selectCurrentLocation);
        }
    }

    private void callDropdownListener(DROPDOWN_LISTENER_SELECT callIndex, City city) {
        if (this.mSearchBarListViewModelListener != null) {
            switch (callIndex) {
                case HIDE_SOFT_INPUT:
                    this.mSearchBarListViewModelListener.onHideInput();
                    return;
                case SELECT_CITY:
                    SearchBarListViewModelListener searchBarListViewModelListener = this.mSearchBarListViewModelListener;
                    boolean z = this.mSearchBarData != null && this.mSearchBarData.isCurrentLocationListShowing();
                    searchBarListViewModelListener.onSelectCity(city, z);
                    return;
                case SHOW_DROPDOWN_LIST:
                    this.mSearchBarListViewModelListener.onShowList();
                    return;
                case ON_CLEAR_POPUP_TALK_BACK_FOCUS:
                    this.mSearchBarListViewModelListener.onClearPopupTalkBackFocus();
                    return;
                default:
                    return;
            }
        }
    }
}
