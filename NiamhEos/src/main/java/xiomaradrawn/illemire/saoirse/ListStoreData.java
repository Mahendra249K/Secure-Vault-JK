package xiomaradrawn.illemire.saoirse;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class ListStoreData extends ListStoreFile {
    @NonNull
    private final Context mContext;
    @Nullable
    private final AppCompatTextView mFileSize;
    @NonNull
    private final AppCompatImageView mThumbnail;
    public ListStoreData(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mFileSize = itemView.findViewById(R.id.filesize);
        mThumbnail = itemView.findViewById(R.id.thumbnail);}
    @Override
    public void bind(@NonNull File file, boolean isMultiChoiceModeEnabled, boolean isSelected, @Nullable ListItemClick listener) {
        super.bind(file, isMultiChoiceModeEnabled, isSelected, listener);
        if (mFileSize != null) {
            mFileSize.setVisibility(View.VISIBLE);
            mFileSize.setText(ListCommand.getHumanReadableFileSize(mContext, file.length()));}
        String extention2 = ListCommand.getFileExtension(file.getName());
        Glide.with(mContext)
                .load(file.getAbsolutePath())
                .apply(new RequestOptions().placeholder(getFileIcon(extention2)).error(getFileIcon(extention2)))
                .into(mThumbnail);}

    public int getFileIcon(String extention) {
        switch (extention) {
            case "apkbin":
            case "apk":
                return R.drawable.vi_apk;
            case "pdf":
            case "pdfbin":
                return R.drawable.vi_pdf;
            case "doc":
            case "docx":
            case "docbin":
            case "docxbin":
                return R.drawable.vi_doc;
            case "ppt":
            case "pptbin":
            case "pptx":
            case "pptxbin":
                return R.drawable.vi_ppt;
            case "xls":
            case "xlsbin":
            case "xlsx":
            case "xlsxbin":
                return R.drawable.vi_xle;
            case "txt":
            case "txtbin":
            case "csv":
            case "csvbin":
            case "rtf":
            case "rtfbin":
            case "odt":
            case "odtbin":
                return R.drawable.vi_txt;
            case "png":
            case "pngbin":
                return R.drawable.vi_png;
            case "jpg":
            case "jpgbin":
            case "svg":
            case "svgbin":
            case "bmp":
            case "bmpbin":
                return R.drawable.vi_jpg;
            case "gif":
            case "gifbin":
                return R.drawable.vi_gif;
            case "mp4":
            case "mp4bin":
            case "3gp":
            case "3gpbin":
            case "3gpp":
            case "3gppbin":
            case "3gpp2":
            case "3gpp2bin":
            case "mpeg":
            case "mpegpng":
            case "mkv":
            case "mkvpng":
            case "mov":
            case "movpng":
                return R.drawable.vi_video;
            case "aac":
            case "aacbin":
            case "flac":
            case "flacbin":
            case "m4a":
            case "m4abin":
            case "mp3":
            case "mp3bin":
            case "oga":
            case "ogabin":
            case "wav":
            case "wavbin":
            case "wma":
            case "wmabin":
                return R.drawable.vi_mp3;
            case "html":
            case "htmlbin":
            case "html5":
            case "html5bin":
            case "htm":
            case "htmbin":
            case "css":
            case "cssbin":
            case "asp":
            case "aspbin":
                return R.drawable.vi_html;
            case "zip":
            case "zipbin":
            case "rar":
            case "rarbin":
            case "rar4":
            case "rar4bin":
                return R.drawable.vi_zip;
            default:
                return R.drawable.vi_other;
        }
    }
}
