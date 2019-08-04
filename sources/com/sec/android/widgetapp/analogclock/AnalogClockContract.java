package com.sec.android.widgetapp.analogclock;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

public interface AnalogClockContract {

    public interface View {
        RemoteViews getRemoteViews();

        void inflate(Context context);

        void setAnalogClockResource(Context context);

        void setDatePosition(Context context, int i);

        void setDateViewVisibility(Context context);

        void setWidgetClickPendingIntent(PendingIntent pendingIntent);
    }

    public interface ViewModel {
        RemoteViews getRemoteViews();

        void refresh(Context context, int i);
    }
}
