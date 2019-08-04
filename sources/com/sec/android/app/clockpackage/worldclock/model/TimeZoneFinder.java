package com.sec.android.app.clockpackage.worldclock.model;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.xmlpull.v1.XmlPullParser;

public class TimeZoneFinder {
    private Context mContext;
    private LinkedList<Integer> mCurrentCitiesId = null;
    private LinkedList<Integer> mCurrentCitiesIdFromTimeZone = null;
    private String mCurrentIso;
    private Map<String, LinkedList<Integer>> mIsoCode2CitiesMap;
    private Map<String, Integer> mIsoCode2CityMap;

    public TimeZoneFinder(Context context) {
        this.mContext = context;
        this.mIsoCode2CityMap = new HashMap();
        this.mIsoCode2CitiesMap = new HashMap();
        loadMap();
        this.mCurrentIso = getCurrentCountryFromMnc();
    }

    public int updateLocationMncAndTimeZone() {
        if (this.mCurrentIso == null || this.mCurrentIso.equalsIgnoreCase("undefined")) {
            this.mCurrentIso = getCurrentCountryFromMnc();
        }
        Log.secD("TimeZoneFinder", "updateLocationMncAndTimeZone =  " + this.mCurrentIso);
        if (this.mCurrentIso.equalsIgnoreCase("undefined")) {
            Log.secD("TimeZoneFinder", "iso code is undefined in updateLocationMncAndTimeZone =  " + this.mCurrentIso);
            return findCurrentCityFromTimeZone();
        } else if (this.mIsoCode2CityMap.containsKey(this.mCurrentIso)) {
            int currentCityId = ((Integer) this.mIsoCode2CityMap.get(this.mCurrentIso)).intValue();
            this.mCurrentCitiesId = new LinkedList();
            this.mCurrentCitiesId.addLast(Integer.valueOf(currentCityId));
            return findCurrentCitiesFromTimeZone() == 1 ? 1 : 0;
        } else if (!this.mIsoCode2CitiesMap.containsKey(this.mCurrentIso)) {
            return -1;
        } else {
            this.mCurrentCitiesId = (LinkedList) this.mIsoCode2CitiesMap.get(this.mCurrentIso);
            if (findCurrentCitiesFromTimeZone() == 1) {
                return 1;
            }
            return 0;
        }
    }

    private int findCurrentCitiesFromTimeZone() {
        int result = 0;
        int baseTzOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        Log.secD("TimeZoneFinder", "findCurrentCitiesFromTimeZone(), timezone baseTzOffset : " + baseTzOffset);
        this.mCurrentCitiesIdFromTimeZone = new LinkedList();
        Iterator it = this.mCurrentCitiesId.iterator();
        while (it.hasNext()) {
            int cityId = ((Integer) it.next()).intValue();
            int localOffset = CityManager.findCityLocalOffsetByUniqueId(Integer.valueOf(cityId));
            if (baseTzOffset == localOffset) {
                Log.secD("TimeZoneFinder", "cityName : " + CityManager.findCityCountryNameByUniqueId(Integer.valueOf(cityId)) + ", localOffset : " + localOffset);
                this.mCurrentCitiesIdFromTimeZone.addLast(Integer.valueOf(cityId));
                result = 1;
            }
        }
        return result;
    }

    private int findCurrentCityFromTimeZone() {
        int result = -1;
        Log.secD("TimeZoneFinder", "findCurrentCitiesFromTimeZone() without iso code");
        String timeZoneId = TimeZone.getDefault().getID();
        String cityName = timeZoneId.split("/")[1];
        int cityId = CityManager.findUniqueIdByEngCityName(cityName).intValue();
        if (cityId != -1) {
            this.mCurrentCitiesIdFromTimeZone = new LinkedList();
            this.mCurrentCitiesIdFromTimeZone.addLast(Integer.valueOf(cityId));
            result = 1;
        }
        Log.secD("TimeZoneFinder", "cityName : " + cityName + " timeZoneId : " + timeZoneId + " cityId : " + cityId);
        return result;
    }

    public LinkedList<Integer> getCurrentCitiesIdFromTimeZone() {
        return this.mCurrentCitiesIdFromTimeZone;
    }

    private String getCurrentCountryFromMnc() {
        TelephonyManager telManager = (TelephonyManager) this.mContext.getSystemService("phone");
        String currentRawIsoCode = telManager != null ? telManager.getNetworkCountryIso() : "";
        if (currentRawIsoCode != null) {
            Log.secD("TimeZoneFinder", "Current Location ISO Code(size)  : " + currentRawIsoCode.length());
        } else {
            Log.secD("TimeZoneFinder", "Error = currentRawIsoCode value is null in TelephonyManager.");
        }
        String timezone = Calendar.getInstance().getTimeZone().getID();
        if (currentRawIsoCode != null && currentRawIsoCode.length() == 0) {
            if (timezone.equals("Asia/Shanghai") || timezone.equals("Asia/Hong_Kong") || timezone.equals("Asia/Taipei")) {
                currentRawIsoCode = "cn";
            } else if (timezone.equals("Asia/Calcutta")) {
                currentRawIsoCode = "in";
            }
        }
        if (currentRawIsoCode == null || currentRawIsoCode.length() == 0) {
            return "undefined";
        }
        String isoCode = currentRawIsoCode.toLowerCase(Locale.US);
        Log.secD("TimeZoneFinder", "Current Location ISO Code(From 1st Provider)  : " + isoCode);
        return isoCode;
    }

    private void loadMap() {
        XmlPullParser parser = this.mContext.getResources().getXml(C0836R.xml.location_map);
        if (parser != null) {
            while (parser.next() != 1) {
                int type = 0;
                int id = -1;
                String isoCode = null;
                String cityName = null;
                String name = parser.getName();
                if (name != null && name.equals("City")) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        String attribute = parser.getAttributeName(i);
                        if (attribute != null && attribute.equals("isoCode")) {
                            isoCode = parser.getAttributeValue(i);
                        } else if (attribute != null && attribute.equals("id")) {
                            id = Integer.parseInt(parser.getAttributeValue(i));
                        } else if (attribute != null && attribute.equals("type")) {
                            type = Integer.parseInt(parser.getAttributeValue(i));
                        } else if (attribute != null && attribute.equals("name")) {
                            cityName = parser.getAttributeValue(i);
                        }
                        if (!(isoCode == null || id == -1 || cityName == null)) {
                            switch (type) {
                                case 1:
                                    this.mIsoCode2CityMap.put(isoCode, Integer.valueOf(id));
                                    break;
                                case 2:
                                case 3:
                                    try {
                                        if (!this.mIsoCode2CitiesMap.containsKey(isoCode)) {
                                            LinkedList<Integer> cities = new LinkedList();
                                            cities.addLast(Integer.valueOf(id));
                                            this.mIsoCode2CitiesMap.put(isoCode, cities);
                                            break;
                                        }
                                        ((LinkedList) this.mIsoCode2CitiesMap.get(isoCode)).addLast(Integer.valueOf(id));
                                        break;
                                    } catch (Exception e) {
                                        Log.secE("TimeZoneFinder", "Exception : " + e.toString());
                                        return;
                                    }
                                default:
                                    continue;
                            }
                        }
                    }
                    continue;
                }
            }
        }
    }

    public void updateCitiesIdFromIso() {
        if (this.mCurrentIso == null || this.mCurrentIso.equalsIgnoreCase("undefined")) {
            this.mCurrentIso = getCurrentCountryFromMnc();
        }
        if (!this.mCurrentIso.equalsIgnoreCase("undefined")) {
            if (this.mIsoCode2CityMap.containsKey(this.mCurrentIso)) {
                int currentCityId = ((Integer) this.mIsoCode2CityMap.get(this.mCurrentIso)).intValue();
                this.mCurrentCitiesId = new LinkedList();
                this.mCurrentCitiesId.addLast(Integer.valueOf(currentCityId));
            } else if (this.mIsoCode2CitiesMap.containsKey(this.mCurrentIso)) {
                this.mCurrentCitiesId = (LinkedList) this.mIsoCode2CitiesMap.get(this.mCurrentIso);
            }
        }
    }

    public boolean isLocalCity(int cityId) {
        if (this.mCurrentCitiesId == null) {
            return false;
        }
        Iterator it = this.mCurrentCitiesId.iterator();
        while (it.hasNext()) {
            if (cityId == ((Integer) it.next()).intValue()) {
                return true;
            }
        }
        return false;
    }
}
