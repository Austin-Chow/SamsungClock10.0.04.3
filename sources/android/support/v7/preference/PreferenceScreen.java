package android.support.v7.preference;

import android.content.Context;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.PreferenceManager.OnNavigateToScreenListener;
import android.util.AttributeSet;

public final class PreferenceScreen extends PreferenceGroup {
    private boolean mShouldUseGeneratedIds = true;

    public PreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs, TypedArrayUtils.getAttr(context, C0263R.attr.preferenceScreenStyle, 16842891));
    }

    protected void onClick() {
        if (getIntent() == null && getFragment() == null && getPreferenceCount() != 0) {
            OnNavigateToScreenListener listener = getPreferenceManager().getOnNavigateToScreenListener();
            if (listener != null) {
                listener.onNavigateToScreen(this);
            }
        }
    }

    protected boolean isOnSameScreenAsChildren() {
        return false;
    }

    public boolean shouldUseGeneratedIds() {
        return this.mShouldUseGeneratedIds;
    }
}
