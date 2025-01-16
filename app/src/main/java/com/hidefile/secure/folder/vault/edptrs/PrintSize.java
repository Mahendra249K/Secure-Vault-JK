package com.hidefile.secure.folder.vault.edptrs;

import android.hardware.Camera;

import java.util.Comparator;

class PrintSize implements Comparator<Camera.Size> {

    public int compare(Camera.Size a, Camera.Size b) {
        return (b.height * b.width) - (a.height * a.width);
    }
}
