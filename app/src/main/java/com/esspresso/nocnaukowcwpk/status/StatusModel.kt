package com.esspresso.nocnaukowcwpk.status

import java.io.Serializable

data class StatusModel(
    val connectionAvailable: Boolean = true,
    val bluetoothAvailable: Boolean = true,
    val locationAvailable: Boolean = true
) : Serializable {
    val isAllEnabled get() = connectionAvailable && bluetoothAvailable && locationAvailable
}
