package com.duke.orca.android.kotlin.travels.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*

abstract class BaseFragment: Fragment() {
    @Suppress("MemberVisibilityCanBePrivate")
    protected val job = Job()
    protected val fragmentScope = CoroutineScope(Dispatchers.Main + job)

    override fun onStart() {
        super.onStart()
        initializeToolbar()
    }

    abstract fun initializeToolbar()
    abstract fun initializeView()

    protected fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        fragmentScope.launch {
            Toast.makeText(requireContext(), text, duration).show()
        }
    }
}