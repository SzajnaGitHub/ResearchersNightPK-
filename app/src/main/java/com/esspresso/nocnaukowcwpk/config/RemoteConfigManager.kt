package com.esspresso.nocnaukowcwpk.config

import com.esspresso.nocnaukowcwpk.ultis.JsonParser
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(private val jsonParser: JsonParser) {
    private val remoteConfig = Firebase.remoteConfig

    val testValue get() = remoteConfig.getString("test_value")

    fun lol() = jsonParser.test()

    init {
        val defaults = mapOf(
            "test_value" to "value"
        )

        remoteConfig.setDefaultsAsync(defaults)
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {})
        remoteConfig.fetchAndActivate()
    }
}
