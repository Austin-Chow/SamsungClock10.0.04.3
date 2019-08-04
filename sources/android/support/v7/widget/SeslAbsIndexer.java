package android.support.v7.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.SparseIntArray;
import java.text.Collator;
import java.util.HashMap;

public abstract class SeslAbsIndexer extends DataSetObserver {
    private static final char DIGIT_CHAR = '#';
    private static final char FAVORITE_CHAR = '★';
    private static final String GROUP_CHAR = "👥︎";
    private static final char GROUP_CHECKER = '?';
    static final String INDEXSCROLL_INDEX_COUNTS = "indexscroll_index_counts";
    static final String INDEXSCROLL_INDEX_TITLES = "indexscroll_index_titles";
    private static final char SYMBOL_BASE_CHAR = '!';
    private static final char SYMBOL_CHAR = '&';
    private final boolean DEBUG;
    private final String TAG;
    private SparseIntArray mAlphaMap;
    private CharSequence mAlphabet;
    private String[] mAlphabetArray;
    private int mAlphabetLength;
    private Bundle mBundle;
    private int[] mCachingValue;
    protected Collator mCollator;
    private int mCurrentLang;
    private final DataSetObservable mDataSetObservable;
    private int mDigitItemCount;
    private int mFavoriteItemCount;
    private int mGroupItemCount;
    private String[] mLangAlphabetArray;
    private HashMap<Integer, Integer> mLangIndexMap;
    private int mProfileItemCount;
    private boolean mUseDigitIndex;
    private boolean mUseFavoriteIndex;
    private boolean mUseGroupIndex;

    protected abstract Bundle getBundle();

    protected abstract String getItemAt(int i);

    protected abstract int getItemCount();

    protected abstract boolean isDataToBeIndexedAvailable();

    public SeslAbsIndexer(CharSequence indexCharacters) {
        this.TAG = "SeslAbsIndexer";
        this.DEBUG = false;
        this.mDataSetObservable = new DataSetObservable();
        this.mProfileItemCount = 0;
        this.mFavoriteItemCount = 0;
        this.mGroupItemCount = 0;
        this.mDigitItemCount = 0;
        this.mUseFavoriteIndex = false;
        this.mUseGroupIndex = false;
        this.mUseDigitIndex = false;
        this.mLangIndexMap = new HashMap();
        this.mCurrentLang = -1;
        this.mUseFavoriteIndex = false;
        this.mProfileItemCount = 0;
        this.mFavoriteItemCount = 0;
        initIndexer(indexCharacters);
    }

    public SeslAbsIndexer(String[] indexCharacters, int aLangIndex) {
        this.TAG = "SeslAbsIndexer";
        this.DEBUG = false;
        this.mDataSetObservable = new DataSetObservable();
        this.mProfileItemCount = 0;
        this.mFavoriteItemCount = 0;
        this.mGroupItemCount = 0;
        this.mDigitItemCount = 0;
        this.mUseFavoriteIndex = false;
        this.mUseGroupIndex = false;
        this.mUseDigitIndex = false;
        this.mLangIndexMap = new HashMap();
        this.mCurrentLang = -1;
        this.mUseFavoriteIndex = false;
        this.mProfileItemCount = 0;
        this.mFavoriteItemCount = 0;
        this.mLangAlphabetArray = indexCharacters;
        setMultiLangIndexer(aLangIndex);
    }

    String[] getLangAlphabetArray() {
        return this.mLangAlphabetArray;
    }

    int getCachingValue(int index) {
        if (index < 0 || index >= this.mAlphabetLength) {
            return -1;
        }
        return this.mCachingValue[index];
    }

    private void setMultiLangIndexer(int aLangIndex) {
        StringBuilder indexerString;
        this.mCurrentLang = aLangIndex;
        if (!this.mUseFavoriteIndex) {
            indexerString = new StringBuilder(String.valueOf(SYMBOL_CHAR));
        } else if (this.mUseGroupIndex) {
            indexerString = new StringBuilder(String.valueOf(FAVORITE_CHAR) + GROUP_CHECKER);
            indexerString.append(SYMBOL_CHAR);
        } else {
            indexerString = new StringBuilder(String.valueOf(FAVORITE_CHAR) + SYMBOL_CHAR);
        }
        int langIndex = 0;
        while (langIndex < this.mLangAlphabetArray.length) {
            for (int j = 0; j < this.mLangAlphabetArray[langIndex].length(); j++) {
                this.mLangIndexMap.put(Integer.valueOf(indexerString.length()), Integer.valueOf(langIndex));
                indexerString.append(this.mLangAlphabetArray[langIndex].charAt(j));
            }
            langIndex++;
        }
        if (this.mUseDigitIndex) {
            this.mLangIndexMap.put(Integer.valueOf(indexerString.length()), Integer.valueOf(langIndex - 1));
            indexerString.append(DIGIT_CHAR);
        }
        initIndexer(indexerString.toString());
    }

    void setProfileItem(int count) {
        if (count >= 0) {
            this.mProfileItemCount = count;
        }
    }

    void setFavoriteItem(int count) {
        if (count >= 0) {
            this.mFavoriteItemCount = count;
            this.mUseFavoriteIndex = true;
            setMultiLangIndexer(this.mCurrentLang);
        }
    }

    void setGroupItem(int count) {
        if (count >= 0) {
            this.mGroupItemCount = count;
            this.mUseGroupIndex = true;
            setMultiLangIndexer(this.mCurrentLang);
        }
    }

    void setDigitItem(int count) {
        if (count >= 0) {
            this.mDigitItemCount = count;
            this.mUseDigitIndex = true;
            setMultiLangIndexer(this.mCurrentLang);
        }
    }

    boolean isUseDigitIndex() {
        return this.mUseDigitIndex;
    }

    int getCurrentLang() {
        return this.mCurrentLang;
    }

    int getLangbyIndex(int aIndex) {
        if (aIndex >= 0 && this.mLangIndexMap != null) {
            Integer lIndexVal = new Integer(aIndex);
            if (lIndexVal != null && this.mLangIndexMap.containsKey(lIndexVal)) {
                return ((Integer) this.mLangIndexMap.get(lIndexVal)).intValue();
            }
        }
        return -1;
    }

    private void initIndexer(CharSequence alphabet) {
        if (alphabet == null || alphabet.length() == 0) {
            throw new IllegalArgumentException("Invalid indexString :" + alphabet);
        }
        this.mAlphabet = alphabet;
        this.mAlphabetLength = alphabet.length();
        this.mCachingValue = new int[this.mAlphabetLength];
        this.mAlphabetArray = new String[this.mAlphabetLength];
        int i = 0;
        while (i < this.mAlphabetLength) {
            if (this.mUseGroupIndex && this.mAlphabet.charAt(i) == GROUP_CHECKER) {
                this.mAlphabetArray[i] = GROUP_CHAR;
            } else {
                this.mAlphabetArray[i] = Character.toString(this.mAlphabet.charAt(i));
            }
            i++;
        }
        this.mAlphaMap = new SparseIntArray(this.mAlphabetLength);
        this.mCollator = Collator.getInstance();
        this.mCollator.setStrength(0);
    }

    String[] getAlphabetArray() {
        return this.mAlphabetArray;
    }

    private int compare(String word, String indexString) {
        return this.mCollator.compare(word, indexString);
    }

    void cacheIndexInfo() {
        if (isDataToBeIndexedAvailable() && getItemCount() != 0) {
            this.mBundle = getBundle();
            if (this.mBundle != null && this.mBundle.containsKey("indexscroll_index_titles") && this.mBundle.containsKey("indexscroll_index_counts")) {
                getBundleInfo();
                return;
            }
            String baseString = "";
            onBeginTransaction();
            for (int sectionIndex = 0; sectionIndex < this.mAlphabetLength; sectionIndex++) {
                this.mCachingValue[sectionIndex] = getPositionForString(baseString + this.mAlphabet.charAt(sectionIndex));
            }
            onEndTransaction();
        }
    }

    private int getPositionForString(String searchString) {
        SparseIntArray alphaMap = this.mAlphaMap;
        int count = getItemCount();
        if (count == 0 || this.mAlphabet == null) {
            return 0;
        }
        if (searchString == null || searchString.length() == 0) {
            return count;
        }
        int start = 0;
        int end = count;
        String targetLetter = searchString;
        char key = searchString.charAt(0);
        int pos = alphaMap.get(key, Integer.MIN_VALUE);
        if (Integer.MIN_VALUE != pos) {
            start = Math.abs(pos);
        } else {
            int sectionIndex = this.mAlphabet.toString().indexOf(key);
            if (sectionIndex > 0 && key > this.mAlphabet.charAt(sectionIndex - 1)) {
                int prevLetterPos = alphaMap.get(this.mAlphabet.charAt(sectionIndex - 1), Integer.MIN_VALUE);
                if (prevLetterPos != Integer.MIN_VALUE) {
                    start = Math.abs(prevLetterPos);
                }
            }
            if (sectionIndex < this.mAlphabet.length() - 1 && key < this.mAlphabet.charAt(sectionIndex + 1)) {
                int nextLetterPos = alphaMap.get(this.mAlphabet.charAt(sectionIndex + 1), Integer.MIN_VALUE);
                if (nextLetterPos != Integer.MIN_VALUE) {
                    end = Math.abs(nextLetterPos);
                }
            }
        }
        char targetChar = targetLetter.charAt(0);
        if (targetChar == '&') {
            targetLetter = "!";
        }
        if (targetChar == '★') {
            if (start < this.mProfileItemCount) {
                start = this.mProfileItemCount;
            }
        } else if (targetChar == '?') {
            if (start < this.mProfileItemCount + this.mFavoriteItemCount) {
                start = this.mProfileItemCount + this.mFavoriteItemCount;
            }
        } else if (start < (this.mProfileItemCount + this.mFavoriteItemCount) + this.mGroupItemCount) {
            start = (this.mProfileItemCount + this.mFavoriteItemCount) + this.mGroupItemCount;
        }
        end -= this.mDigitItemCount;
        if (targetChar == '#') {
            start = end;
        }
        pos = (end + start) / 2;
        while (pos >= start && pos < end) {
            String curName = getItemAt(pos);
            if (curName != null && !curName.equals("")) {
                int diff = compare(curName, targetLetter);
                if (targetChar == '★' || targetChar == '&' || targetChar == '#') {
                    diff = 1;
                }
                if (diff == 0) {
                    if (start == pos) {
                        break;
                    }
                    end = pos;
                } else if (diff < 0) {
                    start = pos + 1;
                    if (start >= count) {
                        pos = count;
                        break;
                    }
                } else {
                    end = pos;
                }
                pos = (start + end) / 2;
            } else if (pos <= start) {
                break;
            } else {
                pos--;
            }
        }
        if (searchString.length() != 1) {
            return pos;
        }
        alphaMap.put(key, pos);
        return pos;
    }

    private void getBundleInfo() {
        String[] sections = this.mBundle.getStringArray("indexscroll_index_titles");
        int[] counts = this.mBundle.getIntArray("indexscroll_index_counts");
        int basePosition = this.mProfileItemCount;
        int baseSectionIndex = 0;
        for (int index = 0; index < this.mAlphabetLength; index++) {
            char targetChar = this.mAlphabet.charAt(index);
            this.mCachingValue[index] = basePosition;
            if (targetChar == FAVORITE_CHAR) {
                basePosition += this.mFavoriteItemCount;
            } else if (targetChar == GROUP_CHECKER) {
                basePosition += this.mGroupItemCount;
            }
            for (int sectionIndex = baseSectionIndex; sectionIndex < sections.length; sectionIndex++) {
                if (targetChar == sections[sectionIndex].charAt(0)) {
                    basePosition += counts[sectionIndex];
                    baseSectionIndex = sectionIndex;
                    break;
                }
            }
            if (targetChar == "#".charAt(0)) {
                this.mCachingValue[index] = getItemCount() - this.mDigitItemCount;
            }
        }
    }

    public void onChanged() {
        super.onChanged();
        this.mAlphaMap.clear();
        this.mDataSetObservable.notifyChanged();
    }

    public void onInvalidated() {
        super.onInvalidated();
        this.mAlphaMap.clear();
        this.mDataSetObservable.notifyInvalidated();
    }

    void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    void unregisterDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    void onBeginTransaction() {
    }

    void onEndTransaction() {
    }
}
