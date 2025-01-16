package xiomaradrawn.illemire.saoirse;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import xiomaradrawn.illemire.saoirse.R;

public class ListDig implements DialogInterface.OnClickListener {
    @Nullable
    private OnNewFolderNameEnteredListener mListener;
    private AlertDialog.Builder mAlert;

    public ListDig(@NonNull Context context) {
        mAlert = new AlertDialog.Builder(context);
        mAlert.setTitle(R.string.efp__new_folder);
        mAlert.setView(LayoutInflater.from(context).inflate(R.layout.ex_newf, null));
        mAlert.setPositiveButton(android.R.string.ok, this);
        mAlert.setNegativeButton(android.R.string.cancel, null);
    }
    public void setOnNewFolderNameEnteredListener(OnNewFolderNameEnteredListener listener) {
        mListener = listener;
    }
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        TextView name = ((AlertDialog) dialogInterface).findViewById(R.id.name);
        if (mListener != null && name != null) {
            mListener.onNewFolderNameEntered(name.getText().toString());
        }
    }
    public void show() {
        mAlert.show();
    }
    public interface OnNewFolderNameEnteredListener {
        void onNewFolderNameEntered(@NonNull String name);
    }
}
