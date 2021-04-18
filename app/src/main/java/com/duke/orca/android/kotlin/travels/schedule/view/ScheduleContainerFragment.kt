package com.duke.orca.android.kotlin.travels.schedule.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentScheduleContainerBinding

class ScheduleContainerFragment: MainTabItemFragment<FragmentScheduleContainerBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentScheduleContainerBinding {
        return FragmentScheduleContainerBinding.inflate(inflater, container, false)
    }
}