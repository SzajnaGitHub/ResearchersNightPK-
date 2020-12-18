package com.esspresso.nocnaukowcwpk.utils

import android.content.Context
import com.esspresso.nocnaukowcwpk.R
import java.io.Serializable

data class SystemDialogModel(
    val header: String,
    val message: String,
    val buttonText: String
) : Serializable {
    companion object {
        fun createNoBluetoothModel(context: Context) =
            SystemDialogModel(context.getString(R.string.warning_title), context.getString(R.string.dialog_bluetooth_off), context.getString(R.string.action_ok))

        fun createNoLocationModel(context: Context) =
            SystemDialogModel(context.getString(R.string.warning_title), context.getString(R.string.dialog_localisation_off), context.getString(R.string.action_ok))

        fun createNoInternetModel(context: Context) =
            SystemDialogModel(context.getString(R.string.warning_title), context.getString(R.string.dialog_network_off), context.getString(R.string.action_ok))
    }
}
