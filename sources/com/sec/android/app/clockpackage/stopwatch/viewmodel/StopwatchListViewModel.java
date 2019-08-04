package com.sec.android.app.clockpackage.stopwatch.viewmodel;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.samsung.android.content.clipboard.SemClipboardManager;
import com.samsung.android.content.clipboard.data.SemTextClipData;
import com.sec.android.app.clockpackage.common.feature.Feature;
import com.sec.android.app.clockpackage.common.util.ClockUtils;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.stopwatch.C0706R;
import com.sec.android.app.clockpackage.stopwatch.callback.StopwatchListViewModelListener;
import com.sec.android.app.clockpackage.stopwatch.model.ListItem;
import com.sec.android.app.clockpackage.stopwatch.model.StopwatchData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class StopwatchListViewModel {
    private OnScrollListener listScrollListener = new C07247();
    private Activity mActivity;
    private ArrayAdapter<ListItem> mArrayAdapter;
    private PopupWindow mCopyPopupWindow;
    private View mFragmentView;
    private Handler mHandler = new Handler();
    private WeakReference<ArrayList<ListItem>> mItems;
    private ListView mLapsList;
    private int mLastScrollPosition = 0;
    private FrameLayout mListLayout;
    private ViewStub mListStub = null;
    private int mListViewHeight;
    private StopwatchListViewModelListener mStopwatchListViewModelListener = null;
    private StopwatchManager mStopwatchManager;
    private LayoutParams mStubLayoutParam;

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$1 */
    class C07181 implements Runnable {
        C07181() {
        }

        public void run() {
            StopwatchListViewModel.this.mArrayAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$2 */
    class C07192 implements OnItemLongClickListener {
        C07192() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            StopwatchListViewModel.this.stopwatchListCopy();
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$3 */
    class C07203 implements OnTouchListener {
        C07203() {
        }

        public boolean onTouch(View view, MotionEvent event) {
            if (event.getButtonState() == 2 && event.getAction() == 0) {
                StopwatchListViewModel.this.stopwatchListCopy();
                view.performClick();
            }
            return false;
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$4 */
    class C07214 implements OnClickListener {
        C07214() {
        }

        public void onClick(View v) {
            SemClipboardManager semClipboard = (SemClipboardManager) StopwatchListViewModel.this.mActivity.getSystemService("semclipboard");
            String strStopwatchList = StopwatchListViewModel.this.getCopiedListItems(StopwatchListViewModel.this.mStopwatchManager.getListItems());
            SemTextClipData clipText = new SemTextClipData();
            clipText.setText(strStopwatchList);
            if (semClipboard.isEnabled()) {
                semClipboard.addClip(StopwatchListViewModel.this.mActivity.getApplicationContext(), clipText, null);
            } else {
                ((ClipboardManager) StopwatchListViewModel.this.mActivity.getApplicationContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", strStopwatchList));
            }
            StopwatchListViewModel.this.mCopyPopupWindow.dismiss();
            ClockUtils.insertSaLog("120", "1302");
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$5 */
    class C07225 implements Runnable {
        C07225() {
        }

        public void run() {
            StopwatchListViewModel.this.mArrayAdapter.notifyDataSetChanged();
            if (StopwatchListViewModel.this.mLapsList != null) {
                StopwatchListViewModel.this.mLapsList.setSelection(0);
                StopwatchListViewModel.this.mLastScrollPosition = 0;
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$6 */
    class C07236 implements Runnable {
        C07236() {
        }

        public void run() {
            if (StopwatchListViewModel.this.mListLayout != null) {
                StopwatchListViewModel.this.mListLayout.setVisibility(0);
            }
        }
    }

    /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$7 */
    class C07247 implements OnScrollListener {
        C07247() {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState != 0) {
                ((ListAdapter) StopwatchListViewModel.this.mArrayAdapter).setAnimate(false);
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (StopwatchListViewModel.this.mArrayAdapter != null) {
                ((ListAdapter) StopwatchListViewModel.this.mArrayAdapter).setAnimate(false);
            }
            if (StopwatchListViewModel.this.mLapsList != null) {
                StopwatchListViewModel.this.mLastScrollPosition = firstVisibleItem;
            }
        }
    }

    private class StopwatchDataSetObserver extends DataSetObserver {

        /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$StopwatchDataSetObserver$1 */
        class C07251 implements Runnable {
            C07251() {
            }

            public void run() {
                if (StopwatchListViewModel.this.mLapsList != null) {
                    StopwatchListViewModel.this.mLapsList.setSelection(0);
                }
            }
        }

        /* renamed from: com.sec.android.app.clockpackage.stopwatch.viewmodel.StopwatchListViewModel$StopwatchDataSetObserver$2 */
        class C07262 implements Runnable {
            C07262() {
            }

            public void run() {
                StopwatchListViewModel.this.mStopwatchListViewModelListener.onExceededMaxCount();
            }
        }

        private StopwatchDataSetObserver() {
        }

        public void onChanged() {
            super.onChanged();
            Log.secD("StopwatchListViewModel", "onChanged");
            if (StopwatchData.getStopwatchState() != 3) {
                StopwatchData.setLapCount(StopwatchListViewModel.this.mStopwatchManager.getListItems().size());
            }
            StopwatchListViewModel.this.mActivity.runOnUiThread(new C07251());
            if (StopwatchData.getStopwatchState() == 1 && StopwatchData.getLapCount() >= 999) {
                StopwatchListViewModel.this.mHandler.postDelayed(new C07262(), 500);
            }
        }
    }

    StopwatchListViewModel(Activity activity, View fragmentView, StopwatchManager stopwatchManager, StopwatchListViewModelListener stopwatchListViewModelListener) {
        this.mActivity = activity;
        this.mFragmentView = fragmentView;
        this.mStopwatchManager = stopwatchManager;
        this.mStopwatchListViewModelListener = stopwatchListViewModelListener;
    }

    public void inflateListLayout() {
        Log.secD("StopwatchListViewModel", "inflateListLayout");
        if (this.mListLayout == null) {
            if (this.mListStub == null) {
                this.mListStub = (ViewStub) this.mFragmentView.findViewById(C0706R.id.list_stub);
            }
            View inflated = this.mListStub.inflate();
            this.mLapsList = (ListView) inflated.findViewById(C0706R.id.stopwatch_watchlist);
            this.mListLayout = (FrameLayout) inflated.findViewById(C0706R.id.stopwatch_list_layout);
            RelativeLayout stubLayout = (RelativeLayout) this.mFragmentView.findViewById(C0706R.id.list_stub_layout);
            if (stubLayout != null) {
                this.mStubLayoutParam = (LayoutParams) stubLayout.getLayoutParams();
            }
            initList();
        }
        this.mListLayout.setVisibility(0);
    }

    private void initList() {
        Log.secD("StopwatchListViewModel", "initList()");
        this.mArrayAdapter = null;
        if (this.mLapsList != null) {
            this.mLapsList.setOnScrollListener(this.listScrollListener);
            this.mItems = new WeakReference(this.mStopwatchManager.getListItems());
            this.mArrayAdapter = new ListAdapter(this.mActivity, C0706R.layout.stopwatch_list_item, this.mItems);
            this.mLapsList.setAdapter(this.mArrayAdapter);
            this.mLapsList.setDivider(null);
            this.mActivity.runOnUiThread(new C07181());
            this.mArrayAdapter.registerDataSetObserver(new StopwatchDataSetObserver());
            ((ListAdapter) this.mArrayAdapter).setAnimate(false);
            this.mLapsList.setOnItemLongClickListener(new C07192());
            if (StateUtils.isContextInDexMode(this.mActivity)) {
                this.mLapsList.setOnTouchListener(new C07203());
            }
        }
    }

    private void stopwatchListCopy() {
        Resources res = this.mActivity.getResources();
        Configuration config = res.getConfiguration();
        int buttonHeight = res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_height);
        int verticalLocation = this.mStopwatchListViewModelListener.onGetVerticalLocation();
        int horizontalLocation = (ClockUtils.convertDpToPixel(this.mActivity, config.screenWidthDp) / 2) - (res.getDimensionPixelSize(C0706R.dimen.stopwatch_list_copypopup_layout_width) / 2);
        if (StateUtils.isRtl()) {
            horizontalLocation = -((ClockUtils.convertDpToPixel(this.mActivity, config.screenWidthDp) / 2) + (res.getDimensionPixelSize(C0706R.dimen.stopwatch_list_copypopup_layout_width) / 2));
        }
        View popupView = ((LayoutInflater) this.mActivity.getBaseContext().getSystemService("layout_inflater")).inflate(C0706R.layout.stopwatch_copypoup, null);
        this.mCopyPopupWindow = new PopupWindow(popupView, -2, -2);
        this.mCopyPopupWindow.setOutsideTouchable(true);
        ((RelativeLayout) popupView.findViewById(C0706R.id.copy_btn_layout)).setOnClickListener(new C07214());
        if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
            if (this.mCopyPopupWindow != null && this.mLapsList != null) {
                this.mCopyPopupWindow.showAsDropDown(this.mLapsList, ((horizontalLocation / 2) - res.getDimensionPixelSize(C0706R.dimen.stopwatch_landscape_listview_margin)) - res.getDimensionPixelSize(C0706R.dimen.stopwatch_landscape_timeview_margin), ((config.screenHeightDp / 2) + (res.getDimensionPixelSize(C0706R.dimen.stopwatch_list_copypopup_layout_height) / 2)) + (ClockUtils.getActionBarHeight(this.mActivity.getApplicationContext()) / 2));
            }
        } else if (StateUtils.isContextInDexMode(this.mActivity) || (Feature.isTablet(this.mActivity) && this.mActivity.getResources().getConfiguration().orientation == 2)) {
            this.mCopyPopupWindow.showAtLocation(this.mLapsList, 17, 0, ((this.mListViewHeight / 2) - (res.getDimensionPixelSize(C0706R.dimen.stopwatch_list_copypopup_layout_height) / 2)) - res.getDimensionPixelSize(C0706R.dimen.stopwatch_button_margin_top));
        } else if (this.mCopyPopupWindow != null && this.mLapsList != null) {
            this.mCopyPopupWindow.showAsDropDown(this.mLapsList, horizontalLocation, -(verticalLocation + buttonHeight));
        }
    }

    public void getListHeight(int height) {
        this.mListViewHeight = height;
    }

    private String getCopiedListItems(ArrayList<ListItem> lapList) {
        Log.secD("StopwatchListViewModel", "get Copied ListItems");
        StringBuilder b = new StringBuilder();
        int lapsNum = lapList.size();
        if (lapsNum == 0) {
            return b.toString();
        }
        for (int i = lapsNum - 1; i >= 0; i--) {
            String index = ((ListItem) lapList.get(i)).getIndex();
            b.append(String.format("%4s", new Object[]{index}));
            String laptime = ((ListItem) lapList.get(i)).getTime() + ((ListItem) lapList.get(i)).getMillisecond();
            b.append(String.format("%14s", new Object[]{laptime}));
            String timediff = ((ListItem) lapList.get(i)).getTimeDiff() + ((ListItem) lapList.get(i)).getMillisecondDiff();
            b.append(String.format("%14s", new Object[]{timediff}));
            b.append('\n');
        }
        return b.toString();
    }

    public void setLapList() {
        Log.secD("StopwatchListViewModel", "setLapList()");
        this.mStopwatchManager.setListItems(this.mStopwatchManager.getListItems());
        this.mItems = new WeakReference(this.mStopwatchManager.getListItems());
        this.mArrayAdapter = new ListAdapter(this.mActivity, C0706R.layout.stopwatch_list_item, this.mItems);
        if (this.mLapsList != null) {
            this.mLapsList.setAdapter(this.mArrayAdapter);
            this.mArrayAdapter.registerDataSetObserver(new StopwatchDataSetObserver());
        }
    }

    public void dismissPopupWindow() {
        if (this.mCopyPopupWindow != null) {
            this.mCopyPopupWindow.dismiss();
        }
    }

    public boolean isPopupWindowShowing() {
        return this.mCopyPopupWindow != null && this.mCopyPopupWindow.isShowing();
    }

    public void updateStopwatchListPosition() {
        if (this.mStubLayoutParam != null) {
            if (StopwatchUtils.checkForLandscape(this.mActivity.getApplicationContext())) {
                this.mStubLayoutParam.removeRule(3);
                this.mStubLayoutParam.addRule(17, C0706R.id.stopwatch_timeview_layout);
                this.mStubLayoutParam.setMarginEnd(this.mActivity.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_landscape_listview_margin));
            } else {
                this.mStubLayoutParam.removeRule(17);
                this.mStubLayoutParam.addRule(3, C0706R.id.stopwatch_timeview_layout);
                this.mStubLayoutParam.setMarginEnd(0);
            }
            if (ClockUtils.convertDpToPixel(this.mActivity, this.mActivity.getResources().getConfiguration().screenWidthDp) >= this.mActivity.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_tablet_max_screen_width)) {
                this.mStubLayoutParam.width = this.mActivity.getResources().getDimensionPixelSize(C0706R.dimen.stopwatch_list_max_width);
            }
        }
    }

    public void addLapList() {
        if (this.mArrayAdapter != null) {
            ((ListAdapter) this.mArrayAdapter).setAnimate(true);
            this.mActivity.runOnUiThread(new C07225());
        }
        if (this.mLapsList != null) {
            if (StopwatchData.getLapCount() > 1) {
                this.mLapsList.setVerticalScrollBarEnabled(true);
            } else {
                this.mLapsList.setVerticalScrollBarEnabled(false);
            }
        }
        this.mHandler.postDelayed(new C07236(), 300);
        if (this.mLapsList != null) {
            this.mLapsList.announceForAccessibility(String.format(this.mActivity.getApplicationContext().getResources().getString(C0706R.string.stopwatch_lap_talk), new Object[]{Integer.valueOf(StopwatchData.getLapCount())}) + ' ' + ((ListItem) this.mStopwatchManager.getListItems().get(0)).getTimeDescription() + ' ' + this.mActivity.getApplicationContext().getResources().getString(C0706R.string.stopwatch_list_split) + ' ' + ((ListItem) this.mStopwatchManager.getListItems().get(0)).getTimeDiffDescription());
        }
    }

    public FrameLayout getListLayout() {
        return this.mListLayout;
    }

    public ListView getLapsList() {
        return this.mLapsList;
    }

    public ArrayAdapter<ListItem> getArrayAdapter() {
        return this.mArrayAdapter;
    }

    public int getLastScrollPosition() {
        return this.mLastScrollPosition;
    }

    public void releaseInstance() {
        if (this.mArrayAdapter != null) {
            this.mArrayAdapter = null;
        }
        if (this.mLapsList != null) {
            this.mLapsList.setOnScrollListener(null);
            this.mLapsList.setAdapter(null);
            this.mLapsList = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        if (this.mItems != null) {
            this.mItems.clear();
            this.mItems = null;
        }
        if (this.mListLayout != null) {
            this.mListLayout.removeAllViews();
            this.mListLayout.destroyDrawingCache();
            this.mListLayout = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        this.mListStub = null;
    }
}
