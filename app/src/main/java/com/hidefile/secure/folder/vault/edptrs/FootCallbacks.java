package com.hidefile.secure.folder.vault.edptrs;


import androidx.annotation.NonNull;

import java.io.File;

interface FootCallbacks {

    void onImageCapture(@NonNull File imageFile);

    void onCameraError(@FootError.CameraErrorCodes int errorCode);
}
