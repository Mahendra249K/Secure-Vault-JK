package xiomaradrawn.illemire.saoirse;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.io.File;
import java.util.Locale;
class ListNameAscComprtn extends ListComprtn {
    @Override
    @IntRange(from = -1, to = 1)
    int compareProperty(@NonNull File file1, @NonNull File file2) {
        return file1.getName().toLowerCase(Locale.getDefault()).compareTo(file2.getName().toLowerCase(Locale.getDefault()));
    }
}
