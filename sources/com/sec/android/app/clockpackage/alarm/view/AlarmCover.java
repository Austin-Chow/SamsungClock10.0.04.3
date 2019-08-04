package com.sec.android.app.clockpackage.alarm.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.model.AlarmItem;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmService;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmWeatherUtil;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.commonalert.view.Cover;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlarmCover extends Cover {
    private AlarmItem mAlarmItem = new AlarmItem();
    private TextView mAlarmName;
    private AlarmCoverDialog mDialog;
    private String mName;
    private View mSnoozeButton;

    private class AlarmCoverDialog extends CoverDialog {

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCover$AlarmCoverDialog$1 */
        class C05511 implements OnClickListener {
            C05511() {
            }

            public void onClick(View v) {
                if (AlarmCover.this.mAlarmItem != null) {
                    Log.secD("AlarmCover", "mSnoozeButton onClick");
                    Intent mAlarmAlert = new Intent();
                    mAlarmAlert.setAction("AlarmSnooze");
                    AlarmCover.this.mContext.sendBroadcast(mAlarmAlert);
                }
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCover$AlarmCoverDialog$2 */
        class C05522 implements OnClickListener {
            C05522() {
            }

            public void onClick(View v) {
                Log.secD("AlarmCover", "weatherIcon onClick");
                AlarmCover.this.finishAlert(false);
                AlarmService.startCpLink(AlarmCover.this.mContext);
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCover$AlarmCoverDialog$3 */
        class C05533 implements OnClickListener {
            C05533() {
            }

            public void onClick(View v) {
                Log.secD("AlarmCover", "poweredByTextAndWeatherCpLogo onClick");
                AlarmCover.this.finishAlert(false);
                AlarmService.startCpLink(AlarmCover.this.mContext);
            }
        }

        public AlarmCoverDialog(Context context, int coverSize) {
            super(context, coverSize);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            if (coverSize == 0) {
                inflater.inflate(C0490R.layout.alarm_cover_view, (ViewGroup) AlarmCover.this.mCoverView);
            } else {
                inflater.inflate(C0490R.layout.alarm_cover_clear_view, (ViewGroup) AlarmCover.this.mCoverView);
            }
        }

        public void initViews() {
            AlarmCover.this.mAlarmName = (TextView) findViewById(C0490R.id.alarm_name);
            AlarmCover.this.mSnoozeButton = findViewById(C0490R.id.alarm_btn_snooze);
            Typeface tf = Typeface.create("sans-serif-light", 0);
            if (tf != null) {
                ((TextClock) findViewById(C0490R.id.alarm_alert_current_time_cover)).setTypeface(tf);
            }
            AlarmCover.this.mSnoozeButton.setOnClickListener(new C05511());
            TextView snoozeView = (TextView) findViewById(C0490R.id.alarm_textview_snooze);
            if (snoozeView != null) {
                ClockUtils.setLargeTextSize(AlarmCover.this.mContext, snoozeView, (float) AlarmCover.this.mContext.getResources().getDimensionPixelSize(C0490R.dimen.alarm_snooze_text_size));
            }
            TextClock currentDate = (TextClock) findViewById(C0490R.id.current_date);
            if (currentDate != null) {
                currentDate.setContentDescription(LocalDateTime.now().format(DateTimeFormatter.ofPattern(AlarmCover.this.mContext.getResources().getString(C0490R.string.alarm_alert_date_format_for_tts))));
            }
            if (AlarmCover.this.mCoverViewSize != 2) {
                TextClock currentTimeAmPmCover;
                boolean is24HMode = DateFormat.is24HourFormat(AlarmCover.this.mContext);
                if (StateUtils.isLeftAmPm()) {
                    currentTimeAmPmCover = (TextClock) findViewById(C0490R.id.alarm_alert_current_time_ampm_kor_cover);
                } else {
                    currentTimeAmPmCover = (TextClock) findViewById(C0490R.id.alarm_alert_current_time_ampm_cover);
                }
                if (currentTimeAmPmCover != null) {
                    if (is24HMode) {
                        currentTimeAmPmCover.setVisibility(8);
                    } else {
                        currentTimeAmPmCover.setVisibility(0);
                    }
                }
            }
            if (!(AlarmCover.this.mAlarmName == null || AlarmCover.this.mName == null || AlarmCover.this.mName.length() <= 0)) {
                String StrAlarmName = AlarmCover.this.mName;
                AlarmCover.this.mAlarmName.setSingleLine(true);
                AlarmCover.this.mAlarmName.setText(StrAlarmName);
            }
            if (AlarmCover.this.mAlarmItem != null && AlarmCover.this.mAlarmItem.isDefaultStop()) {
                AlarmCover.this.mSnoozeButton.setVisibility(4);
            }
            if (AlarmCover.this.mCoverViewSize == 2) {
                setBixbyBriefingWeatherInfo();
            }
            AlarmCover.this.mCoverView.setVisibility(0);
        }

        protected int ccTabSelectorId() {
            return C0490R.id.alarm_cc_tab_selector;
        }

        public void setBixbyBriefingWeatherInfo() {
            Log.secD("AlarmCover", "setBixbyBriefingWeatherInfo");
            if ((AlarmCover.this.mAlarmItem != null && !AlarmCover.this.mAlarmItem.isPossibleBixbyBriefingAlarm()) || AlarmService.sBixbyBriefWeatherConditionCode == 999) {
                Log.secD("AlarmCover", "setBixbyBriefingWeatherInfo return 1");
            } else if (AlarmCover.this.mContext == null) {
                Log.secD("AlarmCover", "setBixbyBriefingWeatherInfo return 2");
            } else {
                int weatherIconNumber = AlarmWeatherUtil.getWeatherIconNumber(AlarmService.sBixbyBriefWeatherConditionCode, AlarmService.sBixbyBriefDaytime);
                if (weatherIconNumber == 115) {
                    Log.secD("AlarmCover", "setBixbyBriefingWeatherInfo return 3");
                    return;
                }
                ImageView weatherIcon = (ImageView) AlarmCover.this.mCoverView.findViewById(C0490R.id.alarm_weather_icon);
                if (weatherIcon != null) {
                    weatherIcon.setVisibility(0);
                    AlarmWeatherUtil.setWeatherImg(AlarmCover.this.mContext, weatherIcon, weatherIconNumber, AlarmService.sBixbyBriefDaytime);
                    weatherIcon.setOnClickListener(new C05522());
                }
                RelativeLayout weatherCpLogo = (RelativeLayout) findViewById(C0490R.id.alarm_weather_cp_logo);
                if (weatherCpLogo != null) {
                    weatherCpLogo.setVisibility(0);
                }
                View poweredByTextAndWeatherCpLogo = findViewById(C0490R.id.alarm_poweredby_text_and_weather_cp_logo);
                if (poweredByTextAndWeatherCpLogo != null) {
                    poweredByTextAndWeatherCpLogo.setVisibility(0);
                    poweredByTextAndWeatherCpLogo.setOnClickListener(new C05533());
                }
                ImageView weatherCpImage = (ImageView) findViewById(C0490R.id.alarm_weather_cp_image);
                if (weatherCpImage != null) {
                    AlarmWeatherUtil.setCpLogo(AlarmCover.this.mContext, weatherCpImage);
                }
            }
        }
    }

    public AlarmCover(Context context, int coverSize, ScoverState state, int type) {
        super(context, coverSize, state, type);
        this.mDialog = new AlarmCoverDialog(context, coverSize);
    }

    public AlarmCoverDialog coverDialog() {
        return this.mDialog;
    }

    protected int coverViewId(int coverSize) {
        if (coverSize == 0) {
            return C0490R.id.alarm_cover_view;
        }
        return C0490R.id.alarm_cover_clear_view;
    }

    protected View getButton() {
        return this.mSnoozeButton;
    }

    protected void finishAlert(boolean forceFinish) {
        String action;
        if ((this.mAlarmItem == null || !this.mAlarmItem.isDefaultStop()) && !forceFinish) {
            action = "AlarmSnooze";
        } else {
            action = "AlarmStopAlert";
        }
        Intent mAlarmAlert = new Intent();
        mAlarmAlert.setAction(action);
        this.mContext.sendBroadcast(mAlarmAlert);
    }

    protected boolean isOptionalButtionVisible() {
        return (this.mAlarmItem == null || this.mAlarmItem.isDefaultStop()) ? false : true;
    }

    public void setAlarmValues(AlarmItem mItem) {
        this.mAlarmItem = mItem;
        if (this.mAlarmItem != null) {
            this.mName = this.mAlarmItem.mAlarmName;
        }
    }

    public void updateWeatherIcon() {
        Log.secD("AlarmCover", "updateWeatherIcon");
        this.mDialog.setBixbyBriefingWeatherInfo();
    }
}
