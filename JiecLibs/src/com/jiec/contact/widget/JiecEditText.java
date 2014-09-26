
package com.jiec.contact.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class JiecEditText extends EditText {

    private String mNumber = "";

    public JiecEditText(Context context) {
        super(context);
    }

    public JiecEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public JiecEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getNumber() {
        if (getText().toString().contains("*")) {
            return mNumber;
        } else {
            return getText().toString();
        }
    }

    public void setNumber(String number) {
        mNumber = number;
    }

}
