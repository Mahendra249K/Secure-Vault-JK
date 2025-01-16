package xiomaradrawn.illemire.saoirse;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public abstract class ListStoreFile extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    @NonNull
    private final AppCompatTextView mFileName;
    @NonNull
    private final AppCompatCheckBox mCheckBox;
    @Nullable
    private ListItemClick mListener;
    ListStoreFile(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mFileName = itemView.findViewById(R.id.filename);
        mCheckBox = itemView.findViewById(R.id.checkbox);}
    public void bind(@NonNull File file, boolean isMultiChoiceModeEnabled, boolean isSelected, @Nullable ListItemClick listener) {
        setOnListItemClickListener(listener);
        mFileName.setText(file.getName());
        mCheckBox.setVisibility(isMultiChoiceModeEnabled ? View.VISIBLE : View.GONE);
        mCheckBox.setChecked(isSelected);}
    void setOnListItemClickListener(@Nullable ListItemClick listener) {
        mListener = listener;}
    int getItemPosition() {
        return getAdapterPosition();
    }
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onListItemClick(getItemPosition());}}
    @Override
    public boolean onLongClick(View view) {
        if (mListener != null) {
            mListener.onListItemLongClick(getItemPosition());}
        return true;}
}
