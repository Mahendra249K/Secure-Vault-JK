package com.Xavier.TravisScott.euphoria;

public class LucyMoses {
    private String title;
    private int icon;
    private Blocker.OnClickListener onClickListener;
    public LucyMoses(String title, int icon, Blocker.OnClickListener onClickListener) {
        this.title = title;
        this.icon = icon;
        this.onClickListener = onClickListener;}
    public String getTitle() {
        return title;
    }
    public int getIcon() {
        return icon;
    }
    public Blocker.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
