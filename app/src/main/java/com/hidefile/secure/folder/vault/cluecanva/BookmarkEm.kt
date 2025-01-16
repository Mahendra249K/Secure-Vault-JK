package com.hidefile.secure.folder.vault.cluecanva

class BookmarkEm {
    var content: String? = null
    var icon: String? = null
    var attachment: String? = null
    var id: String? = null
    var title: String? = null
    var creation: String? = null

    companion object {
        const val TABLE_BOOKMARKS_PASS = "BOOKMARKS_PASS"
        const val COLUMN_PASS_ID = "_id"
        const val COLUMN_PASS_TITLE = "TITLE"
        const val COLUMN_PASS_CONTENT = "CONTENT"
        const val COLUMN_PASS_ICON = "ICON"
        const val COLUMN_PASS_ATTACH = "ATTACHMENT"
        const val COLUMN_PASS_CREATION = "CREATION"
        const val CREATE_PASS_BOOKMARK = ("CREATE TABLE "
                + TABLE_BOOKMARKS_PASS
                + " ("
                + " " + COLUMN_PASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " " + COLUMN_PASS_TITLE + " TEXT,"
                + " " + COLUMN_PASS_CONTENT + " TEXT UNIQUE,"
                + " " + COLUMN_PASS_ICON + " TEXT,"
                + " " + COLUMN_PASS_ATTACH + " TEXT,"
                + " " + COLUMN_PASS_CREATION + " TEXT"
                + ")")
    }
}