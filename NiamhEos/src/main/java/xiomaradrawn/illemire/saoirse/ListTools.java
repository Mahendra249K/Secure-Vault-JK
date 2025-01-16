package xiomaradrawn.illemire.saoirse;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ListTools extends Toolbar {
    private boolean mQuitButtonEnabled;
    private CharSequence mTitle;
    public ListTools(@NonNull Context context) {
        super(context);
    }
    public ListTools(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);}
    public ListTools(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);}
    public void setQuitButtonEnabled(boolean enabled) {
        mQuitButtonEnabled = enabled;
    }
    public void setMultiChoiceModeEnabled(boolean enabled) {
        getMenu().clear();
        if (enabled) {
            inflateMenu(R.menu.ml_chc);
            mTitle = getTitle();
            setTitle(null);
            setNavigationIcon(ListCommand.attrToResId(getContext(), R.attr.xtp_cancel));
        } else {
            inflateMenu(R.menu.ml_slc);
            if (!TextUtils.isEmpty(mTitle)) {
                setTitle(mTitle);}
            if (mQuitButtonEnabled) {
                setNavigationIcon(ListCommand.attrToResId(getContext(), R.attr.xtp_cancel));} else {
                setNavigationIcon(null);}
        }
    }
}
