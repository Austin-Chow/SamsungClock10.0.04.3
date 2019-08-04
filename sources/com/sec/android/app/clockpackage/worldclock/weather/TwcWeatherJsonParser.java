package com.sec.android.app.clockpackage.worldclock.weather;

import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;

class TwcWeatherJsonParser {
    TwcWeatherJsonParser() {
    }

    ArrayList<WorldclockCityWeatherInfo> parseWeatherData(String responseBody) {
        ArrayList<WorldclockCityWeatherInfo> result = new ArrayList();
        if (responseBody == null) {
            try {
                Log.secE("TwcWeatherJsonParser", "parseWeatherData() => responseBody error !!!!");
                return null;
            } catch (JSONException ex) {
                Log.secE("TwcWeatherJsonParser", "JSONException : " + ex);
                return null;
            }
        }
        JSONArray jsonArray = new JSONArray(responseBody);
        int cityNum = jsonArray.length();
        HashMap<String, Integer> placeIdMap = new HashMap();
        for (int i = 0; i < cityNum; i++) {
            WorldclockCityWeatherInfo cityInfo = new WorldclockCityWeatherInfo();
            if (!jsonArray.getJSONObject(i).isNull("v3-location-point")) {
                String placeId = jsonArray.getJSONObject(i).getJSONObject("v3-location-point").getJSONObject("location").getString("placeId");
                if (!placeIdMap.containsKey(placeId)) {
                    if (!jsonArray.getJSONObject(i).isNull("v3-links")) {
                        cityInfo.setMobileLink(jsonArray.getJSONObject(i).getJSONObject("v3-links").getString("web"));
                    }
                    cityInfo.setCurrentTemperature((float) Math.round((float) jsonArray.getJSONObject(i).getJSONObject("v3-wx-observations-current").getInt("temperature")));
                    cityInfo.setWeatherDescription(jsonArray.getJSONObject(i).getJSONObject("v3-wx-observations-current").getString("wxPhraseLong"));
                    cityInfo.setWeatherIconNum(jsonArray.getJSONObject(i).getJSONObject("v3-wx-observations-current").getInt("iconCode"));
                    cityInfo.setDayOrNight(jsonArray.getJSONObject(i).getJSONObject("v3-wx-observations-current").getString("dayOrNight"));
                    cityInfo.setCurrentState(2);
                    placeIdMap.put(placeId, Integer.valueOf(i));
                }
            }
            result.add(cityInfo);
        }
        return result;
    }
}
