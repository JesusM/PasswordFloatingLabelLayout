package jesusm.floatlabellayout.lib;/*
 * Copyright (C) 2014 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jesus.lib.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Layout which an {@link android.widget.EditText} to show a floating label when the hint is hidden
 * due to the user inputting text.
 *
 * @see <a href="https://dribbble.com/shots/1254439--GIF-Mobile-Form-Interaction">Matt D. Smith on Dribble</a>
 * @see <a href="http://bradfrostweb.com/blog/post/float-label-pattern/">Brad Frost's blog post</a>
 */
public class FloatLabelLayout extends LinearLayout {

    private static final int DEFAULT_PADDING_TOP_BOTTOM_DP = 8;
    private static final long ANIMATION_DURATION = 100;
    private int mLabelColor = Color.GRAY;

    private EditText mEditText;
    private TextView mLabel;

    public FloatLabelLayout(Context context) {
        this(context, null);
    }

    public FloatLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);

        final TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.FloatLabelLayout);

        mLabel = new TextView(context);
        initLabelAppearance(context, a);
        mLabelColor = mLabel.getCurrentTextColor();
        addView(mLabel, 0, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        a.recycle();
    }

    private void initLabelAppearance(Context context, TypedArray a) {
        mLabel.setVisibility(INVISIBLE);
        mLabel.setPadding(0, dipsToPix(DEFAULT_PADDING_TOP_BOTTOM_DP), 0, 0);
        mLabel.setTextAppearance(context,
                a.getResourceId(R.styleable.FloatLabelLayout_floatLabelTextAppearance,
                        R.style.PasswordFloatingLabelLayout_DefaultStyle)
        );
    }


    @Override
    public final void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            // If we already have an EditText, throw an exception
            if (mEditText != null) {
                throw new IllegalArgumentException("We already have an EditText, can only have one");
            }
            child.setPadding(child.getPaddingLeft(), dipsToPix(DEFAULT_PADDING_TOP_BOTTOM_DP),
                    child.getPaddingRight(), dipsToPix(DEFAULT_PADDING_TOP_BOTTOM_DP));
            if (mLabel != null) {
                mLabel.setPadding(child.getPaddingLeft(), mLabel.getPaddingTop(),
                        child.getPaddingRight(), mLabel.getPaddingBottom());
            }
            setEditText((EditText) child);
        }

        // Carry on adding the View...
        super.addView(child, index, params);
    }

    protected void setEditText(EditText editText) {
        mEditText = editText;

        // Add focus listener to the EditText so that we can notify the label that it is activated.
        // Allows the use of a ColorStateList for the text color on the label
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                mLabel.setEnabled(focused);
            }
        });

        initTextWatcher();
        updateLabelText();

    }

    protected void updateLabelText() {
        mLabel.setText(mEditText.getHint());
    }

    private void initTextWatcher() {
        getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAfterTextChanged(editable);

            }
        });
    }


    protected void checkAfterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            // The text is empty, so hide the label if it is visible
            if (mLabel.getVisibility() == View.VISIBLE) {
                hideLabel();
            }
        } else {
            actionTextNotEmpty();
        }
    }

    protected void actionTextNotEmpty() {
        // The text is not empty, so show the label if it is not visible
        if (mLabel.getVisibility() != View.VISIBLE) {
            showLabel();
        }
        updateLabelColor();

    }


    /**
     * Need to be overriden to update the label text color
     */
    public void updateLabelColor() {
        getLabel().setTextColor(getmLabelColor());
    }

    /**
     * @return the {@link android.widget.EditText} text input
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * @return the {@link android.widget.TextView} label
     */
    public TextView getLabel() {
        return mLabel;
    }

    /**
     * Show the label using an animation
     */
    private void showLabel() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mLabel, "alpha", 0f, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLabel, "translationY", mLabel.getHeight() / 2, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(alpha, translationY);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }

    /**
     * Hide the label using an animation
     */
    private void hideLabel() {
        ViewHelper.setAlpha(mLabel, 1f);
        ViewHelper.setTranslationY(mLabel, 0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mLabel, "alpha", 0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mLabel, "translationY", mLabel.getHeight() / 2);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                mLabel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(alpha, translationY);
        animatorSet.start();

    }

    /**
     * Helper method to convert dips to pixels.
     */
    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps,
                getResources().getDisplayMetrics());
    }


    public int getmLabelColor() {
        return mLabelColor;
    }

    public void setmLabelColor(int color) {
        this.mLabelColor = color;
    }
}