package com.Joaquin.thiago;
import java.util.ArrayList;
import java.util.List;
public class ListFlCnfg {
    public static List<String> documentMIME = new ArrayList<String>(){
        {add("text/plain");
            add("application/pdf");
            add("application/msword");
            add("application/vnd.ms-excel");
            add("application/vnd.ms-powerpoint");
            add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");}};
    public static List<String> zipExtension = new ArrayList<String>(){
        {add(".zip");add(".rar");}};
    public static List<String> apkExtension = new ArrayList<String>(){
        {add(".apk");}};}
