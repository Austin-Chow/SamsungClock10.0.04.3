package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.constraint.Constraints.LayoutParams;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.View;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConstraintSet {
    private static final int[] VISIBILITY_FLAGS = new int[]{0, 4, 8};
    private static SparseIntArray mapToConstant = new SparseIntArray();
    private HashMap<Integer, Constraint> mConstraints = new HashMap();

    private static class Constraint {
        public float alpha;
        public boolean applyElevation;
        public int baselineToBaseline;
        public int bottomMargin;
        public int bottomToBottom;
        public int bottomToTop;
        public float circleAngle;
        public int circleConstraint;
        public int circleRadius;
        public boolean constrainedHeight;
        public boolean constrainedWidth;
        public String dimensionRatio;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public float elevation;
        public int endMargin;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public int heightDefault;
        public int heightMax;
        public int heightMin;
        public float heightPercent;
        public float horizontalBias;
        public int horizontalChainStyle;
        public float horizontalWeight;
        public int leftMargin;
        public int leftToLeft;
        public int leftToRight;
        public int mBarrierDirection;
        public int mHeight;
        public int mHelperType;
        boolean mIsGuideline;
        public int[] mReferenceIds;
        int mViewId;
        public int mWidth;
        public int orientation;
        public int rightMargin;
        public int rightToLeft;
        public int rightToRight;
        public float rotation;
        public float rotationX;
        public float rotationY;
        public float scaleX;
        public float scaleY;
        public int startMargin;
        public int startToEnd;
        public int startToStart;
        public int topMargin;
        public int topToBottom;
        public int topToTop;
        public float transformPivotX;
        public float transformPivotY;
        public float translationX;
        public float translationY;
        public float translationZ;
        public float verticalBias;
        public int verticalChainStyle;
        public float verticalWeight;
        public int visibility;
        public int widthDefault;
        public int widthMax;
        public int widthMin;
        public float widthPercent;

        private Constraint() {
            this.mIsGuideline = false;
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.circleConstraint = -1;
            this.circleRadius = 0;
            this.circleAngle = 0.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.leftMargin = -1;
            this.rightMargin = -1;
            this.topMargin = -1;
            this.bottomMargin = -1;
            this.endMargin = -1;
            this.startMargin = -1;
            this.visibility = 0;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneEndMargin = -1;
            this.goneStartMargin = -1;
            this.verticalWeight = 0.0f;
            this.horizontalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.alpha = 1.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
            this.rotation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = Float.NaN;
            this.transformPivotY = Float.NaN;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
            this.constrainedWidth = false;
            this.constrainedHeight = false;
            this.widthDefault = 0;
            this.heightDefault = 0;
            this.widthMax = -1;
            this.heightMax = -1;
            this.widthMin = -1;
            this.heightMin = -1;
            this.widthPercent = 1.0f;
            this.heightPercent = 1.0f;
            this.mBarrierDirection = -1;
            this.mHelperType = -1;
        }

        public Constraint clone() {
            Constraint clone = new Constraint();
            clone.mIsGuideline = this.mIsGuideline;
            clone.mWidth = this.mWidth;
            clone.mHeight = this.mHeight;
            clone.guideBegin = this.guideBegin;
            clone.guideEnd = this.guideEnd;
            clone.guidePercent = this.guidePercent;
            clone.leftToLeft = this.leftToLeft;
            clone.leftToRight = this.leftToRight;
            clone.rightToLeft = this.rightToLeft;
            clone.rightToRight = this.rightToRight;
            clone.topToTop = this.topToTop;
            clone.topToBottom = this.topToBottom;
            clone.bottomToTop = this.bottomToTop;
            clone.bottomToBottom = this.bottomToBottom;
            clone.baselineToBaseline = this.baselineToBaseline;
            clone.startToEnd = this.startToEnd;
            clone.startToStart = this.startToStart;
            clone.endToStart = this.endToStart;
            clone.endToEnd = this.endToEnd;
            clone.horizontalBias = this.horizontalBias;
            clone.verticalBias = this.verticalBias;
            clone.dimensionRatio = this.dimensionRatio;
            clone.editorAbsoluteX = this.editorAbsoluteX;
            clone.editorAbsoluteY = this.editorAbsoluteY;
            clone.horizontalBias = this.horizontalBias;
            clone.horizontalBias = this.horizontalBias;
            clone.horizontalBias = this.horizontalBias;
            clone.horizontalBias = this.horizontalBias;
            clone.horizontalBias = this.horizontalBias;
            clone.orientation = this.orientation;
            clone.leftMargin = this.leftMargin;
            clone.rightMargin = this.rightMargin;
            clone.topMargin = this.topMargin;
            clone.bottomMargin = this.bottomMargin;
            clone.endMargin = this.endMargin;
            clone.startMargin = this.startMargin;
            clone.visibility = this.visibility;
            clone.goneLeftMargin = this.goneLeftMargin;
            clone.goneTopMargin = this.goneTopMargin;
            clone.goneRightMargin = this.goneRightMargin;
            clone.goneBottomMargin = this.goneBottomMargin;
            clone.goneEndMargin = this.goneEndMargin;
            clone.goneStartMargin = this.goneStartMargin;
            clone.verticalWeight = this.verticalWeight;
            clone.horizontalWeight = this.horizontalWeight;
            clone.horizontalChainStyle = this.horizontalChainStyle;
            clone.verticalChainStyle = this.verticalChainStyle;
            clone.alpha = this.alpha;
            clone.applyElevation = this.applyElevation;
            clone.elevation = this.elevation;
            clone.rotation = this.rotation;
            clone.rotationX = this.rotationX;
            clone.rotationY = this.rotationY;
            clone.scaleX = this.scaleX;
            clone.scaleY = this.scaleY;
            clone.transformPivotX = this.transformPivotX;
            clone.transformPivotY = this.transformPivotY;
            clone.translationX = this.translationX;
            clone.translationY = this.translationY;
            clone.translationZ = this.translationZ;
            clone.constrainedWidth = this.constrainedWidth;
            clone.constrainedHeight = this.constrainedHeight;
            clone.widthDefault = this.widthDefault;
            clone.heightDefault = this.heightDefault;
            clone.widthMax = this.widthMax;
            clone.heightMax = this.heightMax;
            clone.widthMin = this.widthMin;
            clone.heightMin = this.heightMin;
            clone.widthPercent = this.widthPercent;
            clone.heightPercent = this.heightPercent;
            clone.mBarrierDirection = this.mBarrierDirection;
            clone.mHelperType = this.mHelperType;
            if (this.mReferenceIds != null) {
                clone.mReferenceIds = Arrays.copyOf(this.mReferenceIds, this.mReferenceIds.length);
            }
            clone.circleConstraint = this.circleConstraint;
            clone.circleRadius = this.circleRadius;
            clone.circleAngle = this.circleAngle;
            return clone;
        }

        private void fillFromConstraints(ConstraintHelper helper, int viewId, LayoutParams param) {
            fillFromConstraints(viewId, param);
            if (helper instanceof Barrier) {
                this.mHelperType = 1;
                Barrier barrier = (Barrier) helper;
                this.mBarrierDirection = barrier.getType();
                this.mReferenceIds = barrier.getReferencedIds();
            }
        }

        private void fillFromConstraints(int viewId, LayoutParams param) {
            fillFrom(viewId, param);
            this.alpha = param.alpha;
            this.rotation = param.rotation;
            this.rotationX = param.rotationX;
            this.rotationY = param.rotationY;
            this.scaleX = param.scaleX;
            this.scaleY = param.scaleY;
            this.transformPivotX = param.transformPivotX;
            this.transformPivotY = param.transformPivotY;
            this.translationX = param.translationX;
            this.translationY = param.translationY;
            this.translationZ = param.translationZ;
            this.elevation = param.elevation;
            this.applyElevation = param.applyElevation;
        }

        private void fillFrom(int viewId, ConstraintLayout.LayoutParams param) {
            this.mViewId = viewId;
            this.leftToLeft = param.leftToLeft;
            this.leftToRight = param.leftToRight;
            this.rightToLeft = param.rightToLeft;
            this.rightToRight = param.rightToRight;
            this.topToTop = param.topToTop;
            this.topToBottom = param.topToBottom;
            this.bottomToTop = param.bottomToTop;
            this.bottomToBottom = param.bottomToBottom;
            this.baselineToBaseline = param.baselineToBaseline;
            this.startToEnd = param.startToEnd;
            this.startToStart = param.startToStart;
            this.endToStart = param.endToStart;
            this.endToEnd = param.endToEnd;
            this.horizontalBias = param.horizontalBias;
            this.verticalBias = param.verticalBias;
            this.dimensionRatio = param.dimensionRatio;
            this.circleConstraint = param.circleConstraint;
            this.circleRadius = param.circleRadius;
            this.circleAngle = param.circleAngle;
            this.editorAbsoluteX = param.editorAbsoluteX;
            this.editorAbsoluteY = param.editorAbsoluteY;
            this.orientation = param.orientation;
            this.guidePercent = param.guidePercent;
            this.guideBegin = param.guideBegin;
            this.guideEnd = param.guideEnd;
            this.mWidth = param.width;
            this.mHeight = param.height;
            this.leftMargin = param.leftMargin;
            this.rightMargin = param.rightMargin;
            this.topMargin = param.topMargin;
            this.bottomMargin = param.bottomMargin;
            this.verticalWeight = param.verticalWeight;
            this.horizontalWeight = param.horizontalWeight;
            this.verticalChainStyle = param.verticalChainStyle;
            this.horizontalChainStyle = param.horizontalChainStyle;
            this.constrainedWidth = param.constrainedWidth;
            this.constrainedHeight = param.constrainedHeight;
            this.widthDefault = param.matchConstraintDefaultWidth;
            this.heightDefault = param.matchConstraintDefaultHeight;
            this.constrainedWidth = param.constrainedWidth;
            this.widthMax = param.matchConstraintMaxWidth;
            this.heightMax = param.matchConstraintMaxHeight;
            this.widthMin = param.matchConstraintMinWidth;
            this.heightMin = param.matchConstraintMinHeight;
            this.widthPercent = param.matchConstraintPercentWidth;
            this.heightPercent = param.matchConstraintPercentHeight;
            if (VERSION.SDK_INT >= 17) {
                this.endMargin = param.getMarginEnd();
                this.startMargin = param.getMarginStart();
            }
        }

        public void applyTo(ConstraintLayout.LayoutParams param) {
            param.leftToLeft = this.leftToLeft;
            param.leftToRight = this.leftToRight;
            param.rightToLeft = this.rightToLeft;
            param.rightToRight = this.rightToRight;
            param.topToTop = this.topToTop;
            param.topToBottom = this.topToBottom;
            param.bottomToTop = this.bottomToTop;
            param.bottomToBottom = this.bottomToBottom;
            param.baselineToBaseline = this.baselineToBaseline;
            param.startToEnd = this.startToEnd;
            param.startToStart = this.startToStart;
            param.endToStart = this.endToStart;
            param.endToEnd = this.endToEnd;
            param.leftMargin = this.leftMargin;
            param.rightMargin = this.rightMargin;
            param.topMargin = this.topMargin;
            param.bottomMargin = this.bottomMargin;
            param.goneStartMargin = this.goneStartMargin;
            param.goneEndMargin = this.goneEndMargin;
            param.horizontalBias = this.horizontalBias;
            param.verticalBias = this.verticalBias;
            param.circleConstraint = this.circleConstraint;
            param.circleRadius = this.circleRadius;
            param.circleAngle = this.circleAngle;
            param.dimensionRatio = this.dimensionRatio;
            param.editorAbsoluteX = this.editorAbsoluteX;
            param.editorAbsoluteY = this.editorAbsoluteY;
            param.verticalWeight = this.verticalWeight;
            param.horizontalWeight = this.horizontalWeight;
            param.verticalChainStyle = this.verticalChainStyle;
            param.horizontalChainStyle = this.horizontalChainStyle;
            param.constrainedWidth = this.constrainedWidth;
            param.constrainedHeight = this.constrainedHeight;
            param.matchConstraintDefaultWidth = this.widthDefault;
            param.matchConstraintDefaultHeight = this.heightDefault;
            param.matchConstraintMaxWidth = this.widthMax;
            param.matchConstraintMaxHeight = this.heightMax;
            param.matchConstraintMinWidth = this.widthMin;
            param.matchConstraintMinHeight = this.heightMin;
            param.matchConstraintPercentWidth = this.widthPercent;
            param.matchConstraintPercentHeight = this.heightPercent;
            param.orientation = this.orientation;
            param.guidePercent = this.guidePercent;
            param.guideBegin = this.guideBegin;
            param.guideEnd = this.guideEnd;
            param.width = this.mWidth;
            param.height = this.mHeight;
            if (VERSION.SDK_INT >= 17) {
                param.setMarginStart(this.startMargin);
                param.setMarginEnd(this.endMargin);
            }
            param.validate();
        }
    }

    static {
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintLeft_toLeftOf, 25);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintLeft_toRightOf, 26);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintRight_toLeftOf, 29);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintRight_toRightOf, 30);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintTop_toTopOf, 36);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintTop_toBottomOf, 35);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintBottom_toTopOf, 4);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintBottom_toBottomOf, 3);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintBaseline_toBaselineOf, 1);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_editor_absoluteX, 6);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_editor_absoluteY, 7);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintGuide_begin, 17);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintGuide_end, 18);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintGuide_percent, 19);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_orientation, 27);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintStart_toEndOf, 32);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintStart_toStartOf, 33);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintEnd_toStartOf, 10);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintEnd_toEndOf, 9);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_goneMarginLeft, 13);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_goneMarginTop, 16);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_goneMarginRight, 14);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_goneMarginBottom, 11);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_goneMarginStart, 15);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_goneMarginEnd, 12);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintVertical_weight, 40);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintHorizontal_weight, 39);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintHorizontal_chainStyle, 41);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintVertical_chainStyle, 42);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintHorizontal_bias, 20);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintVertical_bias, 37);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintDimensionRatio, 5);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintLeft_creator, 64);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintTop_creator, 64);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintRight_creator, 64);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintBottom_creator, 64);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintBaseline_creator, 64);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_marginLeft, 24);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_marginRight, 28);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_marginStart, 31);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_marginEnd, 8);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_marginTop, 34);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_marginBottom, 2);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_width, 23);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_layout_height, 21);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_visibility, 22);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_alpha, 43);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_elevation, 44);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_rotationX, 45);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_rotationY, 46);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_rotation, 60);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_scaleX, 47);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_scaleY, 48);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_transformPivotX, 49);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_transformPivotY, 50);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_translationX, 51);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_translationY, 52);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_translationZ, 53);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintWidth_default, 54);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintHeight_default, 55);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintWidth_max, 56);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintHeight_max, 57);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintWidth_min, 58);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintHeight_min, 59);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintCircle, 61);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintCircleRadius, 62);
        mapToConstant.append(C0007R.styleable.ConstraintSet_layout_constraintCircleAngle, 63);
        mapToConstant.append(C0007R.styleable.ConstraintSet_android_id, 38);
    }

    public void clone(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            int id = view.getId();
            if (id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                this.mConstraints.put(Integer.valueOf(id), new Constraint());
            }
            Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(id));
            constraint.fillFrom(id, param);
            constraint.visibility = view.getVisibility();
            if (VERSION.SDK_INT >= 17) {
                constraint.alpha = view.getAlpha();
                constraint.rotation = view.getRotation();
                constraint.rotationX = view.getRotationX();
                constraint.rotationY = view.getRotationY();
                constraint.scaleX = view.getScaleX();
                constraint.scaleY = view.getScaleY();
                float pivotX = view.getPivotX();
                float pivotY = view.getPivotY();
                if (!(((double) pivotX) == 0.0d && ((double) pivotY) == 0.0d)) {
                    constraint.transformPivotX = pivotX;
                    constraint.transformPivotY = pivotY;
                }
                constraint.translationX = view.getTranslationX();
                constraint.translationY = view.getTranslationY();
                if (VERSION.SDK_INT >= 21) {
                    constraint.translationZ = view.getTranslationZ();
                    if (constraint.applyElevation) {
                        constraint.elevation = view.getElevation();
                    }
                }
            }
        }
    }

    public void clone(Constraints constraints) {
        int count = constraints.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; i++) {
            View view = constraints.getChildAt(i);
            LayoutParams param = (LayoutParams) view.getLayoutParams();
            int id = view.getId();
            if (id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                this.mConstraints.put(Integer.valueOf(id), new Constraint());
            }
            Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(id));
            if (view instanceof ConstraintHelper) {
                constraint.fillFromConstraints((ConstraintHelper) view, id, param);
            }
            constraint.fillFromConstraints(id, param);
        }
    }

    public void applyTo(ConstraintLayout constraintLayout) {
        applyToInternal(constraintLayout);
        constraintLayout.setConstraintSet(null);
    }

    void applyToInternal(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        HashSet<Integer> used = new HashSet(this.mConstraints.keySet());
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            int id = view.getId();
            if (id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            Constraint constraint;
            if (this.mConstraints.containsKey(Integer.valueOf(id))) {
                used.remove(Integer.valueOf(id));
                constraint = (Constraint) this.mConstraints.get(Integer.valueOf(id));
                if (constraint.mHelperType != -1) {
                    switch (constraint.mHelperType) {
                        case 1:
                            Barrier barrier = (Barrier) view;
                            barrier.setId(id);
                            barrier.setReferencedIds(constraint.mReferenceIds);
                            barrier.setType(constraint.mBarrierDirection);
                            constraint.applyTo(constraintLayout.generateDefaultLayoutParams());
                            break;
                    }
                }
                ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                constraint.applyTo(param);
                view.setLayoutParams(param);
                view.setVisibility(constraint.visibility);
                if (VERSION.SDK_INT >= 17) {
                    view.setAlpha(constraint.alpha);
                    view.setRotation(constraint.rotation);
                    view.setRotationX(constraint.rotationX);
                    view.setRotationY(constraint.rotationY);
                    view.setScaleX(constraint.scaleX);
                    view.setScaleY(constraint.scaleY);
                    if (!Float.isNaN(constraint.transformPivotX)) {
                        view.setPivotX(constraint.transformPivotX);
                    }
                    if (!Float.isNaN(constraint.transformPivotY)) {
                        view.setPivotY(constraint.transformPivotY);
                    }
                    view.setTranslationX(constraint.translationX);
                    view.setTranslationY(constraint.translationY);
                    if (VERSION.SDK_INT >= 21) {
                        view.setTranslationZ(constraint.translationZ);
                        if (constraint.applyElevation) {
                            view.setElevation(constraint.elevation);
                        }
                    }
                }
            }
        }
        Iterator it = used.iterator();
        while (it.hasNext()) {
            Integer id2 = (Integer) it.next();
            constraint = (Constraint) this.mConstraints.get(id2);
            if (constraint.mHelperType != -1) {
                switch (constraint.mHelperType) {
                    case 1:
                        barrier = new Barrier(constraintLayout.getContext());
                        barrier.setId(id2.intValue());
                        barrier.setReferencedIds(constraint.mReferenceIds);
                        barrier.setType(constraint.mBarrierDirection);
                        param = constraintLayout.generateDefaultLayoutParams();
                        constraint.applyTo(param);
                        constraintLayout.addView(barrier, param);
                        break;
                }
            }
            if (constraint.mIsGuideline) {
                Guideline g = new Guideline(constraintLayout.getContext());
                g.setId(id2.intValue());
                param = constraintLayout.generateDefaultLayoutParams();
                constraint.applyTo(param);
                constraintLayout.addView(g, param);
            }
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide, int margin) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(startID));
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    constraint.leftToLeft = endID;
                    constraint.leftToRight = -1;
                } else if (endSide == 2) {
                    constraint.leftToRight = endID;
                    constraint.leftToLeft = -1;
                } else {
                    throw new IllegalArgumentException("Left to " + sideToString(endSide) + " undefined");
                }
                constraint.leftMargin = margin;
                return;
            case 2:
                if (endSide == 1) {
                    constraint.rightToLeft = endID;
                    constraint.rightToRight = -1;
                } else if (endSide == 2) {
                    constraint.rightToRight = endID;
                    constraint.rightToLeft = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.rightMargin = margin;
                return;
            case 3:
                if (endSide == 3) {
                    constraint.topToTop = endID;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                } else if (endSide == 4) {
                    constraint.topToBottom = endID;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.topMargin = margin;
                return;
            case 4:
                if (endSide == 4) {
                    constraint.bottomToBottom = endID;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                } else if (endSide == 3) {
                    constraint.bottomToTop = endID;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.bottomMargin = margin;
                return;
            case 5:
                if (endSide == 5) {
                    constraint.baselineToBaseline = endID;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
            case 6:
                if (endSide == 6) {
                    constraint.startToStart = endID;
                    constraint.startToEnd = -1;
                } else if (endSide == 7) {
                    constraint.startToEnd = endID;
                    constraint.startToStart = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.startMargin = margin;
                return;
            case 7:
                if (endSide == 7) {
                    constraint.endToEnd = endID;
                    constraint.endToStart = -1;
                } else if (endSide == 6) {
                    constraint.endToStart = endID;
                    constraint.endToEnd = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.endMargin = margin;
                return;
            default:
                throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(startID));
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    constraint.leftToLeft = endID;
                    constraint.leftToRight = -1;
                    return;
                } else if (endSide == 2) {
                    constraint.leftToRight = endID;
                    constraint.leftToLeft = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("left to " + sideToString(endSide) + " undefined");
                }
            case 2:
                if (endSide == 1) {
                    constraint.rightToLeft = endID;
                    constraint.rightToRight = -1;
                    return;
                } else if (endSide == 2) {
                    constraint.rightToRight = endID;
                    constraint.rightToLeft = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 3:
                if (endSide == 3) {
                    constraint.topToTop = endID;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                } else if (endSide == 4) {
                    constraint.topToBottom = endID;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 4:
                if (endSide == 4) {
                    constraint.bottomToBottom = endID;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                } else if (endSide == 3) {
                    constraint.bottomToTop = endID;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 5:
                if (endSide == 5) {
                    constraint.baselineToBaseline = endID;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
            case 6:
                if (endSide == 6) {
                    constraint.startToStart = endID;
                    constraint.startToEnd = -1;
                    return;
                } else if (endSide == 7) {
                    constraint.startToEnd = endID;
                    constraint.startToStart = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            case 7:
                if (endSide == 7) {
                    constraint.endToEnd = endID;
                    constraint.endToStart = -1;
                    return;
                } else if (endSide == 6) {
                    constraint.endToStart = endID;
                    constraint.endToEnd = -1;
                    return;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
            default:
                throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
        }
    }

    public void clear(int viewId) {
        this.mConstraints.remove(Integer.valueOf(viewId));
    }

    public void setMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.leftMargin = value;
                return;
            case 2:
                constraint.rightMargin = value;
                return;
            case 3:
                constraint.topMargin = value;
                return;
            case 4:
                constraint.bottomMargin = value;
                return;
            case 5:
                throw new IllegalArgumentException("baseline does not support margins");
            case 6:
                constraint.startMargin = value;
                return;
            case 7:
                constraint.endMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setHorizontalBias(int viewId, float bias) {
        get(viewId).horizontalBias = bias;
    }

    public void setVerticalBias(int viewId, float bias) {
        get(viewId).verticalBias = bias;
    }

    public void setAlpha(int viewId, float alpha) {
        get(viewId).alpha = alpha;
    }

    public void setScaleX(int viewId, float scaleX) {
        get(viewId).scaleX = scaleX;
    }

    public void setScaleY(int viewId, float scaleY) {
        get(viewId).scaleY = scaleY;
    }

    public void constrainWidth(int viewId, int width) {
        get(viewId).mWidth = width;
    }

    public void constrainMinHeight(int viewId, int height) {
        get(viewId).heightMin = height;
    }

    public void constrainPercentWidth(int viewId, float percent) {
        get(viewId).widthPercent = percent;
    }

    public void constrainDefaultHeight(int viewId, int height) {
        get(viewId).heightDefault = height;
    }

    public void setHorizontalChainStyle(int viewId, int chainStyle) {
        get(viewId).horizontalChainStyle = chainStyle;
    }

    public void setVerticalChainStyle(int viewId, int chainStyle) {
        get(viewId).verticalChainStyle = chainStyle;
    }

    public void addToHorizontalChain(int viewId, int leftId, int rightId) {
        int i;
        connect(viewId, 1, leftId, leftId == 0 ? 1 : 2, 0);
        if (rightId == 0) {
            i = 2;
        } else {
            i = 1;
        }
        connect(viewId, 2, rightId, i, 0);
        if (leftId != 0) {
            connect(leftId, 2, viewId, 1, 0);
        }
        if (rightId != 0) {
            connect(rightId, 1, viewId, 2, 0);
        }
    }

    public void addToVerticalChain(int viewId, int topId, int bottomId) {
        int i;
        connect(viewId, 3, topId, topId == 0 ? 3 : 4, 0);
        if (bottomId == 0) {
            i = 4;
        } else {
            i = 3;
        }
        connect(viewId, 4, bottomId, i, 0);
        if (topId != 0) {
            connect(topId, 4, viewId, 3, 0);
        }
        if (topId != 0) {
            connect(bottomId, 3, viewId, 4, 0);
        }
    }

    public void setGuidelineBegin(int guidelineID, int margin) {
        get(guidelineID).guideBegin = margin;
        get(guidelineID).guideEnd = -1;
        get(guidelineID).guidePercent = -1.0f;
    }

    public void setGuidelineEnd(int guidelineID, int margin) {
        get(guidelineID).guideEnd = margin;
        get(guidelineID).guideBegin = -1;
        get(guidelineID).guidePercent = -1.0f;
    }

    private Constraint get(int id) {
        if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
            this.mConstraints.put(Integer.valueOf(id), new Constraint());
        }
        return (Constraint) this.mConstraints.get(Integer.valueOf(id));
    }

    private String sideToString(int side) {
        switch (side) {
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "top";
            case 4:
                return "bottom";
            case 5:
                return "baseline";
            case 6:
                return "start";
            case 7:
                return "end";
            default:
                return "undefined";
        }
    }

    public void load(Context context, int resourceId) {
        XmlPullParser parser = context.getResources().getXml(resourceId);
        try {
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0:
                        String document = parser.getName();
                        break;
                    case 2:
                        String tagName = parser.getName();
                        Constraint constraint = fillFromAttributeList(context, Xml.asAttributeSet(parser));
                        if (tagName.equalsIgnoreCase("Guideline")) {
                            constraint.mIsGuideline = true;
                        }
                        this.mConstraints.put(Integer.valueOf(constraint.mViewId), constraint);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static int lookupID(TypedArray a, int index, int def) {
        int ret = a.getResourceId(index, def);
        if (ret == -1) {
            return a.getInt(index, -1);
        }
        return ret;
    }

    private Constraint fillFromAttributeList(Context context, AttributeSet attrs) {
        Constraint c = new Constraint();
        TypedArray a = context.obtainStyledAttributes(attrs, C0007R.styleable.ConstraintSet);
        populateConstraint(c, a);
        a.recycle();
        return c;
    }

    private void populateConstraint(Constraint c, TypedArray a) {
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (mapToConstant.get(attr)) {
                case 1:
                    c.baselineToBaseline = lookupID(a, attr, c.baselineToBaseline);
                    break;
                case 2:
                    c.bottomMargin = a.getDimensionPixelSize(attr, c.bottomMargin);
                    break;
                case 3:
                    c.bottomToBottom = lookupID(a, attr, c.bottomToBottom);
                    break;
                case 4:
                    c.bottomToTop = lookupID(a, attr, c.bottomToTop);
                    break;
                case 5:
                    c.dimensionRatio = a.getString(attr);
                    break;
                case 6:
                    c.editorAbsoluteX = a.getDimensionPixelOffset(attr, c.editorAbsoluteX);
                    break;
                case 7:
                    c.editorAbsoluteY = a.getDimensionPixelOffset(attr, c.editorAbsoluteY);
                    break;
                case 8:
                    c.endMargin = a.getDimensionPixelSize(attr, c.endMargin);
                    break;
                case 9:
                    c.endToEnd = lookupID(a, attr, c.endToEnd);
                    break;
                case 10:
                    c.endToStart = lookupID(a, attr, c.endToStart);
                    break;
                case 11:
                    c.goneBottomMargin = a.getDimensionPixelSize(attr, c.goneBottomMargin);
                    break;
                case 12:
                    c.goneEndMargin = a.getDimensionPixelSize(attr, c.goneEndMargin);
                    break;
                case 13:
                    c.goneLeftMargin = a.getDimensionPixelSize(attr, c.goneLeftMargin);
                    break;
                case 14:
                    c.goneRightMargin = a.getDimensionPixelSize(attr, c.goneRightMargin);
                    break;
                case 15:
                    c.goneStartMargin = a.getDimensionPixelSize(attr, c.goneStartMargin);
                    break;
                case 16:
                    c.goneTopMargin = a.getDimensionPixelSize(attr, c.goneTopMargin);
                    break;
                case 17:
                    c.guideBegin = a.getDimensionPixelOffset(attr, c.guideBegin);
                    break;
                case 18:
                    c.guideEnd = a.getDimensionPixelOffset(attr, c.guideEnd);
                    break;
                case 19:
                    c.guidePercent = a.getFloat(attr, c.guidePercent);
                    break;
                case 20:
                    c.horizontalBias = a.getFloat(attr, c.horizontalBias);
                    break;
                case 21:
                    c.mHeight = a.getLayoutDimension(attr, c.mHeight);
                    break;
                case 22:
                    c.visibility = a.getInt(attr, c.visibility);
                    c.visibility = VISIBILITY_FLAGS[c.visibility];
                    break;
                case 23:
                    c.mWidth = a.getLayoutDimension(attr, c.mWidth);
                    break;
                case 24:
                    c.leftMargin = a.getDimensionPixelSize(attr, c.leftMargin);
                    break;
                case 25:
                    c.leftToLeft = lookupID(a, attr, c.leftToLeft);
                    break;
                case 26:
                    c.leftToRight = lookupID(a, attr, c.leftToRight);
                    break;
                case 27:
                    c.orientation = a.getInt(attr, c.orientation);
                    break;
                case 28:
                    c.rightMargin = a.getDimensionPixelSize(attr, c.rightMargin);
                    break;
                case 29:
                    c.rightToLeft = lookupID(a, attr, c.rightToLeft);
                    break;
                case 30:
                    c.rightToRight = lookupID(a, attr, c.rightToRight);
                    break;
                case 31:
                    c.startMargin = a.getDimensionPixelSize(attr, c.startMargin);
                    break;
                case 32:
                    c.startToEnd = lookupID(a, attr, c.startToEnd);
                    break;
                case 33:
                    c.startToStart = lookupID(a, attr, c.startToStart);
                    break;
                case 34:
                    c.topMargin = a.getDimensionPixelSize(attr, c.topMargin);
                    break;
                case 35:
                    c.topToBottom = lookupID(a, attr, c.topToBottom);
                    break;
                case 36:
                    c.topToTop = lookupID(a, attr, c.topToTop);
                    break;
                case 37:
                    c.verticalBias = a.getFloat(attr, c.verticalBias);
                    break;
                case 38:
                    c.mViewId = a.getResourceId(attr, c.mViewId);
                    break;
                case 39:
                    c.horizontalWeight = a.getFloat(attr, c.horizontalWeight);
                    break;
                case 40:
                    c.verticalWeight = a.getFloat(attr, c.verticalWeight);
                    break;
                case 41:
                    c.horizontalChainStyle = a.getInt(attr, c.horizontalChainStyle);
                    break;
                case 42:
                    c.verticalChainStyle = a.getInt(attr, c.verticalChainStyle);
                    break;
                case 43:
                    c.alpha = a.getFloat(attr, c.alpha);
                    break;
                case 44:
                    c.applyElevation = true;
                    c.elevation = a.getDimension(attr, c.elevation);
                    break;
                case 45:
                    c.rotationX = a.getFloat(attr, c.rotationX);
                    break;
                case 46:
                    c.rotationY = a.getFloat(attr, c.rotationY);
                    break;
                case 47:
                    c.scaleX = a.getFloat(attr, c.scaleX);
                    break;
                case 48:
                    c.scaleY = a.getFloat(attr, c.scaleY);
                    break;
                case 49:
                    c.transformPivotX = a.getFloat(attr, c.transformPivotX);
                    break;
                case 50:
                    c.transformPivotY = a.getFloat(attr, c.transformPivotY);
                    break;
                case 51:
                    c.translationX = a.getDimension(attr, c.translationX);
                    break;
                case 52:
                    c.translationY = a.getDimension(attr, c.translationY);
                    break;
                case 53:
                    c.translationZ = a.getDimension(attr, c.translationZ);
                    break;
                case 60:
                    c.rotation = a.getFloat(attr, c.rotation);
                    break;
                case 61:
                    c.circleConstraint = lookupID(a, attr, c.circleConstraint);
                    break;
                case 62:
                    c.circleRadius = a.getDimensionPixelSize(attr, c.circleRadius);
                    break;
                case 63:
                    c.circleAngle = a.getFloat(attr, c.circleAngle);
                    break;
                case 64:
                    Log.w("ConstraintSet", "unused attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                    break;
                default:
                    Log.w("ConstraintSet", "Unknown attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                    break;
            }
        }
    }
}
