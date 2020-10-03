package com.esspresso.nocnaukowcwpk.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.databinding.FragmentEventInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventInfoFragment : Fragment() {
    private lateinit var binding: FragmentEventInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = EventInfoFragment()
    }
}
