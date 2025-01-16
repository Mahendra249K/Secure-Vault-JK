package com.Joaquin.thiago;

public enum ListFlTp {
    DOC(new ListFlPrpt(null, ListFlCnfg.documentMIME)),
    APK(new ListFlPrpt(ListFlCnfg.apkExtension,null)),
    ZIP(new ListFlPrpt(ListFlCnfg.zipExtension,null));
    ListFlPrpt property;
    ListFlTp(ListFlPrpt property){
        this.property = property;
    }
    public ListFlPrpt getProperty() {
        return property;
    }
    public void setProperty(ListFlPrpt property) {
        this.property = property;
    }}
