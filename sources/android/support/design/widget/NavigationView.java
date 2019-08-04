package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.MeasureSpec;

public class NavigationView extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
    OnNavigationItemSelectedListener mListener;
    private int mMaxWidth;
    private final NavigationMenu mMenu;
    private MenuInflater mMenuInflater;
    private final NavigationMenuPresenter mPresenter;

    public interface OnNavigationItemSelectedListener {
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new C00431();
        public Bundle menuState;

        /* renamed from: android.support.design.widget.NavigationView$SavedState$1 */
        static class C00431 implements ClassLoaderCreator<SavedState> {
            C00431() {
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        public SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.menuState = in.readBundle(loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(this.menuState);
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.menuState = new Bundle();
        this.mMenu.savePresenterStates(state.menuState);
        return state;
    }

    protected void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof SavedState) {
            SavedState state = (SavedState) savedState;
            super.onRestoreInstanceState(state.getSuperState());
            this.mMenu.restorePresenterStates(state.menuState);
            return;
        }
        super.onRestoreInstanceState(savedState);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        this.mListener = listener;
    }

    protected void onMeasure(int widthSpec, int heightSpec) {
        switch (MeasureSpec.getMode(widthSpec)) {
            case Integer.MIN_VALUE:
                widthSpec = MeasureSpec.makeMeasureSpec(Math.min(MeasureSpec.getSize(widthSpec), this.mMaxWidth), 1073741824);
                break;
            case 0:
                widthSpec = MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
                break;
        }
        super.onMeasure(widthSpec, heightSpec);
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public int getHeaderCount() {
        return this.mPresenter.getHeaderCount();
    }

    public ColorStateList getItemIconTintList() {
        return this.mPresenter.getItemTintList();
    }

    public void setItemIconTintList(ColorStateList tint) {
        this.mPresenter.setItemIconTintList(tint);
    }

    public ColorStateList getItemTextColor() {
        return this.mPresenter.getItemTextColor();
    }

    public void setItemTextColor(ColorStateList textColor) {
        this.mPresenter.setItemTextColor(textColor);
    }

    public Drawable getItemBackground() {
        return this.mPresenter.getItemBackground();
    }

    public void setItemBackgroundResource(int resId) {
        setItemBackground(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setItemBackground(Drawable itemBackground) {
        this.mPresenter.setItemBackground(itemBackground);
    }

    public void setCheckedItem(int id) {
        MenuItem item = this.mMenu.findItem(id);
        if (item != null) {
            this.mPresenter.setCheckedItem((MenuItemImpl) item);
        }
    }

    public void setItemTextAppearance(int resId) {
        this.mPresenter.setItemTextAppearance(resId);
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(getContext());
        }
        return this.mMenuInflater;
    }
}
