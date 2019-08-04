package com.sec.android.app.clockpackage.alarmwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetContract.View;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;

public class ClockAlarmWidgetView implements View {
    private RemoteViews mRemoteViews;

    public RemoteViews getRemoteViews() {
        return this.mRemoteViews;
    }

    public void inflate(Context context, String packageName, int widgetSize, boolean isSetting) {
        this.mRemoteViews = new RemoteViews(packageName, getLayoutId(context, widgetSize, isSetting));
    }

    private int getLayoutId(Context context, int widgetSize, boolean isSetting) {
        if (!AppWidgetUtils.isSamsungHome() && !StateUtils.isAfwForByod(context)) {
            return R.layout.clock_alarmwidget_full_third_party;
        }
        if ((isSetting || widgetSize != 1) && ((!isSetting || !StateUtils.isScreenDp(context, 457)) && (!isSetting || !WidgetSettingUtils.isLandscape(context) || StateUtils.isScreenDp(context, 457)))) {
            return R.layout.clock_alarmwidget_full;
        }
        return R.layout.clock_alarmwidget_mini;
    }

    public void setBackgroundColorFilter(int colorFilter) {
        getRemoteViews().setImageViewResource(R.id.alarm_widget_background, R.drawable.widget_background_white);
        getRemoteViews().setInt(R.id.alarm_widget_background, "setColorFilter", colorFilter);
    }

    public void setBackgroundImageAlpha(int alpha) {
        getRemoteViews().setInt(R.id.alarm_widget_background, "setImageAlpha", alpha);
    }

    public void setImageColorFilter(int onOffImageColor, boolean isActive) {
        getRemoteViews().setImageViewResource(R.id.alarm_onoff_btn, onOffImageColor);
        if (isActive) {
            getRemoteViews().setInt(R.id.alarm_onoff_btn, "setImageAlpha", 255);
        } else {
            getRemoteViews().setInt(R.id.alarm_onoff_btn, "setImageAlpha", 77);
        }
        getRemoteViews().setInt(R.id.alarm_onoff_btn, "semSetHoverPopupType", 0);
    }

    public void setTextColor(int nameAndDateColor, int timeAndAmPmColor, int noItemColor) {
        getRemoteViews().setTextColor(R.id.label2, noItemColor);
        getRemoteViews().setTextColor(R.id.alarm_name, nameAndDateColor);
        getRemoteViews().setTextColor(R.id.alarm_time, timeAndAmPmColor);
        getRemoteViews().setTextColor(R.id.alarm_ampm, timeAndAmPmColor);
        getRemoteViews().setTextColor(R.id.alarm_ampm_kor, timeAndAmPmColor);
        getRemoteViews().setTextColor(R.id.alarm_date, nameAndDateColor);
    }

    public void setWidgetClickPendingIntent(PendingIntent pendingIntent1, PendingIntent pendingIntent2, boolean isEmpty) {
        if (isEmpty) {
            getRemoteViews().setOnClickPendingIntent(R.id.widget_main, pendingIntent1);
            return;
        }
        getRemoteViews().setOnClickPendingIntent(R.id.widget_main, pendingIntent1);
        if (pendingIntent2 != null) {
            getRemoteViews().setOnClickPendingIntent(R.id.alarm_onoff_btn, pendingIntent2);
        }
    }
}
