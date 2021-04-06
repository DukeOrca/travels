package com.duke.orca.android.kotlin.travels.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.base.MainTabFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentHomeBinding

class HomeFragment: MainTabFragment<FragmentHomeBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        viewModel.tourlist.observe(viewLifecycleOwner, {
            println("gggggggggg")
            println(it)
        })

        return view
    }

    private fun initializeAdapter() {
        viewBinding.recyclerView.apply {

        }
    }
}