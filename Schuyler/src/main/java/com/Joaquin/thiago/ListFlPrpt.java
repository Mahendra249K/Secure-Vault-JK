package com.Joaquin.thiago;
import android.provider.MediaStore;

import java.util.List;
public class ListFlPrpt {
    private List<String> extension;
    private List<String> mime;
    public ListFlPrpt(List<String> extension, List<String> mime) {
        this.extension = extension;
        this.mime = mime;}
    public List<String> getExtension() {
        return extension;
    }
    public void setExtension(List<String> extension) {
        this.extension = extension;
    }
    public List<String> getMime() {
        return mime;
    }
    public void setMime(List<String> mime) {
        this.mime = mime;
    }
    public String createSelection(){
        String selection = null;
        if(extension!=null){
            int size = extension.size();
            StringBuilder extensionBuilder = new StringBuilder();
            for(int i=0;i<size;i++){
                extensionBuilder.append("(").append(MediaStore.Files.FileColumns.DATA).append(" like ? ").append(")");if(i<size-1){extensionBuilder.append(" OR ");}}
            selection = extensionBuilder.toString();}else if(mime!=null){
            int size = mime.size();
            StringBuilder mimeBuilder = new StringBuilder();
            for(int i=0;i<size;i++){
                mimeBuilder.append("(").append(MediaStore.Files.FileColumns.MIME_TYPE).append(" == ? ").append(")");
                if(i<size-1){
                    mimeBuilder.append(" OR ");}}selection = mimeBuilder.toString();}
        return selection;}
    public String[] createSelectionArgs(){
        String[] args = null;
        StringBuilder sb = new StringBuilder();
        if(extension!=null){
            int size = extension.size();
            if(size > 0)
                args = new String[size];
            for(int i=0;i<size;i++){
                args[i] = "%" + extension.get(i);
                sb.append(args[i]);
                if(i < size-1){
                    sb.append(",");}}}else if(mime!=null){
            int size = mime.size();
            if(size > 0)
                args = new String[size];
            for(int i=0;i<size;i++){
                args[i] = mime.get(i);
                sb.append(args[i]);
                if(i < size-1){
                    sb.append(",");}}}
        return args;
    }
}
