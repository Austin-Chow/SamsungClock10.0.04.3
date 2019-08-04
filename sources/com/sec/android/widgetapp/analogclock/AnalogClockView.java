package com.sec.android.widgetapp.analogclock;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;
import com.sec.android.widgetapp.analogclock.AnalogClockContract.View;

public class AnalogClockView implements View {
    private RemoteViews mRemoteViews;

    public RemoteViews getRemoteViews() {
        return this.mRemoteViews;
    }

    public void inflate(Context context) {
        this.mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.analog_clock);
    }

    public void setWidgetClickPendingIntent(PendingIntent pendingIntent) {
        getRemoteViews().setOnClickPendingIntent(R.id.analog_clock, pendingIntent);
    }

    public void setDateViewVisibility(Context context) {
        if (WidgetSettingUtils.isWhiteWallPaper(context)) {
            Log.secD("AnalogClockView", "setDateViewVisibility need_dark_font");
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_full_date_view, 8);
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_week_view, 8);
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_date_view, 8);
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_full_date_view_white_wallpaper, 0);
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_week_view_white_wallpaper, 0);
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_date_view_white_wallpaper, 0);
            return;
        }
        Log.secD("AnalogClockView", "setDateViewVisibility light font");
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_full_date_view, 0);
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_week_view, 0);
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_date_view, 0);
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_full_date_view_white_wallpaper, 8);
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_week_view_white_wallpaper, 8);
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_date_view_white_wallpaper, 8);
    }

    public void setDatePosition(Context context, int gridHeightMargin) {
        Log.secD("AnalogClockView", "setDatePosition gridHeightMargin : " + gridHeightMargin);
        if (gridHeightMargin - 13 > 0) {
            getRemoteViews().setViewPadding(R.id.analog_clock_layout, 0, ClockUtils.convertDpToPixel(context, 13), 0, 0);
            int bottomPaddingDp = ((gridHeightMargin - 13) - 26) + 12;
            if (bottomPaddingDp <= 0) {
                bottomPaddingDp = 0;
            }
            int bottomPadding = ClockUtils.convertDpToPixel(context, bottomPaddingDp);
            getRemoteViews().setViewPadding(R.id.analog_clock_full_date_view, 0, 0, 0, bottomPadding);
            getRemoteViews().setViewPadding(R.id.analog_clock_full_date_view_white_wallpaper, 0, 0, 0, bottomPadding);
        }
    }

    public void setAnalogClockResource(Context context) {
        if (WidgetSettingUtils.isWhiteWallPaper(context)) {
            Log.secD("AnalogClockView", "setAnalogClockResource white wallpaper");
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_normal_view, 8);
            this.mRemoteViews.setViewVisibility(R.id.analog_clock_white_view, 0);
            return;
        }
        Log.secD("AnalogClockView", "setAnalogClockResource normal wallpaper");
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_normal_view, 0);
        this.mRemoteViews.setViewVisibility(R.id.analog_clock_white_view, 8);
    }
}
