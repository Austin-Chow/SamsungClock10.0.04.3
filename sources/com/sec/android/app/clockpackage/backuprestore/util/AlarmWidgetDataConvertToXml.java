package com.sec.android.app.clockpackage.backuprestore.util;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetIdManager;
import com.sec.android.app.clockpackage.alarmwidget.ClockAlarmWidgetProvider;
import com.sec.android.app.clockpackage.common.util.Log;
import java.io.IOException;

public class AlarmWidgetDataConvertToXml extends DataConvertToXML {
    private final String TAG = "BNR_CLOCK_AlarmWidgetDataConvertToXml";

    public AlarmWidgetDataConvertToXml(String filePath, String saveKey, int securityLevel, int whichFunction) {
        super(filePath, saveKey, securityLevel, whichFunction);
        Log.secD("BNR_CLOCK_AlarmWidgetDataConvertToXml", "AlarmWidgetDataConvertToXml()");
    }

    public int exportData(Object obj) {
        Context context = (Context) obj;
        Log.secD("BNR_CLOCK_AlarmWidgetDataConvertToXml", "exportData() / isContext" + (obj instanceof Context));
        try {
            this.mExporter.startBackupVersionExport();
            this.mExporter.startDbExport();
            try {
                int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ClockAlarmWidgetProvider.class));
                for (int i = 0; i < widgetIds.length; i++) {
                    int widgetId = widgetIds[i];
                    int alarmId = ClockAlarmWidgetIdManager.getListItem(context, widgetIds[i], ClockAlarmWidgetIdManager.getListItem(context, widgetIds[i], 0) + 1);
                    this.mExporter.addWidgetItem(Integer.toString(widgetId), Integer.toString(alarmId));
                    Log.secD("BNR_CLOCK_AlarmWidgetDataConvertToXml", "alarmwidget_bnr (**) : exportData / (widgetId, alarmId) =(" + widgetId + "," + alarmId + ")");
                }
                this.mExporter.endDbExport();
                this.mExporter.close();
                close();
                Log.secD("BNR_CLOCK_AlarmWidgetDataConvertToXml", "Alarm Widget export Completed");
                return 0;
            } catch (IllegalStateException e) {
                Log.secE("BNR_CLOCK_AlarmWidgetDataConvertToXml", "getAppWidgetIds IllegalStateException e = " + e);
                return 1;
            }
        } catch (NullPointerException e2) {
            e = e2;
        } catch (IOException e3) {
            e = e3;
        }
        Exception e4;
        e4.printStackTrace();
        return 1;
    }
}
