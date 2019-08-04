package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.ListItem;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneConvertorListAdapter extends Adapter<TimeZoneConvertorViewHolder> {
    List<ListItem> mCityItems;
    private Context mContext;

    public static class TimeZoneConvertorViewHolder extends ViewHolder {
        private TextView mCityNameView;
        private TextView mConvertorAmPmTextView;
        private TextView mConvertorClockTextView;
        private TextView mConvertorDate;
        private LinearLayout mTimeLayout;

        TimeZoneConvertorViewHolder(View mView) {
            super(mView);
            this.mCityNameView = (TextView) mView.findViewById(C0836R.id.item_city_name);
            this.mConvertorDate = (TextView) mView.findViewById(C0836R.id.timezone_convertor_date);
            this.mTimeLayout = (LinearLayout) mView.findViewById(C0836R.id.timezone_convertor_list_item_time_layout);
            if (StateUtils.isLeftAmPm()) {
                mView.findViewById(C0836R.id.timezone_convertor_ampm_textview).setVisibility(8);
                this.mConvertorAmPmTextView = (TextView) mView.findViewById(C0836R.id.timezone_convertor_ampm_left_textview);
            } else {
                mView.findViewById(C0836R.id.timezone_convertor_ampm_left_textview).setVisibility(8);
                this.mConvertorAmPmTextView = (TextView) mView.findViewById(C0836R.id.timezone_convertor_ampm_textview);
            }
            this.mConvertorClockTextView = (TextView) mView.findViewById(C0836R.id.timezone_convertor_time);
            Typeface tf = ClockUtils.getFontFromOpenTheme(mView.getContext());
            if (tf == null) {
                tf = Typeface.create("sans-serif-light", 0);
            }
            this.mConvertorClockTextView.setTypeface(tf);
        }
    }

    public TimeZoneConvertorListAdapter(Context context, List<ListItem> objects) {
        this.mCityItems = objects;
        this.mContext = context;
    }

    public TimeZoneConvertorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimeZoneConvertorViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0836R.layout.worldclock_timezone_convertor_list_item_layout, parent, false));
    }

    public void onBindViewHolder(TimeZoneConvertorViewHolder timeZoneConvertorViewHolder, int position) {
        setConvertorInfo(timeZoneConvertorViewHolder, (ListItem) this.mCityItems.get(position), ((ListItem) this.mCityItems.get(position)).getTimeZone());
        setSelectedItemTextStyle(timeZoneConvertorViewHolder, (ListItem) this.mCityItems.get(position));
    }

    public int getItemCount() {
        return this.mCityItems.size();
    }

    void setConvertorInfo(TimeZoneConvertorViewHolder holder, ListItem item, TimeZone tz) {
        String time;
        String minuteFormat = "mm";
        String amPmFormat = "a";
        String dateFormat = "EEE d MMM";
        String dateFormatYear = "yyyy EEE d MMM";
        String dateFormatFull = "EEEE d MMMM";
        String yearFormat = "yyyy";
        String cityCountrySel = CityManager.findCityCountryNameByUniqueId(Integer.valueOf(item.getUniqueId()));
        String hour12Format = this.mContext.getResources().getString(C0836R.string.time_12_hour_format);
        String hour24Format = this.mContext.getResources().getString(C0836R.string.time_24_hour_format);
        String timeSeparator = ClockUtils.getTimeSeparatorText(this.mContext);
        String defaultYear = getDateOfTimeZone(TimeZone.getDefault(), 0, 11, "yyyy");
        boolean bNeedDisplayYear = (defaultYear == null || defaultYear.equalsIgnoreCase(getDateOfTimeZone(tz, item.getHourDiff(), 11, "yyyy"))) ? false : true;
        String amPm = getTimeOfTimeZone(tz, item, 11, 12, "a");
        String hour = getTimeOfTimeZone(tz, item, 10, 12, hour12Format);
        String hourOfDay = getTimeOfTimeZone(tz, item, 11, 12, hour24Format);
        String minute = getTimeOfTimeZone(tz, item, 11, 12, "mm");
        int hourDiff = item.getHourDiff();
        if (!bNeedDisplayYear) {
            dateFormatYear = dateFormat;
        }
        String date = getDateOfTimeZone(tz, hourDiff, 11, dateFormatYear);
        String dateDesc = getDateOfTimeZone(tz, item.getHourDiff(), 11, "EEEE d MMMM");
        if (cityCountrySel != null) {
            holder.mCityNameView.setText(cityCountrySel.split(" / ")[0]);
        }
        if (DateFormat.is24HourFormat(this.mContext)) {
            holder.mConvertorAmPmTextView.setVisibility(8);
            time = hourOfDay + timeSeparator + minute;
        } else {
            holder.mConvertorAmPmTextView.setVisibility(0);
            holder.mConvertorAmPmTextView.setText(amPm);
            int hourNum = Integer.parseInt(hour);
            if (hourNum / 10 == 0) {
                hour = ClockUtils.toDigitString(hourNum);
            }
            if (Locale.getDefault().getLanguage().equals(Locale.JAPAN.getLanguage()) && hourNum % 12 == 0) {
                hour = ClockUtils.toDigitString(0);
            }
            time = hour + timeSeparator + minute;
        }
        holder.mConvertorDate.setText(date);
        holder.mConvertorDate.setContentDescription(dateDesc);
        holder.mConvertorClockTextView.setText(time);
        holder.mTimeLayout.setPaddingRelative(0, 0, this.mContext.getResources().getDimensionPixelOffset(C0836R.dimen.f23xef71db1f), 0);
        ClockUtils.setLargeTextSize(this.mContext, holder.mCityNameView, (float) this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_city_name_text_size));
        ClockUtils.setLargeTextSize(this.mContext, holder.mConvertorDate, (float) this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_timezone_convertor_date_text_size));
        if (Feature.isTablet(this.mContext)) {
            ClockUtils.setLargeTextSize(this.mContext, holder.mConvertorAmPmTextView, (float) this.mContext.getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_ampm_text_size));
        }
    }

    private void setSelectedItemTextStyle(TimeZoneConvertorViewHolder holder, ListItem item) {
        if (item.getSelected()) {
            holder.mCityNameView.setTypeface(Typeface.create("sec-roboto-light", 1));
            holder.mCityNameView.setTextColor(this.mContext.getColor(C0836R.color.selected_item_city_name_color));
            holder.mConvertorAmPmTextView.setTextColor(this.mContext.getColor(C0836R.color.selected_item_city_name_color));
            holder.mConvertorClockTextView.setTextColor(this.mContext.getColor(C0836R.color.selected_item_city_name_color));
            holder.mConvertorDate.setTextColor(this.mContext.getColor(C0836R.color.selected_item_city_name_color));
            return;
        }
        holder.mCityNameView.setTypeface(Typeface.create("sec-roboto-light", 0));
        holder.mCityNameView.setTextColor(this.mContext.getColor(C0836R.color.worldclock_list_text_color));
        holder.mConvertorAmPmTextView.setTextColor(this.mContext.getColor(C0836R.color.worldclock_list_text_color));
        holder.mConvertorClockTextView.setTextColor(this.mContext.getColor(C0836R.color.worldclock_list_text_color));
        holder.mConvertorDate.setTextColor(this.mContext.getColor(C0836R.color.worldclock_list_text_color));
    }

    private String getTimeOfTimeZone(TimeZone tz, ListItem item, int typeHour, int typeMin, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(tz);
        calendar.set(typeHour, item.getStartHour());
        calendar.set(typeMin, item.getStartMin());
        calendar.add(typeHour, item.getHourDiff());
        calendar.add(typeMin, item.getMinDiff());
        dateFormat.setCalendar(calendar);
        return dateFormat.format(dateFormat.getCalendar().getTime());
    }

    @SuppressLint({"SimpleDateFormat"})
    private String getDateOfTimeZone(TimeZone tz, int diffTime, int type, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), format));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(tz);
        calendar.add(type, diffTime);
        dateFormat.setCalendar(calendar);
        return dateFormat.format(dateFormat.getCalendar().getTime());
    }
}
