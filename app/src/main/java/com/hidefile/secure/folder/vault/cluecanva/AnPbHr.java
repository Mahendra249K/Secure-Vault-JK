package com.hidefile.secure.folder.vault.cluecanva;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import androidx.core.content.res.ResourcesCompat;

import com.hidefile.secure.folder.vault.R;


public class AnPbHr extends ProgressBar {

    private int mBackgroundColor = Color.GRAY;
    private int mProgressColor = Color.BLUE;
    private boolean mIsRounded = true;

    public AnPbHr(Context context) {
        super(context);
        init();
    }

    public AnPbHr(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnPbHr(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init() {
        Drawable drawable;
        if (mIsRounded) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_progress_bar_horizontal, null);
        } else {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.progress_bar_horizontal, null);
        }
        setProgressDrawable(drawable);
        setProgressColors(mBackgroundColor, mProgressColor);
    }

    private void init(Context context, AttributeSet attrs) {
        setUpStyleable(context, attrs);
        LayerDrawable layerDrawable;
        if (mIsRounded) {
            layerDrawable = (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_progress_bar_horizontal, null);
        } else {
            layerDrawable = (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.progress_bar_horizontal, null);
        }
        setProgressDrawable(layerDrawable);

        setProgressColors(mBackgroundColor, mProgressColor);
    }

    private void setUpStyleable(Context context, AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedHorizontalProgress);

        mBackgroundColor = typedArray.getColor(R.styleable.RoundedHorizontalProgress_backgroundColor1, Color.GRAY);
        mProgressColor = typedArray.getColor(R.styleable.RoundedHorizontalProgress_progressColor1, Color.BLUE);
        mIsRounded = typedArray.getBoolean(R.styleable.RoundedHorizontalProgress_isRounded, true);

        typedArray.recycle();
    }

    public void setProgressColors(int backgroundColor, int progressColor) {
        LayerDrawable layerDrawable = (LayerDrawable) getProgressDrawable();
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(android.R.id.background);
        gradientDrawable.setColor(backgroundColor);

        if (mIsRounded) {
            ScaleDrawable scaleDrawable = (ScaleDrawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);
            GradientDrawable progressGradientDrawable = (GradientDrawable) scaleDrawable.getDrawable();
            progressGradientDrawable.setColor(progressColor);
            setProgressDrawable(layerDrawable);
        } else {
            ClipDrawable progressDrawable = (ClipDrawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);
            progressDrawable.setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
            setProgressDrawable(layerDrawable);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void animateProgress(int animationDuration, int from, int to) {
        AnPb anPb = new AnPb(this, from, to);
        anPb.setDuration(animationDuration);
        startAnimation(anPb);
    }

    public void animateProgress(int animationDuration, int to) {
        AnPb anPb = new AnPb(this, getProgress(), to);
        anPb.setDuration(animationDuration);
        startAnimation(anPb);
    }
}
