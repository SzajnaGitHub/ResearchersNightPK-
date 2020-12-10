package com.esspresso.nocnaukowcwpk.utils

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import pub.devrel.easypermissions.EasyPermissions

object PermissionUtility {

    private const val LOCATION_REQUEST_CODE = 1
    private const val CAMERA_REQUEST_CODE = 2
    private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

    fun hasBluetoothAndLocationPermissions(context: Context) = EasyPermissions.hasPermissions(context, LOCATION_PERMISSION)

    fun hasCameraPermission(context: Context) = EasyPermissions.hasPermissions(context, CAMERA_PERMISSION)

    fun requestLocationPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(fragment, fragment.getString(R.string.permission_required_location), LOCATION_REQUEST_CODE, LOCATION_PERMISSION)
    }

    fun requestCameraPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(fragment, fragment.getString(R.string.permission_required_camera), CAMERA_REQUEST_CODE, CAMERA_PERMISSION)
    }

}
