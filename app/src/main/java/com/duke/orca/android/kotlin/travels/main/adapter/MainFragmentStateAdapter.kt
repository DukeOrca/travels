package com.duke.orca.android.kotlin.travels.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duke.orca.android.kotlin.travels.home.HomeFragment
import com.duke.orca.android.kotlin.travels.map.MapFragment
import com.duke.orca.android.kotlin.travels.message.MessageFragment
import com.duke.orca.android.kotlin.travels.setting.SettingFragment
import com.duke.orca.android.kotlin.travels.todo.TodoFragment
import java.security.InvalidParameterException

class MainFragmentStateAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val itemCount = 5

    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> TodoFragment()
            2 -> MapFragment()
            3 -> MessageFragment()
            4 -> SettingFragment()
            else -> throw InvalidParameterException("Invalid position.")
        }
    }
}