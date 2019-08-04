package com.samsung.android.sdk.sgi.base;

final class SGSgfxQuaternion {
    SGSgfxQuaternion() {
    }

    public static SGQuaternion createRotationAxis(float xAxis, float yAxis, float zAxis, float angleRad) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationAxis__SWIG_0(xAxis, yAxis, zAxis, angleRad));
    }

    public static SGQuaternion createRotationAxis(SGVector3f axis, float angleRad) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationAxis__SWIG_1(axis.getData(), angleRad));
    }

    public static SGQuaternion createRotationEuler(float xAngleRad, float yAngleRad, float zAngleRad, SGRotationOrder order) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationEuler__SWIG_0(xAngleRad, yAngleRad, zAngleRad, SGJNI.getData(order)));
    }

    public static SGQuaternion createRotationEuler(SGVector3f anglesRad, SGRotationOrder order) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationEuler__SWIG_1(anglesRad.getData(), SGJNI.getData(order)));
    }

    public static SGQuaternion createRotationX(float angleRad) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationX(angleRad));
    }

    public static SGQuaternion createRotationY(float angleRad) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationY(angleRad));
    }

    public static SGQuaternion createRotationZ(float angleRad) {
        return new SGQuaternion(SGJNI.SGSgfxQuaternion_createRotationZ(angleRad));
    }

    public static SGVector3f getEulerAnglesXYZ(SGQuaternion current) {
        return new SGVector3f(SGJNI.SGSgfxQuaternion_getEulerAnglesXYZ(current.getData()));
    }

    public static void interpolateSpherically(SGQuaternion current, SGQuaternion to, float coeff, float epsilon) {
        SGJNI.SGSgfxQuaternion_interpolateSpherically(current.getData(), to.getData(), coeff, epsilon);
    }

    public static void rotateAxis(SGQuaternion current, float xAxis, float yAxis, float zAxis, float angleRad) {
        SGJNI.SGSgfxQuaternion_rotateAxis__SWIG_0(current.getData(), xAxis, yAxis, zAxis, angleRad);
    }

    public static void rotateAxis(SGQuaternion current, SGVector3f axis, float angleRad) {
        SGJNI.SGSgfxQuaternion_rotateAxis__SWIG_1(current.getData(), axis.getData(), angleRad);
    }

    public static void rotateEuler(SGQuaternion current, float xAngleRad, float yAngleRad, float zAngleRad, SGRotationOrder order) {
        SGJNI.SGSgfxQuaternion_rotateEuler__SWIG_0(current.getData(), xAngleRad, yAngleRad, zAngleRad, SGJNI.getData(order));
    }

    public static void rotateEuler(SGQuaternion current, SGVector3f anglesRad, SGRotationOrder order) {
        SGJNI.SGSgfxQuaternion_rotateEuler__SWIG_1(current.getData(), anglesRad.getData(), SGJNI.getData(order));
    }

    public static void rotateX(SGQuaternion current, float angleRad) {
        SGJNI.SGSgfxQuaternion_rotateX(current.getData(), angleRad);
    }

    public static void rotateY(SGQuaternion current, float angleRad) {
        SGJNI.SGSgfxQuaternion_rotateY(current.getData(), angleRad);
    }

    public static void rotateZ(SGQuaternion current, float angleRad) {
        SGJNI.SGSgfxQuaternion_rotateZ(current.getData(), angleRad);
    }

    public static SGVector3f transformVector(SGQuaternion current, SGVector3f value) {
        return new SGVector3f(SGJNI.SGSgfxQuaternion_transformVector(current.getData(), value.getData()));
    }
}
