package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import java.util.LinkedList;

public class SeslColorPicker extends LinearLayout {
    private static final int OPACITY_BAR_ENABLED_MARGIN_TOP = 6;
    static final int RECENT_COLOR_SLOT_COUNT = 6;
    private static final int RIPPLE_EFFECT_OPACITY = 61;
    private static final String TAG = "SeslColorPicker";
    static boolean sIsLightTheme;
    private String[] mColorDescription = null;
    private final Context mContext;
    private ImageView mCurrentColorView;
    private SeslGradientColorSeekBar mGradientColorSeekBar;
    private SeslGradientColorWheel mGradientColorWheel;
    private final OnClickListener mImageButtonClickListener = new C03556();
    private boolean mIsInputFromUser = false;
    private boolean mIsOpacityBarEnabled = false;
    private OnColorChangedListener mOnColorChangedListener;
    private SeslOpacitySeekBar mOpacitySeekBar;
    private FrameLayout mOpacitySeekBarContainer;
    private PickedColor mPickedColor;
    private ImageView mPickedColorView;
    private final SeslRecentColorInfo mRecentColorInfo;
    private LinearLayout mRecentColorListLayout;
    private final LinkedList<Integer> mRecentColorValues;
    private final Resources mResources;
    private GradientDrawable mSelectedColorBackground;

    /* renamed from: android.support.v7.widget.SeslColorPicker$1 */
    class C03501 implements OnWheelColorChangedListener {
        C03501() {
        }

        public void onWheelColorChanged(float hue, float saturation) {
            SeslColorPicker.this.mIsInputFromUser = true;
            SeslColorPicker.this.mPickedColor.setHS(hue, saturation);
            SeslColorPicker.this.updateCurrentColor();
        }
    }

    /* renamed from: android.support.v7.widget.SeslColorPicker$2 */
    class C03512 implements OnSeekBarChangeListener {
        C03512() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                SeslColorPicker.this.mIsInputFromUser = true;
            }
            SeslColorPicker.this.mPickedColor.setV(((float) seekBar.getProgress()) / ((float) seekBar.getMax()));
            int color = SeslColorPicker.this.mPickedColor.getColor();
            if (SeslColorPicker.this.mSelectedColorBackground != null) {
                SeslColorPicker.this.mSelectedColorBackground.setColor(color);
            }
            if (SeslColorPicker.this.mOpacitySeekBar != null) {
                SeslColorPicker.this.mOpacitySeekBar.changeColorBase(color);
            }
            if (SeslColorPicker.this.mOnColorChangedListener != null) {
                SeslColorPicker.this.mOnColorChangedListener.onColorChanged(color);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }
    }

    /* renamed from: android.support.v7.widget.SeslColorPicker$3 */
    class C03523 implements OnTouchListener {
        C03523() {
        }

        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    SeslColorPicker.this.mGradientColorSeekBar.setSelected(true);
                    return true;
                case 1:
                case 3:
                    SeslColorPicker.this.mGradientColorSeekBar.setSelected(false);
                    return false;
                default:
                    return false;
            }
        }
    }

    /* renamed from: android.support.v7.widget.SeslColorPicker$4 */
    class C03534 implements OnSeekBarChangeListener {
        C03534() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                SeslColorPicker.this.mIsInputFromUser = true;
            }
            SeslColorPicker.this.mPickedColor.setAlpha(progress);
            int color = SeslColorPicker.this.mPickedColor.getColor();
            if (SeslColorPicker.this.mSelectedColorBackground != null) {
                SeslColorPicker.this.mSelectedColorBackground.setColor(color);
            }
            if (SeslColorPicker.this.mOnColorChangedListener != null) {
                SeslColorPicker.this.mOnColorChangedListener.onColorChanged(color);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }
    }

    /* renamed from: android.support.v7.widget.SeslColorPicker$5 */
    class C03545 implements OnTouchListener {
        C03545() {
        }

        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    SeslColorPicker.this.mGradientColorSeekBar.setSelected(true);
                    return true;
                case 1:
                case 3:
                    SeslColorPicker.this.mGradientColorSeekBar.setSelected(false);
                    return false;
                default:
                    return false;
            }
        }
    }

    /* renamed from: android.support.v7.widget.SeslColorPicker$6 */
    class C03556 implements OnClickListener {
        C03556() {
        }

        public void onClick(View v) {
            int size = SeslColorPicker.this.mRecentColorValues.size();
            int i = 0;
            while (i < size && i < 6) {
                if (SeslColorPicker.this.mRecentColorListLayout.getChildAt(i).equals(v)) {
                    SeslColorPicker.this.mIsInputFromUser = true;
                    int color = ((Integer) SeslColorPicker.this.mRecentColorValues.get(i)).intValue();
                    SeslColorPicker.this.mPickedColor.setColor(color);
                    SeslColorPicker.this.mapColorOnColorWheel(color);
                    if (SeslColorPicker.this.mOnColorChangedListener != null) {
                        SeslColorPicker.this.mOnColorChangedListener.onColorChanged(color);
                    }
                }
                i++;
            }
        }
    }

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    private static class PickedColor {
        private int mAlpha = 255;
        private int mColor = -1;
        private float[] mHsv = new float[3];

        public void setColor(int color) {
            this.mColor = color;
            this.mAlpha = Color.alpha(this.mColor);
            Color.colorToHSV(this.mColor, this.mHsv);
        }

        public int getColor() {
            return this.mColor;
        }

        public void setHS(float hue, float saturation) {
            this.mHsv[0] = hue;
            this.mHsv[1] = saturation;
            this.mHsv[2] = 1.0f;
            this.mColor = Color.HSVToColor(this.mAlpha, this.mHsv);
        }

        public void setV(float value) {
            this.mHsv[2] = value;
            this.mColor = Color.HSVToColor(this.mAlpha, this.mHsv);
        }

        public void setAlpha(int alpha) {
            this.mAlpha = alpha;
            this.mColor = Color.HSVToColor(this.mAlpha, this.mHsv);
        }

        public float getV() {
            return this.mHsv[2];
        }

        public int getAlpha() {
            return this.mAlpha;
        }
    }

    public void setOnColorChangedListener(OnColorChangedListener changeListener) {
        this.mOnColorChangedListener = changeListener;
    }

    public SeslColorPicker(Context context, AttributeSet attrs) {
        boolean z = true;
        super(context, attrs);
        this.mContext = context;
        this.mResources = getResources();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
        if (outValue.data == 0) {
            z = false;
        }
        sIsLightTheme = z;
        LayoutInflater.from(context).inflate(C0247R.layout.sesl_color_picker_layout, this);
        this.mRecentColorInfo = new SeslRecentColorInfo();
        this.mRecentColorValues = this.mRecentColorInfo.getRecentColorInfo();
        init();
    }

    private void init() {
        this.mPickedColor = new PickedColor();
        initCurrentColorView();
        initGradientColorSeekBar();
        initOpacitySeekBar();
        initGradientColorWheel();
        initRecentColorLayout();
        updateCurrentColor();
        setInitialColors();
    }

    private void initCurrentColorView() {
        this.mCurrentColorView = (ImageView) findViewById(C0247R.id.sesl_color_picker_current_color_view);
        this.mPickedColorView = (ImageView) findViewById(C0247R.id.sesl_color_picker_picked_color_view);
        TextView currentColorText = (TextView) findViewById(C0247R.id.sesl_color_picker_current_color_text);
        TextView pickedColorText = (TextView) findViewById(C0247R.id.sesl_color_picker_picked_color_text);
        float currentFontScale = this.mResources.getConfiguration().fontScale;
        int selectedColorTextSize = this.mResources.getDimensionPixelOffset(C0247R.dimen.sesl_color_picker_selected_color_text_size);
        if (currentFontScale > 1.2f) {
            currentColorText.setTextSize(0, (float) Math.floor(Math.ceil((double) (((float) selectedColorTextSize) / currentFontScale)) * ((double) 1067030938)));
            pickedColorText.setTextSize(0, (float) Math.floor(Math.ceil((double) (((float) selectedColorTextSize) / currentFontScale)) * ((double) 1067030938)));
        }
        View currentColorContainer = findViewById(C0247R.id.sesl_color_picker_current_color_focus);
        View pickedColorContainer = findViewById(C0247R.id.sesl_color_picker_picked_color_focus);
        this.mSelectedColorBackground = (GradientDrawable) this.mPickedColorView.getBackground();
        this.mSelectedColorBackground.setColor(this.mPickedColor.getColor());
        currentColorContainer.setContentDescription(this.mResources.getString(C0247R.string.sesl_color_picker_current));
        pickedColorContainer.setContentDescription(this.mResources.getString(C0247R.string.sesl_color_picker_new));
    }

    private void initGradientColorWheel() {
        this.mGradientColorWheel = (SeslGradientColorWheel) findViewById(C0247R.id.sesl_color_picker_gradient_wheel);
        this.mGradientColorWheel.setColor(this.mPickedColor.getColor());
        this.mGradientColorWheel.setOnColorWheelInterface(new C03501());
        this.mGradientColorWheel.setContentDescription(this.mResources.getString(C0247R.string.sesl_color_picker_hue_and_saturation) + ", " + this.mResources.getString(C0247R.string.sesl_color_picker_color_wheel) + ", " + this.mResources.getString(C0247R.string.sesl_color_picker_double_tap_to_select));
    }

    private void initGradientColorSeekBar() {
        this.mGradientColorSeekBar = (SeslGradientColorSeekBar) findViewById(C0247R.id.sesl_color_picker_gradient_seekbar);
        FrameLayout gradientColorSeekBarContainer = (FrameLayout) findViewById(C0247R.id.sesl_color_picker_gradient_seekbar_container);
        this.mGradientColorSeekBar.init(this.mPickedColor.getColor());
        this.mGradientColorSeekBar.setOnSeekBarChangeListener(new C03512());
        this.mGradientColorSeekBar.setOnTouchListener(new C03523());
        gradientColorSeekBarContainer.setContentDescription(this.mResources.getString(C0247R.string.sesl_color_picker_brightness) + ", " + this.mResources.getString(C0247R.string.sesl_color_picker_slider) + ", " + this.mResources.getString(C0247R.string.sesl_color_picker_double_tap_to_select));
    }

    private void initOpacitySeekBar() {
        this.mOpacitySeekBar = (SeslOpacitySeekBar) findViewById(C0247R.id.sesl_color_picker_opacity_seekbar);
        this.mOpacitySeekBarContainer = (FrameLayout) findViewById(C0247R.id.sesl_color_picker_opacity_seekbar_container);
        if (!this.mIsOpacityBarEnabled) {
            this.mOpacitySeekBar.setVisibility(8);
            this.mOpacitySeekBarContainer.setVisibility(8);
        }
        this.mOpacitySeekBar.init(this.mPickedColor.getColor());
        this.mOpacitySeekBar.setOnSeekBarChangeListener(new C03534());
        this.mOpacitySeekBar.setOnTouchListener(new C03545());
        this.mOpacitySeekBarContainer.setContentDescription(this.mResources.getString(C0247R.string.sesl_color_picker_opacity) + ", " + this.mResources.getString(C0247R.string.sesl_color_picker_slider) + ", " + this.mResources.getString(C0247R.string.sesl_color_picker_double_tap_to_select));
    }

    private void initRecentColorLayout() {
        this.mRecentColorListLayout = (LinearLayout) findViewById(C0247R.id.sesl_color_picker_used_color_item_list_layout);
        this.mColorDescription = new String[]{this.mResources.getString(C0247R.string.sesl_color_picker_color_one), this.mResources.getString(C0247R.string.sesl_color_picker_color_two), this.mResources.getString(C0247R.string.sesl_color_picker_color_three), this.mResources.getString(C0247R.string.sesl_color_picker_color_four), this.mResources.getString(C0247R.string.sesl_color_picker_color_five), this.mResources.getString(C0247R.string.sesl_color_picker_color_six)};
        int emptyColor = ContextCompat.getColor(this.mContext, sIsLightTheme ? C0247R.color.sesl_color_picker_used_color_item_empty_slot_color_light : C0247R.color.sesl_color_picker_used_color_item_empty_slot_color_dark);
        for (int i = 0; i < 6; i++) {
            View recentColorSlot = this.mRecentColorListLayout.getChildAt(i);
            setImageColor(recentColorSlot, Integer.valueOf(emptyColor));
            recentColorSlot.setFocusable(false);
            recentColorSlot.setClickable(false);
        }
    }

    public void updateRecentColorLayout() {
        int size;
        if (this.mRecentColorValues != null) {
            size = this.mRecentColorValues.size();
        } else {
            size = 0;
        }
        String defaultDescription = ", " + this.mResources.getString(C0247R.string.sesl_color_picker_option);
        for (int i = 0; i < 6; i++) {
            View recentColorSlot = this.mRecentColorListLayout.getChildAt(i);
            if (i < size) {
                setImageColor(recentColorSlot, (Integer) this.mRecentColorValues.get(i));
                recentColorSlot.setContentDescription(this.mColorDescription[i] + defaultDescription);
                recentColorSlot.setFocusable(true);
                recentColorSlot.setClickable(true);
            }
        }
        if (this.mRecentColorInfo.getCurrentColor() != null) {
            int currentColor = this.mRecentColorInfo.getCurrentColor().intValue();
            ((GradientDrawable) this.mCurrentColorView.getBackground()).setColor(currentColor);
            ((GradientDrawable) this.mPickedColorView.getBackground()).setColor(currentColor);
            mapColorOnColorWheel(currentColor);
        } else if (size != 0) {
            int firstColor = ((Integer) this.mRecentColorValues.get(0)).intValue();
            ((GradientDrawable) this.mCurrentColorView.getBackground()).setColor(firstColor);
            ((GradientDrawable) this.mPickedColorView.getBackground()).setColor(firstColor);
            mapColorOnColorWheel(firstColor);
        }
        if (this.mRecentColorInfo.getNewColor() != null) {
            int newColor = this.mRecentColorInfo.getNewColor().intValue();
            ((GradientDrawable) this.mPickedColorView.getBackground()).setColor(newColor);
            mapColorOnColorWheel(newColor);
        }
    }

    private void setInitialColors() {
        mapColorOnColorWheel(this.mPickedColor.getColor());
    }

    private void updateCurrentColor() {
        int color = this.mPickedColor.getColor();
        if (this.mGradientColorSeekBar != null) {
            this.mGradientColorSeekBar.changeColorBase(color);
        }
        if (this.mOpacitySeekBar != null) {
            this.mOpacitySeekBar.changeColorBase(color);
        }
        if (this.mSelectedColorBackground != null) {
            this.mSelectedColorBackground.setColor(color);
        }
        if (this.mGradientColorWheel != null) {
            this.mGradientColorWheel.updateCursorColor(color);
        }
        if (this.mOnColorChangedListener != null) {
            this.mOnColorChangedListener.onColorChanged(color);
        }
    }

    private void setImageColor(View button, Integer color) {
        GradientDrawable gradientDrawable = (GradientDrawable) this.mContext.getDrawable(C0247R.drawable.sesl_color_picker_used_color_item_slot);
        if (color != null) {
            gradientDrawable.setColor(color.intValue());
        }
        int rippleColor = Color.argb(61, 0, 0, 0);
        button.setBackground(new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{rippleColor}), gradientDrawable, null));
        button.setOnClickListener(this.mImageButtonClickListener);
    }

    private void mapColorOnColorWheel(int color) {
        this.mPickedColor.setColor(color);
        if (this.mGradientColorWheel != null) {
            this.mGradientColorWheel.setColor(color);
        }
        if (this.mGradientColorSeekBar != null) {
            this.mGradientColorSeekBar.restoreColor(color);
        }
        if (this.mOpacitySeekBar != null) {
            this.mOpacitySeekBar.restoreColor(color);
        }
        if (this.mSelectedColorBackground != null) {
            this.mSelectedColorBackground.setColor(color);
        }
        if (this.mGradientColorWheel != null) {
            float tempV = this.mPickedColor.getV();
            int tempAlpha = this.mPickedColor.getAlpha();
            this.mPickedColor.setV(1.0f);
            this.mPickedColor.setAlpha(255);
            this.mGradientColorWheel.updateCursorColor(this.mPickedColor.getColor());
            this.mPickedColor.setV(tempV);
            this.mPickedColor.setAlpha(tempAlpha);
        }
    }

    public void saveSelectedColor() {
        this.mRecentColorInfo.saveSelectedColor(this.mPickedColor.getColor());
    }

    public SeslRecentColorInfo getRecentColorInfo() {
        return this.mRecentColorInfo;
    }

    public boolean isUserInputValid() {
        return this.mIsInputFromUser;
    }

    public void setOpacityBarEnabled(boolean enabled) {
        this.mIsOpacityBarEnabled = enabled;
        if (this.mIsOpacityBarEnabled) {
            this.mOpacitySeekBar.setVisibility(0);
            this.mOpacitySeekBarContainer.setVisibility(0);
            if (this.mResources.getConfiguration().orientation == 2) {
                LayoutParams gradientColorWheelLayoutParams = (LayoutParams) ((FrameLayout) findViewById(C0247R.id.sesl_color_picker_gradient_wheel_container)).getLayoutParams();
                gradientColorWheelLayoutParams.topMargin = (int) (((float) gradientColorWheelLayoutParams.topMargin) + (6.0f * this.mResources.getDisplayMetrics().density));
            }
        }
    }
}
