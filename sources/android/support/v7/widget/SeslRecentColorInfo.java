package android.support.v7.widget;

import java.util.LinkedList;

public class SeslRecentColorInfo {
    private static Integer sCurrentColor = null;
    private static Integer sNewColor = null;
    private static LinkedList<Integer> sRecentColorInfo;
    private static Integer sSelectedColor = null;

    public SeslRecentColorInfo() {
        sRecentColorInfo = new LinkedList();
    }

    public LinkedList<Integer> getRecentColorInfo() {
        return sRecentColorInfo;
    }

    public Integer getCurrentColor() {
        return sCurrentColor;
    }

    public Integer getNewColor() {
        return sNewColor;
    }

    public Integer getSelectedColor() {
        return sSelectedColor;
    }

    public void setCurrentColor(Integer currentColor) {
        sCurrentColor = currentColor;
    }

    public void setNewColor(Integer newColor) {
        sNewColor = newColor;
    }

    public void saveSelectedColor(int selectedColor) {
        sSelectedColor = Integer.valueOf(selectedColor);
    }

    public void initRecentColorInfo(int[] colorIntegerArray) {
        if (colorIntegerArray != null) {
            for (int i = colorIntegerArray.length - 1; i >= 0; i--) {
                updateRecentColorInfo(Integer.valueOf(colorIntegerArray[i]));
            }
        }
    }

    private void updateRecentColorInfo(Integer selectedColor) {
        if (sRecentColorInfo.size() >= 6) {
            sRecentColorInfo.removeLast();
        }
        sRecentColorInfo.addFirst(selectedColor);
    }
}
