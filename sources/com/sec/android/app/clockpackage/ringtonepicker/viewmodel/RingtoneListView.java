package com.sec.android.app.clockpackage.ringtonepicker.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFocusRequest.Builder;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.provider.FontsContractCompat.FontRequestCallback;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.ringtonepicker.C0680R;
import com.sec.android.app.clockpackage.ringtonepicker.callback.RingtoneListViewListener;

public class RingtoneListView extends LinearLayout implements OnFocusChangeListener, OnItemClickListener, OnItemSelectedListener, Runnable {
    private static final Handler mHandler = new Handler();
    private final OnAudioFocusChangeListener mAudioFocusListener = new C06982();
    private AudioFocusRequest mAudioFocusRequest = null;
    private AudioManager mAudioManager;
    private Toast mCallToast = null;
    public int mClickedPos = -1;
    private ContentObserver mContentObserver;
    private Context mContext = null;
    private Cursor mCursor;
    private int mCursorCount;
    private DrmCheckManager mDrmCheckManager;
    private Uri mExistingUri;
    private int mFocusedRingtone = -1;
    public boolean mIsSetHighlight = false;
    private RingtoneListAdapter mListAdapter;
    private ListView mListView;
    private int mOffset = 0;
    private Ringtone mRingtone;
    private RingtoneListClickListener mRingtoneListClickListener;
    private RingtoneListViewListener mRingtoneListViewListener;
    private RingtoneManager mRingtoneManager;
    private int mRingtoneVolume;
    private int mSampleRingtonePos = -1;
    private int mSetHighlightRingtonePos = -1;
    private int mType;

    /* renamed from: com.sec.android.app.clockpackage.ringtonepicker.viewmodel.RingtoneListView$2 */
    class C06982 implements OnAudioFocusChangeListener {
        C06982() {
        }

        public void onAudioFocusChange(int focusChange) {
            Log.secD("RingtoneListView", "onAudioFocusChange - focusChange : " + focusChange);
            switch (focusChange) {
                case FontRequestCallback.FAIL_REASON_FONT_LOAD_ERROR /*-3*/:
                case -2:
                case -1:
                    RingtoneListView.this.stopAnyPlayingRingtone();
                    return;
                default:
                    return;
            }
        }
    }

    public interface RingtoneListClickListener {
        void onClickSilentItem(boolean z);
    }

    public void setOnRingtoneListClickListener(RingtoneListClickListener listener) {
        this.mRingtoneListClickListener = listener;
    }

    public RingtoneListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.secD("RingtoneListView", "RingtoneListView");
    }

    public void setContext(Context context) {
        this.mContext = context;
        init();
    }

    public void setListener(RingtoneListViewListener listener) {
        this.mRingtoneListViewListener = listener;
    }

    private void init() {
        Log.secD("RingtoneListView", "init()");
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0680R.layout.ringtone_list_view, this, true);
        this.mDrmCheckManager = new DrmCheckManager(this.mContext);
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAudioFocusRequest = new Builder(2).setAudioAttributes(new AudioAttributes.Builder().setContentType(4).setLegacyStreamType(4).build()).setOnAudioFocusChangeListener(this.mAudioFocusListener).build();
        this.mListView = (ListView) findViewById(C0680R.id.list);
        this.mListView.setOnItemSelectedListener(this);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setOnFocusChangeListener(this);
        this.mListView.setNestedScrollingEnabled(true);
        if (!(StateUtils.isContextInDexMode(this.mContext) || StateUtils.isCustomTheme(this.mContext))) {
            this.mListView.setBackground(this.mContext.getDrawable(C0680R.drawable.contents_area_background));
        }
        try {
            this.mListView.semSetRoundedCorners(15);
            this.mListView.semSetRoundedCornerColor(15, this.mContext.getColor(C0680R.color.window_background_color));
        } catch (NoSuchMethodError e) {
            Log.secE("RingtoneListView", "NoSuchMethodError : " + e);
        }
        this.mContentObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if ("external".equals(uri.getLastPathSegment()) && RingtoneListView.this.mCursorCount != RingtoneListView.this.mRingtoneManager.getCursor().getCount()) {
                    Log.secD("RingtoneListView", "deleted device's audio file.");
                    RingtoneListView.this.updateRingtoneList();
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Media.EXTERNAL_CONTENT_URI, true, this.mContentObserver);
        setDivider();
    }

    private void setDivider() {
        InsetDrawable listDivider;
        int dividerInset = getResources().getDimensionPixelSize(C0680R.dimen.checkbox_inset_for_divider);
        Drawable divider = this.mListView.getDivider();
        if (StateUtils.isRtl()) {
            listDivider = new InsetDrawable(divider, 0, 0, dividerInset, 0);
        } else {
            InsetDrawable insetDrawable = new InsetDrawable(divider, dividerInset, 0, 0, 0);
        }
        this.mListView.setDivider(listDivider);
    }

    private int getRingtoneType(int type) {
        if (type == 0) {
            return 4;
        }
        return 1;
    }

    public void setRingtoneListOption(int type, Uri uri) {
        Log.secD("RingtoneListView", "setRingtoneListOption : " + type);
        this.mType = type;
        this.mExistingUri = uri;
        this.mRingtoneManager = new RingtoneManager(this.mContext);
        this.mRingtoneManager.setType(getRingtoneType(this.mType));
        this.mCursor = this.mRingtoneManager.getCursor();
        this.mCursorCount = this.mCursor.getCount();
        separateOffsetUriValue();
        if (this.mType == 1) {
            addSilentItem();
        }
        if (Uri.EMPTY.equals(this.mExistingUri)) {
            this.mClickedPos = 0;
            if (this.mType == 1 && this.mRingtoneListClickListener != null) {
                this.mRingtoneListClickListener.onClickSilentItem(true);
            }
        } else {
            this.mClickedPos = getListPosition(this.mRingtoneManager.getRingtonePosition(this.mExistingUri));
            if (this.mClickedPos == -1) {
                this.mExistingUri = this.mRingtoneListViewListener.onGetDefaultRingtoneUri();
                this.mClickedPos = getListPosition(this.mRingtoneManager.getRingtonePosition(this.mExistingUri));
            }
        }
        this.mListAdapter = new RingtoneListAdapter(this.mContext, C0680R.layout.tw_simple_list_item_single_choice, this.mCursor, new String[]{"title"}, new int[]{16908308}, 0);
        this.mListView.setAdapter(this.mListAdapter);
        this.mListView.setItemChecked(this.mClickedPos, true);
        this.mListView.setSelection(this.mClickedPos);
    }

    private void addSilentItem() {
        CheckedTextView silentItem = (CheckedTextView) ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0680R.layout.tw_simple_list_item_single_choice, this.mListView, false);
        silentItem.setText(getResources().getString(C0680R.string.timer_sound_silent));
        this.mListView.addHeaderView(silentItem);
    }

    public void setTalkBackEnable(boolean isTalkBackEnabled) {
        this.mListAdapter.setTalkBackEnable(isTalkBackEnabled);
        this.mListAdapter.notifyDataSetInvalidated();
    }

    public void setRingtoneVolume(int volume) {
        this.mRingtoneVolume = volume;
    }

    public Uri getSelectedRingtoneUri() {
        Uri uri = this.mRingtoneManager.getRingtoneUri(getRingtoneManagerPosition(this.mClickedPos));
        if (uri == null) {
            uri = this.mType == 1 ? Uri.EMPTY : this.mRingtoneListViewListener.onGetDefaultRingtoneUri();
            this.mClickedPos = getListPosition(this.mRingtoneManager.getRingtonePosition(uri));
        }
        if (!this.mIsSetHighlight) {
            return uri;
        }
        return Uri.parse(uri.toString() + ("?highlight_offset=" + this.mOffset));
    }

    private void separateOffsetUriValue() {
        if (this.mExistingUri != null) {
            String offset = this.mExistingUri.getQueryParameter("highlight_offset");
            if (offset != null) {
                this.mOffset = Integer.parseInt(offset);
                this.mExistingUri = Uri.parse(this.mExistingUri.toString().replace("?highlight_offset=" + this.mOffset, ""));
                return;
            }
            this.mOffset = 0;
        }
    }

    public void addRingToneFromSoundPicker(Uri uri) {
        this.mExistingUri = ClockUtils.setMusicLibraryRingtone(uri, this.mContext, 4);
        if (this.mDrmCheckManager == null || this.mDrmCheckManager.canSetRingtone(this.mExistingUri)) {
            this.mIsSetHighlight = true;
            separateOffsetUriValue();
            this.mRingtoneManager = new RingtoneManager(this.mContext);
            this.mRingtoneManager.setType(getRingtoneType(this.mType));
            Cursor updatedCursor = this.mRingtoneManager.getCursor();
            this.mCursorCount = updatedCursor.getCount();
            this.mListAdapter.changeCursor(updatedCursor);
            this.mListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this.mContext, this.mContext.getString(C0680R.string.drm_licensed_message), 1).show();
        }
        int tempPos = getListPosition(this.mRingtoneManager.getRingtonePosition(this.mExistingUri));
        Log.secD("RingtoneListView", "new Ringtone position : " + tempPos);
        if (tempPos == -1) {
            this.mExistingUri = this.mRingtoneManager.getRingtoneUri(this.mClickedPos);
            Log.secD("RingtoneListView", "added ringtone invalid. set as before ringtone : " + this.mExistingUri);
        } else {
            this.mClickedPos = tempPos;
        }
        this.mSetHighlightRingtonePos = getRingtoneManagerPosition(this.mClickedPos);
        this.mListView.setItemChecked(this.mClickedPos, true);
        this.mListView.setSelection(this.mClickedPos);
    }

    public void updateRingtoneList() {
        Log.secE("RingtoneListView", "updateRingtoneList");
        this.mRingtoneManager = new RingtoneManager(this.mContext);
        this.mRingtoneManager.setType(getRingtoneType(this.mType));
        Cursor updatedCursor = this.mRingtoneManager.getCursor();
        this.mCursorCount = updatedCursor.getCount();
        this.mListAdapter.changeCursor(updatedCursor);
        this.mListAdapter.notifyDataSetInvalidated();
        if (Uri.EMPTY.equals(this.mExistingUri)) {
            this.mClickedPos = 0;
        } else {
            int tempPos = getListPosition(this.mRingtoneManager.getRingtonePosition(this.mExistingUri));
            if (tempPos == -1) {
                this.mExistingUri = this.mRingtoneListViewListener.onGetDefaultRingtoneUri();
                this.mClickedPos = getListPosition(this.mRingtoneManager.getRingtonePosition(this.mExistingUri));
                Log.secD("RingtoneListView", "set as default ringtone : " + this.mExistingUri);
            } else {
                this.mClickedPos = tempPos;
            }
        }
        this.mListView.setItemChecked(this.mClickedPos, true);
        this.mListView.setSelection(this.mClickedPos);
    }

    public String getRingtoneTitle(int position) {
        if (position >= this.mCursorCount || position < 0) {
            Log.secE("RingtoneListView", "position value is invalid " + position + ", " + this.mCursorCount);
            return "";
        }
        Cursor cursor = (Cursor) this.mListAdapter.getItem(position);
        String ringtoneName = cursor.getString(cursor.getColumnIndex("title"));
        Log.secD("RingtoneListView", "getRingtoneTitle : " + ringtoneName);
        if (ClockUtils.isEnableString(ringtoneName)) {
            return ringtoneName;
        }
        Log.secE("RingtoneListView", "Failed to open ringtone, position = " + position);
        return "";
    }

    private int getRingtoneManagerPosition(int listPos) {
        return listPos - this.mListView.getHeaderViewsCount();
    }

    private int getListPosition(int ringtoneManagerPos) {
        return ringtoneManagerPos < 0 ? ringtoneManagerPos : ringtoneManagerPos + this.mListView.getHeaderViewsCount();
    }

    public Intent getSoundPickerIntent() {
        Intent intent = new Intent();
        intent.setPackage("com.samsung.android.app.soundpicker");
        intent.setType("audio/*");
        intent.setFlags(67108864);
        intent.putExtra("enable_ringtone_recommender", true);
        return intent;
    }

    public void setStreamVolume(int volume) {
        this.mAudioManager.setStreamVolume(4, volume, 0);
        Log.m41d("RingtoneListView", "setStreamVolume STREAM_ALARM volume = " + volume);
    }

    public void playRingtone(int position) {
        if (!StateUtils.isInCallState(this.mContext) && !isRingtonePlaying()) {
            if (!requestAudioFocus()) {
                Log.secW("RingtoneListView", "fail to request Audio Focus");
            } else if (this.mType != 1 || position != 0) {
                this.mSampleRingtonePos = getRingtoneManagerPosition(position);
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 300);
            }
        }
    }

    public void run() {
        if (this.mSampleRingtonePos == this.mSetHighlightRingtonePos) {
            this.mRingtone = this.mRingtoneManager.semGetRingtone(this.mSampleRingtonePos, this.mOffset);
        } else {
            this.mRingtone = this.mRingtoneManager.semGetRingtone(this.mSampleRingtonePos, 0);
        }
        if (this.mRingtone != null) {
            this.mRingtone.setAudioAttributes(new AudioAttributes.Builder(this.mRingtone.getAudioAttributes()).setLegacyStreamType(4).setFlags(0).build());
            Log.secD("RingtoneListView", "mRingtone Play!!!");
            setStreamVolume(this.mRingtoneVolume);
            this.mRingtone.play();
        }
    }

    private boolean requestAudioFocus() {
        Log.secV("RingtoneListView", "requestAudioFocus()");
        if (this.mAudioManager.requestAudioFocus(this.mAudioFocusRequest) != 0) {
            return true;
        }
        Log.secW("RingtoneListView", "requestAudioFocus is failed");
        return false;
    }

    public boolean isRingtonePlaying() {
        return this.mRingtone != null && this.mRingtone.isPlaying();
    }

    public void stopAnyPlayingRingtone() {
        Log.secD("RingtoneListView", "stopAnyPlayingRingtone");
        mHandler.removeCallbacks(this);
        if (isRingtonePlaying()) {
            this.mRingtone.stop();
        }
    }

    private void showUnableToPlayToast() {
        if (this.mCallToast == null) {
            this.mCallToast = Toast.makeText(this.mContext, null, 1);
        }
        if (!this.mCallToast.getView().isShown()) {
            this.mCallToast.setText(this.mContext.getResources().getString(C0680R.string.unable_to_play_during_call));
            this.mCallToast.show();
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.secD("RingtoneListView", "onItemSelected position : " + position);
        stopAnyPlayingRingtone();
        this.mFocusedRingtone = position;
        if (!this.mListView.isEnabled() || !this.mListView.hasFocus()) {
            return;
        }
        if (StateUtils.isInCallState(this.mContext)) {
            showUnableToPlayToast();
        } else {
            playRingtone(position);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
        Log.secD("RingtoneListView", position + " item clicked");
        this.mClickedPos = position;
        Uri uri = this.mRingtoneManager.getRingtoneUri(getRingtoneManagerPosition(this.mClickedPos));
        ClockUtils.insertSaLog(this.mType == 0 ? "104" : "131", this.mType == 0 ? "1030" : "1344");
        if (this.mDrmCheckManager != null && !this.mDrmCheckManager.canSetRingtone(uri)) {
            stopAnyPlayingRingtone();
            Toast.makeText(this.mContext, this.mContext.getString(C0680R.string.drm_licensed_message), 1).show();
        } else if (StateUtils.isInCallState(this.mContext)) {
            showUnableToPlayToast();
        } else {
            stopAnyPlayingRingtone();
            playRingtone(this.mClickedPos);
        }
        if (this.mIsSetHighlight && this.mClickedPos != this.mSetHighlightRingtonePos) {
            this.mIsSetHighlight = false;
        }
        if (this.mType == 1 && this.mRingtoneListClickListener != null) {
            boolean z;
            RingtoneListClickListener ringtoneListClickListener = this.mRingtoneListClickListener;
            if (this.mClickedPos == 0) {
                z = true;
            } else {
                z = false;
            }
            ringtoneListClickListener.onClickSilentItem(z);
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && this.mListView.isEnabled() && this.mFocusedRingtone != -1) {
            playRingtone(this.mFocusedRingtone);
        } else {
            stopAnyPlayingRingtone();
        }
    }

    public void abandonAudioFocus() {
        if (this.mAudioManager != null) {
            this.mAudioManager.abandonAudioFocusRequest(this.mAudioFocusRequest);
        }
    }

    public void removeInstance() {
        if (this.mCursor != null) {
            if (!this.mCursor.isClosed()) {
                this.mCursor.close();
            }
            this.mCursor = null;
        }
        if (this.mAudioManager != null) {
            this.mAudioManager.abandonAudioFocusRequest(this.mAudioFocusRequest);
            this.mAudioManager = null;
        }
        if (this.mContentObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
        if (this.mDrmCheckManager != null) {
            this.mDrmCheckManager.removeInstance();
            this.mDrmCheckManager = null;
        }
    }

    public void performItemClickSilent() {
        if (this.mListView != null) {
            this.mListView.performItemClick(this.mListView.getAdapter().getView(0, null, null), 0, this.mListView.getItemIdAtPosition(0));
        }
    }
}
