package android.support.v7.widget;

import android.os.Bundle;
import java.util.List;

public class SeslArrayIndexer extends SeslAbsIndexer {
    private final boolean DEBUG = false;
    private final String TAG = "SeslArrayIndexer";
    protected List<String> mData;

    public SeslArrayIndexer(List<String> listData, CharSequence indexCharacters) {
        super(indexCharacters);
        this.mData = listData;
    }

    protected int getItemCount() {
        return this.mData.size();
    }

    protected String getItemAt(int pos) {
        return (String) this.mData.get(pos);
    }

    protected Bundle getBundle() {
        return null;
    }

    protected boolean isDataToBeIndexedAvailable() {
        return getItemCount() > 0;
    }
}
