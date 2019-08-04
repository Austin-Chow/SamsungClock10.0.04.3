package sec.color.gradient.interpolater;

public class EasingSineFunc {
    private static EasingSineFunc mInstance = null;

    private EasingSineFunc() {
    }

    public static EasingSineFunc getInstance() {
        if (mInstance == null) {
            mInstance = new EasingSineFunc();
        }
        return mInstance;
    }

    public float easeInOut(float t, float b, float c, float d) {
        return (((-c) / 2.0f) * (((float) Math.cos((3.141592653589793d * ((double) t)) / ((double) d))) - 1.0f)) + b;
    }
}
