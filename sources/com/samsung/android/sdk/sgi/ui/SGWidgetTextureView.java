package com.samsung.android.sdk.sgi.ui;

import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.render.SGSurfaceRenderer;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class SGWidgetTextureView extends SGWidgetSurface {
    private AtomicInteger mFrames = new AtomicInteger();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private float[] mImageRectTransform;
    private boolean mOnFrameAvailableEventOccured = false;
    private OnFrameAvailableListenerImpl mOnFrameAvailableListener = new OnFrameAvailableListenerImpl(this);
    private Method mSetBufferSizeMethod;
    private Method mSetSurfaceTextureMethod;
    private ExclusiveSurfaceTexture mSurfaceTexture;
    private TextureView mTextureView;
    private boolean mUpdateEnabled = true;

    /* renamed from: com.samsung.android.sdk.sgi.ui.SGWidgetTextureView$1 */
    class C04471 implements Runnable {
        C04471() {
        }

        public void run() {
            SGWidgetTextureView.this.onUpdate();
        }
    }

    /* renamed from: com.samsung.android.sdk.sgi.ui.SGWidgetTextureView$3 */
    class C04493 implements Runnable {
        C04493() {
        }

        public void run() {
            SurfaceTextureListener listener = SGWidgetTextureView.this.mTextureView.getSurfaceTextureListener();
            if (listener != null) {
                listener.onSurfaceTextureUpdated(SGWidgetTextureView.this.mSurfaceTexture);
            }
        }
    }

    /* renamed from: com.samsung.android.sdk.sgi.ui.SGWidgetTextureView$4 */
    class C04504 implements OnFrameAvailableListener {
        C04504() {
        }

        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            SGWidgetTextureView.this.mFrames.incrementAndGet();
        }
    }

    private static final class Drawer extends SGSurfaceRenderer {
        private WeakReference<SGWidgetTextureView> mView;

        Drawer(SGWidgetTextureView view) {
            this.mView = new WeakReference(view);
        }

        public void onCreateTexture(int id) {
            super.onCreateTexture(id);
        }

        public void onDestroyTexture() {
            super.onDestroyTexture();
        }

        public void onDraw(int anID) {
            SGWidgetTextureView view = (SGWidgetTextureView) this.mView.get();
            if (view != null) {
                view.draw(anID);
            }
        }
    }

    private static final class ExclusiveSurfaceTexture extends SurfaceTexture {
        private boolean mForced = false;

        public ExclusiveSurfaceTexture(int texName) {
            super(texName);
        }

        public void forcedSetOnFrameAvailableListener(OnFrameAvailableListener l) {
            this.mForced = true;
            setOnFrameAvailableListener(l);
            this.mForced = false;
        }

        public void setOnFrameAvailableListener(OnFrameAvailableListener l) {
            if (this.mForced) {
                super.setOnFrameAvailableListener(l);
            }
        }
    }

    private static final class OnFrameAvailableListenerImpl implements OnFrameAvailableListener {
        private WeakReference<SGWidgetTextureView> mView;

        public OnFrameAvailableListenerImpl(SGWidgetTextureView view) {
            this.mView = new WeakReference(view);
        }

        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            SGWidgetTextureView view = (SGWidgetTextureView) this.mView.get();
            if (view != null) {
                view.onFrameAvailable(surfaceTexture);
            }
        }
    }

    public SGWidgetTextureView(SGVector2f size, TextureView textureView) {
        super(size, new SGSurfaceRenderer());
        obtainCallMethods();
        setTextureView(textureView);
    }

    private void draw(int anID) {
        synchronized (this) {
            if (this.mSurfaceTexture == null) {
                this.mSurfaceTexture = new ExclusiveSurfaceTexture(anID);
                this.mSurfaceTexture.forcedSetOnFrameAvailableListener(this.mOnFrameAvailableListener);
                SGVector2f sizeWidget = getSize();
                final int textureWidth = getTextureWidth(sizeWidget.getX(), sizeWidget.getY());
                final int textureHeight = getTextureHeight(sizeWidget.getX(), sizeWidget.getY());
                try {
                    this.mSetBufferSizeMethod.invoke(this.mSurfaceTexture, new Object[]{Integer.valueOf(textureWidth), Integer.valueOf(textureHeight)});
                    this.mSetSurfaceTextureMethod.invoke(this.mTextureView, new Object[]{this.mSurfaceTexture});
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            SurfaceTextureListener listener = SGWidgetTextureView.this.mTextureView.getSurfaceTextureListener();
                            if (listener != null) {
                                listener.onSurfaceTextureAvailable(SGWidgetTextureView.this.mSurfaceTexture, textureWidth, textureHeight);
                            }
                        }
                    });
                } catch (InvocationTargetException e) {
                    Log.e("SGSurfaceRenderer", "Error on setSurfaceTexture into TextureView, - " + e.getMessage());
                } catch (IllegalAccessException e2) {
                    Log.e("SGSurfaceRenderer", "Error on setSurfaceTexture into TextureView, - " + e2.getMessage());
                }
            } else {
                while (this.mFrames.get() > 0) {
                    this.mSurfaceTexture.updateTexImage();
                    this.mFrames.getAndDecrement();
                    updateTextureMatrix(getTextureTrasform());
                }
                this.mHandler.post(new C04493());
            }
        }
    }

    private SGMatrix4f getTextureTrasform() {
        this.mSurfaceTexture.getTransformMatrix(this.mImageRectTransform);
        SGMatrix4f transform = new SGMatrix4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                transform.setElement(j, i, this.mImageRectTransform[(i * 4) + j]);
            }
        }
        return transform;
    }

    private void obtainCallMethods() {
        try {
            this.mSetSurfaceTextureMethod = TextureView.class.getDeclaredMethod("setSurfaceTexture", new Class[]{SurfaceTexture.class});
            this.mSetBufferSizeMethod = SurfaceTexture.class.getDeclaredMethod("setDefaultBufferSize", new Class[]{Integer.TYPE, Integer.TYPE});
        } catch (NoSuchMethodException e) {
            this.mSetSurfaceTextureMethod = null;
            this.mSetBufferSizeMethod = null;
            throw new RuntimeException("API level 16 is minimal for using this class");
        }
    }

    private void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            onUpdate();
        } else {
            this.mHandler.post(new C04471());
        }
    }

    private void onUpdate() {
        if (this.mUpdateEnabled) {
            this.mFrames.incrementAndGet();
            invalidate();
            return;
        }
        this.mOnFrameAvailableEventOccured = true;
    }

    private synchronized void resizeSurfaceTexture(int width, int height) {
        if (this.mSurfaceTexture != null) {
            int textureWidth = getTextureWidth((float) width, (float) height);
            int textureHeight = getTextureHeight((float) width, (float) height);
            try {
                this.mSetBufferSizeMethod.invoke(this.mSurfaceTexture, new Object[]{Integer.valueOf(textureWidth), Integer.valueOf(textureHeight)});
                SurfaceTextureListener listener = this.mTextureView.getSurfaceTextureListener();
                if (listener != null) {
                    listener.onSurfaceTextureSizeChanged(this.mSurfaceTexture, textureWidth, textureHeight);
                }
            } catch (InvocationTargetException e) {
                Log.e("SGSurfaceRenderer", "Error on setDefaultBufferSize into SurfaceTexture, - " + e.getMessage());
            } catch (IllegalAccessException e2) {
                Log.e("SGSurfaceRenderer", "Error on setDefaultBufferSize into SurfaceTexture, - " + e2.getMessage());
            }
        }
    }

    public /* bridge */ /* synthetic */ RectF getContentRect() {
        return super.getContentRect();
    }

    public /* bridge */ /* synthetic */ SGVector2f getContentRectPivot() {
        return super.getContentRectPivot();
    }

    public /* bridge */ /* synthetic */ SGVector2f getContentRectScale() {
        return super.getContentRectScale();
    }

    public /* bridge */ /* synthetic */ SGSurfaceRenderer getRenderer() {
        return super.getRenderer();
    }

    protected int getTextureHeight(float width, float height) {
        return (int) height;
    }

    protected int getTextureWidth(float width, float height) {
        return (int) width;
    }

    public /* bridge */ /* synthetic */ void invalidate() {
        super.invalidate();
    }

    public /* bridge */ /* synthetic */ void invalidate(RectF rectF) {
        super.invalidate(rectF);
    }

    public boolean onTouchEvent(SGTouchEvent event) {
        if (this.mTextureView != null) {
            MotionEvent motionEvent = SGTouchEvent.createMotionEvent(event, this);
            if (motionEvent != null) {
                boolean result = this.mTextureView.onTouchEvent(motionEvent);
                motionEvent.recycle();
                return result;
            }
        }
        return false;
    }

    public /* bridge */ /* synthetic */ void setContentRect(RectF rectF) {
        super.setContentRect(rectF);
    }

    public /* bridge */ /* synthetic */ void setContentRectPivot(SGVector2f sGVector2f) {
        super.setContentRectPivot(sGVector2f);
    }

    public /* bridge */ /* synthetic */ void setContentRectScale(SGVector2f sGVector2f) {
        super.setContentRectScale(sGVector2f);
    }

    public /* bridge */ /* synthetic */ void setRenderer(SGSurfaceRenderer sGSurfaceRenderer) {
        super.setRenderer(sGSurfaceRenderer);
    }

    public void setSize(float width, float height) {
        super.setSize(width, height);
        resizeSurfaceTexture((int) width, (int) height);
    }

    public void setSize(SGVector2f size) {
        super.setSize(size);
        resizeSurfaceTexture((int) size.getX(), (int) size.getY());
    }

    public void setTextureView(TextureView textureView) {
        if (this.mTextureView == null && textureView != null) {
            setRenderer(new Drawer(this));
            this.mTextureView = textureView;
            this.mImageRectTransform = new float[16];
        }
    }

    public void setUpdateEnabled(boolean aUpdateEnabled) {
        boolean prevUpdateEnabled = this.mUpdateEnabled;
        this.mUpdateEnabled = aUpdateEnabled;
        if (this.mOnFrameAvailableEventOccured && !prevUpdateEnabled) {
            this.mOnFrameAvailableEventOccured = false;
            this.mFrames.incrementAndGet();
            invalidate();
        }
    }

    public void setVisibility(SGWidgetVisibility visibility) {
        super.setVisibility(visibility);
        switch (visibility) {
            case VISIBLE:
                this.mTextureView.setVisibility(0);
                if (this.mSurfaceTexture != null) {
                    if (this.mFrames.get() > 0) {
                        invalidate();
                    }
                    this.mSurfaceTexture.forcedSetOnFrameAvailableListener(this.mOnFrameAvailableListener);
                    return;
                }
                return;
            case INVISIBLE:
                this.mTextureView.setVisibility(4);
                if (this.mSurfaceTexture != null) {
                    this.mSurfaceTexture.setOnFrameAvailableListener(new C04504());
                    return;
                }
                return;
            case GONE:
                this.mTextureView.setVisibility(8);
                return;
            default:
                return;
        }
    }

    public /* bridge */ /* synthetic */ void updateTextureMatrix(SGMatrix4f sGMatrix4f) {
        super.updateTextureMatrix(sGMatrix4f);
    }
}
