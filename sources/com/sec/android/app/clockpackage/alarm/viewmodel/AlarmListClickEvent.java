package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import com.sec.android.app.clockpackage.alarm.C0490R;

public class AlarmListClickEvent {
    private final OnChildAttachStateChangeListener mAttachListener = new C06043();
    private final OnClickListener mOnClickListener = new C06021();
    private OnAlarmItemClickListener mOnItemClickListener;
    private final OnLongClickListener mOnLongClickListener = new C06032();
    private final RecyclerView mRecyclerView;
    private OnAlarmItemLongClickListener onItemLongClickListener;

    public interface OnAlarmItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int i, View view);
    }

    public interface OnAlarmItemLongClickListener {
        void onItemLongClicked(RecyclerView recyclerView, int i, View view);
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListClickEvent$1 */
    class C06021 implements OnClickListener {
        C06021() {
        }

        public void onClick(View view) {
            if (AlarmListClickEvent.this.mOnItemClickListener != null) {
                ViewHolder holder = AlarmListClickEvent.this.mRecyclerView.getChildViewHolder(view);
                if (holder != null) {
                    AlarmListClickEvent.this.mOnItemClickListener.onItemClicked(AlarmListClickEvent.this.mRecyclerView, holder.getAdapterPosition(), view);
                }
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListClickEvent$2 */
    class C06032 implements OnLongClickListener {
        C06032() {
        }

        public boolean onLongClick(View view) {
            if (AlarmListClickEvent.this.onItemLongClickListener != null) {
                ViewHolder holder = AlarmListClickEvent.this.mRecyclerView.getChildViewHolder(view);
                if (holder != null) {
                    AlarmListClickEvent.this.onItemLongClickListener.onItemLongClicked(AlarmListClickEvent.this.mRecyclerView, holder.getAdapterPosition(), view);
                }
            }
            return true;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.alarm.viewmodel.AlarmListClickEvent$3 */
    class C06043 implements OnChildAttachStateChangeListener {
        C06043() {
        }

        public void onChildViewAttachedToWindow(View view) {
            if (AlarmListClickEvent.this.mOnItemClickListener != null) {
                view.setOnClickListener(AlarmListClickEvent.this.mOnClickListener);
            }
            if (AlarmListClickEvent.this.onItemLongClickListener != null) {
                view.setOnLongClickListener(AlarmListClickEvent.this.mOnLongClickListener);
            }
        }

        public void onChildViewDetachedFromWindow(View view) {
        }
    }

    public AlarmListClickEvent setOnItemClickListener(OnAlarmItemClickListener listener) {
        this.mOnItemClickListener = listener;
        return this;
    }

    public AlarmListClickEvent setOnItemLongClickListener(OnAlarmItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
        return this;
    }

    private AlarmListClickEvent(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        this.mRecyclerView.setTag(Integer.valueOf(C0490R.id.alarm_list));
        this.mRecyclerView.addOnChildAttachStateChangeListener(this.mAttachListener);
    }

    public static AlarmListClickEvent addTo(RecyclerView view) {
        AlarmListClickEvent alarmListClickEvent = (AlarmListClickEvent) view.getTag(C0490R.id.alarm_list);
        if (alarmListClickEvent == null) {
            return new AlarmListClickEvent(view);
        }
        return alarmListClickEvent;
    }

    public static AlarmListClickEvent removeFrom(RecyclerView view) {
        AlarmListClickEvent alarmListClickEvent = (AlarmListClickEvent) view.getTag(C0490R.id.alarm_list);
        if (alarmListClickEvent != null) {
            alarmListClickEvent.detach(view);
        }
        return alarmListClickEvent;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(this.mAttachListener);
        view.setTag(C0490R.id.alarm_list, null);
    }
}
