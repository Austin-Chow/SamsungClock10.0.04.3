package com.samsung.android.sdk.sgi.vi;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.samsung.android.sdk.sgi.render.SGRenderDataProvider;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent;
import com.samsung.android.sdk.sgi.vi.SGViewImpl.DefaultCheckInputConnectionProxyStrategy;
import com.samsung.android.sdk.sgi.vi.SGViewImpl.DefaultOnKeyEventStrategy;
import com.samsung.android.sdk.sgi.vi.SGViewImpl.DefaultOnKeyLongPress;

public class SGView extends SurfaceView {
    private static int mRetrySurfaceCreationCounter = 1;
    private static boolean mWasAttachedToNW = false;
    private Callback mCallback;
    private SGViewImpl mImpl;

    /* renamed from: com.samsung.android.sdk.sgi.vi.SGView$1 */
    class C04561 implements DefaultCheckInputConnectionProxyStrategy {
        C04561() {
        }

        public boolean checkInputConnectionProxy(View view) {
            return super.checkInputConnectionProxy(view);
        }
    }

    /* renamed from: com.samsung.android.sdk.sgi.vi.SGView$2 */
    class C04572 implements DefaultOnKeyLongPress {
        C04572() {
        }

        public boolean onKeyLongPress(int keyCode, KeyEvent event) {
            return super.onKeyLongPress(keyCode, event);
        }
    }

    /* renamed from: com.samsung.android.sdk.sgi.vi.SGView$3 */
    class C04583 implements DefaultOnKeyEventStrategy {
        C04583() {
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
            return super.onKeyDown(keyCode, event);
        }

        public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }

        public boolean onKeyUp(int keyCode, KeyEvent event) {
            return super.onKeyUp(keyCode, event);
        }
    }

    class SurfaceHolderCallBack implements Callback {

        private class SurfaceHolderCallbackRunner implements Runnable {
            SurfaceHolderCallBack mSurfaceHolderCallBack;
            SurfaceHolder msurfaceHolder;

            SurfaceHolderCallbackRunner(SurfaceHolderCallBack aSurfaceHolderCallBack, SurfaceHolder holder) {
                this.msurfaceHolder = holder;
                this.mSurfaceHolderCallBack = aSurfaceHolderCallBack;
            }

            public void run() {
                if (SGView.mWasAttachedToNW) {
                    Log.w("SGI", "NativeWindow is already attached. Bailing out!");
                    return;
                }
                this.mSurfaceHolderCallBack.surfaceCreated(this.msurfaceHolder);
                SGView.access$208();
            }
        }

        SurfaceHolderCallBack() {
        }

        protected void scheduleNextSurfaceCreated(SurfaceHolder holder) {
            new Handler(Looper.getMainLooper()).postDelayed(new SurfaceHolderCallbackRunner(this, holder), 25);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            SGView.this.mImpl.surfaceChanged(width, height);
            SGView.this.mImpl.setResumed(false);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            holder.setFormat(-3);
            holder.setType(2);
            if (SGView.this.mImpl.surfaceAvailable(holder.getSurface())) {
                synchronized (this) {
                    SGView.mWasAttachedToNW = true;
                }
                return;
            }
            synchronized (this) {
                SGView.mWasAttachedToNW = false;
            }
            if (SGView.mRetrySurfaceCreationCounter < 5) {
                scheduleNextSurfaceCreated(holder);
            } else {
                Log.e("SGI", "Surface recreation with NativeWindow=null retry count exceeded! Bailing out!");
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            SGView.this.mImpl.setSuspended(false);
            SGView.this.mImpl.surfaceDestroyed();
        }
    }

    public SGView(Context context) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, null, null);
        init();
    }

    public SGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mImpl = new SGViewImpl((View) this, null, null);
        init();
    }

    public SGView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mImpl = new SGViewImpl((View) this, null, null);
        init();
    }

    public SGView(Context context, SGRenderDataProvider renderDataProvider) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, renderDataProvider, null);
        init();
    }

    public SGView(Context context, SGRenderDataProvider renderDataProvider, SGContextConfiguration contextConfiguration) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, renderDataProvider, contextConfiguration);
        init();
    }

    public SGView(Context context, SGContextConfiguration contextConfiguration) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, null, contextConfiguration);
        init();
    }

    static /* synthetic */ int access$208() {
        int i = mRetrySurfaceCreationCounter;
        mRetrySurfaceCreationCounter = i + 1;
        return i;
    }

    public static SGInputConnectionListener getInputConnectionListener() {
        return SGViewImpl.getInputConnectionListener();
    }

    public static void releaseSip(SGInputConnectionListener inputDest) {
        SGViewImpl.releaseSip(inputDest);
    }

    public static void requestSip(SGInputConnectionListener inputDest) {
        SGViewImpl.requestSip(inputDest);
    }

    public void attachCurrentThread() {
        this.mImpl.attachCurrentThread();
    }

    public boolean checkInputConnectionProxy(View view) {
        return this.mImpl.checkInputConnectionProxy(view, new C04561());
    }

    protected final SGTouchEvent createHoverEvent(MotionEvent event) {
        return this.mImpl.createHoverEvent(event);
    }

    protected final SGTouchEvent createTouchEvent(MotionEvent event) {
        return this.mImpl.createTouchEvent(event);
    }

    public void detachCurrentThread() {
        this.mImpl.detachCurrentThread();
    }

    public float getAlpha() {
        return getSurface().getOpacity();
    }

    protected Callback getCallback() {
        if (this.mCallback == null) {
            this.mCallback = new SurfaceHolderCallBack();
        }
        return this.mCallback;
    }

    SGViewImpl getImpl() {
        return this.mImpl;
    }

    public SGMotionEventConverter getMotionEventConverter() {
        return this.mImpl.getMotionEventConverter();
    }

    public SGSurface getSurface() {
        return this.mImpl.getSurface();
    }

    public SGSurfaceStateListener getSurfaceStateListener() {
        return this.mImpl.getSurfaceStateListener();
    }

    protected void init() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.setFormat(-3);
        surfaceHolder.setType(2);
        surfaceHolder.addCallback(getCallback());
        setFocusable(true);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mImpl.onAttachedToWindow();
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttributes) {
        return this.mImpl.onCreateInputConnection(outAttributes);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mImpl.onDetachedFromWindow();
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        this.mImpl.onFocusChanged(gainFocus);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public boolean onHoverEvent(MotionEvent event) {
        return !this.mImpl.onHoverEvent(event) ? super.onHoverEvent(event) : true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return onKeyEvent(keyCode, 0, event);
    }

    public boolean onKeyEvent(int keyCode, int repeatCount, KeyEvent event) {
        return this.mImpl.onKeyEvent(keyCode, repeatCount, event, new C04583());
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return this.mImpl.onKeyLongPress(keyCode, event, new C04572());
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return onKeyEvent(keyCode, repeatCount, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return onKeyEvent(keyCode, 0, event);
    }

    protected boolean onSetAlpha(int alpha) {
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return !this.mImpl.onTouchEvent(event) ? super.onTouchEvent(event) : true;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            SGSurface surface = getSurface();
            if (surface != null) {
                surface.getRoot().setVisibility(true);
            }
        }
    }

    public void resume() {
        this.mImpl.resume();
    }

    public void setAlpha(float alpha) {
        getSurface().setOpacity(alpha);
    }

    public void setBackgroundColor(int color) {
        getSurface().getDefaultCamera().setClearColor(color);
    }

    public void setBackgroundDrawable(Drawable background) {
        Log.w("SGI", "SGView doesn't support drawables.");
    }

    public void setMotionEventConverter(SGMotionEventConverter motionEventConverter) {
        this.mImpl.setMotionEventConverter(motionEventConverter);
    }

    public void setSurfaceStateListener(SGSurfaceStateListener aListener) {
        this.mImpl.setSurfaceStateListener(aListener);
    }

    public void setVisibility(int visibility) {
        Log.i("SGI", "setVisibility " + visibility + ", current visibility = " + getVisibility());
        if (getVisibility() != visibility) {
            if (visibility == 0) {
                super.setVisibility(visibility);
                resume();
            } else if (visibility == 4 || visibility == 8) {
                suspend();
                super.setVisibility(visibility);
            }
        }
    }

    public void suspend() {
        this.mImpl.suspend();
    }
}
