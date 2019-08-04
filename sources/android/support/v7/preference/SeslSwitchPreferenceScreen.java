package android.support.v7.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.SeslViewReflector;
import android.support.v4.widget.SeslHoverPopupWindowReflector;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;

public class SeslSwitchPreferenceScreen extends SwitchPreferenceCompat {
    private OnKeyListener mSwitchKeyListener;

    /* renamed from: android.support.v7.preference.SeslSwitchPreferenceScreen$1 */
    class C02671 implements OnKeyListener {
        C02671() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            boolean handled = false;
            switch (event.getKeyCode()) {
                case 21:
                    if (SeslSwitchPreferenceScreen.this.isChecked()) {
                        if (SeslSwitchPreferenceScreen.this.callChangeListener(Boolean.valueOf(false))) {
                            SeslSwitchPreferenceScreen.this.setChecked(false);
                        }
                        handled = true;
                        break;
                    }
                    break;
                case 22:
                    if (!SeslSwitchPreferenceScreen.this.isChecked()) {
                        if (SeslSwitchPreferenceScreen.this.callChangeListener(Boolean.valueOf(true))) {
                            SeslSwitchPreferenceScreen.this.setChecked(true);
                        }
                        handled = true;
                        break;
                    }
                    break;
            }
            if (handled) {
                return true;
            }
            return false;
        }
    }

    public SeslSwitchPreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSwitchKeyListener = new C02671();
        TypedArray a = context.obtainStyledAttributes(attrs, C0263R.styleable.Preference, defStyleAttr, defStyleRes);
        String fragment = a.getString(C0263R.styleable.Preference_android_fragment);
        if (fragment == null || fragment.equals("")) {
            Log.w("SwitchPreferenceScreen", "SwitchPreferenceScreen should get fragment property. Fragment property does not exsit in SwitchPreferenceScreen");
        }
        setLayoutResource(C0263R.layout.sesl_switch_preference_screen);
        setWidgetLayoutResource(C0263R.layout.sesl_switch_preference_screen_widget_divider);
        a.recycle();
    }

    public SeslSwitchPreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslSwitchPreferenceScreen(Context context, AttributeSet attrs) {
        this(context, attrs, C0263R.attr.switchPreferenceStyle);
    }

    protected void onClick() {
    }

    protected void callClickListener() {
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setOnKeyListener(this.mSwitchKeyListener);
        TextView titleView = (TextView) holder.findViewById(16908310);
        View switchView = holder.findViewById(16908352);
        if (titleView != null && switchView != null) {
            SeslViewReflector.semSetHoverPopupType(switchView, SeslHoverPopupWindowReflector.getField_TYPE_NONE());
            switchView.setContentDescription(titleView.getText().toString());
        }
    }
}
