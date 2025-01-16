package com.Joaquin.thiago;
import java.io.Serializable;
public class ListReltBs implements Serializable {
    private long totalSize;
    public ListReltBs() {}
    public ListReltBs(long totalSize) {
        this.totalSize = totalSize;
    }
    public long getTotalSize() {
        return totalSize;
    }
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }}
