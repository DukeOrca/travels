package com.duke.orca.android.kotlin.travels.map

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentMapBinding

class MapFragment: MainTabItemFragment<FragmentMapBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater, container, false)
    }
}