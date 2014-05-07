package com.jesusm.floatinglabelpass.app.ui.activities.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.jesusm.floatinglabelpass.app.R;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Jesus on 05/05/14.
 */
public abstract class CheckStrenghtLabelLayout extends FloatLabelLayout {

    private static final long LABEL_COLOR_CHANGE_ANIMATION_TIME = 333L;

    private BitmapDrawable acceptDrawable;
    private int lastColor = getResources().getColor(R.color.float_label);

    public CheckStrenghtLabelLayout(Context context) {
        this(context, null);
    }

    public CheckStrenghtLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckStrenghtLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initAcceptDrawableCompound();
    }

    abstract int getColorFromCriteria(String text);


    private void checkPassLabelColor() {
        int color = getColorFromCriteria(getEditText().getText().toString());
        setmLabelColor(color);
        boolean animateLabel = lastColor != color;
        if (animateLabel) {
            animateLabelColor();
            checkDrawAcceptIcon();
        } else {
            getLabel().setTextColor(getmLabelColor());
        }
        updateLabelText();
        lastColor = color;
    }

    abstract int checkInputType();


    private void checkDrawAcceptIcon() {
        boolean showCompoundDrawable = showLabelIcon(getEditText().getText().toString());
        getLabel().setCompoundDrawablesWithIntrinsicBounds(null, null,
                showCompoundDrawable ? acceptDrawable : null, null);
    }

    protected abstract boolean showLabelIcon(String text);


    private void initAcceptDrawableCompound() {
        // Read your drawable from somewhere
        Drawable dr = getResources().getDrawable(R.drawable.ic_action_navigation_accept);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to the label size
        int acceptDrawableSize = (int) getLabel().getTextSize();
        acceptDrawable = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, acceptDrawableSize,
                        acceptDrawableSize, true)
        );
    }

    @Override
    protected void setEditText(EditText editText) {
        super.setEditText(editText);
        int inputType = checkInputType();
        getEditText().setInputType(InputType.TYPE_CLASS_TEXT | inputType);

    }


    public void updateLabelColor() {
        checkPassLabelColor();
    }


    /**
     * Animate label text color
     */
    private void animateLabelColor() {

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), getLabel().getCurrentTextColor(), getmLabelColor());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                getLabel().setTextColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.setDuration(LABEL_COLOR_CHANGE_ANIMATION_TIME);
        colorAnimation.start();

    }

}
