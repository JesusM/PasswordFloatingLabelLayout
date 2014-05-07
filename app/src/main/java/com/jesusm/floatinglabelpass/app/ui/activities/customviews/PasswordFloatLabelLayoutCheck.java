package com.jesusm.floatinglabelpass.app.ui.activities.customviews;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import com.jesusm.floatinglabelpass.app.R;

/**
 * Created by Jesus on 03/05/14.
 */
public class PasswordFloatLabelLayoutCheck extends CheckStrenghtLabelLayout {

    private int passLength = 0;
    private boolean showLabelIcon = false;

    public PasswordFloatLabelLayoutCheck(Context context) {
        this(context, null);
    }

    public PasswordFloatLabelLayoutCheck(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordFloatLabelLayoutCheck(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    int getColorFromCriteria(String text) {
        passLength = getEditText().getText().toString().length();
        if (passLength < 3) {
            showLabelIcon = false;
            return getResources().getColor(R.color.float_label_password_bad);
        } else if (passLength < 6) {
            showLabelIcon = false;
            return getResources().getColor(R.color.float_label_password_regular);
        } else {
            showLabelIcon = true;
            return getResources().getColor(R.color.float_label_password_good);
        }


    }

    @Override
    public int checkInputType() {
        return InputType.TYPE_TEXT_VARIATION_PASSWORD;
    }

    @Override
    protected boolean showLabelIcon(String text) {
        return showLabelIcon;
    }


}
