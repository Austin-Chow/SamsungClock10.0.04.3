package com.sec.android.widgetapp.dualclockdigital;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.R;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.commonwidget.ViewModelHelper;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingActivity;
import com.sec.android.app.clockpackage.commonwidget.WidgetSettingUtils;
import com.sec.android.app.clockpackage.worldclock.model.CityManager;
import com.sec.android.widgetapp.dualclockdigital.DualClockDigitalContract.ViewModel;

public class DualClockDigitalSettingActivity extends WidgetSettingActivity {
    TextView mFirstCityButton;
    TextView mFirstCityName;
    private final BroadcastReceiver mIntentReceiver = new C09183();
    private ViewGroup mPreviewFrame;
    TextView mSecondCityButton;
    TextView mSecondCityName;
    private boolean mSupportedWidget = true;
    private ViewModel mViewModel;

    /* renamed from: com.sec.android.widgetapp.dualclockdigital.DualClockDigitalSettingActivity$1 */
    class C09161 implements OnClickListener {
        C09161() {
        }

        public void onClick(View v) {
            DualClockUtils.changeCity(DualClockDigitalSettingActivity.this.mContext, 1, DualClockDigitalSettingActivity.this.mAppWidgetId, DualClockDigitalViewModel.sFirstUniqueId);
        }
    }

    /* renamed from: com.sec.android.widgetapp.dualclockdigital.DualClockDigitalSettingActivity$2 */
    class C09172 implements OnClickListener {
        C09172() {
        }

        public void onClick(View v) {
            DualClockUtils.changeCity(DualClockDigitalSettingActivity.this.mContext, 2, DualClockDigitalSettingActivity.this.mAppWidgetId, DualClockDigitalViewModel.sSecondUniqueId == -1 ? DualClockDigitalViewModel.sFirstUniqueId : DualClockDigitalViewModel.sSecondUniqueId);
        }
    }

    /* renamed from: com.sec.android.widgetapp.dualclockdigital.DualClockDigitalSettingActivity$3 */
    class C09183 extends BroadcastReceiver {
        C09183() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.secD("DualClockDigitalSettingActivity", "Intent action : " + action);
            if ("com.sec.android.app.clockpackage.dualclockdigital.ADD_CITY".equals(action)) {
                DualClockUtils.saveDBCityCountry(context, intent.getIntExtra("homezone", 0), intent.getIntExtra("wid", 0), intent.getIntExtra("uniqueid", 0));
                DualClockDigitalSettingActivity.this.drawPreview();
                DualClockDigitalSettingActivity.this.setCityInfo();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.secD("DualClockDigitalSettingActivity", "onCreate");
        super.onCreate(savedInstanceState);
        initReceiver();
        this.mAppWidgetId = getIntent().getIntExtra("appWidgetId", 0);
        startActivityByLaunchWidget();
    }

    private void startActivityByLaunchWidget() {
        if (DualClockDigitalViewModel.sSecondUniqueId == -1) {
            String cityName = CityManager.findCityCountryNameByUniqueId(Integer.valueOf(-1));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setComponent(new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockGlobeMain"));
            intent.setFlags(2097152);
            intent.putExtra("where", "menu_dualclock_launch");
            intent.putExtra("homezone", 2);
            intent.putExtra("cityname", cityName);
            intent.putExtra("uniqueid", -1);
            intent.putExtra("wid", this.mAppWidgetId);
            try {
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.worldclock_animation_fade_in, R.anim.worldclock_animation_fade_out);
            } catch (ActivityNotFoundException e) {
                try {
                    ComponentName cn = new ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.worldclock.viewmodel.WorldclockCityListActivity");
                    try {
                        intent.setComponent(cn);
                        startActivityForResult(intent, 0);
                    } catch (ActivityNotFoundException e2) {
                        ComponentName componentName = cn;
                        Toast.makeText(this.mContext, String.format(this.mContext.getResources().getString(R.string.need_samsung_apps), new Object[]{this.mContext.getResources().getString(R.string.app_name)}), 0).show();
                    }
                } catch (ActivityNotFoundException e3) {
                    Toast.makeText(this.mContext, String.format(this.mContext.getResources().getString(R.string.need_samsung_apps), new Object[]{this.mContext.getResources().getString(R.string.app_name)}), 0).show();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0) {
            return;
        }
        if (resultCode == -1) {
            DualClockUtils.saveDBCityCountry(this.mContext, data.getIntExtra("homezone", 0), data.getIntExtra("wid", 0), data.getIntExtra("uniqueid", 0));
            return;
        }
        finish();
    }

    protected void initLayout() {
        Log.secD("DualClockDigitalSettingActivity", "initLayout");
        super.initLayout();
        drawPreview();
        if (this.mSupportedWidget) {
            this.mFirstCityName = (TextView) findViewById(R.id.dual_clock_first_city_name);
            this.mSecondCityName = (TextView) findViewById(R.id.dual_clock_second_city_name);
            this.mFirstCityButton = (TextView) findViewById(R.id.first_city_button_change);
            this.mSecondCityButton = (TextView) findViewById(R.id.second_city_button_change);
            setCityInfo();
            this.mFirstCityButton.setOnClickListener(new C09161());
            this.mSecondCityButton.setOnClickListener(new C09172());
            return;
        }
        findViewById(R.id.widget_bottom_layout).setVisibility(8);
    }

    protected void updatePreview() {
        Log.secD("DualClockDigitalSettingActivity", "updatePreview");
        if (this.mPreviewFrame != null) {
            boolean isDarkFont = WidgetSettingUtils.isDarkFont(this, this.mTheme, this.mTransparency);
            int theme = ContextCompat.getColor(this, this.mTheme == 1 ? R.color.widget_dark_bg_color : R.color.widget_light_bg_color);
            int textColor = ContextCompat.getColor(this, isDarkFont ? R.color.widget_text_color_theme_light : R.color.widget_text_color_theme_dark);
            ImageView widgetBackground = (ImageView) this.mPreviewFrame.findViewById(R.id.dual_clock_widget_background);
            ImageView dualClockMiddleLine = (ImageView) this.mPreviewFrame.findViewById(R.id.middle_line);
            TextView firstCityText = (TextView) this.mPreviewFrame.findViewById(R.id.first_city_text);
            TextClock firstClockText = (TextClock) this.mPreviewFrame.findViewById(R.id.first_clock_text);
            TextClock firstAmPmLeft = (TextClock) this.mPreviewFrame.findViewById(R.id.first_ampm_left);
            TextClock firstAmPm = (TextClock) this.mPreviewFrame.findViewById(R.id.first_ampm);
            TextClock dateTextFirst = (TextClock) this.mPreviewFrame.findViewById(R.id.date_text_first);
            TextView secondSetCityText = (TextView) this.mPreviewFrame.findViewById(R.id.second_set_city_text);
            TextView secondCityText = (TextView) this.mPreviewFrame.findViewById(R.id.second_city_text);
            TextClock secondClockText = (TextClock) this.mPreviewFrame.findViewById(R.id.second_clock_text);
            TextClock secondAmPmLeft = (TextClock) this.mPreviewFrame.findViewById(R.id.second_ampm_left);
            TextClock secondAmPm = (TextClock) this.mPreviewFrame.findViewById(R.id.second_ampm);
            TextClock dateTextSecond = (TextClock) this.mPreviewFrame.findViewById(R.id.date_text_second);
            if (widgetBackground != null) {
                widgetBackground.setColorFilter(theme);
                widgetBackground.setImageAlpha(255 - this.mTransparency);
            }
            if (dualClockMiddleLine != null) {
                widgetBackground.setColorFilter(theme);
            }
            if (firstCityText != null) {
                firstCityText.setTextColor(textColor);
            }
            if (firstClockText != null) {
                firstClockText.setTextColor(textColor);
            }
            if (firstAmPmLeft != null) {
                firstAmPmLeft.setTextColor(textColor);
            }
            if (firstAmPm != null) {
                firstAmPm.setTextColor(textColor);
            }
            if (dateTextFirst != null) {
                dateTextFirst.setTextColor(textColor);
            }
            if (secondSetCityText != null) {
                secondSetCityText.setTextColor(textColor);
            }
            if (secondCityText != null) {
                secondCityText.setTextColor(textColor);
            }
            if (secondClockText != null) {
                secondClockText.setTextColor(textColor);
            }
            if (secondAmPmLeft != null) {
                secondAmPmLeft.setTextColor(textColor);
            }
            if (secondAmPm != null) {
                secondAmPm.setTextColor(textColor);
            }
            if (dateTextSecond != null) {
                dateTextSecond.setTextColor(textColor);
            }
        }
    }

    protected void drawPreview() {
        ViewModel viewModel = getViewModel();
        viewModel.setTransparency(this, this.mTheme, this.mTransparency);
        Log.secD("DualClockDigitalSettingActivity", "drawPreview : mAppWidgetId = " + this.mAppWidgetId);
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
            DualClockDigitalWidgetModel model = DualClockDigitalDataManager.loadModel(this, this.mAppWidgetId);
            this.mViewModel = new DualClockDigitalViewModel(model);
            this.mSupportedWidget = model.getSupportedWidget();
        }
        return this.mViewModel;
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.sec.android.app.clockpackage.dualclockdigital.ADD_CITY");
        this.mContext.registerReceiver(this.mIntentReceiver, filter);
    }

    protected void onGlobalLayoutChanged() {
        setPreviewSize();
        this.mViewModel = null;
        drawPreview();
    }

    private void setCityInfo() {
        this.mFirstCityName.setText(DualClockDigitalViewModel.sFirstCityName);
        if (DualClockDigitalViewModel.sSecondUniqueId != -1) {
            this.mSecondCityName.setText(DualClockDigitalViewModel.sSecondCityName);
            this.mSecondCityButton.setText(getString(R.string.worldclock_change_city));
            return;
        }
        this.mSecondCityName.setText(getString(R.string.No_cities));
        this.mSecondCityButton.setText(getString(R.string.add));
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.secD("DualClockDigitalSettingActivity", "onDestroy()");
        this.mContext.unregisterReceiver(this.mIntentReceiver);
    }

    protected String getThemePreferenceKeyString() {
        return new ViewModelHelper(this, this.mAppWidgetId).getThemeKey(1);
    }

    protected String getTransparentPreferenceKeyString() {
        return new ViewModelHelper(this, this.mAppWidgetId).getTransparentKey(1);
    }

    protected String getIntentActionName() {
        return "com.sec.android.widgetapp.dualclockdigital.ACTION_DUAL_CLOCK_DIGITAL_SETTING_CHANGED";
    }

    protected int getWidgetTypeFromId() {
        return 1;
    }
}
