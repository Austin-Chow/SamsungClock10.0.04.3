package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.appcompat.C0247R;
import android.support.v7.widget.SeslColorPicker;
import android.support.v7.widget.SeslColorPicker.OnColorChangedListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SeslColorPickerDialogFragment extends AppCompatDialogFragment implements OnClickListener {
    private static final String KEY_CURRENT_COLOR = "current_color";
    private static final String KEY_OPACITY_BAR_ENABLED = "opacity_bar_enabled";
    private static final String KEY_RECENTLY_USED_COLORS = "recently_used_colors";
    private static final String TAG = "SeslColorPickerDialogFragment";
    private AlertDialog mAlertDialog;
    private SeslColorPicker mColorPicker;
    private Integer mCurrentColor;
    private boolean mIsTransparencyControlEnabled;
    private Integer mNewColor;
    private OnColorChangedListener mOnColorChangedListener;
    private OnColorSetListener mOnColorSetListener;
    private int[] mRecentlyUsedColors;

    public interface OnColorSetListener {
        void onColorSet(int i);
    }

    public void setOnColorChangedListener(OnColorChangedListener changeListener) {
        this.mOnColorChangedListener = changeListener;
    }

    public SeslColorPickerDialogFragment() {
        this.mCurrentColor = null;
        this.mNewColor = null;
        this.mRecentlyUsedColors = null;
        this.mIsTransparencyControlEnabled = false;
    }

    public SeslColorPickerDialogFragment(OnColorSetListener listener) {
        this.mCurrentColor = null;
        this.mNewColor = null;
        this.mRecentlyUsedColors = null;
        this.mIsTransparencyControlEnabled = false;
        this.mOnColorSetListener = listener;
    }

    public SeslColorPickerDialogFragment(OnColorSetListener listener, int currentColor) {
        this(listener);
        this.mCurrentColor = Integer.valueOf(currentColor);
    }

    public SeslColorPickerDialogFragment(OnColorSetListener listener, int[] recentlyUsedColors) {
        this(listener);
        this.mRecentlyUsedColors = recentlyUsedColors;
    }

    public SeslColorPickerDialogFragment(OnColorSetListener listener, int currentColor, int[] recentlyUsedColors) {
        this(listener);
        this.mCurrentColor = Integer.valueOf(currentColor);
        this.mRecentlyUsedColors = recentlyUsedColors;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mRecentlyUsedColors = savedInstanceState.getIntArray(KEY_RECENTLY_USED_COLORS);
            this.mCurrentColor = (Integer) savedInstanceState.getSerializable(KEY_CURRENT_COLOR);
            this.mIsTransparencyControlEnabled = savedInstanceState.getBoolean(KEY_OPACITY_BAR_ENABLED);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View colorPickerDialogView = inflater.inflate(C0247R.layout.sesl_color_picker_dialog, null);
        this.mColorPicker = (SeslColorPicker) colorPickerDialogView.findViewById(C0247R.id.sesl_color_picker_content_view);
        if (this.mCurrentColor != null) {
            this.mColorPicker.getRecentColorInfo().setCurrentColor(this.mCurrentColor);
        }
        if (this.mNewColor != null) {
            this.mColorPicker.getRecentColorInfo().setNewColor(this.mNewColor);
        }
        if (this.mRecentlyUsedColors != null) {
            this.mColorPicker.getRecentColorInfo().initRecentColorInfo(this.mRecentlyUsedColors);
        }
        this.mColorPicker.setOpacityBarEnabled(this.mIsTransparencyControlEnabled);
        this.mColorPicker.updateRecentColorLayout();
        this.mColorPicker.setOnColorChangedListener(this.mOnColorChangedListener);
        this.mAlertDialog.setView(colorPickerDialogView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog;
        Context themeContext = getContext();
        TypedValue outValue = new TypedValue();
        themeContext.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            alertDialog = new AlertDialog(getActivity(), C0247R.style.ThemeOverlay_AppCompat_Light_Dialog);
        } else {
            alertDialog = new AlertDialog(getActivity(), C0247R.style.ThemeOverlay_AppCompat_Dialog);
        }
        this.mAlertDialog = alertDialog;
        this.mAlertDialog.setButton(-1, themeContext.getString(C0247R.string.sesl_picker_done), (OnClickListener) this);
        this.mAlertDialog.setButton(-2, themeContext.getString(17039360), (OnClickListener) this);
        return this.mAlertDialog;
    }

    public void onClick(DialogInterface dialog, int whichButton) {
        switch (whichButton) {
            case -2:
                dialog.dismiss();
                return;
            case -1:
                this.mColorPicker.saveSelectedColor();
                if (this.mOnColorSetListener == null) {
                    return;
                }
                if (this.mCurrentColor == null || this.mColorPicker.isUserInputValid()) {
                    this.mOnColorSetListener.onColorSet(this.mColorPicker.getRecentColorInfo().getSelectedColor().intValue());
                    return;
                } else {
                    this.mOnColorSetListener.onColorSet(this.mCurrentColor.intValue());
                    return;
                }
            default:
                return;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(this.mColorPicker.getRecentColorInfo().getSelectedColor());
        outState.putIntArray(KEY_RECENTLY_USED_COLORS, this.mRecentlyUsedColors);
        outState.putSerializable(KEY_CURRENT_COLOR, this.mCurrentColor);
        outState.putBoolean(KEY_OPACITY_BAR_ENABLED, this.mIsTransparencyControlEnabled);
    }

    public SeslColorPicker getColorPicker() {
        return this.mColorPicker;
    }

    public void setNewColor(Integer newColor) {
        this.mNewColor = newColor;
    }

    public void setTransparencyControlEnabled(boolean enabled) {
        this.mIsTransparencyControlEnabled = enabled;
    }
}
