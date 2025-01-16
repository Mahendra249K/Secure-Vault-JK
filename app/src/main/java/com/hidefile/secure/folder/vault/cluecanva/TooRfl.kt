package com.hidefile.secure.folder.vault.cluecanva

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.Joaquin.thiago.ListIdPic
import androidx.documentfile.provider.DocumentFile
import com.hidefile.secure.folder.vault.edptrs.AddNew
import com.google.api.services.drive.Drive
import com.hidefile.secure.folder.vault.AdActivity.MyApplication
import com.hidefile.secure.folder.vault.R
import java.io.*
import java.net.URLConnection
import java.util.ArrayList
import java.util.HashMap

object TooRfl {
    private val TAG = TooRfl::class.java.simpleName
    @JvmStatic
    fun deleteTempFolder() {
        val file = File(MyApplication.TEMP_COPY_FOLDER)
        if (file.exists()) {
            val files = file.listFiles()
            if (files != null && files.size > 0) {
                for (tmp in files) {
                    tmp.delete()
                }
            }
            val isDeleted = file.delete()
        }
    }

    @JvmStatic
    fun isContentUri(newPath: String): Boolean {
        return newPath.startsWith("content://")
    }

    fun addImageToGallery(context: Context, filePath: String, mimetype: String?, from: Int) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimetype)
        values.put(MediaStore.MediaColumns.DATA, filePath)
        val currUri: Uri
        currUri = when (from) {
            1 -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            2 -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }
        context.contentResolver.insert(currUri, values)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath),
            null
        ) { path: String?, uri: Uri? -> }
    }

    @JvmStatic
    fun shareImage(mContext: Context, newPath: String, displayName: String) {
        val file = copyTemporaryToFolder(mContext, newPath, displayName) ?: return
        val intent = Intent(Intent.ACTION_SEND)
        val photoURI =
            FileProvider.getUriForFile(mContext, mContext.packageName + ".provider", file)
        intent.type = TillsFl.getMimeType(
            mContext,
            Uri.fromFile(file)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.resources.getString(R.string.app_name))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, photoURI)
        mContext.startActivity(intent)
    }

    @JvmStatic
    fun shareMultipleImages(mContext: Context, filesList: ArrayList<ListIdPic>, mimeType: String?) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.resources.getString(R.string.app_name))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = mimeType

        val files = ArrayList<Uri>()
        for (item in filesList) {
            val file = copyTemporaryToFolder(mContext, item.newPath, item.displayName)
                ?: return
            val fileURI =
                FileProvider.getUriForFile(mContext, mContext.packageName + ".provider", file)
            files.add(fileURI)
        }
        intent.putExtra(Intent.EXTRA_STREAM, files)
        mContext.startActivity(intent)
    }

    fun copyTemporaryToFolder(context: Context, newPath: String, displayName: String): File? {
        val uri: Uri
        uri = if (!isContentUri(newPath)) {
            Uri.fromFile(File(newPath))
        } else {
            Uri.parse(newPath)
        }
        try {
            val temp = TillsFl.getTargetLocation(
                MyApplication.TEMP_COPY_FOLDER + File.separator + displayName
            )
            val targetFile = File(temp.parent, displayName)
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream: OutputStream = FileOutputStream(targetFile)
            TillsFl.copy(inputStream, outputStream)
            return targetFile
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun openWith(mContext: Context, listIdPic: ListIdPic) {
        val intent = Intent(Intent.ACTION_VIEW)
        val file = copyTemporaryToFolder(mContext, listIdPic.newPath, listIdPic.displayName)
            ?: return
        val fileURI = FileProvider.getUriForFile(mContext, mContext.packageName + ".provider", file)
        intent.setDataAndType(fileURI, TillsFl.getMimeType(mContext, Uri.fromFile(file)))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooserIntent = Intent.createChooser(intent, "Open With")
        mContext.startActivity(chooserIntent)
    }

    @JvmStatic
    fun makeUnHide(
        mContext: Context,
        orgPath: String?,
        newPath: String?,
        restorePath: String?,
        from: Int
    ): Boolean {
        if (newPath != null && !newPath.trim { it <= ' ' }.isEmpty()) {
            var newUnHidePath = restorePath
            if (newUnHidePath == null || newUnHidePath.trim { it <= ' ' }.isEmpty() || isContentUri(
                    newUnHidePath
                )
            ) {
                newUnHidePath = restorePath
            }
            val temp = TillsFl.getTargetLocation(newUnHidePath)
            val fileName = TillsFl.getOriginalFileName(temp.name)
            val targetFile = File(temp.parent, fileName)
            val mimeType = TillsFl.getMimeType(mContext, Uri.fromFile(targetFile))
            val uri: Uri
            uri = if (!isContentUri(newPath)) {
                Uri.fromFile(File(newPath))
            } else {
                Uri.parse(newPath)
            }
            val resolver = mContext.contentResolver
            try {
                val inputStream = resolver.openInputStream(uri)
                val outputStream: OutputStream
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (isVideo(File(orgPath).name)) {
                        val contentValues = ContentValues()
                        contentValues.put(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            targetFile.name.split("\\.").toTypedArray()[0]
                        )
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                        contentValues.put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            "Pictures" + "/" + "VaultSecure/RebuildItem/"
                        )
                        val imageUri = resolver.insert(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                        )
                        val inputStream1 = resolver.openInputStream(uri) as FileInputStream?
                        val fos = resolver.openOutputStream(imageUri!!) as FileOutputStream?
                        val inChannel = inputStream1!!.channel
                        val outChannel = fos!!.channel
                        try {
                            outChannel.transferFrom(inChannel, 0, inChannel.size())
                            inChannel.close()
                            outChannel.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        getPath(imageUri, mContext)
                    } else {
                        val contentValues = ContentValues()
                        contentValues.put(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            targetFile.name.split("\\.").toTypedArray()[0]
                        )
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                        contentValues.put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            "Pictures" + "/" + "VaultSecure/RebuildItem/"
                        )
                        val imageUri = resolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                        )
                        val inputStream1 = resolver.openInputStream(uri) as FileInputStream?
                        val fos = resolver.openOutputStream(imageUri!!) as FileOutputStream?
                        val inChannel = inputStream1!!.channel
                        val outChannel = fos!!.channel
                        try {
                            outChannel.transferFrom(inChannel, 0, inChannel.size())
                            inChannel.close()
                            outChannel.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        getPath(imageUri, mContext)
                    }
                } else {
                    outputStream = FileOutputStream(targetFile)
                    TillsFl.copy(inputStream, outputStream)
                }
                if (isContentUri(newPath)) {
                    val file = DocumentFile.fromSingleUri(mContext, Uri.parse(newPath))
                    if (file!!.exists()) {
                        file.delete()
                    }
                } else {
                    val file = File(newPath)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                addImageToGallery(mContext, temp.absolutePath, mimeType, from)
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun isVideo(filename: String?): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(filename)
        return mimeType != null && mimeType.startsWith("video")
    }

    fun getPath(uri: Uri?, context: Context): String? {
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        val cursor =
            context.contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun createFileAboveQ(
        context: Context,
        fileName: String,
        mimeType: String?,
        isFromHide: Boolean
    ): Uri {
        var fileName = fileName
        val uriString = SupPref.getburyUri(context)
        val rootUri = Uri.parse(uriString)
        context.contentResolver.takePersistableUriPermission(
            rootUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        val rootDocumentId = DocumentsContract.getTreeDocumentId(rootUri)
        if (isFromHide) {
            fileName = ".$fileName"
        }
        val path = File.separator + fileName
        val addNew = AddNew(
            context,
            context.contentResolver,
            path,
            rootUri,
            rootDocumentId,
            false,
            "",
            false,
            mimeType!!
        )
        val isFileCreated = addNew.createNewFile(true, true)
        return addNew.blankUri
    }

    @JvmStatic
    fun deleteImageVideoFile(
        mContext: Context?,
        id: Int,
        newPath: String?,
        RDbhp: RDbhp,
        isCheckedTrash: Boolean,
        isCheckCloud: Boolean,
        service: Drive?,
        from: Int
    ) {
        if (!isCheckedTrash && !isCheckCloud) {
            if (newPath != null && !newPath.trim { it <= ' ' }.isEmpty()) {
                if (isContentUri(newPath)) {
                    val file = DocumentFile.fromSingleUri(
                        mContext!!, Uri.parse(
                            newPath
                        )
                    )
                    if (file != null && file.exists()) {
                        file.delete()
                    }
                } else {
                    val file = File(newPath)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
            when (from) {
                1 -> RDbhp.deletePhotoItem(id)
                2 -> RDbhp.deleteVideoItem(id)
                3 -> RDbhp.deleteFileItem(id)
            }
        } else {
            if (isCheckedTrash) {
                when (from) {
                    1 -> RDbhp.moveTrashPhoto(id)
                    2 -> RDbhp.moveTrashVideo(id)
                    3 -> RDbhp.moveTrashFile(id)
                }
            }
            if (isCheckCloud) {
            }
        }
    }

    @JvmStatic
    fun hideFiles(
        mContext: Context,
        selectedList: HashMap<String, Uri>,
        RDbhp: RDbhp,
        from: Int
    ): ArrayList<Uri> {
        val deleteRequestList = ArrayList<Uri>()
        for ((inputPath, inputUri) in selectedList) {
            val file = File(inputPath)
            val newFileName = file.name + ".bin"
            val mimeType = TillsFl.getMimeType(mContext, Uri.parse(file.path))
            val contentResolver = mContext.contentResolver
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                inputStream = contentResolver.openInputStream(inputUri)
                var newPath: String
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val outputUri = createFileAboveQ(mContext, newFileName, mimeType, true)
                    outputStream = contentResolver.openOutputStream(outputUri, "w")
                    newPath = outputUri.toString()
                } else {
                    var targetFileName: String
                    targetFileName = when (from) {
                        1 -> TillsPth.nohideImage
                        2 -> TillsPth.nohideVideo
                        else -> TillsPth.nohideFile
                    }
                    val targetLocation = TillsFl.getTargetLocation(targetFileName + newFileName)
                    outputStream = FileOutputStream(targetLocation)
                    newPath = targetLocation.path
                }
                TillsFl.copy(inputStream, outputStream)
                var filesUri: Uri?
                filesUri = when (from) {
                    1 -> {
                        RDbhp.insertImage(
                            file.name,
                            file.absolutePath,
                            newPath,
                            file.length(),
                            mimeType
                        )
                        MediaStore.Images.Media.getContentUri("external")
                    }
                    2 -> {
                        RDbhp.insertVideo(
                            file.name,
                            file.absolutePath,
                            newPath,
                            file.length(),
                            mimeType
                        )
                        MediaStore.Video.Media.getContentUri("external")
                    }
                    else -> {
                        RDbhp.insertFile(
                            file.name,
                            file.absolutePath,
                            newPath,
                            file.length(),
                            mimeType
                        )
                        MediaStore.Files.getContentUri("external")
                    }
                }
                val where = MediaStore.MediaColumns.DATA + "=?"
                val selectionArgs = arrayOf(file.absolutePath)
                contentResolver.delete(filesUri, where, selectionArgs)
                if (file.exists()) {
                    contentResolver.delete(filesUri, where, selectionArgs)
                    if (file.exists()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (file.exists()) {
                                deleteRequestList.add(inputUri)
                            }
                        } else {
                            file.delete()
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    outputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return deleteRequestList
    }
}