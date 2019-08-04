package com.sec.android.app.clockpackage.worldclock.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.sec.android.app.clockpackage.common.util.Log;
import com.sec.android.app.clockpackage.worldclock.callback.SgiPlayerListener;
import com.sec.android.app.clockpackage.worldclock.model.City;
import com.sec.android.app.clockpackage.worldclock.sgi.SGIPlayer;

public class SGIPlayerManager {
    private Context mContext;
    private DisplayMetrics mDisplaymetrics;
    private SGIPlayer mSGIPlayer;
    private SgiPlayerListener mSgiPlayerListener;

    public SGIPlayerManager(Context context) {
        this.mContext = context;
    }

    public void initView(Context context, City startCity, ViewGroup parentView, Bundle savedInstanceState, SgiPlayerListener mSgiPlayerListener) {
        if (startCity != null) {
            this.mDisplaymetrics = this.mContext.getResources().getDisplayMetrics();
            this.mSGIPlayer = new SGIPlayer(context, startCity, parentView, 0, savedInstanceState, mSgiPlayerListener);
        }
        this.mSgiPlayerListener = mSgiPlayerListener;
    }

    public void moveToCity(City city, float zoomLevel) {
        if (this.mSGIPlayer != null && city != null) {
            this.mSGIPlayer.moveToCity(city, zoomLevel, true, null);
        }
    }

    public void showCityUnderSelection(int cityUnderSelID) {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.showCityUnderSelection(cityUnderSelID);
        }
    }

    public void reloadCitiesLayer(boolean isfullUpdate) {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.reloadCitiesLayer(isfullUpdate);
        }
    }

    public void showCityUnderSelection() {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.showCityUnderSelection();
        }
    }

    public void updateNightImage() {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.updateNightImage();
        }
    }

    public void updateCityTime() {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.updateCityTime();
        }
    }

    public void sgViewSuspend() {
        this.mSGIPlayer.viewSuspend();
        Log.secD("SGIPlayerManager", "onPause 3DView getVisibility = " + this.mSGIPlayer.getSGViewVisibility());
    }

    public void sgViewResume() {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.viewResume();
            if (this.mSgiPlayerListener.onCityUnderSelection() != null) {
                this.mDisplaymetrics = this.mContext.getResources().getDisplayMetrics();
            }
        }
    }

    public void processClickEvent(MotionEvent evt) {
        this.mSGIPlayer.processClickEvent(evt);
    }

    public void processDoubleTapEvent(MotionEvent evt) {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.processDoubleTapEvent(evt);
        }
    }

    public void processFlingEvent(float velocityX, float velocityY, float touchX, float touchY) {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.processFlingEvent(velocityX, velocityY, touchX, touchY);
        }
    }

    public float cityCardOffset(City city) {
        if (this.mSGIPlayer != null) {
            return this.mSGIPlayer.cityCardOffset(city);
        }
        return 0.0f;
    }

    public void zoomInOut(float deltaDistance) {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.zoomInOut(deltaDistance);
        }
    }

    public void destroy() {
        if (this.mSGIPlayer != null) {
            this.mSGIPlayer.destroy();
            this.mSGIPlayer = null;
        }
        if (this.mContext != null) {
            this.mContext = null;
        }
        if (this.mDisplaymetrics != null) {
            this.mDisplaymetrics = null;
        }
        if (this.mSgiPlayerListener != null) {
            this.mSgiPlayerListener = null;
        }
    }

    public void setSgiVisibility(int visibility) {
        this.mSGIPlayer.setSgiVisibility(visibility);
    }

    public float getHRotation() {
        return this.mSGIPlayer.getHRotation();
    }

    public float getVRotation() {
        return this.mSGIPlayer.getVRotation();
    }

    public float getDistance() {
        return this.mSGIPlayer.getDistance();
    }
}
