package com.samsung.android.sdk.sgi.base;

final class SGSgfxBox3f {
    SGSgfxBox3f() {
    }

    public static SGBox3f createTransformed(SGBox3f current, SGMatrix4f tran) {
        return new SGBox3f(SGJNI.SGSgfxBox3f_createTransformed(current.getData(), tran.getData()));
    }
}
