package com.Joaquin.thiago;
import java.io.Serializable;
public class ListTermBs implements Serializable {
    public int id;
    public String displayName;
    public String path;
    public String newPath;
    public long size;
    public long modified;
    public String mimeType;
    public String couldId;
    public int isTrash;
    public ListTermBs() {}
    public String getNewPath() {
        return newPath;
    }
    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }
    public ListTermBs(int id, String displayName, String path, long size, long modified, String mimeType) {
        this.id = id;
        this.displayName = displayName;
        this.path = path;
        this.size = size;
        this.modified = modified;
        this.mimeType = mimeType;}
    public ListTermBs(int id, String displayName, String path) {
        this(id, displayName, path, 0);
    }
    public ListTermBs(int id, String displayName, String path, long size) {
        this(id, displayName, path, size, 0);}
    public ListTermBs(int id, String displayName, String path, long size, long modified) {}
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public long getModified() {
        return modified;
    }
    public void setModified(long modified) {
        this.modified = modified;
    }
    public String getCouldId() {
        return couldId;
    }
    public void setCouldId(String couldId) {
        this.couldId = couldId;
    }
    public int getIsTrash() {
        return isTrash;
    }
    public void setIsTrash(int isTrash) {
        this.isTrash = isTrash;
    }}
