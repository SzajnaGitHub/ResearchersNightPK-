package com.esspresso.nocnaukowcwpk.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.db.userquestions.UserQuestionsDao
import com.esspresso.db.userquestions.UserQuestionsDatabase
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.databinding.FragmentProfileBinding
import com.esspresso.nocnaukowcwpk.questions.QuestionManager
import com.esspresso.nocnaukowcwpk.store.KeyValueStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    internal lateinit var store: KeyValueStore
    @Inject
    internal lateinit var questionManager: QuestionManager
    @Inject
    internal lateinit var questionsDao: UserQuestionsDao

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        setupModel()
        return binding.root
    }

    private fun setupModel() {
        questionsDao.getAllQuestions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { list ->
                val allQuestionsSize = list.size
                val questionsAnsweredProperly = list.filter { it.questionAnsweredCorrectly }
                Pair(allQuestionsSize, questionsAnsweredProperly)
            }
            .subscribe {(allQuestionsSize, questionsAnsweredProperly) ->
                binding.model = ProfileFragmentViewModel(
                    userName = store.userName,
                    userPoints = questionsAnsweredProperly.sumBy { it.points },
                    correctAnswers = questionsAnsweredProperly.size,
                    allAnsweredQuestions = allQuestionsSize
                )
                getUserAnsweredCategories(questionsAnsweredProperly.map { it.category }.toSet())
            }
            .let(disposable::add)
    }

    private fun getUserAnsweredCategories(categorySet: Set<String>) {
        questionManager.getUserAnsweredCategories(categorySet)
            .map { ArrayList(it) }
            .subscribe({
                binding.items = it
                binding.questionCategoryList.adapter?.notifyDataSetChanged()
                binding.questionCategoryList.scheduleLayoutAnimation()
            },{})
            .let(disposable::add)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
