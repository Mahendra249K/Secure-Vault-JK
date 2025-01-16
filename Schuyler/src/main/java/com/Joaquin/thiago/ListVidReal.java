package com.Joaquin.thiago;
import java.util.List;
public class ListVidReal extends ListReltBs {
    private List<ListVedFl> folders;
    private List<ListItVid> items;
    public ListVidReal() {}
    public ListVidReal(List<ListVedFl> folders, List<ListItVid> items) {
        this.folders = folders;
        this.items = items;}
    public ListVidReal(List<ListVedFl> folders, List<ListItVid> items, long totalSize) {
        super(totalSize);
        this.folders = folders;
        this.items = items;}
    public List<ListVedFl> getFolders() {
        return folders;
    }
    public void setFolders(List<ListVedFl> folders) {
        this.folders = folders;
    }
    public List<ListItVid> getItems() {
        return items;
    }
    public void setItems(List<ListItVid> items) {
        this.items = items;
    }}
