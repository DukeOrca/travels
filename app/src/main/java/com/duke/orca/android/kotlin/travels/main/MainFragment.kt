package com.duke.orca.android.kotlin.travels.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentMainBinding
import com.duke.orca.android.kotlin.travels.main.adapter.MainFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment: Fragment() {
    private var viewBinding: FragmentMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false)

        initializeView()

        return viewBinding?.root
    }

    private fun initializeLiveData() {

    }

    private fun initializeView() {
        viewBinding?.let { viewBinding ->
            viewBinding.viewPager2.adapter = MainFragmentStateAdapter(this)
            TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
                tab.tag = position
                //tab.text = tabTexts[position]
            }.attach()
        }
    }
}