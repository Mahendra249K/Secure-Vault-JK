package com.Joaquin.thiago;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
public class ListVedFl extends ListBsFld {
    private List<ListItVid> items = new ArrayList<>();
    public List<ListItVid> getItems() {
        return items;
    }
    public void setItems(List<ListItVid> items) {
        this.items = items;
    }
    public void addItem(ListItVid item){
        items.add(item);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListVedFl)) return false;
        ListVedFl directory = (ListVedFl) o;
        boolean hasId = !TextUtils.isEmpty(getId());
        boolean otherHasId = !TextUtils.isEmpty(directory.getId());
        if (hasId && otherHasId) {if (!TextUtils.equals(getId(), directory.getId())) {return false;}
            return TextUtils.equals(getName(), directory.getName());}
        return false;}
    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(getId())) {
            if (TextUtils.isEmpty(getName())) {
                return 0;}
            return getName().hashCode();}
        int result = getId().hashCode();
        if (TextUtils.isEmpty(getName())) {return result;}
        result = 31 * result + getName().hashCode();
        return result;
    }
}
