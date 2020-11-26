package com.esspresso.nocnaukowcwpk.questions

import androidx.annotation.Keep
import com.esspresso.nocnaukowcwpk.beacons.BeaconId
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class QuestionConfigModel(
    val major: String,
    val minor: String,
    val category: String,
    val questionId: String,
    val answerAId: String,
    val answerBId: String,
    val answerCId: String,
    val answerDId: String,
    val correctAnswerId: String,
    val points: Int
)

@Keep
data class QuestionModel(
    val id: BeaconId,
    val category: String,
    val question: String,
    val answerA: String,
    val answerB: String,
    val answerC: String,
    val answerD: String,
    val correctAnswer: String,
    val points: Int
)
