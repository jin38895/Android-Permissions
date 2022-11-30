package com.ispsolutionsofficial.permissionsdemo

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    // Requesting for only 1 permission
    private val cameraResultLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permission Granted for Camera", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_LONG).show()
        }
    }

    // requesting for multiple permissions
    private val cameraLocationResultLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                Log.v("CustomPermission","$permissionName: $isGranted")
                if(isGranted) {
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Access granted for fine Location", Toast.LENGTH_LONG).show()
                    } else if(permissionName==Manifest.permission.CAMERA) {
                        Toast.makeText(this, "Access granted for Camera", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Access granted for coarse Location", Toast.LENGTH_LONG).show()
                    }
            } else {
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION || permissionName == Manifest.permission.ACCESS_COARSE_LOCATION) {
                        Toast.makeText(this, "Access denied for fine Location", Toast.LENGTH_LONG).show()
                    } else if(permissionName==Manifest.permission.CAMERA) {
                        Toast.makeText(this, "Access denied for Camera", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Access denied for coarse Location", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPermissions:Button = findViewById(R.id.btnPermissions)
        btnPermissions.setOnClickListener {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showRationaleDialog("App requires camera access", "Camera cannot be used because access is denied")
            } else if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                        || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))){
                showRationaleDialog("App Requires Location Access", "Location cannot be used access is denied")
            } else {
                cameraLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    }

    private fun showRationaleDialog(title:String, message:String) {
        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel"){dialog, _-> dialog.dismiss()}
        builder.create().show()
    }
}