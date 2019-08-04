package com.sec.android.app.clockpackage.timer.viewmodel;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteFullException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.view.animation.SineInOut33;
import com.samsung.android.view.animation.SineInOut80;
import com.samsung.android.view.animation.SineInOut90;
import com.sec.android.app.clockpackage.common.callback.DndHelper;
import com.sec.android.app.clockpackage.common.callback.DndHelper.ItemListener;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.timer.C0728R;
import com.sec.android.app.clockpackage.timer.callback.TimerPresetAdapterListener;
import com.sec.android.app.clockpackage.timer.model.TimerPresetItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TimerPresetAdapter extends Adapter<PresetViewHolder> implements ItemListener {
    private SparseBooleanArray mCheckedItems = new SparseBooleanArray();
    private Context mContext;
    private DndHelper mDndHelper = new DndHelper(this);
    private boolean mIsDisplayCheckBox = false;
    private boolean mIsEnabled = true;
    private boolean mIsItemMoved = false;
    private ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(this.mDndHelper);
    private ArrayList<TimerPresetItem> mPresetItems;
    private int mPressedItemPosition = -1;
    private ValueAnimator mScaleDownAnimator;
    private long mSelectedPresetId;
    private TimerPresetAdapterListener mTimerPresetAdapterListener;

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetAdapter$2 */
    class C08082 extends AsyncTask<Void, Void, Void> {
        C08082() {
        }

        protected Void doInBackground(Void[] params) {
            int size = TimerPresetAdapter.this.getItemCount();
            int i = 0;
            while (i < size) {
                TimerPresetItem preset = TimerPresetAdapter.this.getItem(i);
                if (!(preset == null || preset.getOrder() == i)) {
                    preset.setOrder(i);
                    TimerPresetItem.updatePreset(TimerPresetAdapter.this.mContext.getContentResolver(), preset);
                }
                i++;
            }
            return null;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.timer.viewmodel.TimerPresetAdapter$4 */
    class C08104 implements AnimatorListener {
        C08104() {
        }

        public void onAnimationStart(Animator animator) {
        }

        public void onAnimationEnd(Animator animator) {
            TimerPresetAdapter.this.mPressedItemPosition = -1;
        }

        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationRepeat(Animator animator) {
        }
    }

    public static class PresetViewHolder extends ViewHolder {
        private CheckBox mCheckBox;
        private TextView mNameView;
        private LinearLayout mPresetItem;
        private TextView mTimeView;

        private PresetViewHolder(View v) {
            super(v);
            this.mNameView = (TextView) v.findViewById(C0728R.id.name);
            this.mTimeView = (TextView) v.findViewById(C0728R.id.time);
            this.mPresetItem = (LinearLayout) v.findViewById(C0728R.id.preset_item);
        }

        private void inflateCheckBox() {
            if (this.mCheckBox == null) {
                this.mCheckBox = (CheckBox) ((ViewStub) this.itemView.findViewById(C0728R.id.checkbox_stub)).inflate();
                this.mCheckBox.setPaddingRelative(0, 0, 0, 0);
            }
        }
    }

    public TimerPresetAdapter(ArrayList<TimerPresetItem> items, Context context, TimerPresetAdapterListener adapterListener) {
        this.mContext = context;
        this.mPresetItems = items;
        this.mTimerPresetAdapterListener = adapterListener;
        this.mDndHelper.setDragFlag(51);
    }

    public int getItemCount() {
        return this.mPresetItems != null ? this.mPresetItems.size() : 0;
    }

    public long getItemId(int position) {
        if (this.mPresetItems == null || position < 0 || position >= this.mPresetItems.size()) {
            return -1;
        }
        return ((TimerPresetItem) this.mPresetItems.get(position)).getId();
    }

    public PresetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final PresetViewHolder holder = new PresetViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0728R.layout.timer_preset_item, parent, false));
        holder.itemView.setOnTouchListener(new OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                int position = holder.getAdapterPosition();
                if (action == 0) {
                    if (position != TimerPresetAdapter.this.mPressedItemPosition) {
                        TimerPresetAdapter.this.startScaleDownAnimation(holder);
                        TimerPresetAdapter.this.mPressedItemPosition = position;
                    }
                } else if ((action == 1 || action == 3) && position == TimerPresetAdapter.this.mPressedItemPosition) {
                    if (TimerPresetAdapter.this.mScaleDownAnimator != null) {
                        TimerPresetAdapter.this.mScaleDownAnimator.cancel();
                    }
                    TimerPresetAdapter.this.startScaleUpAnimation(holder);
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(TimerPresetAdapter$$Lambda$1.lambdaFactory$(this, holder));
        holder.itemView.setOnLongClickListener(TimerPresetAdapter$$Lambda$4.lambdaFactory$(this, holder));
        if (StateUtils.isContextInDexMode(this.mContext)) {
            holder.itemView.setOnCreateContextMenuListener(TimerPresetAdapter$$Lambda$5.lambdaFactory$(this, holder));
        }
        return holder;
    }

    private /* synthetic */ void lambda$onCreateViewHolder$0(PresetViewHolder holder, View v) {
        this.mTimerPresetAdapterListener.onItemClick(v, holder.getAdapterPosition());
    }

    private /* synthetic */ boolean lambda$onCreateViewHolder$1(PresetViewHolder holder, View v) {
        this.mTimerPresetAdapterListener.onItemLongClick(v, holder.getAdapterPosition());
        return true;
    }

    private /* synthetic */ void lambda$onCreateViewHolder$3(PresetViewHolder holder, ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (menu != null) {
            int position = holder.getAdapterPosition();
            menu.setHeaderTitle(holder.mTimeView.getText().toString() + ' ' + holder.mNameView.getText().toString());
            menu.add(0, 2, 0, C0728R.string.delete).setOnMenuItemClickListener(TimerPresetAdapter$$Lambda$6.lambdaFactory$(this, position));
        }
    }

    private /* synthetic */ boolean lambda$null$2(int position, MenuItem menuItem) {
        this.mTimerPresetAdapterListener.onDeleteContextMenuClick(position);
        return true;
    }

    public void onBindViewHolder(PresetViewHolder holder, int position) {
        boolean isActionMode = this.mTimerPresetAdapterListener.isActionMode();
        TimerPresetItem item = (TimerPresetItem) this.mPresetItems.get(position);
        if (holder != null && item != null) {
            boolean z;
            int textColor;
            Typeface textFont;
            if (item.getName().length() > 0) {
                holder.mNameView.setText(item.getName());
                holder.mNameView.setHorizontallyScrolling(true);
                holder.mNameView.setVisibility(0);
            } else {
                holder.mNameView.setVisibility(8);
            }
            holder.mTimeView.setText(item.getDisplayTime(this.mContext));
            Resources res = this.mContext.getResources();
            holder.mTimeView.setVisibility(0);
            if (isActionMode) {
                holder.inflateCheckBox();
                if (this.mIsDisplayCheckBox) {
                    holder.mCheckBox.setVisibility(0);
                }
                holder.mCheckBox.setChecked(this.mCheckedItems.get(position));
                holder.mCheckBox.setContentDescription(getContentDescription(item));
                holder.mCheckBox.setImportantForAccessibility(1);
                holder.mPresetItem.setImportantForAccessibility(2);
            } else {
                if (holder.mCheckBox != null) {
                    holder.mCheckBox.setVisibility(8);
                    holder.mCheckBox.setChecked(false);
                    holder.mCheckBox.setImportantForAccessibility(2);
                }
                holder.mPresetItem.setContentDescription(getContentDescription(item));
                holder.mPresetItem.setImportantForAccessibility(1);
            }
            DndHelper dndHelper = this.mDndHelper;
            if (!isActionMode || this.mPresetItems.size() <= 1) {
                z = false;
            } else {
                z = true;
            }
            dndHelper.setLongPressDragEnabled(z);
            View view = holder.itemView;
            if (isActionMode) {
                z = false;
            } else {
                z = true;
            }
            view.setHapticFeedbackEnabled(z);
            holder.itemView.setEnabled(this.mIsEnabled);
            if (item.getId() == this.mSelectedPresetId) {
                textColor = res.getColor(C0728R.color.timer_preset_item_selected_text_color, null);
                textFont = Typeface.create("sec-roboto-light", 1);
            } else {
                textColor = res.getColor(C0728R.color.timer_preset_item_default_text_color, null);
                textFont = Typeface.create("sec-roboto-light", 0);
            }
            holder.mNameView.setTextColor(textColor);
            holder.mNameView.setTypeface(textFont);
            ClockUtils.setLargeTextSize(this.mContext, holder.mNameView, (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_preset_item_name_textsize));
            holder.mTimeView.setTextColor(textColor);
            holder.mTimeView.setTypeface(textFont);
            ClockUtils.setLargeTextSize(this.mContext, holder.mTimeView, (float) res.getDimensionPixelSize(C0728R.dimen.timer_common_preset_item_time_textsize));
        }
    }

    private StringBuilder getContentDescription(TimerPresetItem item) {
        Resources res = this.mContext.getResources();
        StringBuilder description = new StringBuilder();
        description.append(res.getString(item.getId() == this.mSelectedPresetId ? C0728R.string.selected : C0728R.string.not_selected)).append(", ").append(res.getString(C0728R.string.timer_preset_timer)).append(", ");
        if (!item.getName().isEmpty()) {
            description.append(item.getName()).append(", ");
        }
        if (item.getHour() != 0) {
            description.append(item.getHour()).append(' ').append(res.getString(C0728R.string.timer_hour)).append(' ');
        }
        if (item.getMinute() != 0) {
            description.append(item.getMinute()).append(' ').append(res.getString(C0728R.string.timer_minute)).append(' ');
        }
        if (item.getSecond() != 0) {
            description.append(item.getSecond()).append(' ').append(res.getString(C0728R.string.timer_second));
        }
        return description;
    }

    public void setPresetList(ArrayList<TimerPresetItem> items) {
        this.mPresetItems = items;
        notifyDataSetChanged();
    }

    public void destroy() {
        this.mContext = null;
    }

    ItemTouchHelper getDndHelper() {
        return this.mItemTouchHelper;
    }

    public void onItemMoved(int fromPosition, int toPosition) {
        if (toPosition != getItemCount()) {
            Collections.swap(this.mPresetItems, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            if (this.mCheckedItems.get(fromPosition, false)) {
                if (!this.mCheckedItems.get(toPosition, false)) {
                    this.mCheckedItems.delete(fromPosition);
                    this.mCheckedItems.put(toPosition, true);
                }
            } else if (this.mCheckedItems.get(toPosition, false)) {
                this.mCheckedItems.delete(toPosition);
                this.mCheckedItems.put(fromPosition, true);
            }
            this.mIsItemMoved = true;
        }
    }

    public void onDrop() {
        if (this.mIsItemMoved) {
            this.mIsItemMoved = false;
            try {
                new C08082().execute(new Void[0]);
            } catch (SQLiteFullException e) {
                Toast.makeText(this.mContext, this.mContext.getString(C0728R.string.memory_full), 1).show();
            }
            ClockUtils.insertSaLog("137", "1350");
        }
    }

    public void onDrag(ViewHolder viewHolder) {
        if (viewHolder != null) {
            viewHolder.itemView.animate().alpha(0.8f).setDuration(120).setInterpolator(new SineInOut33()).start();
        }
    }

    public void onClearView(ViewHolder viewHolder) {
        if (viewHolder != null) {
            viewHolder.itemView.animate().alpha(1.0f).setDuration(250).setInterpolator(new SineInOut33()).start();
        }
    }

    public void remove(int position) {
        if (this.mPresetItems.size() > position) {
            this.mPresetItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public long getSelectedPresetId() {
        return this.mSelectedPresetId;
    }

    public void setSelectedPresetId(long id) {
        this.mSelectedPresetId = id;
        notifyDataSetChanged();
    }

    public void setIsDisplayCheckBox(boolean isDisplayCheckBox) {
        this.mIsDisplayCheckBox = isDisplayCheckBox;
    }

    public void setEnabled(boolean enabled) {
        this.mIsEnabled = enabled;
        notifyDataSetChanged();
    }

    public void toggleSelectAllCheck(boolean isChecked) {
        this.mCheckedItems.clear();
        if (isChecked) {
            for (int i = 0; i < getItemCount(); i++) {
                this.mCheckedItems.put(i, true);
            }
        }
        notifyDataSetChanged();
    }

    public void toggleCheck(int position) {
        if (this.mCheckedItems.get(position, false)) {
            this.mCheckedItems.delete(position);
        } else {
            this.mCheckedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public int getCheckedItemCount() {
        return this.mCheckedItems != null ? this.mCheckedItems.size() : 0;
    }

    public void clearCheckedItems() {
        if (this.mCheckedItems != null) {
            this.mCheckedItems.clear();
            notifyDataSetChanged();
        }
    }

    public SparseBooleanArray getCheckedItems() {
        return this.mCheckedItems;
    }

    public void setCheckedItems(ArrayList<Integer> checkedPositions) {
        this.mCheckedItems.clear();
        Iterator it = checkedPositions.iterator();
        while (it.hasNext()) {
            this.mCheckedItems.put(((Integer) it.next()).intValue(), true);
        }
        notifyDataSetChanged();
    }

    public TimerPresetItem getItem(int position) {
        return this.mPresetItems.size() > position ? (TimerPresetItem) this.mPresetItems.get(position) : null;
    }

    public int getPressedItemPosition() {
        return this.mPressedItemPosition;
    }

    public void startScaleUpAnimation(final ViewHolder holder) {
        ValueAnimator scaleUpAnimator = ValueAnimator.ofFloat(new float[]{0.9f, 1.0f});
        scaleUpAnimator.setDuration(200).setInterpolator(new SineInOut80());
        scaleUpAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = ((Float) animation.getAnimatedValue()).floatValue();
                holder.itemView.setScaleX(scale);
                holder.itemView.setScaleY(scale);
            }
        });
        scaleUpAnimator.addListener(new C08104());
        scaleUpAnimator.start();
    }

    private void startScaleDownAnimation(final ViewHolder holder) {
        this.mScaleDownAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.9f});
        this.mScaleDownAnimator.setDuration(200).setInterpolator(new SineInOut80());
        this.mScaleDownAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = ((Float) animation.getAnimatedValue()).floatValue();
                holder.itemView.setScaleX(scale);
                holder.itemView.setScaleY(scale);
            }
        });
        this.mScaleDownAnimator.start();
    }

    public void startActionModeAnimation(PresetViewHolder holder) {
        Animation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(new SineInOut90());
        scaleAnimation.setDuration(400);
        this.mIsDisplayCheckBox = true;
        if (holder.mCheckBox != null) {
            holder.mCheckBox.setVisibility(0);
            holder.mCheckBox.startAnimation(scaleAnimation);
        }
    }
}
