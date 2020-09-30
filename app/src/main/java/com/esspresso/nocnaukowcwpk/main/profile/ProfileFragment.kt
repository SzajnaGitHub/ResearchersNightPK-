package com.esspresso.nocnaukowcwpk.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.core.MenuItemModel
import com.esspresso.nocnaukowcwpk.databinding.FragmentProfileBinding
import com.esspresso.nocnaukowcwpk.questions.QuestionManager
import com.esspresso.nocnaukowcwpk.store.KeyValueStore
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    internal lateinit var store: KeyValueStore

    @Inject
    internal lateinit var questionManager: QuestionManager

    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        //setupModel()
        binding.mapSquare.model = MenuItemModel(R.drawable.background_blue, R.drawable.ic_map, "Map")
        binding.scannerSquare.model = MenuItemModel(R.drawable.background_brown, R.drawable.ic_bt_scanner, "Scanner")
        binding.qrSquare.model = MenuItemModel(R.drawable.background_purple, R.drawable.ic_qr_code, "QR")
        binding.eventSquare.model = MenuItemModel(R.drawable.background_green, R.drawable.ic_schedule, "Schedule")
        binding.profileSquare.model = MenuItemModel(R.drawable.background_orange, R.drawable.ic_profile, "Profile")
        binding.infoSquare.model = MenuItemModel(R.drawable.background_yellow, R.drawable.ic_help, "Info")
        return binding.root
    }

/*
    private fun setupModel() {
        val correctAnswers = store.userQuestionAnsweredCorrectly.size
        binding.model = ProfileFragmentViewModel(
            userName = store.userName,
            userPoints = questionManager.getUserPoints(store.userQuestionAnsweredCorrectly),
            correctAnswers = correctAnswers,
            allAnsweredQuestions = correctAnswers + store.userQuestionAnsweredIncorrectly.size
        )
        println("TEKST ${binding.model}")
    }
*/

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}