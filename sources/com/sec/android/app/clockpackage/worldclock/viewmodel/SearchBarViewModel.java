package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.samsung.android.widget.SemHoverPopupWindow;
import com.sec.android.app.clockpackage.common.callback.OnSingleClickListener;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarListViewModelListener;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarTextWatcherListener;
import com.sec.android.app.clockpackage.worldclock.callback.SearchBarViewListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.SearchBarData;
import com.sec.android.app.clockpackage.worldclock.model.TimeZoneFinder;
import java.util.ArrayList;
import java.util.Locale;

public class SearchBarViewModel {
    private Activity mActivity;
    private EditText mAutoText;
    private ImageButton mClearButton;
    private ArrayList<String> mCurrentLocationList = null;
    private final OnLongClickListener mCurrentLocationLongClickListener = new OnLongClickListener() {
        public boolean onLongClick(View v) {
            int xOffset;
            int yOffset;
            int[] screenPos = new int[2];
            Rect displayFrame = new Rect();
            v.getLocationOnScreen(screenPos);
            v.getWindowVisibleDisplayFrame(displayFrame);
            if (StateUtils.checkHapticFeedbackEnabled(SearchBarViewModel.this.mActivity)) {
                v.setHapticFeedbackEnabled(true);
            } else {
                v.setHapticFeedbackEnabled(false);
            }
            View customToast = ((LayoutInflater) SearchBarViewModel.this.mActivity.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_search_bar_toast_layout, null);
            ((TextView) customToast.findViewById(C0836R.id.toast_text)).setText(v.getContentDescription());
            if (SearchBarViewModel.this.mLocationButtonToast == null) {
                SearchBarViewModel.this.mLocationButtonToast = new Toast(SearchBarViewModel.this.mActivity);
            }
            SearchBarViewModel.this.mLocationButtonToast.setDuration(1);
            SearchBarViewModel.this.mLocationButtonToast.setView(customToast);
            if (StateUtils.isContextInDexMode(SearchBarViewModel.this.mActivity)) {
                xOffset = SearchBarViewModel.this.mActivity.getResources().getInteger(C0836R.integer.worldclock_current_location_popup_xoffset_dexmode);
                yOffset = SearchBarViewModel.this.mActivity.getResources().getInteger(C0836R.integer.worldclock_current_location_popup_yoffset_dexmode);
            } else {
                xOffset = (SearchBarViewModel.this.mActivity.getResources().getDisplayMetrics().widthPixels - displayFrame.right) + (v.getWidth() / 2);
                yOffset = (screenPos[1] + SearchBarViewModel.this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_tooltip_y_offset_normal)) + (v.getHeight() / 2);
            }
            SearchBarViewModel.this.mLocationButtonToast.setGravity(8388661, xOffset, yOffset);
            SearchBarViewModel.this.mLocationButtonToast.show();
            return true;
        }
    };
    private Handler mHandler = new Handler();
    private Toast mLocationButtonToast = null;
    private final SearchBarData mSearchBarData = new SearchBarData();
    private LinearLayout mSearchBarLayout;
    private SearchBarListViewModel mSearchBarListViewModel;
    private View mSearchBarView;
    private final SearchBarViewListener mSearchBarViewListener;
    private ImageButton mVoiceButton;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarViewModel$1 */
    class C08661 implements SearchBarListViewModelListener {
        C08661() {
        }

        public void onHideInput() {
            SearchBarViewModel.this.hideSoftInput();
        }

        public void onSelectCity(City city, boolean currentLocation) {
            SearchBarViewModel.this.showSelectedCity(city, currentLocation);
        }

        public void onShowList() {
            SearchBarViewModel.this.showDropdownList();
        }

        public void onClearPopupTalkBackFocus() {
            if (SearchBarViewModel.this.mSearchBarViewListener != null) {
                SearchBarViewModel.this.mSearchBarViewListener.onClearPopupTalkBackFocus();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarViewModel$3 */
    class C08683 implements OnFocusChangeListener {
        C08683() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && !v.isInTouchMode() && SearchBarViewModel.this.mAutoText != null) {
                SearchBarViewModel.this.setAutoTextCursor(true);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarViewModel$4 */
    class C08694 extends OnSingleClickListener {
        C08694() {
        }

        public void onSingleClick(View v) {
            ClockUtils.insertSaLog("112", "1295");
            SearchBarViewModel.this.performTouchSearchBar();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarViewModel$6 */
    class C08716 implements SearchBarTextWatcherListener {
        C08716() {
        }

        public void onSetClearButtonVisibility(boolean visible) {
            if (SearchBarViewModel.this.mClearButton != null) {
                SearchBarViewModel.this.mClearButton.setVisibility(visible ? 0 : 8);
            }
        }

        public void onSetVoiceButtonVisibility(boolean visible) {
            if (SearchBarViewModel.this.mVoiceButton != null) {
                SearchBarViewModel.this.mVoiceButton.setVisibility(visible ? 0 : 8);
            }
        }

        public void onChangeList() {
            SearchBarViewModel.this.changeList();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarViewModel$8 */
    class C08738 extends OnSingleClickListener {
        C08738() {
        }

        public void onSingleClick(View v) {
            if (SearchBarViewModel.this.mAutoText != null && SearchBarViewModel.this.mAutoText.getEditableText() != null) {
                SearchBarViewModel.this.mAutoText.setText("");
                SearchBarViewModel.this.performTouchSearchBar();
                SearchBarViewModel.this.showSoftInput();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.SearchBarViewModel$9 */
    class C08749 implements Runnable {
        C08749() {
        }

        public void run() {
            SearchBarViewModel.this.performTouchSearchBar();
            if (SearchBarViewModel.this.mSearchBarListViewModel != null) {
                SearchBarViewModel.this.mSearchBarListViewModel.setPositionList();
            }
        }
    }

    SearchBarViewModel(Activity activity, SearchBarViewListener searchBarListener, String fromWhere, int modelType, int index) {
        this.mActivity = activity;
        this.mSearchBarViewListener = searchBarListener;
        this.mSearchBarData.setModelType(modelType);
        this.mSearchBarData.setLastString("");
        initViews();
        initSearchBarList(fromWhere, index);
        initInputMethod(activity);
        initSearchBar(activity);
    }

    private void initSearchBarList(String fromWhere, int index) {
        String str = fromWhere;
        int i = index;
        this.mSearchBarListViewModel.init(this.mActivity, str, i, this.mSearchBarData, new C08661(), this.mAutoText);
    }

    void bindSearchBarList() {
        this.mSearchBarListViewModel.bindList();
    }

    EditText getAutoText() {
        return this.mAutoText;
    }

    boolean isDropdownListShown() {
        return this.mSearchBarData.isDropdownListShown();
    }

    private void initInputMethod(Context context) {
        if (this.mSearchBarData.getImm() == null) {
            Log.secD("SearchBarViewModel", "initInputMethod");
            this.mSearchBarData.setImm((InputMethodManager) context.getSystemService("input_method"));
        }
    }

    private void initViews() {
        FrameLayout searchBarLayoutId = (FrameLayout) this.mActivity.findViewById(C0836R.id.search_bar_layout_id);
        LayoutInflater inflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        if (this.mSearchBarView == null) {
            this.mSearchBarView = inflater.inflate(C0836R.layout.worldclock_search_bar_layout, null);
            searchBarLayoutId.addView(this.mSearchBarView);
        }
        this.mSearchBarLayout = (LinearLayout) this.mSearchBarView.findViewById(C0836R.id.worldclock_search_box);
        this.mAutoText = (EditText) this.mSearchBarView.findViewById(C0836R.id.worldclock_search_map_txt_find);
        this.mVoiceButton = (ImageButton) this.mSearchBarView.findViewById(C0836R.id.voice_search);
        this.mClearButton = (ImageButton) this.mSearchBarView.findViewById(C0836R.id.clear_map_search);
        this.mSearchBarListViewModel = (SearchBarListViewModel) this.mSearchBarView.findViewById(C0836R.id.search_bar_list_view);
    }

    private void setCurrentLocationButton(final Context context) {
        ImageButton currentLocationButton = (ImageButton) this.mSearchBarView.findViewById(C0836R.id.current_location_map_button);
        if (currentLocationButton != null) {
            if (Feature.isLiveDemoBinary() || WorldclockUtils.isWifiOnly(context)) {
                View currentLocationButtonEmpty = this.mSearchBarView.findViewById(C0836R.id.current_location_map_button_empty);
                currentLocationButton.setVisibility(8);
                currentLocationButtonEmpty.setVisibility(0);
            } else {
                currentLocationButton.setVisibility(0);
            }
            SemHoverPopupWindow hoverPopupWindow = currentLocationButton.semGetHoverPopup(true);
            if (hoverPopupWindow != null) {
                hoverPopupWindow.setGravity(20819);
            }
            currentLocationButton.setOnLongClickListener(this.mCurrentLocationLongClickListener);
            currentLocationButton.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View view) {
                    ClockUtils.insertSaLog("112", "1296");
                    SearchBarViewModel.this.mSearchBarViewListener.onHideAllPopup(true);
                    SearchBarViewModel.this.hideSoftInput();
                    SearchBarViewModel.this.findLocationByMnc(context);
                }
            });
            currentLocationButton.setNextFocusDownId(C0836R.id.worldclock_popup_add_button);
        }
    }

    private void setAutoText(final Context context) {
        if (this.mAutoText != null) {
            setAutoTextCursor(false);
            this.mAutoText.setInputType(540673);
            this.mAutoText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            this.mAutoText.setHint(context.getResources().getString(C0836R.string.search_for_city));
            this.mAutoText.setOnFocusChangeListener(new C08683());
            this.mAutoText.setFilters(new InputFilter[]{new WorldclockInputFilter(context)});
            setTextWatcher();
            this.mAutoText.setOnClickListener(new C08694());
            this.mAutoText.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event == null) {
                        return false;
                    }
                    if (actionId != 3 && actionId != 6 && (event.getAction() != 0 || event.getKeyCode() != 66)) {
                        return false;
                    }
                    SearchBarViewModel.this.mAutoText.setCursorVisible(false);
                    if (!StateUtils.isMobileKeyboard(context)) {
                        SearchBarViewModel.this.hideSoftInput();
                    }
                    return true;
                }
            });
        }
    }

    private void setTextWatcher() {
        this.mAutoText.addTextChangedListener(new SearchBarTextWatcher(this.mActivity, this.mSearchBarData, this.mAutoText, new C08716()));
    }

    private void setVoiceSearch(final Context context) {
        if (this.mVoiceButton != null && SpeechRecognizer.isRecognitionAvailable(context)) {
            this.mVoiceButton.setVisibility(StateUtils.isUltraPowerSavingMode(context) ? 8 : 0);
            this.mVoiceButton.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View v) {
                    RuntimeException e;
                    if (StateUtils.isInLockTaskMode(context)) {
                        Toast.makeText(context, C0836R.string.unpin_application, 1).show();
                        return;
                    }
                    try {
                        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
                        intent.putExtra("android.speech.extra.PROMPT", SearchBarViewModel.this.mActivity.getString(C0836R.string.speak_now));
                        intent.putExtra("android.speech.extra.MAX_RESULTS", 1);
                        SearchBarViewModel.this.mActivity.startActivityForResult(intent, 100);
                    } catch (NullPointerException e2) {
                        e = e2;
                        Log.secE("SearchBarViewModel", e.toString());
                    } catch (ActivityNotFoundException e3) {
                        e = e3;
                        Log.secE("SearchBarViewModel", e.toString());
                    }
                }
            });
        }
    }

    private void setClearButton() {
        if (this.mClearButton != null) {
            this.mClearButton.setOnClickListener(new C08738());
        }
    }

    private void initSearchBar(Context context) {
        setCurrentLocationButton(context);
        setAutoText(context);
        setVoiceSearch(context);
        setClearButton();
        if (this.mSearchBarData.getModelType() != 1) {
            new Handler().postDelayed(new C08749(), 120);
        }
    }

    private void performTouchSearchBar() {
        this.mSearchBarData.setCurrentLocationListShowing(false);
        if (this.mAutoText != null) {
            setAutoTextCursor(true);
        }
        changeList();
        this.mHandler.postDelayed(SearchBarViewModel$$Lambda$1.lambdaFactory$(this), 200);
    }

    private /* synthetic */ void lambda$performTouchSearchBar$0() {
        this.mSearchBarViewListener.setSgiVisibility(4);
    }

    private void showSelectedCity(final City city, final boolean currentLocation) {
        long j;
        this.mAutoText.setText(CityManager.findCityCountryNameByUniqueId(Integer.valueOf(city.getUniqueId())));
        this.mAutoText.setSelection(this.mAutoText.length());
        setAutoTextCursor(false);
        this.mAutoText.clearFocus();
        hideDropdownList();
        Handler handler = new Handler();
        Runnable anonymousClass11 = new Runnable() {
            public void run() {
                SearchBarViewModel.this.updateWeatherLogoZoomBtn();
                city.setDBCurrentLocation(currentLocation);
                SearchBarViewModel.this.mSearchBarViewListener.onMoveToCity(city);
                SearchBarViewModel.this.mSearchBarViewListener.onCityTouched(city.getUniqueId());
            }
        };
        if (StateUtils.isSplitMode()) {
            j = 300;
        } else {
            j = 0;
        }
        handler.postDelayed(anonymousClass11, j);
    }

    private void findLocationByMnc(Context context) {
        TimeZoneFinder timeZoneFinder = new TimeZoneFinder(context);
        int result = timeZoneFinder.updateLocationMncAndTimeZone();
        Log.secD("SearchBarViewModel", "result from findLocationByMnc = " + result);
        if (result != 1) {
            Toast.makeText(this.mActivity, this.mActivity.getString(result == 0 ? C0836R.string.wc_no_current_city : C0836R.string.ss_failed_to_find_location), 1).show();
            return;
        }
        CityManager.cleanDBCurrentLocation();
        this.mCurrentLocationList = new ArrayList();
        int cityCountOfCurrentLocation = timeZoneFinder.getCurrentCitiesIdFromTimeZone().size();
        Log.secD("SearchBarViewModel", "findLocationByMnc => cityCountOfCurrentLocation : " + cityCountOfCurrentLocation);
        for (int index = 0; index < cityCountOfCurrentLocation; index++) {
            String cityCountryName = CityManager.findCityCountryNameByUniqueId((Integer) timeZoneFinder.getCurrentCitiesIdFromTimeZone().get(index));
            if (cityCountryName != null) {
                City city = CityManager.findCityObjectByName(cityCountryName);
                if (city != null) {
                    if (this.mSearchBarData.getModelType() == 1 && cityCountOfCurrentLocation == 1) {
                        showSelectedCity(city, true);
                    } else if (cityCountOfCurrentLocation > 1) {
                        this.mCurrentLocationList.add(cityCountryName);
                    }
                }
            }
        }
        if (!this.mCurrentLocationList.isEmpty()) {
            Log.secD("SearchBarViewModel", "findLocationByMnc => find CurrentLocationList");
            this.mSearchBarData.setCurrentLocationListShowing(true);
            changeList();
            this.mAutoText.setText("");
            setAutoTextCursor(false);
        }
    }

    void showSoftInput() {
        if (this.mAutoText.hasFocus() && isDropdownListShown() && !StateUtils.isMobileKeyboard(this.mActivity)) {
            this.mSearchBarData.getImm().showSoftInput(this.mAutoText, 1);
        }
    }

    void hideSoftInput() {
        InputMethodManager imm = this.mSearchBarData.getImm();
        if (this.mAutoText != null && imm != null) {
            imm.hideSoftInputFromWindow(this.mAutoText.getWindowToken(), 0);
        }
    }

    void onActivityResultParent(int requestCode, int resultCode, Intent data) {
        if (requestCode != 100) {
            return;
        }
        if (resultCode == -1 && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (!(results == null || this.mAutoText == null)) {
                this.mSearchBarData.setCurrentLocationListShowing(false);
                this.mSearchBarListViewModel.setSelectCurrentLocation(false);
                this.mAutoText.setText((CharSequence) results.get(0));
                this.mAutoText.setSelection(((String) results.get(0)).length());
            }
            showDropdownList();
        } else if (this.mSearchBarData.isDropdownListShown()) {
            showDropdownList();
        }
    }

    boolean onBackPressedParent(Context context) {
        if (!(this.mAutoText == null || !this.mAutoText.isInTouchMode() || StateUtils.isMobileKeyboard(context))) {
            setAutoTextCursor(false);
        }
        if (this.mSearchBarData.isDropdownListShown()) {
            hideDropdownList();
            if (this.mAutoText != null) {
                this.mAutoText.requestFocus();
            }
            return true;
        }
        cancelLocationToast();
        return false;
    }

    void releaseInstance() {
        this.mAutoText = null;
        this.mSearchBarView = null;
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mSearchBarLayout != null) {
            this.mSearchBarLayout.setBackgroundResource(0);
            this.mSearchBarLayout = null;
        }
    }

    private void setInputModeType(int type) {
        int mode = type | 1;
        if (this.mActivity.getParent() == null) {
            this.mActivity.getWindow().setSoftInputMode(mode);
        } else {
            this.mActivity.getParent().getWindow().setSoftInputMode(mode);
        }
    }

    void onSaveInstance(Bundle outState) {
        if (outState != null) {
            outState.putString("SearchBarLastString", this.mSearchBarData.getLastString());
            outState.putBoolean("SearchBarIsShowDropdownList", this.mSearchBarData.isDropdownListShown());
            outState.putBoolean("CurrentLocationList", this.mSearchBarData.isCurrentLocationListShowing());
        }
    }

    void onRestoreInstance(Bundle instance) {
        if (instance != null) {
            boolean isDropdownListShown = instance.getBoolean("SearchBarIsShowDropdownList", false);
            boolean isCurrentLocationListShown = instance.getBoolean("CurrentLocationList", false);
            this.mSearchBarData.setDropdownListShown(isDropdownListShown);
            this.mSearchBarData.setCurrentLocationListShowing(isCurrentLocationListShown);
            this.mSearchBarData.setLastString(instance.getString("SearchBarLastString", null));
            if (isDropdownListShown && !isCurrentLocationListShown) {
                setAutoTextCursor(true);
            }
            if (isCurrentLocationListShown) {
                findLocationByMnc(this.mActivity);
            }
        }
    }

    private void showDropdownList() {
        Log.secD("SearchBarViewModel", "showDropdownList()");
        ClockUtils.insertSaLog("113");
        if (this.mSearchBarLayout != null) {
            updateWeatherLogoZoomBtn();
            this.mSearchBarListViewModel.setVisibility(0);
            if (this.mSearchBarData.getModelType() == 1 && !StateUtils.isCustomTheme(this.mActivity)) {
                this.mSearchBarLayout.setBackgroundResource(C0836R.drawable.worldclock_search_field_bg);
            }
            this.mSearchBarData.setDropdownListShown(true);
        }
        this.mSearchBarViewListener.onHideAllPopup(false);
        setInputModeType(16);
        this.mSearchBarListViewModel.setIndexScrollVisibility();
    }

    private void hideDropdownList() {
        Log.secD("SearchBarViewModel", "hideDropdownList()");
        this.mHandler.removeCallbacksAndMessages(null);
        this.mSearchBarViewListener.setSgiVisibility(0);
        if (this.mSearchBarLayout != null) {
            this.mSearchBarLayout.setBackgroundResource(C0836R.drawable.worldclock_search_field_bg);
            this.mSearchBarListViewModel.setVisibility(8);
            this.mSearchBarData.setDropdownListShown(false);
            updateWeatherLogoZoomBtn();
        }
        setInputModeType(32);
    }

    void changeList() {
        this.mSearchBarListViewModel.changeList(this.mCurrentLocationList);
    }

    void initMassData(String cityCountryName, int homeZone, int widgetID) {
        this.mSearchBarListViewModel.initMassData(cityCountryName, homeZone, widgetID);
    }

    private void setAutoTextCursor(boolean flag) {
        this.mAutoText.setCursorVisible(flag);
        this.mAutoText.setLongClickable(flag);
    }

    private void updateWeatherLogoZoomBtn() {
        this.mSearchBarViewListener.onSetZoomInOutVisibility();
        this.mSearchBarViewListener.onUpdateWeatherLogo();
    }

    void cancelLocationToast() {
        if (this.mLocationButtonToast != null) {
            this.mLocationButtonToast.cancel();
            this.mLocationButtonToast = null;
        }
    }
}
