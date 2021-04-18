package com.duke.orca.android.kotlin.travels.schedule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentScheduleBinding
import com.duke.orca.android.kotlin.travels.schedule.adapter.ScheduleAdapter
import com.duke.orca.android.kotlin.travels.schedule.data.Schedule

class ScheduleFragment: MainTabItemFragment<FragmentScheduleBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentScheduleBinding {
        return FragmentScheduleBinding.inflate(inflater, container, false)
    }

    private val scheduleAdapter = ScheduleAdapter {
        viewModel.setSchedule(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }

        initializeLiveData()

        return view
    }

    private fun initializeLiveData() {
        viewModel.schedules.observe(viewLifecycleOwner, {
            scheduleAdapter.submitList(it)
        })

        viewModel.navigateDetailFragmentEvent.observe(viewLifecycleOwner, {
            viewModel.currentSchedule?.let {
                navigateDetailFragment(it)
            }
        })
    }

    private fun navigateDetailFragment(schedule: Schedule) {
        findNavController().navigate(
            ScheduleFragmentDirections.actionScheduleFragmentToDetailFragment(
                schedule
            )
        )
    }
}