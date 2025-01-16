package com.hidefile.secure.folder.vault.dashex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hidefile.secure.folder.vault.R
import com.hidefile.secure.folder.vault.cluecanva.SupPref
import com.hidefile.secure.folder.vault.cluecanva.TilsCo
import com.hidefile.secure.folder.vault.cluecanva.TooRfl
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class Str : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.str)
        Handler().postDelayed({ init() }, 1500) }
    private fun init() {
        TilsCo.isStr = true
        TooRfl.deleteTempFolder()
        if (isPermitted()) {
            val hidden_URI = SupPref.getburyUri(this@Str)
            if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (hidden_URI == null || hidden_URI.trim { it <= ' ' }.isEmpty()) {
                    permitAccess()
                } else {
                    try { contentResolver.takePersistableUriPermission(
                            Uri.parse(hidden_URI),
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        nextMove()
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        permitAccess()
                    }
                }
            } else {
                nextMove()
            }
        } else {
            permitAccess()
        }
    }

    fun requestPermissions() {
        val permissionList: MutableCollection<String> = ArrayList()
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionList.add(Manifest.permission.CAMERA)
        if (VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        Dexter.withContext(this@Str)
            .withPermissions(permissionList)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val hidden_URI = SupPref.getburyUri(this@Str)
                        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (hidden_URI == null || hidden_URI.trim { it <= ' ' }.isEmpty()) {
                                permitAccess()
                            } else {
                                try {
                                    contentResolver.takePersistableUriPermission(
                                        Uri.parse(
                                            hidden_URI
                                        ),
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                    )
                                    nextMove()
                                } catch (e: SecurityException) {
                                    e.printStackTrace()
                                    permitAccess()
                                }
                            }
                        } else {
                            nextMove()
                        }
                    } else {
                        permitAccess()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener { dexterError: DexterError? ->
            }.check()
    }

    private fun isPermitted(): Boolean {
        var isPermissionGranted = false
        if (VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val permissionStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (checkCallingOrSelfPermission(permissionStorage) == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
            }
        }
        val permissionStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (checkCallingOrSelfPermission(permissionStorage) == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
        }
        val permissionCamera = Manifest.permission.CAMERA
        if (checkCallingOrSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
        }
        return isPermissionGranted
    }

    private fun permitAccess() {
        val mainIntent = Intent(this@Str, PermitAccess::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun nextMove() {
        val mainIntent = Intent(this@Str, Pswd::class.java)
        startActivity(mainIntent)
        finish()
    }
}