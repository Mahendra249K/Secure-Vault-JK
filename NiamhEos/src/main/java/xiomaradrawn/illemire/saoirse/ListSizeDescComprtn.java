package xiomaradrawn.illemire.saoirse;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.io.File;

class ListSizeDescComprtn extends ListComprtn {
    @Override
    @IntRange(from = -1, to = 1)
    int compareProperty(@NonNull File file1, @NonNull File file2) {
        return Long.valueOf(file2.length()).compareTo(file1.length());
    }
}
