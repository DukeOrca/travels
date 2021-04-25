package com.duke.orca.android.kotlin.travels.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.duke.orca.android.kotlin.travels.main.viewmodel.MainViewModel
import kotlinx.coroutines.launch

abstract class MainTabItemFragment<VB : ViewBinding>: Fragment() {
    private var _viewBinding: VB? = null
    protected val viewBinding: VB
        get() = _viewBinding!!

    protected val viewModel by activityViewModels<MainViewModel>()

    abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): VB

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = inflate(inflater, container)

        return viewBinding.root
    }

    @CallSuper
    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    protected fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        lifecycleScope.launch {
            Toast.makeText(requireContext(), text, duration).show()
        }
    }
}