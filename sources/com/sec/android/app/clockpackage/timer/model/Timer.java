package com.sec.android.app.clockpackage.timer.model;

import com.sec.android.app.clockpackage.common.feature.Feature;

public class Timer {
    public static final int PRESET_MAX_COUNT = (Feature.isSupportChinaPresetTimer() ? 20 : 8);
}
