package android.support.v4.widget;

import android.support.v4.SeslBaseReflector;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SeslAbsListViewReflector {
    static final AbsListViewBaseImpl IMPL = new AbsListViewApi21Impl();
    private static final Class<?> mClass = AbsListView.class;

    private interface AbsListViewBaseImpl {
        EdgeEffect getField_mEdgeGlowTop(AbsListView absListView);

        void positionSelector(AbsListView absListView, int i, View view);

        void setField_mEdgeGlowBottom(AbsListView absListView, EdgeEffect edgeEffect);

        void setField_mEdgeGlowTop(AbsListView absListView, EdgeEffect edgeEffect);
    }

    private static class AbsListViewApi21Impl implements AbsListViewBaseImpl {
        private AbsListViewApi21Impl() {
        }

        public EdgeEffect getField_mEdgeGlowTop(AbsListView listView) {
            Field field = SeslBaseReflector.getDeclaredField(SeslAbsListViewReflector.mClass, "mEdgeGlowTop");
            if (field != null) {
                field.setAccessible(true);
                Object object = SeslBaseReflector.get(listView, field);
                if (object instanceof EdgeEffect) {
                    return (EdgeEffect) object;
                }
            }
            return null;
        }

        public void setField_mEdgeGlowTop(AbsListView listView, EdgeEffect edgeEffect) {
            Field field = SeslBaseReflector.getDeclaredField(SeslAbsListViewReflector.mClass, "mEdgeGlowTop");
            if (field != null) {
                field.setAccessible(true);
                SeslBaseReflector.set(listView, field, edgeEffect);
            }
        }

        public void setField_mEdgeGlowBottom(AbsListView listView, EdgeEffect edgeEffect) {
            Field field = SeslBaseReflector.getDeclaredField(SeslAbsListViewReflector.mClass, "mEdgeGlowBottom");
            if (field != null) {
                field.setAccessible(true);
                SeslBaseReflector.set(listView, field, edgeEffect);
            }
        }

        public void positionSelector(AbsListView listView, int position, View sel) {
            Method method = SeslBaseReflector.getDeclaredMethod(SeslAbsListViewReflector.mClass, "positionSelector", Integer.TYPE, View.class);
            if (method != null) {
                method.setAccessible(true);
                SeslBaseReflector.invoke(listView, method, Integer.valueOf(position), sel);
            }
        }
    }

    public static EdgeEffect getField_mEdgeGlowTop(AbsListView listView) {
        return IMPL.getField_mEdgeGlowTop(listView);
    }

    public static void setField_mEdgeGlowTop(AbsListView listView, EdgeEffect edgeEffect) {
        IMPL.setField_mEdgeGlowTop(listView, edgeEffect);
    }

    public static void setField_mEdgeGlowBottom(AbsListView listView, EdgeEffect edgeEffect) {
        IMPL.setField_mEdgeGlowBottom(listView, edgeEffect);
    }

    public static void positionSelector(AbsListView listView, int position, View sel) {
        IMPL.positionSelector(listView, position, sel);
    }
}
