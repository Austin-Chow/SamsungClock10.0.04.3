package com.sec.android.app.clockpackage.worldclock.weather;

import android.net.Uri;
import android.os.Build;
import com.sec.android.app.clockpackage.common.util.Log;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.HeaderGroup;

public class WeatherUrlManager {
    private static String[] localeArray = new String[]{"az#ru", "in#id", "ms#ms", "ca#ca", "cs#cs", "da#da", "de#de", "et#et", "es#es", "fil_PH#en", "fr#fr", "hr#hr", "it#it", "lv#lv", "lt#lt", "hu#hu", "nl#nl", "uz#ru", "pl#pl", "pt#pt", "ro#ro", "sk#sk", "sl#sl", "sr#sr", "fi#fi", "sv#sv", "vi#vi", "tr#tr", "tk#ru", "el#el", "bg#bg", "ky#ru", "kk#kk", "mk#mk", "ru#ru", "tg#ru", "uk#uk", "hy#ru", "iw#he", "ur#ur", "ar#ar", "fa#fa", "hi#hi", "bn#bn", "th#th", "ko#ko", "ja#ja", "zh_CN#zh-cn", "zh_TW#zh-tw", "zh_HK#zh-hk", "zh#zh-cn", "zh_MO#zh-hk"};

    HeaderGroup makeHeader(String packageName) {
        HeaderGroup headerGroup = new HeaderGroup();
        headerGroup.addHeader(new BasicHeader("User-Agent", "SAMSUNG-Android"));
        headerGroup.addHeader(new BasicHeader("Accept", "*,*/*"));
        headerGroup.addHeader(new BasicHeader("Content-Type", "text/xml"));
        headerGroup.addHeader(new BasicHeader("X-Client-Info", packageName));
        return headerGroup;
    }

    URL makeUrlForGetDetailDataList(String placeId, String units) {
        try {
            String language = Uri.encode(getLanguageString());
            return new URL("https", "api.weather.com", 443, String.format(Locale.US, "/v2/aggcommon/v3-location-point;v3-wx-observations-current;v3-links?units=%s&placeids=%s&format=json&language=%s&par=samsung_clock&apiKey=%s", new Object[]{units, placeId, language, "45720848946ac3b87c8eeca0686a11ad"}));
        } catch (MalformedURLException e) {
            Log.secE("WeatherUrlManager", "MalformedURLException : " + e.toString());
            return null;
        }
    }

    public static String getPackageName(boolean autoRefresh) {
        String packageName = "TWC";
        String deviceId = Build.MODEL.toUpperCase(Locale.US).replace("SAMSUNG-", "");
        int end = packageName.lastIndexOf(46);
        if (autoRefresh) {
            return "Auto " + packageName.substring(end + 1) + ", " + deviceId;
        }
        return "Manual " + packageName.substring(end + 1) + ", " + deviceId;
    }

    Uri getHomeUri() {
        return Uri.parse("http://www.weather.com");
    }

    static String getLanguageString() {
        String localeString = Locale.getDefault().toString();
        String result = findLangString(localeString);
        if (result == null) {
            String value = localeString.substring(0, 2);
            if ("zh".equalsIgnoreCase(value) && localeString.length() >= 5) {
                value = localeString.substring(0, 5);
            }
            result = findLangString(value);
            if (result == null) {
                Log.secD("WeatherUrlManager", "getLanguageString() => set default English");
                return "en";
            }
        }
        return result;
    }

    private static String findLangString(String loc) {
        for (String aLocaleArray : localeArray) {
            String[] values = aLocaleArray.split("#");
            if (values.length == 2 && loc.equalsIgnoreCase(values[0])) {
                return values[1];
            }
        }
        return null;
    }

    public Uri setViewUri(String position, String mMobileLink) {
        if (position == null) {
            return null;
        }
        if (position.equals("DETAIL_HOME") && mMobileLink != null) {
            return Uri.parse(mMobileLink);
        }
        Log.secD("WeatherUrlManager", "setViewUri() => mMobileLink : " + mMobileLink);
        return getHomeUri();
    }
}
