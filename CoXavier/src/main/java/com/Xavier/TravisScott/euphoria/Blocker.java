package com.Xavier.TravisScott.euphoria;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Xavier.TravisScott.R;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;

public class Blocker implements Latinx {

    public static final int BUTTON_POSITIVE = 1;
    public static final int BUTTON_NEGATIVE = -1;
    public static final int NO_ICON = -111;
    public static final int NO_ANIMATION = -111;
    protected Dialog mDialog;
    protected Activity mActivity;
    protected String title;
    protected String message;
    protected boolean mCancelable;
    protected LucyMoses mPositiveButton;
    protected LucyMoses mNegativeButton;
    protected int mAnimationResId;
    protected String mAnimationFile;
    protected LottieAnimationView mAnimationView;

    protected CurAnxiety mCurAnxiety;
    protected Prison mPrison;
    protected Strobel mStrobel;


    protected Blocker(@NonNull Activity mActivity,
                      @NonNull String title,
                      @NonNull String message,
                      boolean mCancelable,
                      @NonNull LucyMoses mPositiveButton,
                      @NonNull LucyMoses mNegativeButton,
                      int mAnimationResId,
                      @NonNull String mAnimationFile) {
        this.mActivity = mActivity;
        this.title = title;
        this.message = message;
        this.mCancelable = mCancelable;
        this.mPositiveButton = mPositiveButton;
        this.mNegativeButton = mNegativeButton;
        this.mAnimationResId = mAnimationResId;
        this.mAnimationFile = mAnimationFile;
    }

    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        final View dialogView = inflater.inflate(R.layout.treach, container, false);
        TextView mTitleView = dialogView.findViewById(R.id.tv_sort_title);
        TextView mMessageView = dialogView.findViewById(R.id.textView_message);
        MaterialButton mPositiveButtonView = dialogView.findViewById(R.id.btn_positive);
        MaterialButton mNegativeButtonView = dialogView.findViewById(R.id.btn_negative);
        mAnimationView = dialogView.findViewById(R.id.animation_view);
        if (title != null) {
            mTitleView.setVisibility(View.VISIBLE);
            mTitleView.setText(title);
        } else {
            mTitleView.setVisibility(View.GONE);
        }
        if (message != null) {
            mMessageView.setVisibility(View.VISIBLE);
            mMessageView.setText(message);
        } else {
            mMessageView.setVisibility(View.GONE);
        }
        if (mPositiveButton != null) {
            mPositiveButtonView.setVisibility(View.VISIBLE);
            mPositiveButtonView.setText(mPositiveButton.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mPositiveButton.getIcon() != NO_ICON) {
                mPositiveButtonView.setIcon(mActivity.getDrawable(mPositiveButton.getIcon()));
            }

            mPositiveButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPositiveButton.getOnClickListener().onClick(Blocker.this, BUTTON_POSITIVE);
                }
            });
        } else {
            mPositiveButtonView.setVisibility(View.INVISIBLE);
        }
        if (mNegativeButton != null) {
            mNegativeButtonView.setVisibility(View.VISIBLE);
            mNegativeButtonView.setText(mNegativeButton.getTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mNegativeButton.getIcon() != NO_ICON) {
                mNegativeButtonView.setIcon(mActivity.getDrawable(mNegativeButton.getIcon()));
            }

            mNegativeButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNegativeButton.getOnClickListener().onClick(Blocker.this, BUTTON_NEGATIVE);
                }
            });
        } else {
            mNegativeButtonView.setVisibility(View.INVISIBLE);
        }
        if (mAnimationResId != NO_ANIMATION) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.setAnimation(mAnimationResId);
            mAnimationView.playAnimation();
        } else if (mAnimationFile != null) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.setAnimation(mAnimationFile);
            mAnimationView.playAnimation();

        } else {
            mAnimationView.setVisibility(View.GONE);
        }

        return dialogView;
    }
    public void show() {
        if (mDialog != null) {
            mDialog.show();
        } else {
            throwNullDialog();
        }
    }
    @Override
    public void cancel() {
        if (mDialog != null) {
            mDialog.cancel();
        } else {
            throwNullDialog();
        }
    }
    @Override
    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        } else {
            throwNullDialog();
        }
    }
    public void setOnShowListener(@NonNull final Strobel strobel) {
        this.mStrobel = strobel;

        mDialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface dialogInterface) {
                showCallback();
            }
        });
    }
    public void setOnCancelListener(@NonNull final Prison prison) {
        this.mPrison = prison;

        mDialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(android.content.DialogInterface dialogInterface) {
                cancelCallback();
            }
        });
    }
    public void setOnDismissListener(@NonNull final CurAnxiety curAnxiety) {
        this.mCurAnxiety = curAnxiety;

        mDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(android.content.DialogInterface dialogInterface) {
                dismissCallback();
            }
        });
    }
    public LottieAnimationView getAnimationView() {
        return mAnimationView;
    }

    private void showCallback() {
        if (mStrobel != null) {
            mStrobel.onShow(this);
        }
    }

    private void dismissCallback() {
        if (mCurAnxiety != null) {
            mCurAnxiety.onDismiss(this);
        }
    }

    private void cancelCallback() {
        if (mPrison != null) {
            mPrison.onCancel(this);
        }
    }

    private void throwNullDialog() {
        throw new NullPointerException("Called method on null Dialog. Create dialog using `Builder` before calling on Dialog");
    }

    public interface OnClickListener {
        void onClick(Latinx latinx, int which);
    }
}
