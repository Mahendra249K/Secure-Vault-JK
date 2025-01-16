package xiomaradrawn.illemire.saoirse;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class ListSortDig implements DialogInterface.OnClickListener {
    private final AlertDialog.Builder mAlert;
    private OnSortingSelectedListener mOnSortingSelectedListener;
    public ListSortDig(Context context) {
        mAlert = new AlertDialog.Builder(context);
        mAlert.setItems(context.getResources().getStringArray(R.array.efp__sorting_types), this);}
    public void setOnSortingSelectedListener(OnSortingSelectedListener listener) {
        mOnSortingSelectedListener = listener;}
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        ListChoseA.SortingType sortingType = ListChoseA.SortingType.NAME_ASC;
        switch (which) {
            case 1:
                sortingType = ListChoseA.SortingType.NAME_DESC;
                break;
            case 2:
                sortingType = ListChoseA.SortingType.SIZE_ASC;
                break;
            case 3:
                sortingType = ListChoseA.SortingType.SIZE_DESC;
                break;
            case 4:
                sortingType = ListChoseA.SortingType.DATE_ASC;
                break;
            case 5:
                sortingType = ListChoseA.SortingType.DATE_DESC;
                break;
        }
        mOnSortingSelectedListener.onSortingSelected(sortingType);
    }
    public void show() {
        mAlert.show();
    }
    public interface OnSortingSelectedListener {
        void onSortingSelected(ListChoseA.SortingType sortingType);
    }
}
