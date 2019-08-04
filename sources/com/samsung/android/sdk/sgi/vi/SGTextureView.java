package com.samsung.android.sdk.sgi.vi;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.samsung.android.sdk.sgi.render.SGRenderDataProvider;
import com.samsung.android.sdk.sgi.ui.SGTouchEvent;
import com.samsung.android.sdk.sgi.vi.SGViewImpl.DefaultCheckInputConnectionProxyStrategy;
import com.samsung.android.sdk.sgi.vi.SGViewImpl.DefaultOnKeyEventStrategy;
import com.samsung.android.sdk.sgi.vi.SGViewImpl.DefaultOnKeyLongPress;

public class SGTextureView extends TextureView {
    private SGViewImpl mImpl;
    private SurfaceTextureListener mSurfaceTextureListener;

    /* renamed from: com.samsung.android.sdk.sgi.vi.SGTextureView$1 */
    class C04531 implements DefaultCheckInputConnectionProxyStrategy {
        C04531() {
        }

        public boolean checkInputConnectionProxy(View view) {
            return super.checkInputConnectionProxy(view);
        }
    }

    /* renamed from: com.samsung.android.sdk.sgi.vi.SGTextureView$2 */
    class C04542 implements DefaultOnKeyLongPress {
        C04542() {
        }

        public boolean onKeyLongPress(int keyCode, KeyEvent event) {
            return super.onKeyLongPress(keyCode, event);
        }
    }

    /* renamed from: com.samsung.android.sdk.sgi.vi.SGTextureView$3 */
    class C04553 implements DefaultOnKeyEventStrategy {
        C04553() {
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

    class SGSurfaceTextureListener implements SurfaceTextureListener {
        SGSurfaceTextureListener() {
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            SGTextureView.this.mImpl.surfaceAvailable(new Surface(surface));
            SGTextureView.this.mImpl.surfaceChanged(width, height);
            SGTextureView.this.mImpl.setResumed(false);
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            SGTextureView.this.mImpl.setSuspended(false);
            SGTextureView.this.mImpl.surfaceDestroyed();
            return true;
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            SGTextureView.this.mImpl.surfaceChanged(width, height);
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }

    public SGTextureView(Context context) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, null, null);
        init();
    }

    public SGTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mImpl = new SGViewImpl((View) this, null, null);
        init();
    }

    public SGTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mImpl = new SGViewImpl((View) this, null, null);
        init();
    }

    public SGTextureView(Context context, SGRenderDataProvider renderDataProvider) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, renderDataProvider, null);
        init();
    }

    public SGTextureView(Context context, SGRenderDataProvider renderDataProvider, SGContextConfiguration contextConfiguration) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, renderDataProvider, contextConfiguration);
        init();
    }

    public SGTextureView(Context context, SGContextConfiguration contextConfiguration) {
        super(context);
        this.mImpl = new SGViewImpl((View) this, null, contextConfiguration);
        init();
    }

    public void attachCurrentThread() {
        this.mImpl.attachCurrentThread();
    }

    public boolean checkInputConnectionProxy(View view) {
        return this.mImpl.checkInputConnectionProxy(view, new C04531());
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

    protected SurfaceTextureListener getCallback() {
        if (this.mSurfaceTextureListener == null) {
            this.mSurfaceTextureListener = new SGSurfaceTextureListener();
        }
        return this.mSurfaceTextureListener;
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
        super.setSurfaceTextureListener(getCallback());
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
        return this.mImpl.onKeyEvent(keyCode, repeatCount, event, new C04553());
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return this.mImpl.onKeyLongPress(keyCode, event, new C04542());
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
        Log.w("SGI", "SGTextureView doesn't support drawables.");
    }

    public void setMotionEventConverter(SGMotionEventConverter motionEventConverter) {
        this.mImpl.setMotionEventConverter(motionEventConverter);
    }

    public void setSurfaceStateListener(SGSurfaceStateListener aListener) {
        this.mImpl.setSurfaceStateListener(aListener);
    }

    public final void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        throw new IllegalStateException("SGTexureView: setSurfaceTexture() call is not allowed");
    }

    public final void setSurfaceTextureListener(SurfaceTextureListener listener) {
        throw new IllegalStateException("SGTexureView: setSurfaceTextureListener() call is not allowed");
    }

    public void setVisibility(int visibility) {
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
