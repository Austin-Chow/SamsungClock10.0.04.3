package android.support.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi21 extends ViewUtilsApi19 {
    private static Method sTransformMatrixToGlobalMethod;
    private static boolean sTransformMatrixToGlobalMethodFetched;
    private static Method sTransformMatrixToLocalMethod;
    private static boolean sTransformMatrixToLocalMethodFetched;

    ViewUtilsApi21() {
    }

    public void transformMatrixToGlobal(View view, Matrix matrix) {
        fetchTransformMatrixToGlobalMethod();
        if (sTransformMatrixToGlobalMethod != null) {
            try {
                sTransformMatrixToGlobalMethod.invoke(view, new Object[]{matrix});
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
    }

    public void transformMatrixToLocal(View view, Matrix matrix) {
        fetchTransformMatrixToLocalMethod();
        if (sTransformMatrixToLocalMethod != null) {
            try {
                sTransformMatrixToLocalMethod.invoke(view, new Object[]{matrix});
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
    }

    private void fetchTransformMatrixToGlobalMethod() {
        if (!sTransformMatrixToGlobalMethodFetched) {
            try {
                sTransformMatrixToGlobalMethod = View.class.getDeclaredMethod("transformMatrixToGlobal", new Class[]{Matrix.class});
                sTransformMatrixToGlobalMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToGlobal method", e);
            }
            sTransformMatrixToGlobalMethodFetched = true;
        }
    }

    private void fetchTransformMatrixToLocalMethod() {
        if (!sTransformMatrixToLocalMethodFetched) {
            try {
                sTransformMatrixToLocalMethod = View.class.getDeclaredMethod("transformMatrixToLocal", new Class[]{Matrix.class});
                sTransformMatrixToLocalMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToLocal method", e);
            }
            sTransformMatrixToLocalMethodFetched = true;
        }
    }
}
