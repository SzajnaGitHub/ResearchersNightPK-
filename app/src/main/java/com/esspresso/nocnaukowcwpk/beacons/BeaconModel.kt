package com.esspresso.nocnaukowcwpk.beacons

import androidx.annotation.Keep

@Keep
data class BeaconModel(private val major: String, private val minor: String) {
    val id get() = major + minor
}
