package com.sec.android.app.clockpackage.ringtonepicker.viewmodel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.ringtonepicker.C0680R;

public class RingtoneVibrationBar extends LinearLayout {
    private Context mContext;
    private Switch mTimerVibBtn = null;
    VibrationSwitchListener mVibrationSwitchListener;

    public interface VibrationSwitchListener {
        void onChanged(boolean z);
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtoneVibrationBar$1 */
    class C07041 implements OnClickListener {
        C07041() {
        }

        public void onClick(View v) {
            if (RingtoneVibrationBar.this.mTimerVibBtn.isEnabled()) {
                RingtoneVibrationBar.this.mTimerVibBtn.performClick();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtoneVibrationBar$2 */
    class C07052 implements OnCheckedChangeListener {
        C07052() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.secD("RingtoneVibrationBar", "onCheckedChanged isChecked : " + isChecked);
            RingtoneVibrationBar.this.mVibrationSwitchListener.onChanged(isChecked);
        }
    }

    public RingtoneVibrationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.secD("RingtoneVibrationBar", "RingtoneVibrationBar");
        this.mContext = context;
    }

    public void setVibrationSwitchListener(VibrationSwitchListener listener) {
        this.mVibrationSwitchListener = listener;
    }

    public void init(boolean isVibOn) {
        Log.secD("RingtoneVibrationBar", "init : " + isVibOn);
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0680R.layout.ringtone_vibration_bar, this, true);
        View timerVibLayout = findViewById(C0680R.id.timer_vibration_layout);
        this.mTimerVibBtn = (Switch) findViewById(C0680R.id.timer_vibration_switch);
        ClockUtils.setAccessibilityFontSize(this.mContext, (TextView) findViewById(C0680R.id.timer_vibration_subject));
        timerVibLayout.setOnClickListener(new C07041());
        this.mTimerVibBtn.setOnCheckedChangeListener(new C07052());
        this.mTimerVibBtn.setChecked(isVibOn);
    }
}
