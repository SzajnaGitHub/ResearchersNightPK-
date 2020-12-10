package com.esspresso.nocnaukowcwpk.utils

import java.io.Serializable

data class SystemDialogModel(
    val header: String,
    val message: String,
    val buttonText: String
) : Serializable {
    companion object {
        fun createNoBluetoothModel() = SystemDialogModel("Warning", "Bluetooth is turned off, please turn it on to continue searching for treasures", "ok")
        fun createNoLocationModel() = SystemDialogModel("Warning", "Location is turned off, please turn it on to continue searching for treasures", "ok")
        fun createNoInternetModel() = SystemDialogModel("Warning", "Network connection is turned off, please turn it on to continue searching for treasures", "ok")
    }
}
