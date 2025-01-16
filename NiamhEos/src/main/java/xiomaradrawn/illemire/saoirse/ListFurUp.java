package xiomaradrawn.illemire.saoirse;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class ListFurUp extends ListStoreFile {
    @NonNull
    private final AppCompatTextView mFileName;
    @NonNull
    private final AppCompatCheckBox mCheckBox;
    @Nullable
    private final AppCompatTextView mFileSize;
    @NonNull
    private final AppCompatImageView mThumbnail;
    public ListFurUp(@NonNull View itemView) {
        super(itemView);
        mFileName = itemView.findViewById(R.id.filename);
        mCheckBox = itemView.findViewById(R.id.checkbox);
        mFileSize = itemView.findViewById(R.id.filesize);
        mThumbnail = itemView.findViewById(R.id.thumbnail);}
    public void bind(@Nullable ListItemClick listener) {
        setOnListItemClickListener(listener);
        mFileName.setText("..");
        mCheckBox.setVisibility(View.GONE);
        if (mFileSize != null) {
            mFileSize.setVisibility(View.GONE);}
        mThumbnail.setImageResource(R.drawable.vi_pu);}
    @Override
    int getItemPosition() {
        return ListItemClick.POSITION_UP;
    }
}
