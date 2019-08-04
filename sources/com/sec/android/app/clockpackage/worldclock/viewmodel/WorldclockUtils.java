package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.viewmodel.ClockTab;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockCityWeatherInfo;
import com.sec.android.app.clockpackage.worldclock.model.WorldclockDBManager;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class WorldclockUtils {
    static boolean isWifiOnly(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || connectivityManager.semIsNetworkSupported(0)) {
            return false;
        }
        return true;
    }

    static void addCityToDualClockAod(Context context, int uniqueId, int homeZone, int widgetId) {
        Log.secD("WorldclockUtils", "addCityToDualClockAod");
        Intent intent = new Intent("dualclock.add_city");
        intent.addFlags(402653184);
        intent.putExtra("where", "menu_dualclock_aod");
        intent.putExtra("homezone", homeZone);
        intent.putExtra("uniqueid", uniqueId);
        intent.putExtra("wid", widgetId);
        intent.setPackage("com.samsung.android.app.aodservice");
        context.sendBroadcast(intent);
    }

    static void addCityToDualClockDigitalWidget(Context context, int uniqueId, int homeZone, int widgetId) {
        Log.secD("WorldclockUtils", "addCityToDualClockDigitalWidget");
        Intent intent = new Intent("com.sec.android.app.clockpackage.dualclockdigital.ADD_CITY");
        intent.addFlags(402653184);
        intent.putExtra("homezone", homeZone);
        intent.putExtra("uniqueid", uniqueId);
        intent.putExtra("wid", widgetId);
        intent.setPackage("com.sec.android.app.clockpackage");
        context.sendBroadcast(intent);
    }

    static Intent addCityToDualClockDigitalLaunch(Context context, int uniqueId, int homeZone, int widgetId) {
        Log.secD("WorldclockUtils", "addCityToDualClockDigitalLaunch");
        Intent intent = new Intent("com.sec.android.app.clockpackage.dualclockdigital.ADD_CITY");
        intent.addFlags(402653184);
        intent.putExtra("homezone", homeZone);
        intent.putExtra("uniqueid", uniqueId);
        intent.putExtra("wid", widgetId);
        intent.setPackage("com.sec.android.app.clockpackage");
        context.sendBroadcast(intent);
        return intent;
    }

    static Intent addCityDualClockDigitalWatch(Context context, City city) {
        Log.secE("WorldclockUtils", "addCityDualClockDigitalWatch");
        Bundle extra = new Bundle();
        Intent intent = new Intent("watchdualclock.WATCH_DUALCLOCK_SETTINGINFO");
        extra.putString("cityname", city.getName());
        extra.putString("countryname", city.getCountry());
        extra.putInt("gmt", city.getLocalOffset());
        extra.putString("gmt_real", city.getCurrentTimeGMT());
        extra.putString("timezoneid", city.getTimeZoneId());
        extra.putInt("uniqueid", city.getUniqueId());
        extra.putString("cityarray", "Cities");
        extra.putString("mappingtable", "MappingTables");
        extra.putString("countryarray", "Countries");
        intent.putExtras(extra);
        context.sendBroadcast(intent);
        return intent;
    }

    static Intent addCityDualClockDigitalPremiumWatch(City city) {
        Log.secI("WorldclockUtils", "addCityDualClockDigitalPremiumWatch");
        Bundle extra = new Bundle();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(new ComponentName("com.sec.android.app.premiumwatch", "com.sec.android.app.premiumwatch.settings.WatchStyleSettings"));
        if (city != null) {
            extra.putString("cityname", CityManager.findEngOnlyCityNameByUniqueId(Integer.valueOf(city.getUniqueId())));
            extra.putString("timezoneid", city.getTimeZoneId());
        }
        intent.putExtras(extra);
        return intent;
    }

    static Intent addCity(Context context, City city, boolean changeCity, int listPosition, int index, ArrayList<WorldclockCityWeatherInfo> weatherInfoList, WorldclockCityWeatherInfo currentWeatherInfo) {
        if (WorldclockDBManager.isDuplication(context, city.getUniqueId())) {
            showDuplicationToast(context, city.getName());
            return null;
        }
        Intent intent = new Intent();
        if (changeCity) {
            if (!WorldclockDBManager.updateDB(context, city, "_id = " + index)) {
                showNotEnoughSpaceToast(context);
            }
            intent.putExtra("city_result_type", 2);
            if (!(currentWeatherInfo == null || weatherInfoList.isEmpty() || weatherInfoList.size() <= listPosition || WorldclockWeatherUtils.isDisableWeather(context))) {
                weatherInfoList.set(listPosition, currentWeatherInfo);
            }
        } else if (WorldclockDBManager.getDBCursorCnt(context) >= 20) {
            showMaxCountToast(context);
            return null;
        } else {
            if (!WorldclockDBManager.saveDB(context, city)) {
                showNotEnoughSpaceToast(context);
            }
            if (currentWeatherInfo != null) {
                weatherInfoList.add(0, currentWeatherInfo);
            }
            intent.putExtra("city_result_type", 1);
        }
        intent.putExtra("ListPosition", listPosition);
        intent.putParcelableArrayListExtra("WorldclockWeatherListInfoKey", weatherInfoList);
        return intent;
    }

    static void showNotEnoughSpaceToast(Context context) {
        Toast.makeText(context, context.getString(C0836R.string.memory_full), 1).show();
    }

    static void showMaxCountToast(Context context) {
        Toast.makeText(context, context.getString(C0836R.string.wc_time_maxsize, new Object[]{Integer.valueOf(20)}), 0).show();
    }

    static void showDuplicationToast(Context context, String cityName) {
        Toast.makeText(context, context.getString(C0836R.string.wc_time_already_exit, new Object[]{cityName}), 0).show();
    }

    static boolean isFromExternal(String fromWhere) {
        return isFromDualClockDigitalWidget(fromWhere) || isFromDualClockDigitalWatch(fromWhere) || isFromDualClockDigitalAOD(fromWhere) || isFromDualClockDigitalPremiumWatch(fromWhere) || isFromDualClockDigitalLaunch(fromWhere);
    }

    static boolean isFromDualClockDigitalWidget(String fromWhere) {
        return "menu_dualclock_widget".equals(fromWhere);
    }

    static boolean isFromDualClockDigitalLaunch(String fromWhere) {
        return "menu_dualclock_launch".equals(fromWhere);
    }

    static boolean isFromDualClockDigitalWatch(String fromWhere) {
        return "menu_watch_dualclock".equals(fromWhere);
    }

    static boolean isFromDualClockDigitalAOD(String fromWhere) {
        return "menu_dualclock_aod".equals(fromWhere);
    }

    static boolean isFromDualClockDigitalPremiumWatch(String fromWhere) {
        return "menu_premiumwatch_dualclock".equals(fromWhere);
    }

    static boolean isFromWorldclockList(String fromWhere) {
        return "add".equals(fromWhere) || "city".equals(fromWhere);
    }

    static boolean isFromWorldclockListWhereCity(String fromWhere) {
        return "city".equals(fromWhere);
    }

    static boolean isWorldclockTab() {
        return ClockTab.isWorldclockTab();
    }

    private static void setTimeDiffString(Context context, TextView textView, float timeDiff) {
        int hourDiff = (int) Math.abs(timeDiff);
        int minDiff = (int) ((Math.abs(timeDiff) - ((float) hourDiff)) * 60.0f);
        Log.secD("WorldclockUtils", "timeDiff : " + timeDiff + " hourDiff : " + hourDiff + " minDiff : " + minDiff);
        String[] diffString = getTimeDiffString(context, timeDiff, hourDiff, minDiff);
        textView.setText(diffString[0]);
        if (timeDiff != 0.0f) {
            textView.setContentDescription(diffString[1]);
        } else {
            textView.setContentDescription(diffString[0]);
        }
    }

    private static String[] getTimeDiffString(Context context, float timeDiff, int hourDiff, int minDiff) {
        String[] diffString = new String[2];
        if (timeDiff == 0.0f) {
            int i;
            if (Feature.isFolder(context)) {
                i = C0836R.string.local_time_zone;
            } else {
                i = C0836R.string.same_local_time;
            }
            diffString[0] = context.getString(i);
        } else if (minDiff == 0) {
            if (timeDiff == 1.0f) {
                diffString[0] = context.getString(C0836R.string.one_hour_behind);
                diffString[1] = context.getString(C0836R.string.one_hour_behind_description);
            } else if (timeDiff > 0.0f) {
                diffString[0] = context.getString(C0836R.string.hours_behind, new Object[]{Integer.toString(hourDiff)});
                diffString[1] = context.getString(C0836R.string.hours_behind_description, new Object[]{Integer.valueOf(hourDiff)});
            } else if (timeDiff == -1.0f) {
                diffString[0] = context.getString(C0836R.string.one_hour_ahead);
                diffString[1] = context.getString(C0836R.string.one_hour_ahead_description);
            } else {
                diffString[0] = context.getString(C0836R.string.hours_ahead, new Object[]{Integer.toString(hourDiff)});
                diffString[1] = context.getString(C0836R.string.hours_ahead_description, new Object[]{Integer.valueOf(hourDiff)});
            }
        } else if (hourDiff == 0) {
            diffString[0] = context.getString(timeDiff > 0.0f ? C0836R.string.min_behind : C0836R.string.min_ahead, new Object[]{Integer.valueOf(minDiff)});
            diffString[1] = context.getString(timeDiff > 0.0f ? C0836R.string.min_behind_description : C0836R.string.min_ahead_description, new Object[]{Integer.valueOf(minDiff)});
        } else if (hourDiff == 1) {
            diffString[0] = context.getString(timeDiff > 0.0f ? C0836R.string.one_hour_min_behind : C0836R.string.one_hour_min_ahead, new Object[]{Integer.valueOf(minDiff)});
            diffString[1] = context.getString(timeDiff > 0.0f ? C0836R.string.one_hour_min_behind_description : C0836R.string.one_hour_min_ahead_description, new Object[]{Integer.valueOf(minDiff)});
        } else {
            diffString[0] = context.getString(timeDiff > 0.0f ? C0836R.string.hours_min_behind : C0836R.string.hours_min_ahead, new Object[]{Integer.valueOf(hourDiff), Integer.valueOf(minDiff)});
            diffString[1] = context.getString(timeDiff > 0.0f ? C0836R.string.hours_min_behind_description : C0836R.string.hours_min_ahead_description, new Object[]{Integer.valueOf(hourDiff), Integer.valueOf(minDiff)});
        }
        convertDiffString(diffString);
        return diffString;
    }

    private static void convertDiffString(String[] diffString) {
        if ("ar".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            diffString[0] = ClockUtils.numberToArabic(diffString[0]);
            diffString[1] = ClockUtils.numberToArabic(diffString[1]);
        } else if ("fa".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            diffString[0] = ClockUtils.numberToPersian(diffString[0]);
            diffString[1] = ClockUtils.numberToPersian(diffString[1]);
        }
    }

    static void getCityDayTimeDiffString(Context context, boolean isLocalCity, int id, TextView timeView, TextView dayView, TimeZone tz, boolean isFromList) {
        if (timeView == null) {
            return;
        }
        if (!isFromList || id >= 0) {
            TimeZone baseTz = TimeZone.getDefault();
            long now = System.currentTimeMillis();
            float timeDiff = ((float) (baseTz.getOffset(now) - tz.getOffset(now))) / 3600000.0f;
            if (isLocalCity && timeDiff == 0.0f) {
                timeView.setText(context.getString(C0836R.string.local_time_zone));
                timeView.setContentDescription(context.getString(C0836R.string.local_time_zone));
            } else {
                setTimeDiffString(context, timeView, timeDiff);
            }
            getCityDayDiffString(context, tz, dayView, isFromList);
            if (isFromList) {
                ClockUtils.setLargeTextSize(context, timeView, (float) timeView.getContext().getResources().getDimensionPixelSize(C0836R.dimen.worldclock_list_item_city_time_diff_text_size));
            }
        }
    }

    private static void getCityDayDiffString(Context context, TimeZone tz, TextView tv, boolean isFromList) {
        GregorianCalendar cityCalendar = new GregorianCalendar();
        cityCalendar.setTimeZone(tz);
        GregorianCalendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTimeZone(TimeZone.getDefault());
        Log.secD("WorldclockUtils", "currentCalendar date : " + currentCalendar.get(5));
        Log.secD("WorldclockUtils", "cityCalendar date : " + cityCalendar.get(5));
        int diffDay = currentCalendar.get(5) - cityCalendar.get(5);
        if (tv == null) {
            return;
        }
        if (diffDay == 0) {
            if (isFromList) {
                tv.append("");
            } else {
                tv.setText(context.getString(C0836R.string.today));
            }
        } else if (diffDay > 0) {
            if (isFromList) {
                tv.append(", " + context.getString(diffDay > 1 ? C0836R.string.tomorrow : C0836R.string.yesterday));
            } else {
                tv.setText(context.getString(diffDay > 1 ? C0836R.string.tomorrow : C0836R.string.yesterday));
            }
        } else if (isFromList) {
            tv.append(", " + context.getString(diffDay < -1 ? C0836R.string.yesterday : C0836R.string.tomorrow));
        } else {
            tv.setText(context.getString(diffDay < -1 ? C0836R.string.yesterday : C0836R.string.tomorrow));
        }
    }

    static int getActionType(Context context, int uniqueId, String fromWhere) {
        if (isFromExternal(fromWhere)) {
            Log.secD("WorldclockUtils", "getActionType() -> uniqueId : " + uniqueId);
            if (uniqueId != -1) {
                return 2;
            }
            return 1;
        } else if (WorldclockDBManager.isDuplication(context, uniqueId)) {
            return 0;
        } else {
            return isFromWorldclockListWhereCity(fromWhere) ? 2 : 1;
        }
    }
}
