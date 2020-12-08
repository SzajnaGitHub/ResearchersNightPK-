package com.esspresso.nocnaukowcwpk.questions

import android.content.Context
import com.esspresso.nocnaukowcwpk.beacons.BeaconId
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver.CATEGORY_BUILDINGS
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver.CATEGORY_GAMES
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver.CATEGORY_MECHANICS
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver.CATEGORY_SCIENCE
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver.CATEGORY_TECHNOLOGY
import com.esspresso.nocnaukowcwpk.utils.CategoryResolver.CATEGORY_VEHICLES
import com.esspresso.nocnaukowcwpk.utils.translateFromId
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionManager @Inject constructor(private val remoteConfig: RemoteConfigManager, @ApplicationContext private val context: Context) {

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
                points = it.points,
                category = it.category
            )
        }
    }

    fun getQuestion(id: BeaconId) = questionList.find { it.id == id }

    fun getUserAnsweredCategories(answeredQuestions: Set<String>): Single<List<ItemCategoryModel>> = Single.just(answeredQuestions)
        .subscribeOn(Schedulers.single())
        .map { answeredQuestionsList ->
            listOf(
                ItemCategoryModel(CATEGORY_TECHNOLOGY, answeredQuestionsList.filter { it == CATEGORY_TECHNOLOGY }.size),
                ItemCategoryModel(CATEGORY_SCIENCE, answeredQuestionsList.filter { it == CATEGORY_SCIENCE }.size),
                ItemCategoryModel(CATEGORY_MECHANICS, answeredQuestionsList.filter { it == CATEGORY_MECHANICS }.size),
                ItemCategoryModel(CATEGORY_GAMES, answeredQuestionsList.filter { it == CATEGORY_GAMES }.size),
                ItemCategoryModel(CATEGORY_VEHICLES, answeredQuestionsList.filter { it == CATEGORY_VEHICLES }.size),
                ItemCategoryModel(CATEGORY_BUILDINGS, answeredQuestionsList.filter { it == CATEGORY_BUILDINGS }.size)
            )
        }
        .observeOn(AndroidSchedulers.mainThread())

    init {
        getQuestions()
    }
}
