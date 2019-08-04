package com.sec.android.widgetapp.analogclock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.widgetapp.analogclock.AnalogClockContract.View;
import com.sec.android.widgetapp.analogclock.AnalogClockContract.ViewModel;
import java.util.Locale;

public class AnalogClockViewModel implements ViewModel {
    private View mView;

    private AnalogClockViewModel() {
        attachView(new AnalogClockView());
    }

    public void attachView(View view) {
        this.mView = view;
    }

    public RemoteViews getRemoteViews() {
        return this.mView.getRemoteViews();
    }

    public void refresh(Context context, int appWidgetId) {
        this.mView.inflate(context);
        Log.secD("AnalogClockViewModel", "refresh appWidgetId : " + appWidgetId);
        setFullDateText(getRemoteViews(), R.id.analog_clock_full_date_view);
        setFullDateText(getRemoteViews(), R.id.analog_clock_full_date_view_white_wallpaper);
        setWeekText(getRemoteViews(), R.id.analog_clock_week_view);
        setWeekText(getRemoteViews(), R.id.analog_clock_week_view_white_wallpaper);
        setDateText(getRemoteViews(), R.id.analog_clock_date_view);
        setDateText(getRemoteViews(), R.id.analog_clock_date_view_white_wallpaper);
        this.mView.setDateViewVisibility(context);
        setDatePosition(context, appWidgetId);
        this.mView.setAnalogClockResource(context);
        Intent showClockPackage = new Intent("com.sec.android.widgetapp.analogclock.SHOW_CLOCKPACKAGE");
        showClockPackage.setPackage("com.sec.android.app.clockpackage");
        this.mView.setWidgetClickPendingIntent(PendingIntent.getBroadcast(context, appWidgetId, showClockPackage, 0));
    }

    static void updateAnalogClock(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = getView(context, appWidgetId);
            if (views != null) {
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    private static RemoteViews getView(Context context, int appWidgetId) {
        ViewModel viewModel = new AnalogClockViewModel();
        viewModel.refresh(context, appWidgetId);
        return viewModel.getRemoteViews();
    }

    private void setFullDateText(RemoteViews v, int viewId) {
        Log.secD("AnalogClockViewModel", "setFullDateText viewId : " + viewId);
        if (Locale.getDefault().getLanguage().equals(Locale.KOREA.getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE d MMMM"));
        } else if ("de".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "my".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "bo".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMMM"));
        } else if ("lt".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMMM EEE"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM-d-EEE"));
        } else {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE d MMMM"));
        }
    }

    private void setWeekText(RemoteViews v, int viewId) {
        if ("TW".equalsIgnoreCase(Locale.getDefault().getLanguage()) || ("HK".equalsIgnoreCase(Locale.getDefault().getLanguage()) && !"zh-Hans-HK".equalsIgnoreCase(Locale.getDefault().toLanguageTag()))) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE"));
        } else if ("ja".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMMM"));
        } else {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE"));
        }
    }

    private void setDateText(RemoteViews v, int viewId) {
        Log.secD("AnalogClockViewModel", "setDateText viewId : " + viewId);
        if ("ja".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE"));
        } else if ("iw".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMM"));
        } else {
            v.setCharSequence(viewId, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMMM"));
            v.setCharSequence(viewId, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), "d MMMM"));
        }
    }

    private void setDatePosition(Context context, int appWidgetId) {
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        this.mView.setDatePosition(context, awm.getAppWidgetOptions(appWidgetId).getInt("appWidgetMinHeight", -1) - awm.getAppWidgetOptions(appWidgetId).getInt("appWidgetMinWidth", -1));
    }
}
