package com.esspresso.nocnaukowcwpk.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.toAndroidPair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.ActivityMainBinding
import com.esspresso.nocnaukowcwpk.main.ListFragment
import com.esspresso.nocnaukowcwpk.main.MapFragment
import com.esspresso.nocnaukowcwpk.main.ProfileFragment
import com.esspresso.nocnaukowcwpk.main.SettingsFragment
import com.esspresso.nocnaukowcwpk.status.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    internal lateinit var remoteConfig: RemoteConfigManager
    @Inject
    internal lateinit var permissionManager: PermissionManager

    private val disposables = CompositeDisposable()
    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomNavigation()
        selectDefaultItem()
        permissionManager.init(this)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.profile_view -> ProfileFragment.newInstance()
                R.id.map_view -> MapFragment.newInstance()
                R.id.list_view -> ListFragment.newInstance()
                R.id.about_view -> SettingsFragment.newInstance()
                else -> null
            }
            fragment?.let { handleSelectedMenuItem(fragment, fragment::javaClass.name) }
            true
        }
    }

    private fun selectDefaultItem() {
        binding.bottomNavigation.selectedItemId = R.id.list_view
    }

    private fun handleSelectedMenuItem(fragment: Fragment, tag: String) {
        try {
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment, tag).commit()
        } catch (e: Exception) { }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
