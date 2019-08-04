package com.sec.android.app.clockpackage.worldclock.weather;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;

public class WorldclockWeatherUtils {
    private static String TAG = "WorldclockWeatherUtils";
    private static boolean sIsCompletedReorder = false;
    private static boolean sMobileLinkActive = false;

    public static void setWeatherImg(ImageView dayWeatherImageView, int weatherIconNum, String weatherDescription, boolean isDay) {
        if (dayWeatherImageView == null) {
            Log.secD(TAG, "setWeatherImg() => dayWeatherImageView null");
            return;
        }
        Log.secD(TAG, "setWeatherImg() => weatherIconNum : " + weatherIconNum);
        dayWeatherImageView.setContentDescription(weatherDescription);
        switch (weatherIconNum) {
            case 0:
            case 1:
            case 2:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_hurricane_mtrl);
                return;
            case 3:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_thunderstorm_mtrl);
                return;
            case 4:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_hail_mtrl);
                return;
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 35:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_rainandsnowmixed_mtrl);
                return;
            case 9:
            case 11:
            case 12:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_rain_mtrl);
                return;
            case 13:
            case 14:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_flurries_mtrl);
                return;
            case 15:
            case 17:
            case 18:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_ice_mtrl);
                return;
            case 16:
            case 42:
            case 43:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_snow_mtrl);
                return;
            case 19:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_sandstorm_mtrl);
                return;
            case 20:
            case 21:
            case 22:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_fog_mtrl);
                return;
            case 23:
            case 24:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_windy_mtrl);
                return;
            case 25:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_cold_mtrl);
                return;
            case 26:
            case 27:
            case 28:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_cloudy_mtrl);
                return;
            case 40:
                dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_heavyrain_mtrl);
                return;
            default:
                if (isDay) {
                    switch (weatherIconNum) {
                        case 29:
                        case 30:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_partlysunny_mtrl);
                            return;
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_sunny_mtrl);
                            return;
                        case 36:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_hot_mtrl);
                            return;
                        case 37:
                        case 38:
                        case 47:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_partlysunnywiththundershower_mtrl);
                            return;
                        case 39:
                        case 45:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_partlysunnywithshower_mtrl);
                            return;
                        case 41:
                        case 46:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_partlysunnywithflurries_mtrl);
                            return;
                        default:
                            dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_sunny_mtrl);
                            return;
                    }
                }
                switch (weatherIconNum) {
                    case 29:
                    case 30:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_mostlyclear_mtrl);
                        return;
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_clear_mtrl);
                        return;
                    case 36:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_clear_mtrl);
                        return;
                    case 37:
                    case 38:
                    case 47:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_thunderstorm_mtrl);
                        return;
                    case 39:
                    case 45:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_shower_mtrl);
                        return;
                    case 41:
                    case 46:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_flurries_mtrl);
                        return;
                    default:
                        dayWeatherImageView.setBackgroundResource(C0836R.drawable.weather_ic_30_sunny_mtrl);
                        return;
                }
        }
    }

    public static int getDefaultTempScale(Context context) {
        int convertUnit;
        if (Feature.getWeatherDefaultUnit() == 1) {
            convertUnit = 0;
        } else {
            convertUnit = 1;
        }
        return context.getResources().getBoolean(C0836R.bool.cts_use_fahrenheit) ? 1 : convertUnit;
    }

    public static void showNetworkUnavailableToast(Context context) {
        if (context.getSharedPreferences("ClocksTabStatus", 0).getBoolean("WeatherSwitch", !Feature.isWeatherDefaultOff()) && !StateUtils.isNetWorkConnected(context)) {
            Toast toast = Toast.makeText(context, context.getString(C0836R.string.ss_weather_services_not_available), 1);
            toast.setGravity(81, 0, context.getResources().getDimensionPixelSize(Resources.getSystem().getIdentifier("toast_y_offset", "dimen", "android")));
            toast.show();
        }
    }

    public static boolean isDisableWeather(Context context) {
        if (context.getSharedPreferences("ClocksTabStatus", 0).getBoolean("WeatherSwitch", !Feature.isWeatherDefaultOff()) && StateUtils.isNetWorkConnected(context)) {
            return false;
        }
        return true;
    }

    public static String getWeatherLayoutDescription(Context context, WorldclockCityWeatherInfo weatherInfo) {
        return weatherInfo.getWeatherDescription() + ", " + weatherInfo.getCurrentTemperature() + context.getString(C0836R.string.weather_degree) + ", " + context.getString(C0836R.string.link);
    }
}
