package com.sec.android.app.clockpackage.commonwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ViewModelHelper {
    private final Context mContext;
    private final SharedPreferences mPref;
    private String mWidgetIdString;

    public ViewModelHelper(Context context, int appWidgetId) {
        this.mContext = context;
        this.mPref = WidgetSettingUtils.getSharedPreferences(context);
        this.mWidgetIdString = String.valueOf(appWidgetId);
    }

    public String getTransparentKey(int widgetType) {
        if (widgetType == 0) {
            return "preferences_digital_clock_widget_transparent_" + this.mWidgetIdString;
        }
        if (widgetType == 1) {
            return "preferences_dual_clock_widget_transparent_" + this.mWidgetIdString;
        }
        return "preferences_alarm_widget_transparent_" + this.mWidgetIdString;
    }

    public String getThemeKey(int widgetType) {
        if (widgetType == 0) {
            return "preferences_digital_clock_widget_theme_" + this.mWidgetIdString;
        }
        if (widgetType == 1) {
            return "preferences_dual_clock_widget_theme_" + this.mWidgetIdString;
        }
        return "preferences_alarm_widget_theme_" + this.mWidgetIdString;
    }

    public int getTransparency(int widgetType) {
        return this.mPref.getInt(getTransparentKey(widgetType), WidgetSettingUtils.getDefaultTransparency(widgetType));
    }

    public int getTheme(int widgetType) {
        return this.mPref.getInt(getThemeKey(widgetType), 0);
    }

    public void removePreference(int widgetType) {
        Editor editor = WidgetSettingUtils.getSharedPreferences(this.mContext).edit();
        editor.remove(getTransparentKey(widgetType));
        editor.remove(getThemeKey(widgetType));
        editor.apply();
    }
}
