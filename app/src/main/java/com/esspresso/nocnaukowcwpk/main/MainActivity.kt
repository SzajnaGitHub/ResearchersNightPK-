package com.esspresso.nocnaukowcwpk.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    internal lateinit var remoteConfig: RemoteConfigManager

    private val disposables = CompositeDisposable()
    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        println("TEKST REMOTE CONFIG VALUE: ${remoteConfig.testValue}")
        remoteConfig.lol()
        //startActivity(StartupActivity.createIntent(this))
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

}
