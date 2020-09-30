package com.esspresso.nocnaukowcwpk.questions

import android.content.Context
import com.esspresso.nocnaukowcwpk.beacons.BeaconId
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.utils.translateFromId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionManager @Inject constructor(private val remoteConfig: RemoteConfigManager, private val context: Context) {

    private lateinit var questionList: List<QuestionModel>

    private fun getQuestions() {
        questionList = remoteConfig.getQuestions().map {
            QuestionModel(
                id = BeaconId(it.major, it.minor),
                question = it.questionId.translateFromId(context),
                answerA = it.answerAId.translateFromId(context),
                answerB = it.answerBId.translateFromId(context),
                answerC = it.answerCId.translateFromId(context),
                answerD = it.answerDId.translateFromId(context),
                correctAnswer = it.correctAnswerId.translateFromId(context),
                points = it.points
            )
        }
    }

    fun getQuestion(id: BeaconId) = questionList.find { it.id == id }
    private fun getQuestion(id: String) = questionList.find { it.id.toString() == id }

    fun getUserPoints(answeredQuestions: Set<String>): Int {
        var points = 0
        answeredQuestions.forEach {
            points += getQuestion(it)?.points ?: 0
        }
        return points
    }

    init {
        getQuestions()
    }
}
