package com.samsung.android.sdk.sgi.ui;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;

final class SGWidgetListenerHolder extends SGWidgetListenerBase {
    public SGKeyEventListener mKeyEventListener;
    private SGWidget mWidget;
    public SGWidgetAnimationListener mWidgetAnimationListener;
    public SGWidgetTransformationListener mWidgetTransformationListener;

    public SGWidgetListenerHolder(SGWidget widget) {
        this.mWidget = widget;
    }

    public void onFinished(long widget) {
        try {
            this.mWidgetAnimationListener.onFinished(this.mWidget);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetAnimationListener::onFinished error: uncaught exception");
        }
    }

    public boolean onKeyEvent(SGKeyEvent event, long widget) {
        try {
            return this.mKeyEventListener.onKeyEvent(event, this.mWidget);
        } catch (Exception e) {
            SGUIException.handle(e, "SGKeyEventListener::onKeyEvent error: uncaught exception");
            return false;
        }
    }

    public void onLocalTransformChanged(long widget, SGMatrix4f localTransform) {
        try {
            this.mWidgetTransformationListener.onLocalTransformChanged(this.mWidget, localTransform);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetTransformationListener::onLocalTransformChanged error: uncaught exception");
        }
    }

    public void onOpacityChanged(long widget, float opacity) {
        try {
            this.mWidgetTransformationListener.onOpacityChanged(this.mWidget, opacity);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetTransformationListener::onOpacityChanged error: uncaught exception");
        }
    }

    public void onPositionChanged(long widget, SGVector3f position) {
        try {
            this.mWidgetTransformationListener.onPositionChanged(this.mWidget, position);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetTransformationListener::onPositionChanged error: uncaught exception");
        }
    }

    public void onRotationChanged(long widget, SGQuaternion rotation) {
        try {
            this.mWidgetTransformationListener.onRotationChanged(this.mWidget, rotation);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetTransformationListener::onRotationChanged error: uncaught exception");
        }
    }

    public void onScaleChanged(long widget, SGVector3f scale) {
        try {
            this.mWidgetTransformationListener.onScaleChanged(this.mWidget, scale);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetTransformationListener::onScaleChanged error: uncaught exception");
        }
    }

    public void onSizeChanged(long widget, SGVector2f size) {
        try {
            this.mWidgetTransformationListener.onSizeChanged(this.mWidget, size);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetTransformationListener::onSizeChanged error: uncaught exception");
        }
    }

    public void onStarted(long widget) {
        try {
            this.mWidgetAnimationListener.onStarted(this.mWidget);
        } catch (Exception e) {
            SGUIException.handle(e, "SGWidgetAnimationListener::onStarted error: uncaught exception");
        }
    }
}
