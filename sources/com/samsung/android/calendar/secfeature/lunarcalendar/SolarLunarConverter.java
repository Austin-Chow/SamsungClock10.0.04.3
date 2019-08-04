package com.samsung.android.calendar.secfeature.lunarcalendar;

import android.text.format.Time;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;

public class SolarLunarConverter {
    private static int sIndexOfYear = -1;
    private final int MAX_LUNAR_YEAR_OFFSET = SGKeyCode.CODE_CLIPBOARD;
    private final int TOTAL_DAYS_TO_18810130 = 686686;
    private int[] mAcmDaysInLeapYear = new int[]{0, 31, 60, 91, 121, SGKeyCode.CODE_NUMPAD_8, 182, 213, 244, 274, 305, 335, 366};
    private int[] mAcmDaysInYear = new int[]{0, 31, 59, 90, 120, SGKeyCode.CODE_NUMPAD_7, 181, 212, 243, 273, 304, 334, 365};
    private int mDay;
    private boolean mIsLeapMonth;
    private int mMonth;
    private SolarLunarTables mSolarLunarTables;
    private int mYear;

    public SolarLunarConverter(SolarLunarTables tables) {
        this.mSolarLunarTables = tables;
    }

    public int getTotalDaysTo(int y) {
        int year = y - 1;
        return (((year * 365) + (year / 4)) - (year / 100)) + (year / 400);
    }

    private boolean isLeapYear(int y) {
        return y % 4 <= 0 && (y % 100 >= 1 || y % 400 <= 0);
    }

    private int[] getAccumulatedDays(int year) {
        if (isLeapYear(year)) {
            return this.mAcmDaysInLeapYear;
        }
        return this.mAcmDaysInYear;
    }

    public void convertSolarToLunar(int y, int m, int d) {
        this.mIsLeapMonth = false;
        if (y < 1881 || y > 2100 || m < 0 || m > 11 || d < 1 || d > 31) {
            throw new IllegalArgumentException("The date " + y + "/" + m + "/" + d + " is out of range.");
        }
        int indexOfYear;
        this.mDay = (((getTotalDaysTo(y) + getAccumulatedDays(y)[m]) + d) - 686686) + 1;
        if (sIndexOfYear <= 0 || this.mSolarLunarTables.accumulatedLunarDays[sIndexOfYear - 1] >= this.mDay || this.mDay > this.mSolarLunarTables.accumulatedLunarDays[sIndexOfYear]) {
            indexOfYear = this.mDay <= this.mSolarLunarTables.accumulatedLunarDays[110] ? 1 : 111;
            while (indexOfYear < SGKeyCode.CODE_CLIPBOARD && this.mDay > this.mSolarLunarTables.accumulatedLunarDays[indexOfYear]) {
                indexOfYear++;
            }
            sIndexOfYear = indexOfYear;
        } else {
            indexOfYear = sIndexOfYear;
        }
        indexOfYear--;
        this.mSolarLunarTables.getClass();
        int startIndexOfYear = indexOfYear * 14;
        this.mYear = indexOfYear + 1881;
        this.mDay -= this.mSolarLunarTables.accumulatedLunarDays[indexOfYear];
        byte[] bArr = this.mSolarLunarTables.lunar;
        this.mSolarLunarTables.getClass();
        int leapMonth = bArr[startIndexOfYear + 13];
        int numOfMonth = leapMonth == 127 ? 12 : 13;
        this.mMonth = -1;
        int j = 0;
        while (j < numOfMonth) {
            int m1 = this.mSolarLunarTables.lunar[startIndexOfYear + j];
            if (leapMonth == j) {
                this.mIsLeapMonth = true;
            } else {
                this.mMonth++;
                this.mIsLeapMonth = false;
            }
            if (this.mDay > m1) {
                this.mDay -= m1;
                j++;
            } else {
                return;
            }
        }
    }

    public boolean isHoliday(Time solarTime) {
        convertSolarToLunar(solarTime.year, solarTime.month, solarTime.monthDay);
        return this.mSolarLunarTables.isOtherHoliday(solarTime) || this.mSolarLunarTables.isLunarHoliday(this.mYear, this.mMonth, this.mDay, this.mIsLeapMonth);
    }

    public boolean isSubstHoliday(Time solarTime) {
        return this.mSolarLunarTables.isSubstHoliday(solarTime);
    }
}
