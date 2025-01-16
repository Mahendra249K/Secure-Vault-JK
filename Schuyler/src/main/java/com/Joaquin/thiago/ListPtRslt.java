package com.Joaquin.thiago;
import java.util.List;
public class ListPtRslt extends ListReltBs {
    private List<ListPtFld> folders;
    private List<ListIdPic> items;
    public ListPtRslt() {}
    public ListPtRslt(List<ListPtFld> folders, List<ListIdPic> items) {
        this.folders = folders;
        this.items = items;}
    public ListPtRslt(List<ListPtFld> folders, List<ListIdPic> items, long totalSize) {
        super(totalSize);
        this.folders = folders;
        this.items = items;}
    public List<ListPtFld> getFolders() {
        return folders;
    }
    public void setFolders(List<ListPtFld> folders) {
        this.folders = folders;
    }
    public List<ListIdPic> getItems() {
        return items;
    }
    public void setItems(List<ListIdPic> items) {
        this.items = items;
    }}
