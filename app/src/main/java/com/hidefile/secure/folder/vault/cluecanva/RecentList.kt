package com.hidefile.secure.folder.vault.cluecanva

class RecentList(var image: String, var url: String, var title: String) {
    override fun toString(): String {
        return "AppDataModel{" +
                "image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}'
    }
}