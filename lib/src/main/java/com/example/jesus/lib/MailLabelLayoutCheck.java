package com.example.jesus.lib;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;

public class MailLabelLayoutCheck extends CheckStrenghtLabelLayout {
    private boolean valid = false;


    public MailLabelLayoutCheck(Context context) {
        this(context, null);
    }

    public MailLabelLayoutCheck(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MailLabelLayoutCheck(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    int getColorFromCriteria(String target) {
        valid = isValidEmail(target);
        return valid ? getResources().getColor(R.color.float_label_password_good) :
                getResources().getColor(R.color.float_label_password_error);
    }

    @Override
    public int checkInputType() {
        return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected boolean showLabelIcon(String text) {
        return valid;
    }
}
