package com.sharity.sharityUser.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.sharity.sharityUser.R;


/*
* Custom TextView for RobotoBold Text
*
* */

public class TextViewOpenSansSB extends TextView {
    private static final String TAG = "TextView";

    public TextViewOpenSansSB(Context context) {
        super(context);
    }

    public TextViewOpenSansSB(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewOpenSansSB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewOpenSansSemiBold);
        String customFont = a.getString(R.styleable.TextViewOpenSansSemiBold_OpenSansSB);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/"+asset);
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: "+e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}