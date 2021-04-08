package com.duke.orca.android.kotlin.travels.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentSettingBinding

class SettingFragment: MainTabItemFragment<FragmentSettingBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }
}