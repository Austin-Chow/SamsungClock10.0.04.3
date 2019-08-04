package com.sec.android.widgetapp.dualclockdigital;

import android.app.PendingIntent;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RemoteViews;
import android.widget.TextClock;
import android.widget.TextView;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.widgetapp.dualclockdigital.DualClockDigitalContract.View;

public class DualClockDigitalView implements View {
    private RemoteViews mRemoteViews;
    private boolean mSuppported;

    public RemoteViews getRemoteViews() {
        return this.mRemoteViews;
    }

    public void inflate(Context context, String packageName, boolean isSetting, boolean suppported) {
        this.mSuppported = suppported;
        this.mRemoteViews = new RemoteViews(packageName, getLayoutId(context, isSetting));
    }

    private int getLayoutId(Context context, boolean isSetting) {
        if (this.mSuppported) {
            return AppWidgetUtils.needThirdPartyLayout(context) ? isSetting ? R.layout.dual_clock_digital_third_party_in_setting : R.layout.dual_clock_digital_third_party : isSetting ? R.layout.dual_clock_digital_in_setting : R.layout.dual_clock_digital;
        } else {
            return R.layout.dual_clock_digital_deprecated;
        }
    }

    public void setBackgroundColorFilter(int colorFilter) {
        getRemoteViews().setImageViewResource(R.id.dual_clock_widget_background, R.drawable.widget_background_white);
        getRemoteViews().setInt(R.id.dual_clock_widget_background, "setColorFilter", colorFilter);
    }

    public void setBackgroundImageAlpha(int alpha) {
        getRemoteViews().setInt(R.id.dual_clock_widget_background, "setImageAlpha", alpha);
    }

    public void setImageColorFilter(int colorFilter) {
        getRemoteViews().setImageViewResource(R.id.middle_line, R.drawable.dualclock_middleline);
        getRemoteViews().setInt(R.id.middle_line, "setColorFilter", colorFilter);
    }

    public void setTextColor(int color) {
        getRemoteViews().setTextColor(R.id.first_city_text, color);
        getRemoteViews().setTextColor(R.id.first_clock_text, color);
        getRemoteViews().setTextColor(R.id.first_ampm_left, color);
        getRemoteViews().setTextColor(R.id.first_ampm, color);
        getRemoteViews().setTextColor(R.id.date_text_first, color);
        getRemoteViews().setTextColor(R.id.second_set_city_text, color);
        getRemoteViews().setTextColor(R.id.second_city_text, color);
        getRemoteViews().setTextColor(R.id.second_clock_text, color);
        getRemoteViews().setTextColor(R.id.second_ampm_left, color);
        getRemoteViews().setTextColor(R.id.second_ampm, color);
        getRemoteViews().setTextColor(R.id.date_text_second, color);
        getRemoteViews().setTextColor(R.id.dual_clock_digital_deprecated, color);
    }

    public void setTextSize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        if (inflater != null) {
            android.view.View dualClockView = inflater.inflate(getLayoutId(context, true), null);
            if (this.mSuppported) {
                TextView firstCityText = (TextView) dualClockView.findViewById(R.id.first_city_text);
                TextClock firstClockText = (TextClock) dualClockView.findViewById(R.id.first_clock_text);
                TextClock firstAmPmLeft = (TextClock) dualClockView.findViewById(R.id.first_ampm_left);
                TextClock firstAmPm = (TextClock) dualClockView.findViewById(R.id.first_ampm);
                TextClock dateTextFirst = (TextClock) dualClockView.findViewById(R.id.date_text_first);
                TextView secondSetCityText = (TextView) dualClockView.findViewById(R.id.second_set_city_text);
                TextView secondCityText = (TextView) dualClockView.findViewById(R.id.second_city_text);
                TextClock secondClockText = (TextClock) dualClockView.findViewById(R.id.second_clock_text);
                TextClock secondAmPmLeft = (TextClock) dualClockView.findViewById(R.id.second_ampm_left);
                TextClock secondAmPm = (TextClock) dualClockView.findViewById(R.id.second_ampm);
                TextClock dateTextSecond = (TextClock) dualClockView.findViewById(R.id.date_text_second);
                float density = context.getResources().getDisplayMetrics().density;
                float textRatio = StateUtils.isScreenDp(context, 457) ? 1.0f : 0.87f;
                if (firstCityText != null) {
                    getRemoteViews().setTextViewTextSize(R.id.first_city_text, 1, (firstCityText.getTextSize() / density) * textRatio);
                }
                if (firstClockText != null) {
                    getRemoteViews().setTextViewTextSize(R.id.first_clock_text, 1, (firstClockText.getTextSize() / density) * textRatio);
                }
                if (firstAmPmLeft != null) {
                    getRemoteViews().setTextViewTextSize(R.id.first_ampm_left, 1, (firstAmPmLeft.getTextSize() / density) * textRatio);
                }
                if (firstAmPm != null) {
                    getRemoteViews().setTextViewTextSize(R.id.first_ampm, 1, (firstAmPm.getTextSize() / density) * textRatio);
                }
                if (dateTextFirst != null) {
                    getRemoteViews().setTextViewTextSize(R.id.date_text_first, 1, (dateTextFirst.getTextSize() / density) * textRatio);
                }
                if (secondSetCityText != null) {
                    getRemoteViews().setTextViewTextSize(R.id.second_set_city_text, 1, (secondSetCityText.getTextSize() / density) * textRatio);
                }
                if (secondCityText != null) {
                    getRemoteViews().setTextViewTextSize(R.id.second_city_text, 1, (secondCityText.getTextSize() / density) * textRatio);
                }
                if (secondClockText != null) {
                    getRemoteViews().setTextViewTextSize(R.id.second_clock_text, 1, (secondClockText.getTextSize() / density) * textRatio);
                }
                if (secondAmPmLeft != null) {
                    getRemoteViews().setTextViewTextSize(R.id.second_ampm_left, 1, (secondAmPmLeft.getTextSize() / density) * textRatio);
                }
                if (secondAmPm != null) {
                    getRemoteViews().setTextViewTextSize(R.id.second_ampm, 1, (secondAmPm.getTextSize() / density) * textRatio);
                }
                if (dateTextSecond != null) {
                    getRemoteViews().setTextViewTextSize(R.id.date_text_second, 1, (dateTextSecond.getTextSize() / density) * textRatio);
                }
            }
        }
    }

    public void setWidgetClickPendingIntent(int layoutId, PendingIntent pendingIntent) {
        getRemoteViews().setOnClickPendingIntent(layoutId, pendingIntent);
    }
}
