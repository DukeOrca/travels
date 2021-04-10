package com.duke.orca.android.kotlin.travels.main

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
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.databinding.FragmentMainBinding
import com.duke.orca.android.kotlin.travels.main.adapter.MainFragmentStateAdapter
import com.duke.orca.android.kotlin.travels.util.fadeIn
import com.duke.orca.android.kotlin.travels.util.fadeOut
import com.duke.orca.android.kotlin.travels.util.scale
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment: Fragment() {
    private val duration = 200
    private var viewBinding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false)

        initializeView()

        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.tabLayout?.getTabAt(0)?.let {
            it.select()
            animateSelectedTab(it)
        }
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

        viewBinding?.let { viewBinding ->
            viewBinding.viewPager2.adapter = MainFragmentStateAdapter(this)
            TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
                tab.customView = layoutInflater.inflate(R.layout.tab_custom_view, tab.parent, false)
                tab.customView?.findViewById<LinearLayout>(R.id.linear_layout)
                tab.customView?.findViewById<TextView>(R.id.text_view)?.text = tabTexts[position]
                tab.customView?.findViewById<ImageView>(R.id.image_view)?.setImageDrawable(tabIcons[position])
            }.attach()

            val viewGroup = viewBinding.tabLayout.getChildAt(0) as ViewGroup

            if (viewGroup.getChildAt(0) is ViewGroup) {
                (viewGroup.getChildAt(0) as ViewGroup).clipToPadding = false
                (viewGroup.getChildAt(0) as ViewGroup).clipChildren = false
            }

            viewBinding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    animateSelectedTab(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                   animateUnselectedTab(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
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

    @ColorInt
    private fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(requireContext(), id)
    }

    private fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(requireContext(), id)
    }
}