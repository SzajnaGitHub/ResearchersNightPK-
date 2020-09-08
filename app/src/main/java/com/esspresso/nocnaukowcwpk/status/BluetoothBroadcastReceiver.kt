package com.esspresso.nocnaukowcwpk.status

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import javax.inject.Inject

class BluetoothBroadcastReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context?, receivedIntent: Intent?) {
        when (receivedIntent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
            BluetoothAdapter.STATE_OFF -> println("TEKST STATE OFF")
            BluetoothAdapter.STATE_ON -> println("TEKST STATE ON")
        }
    }

    fun register(activity: Activity) = activity.registerReceiver(this, IntentFilter(BLUETOOTH_STATE_CHANGED_ACTION))
    fun unregister(activity: Activity) = activity.unregisterReceiver(this)

    companion object {
        private const val BLUETOOTH_STATE_CHANGED_ACTION = BluetoothAdapter.ACTION_STATE_CHANGED
    }
}
