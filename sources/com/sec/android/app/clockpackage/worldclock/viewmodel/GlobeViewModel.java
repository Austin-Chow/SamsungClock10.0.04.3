package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.sec.android.app.clockpackage.common.callback.OnSingleClickListener;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.common.util.StateUtils;
import com.sec.android.app.clockpackage.worldclock.C0836R;
import com.sec.android.app.clockpackage.worldclock.callback.GlobeViewModelListener;
import com.sec.android.app.clockpackage.worldclock.callback.SgiPlayerListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.weather.WeatherUrlManager;
import com.sec.android.app.clockpackage.worldclock.weather.WorldclockWeatherUtils;

public class GlobeViewModel extends FrameLayout implements OnTouchListener {
    private final Context mContext;
    private GestureDetectorCompat mGestureDetector;
    private GlobeViewModelListener mGlobeViewModelListener;
    private SearchBarViewModel mSearchBarViewModel;
    private SGIPlayerManager mSgiPlayerManager;
    private FrameLayout mSurfaceFrame;
    private RelativeLayout mWeatherLogoLayout;
    private ImageView mZoomIn;
    private LinearLayout mZoomInOut;
    private ImageView mZoomOut;

    /* renamed from: com.sec.android.app.clockpackage.worldclock.viewmodel.GlobeViewModel$1 */
    class C08571 extends OnSingleClickListener {
        C08571() {
        }

        public void onSingleClick(View v) {
            try {
                Uri uri = new WeatherUrlManager().setViewUri("http://www.weather.com", null);
                Log.secD("GlobeViewModel", "Weather logo onSingleClick() => uri : " + uri);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(uri);
                GlobeViewModel.this.mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.secE("GlobeViewModel", "Weather logo click ActivityNotFoundException :" + e.toString());
            }
        }
    }

    public class GestureListener extends SimpleOnGestureListener {
        public boolean onDown(MotionEvent event) {
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (GlobeViewModel.this.mSgiPlayerManager != null) {
                GlobeViewModel.this.mSgiPlayerManager.processClickEvent(e);
            }
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (GlobeViewModel.this.mSgiPlayerManager != null) {
                GlobeViewModel.this.mSgiPlayerManager.processDoubleTapEvent(e);
            }
            return true;
        }

        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (GlobeViewModel.this.mSgiPlayerManager != null) {
                GlobeViewModel.this.mSgiPlayerManager.processFlingEvent(velocityX, 0.0f, event1.getX(), event1.getY());
            }
            return false;
        }
    }

    public void setGlobeViewModel(SearchBarViewModel searchBarViewModel, GlobeViewModelListener listener) {
        this.mSearchBarViewModel = searchBarViewModel;
        this.mGlobeViewModelListener = listener;
    }

    public GlobeViewModel(Context context) {
        super(context);
        this.mContext = context;
    }

    public GlobeViewModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public GlobeViewModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mSgiPlayerManager == null) {
            return;
        }
        if (visibility == 0) {
            this.mSgiPlayerManager.sgViewResume();
        } else {
            this.mSgiPlayerManager.sgViewSuspend();
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        this.mGlobeViewModelListener.onHideAllPopup(true);
        showCityUnderSelection(Integer.valueOf(-1));
        if (this.mGestureDetector != null) {
            this.mGestureDetector.onTouchEvent(event);
        }
        if (this.mSearchBarViewModel != null) {
            this.mSearchBarViewModel.hideSoftInput();
        }
        if (!(this.mSearchBarViewModel == null || this.mSearchBarViewModel.getAutoText() == null || !this.mSearchBarViewModel.getAutoText().hasFocus() || StateUtils.isMobileKeyboard(this.mContext))) {
            this.mSearchBarViewModel.getAutoText().setCursorVisible(false);
        }
        return true;
    }

    private void initView() {
        inflate(this.mContext, C0836R.layout.worldclock_globe_view, this);
        this.mSurfaceFrame = (FrameLayout) findViewById(C0836R.id.surface_framelayout);
        this.mWeatherLogoLayout = (RelativeLayout) findViewById(C0836R.id.worldclock_weather_logo_layout);
        this.mZoomInOut = (LinearLayout) findViewById(C0836R.id.zoom_in_out_button);
        this.mZoomIn = (ImageView) findViewById(C0836R.id.zoom_in);
        this.mZoomOut = (ImageView) findViewById(C0836R.id.zoom_out);
        this.mWeatherLogoLayout.setOnClickListener(new C08571());
    }

    public void initSgiView(City startCity, Bundle savedInstanceState, SgiPlayerListener sgiPlayerListener, String fromWhere) {
        this.mSgiPlayerManager = new SGIPlayerManager(this.mContext);
        this.mSgiPlayerManager.initView(this.mContext, startCity, this.mSurfaceFrame, savedInstanceState, sgiPlayerListener);
        Log.secD("GlobeViewModel", "3D View was created.");
        this.mGestureDetector = new GestureDetectorCompat(this.mContext, new GestureListener());
        showCityUnderSelection();
        updateWeatherLogo(fromWhere);
        updateZoomInOut();
    }

    public void updateWeatherLogo(String fromWhere) {
        if (WorldclockUtils.isFromExternal(fromWhere) || WorldclockWeatherUtils.isDisableWeather(this.mContext) || this.mSearchBarViewModel == null || this.mSearchBarViewModel.isDropdownListShown()) {
            this.mWeatherLogoLayout.setVisibility(8);
        } else {
            this.mWeatherLogoLayout.setVisibility(0);
        }
    }

    public void updateZoomInOut() {
        int i = 0;
        if (this.mSgiPlayerManager != null && this.mZoomInOut != null && this.mZoomIn != null && this.mZoomOut != null) {
            boolean needVisible;
            AccessibilityManager am = (AccessibilityManager) this.mContext.getSystemService("accessibility");
            if (((am == null || !am.isEnabled()) && !StateUtils.isContextInDexMode(this.mContext)) || this.mSearchBarViewModel == null || this.mSearchBarViewModel.isDropdownListShown()) {
                needVisible = false;
            } else {
                needVisible = true;
            }
            LinearLayout linearLayout = this.mZoomInOut;
            if (!needVisible) {
                i = 8;
            }
            linearLayout.setVisibility(i);
            if (!this.mZoomIn.hasOnClickListeners()) {
                this.mZoomIn.setOnClickListener(GlobeViewModel$$Lambda$1.lambdaFactory$(this));
            }
            if (!this.mZoomOut.hasOnClickListeners()) {
                this.mZoomOut.setOnClickListener(GlobeViewModel$$Lambda$4.lambdaFactory$(this));
            }
        }
    }

    private /* synthetic */ void lambda$updateZoomInOut$0(View v) {
        this.mSgiPlayerManager.zoomInOut(-1.0f);
    }

    private /* synthetic */ void lambda$updateZoomInOut$1(View v) {
        this.mSgiPlayerManager.zoomInOut(1.0f);
    }

    public float cityCardOffset(City city) {
        return this.mSgiPlayerManager.cityCardOffset(city);
    }

    public void moveToCity(City city, float zoomLevel) {
        this.mSgiPlayerManager.moveToCity(city, zoomLevel);
    }

    public void showCityUnderSelection(Integer uniqueID) {
        if (this.mSgiPlayerManager != null) {
            this.mSgiPlayerManager.showCityUnderSelection(uniqueID.intValue());
        }
    }

    public void showCityUnderSelection() {
        this.mSgiPlayerManager.showCityUnderSelection();
    }

    public void updateNightImage() {
        this.mSgiPlayerManager.updateNightImage();
    }

    public void updateCityTime() {
        this.mSgiPlayerManager.updateCityTime();
    }

    public void reloadCitiesLayer() {
        this.mSgiPlayerManager.reloadCitiesLayer(true);
    }

    public void saveInstanceState(Bundle outState, boolean isShowCityPopup) {
        if (!isShowCityPopup) {
            outState.putSerializable("HRotation", Float.valueOf(this.mSgiPlayerManager.getHRotation()));
            outState.putSerializable("VRotation", Float.valueOf(this.mSgiPlayerManager.getVRotation()));
        }
        outState.putSerializable("Distance", Float.valueOf(this.mSgiPlayerManager.getDistance()));
    }

    public void releaseInstance() {
        this.mGlobeViewModelListener = null;
        this.mZoomInOut = null;
        this.mZoomIn = null;
        this.mZoomOut = null;
        this.mGestureDetector = null;
        if (this.mSgiPlayerManager != null) {
            this.mSgiPlayerManager.destroy();
            this.mSgiPlayerManager = null;
        }
    }

    public void setSgiVisibility(int visibility) {
        if (this.mSgiPlayerManager != null) {
            this.mSgiPlayerManager.setSgiVisibility(visibility);
        }
    }
}
