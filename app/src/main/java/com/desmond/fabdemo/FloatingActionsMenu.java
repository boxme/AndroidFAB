package com.desmond.fabdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

/**
 * Created by desmond on 5/2/15.
 */
public class FloatingActionsMenu extends ViewGroup {

    public static final int EXPAND_UP = 0;
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_LEFT = 2;
    public static final int EXPAND_RIGHT = 3;

    public static final int LABELS_ON_LEFT_SIDE = 0;
    public static final int LABELS_ON_RIGHT_SIDE = 1;

    private static final int ANIMATION_DURATION = 300;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

    private int mAddButtonPlusColor;
    private int mAddButtonColorNormal;
    private int mAddButtonColorPressed;
    private int mAddButtonSize;
    private boolean mAddButtonStrokeVisible;
    private int mExpandDirection;

    private int mButtonSpacing;
    private int mLabelsMargin;
    private int mLabelsVerticalOffset;

    private boolean mExpanded;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AddFloatingActionButton mAddButton;
    private RotatingDrawable mRotatingDrawable;
    private int mMaxButtonWidth;
    private int mMaxButtonHeight;
    private int mLabelsStyle;
    private int mLabelsPosition;
    private int mButtonsCount;

    private OnFloatingActionsMenuUpdateListener mListener;

    public interface OnFloatingActionsMenuUpdateListener {
        void onMenuExpanded();
        void onMenuCollapsed();
    }

    public FloatingActionsMenu(Context context) {
        this(context, null);
    }

    public FloatingActionsMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionsMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,  attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mButtonSpacing =
                (int) (getResources().getDimension(R.dimen.fab_actions_spacing)
                        - getResources().getDimension(R.dimen.fab_shadow_radius)
                        - getResources().getDimension(R.dimen.fab_shadow_offset));

        mLabelsMargin = getResources().getDimensionPixelSize(R.dimen.fab_labels_margin);
        mLabelsVerticalOffset = getResources().getDimensionPixelSize(R.dimen.fab_shadow_offset);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionsMenu, 0, 0);
        mAddButtonPlusColor = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonPlusIconColor, getColor(android.R.color.white));
        mAddButtonColorNormal = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonColorNormal, getColor(android.R.color.holo_blue_dark));
        mAddButtonColorPressed = attr.getColor(R.styleable.FloatingActionsMenu_fab_addButtonColorPressed, getColor(android.R.color.holo_blue_light));
        mAddButtonSize = attr.getInt(R.styleable.FloatingActionsMenu_fab_addButtonSize, FloatingActionButton.SIZE_NORMAL);
        mAddButtonStrokeVisible = attr.getBoolean(R.styleable.FloatingActionsMenu_fab_addButtonStrokeVisible, true);
        mExpandDirection = attr.getInt(R.styleable.FloatingActionsMenu_fab_expandDirection, EXPAND_UP);
        mLabelsStyle = attr.getResourceId(R.styleable.FloatingActionsMenu_fab_labelStyle, 0);
        mLabelsPosition = attr.getInt(R.styleable.FloatingActionsMenu_fab_labelsPosition, LABELS_ON_LEFT_SIDE);
        attr.recycle();

        if (mLabelsStyle != 0 && expandsHorizontally()) {
            throw new IllegalStateException("Action labels in horizontal expand orientation is not supported.");
        }

        createAddButton(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void createAddButton(Context context) {
        mAddButton = new AddFloatingActionButton(context) {

            @Override
            void updateBackground() {
                mPlusColor = mAddButtonPlusColor;
                mColorNormal = mAddButtonColorNormal;
                mColorPressed = mAddButtonColorPressed;
                mStrokeVisible = mAddButtonStrokeVisible;
                super.updateBackground();
            }

            @Override
            Drawable getIconDrawable() {
                final RotatingDrawable rotatingDrawable  = new RotatingDrawable(super.getIconDrawable());
                mRotatingDrawable = rotatingDrawable;

                final OvershootInterpolator interpolator = new OvershootInterpolator();

                final ObjectAnimator collapseAnimator =
                        ObjectAnimator.ofFloat(rotatingDrawable, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
                final ObjectAnimator expandAnimator =
                        ObjectAnimator.ofFloat(rotatingDrawable, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);

                collapseAnimator.setInterpolator(interpolator);
                expandAnimator.setInterpolator(interpolator);

                mExpandAnimation.play(expandAnimator);
                mCollapseAnimation.play(collapseAnimator);

                return rotatingDrawable;
            }
        };

        mAddButton.setId(R.id.fab_expand_menu_button);
        mAddButton.setSize(mAddButtonSize);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        // super.generateDefaultLayoutParams()
        // returns a set of default layout parameters. These parameters are requested
        // when the View passed to {@link #addView(View)} has no layout parameters
        // already set.
        addView(mAddButton, super.generateDefaultLayoutParams());
    }

    public void toggle() {
        // TODO:
    }

    public void setOnFloatingActionsMenuUpdateListener(OnFloatingActionsMenuUpdateListener listener) {
        mListener = listener;
    }

    private boolean expandsHorizontally() {
        return mExpandDirection == EXPAND_LEFT || mExpandDirection == EXPAND_RIGHT;
    }

    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    private static class RotatingDrawable extends LayerDrawable {

        float mRotation;

        public RotatingDrawable(Drawable layer) {
            super(new Drawable[] {layer});
        }

        public float getRotation() {
            return mRotation;
        }

        public void setRotation(float rotation) {
            this.mRotation = rotation;

            // Use the current {@link Callback} implementation to have this Drawable
            // redrawn.  Does nothing if there is no Callback attached to the
            // Drawable.
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }
}
