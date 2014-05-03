package com.jesusm.floatinglabelpass.app.ui.activities.customviews;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.jesusm.floatinglabelpass.app.R;

/**
 * Created by Jesus on 03/05/14.
 */
public class PasswordFloatLabelLayout extends FloatLabelLayout {


    private static final int PASSWORD_STRONG_GOOD = 1;
    private static final int PASSWORD_STRONG_REGULAR = 2;
    private static final int PASSWORD_STRONG_BAD = 3;
    private static final long LABEL_COLOR_CHANGE_ANIMATION_TIME = 333L;
    private BitmapDrawable acceptDrawable;
    private int passwordStrongState = PASSWORD_STRONG_BAD;
    private boolean isPassword = false;
    private int passLength = 0;
    private boolean showStateIcon = false;

    public PasswordFloatLabelLayout(Context context) {
        this(context, null);
    }

    public PasswordFloatLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordFloatLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        final TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.PasswordFloatLabelLayout);

        showStateIcon = a.getBoolean(R.styleable.PasswordFloatLabelLayout_showStateIcon, false);
        if (showStateIcon){
            initAcceptDrawableCompound();
        }
    }

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
        checkIfIsPass();
    }

    private void checkIfIsPass() {
        int type = getEditText().getInputType();
        isPassword = ((type & InputType.TYPE_TEXT_VARIATION_PASSWORD)) > 0;
    }

    @Override
    protected void actionTextNotEmpty() {
        super.actionTextNotEmpty();
        if (isPassword)
            checkPassword();
    }

    private void checkPassword() {
        checkPassStrong();
    }

    private void checkPassStrong() {
        checkPasswordStrongState();

    }

    /**
     * In this case, only check the length of the password. You should add another methods to check
     * the strong of the pass
     */
    protected void checkPasswordStrongState() {
        passLength = getEditText().getText().toString().length();
        int oldPasswordStrongState = passwordStrongState;
        if (passLength < 3) {
            passwordStrongState = PASSWORD_STRONG_BAD;
        } else if (passLength < 6) {
            passwordStrongState = PASSWORD_STRONG_REGULAR;
        } else {
            passwordStrongState = PASSWORD_STRONG_GOOD;
        }

        boolean animateLabel = oldPasswordStrongState != passwordStrongState;
        checkPassLabelColor(animateLabel);

    }

    /**
     * Check the new textColor for mLabel
     *
     * @param stateChanged indicate if the password strong state has changed
     */
    private void checkPassLabelColor(boolean stateChanged) {
        switch (passwordStrongState) {

            case PASSWORD_STRONG_REGULAR:
                setmLabelColor(getResources().getColor(R.color.float_label_password_regular));

                break;
            case PASSWORD_STRONG_GOOD:
                setmLabelColor(getResources().getColor(R.color.float_label_password_good));
                break;
            case PASSWORD_STRONG_BAD:
            default:
                setmLabelColor(getResources().getColor(R.color.float_label_password_bad));
                break;
        }
        if (stateChanged) {
            animateLabelColor();
            checkDrawAcceptIcon();
        } else {
            getLabel().setTextColor(getmLabelColor());
        }
    }

    private void checkDrawAcceptIcon() {
        getLabel().setCompoundDrawablesWithIntrinsicBounds(null, null,
                passwordStrongState == PASSWORD_STRONG_GOOD ? acceptDrawable : null, null);
    }


    /**
     * Animate label text color
     */
    private void animateLabelColor() {
        final ObjectAnimator animator = ObjectAnimator.ofInt(getLabel(), textColorProperty, getmLabelColor());
        animator.setDuration(LABEL_COLOR_CHANGE_ANIMATION_TIME);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.start();
    }


    /**
     * Based on a Cyril Mottier post: https://plus.google.com/+CyrilMottier/posts/X4yoNHHszwq,
     * custom property to animate text color change
     */
    final Property<TextView, Integer> textColorProperty = new Property<TextView, Integer>(int.class, "textColor") {
        @Override
        public Integer get(TextView object) {
            return object.getCurrentTextColor();
        }

        @Override
        public void set(TextView object, Integer value) {
            object.setTextColor(value);
        }
    };
}
