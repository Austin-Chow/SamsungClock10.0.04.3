package com.sec.android.widgetapp.dualclockdigital;

import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

public interface DualClockDigitalContract {

    public interface View {
        RemoteViews getRemoteViews();

        void inflate(Context context, String str, boolean z, boolean z2);

        void setBackgroundColorFilter(int i);

        void setBackgroundImageAlpha(int i);

        void setImageColorFilter(int i);

        void setTextColor(int i);

        void setTextSize(Context context);

        void setWidgetClickPendingIntent(int i, PendingIntent pendingIntent);
    }

    public interface ViewModel {
        RemoteViews getRemoteViews();

        void refresh(Context context, int i, boolean z);

        void setTransparency(Context context, int i, int i2);
    }
}
