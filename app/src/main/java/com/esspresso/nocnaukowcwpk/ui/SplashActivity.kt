package com.esspresso.nocnaukowcwpk.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.databinding.ActivitySplashBinding
import com.esspresso.nocnaukowcwpk.utils.postAction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        setupTransition(binding.root as MotionLayout)
    }

    private fun setupTransition(layout: MotionLayout) {
        postAction(300) { layout.transitionToState(R.id.fill_state) }

        layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                when (state) {
                    R.id.fill_state -> postAction(200) { layout.transitionToState(R.id.end_state) }
                    R.id.end_state -> postAction(400) { finishSplash() }
                    else -> {}
                }
            }
        })
    }

    private fun finishSplash() {
        startActivity(MainActivity.createIntent(this).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        finish()
    }
}
