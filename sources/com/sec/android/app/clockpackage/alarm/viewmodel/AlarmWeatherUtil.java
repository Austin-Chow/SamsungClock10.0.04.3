package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.os.SemSystemProperties;
import android.text.TextUtils;
import android.widget.ImageView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;

public class AlarmWeatherUtil {
    private static int getWeatherBackgroundNum(int weatherCodeNum) {
        int background;
        switch (weatherCodeNum) {
            case 1:
            case 3:
            case 6:
            case 9:
            case 10:
            case 17:
            case 27:
            case 34:
            case 36:
            case 39:
            case 41:
            case 42:
            case 54:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                background = 3;
                break;
            case 2:
            case 4:
            case 13:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            case 29:
            case 30:
            case 31:
            case 35:
            case 37:
            case 50:
            case 51:
            case 59:
            case 60:
            case 61:
            case 78:
            case 82:
                background = 6;
                break;
            case 5:
            case 23:
            case 32:
            case 33:
            case 55:
            case 56:
            case 68:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                background = 5;
                break;
            case 7:
            case 8:
            case 14:
            case 38:
            case 40:
            case 48:
            case 49:
            case 53:
            case 58:
                background = 2;
                break;
            case 11:
            case 16:
            case 28:
            case 52:
            case 69:
            case 79:
            case 80:
            case 83:
                background = 4;
                break;
            case 12:
            case 15:
            case 18:
            case 19:
            case 26:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 57:
            case 62:
            case 81:
                background = 1;
                break;
            default:
                background = 7;
                break;
        }
        Log.secD("AlarmWeatherUtil", "getWeatherBackgroundNum weatherCodeNum = " + weatherCodeNum + ", background = " + background);
        return background;
    }

    public static String getWeatherBackgroundPath(int weatherCodeNum) {
        String backgroundPath;
        int bgm = getWeatherBackgroundNum(weatherCodeNum);
        switch (bgm) {
            case 1:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/fog_cloudy";
                break;
            case 2:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/rain";
                break;
            case 3:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/snow";
                break;
            case 4:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/sunny_clear_hot_warm";
                break;
            case 5:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/thunder";
                break;
            case 6:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/windy_cold";
                break;
            default:
                backgroundPath = "android.resource://com.sec.android.app.clockpackage/raw/default_sound";
                break;
        }
        Log.secD("AlarmWeatherUtil", "getWeatherBackgroundPath bgm = " + bgm + ", backgroundPath = " + backgroundPath);
        return backgroundPath;
    }

    public static void setWeatherImg(Context context, ImageView dayWeatherImageView, int weatherIconNumber, int daytime) {
        Log.secD("AlarmWeatherUtil", "setWeatherImg weatherIconNumber = " + weatherIconNumber + ", bDaytime = " + daytime);
        if (dayWeatherImageView == null) {
            Log.secD("AlarmWeatherUtil", "setWeatherImg dayWeatherImageView null");
            return;
        }
        int weatherRes;
        if (daytime == 1) {
            weatherRes = C0490R.drawable.alarm_alert_weather_icon_list_popup;
        } else {
            weatherRes = C0490R.drawable.alarm_alert_weather_icon_list_popup_night;
        }
        if (dayWeatherImageView != null) {
            dayWeatherImageView.setImageDrawable(context.getResources().getDrawable(weatherRes, null));
        }
        switch (weatherIconNumber) {
            case 101:
                dayWeatherImageView.setImageLevel(101);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.clear));
                return;
            case 102:
            case 113:
                dayWeatherImageView.setImageLevel(102);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.snow));
                return;
            case 103:
                dayWeatherImageView.setImageLevel(103);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.cold));
                return;
            case 104:
                dayWeatherImageView.setImageLevel(104);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.hot));
                return;
            case 106:
                dayWeatherImageView.setImageLevel(106);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.fog));
                return;
            case 107:
                dayWeatherImageView.setImageLevel(107);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.sunny));
                return;
            case 108:
                dayWeatherImageView.setImageLevel(108);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.cloudy));
                return;
            case 109:
            case 114:
                dayWeatherImageView.setImageLevel(109);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.rain));
                return;
            case 110:
            case 112:
                dayWeatherImageView.setImageLevel(110);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.weather_music_thunder));
                return;
            case 111:
                dayWeatherImageView.setImageLevel(111);
                dayWeatherImageView.setContentDescription(context.getString(C0490R.string.windy));
                return;
            default:
                dayWeatherImageView.setVisibility(4);
                return;
        }
    }

    public static int getWeatherIconNumber(int weatherCodeNum, int daytime) {
        int weatherIconNumber;
        switch (weatherCodeNum) {
            case 1:
            case 3:
            case 17:
            case 27:
            case 34:
            case 36:
            case 39:
            case 41:
            case 42:
            case 54:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                weatherIconNumber = 102;
                break;
            case 2:
            case 4:
            case 29:
            case 35:
            case 37:
            case 50:
            case 51:
            case 82:
                weatherIconNumber = 111;
                break;
            case 5:
                weatherIconNumber = 112;
                break;
            case 6:
            case 9:
            case 10:
                weatherIconNumber = 113;
                break;
            case 7:
            case 8:
                weatherIconNumber = 114;
                break;
            case 11:
            case 52:
                weatherIconNumber = 101;
                break;
            case 12:
            case 43:
            case 44:
            case 46:
                weatherIconNumber = 108;
                break;
            case 13:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            case 30:
            case 31:
            case 59:
            case 60:
            case 61:
            case 78:
                weatherIconNumber = 103;
                break;
            case 14:
            case 38:
            case 40:
            case 48:
            case 49:
            case 53:
            case 58:
                weatherIconNumber = 109;
                break;
            case 15:
            case 18:
            case 19:
            case 26:
            case 45:
            case 47:
            case 57:
            case 62:
            case 81:
                weatherIconNumber = 106;
                break;
            case 16:
            case 69:
            case 83:
                weatherIconNumber = 107;
                break;
            case 23:
            case 32:
            case 33:
            case 55:
            case 56:
            case 68:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                weatherIconNumber = 110;
                break;
            case 28:
            case 79:
            case 80:
                weatherIconNumber = 104;
                break;
            default:
                weatherIconNumber = 115;
                break;
        }
        Log.secD("AlarmWeatherUtil", "getWeatherIconNumber weatherIconNumber = " + weatherIconNumber);
        switch (weatherIconNumber) {
            case 101:
                if (daytime == 1) {
                    weatherIconNumber = 107;
                    break;
                }
                break;
            case 104:
            case 107:
                if (daytime == 2) {
                    weatherIconNumber = 101;
                    break;
                }
                break;
        }
        Log.m41d("AlarmWeatherUtil", "getWeatherIconNumber real_weatherIconNumber = " + weatherIconNumber + ", weatherCodeNum = " + weatherCodeNum + ", daytime = " + daytime);
        return weatherIconNumber;
    }

    public static void setCpLogo(Context context, ImageView cpLogo) {
        int cpRes;
        String countryCode = SemSystemProperties.getCountryIso();
        String description = "";
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "US";
        }
        Object obj = -1;
        switch (countryCode.hashCode()) {
            case 2155:
                if (countryCode.equals("CN")) {
                    obj = 1;
                    break;
                }
                break;
            case 2407:
                if (countryCode.equals("KR")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                cpRes = C0490R.drawable.clock_ic_weathernews_mtrl;
                description = context.getResources().getString(C0490R.string.weather_news_tts);
                break;
            case 1:
                if (!Feature.isSupportBixbyBriefingMenu(context)) {
                    cpRes = C0490R.drawable.clock_ic_twc_mtrl;
                    description = context.getResources().getString(C0490R.string.the_weather_channer_tts);
                    break;
                }
                cpRes = C0490R.drawable.clock_ic_huafengaccu_mtrl;
                description = context.getResources().getString(C0490R.string.huafeng_accu_weather_tts);
                break;
            default:
                cpRes = C0490R.drawable.clock_ic_twc_mtrl;
                description = context.getResources().getString(C0490R.string.the_weather_channer_tts);
                break;
        }
        Log.secD("AlarmWeatherUtil", "setCpLogo cpRes = " + cpRes + "countryCode = " + countryCode);
        if (cpLogo != null) {
            Log.secD("AlarmWeatherUtil", "setCpLogo setImageResource = " + cpRes + "countryCode = " + countryCode);
            cpLogo.setImageResource(cpRes);
            cpLogo.setContentDescription(description);
        }
    }
}
