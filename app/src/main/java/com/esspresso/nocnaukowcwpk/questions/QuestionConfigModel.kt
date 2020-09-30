package com.esspresso.nocnaukowcwpk.questions

import androidx.annotation.Keep
import com.esspresso.nocnaukowcwpk.beacons.BeaconId

@Keep
data class QuestionConfigModel(
    val major: String,
    val minor: String,
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
    val question: String,
    val answerA: String,
    val answerB: String,
    val answerC: String,
    val answerD: String,
    val correctAnswer: String,
    val points: Int
)
