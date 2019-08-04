package com.samsung.android.sdk.sgi.base;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

class SGJNI {
    private static Map<Class, Constructor> mCache = new HashMap();

    static {
        SGConfiguration.initLibrary();
        swig_module_init();
    }

    SGJNI() {
    }

    public static final native double SGAngleConverter_deg2Rad(double d);

    public static final native double SGAngleConverter_rad2Deg(double d);

    public static final native String SGConfiguration_getBuildDate();

    public static final native int SGConfiguration_getGLESVersion(boolean z);

    public static final native int SGConfiguration_getVersionInformation(int i);

    public static final native boolean SGConfiguration_isDebugInfoEnabled();

    public static final native boolean SGConfiguration_isSystraceEnabled();

    public static final native void SGConfiguration_setDebugInfoEnabled(boolean z);

    public static final native void SGConfiguration_setGLESVersion(int i);

    public static final native void SGConfiguration_setSystraceEnabled(boolean z);

    public static final native boolean SGRegistrator_AddToManagementList(long j, SGRegistrator sGRegistrator, long j2);

    public static final native boolean SGRegistrator_Deregister(long j, SGRegistrator sGRegistrator, long j2);

    public static final native Object SGRegistrator_GetObjectByPointer(long j, SGRegistrator sGRegistrator, long j2);

    public static final native boolean SGRegistrator_Register(long j, SGRegistrator sGRegistrator, Object obj, long j2);

    public static final native boolean SGRegistrator_RemoveFromManagementList(long j, SGRegistrator sGRegistrator, long j2);

    public static final native void SGRegistrator_change_ownership(SGRegistrator sGRegistrator, long j, boolean z);

    public static final native void SGRegistrator_director_connect(SGRegistrator sGRegistrator, long j, boolean z, boolean z2);

    public static final native float[] SGSgfxBox3f_createTransformed(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_createLookAtLH(float[] fArr, float[] fArr2, float[] fArr3);

    public static final native float[] SGSgfxMatrix4f_createLookAtRH(float[] fArr, float[] fArr2, float[] fArr3);

    public static final native float[] SGSgfxMatrix4f_createOrthoLH__SWIG_0(float f, float f2, float f3, float f4, float f5, float f6);

    public static final native float[] SGSgfxMatrix4f_createOrthoLH__SWIG_1(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxMatrix4f_createOrthoRH__SWIG_0(float f, float f2, float f3, float f4, float f5, float f6);

    public static final native float[] SGSgfxMatrix4f_createOrthoRH__SWIG_1(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxMatrix4f_createPerspectiveFovLH(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxMatrix4f_createPerspectiveFovRH(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxMatrix4f_createPerspectiveLH__SWIG_0(float f, float f2, float f3, float f4, float f5, float f6);

    public static final native float[] SGSgfxMatrix4f_createPerspectiveLH__SWIG_1(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxMatrix4f_createPerspectiveRH__SWIG_0(float f, float f2, float f3, float f4, float f5, float f6);

    public static final native float[] SGSgfxMatrix4f_createPerspectiveRH__SWIG_1(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxMatrix4f_createRotationAxis(float[] fArr, float f);

    public static final native float[] SGSgfxMatrix4f_createRotationX(float f);

    public static final native float[] SGSgfxMatrix4f_createRotationY(float f);

    public static final native float[] SGSgfxMatrix4f_createRotationZ(float f);

    public static final native float[] SGSgfxMatrix4f_createRotation__SWIG_0(float[] fArr);

    public static final native float[] SGSgfxMatrix4f_createRotation__SWIG_1(float[] fArr, int i);

    public static final native float SGSgfxMatrix4f_getDeterminant(float[] fArr);

    public static final native float[] SGSgfxMatrix4f_getFullTranslation(float[] fArr);

    public static final native float[] SGSgfxMatrix4f_getQuaternion(float[] fArr);

    public static final native float[] SGSgfxMatrix4f_getTranslation(float[] fArr);

    public static final native void SGSgfxMatrix4f_interpolateLineary(float[] fArr, float[] fArr2, float f);

    public static final native void SGSgfxMatrix4f_interpolateSpherically(float[] fArr, float[] fArr2, float f);

    public static final native void SGSgfxMatrix4f_inverse(float[] fArr);

    public static final native boolean SGSgfxMatrix4f_isIdentity(float[] fArr);

    public static final native void SGSgfxMatrix4f_multiply(float[] fArr, float[] fArr2);

    public static final native void SGSgfxMatrix4f_multiplyByElements(float[] fArr, float[] fArr2);

    public static final native void SGSgfxMatrix4f_rotateAxis(float[] fArr, float[] fArr2, float f);

    public static final native float[] SGSgfxMatrix4f_rotateQuaternion(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_rotateVector__SWIG_0(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_rotateVector__SWIG_1(float[] fArr, float[] fArr2);

    public static final native void SGSgfxMatrix4f_rotateX(float[] fArr, float f);

    public static final native void SGSgfxMatrix4f_rotateY(float[] fArr, float f);

    public static final native void SGSgfxMatrix4f_rotateZ(float[] fArr, float f);

    public static final native void SGSgfxMatrix4f_rotate__SWIG_0(float[] fArr, float[] fArr2);

    public static final native void SGSgfxMatrix4f_rotate__SWIG_1(float[] fArr, float[] fArr2, int i);

    public static final native void SGSgfxMatrix4f_scale(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_transformVector__SWIG_0(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_transformVector__SWIG_1(float[] fArr, float[] fArr2);

    public static final native void SGSgfxMatrix4f_translate(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_translateVector__SWIG_0(float[] fArr, float[] fArr2);

    public static final native float[] SGSgfxMatrix4f_translateVector__SWIG_1(float[] fArr, float[] fArr2);

    public static final native void SGSgfxMatrix4f_transpose(float[] fArr);

    public static final native float[] SGSgfxQuaternion_createRotationAxis__SWIG_0(float f, float f2, float f3, float f4);

    public static final native float[] SGSgfxQuaternion_createRotationAxis__SWIG_1(float[] fArr, float f);

    public static final native float[] SGSgfxQuaternion_createRotationEuler__SWIG_0(float f, float f2, float f3, int i);

    public static final native float[] SGSgfxQuaternion_createRotationEuler__SWIG_1(float[] fArr, int i);

    public static final native float[] SGSgfxQuaternion_createRotationX(float f);

    public static final native float[] SGSgfxQuaternion_createRotationY(float f);

    public static final native float[] SGSgfxQuaternion_createRotationZ(float f);

    public static final native float[] SGSgfxQuaternion_getEulerAnglesXYZ(float[] fArr);

    public static final native void SGSgfxQuaternion_interpolateSpherically(float[] fArr, float[] fArr2, float f, float f2);

    public static final native void SGSgfxQuaternion_rotateAxis__SWIG_0(float[] fArr, float f, float f2, float f3, float f4);

    public static final native void SGSgfxQuaternion_rotateAxis__SWIG_1(float[] fArr, float[] fArr2, float f);

    public static final native void SGSgfxQuaternion_rotateEuler__SWIG_0(float[] fArr, float f, float f2, float f3, int i);

    public static final native void SGSgfxQuaternion_rotateEuler__SWIG_1(float[] fArr, float[] fArr2, int i);

    public static final native void SGSgfxQuaternion_rotateX(float[] fArr, float f);

    public static final native void SGSgfxQuaternion_rotateY(float[] fArr, float f);

    public static final native void SGSgfxQuaternion_rotateZ(float[] fArr, float f);

    public static final native float[] SGSgfxQuaternion_transformVector(float[] fArr, float[] fArr2);

    public static boolean SwigDirector_SGRegistrator_AddToManagementList(SGRegistrator jself, long aPointer) {
        return jself.AddToManagementList(aPointer);
    }

    public static boolean SwigDirector_SGRegistrator_Deregister(SGRegistrator jself, long aPointer) {
        return jself.Deregister(aPointer);
    }

    public static Object SwigDirector_SGRegistrator_GetObjectByPointer(SGRegistrator jself, long aPointer) {
        return jself.GetObjectByPointer(aPointer);
    }

    public static boolean SwigDirector_SGRegistrator_Register(SGRegistrator jself, Object obj, long aPointer) {
        return jself.Register(obj, aPointer);
    }

    public static boolean SwigDirector_SGRegistrator_RemoveFromManagementList(SGRegistrator jself, long aPointer) {
        return jself.RemoveFromManagementList(aPointer);
    }

    static synchronized Object createObjectFromNativePtr(Class objClass, long cPtr, boolean cMemoryOwn) {
        Object ret;
        synchronized (SGJNI.class) {
            ret = null;
            Constructor<?> swigCtr = (Constructor) mCache.get(objClass);
            if (swigCtr == null) {
                try {
                    swigCtr = objClass.getDeclaredConstructor(new Class[]{Long.TYPE, Boolean.TYPE});
                    if (swigCtr != null) {
                        swigCtr.setAccessible(true);
                        mCache.put(objClass, swigCtr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (swigCtr != null) {
                ret = swigCtr.newInstance(new Object[]{Long.valueOf(cPtr), Boolean.valueOf(cMemoryOwn)});
            }
        }
        return ret;
    }

    public static final native void delete_SGAngleConverter(long j);

    public static final native void delete_SGConfiguration(long j);

    public static final native void delete_SGRegistrator(long j);

    public static final native void delete_SGSgfxBox3f(long j);

    public static final native void delete_SGSgfxMatrix4f(long j);

    public static final native void delete_SGSgfxQuaternion(long j);

    static int getData(Enum param) {
        if (param != null) {
            return param.ordinal();
        }
        throw new NullPointerException();
    }

    public static final native long new_SGRegistrator();

    private static final native void swig_module_init();
}
