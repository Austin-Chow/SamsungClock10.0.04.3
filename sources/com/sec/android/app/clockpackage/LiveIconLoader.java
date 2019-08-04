package com.sec.android.app.clockpackage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import com.samsung.android.sdk.sgi.ui.SGKeyCode;
import com.sec.android.app.clockpackage.common.util.Log;
import java.util.Calendar;
import java.util.Locale;

final class LiveIconLoader {
    public static final int DENSITY_720 = 720;
    private static final String PKG_NAME_EASY_LAUNCHER = "com.sec.android.app.easylauncher";
    private static final String PKG_NAME_LAUNCHER = "com.sec.android.app.launcher";
    private static final String PKG_NAME_SYSTEM_UI = "com.android.systemui";
    private static final String PKG_NAME_UPSM_LAUNCHER = "com.sec.android.emergencylauncher";
    private static final String TAG = "LiveIconLoader";
    private static String mClockBg;
    private static String mClockHourHand;
    private static String mClockMinuteHand;
    private static int mIconDpi = 0;

    public interface DrawStrategy {
        void draw(Canvas canvas);
    }

    LiveIconLoader() {
    }

    public static Drawable getLiveIcon(Context context) {
        return getLiveIcon(context, false);
    }

    private static Drawable getLiveIcon(Context context, boolean isLarge) {
        Log.m41d(TAG, "getLiveIcon was called in ClockPackage");
        final Context ctx = context;
        try {
            if (System.getString(context.getContentResolver(), "current_sec_appicon_theme_package") == null) {
                mClockBg = "mipmap/clock_bg_default";
                mClockHourHand = "mipmap/clock_hour_hand_default";
                mClockMinuteHand = "mipmap/clock_minute_hand_default";
            } else {
                mClockBg = "mipmap/clock_bg";
                mClockHourHand = "mipmap/clock_hour_hand";
                mClockMinuteHand = "mipmap/clock_minute_hand";
            }
            ApplicationInfo r = null;
            try {
                r = ctx.getPackageManager().getApplicationInfo("com.sec.android.app.clockpackage", 8192);
            } catch (NameNotFoundException e) {
                Log.m41d(TAG, "there is no app named com.sec.android.app.clockpackage");
            }
            final Resources res = ctx.getPackageManager().getResourcesForApplication(r);
            Log.m41d(TAG, "getLiveIcon res = com.sec.android.app.clockpackage");
            loadDensityDpi(ctx);
            int id = res.getIdentifier("com.sec.android.app.clockpackage:" + mClockBg, null, null);
            if (id != 0) {
                return overlayDraw(ctx, res.getDrawableForDensity(id, mIconDpi), new DrawStrategy() {
                    public void draw(Canvas canvas) {
                        Calendar cal = Calendar.getInstance(Locale.getDefault());
                        int hour = cal.get(11);
                        int minute = cal.get(12);
                        int second = cal.get(13);
                        int id_h = res.getIdentifier("com.sec.android.app.clockpackage:" + LiveIconLoader.mClockHourHand, null, null);
                        int id_m = res.getIdentifier("com.sec.android.app.clockpackage:" + LiveIconLoader.mClockMinuteHand, null, null);
                        drawResourceWithDegree(ctx, canvas, id_h, (((float) hour) * 30.0f) + (((float) minute) * 0.5f));
                        drawResourceWithDegree(ctx, canvas, id_m, ((float) minute) * 6.0f);
                    }

                    private void drawResourceWithDegree(Context ctx, Canvas canvas, int id, float degree) {
                        Drawable drawable = res.getDrawableForDensity(id, LiveIconLoader.mIconDpi);
                        Bitmap bmHour = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), true);
                        Matrix matrix = new Matrix();
                        matrix.setRotate(degree, ((float) bmHour.getWidth()) / 2.0f, ((float) bmHour.getHeight()) / 2.0f);
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setFilterBitmap(true);
                        canvas.drawBitmap(bmHour, matrix, paint);
                    }
                });
            }
            return null;
        } catch (NameNotFoundException e2) {
            Log.m41d(TAG, "there is no app named_ com.sec.android.app.clockpackage");
            return null;
        }
    }

    private static Drawable overlayDraw(Context ctx, Drawable drawable, DrawStrategy strategy) {
        boolean mutable;
        Bitmap bitmap;
        if ((drawable instanceof BitmapDrawable) && ((BitmapDrawable) drawable).getBitmap().isMutable()) {
            mutable = true;
        } else {
            mutable = false;
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (mutable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        if (!mutable) {
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
        }
        strategy.draw(canvas);
        if (mutable) {
            return drawable;
        }
        Drawable mergedDrawable = new BitmapDrawable(ctx.getResources(), bitmap);
        if (drawable instanceof BitmapDrawable) {
            mergedDrawable.setTargetDensity(((BitmapDrawable) drawable).getBitmap().getDensity());
        }
        return mergedDrawable;
    }

    private static float loadLauncherDimen(Resources res, String packageName, String name, int defaultValue) {
        int id = res.getIdentifier(name, "dimen", packageName);
        if (id > 0) {
            return res.getDimension(id);
        }
        return (float) defaultValue;
    }

    private static void loadDensityDpi(Context ctx) {
        int targetIconSize;
        Resources appRes = ctx.getResources();
        int stdIconSize = Resources.getSystem().getDimensionPixelSize(17104896);
        int normalIconSize = (int) loadLauncherDimen(appRes, PKG_NAME_LAUNCHER, "app_icon_size", stdIconSize);
        int menuIconSize = (int) loadLauncherDimen(appRes, PKG_NAME_LAUNCHER, "menu_icon_size", stdIconSize);
        int easyIconSize = (int) loadLauncherDimen(appRes, PKG_NAME_EASY_LAUNCHER, "grid_app_icon_size", stdIconSize);
        int shortcutIconSize = (int) loadLauncherDimen(appRes, PKG_NAME_SYSTEM_UI, "shortcut_icon_default_size", stdIconSize);
        int recentAppIconSize = (int) loadLauncherDimen(appRes, PKG_NAME_SYSTEM_UI, "recents_app_list_item_icon_size", stdIconSize);
        int upsmIconSize = (int) loadLauncherDimen(appRes, PKG_NAME_UPSM_LAUNCHER, "grid_app_icon_size", stdIconSize);
        if (normalIconSize > menuIconSize) {
            targetIconSize = normalIconSize;
        } else {
            targetIconSize = menuIconSize;
        }
        if (targetIconSize <= easyIconSize) {
            targetIconSize = easyIconSize;
        }
        if (targetIconSize <= shortcutIconSize) {
            targetIconSize = shortcutIconSize;
        }
        if (targetIconSize <= recentAppIconSize) {
            targetIconSize = recentAppIconSize;
        }
        if (targetIconSize <= upsmIconSize) {
            targetIconSize = upsmIconSize;
        }
        Log.m41d(TAG, "stdIconSize : " + stdIconSize + " , targetIconSize : " + targetIconSize);
        DisplayMetrics dm = appRes.getDisplayMetrics();
        int mTargetIconDpi = dm.densityDpi;
        if (targetIconSize == stdIconSize) {
            mIconDpi = dm.densityDpi;
            return;
        }
        int targetDPI = (int) ((((float) targetIconSize) / ((float) stdIconSize)) * ((float) dm.densityDpi));
        if (targetDPI <= 120) {
            mIconDpi = 120;
        } else if (targetDPI <= SGKeyCode.CODE_NUMPAD_ENTER) {
            mIconDpi = SGKeyCode.CODE_NUMPAD_ENTER;
        } else if (targetDPI <= 240) {
            mIconDpi = 240;
        } else if (targetDPI <= 320) {
            mIconDpi = 320;
        } else if (targetDPI <= 480) {
            mIconDpi = 480;
        } else if (targetDPI <= 640) {
            mIconDpi = 640;
        } else {
            mIconDpi = DENSITY_720;
        }
        Log.m41d(TAG, "mIconDpi : " + mIconDpi + " , mTargetIconDpi : " + mTargetIconDpi);
    }
}
