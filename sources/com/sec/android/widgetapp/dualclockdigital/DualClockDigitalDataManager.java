package com.sec.android.widgetapp.dualclockdigital;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.commonwidget.AppWidgetUtils;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;

public class DualClockDigitalDataManager {
    public static DualClockDigitalWidgetModel loadModel(Context context, int appWidgetId) {
        ViewModelHelper viewModelHelper = new ViewModelHelper(context, appWidgetId);
        DualClockDigitalWidgetModel model = new DualClockDigitalWidgetModel();
        model.setTransparency(255 - viewModelHelper.getTransparency(1));
        model.setBackgroundColor(getBackgroundColor(context, viewModelHelper.getTheme(1)));
        boolean isDarkFont = WidgetSettingUtils.isDarkFont(context, viewModelHelper.getTheme(1), viewModelHelper.getTransparency(1));
        model.setTextColor(getTextFontColor(context, isDarkFont));
        model.setImageColor(getImageColor(context, isDarkFont));
        setTransparency(context, model, viewModelHelper.getTheme(1), viewModelHelper.getTransparency(1));
        model.setSupportedWidget(AppWidgetUtils.isSupportedWidget(context, appWidgetId));
        return model;
    }

    private static int getBackgroundColor(Context context, int theme) {
        return ContextCompat.getColor(context, theme == 1 ? R.color.widget_dark_bg_color : R.color.widget_light_bg_color);
    }

    private static int getTextFontColor(Context context, boolean isDarkFont) {
        return ContextCompat.getColor(context, isDarkFont ? R.color.widget_text_color_theme_light : R.color.widget_text_color_theme_dark);
    }

    private static int getImageColor(Context context, boolean isDarkFont) {
        return ContextCompat.getColor(context, isDarkFont ? R.color.dualclock_widget_middleline_color_theme_light : R.color.dualclock_widget_middleline_color_theme_dark);
    }

    public static void setTransparency(Context context, DualClockDigitalWidgetModel model, int theme, int transparency) {
        boolean isDarkFont = WidgetSettingUtils.isDarkFont(context, theme, transparency);
        model.setTextColor(getTextFontColor(context, isDarkFont));
        model.setBackgroundColor(getBackgroundColor(context, theme));
        model.setTransparency(255 - transparency);
        model.setImageColor(getImageColor(context, isDarkFont));
    }
}
