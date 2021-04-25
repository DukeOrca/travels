package com.duke.orca.android.kotlin.travels.main.view

import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.databinding.FragmentMainBinding
import com.duke.orca.android.kotlin.travels.main.adapter.MainFragmentStateAdapter
import com.duke.orca.android.kotlin.travels.main.viewmodel.MainViewModel
import com.duke.orca.android.kotlin.travels.util.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment: Fragment() {
    private val duration = 200
    private val simpleExoPlayerManager = SimpleExoPlayerManager()

    private val viewModel by activityViewModels<MainViewModel>()
    private var _viewBinding: FragmentMainBinding? = null
    private val viewBinding: FragmentMainBinding
        get() = _viewBinding!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentMainBinding.inflate(inflater, container, false)

        initializeToolbar()
        initializeView()
        initializeLiveData()

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.tabLayout.getTabAt(0)?.let {
            it.select()
            changeToolBarByTabPosition(0)
            animateSelectedTab(it)
        }
    }

    private fun initializeLiveData() {
        viewModel.viewPager2UserInputEnabled.observe(viewLifecycleOwner, {
            viewBinding.viewPager2.isUserInputEnabled = it
        })

        viewModel.schedule.observe(viewLifecycleOwner, { schedule ->
            if (schedule == viewModel.currentSchedule) {
                viewModel.callNavigateDetailFragmentEvent()
            } else {
                viewModel.setCurrentSchedule(schedule)
                simpleExoPlayerManager.stop()
                simpleExoPlayerManager.play(schedule.audioUrl)

                viewBinding.relativeLayoutPlayerControlViewContainer.show()
                viewBinding.playerControlView.findViewById<TextView>(R.id.text_view_tourspotname)?.let {
                    it.text = schedule.tourspotname
                }
            }
        })

        viewModel.toggleAudioPlaybackStateEvent.observe(viewLifecycleOwner, {
            if (simpleExoPlayerManager.isLoading() || simpleExoPlayerManager.isPlaying()) {
                simpleExoPlayerManager.pause()
            } else {
                viewBinding.relativeLayoutPlayerControlViewContainer.show()

                viewModel.currentSchedule?.let {
                    simpleExoPlayerManager.resume()
                } ?: run {
                    viewModel.setCurrentSchedule(it)
                    simpleExoPlayerManager.play(it.audioUrl)
                }
            }
        })

        viewModel.mapSchedule.observe(viewLifecycleOwner, {
            viewBinding.viewPager2.setCurrentItem(2, true)
        })
    }

    private fun initializeView() {
        val tabTexts = resources.getStringArray(R.array.main_tab_item)
        val tabIcons = arrayOf(
                getDrawable(R.drawable.ic_baseline_home_24),
                getDrawable(R.drawable.ic_baseline_notes_24),
                getDrawable(R.drawable.ic_baseline_location_on_24),
                getDrawable(R.drawable.ic_baseline_message_24),
                getDrawable(R.drawable.ic_baseline_settings_24)
        )

        viewBinding.viewPager2.adapter = MainFragmentStateAdapter(this)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
            tab.customView = layoutInflater.inflate(R.layout.tab_custom_view, tab.parent, false)
            tab.customView?.findViewById<LinearLayout>(R.id.linear_layout)
            tab.customView?.findViewById<TextView>(R.id.text_view)?.text = tabTexts[position]
            tab.customView?.findViewById<ImageView>(R.id.image_view)?.setImageDrawable(tabIcons[position])
        }.attach()

        val viewGroup = viewBinding.tabLayout.getChildAt(0) as ViewGroup

        if (viewGroup.getChildAt(0) is ViewGroup) {
            (viewGroup.getChildAt(0) as ViewGroup).clipChildren = false
            (viewGroup.getChildAt(0) as ViewGroup).clipToPadding = false
        }

        viewBinding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                animateSelectedTab(tab)
                changeToolBarByTabPosition(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                animateUnselectedTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewBinding.playerControlView.findViewById<ImageView>(R.id.image_view_close).setOnClickListener {
            simpleExoPlayerManager.stop()
            viewBinding.relativeLayoutPlayerControlViewContainer.hide()
            viewModel.clearCurrentSchedule()
        }
    }

    private fun initializeToolbar() {
        val activity = requireActivity()

        if (activity is MainActivity)
            activity.setSupportActionBar(viewBinding.toolbar)
    }

    private fun animateSelectedTab(tab: TabLayout.Tab) {
        tab.customView?.findViewById<TextView>(R.id.text_view)?.fadeOut(duration / 2)
        tab.view.scale(1.5F, duration)
        tab.customView?.findViewById<ImageView>(R.id.image_view)?.setColorFilter(
                getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN
        )

        val transitionDrawable = tab.view.findViewById<FrameLayout>(R.id.frame_layout).background as TransitionDrawable

        transitionDrawable.startTransition(duration)
    }

    private fun animateUnselectedTab(tab: TabLayout.Tab) {
        tab.customView?.findViewById<TextView>(R.id.text_view)?.fadeIn(duration / 2)
        tab.view.scale(1.0F, duration)
        tab.customView?.findViewById<ImageView>(R.id.image_view)?.setColorFilter(
                getColor(R.color.lime_green),
                android.graphics.PorterDuff.Mode.SRC_IN
        )

        val transitionDrawable = tab.view.findViewById<FrameLayout>(R.id.frame_layout).background as TransitionDrawable

        transitionDrawable.reverseTransition(duration)
    }

    private fun changeToolBarByTabPosition(position: Int) {
        when(position) {
            0 -> {
                viewBinding.textViewTitle.text = getString(R.string.main_fragment_000)
                viewBinding.imageViewSort.show()
            }
            1 -> {
                viewBinding.textViewTitle.text = getString(R.string.main_fragment_001)
                viewBinding.imageViewSort.hide()
            }
            2 -> {
                viewBinding.toolbar.hide()
                viewBinding.imageViewSort.hide()
            }
            3 -> {
                viewBinding.imageViewSort.hide()
            }
            4 -> {
                viewBinding.imageViewSort.hide()
            }
        }
    }

    inner class SimpleExoPlayerManager {
        fun play(audioUrl: String) {
            val simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

            viewBinding.playerControlView.player = simpleExoPlayer

            val mediaItem = MediaItem.fromUri(audioUrl)

            simpleExoPlayer.setMediaItem(mediaItem)
            simpleExoPlayer.prepare()
            simpleExoPlayer.play()
        }

        fun pause() {
            viewBinding.playerControlView.player?.playWhenReady = false
        }

        fun resume() {
            viewBinding.playerControlView.player?.playWhenReady = true
        }

        fun stop() {
            viewBinding.playerControlView.player?.let {
                it.stop()
                it.clearMediaItems()
            }
        }

        fun isPlaying() = viewBinding.playerControlView.player?.isPlaying ?: false
        fun isLoading() = viewBinding.playerControlView.player?.isLoading ?: false
    }

    @ColorInt
    private fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(requireContext(), id)
    }

    private fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(requireContext(), id)
    }
}