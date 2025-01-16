package com.hidefile.secure.folder.vault.cluecanva;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.util.AttributeSet;

import com.hidefile.secure.folder.vault.R;

public class VTv extends androidx.appcompat.widget.AppCompatTextView {
    float mStroke = 0.0f;
    ColorStateList oriColor;
    int outlineColor = Color.WHITE;
    private Context mContext;

    public VTv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public VTv(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public VTv(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_fontName);
            if (fontName != null && !fontName.trim().isEmpty()) {
                Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);}
            mStroke = a.getFloat(R.styleable.CustomTextView_stroke, 0.0f);
            a.recycle();
            oriColor = getTextColors();
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        oriColor = getTextColors();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        oriColor = getTextColors();
    }

    private void setPaintToOutline() {
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStroke);
        super.setTextColor(outlineColor);
    }

    private void setPaintToRegular() {
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        super.setTextColor(oriColor);
    }
    public void setSpannableText(String textOne, String textTwo) {
        //todo
//        Spannable spanText = EntryAux.getSpannableForTwoStrings(mContext, textOne, textTwo, R.font.terresa, R.font.terresa);
//        setText(spanText);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (mStroke > 0) {
            setPaintToOutline();
            super.onDraw(canvas);
            setPaintToRegular();
        }
        super.onDraw(canvas);
    }
}
