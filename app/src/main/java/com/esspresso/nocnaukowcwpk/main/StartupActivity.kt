package com.esspresso.nocnaukowcwpk.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esspresso.nocnaukowcwpk.R
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class StartupActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private val rxPermissions = RxPermissions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        requestPermission()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun requestPermission() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { granted ->
                if (granted) {
                    println("TEKST GRANTED")
                } else {
                    println("TEKST DUPA")
                }
            }.let(disposables::add)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, StartupActivity::class.java)
    }
}
