package xiomaradrawn.illemire.saoirse;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListResPickStore implements Parcelable {
    @NonNull
    private String mPath = "";
    @NonNull
    private List<String> mNames = new ArrayList<>();
    private int mCount = 0;

    private ListResPickStore(Parcel in) {
        mPath = in.readString();
        mNames = in.createStringArrayList();
        mCount = in.readInt();
    }

    public ListResPickStore(@NonNull String path, @NonNull List<String> names) {
        mPath = path;
        mNames = names;
        mCount = names.size();
    }

    @NonNull
    public String getPath() {
        return mPath;
    }

    @NonNull
    public List<String> getNames() {
        return mNames;
    }

    public int getCount() {
        return mCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPath);
        parcel.writeStringList(mNames);
        parcel.writeInt(mCount);
    }

    public static final Creator<ListResPickStore> CREATOR = new Creator<ListResPickStore>() {
        @Override
        public ListResPickStore createFromParcel(Parcel in) {
            return new ListResPickStore(in);
        }

        @Override
        public ListResPickStore[] newArray(int size) {
            return new ListResPickStore[size];
        }
    };

    @Nullable
    public static ListResPickStore getFromIntent(@Nullable Intent intent) {
        if (intent != null) {
            return intent.getParcelableExtra(ListChoseA.EXTRA_RESULT);
        }
        return null;
    }
}
