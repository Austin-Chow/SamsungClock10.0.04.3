package com.sec.android.app.clockpackage.worldclock.model;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.os.SemSystemProperties;
import android.util.SparseArray;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import java.text.Collator;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class CityManager {
    public static HashMap<String, City> sCities = null;
    public static City[] sCitiesByRawOffset;
    public static City[] sCitiesByRawOffsetEn;
    public static SparseArray<String> sCitiesId = null;
    private static int sCityCount;
    public static boolean sIsCityManagerLoad = false;
    public static boolean sIsCityManagerLoadForGlobe = false;
    public static boolean sIsCityRestored = false;
    private static boolean sIsReplaceNameTaiwanWithTaipei = Feature.isReplaceNameTaiwanWithTaipei();
    public static boolean sRunActivityInDualclock = false;

    public static void initCity(Context context) {
        if (sIsCityManagerLoadForGlobe) {
            initCityForGlobe(context);
            return;
        }
        removeCity();
        loadCities(context);
        loadCitiesId(context);
        sIsCityManagerLoad = true;
        sIsCityManagerLoadForGlobe = false;
    }

    public static void initCityForGlobe(Context context, boolean isRun) {
        sRunActivityInDualclock = isRun;
        initCityForGlobe(context);
    }

    public static void initCityForGlobe(Context context) {
        removeCity();
        loadCitiesForGlobe(context);
        loadCitiesId(context);
        loadCitiesEn(context);
        sIsCityManagerLoad = true;
        sIsCityManagerLoadForGlobe = true;
    }

    public static void removeCity() {
        Log.secD("CityManager", "removeCity sRunActivityInDualclock : " + sRunActivityInDualclock);
        if (!sRunActivityInDualclock) {
            unloadCities();
            unloadCitiesId();
            sIsCityManagerLoad = false;
            sIsCityManagerLoadForGlobe = false;
        }
    }

    public static synchronized void loadCities(Context context) {
        synchronized (CityManager.class) {
            if (sCities != null) {
                sCities.clear();
                sCities = null;
            }
            sCities = new HashMap();
            String[] cityNamePinyin = null;
            String[] countriesPinyin = null;
            String[] cityName = context.getResources().getStringArray(C0836R.array.Cities);
            int[] cityToCountryMap = context.getResources().getIntArray(C0836R.array.city_country_mapping_table);
            String[] countries = context.getResources().getStringArray(C0836R.array.Countries);
            String[] timeZone = context.getResources().getStringArray(C0836R.array.time_zone_list);
            String[] cityPlaceIds = context.getResources().getStringArray(C0836R.array.city_place_ids);
            if (Feature.isDisableIsraelCountry()) {
                countries[33] = "";
            }
            boolean isChineseLanguage = isChineseLanguage();
            if (isChineseLanguage) {
                cityNamePinyin = context.getResources().getStringArray(C0836R.array.CitiesPinyin);
                countriesPinyin = context.getResources().getStringArray(C0836R.array.CountriesPinyin);
            }
            sCityCount = cityName.length;
            sCitiesByRawOffset = new City[sCityCount];
            for (int i = 0; i < sCityCount; i++) {
                int countryId = cityToCountryMap[i];
                if (sIsReplaceNameTaiwanWithTaipei && countryId == 64) {
                    countryId = "HK".equals(SemSystemProperties.getCountryIso()) ? 210 : 17;
                }
                City city = new City(cityName[i], countries[countryId], timeZone[i], i, cityPlaceIds[i]);
                if (isChineseLanguage) {
                    city.setPinYin(cityNamePinyin[i], countriesPinyin[countryId]);
                }
                sCities.put(cityName[i] + (countries[countryId].length() > 0 ? " / " + countries[countryId] : ""), city);
                sCitiesByRawOffset[i] = city;
            }
        }
    }

    private static synchronized void loadCitiesForGlobe(Context context) {
        synchronized (CityManager.class) {
            if (sCities != null) {
                sCities.clear();
                sCities = null;
            }
            sCities = new HashMap();
            String[] cityNamePinyin = null;
            String[] countriesPinyin = null;
            String[] cityName = context.getResources().getStringArray(C0836R.array.Cities);
            int[] cityToCountryMap = context.getResources().getIntArray(C0836R.array.city_country_mapping_table);
            String[] countries = context.getResources().getStringArray(C0836R.array.Countries);
            String[] timeZone = context.getResources().getStringArray(C0836R.array.time_zone_list);
            String[] cityPlaceIds = context.getResources().getStringArray(C0836R.array.city_place_ids);
            int[] zoomLevel = context.getResources().getIntArray(C0836R.array.zoom_level);
            int[] xLatitude = context.getResources().getIntArray(C0836R.array.x_coordinate);
            int[] yLongitude = context.getResources().getIntArray(C0836R.array.y_coordinate);
            int[] arrowDirection = context.getResources().getIntArray(C0836R.array.arrow_direction);
            String[] xLatitudeBillboard = context.getResources().getStringArray(C0836R.array.x_coordinate_billboard);
            String[] yLongitudeBillboard = context.getResources().getStringArray(C0836R.array.y_coordinate_billboard);
            int[] xTextOffset = context.getResources().getIntArray(C0836R.array.x_textOffset);
            int[] yTextOffset = context.getResources().getIntArray(C0836R.array.y_textOffset);
            if (Feature.isDisableIsraelCountry()) {
                countries[33] = "";
            }
            boolean isChineseLanguage = isChineseLanguage();
            if (isChineseLanguage) {
                cityNamePinyin = context.getResources().getStringArray(C0836R.array.CitiesPinyin);
                countriesPinyin = context.getResources().getStringArray(C0836R.array.CountriesPinyin);
            }
            float latitudeBillboard = 0.0f;
            float longitudeBillboard = 0.0f;
            sCityCount = cityName.length;
            sCitiesByRawOffset = new City[sCityCount];
            for (int i = 0; i < sCityCount; i++) {
                int countryId = cityToCountryMap[i];
                try {
                    latitudeBillboard = Float.parseFloat(xLatitudeBillboard[i]);
                } catch (IndexOutOfBoundsException e) {
                    Log.secE("CityManager", e.toString() + " Float.parseFloat error! Parsed xLatitudeBillboard[" + i + "] string = " + xLatitudeBillboard[i]);
                }
                try {
                    longitudeBillboard = Float.parseFloat(yLongitudeBillboard[i]);
                } catch (IndexOutOfBoundsException e2) {
                    Log.secE("CityManager", e2.toString() + " Float.parseFloat error! Parsed yLongitudeBillboard[" + i + "] string = " + yLongitudeBillboard[i]);
                }
                if (sIsReplaceNameTaiwanWithTaipei && countryId == 64) {
                    countryId = "HK".equals(SemSystemProperties.getCountryIso()) ? 210 : 17;
                }
                City city = new City(cityName[i], countries[countryId], timeZone[i], zoomLevel[i], arrowDirection[i], i, (float) xLatitude[i], (float) yLongitude[i], latitudeBillboard, longitudeBillboard, xTextOffset[i], yTextOffset[i], cityPlaceIds[i]);
                if (isChineseLanguage) {
                    city.setPinYin(cityNamePinyin[i], countriesPinyin[countryId]);
                }
                sCities.put(cityName[i] + (countries[countryId].length() > 0 ? " / " + countries[countryId] : ""), city);
                sCitiesByRawOffset[i] = city;
            }
        }
    }

    public static synchronized void loadCitiesEn(Context context) {
        synchronized (CityManager.class) {
            Configuration conf = context.getResources().getConfiguration();
            Locale currentLoc = Locale.getDefault();
            conf.setLocale(Locale.ENGLISH);
            String[] cityName = context.createConfigurationContext(conf).getResources().getStringArray(C0836R.array.Cities);
            String[] countries = context.createConfigurationContext(conf).getResources().getStringArray(C0836R.array.Countries);
            conf.setLocale(currentLoc);
            int[] cityToCountryMap = context.getResources().getIntArray(C0836R.array.city_country_mapping_table);
            String[] timeZone = context.getResources().getStringArray(C0836R.array.time_zone_list);
            String[] cityPlaceIds = context.getResources().getStringArray(C0836R.array.city_place_ids);
            if (Feature.isDisableIsraelCountry()) {
                countries[33] = "";
            }
            sCityCount = cityName.length;
            sCitiesByRawOffsetEn = new City[sCityCount];
            for (int i = 0; i < sCityCount; i++) {
                int countryId = cityToCountryMap[i];
                if (sIsReplaceNameTaiwanWithTaipei && countryId == 64) {
                    countryId = "HK".equals(SemSystemProperties.getCountryIso()) ? 210 : 17;
                }
                sCitiesByRawOffsetEn[i] = new City(cityName[i], countries[countryId], timeZone[i], i, cityPlaceIds[i]);
            }
        }
    }

    public static synchronized void unloadCities() {
        synchronized (CityManager.class) {
            if (sCities != null) {
                int count;
                int idxCityRawOffset;
                sCities.clear();
                sCities = null;
                if (sCitiesByRawOffset != null) {
                    count = sCitiesByRawOffset.length;
                    for (idxCityRawOffset = 0; idxCityRawOffset < count; idxCityRawOffset++) {
                        sCitiesByRawOffset[idxCityRawOffset] = null;
                    }
                }
                if (sCitiesByRawOffsetEn != null) {
                    count = sCitiesByRawOffsetEn.length;
                    for (idxCityRawOffset = 0; idxCityRawOffset < count; idxCityRawOffset++) {
                        sCitiesByRawOffsetEn[idxCityRawOffset] = null;
                    }
                }
                sCitiesByRawOffset = null;
                sCitiesByRawOffsetEn = null;
            }
        }
    }

    public static synchronized void loadCitiesId(Context context) {
        synchronized (CityManager.class) {
            if (sCitiesId != null) {
                sCitiesId.clear();
                sCitiesId = null;
            }
            sCitiesId = new SparseArray();
            String[] cityName = context.getResources().getStringArray(C0836R.array.Cities);
            int[] cityToCountryMap = context.getResources().getIntArray(C0836R.array.city_country_mapping_table);
            String[] countries = context.getResources().getStringArray(C0836R.array.Countries);
            if (Feature.isDisableIsraelCountry()) {
                countries[33] = "";
            }
            for (int i = 0; i < sCityCount; i++) {
                int countryId = cityToCountryMap[i];
                if (sIsReplaceNameTaiwanWithTaipei && countryId == 64) {
                    countryId = "HK".equals(SemSystemProperties.getCountryIso()) ? 210 : 17;
                }
                sCitiesId.put(i, cityName[i] + (countries[countryId].length() > 0 ? " / " + countries[countryId] : ""));
            }
        }
    }

    private static synchronized void unloadCitiesId() {
        synchronized (CityManager.class) {
            if (sCitiesId != null) {
                sCitiesId.clear();
                sCitiesId = null;
            }
        }
    }

    public static void cleanDBSelected() {
        if (sCities != null) {
            for (City c : sCitiesByRawOffset) {
                c.setDBSelected(false);
            }
        }
    }

    public static void cleanDBCurrentLocation() {
        if (sCities != null) {
            for (City c : sCitiesByRawOffset) {
                c.setDBCurrentLocation(false);
            }
        }
    }

    public static City[] getCitiesByName() {
        TreeSet<String> ts = new TreeSet();
        Hashtable<String, City> ht = new Hashtable();
        try {
            boolean isChineseLanguage = isChineseLanguage();
            for (City c : sCitiesByRawOffset) {
                if (isChineseLanguage) {
                    ts.add(c.getNamePinyin() + " / " + c.getCountryPinyin());
                    ht.put(c.getNamePinyin() + " / " + c.getCountryPinyin(), c);
                } else {
                    ts.add(c.getName() + " / " + c.getCountry());
                    ht.put(c.getName() + " / " + c.getCountry(), c);
                }
            }
        } catch (NullPointerException e) {
            Log.secE("CityManager", "Exception : " + e.toString());
        }
        Iterator<String> it = ts.iterator();
        City[] c2 = new City[ts.size()];
        int n = 0;
        ArrayList<String> list = new ArrayList();
        while (it.hasNext()) {
            list.add(it.next());
        }
        Collator collator = Collator.getInstance(new Locale(Locale.getDefault().getLanguage()));
        if ("TW".equals(Locale.getDefault().getCountry())) {
            collator = Collator.getInstance(Locale.TAIWAN);
            collator.setStrength(0);
        }
        Collections.sort(list, collator);
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            int n2 = n + 1;
            c2[n] = (City) ht.get((String) it2.next());
            n = n2;
        }
        return c2;
    }

    public static City[] getCitiesByEngName() {
        TreeSet<String> ts = new TreeSet();
        Hashtable<String, City> ht = new Hashtable();
        try {
            if (sCitiesByRawOffsetEn != null) {
                for (City c : sCitiesByRawOffsetEn) {
                    ts.add(c.getName() + " / " + c.getCountry());
                    ht.put(c.getName() + " / " + c.getCountry(), c);
                }
            }
        } catch (NullPointerException e) {
            Log.secE("CityManager", "Exception : " + e.toString());
        }
        Iterator<String> it = ts.iterator();
        City[] c2 = new City[ts.size()];
        int n = 0;
        ArrayList<String> list = new ArrayList();
        while (it.hasNext()) {
            list.add(it.next());
        }
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            int n2 = n + 1;
            c2[n] = (City) ht.get((String) it2.next());
            n = n2;
        }
        return c2;
    }

    private static int checkCapitalIndex(TimeZone timezone, ArrayList allCitiesInCurrentTimeZone) {
        int index = 0;
        if (timezone.getID().equalsIgnoreCase("Africa/Douala")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Africa/Abidjan")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Pacific/Auckland")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Asia/Kolkata")) {
            index = 3;
        } else if (timezone.getID().equalsIgnoreCase("Asia/Riyadh")) {
            index = 2;
        } else if (timezone.getID().equalsIgnoreCase("Europe/Moscow")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Asia/Karachi")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Europe/Kiev")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Africa/Casablanca")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("America/Sao_Paulo")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("America/Los_Angeles")) {
            index = 1;
        } else if (timezone.getID().equalsIgnoreCase("Asia/Yekaterinburg")) {
            index = 1;
        }
        if (index >= allCitiesInCurrentTimeZone.size()) {
            return 0;
        }
        return index;
    }

    public static String findDefaultCityByCapital(TimeZone timezone) {
        int length;
        int i = 0;
        ArrayList<City> allCitiesInCurrentTimeZone = new ArrayList();
        Log.secD("CityManager", "findDefaultCityByCapital() timezone.getID() : " + timezone.getID());
        String[][] convertingTimeZoneIds = new String[13][];
        convertingTimeZoneIds[0] = new String[]{"Asia/Saigon", "Asia/Ho_Chi_Minh"};
        convertingTimeZoneIds[1] = new String[]{"Asia/Vientiane", "Asia/Bangkok"};
        convertingTimeZoneIds[2] = new String[]{"Asia/Calcutta", "Asia/Kolkata"};
        convertingTimeZoneIds[3] = new String[]{"America/Chihuahua", "America/Mazatlan"};
        convertingTimeZoneIds[4] = new String[]{"America/Manaus", "America/Sao_Paulo"};
        convertingTimeZoneIds[5] = new String[]{"Atlantic/Cape_Verde", "Atlantic/Azores"};
        convertingTimeZoneIds[6] = new String[]{"Europe/Sarajevo", "Europe/Belgrade"};
        convertingTimeZoneIds[7] = new String[]{"Africa/Brazzaville", "Africa/Kinshasa"};
        convertingTimeZoneIds[8] = new String[]{"Africa/Windhoek", "Africa/Luanda"};
        convertingTimeZoneIds[9] = new String[]{"Asia/Oral", "Asia/Yekaterinburg"};
        convertingTimeZoneIds[10] = new String[]{"Pacific/Majuro", "Pacific/Tarawa"};
        convertingTimeZoneIds[11] = new String[]{"Australia/Lord_Howe", "Australia/Sydney"};
        convertingTimeZoneIds[12] = new String[]{"America/Buenos_Aires", "America/Argentina/Buenos_Aires"};
        for (String[] timeZoneIds : convertingTimeZoneIds) {
            if (timeZoneIds[0].equalsIgnoreCase(timezone.getID())) {
                timezone.setID(timeZoneIds[1]);
                break;
            }
        }
        if (sCitiesByRawOffset != null) {
            City[] cityArr = sCitiesByRawOffset;
            length = cityArr.length;
            while (i < length) {
                City c = cityArr[i];
                if (c.getTimeZoneId().equals(timezone.getID())) {
                    allCitiesInCurrentTimeZone.add(c);
                }
                i++;
            }
        }
        if (allCitiesInCurrentTimeZone.size() < 1) {
            return "";
        }
        int capitalIndex = checkCapitalIndex(timezone, allCitiesInCurrentTimeZone);
        Log.secD("CityManager", "capital city----" + ((City) allCitiesInCurrentTimeZone.get(capitalIndex)).getName());
        Log.secD("CityManager", "Time zone id- " + timezone.getID());
        return (((City) allCitiesInCurrentTimeZone.get(capitalIndex)).getName().length() > 0 ? ((City) allCitiesInCurrentTimeZone.get(capitalIndex)).getName() : "") + (((City) allCitiesInCurrentTimeZone.get(capitalIndex)).getCountry().length() > 0 ? " / " + ((City) allCitiesInCurrentTimeZone.get(capitalIndex)).getCountry() : "");
    }

    public static City findCityByName(String city) {
        if (sCitiesByRawOffset != null) {
            for (City c : sCitiesByRawOffset) {
                if (c.getName().equals(city)) {
                    return c;
                }
            }
        }
        return null;
    }

    public static City findCityByUniqueId(Integer uniqueId) {
        String cityCountry = findCityCountryNameByUniqueId(uniqueId);
        if (sCities == null || cityCountry == null) {
            return null;
        }
        return (City) sCities.get(cityCountry);
    }

    public static City findCityObjectByName(String cityCountry) {
        if (sCities == null || cityCountry == null) {
            return null;
        }
        return (City) sCities.get(cityCountry);
    }

    public static String findCityCountryNameByUniqueId(Integer cityUniqueId) {
        if (sCitiesId == null || cityUniqueId == null) {
            return null;
        }
        String cityName = (String) sCitiesId.get(cityUniqueId.intValue());
        return cityName != null ? cityName : null;
    }

    static Integer findUniqueIdByEngCityName(String cityName) {
        Log.secD("CityManager", "findUniqueIdByEngCityName() => cityName : " + cityName);
        int cityUniqueId = -1;
        if (sCitiesByRawOffsetEn == null) {
            Log.secD("CityManager", "findUniqueIdByEngCityName() => sCitiesByRawOffsetEn is null");
            return Integer.valueOf(-1);
        }
        for (City c : sCitiesByRawOffsetEn) {
            if (c.getName().equals(cityName)) {
                cityUniqueId = c.getUniqueId();
                break;
            }
        }
        return Integer.valueOf(cityUniqueId);
    }

    public static Integer findUniqueIdByIOsEngCityName(String cityName) {
        int i = 0;
        Log.secD("CityManager", "findUniqueIdByIOsEngCityName() => cityName : " + cityName);
        int cityUniqueId = -1;
        if (sCitiesByRawOffsetEn == null) {
            Log.secD("CityManager", "findUniqueIdByIOsEngCityName() => sCitiesByRawOffsetEn is null");
            return Integer.valueOf(-1);
        }
        String[] strArray = cityName.split("#");
        if (strArray != null) {
            if (strArray.length == 2) {
                if ("Kingston".equals(strArray[0]) && "jm".equals(strArray[1])) {
                    return Integer.valueOf(219);
                }
                if ("St. John's".equals(strArray[0])) {
                    if ("ca".equals(strArray[1])) {
                        return Integer.valueOf(286);
                    }
                    if ("ag".equals(strArray[1])) {
                        return Integer.valueOf(32);
                    }
                }
            } else if (strArray.length == 1) {
                if ("San Jose".equals(cityName)) {
                    return Integer.valueOf(276);
                }
                if ("San José".equals(cityName)) {
                    return Integer.valueOf(277);
                }
                if ("The Settlement".equals(cityName)) {
                    return Integer.valueOf(-1);
                }
                cityName = validateIosCityName(cityName);
            }
        }
        City[] cityArr = sCitiesByRawOffsetEn;
        int length = cityArr.length;
        while (i < length) {
            City c = cityArr[i];
            if (c.getName().equals(cityName)) {
                cityUniqueId = c.getUniqueId();
                break;
            }
            i++;
        }
        return Integer.valueOf(cityUniqueId);
    }

    private static String validateIosCityName(String parameter) {
        if (parameter == null) {
            return null;
        }
        parameter = deAccent(parameter);
        if ("Petropavlovsk-Kamchatsky".equals(parameter)) {
            return String.valueOf("Petropavlovsk-Kamchatskiy");
        }
        if ("Washington".equals(parameter)) {
            return String.valueOf("Washington DC");
        }
        if ("Nukuʻalofa".equals(parameter)) {
            return String.valueOf("Nuku'alofa");
        }
        if ("Sanaa".equals(parameter)) {
            return String.valueOf("Sana'a");
        }
        if ("Canton".equals(parameter)) {
            return String.valueOf("Guangzhou");
        }
        return parameter;
    }

    private static String deAccent(String str) {
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(Normalizer.normalize(str, Form.NFD)).replaceAll("");
    }

    public static int findCityLocalOffsetByUniqueId(Integer cityUniqueId) {
        if (sCitiesId == null || cityUniqueId == null) {
            return 0;
        }
        String cityName = (String) sCitiesId.get(cityUniqueId.intValue());
        if (cityName == null) {
            return 0;
        }
        City city = findCityObjectByName(cityName);
        if (city != null) {
            return city.getLocalOffset();
        }
        return 0;
    }

    public static ArrayList<String> getCitiesOrderByName() {
        int i = 0;
        City[] city = getCitiesByName();
        ArrayList<String> items = new ArrayList();
        int length;
        if (isChineseLanguage()) {
            length = city.length;
            while (i < length) {
                items.add(city[i].getNamePinyin());
                i++;
            }
        } else {
            length = city.length;
            while (i < length) {
                items.add(city[i].getName());
                i++;
            }
        }
        return items;
    }

    public static String findEngOnlyCityNameByUniqueId(Integer cityUniqueId) {
        if (sCitiesByRawOffsetEn != null) {
            for (City c : sCitiesByRawOffsetEn) {
                if (c.getUniqueId() == cityUniqueId.intValue()) {
                    return c.getName();
                }
            }
        }
        return null;
    }

    public static City getCityOfDefaultTime(Context context) {
        TimeZone tz = TimeZone.getDefault();
        if (("GMT".equals(tz.getID()) && tz.getRawOffset() == 0) || tz.getDisplayName().equals("GMT+00:00")) {
            Log.secD("CityManager", "getCityOfDefaultTime() default to Europe/London cause ID:GMT, offset:0");
            tz.setID("Europe/London");
        }
        if ("OMD".equals(SemSystemProperties.getSalesCode()) || "MDO".equals(SemSystemProperties.getSalesCode())) {
            Log.secD("CityManager", "In case of MDO and OMD buyer default city set to Europe/Chisinau");
            tz.setID("Europe/Chisinau");
        }
        String cityCountry = findDefaultCityByCapital(tz);
        if (cityCountry.length() == 0) {
            cityCountry = getCityCountry(context, 44, 69);
        }
        Log.secD("CityManager", "getCityOfDefaultTime() => default city : " + cityCountry + ", timezone : " + tz.getID());
        return findCityObjectByName(cityCountry);
    }

    public static City get2ndDefaultCity(Context context) {
        City defaultCity = getCityOfDefaultTime(context);
        String cityCountry = (defaultCity == null || defaultCity.getUniqueId() != 44) ? getCityCountry(context, 44, 69) : getCityCountry(context, 125, 53);
        Log.secD("CityManager", "get2ndDefaultCity() : " + cityCountry);
        return findCityObjectByName(cityCountry);
    }

    private static String getCityCountry(Context context, int cityId, int countryId) {
        String cityCountry = "";
        try {
            cityCountry = context.getResources().getStringArray(C0836R.array.Cities)[cityId] + " / " + context.getResources().getStringArray(C0836R.array.Countries)[countryId];
        } catch (NotFoundException e) {
            Log.secE("CityManager", "Exception : " + e.toString());
        }
        return cityCountry;
    }

    public static City[] getCitiesByRawOffset() {
        return sCitiesByRawOffset;
    }

    public static void setIsCityRestored(boolean restored) {
        sIsCityRestored = restored;
    }

    private static boolean isChineseLanguage() {
        String languageTag = Locale.getDefault().toLanguageTag();
        if ("zh-Hans-CN".equalsIgnoreCase(languageTag) || "zh-Hans-HK".equalsIgnoreCase(languageTag) || "zh-Hans-MO".equalsIgnoreCase(languageTag) || "zh-Hant-TW".equalsIgnoreCase(languageTag) || "zh-Hant-MO".equalsIgnoreCase(languageTag) || "zh-Hant-HK".equalsIgnoreCase(languageTag)) {
            return true;
        }
        return false;
    }
}
