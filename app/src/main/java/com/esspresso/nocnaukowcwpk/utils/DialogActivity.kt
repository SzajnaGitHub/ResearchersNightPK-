package com.esspresso.nocnaukowcwpk.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.databinding.ActivityDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDialogBinding>(this, R.layout.activity_dialog)
        val model = (intent.getSerializableExtra(MODEL) as? SystemDialogModel)
        binding.model = model
        binding.background.setOnClickListener { finishDialog() }
    }

    private fun finishDialog() {
        finish()
        overridePendingTransition(0, R.anim.fade_out)
    }

    companion object {
        private const val MODEL = "model"
        private fun createIntent(context: Context) = Intent(context, DialogActivity::class.java)

        fun createNoBluetoothIntent(context: Context) = createIntent(context).apply { putExtra(MODEL, SystemDialogModel.createNoBluetoothModel()) }
        fun createNoLocationIntent(context: Context) = createIntent(context).apply { putExtra(MODEL, SystemDialogModel.createNoLocationModel()) }
        fun createNoInternetIntent(context: Context) = createIntent(context).apply { putExtra(MODEL, SystemDialogModel.createNoInternetModel()) }
    }
}
