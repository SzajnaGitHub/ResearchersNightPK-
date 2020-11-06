package com.esspresso.nocnaukowcwpk.core

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.databinding.ActivitySplashBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        setupTransition(binding.root as MotionLayout)
    }

    private fun setupTransition(layout: MotionLayout) {
        runDelayedAction(300) { layout.transitionToState(R.id.fill_state) }

        layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                when (state) {
                    R.id.fill_state -> runDelayedAction { layout.transitionToState(R.id.end_state) }
                    R.id.end_state -> runDelayedAction(400) { finishSplash() }
                    else -> {}
                }
            }
        })
    }

    private fun finishSplash() {
        startActivity(MainActivity.createIntent(this@SplashActivity).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        finish()
    }

    private fun runDelayedAction(delay: Long = 200, action: () -> Unit) {
        Completable.timer(delay, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                action.invoke()
            }.let(disposables::add)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
