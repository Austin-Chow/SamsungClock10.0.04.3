package android.support.design.widget;

import android.widget.ImageButton;

class VisibilityAwareImageButton extends ImageButton {
    private int mUserSetVisibility;

    public void setVisibility(int visibility) {
        internalSetVisibility(visibility, true);
    }

    final void internalSetVisibility(int visibility, boolean fromUser) {
        super.setVisibility(visibility);
        if (fromUser) {
            this.mUserSetVisibility = visibility;
        }
    }

    final int getUserSetVisibility() {
        return this.mUserSetVisibility;
    }
}
