package com.samsung.android.calendar.secfeature.lunarcalendar;

import android.text.format.Time;

public class SolarLunarTablesCHN extends SolarLunarTables {
    protected final int[] accumulatedLunarDaysCHN;
    protected final byte[] lunarCHN;

    public SolarLunarTablesCHN() {
        this.lunarCHN = new byte[]{(byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 7, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 5, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 2, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 6, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 5, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 8, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 2, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 6, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 2, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 7, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 30, (byte) 2, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 7, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 4, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 2, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 7, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 3, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 8, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 7, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 8, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 10, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 8, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 2, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 7, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 5, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 9, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 6, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 2, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 5, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 11, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 6, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 5, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 2, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 7, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 8, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 3, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 7, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 8, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 3, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 7, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 5, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 30, (byte) 4, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 8, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 6, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 4, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE, (byte) 30, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 3, (byte) 30, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 30, (byte) 29, (byte) 29, (byte) 30, (byte) 29, (byte) 0, Byte.MAX_VALUE};
        this.accumulatedLunarDaysCHN = new int[]{0, 384, 739, 1093, 1477, 1831, 2185, 2569, 2923, 3278, 3662, 4017, 4401, 4755, 5109, 5492, 5847, 6201, 6585, 6940, 7324, 7678, 8033, 8416, 8770, 9125, 9509, 9863, 10218, 10602, 10956, 11340, 11694, 12048, 12432, 12786, 13141, 13525, 13880, 14264, 14618, 14972, 15356, 15710, 16064, 16449, 16803, 17158, 17542, 17896, 18279, 18633, 18988, 19372, 19727, 20081, 20465, 20819, 21203, 21557, 21911, 22295, 22650, 23004, 23389, 23743, 24097, 24481, 24835, 25219, 25573, 25928, 26312, 26666, 27021, 27405, 27759, 28142, 28497, 28851, 29235, 29590, 29944, 30328, 30683, 31036, 31420, 31775, 32159, 32513, 32868, 33252, 33606, 33960, 34344, 34698, 35082, 35436, 35791, 36175, 36530, 36884, 37268, 37622, 38006, 38360, 38714, 39098, 39453, 39808, 40192, 40546, 40900, 41283, 41638, 42022, 42376, 42731, 43115, 43469, 43823, 44207, 44561, 44916, 45300, 45654, 46039, 46393, 46747, 47131, 47485, 47839, 48223, 48578, 48962, 49316, 49671, 50055, 50409, 50763, 51147, 51501, 51856, 52240, 52594, 52978, 53332, 53686, 54070, 54425, 54779, 55163, 55518, 55902, 56256, 56610, 56994, 57348, 57702, 58086, 58441, 58796, 59180, 59534, 59918, 60272, 60626, 61010, 61364, 61719, 62103, 62458, 62842, 63196, 63550, 63933, 64288, 64642, 65026, 65381, 65736, 66119, 66473, 66857, 67211, 67566, 67950, 68304, 68659, 69043, 69397, 69781, 70135, 70489, 70873, 71228, 71582, 71966, 72321, 72675, 73059, 73413, 73797, 74151, 74506, 74890, 75244, 75599, 75983, 76336, 76720, 77074, 77429, 77813, 78168, 78522, 78906, 79260, 79614, 79998, 80352};
        this.lunar = this.lunarCHN;
        this.accumulatedLunarDays = this.accumulatedLunarDaysCHN;
    }

    public boolean isTombSweeping(Time time) {
        int[][] arrayTombSweeping = new int[][]{new int[]{5, 6, 6, 5, 5, 6, 6, 5, 5, 6}, new int[]{6, 5, 5, 5, 6, 5, 5, 5, 6, 5}, new int[]{5, 5, 6, 5, 5, 5, 6, 5, 5, 5}, new int[]{6, 5, 5, 5, 6, 5, 5, 5, 6, 5}, new int[]{5, 5, 6, 5, 5, 5, 5, 5, 5, 5}, new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, new int[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, new int[]{5, 5, 5, 5, 5, 4, 5, 5, 5, 4}, new int[]{5, 5, 5, 4, 5, 5, 5, 4, 5, 5}, new int[]{5, 4, 5, 5, 5, 4, 5, 5, 5, 4}, new int[]{5, 5, 5, 4, 5, 5, 5, 4, 4, 5}, new int[]{5, 4, 4, 5, 5, 4, 4, 5, 5, 4}, new int[]{4, 5, 5, 4, 4, 5, 5, 4, 4, 5}, new int[]{5, 4, 4, 5, 5, 4, 4, 5, 5, 4}, new int[]{4, 4, 5, 4, 4, 4, 5, 4, 4, 4}};
        if (time.month != 3) {
            return false;
        }
        int tempYear = time.year - 1901;
        if (time.monthDay == arrayTombSweeping[tempYear / 10][tempYear % 10]) {
            return true;
        }
        return false;
    }

    public boolean isLunarHoliday(int lYear, int lMonth, int lDay, boolean isLeap) {
        if (isLeap) {
            return false;
        }
        if (lMonth == 0 && lDay == 1) {
            return true;
        }
        if (lMonth == 4 && lDay == 5) {
            return true;
        }
        if (lMonth == 7 && lDay == 15) {
            return true;
        }
        return false;
    }

    public boolean isOtherHoliday(Time time) {
        if (time.month == 0 && time.monthDay == 1) {
            return true;
        }
        if (time.month == 4 && time.monthDay == 1) {
            return true;
        }
        if ((time.year > 1948 && time.month == 9 && time.monthDay == 1) || isTombSweeping(time)) {
            return true;
        }
        return false;
    }
}
