package com.esspresso.nocnaukowcwpk.store

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("SameParameterValue")
@Singleton
class KeyValueStore @Inject constructor(private val preferences: SharedPreferences) {

    var userName: String
        get() = getString(USER_NAME_TAG)
        set(value) = put(USER_NAME_TAG, value)

    private fun put(tag: String, value: String) = preferences.edit().putString(tag, value).apply()
    private fun put(tag: String, value: Set<String>) = preferences.edit().putStringSet(tag, value).apply()
    private fun getStringSet(tag: String) = preferences.getStringSet(tag, emptySet()) ?: emptySet()
    private fun getString(tag: String) = preferences.getString(tag, STRING_VALUE_NOT_INITIALIZED) ?: STRING_VALUE_NOT_INITIALIZED

    companion object {
        const val STRING_VALUE_NOT_INITIALIZED = "string value not initialized"

        private const val USER_NAME_TAG = "user_name_tag"
    }
}
