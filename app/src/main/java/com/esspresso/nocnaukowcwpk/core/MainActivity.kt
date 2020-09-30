package com.esspresso.nocnaukowcwpk.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.database.DatabaseManager
import com.esspresso.nocnaukowcwpk.database.user.DBUserModel
import com.esspresso.nocnaukowcwpk.databinding.ActivityMainBinding
import com.esspresso.nocnaukowcwpk.main.ListFragment
import com.esspresso.nocnaukowcwpk.main.profile.ProfileFragment
import com.esspresso.nocnaukowcwpk.main.barcode.BarCodeReaderFragment
import com.esspresso.nocnaukowcwpk.main.map.MapFragment
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
    @Inject
    internal lateinit var databaseManager: DatabaseManager

    private val disposables = CompositeDisposable()
    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }
    private var latestTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager.init(this)

        setupBottomNavigation()
        selectDefaultItem()
        val user = DBUserModel().apply {
            number_of_points = 16
        }

        databaseManager.initialUserModel = user
        Handler().postDelayed({ databaseManager.saveUserToDatabase() }, 5000)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.profile_view -> ProfileFragment.newInstance()
                R.id.map_view -> MapFragment.newInstance()
                R.id.list_view -> ListFragment.newInstance()
                R.id.about_view -> BarCodeReaderFragment.newInstance()
                else -> null
            }
            fragment?.let { handleSelectedMenuItem(fragment, fragment::class.java.name) }
            true
        }
    }

    private fun selectDefaultItem() {
        binding.bottomNavigation.selectedItemId = R.id.map_view
    }

    private fun handleSelectedMenuItem(fragment: Fragment, tag: String) {
        if (latestTag != tag) {
            try {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment, tag).commit()
                latestTag = tag
            } catch (e: Exception) {}
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
