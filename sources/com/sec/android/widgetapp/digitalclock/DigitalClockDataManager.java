package com.sec.android.widgetapp.digitalclock;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;

public class DigitalClockDataManager {
    public static DigitalClockWidgetModel loadModel(Context context, int appWidgetId, int widgetSize) {
        ViewModelHelper viewModelHelper = new ViewModelHelper(context, appWidgetId);
        DigitalClockWidgetModel model = new DigitalClockWidgetModel();
        model.setTransparency(255 - viewModelHelper.getTransparency(0));
        model.setBackgroundColor(getBackgroundColor(context, viewModelHelper.getTheme(0)));
        model.setTextColor(getTextFontColor(context, WidgetSettingUtils.isDarkFont(context, viewModelHelper.getTheme(0), viewModelHelper.getTransparency(0))));
        setWidgetSize(model, widgetSize);
        setTransparency(context, model, viewModelHelper.getTheme(0), viewModelHelper.getTransparency(0));
        return model;
    }

    private static int getBackgroundColor(Context context, int theme) {
        return ContextCompat.getColor(context, theme == 1 ? R.color.widget_dark_bg_color : R.color.widget_light_bg_color);
    }

    private static int getTextFontColor(Context context, boolean isDarkFont) {
        return ContextCompat.getColor(context, isDarkFont ? R.color.widget_text_color_theme_light : R.color.widget_text_color_theme_dark);
    }

    public static void setTransparency(Context context, DigitalClockWidgetModel model, int theme, int transparency) {
        model.setTextColor(getTextFontColor(context, WidgetSettingUtils.isDarkFont(context, theme, transparency)));
        model.setBackgroundColor(getBackgroundColor(context, theme));
        model.setTransparency(255 - transparency);
    }

    private static void setWidgetSize(DigitalClockWidgetModel model, int widgetSize) {
        model.setWidgetSize(widgetSize);
    }
}
