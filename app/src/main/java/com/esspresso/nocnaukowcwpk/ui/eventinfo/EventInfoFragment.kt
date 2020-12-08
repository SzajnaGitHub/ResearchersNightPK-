package com.esspresso.nocnaukowcwpk.ui.eventinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esspresso.nocnaukowcwpk.config.RemoteConfigManager
import com.esspresso.nocnaukowcwpk.databinding.FragmentEventInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventInfoFragment : Fragment() {
    @Inject
    internal lateinit var remoteConfig: RemoteConfigManager

    private lateinit var binding: FragmentEventInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false)
        binding.items = ArrayList(remoteConfig.getEventScheduleItems().map {
            EventInfoItemModel.create(it)
        })
        return binding.root
    }

    companion object {
        fun newInstance() = EventInfoFragment()
    }
}
