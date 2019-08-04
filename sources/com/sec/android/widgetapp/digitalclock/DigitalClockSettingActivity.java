package com.sec.android.widgetapp.digitalclock;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingActivity;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;
import com.sec.android.widgetapp.digitalclock.DigitalClockContract.ViewModel;

public class DigitalClockSettingActivity extends WidgetSettingActivity {
    private ViewGroup mPreviewFrame;
    private ViewModel mViewModel;

    protected void initLayout() {
        super.initLayout();
        drawPreview();
    }

    protected void updatePreview() {
        Log.secD("DigitalClockSettingActivity", "updatePreview");
        if (this.mPreviewFrame != null) {
            boolean isDarkFont = WidgetSettingUtils.isDarkFont(this, this.mTheme, this.mTransparency);
            int theme = ContextCompat.getColor(this, this.mTheme == 1 ? R.color.widget_dark_bg_color : R.color.widget_light_bg_color);
            int textColor = ContextCompat.getColor(this, isDarkFont ? R.color.widget_text_color_theme_light : R.color.widget_text_color_theme_dark);
            ImageView widgetBackground = (ImageView) this.mPreviewFrame.findViewById(R.id.widget_background);
            TextClock clockTime = (TextClock) this.mPreviewFrame.findViewById(R.id.clock_time);
            TextClock ampmTextLeft = (TextClock) this.mPreviewFrame.findViewById(R.id.ampm_text_left);
            TextClock ampmText = (TextClock) this.mPreviewFrame.findViewById(R.id.ampm_text);
            TextClock weekText = (TextClock) this.mPreviewFrame.findViewById(R.id.week_text);
            TextClock dateTextLarge = (TextClock) this.mPreviewFrame.findViewById(R.id.date_text_large);
            TextClock dateTextLargePersian = (TextClock) this.mPreviewFrame.findViewById(R.id.date_text_large_persian);
            if (widgetBackground != null) {
                widgetBackground.setColorFilter(theme);
                widgetBackground.setImageAlpha(255 - this.mTransparency);
            }
            if (clockTime != null) {
                clockTime.setTextColor(textColor);
            }
            if (ampmTextLeft != null) {
                ampmTextLeft.setTextColor(textColor);
            }
            if (ampmText != null) {
                ampmText.setTextColor(textColor);
            }
            if (weekText != null) {
                weekText.setTextColor(textColor);
            }
            if (dateTextLarge != null) {
                dateTextLarge.setTextColor(textColor);
            }
            if (dateTextLargePersian != null) {
                dateTextLargePersian.setTextColor(textColor);
            }
        }
    }

    protected void drawPreview() {
        ViewModel viewModel = getViewModel();
        viewModel.setTransparency(this, this.mTheme, this.mTransparency);
        viewModel.refresh(this, this.mAppWidgetId, true);
        ViewGroup previewFrame = getPreviewFrame();
        previewFrame.addView(viewModel.getRemoteViews().apply(this, previewFrame));
    }

    private ViewGroup getPreviewFrame() {
        if (this.mPreviewFrame == null) {
            this.mPreviewFrame = (ViewGroup) findViewById(R.id.widget_preview);
        }
        this.mPreviewFrame.removeAllViewsInLayout();
        return this.mPreviewFrame;
    }

    private ViewModel getViewModel() {
        if (this.mViewModel == null) {
            int widgetSize = (StateUtils.isScreenDp(this.mContext, 457) || (!StateUtils.isScreenDp(this.mContext, 457) && WidgetSettingUtils.isLandscape(this.mContext))) ? 1 : 2;
            this.mViewModel = new DigitalClockViewModel(DigitalClockDataManager.loadModel(this, this.mAppWidgetId, widgetSize));
        }
        return this.mViewModel;
    }

    protected void onGlobalLayoutChanged() {
        setPreviewSize();
        this.mViewModel = null;
        drawPreview();
    }

    protected String getThemePreferenceKeyString() {
        return new ViewModelHelper(this, this.mAppWidgetId).getThemeKey(0);
    }

    protected String getTransparentPreferenceKeyString() {
        return new ViewModelHelper(this, this.mAppWidgetId).getTransparentKey(0);
    }

    protected String getIntentActionName() {
        return "com.sec.android.widgetapp.digitalclock.ACTION_DIGITAL_CLOCK_SETTING_CHANGED";
    }

    protected int getWidgetTypeFromId() {
        return 0;
    }
}
