package com.duke.orca.android.kotlin.travels.schedule.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentDetailBinding
import com.duke.orca.android.kotlin.travels.schedule.data.Schedule
import com.duke.orca.android.kotlin.travels.schedule.detail.adapter.DetailItem
import com.duke.orca.android.kotlin.travels.schedule.detail.adapter.DetailItemAdapter

class DetailFragment: MainTabItemFragment<FragmentDetailBinding>() {
    private val detailItemAdapter = DetailItemAdapter()
    private val navArgs by navArgs<DetailFragmentArgs>()
    private val schedule by lazy { navArgs.schedule }
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        initializeView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailItemAdapter.submitList(createDetailItems(schedule))
    }

    override fun onResume() {
        super.onResume()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.scheduleFragment)
            }
        }

        onBackPressedCallback?.let {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, it)
        }
    }

    private fun initializeView() {
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = detailItemAdapter
        }

        viewBinding.textViewTourspotname.text = schedule.tourspotname

        Glide.with(requireContext())
            .load(schedule.tourimg)
            .transform(CenterCrop())
            .into(viewBinding.imageViewImg)

        viewBinding.imageViewAudioPlayback.setOnClickListener {
            viewModel.callToggleAudioPlaybackStateEvent(schedule)
        }
    }

    override fun onPause() {
        onBackPressedCallback?.remove()
        super.onPause()
    }

    private fun createDetailItems(schedule: Schedule): List<DetailItem> {
        val address = DetailItem(
                content = schedule.address,
                iconId = R.drawable.ic_map,
                title = getString(R.string.detail_fragment_000),
                onItemClick = {

                }
        )
        val phoneNumber = DetailItem(
                content = schedule.phoneNumber,
                iconId = R.drawable.ic_phone,
                title = getString(R.string.detail_fragment_001),
                onItemClick = {

                }
        )
        val oneLineDescription = DetailItem(
                content = schedule.oneLineDescription,
                iconId = null,
                title = getString(R.string.detail_fragment_002),
                onItemClick = {

                }
        )
        val detailedDescription = DetailItem(
                content = schedule.detailedDescription,
                iconId = null,
                title = getString(R.string.detail_fragment_003),
                onItemClick = {

                }
        )

        return listOf(address, phoneNumber, oneLineDescription, detailedDescription)
    }
}