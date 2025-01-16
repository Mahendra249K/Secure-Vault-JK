package xiomaradrawn.illemire.saoirse;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import java.io.File;

public class ListStoreFileDir extends ListStoreFile {
    @Nullable
    private final AppCompatTextView mFileSize;
    @NonNull
    private final AppCompatImageView mThumbnail;
    public ListStoreFileDir(@NonNull View itemView) {
        super(itemView);
        mFileSize = itemView.findViewById(R.id.filesize);
        mThumbnail = itemView.findViewById(R.id.thumbnail);}
    @Override
    public void bind(@NonNull File file, boolean isMultiChoiceModeEnabled, boolean isSelected, @Nullable ListItemClick listener) {
        super.bind(file, isMultiChoiceModeEnabled, isSelected, listener);
        if (mFileSize != null) {
            mFileSize.setVisibility(View.GONE);}
        mThumbnail.setImageResource(R.drawable.vi_folder);
    }
}
