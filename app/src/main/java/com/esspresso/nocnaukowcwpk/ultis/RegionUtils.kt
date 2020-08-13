package com.esspresso.nocnaukowcwpk.ultis

import org.altbeacon.beacon.Region
import java.util.*

fun createRegion() = Region(UUID.randomUUID().toString(), null, null, null)
