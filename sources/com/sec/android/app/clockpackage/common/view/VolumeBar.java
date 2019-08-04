package com.sec.android.app.clockpackage.common.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.sec.android.app.clockpackage.common.C0645R;
import com.sec.android.app.clockpackage.common.callback.VolumeProgressListener;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;

public class VolumeBar extends LinearLayout implements OnSeekBarChangeListener {
    private Context mContext;
    private MyBroadcastReceiver mReceiver;
    private SeekBar mSeekBar;
    private ImageView mVolumeIcon;
    private VolumeIconPressListener mVolumeIconPressListener;
    private VolumeProgressListener mVolumeProgressListener;

    public interface VolumeIconPressListener {
        void onStopPlay();
    }

    /* renamed from: com.sec.android.app.clockpackage.common.view.VolumeBar$1 */
    class C06521 implements OnClickListener {
        C06521() {
        }

        public void onClick(View arg0) {
            VolumeBar.this.changeVolumeIcon();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.common.view.VolumeBar$2 */
    class C06532 implements OnFocusChangeListener {
        C06532() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                VolumeBar.this.mVolumeIconPressListener.onStopPlay();
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
            r6 = this;
            r4 = -1;
            if (r8 == 0) goto L_0x0009;
        L_0x0003:
            r3 = r8.getAction();
            if (r3 != 0) goto L_0x000a;
        L_0x0009:
            return;
        L_0x000a:
            r3 = r8.getAction();
            r5 = r3.hashCode();
            switch(r5) {
                case -1940635523: goto L_0x0034;
                default: goto L_0x0015;
            };
        L_0x0015:
            r3 = r4;
        L_0x0016:
            switch(r3) {
                case 0: goto L_0x001a;
                default: goto L_0x0019;
            };
        L_0x0019:
            goto L_0x0009;
        L_0x001a:
            r3 = "android.media.EXTRA_VOLUME_STREAM_TYPE";
            r1 = r8.getIntExtra(r3, r4);
            r3 = 4;
            if (r1 != r3) goto L_0x0009;
        L_0x0023:
            r3 = "android.media.EXTRA_VOLUME_SHOW_UI";
            r4 = 1;
            r0 = r8.getBooleanExtra(r3, r4);
            if (r0 != 0) goto L_0x003e;
        L_0x002c:
            r3 = "VolumeBar";
            r4 = "volume panel is not visible. it doesn't need to setStreamVolume";
            com.sec.android.app.clockpackage.common.util.Log.secD(r3, r4);
            goto L_0x0009;
        L_0x0034:
            r5 = "android.media.VOLUME_CHANGED_ACTION";
            r3 = r3.equals(r5);
            if (r3 == 0) goto L_0x0015;
        L_0x003c:
            r3 = 0;
            goto L_0x0016;
        L_0x003e:
            r3 = "android.media.EXTRA_VOLUME_STREAM_VALUE";
            r4 = 11;
            r2 = r8.getIntExtra(r3, r4);
            r3 = com.sec.android.app.clockpackage.common.view.VolumeBar.this;
            r3 = r3.mSeekBar;
            r3 = r3.isEnabled();
            if (r3 == 0) goto L_0x0009;
        L_0x0052:
            r3 = com.sec.android.app.clockpackage.common.view.VolumeBar.this;
            r3 = r3.mSeekBar;
            r3 = r3.getProgress();
            if (r3 == r2) goto L_0x0009;
        L_0x005e:
            r3 = com.sec.android.app.clockpackage.common.view.VolumeBar.this;
            r3 = r3.mContext;
            r3 = com.sec.android.app.clockpackage.common.util.StateUtils.isTurnOffAllSoundMode(r3);
            if (r3 != 0) goto L_0x0009;
        L_0x006a:
            r3 = com.sec.android.app.clockpackage.common.view.VolumeBar.this;
            r3.setAlarmVolume(r2);
            goto L_0x0009;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.clockpackage.common.view.VolumeBar.MyBroadcastReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    public void setOnVolumeBarListener(VolumeProgressListener listener) {
        this.mVolumeProgressListener = listener;
    }

    public VolumeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.secD("VolumeBar", "VolumeBar");
        this.mContext = context;
    }

    public void setAlarmVolume(int volume) {
        SeekBar seekBar = this.mSeekBar;
        if (volume < 1) {
            volume = 1;
        }
        seekBar.setProgress(volume);
    }

    public SeekBar getSeekBar() {
        return this.mSeekBar;
    }

    public void setVolumeIconPressListener(VolumeIconPressListener listener) {
        this.mVolumeIconPressListener = listener;
    }

    public void onVolumeKeyPressed(boolean isVolumeUp) {
        int volume = this.mSeekBar.getProgress() + (isVolumeUp ? 1 : -1);
        if (volume > 15) {
            volume = 15;
        } else if (volume < 1) {
            volume = 1;
        }
        this.mSeekBar.setProgress(volume);
    }

    public void init(int volume) {
        Log.secD("VolumeBar", "init " + volume);
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0645R.layout.volume_bar, this, true);
        this.mSeekBar = (SeekBar) findViewById(C0645R.id.seekbar);
        this.mVolumeIcon = (ImageView) findViewById(C0645R.id.volume_icon);
        this.mVolumeIcon.setOnClickListener(new C06521());
        if (StateUtils.isRtl()) {
            this.mVolumeIcon.setRotation(180.0f);
        }
        this.mSeekBar.semSetMin(1);
        this.mSeekBar.setMax(15);
        setAlarmVolume(volume);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mSeekBar.setOnFocusChangeListener(new C06532());
    }

    public void registerVolumeReceiver() {
        Log.secD("VolumeBar", "registerVolumeReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        if (this.mReceiver == null) {
            this.mReceiver = new MyBroadcastReceiver();
        }
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    public void unregisterVolumeReceiver() {
        try {
            this.mContext.unregisterReceiver(this.mReceiver);
        } catch (IllegalArgumentException e) {
            Log.secE("VolumeBar", "unregisterReceiver IllegalArgumentException");
        }
    }

    public void changeVolumeIcon() {
        if (this.mSeekBar.getProgress() > 1) {
            this.mSeekBar.setProgress(1);
        }
    }

    public void setEnableVolumeOption(boolean isEnable) {
        this.mSeekBar.setEnabled(isEnable);
        this.mVolumeIcon.setEnabled(isEnable);
        this.mVolumeIcon.setAlpha(isEnable ? 1.0f : 0.4f);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.secD("VolumeBar", "onProgressChanged " + progress);
        if (seekBar.getProgress() < 1) {
            seekBar.setProgress(1);
        } else {
            this.mVolumeProgressListener.onProgressChanged(progress);
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.mVolumeProgressListener.onStartTrackingTouch();
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        this.mVolumeProgressListener.onStopTrackingTouch();
    }

    public void removeInstance() {
        if (this.mVolumeIcon != null) {
            this.mVolumeIcon.destroyDrawingCache();
            this.mVolumeIcon.setImageDrawable(null);
            this.mVolumeIcon.setOnClickListener(null);
            this.mVolumeIcon = null;
        }
        if (this.mSeekBar != null) {
            this.mSeekBar.setOnSeekBarChangeListener(null);
            this.mSeekBar.setOnFocusChangeListener(null);
            this.mSeekBar = null;
        }
        if (this.mReceiver != null) {
            unregisterVolumeReceiver();
            this.mReceiver = null;
        }
        this.mContext = null;
    }
}
