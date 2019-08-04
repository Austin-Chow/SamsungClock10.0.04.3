package com.sec.android.app.clockpackage.common.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

public class DndHelper extends Callback {
    private int mDragFlag = 3;
    private boolean mIsFinishedClearView;
    private boolean mIsLongPressDragEnabled = false;
    private ItemListener mListener;
    private int mSelectedActionState = 0;

    public interface ItemListener {
        void onClearView(ViewHolder viewHolder);

        void onDrag(ViewHolder viewHolder);

        void onDrop();

        void onItemMoved(int i, int i2);
    }

    public DndHelper(ItemListener listener) {
        this.mListener = listener;
    }

    public void setLongPressDragEnabled(boolean enabled) {
        this.mIsLongPressDragEnabled = enabled;
    }

    public void setDragFlag(int dragFlag) {
        this.mDragFlag = dragFlag;
    }

    public boolean isLongPressDragEnabled() {
        return this.mIsLongPressDragEnabled;
    }

    public void onSwiped(ViewHolder viewHolder, int i) {
    }

    public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
        return Callback.makeMovementFlags(this.mDragFlag, 0);
    }

    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        if (this.mListener != null) {
            this.mListener.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (this.mListener != null) {
            if (actionState == 0) {
                this.mListener.onDrop();
            } else if (actionState == 2) {
                this.mListener.onDrag(viewHolder);
            }
        }
        this.mIsFinishedClearView = false;
        this.mSelectedActionState = actionState;
    }

    public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (this.mListener != null) {
            this.mListener.onClearView(viewHolder);
        }
        this.mIsFinishedClearView = true;
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (!this.mIsLongPressDragEnabled) {
            float topY = ((float) viewHolder.itemView.getTop()) + dY;
            float bottomY = topY + ((float) viewHolder.itemView.getHeight());
            float topBoundaryLine = ((float) viewHolder.itemView.getHeight()) / -2.0f;
            float bottomBoundaryLine = ((float) viewHolder.itemView.getHeight()) / 2.0f;
            if (topY < topBoundaryLine && !this.mIsFinishedClearView) {
                dY = topBoundaryLine;
            } else if (bottomY > ((float) recyclerView.getHeight()) + bottomBoundaryLine) {
                float originDy = dY;
                dY = (((float) recyclerView.getHeight()) - bottomBoundaryLine) - ((float) viewHolder.itemView.getTop());
                if (this.mSelectedActionState == 0 && dY < 0.0f) {
                    dY = originDy;
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
