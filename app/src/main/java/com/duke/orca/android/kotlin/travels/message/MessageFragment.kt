package com.duke.orca.android.kotlin.travels.message

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentMessageBinding

class MessageFragment: MainTabItemFragment<FragmentMessageBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentMessageBinding {
        return FragmentMessageBinding.inflate(inflater, container, false)
    }
}