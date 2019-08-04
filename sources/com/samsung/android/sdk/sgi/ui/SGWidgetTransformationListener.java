package com.samsung.android.sdk.sgi.ui;

import com.samsung.android.sdk.sgi.base.SGMatrix4f;
import com.samsung.android.sdk.sgi.base.SGQuaternion;
import com.samsung.android.sdk.sgi.base.SGVector2f;
import com.samsung.android.sdk.sgi.base.SGVector3f;

public interface SGWidgetTransformationListener {
    void onLocalTransformChanged(SGWidget sGWidget, SGMatrix4f sGMatrix4f);

    void onOpacityChanged(SGWidget sGWidget, float f);

    void onPositionChanged(SGWidget sGWidget, SGVector3f sGVector3f);

    void onRotationChanged(SGWidget sGWidget, SGQuaternion sGQuaternion);

    void onScaleChanged(SGWidget sGWidget, SGVector3f sGVector3f);

    void onSizeChanged(SGWidget sGWidget, SGVector2f sGVector2f);
}
