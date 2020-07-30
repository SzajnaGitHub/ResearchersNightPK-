package com.esspresso.nocnaukowcwpk.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.ActivityMainBinding
import com.esspresso.nocnaukowcwpk.main.ListFragment
import com.esspresso.nocnaukowcwpk.main.MapFragment
import com.esspresso.nocnaukowcwpk.main.SettingsFragment
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
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.profile_view -> ListFragment.newInstance()
                R.id.map_view -> MapFragment.newInstance()
                R.id.list_view -> ListFragment.newInstance()
                R.id.about_view -> SettingsFragment.newInstance()
                else -> null
            }
            fragment?.let { handleSelectedMenuItem(fragment, fragment::javaClass.name) }
            true
        }
    }

    private fun handleSelectedMenuItem(fragment: Fragment, tag: String) {
        try {
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment, tag).commit()
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
