package com.sec.android.app.clockpackage.worldclock.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import java.util.Locale;

public class WeatherInfoView extends ConstraintLayout {
    private Context mContext;
    private ImageView mWeatherImage;
    private ProgressBar mWeatherProgress;
    private ImageView mWeatherReloadIcon;
    private TextView mWeatherTemperature;
    private TextView mWeatherUnit;

    public WeatherInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
    }

    public void initView(AttributeSet attrs) {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_weather_info_layout, this, true);
        this.mWeatherImage = (ImageView) findViewById(C0836R.id.worldclock_weather_image);
        this.mWeatherReloadIcon = (ImageView) findViewById(C0836R.id.worldclock_weather_reload);
        this.mWeatherProgress = (ProgressBar) findViewById(C0836R.id.worldclock_weather_progress);
        this.mWeatherTemperature = (TextView) findViewById(C0836R.id.worldclock_weather_temperature);
        this.mWeatherUnit = (TextView) findViewById(C0836R.id.worldclock_weather_unit);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, C0836R.styleable.WeatherInfoView);
        int imageSize = typedArray.getDimensionPixelOffset(C0836R.styleable.WeatherInfoView_image_size, 0);
        typedArray.recycle();
        this.mWeatherImage.getLayoutParams().width = imageSize;
        this.mWeatherImage.getLayoutParams().height = imageSize;
        this.mWeatherReloadIcon.getLayoutParams().width = imageSize;
        this.mWeatherReloadIcon.getLayoutParams().height = imageSize;
        this.mWeatherProgress.getLayoutParams().width = imageSize;
        this.mWeatherProgress.getLayoutParams().height = imageSize;
    }

    public void startWeatherProgress() {
        this.mWeatherProgress.setVisibility(0);
        this.mWeatherImage.setVisibility(8);
        this.mWeatherTemperature.setVisibility(8);
        this.mWeatherUnit.setVisibility(8);
        this.mWeatherReloadIcon.setVisibility(8);
    }

    public void setDisplayWeatherData(int temp, int iconNum, String weatherDescription, String dayOrNight) {
        boolean isDay = "D".equalsIgnoreCase(dayOrNight);
        this.mWeatherImage.setVisibility(0);
        this.mWeatherTemperature.setVisibility(0);
        this.mWeatherUnit.setVisibility(0);
        this.mWeatherProgress.setVisibility(8);
        this.mWeatherReloadIcon.setVisibility(8);
        WorldclockWeatherUtils.setWeatherImg(this.mWeatherImage, iconNum, weatherDescription, isDay);
        this.mWeatherImage.semSetHoverPopupType(1);
        if (StateUtils.isUseArabianNumberInRtl()) {
            String temperature;
            if (temp >= 0) {
                temperature = String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(temp)}) + this.mContext.getResources().getString(C0836R.string.weather_degree);
            } else {
                temperature = String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(-temp)}) + this.mContext.getResources().getString(C0836R.string.weather_degree) + "-";
            }
            this.mWeatherTemperature.setText(temperature);
            this.mWeatherUnit.setVisibility(8);
        } else {
            this.mWeatherTemperature.setText(String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(temp)}));
            this.mWeatherUnit.setVisibility(0);
        }
        ClockUtils.setLargeTextSize(this.mContext, this.mWeatherTemperature, (float) this.mWeatherTemperature.getContext().getResources().getDimensionPixelSize(C0836R.dimen.worldclock_weather_temperature_text_size));
        ClockUtils.setLargeTextSize(this.mContext, this.mWeatherUnit, (float) this.mWeatherUnit.getContext().getResources().getDimensionPixelSize(C0836R.dimen.worldclock_weather_temperature_text_size));
    }

    public void setVisibleWeatherReloadIcon() {
        this.mWeatherReloadIcon.setVisibility(0);
        this.mWeatherProgress.setVisibility(8);
        this.mWeatherImage.setVisibility(8);
        this.mWeatherTemperature.setVisibility(8);
        this.mWeatherUnit.setVisibility(8);
        this.mWeatherReloadIcon.semSetHoverPopupType(1);
    }
}
