package android.support.v7.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.appcompat.C0247R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SeslIndexScrollView extends FrameLayout implements OnScrollListener {
    public static final int GRAVITY_INDEX_BAR_LEFT = 0;
    public static final int GRAVITY_INDEX_BAR_RIGHT = 1;
    private static final float OUT_OF_BOUNDARY = -9999.0f;
    private Context mContext;
    private String mCurrentIndex;
    private boolean mHasOverlayChild = false;
    private int mIndexBarGravity = 1;
    IndexScroll mIndexScroll;
    private IndexScrollPreview mIndexScrollPreview;
    private SeslAbsIndexer mIndexer;
    private final IndexerObserver mIndexerObserver = new IndexerObserver();
    private boolean mIsSimpleIndexScroll = false;
    private int mNumberOfLanguages;
    private OnIndexBarEventListener mOnIndexBarEventListener = null;
    private final Runnable mPreviewDelayRunnable = new C03691();
    private boolean mRegisteredDataSetObserver = false;
    private Typeface mSECRobotoLightRegularFont;
    private long mStartTouchDown = 0;
    private float mTouchY = OUT_OF_BOUNDARY;
    private ViewGroupOverlay mViewGroupOverlay;

    /* renamed from: android.support.v7.widget.SeslIndexScrollView$1 */
    class C03691 implements Runnable {
        C03691() {
        }

        public void run() {
            if (SeslIndexScrollView.this.mIndexScrollPreview != null) {
                SeslIndexScrollView.this.mIndexScrollPreview.fadeOutAnimation();
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityIndexBar {
    }

    class IndexScroll {
        public static final int FIRST_LETTER_NOT_RELEVANT_NOT_MULTI_LANGUAGE = -1;
        public static final int GRAVITY_INDEX_BAR_LEFT = 0;
        public static final int GRAVITY_INDEX_BAR_RIGHT = 1;
        public static final int LAST_LETTER_NOT_RELEVANT_NOT_MULTI_LANGUAGE = -1;
        public static final int NO_SELECTED_INDEX = -1;
        private int mAdditionalSpace;
        private String[] mAlphabetArray = null;
        private int mAlphabetArrayFirstLetterIndex = -1;
        private int mAlphabetArrayLastLetterIndex = -1;
        private String[] mAlphabetArrayToDraw;
        private int mAlphabetSize;
        private int mAlphabetToDrawSize;
        private Drawable mBgDrawableDefault = null;
        private Rect mBgRect;
        private boolean mBgRectParamsSet;
        private int mBgRectWidth;
        private int mBgTintColor;
        private String mBigText;
        private float mContentMinHeight;
        private int mContentPadding;
        private Context mContext;
        private int mDotHeight;
        LangAttributeValues mFirstLang;
        private int mHeight;
        private float mIndexScrollPreviewRadius;
        private boolean mIsAlphabetInit = false;
        private float mItemHeight;
        private int mItemWidth;
        private int mItemWidthGap;
        private Paint mPaint;
        private int mPosition = 0;
        private float mPreviewLimitY;
        private int mScreenHeight;
        private int mScrollBottom;
        private int mScrollBottomMargin;
        private int mScrollThumbAdditionalHeight;
        private Drawable mScrollThumbBgDrawable = null;
        private Rect mScrollThumbBgRect;
        private int mScrollThumbBgRectHeight;
        private int mScrollThumbBgRectPadding;
        private int mScrollTop;
        private int mScrollTopMargin;
        LangAttributeValues mSecondLang;
        private int mSelectedIndex = -1;
        private float mSeparatorHeight;
        private String mSmallText;
        private Rect mTextBounds;
        private int mTextColorDimmed;
        private int mTextSize;
        private int mThumbColor = 0;
        private int mWidth;
        private int mWidthShift;

        class LangAttributeValues {
            String[] alphabetArray;
            int dotCount;
            float height;
            int indexCount;
            float separatorHeight;
            int totalCount;

            public LangAttributeValues(int indexCount, int dotCount, int totalCount, float height, float separatorHeight) {
                this.indexCount = indexCount;
                this.dotCount = dotCount;
                this.totalCount = totalCount;
                this.height = height;
                this.separatorHeight = separatorHeight;
            }
        }

        public IndexScroll(Context context, int height, int width) {
            this.mHeight = height;
            this.mWidth = width;
            this.mWidthShift = 0;
            this.mScrollTop = 0;
            this.mTextBounds = new Rect();
            this.mBgRectParamsSet = false;
            this.mContext = context;
            init();
        }

        public IndexScroll(Context context, int height, int width, int position) {
            this.mHeight = height;
            this.mWidth = width;
            this.mPosition = position;
            this.mWidthShift = 0;
            this.mScrollTop = 0;
            this.mTextBounds = new Rect();
            this.mBgRectParamsSet = false;
            this.mContext = context;
            init();
        }

        public boolean isAlphabetInit() {
            return this.mIsAlphabetInit;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public int getSelectedIndex() {
            return this.mSelectedIndex;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public void setSimpleIndexScrollWidth(int itemWidth) {
            if (itemWidth > 0) {
                this.mItemWidth = itemWidth;
                this.mBgRectWidth = itemWidth;
                allocateBgRectangle();
            }
        }

        public void setIndexScrollBgMargin(int topMargin, int bottomMargin) {
            this.mScrollTopMargin = topMargin;
            this.mScrollBottomMargin = bottomMargin;
        }

        public void setPosition(int position) {
            this.mPosition = position;
            setBgRectParams();
        }

        public void setDimensions(int width, int height) {
            if (this.mIsAlphabetInit) {
                this.mWidth = width;
                this.mHeight = height - (((this.mScrollTop + this.mScrollBottom) + this.mScrollTopMargin) + this.mScrollBottomMargin);
                this.mScreenHeight = height;
                this.mItemHeight = ((float) this.mHeight) / ((float) this.mAlphabetSize);
                this.mSeparatorHeight = Math.max(this.mItemHeight, this.mContentMinHeight);
                setBgRectParams();
                if (this.mFirstLang != null && this.mSecondLang != null) {
                    this.mFirstLang.separatorHeight = this.mContentMinHeight;
                    this.mSecondLang.separatorHeight = this.mContentMinHeight;
                    manageIndexScrollHeight();
                }
            }
        }

        private void init() {
            Resources res = this.mContext.getResources();
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            if (SeslIndexScrollView.this.mSECRobotoLightRegularFont == null) {
                SeslIndexScrollView.this.mSECRobotoLightRegularFont = Typeface.create("sec-roboto-light", 0);
            }
            this.mPaint.setTypeface(SeslIndexScrollView.this.mSECRobotoLightRegularFont);
            this.mScrollTopMargin = 0;
            this.mScrollBottomMargin = 0;
            this.mItemWidth = 1;
            this.mItemWidthGap = 1;
            this.mBgRectWidth = (int) res.getDimension(C0247R.dimen.sesl_indexbar_width);
            this.mTextSize = (int) res.getDimension(C0247R.dimen.sesl_indexbar_text_size);
            this.mScrollTop = (int) res.getDimension(C0247R.dimen.sesl_indexbar_margin_top);
            this.mScrollBottom = (int) res.getDimension(C0247R.dimen.sesl_indexbar_margin_bottom);
            this.mWidthShift = (int) res.getDimension(C0247R.dimen.sesl_indexbar_margin_horizontal);
            this.mContentPadding = (int) res.getDimension(C0247R.dimen.sesl_indexbar_content_padding);
            this.mContentMinHeight = res.getDimension(C0247R.dimen.sesl_indexbar_content_min_height);
            this.mAdditionalSpace = (int) res.getDimension(C0247R.dimen.sesl_indexbar_additional_touch_boundary);
            this.mIndexScrollPreviewRadius = res.getDimension(C0247R.dimen.sesl_index_scroll_preview_radius);
            this.mPreviewLimitY = res.getDimension(C0247R.dimen.sesl_index_scroll_preview_ypos_limit);
            TypedValue outValue = new TypedValue();
            Theme theme = this.mContext.getTheme();
            theme.resolveAttribute(C0247R.attr.colorPrimary, outValue, true);
            int colorPrimary = ResourcesCompat.getColor(res, outValue.resourceId, null);
            this.mFirstLang = new LangAttributeValues(0, 0, 0, 0.0f, 0.0f);
            this.mSecondLang = new LangAttributeValues(0, 0, 0, 0.0f, 0.0f);
            this.mScrollThumbBgRectPadding = (int) res.getDimension(C0247R.dimen.sesl_indexbar_thumb_padding);
            this.mScrollThumbAdditionalHeight = (int) res.getDimension(C0247R.dimen.sesl_indexbar_thumb_additional_height);
            this.mDotHeight = (int) res.getDimension(C0247R.dimen.sesl_indexbar_dot_separator_height);
            SeslIndexScrollView.this.mIndexScrollPreview.setBackgroundColor(getColorWithAlpha(colorPrimary, 0.8f));
            this.mScrollThumbBgDrawable = res.getDrawable(C0247R.drawable.sesl_index_bar_thumb_shape, SeslIndexScrollView.this.getContext().getTheme());
            this.mScrollThumbBgDrawable.setColorFilter(colorPrimary, Mode.MULTIPLY);
            this.mThumbColor = colorPrimary;
            this.mContext.getTheme().resolveAttribute(C0247R.attr.isLightTheme, outValue, true);
            if (outValue.data != 0) {
                this.mTextColorDimmed = ResourcesCompat.getColor(res, C0247R.color.sesl_index_bar_text_color, theme);
                this.mBgTintColor = ResourcesCompat.getColor(res, C0247R.color.sesl_index_bar_background_tint_color, theme);
            } else {
                this.mTextColorDimmed = ResourcesCompat.getColor(res, C0247R.color.sesl_index_bar_text_color_dark, theme);
                this.mBgTintColor = ResourcesCompat.getColor(res, C0247R.color.sesl_index_bar_background_tint_color_dark, theme);
            }
            this.mBgDrawableDefault = res.getDrawable(C0247R.drawable.sesl_index_bar_bg, theme);
            this.mBgDrawableDefault.setColorFilter(this.mBgTintColor, Mode.MULTIPLY);
            setBgRectParams();
        }

        private int getColorWithAlpha(int color, float ratio) {
            return Color.argb(Math.round(((float) Color.alpha(color)) * ratio), Color.red(color), Color.green(color), Color.blue(color));
        }

        public void setAlphabetArray(String[] alphabetArray, int alphabetArrayFirstLetterIndex, int alphabetArrayLastLetterIndex) {
            if (alphabetArray != null) {
                this.mAlphabetArray = alphabetArray;
                this.mAlphabetSize = this.mAlphabetArray.length;
                this.mAlphabetArrayFirstLetterIndex = alphabetArrayFirstLetterIndex;
                this.mAlphabetArrayLastLetterIndex = alphabetArrayLastLetterIndex;
                this.mItemHeight = ((float) this.mHeight) / ((float) this.mAlphabetSize);
                this.mSeparatorHeight = Math.max(this.mItemHeight, this.mContentMinHeight);
                this.mIsAlphabetInit = true;
            }
        }

        private void adjustSeparatorHeight() {
            if (SeslIndexScrollView.this.mNumberOfLanguages == 1) {
                this.mFirstLang.separatorHeight = ((float) (this.mHeight - (this.mDotHeight * this.mFirstLang.dotCount))) / ((float) this.mFirstLang.indexCount);
                this.mFirstLang.height = (float) this.mHeight;
                return;
            }
            if (this.mFirstLang.height > ((float) this.mHeight) * 0.6f) {
                this.mFirstLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.6d) - ((double) (this.mDotHeight * this.mFirstLang.dotCount)))) / ((float) this.mFirstLang.indexCount);
                this.mSecondLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.4d) - ((double) (this.mDotHeight * this.mSecondLang.dotCount)))) / ((float) this.mSecondLang.indexCount);
                this.mFirstLang.height = ((float) this.mHeight) * 0.6f;
                this.mSecondLang.height = ((float) this.mHeight) * 0.4f;
            } else if (this.mFirstLang.height <= ((float) this.mHeight) * 0.5f) {
                this.mFirstLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.5d) - ((double) (this.mDotHeight * this.mFirstLang.dotCount)))) / ((float) this.mFirstLang.indexCount);
                this.mSecondLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.5d) - ((double) (this.mDotHeight * this.mSecondLang.dotCount)))) / ((float) this.mSecondLang.indexCount);
                LangAttributeValues langAttributeValues = this.mFirstLang;
                float f = ((float) this.mHeight) * 0.5f;
                this.mSecondLang.height = f;
                langAttributeValues.height = f;
            } else {
                this.mFirstLang.separatorHeight = (this.mFirstLang.height - ((float) (this.mDotHeight * this.mFirstLang.dotCount))) / ((float) this.mFirstLang.indexCount);
                this.mSecondLang.separatorHeight = (this.mSecondLang.height - ((float) (this.mDotHeight * this.mSecondLang.dotCount))) / ((float) this.mSecondLang.indexCount);
            }
            if (this.mSecondLang.totalCount == 0) {
                this.mFirstLang.separatorHeight = ((float) (this.mHeight - (this.mDotHeight * this.mFirstLang.dotCount))) / ((float) this.mFirstLang.indexCount);
                this.mFirstLang.height = (float) this.mHeight;
                this.mSecondLang.separatorHeight = 0.0f;
                this.mSecondLang.height = 0.0f;
            } else if (this.mFirstLang.totalCount == 0) {
                this.mSecondLang.separatorHeight = ((float) (this.mHeight - (this.mDotHeight * this.mSecondLang.dotCount))) / ((float) this.mSecondLang.indexCount);
                this.mSecondLang.height = (float) this.mHeight;
                this.mFirstLang.separatorHeight = 0.0f;
                this.mFirstLang.height = 0.0f;
            }
        }

        private void manageIndexScrollHeight() {
            if (this.mIsAlphabetInit && SeslIndexScrollView.this.mNumberOfLanguages <= 2) {
                if (this.mAlphabetArrayFirstLetterIndex == -1) {
                    this.mAlphabetArrayFirstLetterIndex = 0;
                }
                if (this.mAlphabetArrayLastLetterIndex == -1) {
                    this.mAlphabetArrayLastLetterIndex = 0;
                }
                this.mFirstLang.indexCount = this.mAlphabetSize - this.mAlphabetArrayLastLetterIndex;
                this.mFirstLang.totalCount = this.mFirstLang.indexCount;
                this.mFirstLang.alphabetArray = new String[this.mFirstLang.totalCount];
                this.mFirstLang.dotCount = 0;
                this.mSecondLang.indexCount = this.mAlphabetSize - this.mFirstLang.indexCount;
                this.mSecondLang.totalCount = this.mSecondLang.indexCount;
                this.mSecondLang.alphabetArray = new String[this.mSecondLang.totalCount];
                this.mSecondLang.dotCount = 0;
                this.mFirstLang.height = ((float) this.mFirstLang.indexCount) * this.mContentMinHeight;
                this.mSecondLang.height = ((float) this.mHeight) - this.mFirstLang.height;
                this.mAlphabetArrayToDraw = this.mAlphabetArray;
                this.mAlphabetToDrawSize = this.mAlphabetSize;
                adjustSeparatorHeight();
                int digitIndexCount = 0;
                if (this.mAlphabetArrayFirstLetterIndex > 0 && SeslIndexScrollView.this.mIndexer.isUseDigitIndex()) {
                    digitIndexCount = 1;
                }
                if (SeslIndexScrollView.this.mNumberOfLanguages == 1) {
                    calcDotPosition(this.mFirstLang, this.mAlphabetArrayFirstLetterIndex, 0, digitIndexCount);
                    return;
                }
                calcDotPosition(this.mFirstLang, this.mAlphabetArrayFirstLetterIndex, 0, 0);
                calcDotPosition(this.mSecondLang, 0, this.mAlphabetSize - this.mAlphabetArrayLastLetterIndex, digitIndexCount);
            }
        }

        private void calcDotPosition(LangAttributeValues language, int specialIndexCount, int startIndexPosition, int digitIndexCount) {
            int index;
            int earlyLangCount = language.indexCount - specialIndexCount;
            int numberOfMissingElements = 0;
            int endIndexPosition = language.totalCount + startIndexPosition;
            boolean isFullCountState = false;
            for (index = startIndexPosition; index < endIndexPosition; index++) {
                language.alphabetArray[index - startIndexPosition] = this.mAlphabetArray[index];
            }
            while (language.separatorHeight < this.mContentMinHeight && this.mAlphabetArrayToDraw.length > 0) {
                int langCount = earlyLangCount - digitIndexCount;
                int fullDotCount = (langCount / 2) - 1;
                String[] alphabetArrWithDots;
                int targetIndex;
                if (language.dotCount >= fullDotCount || isFullCountState) {
                    isFullCountState = true;
                    boolean isDotPosition = false;
                    int dotCount = 0;
                    switch ((language.totalCount - specialIndexCount) - digitIndexCount) {
                        case 0:
                            if (digitIndexCount <= 0) {
                                if (specialIndexCount > 0) {
                                    specialIndexCount--;
                                    break;
                                }
                            }
                            digitIndexCount--;
                            break;
                            break;
                        case 1:
                            if (specialIndexCount != 0 && language.dotCount == 0) {
                                language.indexCount--;
                                language.dotCount++;
                                isDotPosition = true;
                            } else if (specialIndexCount == 0 || language.dotCount != 1) {
                                language.indexCount--;
                                language.totalCount--;
                            } else {
                                language.dotCount--;
                                language.totalCount--;
                            }
                            numberOfMissingElements++;
                            break;
                        case 2:
                            language.dotCount--;
                            language.totalCount--;
                            break;
                        case 3:
                            language.indexCount--;
                            language.totalCount--;
                            numberOfMissingElements++;
                            break;
                        default:
                            if (((language.indexCount - language.dotCount) - specialIndexCount) - digitIndexCount != 1) {
                                language.indexCount--;
                                language.totalCount--;
                                numberOfMissingElements++;
                                break;
                            }
                            language.dotCount--;
                            language.totalCount--;
                            break;
                    }
                    if (language.totalCount <= 0 || language.dotCount < 0 || language.indexCount < 0) {
                        adjustSeparatorHeight();
                        return;
                    }
                    alphabetArrWithDots = new String[language.totalCount];
                    int averageElementsEachDot = 0;
                    int extraMissingElements = 0;
                    if (language.dotCount > 0) {
                        averageElementsEachDot = numberOfMissingElements / language.dotCount;
                        extraMissingElements = numberOfMissingElements % language.dotCount;
                    }
                    for (index = 0; index < specialIndexCount; index++) {
                        alphabetArrWithDots[index] = this.mAlphabetArray[index];
                    }
                    targetIndex = specialIndexCount;
                    int indexCount = language.totalCount - digitIndexCount;
                    for (index = specialIndexCount; index < indexCount; index++) {
                        if (targetIndex < this.mAlphabetArray.length - digitIndexCount) {
                            if (isDotPosition) {
                                alphabetArrWithDots[index] = ".";
                                dotCount++;
                                targetIndex += averageElementsEachDot;
                                if (extraMissingElements > 0) {
                                    extraMissingElements--;
                                    targetIndex++;
                                }
                                isDotPosition = false;
                            } else {
                                alphabetArrWithDots[index] = this.mAlphabetArray[targetIndex + startIndexPosition];
                                targetIndex++;
                                if (dotCount < language.dotCount) {
                                    isDotPosition = true;
                                }
                            }
                        }
                    }
                    if (digitIndexCount > 0) {
                        alphabetArrWithDots[indexCount] = this.mAlphabetArray[this.mAlphabetArray.length - 1];
                    }
                    language.alphabetArray = alphabetArrWithDots;
                } else {
                    alphabetArrWithDots = new String[language.totalCount];
                    language.dotCount++;
                    language.indexCount--;
                    numberOfMissingElements++;
                    int gapOfDot = (langCount / (language.dotCount + 1)) + 1;
                    if (language.dotCount == fullDotCount) {
                        gapOfDot = 2;
                    }
                    int remainDotCount = language.dotCount;
                    int indexShift = 0;
                    while (remainDotCount != 0) {
                        if (remainDotCount != language.dotCount) {
                            remainDotCount = language.dotCount;
                        }
                        for (index = startIndexPosition; index < endIndexPosition; index++) {
                            alphabetArrWithDots[index - startIndexPosition] = this.mAlphabetArray[index];
                        }
                        for (int i = 1; i < language.dotCount + 1; i++) {
                            targetIndex = (gapOfDot * i) - (indexShift * i);
                            if (specialIndexCount > 1) {
                                targetIndex += specialIndexCount - 1;
                            }
                            if (targetIndex > 0 && targetIndex < langCount) {
                                alphabetArrWithDots[targetIndex] = ".";
                                remainDotCount--;
                            } else if (targetIndex >= langCount && remainDotCount > 0) {
                                if (targetIndex - (gapOfDot / 2) < langCount) {
                                    alphabetArrWithDots[targetIndex - (gapOfDot / 2)] = ".";
                                    remainDotCount--;
                                } else {
                                    indexShift = 1;
                                }
                            }
                        }
                    }
                    language.alphabetArray = alphabetArrWithDots;
                }
                adjustSeparatorHeight();
            }
        }

        public String getIndexByPosition(int x, int y) {
            if (this.mBgRect == null) {
                return "";
            }
            if (!this.mIsAlphabetInit) {
                return "";
            }
            if ((this.mPosition == 0 && x < this.mBgRect.left - this.mAdditionalSpace) || (this.mPosition == 1 && x > this.mBgRect.right + this.mAdditionalSpace)) {
                return "";
            }
            if (x < this.mBgRect.left - this.mAdditionalSpace || x > this.mBgRect.right + this.mAdditionalSpace) {
                if ((this.mPosition == 0 && x >= (this.mWidthShift + this.mItemWidth) + this.mItemWidthGap) || (this.mPosition == 1 && x <= (this.mWidth - this.mWidthShift) - (this.mItemWidth + this.mItemWidthGap))) {
                    return null;
                }
                if (!isInSelectedIndexRect(y)) {
                    return getIndexByY(y);
                }
                if (this.mAlphabetArrayToDraw == null || this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetArrayToDraw.length) {
                    return "";
                }
                return this.mAlphabetArrayToDraw[this.mSelectedIndex];
            } else if (!isInSelectedIndexRect(y)) {
                return getIndexByY(y);
            } else {
                if (this.mAlphabetArrayToDraw == null || this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetArrayToDraw.length) {
                    return "";
                }
                return getIndexByY(y);
            }
        }

        private int getIndex(int y) {
            int index;
            float firstLangCount = (float) (this.mAlphabetSize - this.mAlphabetArrayLastLetterIndex);
            if (((float) y) < ((float) (this.mScrollTop + this.mScrollTopMargin)) + this.mFirstLang.height) {
                index = (int) (((float) ((y - this.mScrollTop) - this.mScrollTopMargin)) / (this.mFirstLang.height / firstLangCount));
            } else {
                index = (int) (((float) ((int) ((((float) ((y - this.mScrollTop) - this.mScrollTopMargin)) - this.mFirstLang.height) / (this.mSecondLang.height / ((float) this.mAlphabetArrayLastLetterIndex))))) + firstLangCount);
            }
            if (index < 0) {
                return 0;
            }
            if (index >= this.mAlphabetToDrawSize) {
                return this.mAlphabetToDrawSize - 1;
            }
            return index;
        }

        private String getIndexByY(int y) {
            if (y <= this.mBgRect.top - this.mAdditionalSpace || y >= this.mBgRect.bottom + this.mAdditionalSpace) {
                return "";
            }
            if (y < this.mBgRect.top) {
                this.mSelectedIndex = 0;
            } else if (y > this.mBgRect.bottom) {
                this.mSelectedIndex = this.mAlphabetToDrawSize - 1;
            } else {
                this.mSelectedIndex = getIndex(y);
                if (this.mSelectedIndex == this.mAlphabetToDrawSize) {
                    this.mSelectedIndex--;
                }
            }
            if (this.mSelectedIndex == this.mAlphabetToDrawSize || this.mSelectedIndex == this.mAlphabetToDrawSize + 1) {
                this.mSelectedIndex = this.mAlphabetToDrawSize - 1;
            }
            if (this.mAlphabetArrayToDraw == null || this.mSelectedIndex <= -1 || this.mSelectedIndex > this.mAlphabetToDrawSize) {
                return "";
            }
            return this.mAlphabetArrayToDraw[this.mSelectedIndex];
        }

        private boolean isInSelectedIndexRect(int y) {
            if (this.mSelectedIndex == -1 || this.mSelectedIndex >= this.mAlphabetToDrawSize || y < ((int) (((float) (this.mScrollTop + this.mScrollTopMargin)) + (this.mSeparatorHeight * ((float) this.mSelectedIndex)))) || y > ((int) (((float) (this.mScrollTop + this.mScrollTopMargin)) + (this.mSeparatorHeight * ((float) (this.mSelectedIndex + 1)))))) {
                return false;
            }
            return true;
        }

        public void resetSelectedIndex() {
            this.mSelectedIndex = -1;
        }

        public void draw(Canvas canvas) {
            if (this.mIsAlphabetInit) {
                drawScroll(canvas);
            }
        }

        public void drawScroll(Canvas canvas) {
            drawBgRectangle(canvas);
            drawAlphabetCharacters(canvas);
            if ((this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetSize) && SeslIndexScrollView.this.mIndexScrollPreview != null) {
                SeslIndexScrollView.this.mIndexScrollPreview.close();
            }
        }

        public void setEffectText(String effectText) {
            this.mBigText = effectText;
        }

        public void drawEffect(float effectPositionY) {
            if (this.mSelectedIndex != -1) {
                float topDrawY;
                float bottomDrawY;
                this.mSmallText = this.mAlphabetArrayToDraw[this.mSelectedIndex];
                this.mPaint.getTextBounds(this.mSmallText, 0, this.mSmallText.length(), this.mTextBounds);
                if (((float) this.mScreenHeight) <= (((this.mIndexScrollPreviewRadius * 2.0f) + this.mPreviewLimitY) + ((float) this.mScrollTopMargin)) + ((float) this.mScrollBottomMargin)) {
                    topDrawY = ((float) (this.mScrollTop + this.mScrollTopMargin)) + (this.mFirstLang.separatorHeight * 0.5f);
                    bottomDrawY = ((((float) ((this.mScrollTop + this.mScrollTopMargin) - this.mScrollBottomMargin)) + this.mFirstLang.height) + this.mSecondLang.height) - (this.mFirstLang.separatorHeight * 0.5f);
                } else {
                    topDrawY = (((float) this.mScrollTopMargin) + this.mPreviewLimitY) + this.mIndexScrollPreviewRadius;
                    bottomDrawY = (((float) (this.mScreenHeight - this.mScrollBottomMargin)) - this.mPreviewLimitY) - this.mIndexScrollPreviewRadius;
                }
                float drawY = SeslIndexScrollView.OUT_OF_BOUNDARY;
                if (effectPositionY > topDrawY && effectPositionY < bottomDrawY) {
                    drawY = effectPositionY;
                } else if (effectPositionY <= topDrawY) {
                    drawY = topDrawY;
                } else if (effectPositionY >= bottomDrawY) {
                    drawY = bottomDrawY;
                }
                if (drawY != SeslIndexScrollView.OUT_OF_BOUNDARY) {
                    SeslIndexScrollView.this.mIndexScrollPreview.open(drawY, this.mBigText);
                    if (SeslIndexScrollView.this.mOnIndexBarEventListener != null) {
                        SeslIndexScrollView.this.mOnIndexBarEventListener.onPressed(drawY);
                    }
                }
            }
        }

        private void allocateBgRectangle() {
            int right;
            int left;
            if (this.mPosition == 1) {
                right = this.mWidth - this.mWidthShift;
                left = right - this.mBgRectWidth;
            } else {
                right = this.mWidthShift + this.mBgRectWidth;
                left = this.mWidthShift;
            }
            if (this.mBgRect == null) {
                this.mBgRect = new Rect(left, (this.mScrollTop + this.mScrollTopMargin) - this.mContentPadding, right, ((this.mHeight + this.mScrollTop) + this.mScrollTopMargin) + this.mContentPadding);
            } else {
                this.mBgRect.set(left, (this.mScrollTop + this.mScrollTopMargin) - this.mContentPadding, right, ((this.mHeight + this.mScrollTop) + this.mScrollTopMargin) + this.mContentPadding);
            }
            this.mScrollThumbBgRectHeight = ((int) (this.mContentMinHeight * 3.0f)) + this.mScrollThumbAdditionalHeight;
            left += this.mScrollThumbBgRectPadding;
            right -= this.mScrollThumbBgRectPadding;
            int top = (int) (SeslIndexScrollView.this.mTouchY - ((float) (this.mScrollThumbBgRectHeight / 2)));
            int bottom = (int) (SeslIndexScrollView.this.mTouchY + ((float) (this.mScrollThumbBgRectHeight / 2)));
            if ((top < this.mBgRect.top + this.mScrollThumbBgRectPadding && bottom > this.mBgRect.bottom - this.mScrollThumbBgRectPadding) || this.mScrollThumbBgRectHeight >= (this.mBgRect.bottom - this.mBgRect.top) - (this.mScrollThumbBgRectPadding * 2)) {
                top = this.mBgRect.top + this.mScrollThumbBgRectPadding;
                bottom = this.mBgRect.bottom - this.mScrollThumbBgRectPadding;
            } else if (top < this.mBgRect.top + this.mScrollThumbBgRectPadding) {
                top = this.mBgRect.top + this.mScrollThumbBgRectPadding;
                bottom = top + this.mScrollThumbBgRectHeight;
            } else if (bottom > this.mBgRect.bottom - this.mScrollThumbBgRectPadding) {
                bottom = this.mBgRect.bottom - this.mScrollThumbBgRectPadding;
                top = bottom - this.mScrollThumbBgRectHeight;
            }
            if (this.mScrollThumbBgRect == null) {
                this.mScrollThumbBgRect = new Rect(left, top, right, bottom);
            } else {
                this.mScrollThumbBgRect.set(left, top, right, bottom);
            }
        }

        private void drawBgRectangle(Canvas canvas) {
            if (!this.mBgRectParamsSet) {
                setBgRectParams();
                this.mBgRectParamsSet = true;
            }
            this.mBgDrawableDefault.draw(canvas);
            if (SeslIndexScrollView.this.mTouchY != SeslIndexScrollView.OUT_OF_BOUNDARY) {
                this.mScrollThumbBgDrawable.draw(canvas);
            }
        }

        private void setBgRectParams() {
            allocateBgRectangle();
            this.mBgDrawableDefault.setBounds(this.mBgRect);
            this.mScrollThumbBgDrawable.setBounds(this.mScrollThumbBgRect);
        }

        private void drawAlphabetCharacters(Canvas canvas) {
            this.mPaint.setColor(this.mTextColorDimmed);
            this.mPaint.setTextSize((float) this.mTextSize);
            if (this.mAlphabetArrayToDraw != null && this.mFirstLang.totalCount != 0) {
                float startPosY = (float) (this.mScrollTop + this.mScrollTopMargin);
                int indexCount = this.mFirstLang.totalCount + this.mSecondLang.totalCount;
                for (int index = 0; index < indexCount; index++) {
                    String text;
                    float separatorHeight;
                    float textPosY;
                    if (index < this.mFirstLang.totalCount) {
                        text = this.mFirstLang.alphabetArray[index];
                        separatorHeight = this.mFirstLang.separatorHeight;
                    } else {
                        text = this.mSecondLang.alphabetArray[index - this.mFirstLang.totalCount];
                        separatorHeight = this.mSecondLang.separatorHeight;
                    }
                    this.mPaint.getTextBounds(text, 0, text.length(), this.mTextBounds);
                    float textPosX = ((float) this.mBgRect.centerX()) - (this.mPaint.measureText(text) * 0.5f);
                    if (".".equals(text)) {
                        textPosY = startPosY + ((((float) this.mDotHeight) * 0.5f) - (((float) this.mTextBounds.top) * 0.5f));
                        startPosY += (float) this.mDotHeight;
                    } else {
                        textPosY = startPosY + ((separatorHeight * 0.5f) - (((float) this.mTextBounds.top) * 0.5f));
                        startPosY += separatorHeight;
                    }
                    canvas.drawText(text, textPosX, textPosY, this.mPaint);
                }
            }
        }
    }

    class IndexScrollPreview extends View {
        private boolean mIsOpen = false;
        private float mPreviewCenterMargin;
        private float mPreviewCenterX;
        private float mPreviewCenterY;
        private float mPreviewRadius;
        private String mPreviewText;
        private Paint mShapePaint;
        private Rect mTextBounds;
        private Paint mTextPaint;
        private int mTextSize;
        private int mTextWidhtLimit;

        public IndexScrollPreview(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            Resources res = context.getResources();
            this.mShapePaint = new Paint();
            this.mShapePaint.setStyle(Style.FILL);
            this.mShapePaint.setAntiAlias(true);
            this.mTextSize = (int) res.getDimension(C0247R.dimen.sesl_index_scroll_preview_text_size);
            this.mTextWidhtLimit = (int) res.getDimension(C0247R.dimen.sesl_index_scroll_preview_text_width_limit);
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTypeface(SeslIndexScrollView.this.mSECRobotoLightRegularFont);
            this.mTextPaint.setTextAlign(Align.CENTER);
            this.mTextPaint.setTextSize((float) this.mTextSize);
            this.mTextPaint.setColor(ResourcesCompat.getColor(res, C0247R.color.sesl_index_scroll_preview_text_color_light, null));
            this.mTextBounds = new Rect();
            this.mPreviewRadius = res.getDimension(C0247R.dimen.sesl_index_scroll_preview_radius);
            this.mPreviewCenterMargin = res.getDimension(C0247R.dimen.sesl_index_scroll_preview_margin_center);
            this.mIsOpen = false;
        }

        public void setLayout(int l, int t, int r, int b) {
            layout(l, t, r, b);
            if (SeslIndexScrollView.this.mIndexBarGravity == 0) {
                this.mPreviewCenterX = this.mPreviewCenterMargin;
            } else {
                this.mPreviewCenterX = ((float) r) - this.mPreviewCenterMargin;
            }
        }

        public void setBackgroundColor(int bgColor) {
            this.mShapePaint.setColor(bgColor);
        }

        public void setTextColor(int txtColor) {
            this.mTextPaint.setColor(txtColor);
        }

        public void open(float y, String text) {
            int textSize = this.mTextSize;
            this.mPreviewCenterY = y;
            this.mPreviewText = text;
            this.mTextPaint.setTextSize((float) textSize);
            while (this.mTextPaint.measureText(text) > ((float) this.mTextWidhtLimit)) {
                textSize--;
                this.mTextPaint.setTextSize((float) textSize);
            }
            if (!this.mIsOpen) {
                startAnimation();
                this.mIsOpen = true;
            }
        }

        public void close() {
            long gap = System.currentTimeMillis() - SeslIndexScrollView.this.mStartTouchDown;
            removeCallbacks(SeslIndexScrollView.this.mPreviewDelayRunnable);
            if (gap <= 100) {
                postDelayed(SeslIndexScrollView.this.mPreviewDelayRunnable, 100);
            } else {
                fadeOutAnimation();
            }
        }

        private void fadeOutAnimation() {
            if (this.mIsOpen) {
                startAnimation();
                this.mIsOpen = false;
            }
        }

        public void startAnimation() {
            ObjectAnimator anim;
            if (this.mIsOpen) {
                anim = ObjectAnimator.ofFloat(SeslIndexScrollView.this.mIndexScrollPreview, "alpha", new float[]{1.0f, 0.0f});
            } else {
                anim = ObjectAnimator.ofFloat(SeslIndexScrollView.this.mIndexScrollPreview, "alpha", new float[]{0.0f, 1.0f});
            }
            anim.setDuration(167);
            AnimatorSet set = new AnimatorSet();
            set.play(anim);
            set.start();
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.mIsOpen) {
                canvas.drawCircle(this.mPreviewCenterX, this.mPreviewCenterY, this.mPreviewRadius, this.mShapePaint);
                this.mTextPaint.getTextBounds(this.mPreviewText, 0, this.mPreviewText.length() - 1, this.mTextBounds);
                canvas.drawText(this.mPreviewText, this.mPreviewCenterX, this.mPreviewCenterY - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f), this.mTextPaint);
            }
        }
    }

    class IndexerObserver extends DataSetObserver {
        private final long INDEX_UPDATE_DELAY = 200;
        boolean mDataInvalid = false;
        Runnable mUpdateIndex = new C03701();

        /* renamed from: android.support.v7.widget.SeslIndexScrollView$IndexerObserver$1 */
        class C03701 implements Runnable {
            C03701() {
            }

            public void run() {
                IndexerObserver.this.mDataInvalid = false;
            }
        }

        IndexerObserver() {
        }

        public void onChanged() {
            super.onChanged();
            notifyDataSetChange();
        }

        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChange();
        }

        public boolean hasIndexerDataValid() {
            return !this.mDataInvalid;
        }

        private void notifyDataSetChange() {
            this.mDataInvalid = true;
            SeslIndexScrollView.this.removeCallbacks(this.mUpdateIndex);
            SeslIndexScrollView.this.postDelayed(this.mUpdateIndex, 200);
        }
    }

    public interface OnIndexBarEventListener {
        void onIndexChanged(int i);

        void onPressed(float f);

        void onReleased(float f);
    }

    public SeslIndexScrollView(Context context) {
        super(context);
        this.mContext = context;
        this.mCurrentIndex = null;
        init();
    }

    public SeslIndexScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mIndexBarGravity = 1;
        init();
    }

    private void init() {
        this.mViewGroupOverlay = getOverlay();
        if (this.mIndexScrollPreview == null) {
            this.mIndexScrollPreview = new IndexScrollPreview(this.mContext);
            this.mIndexScrollPreview.setLayout(0, 0, getWidth(), getHeight());
            this.mViewGroupOverlay.add(this.mIndexScrollPreview);
        }
        this.mHasOverlayChild = true;
        this.mIndexScroll = new IndexScroll(this.mContext, getHeight(), getWidth(), this.mIndexBarGravity);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mIndexScroll != null) {
            this.mIndexScroll.setDimensions(getWidth(), getHeight());
            if (!(this.mCurrentIndex == null || this.mCurrentIndex.length() == 0 || this.mIndexScrollPreview == null)) {
                this.mIndexScrollPreview.setLayout(0, 0, getWidth(), getHeight());
                this.mIndexScrollPreview.invalidate();
            }
            if (this.mIndexScroll != null && this.mIndexScroll.isAlphabetInit()) {
                this.mIndexScroll.draw(canvas);
            }
        }
    }

    public void setIndexer(SeslAbsIndexer indexer) {
        if (indexer == null) {
            throw new IllegalArgumentException("SeslIndexView.setIndexer(indexer) : indexer=null.");
        }
        if (this.mIndexer != null && this.mRegisteredDataSetObserver) {
            this.mIndexer.unregisterDataSetObserver(this.mIndexerObserver);
            this.mRegisteredDataSetObserver = false;
        }
        this.mIsSimpleIndexScroll = false;
        this.mIndexer = indexer;
        this.mIndexer.registerDataSetObserver(this.mIndexerObserver);
        this.mRegisteredDataSetObserver = true;
        if (this.mIndexScroll.mScrollThumbBgDrawable != null) {
            this.mIndexScroll.mScrollThumbBgDrawable.setColorFilter(this.mIndexScroll.mThumbColor, Mode.MULTIPLY);
        }
        this.mIndexer.cacheIndexInfo();
        this.mIndexScroll.setAlphabetArray(this.mIndexer.getAlphabetArray(), getFirstAlphabetCharacterIndex(), getLastAlphabetCharacterIndex());
        if (!this.mIsSimpleIndexScroll && this.mIndexer != null && this.mIndexer.getLangAlphabetArray() != null) {
            this.mNumberOfLanguages = this.mIndexer.getLangAlphabetArray().length;
        }
    }

    public void setSimpleIndexScroll(String[] indexBarChar, int width) {
        if (indexBarChar == null) {
            throw new IllegalArgumentException("SeslIndexView.setSimpleIndexScroll(indexBarChar) ");
        }
        this.mIsSimpleIndexScroll = true;
        setSimpleIndexWidth((int) this.mContext.getResources().getDimension(C0247R.dimen.sesl_indexbar_simple_index_width));
        if (width != 0) {
            setSimpleIndexWidth(width);
        }
        if (this.mIndexScroll.mScrollThumbBgDrawable != null) {
            this.mIndexScroll.mScrollThumbBgDrawable.setColorFilter(this.mIndexScroll.mThumbColor, Mode.MULTIPLY);
        }
        this.mIndexScroll.setAlphabetArray(indexBarChar, -1, -1);
    }

    private void setSimpleIndexWidth(int width) {
        if (this.mIndexScroll != null) {
            this.mIndexScroll.setSimpleIndexScrollWidth(width);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mHasOverlayChild) {
            this.mViewGroupOverlay.remove(this.mIndexScrollPreview);
            this.mHasOverlayChild = false;
        }
        if (this.mIndexer != null && this.mRegisteredDataSetObserver) {
            this.mIndexer.unregisterDataSetObserver(this.mIndexerObserver);
            this.mRegisteredDataSetObserver = false;
        }
        if (this.mPreviewDelayRunnable != null) {
            removeCallbacks(this.mPreviewDelayRunnable);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mHasOverlayChild) {
            this.mViewGroupOverlay.add(this.mIndexScrollPreview);
            this.mHasOverlayChild = true;
        }
        if (this.mIndexer != null && !this.mRegisteredDataSetObserver) {
            this.mIndexer.registerDataSetObserver(this.mIndexerObserver);
            this.mRegisteredDataSetObserver = true;
        }
    }

    public void setIndexBarBackgroundDrawable(Drawable bgDrawable) {
        this.mIndexScroll.mBgDrawableDefault = bgDrawable;
    }

    public void setIndexBarBackgroundColor(int bgColor) {
        this.mIndexScroll.mBgDrawableDefault.setColorFilter(bgColor, Mode.MULTIPLY);
    }

    public void setIndexBarTextColor(int textColor) {
        this.mIndexScroll.mTextColorDimmed = textColor;
    }

    public void setIndexBarPressedTextColor(int pressedTextColor) {
        this.mIndexScroll.mScrollThumbBgDrawable.setColorFilter(pressedTextColor, Mode.MULTIPLY);
        this.mIndexScroll.mThumbColor = pressedTextColor;
    }

    public void setEffectTextColor(int effectTextColor) {
        this.mIndexScrollPreview.setTextColor(effectTextColor);
    }

    public void setEffectBackgroundColor(int effectBackgroundColor) {
        this.mIndexScrollPreview.setBackgroundColor(this.mIndexScroll.getColorWithAlpha(effectBackgroundColor, 0.8f));
    }

    public void setIndexBarGravity(int gravity) {
        this.mIndexBarGravity = gravity;
        this.mIndexScroll.setPosition(gravity);
    }

    private int getListViewPosition(String indexPath) {
        if (indexPath == null || this.mIndexer == null) {
            return -1;
        }
        return this.mIndexer.getCachingValue(this.mIndexScroll.getSelectedIndex());
    }

    public void setIndexScrollMargin(int topMargin, int bottomMargin) {
        if (this.mIndexScroll != null) {
            this.mIndexScroll.setIndexScrollBgMargin(topMargin, bottomMargin);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return handleMotionEvent(ev);
    }

    private boolean handleMotionEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        float x = ev.getX();
        int position;
        switch (action) {
            case 0:
                this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                this.mStartTouchDown = System.currentTimeMillis();
                if (this.mCurrentIndex != null) {
                    if (!(!this.mIndexScroll.isAlphabetInit() || this.mCurrentIndex == null || this.mCurrentIndex.length() == 0)) {
                        this.mIndexScroll.setEffectText(this.mCurrentIndex);
                        this.mIndexScroll.drawEffect(y);
                        this.mIndexScrollPreview.setLayout(0, 0, getWidth(), getHeight());
                        this.mIndexScrollPreview.invalidate();
                        this.mTouchY = y;
                    }
                    if (this.mIsSimpleIndexScroll) {
                        position = this.mIndexScroll.getSelectedIndex();
                    } else {
                        position = getListViewPosition(this.mCurrentIndex);
                    }
                    if (position != -1) {
                        notifyIndexChange(position);
                        break;
                    }
                }
                return false;
                break;
            case 1:
            case 3:
                this.mCurrentIndex = null;
                this.mIndexScroll.resetSelectedIndex();
                this.mIndexScrollPreview.close();
                this.mTouchY = OUT_OF_BOUNDARY;
                if (this.mOnIndexBarEventListener != null) {
                    this.mOnIndexBarEventListener.onReleased(y);
                    break;
                }
                break;
            case 2:
                String calculatedIndexStr = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                if (this.mCurrentIndex == null || calculatedIndexStr != null || this.mIsSimpleIndexScroll) {
                    if (this.mCurrentIndex != null && calculatedIndexStr != null && calculatedIndexStr.length() < this.mCurrentIndex.length()) {
                        this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                        if (this.mIsSimpleIndexScroll) {
                            position = this.mIndexScroll.getSelectedIndex();
                        } else {
                            position = getListViewPosition(this.mCurrentIndex);
                        }
                        if (position != -1) {
                            notifyIndexChange(position);
                            break;
                        }
                    }
                    this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                    if (!(!this.mIndexScroll.isAlphabetInit() || this.mCurrentIndex == null || this.mCurrentIndex.length() == 0)) {
                        this.mIndexScroll.setEffectText(this.mCurrentIndex);
                        this.mIndexScroll.drawEffect(y);
                        this.mTouchY = y;
                    }
                    if (this.mIsSimpleIndexScroll) {
                        position = this.mIndexScroll.getSelectedIndex();
                    } else {
                        position = getListViewPosition(this.mCurrentIndex);
                    }
                    if (position != -1) {
                        notifyIndexChange(position);
                        break;
                    }
                }
                calculatedIndexStr = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                position = getListViewPosition(calculatedIndexStr);
                if (position != -1) {
                    notifyIndexChange(position);
                    break;
                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private int getFirstAlphabetCharacterIndex() {
        int currentLang = this.mIndexer.getCurrentLang();
        int indexerAlphabetSize = this.mIndexer.getAlphabetArray().length;
        int index = 0;
        while (index < indexerAlphabetSize && currentLang != this.mIndexer.getLangbyIndex(index)) {
            index++;
        }
        return index < indexerAlphabetSize ? index : -1;
    }

    private int getLastAlphabetCharacterIndex() {
        if (this.mIndexer == null) {
            return -1;
        }
        int currentLang = this.mIndexer.getCurrentLang();
        int indexerAlphabetSize = this.mIndexer.getAlphabetArray().length;
        int index = indexerAlphabetSize - 1;
        while (index >= 0 && this.mIndexer != null && currentLang != this.mIndexer.getLangbyIndex(index)) {
            index--;
        }
        if (index > 0) {
            return (indexerAlphabetSize - 1) - index;
        }
        return -1;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void notifyIndexChange(int pos) {
        if (this.mOnIndexBarEventListener != null) {
            this.mOnIndexBarEventListener.onIndexChanged(pos);
        }
    }

    public void setOnIndexBarEventListener(OnIndexBarEventListener iOnIndexBarEventListener) {
        this.mOnIndexBarEventListener = iOnIndexBarEventListener;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
