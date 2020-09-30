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

    var userQuestionAnsweredCorrectly: Set<String>
        get() = getStringSet(USER_QUESTION_ANSWERED_CORRECTLY)
        set(value) = put(USER_QUESTION_ANSWERED_CORRECTLY, value)

    var userQuestionAnsweredIncorrectly: Set<String>
        get() = getStringSet(USER_QUESTION_ANSWERED_INCORRECTLY)
        set(value) = put(USER_QUESTION_ANSWERED_INCORRECTLY, value)

    private fun put(tag: String, value: String) = preferences.edit().putString(tag, value).apply()
    private fun put(tag: String, value: Set<String>) = preferences.edit().putStringSet(tag, value).apply()
    private fun getStringSet(tag: String) = preferences.getStringSet(tag, emptySet()) ?: emptySet()
    private fun getString(tag: String) = preferences.getString(tag, STRING_VALUE_NOT_INITIALIZED) ?: STRING_VALUE_NOT_INITIALIZED

    companion object {
        const val STRING_VALUE_NOT_INITIALIZED = "string value not initialized"

        private const val USER_NAME_TAG = "user_name_tag"
        private const val USER_QUESTION_ANSWERED_CORRECTLY = "user_question_answered_correctly"
        private const val USER_QUESTION_ANSWERED_INCORRECTLY = "user_question_answered_incorrectly"
    }
}
