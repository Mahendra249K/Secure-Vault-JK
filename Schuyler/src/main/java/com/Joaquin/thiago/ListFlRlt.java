package com.Joaquin.thiago;
import java.util.List;
public class ListFlRlt extends ListReltBs {
    private List<ListFlTerm> items;
    public ListFlRlt() {}
    public ListFlRlt(long totalSize, List<ListFlTerm> items) {
        super(totalSize);
        this.items = items;}
    public List<ListFlTerm> getItems() {
        return items;
    }
    public void setItems(List<ListFlTerm> items) {
        this.items = items;
    }}
