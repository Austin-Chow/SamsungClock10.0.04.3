package com.sec.android.widgetapp.digitalclock;

import android.app.PendingIntent;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RemoteViews;
import android.widget.TextClock;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.widgetapp.digitalclock.DigitalClockContract.View;
import java.util.Locale;

public class DigitalClockView implements View {
    private RemoteViews mRemoteViews;

    public RemoteViews getRemoteViews() {
        return this.mRemoteViews;
    }

    public void inflate(Context context, int widgetSize) {
        this.mRemoteViews = new RemoteViews(context.getPackageName(), getLayoutId(context, widgetSize));
    }

    private int getLayoutId(Context context, int widgetSize) {
        if (widgetSize != 1) {
            return R.layout.digital_clock_full;
        }
        if (AppWidgetUtils.needThirdPartyLayout(context)) {
            return R.layout.digital_clock_third_party;
        }
        return R.layout.digital_clock;
    }

    public void setBackgroundColorFilter(int colorFilter) {
        getRemoteViews().setImageViewResource(R.id.widget_background, R.drawable.widget_background_white);
        getRemoteViews().setInt(R.id.widget_background, "setColorFilter", colorFilter);
    }

    public void setBackgroundImageAlpha(int alpha) {
        getRemoteViews().setInt(R.id.widget_background, "setImageAlpha", alpha);
    }

    public void setTextColor(int color) {
        getRemoteViews().setTextColor(R.id.clock_time, color);
        getRemoteViews().setTextColor(R.id.ampm_text_left, color);
        getRemoteViews().setTextColor(R.id.ampm_text, color);
        getRemoteViews().setTextColor(R.id.week_text, color);
        getRemoteViews().setTextColor(R.id.date_text_large, color);
        getRemoteViews().setTextColor(R.id.date_text_large_persian, color);
    }

    public void setTextSize(Context context, int widgetSize) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        if (inflater != null) {
            android.view.View digitalClockView = inflater.inflate(getLayoutId(context, widgetSize), null);
            TextClock clockTime = (TextClock) digitalClockView.findViewById(R.id.clock_time);
            TextClock ampmTextLeft = (TextClock) digitalClockView.findViewById(R.id.ampm_text_left);
            TextClock ampmText = (TextClock) digitalClockView.findViewById(R.id.ampm_text);
            TextClock weekText = (TextClock) digitalClockView.findViewById(R.id.week_text);
            TextClock dateTextLarge = (TextClock) digitalClockView.findViewById(R.id.date_text_large);
            TextClock dateTextLargePersian = (TextClock) digitalClockView.findViewById(R.id.date_text_large_persian);
            float density = context.getResources().getDisplayMetrics().density;
            float textRatio = StateUtils.isScreenDp(context, 457) ? 1.0f : 0.87f;
            if (clockTime != null) {
                getRemoteViews().setTextViewTextSize(R.id.clock_time, 1, (clockTime.getTextSize() / density) * textRatio);
            }
            if (ampmTextLeft != null) {
                getRemoteViews().setTextViewTextSize(R.id.ampm_text_left, 1, (ampmTextLeft.getTextSize() / density) * textRatio);
            }
            if (ampmText != null) {
                getRemoteViews().setTextViewTextSize(R.id.ampm_text, 1, (ampmText.getTextSize() / density) * textRatio);
            }
            if (dateTextLarge != null) {
                getRemoteViews().setTextViewTextSize(R.id.date_text_large, 1, (dateTextLarge.getTextSize() / density) * textRatio);
            }
            if (weekText != null) {
                getRemoteViews().setTextViewTextSize(R.id.week_text, 1, (weekText.getTextSize() / density) * textRatio);
            }
            Locale locale = Locale.getDefault();
            if (widgetSize != 2 || !Feature.isPersianCalendar()) {
                return;
            }
            if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage()) || ("fa".equalsIgnoreCase(locale.getLanguage()) && dateTextLargePersian != null)) {
                getRemoteViews().setTextViewTextSize(R.id.date_text_large_persian, 1, (dateTextLargePersian.getTextSize() / density) * textRatio);
            }
        }
    }

    public void setWidgetClickPendingIntent(PendingIntent pendingIntent) {
        getRemoteViews().setOnClickPendingIntent(R.id.digital_clock, pendingIntent);
    }
}
