package android.support.constraint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.view.View;

public class Placeholder extends View {
    private View mContent;
    private int mContentId;
    private int mEmptyVisibility;

    public void setEmptyVisibility(int visibility) {
        this.mEmptyVisibility = visibility;
    }

    public int getEmptyVisibility() {
        return this.mEmptyVisibility;
    }

    public View getContent() {
        return this.mContent;
    }

    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            Paint paint = new Paint();
            paint.setARGB(255, 210, 210, 210);
            paint.setTextAlign(Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
            Rect r = new Rect();
            canvas.getClipBounds(r);
            paint.setTextSize((float) r.height());
            int cHeight = r.height();
            int cWidth = r.width();
            paint.setTextAlign(Align.LEFT);
            String text = "?";
            paint.getTextBounds(text, 0, text.length(), r);
            canvas.drawText(text, ((((float) cWidth) / 2.0f) - (((float) r.width()) / 2.0f)) - ((float) r.left), ((((float) cHeight) / 2.0f) + (((float) r.height()) / 2.0f)) - ((float) r.bottom), paint);
        }
    }

    public void updatePreLayout(ConstraintLayout container) {
        if (this.mContentId == -1 && !isInEditMode()) {
            setVisibility(this.mEmptyVisibility);
        }
        this.mContent = container.findViewById(this.mContentId);
        if (this.mContent != null) {
            ((LayoutParams) this.mContent.getLayoutParams()).isInPlaceholder = true;
            this.mContent.setVisibility(0);
            setVisibility(0);
        }
    }

    public void setContentId(int id) {
        if (this.mContentId != id) {
            if (this.mContent != null) {
                this.mContent.setVisibility(0);
                ((LayoutParams) this.mContent.getLayoutParams()).isInPlaceholder = false;
                this.mContent = null;
            }
            this.mContentId = id;
            if (id != -1) {
                View v = ((View) getParent()).findViewById(id);
                if (v != null) {
                    v.setVisibility(8);
                }
            }
        }
    }

    public void updatePostMeasure(ConstraintLayout container) {
        if (this.mContent != null) {
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            LayoutParams layoutParamsContent = (LayoutParams) this.mContent.getLayoutParams();
            layoutParamsContent.widget.setVisibility(0);
            layoutParams.widget.setWidth(layoutParamsContent.widget.getWidth());
            layoutParams.widget.setHeight(layoutParamsContent.widget.getHeight());
            layoutParamsContent.widget.setVisibility(8);
        }
    }
}
