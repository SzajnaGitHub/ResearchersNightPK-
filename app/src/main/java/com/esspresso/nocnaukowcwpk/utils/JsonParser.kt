package com.esspresso.nocnaukowcwpk.utils

import android.util.MalformedJsonException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonParser @Inject constructor(private val moshi: Moshi) {

    fun <T> parseList(value: String, tag: Class<T>): List<T> {
        var list = listOf<T>()
        try {
            list = moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, tag)).fromJson(value) ?: emptyList()
        }
        catch (malformedJson: MalformedJsonException) { }
        catch (exception: Exception) { }
        return list
    }

    fun <T> parse(value: String, tag: Class<T>): T? {
        var model: T? = null
        try {
            moshi.adapter(tag).fromJson(value)?.let { model = it }
        }
        catch (malformedJson: MalformedJsonException) { }
        catch (exception: Exception) { }
        return model
    }
}
