package com.sec.android.app.clockpackage.ringtonepicker.viewmodel;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.ringtonepicker.C0680R;
import com.sec.android.app.clockpackage.ringtonepicker.util.RingtonePlayer;

public class HighlightDialogBuilder extends Builder implements Runnable {
    private Context mContext;
    private final Handler mHandler = new Handler();
    private BaseAdapter mHighlightAdapter;
    private HighlightButtonClickListener mHighlightButtonClickListener;
    private boolean mIsVolumePopup = false;
    private int mOffset = 0;
    private Uri mResultUri;
    private int mSelectedPosition = 0;
    private Uri mSelectedUri = null;
    private int mType;
    private int mVolume;

    public interface HighlightButtonClickListener {
        void onDismiss(int i);

        void onPositiveButtonClick(Uri uri);
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder$2 */
    class C06922 implements OnClickListener {
        C06922() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            HighlightDialogBuilder.this.mHighlightAdapter.notifyDataSetChanged();
            HighlightDialogBuilder.this.mSelectedPosition = whichButton;
            HighlightDialogBuilder.this.stopAnyPlayingRingtone(false);
            if (HighlightDialogBuilder.this.mSelectedPosition == 0) {
                if (HighlightDialogBuilder.this.mSelectedUri != null) {
                    HighlightDialogBuilder.this.mResultUri = HighlightDialogBuilder.this.mSelectedUri;
                }
            } else if (HighlightDialogBuilder.this.mOffset > 0) {
                HighlightDialogBuilder.this.mResultUri = HighlightDialogBuilder.this.mSelectedUri.buildUpon().appendQueryParameter("highlight_offset", String.valueOf(HighlightDialogBuilder.this.mOffset)).build();
                Log.secD("HighlightDialogBuilder", "mOffset mResultUri : " + HighlightDialogBuilder.this.mResultUri);
            }
            HighlightDialogBuilder.this.mIsVolumePopup = false;
            HighlightDialogBuilder.this.playRingtone();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder$3 */
    class C06933 implements OnKeyListener {
        C06933() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            int i = 15;
            if ((keyCode == 24 || keyCode == 25) && event.getAction() == 0) {
                HighlightDialogBuilder.this.mIsVolumePopup = true;
                if (keyCode == 24) {
                    HighlightDialogBuilder highlightDialogBuilder = HighlightDialogBuilder.this;
                    if (HighlightDialogBuilder.access$704(HighlightDialogBuilder.this) <= 15) {
                        i = HighlightDialogBuilder.this.mVolume;
                    }
                    highlightDialogBuilder.mVolume = i;
                } else {
                    HighlightDialogBuilder.this.mVolume = HighlightDialogBuilder.access$706(HighlightDialogBuilder.this) < 1 ? 1 : HighlightDialogBuilder.this.mVolume;
                }
                Log.secD("HighlightDialogBuilder", "volume key pressed : " + HighlightDialogBuilder.this.mVolume);
                HighlightDialogBuilder.this.playRingtone();
            }
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder$4 */
    class C06944 implements OnDismissListener {
        C06944() {
        }

        public void onDismiss(DialogInterface dialog) {
            Log.secD("HighlightDialogBuilder", "onDismiss " + HighlightDialogBuilder.this.mVolume);
            if (HighlightDialogBuilder.this.mHighlightButtonClickListener != null) {
                HighlightDialogBuilder.this.mHighlightButtonClickListener.onDismiss(HighlightDialogBuilder.this.mVolume);
            }
            HighlightDialogBuilder.this.removeInstance();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder$5 */
    class C06955 implements OnClickListener {
        C06955() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            Log.secD("HighlightDialogBuilder", "HighlightDialogBuilder - okay");
            if (HighlightDialogBuilder.this.mHighlightButtonClickListener != null) {
                HighlightDialogBuilder.this.mHighlightButtonClickListener.onPositiveButtonClick(HighlightDialogBuilder.this.mResultUri);
            }
            dialog.cancel();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.HighlightDialogBuilder$6 */
    class C06966 implements OnClickListener {
        C06966() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Log.secD("HighlightDialogBuilder", "HighlightDialogBuilder - CANCEL");
            dialog.cancel();
        }
    }

    static /* synthetic */ int access$704(HighlightDialogBuilder x0) {
        int i = x0.mVolume + 1;
        x0.mVolume = i;
        return i;
    }

    static /* synthetic */ int access$706(HighlightDialogBuilder x0) {
        int i = x0.mVolume - 1;
        x0.mVolume = i;
        return i;
    }

    public void setOnHighlightButtonClickListener(HighlightButtonClickListener listener) {
        this.mHighlightButtonClickListener = listener;
    }

    public HighlightDialogBuilder(Context context, Uri selectedUri, int offset, int type, int volume) {
        super(context, C0680R.style.MyCustomThemeForDialog);
        this.mContext = context;
        this.mSelectedUri = selectedUri;
        this.mResultUri = this.mSelectedUri;
        this.mOffset = offset;
        this.mType = type;
        this.mVolume = volume;
        initView();
        buildDialog();
    }

    private void initView() {
        final String[] recommendRingtoneArray = this.mContext.getResources().getStringArray(C0680R.array.recommendation_ringtone);
        final String[] recommendRingtoneArraySub = new String[]{"", this.mContext.getResources().getString(C0680R.string.auto_recommendation_help)};
        final LayoutInflater dialogInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        this.mHighlightAdapter = new BaseAdapter() {
            public int getCount() {
                return 2;
            }

            public Object getItem(int arg0) {
                return recommendRingtoneArray[arg0];
            }

            public long getItemId(int arg0) {
                return (long) arg0;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                boolean z;
                View view = convertView != null ? convertView : dialogInflater.inflate(C0680R.layout.recommendation_ringtone, parent, false);
                RadioButton radioButton = (RadioButton) view.findViewById(C0680R.id.radio_button);
                ((TextView) view.findViewById(C0680R.id.text_primary)).setText(recommendRingtoneArray[position]);
                TextView textSecondary = (TextView) view.findViewById(C0680R.id.text_secondary);
                if (TextUtils.isEmpty(recommendRingtoneArraySub[position])) {
                    textSecondary.setVisibility(8);
                } else {
                    textSecondary.setVisibility(0);
                    textSecondary.setText(recommendRingtoneArraySub[position]);
                }
                if (position == HighlightDialogBuilder.this.mSelectedPosition) {
                    z = true;
                } else {
                    z = false;
                }
                radioButton.setChecked(z);
                return view;
            }
        };
    }

    private void buildDialog() {
        setIcon(null).setTitle(this.mType == 0 ? C0680R.string.alarm_sound : C0680R.string.timer_set_timer_sound);
        setSingleChoiceItems(this.mHighlightAdapter, this.mSelectedPosition, new C06922());
        setOnKeyListener(new C06933());
        setOnDismissListener(new C06944());
        setPositiveButton(C0680R.string.okay, new C06955());
        setNegativeButton(C0680R.string.cancel, new C06966());
    }

    public void removeInstance() {
        stopAnyPlayingRingtone(true);
        this.mHighlightAdapter = null;
    }

    private void playRingtone() {
        Log.secD("HighlightDialogBuilder", "playRingtone");
        if (!StateUtils.isInCallState(this.mContext) && !isRingtonePlaying() && RingtonePlayer.requestAudioFocus(this.mContext)) {
            this.mHandler.removeCallbacks(this);
            this.mHandler.postDelayed(this, RingtonePlayer.isActiveStreamAlarm() ? 300 : 0);
        }
    }

    public void stopAnyPlayingRingtone(boolean abandonAudioFocus) {
        Log.secD("HighlightDialogBuilder", "stopAnyPlayingRingtone");
        this.mHandler.removeCallbacks(this);
        if (abandonAudioFocus) {
            RingtonePlayer.stopMediaPlayer();
        } else {
            RingtonePlayer.stopMediaPlayerExceptAbandon();
        }
    }

    private boolean isRingtonePlaying() {
        return RingtonePlayer.getMediaPlayer().isPlaying();
    }

    public void run() {
        RingtonePlayer.setStreamVolume(this.mContext, this.mVolume, this.mIsVolumePopup);
        RingtonePlayer.playMediaPlayer(this.mContext, this.mResultUri);
    }
}
