package com.esspresso.nocnaukowcwpk.ui.profile

data class ProfileFragmentViewModel(
    val userName: String,
    val userPoints: Int,
    val correctAnswers: Int,
    val allAnsweredQuestions: Int,
)
