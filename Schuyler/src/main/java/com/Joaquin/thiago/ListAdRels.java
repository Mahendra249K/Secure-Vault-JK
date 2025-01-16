package com.Joaquin.thiago;

import java.util.List;
public class ListAdRels extends ListReltBs {
    private List<ListAudMd> items;
    public ListAdRels() {}
    public ListAdRels(List<ListAudMd> items) {
        this.items = items;
    }
    public ListAdRels(long totalSize, List<ListAudMd> items) {
        super(totalSize);
        this.items = items;}
    public List<ListAudMd> getItems() {
        return items;
    }
    public void setItems(List<ListAudMd> items) {
        this.items = items;
    }}
