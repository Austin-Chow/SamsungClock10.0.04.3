package com.samsung.android.calendar.secfeature;

import com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarConverter;
import com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarTablesKOR;

public class KOR_SECCalendarFeatures extends SECCalendarFeatures {
    public SolarLunarConverter getSolarLunarConverter() {
        return new SolarLunarConverter(new SolarLunarTablesKOR());
    }
}
