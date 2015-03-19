package jesusm.floatlabellayout.lib;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import com.example.jesus.lib.R;

public class PasswordFloatLabelLayoutCheck extends CheckStrenghtLabelLayout {

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
        int passLength = getEditText().getText().toString().length();
        if (passLength < 3) {
            showLabelIcon = false;
            return getResources().getColor(R.color.float_label_password_error);
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
