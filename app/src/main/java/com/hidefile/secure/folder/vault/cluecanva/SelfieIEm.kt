package com.hidefile.secure.folder.vault.cluecanva

import java.io.Serializable

class SelfieIEm : Serializable {
    var isChecked: Boolean
    var hiddenSelfieName: String? = null
    var hiddenSelfiePath: String

    constructor(isChecked: Boolean, hiddenSelfiePath: String) {
        this.isChecked = isChecked
        this.hiddenSelfiePath = hiddenSelfiePath
    }

    constructor(isChecked: Boolean, hiddenSelfiePath: String, hiddenSelfieName: String?) {
        this.isChecked = isChecked
        this.hiddenSelfiePath = hiddenSelfiePath
        this.hiddenSelfieName = hiddenSelfieName
    }

    override fun toString(): String {
        return "HiddenSelfieItem{checked=$isChecked, path='$hiddenSelfiePath'}"
    }
}