package com.esspresso.nocnaukowcwpk.utils

import com.esspresso.nocnaukowcwpk.beacons.Signal

object RssiResolver {
    private const val UPPER_BOUNDARY_VALUE = -77
    private const val LOWER_BOUNDARY_VALUE = -100

    fun resolve(rssi: Int) = when {
        rssi >= UPPER_BOUNDARY_VALUE -> Signal.STRONG
        rssi <= LOWER_BOUNDARY_VALUE -> Signal.WEAK
        else -> Signal.NORMAL
    }
}
