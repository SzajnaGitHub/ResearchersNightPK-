package com.esspresso.nocnaukowcwpk.utils

import com.esspresso.nocnaukowcwpk.beacons.Signal

object RssiResolver {
    private const val UPPER_THRESHOLD_VALUE = -77
    private const val LOWER_THRESHOLD_VALUE = -100

    fun resolve(rssi: Int) = when {
        rssi >= UPPER_THRESHOLD_VALUE -> Signal.STRONG
        rssi <= LOWER_THRESHOLD_VALUE -> Signal.WEAK
        else -> Signal.NORMAL
    }
}
