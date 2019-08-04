package android.support.v7.preference;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;

public class PreferenceViewHolder extends ViewHolder {
    private final SparseArray<View> mCachedViews = new SparseArray(4);
    private boolean mDividerAllowedAbove;
    private boolean mDividerAllowedBelow;
    boolean mDrawBackground = false;
    int mDrawCorners;
    boolean mSubheaderRound = false;

    PreferenceViewHolder(View itemView) {
        super(itemView);
        this.mCachedViews.put(16908310, itemView.findViewById(16908310));
        this.mCachedViews.put(16908304, itemView.findViewById(16908304));
        this.mCachedViews.put(16908294, itemView.findViewById(16908294));
        this.mCachedViews.put(C0263R.id.icon_frame, itemView.findViewById(C0263R.id.icon_frame));
        this.mCachedViews.put(16908350, itemView.findViewById(16908350));
    }

    public View findViewById(int id) {
        View cachedView = (View) this.mCachedViews.get(id);
        if (cachedView != null) {
            return cachedView;
        }
        View v = this.itemView.findViewById(id);
        if (v != null) {
            this.mCachedViews.put(id, v);
        }
        return v;
    }

    void seslSetPreferenceBackgroundType(boolean draw, int corners, boolean subheaderRound) {
        this.mDrawBackground = draw;
        this.mDrawCorners = corners;
        this.mSubheaderRound = subheaderRound;
    }

    public boolean isDividerAllowedAbove() {
        return this.mDividerAllowedAbove;
    }

    public void setDividerAllowedAbove(boolean allowed) {
        this.mDividerAllowedAbove = allowed;
    }

    public boolean isDividerAllowedBelow() {
        return this.mDividerAllowedBelow;
    }

    public void setDividerAllowedBelow(boolean allowed) {
        this.mDividerAllowedBelow = allowed;
    }

    public int seslGetDrawCorners() {
        return this.mDrawCorners;
    }

    public boolean seslIsDrawSubheaderRound() {
        return this.mSubheaderRound;
    }
}
