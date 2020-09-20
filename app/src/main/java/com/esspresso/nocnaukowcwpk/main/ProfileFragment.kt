package com.esspresso.nocnaukowcwpk.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.R
import com.esspresso.nocnaukowcwpk.databinding.FragmentProfileBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ProfileFragment : Fragment() {
    private val disposable = CompositeDisposable()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }


    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
