package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextClock;
import android.widget.TextView;
import com.samsung.android.view.animation.ElasticCustom;
import com.samsung.android.view.animation.SineInOut80;
import com.sec.android.app.clockpackage.common.callback.OnSingleClickListener;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.PopupViewListener;
import com.sec.android.app.clockpackage.worldclock.callback.WeatherHandlerListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.TimeZoneFinder;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherHandler;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherInfoView;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherUrlManager;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PopupViewModel {
    private Activity mActivity;
    private int mBeforeCityId;
    private int mCityPopupViewHeight;
    private int mCityPopupViewWidth;
    private Context mContext;
    private boolean mIsCityInfoPopupShowing = false;
    private boolean mIsExternal;
    private int mLatestUniqueId = -1;
    private CardView mPopupLayout;
    private View mPopupView;
    private PopupViewListener mPopupViewListener;
    private int mTalkBackSet = 0;
    private TimeZoneFinder mTimeZoneFinder;
    private WeatherHandler mWeatherHandler;
    private WorldclockCityWeatherInfo mWeatherInfo = new WorldclockCityWeatherInfo();
    private WeatherInfoView mWeatherView;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.PopupViewModel$1 */
    class C08581 implements WeatherHandlerListener {
        C08581() {
        }

        public void onTimeOut(int cityId, int position) {
            PopupViewModel.this.mWeatherHandler.stopThreadAndMessage();
            if (cityId != -1) {
                PopupViewModel.this.mBeforeCityId = -1;
                PopupViewModel.this.mWeatherView.setVisibleWeatherReloadIcon();
                PopupViewModel.this.mWeatherInfo.setCurrentState(1);
                PopupViewModel.this.setWeatherListener(cityId, null);
            }
        }

        public void onSaveData(Object obj, int position) {
            PopupViewModel.this.mWeatherInfo = (WorldclockCityWeatherInfo) ((ArrayList) obj).get(0);
            PopupViewModel.this.mWeatherHandler.stopThreadAndMessage();
            if (PopupViewModel.this.mWeatherInfo != null) {
                PopupViewModel.this.mWeatherInfo.setCurrentState(2);
                PopupViewModel.this.mWeatherView.setDisplayWeatherData(PopupViewModel.this.mWeatherInfo.getCurrentTemperature(), PopupViewModel.this.mWeatherInfo.getWeatherIconNum(), PopupViewModel.this.mWeatherInfo.getWeatherDescription(), PopupViewModel.this.mWeatherInfo.getDayOrNight());
                PopupViewModel.this.mWeatherView.setContentDescription(WorldclockWeatherUtils.getWeatherLayoutDescription(PopupViewModel.this.mActivity, PopupViewModel.this.mWeatherInfo));
                PopupViewModel.this.setWeatherListener(-1, PopupViewModel.this.mWeatherInfo.getMobileLink());
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.PopupViewModel$3 */
    class C08603 implements AnimationListener {
        C08603() {
        }

        public void onAnimationStart(Animation animation) {
            PopupViewModel.this.updateCityInfoLayout();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            PopupViewModel.this.setUniversalSwitchForCityInformationPopup();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.PopupViewModel$4 */
    class C08614 implements AnimationListener {
        C08614() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            PopupViewModel.this.mPopupLayout.setVisibility(8);
        }
    }

    PopupViewModel(Activity activity, PopupViewListener popupViewListener) {
        this.mActivity = activity;
        this.mContext = this.mActivity.getApplicationContext();
        this.mPopupViewListener = popupViewListener;
        initWeatherHandler();
        this.mTimeZoneFinder = new TimeZoneFinder(this.mActivity.getApplicationContext());
        this.mTimeZoneFinder.updateCitiesIdFromIso();
    }

    private void initWeatherHandler() {
        this.mWeatherHandler = new WeatherHandler(this.mActivity, new C08581());
    }

    void initPopup() {
        LinearLayout popupViewLayoutId = (LinearLayout) this.mActivity.findViewById(C0836R.id.pupup_view_layout_id);
        LayoutInflater inflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        if (this.mPopupView == null && inflater != null) {
            this.mPopupView = inflater.inflate(C0836R.layout.worldclock_popup_view_layout, null);
            popupViewLayoutId.addView(this.mPopupView);
            this.mWeatherView = (WeatherInfoView) this.mPopupView.findViewById(C0836R.id.worldclock_popup_weather_view);
            this.mPopupLayout = (CardView) this.mPopupView.findViewById(C0836R.id.worldclock_popup_cardview);
            this.mPopupLayout.setVisibility(8);
        }
        if (this.mIsCityInfoPopupShowing) {
            updateCityInfoLayout();
            updateCityInfoPopupTime();
        }
    }

    private void updateCityInfoLayout() {
        City city = CityManager.findCityByUniqueId(Integer.valueOf(this.mPopupViewListener.onGetCityUniqueIdSel()));
        this.mPopupView.setLayoutParams(new LayoutParams(this.mCityPopupViewWidth, this.mCityPopupViewHeight));
        updateLayoutMargin(city, city == null ? 4 : city.getArrowDirection());
    }

    void showCityInfoPopup(int uniqueId, boolean withAnimation, Bundle instance, String fromWhere, final int actionType) {
        hideAllPopup(true);
        this.mPopupViewListener.onShowCityUnderSelection(uniqueId);
        this.mLatestUniqueId = uniqueId;
        this.mIsCityInfoPopupShowing = true;
        this.mIsExternal = WorldclockUtils.isFromExternal(fromWhere);
        updateCityPopupLayoutHeight(actionType, StateUtils.isMultiWindowMinSize(this.mActivity, 293, false));
        updateCityPopupTextSize(StateUtils.isMultiWindowMinSize(this.mActivity, 293, false));
        if (this.mPopupView != null) {
            updateActionText(actionType);
            updateWeather(uniqueId, instance);
            setCityInfoPopup();
            final TextView addButton = (TextView) this.mPopupView.findViewById(C0836R.id.worldclock_popup_add_button);
            addButton.semSetHoverPopupType(0);
            addButton.semSetButtonShapeEnabled(true);
            addButton.setOnClickListener(new OnSingleClickListener() {
                public void onSingleClick(View view) {
                    Log.secD("PopupViewModel", "addCityView onClick mActionType : " + actionType);
                    addButton.clearFocus();
                    City city = CityManager.findCityByUniqueId(Integer.valueOf(PopupViewModel.this.mPopupViewListener.onGetCityUniqueIdSel()));
                    if (city == null) {
                        Log.secD("PopupViewModel", "addCityView onClick city is null");
                        return;
                    }
                    PopupViewModel.this.mPopupViewListener.onSaveDB(city, PopupViewModel.this.mWeatherInfo);
                    PopupViewModel.this.mPopupViewListener.onHideSoftInput();
                    PopupViewModel.this.hideAllPopup(false);
                    PopupViewModel.this.mPopupViewListener.onShowCityUnderSelection(-1);
                    if (actionType == 1) {
                        ClockUtils.insertSaLog("112", "1272");
                    } else if (actionType == 2) {
                        ClockUtils.insertSaLog("112", "1273");
                    }
                }
            });
            if (withAnimation) {
                startPopupAnimation();
                return;
            }
            updateCityInfoLayout();
            this.mPopupLayout.setVisibility(0);
        }
    }

    private void startPopupAnimation() {
        if (this.mPopupLayout != null) {
            this.mPopupLayout.setVisibility(0);
            ElasticCustom elastic = new ElasticCustom(1.0f, 0.8f);
            Animation bounceAnimation = AnimationUtils.loadAnimation(this.mActivity.getApplicationContext(), C0836R.anim.animation_poping);
            bounceAnimation.setInterpolator(elastic);
            bounceAnimation.setAnimationListener(new C08603());
            this.mPopupLayout.startAnimation(bounceAnimation);
        }
    }

    private void setUniversalSwitchForCityInformationPopup() {
        int universalSwitchSet = Secure.getInt(this.mContext.getContentResolver(), "universal_switch_enabled", 0);
        if (this.mTalkBackSet == 1 && universalSwitchSet == 0) {
            setCityInfoPopup();
            this.mPopupLayout.requestFocus();
            this.mPopupLayout.performAccessibilityAction(64, null);
        }
    }

    void clearCityPopupTalkbackFocus() {
        if (this.mPopupLayout != null && this.mPopupLayout.getVisibility() == 0 && this.mTalkBackSet == 1) {
            this.mPopupLayout.performAccessibilityAction(128, null);
        }
    }

    private void updateLayoutMargin(City city, int cityArrowDirection) {
        Log.secD("PopupViewModel", "updateLayoutMarginPort. cityArrowDirection : " + cityArrowDirection);
        if (this.mPopupView != null && this.mPopupLayout != null) {
            boolean needCenterAlign;
            LayoutParams popupParams = (LayoutParams) this.mPopupView.getLayoutParams();
            if (StateUtils.isMultiWindowMinSize(this.mActivity, 578, false) || StateUtils.isMobileKeyboard(this.mContext)) {
                needCenterAlign = true;
            } else {
                needCenterAlign = false;
            }
            if (needCenterAlign) {
                popupParams.bottomMargin = 0;
            } else {
                popupParams.bottomMargin = this.mCityPopupViewHeight + ((((int) this.mPopupViewListener.onCityCardOffset(city)) + this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_city_popup_margin_bottom_gap)) * 2);
            }
            this.mPopupLayout.setLayoutParams(popupParams);
        }
    }

    private void updateCityPopupLayoutHeight(int actionType, boolean isMinSize) {
        Log.secD("PopupViewModel", "Popup Card actionType : " + actionType);
        int resId = isMinSize ? C0836R.dimen.worldclock_popup_layout_height_for_smallest : C0836R.dimen.worldclock_popup_layout_height;
        if (actionType == 0) {
            resId = isMinSize ? C0836R.dimen.worldclock_popup_no_action_height_for_smallest : C0836R.dimen.worldclock_popup_no_action_height;
        }
        this.mCityPopupViewHeight = this.mActivity.getResources().getDimensionPixelOffset(resId);
        this.mCityPopupViewWidth = this.mActivity.getResources().getDimensionPixelOffset(C0836R.dimen.worldclock_popup_layout_width);
        resId = isMinSize ? C0836R.dimen.worldclock_popup_city_name_margin_top_for_smallest : C0836R.dimen.worldclock_popup_city_name_margin_top;
        if (actionType == 0) {
            resId = isMinSize ? C0836R.dimen.worldclock_popup_city_name_margin_top_for_smallest_type_none : C0836R.dimen.worldclock_popup_city_name_margin_top;
        }
        ((ConstraintLayout.LayoutParams) this.mPopupView.findViewById(C0836R.id.worldclock_popup_city_name).getLayoutParams()).topMargin = this.mActivity.getResources().getDimensionPixelSize(resId);
        ((ConstraintLayout.LayoutParams) this.mPopupView.findViewById(C0836R.id.worldclock_popup_city_name_space).getLayoutParams()).bottomMargin = this.mActivity.getResources().getDimensionPixelSize(isMinSize ? C0836R.dimen.worldclock_popup_space_margin_bottom_for_smallest : C0836R.dimen.worldclock_popup_space_margin_bottom);
        ((ConstraintLayout.LayoutParams) this.mPopupView.findViewById(C0836R.id.worldclock_popup_time_space).getLayoutParams()).bottomMargin = this.mActivity.getResources().getDimensionPixelSize(isMinSize ? C0836R.dimen.worldclock_popup_space_margin_bottom_for_smallest : C0836R.dimen.worldclock_popup_space_margin_bottom);
        ((ConstraintLayout.LayoutParams) this.mPopupView.findViewById(C0836R.id.worldclock_popup_divider).getLayoutParams()).topMargin = this.mActivity.getResources().getDimensionPixelSize(isMinSize ? C0836R.dimen.worldclock_popup_divider_margin_top_for_smallest : C0836R.dimen.worldclock_popup_divider_margin_top);
        ConstraintLayout.LayoutParams weatherParams = (ConstraintLayout.LayoutParams) this.mPopupView.findViewById(C0836R.id.worldclock_popup_weather_view).getLayoutParams();
        weatherParams.bottomMargin = this.mActivity.getResources().getDimensionPixelSize(isMinSize ? C0836R.dimen.worldclock_popup_weather_margin_bottom_for_smallest : C0836R.dimen.worldclock_popup_weather_margin_bottom);
        weatherParams.height = this.mActivity.getResources().getDimensionPixelSize(isMinSize ? C0836R.dimen.worldclock_popup_weather_layout_height_for_smallest : C0836R.dimen.worldclock_popup_weather_layout_height);
    }

    private void updateCityPopupTextSize(boolean isMinSize) {
        if (this.mPopupView != null) {
            int textSize;
            TextClock textClock = (TextClock) this.mPopupView.findViewById(C0836R.id.worldclock_popup_time);
            if (isMinSize) {
                textSize = this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_digitalclock_textview_textsize_smallest);
            } else {
                textSize = this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_digitalclock_textview_textsize);
            }
            textClock.setTextSize(0, (float) textSize);
        }
    }

    private void updateActionText(int actionType) {
        TextView addButton = (TextView) this.mPopupView.findViewById(C0836R.id.worldclock_popup_add_button);
        View divider = this.mPopupView.findViewById(C0836R.id.worldclock_popup_divider);
        divider.setVisibility(0);
        addButton.setVisibility(0);
        addButton.semSetHoverPopupType(0);
        if (actionType == 0) {
            addButton.setVisibility(8);
            divider.setVisibility(4);
        } else if (actionType == 2) {
            addButton.setText(this.mActivity.getString(C0836R.string.worldclock_change_city));
            addButton.setContentDescription(this.mActivity.getString(C0836R.string.worldclock_change_city));
        } else {
            addButton.setText(this.mActivity.getString(C0836R.string.add));
            addButton.setContentDescription(this.mActivity.getString(C0836R.string.add));
        }
    }

    private void hideCityInfoPopup(boolean withAnimation) {
        if (this.mPopupLayout != null && this.mIsCityInfoPopupShowing && this.mPopupLayout.getVisibility() == 0) {
            this.mLatestUniqueId = -1;
            this.mIsCityInfoPopupShowing = false;
            if (withAnimation) {
                this.mPopupView.clearAnimation();
                Animation bounceAnimation = AnimationUtils.loadAnimation(this.mActivity.getApplicationContext(), C0836R.anim.animation_hiding);
                bounceAnimation.setInterpolator(new SineInOut80());
                bounceAnimation.setAnimationListener(new C08614());
                this.mPopupLayout.startAnimation(bounceAnimation);
                return;
            }
            this.mPopupLayout.setVisibility(8);
        }
    }

    void hideAllPopup(boolean withAnimation) {
        this.mWeatherHandler.stopThreadAndMessage();
        if (this.mWeatherView != null) {
            this.mWeatherView.setVisibility(8);
        }
        if (this.mPopupLayout.getVisibility() == 0) {
            hideCityInfoPopup(withAnimation);
        }
    }

    private void setCityInfoPopup() {
        updateCityInfoPopupTime();
        TextView cityNameTextView = (TextView) this.mPopupLayout.findViewById(C0836R.id.worldclock_popup_city_name);
        City city = CityManager.findCityByUniqueId(Integer.valueOf(this.mPopupViewListener.onGetCityUniqueIdSel()));
        cityNameTextView.setText(city != null ? city.getName() : "");
        ClockUtils.setLargeTextSize(this.mActivity, cityNameTextView, this.mActivity.getResources().getDimension(C0836R.dimen.worldclock_popup_title_textview_textsize));
    }

    void updateCityInfoPopupTime() {
        if (this.mPopupView != null) {
            TextClock amPm;
            int uniqueId = this.mPopupViewListener.onGetCityUniqueIdSel();
            City city = CityManager.findCityByUniqueId(Integer.valueOf(uniqueId));
            TimeZone tz = city == null ? TimeZone.getDefault() : city.getTimeZone();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeZone(tz);
            if (StateUtils.isLeftAmPm()) {
                this.mPopupView.findViewById(C0836R.id.worldclock_popup_ampm).setVisibility(8);
                amPm = (TextClock) this.mPopupView.findViewById(C0836R.id.worldclock_popup_ampm_left);
            } else {
                this.mPopupView.findViewById(C0836R.id.worldclock_popup_ampm_left).setVisibility(8);
                amPm = (TextClock) this.mPopupView.findViewById(C0836R.id.worldclock_popup_ampm);
            }
            if (amPm.is24HourModeEnabled()) {
                amPm.setVisibility(8);
            } else {
                amPm.setVisibility(0);
            }
            amPm.setTimeZone(calendar.getTimeZone().getID());
            ((TextClock) this.mPopupView.findViewById(C0836R.id.worldclock_popup_time)).setTimeZone(calendar.getTimeZone().getID());
            TextView timeDiff = (TextView) this.mPopupView.findViewById(C0836R.id.worldclock_popup_time_diff);
            WorldclockUtils.getCityDayTimeDiffString(this.mActivity.getApplicationContext(), this.mTimeZoneFinder.isLocalCity(uniqueId), -1, timeDiff, (TextView) this.mPopupView.findViewById(C0836R.id.worldclock_popup_day_diff), tz, false);
            if (timeDiff == null) {
                return;
            }
            if (this.mWeatherView == null || this.mWeatherView.getVisibility() != 0) {
                timeDiff.setMaxWidth(this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_layout_width) - (this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_item_padding_start) * 2));
            } else {
                timeDiff.setMaxWidth((this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_layout_width) - this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_weather_layout_width)) - this.mActivity.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_popup_item_padding_start));
            }
        }
    }

    public void releaseInstance() {
        this.mPopupView = null;
    }

    void updateWeather(int currentCityId, Bundle instance) {
        if (!WorldclockWeatherUtils.isDisableWeather(this.mContext) && !this.mIsExternal && this.mWeatherView != null) {
            this.mWeatherView.setVisibility(0);
            if (instance != null) {
                this.mWeatherInfo = (WorldclockCityWeatherInfo) instance.getParcelable("WorldclockSaveWeatherInfoKey");
                this.mWeatherHandler.stopThreadAndMessage();
                if (this.mWeatherInfo != null) {
                    this.mWeatherView.setDisplayWeatherData(this.mWeatherInfo.getCurrentTemperature(), this.mWeatherInfo.getWeatherIconNum(), this.mWeatherInfo.getWeatherDescription(), this.mWeatherInfo.getDayOrNight());
                    this.mWeatherView.setContentDescription(WorldclockWeatherUtils.getWeatherLayoutDescription(this.mActivity, this.mWeatherInfo));
                    setWeatherListener(-1, this.mWeatherInfo.getMobileLink());
                }
                this.mBeforeCityId = currentCityId;
                return;
            }
            Log.secD("PopupViewModel", "currentCityId : " + currentCityId + " , mBeforeCityId : " + this.mBeforeCityId);
            if (currentCityId != this.mBeforeCityId) {
                City city = CityManager.findCityByUniqueId(Integer.valueOf(currentCityId));
                if (city != null) {
                    ArrayList<String> placeId = new ArrayList();
                    placeId.add(city.getCityPlaceId());
                    new Handler().postDelayed(PopupViewModel$$Lambda$1.lambdaFactory$(this), 50);
                    this.mBeforeCityId = currentCityId;
                    this.mWeatherInfo.setCurrentState(3);
                    this.mWeatherHandler.sendMessageDelayed(100, 0, -1, Integer.valueOf(currentCityId));
                    this.mWeatherHandler.sendMessage(800, currentCityId, -1, placeId.clone());
                }
            }
        } else if (this.mWeatherView != null) {
            this.mWeatherView.setVisibility(8);
        }
    }

    private /* synthetic */ void lambda$updateWeather$0() {
        this.mWeatherView.startWeatherProgress();
    }

    void onSaveInstance(Bundle outState) {
        if (outState != null && this.mIsCityInfoPopupShowing) {
            outState.putBoolean("IsShowCityPopup", true);
            if (CityManager.findCityByUniqueId(Integer.valueOf(this.mLatestUniqueId)) != null) {
                outState.putBoolean("CurrentLocationPopup", CityManager.findCityByUniqueId(Integer.valueOf(this.mLatestUniqueId)).getDBCurrentLocation());
            }
            outState.putInt("CityPopupLastCityUniqueId", this.mLatestUniqueId);
            outState.putParcelable("WorldclockSaveWeatherInfoKey", this.mWeatherInfo);
        }
    }

    boolean isShowCityPopup() {
        return this.mIsCityInfoPopupShowing;
    }

    private void setWeatherListener(final int cityId, final String mobileLink) {
        this.mWeatherView.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View v) {
                if (cityId > -1) {
                    PopupViewModel.this.updateWeather(cityId, null);
                } else if (mobileLink != null) {
                    try {
                        Uri uri = new WeatherUrlManager().setViewUri("DETAIL_HOME", mobileLink);
                        Log.secD("PopupViewModel", "Weather View onSingleClick() => final uri : " + uri);
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(uri);
                        PopupViewModel.this.mContext.startActivity(intent);
                        ClockUtils.insertSaLog("112", "1271");
                    } catch (ActivityNotFoundException e) {
                        Log.secE("PopupViewModel", "onSingleClick Weather View :" + e.toString());
                    }
                }
            }
        });
    }
}
