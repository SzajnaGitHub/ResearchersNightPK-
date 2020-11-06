package com.esspresso.nocnaukowcwpk.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.database.DatabaseManager
import com.esspresso.nocnaukowcwpk.database.user.DBUserModel
import com.esspresso.nocnaukowcwpk.databinding.ActivityMainBinding
import com.esspresso.nocnaukowcwpk.main.EventInfoFragment
import com.esspresso.nocnaukowcwpk.main.ListFragment
import com.esspresso.nocnaukowcwpk.main.SettingsFragment
import com.esspresso.nocnaukowcwpk.main.barcode.BarCodeReaderFragment
import com.esspresso.nocnaukowcwpk.main.map.MapFragment
import com.esspresso.nocnaukowcwpk.main.profile.ProfileFragment
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
    private val motionLayout by lazy(LazyThreadSafetyMode.NONE) { binding.root as MotionLayout }
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMenu()
        permissionManager.init(this)
        setupTransitionListener()
        setupMenuBackClick()
        setupMenuOnClick()
        val user = DBUserModel().apply {
            number_of_points = 16
        }
        databaseManager.initialUserModel = user
        //  Handler().postDelayed({ databaseManager.saveUserToDatabase() }, 5000)
    }

    private fun setupMenu() {
        binding.mapSquare.model = MenuItemModel(R.drawable.background_blue, R.drawable.ic_map, getString(R.string.menu_item_map))
        binding.scannerSquare.model = MenuItemModel(R.drawable.background_brown, R.drawable.ic_bt_scanner, getString(R.string.menu_item_scanner))
        binding.qrSquare.model = MenuItemModel(R.drawable.background_purple, R.drawable.ic_qr_code, getString(R.string.menu_item_qr))
        binding.eventSquare.model = MenuItemModel(R.drawable.background_green, R.drawable.ic_schedule, getString(R.string.menu_item_schedule))
        binding.profileSquare.model = MenuItemModel(R.drawable.background_orange, R.drawable.ic_profile, getString(R.string.menu_item_profile))
        binding.infoSquare.model = MenuItemModel(R.drawable.background_yellow, R.drawable.ic_help, getString(R.string.menu_item_info))
    }

    private fun setupMenuOnClick() {
        binding.mapSquare.root.setOnClickListener { motionLayout.transitionToEndState(R.id.map_square_end) }
        binding.scannerSquare.root.setOnClickListener { motionLayout.transitionToEndState(R.id.scanner_square_end) }
        binding.qrSquare.root.setOnClickListener { motionLayout.transitionToEndState(R.id.qr_square_end) }
        binding.eventSquare.root.setOnClickListener { motionLayout.transitionToEndState(R.id.event_square_end) }
        binding.profileSquare.root.setOnClickListener { motionLayout.transitionToEndState(R.id.profile_square_end) }
        binding.infoSquare.root.setOnClickListener { motionLayout.transitionToEndState(R.id.info_square_end) }
    }

    private fun setupMenuBackClick() {
        binding.mapSquare.iconArrowBack.setOnClickListener { transitionToStart() }
        binding.scannerSquare.iconArrowBack.setOnClickListener { transitionToStart() }
        binding.qrSquare.iconArrowBack.setOnClickListener { transitionToStart() }
        binding.eventSquare.iconArrowBack.setOnClickListener { transitionToStart() }
        binding.profileSquare.iconArrowBack.setOnClickListener { transitionToStart() }
        binding.infoSquare.iconArrowBack.setOnClickListener { transitionToStart() }
    }

    private fun setupTransitionListener() {
        motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                when (state) {
                    R.id.map_square_end -> handleSelectedMenuItem(MapFragment.newInstance())
                    R.id.scanner_square_end -> handleSelectedMenuItem(ListFragment.newInstance())
                    R.id.qr_square_end -> handleSelectedMenuItem(BarCodeReaderFragment.newInstance())
                    R.id.event_square_end -> handleSelectedMenuItem(EventInfoFragment.newInstance())
                    R.id.profile_square_end -> handleSelectedMenuItem(ProfileFragment.newInstance())
                    R.id.info_square_end -> handleSelectedMenuItem(SettingsFragment.newInstance())
                }
            }
        })
    }

    private fun handleSelectedMenuItem(fragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment, fragment::class.java.name).commit()
            currentFragment = fragment
            binding.fragmentContainer.visibility = View.VISIBLE
            binding.fragmentContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        } catch (e: Exception) {}
    }

    private fun transitionToStart() {
        hideFragmentContent()
        motionLayout.transitionToEndState(R.id.start)
    }

    private fun removeCurrentFragment() {
        currentFragment?.let { supportFragmentManager.beginTransaction().remove(it).commit() }
        currentFragment = null
        binding.fragmentContainer.visibility = View.GONE
    }

    private fun hideFragmentContent() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.quick_fade_out)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                removeCurrentFragment()
            }
        })
        binding.fragmentContainer.apply { startAnimation(animation) }
    }

    override fun onBackPressed() {
        if (motionLayout.currentState != R.id.start) {
            transitionToStart()
        } else {
            super.onBackPressed()
        }
    }

    private fun MotionLayout.transitionToEndState(state: Int) {
        this.transitionToState(state)
        this.transitionToEnd()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
