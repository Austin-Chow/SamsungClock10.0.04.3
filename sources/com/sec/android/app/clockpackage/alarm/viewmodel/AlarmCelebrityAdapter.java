package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.common.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class AlarmCelebrityAdapter extends CursorAdapter {
    private final String TAG = "AlarmCelebrityAdapter";
    private int mCheckedCursorPos = -1;
    private Context mContext;
    private ArrayList<String> mExpiredDateStr = new ArrayList();
    private boolean mIs24DateFormat;
    private boolean mIsActionMode = false;
    private boolean mIsCelebrityVoiceOn;
    private ArrayList<Boolean> mIsExpiredDate = new ArrayList();
    private ArrayList<Drawable> mPreviewImage = new ArrayList();
    private final SparseBooleanArray mSelectedItemsPositions = new SparseBooleanArray();

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebrityAdapter$1 */
    class C05911 implements OnCheckedChangeListener {
        C05911() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            buttonView.setChecked(isChecked);
        }
    }

    private static class ViewHolder {
        private ImageView checkImage;
        private CheckBox checkbox;
        private TextView contentName;
        private TextView expiredDate;
        private ImageView previewImage;

        private ViewHolder() {
        }
    }

    public AlarmCelebrityAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.mContext = context;
        this.mIs24DateFormat = DateFormat.is24HourFormat(context);
        makeDataList();
    }

    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        makeDataList();
    }

    private void makeDataList() {
        Log.secD("AlarmCelebrityAdapter", "makeDataList");
        if (this.mIsExpiredDate.size() > 0 && this.mExpiredDateStr.size() > 0 && this.mPreviewImage.size() > 0) {
            this.mIsExpiredDate.clear();
            this.mExpiredDateStr.clear();
            this.mPreviewImage.clear();
        }
        for (int i = 0; i < getCount(); i++) {
            Cursor cursor = (Cursor) getItem(i);
            if (cursor != null && cursor.getCount() > 0) {
                Long expiredDate = Long.valueOf(cursor.getLong(cursor.getColumnIndex("VALID_DATE_LONG")));
                boolean isExpired = expiredDate.longValue() < System.currentTimeMillis();
                String displayDate = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mIs24DateFormat ? "MMM d, yyyy HH:mm" : "MMM d, yyyy hh:mm aaa"), Locale.getDefault()).format(expiredDate);
                Log.secD("AlarmCelebrityAdapter", "date : " + displayDate + "," + isExpired);
                this.mIsExpiredDate.add(Boolean.valueOf(isExpired));
                this.mExpiredDateStr.add(displayDate);
                byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex("REP_STATIC"));
                RoundedBitmapDrawable roundImage = RoundedBitmapDrawableFactory.create(this.mContext.getResources(), BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length));
                roundImage.setCircular(true);
                this.mPreviewImage.add(roundImage);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isExpired(int position) {
        if (this.mIsExpiredDate.size() <= 0 || !((Boolean) this.mIsExpiredDate.get(position)).booleanValue()) {
            return false;
        }
        Log.secE("AlarmCelebrityAdapter", "This voice is expired.");
        return true;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, C0490R.layout.celeb_voice_list_item, null);
        ViewHolder holder = new ViewHolder();
        holder.checkImage = (ImageView) view.findViewById(C0490R.id.check_image);
        holder.previewImage = (ImageView) view.findViewById(C0490R.id.preview_image);
        holder.contentName = (TextView) view.findViewById(C0490R.id.content_name);
        holder.expiredDate = (TextView) view.findViewById(C0490R.id.expired_date);
        holder.checkbox = (CheckBox) ((ViewStub) view.findViewById(C0490R.id.checkBox_stub)).inflate();
        holder.checkbox.setPadding(0, 0, 0, 0);
        holder.checkbox.setOnCheckedChangeListener(new C05911());
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (!this.mIsExpiredDate.isEmpty() || !this.mExpiredDateStr.isEmpty() || !this.mPreviewImage.isEmpty()) {
            String contentName = cursor.getString(cursor.getColumnIndex("CONTENT_NAME"));
            int position = cursor.getPosition();
            holder.previewImage.setImageDrawable((Drawable) this.mPreviewImage.get(position));
            holder.contentName.setText(contentName);
            boolean isExpiredDate = ((Boolean) this.mIsExpiredDate.get(position)).booleanValue();
            if (isExpiredDate) {
                holder.expiredDate.setText(context.getString(C0490R.string.expired));
            } else {
                holder.expiredDate.setText(context.getString(C0490R.string.expires, new Object[]{this.mExpiredDateStr.get(position)}));
            }
            if (this.mIsActionMode) {
                holder.checkbox.setVisibility(0);
                holder.checkbox.setChecked(this.mSelectedItemsPositions.get(position));
            } else {
                holder.checkbox.setVisibility(8);
                holder.checkbox.setChecked(false);
                if (position == this.mCheckedCursorPos) {
                    holder.previewImage.setColorFilter(context.getColor(C0490R.color.alarm_celebrity_preview_checked_mask_color));
                    holder.checkImage.setVisibility(0);
                } else {
                    holder.previewImage.setColorFilter(context.getColor(17170445));
                    holder.checkImage.setVisibility(8);
                }
            }
            boolean z = this.mIsActionMode || this.mIsCelebrityVoiceOn;
            view.setEnabled(z);
            z = (this.mIsCelebrityVoiceOn || this.mIsActionMode) ? false : true;
            view.setClickable(z);
            float f = (this.mIsActionMode || this.mIsCelebrityVoiceOn) ? 1.0f : 0.4f;
            view.setAlpha(f);
            boolean isAlpha = (this.mIsActionMode || this.mIsCelebrityVoiceOn) && isExpiredDate;
            holder.previewImage.setAlpha(isAlpha ? 0.4f : 1.0f);
            holder.contentName.setAlpha(isAlpha ? 0.4f : 1.0f);
            TextView access$400 = holder.expiredDate;
            if (isAlpha) {
                f = 0.4f;
            } else {
                f = 1.0f;
            }
            access$400.setAlpha(f);
        }
    }

    public void setIsCelebrityOn(boolean isCelebrityOn) {
        this.mIsCelebrityVoiceOn = isCelebrityOn;
    }

    public void setCheckedPosition(int position) {
        this.mCheckedCursorPos = position;
    }

    public void toggleSelect(View view, int position) {
        if (this.mIsActionMode && view != null) {
            toggleSelect(view, position, !this.mSelectedItemsPositions.get(position));
        }
    }

    public void toggleSelect(View view, int position, boolean isChecked) {
        if (this.mIsActionMode && view != null) {
            if (!isChecked) {
                this.mSelectedItemsPositions.delete(position);
            } else if (!this.mSelectedItemsPositions.get(position)) {
                this.mSelectedItemsPositions.put(position, true);
            }
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder != null && viewHolder.checkbox != null) {
                viewHolder.checkbox.setChecked(this.mSelectedItemsPositions.get(position));
            }
        }
    }

    public void toggleSelects(ArrayList<Integer> list) {
        if (this.mIsActionMode) {
            this.mSelectedItemsPositions.clear();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                this.mSelectedItemsPositions.put(((Integer) it.next()).intValue(), true);
            }
            notifyDataSetChanged();
        }
    }

    public void toggleSelectAll(boolean isAllCheck) {
        this.mSelectedItemsPositions.clear();
        int totalCount = getCount();
        if (isAllCheck) {
            for (int i = 0; i < totalCount; i++) {
                this.mSelectedItemsPositions.put(i, true);
            }
        }
        notifyDataSetChanged();
    }

    public void setActionMode(boolean isActionMode) {
        this.mIsActionMode = isActionMode;
        if (isActionMode) {
            this.mSelectedItemsPositions.clear();
        }
        notifyDataSetChanged();
    }

    public boolean isActionMode() {
        return this.mIsActionMode;
    }

    public SparseBooleanArray getSelectedPositions() {
        return this.mSelectedItemsPositions;
    }

    public void clearSelectedPositions() {
        this.mSelectedItemsPositions.clear();
    }

    public void updateExpiredDate() {
        if (this.mIsExpiredDate.isEmpty() && this.mExpiredDateStr.isEmpty()) {
            Log.secE("AlarmCelebrityAdapter", "can not update Expired Date, the date list is null");
            return;
        }
        boolean is24DateFormat = DateFormat.is24HourFormat(this.mContext);
        boolean changedDate = false;
        for (int i = 0; i < getCount(); i++) {
            Cursor cursor = (Cursor) getItem(i);
            if (cursor != null && cursor.getCount() > 0) {
                Long expiredDate = Long.valueOf(cursor.getLong(cursor.getColumnIndex("VALID_DATE_LONG")));
                boolean isExpiredDate = expiredDate.longValue() < System.currentTimeMillis();
                if (((Boolean) this.mIsExpiredDate.get(i)).booleanValue() != isExpiredDate) {
                    this.mIsExpiredDate.set(i, Boolean.valueOf(isExpiredDate));
                    changedDate = true;
                }
                if (this.mIs24DateFormat != is24DateFormat) {
                    this.mExpiredDateStr.set(i, new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), is24DateFormat ? "MMM d, yyyy HH:mm" : "MMM d, yyyy hh:mm aaa"), Locale.getDefault()).format(expiredDate));
                    changedDate = true;
                }
            }
        }
        this.mIs24DateFormat = is24DateFormat;
        Log.secD("AlarmCelebrityAdapter", "updateExpiredDate " + changedDate);
        if (changedDate) {
            notifyDataSetChanged();
        }
    }

    public void removeInstance() {
        this.mIsExpiredDate.clear();
        this.mExpiredDateStr.clear();
        for (int i = 0; i < this.mPreviewImage.size(); i++) {
            Bitmap image = ((RoundedBitmapDrawable) this.mPreviewImage.get(i)).getBitmap();
            if (image != null) {
                image.recycle();
            }
        }
        this.mPreviewImage.clear();
    }
}
