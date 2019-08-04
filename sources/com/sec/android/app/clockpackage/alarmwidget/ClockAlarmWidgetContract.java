package com.sec.android.app.clockpackage.alarmwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

public interface ClockAlarmWidgetContract {

    public interface View {
        RemoteViews getRemoteViews();

        void inflate(Context context, String str, int i, boolean z);

        void setBackgroundColorFilter(int i);

        void setBackgroundImageAlpha(int i);

        void setImageColorFilter(int i, boolean z);

        void setTextColor(int i, int i2, int i3);

        void setWidgetClickPendingIntent(PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z);
    }

    public interface ViewModel {
        RemoteViews getRemoteViews();

        void refresh(Context context, int i, int i2, boolean z);

        void setTransparency(Context context, int i, int i2);
    }
}
