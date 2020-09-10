package com.esspresso.nocnaukowcwpk.status

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.esspresso.nocnaukowcwpk.utils.DialogActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor() : ConnectivityManager.NetworkCallback() {
    private lateinit var connectivityManager: ConnectivityManager

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
    }

    fun register(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), this)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(this)
    }
}
