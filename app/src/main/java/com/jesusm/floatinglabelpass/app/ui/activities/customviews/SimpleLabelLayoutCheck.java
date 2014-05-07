package com.jesusm.floatinglabelpass.app.ui.activities.customviews;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import com.jesusm.floatinglabelpass.app.R;

/**
 * Created by Jesus on 05/05/14.
 */
public class SimpleLabelLayoutCheck extends CheckStrenghtLabelLayout {

    private int color = getResources().getColor(R.color.float_label);

    public SimpleLabelLayoutCheck(Context context) {
        this(context, null);
    }

    public SimpleLabelLayoutCheck(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLabelLayoutCheck(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    int getColorFromCriteria(String text) {
        return color;
    }

    @Override
    int checkInputType() {
        return InputType.TYPE_CLASS_TEXT;
    }

    @Override
    protected boolean showLabelIcon(String text) {
        return false;
    }
}
