package com.sec.android.app.clockpackage.alarm.viewmodel;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseIntArray;

public abstract class AlarmCursorRecyclerViewAdapter<VH extends ViewHolder> extends Adapter<VH> {
    private Cursor mCursor;
    private DataSetObserver mDataSetObserver;
    private boolean mDataValid = false;
    private int mRowIdColumn = -1;

    private class NotifyingDataSetObserver extends DataSetObserver {
        private NotifyingDataSetObserver() {
        }

        public void onChanged() {
            super.onChanged();
            AlarmCursorRecyclerViewAdapter.this.mDataValid = true;
        }

        public void onInvalidated() {
            super.onInvalidated();
            AlarmCursorRecyclerViewAdapter.this.mDataValid = false;
        }
    }

    protected abstract void onBindViewHolder(VH vh, Cursor cursor);

    AlarmCursorRecyclerViewAdapter(Cursor cursor) {
        this.mCursor = cursor;
        registerObserver();
    }

    private void registerObserver() {
        if (this.mCursor != null) {
            if (this.mDataSetObserver == null) {
                this.mDataSetObserver = new NotifyingDataSetObserver();
            }
            this.mCursor.registerDataSetObserver(this.mDataSetObserver);
            this.mRowIdColumn = this.mCursor.getColumnIndex("_id");
            this.mDataValid = true;
        }
    }

    public Cursor getCursor() {
        return this.mCursor;
    }

    public int getItemCount() {
        if (!this.mDataValid || this.mCursor == null) {
            return 0;
        }
        return this.mCursor.getCount();
    }

    public long getItemId(int position) {
        if (this.mDataValid && this.mCursor != null && this.mCursor.moveToPosition(position)) {
            return this.mCursor.getLong(this.mRowIdColumn);
        }
        return 0;
    }

    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public void onBindViewHolder(VH viewHolder, int position) {
        if (!this.mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        } else if (this.mCursor.moveToPosition(position)) {
            onBindViewHolder((ViewHolder) viewHolder, this.mCursor);
        } else {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
    }

    public void changeCursor(Cursor cursor) {
        if (this.mCursor == null) {
            swapCursor(cursor, null);
            return;
        }
        SparseIntArray changes = null;
        if (!(cursor == null || cursor == this.mCursor)) {
            changes = diffCursors(this.mCursor, cursor);
        }
        Cursor old = swapCursor(cursor, changes);
        if (old != null) {
            old.close();
        }
    }

    private SparseIntArray diffCursors(Cursor oldCursor, Cursor newCursor) {
        SparseIntArray changedOrInserted = getChangeOrInsertRecords(oldCursor, newCursor);
        int changedOrInsertedSize = changedOrInserted.size();
        if (changedOrInserted.get(-1) == 1) {
            return changedOrInserted;
        }
        SparseIntArray deleted = getDeletedRecords(oldCursor, newCursor);
        int deletedSize = deleted.size();
        if (deleted.get(-1) == 1) {
            return deleted;
        }
        int i;
        SparseIntArray changes = new SparseIntArray(changedOrInsertedSize + deletedSize);
        for (i = 0; i < changedOrInsertedSize; i++) {
            changes.put(changedOrInserted.keyAt(i), changedOrInserted.valueAt(i));
        }
        for (i = 0; i < deletedSize; i++) {
            changes.put(deleted.keyAt(i), deleted.valueAt(i));
        }
        return changes;
    }

    private SparseIntArray getDeletedRecords(Cursor oldCursor, Cursor newCursor) {
        SparseIntArray changes = new SparseIntArray();
        int newCursorPosition = newCursor.getPosition();
        if (oldCursor.moveToFirst()) {
            int cursorIndex = 0;
            do {
                if (newCursor.moveToFirst()) {
                    boolean oldRecordFound = false;
                    while (oldCursor.getInt(this.mRowIdColumn) != newCursor.getInt(this.mRowIdColumn)) {
                        if (!newCursor.moveToNext()) {
                            break;
                        }
                    }
                    oldRecordFound = true;
                    if (!oldRecordFound) {
                        changes.put(cursorIndex, 2);
                    }
                    cursorIndex++;
                }
            } while (oldCursor.moveToNext());
        } else {
            changes.put(-1, 1);
        }
        newCursor.moveToPosition(newCursorPosition);
        return changes;
    }

    private SparseIntArray getChangeOrInsertRecords(Cursor oldCursor, Cursor newCursor) {
        SparseIntArray changes = new SparseIntArray();
        int newCursorPosition = newCursor.getPosition();
        if (newCursor.moveToFirst()) {
            int cursorIndex = 0;
            while (oldCursor.moveToFirst()) {
                boolean newRecordFound = false;
                while (oldCursor.getInt(this.mRowIdColumn) != newCursor.getInt(this.mRowIdColumn)) {
                    if (!oldCursor.moveToNext()) {
                        break;
                    }
                }
                newRecordFound = true;
                if (oldCursor.getLong(3) != newCursor.getLong(3) || oldCursor.getInt(4) != newCursor.getInt(4) || oldCursor.getInt(1) != newCursor.getInt(1) || oldCursor.getInt(5) != newCursor.getInt(5) || !oldCursor.getString(20).contentEquals(newCursor.getString(20)) || oldCursor.getInt(7) != newCursor.getInt(7) || oldCursor.getInt(11) != newCursor.getInt(11)) {
                    changes.put(cursorIndex, 3);
                }
                if (!newRecordFound) {
                    changes.put(cursorIndex, 1);
                }
                cursorIndex++;
                if (!newCursor.moveToNext()) {
                    break;
                }
            }
            changes.put(-1, 1);
        } else {
            changes.put(-1, 2);
        }
        newCursor.moveToPosition(newCursorPosition);
        return changes;
    }

    private Cursor swapCursor(Cursor newCursor, SparseIntArray changes) {
        if (newCursor == this.mCursor) {
            return null;
        }
        Cursor oldCursor = this.mCursor;
        if (!(oldCursor == null || this.mDataSetObserver == null)) {
            oldCursor.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.mCursor = newCursor;
        if (this.mCursor != null) {
            registerObserver();
        } else {
            this.mRowIdColumn = -1;
            this.mDataValid = false;
        }
        if (changes != null) {
            if (changes.get(-1) == 1) {
                notifyItemRangeInserted(0, newCursor.getCount());
                return oldCursor;
            } else if (changes.get(-1) == 2) {
                notifyItemRangeRemoved(0, oldCursor.getCount());
                return oldCursor;
            } else {
                int startPos = -1;
                int deleteCount = 0;
                int totalDeleteCount = 0;
                int i = 0;
                while (i < changes.size()) {
                    switch (changes.valueAt(i)) {
                        case 1:
                            notifyItemInserted(changes.keyAt(i));
                            break;
                        case 2:
                            if (changes.size() > i + 1 && changes.valueAt(i + 1) == 2 && changes.valueAt(i) + 1 == changes.valueAt(i + 1)) {
                                if (startPos == -1) {
                                    startPos = changes.valueAt(i);
                                }
                                deleteCount++;
                            } else if (startPos >= 0) {
                                notifyItemRangeRemoved(startPos, startPos + deleteCount);
                                startPos = -1;
                                deleteCount = 0;
                            } else {
                                notifyItemRemoved(changes.keyAt(i) - totalDeleteCount);
                            }
                            totalDeleteCount++;
                            break;
                        case 3:
                            notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }
                    i++;
                }
                return oldCursor;
            }
        } else if (this.mCursor == null) {
            return oldCursor;
        } else {
            notifyItemRangeInserted(0, this.mCursor.getCount());
            return oldCursor;
        }
    }
}
