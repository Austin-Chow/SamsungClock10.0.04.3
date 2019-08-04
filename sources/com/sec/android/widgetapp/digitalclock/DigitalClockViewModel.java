package com.sec.android.widgetapp.digitalclock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.text.format.DateFormat;
import android.view.Display;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.widgetapp.digitalclock.DigitalClockContract.View;
import com.sec.android.widgetapp.digitalclock.DigitalClockContract.ViewModel;
import java.util.Locale;

public class DigitalClockViewModel implements ViewModel {
    private DigitalClockWidgetModel mModel;
    private String mSkeletonDate = "d MMMM";
    private String mSkeletonWeek12H;
    private String mSkeletonWeek24H;
    private View mView;

    public DigitalClockViewModel(DigitalClockWidgetModel model) {
        this.mModel = model;
        attachView(new DigitalClockView());
    }

    public void attachView(View view) {
        this.mView = view;
    }

    public void refresh(Context context, int appWidgetId, boolean isSetting) {
        refresh(context, appWidgetId, this.mModel, isSetting);
    }

    public void refresh(Context context, int appWidgetId, DigitalClockWidgetModel model, boolean isSetting) {
        boolean isPortrait;
        Display display = ((DisplayManager) context.getSystemService("display")).getDisplay(0);
        if (display.getRotation() == 0 || display.getRotation() == 3) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }
        this.mView.inflate(context, model.getWidgetSize());
        drawDateText(context, getRemoteViews(), model.getWidgetSize(), isPortrait);
        drawAmPm(getRemoteViews());
        this.mView.setBackgroundColorFilter(model.getBackgroundColor());
        this.mView.setBackgroundImageAlpha(model.getTransparency());
        this.mView.setTextColor(model.getTextColor());
        if (isSetting) {
            this.mView.setTextSize(context, model.getWidgetSize());
            return;
        }
        Intent showClockPackage = new Intent("com.sec.android.widgetapp.digitalclock.SHOW_CLOCKPACKAGE");
        showClockPackage.setPackage("com.sec.android.app.clockpackage");
        this.mView.setWidgetClickPendingIntent(PendingIntent.getBroadcast(context, appWidgetId, showClockPackage, 0));
        if (AppWidgetUtils.canSetFlagForClockWidgetTextViewWidth()) {
            getRemoteViews().setBoolean(R.id.clock_time, "setFlagForClockWidgetTextViewWidth", true);
        }
    }

    public RemoteViews getRemoteViews() {
        return this.mView.getRemoteViews();
    }

    public void setTransparency(Context context, int theme, int transparency) {
        DigitalClockDataManager.setTransparency(context, this.mModel, theme, transparency);
    }

    static void updateDigitalClock(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = getView(context, appWidgetId);
            if (views != null) {
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    private static RemoteViews getView(Context context, int appWidgetId) {
        ViewModel viewModel = new DigitalClockViewModel(DigitalClockDataManager.loadModel(context, appWidgetId, AppWidgetUtils.getWidgetSize(context, appWidgetId)));
        viewModel.refresh(context, appWidgetId, false);
        return viewModel.getRemoteViews();
    }

    private void drawAmPm(RemoteViews views) {
        if (StateUtils.isLeftAmPm()) {
            views.setViewVisibility(R.id.ampm_text, 8);
            views.setViewVisibility(R.id.ampm_text_left, 0);
            return;
        }
        views.setViewVisibility(R.id.ampm_text_left, 8);
        views.setViewVisibility(R.id.ampm_text, 0);
    }

    private void drawDateText(Context context, RemoteViews views, int widgetSize, boolean portrait) {
        boolean needDateGone = true;
        int i = 0;
        views.setViewVisibility(R.id.clock_time, 0);
        views.setViewVisibility(R.id.week_text, 0);
        if (Locale.getDefault() != null) {
            if (!(portrait && widgetSize == 1) && ((portrait || widgetSize != 2) && !Feature.isTablet(context))) {
                needDateGone = false;
            }
            if (needDateGone) {
                setWeekTextFormat(widgetSize);
            } else {
                setWeekDateTextFormat();
                views.setCharSequence(R.id.date_text_large, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mSkeletonDate));
                views.setCharSequence(R.id.date_text_large, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mSkeletonDate));
            }
            views.setCharSequence(R.id.week_text, "setFormat12Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mSkeletonWeek12H));
            views.setCharSequence(R.id.week_text, "setFormat24Hour", DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mSkeletonWeek24H));
            if (needDateGone) {
                i = 8;
            }
            views.setViewVisibility(R.id.date_text_large, i);
            drawPersianDateText(views, widgetSize);
        }
    }

    private void setWeekTextFormat(int widgetSize) {
        if (Locale.getDefault().getLanguage().equals(Locale.KOREA.getLanguage())) {
            this.mSkeletonWeek12H = "EEEE d MMMM";
            this.mSkeletonWeek24H = "EEEE d MMMM";
        } else if ("de".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "my".equalsIgnoreCase(Locale.getDefault().getLanguage()) || "zh".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            this.mSkeletonWeek12H = "EEE d MMMM";
            this.mSkeletonWeek24H = "EEE d MMMM";
        } else if ("lt".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            this.mSkeletonWeek12H = "d MMMM EEE";
            this.mSkeletonWeek24H = "MMM-d-EEE";
        } else if (widgetSize == 1) {
            this.mSkeletonWeek12H = "EEE d MMM";
            this.mSkeletonWeek24H = "EEE d MMM";
        } else {
            this.mSkeletonWeek12H = "EEEE d MMMM";
            this.mSkeletonWeek24H = "EEEE d MMMM";
        }
    }

    private void setWeekDateTextFormat() {
        if ("TW".equalsIgnoreCase(Locale.getDefault().getLanguage()) || ("HK".equalsIgnoreCase(Locale.getDefault().getLanguage()) && !"zh-Hans-HK".equalsIgnoreCase(Locale.getDefault().toLanguageTag()))) {
            this.mSkeletonWeek12H = "EEE";
            this.mSkeletonWeek24H = "EEE";
            this.mSkeletonDate = "d MMMM";
        } else if ("ja".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            this.mSkeletonWeek12H = "d MMMM";
            this.mSkeletonWeek24H = "d MMMM";
            this.mSkeletonDate = "EEEE";
        } else if ("iw".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            this.mSkeletonWeek12H = "EEEE";
            this.mSkeletonWeek24H = "EEEE";
            this.mSkeletonDate = "d MMM";
        } else {
            this.mSkeletonWeek12H = "EEEE";
            this.mSkeletonWeek24H = "EEEE";
            this.mSkeletonDate = "d MMMM";
        }
    }

    private void drawPersianDateText(RemoteViews views, int widgetSize) {
        int i = 0;
        if (widgetSize == 2) {
            boolean needPersianVisible = Feature.isPersianCalendar() && (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage()) || "fa".equalsIgnoreCase(Locale.getDefault().getLanguage()));
            views.setCharSequence(R.id.date_text_large_persian, "setFormat12Hour", Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage()) ? "d MMMM per eng" : "d MMMM per");
            views.setCharSequence(R.id.date_text_large_persian, "setFormat24Hour", Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage()) ? "d MMMM per eng" : "d MMMM per");
            if (!needPersianVisible) {
                i = 8;
            }
            views.setViewVisibility(R.id.date_text_large_persian, i);
        }
    }
}
