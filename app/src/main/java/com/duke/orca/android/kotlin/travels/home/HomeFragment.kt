package com.duke.orca.android.kotlin.travels.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentHomeBinding
import com.duke.orca.android.kotlin.travels.home.adapter.TourAdapter

class HomeFragment: MainTabItemFragment<FragmentHomeBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private val tourAdapter = TourAdapter {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        initializeAdapter()

        viewModel.tourlist.observe(viewLifecycleOwner, {
            tourAdapter.submitList(it)
        })

        return view
    }

    private fun initializeAdapter() {
        viewBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tourAdapter
        }
    }
}