package android.support.v7.widget;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class SeslCursorIndexer extends SeslAbsIndexer {
    public static final String EXTRA_INDEX_COUNTS = "indexscroll_index_counts";
    public static final String EXTRA_INDEX_TITLES = "indexscroll_index_titles";
    private final boolean DEBUG = true;
    private final String TAG = "SeslCursorIndexer";
    protected int mColumnIndex;
    protected Cursor mCursor;
    protected int mSavedCursorPos;

    public SeslCursorIndexer(Cursor cursor, int sortedColumnIndex, CharSequence indexCharacters) {
        super(indexCharacters);
        this.mCursor = cursor;
        this.mColumnIndex = sortedColumnIndex;
        Log.d("SeslCursorIndexer", "SeslCursorIndexer constructor");
        if (sortedColumnIndex < 0) {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            Log.w("SeslCursorIndexer", "SeslCursorIndexer() called with " + sortedColumnIndex, e);
        }
    }

    public SeslCursorIndexer(Cursor cursor, int sortedColumnIndex, String[] indexCharacters, int aLangIndex) {
        super(indexCharacters, aLangIndex);
        this.mCursor = cursor;
        this.mColumnIndex = sortedColumnIndex;
        Log.d("SeslCursorIndexer", "SeslCursorIndexer constructor");
        if (sortedColumnIndex < 0) {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            Log.w("SeslCursorIndexer", "SeslCursorIndexer() called with " + sortedColumnIndex, e);
        }
    }

    protected boolean isDataToBeIndexedAvailable() {
        return getItemCount() > 0 && !this.mCursor.isClosed();
    }

    protected String getItemAt(int pos) {
        String str = null;
        if (this.mCursor.isClosed()) {
            Log.d("SeslCursorIndexer", "SeslCursorIndexer getItemCount : mCursor is closed.");
        } else {
            if (this.mColumnIndex < 0) {
                Log.d("SeslCursorIndexer", "getItemAt() mColumnIndex : " + this.mColumnIndex);
            }
            this.mCursor.moveToPosition(pos);
            try {
                str = this.mCursor.getString(this.mColumnIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    protected int getItemCount() {
        if (!this.mCursor.isClosed()) {
            return this.mCursor.getCount();
        }
        Log.d("SeslCursorIndexer", "SeslCursorIndexer getItemCount : mCursor is closed.");
        return 0;
    }

    protected Bundle getBundle() {
        Log.d("SeslCursorIndexer", "SemCursorIndexer getBundle : Bundle was used by Indexer");
        return this.mCursor.getExtras();
    }

    void onBeginTransaction() {
        this.mSavedCursorPos = this.mCursor.getPosition();
        Log.d("SeslCursorIndexer", "SeslCursorIndexer.onBeginTransaction() : Current cursor pos to save is : " + this.mSavedCursorPos);
    }

    void onEndTransaction() {
        Log.d("SeslCursorIndexer", "SeslCursorIndexer.onEndTransaction() : Saved cursor pos to restore  is : " + this.mSavedCursorPos);
        this.mCursor.moveToPosition(this.mSavedCursorPos);
    }

    public void setProfileItemsCount(int count) {
        setProfileItem(count);
    }

    public void setFavoriteItemsCount(int count) {
        setFavoriteItem(count);
    }

    public void setGroupItemsCount(int count) {
        setGroupItem(count);
    }

    public void setMiscItemsCount(int count) {
        setDigitItem(count);
    }
}
