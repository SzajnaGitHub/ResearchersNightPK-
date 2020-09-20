package com.esspresso.nocnaukowcwpk.utils

import java.io.Serializable

data class SystemDialogModel(
    val header: String,
    val message: String,
    val buttonText: String
) : Serializable {
    companion object {
        fun createBluetoothPermissionModel() = SystemDialogModel("Warning", "Please enable bluetooth permission for app to work properly", "Settings")
        fun createLocationPermissionModel() = SystemDialogModel("Warning", "Please enable location permission for app to work properly", "Settings")
        fun createCameraPermissionModel() = SystemDialogModel("Warning","Please enable camera permission for app to use camera","Settings")
        fun createNoBluetoothModel() = SystemDialogModel("Warning", "Bluetooth is turned off, please turn it on to continue searching for treasures", "ok")
        fun createNoLocationModel() = SystemDialogModel("Warning", "Location is turned off, please turn it on to continue searching for treasures", "ok")
        fun createNoInternetModel() = SystemDialogModel("Warning", "Network connection is turned off, please turn it on to continue searching for treasures", "ok")
    }
}
