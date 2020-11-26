package com.esspresso.nocnaukowcwpk.status

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.esspresso.nocnaukowcwpk.di.BluetoothState
import com.esspresso.nocnaukowcwpk.utils.DialogActivity
import com.jakewharton.rxrelay3.Relay
import javax.inject.Inject

class BluetoothBroadcastReceiver @Inject constructor(@BluetoothState private val bluetoothRelay: Relay<Boolean>) : BroadcastReceiver() {

    override fun onReceive(context: Context?, receivedIntent: Intent?) {
        when (receivedIntent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
            BluetoothAdapter.STATE_OFF -> {
                bluetoothRelay.accept(false)
                context?.let { it.startActivity(DialogActivity.createNoBluetoothIntent(it)) }
            }
            BluetoothAdapter.STATE_ON -> bluetoothRelay.accept(true)
        }
    }

    fun register(activity: Activity) = activity.registerReceiver(this, IntentFilter(BLUETOOTH_STATE_CHANGED_ACTION))
    fun unregister(activity: Activity) = activity.unregisterReceiver(this)

    companion object {
        private const val BLUETOOTH_STATE_CHANGED_ACTION = BluetoothAdapter.ACTION_STATE_CHANGED
    }
}
