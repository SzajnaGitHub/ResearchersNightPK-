package com.esspresso.nocnaukowcwpk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.esspresso.db.userquestions.UserQuestion
import com.esspresso.db.userquestions.UserQuestionsDao
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.beacons.BeaconCardViewModel
import com.esspresso.nocnaukowcwpk.beacons.BeaconId
import com.esspresso.nocnaukowcwpk.databinding.ActivityBeaconItemBinding
import com.esspresso.nocnaukowcwpk.questions.QuestionManager
import com.esspresso.nocnaukowcwpk.questions.QuestionModel
import com.esspresso.nocnaukowcwpk.store.KeyValueStore
import com.esspresso.nocnaukowcwpk.utils.postAction
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_question.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class BeaconCardActivity : AppCompatActivity() {
    @Inject
    internal lateinit var questionManager: QuestionManager
    @Inject
    internal lateinit var store: KeyValueStore
    @Inject
    internal lateinit var userQuestionDAO: UserQuestionsDao

    private val binding by lazy(LazyThreadSafetyMode.NONE) { DataBindingUtil.setContentView<ActivityBeaconItemBinding>(this, R.layout.activity_beacon_item) }
    private val disposables = CompositeDisposable()
    private var answerSubmitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupModel()
        setupBinding()
    }

    private fun setupModel() {
        val beaconId = intent.getSerializableExtra(BEACON_ID) as BeaconId? ?: BeaconId("", "")
        val model = BeaconCardViewModel(
            beaconId = beaconId,
            categoryId = intent.getStringExtra(BEACON_CATEGORY),
            questionModel = questionManager.getQuestion(beaconId)
        )
        binding.model = model
        postAction(500){
            binding.background.visibility = View.VISIBLE
            binding.categoryText.visibility = View.VISIBLE
        }
    }

    private fun setupBinding() {
        val questionModel = binding.model?.questionModel
        binding.claimButton.setOnClickListener {
            if (questionModel == null) {
                finishAfterTransition()
            } else {
                binding.questionView.answerGroup.children.forEach { it.isEnabled = false }
                val userAnsweredCorrectly = when (binding.questionView.answerGroup.checkedRadioButtonId) {
                    R.id.radio_option_1 -> (questionModel.answerA == questionModel.correctAnswer).also { binding.questionView.answerGroup.radio_option_1.setBackgroundColor(getBackgroundColor(it)) }
                    R.id.radio_option_2 -> (questionModel.answerB == questionModel.correctAnswer).also { binding.questionView.answerGroup.radio_option_2.setBackgroundColor(getBackgroundColor(it)) }
                    R.id.radio_option_3 -> (questionModel.answerC == questionModel.correctAnswer).also { binding.questionView.answerGroup.radio_option_3.setBackgroundColor(getBackgroundColor(it)) }
                    R.id.radio_option_4 -> (questionModel.answerD == questionModel.correctAnswer).also { binding.questionView.answerGroup.radio_option_4.setBackgroundColor(getBackgroundColor(it)) }
                    else -> false
                }
                submitAnswer(userAnsweredCorrectly, questionModel)
            }
        }
        binding.toolbar.setOnClickListener {
            finishAfterTransition()
        }
    }

    private fun submitAnswer(userAnsweredCorrectly: Boolean, model: QuestionModel) {
        if (!answerSubmitted) {
            answerSubmitted = true
            userQuestionDAO.upsert(UserQuestion(id = model.id.toString(),questionAnsweredCorrectly = userAnsweredCorrectly, category = model.category, points = model.points))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    finishWithResult()
                }
                .let(disposables::add)
        }
    }

    private fun finishWithResult() {
        Completable.timer(800, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setResult(Activity.RESULT_OK)
                finishAfterTransition()
            }, {})
            .let(disposables::add)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun getBackgroundColor(userAnsweredCorrectly: Boolean) = ContextCompat.getColor(this, if (userAnsweredCorrectly) android.R.color.holo_green_light else android.R.color.holo_red_light)

    companion object {
        private const val BEACON_ID = "beacon_id"
        private const val BEACON_CATEGORY = "beacon_category"
        fun createIntent(context: Context, beaconId: BeaconId, beaconCategory: String?) = Intent(context, BeaconCardActivity::class.java).apply {
            putExtra(BEACON_ID, beaconId)
            putExtra(BEACON_CATEGORY, beaconCategory ?: "")
        }
    }
}
