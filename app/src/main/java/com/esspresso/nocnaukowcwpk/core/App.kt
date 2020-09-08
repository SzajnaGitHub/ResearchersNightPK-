package com.esspresso.nocnaukowcwpk.core

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.esspresso.nocnaukowcwpk.status.NetworkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), LifecycleObserver {
    @Inject
    internal lateinit var networkManager: NetworkManager

    override fun onCreate() {
        super.onCreate()
        appContext = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppStart() {
        networkManager.register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppStop() {
        networkManager.unregister()
    }

    companion object {
        lateinit var appContext: Context
    }
}
