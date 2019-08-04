package com.samsung.android.sdk.sgi.base;

final class SGSgfxMatrix4f {
    SGSgfxMatrix4f() {
    }

    public static SGMatrix4f createLookAtLH(SGVector3f eye, SGVector3f at, SGVector3f up) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createLookAtLH(eye.getData(), at.getData(), up.getData()));
    }

    public static SGMatrix4f createLookAtRH(SGVector3f eye, SGVector3f at, SGVector3f up) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createLookAtRH(eye.getData(), at.getData(), up.getData()));
    }

    public static SGMatrix4f createOrthoLH(float width, float height, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createOrthoLH__SWIG_1(width, height, zNear, zFar));
    }

    public static SGMatrix4f createOrthoLH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createOrthoLH__SWIG_0(left, right, top, bottom, zNear, zFar));
    }

    public static SGMatrix4f createOrthoRH(float width, float height, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createOrthoRH__SWIG_1(width, height, zNear, zFar));
    }

    public static SGMatrix4f createOrthoRH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createOrthoRH__SWIG_0(left, right, top, bottom, zNear, zFar));
    }

    public static SGMatrix4f createPerspectiveFovLH(float fovY, float aspect, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createPerspectiveFovLH(fovY, aspect, zNear, zFar));
    }

    public static SGMatrix4f createPerspectiveFovRH(float fovY, float aspect, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createPerspectiveFovRH(fovY, aspect, zNear, zFar));
    }

    public static SGMatrix4f createPerspectiveLH(float width, float height, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createPerspectiveLH__SWIG_1(width, height, zNear, zFar));
    }

    public static SGMatrix4f createPerspectiveLH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createPerspectiveLH__SWIG_0(left, right, top, bottom, zNear, zFar));
    }

    public static SGMatrix4f createPerspectiveRH(float width, float height, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createPerspectiveRH__SWIG_1(width, height, zNear, zFar));
    }

    public static SGMatrix4f createPerspectiveRH(float left, float right, float top, float bottom, float zNear, float zFar) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createPerspectiveRH__SWIG_0(left, right, top, bottom, zNear, zFar));
    }

    public static SGMatrix4f createRotation(SGQuaternion rotation) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createRotation__SWIG_0(rotation.getData()));
    }

    public static SGMatrix4f createRotation(SGVector3f anglesRad, SGRotationOrder order) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createRotation__SWIG_1(anglesRad.getData(), SGJNI.getData(order)));
    }

    public static SGMatrix4f createRotationAxis(SGVector3f axis, float angleRad) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createRotationAxis(axis.getData(), angleRad));
    }

    public static SGMatrix4f createRotationX(float angleRad) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createRotationX(angleRad));
    }

    public static SGMatrix4f createRotationY(float angleRad) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createRotationY(angleRad));
    }

    public static SGMatrix4f createRotationZ(float angleRad) {
        return new SGMatrix4f(SGJNI.SGSgfxMatrix4f_createRotationZ(angleRad));
    }

    public static float getDeterminant(SGMatrix4f current) {
        return SGJNI.SGSgfxMatrix4f_getDeterminant(current.getData());
    }

    public static SGVector4f getFullTranslation(SGMatrix4f current) {
        return new SGVector4f(SGJNI.SGSgfxMatrix4f_getFullTranslation(current.getData()));
    }

    public static SGQuaternion getQuaternion(SGMatrix4f current) {
        return new SGQuaternion(SGJNI.SGSgfxMatrix4f_getQuaternion(current.getData()));
    }

    public static SGVector3f getTranslation(SGMatrix4f current) {
        return new SGVector3f(SGJNI.SGSgfxMatrix4f_getTranslation(current.getData()));
    }

    public static void interpolateLineary(SGMatrix4f current, SGMatrix4f to, float coeff) {
        SGJNI.SGSgfxMatrix4f_interpolateLineary(current.getData(), to.getData(), coeff);
    }

    public static void interpolateSpherically(SGMatrix4f current, SGMatrix4f to, float coeff) {
        SGJNI.SGSgfxMatrix4f_interpolateSpherically(current.getData(), to.getData(), coeff);
    }

    public static void inverse(SGMatrix4f current) {
        SGJNI.SGSgfxMatrix4f_inverse(current.getData());
    }

    public static boolean isIdentity(SGMatrix4f current) {
        return SGJNI.SGSgfxMatrix4f_isIdentity(current.getData());
    }

    public static void multiply(SGMatrix4f current, SGMatrix4f other) {
        SGJNI.SGSgfxMatrix4f_multiply(current.getData(), other.getData());
    }

    public static void multiplyByElements(SGMatrix4f current, SGMatrix4f other) {
        SGJNI.SGSgfxMatrix4f_multiplyByElements(current.getData(), other.getData());
    }

    public static void rotate(SGMatrix4f current, SGQuaternion rotation) {
        SGJNI.SGSgfxMatrix4f_rotate__SWIG_0(current.getData(), rotation.getData());
    }

    public static void rotate(SGMatrix4f current, SGVector3f anglesRad, SGRotationOrder order) {
        SGJNI.SGSgfxMatrix4f_rotate__SWIG_1(current.getData(), anglesRad.getData(), SGJNI.getData(order));
    }

    public static void rotateAxis(SGMatrix4f current, SGVector3f axis, float angleRad) {
        SGJNI.SGSgfxMatrix4f_rotateAxis(current.getData(), axis.getData(), angleRad);
    }

    public static SGQuaternion rotateQuaternion(SGMatrix4f current, SGQuaternion quaternion) {
        return new SGQuaternion(SGJNI.SGSgfxMatrix4f_rotateQuaternion(current.getData(), quaternion.getData()));
    }

    public static SGVector3f rotateVector(SGMatrix4f current, SGVector3f vector) {
        return new SGVector3f(SGJNI.SGSgfxMatrix4f_rotateVector__SWIG_0(current.getData(), vector.getData()));
    }

    public static SGVector4f rotateVector(SGMatrix4f current, SGVector4f vector) {
        return new SGVector4f(SGJNI.SGSgfxMatrix4f_rotateVector__SWIG_1(current.getData(), vector.getData()));
    }

    public static void rotateX(SGMatrix4f current, float angleRad) {
        SGJNI.SGSgfxMatrix4f_rotateX(current.getData(), angleRad);
    }

    public static void rotateY(SGMatrix4f current, float angleRad) {
        SGJNI.SGSgfxMatrix4f_rotateY(current.getData(), angleRad);
    }

    public static void rotateZ(SGMatrix4f current, float angleRad) {
        SGJNI.SGSgfxMatrix4f_rotateZ(current.getData(), angleRad);
    }

    public static void scale(SGMatrix4f current, SGVector3f scale) {
        SGJNI.SGSgfxMatrix4f_scale(current.getData(), scale.getData());
    }

    public static SGVector3f transformVector(SGMatrix4f current, SGVector3f vector) {
        return new SGVector3f(SGJNI.SGSgfxMatrix4f_transformVector__SWIG_0(current.getData(), vector.getData()));
    }

    public static SGVector4f transformVector(SGMatrix4f current, SGVector4f vector) {
        return new SGVector4f(SGJNI.SGSgfxMatrix4f_transformVector__SWIG_1(current.getData(), vector.getData()));
    }

    public static void translate(SGMatrix4f current, SGVector3f vector) {
        SGJNI.SGSgfxMatrix4f_translate(current.getData(), vector.getData());
    }

    public static SGVector3f translateVector(SGMatrix4f current, SGVector3f vector) {
        return new SGVector3f(SGJNI.SGSgfxMatrix4f_translateVector__SWIG_0(current.getData(), vector.getData()));
    }

    public static SGVector4f translateVector(SGMatrix4f current, SGVector4f vector) {
        return new SGVector4f(SGJNI.SGSgfxMatrix4f_translateVector__SWIG_1(current.getData(), vector.getData()));
    }

    public static void transpose(SGMatrix4f current) {
        SGJNI.SGSgfxMatrix4f_transpose(current.getData());
    }
}
