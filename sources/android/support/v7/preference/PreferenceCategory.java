package android.support.v7.preference;

import android.content.Context;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class PreferenceCategory extends PreferenceGroup {
    private String mHeader;

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mHeader = "Header";
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCategory(Context context, AttributeSet attrs) {
        Throwable e;
        this(context, attrs, TypedArrayUtils.getAttr(context, C0263R.attr.preferenceCategoryStyle, 16842892));
        try {
            this.mHeader = context.getString(C0263R.string.sesl_preferencecategory_added_title);
            return;
        } catch (Exception e2) {
            e = e2;
        } catch (NoSuchFieldError e3) {
            e = e3;
        }
        Log.d("PreferenceCategory", "Can not find the string. Please updates latest sesl-appcompat library, ", e);
    }

    protected boolean onPrepareAddPreference(Preference preference) {
        if (!(preference instanceof PreferenceCategory)) {
            return super.onPrepareAddPreference(preference);
        }
        throw new IllegalArgumentException("Cannot add a PreferenceCategory directly to a PreferenceCategory");
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean shouldDisableDependents() {
        return !super.isEnabled();
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(16908310);
        if (titleView != null) {
            titleView.setContentDescription(titleView.getText().toString() + ", " + this.mHeader);
        }
        if (this.mIsSolidRoundedCorner && titleView != null) {
            titleView.setBackgroundColor(this.mSubheaderColor);
        }
    }
}
