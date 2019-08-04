package com.samsung.android.calendar.secfeature;

import com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarConverter;
import com.samsung.android.feature.SemCscFeature;

public class SECCalendarFeatures {
    private static SECCalendarFeatures sInstance = null;

    public static SECCalendarFeatures getInstance() {
        if (sInstance == null) {
            String CSC = SemCscFeature.getInstance().getString("CscFeature_Calendar_EnableLocalHolidayDisplay");
            if ("KOREA".equals(CSC)) {
                sInstance = new KOR_SECCalendarFeatures();
            } else if ("CHINA".equals(CSC)) {
                sInstance = new CHN_SECCalendarFeatures();
            } else if ("HKTW".equals(CSC)) {
                sInstance = new HKTW_SECCalendarFeatures();
            } else if ("JAPAN".equals(CSC)) {
                sInstance = new JPN_SECCalendarFeatures();
            } else if ("VI".equals(CSC)) {
                sInstance = new VI_SECCalendarFeatures();
            } else {
                sInstance = new SECCalendarFeatures();
            }
        }
        return sInstance;
    }

    public SolarLunarConverter getSolarLunarConverter() {
        return null;
    }
}
