package com.duke.orca.android.kotlin.travels.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*

abstract class BaseFragment: Fragment() {
    @CallSuper
    override fun onStart() {
        super.onStart()
        initializeToolbar()
    }

    abstract fun initializeToolbar()

    protected fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        lifecycleScope.launch {
            Toast.makeText(requireContext(), text, duration).show()
        }
    }
}