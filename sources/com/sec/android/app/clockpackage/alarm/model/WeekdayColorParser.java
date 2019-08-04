package com.sec.android.app.clockpackage.alarm.model;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.Log;

public class WeekdayColorParser {
    public static int[] getColors(int[] colorSet, int firstWeekDay) {
        String weekdayString;
        String DEFAULT_WEEKDAY_COLORS = "XXXXXXR";
        try {
            weekdayString = Feature.getWeekDayColor();
        } catch (NullPointerException e) {
            weekdayString = null;
            Log.secD("WeekdayColorParser", "NullPointerException - Feature.getCscString");
        }
        if (weekdayString == null || "".equals(weekdayString) || weekdayString.isEmpty() || weekdayString.length() != 7) {
            weekdayString = "XXXXXXR";
        }
        if (colorSet == null || colorSet.length != 3) {
            colorSet = new int[]{ViewCompat.MEASURED_STATE_MASK, -16776961, SupportMenu.CATEGORY_MASK};
        }
        int[] colorValues = new int[7];
        for (int i = 0; i < 7; i++) {
            colorValues[i] = getColor(weekdayString.charAt(i), colorSet);
        }
        return rotate(colorValues, getRotateOffset(firstWeekDay));
    }

    private static int[] rotate(int[] colorValues, int offset) {
        int length = colorValues.length;
        int[] newColors = new int[length];
        for (int i = 0; i < length; i++) {
            newColors[(i + offset) % length] = colorValues[i];
        }
        return newColors;
    }

    private static int getRotateOffset(int startWeekDay) {
        switch (startWeekDay) {
            case 0:
                return 1;
            case 2:
                return 6;
            case 3:
                return 5;
            case 4:
                return 4;
            case 5:
                return 3;
            case 6:
                return 2;
            default:
                return 0;
        }
    }

    private static int getColor(char color, int[] colorSet) {
        if (color == 'R') {
            return colorSet[2];
        }
        if (color == 'B') {
            return colorSet[1];
        }
        return colorSet[0];
    }
}
