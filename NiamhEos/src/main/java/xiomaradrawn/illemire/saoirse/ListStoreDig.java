package xiomaradrawn.illemire.saoirse;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.util.LinkedHashMap;

public class ListStoreDig implements DialogInterface.OnClickListener {
    private final AlertDialog.Builder mAlert;
    private OnStorageSelectedListener mOnStorageSelectedListener;
    private LinkedHashMap<String, String> mStorages;
    public ListStoreDig(Context context) {
        mAlert = new AlertDialog.Builder(context);
        mAlert.setTitle(R.string.efp__storage);
        mStorages = ListCommand.getExternalStoragePaths(context);
        mAlert.setItems(mStorages.values().toArray(new String[0]), this);}
    public void setOnStorageSelectedListener(OnStorageSelectedListener listener) {
        mOnStorageSelectedListener = listener;}
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        mOnStorageSelectedListener.onStorageSelected((String) mStorages.keySet().toArray()[which]);}
    public void show() {
        mAlert.show();
    }
    public interface OnStorageSelectedListener {
        void onStorageSelected(String path);}
}
