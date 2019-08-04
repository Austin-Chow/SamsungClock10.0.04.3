package com.sec.android.app.clockpackage.worldclock.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;

public class WorldclockMainListItemDecoration extends ItemDecoration {
    private final int mDividerSize;

    @SuppressLint({"InflateParams"})
    public WorldclockMainListItemDecoration(Context context) {
        this.mDividerSize = context.getResources().getDimensionPixelOffset(C0836R.dimen.clock_list_divider_height);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (!(parent.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }
        if (StateUtils.isRtl()) {
            if (parent.getChildAdapterPosition(view) % 2 == 0) {
                outRect.left = this.mDividerSize / 2;
            } else {
                outRect.right = this.mDividerSize / 2;
            }
        } else if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right = this.mDividerSize / 2;
        } else {
            outRect.left = this.mDividerSize / 2;
        }
    }
}
