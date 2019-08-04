package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.support.constraint.Guideline;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.PathInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.stopwatch.C0706R;
import com.sec.android.app.clockpackage.stopwatch.model.ListItem;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater inflater;
    private boolean mAnimate = false;
    private Context mContext;
    private ViewHolder mHolder;
    private int mLayoutID;

    private static class ViewHolder {
        private TextView index;
        private Guideline listStartGuideline;
        private final Context mContext;
        private TextView time;
        private TextView timeDiff;
        private View view;

        private ViewHolder(View v, Context context) {
            this.view = v;
            this.mContext = context;
        }

        private Guideline getListGuideLine() {
            if (this.listStartGuideline == null) {
                this.listStartGuideline = (Guideline) this.view.findViewById(C0706R.id.guide_list_start);
            }
            if (StateUtils.isSplitMode()) {
                TypedValue value = new TypedValue();
                this.mContext.getResources().getValue(C0706R.dimen.stopwatch_split_list_vertical_guide_line, value, true);
                this.listStartGuideline.setGuidelinePercent(value.getFloat());
            }
            return this.listStartGuideline;
        }

        private TextView getIndex() {
            if (this.index == null) {
                this.index = (TextView) this.view.findViewById(C0706R.id.stopwatch_index);
                if (this.mContext.getResources().getConfiguration().orientation == 1) {
                    this.index.setGravity(8388627);
                } else {
                    this.index.setGravity(8388629);
                }
            }
            if (StateUtils.isSplitMode()) {
                TypedValue value = new TypedValue();
                this.mContext.getResources().getValue(C0706R.dimen.stopwatch_split_list_index_percent, value, true);
                ((LayoutParams) this.index.getLayoutParams()).matchConstraintPercentWidth = value.getFloat();
            }
            return this.index;
        }

        private TextView getTime() {
            if (this.time == null) {
                this.time = (TextView) this.view.findViewById(C0706R.id.stopwatch_laptime);
                this.time.setGravity(8388629);
            }
            if (StateUtils.isSplitMode()) {
                TypedValue value = new TypedValue();
                this.mContext.getResources().getValue(C0706R.dimen.stopwatch_split_list_laptime_percent, value, true);
                ((LayoutParams) this.time.getLayoutParams()).matchConstraintPercentWidth = value.getFloat();
            }
            return this.time;
        }

        private TextView getTimeDiff() {
            if (this.timeDiff == null) {
                this.timeDiff = (TextView) this.view.findViewById(C0706R.id.stopwatch_timeDiff);
                this.timeDiff.setGravity(8388629);
            }
            if (StateUtils.isSplitMode()) {
                TypedValue value = new TypedValue();
                this.mContext.getResources().getValue(C0706R.dimen.stopwatch_split_list_timediff_percent, value, true);
                ((LayoutParams) this.timeDiff.getLayoutParams()).matchConstraintPercentWidth = value.getFloat();
            }
            return this.timeDiff;
        }
    }

    public ListAdapter(Context context, int layoutId, WeakReference<ArrayList<ListItem>> objects) {
        super(context, layoutId, (List) objects.get());
        this.mLayoutID = layoutId;
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @SuppressLint({"SetTextI18n"})
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(!((Activity) this.mContext).isInMultiWindowMode() || parent == null || convertView.getWidth() == parent.getMeasuredWidth())) {
            try {
                convertView = this.inflater.inflate(this.mLayoutID, null);
                this.mHolder = new ViewHolder(convertView, this.mContext.getApplicationContext());
                if (convertView != null) {
                    convertView.setTag(this.mHolder);
                }
            } catch (OutOfMemoryError e) {
                Log.secE("ListAdapter", "Exception : " + e.toString());
                Log.secI("StopWatch", "MemoryError");
            } catch (Exception e2) {
                Log.secE("ListAdapter", "Exception : " + e2.toString());
                Log.secI("StopWatch", "inflate exception");
            }
        } else {
            this.mHolder = (ViewHolder) convertView.getTag();
        }
        ListItem item = (ListItem) getItem(position);
        if (item != null) {
            this.mHolder.getListGuideLine();
            this.mHolder.getIndex().setContentDescription(String.format(this.mContext.getApplicationContext().getResources().getString(C0706R.string.stopwatch_lap_talk), new Object[]{Integer.valueOf(Integer.parseInt(item.getIndex()))}));
            this.mHolder.getIndex().setText(item.getIndex());
            this.mHolder.getTime().setText(item.getTime() + item.getMillisecond());
            this.mHolder.getTime().setContentDescription(item.getTimeDescription() + ' ' + this.mContext.getResources().getString(C0706R.string.stopwatch_list_split));
            this.mHolder.getTimeDiff().setText(item.getTimeDiff() + item.getMillisecondDiff());
            this.mHolder.getTimeDiff().setContentDescription(item.getTimeDiffDescription());
        }
        ClockUtils.setLargeTextSize(this.mContext.getApplicationContext(), this.mHolder.getIndex(), (float) this.mContext.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_list_index_time_text_size));
        ClockUtils.setLargeTextSize(this.mContext.getApplicationContext(), this.mHolder.getTime(), (float) this.mContext.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_list_index_time_text_size));
        ClockUtils.setLargeTextSize(this.mContext.getApplicationContext(), this.mHolder.getTimeDiff(), (float) this.mContext.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_list_index_time_text_size));
        if (this.mAnimate) {
            if (position == 0) {
                Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(300);
                fadeIn.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f));
                if (convertView != null) {
                    convertView.startAnimation(fadeIn);
                }
            } else {
                TranslateAnimation transAnim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                transAnim.setDuration(300);
                transAnim.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.2f, 1.0f));
                if (convertView != null) {
                    convertView.startAnimation(transAnim);
                }
            }
        }
        return convertView;
    }

    public void setAnimate(boolean mAnimate) {
        this.mAnimate = mAnimate;
    }
}
