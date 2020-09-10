package com.esspresso.nocnaukowcwpk.status

import java.io.Serializable

data class StatusModel(
    val connectionAvailable: Boolean,
    val bluetoothAvailable: Boolean,
    val locationAvailable: Boolean
) : Serializable {
    val isAllEnabled get() = connectionAvailable && bluetoothAvailable && locationAvailable
}
