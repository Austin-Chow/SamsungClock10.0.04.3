package android.support.v7.util;

import android.content.ContentResolver;
import android.graphics.drawable.Drawable;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;

public class SeslShowButtonBackgroundHelper {
    private Drawable mBackgroundOff;
    private Drawable mBackgroundOn;
    private ContentResolver mContentResolver;
    private View mView;

    public SeslShowButtonBackgroundHelper(View view, Drawable backgroundOn, Drawable backgroundOff) {
        this.mView = view;
        this.mContentResolver = view.getContext().getContentResolver();
        this.mBackgroundOn = backgroundOn;
        this.mBackgroundOff = backgroundOff;
    }

    public void setBackgroundOff(Drawable backgroundOff) {
        if (this.mBackgroundOn == null || this.mBackgroundOn != backgroundOff) {
            this.mBackgroundOff = backgroundOff;
        } else {
            Log.w("SeslSBBHelper", backgroundOff + "is same drawable with mBackgroundOn");
        }
    }

    public void setBackgroundOn(Drawable backgroundOn) {
        this.mBackgroundOn = backgroundOn;
    }

    public void updateButtonBackground() {
        boolean show = true;
        if (System.getInt(this.mContentResolver, "show_button_background", 0) != 1) {
            show = false;
        }
        this.mView.setBackground(show ? this.mBackgroundOn : this.mBackgroundOff);
    }

    public void updateOverflowButtonBackground(Drawable backgroundOn) {
        this.mBackgroundOn = backgroundOn;
        updateButtonBackground();
    }
}
