package com.esspresso.nocnaukowcwpk.status

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.esspresso.nocnaukowcwpk.main.ListFragment
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor() {
    private lateinit var rxPermissions: RxPermissions

    fun init(context: Activity) {
        this.rxPermissions = RxPermissions(context as FragmentActivity)
    }

    fun checkPermissionGranted(permission: String) = rxPermissions.isGranted(permission)
    fun checkIfAllPermissionsGranted() = checkPermissionGranted(LOCATION_PERMISSION) && checkPermissionGranted(BLUETOOTH_PERMISSION)

    fun checkPermission(permission: String): Observable<Boolean> = rxPermissions.request(permission).observeOn(AndroidSchedulers.mainThread())
    fun shouldShowPermission(context: Activity, permission: String): Observable<Boolean> = rxPermissions.shouldShowRequestPermissionRationale(context, permission)

    fun getApplicationSettingsIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        return intent
    }

    companion object {
        const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        const val BLUETOOTH_PERMISSION = Manifest.permission.BLUETOOTH
    }
}
