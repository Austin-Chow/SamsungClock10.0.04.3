package com.sec.android.app.clockpackage.alarm.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.SemLongPressMultiSelectionListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.samsung.android.animation.SemAddDeleteListAnimator;
import com.samsung.android.animation.SemAddDeleteListAnimator.OnAddDeleteListener;
import com.samsung.android.graphics.spr.SemPathRenderingDrawable;
import com.sec.android.app.clockpackage.alarm.C0490R;
import com.sec.android.app.clockpackage.alarm.view.AlarmCelebActionModeCallback.CelebActionModeListener;
import com.sec.android.app.clockpackage.alarm.viewmodel.AlarmCelebrityAdapter;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AlarmCelebrityListView extends RelativeLayout implements OnFocusChangeListener, OnItemClickListener, OnItemLongClickListener {
    private final Uri CELEB_VOICE_URI = Uri.parse("content://com.sec.android.app.clockpackage.celebvoice/preview/clockvoice/*");
    private final String TAG = "AlarmCelebrityListView";
    private AlarmCelebActionModeCallback mActionModeCallBack;
    private AlarmCelebrityAdapter mAdapter;
    private SemAddDeleteListAnimator mAddDeleteAnimator;
    private CelebrityListViewListener mCelebrityListener;
    private Context mContext;
    private int mCurPosition = -1;
    private View mDefaultBixbyVoice;
    private View mDefaultCelebVoice;
    private ListView mListView;

    public interface CelebrityListViewListener {
        void addViewOnToolBar(View view);

        void changeCelebrityPath(int i);

        void deleteContent(String str, String str2);

        void removeViewOnToolBar(View view);

        void setBottomBarVisibility(boolean z);

        void startPlay();

        void stopPlay(boolean z);
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCelebrityListView$1 */
    class C05471 implements SemLongPressMultiSelectionListener {
        private Set<Integer> selectedItems = null;

        C05471() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            int itemPos = position - AlarmCelebrityListView.this.mListView.getHeaderViewsCount();
            if (this.selectedItems != null) {
                if (this.selectedItems.contains(Integer.valueOf(itemPos))) {
                    AlarmCelebrityListView.this.mAdapter.toggleSelect(view, itemPos);
                    return;
                }
                AlarmCelebrityListView.this.mAdapter.toggleSelect(view, itemPos, true);
                this.selectedItems.add(Integer.valueOf(itemPos));
            }
        }

        public void onLongPressMultiSelectionStarted(int startX, int startY) {
            if (this.selectedItems == null) {
                this.selectedItems = new HashSet();
            }
            this.selectedItems.clear();
        }

        public void onLongPressMultiSelectionEnded(int endX, int endY) {
            if (this.selectedItems != null) {
                this.selectedItems.clear();
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCelebrityListView$2 */
    class C05482 implements CelebActionModeListener {
        C05482() {
        }

        public void toggleSelectAll(boolean isChecked) {
            AlarmCelebrityListView.this.mAdapter.toggleSelectAll(isChecked);
            AlarmCelebrityListView.this.mActionModeCallBack.updateSelectionMenu(AlarmCelebrityListView.this.mAdapter.getCount(), AlarmCelebrityListView.this.getSelectedPositions().size());
        }

        public void setBottomBarVisibility(boolean isVisible) {
            AlarmCelebrityListView.this.mCelebrityListener.setBottomBarVisibility(isVisible);
        }

        public void addViewOnToolBar(View view) {
            AlarmCelebrityListView.this.mCelebrityListener.addViewOnToolBar(view);
        }

        public void removeViewOnToolBar(View view) {
            AlarmCelebrityListView.this.mCelebrityListener.removeViewOnToolBar(view);
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.view.AlarmCelebrityListView$4 */
    class C05504 implements OnAddDeleteListener {
        C05504() {
        }

        public void onDelete() {
            AlarmCelebrityListView.this.deleteData();
        }

        public void onAdd() {
        }

        public void onAnimationStart(boolean b) {
        }

        public void onAnimationEnd(boolean b) {
            if (!b) {
                AlarmCelebrityListView.this.finishActionMode();
            }
        }
    }

    private static final class SavedState extends BaseSavedState {
        boolean isActionMode;
        ArrayList<Integer> selectedIds;

        SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public void setOnCelebrityListViewListener(CelebrityListViewListener listViewListener) {
        this.mCelebrityListener = listViewListener;
    }

    public AlarmCelebrityListView(Context context) {
        super(context);
    }

    public AlarmCelebrityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmCelebrityListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContext(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        Log.secE("AlarmCelebrityListView", "init");
        LayoutInflater.from(this.mContext).inflate(C0490R.layout.ringtone_list_view, this, true);
        this.mListView = (ListView) findViewById(C0490R.id.list);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setOnItemLongClickListener(this);
        this.mListView.setOnFocusChangeListener(this);
        this.mListView.setChoiceMode(1);
        this.mListView.setMultiChoiceModeListener(this.mActionModeCallBack);
        this.mListView.semSetLongPressMultiSelectionEnabled(true);
        this.mListView.semSetLongPressMultiSelectionListener(new C05471());
        Cursor cursor = getData();
        this.mActionModeCallBack = new AlarmCelebActionModeCallback(this.mContext);
        this.mActionModeCallBack.setOnCelebActionModeListener(new C05482());
        this.mAdapter = new AlarmCelebrityAdapter(this.mContext, cursor, true);
        addHeaderFooterItem();
        this.mListView.setAdapter(this.mAdapter);
        setAddDeleteAnimator();
        this.mAdapter.notifyDataSetChanged();
        setDivider();
    }

    private void setDivider() {
        InsetDrawable listDivider;
        int dividerInset = getResources().getDimensionPixelSize(C0490R.dimen.celeb_listview_inset_for_divider);
        Drawable divider = this.mListView.getDivider();
        if (StateUtils.isRtl()) {
            listDivider = new InsetDrawable(divider, 0, 0, dividerInset, 0);
        } else {
            InsetDrawable insetDrawable = new InsetDrawable(divider, dividerInset, 0, 0, 0);
        }
        this.mListView.setDivider(listDivider);
    }

    public void setCheckItem(int cursorPosition) {
        int headerCount = this.mListView.getHeaderViewsCount();
        this.mListView.setItemChecked(cursorPosition + headerCount, true);
        this.mListView.setSelection(cursorPosition + headerCount);
        this.mAdapter.setCheckedPosition(cursorPosition);
        checkDefaultPreview(cursorPosition);
    }

    private void addHeaderFooterItem() {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        if (Feature.isSupportBixbyBriefingMenu(this.mContext)) {
            this.mDefaultBixbyVoice = makeHeaderView(inflater, -2, C0490R.drawable.bixby, getResources().getString(C0490R.string.bixby));
            this.mListView.addHeaderView(this.mDefaultBixbyVoice);
        }
        this.mDefaultCelebVoice = makeHeaderView(inflater, -3, C0490R.drawable.sca_default_v01_preview, getResources().getString(C0490R.string.default_celeb_title));
        this.mListView.addHeaderView(this.mDefaultCelebVoice);
    }

    public void updateBixbyMenu() {
        if (Feature.isSupportBixbyBriefingMenu(this.mContext)) {
            if (this.mDefaultBixbyVoice == null) {
                this.mDefaultBixbyVoice = makeHeaderView(LayoutInflater.from(this.mContext), -2, C0490R.drawable.bixby, getResources().getString(C0490R.string.bixby));
                this.mListView.addHeaderView(this.mDefaultBixbyVoice);
            }
        } else if (this.mDefaultBixbyVoice != null) {
            if (this.mCurPosition == -2) {
                setCheckItem(-3);
                this.mCelebrityListener.changeCelebrityPath(-3);
            }
            this.mListView.removeHeaderView(this.mDefaultBixbyVoice);
            this.mDefaultBixbyVoice = null;
        }
    }

    private View makeHeaderView(LayoutInflater inflater, final int pos, int drawable, String title) {
        View headerView = inflater.inflate(C0490R.layout.alarm_celeb_default_header, this.mListView, false);
        ((ImageView) headerView.findViewById(C0490R.id.preview_image)).setImageDrawable(getRoundPreviewImage(drawable));
        ((TextView) headerView.findViewById(C0490R.id.content_name)).setText(title);
        headerView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!AlarmCelebrityListView.this.isActionMode()) {
                    AlarmCelebrityListView.this.checkDefaultPreview(pos);
                    AlarmCelebrityListView.this.mCelebrityListener.stopPlay(true);
                    AlarmCelebrityListView.this.mCelebrityListener.changeCelebrityPath(pos);
                    AlarmCelebrityListView.this.mCelebrityListener.startPlay();
                }
            }
        });
        return headerView;
    }

    private RoundedBitmapDrawable getRoundPreviewImage(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof SemPathRenderingDrawable) {
            bitmap = ((SemPathRenderingDrawable) drawable).getBitmap();
        }
        if (bitmap == null) {
            return null;
        }
        RoundedBitmapDrawable roundImage = RoundedBitmapDrawableFactory.create(this.mContext.getResources(), bitmap);
        roundImage.setCircular(true);
        return roundImage;
    }

    private void checkHeaderView(View view) {
        if (view != null) {
            ((ImageView) view.findViewById(C0490R.id.preview_image)).setColorFilter(getResources().getColor(C0490R.color.alarm_celebrity_preview_checked_mask_color));
            ((ImageView) view.findViewById(C0490R.id.check_image)).setVisibility(0);
            this.mAdapter.setCheckedPosition(-1);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void unCheckHeadView(View view) {
        if (view != null) {
            ((ImageView) view.findViewById(C0490R.id.preview_image)).setColorFilter(getResources().getColor(17170445));
            ((ImageView) view.findViewById(C0490R.id.check_image)).setVisibility(8);
        }
    }

    public void checkDefaultPreview(int pos) {
        this.mCurPosition = pos;
        unCheckHeadView(this.mDefaultBixbyVoice);
        unCheckHeadView(this.mDefaultCelebVoice);
        if (pos == -2) {
            checkHeaderView(this.mDefaultBixbyVoice);
        } else if (pos == -3) {
            checkHeaderView(this.mDefaultCelebVoice);
        }
    }

    private void setDefaultItemEnabled(boolean isEnabled) {
        if (this.mDefaultBixbyVoice != null) {
            setEnabled(this.mDefaultBixbyVoice, isEnabled);
        }
        if (this.mDefaultCelebVoice != null) {
            setEnabled(this.mDefaultCelebVoice, isEnabled);
        }
    }

    private void setEnabled(View view, boolean isEnabled) {
        float f = 1.0f;
        if (view != null) {
            float f2;
            View findViewById = view.findViewById(C0490R.id.preview_image);
            if (isEnabled) {
                f2 = 1.0f;
            } else {
                f2 = 0.4f;
            }
            findViewById.setAlpha(f2);
            View findViewById2 = view.findViewById(C0490R.id.check_image);
            if (!isEnabled) {
                f = 0.4f;
            }
            findViewById2.setAlpha(f);
            view.setEnabled(isEnabled);
        }
    }

    protected Parcelable onSaveInstanceState() {
        Log.secD("AlarmCelebrityListView", "onSaveInstanceState");
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isActionMode = this.mActionModeCallBack.isActionMode();
        savedState.selectedIds = getSelectedPositions();
        return savedState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            Log.secD("AlarmCelebrityListView", "onRestoreInstanceState " + state);
            SavedState savedState = (SavedState) state;
            if (savedState.isActionMode) {
                startActionMode(false);
                ArrayList<Integer> selectedItems = savedState.selectedIds;
                if (!(selectedItems == null || selectedItems.size() <= 0 || this.mAdapter == null)) {
                    this.mAdapter.toggleSelects(selectedItems);
                    this.mActionModeCallBack.updateSelectionMenu(this.mAdapter.getCount(), getSelectedPositions().size());
                }
            }
            super.onRestoreInstanceState(savedState.getSuperState());
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void setAddDeleteAnimator() {
        this.mAddDeleteAnimator = new SemAddDeleteListAnimator(this.mContext, this.mListView);
        this.mAddDeleteAnimator.setOnAddDeleteListener(new C05504());
    }

    public String getCelebrityPath(int position) {
        if (position < 0) {
            Log.secE("AlarmCelebrityListView", "getCelebrityPath invalid case");
            return "";
        }
        Cursor cursor = (Cursor) this.mAdapter.getItem(position);
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        String path = cursor.getString(cursor.getColumnIndex("PKG_NAME"));
        Log.secD("AlarmCelebrityListView", "getCelebrityPath " + path);
        return path;
    }

    private Cursor getData() {
        return this.mContext.getContentResolver().query(this.CELEB_VOICE_URI, null, null, null, "_id DESC");
    }

    private void deleteData() {
        ArrayList<Integer> selectedIds = getSelectedPositions();
        Log.secD("AlarmCelebrityListView", "deleteData : " + selectedIds);
        for (int i = 0; i < selectedIds.size(); i++) {
            Cursor cursor = (Cursor) this.mAdapter.getItem(((Integer) selectedIds.get(i)).intValue());
            if (cursor != null && cursor.getCount() > 0) {
                String packageName = cursor.getString(cursor.getColumnIndex("PKG_NAME"));
                this.mCelebrityListener.deleteContent(cursor.getString(cursor.getColumnIndex("TYPE")), packageName);
            }
        }
        this.mAdapter.clearSelectedPositions();
        Cursor updateCursor = getData();
        if (updateCursor != null) {
            this.mAdapter.changeCursor(updateCursor);
        }
    }

    public ArrayList<Integer> getSelectedPositions() {
        SparseBooleanArray selected = this.mAdapter.getSelectedPositions();
        ArrayList<Integer> selectedItemPositions = new ArrayList();
        for (int i = this.mAdapter.getCount() - 1; i >= 0; i--) {
            if (selected.get(i)) {
                selectedItemPositions.add(Integer.valueOf(i));
            }
        }
        return selectedItemPositions;
    }

    public int getPositionFromCelebrityPath(String path) {
        int position = -1;
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            Cursor cursor = (Cursor) this.mAdapter.getItem(i);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.getString(cursor.getColumnIndex("PKG_NAME")).equals(path)) {
                    position = cursor.getPosition();
                    break;
                }
                cursor.moveToNext();
            }
        }
        Log.secD("AlarmCelebrityListView", "getPositionFromCelebrityPath " + path + ", position " + position);
        if (position >= 0 || position <= this.mAdapter.getCount()) {
            return position;
        }
        return -1;
    }

    public int getItemCount() {
        return this.mAdapter.getCount();
    }

    public void setEnabled(boolean isCelebrityOn) {
        this.mAdapter.setIsCelebrityOn(isCelebrityOn);
        this.mAdapter.notifyDataSetChanged();
    }

    public void changeCursor() {
        Cursor updateCursor = getData();
        if (updateCursor != null) {
            this.mAdapter.changeCursor(updateCursor);
        }
    }

    public void startActionMode(boolean isPressedOption) {
        this.mListView.startActionMode(this.mActionModeCallBack);
        setActionMode(true);
        if (isPressedOption && this.mAdapter != null && this.mAdapter.getCount() == 1) {
            this.mAdapter.toggleSelectAll(true);
            this.mActionModeCallBack.updateSelectionMenu(this.mAdapter.getCount(), getSelectedPositions().size());
        }
    }

    public boolean isActionMode() {
        return this.mActionModeCallBack != null && this.mActionModeCallBack.isActionMode();
    }

    public void finishActionMode() {
        if (this.mActionModeCallBack != null) {
            setActionMode(false);
            this.mActionModeCallBack.finish();
        }
    }

    private void setActionMode(boolean isActionMode) {
        setDefaultItemEnabled(!isActionMode);
        this.mAdapter.setActionMode(isActionMode);
        this.mAdapter.notifyDataSetChanged();
        this.mAdapter.clearSelectedPositions();
    }

    public void updateActionMode() {
        if (this.mAdapter.isActionMode()) {
            ArrayList<Integer> selectedItems = getSelectedPositions();
            for (int i = 0; i < selectedItems.size(); i++) {
                selectedItems.set(i, Integer.valueOf(((Integer) selectedItems.get(i)).intValue() + 1));
            }
            this.mAdapter.toggleSelects(selectedItems);
            this.mActionModeCallBack.updateSelectionMenu(this.mAdapter.getCount(), getSelectedPositions().size());
        }
    }

    public void updateExpiredDate() {
        this.mAdapter.updateExpiredDate();
    }

    public void onFocusChange(View view, boolean b) {
        Log.secE("AlarmCelebrityListView", "view " + view);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.secD("AlarmCelebrityListView", position + " clicked " + view + ". " + parent + ", " + this.mAdapter.getCount());
        int itemPos = position - this.mListView.getHeaderViewsCount();
        if (this.mActionModeCallBack.isActionMode()) {
            if (itemPos >= 0) {
                this.mAdapter.toggleSelect(view, itemPos);
                this.mActionModeCallBack.updateSelectionMenu(this.mAdapter.getCount(), getSelectedPositions().size());
            }
        } else if (itemPos < 0) {
            view.performClick();
        } else if (view.isEnabled() && !this.mAdapter.isExpired(itemPos)) {
            this.mCelebrityListener.stopPlay(true);
            setCheckItem(itemPos);
            this.mAdapter.notifyDataSetChanged();
            this.mCelebrityListener.changeCelebrityPath(itemPos);
            Cursor cursor = (Cursor) this.mAdapter.getItem(itemPos);
            ClockUtils.insertSaLog("613", "6132", cursor.getString(cursor.getColumnIndex("CONTENT_NAME")));
            this.mCelebrityListener.startPlay();
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        int itemPos = position - this.mListView.getHeaderViewsCount();
        this.mCelebrityListener.stopPlay(false);
        if (itemPos < 0 || itemPos > this.mAdapter.getCount() || this.mActionModeCallBack == null) {
            return false;
        }
        if (this.mActionModeCallBack.isActionMode()) {
            return true;
        }
        startActionMode(false);
        this.mAdapter.toggleSelect(view, itemPos, true);
        this.mActionModeCallBack.updateSelectionMenu(this.mAdapter.getCount(), getSelectedPositions().size());
        return true;
    }

    public void removeInstance() {
        if (this.mAdapter != null) {
            this.mAdapter.removeInstance();
        }
    }

    public void deleteItems() {
        this.mAddDeleteAnimator.setDelete(getSelectedPositions());
    }
}
