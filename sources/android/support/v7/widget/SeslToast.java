package android.support.v7.widget;

import android.content.Context;
import android.support.v4.widget.SeslToastReflector;
import android.support.v7.appcompat.C0247R;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

public class SeslToast extends Toast {
    public SeslToast(Context context) {
        super(context);
    }

    public static SeslToast makeText(Context context, CharSequence text, int duration) {
        final SeslToast result = new SeslToast(context);
        View v = LayoutInflater.from(context).inflate(C0247R.layout.sesl_transient_notification, null);
        v.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == 0 || event.getAction() == 4) {
                    result.cancel();
                }
                return true;
            }
        });
        ((TextView) v.findViewById(C0247R.id.message)).setText(text);
        result.setView(v);
        result.setDuration(duration);
        return result;
    }

    public static SeslToast makeTextForTooltip(Context context, CharSequence text, int duration) {
        final SeslToast result = new SeslToast(context);
        View v = LayoutInflater.from(context).inflate(C0247R.layout.sesl_transient_notification_actionbar, null);
        v.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == 0 || event.getAction() == 4) {
                    result.cancel();
                }
                return true;
            }
        });
        ((TextView) v.findViewById(C0247R.id.message)).setText(text);
        result.setView(v);
        result.setDuration(duration);
        return result;
    }

    public static Toast twMakeTextForTooltip(Context context, CharSequence text, int duration) {
        return (Toast) SeslToastReflector.twMakeText(new Toast(context), context, text, duration);
    }
}
