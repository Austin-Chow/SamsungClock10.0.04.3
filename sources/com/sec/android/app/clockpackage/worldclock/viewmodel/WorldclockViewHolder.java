package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.graphics.Typeface;
import android.support.constraint.Guideline;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextClock;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.common.view.CheckableConstraintLayout;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherInfoView;

public class WorldclockViewHolder extends ViewHolder {
    TextClock mAmPmTextClock;
    TextClock mAmPmTextClockLeft;
    CardView mCardView;
    CheckBox mCheckBox;
    Guideline mCityInfoGuideline;
    TextView mCityNameView;
    TextClock mDigitalClock;
    CheckableConstraintLayout mListItem;
    FrameLayout mReorderFrameLayout;
    TextView mTimeDiff;
    Guideline mTimeInfoGuideline;
    WeatherInfoView mWeatherInfoView;

    WorldclockViewHolder(View mView) {
        super(mView);
        this.mCardView = (CardView) mView.findViewById(C0836R.id.worldclock_cardview);
        this.mCheckBox = (CheckBox) mView.findViewById(C0836R.id.worldclock_checkbox);
        this.mTimeInfoGuideline = (Guideline) mView.findViewById(C0836R.id.time_info_guideline);
        this.mCityInfoGuideline = (Guideline) mView.findViewById(C0836R.id.city_info_guideline);
        this.mWeatherInfoView = (WeatherInfoView) mView.findViewById(C0836R.id.worldclock_list_item_weather_view);
        this.mCityNameView = (TextView) mView.findViewById(C0836R.id.item_city_name);
        this.mAmPmTextClock = (TextClock) mView.findViewById(C0836R.id.item_city_ampm);
        this.mAmPmTextClockLeft = (TextClock) mView.findViewById(C0836R.id.item_city_ampm_left);
        this.mDigitalClock = (TextClock) mView.findViewById(C0836R.id.item_city_clock);
        this.mTimeDiff = (TextView) mView.findViewById(C0836R.id.city_time_diff);
        this.mListItem = (CheckableConstraintLayout) mView.findViewById(C0836R.id.worldclock_list_item);
        this.mReorderFrameLayout = (FrameLayout) mView.findViewById(C0836R.id.list_reorder_layout);
        if (!StateUtils.isContextInDexMode(this.mCardView.getContext()) && !StateUtils.isCustomTheme(this.mCardView.getContext())) {
            this.mCardView.setBackground(this.mCardView.getContext().getDrawable(C0836R.drawable.contents_area_background));
            this.mListItem.setBackground(this.mListItem.getContext().getDrawable(C0836R.drawable.common_cardview_item_area_background));
        } else if (StateUtils.isContextInDexMode(this.mListItem.getContext())) {
            this.mListItem.setBackground(this.mListItem.getContext().getDrawable(C0836R.drawable.common_cardview_item_area_background_for_dexmode));
        }
        Typeface tf = ClockUtils.getFontFromOpenTheme(mView.getContext());
        if (tf == null) {
            tf = Typeface.create("sans-serif-light", 0);
        }
        this.mDigitalClock.setTypeface(tf);
    }
}
