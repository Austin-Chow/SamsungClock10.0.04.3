package com.samsung.android.sdk.sgi.animation;

import com.samsung.android.sdk.sgi.base.SGMemoryRegistrator;

public final class SGTimingFunctionFactory {
    public static SGAnimationTimingFunction createAccelerateTimingFunction(SGAccelerateTimingFunctionType type) {
        long cPtr = SGJNI.SGTimingFunctionFactory_createAccelerateTimingFunction__SWIG_0(SGJNI.getData(type));
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }

    public static SGAnimationTimingFunction createAccelerateTimingFunction(SGAccelerateTimingFunctionType type, float platoValue) {
        long cPtr = SGJNI.SGTimingFunctionFactory_createAccelerateTimingFunction__SWIG_1(SGJNI.getData(type), platoValue);
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }

    public static SGAnimationTimingFunction createBounceEaseTimingFunction(SGEaseTimingFunctionType type) {
        long cPtr = SGJNI.SGTimingFunctionFactory_createBounceEaseTimingFunction(SGJNI.getData(type));
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }

    public static SGAnimationTimingFunction createCubicBezierTimingFunction(float timeProgress1, float progress1, float timeProgress2, float progress2) {
        long cPtr = SGJNI.SGTimingFunctionFactory_createCubicBezierTimingFunction__SWIG_1(timeProgress1, progress1, timeProgress2, progress2);
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }

    public static SGAnimationTimingFunction createCubicBezierTimingFunction(SGCubicBezierTimingFunctionType type) {
        long cPtr = SGJNI.SGTimingFunctionFactory_createCubicBezierTimingFunction__SWIG_0(SGJNI.getData(type));
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }

    public static SGAnimationTimingFunction createLinearTimingFunction() {
        long cPtr = SGJNI.SGTimingFunctionFactory_createLinearTimingFunction();
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }

    public static SGAnimationTimingFunction createPredefinedTimingFunction(SGPredefinedTimingFunctionType type) {
        long cPtr = SGJNI.SGTimingFunctionFactory_createPredefinedTimingFunction(SGJNI.getData(type));
        if (cPtr == 0) {
            return null;
        }
        SGAnimationTimingFunction result = (SGAnimationTimingFunction) SGJNI.createObjectFromNativePtr(SGTimingFunctionHolder.class, cPtr, true);
        SGMemoryRegistrator.getInstance().Register(result, cPtr);
        return result;
    }
}
