package com.esspresso.nocnaukowcwpk.config

import com.esspresso.nocnaukowcwpk.beacons.BeaconConfigModel
import com.esspresso.nocnaukowcwpk.questions.QuestionConfigModel
import com.esspresso.nocnaukowcwpk.ui.eventinfo.EventInfoConfigModel
import com.esspresso.nocnaukowcwpk.utils.JsonParser
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(private val jsonParser: JsonParser) {
    private val remoteConfig = Firebase.remoteConfig

    fun getBeacons() = jsonParser.parseList(remoteConfig.getString("beacons"), BeaconConfigModel::class.java)
    fun getQuestions() = jsonParser.parseList(remoteConfig.getString("beacon_questions"), QuestionConfigModel::class.java)
    fun getEventScheduleItems() = jsonParser.parseList(remoteConfig.getString("event_schedule"), EventInfoConfigModel::class.java)

    init {
        val defaults = mapOf<String, Any>()

        //ONLY FOR DEBUG PURPOSES TODO
        remoteConfig.fetch(0)
        remoteConfig.setDefaultsAsync(defaults)
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {})
        remoteConfig.fetchAndActivate()
    }
}
