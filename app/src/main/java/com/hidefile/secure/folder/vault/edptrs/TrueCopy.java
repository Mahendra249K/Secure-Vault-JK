package com.hidefile.secure.folder.vault.edptrs;

import android.os.Parcel;
import android.os.Parcelable;

public class TrueCopy implements Parcelable {
    public static final Parcelable.Creator<TrueCopy> CREATOR = new Parcelable.Creator<TrueCopy>() {
        @Override
        public TrueCopy createFromParcel(Parcel source) {
            return new TrueCopy(source);
        }
        @Override
        public TrueCopy[] newArray(int size) {
            return new TrueCopy[size];
        }
    };
    public long id;
    public String name;
    public String path;
    public boolean isSelected;
    public TrueCopy(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }
    private TrueCopy(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
    }
}
