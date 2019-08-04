package com.sec.android.app.clockpackage.worldclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;

public class ToWorldclockReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            Log.secI("ToWorldclockReceiver", "ToWorldclockReceiver : intent.getAction() = " + intent.getAction().substring(intent.getAction().lastIndexOf(46)));
            if ("watchdualclock.request.CITYINFO".equals(intent.getAction())) {
                Bundle data = intent.getExtras();
                if (data == null) {
                    Log.secD("ToWorldclockReceiver", "there is no extras");
                    return;
                }
                int uniqueId = data.getInt("uniqueid");
                Log.secD("ToWorldclockReceiver", "uniqueId : " + uniqueId);
                if (uniqueId != -1) {
                    if (!CityManager.sIsCityManagerLoad) {
                        CityManager.initCity(context);
                    }
                    City city = CityManager.findCityByUniqueId(Integer.valueOf(uniqueId));
                    Bundle extra = new Bundle();
                    Intent iWatchCityInfo = new Intent("watchdualclock.response.CITYINFO");
                    if (city != null) {
                        extra.putString("cityname", city.getName());
                        extra.putInt("gmt", city.getLocalOffset());
                        extra.putString("timezoneid", city.getTimeZoneId());
                        extra.putInt("uniqueid", city.getUniqueId());
                        Log.secI("ToWorldclockReceiver", "cityname = " + city.getName());
                        Log.secI("ToWorldclockReceiver", "gmt = " + city.getLocalOffset());
                        Log.secI("ToWorldclockReceiver", "timezoneid = " + city.getTimeZoneId());
                        Log.secI("ToWorldclockReceiver", "uniqueid = " + city.getUniqueId());
                    }
                    iWatchCityInfo.putExtras(extra);
                    context.getApplicationContext().sendBroadcast(iWatchCityInfo);
                }
            }
        }
    }
}
