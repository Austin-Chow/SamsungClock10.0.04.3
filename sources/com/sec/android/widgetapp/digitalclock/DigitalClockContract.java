package com.sec.android.widgetapp.digitalclock;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

public interface DigitalClockContract {

    public interface View {
        RemoteViews getRemoteViews();

        void inflate(Context context, int i);

        void setBackgroundColorFilter(int i);

        void setBackgroundImageAlpha(int i);

        void setTextColor(int i);

        void setTextSize(Context context, int i);

        void setWidgetClickPendingIntent(PendingIntent pendingIntent);
    }

    public interface ViewModel {
        RemoteViews getRemoteViews();

        void refresh(Context context, int i, boolean z);

        void setTransparency(Context context, int i, int i2);
    }
}
