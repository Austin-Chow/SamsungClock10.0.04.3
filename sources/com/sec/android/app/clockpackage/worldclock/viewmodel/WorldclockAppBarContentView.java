package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import java.util.Date;
import java.util.TimeZone;

public class WorldclockAppBarContentView extends ConstraintLayout {
    private float mAlphaRange;
    private float mAlphaStart;
    private TextClock mAmPm;
    private Context mContext;
    private View mOverlapSpace;
    private TextView mSpaceCharacter;
    private TextClock mTime;
    private TextClock mTimeForLandscape;
    private TextView mTimeZone;
    private TextView mTitleLocalTimeText;

    public WorldclockAppBarContentView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public WorldclockAppBarContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public WorldclockAppBarContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    public void initView() {
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(C0836R.layout.worldclock_app_bar_content_layout, this, true);
        this.mAlphaStart = getResources().getDimension(C0836R.dimen.worldclock_extended_appbar_alpha_start);
        this.mAlphaRange = getResources().getDimension(C0836R.dimen.worldclock_extended_appbar_alpha_range);
        this.mTime = (TextClock) findViewById(C0836R.id.worldclock_current_local_time);
        this.mTimeForLandscape = (TextClock) findViewById(C0836R.id.worldclock_current_local_time_for_landscape);
        this.mSpaceCharacter = (TextView) findViewById(C0836R.id.worldclock_space_character_for_landscape);
        this.mOverlapSpace = findViewById(C0836R.id.worldclock_overlap_space);
        this.mTitleLocalTimeText = (TextView) findViewById(C0836R.id.worldclock_current_local_text);
        this.mTimeZone = (TextView) findViewById(C0836R.id.worldclock_current_local_timezone_info);
        updateTimeZoneInfo();
        if (StateUtils.isLeftAmPm()) {
            findViewById(C0836R.id.worldclock_current_local_ampm).setVisibility(8);
            this.mAmPm = (TextClock) findViewById(C0836R.id.worldclock_current_local_ampm_left);
        } else {
            findViewById(C0836R.id.worldclock_current_local_ampm_left).setVisibility(8);
            this.mAmPm = (TextClock) findViewById(C0836R.id.worldclock_current_local_ampm);
        }
        this.mAmPm.setVisibility(this.mAmPm.is24HourModeEnabled() ? 8 : 0);
        updateTextSize();
    }

    public void updateTextSize() {
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mAmPm, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_app_bar_title_text_size));
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mTime, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_app_bar_title_time_size));
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mTimeForLandscape, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_app_bar_title_text_size));
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mSpaceCharacter, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_app_bar_title_text_size));
        ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mTitleLocalTimeText, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_app_bar_title_text_size));
        if (StateUtils.isScreenDp(this.mContext, 512)) {
            ClockUtils.setLargeTextSizeForAppBar(this.mContext, this.mTimeZone, (float) getResources().getDimensionPixelSize(C0836R.dimen.worldclock_app_bar_sub_title_text_size));
        }
    }

    public void updateTimeZoneInfo() {
        TimeZone tz = TimeZone.getDefault();
        this.mTimeZone.setText(tz.getDisplayName(tz.inDaylightTime(new Date(System.currentTimeMillis())), 1));
    }

    public void updateColor(View toolbarTitle, int layoutPosition, boolean isCollapsed) {
        double titleAlpha = (double) (255.0f - ((100.0f / this.mAlphaRange) * (((float) layoutPosition) - this.mAlphaStart)));
        double actionModeToolbarTitleAlpha = 255.0d - (2.0d * titleAlpha);
        boolean canUpdateActionModeToolbarTitle = false;
        if (titleAlpha < 0.0d) {
            actionModeToolbarTitleAlpha = 255.0d - (2.0d * 0.0d);
            canUpdateActionModeToolbarTitle = true;
        } else if (titleAlpha > 255.0d) {
            actionModeToolbarTitleAlpha = 0.0d;
            canUpdateActionModeToolbarTitle = true;
        }
        if (isCollapsed) {
            actionModeToolbarTitleAlpha = 255.0d;
        }
        if (actionModeToolbarTitleAlpha >= 0.0d && actionModeToolbarTitleAlpha <= 255.0d) {
            canUpdateActionModeToolbarTitle = true;
        }
        if (canUpdateActionModeToolbarTitle && toolbarTitle != null) {
            TextView selectionTitle = (TextView) toolbarTitle.findViewById(C0836R.id.selection_title);
            selectionTitle.setTextColor(ColorUtils.setAlphaComponent(selectionTitle.getCurrentTextColor(), (int) actionModeToolbarTitleAlpha));
        }
    }

    public void onActionModeUpdate(int selectedCount) {
        String title;
        if (selectedCount == 0) {
            title = getResources().getString(C0836R.string.select_cities);
        } else {
            title = getResources().getString(C0836R.string.pd_selected, new Object[]{Integer.valueOf(selectedCount)});
        }
        this.mAmPm.setVisibility(8);
        this.mTime.setVisibility(8);
        this.mTimeZone.setVisibility(8);
        this.mTimeForLandscape.setVisibility(8);
        this.mSpaceCharacter.setVisibility(8);
        this.mOverlapSpace.setVisibility(8);
        this.mTitleLocalTimeText.setText(title);
    }

    public void onActionModeFinished() {
        String title = getResources().getString(C0836R.string.local_time);
        this.mTimeZone.setVisibility(0);
        this.mTitleLocalTimeText.setText(title);
        setOrientation();
    }

    public void setOrientation() {
        if (StateUtils.isScreenDp(this.mContext, 600) && getResources().getConfiguration().orientation == 2) {
            this.mTimeForLandscape.setVisibility(0);
            this.mSpaceCharacter.setVisibility(0);
            this.mOverlapSpace.setVisibility(8);
            this.mTime.setVisibility(8);
            this.mAmPm.setVisibility(8);
            return;
        }
        this.mTimeForLandscape.setVisibility(8);
        this.mSpaceCharacter.setVisibility(8);
        this.mOverlapSpace.setVisibility(0);
        this.mTime.setVisibility(0);
        this.mAmPm.setVisibility(0);
    }
}
