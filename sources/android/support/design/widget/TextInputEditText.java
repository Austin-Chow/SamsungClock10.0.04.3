package android.support.design.widget;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.WithHint;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class TextInputEditText extends AppCompatEditText {
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection ic = super.onCreateInputConnection(outAttrs);
        if (ic != null && outAttrs.hintText == null) {
            for (ViewParent parent = getParent(); parent instanceof View; parent = parent.getParent()) {
                if (parent instanceof WithHint) {
                    outAttrs.hintText = ((WithHint) parent).getHint();
                    break;
                }
            }
        }
        return ic;
    }
}
