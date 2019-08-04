package android.support.v7.widget;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.view.View;

public class TooltipCompat {
    private static final ViewCompatImpl IMPL;

    private interface ViewCompatImpl {
        void setTooltipNull(boolean z);

        void setTooltipPosition(int i, int i2, int i3);

        void setTooltipText(View view, CharSequence charSequence);
    }

    @TargetApi(26)
    private static class Api26ViewCompatImpl implements ViewCompatImpl {
        private Api26ViewCompatImpl() {
        }

        public void setTooltipText(View view, CharSequence tooltipText) {
            TooltipCompatHandler.setTooltipText(view, tooltipText);
        }

        public void setTooltipPosition(int x, int y, int layoutDirection) {
            TooltipCompatHandler.setTooltipPosition(x, y, layoutDirection);
        }

        public void setTooltipNull(boolean tooltipNull) {
            TooltipCompatHandler.setTooltipNull(tooltipNull);
        }
    }

    private static class BaseViewCompatImpl implements ViewCompatImpl {
        private BaseViewCompatImpl() {
        }

        public void setTooltipText(View view, CharSequence tooltipText) {
            TooltipCompatHandler.setTooltipText(view, tooltipText);
        }

        public void setTooltipPosition(int x, int y, int layoutDirection) {
            TooltipCompatHandler.setTooltipPosition(x, y, layoutDirection);
        }

        public void setTooltipNull(boolean tooltipNull) {
            TooltipCompatHandler.setTooltipNull(tooltipNull);
        }
    }

    static {
        if (VERSION.SDK_INT >= 26) {
            IMPL = new Api26ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static void setTooltipText(View view, CharSequence tooltipText) {
        IMPL.setTooltipText(view, tooltipText);
    }

    public static void setTooltipPosition(int x, int y, int layoutDirection) {
        IMPL.setTooltipPosition(x, y, layoutDirection);
    }

    public static void setTooltipNull(boolean tooltipNull) {
        IMPL.setTooltipNull(tooltipNull);
    }

    private TooltipCompat() {
    }
}
