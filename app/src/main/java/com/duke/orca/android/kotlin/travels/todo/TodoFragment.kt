package com.duke.orca.android.kotlin.travels.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentTodoBinding

class TodoFragment: MainTabItemFragment<FragmentTodoBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentTodoBinding {
        return FragmentTodoBinding.inflate(inflater, container, false)
    }
}