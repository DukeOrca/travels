package com.duke.orca.android.kotlin.travels.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.base.BaseFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentEntryBinding
import com.duke.orca.android.kotlin.travels.util.BLANK

class EntryFragment: BaseFragment() {
    private var viewBinding: FragmentEntryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentEntryBinding.inflate(inflater, container, false)

        initializeView()

        return viewBinding?.root
    }

    override fun initializeToolbar() {
        (requireActivity() as EntryActivity).supportActionBar?.apply {
            title = BLANK
            setDisplayHomeAsUpEnabled(false)
            setHasOptionsMenu(false)
        }
    }

    private fun initializeView() {
        viewBinding?.materialButtonLogin?.setOnClickListener {
            findNavController().navigate(EntryFragmentDirections.actionEntryFragmentToLoginFragment())
        }

        viewBinding?.materialButtonSignUp?.setOnClickListener {
            findNavController().navigate(EntryFragmentDirections.actionEntryFragmentToSignUpFragment())
        }
    }
}