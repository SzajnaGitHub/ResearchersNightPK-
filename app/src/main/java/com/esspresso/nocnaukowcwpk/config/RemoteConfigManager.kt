package com.esspresso.nocnaukowcwpk.config

import com.esspresso.nocnaukowcwpk.beacons.BeaconConfigModel
import com.esspresso.nocnaukowcwpk.faculties.FacultyModel
import com.esspresso.nocnaukowcwpk.questions.QuestionConfigModel
import com.esspresso.nocnaukowcwpk.utils.JsonParser
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(private val jsonParser: JsonParser) {
    private val remoteConfig = Firebase.remoteConfig

    fun getFaculties() = jsonParser.parseList(remoteConfig.getString("faculties"), FacultyModel::class.java)
    fun getBeacons() = jsonParser.parseList(remoteConfig.getString("beacons"), BeaconConfigModel::class.java)
    fun getQuestions() = jsonParser.parseList(remoteConfig.getString("beacon_questions"), QuestionConfigModel::class.java)

    init {
        val defaults = mapOf(
            "test_value" to "value"
            /*     "faculties" to listOf(
                     FacultyModel(id = "WM", lat = 50.075219, lng = 19.997690),
                     FacultyModel(id = "WIL", lat = 50.071365, lng = 19.940940),
                     FacultyModel(id = "WA", lat = 50.075672, lng = 19.909326)
                 ),
                 "beacons" to listOf(
                     BeaconConfigModel(major = "48660", minor = "53082"),
                     BeaconConfigModel(major = "45508", minor = "659"),
                     BeaconConfigModel(major = "62626", minor = "16444")
                 )*/
        )

        //ONLY FOR DEBUG PURPOSES TODO
        remoteConfig.fetch(0)
        remoteConfig.setDefaultsAsync(defaults)
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {})
        remoteConfig.fetchAndActivate()
    }
}

/*
        val architecture = LatLng(50.075468, 19.909633)
        val materialAndPhysics = LatLng(50.075626, 19.908789)
        val mechanicalEngineering = LatLng(50.075219, 19.997690)
        val civilEngineering = LatLng(50.071500, 19.071500)
        val envAndPowerEngineering = LatLng(50.072239, 19.943927)
        val compScienceAndTelecommunications = LatLng(50.071259, 19.941291)
        val electricalAndCompEngineering = LatLng(50.071006, 19.943079)
        val chemicalEngineeringAndTech = LatLng(50.071488, 19.940927)
 */
