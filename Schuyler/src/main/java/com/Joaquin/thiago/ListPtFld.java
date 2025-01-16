package com.Joaquin.thiago;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
public class ListPtFld extends ListBsFld {
    private String cover;
    private List<ListIdPic> items = new ArrayList<>();
    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public List<ListIdPic> getItems() {
        return items;
    }
    public void setItems(List<ListIdPic> items) {
        this.items = items;
    }
    public void addItem(ListIdPic item){
        items.add(item);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListPtFld)) return false;
        ListPtFld directory = (ListPtFld) o;
        boolean hasId = !TextUtils.isEmpty(getId());
        boolean otherHasId = !TextUtils.isEmpty(directory.getId());
        if (hasId && otherHasId) {
            if (!TextUtils.equals(getId(), directory.getId())) {
                return false;}
            return TextUtils.equals(getName(), directory.getName());}
        return false;}
    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(getId())) {
            if (TextUtils.isEmpty(getName())) {return 0;}return getName().hashCode();}
        int result = getId().hashCode();
        if (TextUtils.isEmpty(getName())) {return result;}
        result = 31 * result + getName().hashCode();
        return result;
    }
}
