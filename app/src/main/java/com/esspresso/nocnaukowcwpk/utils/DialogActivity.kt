package com.esspresso.nocnaukowcwpk.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.databinding.ActivityDialogBinding
import com.esspresso.nocnaukowcwpk.di.RemotelyCloseDialogActivity
import com.esspresso.nocnaukowcwpk.status.PermissionManager
import com.jakewharton.rxrelay3.Relay
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class DialogActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDialogBinding>(this, R.layout.activity_dialog)
        val model = (intent.getSerializableExtra(MODEL) as? SystemDialogModel)
        binding.model = model
        binding.background.setOnClickListener { finishDialog() }
        binding.button.setOnClickListener {
            handler?.invoke()
            finishDialog()
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun finishDialog() {
        finish()
        overridePendingTransition(0, R.anim.fade_out)
    }

    companion object {
        private var handler: (() -> Unit)? = null

        private const val MODEL = "model"
        private fun createIntent(context: Context) = Intent(context, DialogActivity::class.java)

        fun createPermissionIntent(context: Context, permission: String, handler: (() -> Unit)?) = createIntent(context).apply {
            this@Companion.handler = handler
            val model = when (permission) {
                PermissionManager.LOCATION_PERMISSION -> SystemDialogModel("Warning", "Please enable location permission for app to work properly", "Settings")
                PermissionManager.BLUETOOTH_PERMISSION -> SystemDialogModel("Warning", "Please enable bluetooth permission for app to work properly", "Settings")
                else -> null
            }
            putExtra(MODEL, model)
        }
    }
}

data class SystemDialogModel(
    val header: String,
    val message: String,
    val buttonText: String
) : Serializable
