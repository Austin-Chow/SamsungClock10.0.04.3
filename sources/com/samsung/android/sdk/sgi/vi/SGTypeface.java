package com.samsung.android.sdk.sgi.vi;

public class SGTypeface {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    public static final SGTypeface DEFAULT = create(null, 0);
    public static final SGTypeface DEFAULT_BOLD = create(null, 1);
    public static final int ITALIC = 2;
    public static final SGTypeface MONOSPACE = create("monospace", 0);
    public static final int NORMAL = 0;
    public static final SGTypeface SANS_SERIF = create("sans-serif", 0);
    public static final SGTypeface SERIF = create("serif", 0);
    private boolean swigCMemOwn;
    private long swigCPtr;

    SGTypeface(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    public SGTypeface(SGTypeface other) {
        this(SGJNI.new_SGTypeface(getCPtr(other), other), true);
    }

    public static SGTypeface create(int style) {
        long cPtr = SGJNI.SGTypeface_create__SWIG_1(style);
        return cPtr != 0 ? (SGTypeface) SGJNI.createObjectFromNativePtr(SGTypeface.class, cPtr, true) : null;
    }

    public static SGTypeface create(String familyName, int style) {
        long cPtr = SGJNI.SGTypeface_create__SWIG_0(familyName, style);
        return cPtr != 0 ? (SGTypeface) SGJNI.createObjectFromNativePtr(SGTypeface.class, cPtr, true) : null;
    }

    public static long getCPtr(SGTypeface obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    private int getHandle() {
        return SGJNI.SGTypeface_getHandle(this.swigCPtr, this);
    }

    private static void reinitDefaultFonts() {
        SGJNI.SGTypeface_reinitDefaultFonts();
    }

    private static void resetDefault(SGTypeface typeFace, String familyName, int style) {
        SGJNI.SGTypeface_resetDefault(getCPtr(typeFace), typeFace, familyName, style);
    }

    public static void resetDefaults() {
        reinitDefaultFonts();
        resetDefault(DEFAULT, null, 0);
        resetDefault(DEFAULT_BOLD, null, 1);
        resetDefault(SANS_SERIF, "sans-serif", 0);
        resetDefault(SERIF, "serif", 0);
        resetDefault(MONOSPACE, "monospace", 0);
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof SGTypeface) && ((SGTypeface) other).getHandle() == getHandle();
    }

    public void finalize() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SGJNI.delete_SGTypeface(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public String getFamilyName() {
        return SGJNI.SGTypeface_getFamilyName(this.swigCPtr, this);
    }

    public int getStyle() {
        return SGJNI.SGTypeface_getStyle(this.swigCPtr, this);
    }

    public int hashCode() {
        long handle = (long) getHandle();
        return (handle >>> 32) > 0 ? ((int) handle) + 1 : (int) handle;
    }
}
